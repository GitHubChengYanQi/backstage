package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.config.MobileService;
import cn.atsoft.dasheng.erp.entity.Inkind;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.mapper.InkindMapper;
import cn.atsoft.dasheng.erp.model.params.InkindParam;
import cn.atsoft.dasheng.erp.model.result.BackSku;
import cn.atsoft.dasheng.erp.model.result.InkindResult;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.InkindService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.orCode.service.impl.QrCodeCreateService;
import cn.atsoft.dasheng.printTemplate.entity.PrintTemplate;
import cn.atsoft.dasheng.printTemplate.model.result.PrintTemplateResult;
import cn.atsoft.dasheng.printTemplate.service.PrintTemplateService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static cn.atsoft.dasheng.form.pojo.PrintTemplateEnum.PHYSICALDETAIL;


/**
 * <p>
 * 实物表 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-11-01
 */
@Service
public class InkindServiceImpl extends ServiceImpl<InkindMapper, Inkind> implements InkindService {
    @Autowired
    private SkuService skuService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private PrintTemplateService printTemplateService;
    @Autowired
    private QrCodeCreateService qrCodeCreateService;
    @Autowired
    private MobileService mobileService;
    @Autowired
    private OrCodeBindService orCodeBindService;


    @Override
    public Long add(InkindParam param) {
        Inkind entity = getEntity(param);
        this.save(entity);
        return entity.getInkindId();
    }

    @Override

    public void delete(InkindParam param) {
        this.removeById(getKey(param));
    }

    @Override

    public void update(InkindParam param) {
        Inkind oldEntity = getOldEntity(param);
        Inkind newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public InkindResult findBySpec(InkindParam param) {
        return null;
    }

    @Override
    public List<InkindResult> findListBySpec(InkindParam param) {
        return null;
    }

    @Override
    public PageInfo<InkindResult> findPageBySpec(InkindParam param) {
        Page<InkindResult> pageContext = getPageContext();
        IPage<InkindResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    @Override
    public InkindResult backInKindgetById(Long id) {
        Inkind inkind = this.getById(id);
        if (ToolUtil.isEmpty(inkind)) {
            return null;
        }
        InkindResult inkindResult = new InkindResult();
        ToolUtil.copyProperties(inkind, inkindResult);
        List<BackSku> backSku = skuService.backSku(inkindResult.getSkuId());
        Brand brand = brandService.query().eq("brand_id", inkindResult.getBrandId()).one();
        inkindResult.setBrand(brand);
        inkindResult.setBackSku(backSku);
        return inkindResult;
    }

    @Override
    public List<InkindResult> getInKinds(List<Long> inKindIds) {
        if (ToolUtil.isEmpty(inKindIds)) {
            return new ArrayList<>();
        }
        List<Inkind> inKinds = this.listByIds(inKindIds);

        List<InkindResult> inKindResults = new ArrayList<>();
        List<Long> skuIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        for (Inkind inKind : inKinds) {
            skuIds.add(inKind.getSkuId());
            brandIds.add(inKind.getBrandId());

            InkindResult inkindResult = new InkindResult();
            ToolUtil.copyProperties(inKind, inkindResult);
            inKindResults.add(inkindResult);
        }

        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);
        List<BrandResult> brandResults = brandService.getBrandResults(brandIds);

        for (InkindResult inKindResult : inKindResults) {
            for (SkuResult skuResult : skuResults) {
                if (skuResult.getSkuId().equals(inKindResult.getSkuId())) {
                    inKindResult.setSkuResult(skuResult);
                    break;
                }
            }
            for (BrandResult brandResult : brandResults) {
                if (brandResult.getBrandId().equals(inKindResult.getBrandId())) {
                    inKindResult.setBrandResult(brandResult);
                    break;
                }
            }

        }

        return inKindResults;
    }

    @Override
    public InkindResult inkindDetail(InkindParam param) {
        Inkind inkind = this.getById(param.getInkindId());
        if (ToolUtil.isEmpty(inkind)) {
            throw new ServiceException(500, "没有此物");
        }
        List<Long> skuIds = new ArrayList<>();
        skuIds.add(inkind.getSkuId());
        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);
        SkuResult skuResult = skuResults.get(0);
        InkindResult inkindResult = new InkindResult();
        ToolUtil.copyProperties(inkind, inkindResult);
        inkindResult.setSkuResult(skuResult);
        inkindResult.setBrandResult(brandService.getBrandResult(inkind.getBrandId()));
        this.returnPrintTemplate(inkindResult);
        inkindResult.setSkuResult(null);
        inkindResult.setBrandResult(null);
        return inkindResult;
    }
    public List<InkindResult> inkindDetails(InkindParam param){
        
    }

