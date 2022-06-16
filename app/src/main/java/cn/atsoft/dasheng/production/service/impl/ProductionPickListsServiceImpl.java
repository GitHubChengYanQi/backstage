package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.model.params.OutstockOrderParam;
import cn.atsoft.dasheng.app.model.result.ErpPartsDetailResult;
import cn.atsoft.dasheng.app.model.result.PartsResult;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.app.pojo.Listing;
import cn.atsoft.dasheng.app.service.ErpPartsDetailService;
import cn.atsoft.dasheng.app.service.OutstockOrderService;
import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.config.MobileService;
import cn.atsoft.dasheng.erp.entity.Announcements;
import cn.atsoft.dasheng.erp.entity.ApplyDetails;
import cn.atsoft.dasheng.erp.model.params.OutstockListingParam;
import cn.atsoft.dasheng.erp.model.result.AnnouncementsResult;
import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import cn.atsoft.dasheng.erp.service.AnnouncementsService;
import cn.atsoft.dasheng.erp.service.CodingRulesService;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsService;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.model.result.ActivitiSetpSetDetailResult;
import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.production.entity.*;
import cn.atsoft.dasheng.production.mapper.ProductionPickListsMapper;
import cn.atsoft.dasheng.production.model.params.ProductionPickListsCartParam;
import cn.atsoft.dasheng.production.model.params.ProductionPickListsDetailParam;
import cn.atsoft.dasheng.production.model.params.ProductionPickListsParam;
import cn.atsoft.dasheng.production.model.request.SavePickListsObject;
import cn.atsoft.dasheng.production.model.result.*;
import cn.atsoft.dasheng.production.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.sendTemplate.WxCpTemplate;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 领料单 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-25
 */
@Service
public class ProductionPickListsServiceImpl extends ServiceImpl<ProductionPickListsMapper, ProductionPickLists> implements ProductionPickListsService {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductionTaskService productionTaskService;

    @Autowired
    private ProductionWorkOrderService workOrderService;

    @Autowired
    private ActivitiSetpSetService setpSetService;

    @Autowired
    private ProductionTaskDetailService taskDetailService;

    @Autowired
    private ProductionPickListsDetailService pickListsDetailService;

    @Autowired
    private ProductionPickListsCartService pickListsCartService;

    @Autowired
    private SkuService skuService;

    @Autowired
    private PartsService partsService;

    @Autowired
    private ErpPartsDetailService partsDetailService;

    @Autowired
    private StockDetailsService stockDetailsService;

    @Autowired
    private StorehousePositionsService storehousePositionsService;

    @Autowired
    private OutstockOrderService outstockOrderService;

    @Autowired
    private ProductionPickCodeService pickCodeService;

    @Autowired
    private ProductionPickCodeBindService codeBindService;

    @Autowired
    private MobileService mobileService;

    @Autowired
    private WxCpSendTemplate wxCpSendTemplate;
    @Autowired
    private CodingRulesService codingRulesService;
    @Autowired
    private ActivitiSetpSetDetailService setpSetDetailService;
    @Autowired
    private ShipSetpService shipSetpService;
    @Autowired
    private ActivitiProcessService activitiProcessService;
    @Autowired
    private ActivitiProcessTaskService activitiProcessTaskService;
    @Autowired
    private ActivitiProcessLogService activitiProcessLogService;
    @Autowired
    private AnnouncementsService announcementsService;


