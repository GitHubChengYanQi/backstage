package cn.atsoft.dasheng.printTemplate.service.impl;


import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.erp.entity.Inkind;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.InkindService;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.orCode.service.impl.QrCodeCreateService;
import cn.atsoft.dasheng.printTemplate.entity.PrintTemplate;
import cn.atsoft.dasheng.printTemplate.mapper.PrintTemplateMapper;
import cn.atsoft.dasheng.printTemplate.model.params.PrintTemplateParam;
import cn.atsoft.dasheng.printTemplate.model.result.PrintTemplateResult;
import cn.atsoft.dasheng.printTemplate.service.PrintTemplateService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.atsoft.dasheng.form.pojo.PrintTemplateEnum.PHYSICALDETAIL;
import static cn.atsoft.dasheng.form.pojo.PrintTemplateEnum.SKU;

/**
 * <p>
 * 编辑模板 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-28
 */
@Service
public class PrintTemplateServiceImpl extends ServiceImpl<PrintTemplateMapper, PrintTemplate> implements PrintTemplateService {

    @Autowired
    private SkuService skuService;

    @Autowired
    private InkindService inkindService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private QrCodeCreateService qrCodeCreateService;
    @Autowired
    private OrCodeBindService orCodeBindService;

    @Override
    public void add(PrintTemplateParam param) {
        PrintTemplate type = this.getOne(new QueryWrapper<PrintTemplate>() {{
            eq("type", param.getType());
            eq("display", 1);
        }});
        if (ToolUtil.isNotEmpty(type)) {
            throw new ServiceException(500, "已有此类模板不可重复添加，如有需求请修改之前模板");
        }
        PrintTemplate entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(PrintTemplateParam param) {
        PrintTemplate printTemplate = this.getEntity(param);
        printTemplate.setDisplay(0);
        this.updateById(printTemplate);
    }

    @Override
    public void update(PrintTemplateParam param) {

        PrintTemplate oldEntity = getOldEntity(param);
        PrintTemplate newEntity = getEntity(param);
        if (!oldEntity.getType().toString().equals(newEntity.getType().toString())) {
            throw new ServiceException(500, "不可以修改此模板");
        }
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public PrintTemplateResult findBySpec(PrintTemplateParam param) {
        return null;
    }

    @Override
    public List<PrintTemplateResult> findListBySpec(PrintTemplateParam param) {
        return null;
    }

    @Override
    public PageInfo<PrintTemplateResult> findPageBySpec(DataScope dataScope, PrintTemplateParam param) {
        Page<PrintTemplateResult> pageContext = getPageContext();
        IPage<PrintTemplateResult> page = this.baseMapper.customPageList(dataScope, pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(PrintTemplateParam param) {
        return param.getPrintTemplateId();
    }

    private Page<PrintTemplateResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private PrintTemplate getOldEntity(PrintTemplateParam param) {
        return this.getById(getKey(param));
    }

    private PrintTemplate getEntity(PrintTemplateParam param) {
        PrintTemplate entity = new PrintTemplate();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    /**
     * 模板替换
     *
     * @param inkindId
     * @return
     */
    @Override
    public String replaceInkindTemplate( Long inkindId) {
        PrintTemplate printTemplate = this.getOne(new QueryWrapper<PrintTemplate>() {{
            eq("type", PHYSICALDETAIL);
            eq("display", 1);
        }});

        if (ToolUtil.isEmpty(printTemplate)) {
            throw new ServiceException(500, "请先定义二维码模板");
        }
        String templete = printTemplate.getTemplete();
        if (ToolUtil.isEmpty(inkindId)) {
            if (templete.contains("${{序号}}")) {
                templete = templete.replace("${{序号}}", "");
            }
            if (templete.contains("${{物料编码}}")) {
                templete = templete.replace("${{物料编码}}", "");
            }
            if (templete.contains("${{产品名称}}")) {
                templete = templete.replace("${{产品名称}}", "");
            }
            if (templete.contains("${{型号}}")) {
                templete = templete.replace("${{型号}}", "");
            }
            if (templete.contains("${{规格}}")) {
                templete = templete.replace("${{规格}}", "");
            }
            if (templete.contains("${{品牌}}")) {
                templete = templete.replace("${{品牌}}", "");
            }
            if (templete.contains("${{供应商}}")) {
                templete = templete.replace("${{供应商}}", "");
            }

            return templete;
        }

        String regStr = "\\<tr(.*?)\\>([\\w\\W]+?)<\\/tr>";
        Pattern pattern = Pattern.compile(regStr);
        Matcher m = pattern.matcher(templete);

        while (m.find()) {
            String inkindGrop = m.group(0);
            if (inkindGrop.contains("sku")) {
                Inkind inkind = inkindService.getById(inkindId);
                SkuResult skuResult = BeanUtil.copyProperties(skuService.getById(inkind.getSkuId()), SkuResult.class);
                Brand brand = inkind.getBrandId().equals(0L) ? null : brandService.getById(inkind.getBrandId());
                Customer customer = ToolUtil.isNotEmpty(inkind.getCustomerId()) ? null : customerService.getById(inkind.getCustomerId());
                StringBuilder replaceStr = new StringBuilder(m.group(0));
                if (replaceStr.toString().contains("${{物料编码}}") && ToolUtil.isNotEmpty(skuResult)) {
                    replaceStr = new StringBuilder(replaceStr.toString().replace("${{物料编码}}", skuResult.getStandard()));
                }
                if (replaceStr.toString().contains("${{产品名称}}") && ToolUtil.isNotEmpty(skuResult)) {
                    replaceStr = new StringBuilder(replaceStr.toString().replace("${{产品名称}}", skuResult.getSpuResult().getName()));
                }
                if (replaceStr.toString().contains("${{型号}}") && ToolUtil.isNotEmpty(skuResult)) {
                    replaceStr = new StringBuilder(replaceStr.toString().replace("${{型号}}", skuResult.getSkuName()));
                }
                if (replaceStr.toString().contains("${{规格}}")) {
                    if (ToolUtil.isNotEmpty(skuResult) && ToolUtil.isNotEmpty(skuResult.getSpecifications())) {
                        replaceStr = new StringBuilder(replaceStr.toString().replace("${{规格}}", skuResult.getSpecifications()));
                    } else {
                        replaceStr = new StringBuilder(replaceStr.toString().replace("${{规格}}", ""));
                    }
                }
                if (replaceStr.toString().contains("${{品牌}}")) {
                    if (ToolUtil.isNotEmpty(brand) && ToolUtil.isNotEmpty(brand.getBrandName())) {
                        replaceStr = new StringBuilder(replaceStr.toString().replace("${{品牌}}", brand.getBrandName()));
                    } else {
                        replaceStr = new StringBuilder(replaceStr.toString().replace("${{品牌}}", "无品牌"));
                    }
                }
                if (replaceStr.toString().contains("${{供应商}}")) {
                    if (ToolUtil.isNotEmpty(customer) && ToolUtil.isNotEmpty(customer.getCustomerName())) {
                        replaceStr = new StringBuilder(replaceStr.toString().replace("${{供应商}}", customer.getCustomerName()));
                    } else {
                        replaceStr = new StringBuilder(replaceStr.toString().replace("${{供应商}}", "无供应商"));
                    }
                }
                if (replaceStr.toString().contains("${{qrCode}}")) {
                    OrCodeBind orCodeBind = orCodeBindService.getOne(new QueryWrapper<OrCodeBind>() {{
                        eq("form_id", inkind.getInkindId());
                        eq("source", "item");
                    }});
                    Long url = orCodeBind.getOrCodeId();
                    String qrCode = qrCodeCreateService.createQrCode(url.toString());
                    templete = templete.replace("${{qrCode}}", qrCode);
                }

                String toString = replaceStr.toString();
                String group = m.group(0);
                templete = templete.replace(group, toString);
            }
        }
        return templete;
    }
    @Override
    public String replaceSkuTemplate(Long skuId) {
        PrintTemplate printTemplate = this.getOne(new QueryWrapper<PrintTemplate>() {{
            eq("type", SKU);
            eq("display", 1);
        }});

        if (ToolUtil.isEmpty(printTemplate)) {
            throw new ServiceException(500, "请先定义二维码模板");
        }
        String templete = printTemplate.getTemplete();
        if (ToolUtil.isEmpty(skuId)) {
            if (templete.contains("${{序号}}")) {
                templete = templete.replace("${{序号}}", "");
            }
            if (templete.contains("${{物料编码}}")) {
                templete = templete.replace("${{物料编码}}", "");
            }
            if (templete.contains("${{产品名称}}")) {
                templete = templete.replace("${{产品名称}}", "");
            }
            if (templete.contains("${{型号}}")) {
                templete = templete.replace("${{型号}}", "");
            }
            if (templete.contains("${{规格}}")) {
                templete = templete.replace("${{规格}}", "");
            }
            if (templete.contains("${{品牌}}")) {
                templete = templete.replace("${{品牌}}", "");
            }
            if (templete.contains("${{供应商}}")) {
                templete = templete.replace("${{供应商}}", "");
            }

            return templete;
        }

        String regStr = "\\<tr(.*?)\\>([\\w\\W]+?)<\\/tr>";
        Pattern pattern = Pattern.compile(regStr);
        Matcher m = pattern.matcher(templete);

        while (m.find()) {
            String inkindGrop = m.group(0);
            if (inkindGrop.contains("sku")) {
                SkuResult skuResult = BeanUtil.copyProperties(skuService.getById(skuId), SkuResult.class);
                skuService.format(new ArrayList<SkuResult>(){{
                    add(skuResult);
                }});
                StringBuilder replaceStr = new StringBuilder(m.group(0));
                if (replaceStr.toString().contains("${{物料编码}}") && ToolUtil.isNotEmpty(skuResult)) {
                    replaceStr = new StringBuilder(replaceStr.toString().replace("${{物料编码}}", skuResult.getStandard()));
                }
                if (replaceStr.toString().contains("${{产品名称}}") && ToolUtil.isNotEmpty(skuResult)) {
                    replaceStr = new StringBuilder(replaceStr.toString().replace("${{产品名称}}", skuResult.getSpuResult().getName()));
                }
                if (replaceStr.toString().contains("${{型号}}") && ToolUtil.isNotEmpty(skuResult)) {
                    replaceStr = new StringBuilder(replaceStr.toString().replace("${{型号}}", skuResult.getSkuName()));
                }
                if (replaceStr.toString().contains("${{规格}}")) {
                    if (ToolUtil.isNotEmpty(skuResult) && ToolUtil.isNotEmpty(skuResult.getSpecifications())) {
                        replaceStr = new StringBuilder(replaceStr.toString().replace("${{规格}}", skuResult.getSpecifications()));
                    } else {
                        replaceStr = new StringBuilder(replaceStr.toString().replace("${{规格}}", ""));
                    }
                }
                if (replaceStr.toString().contains("${{qrCode}}")) {
                    OrCodeBind orCodeBind = orCodeBindService.getOne(new QueryWrapper<OrCodeBind>() {{
                        eq("form_id", skuResult.getSkuId());
                        eq("source", "sku");
                    }});
                    Long url = orCodeBind.getOrCodeId();
                    String qrCode = qrCodeCreateService.createQrCode(url.toString());
                    templete = templete.replace("${{qrCode}}", qrCode);
                }

                String toString = replaceStr.toString();
                String group = m.group(0);
                templete = templete.replace(group, toString);
            }
        }
        return templete;
    }


}
