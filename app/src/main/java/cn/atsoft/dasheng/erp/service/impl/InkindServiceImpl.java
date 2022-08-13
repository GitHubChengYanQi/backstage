package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.config.MobileService;
import cn.atsoft.dasheng.erp.entity.Inkind;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.entity.StorehousePositions;
import cn.atsoft.dasheng.erp.mapper.InkindMapper;
import cn.atsoft.dasheng.erp.model.params.InkindParam;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.service.StepsService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.orCode.service.impl.QrCodeCreateService;
import cn.atsoft.dasheng.printTemplate.entity.PrintTemplate;
import cn.atsoft.dasheng.printTemplate.model.result.PrintTemplateResult;
import cn.atsoft.dasheng.printTemplate.service.PrintTemplateService;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.log.Log;
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
    @Autowired
    private MaintenanceLogDetailService maintenanceLogDetailService;
    @Autowired
    private StorehousePositionsService positionsService;
    @Autowired
    private UserService userService;
    @Autowired
    private StepsService stepsService;


    @Override
    public Long add(InkindParam param) {
        Inkind entity = getEntity(param);
        this.save(entity);
        return entity.getInkindId();
    }

    @Override

    public void delete(InkindParam param) {
        param.setDisplay(0);
        this.updateById(this.getEntity(param));
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
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    /**
     * 修改异常实物
     */
    @Override
    public void updateAnomalyInKind(List<Long> inKindIds) {
        if (ToolUtil.isEmpty(inKindIds) || inKindIds.size() == 0) {
            return;
        }
        Inkind inkind = new Inkind();
        inkind.setAnomaly(1);

        QueryWrapper<Inkind> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("inkind_id", inKindIds);
        this.update(inkind, queryWrapper);
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

    /**
     * 异常实物获取skuId
     *
     * @param inkindIds
     * @return
     */
    @Override
    public List<Long> anomalySku(List<Long> inkindIds) {
        if (ToolUtil.isEmpty(inkindIds)) {
            return new ArrayList<>();
        }
        List<Inkind> inkinds = this.query().in("inkind_id", inkindIds).eq("anomaly", 1).list();
        if (ToolUtil.isEmpty(inkinds)) {
            return new ArrayList<>();
        }
        List<Long> skuIds = new ArrayList<>();
        for (Inkind inkind : inkinds) {
            skuIds.add(inkind.getSkuId());
        }
        return skuIds;
    }

//    @Override
//    public List<InkindResult> details (InkindParam param){
//        param.getInkindIds();
//        param.getSkuIds();
//        List<Inkind> inkinds = this.lambdaQuery().in(Inkind::getInkindId, param.getInkindIds()).list();
//        List<Sku> skus = this.skuService.lambdaQuery().in(Sku::getSkuId, param.getSkuIds()).list();
//        List<InkindParam> inkindParams = new ArrayList<>();
//        for (Inkind inkind : inkinds) {
//            InkindParam inkindParam = new InkindParam();
//            for (Sku sku : skus) {
//                if (inkind.getSkuId().equals(sku.getSkuId())){
//                    ToolUtil.copyProperties(inkind,inkindParam);
//                    inkindParams.add(inkindParam);
//                }
//            }
//        }
//        List<InkindResult> inkindResults = new ArrayList<>();
//        for (InkindParam inkindParam : inkindParams) {
//            InkindResult inkindResult = this.inkindDetail(inkindParam);
//            inkindResults.add(inkindResult);
//        }
//
//        return inkindResults;
//
//    }

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
        //打印查询打印模板
        PrintTemplate printTemplate = printTemplateService.getOne(new QueryWrapper<PrintTemplate>() {{
            eq("type", PHYSICALDETAIL);
            eq("display", 1);
        }});
        if (ToolUtil.isNotEmpty(printTemplate)) {
            this.returnPrintTemplate(inkindResult, printTemplate);
        }
        inkindResult.setSkuResult(null);
        inkindResult.setBrandResult(null);
        return inkindResult;
    }

    public List<InkindResult> details(InkindParam param) {
        PrintTemplate printTemplate = printTemplateService.getOne(new QueryWrapper<PrintTemplate>() {{
            eq("type", PHYSICALDETAIL);
            eq("display", 1);
        }});

        /**
         * 查询出实物（inkind) 然后查出对应物料（sku）, 品牌
         */
        List<InkindResult> inkindResults = new ArrayList<>();
        List<Inkind> inkinds = this.lambdaQuery().in(Inkind::getInkindId, param.getInkindIds()).list();

        List<Long> brandIds = new ArrayList<>();
        List<Long> skuIds = new ArrayList<>();
        for (Inkind inKind : inkinds) {
            skuIds.add(inKind.getSkuId());
            brandIds.add(inKind.getBrandId());
        }

        //查询sku
        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);

        //查询品牌
        List<BrandResult> brandResults = brandService.getBrandResults(brandIds);

        //循环匹配sku
        for (Inkind inkind : inkinds) {
            InkindResult inkindResult = new InkindResult();
            ToolUtil.copyProperties(inkind, inkindResult);
            for (SkuResult skuResult : skuResults) {
                if (inkindResult.getSkuId().equals(skuResult.getSkuId())) {
                    inkindResult.setSkuResult(skuResult);
                }
            }
            for (BrandResult brandResult : brandResults) {
                if (brandResult.getBrandId().equals(inkind.getBrandId())) {
                    inkindResult.setBrandResult(brandResult);
                }
            }
            inkindResults.add(inkindResult);
        }

        //查询不到模板 不执行返回打印模板操作
        if (ToolUtil.isNotEmpty(printTemplate)) {
            for (InkindResult inkindResult : inkindResults) {
                this.returnPrintTemplate(inkindResult, printTemplate);
                inkindResult.setSkuResult(null);
                inkindResult.setBrandResult(null);
            }
        }

        return inkindResults;
    }

    private void returnPrintTemplate(InkindResult param, PrintTemplate printTemplate) {

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
//            Sku sku = ToolUtil.isEmpty(param.getSkuId()) ? new Sku() : skuService.getById(param.getSkuId());
            SkuResult skuResult = param.getSkuResult();
            if (ToolUtil.isNotEmpty(skuResult) && ToolUtil.isNotEmpty(skuResult.getCoding())) {
                templete = templete.replace("${skuCoding}", skuResult.getCoding());
            } else if (ToolUtil.isNotEmpty(skuResult) && ToolUtil.isEmpty(skuResult.getCoding()) && ToolUtil.isEmpty(skuResult.getStandard())) {
                templete = templete.replace("${skuCoding}", "无");
            } else if (ToolUtil.isNotEmpty(skuResult) && ToolUtil.isNotEmpty(skuResult.getStandard())) {
                templete = templete.replace("${skuCoding}", skuResult.getStandard());
            }
        }
        if (templete.contains("${name}")) {
            String name = param.getSkuResult().getSpuResult().getSpuClassificationResult().getName();
            templete = templete.replace("${name}", name + "/" + param.getSkuResult().getSpuResult().getName());
        }
        if (templete.contains("${number}")) {
            templete = templete.replace("${number}", param.getNumber().toString());
        }
        if (templete.contains("${brand}")) {
            if (ToolUtil.isEmpty(param.getBrandResult()) || ToolUtil.isEmpty(param.getBrandResult().getBrandName())) {
                templete = templete.replace("${brand}", "");
            } else {
                templete = templete.replace("${brand}", param.getBrandResult().getBrandName());
            }
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

    private void format(List<InkindResult> data) {

        List<Long> skuIds = new ArrayList<>();
        List<Long> positionIds = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        List<Long> inkindIds = new ArrayList<>();
        for (InkindResult datum : data) {
            skuIds.add(datum.getSkuId());
            positionIds.add(datum.getPositionId());
            userIds.add(datum.getCreateUser());
        }

        List<SkuSimpleResult> simpleResults = skuService.simpleFormatSkuResult(skuIds);
        List<StorehousePositionsResult> positionsResultList = positionsService.details(positionIds);
        List<User> userList = userIds.size() == 0 ? new ArrayList<>() : userService.listByIds(userIds);
        List<MaintenanceLogDetailResult> maintenanceLogDetailResults = maintenanceLogDetailService.lastLogByInkindIds(inkindIds);

        for (InkindResult datum : data) {

            for (SkuSimpleResult simpleResult : simpleResults) {
                if (datum.getSkuId().equals(simpleResult.getSkuId())) {
                    datum.setSkuSimpleResult(simpleResult);
                    break;
                }
            }

            for (StorehousePositionsResult storehousePositionsResult : positionsResultList) {
                if (ToolUtil.isNotEmpty(datum.getPositionId()) && datum.getPositionId().equals(storehousePositionsResult.getStorehousePositionsId())) {
                    datum.setPositionsResult(storehousePositionsResult);
                    break;
                }
            }

            for (MaintenanceLogDetailResult maintenanceLogDetailResult : maintenanceLogDetailResults) {
                if (ToolUtil.isNotEmpty(datum.getInkindId()) && datum.getInkindId().equals(maintenanceLogDetailResult.getInkindId())) {
                    datum.setMaintenanceLogResult(maintenanceLogDetailResult);
                    break;
                }
            }

            for (User user : userList) {
                if (datum.getCreateUser().equals(user.getUserId())) {
                    String imgUrl = stepsService.imgUrl(user.getUserId().toString());
                    user.setAvatar(imgUrl);
                    datum.setUser(user);
                    break;
                }
            }
        }

    }

    @Override
    public void resultFormat(List<InkindResult> data) {

        List<Long> inkindIds = new ArrayList<>();
        for (InkindResult datum : data) {
            inkindIds.add(datum.getInkindId());
        }

        List<OrCodeBind> orCodeBinds = inkindIds.size() == 0 ? new ArrayList<>() : orCodeBindService.query().in("form_id", inkindIds).eq("display", 1).list();

        for (InkindResult datum : data) {
            for (OrCodeBind orCodeBind : orCodeBinds) {
                if (datum.getInkindId().equals(orCodeBind.getFormId())) {
                    datum.setQrcode(orCodeBind.getOrCodeId());
                    break;
                }
            }
        }

    }


}