    @Override
    public void add(ProductionPickListsParam param) {
        ProductionPickLists entity = getEntity(param);
        entity.setCoding(codingRulesService.defaultEncoding());
        entity.setStatus(0L);

        entity.setUserId(LoginContextHolder.getContext().getUserId());
        this.save(entity);

        if (ToolUtil.isNotEmpty(param.getPickListsDetailParams())) {
            List<ProductionPickListsDetail> details = new ArrayList<>();
            for (ProductionPickListsDetailParam pickListsDetailParam : param.getPickListsDetailParams()) {
                ProductionPickListsDetail detail = new ProductionPickListsDetail();
                ToolUtil.copyProperties(pickListsDetailParam, detail);
                detail.setPickListsId(entity.getPickListsId());
                details.add(detail);
            }
            pickListsDetailService.saveBatch(details);
        }
        ActivitiProcess activitiProcess = activitiProcessService.query().eq("type", "OUTSTOCK").eq("status", 99).eq("module", "pickLists").one();
        if (ToolUtil.isNotEmpty(activitiProcess)) {
//                this.power(activitiProcess);//检查创建权限
//                LoginUser user = LoginContextHolder.getContext().getUser();
            ActivitiProcessTaskParam activitiProcessTaskParam = new ActivitiProcessTaskParam();
//                activitiProcessTaskParam.setTaskName(user.getName() + "发起的质检任务 (" + param.getCoding() + ")");
            activitiProcessTaskParam.setTaskName("新的领料申请 (" + entity.getCoding() + ")");
            activitiProcessTaskParam.setUserId(param.getUserId());
            activitiProcessTaskParam.setFormId(entity.getPickListsId());
            activitiProcessTaskParam.setType("OUTSTOCK");
            activitiProcessTaskParam.setProcessId(activitiProcess.getProcessId());
            ActivitiProcessTask activitiProcessTask = new ActivitiProcessTask();
            ToolUtil.copyProperties(activitiProcessTaskParam, activitiProcessTask);
            Long taskId = activitiProcessTaskService.add(activitiProcessTaskParam);
            //添加铃铛
            wxCpSendTemplate.setSource("processTask");
            wxCpSendTemplate.setSourceId(taskId);
            //添加log
            activitiProcessLogService.addLog(activitiProcess.getProcessId(), taskId);
            activitiProcessLogService.autoAudit(taskId, 1, LoginContextHolder.getContext().getUserId());


        } else {
            throw new ServiceException(500, "请创建质检流程！");
        }
    }

