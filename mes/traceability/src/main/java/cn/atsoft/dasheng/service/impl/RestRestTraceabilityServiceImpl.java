package cn.atsoft.dasheng.service.impl;


//import cn.atsoft.dasheng.app.entity.Brand;
//import cn.atsoft.dasheng.app.model.result.BrandResult;
//import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
//import cn.atsoft.dasheng.erp.config.MobileService;
//import cn.atsoft.dasheng.erp.entity.Traceability;
//import cn.atsoft.dasheng.erp.mapper.TraceabilityMapper;
//import cn.atsoft.dasheng.erp.model.params.TraceabilityParam;
//import cn.atsoft.dasheng.form.service.StepsService;
import cn.atsoft.dasheng.entity.RestTraceability;
import cn.atsoft.dasheng.goods.sku.service.RestSkuService;
import cn.atsoft.dasheng.mapper.RestTraceabilityMapper;
//import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
//import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
//import cn.atsoft.dasheng.orCode.service.impl.QrCodeCreateService;
//import cn.atsoft.dasheng.printTemplate.entity.PrintTemplate;
//import cn.atsoft.dasheng.printTemplate.model.result.PrintTemplateResult;
//import cn.atsoft.dasheng.printTemplate.service.PrintTemplateService;
//import cn.atsoft.dasheng.sys.modular.system.entity.User;
//import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.atsoft.dasheng.model.params.RestTraceabilityParam;
import cn.atsoft.dasheng.model.result.RestTraceabilityResult;
import cn.atsoft.dasheng.service.RestTraceabilityService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//import static cn.atsoft.dasheng.form.pojo.PrintTemplateEnum.PHYSICALDETAIL;


/**
 * <p>
 * 实物表 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-11-01
 */
@Service
public class RestRestTraceabilityServiceImpl extends ServiceImpl<RestTraceabilityMapper, RestTraceability> implements RestTraceabilityService {
    @Autowired
    private RestSkuService skuService;
//    @Autowired
//    private BrandService brandService;
//    @Autowired
//    private PrintTemplateService printTemplateService;
//    @Autowired
//    private QrCodeCreateService qrCodeCreateService;
//    @Autowired
//    private MobileService mobileService;
//    @Autowired
//    private OrCodeBindService orCodeBindService;
//    @Autowired
//    private MaintenanceLogDetailService maintenanceLogDetailService;
//    @Autowired
//    private StorehousePositionsService positionsService;
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private StepsService stepsService;


    @Override
    public Long add(RestTraceabilityParam param) {
        RestTraceability entity = getEntity(param);
        this.save(entity);
        return entity.getInkindId();
    }

    @Override

    public void delete(RestTraceabilityParam param) {
        param.setDisplay(0);
        this.updateById(this.getEntity(param));
    }

    @Override