    private void returnPrintTemplate(InkindResult param) {
        PrintTemplate printTemplate = printTemplateService.getOne(new QueryWrapper<PrintTemplate>() {{
            eq("type", PHYSICALDETAIL);
        }});
        param.getInkindId();
        if (ToolUtil.isEmpty(printTemplate)) {
            throw new ServiceException(500, "请先定义二维码模板");
        }
        System.out.println(PHYSICALDETAIL);
        String templete = printTemplate.getTemplete();

        if (templete.contains("${coding}")) {
            String inkindId = param.getInkindId().toString();
            String substring = inkindId.substring(inkindId.length() - 6);
            templete = templete.replace("${coding}", substring);
        }
        if (templete.contains("${skuCoding}")) {
            Sku sku = ToolUtil.isEmpty(param.getSkuId()) ? new Sku() : skuService.getById(param.getSkuId());
            if (ToolUtil.isNotEmpty(sku) && ToolUtil.isNotEmpty(sku.getCoding())) {
                templete = templete.replace("${skuCoding}", sku.getCoding());
            } else if (ToolUtil.isNotEmpty(sku) && ToolUtil.isEmpty(sku.getCoding()) && ToolUtil.isEmpty(sku.getStandard())) {
                templete = templete.replace("${skuCoding}", "无");
            } else if (ToolUtil.isNotEmpty(sku) && ToolUtil.isNotEmpty(sku.getStandard())) {
                templete = templete.replace("${skuCoding}", sku.getStandard());
            }
        }
        if (templete.contains("${name}")) {
            templete = templete.replace("${name}", param.getSkuResult().getSkuName() + "/" + param.getSkuResult().getSpuResult().getName());
        }
        if (templete.contains("${number}")) {
            templete = templete.replace("${number}", param.getNumber().toString());
        }
        if (templete.contains("${brand}")) {
            templete = templete.replace("${brand}", param.getBrandResult().getBrandName());
        }
        if (templete.contains("${qrCode}")) {
            OrCodeBind orCodeBind = orCodeBindService.getOne(new QueryWrapper<OrCodeBind>() {{
                eq("form_id", param.getInkindId());
                eq("source", "item");
            }});
            Long url = orCodeBind.getOrCodeId();
            String qrCode = qrCodeCreateService.createQrCode(url.toString());
            templete = templete.replace("${qrCode}", qrCode);
        }
        PrintTemplateResult printTemplateResult = new PrintTemplateResult();
        ToolUtil.copyProperties(printTemplate, printTemplateResult);
        printTemplateResult.setTemplete(templete);
        param.setPrintTemplateResult(printTemplateResult);
    }


    @Override
    public InkindResult getInkindResult(Long id) {
        Inkind inkind = this.getById(id);
        if (ToolUtil.isEmpty(inkind)) {
            throw new ServiceException(500, "当前数据不存在");
        }
        InkindResult inkindResult = new InkindResult();
        ToolUtil.copyProperties(inkind, inkindResult);

        SkuResult skuResult = skuService.getSku(inkind.getSkuId());
        Brand brand = brandService.getById(inkind.getBrandId());
        inkindResult.setSkuResult(skuResult);
        inkindResult.setBrand(brand);

        return inkindResult;
    }

    private Serializable getKey(InkindParam param) {
        return param.getInkindId();
    }

    private Page<InkindResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Inkind getOldEntity(InkindParam param) {
        return this.getById(getKey(param));
    }

    private Inkind getEntity(InkindParam param) {
        Inkind entity = new Inkind();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }


}