    @Override
    public void delete(ProductionPickListsParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(ProductionPickListsParam param) {
        ProductionPickLists oldEntity = getOldEntity(param);
        ProductionPickLists newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ProductionPickListsResult findBySpec(ProductionPickListsParam param) {
        return null;
    }

    @Override
    public List<ProductionPickListsResult> findListBySpec(ProductionPickListsParam param) {
        List<ProductionPickListsResult> productionPickListsResults = this.baseMapper.customList(param);
        this.format(productionPickListsResults);
        return productionPickListsResults;
    }

    @Override
    public PageInfo<ProductionPickListsResult> findPageBySpec(ProductionPickListsParam param) {
        Page<ProductionPickListsResult> pageContext = getPageContext();
        IPage<ProductionPickListsResult> page = this.baseMapper.customPageList(pageContext, param);
        if (ToolUtil.isNotEmpty(page.getRecords())) {
            this.format(page.getRecords());
        }
        return PageFactory.createPageInfo(page);
    }

    @Override
    public void format(List<ProductionPickListsResult> results) {
        List<Long> pickListsIds = new ArrayList<>();
        List<Long> productionTaskIds = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        List<Long> announcementsIds = new ArrayList<>();
        for (ProductionPickListsResult result : results) {
            pickListsIds.add(result.getPickListsId());
            userIds.add(result.getUserId());
            userIds.add(result.getCreateUser());
            if (ToolUtil.isNotEmpty(result.getSource()) && result.getSource().equals("productionTask")) {
                productionTaskIds.add(result.getSourceId());
            }
            if (ToolUtil.isNotEmpty(result.getRemarks())) {
                announcementsIds.addAll(Arrays.asList(result.getRemarks().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList()));
            }
        }
        List<Announcements> announcements = announcementsIds.size() == 0 ? new ArrayList<>() : announcementsService.listByIds(announcementsIds);
        List<AnnouncementsResult> announcementsResults = BeanUtil.copyToList(announcements, AnnouncementsResult.class, new CopyOptions());
        List<UserResult> userResultsByIds =userIds.size() == 0 ? new ArrayList<>() : userService.getUserResultsByIds(userIds);
        /**
         * 查询备料单与领料单
         */
        List<ProductionPickListsDetailResult> detailResults = pickListsDetailService.findListBySpec(new ProductionPickListsDetailParam() {{
            setStatus(0);
        }});
        List<Long> skuIds = new ArrayList<>();
        for (ProductionPickListsDetailResult detailResult : detailResults) {
            skuIds.add(detailResult.getSkuId());
        }
//        List<SkuResult> skuResults = skuIds.size() == 0 ? new ArrayList<>() : skuService.formatSkuResult(skuIds);
        List<SkuSimpleResult> skuSimpleResults = skuIds.size() == 0 ? new ArrayList<>() : skuService.simpleFormatSkuResult(skuIds);
        for (ProductionPickListsDetailResult detailResult : detailResults) {
            for (SkuSimpleResult skuResult : skuSimpleResults) {
                if (skuResult.getSkuId().equals(detailResult.getSkuId())) {
                    detailResult.setSkuResult(skuResult);
                    break;
                }
            }
        }
        List<Long> shipSetpIds = new ArrayList<>();
        List<ProductionTaskResult> productionTaskResults = productionTaskIds.size() == 0 ? new ArrayList<>() : productionTaskService.resultsByIds(productionTaskIds);
        for (ProductionTaskResult productionTaskResult : productionTaskResults) {
            shipSetpIds.add(productionTaskResult.getShipSetpId());
        }
        List<ShipSetpResult> shipSetpResults = shipSetpIds.size() == 0 ? new ArrayList<>() : shipSetpService.getResultsByids(shipSetpIds);


        for (ProductionPickListsResult result : results) {
            for (ProductionTaskResult productionTaskResult : productionTaskResults) {
                if (ToolUtil.isNotEmpty(result.getSource()) && result.getSource().equals("productionTask") && result.getSourceId().equals(productionTaskResult.getProductionTaskId())) {
                    for (ShipSetpResult shipSetpResult : shipSetpResults) {
                        if (productionTaskResult.getShipSetpId().equals(shipSetpResult.getShipSetpId())) {
                            productionTaskResult.setShipSetpResult(shipSetpResult);
                        }
                    }
                    result.setProductionTaskResult(productionTaskResult);
                }
            }

            for (UserResult userResultsById : userResultsByIds) {
                if (result.getUserId().equals(userResultsById.getUserId())) {
                    result.setUserResult(userResultsById);
                }
                if (result.getCreateUser().equals(userResultsById.getUserId())) {
                    result.setCreateUserResult(userResultsById);
                }
            }
            List<ProductionPickListsDetailResult> detailResultList = new ArrayList<>();
            for (ProductionPickListsDetailResult detailResult : detailResults) {
                if (result.getPickListsId().equals(detailResult.getPickListsId())) {
                    detailResult.setUserResult(result.getUserResult());
                    detailResult.setPickListsCoding(result.getCoding());
                    detailResultList.add(detailResult);
                }
            }
            result.setDetailResults(detailResultList);
            List<AnnouncementsResult> announcementsResultList = new ArrayList<>();
            List<Long> collect = Arrays.asList(result.getRemarks().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            for (Long aLong : collect) {
                for (AnnouncementsResult announcementsResult : announcementsResults) {
                    if (announcementsResult.getNoticeId().equals(aLong)) {
                        announcementsResultList.add(announcementsResult);
                    }
                }
            }
            result.setAnnouncementsResults(announcementsResultList);
        }

    }

    @Override
    public void formatStatus99(List<ProductionPickListsResult> results) {
        List<Long> pickListsIds = new ArrayList<>();
        List<Long> productionTaskIds = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        for (ProductionPickListsResult result : results) {
            pickListsIds.add(result.getPickListsId());
            userIds.add(result.getUserId());
            userIds.add(result.getCreateUser());
            productionTaskIds.add(result.getSourceId());
        }

        List<UserResult> userResultsByIds = userService.getUserResultsByIds(userIds);
        /**
         * 查询备料单与领料单
         */
        List<ProductionPickListsDetailResult> detailResults = pickListsDetailService.getByTask(new ProductionPickListsDetailParam() {{
        }});
        List<Long> skuIds = new ArrayList<>();
        for (ProductionPickListsDetailResult detailResult : detailResults) {
            skuIds.add(detailResult.getSkuId());
        }
//        List<SkuResult> skuResults = skuIds.size() == 0 ? new ArrayList<>() : skuService.formatSkuResult(skuIds);
        List<SkuSimpleResult> skuResults = skuIds.size() == 0 ? new ArrayList<>() : skuService.simpleFormatSkuResult(skuIds);
        for (ProductionPickListsDetailResult detailResult : detailResults) {
            for (SkuSimpleResult skuResult : skuResults) {
                if (skuResult.getSkuId().equals(detailResult.getSkuId())) {
                    detailResult.setSkuResult(skuResult);
                    break;
                }
            }
        }


        List<ProductionTaskResult> productionTaskResults = productionTaskIds.size() == 0 ? new ArrayList<>() : productionTaskService.resultsByIds(productionTaskIds);

        for (ProductionPickListsResult result : results) {
            for (ProductionTaskResult productionTaskResult : productionTaskResults) {
                if (result.getSource().equals("productionTask") && result.getSourceId().equals(productionTaskResult.getProductionTaskId())) {
                    result.setProductionTaskResult(productionTaskResult);
                }
            }
            List<ProductionPickListsDetailResult> detailResultList = new ArrayList<>();
            for (ProductionPickListsDetailResult detailResult : detailResults) {
                if (result.getPickListsId().equals(detailResult.getPickListsId())) {
                    detailResultList.add(detailResult);
                }
            }
            result.setDetailResults(detailResultList);
            for (UserResult userResultsById : userResultsByIds) {
                if (result.getUserId().equals(userResultsById.getUserId())) {
                    result.setUserResult(userResultsById);
                }
                if (result.getCreateUser().equals(userResultsById.getUserId())) {
                    result.setUserResult(userResultsById);
                }
            }
        }
    }

    @Override
    public String addByProductionTask(Object param) {
        SavePickListsObject savePickListsObject = JSON.parseObject(param.toString(), SavePickListsObject.class);
        Long taskId = savePickListsObject.getProductionTask().getProductionTaskId();

        ProductionPickLists productionPickLists = new ProductionPickLists();
        productionPickLists.setCoding(codingRulesService.defaultEncoding());
        productionPickLists.setStatus(0L);
        productionPickLists.setSourceId(taskId);
        productionPickLists.setSource("productionTask");
        productionPickLists.setUserId(savePickListsObject.getProductionTask().getUserId());
        this.save(productionPickLists);
//        for (ProductionTaskDetail detail : savePickListsObject.getDetails()) {
//            outskuIds.add(detail.getOutSkuId());
//        }
        ProductionTask task = productionTaskService.getById(taskId);
        Long workOrderId = task.getWorkOrderId();
        ProductionWorkOrder workOrder = workOrderService.getById(workOrderId);
        Long stepsId = workOrder.getStepsId();
        List<ActivitiSetpSetDetailResult> resultByStepsIds = setpSetDetailService.getResultByStepsIds(new ArrayList<Long>() {{
            add(stepsId);
        }});
        List<Long> outSkuIds = new ArrayList<>();
        List<Long> inSkuIds = new ArrayList<>();
        for (ActivitiSetpSetDetailResult resultByStepsId : resultByStepsIds) {
            if (resultByStepsId.getType().equals("out")) {
                outSkuIds.add(resultByStepsId.getSkuId());
            } else if (resultByStepsId.getType().equals("in")) {
                inSkuIds.add(resultByStepsId.getSkuId());
            }
        }
        if (outSkuIds.size() > 0) {
            List<Parts> parts = outSkuIds.size() == 0 ? new ArrayList<>() : partsService.query().in("sku_id", outSkuIds).eq("status", 99).list();
            List<Long> partIds = new ArrayList<>();
            List<PartsResult> partsResults = new ArrayList<>();
            for (Parts part : parts) {
                partIds.add(part.getPartsId());
                PartsResult partsResult = new PartsResult();
                ToolUtil.copyProperties(part, partsResult);
                partsResults.add(partsResult);
            }
            List<ErpPartsDetailResult> partsDetailResults = partsDetailService.getDetails(partIds);
            for (PartsResult partsResult : partsResults) {
                List<ErpPartsDetailResult> partsDetailResultList = new ArrayList<>();
                for (ErpPartsDetailResult partsDetailResult : partsDetailResults) {
                    if (partsDetailResult.getPartsId().equals(partsResult.getPartsId())) {
                        partsDetailResultList.add(partsDetailResult);
                    }
                }
                partsResult.setParts(partsDetailResultList);
            }

            List<ProductionPickListsDetail> details = new ArrayList<>();

            for (ProductionTaskDetail detail : savePickListsObject.getDetails()) {
                for (PartsResult partsResult : partsResults) {
                    if (detail.getOutSkuId().equals(partsResult.getSkuId())) {
                        for (ErpPartsDetailResult part : partsResult.getParts()) {
                            ProductionPickListsDetail productionPickListsDetail = new ProductionPickListsDetail();
                            productionPickListsDetail.setNumber((int) (detail.getNumber() * part.getNumber()));
                            productionPickListsDetail.setSkuId(part.getSkuId());
                            productionPickListsDetail.setPickListsId(productionPickLists.getPickListsId());
                            details.add(productionPickListsDetail);
                        }
                        break;
                    }
                }
            }


            pickListsDetailService.saveBatch(details);
        } else if (inSkuIds.size() > 0) {
            List<ProductionPickListsDetail> details = new ArrayList<>();
            for (Long inSkuId : inSkuIds) {
                for (ProductionTaskDetail detail : savePickListsObject.getDetails()) {
                    if (detail.getOutSkuId().equals(inSkuId)) {
                        ProductionPickListsDetail productionPickListsDetail = new ProductionPickListsDetail();
                        productionPickListsDetail.setNumber(detail.getNumber());
                        productionPickListsDetail.setSkuId(inSkuId);
                        productionPickListsDetail.setPickListsId(productionPickLists.getPickListsId());
                        details.add(productionPickListsDetail);
                    }
                }
            }
            pickListsDetailService.saveBatch(details);
        }

        return null;
    }

    public void takePick(ProductionPickListsParam param) {
        ProductionPickLists pickLists = this.getById(param.getPickListsId());

        if (!pickLists.getStatus().equals(98)) {
            throw new ServiceException(500, "您的领料单还没有准备好");
        }
        pickLists.setStatus(99L);
        this.updateById(pickLists);

    }

    @Override
    public void sendPersonPick(ProductionPickListsParam param) {
//        List<ProductionPickListsCart> carts = new ArrayList<>();
//        for (ProductionPickListsCartParam cartsParam : param.getCartsParams()) {
//            ProductionPickListsCart cart = new ProductionPickListsCart();
//            ToolUtil.copyProperties(cartsParam, cart);
//            cart.setStatus(99);
//            carts.add(cart);
//        }
//        pickListsCartService.updateBatchById(carts);
        WxCpTemplate wxCpTemplate = new WxCpTemplate();
        wxCpTemplate.setUrl(mobileService.getMobileConfig().getUrl() + "/#/Work/MyPicking");
        wxCpTemplate.setTitle("新的生产任务");
        wxCpTemplate.setDescription("库管那里有新的物料待领取");
        wxCpTemplate.setUserIds(Arrays.asList(param.getUserIds().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList()));
        wxCpTemplate.setType(0);
        wxCpSendTemplate.setSource("selfPick");
        wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
        wxCpSendTemplate.sendTemplate();
    }

    private Serializable getKey(ProductionPickListsParam param) {
        return param.getPickListsId();
    }

    private Page<ProductionPickListsResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ProductionPickLists getOldEntity(ProductionPickListsParam param) {
        return this.getById(getKey(param));
    }

    private ProductionPickLists getEntity(ProductionPickListsParam param) {
        ProductionPickLists entity = new ProductionPickLists();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public List<StorehouseResult> getStockSkus(List<Long> skuIds) {
//        List<StockDetails> stockSkus = stockDetailsService.query().in("sku_id", skuIds).list();
//
//        List<StockSkuTotal> stockDetails = new ArrayList<>();
//        for (StockDetails skus : stockSkus) {
//            StockSkuTotal stockSkuTotal = new StockSkuTotal();
//            stockSkuTotal.setSkuId(skus.getSkuId());
//            stockSkuTotal.setStorehousePositionsId(skus.getStorehousePositionsId());
//            stockSkuTotal.setNumber(skus.getNumber());
//            stockDetails.add(stockSkuTotal);
//        }
//        List<StockSkuTotal> totalList = new ArrayList<>();
//        stockDetails.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + '_' + item.getStorehousePositionsId(), Collectors.toList())).forEach(
//                (id, transfer) -> {
//                    transfer.stream().reduce((a, b) -> new StockSkuTotal(a.getSkuId(), a.getStorehousePositionsId(), a.getNumber() + b.getNumber(), new StorehousePositionsResult())).ifPresent(totalList::add);
//                }
//        );
//
//        /**
//         * 查找库位
//         */
//        List<StorehousePositions> storehousePositions = storehousePositionsService.list();
//        List<StorehousePositionsResult> storehousePositionsResults = new ArrayList<>();
//        for (StorehousePositions storehousePosition : storehousePositions) {
//            StorehousePositionsResult storehousePositionsResult = new StorehousePositionsResult();
//            ToolUtil.copyProperties(storehousePosition, storehousePositionsResult);
//            storehousePositionsResults.add(storehousePositionsResult);
//        }
//
        return null;
    }

    @Override
    public void outStock(ProductionPickListsParam param) {
        List<Long> stockIds = new ArrayList<>();
        List<Long> pickListsIds = new ArrayList<>();
        List<Long> pickListsCartIds = new ArrayList<>();
        for (ProductionPickListsCartParam pickListsCartParam : param.getCartsParams()) {
            stockIds.add(pickListsCartParam.getStorehouseId());
            pickListsIds.add(pickListsCartParam.getPickListsId());
        }


        /**
         * 更新子表记录
         */


        List<ProductionPickLists> pickLists = pickListsIds.size() == 0 ? new ArrayList<>() : this.query().in("pick_lists_id", pickListsIds).list();
        List<Long> userIds = new ArrayList<>();
        for (ProductionPickLists pickList : pickLists) {
            userIds.add(pickList.getUserId());
        }

        /**
         * 查询购物车
         */
        List<ProductionPickListsCart> listsCarts = pickListsCartService.query().in("pick_lists_id", pickListsIds).eq("display", 1).eq("status", 0).list();


//        /**
//         * 拦截判断
//         */
//
//        List<ProductionPickCode> code = pickCodeService.query().eq("code", param.getCode()).list();
//        userIds = userIds.stream().distinct().collect(Collectors.toList());
//        for (Long userId : userIds) {
//            if (code.stream().noneMatch(i->i.getUserId().equals(userId))) {
//                throw new ServiceException(500,"提交的数据与领料码不完全匹配");
//            }
//        }
//        List<ProductionPickCodeBind> codeBinds = new ArrayList<>();
//        for (ProductionPickCode productionPickCode : code) {
//            for (ProductionPickListsDetailParam pickListsDetailParam : param.getPickListsDetailParams()) {
//               if (pickListsDetailParam.getPickListsId().equals(productionPickCode.getPickListsId())){
//                   ProductionPickCodeBind productionPickCodeBind = new ProductionPickCodeBind();
//                   productionPickCodeBind.setPickCodeId(productionPickCode.getPickCodeId());
//                   productionPickCodeBind.setPickListsId(pickListsDetailParam.getPickListsId());
//                   productionPickCodeBind.setSkuId(pickListsDetailParam.getSkuId());
//                   productionPickCodeBind.setNumber(pickListsDetailParam.getNumber());
//                   codeBinds.add(productionPickCodeBind);
//               }
//            }
//        }
//
//
//        codeBindService.saveBatch(codeBinds);
        List<ProductionPickListsCart> newCarts = new ArrayList<>();
        List<ProductionPickListsDetail> pickListsDetails = pickListsIds.size() == 0 ? new ArrayList<>() : pickListsDetailService.query().in("pick_lists_id", pickListsIds).eq("status", 0).list();
        for (ProductionPickListsCartParam pickListsCartParam : param.getCartsParams()) {
            int num = pickListsCartParam.getNumber();
            for (ProductionPickListsCart listsCart : listsCarts) {
                if (listsCart.getPickListsId().equals(pickListsCartParam.getPickListsId()) && listsCart.getBrandId().equals(pickListsCartParam.getBrandId()) && listsCart.getSkuId().equals(pickListsCartParam.getSkuId())) {

                    if (ToolUtil.isNotEmpty(pickListsCartParam.getBrandIds()) && pickListsCartParam.getBrandIds().stream().anyMatch(i->i.equals(listsCart.getBrandId()))){
                        if (pickListsCartParam.getBrandIds().equals(listsCart.getBrandId())){
                            num -= listsCart.getNumber();
                            if (num >= 0) {
                                listsCart.setDisplay(0);
                                listsCart.setStatus(99);

                                for (ProductionPickListsDetail pickListsDetail : pickListsDetails) {
                                    if (listsCart.getPickListsDetailId().equals(pickListsDetail.getPickListsDetailId())) {
                                        pickListsDetail.setStatus(99);
                                        pickListsDetail.setReceivedNumber(pickListsDetail.getNumber());
                                    }
                                }
                            } else {
                                listsCart.setNumber(listsCart.getNumber() - (num * -1));
                                listsCart.setStatus(99);
                                ProductionPickListsCart newCart = new ProductionPickListsCart();
                                ToolUtil.copyProperties(listsCart, newCart);
                                newCart.setNumber(num * -1);
                                newCarts.add(newCart);
                                for (ProductionPickListsDetail pickListsDetail : pickListsDetails) {
                                    if (listsCart.getPickListsDetailId().equals(pickListsDetail.getPickListsDetailId())) {
                                        if (ToolUtil.isNotEmpty(pickListsDetail.getReceivedNumber())) {
                                            pickListsDetail.setReceivedNumber(pickListsDetail.getReceivedNumber() + pickListsCartParam.getNumber());
                                        } else {
                                            pickListsDetail.setReceivedNumber(pickListsCartParam.getNumber());
                                        }
                                    }
                                }

                            }
                        }
                    }

                }
            }


//            int num = pickListsCartParam.getNumber();
//
        }
        //如果购物车有数量拆分情况  则新增一条购物车
        pickListsCartService.updateBatchById(listsCarts);

        if (newCarts.size() > 0) {
            pickListsCartService.saveBatch(newCarts);
        }
        pickListsDetailService.updateBatchById(pickListsDetails);
        /**
         * 更新领料单状态
         */
        List<ProductionPickListsDetail> update = pickListsIds.size() == 0 ? new ArrayList<>() : pickListsDetailService.query().in("pick_lists_id", pickListsIds).ne("status", 99).list();
        for (
                ProductionPickLists pickList : pickLists) {
            if (update.stream().noneMatch(i -> i.getPickListsId().equals(pickList.getPickListsId()))) {
                pickList.setStatus(99L);
            }
        }
        this.

                updateBatchById(pickLists);
        /**
         * 创建入库记录
         */
        stockIds = stockIds.stream().distinct().collect(Collectors.toList());
        for (Long stockId : stockIds) {
            OutstockOrderParam outstockOrder = new OutstockOrderParam();
            outstockOrder.setStorehouseId(stockId);

            outstockOrder.setUserId(LoginContextHolder.getContext().getUserId());
            List<OutstockListingParam> listings = new ArrayList<>();

            for (ProductionPickListsCart listsCart : listsCarts) {
                if (listsCart.getStatus() == 99 && listsCart.getStorehouseId().equals(stockId)){
                    OutstockListingParam listingParam = new OutstockListingParam();
                    listingParam.setNumber(Long.valueOf(listsCart.getNumber()));
                    listingParam.setSkuId(listsCart.getSkuId());
                    listingParam.setPositionsId(listsCart.getStorehousePositionsId());
                    if (ToolUtil.isNotEmpty(listsCart.getBrandId()) || listsCart.getBrandId().equals(0L)) {
                        listingParam.setBrandId(listsCart.getBrandId());
                    }
                    listings.add(listingParam);
                }
            }
            List<OutstockListingParam> outstockListingParams = outstockOrderService.outBoundByLists(listings);
            outstockOrder.setListingParams(outstockListingParams);
            outstockOrderService.saveOutStockOrderByPickLists(outstockOrder);
        }


        pickListsCartService.updateBatchById(listsCarts);
    }

    @Override
    public ProductionPickListsResult detail(Long id) {
        ProductionPickLists productionPickLists = this.getById(id);
        ProductionPickListsResult result = new ProductionPickListsResult();
        ToolUtil.copyProperties(productionPickLists, result);
        this.format(new ArrayList<ProductionPickListsResult>() {{
            add(result);
        }});
        List<ProductionPickListsDetailResult> detailResults = pickListsDetailService.findListBySpec(new ProductionPickListsDetailParam() {{
            setPickListsId(id);
            setStatus(0);
        }});
        List<Long> pickListsIds = new ArrayList<>();
        for (ProductionPickListsDetailResult productionPickListsDetailResult : detailResults) {
            pickListsIds.add(productionPickListsDetailResult.getPickListsId());
        }
        pickListsIds = pickListsIds.stream().distinct().collect(Collectors.toList());

        List<ProductionPickListsCart> listsCarts = pickListsCartService.query().in("pick_lists_id", pickListsIds).ne("status", -1).list();
        for (ProductionPickListsDetailResult pickListsDetailResult : detailResults) {
            for (ProductionPickListsCart listsCart : listsCarts) {
                if (pickListsDetailResult.getPickListsId().equals(listsCart.getPickListsId()) &&
//                        ToolUtil.isNotEmpty(listsCart.getBrandId()) && ToolUtil.isNotEmpty(pickListsDetailResult.getBrandId()) &&
//                        listsCart.getBrandId().equals(pickListsDetailResult.getBrandId()) &&
                        listsCart.getSkuId().equals(pickListsDetailResult.getSkuId()) &&
                        listsCart.getPickListsDetailId().equals(pickListsDetailResult.getPickListsDetailId())

                ) {
                    pickListsDetailResult.setNumber(pickListsDetailResult.getNumber() - listsCart.getNumber());
                }
//                else if (pickListsDetailResult.getPickListsId().equals(listsCart.getPickListsId()) &&
//                        ToolUtil.isEmpty(listsCart.getBrandId()) && ToolUtil.isEmpty(pickListsDetailResult.getBrandId()) &&
//                        listsCart.getSkuId().equals(pickListsDetailResult.getSkuId())){
//                    pickListsDetailResult.setNumber(pickListsDetailResult.getNumber()- listsCart.getNumber());
//                }
            }
        }
        this.pickListsDetailService.format(detailResults);


        result.setDetailResults(detailResults);







        return result;

    }

    @Override
    public List<Map<String, Object>> listByUser(ProductionPickListsParam pickListsParam) {
        List<ProductionPickListsResult> listBySpec = this.findListBySpec(pickListsParam);
        List<Long> userIds = new ArrayList<>();
        List<Long> pickListsIds = new ArrayList<>();
        for (ProductionPickListsResult result : listBySpec) {
            userIds.add(result.getUserId());
            pickListsIds.add(result.getPickListsId());
        }
        List<ProductionPickListsCart> carts = pickListsCartService.query().in("pick_lists_id", pickListsIds).eq("display", 1).list();
        List<ProductionPickListsCartResult> productionPickListsCartResults = BeanUtil.copyToList(carts, ProductionPickListsCartResult.class, new CopyOptions());
        pickListsCartService.format(productionPickListsCartResults);


        userIds = userIds.stream().distinct().collect(Collectors.toList());
        List<User> users = userIds.size() == 0 ? new ArrayList<>() : userService.listByIds(userIds);
        List<Map<String, Object>> result = new ArrayList<>();
        for (User user : users) {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", user.getUserId());
            map.put("userName", user.getName());
            List<ProductionPickListsResult> pickListsResults = new ArrayList<>();
            for (ProductionPickListsResult productionPickListsResult : listBySpec) {
                List<ProductionPickListsCartResult> cartResults = new ArrayList<>();
                for (ProductionPickListsCartResult productionPickListsCartResult : productionPickListsCartResults) {
                    if (productionPickListsCartResult.getPickListsId().equals(productionPickListsResult.getPickListsId())) {
                        cartResults.add(productionPickListsCartResult);
                    }
                }
                productionPickListsResult.setCartResults(cartResults);
                if (user.getUserId().equals(productionPickListsResult.getUserId())) {
                    pickListsResults.add(productionPickListsResult);
                }
            }
            map.put("pickListsResults", pickListsResults);
            result.add(map);
        }
        return result;
    }
}