    public void update(RestTraceabilityParam param) {
        RestTraceability oldEntity = getOldEntity(param);
        RestTraceability newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public RestTraceabilityResult findBySpec(RestTraceabilityParam param) {
        return null;
    }

    @Override
    public List<RestTraceabilityResult> findListBySpec(RestTraceabilityParam param) {
        return null;
    }

    @Override
    public PageInfo<RestTraceabilityResult> findPageBySpec(RestTraceabilityParam param) {
        Page<RestTraceabilityResult> pageContext = getPageContext();
        IPage<RestTraceabilityResult> page = this.baseMapper.customPageList(pageContext, param);
//        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }


    @Override
    public PageInfo<RestTraceabilityResult> stockTraceability(RestTraceabilityParam param) {
        Page<RestTraceabilityResult> pageContext = getPageContext();
        IPage<RestTraceabilityResult> page = this.baseMapper.stockTraceabilityList(pageContext, param);
//        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    /**
     * 修改异常实物
     */
    @Override
    public void updateAnomalyTraceability(List<Long> InkindIds) {
        if (ToolUtil.isEmpty(InkindIds) || InkindIds.size() == 0) {
            return;
        }
        RestTraceability RestTraceability = new RestTraceability();
        RestTraceability.setAnomaly(1);

        QueryWrapper<RestTraceability> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("Traceability_id", InkindIds);
        this.update(RestTraceability, queryWrapper);
    }

    @Override
    public RestTraceabilityResult backTraceabilitygetById(Long id) {
        RestTraceability RestTraceability = this.getById(id);
        if (ToolUtil.isEmpty(RestTraceability)) {
            return null;
        }
        RestTraceabilityResult RestTraceabilityResult = new RestTraceabilityResult();
        ToolUtil.copyProperties(RestTraceability, RestTraceabilityResult);
//        List<BackSku> backSku = skuService.backSku(TraceabilityResult.getSkuId());
//        Brand brand = brandService.query().eq("brand_id", TraceabilityResult.getBrandId()).one();
//        TraceabilityResult.setBrand(brand);
//        TraceabilityResult.setBackSku(backSku);
        return RestTraceabilityResult;
    }

    /**
     * 异常实物获取skuId
     *
     * @param InkindIds
     * @return
     */
    @Override
    public List<Long> anomalySku(List<Long> InkindIds) {
        if (ToolUtil.isEmpty(InkindIds)) {
            return new ArrayList<>();
        }
        List<RestTraceability> restTraceabilities = this.query().in("Traceability_id", InkindIds).eq("anomaly", 1).list();
        if (ToolUtil.isEmpty(restTraceabilities)) {
            return new ArrayList<>();
        }
        List<Long> skuIds = new ArrayList<>();
        for (RestTraceability RestTraceability : restTraceabilities) {
            skuIds.add(RestTraceability.getSkuId());
        }
        return skuIds;
    }

//    @Override
//    public List<TraceabilityResult> details (TraceabilityParam param){
//        param.getInkindIds();
//        param.getSkuIds();
//        List<Traceability> Traceabilitys = this.lambdaQuery().in(Traceability::getInkindId, param.getInkindIds()).list();
//        List<Sku> skus = this.skuService.lambdaQuery().in(Sku::getSkuId, param.getSkuIds()).list();
//        List<TraceabilityParam> TraceabilityParams = new ArrayList<>();
//        for (Traceability Traceability : Traceabilitys) {
//            TraceabilityParam TraceabilityParam = new TraceabilityParam();
//            for (Sku sku : skus) {
//                if (Traceability.getSkuId().equals(sku.getSkuId())){
//                    ToolUtil.copyProperties(Traceability,TraceabilityParam);
//                    TraceabilityParams.add(TraceabilityParam);
//                }
//            }
//        }
//        List<TraceabilityResult> TraceabilityResults = new ArrayList<>();
//        for (TraceabilityParam TraceabilityParam : TraceabilityParams) {
//            TraceabilityResult TraceabilityResult = this.TraceabilityDetail(TraceabilityParam);
//            TraceabilityResults.add(TraceabilityResult);
//        }
//
//        return TraceabilityResults;
//
//    }

    @Override
    public List<RestTraceabilityResult> getTraceabilitys(List<Long> InkindIds) {
        if (ToolUtil.isEmpty(InkindIds)) {
            return new ArrayList<>();
        }
        List<RestTraceability> restTraceabilities = this.listByIds(InkindIds);

        List<RestTraceabilityResult> restTraceabilityResults = new ArrayList<>();
        List<Long> skuIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        for (RestTraceability RestTraceability : restTraceabilities) {
            skuIds.add(RestTraceability.getSkuId());
            brandIds.add(RestTraceability.getBrandId());

            RestTraceabilityResult RestTraceabilityResult = new RestTraceabilityResult();
            ToolUtil.copyProperties(RestTraceability, RestTraceabilityResult);
            restTraceabilityResults.add(RestTraceabilityResult);
        }

//        List<RestSkuResult> skuResults = skuService.formatSkuResult(skuIds);
//        List<BrandResult> brandResults = brandService.getBrandResults(brandIds);
//        List<OrCodeBind> qrCodes =InkindIds.size() == 0 ? new ArrayList<>() : orCodeBindService.query().eq("source", "item").in("form_id", InkindIds).list();
        for (RestTraceabilityResult RestTraceabilityResult : restTraceabilityResults) {
//            for (RestSkuResult skuResult : skuResults) {
//                if (skuResult.getSkuId().equals(TraceabilityResult.getSkuId())) {
//                    TraceabilityResult.setSkuResult(skuResult);
//                    break;
//                }
//            }
//            for (BrandResult brandResult : brandResults) {
//                if (brandResult.getBrandId().equals(TraceabilityResult.getBrandId())) {
//                    TraceabilityResult.setBrandResult(brandResult);
//                    break;
//                }
//            }
//            for (OrCodeBind qrCode : qrCodes) {
//                if (qrCode.getFormId().equals(TraceabilityResult.getInkindId())) {
//                    TraceabilityResult.setQrCodeId(qrCode.getOrCodeId());
//                }
//            }
        }

        return restTraceabilityResults;
    }

//    @Override
//    public TraceabilityResult TraceabilityDetail(TraceabilityParam param) {
//        Traceability Traceability = this.getById(param.getInkindId());
//        if (ToolUtil.isEmpty(Traceability)) {
//            throw new ServiceException(500, "没有此物");
//        }
//        List<Long> skuIds = new ArrayList<>();
//        skuIds.add(Traceability.getSkuId());
//        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);
//        SkuResult skuResult = skuResults.get(0);
//        TraceabilityResult TraceabilityResult = new TraceabilityResult();
//        ToolUtil.copyProperties(Traceability, TraceabilityResult);
//        TraceabilityResult.setSkuResult(skuResult);
////        TraceabilityResult.setBrandResult(brandService.getBrandResult(Traceability.getBrandId()));
//        //打印查询打印模板
//        String template = printTemplateService.replaceTraceabilityTemplate(Traceability.getInkindId());
////        PrintTemplate printTemplate = printTemplateService.getOne(new QueryWrapper<PrintTemplate>() {{
////            eq("type", PHYSICALDETAIL);
////            eq("display", 1);
////        }});
////        if (ToolUtil.isNotEmpty(printTemplate)) {
////            this.returnPrintTemplate(TraceabilityResult, printTemplate);
////        }
//        TraceabilityResult.setPrintTemplate(template);
//        TraceabilityResult.setSkuResult(null);
//        TraceabilityResult.setBrandResult(null);
//        return TraceabilityResult;
//    }

//    public List<TraceabilityResult> details(TraceabilityParam param) {
//        PrintTemplate printTemplate = printTemplateService.getOne(new QueryWrapper<PrintTemplate>() {{
//            eq("type", PHYSICALDETAIL);
//            eq("display", 1);
//        }});
//
//        /**
//         * 查询出实物（Traceability) 然后查出对应物料（sku）, 品牌
//         */
//        List<TraceabilityResult> TraceabilityResults = new ArrayList<>();
//        List<Traceability> Traceabilitys = this.lambdaQuery().in(Traceability::getInkindId, param.getInkindIds()).list();
//
//        List<Long> brandIds = new ArrayList<>();
//        List<Long> skuIds = new ArrayList<>();
//        for (Traceability Traceability : Traceabilitys) {
//            skuIds.add(Traceability.getSkuId());
//            brandIds.add(Traceability.getBrandId());
//        }
//
//        //查询sku
//        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);
//
//        //查询品牌
//        List<BrandResult> brandResults = brandService.getBrandResults(brandIds);
//
//        //循环匹配sku
//        for (Traceability Traceability : Traceabilitys) {
//            TraceabilityResult TraceabilityResult = new TraceabilityResult();
//            ToolUtil.copyProperties(Traceability, TraceabilityResult);
//            for (SkuResult skuResult : skuResults) {
//                if (TraceabilityResult.getSkuId().equals(skuResult.getSkuId())) {
//                    TraceabilityResult.setSkuResult(skuResult);
//                }
//            }
//            for (BrandResult brandResult : brandResults) {
//                if (brandResult.getBrandId().equals(Traceability.getBrandId())) {
//                    TraceabilityResult.setBrandResult(brandResult);
//                }
//            }
//            TraceabilityResults.add(TraceabilityResult);
//        }
//
//        //查询不到模板 不执行返回打印模板操作
//        if (ToolUtil.isNotEmpty(printTemplate)) {
//            for (TraceabilityResult TraceabilityResult : TraceabilityResults) {
//                this.returnPrintTemplate(TraceabilityResult, printTemplate);
//                TraceabilityResult.setSkuResult(null);
//                TraceabilityResult.setBrandResult(null);
//            }
//        }
//
//        return TraceabilityResults;
//    }

//    private void returnPrintTemplate(TraceabilityResult param, PrintTemplate printTemplate) {
//
//        if (ToolUtil.isEmpty(printTemplate)) {
//            throw new ServiceException(500, "请先定义二维码模板");
//        }
//        System.out.println(PHYSICALDETAIL);
//        String templete = printTemplate.getTemplete();
//
//        if (templete.contains("${coding}")) {
//            String InkindId = param.getInkindId().toString();
//            String substring = InkindId.substring(InkindId.length() - 6);
//            templete = templete.replace("${coding}", substring);
//        }
//        if (templete.contains("${skuCoding}")) {
////            Sku sku = ToolUtil.isEmpty(param.getSkuId()) ? new Sku() : skuService.getById(param.getSkuId());
//            SkuResult skuResult = param.getSkuResult();
//            if (ToolUtil.isNotEmpty(skuResult) && ToolUtil.isNotEmpty(skuResult.getCoding())) {
//                templete = templete.replace("${skuCoding}", skuResult.getCoding());
//            } else if (ToolUtil.isNotEmpty(skuResult) && ToolUtil.isEmpty(skuResult.getCoding()) && ToolUtil.isEmpty(skuResult.getStandard())) {
//                templete = templete.replace("${skuCoding}", "无");
//            } else if (ToolUtil.isNotEmpty(skuResult) && ToolUtil.isNotEmpty(skuResult.getStandard())) {
//                templete = templete.replace("${skuCoding}", skuResult.getStandard());
//            }
//        }
//        if (templete.contains("${name}")) {
//            String name = param.getSkuResult().getSpuResult().getSpuClassificationResult().getName();
//            templete = templete.replace("${name}", name + "/" + param.getSkuResult().getSpuResult().getName());
//        }
//        if (templete.contains("${number}")) {
//            templete = templete.replace("${number}", param.getNumber().toString());
//        }
//        if (templete.contains("${brand}")) {
//            if (ToolUtil.isEmpty(param.getBrandResult()) || ToolUtil.isEmpty(param.getBrandResult().getBrandName())) {
//                templete = templete.replace("${brand}", "");
//            } else {
//                templete = templete.replace("${brand}", param.getBrandResult().getBrandName());
//            }
//        }
//        if (templete.contains("${qrCode}")) {
//            OrCodeBind orCodeBind = orCodeBindService.getOne(new QueryWrapper<OrCodeBind>() {{
//                eq("form_id", param.getInkindId());
//                eq("source", "item");
//            }});
//            Long url = orCodeBind.getOrCodeId();
//            String qrCode = qrCodeCreateService.createQrCode(url.toString());
//            templete = templete.replace("${qrCode}", qrCode);
//        }
//        PrintTemplateResult printTemplateResult = new PrintTemplateResult();
//        ToolUtil.copyProperties(printTemplate, printTemplateResult);
//        printTemplateResult.setTemplete(templete);
//        param.setPrintTemplateResult(printTemplateResult);
//    }


//    @Override
//    public TraceabilityResult getTraceabilityResult(Long id) {
//        Traceability Traceability = this.getById(id);
//        if (ToolUtil.isEmpty(Traceability)) {
//            throw new ServiceException(500, "当前数据不存在");
//        }
//        TraceabilityResult TraceabilityResult = new TraceabilityResult();
//        ToolUtil.copyProperties(Traceability, TraceabilityResult);
//
//        SkuResult skuResult = skuService.getSku(Traceability.getSkuId());
//        Brand brand = brandService.getById(Traceability.getBrandId());
//        TraceabilityResult.setSkuResult(skuResult);
//        TraceabilityResult.setBrand(brand);
//
//        return TraceabilityResult;
//    }

    private Serializable getKey(RestTraceabilityParam param) {
        return param.getInkindId();
    }

    private Page<RestTraceabilityResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private RestTraceability getOldEntity(RestTraceabilityParam param) {
        return this.getById(getKey(param));
    }

    private RestTraceability getEntity(RestTraceabilityParam param) {
        RestTraceability entity = new RestTraceability();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

//    private void format(List<TraceabilityResult> data) {
//
//        List<Long> skuIds = new ArrayList<>();
//        List<Long> positionIds = new ArrayList<>();
//        List<Long> userIds = new ArrayList<>();
//        List<Long> InkindIds = new ArrayList<>();
//        for (TraceabilityResult datum : data) {
//            skuIds.add(datum.getSkuId());
//            positionIds.add(datum.getPositionId());
//            userIds.add(datum.getCreateUser());
//            InkindIds.add(datum.getInkindId());
//        }
//
//        List<SkuSimpleResult> simpleResults = skuService.simpleFormatSkuResult(skuIds);
//        List<StorehousePositionsResult> positionsResultList = positionsService.details(positionIds);
//        List<User> userList = userIds.size() == 0 ? new ArrayList<>() : userService.listByIds(userIds);
//        List<MaintenanceLogDetailResult> maintenanceLogDetailResults = maintenanceLogDetailService.lastLogByInkindIds(InkindIds);
//        List<OrCodeBind> orCodeBinds = InkindIds.size() == 0 ? new ArrayList<>() : orCodeBindService.query().in("form_id", InkindIds).eq("display", 1).list();
//
//        for (TraceabilityResult datum : data) {
//
//            for (SkuSimpleResult simpleResult : simpleResults) {
//                if (datum.getSkuId().equals(simpleResult.getSkuId())) {
//                    datum.setSkuSimpleResult(simpleResult);
//                    break;
//                }
//            }
//
//            for (StorehousePositionsResult storehousePositionsResult : positionsResultList) {
//                if (ToolUtil.isNotEmpty(datum.getPositionId()) && datum.getPositionId().equals(storehousePositionsResult.getStorehousePositionsId())) {
//                    datum.setPositionsResult(storehousePositionsResult);
//                    break;
//                }
//            }
//
//            for (MaintenanceLogDetailResult maintenanceLogDetailResult : maintenanceLogDetailResults) {
//                if (ToolUtil.isNotEmpty(datum.getInkindId()) && datum.getInkindId().equals(maintenanceLogDetailResult.getInkindId())) {
//                    datum.setMaintenanceLogResult(maintenanceLogDetailResult);
//                    break;
//                }
//            }
//
//            for (User user : userList) {
//                if (datum.getCreateUser().equals(user.getUserId())) {
//                    String imgUrl = stepsService.imgUrl(user.getUserId().toString());
//                    user.setAvatar(imgUrl);
//                    datum.setUser(user);
//                    break;
//                }
//            }
//
//            for (OrCodeBind orCodeBind : orCodeBinds) {
//                if (datum.getInkindId().equals(orCodeBind.getFormId())) {
//                    datum.setQrCodeId(orCodeBind.getOrCodeId());
//                    break;
//                }
//            }
//
//        }
//
//    }

//    @Override
//    public void resultFormat(List<TraceabilityResult> data) {
//
//        List<Long> InkindIds = new ArrayList<>();
//        for (TraceabilityResult datum : data) {
//            InkindIds.add(datum.getInkindId());
//        }
//
//        List<OrCodeBind> orCodeBinds = InkindIds.size() == 0 ? new ArrayList<>() : orCodeBindService.query().in("form_id", InkindIds).eq("display", 1).list();
//
//        for (TraceabilityResult datum : data) {
//            for (OrCodeBind orCodeBind : orCodeBinds) {
//                if (datum.getInkindId().equals(orCodeBind.getFormId())) {
//                    datum.setQrCodeId(orCodeBind.getOrCodeId());
//                    break;
//                }
//            }
//        }
//
//    }


}
