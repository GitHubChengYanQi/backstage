package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.action.Enum.OutStockActionEnum;
import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.model.params.OutstockOrderParam;
import cn.atsoft.dasheng.app.model.result.ErpPartsDetailResult;
import cn.atsoft.dasheng.app.model.result.PartsResult;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.app.pojo.StockSkuBrand;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.appBase.service.MediaService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.config.MobileService;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.model.params.OutstockListingParam;
import cn.atsoft.dasheng.erp.model.result.AnnouncementsResult;
import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.form.entity.*;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.model.result.ActivitiSetpSetDetailResult;
import cn.atsoft.dasheng.form.pojo.ActionStatus;
import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.message.entity.MarkDownTemplate;
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
import cn.atsoft.dasheng.purchase.service.GetOrigin;
import cn.atsoft.dasheng.sendTemplate.RedisSendCheck;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.sendTemplate.pojo.MarkDownTemplateTypeEnum;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.RoleService;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private DocumentsActionService actionService;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private DocumentStatusService statusService;
    @Autowired
    private ActivitiAuditService auditService;

    @Autowired
    private InstockLogDetailService instockLogDetailService;

    @Autowired
    private ShopCartService shopCartService;

    @Autowired
    private ActivitiProcessLogService processLogService;

    @Autowired
    private ActivitiStepsService stepsService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RedisSendCheck redisSendCheck;
    @Autowired
    private GetOrigin getOrigin;
    @Autowired
    private StorehousePositionsBindService positionsBindService;


    @Override
    @Transactional
    public ProductionPickLists add(ProductionPickListsParam param) {
        ProductionPickLists entity = getEntity(param);
        entity.setCoding(codingRulesService.defaultEncoding());
        entity.setStatus(0L);
//        entity.setUserId(LoginContextHolder.getContext().getUserId());
        this.save(entity);
//        String origin = getOrigin.newThemeAndOrigin("OUTSTOCK", entity.getPickListsId(), entity.getSource(), entity.getSourceId());
//        entity.setOrigin(origin);
//        this.updateById(entity);

        if (ToolUtil.isNotEmpty(param.getPickListsDetailParams())) {
            List<ShopCart> carts = new ArrayList<>();
            List<ProductionPickListsDetail> details = new ArrayList<>();
            for (ProductionPickListsDetailParam pickListsDetailParam : param.getPickListsDetailParams()) {
                ProductionPickListsDetail detail = new ProductionPickListsDetail();
                ToolUtil.copyProperties(pickListsDetailParam, detail);
                detail.setPickListsId(entity.getPickListsId());
                details.add(detail);
                carts.add(new ShopCart() {{
                    setCartId(pickListsDetailParam.getCartId());
                    setDisplay(0);
                }});
            }
            shopCartService.updateBatchById(carts);
            pickListsDetailService.saveBatch(details);
        }
        ActivitiProcess activitiProcess = activitiProcessService.query().eq("type", "OUTSTOCK").eq("status", 99).eq("module", "pickLists").one();
        if (ToolUtil.isNotEmpty(activitiProcess)) {
            activitiProcessTaskService.checkStartUser(activitiProcess.getProcessId());
            auditService.power(activitiProcess);//检查创建权限
            ActivitiProcessTaskParam activitiProcessTaskParam = new ActivitiProcessTaskParam();
            String name = LoginContextHolder.getContext().getUser().getName();
            activitiProcessTaskParam.setTaskName(name + "的出库申请 ");
            activitiProcessTaskParam.setUserId(param.getUserId());
            activitiProcessTaskParam.setFormId(entity.getPickListsId());
            activitiProcessTaskParam.setType("OUTSTOCK");
            activitiProcessTaskParam.setUserId(param.getUserId());
            activitiProcessTaskParam.setProcessId(activitiProcess.getProcessId());
            activitiProcessTaskParam.setSource(param.getSource());
            activitiProcessTaskParam.setSourceId(param.getSourceId());
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
        return entity;
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
    public String createCode(ProductionPickListsParam param) {
        String code = String.valueOf(RandomUtil.randomLong(1000, 9999));
        List<Object> list = redisSendCheck.getList(code);
        param.getCartsParams();
        List<Object> objects = BeanUtil.copyToList(param.getCartsParams(), Object.class);
        if (ToolUtil.isEmpty(list)) {
            redisSendCheck.pushList(code, objects,1000L * 60L * 10L);
            return code;
        }
        return createCode(param);
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
    public void taskFormat(List<ProductionPickListsResult> results) {
        List<Long> pickListsIds = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        for (ProductionPickListsResult result : results) {

            pickListsIds.add(result.getPickListsId());
            userIds.add(result.getUserId());
            userIds.add(result.getCreateUser());

        }

        List<ProductionPickListsDetailResult> detailResults = pickListsDetailService.resultsByPickListsIds(pickListsIds);
        List<Long> skuIds = new ArrayList<>();
        for (ProductionPickListsDetailResult detailResult : detailResults) {
            skuIds.add(detailResult.getSkuId());
        }
        List<StorehousePositionsBind> positionsBinds = skuIds.size() == 0 ? new ArrayList<>() : positionsBindService.query().in("sku_id", skuIds).eq("display", 1).list();


        for (ProductionPickListsResult result : results) {
            List<Long> listsSkuIds = new ArrayList<>();
            List<Long> listsPositionIds = new ArrayList<>();
            Integer numberCount = 0;
            Integer receivedCount = 0;
            for (ProductionPickListsDetailResult detailResult : detailResults) {
                listsSkuIds.add(detailResult.getSkuId());
                if (detailResult.getPickListsId().equals(result.getPickListsId())) {
                    numberCount += detailResult.getNumber();
                    receivedCount += detailResult.getReceivedNumber();
                    for (StorehousePositionsBind positionsBind : positionsBinds) {
                        if (detailResult.getSkuId().equals(positionsBind.getSkuId())) {
                            listsPositionIds.add(positionsBind.getPositionId());
                        }
                    }
                }
            }
            result.setSkuCount(listsSkuIds.stream().distinct().collect(Collectors.toList()).size());
            result.setPositionCount(listsPositionIds.stream().distinct().collect(Collectors.toList()).size());
            result.setNumberCount(numberCount);
            result.setReceivedCount(receivedCount);
        }


    }

    @Override
    public void format(List<ProductionPickListsResult> results) {
        List<Long> pickListsIds = new ArrayList<>();
        List<Long> productionTaskIds = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        List<Long> announcementsIds = new ArrayList<>();
        for (ProductionPickListsResult result : results) {
            if (ToolUtil.isNotEmpty(result.getEnclosure())) {
                List<Long> enclosureIds = Arrays.asList(result.getEnclosure().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                List<String> enclosureUrl = new ArrayList<>();
                for (Long enclosureId : enclosureIds) {
                    String mediaUrl = mediaService.getMediaUrl(enclosureId, 1L);
                    enclosureUrl.add(mediaUrl);
                }
                result.setEnclosureUrl(enclosureUrl);
            }
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
        List<UserResult> userResultsByIds = userIds.size() == 0 ? new ArrayList<>() : userService.getUserResultsByIds(userIds);
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
        List<SkuSimpleResult> skuSimpleResults = skuIds.size() == 0 ? new ArrayList<>() : skuService.simpleFormatSkuResult(skuIds);
        List<Long> shipSetpIds = new ArrayList<>();


        for (ProductionPickListsResult result : results) {


            for (UserResult userResultsById : userResultsByIds) {
                if (ToolUtil.isNotEmpty(result.getUserId()) && result.getUserId().equals(userResultsById.getUserId())) {
                    result.setUserResult(userResultsById);
                }
                if (result.getCreateUser().equals(userResultsById.getUserId())) {
                    result.setCreateUserResult(userResultsById);
                }
            }
            List<ProductionPickListsDetailResult> detailResultList = new ArrayList<>();
            List<SkuSimpleResult> skuResult = new ArrayList<>();
            for (ProductionPickListsDetailResult detailResult : detailResults) {
                if (result.getPickListsId().equals(detailResult.getPickListsId())) {
                    detailResult.setUserResult(result.getUserResult());
                    detailResult.setPickListsCoding(result.getCoding());
                    detailResultList.add(detailResult);
                    for (SkuSimpleResult skuSimpleResult : skuSimpleResults) {
                        if (detailResult.getSkuId().equals(skuSimpleResult.getSkuId())) {
                            skuResult.add(skuSimpleResult);
                        }
                    }
                }
            }
            result.setSkuResults(skuResult);
            result.setDetailResults(detailResultList);
            if (ToolUtil.isNotEmpty(result.getRemarks())) {
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
        wxCpSendTemplate.sendMarkDownTemplate(new MarkDownTemplate() {{
            setItems("领料通知");
            setUrl(mobileService.getMobileConfig().getUrl() + "/#/Work/MyPicking");
            setDescription("库管那里有新的物料待领取");
            setFunction(MarkDownTemplateTypeEnum.pickSend);
            setType(0);
            setUserIds(Arrays.asList(param.getUserIds().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList()));
        }});
        if (ToolUtil.isNotEmpty(param.getPickListsIds())) {
            for (Long pickListsId : param.getPickListsIds()) {
                shopCartService.addDynamic(pickListsId, "通知领料人进行领料");
            }
        }
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

    /**
     * 库存预警
     */
    @Override
    public void warning(ProductionPickListsParam param) {

        List<StockSkuBrand> stockSkuBrands = stockDetailsService.stockSkuBrands();
        if (stockSkuBrands.size() == 0) {
            throw new ServiceException(500, "库存数不足");
        }

        //申请的物料跟库存数比较  并更新库存
        for (ProductionPickListsDetailParam detailParam : param.getPickListsDetailParams()) {
            updateStock(detailParam, stockSkuBrands);
        }

        Map<Integer, List<ActivitiProcessTask>> map = this.unExecuted(null);
        List<ActivitiProcessTask> executed = map.get(99);  // 到执行节点的
        List<ActivitiProcessTask> unExecuted = map.get(50);  //未到执行节点的


        List<Long> pickListsIds = new ArrayList<>();
        for (ActivitiProcessTask processTask : executed) {
            pickListsIds.add(processTask.getFormId());
        }
        for (ActivitiProcessTask processTask : unExecuted) {
            pickListsIds.add(processTask.getFormId());
        }

        List<ProductionPickListsDetail> pickListsDetails = pickListsIds.size() == 0 ? new ArrayList<>() : pickListsDetailService.query().in("pick_lists_id", pickListsIds)
                .eq("display", 1)
                .eq("status", 0).list();

        List<ProductionPickListsDetailParam> pickListsDetailParams = BeanUtil.copyToList(pickListsDetails, ProductionPickListsDetailParam.class, new CopyOptions());

        for (ProductionPickListsDetailParam listsParam : pickListsDetailParams) {
            if (updateStock(listsParam, stockSkuBrands)) {
                //TODO  此处调用库存预警

//                wxCpSendTemplate.sendMarkDownTemplate(new MarkDownTemplate(){{
//                    setTitle("库存不足预警");
//                    setDescription("库存数量已不满足出库申请");
//
//                }});
            }
        }
    }

    /**
     * 更新库存
     *
     * @param detailParam
     * @param stockSkuBrands
     */
    @Override
    public boolean updateStock(ProductionPickListsDetailParam detailParam, List<StockSkuBrand> stockSkuBrands) {
        if (ToolUtil.isEmpty(stockSkuBrands)) {
            return true;
        }
        boolean f = false;

        /**
         * 比对库存数量
         */
        for (StockSkuBrand stockSkuBrand : stockSkuBrands) {
            if (ToolUtil.isNotEmpty(detailParam.getBrandId()) && detailParam.getBrandId().equals(stockSkuBrand.getBrandId()) && detailParam.getSkuId().equals(stockSkuBrand.getSkuId())) {  //指定品牌
                long number = stockSkuBrand.getNumber() - detailParam.getNumber();
                if (number < 0) {
                    f = true;
                }
                stockSkuBrand.setNumber(number);
                break;
            } else if ((ToolUtil.isEmpty(detailParam.getBrandId()) || detailParam.getBrandId() == 0) && detailParam.getSkuId().equals(stockSkuBrand.getSkuId())) {  //不指定品牌
                long number = stockSkuBrand.getNumber() - detailParam.getNumber();
                if (number < 0) {
                    f = true;
                }
                stockSkuBrand.setNumber(number);

            }
        }

        if (f) {
            return true;
        }
        return false;
    }

    @Override
    public List<StorehouseResult> getStockSkus(List<Long> skuIds) {
        return null;
    }


    /**
     * 所有未执行的出库单
     */
    @Override
    public Map<Integer, List<ActivitiProcessTask>> unExecuted(Long taskId) {

        Map<Integer, List<ActivitiProcessTask>> map = new HashMap<>();
        List<ActivitiProcessTask> executed = new ArrayList<>();
        List<ActivitiProcessTask> unExecuted = new ArrayList<>();
        /**
         * 先取出领料未完成的任务
         */
        List<ActivitiProcessTask> activitiProcessTasks = activitiProcessTaskService.query().select("process_task_id AS processTaskId").eq("type", "OUTSTOCK").eq("display", 1).ne("status", 99).list();

        List<Long> taskIds = new ArrayList<>();
        for (ActivitiProcessTask activitiProcessTask : activitiProcessTasks) {
            taskIds.add(activitiProcessTask.getProcessTaskId());
        }
        /**
         * 取出任务的log  组合map
         */
        List<ActivitiProcessTask> processTasks = taskIds.size() == 0 ? new ArrayList<>() : activitiProcessTaskService.listByIds(taskIds);
        List<ActivitiProcessLog> processLogs = taskIds.size() == 0 ? new ArrayList<>() : processLogService.query().in("task_id", taskIds).list();

        Map<Long, List<ActivitiProcessLog>> logMap = new HashMap<>();
        Map<Long, List<ActivitiSteps>> stepMaps = new HashMap<>();
        Set<Long> processIds = new HashSet<>();

        for (ActivitiProcessLog processLog : processLogs) {
            processIds.add(processLog.getPeocessId());

            List<ActivitiProcessLog> logs = logMap.get(processLog.getTaskId());
            if (ToolUtil.isEmpty(logs)) {
                logs = new ArrayList<>();
            }
            logs.add(processLog);
            logMap.put(processLog.getTaskId(), logs);
        }
        /**
         * 通过流程取出结构  组合成map
         */
        List<ActivitiSteps> activitiSteps = processIds.size() == 0 ? new ArrayList<>() : stepsService.query().in("process_id", processIds).list();
        for (ActivitiSteps activitiStep : activitiSteps) {
            List<ActivitiSteps> steps = stepMaps.get(activitiStep.getProcessId());
            if (ToolUtil.isEmpty(steps)) {
                steps = new ArrayList<>();
            }
            steps.add(activitiStep);
            stepMaps.put(activitiStep.getProcessId(), steps);
        }


        if (ToolUtil.isNotEmpty(processTasks) && ToolUtil.isNotEmpty(taskId)) {
            processTasks.removeIf(i -> i.getProcessTaskId().equals(taskId));  //排除自己
        }

        /**
         * 比对结构
         */
        for (ActivitiProcessTask processTask : processTasks) {
            boolean judgeNode = judgeNode(logMap.get(processTask.getProcessTaskId()), stepMaps);
            if (judgeNode) {
                executed.add(processTask);
            } else {
                unExecuted.add(processTask);
            }
        }
        map.put(99, executed);            //执行节点的领料任务
        map.put(50, unExecuted);          //未到执行节点的领料任务

        return map;
    }

    /**
     * 通过任务判断节点
     */
    private boolean judgeNode(List<ActivitiProcessLog> processLogs, Map<Long, List<ActivitiSteps>> stepMaps) {

        boolean t = false;

        /**
         * 取出当前执行节点
         */
        for (ActivitiProcessLog processLog : processLogs) {
            if (ToolUtil.isNotEmpty(processLog.getActionStatus())) {     //找出配置动作的节点
                List<ActionStatus> statuses = JSON.parseArray(processLog.getActionStatus(), ActionStatus.class);
                for (ActionStatus status : statuses) {
                    if (status.getAction().equals(OutStockActionEnum.outStock.name())) {   //找出执行节点
                        if (isExecution(processLog, processLogs, stepMaps.get(processLog.getPeocessId()))) {
                            t = true;
                            break;
                        }
                    }
                }
                if (t) {
                    break;
                }
            }
        }

        return t;
    }

    /**
     * 判断当前是否到执行节点
     */
    private boolean isExecution(ActivitiProcessLog processLog, List<ActivitiProcessLog> processLogs, List<ActivitiSteps> activitiSteps) {

        ActivitiSteps parent = null;
        for (ActivitiSteps activitiStep : activitiSteps) {
            if (processLog.getSetpsId().equals(activitiStep.getSetpsId())) {
                parent = findParent(activitiStep, activitiSteps);     //判断执行节点 上一级log状态
            }
        }
        for (ActivitiProcessLog log : processLogs) {     //上级节点log 通过 说明当前已经到执行节点
            if (ToolUtil.isNotEmpty(parent) && log.getSetpsId().equals(parent.getSetpsId()) && log.getStatus() == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 找上级节点
     *
     * @param step
     * @param activitiSteps
     * @return
     */
    private ActivitiSteps findParent(ActivitiSteps step, List<ActivitiSteps> activitiSteps) {

        ActivitiSteps steps = null;

        for (ActivitiSteps activitiStep : activitiSteps) {
            if (step.getSupper().equals(activitiStep.getSetpsId())) {
                if (activitiStep.getType().toString().equals("4") || activitiStep.getType().toString().equals("3")) {   //上级如果是路由或者分支 在找上级
                    steps = findParent(activitiStep, activitiSteps);
                } else {
                    steps = activitiStep;
                }
                break;
            }
        }
        return steps;

    }


//    @Override
//    public String outStock(ProductionPickListsParam param) {
//        List<Long> stockIds = new ArrayList<>();
//        List<Long> pickListsIds = new ArrayList<>();
//        for (ProductionPickListsCartParam pickListsCartParam : param.getCartsParams()) {
//            pickListsIds.add(pickListsCartParam.getPickListsId());
//        }
//        /**
//         * 取出购物车数据
//         */
//        List<ProductionPickLists> pickLists = pickListsIds.size() == 0 ? new ArrayList<>() : this.query().in("pick_lists_id", pickListsIds).list();
//
//
//        /**
//         * 查询购物车
//         */
//        List<ProductionPickListsCart> listsCarts = pickListsCartService.query().in("pick_lists_id", pickListsIds).eq("display", 1).eq("status", 0).list();
//        for (ProductionPickListsCart listsCart : listsCarts) {
//            stockIds.add(listsCart.getStorehouseId());
//        }
//        /**
//         * true 部分领料 标记部分领料购物车状态  或 拆分购物车标记状态
//         * false 全部领料  走默认出库流程
//         */
//        for (Long pickListsId : pickListsIds) {
//            shopCartService.addDynamic(pickListsId, "领取了物料");
//        }
//        if (this.createAllOrPart(listsCarts, param.getCartsParams())) {
//            this.allForOut(pickLists, listsCarts, param.getCartsParams());
//            return null;
//
//        } else {
//            return this.partForOut(listsCarts, param.getCartsParams());
//        }
//
//
//    }


    @Override
    public String outStock(ProductionPickListsParam param) {
        List<Long> stockIds = new ArrayList<>();
        List<Long> pickListsIds = new ArrayList<>();
        for (ProductionPickListsCartParam pickListsCartParam : param.getCartsParams()) {
            pickListsIds.add(pickListsCartParam.getPickListsId());
        }
        /**
         * 取出购物车数据
         */
        List<ProductionPickLists> pickLists = pickListsIds.size() == 0 ? new ArrayList<>() : this.query().in("pick_lists_id", pickListsIds).list();


        /**
         * 查询购物车
         */
        List<ProductionPickListsCart> listsCarts = pickListsCartService.query().in("pick_lists_id", pickListsIds).eq("display", 1).eq("status", 0).list();
        for (ProductionPickListsCart listsCart : listsCarts) {
            stockIds.add(listsCart.getStorehouseId());
        }


//        /**
//         * 拦截判断
//         */
//        List<Long> userIds = new ArrayList<>();
//        for (ProductionPickLists pickList : pickLists) {
//            userIds.add(pickList.getUserId());
//        }
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
        /**
         * 取出出库申请子表 未完成状态数据
         * 出库数据处理会更新此状态
         * 如子表全部数据 更新状态为完成  主表在最后更新数据
         */
        List<ProductionPickListsDetail> pickListsDetails = pickListsIds.size() == 0 ? new ArrayList<>() : pickListsDetailService.query().in("pick_lists_id", pickListsIds).eq("display", 1).eq("status", 0).list();
        for (ProductionPickListsCartParam pickListsCartParam : param.getCartsParams()) {


            /**
             * 判读申请中没有指定品牌的数据
             * 将申请单详情中不包含的品牌拿出来
             */
            for (ProductionPickListsCart listsCart : listsCarts) {
                if (ToolUtil.isEmpty(listsCart.getBrandId())) {
                    listsCart.setBrandId(0L);
                }
                if (pickListsCartParam.getNumber() > 0) {
                    /**
                     * 处理出库数量对应购物车
                     * 如部分出库 数量与购物车不符 会拆分购物车
                     * 原购物车  更改剩余领料数量
                     * 创建新购物车记录  存放出库数量
                     */
                    if (listsCart.getSkuId().equals(pickListsCartParam.getSkuId()) && listsCart.getBrandId().equals(pickListsCartParam.getBrandId()) && listsCart.getStatus() == 0 && listsCart.getStorehouseId().equals(pickListsCartParam.getStorehouseId())) {
                        int num = pickListsCartParam.getNumber();
                        int lastNum = num;
                        num -= listsCart.getNumber();
                        if (num >= 0) {
                            listsCart.setDisplay(0);
                            listsCart.setStatus(99);
                            pickListsCartParam.setNumber(pickListsCartParam.getNumber() - listsCart.getNumber());
                            for (ProductionPickListsDetail pickListsDetail : pickListsDetails) {
                                if (listsCart.getPickListsDetailId().equals(pickListsDetail.getPickListsDetailId())) {
                                    if (ToolUtil.isNotEmpty(pickListsDetail.getReceivedNumber())) {
                                        pickListsDetail.setReceivedNumber(pickListsDetail.getReceivedNumber() + listsCart.getNumber());
                                    } else {
                                        pickListsDetail.setReceivedNumber(lastNum);
                                    }
                                    if (Objects.equals(pickListsDetail.getNumber(), pickListsDetail.getReceivedNumber())) {
                                        pickListsDetail.setStatus(99);
                                    }
                                }
                            }
                        } else {
                            listsCart.setNumber(listsCart.getNumber() - lastNum);
                            ProductionPickListsCart newCart = new ProductionPickListsCart();
                            ToolUtil.copyProperties(listsCart, newCart);
                            newCart.setPickListsCart(null);
                            newCart.setStatus(99);
                            newCart.setDisplay(0);
                            newCart.setNumber(lastNum);
                            newCarts.add(newCart);
                            for (ProductionPickListsDetail pickListsDetail : pickListsDetails) {
                                if (listsCart.getPickListsDetailId().equals(pickListsDetail.getPickListsDetailId())) {
                                    if (ToolUtil.isNotEmpty(pickListsDetail.getReceivedNumber())) {
                                        pickListsDetail.setReceivedNumber(pickListsDetail.getReceivedNumber() + lastNum);
                                    } else {
                                        pickListsDetail.setReceivedNumber(lastNum);
                                    }
                                    if (Objects.equals(pickListsDetail.getNumber(), pickListsDetail.getReceivedNumber())) {
                                        pickListsDetail.setStatus(99);
                                    }
                                }
                            }
                        }
                    }
                }


            }
        }


        Map<Long, Long> oldAndNewInkindIds = new HashMap<>();
        /**
         * 创建入库记录
         */
        stockIds = stockIds.stream().distinct().collect(Collectors.toList());
        for (Long stockId : stockIds) {
            OutstockOrderParam outstockOrder = new OutstockOrderParam();
            outstockOrder.setStorehouseId(stockId);

            outstockOrder.setUserId(LoginContextHolder.getContext().getUserId());
            List<OutstockListingParam> listings = new ArrayList<>();
            /**
             * 如果部分领取涉及到拆分购物车 老购物车保留 出库出新购物车
             */
            List<InstockLogDetail> logDetails = new ArrayList<>();
            for (ProductionPickListsCart listsCart : listsCarts) {
                if (listsCart.getStatus() == 99 && listsCart.getStorehouseId().equals(stockId)) {
                    OutstockListingParam listingParam = new OutstockListingParam();
                    listingParam.setNumber(Long.valueOf(listsCart.getNumber()));
                    listingParam.setSkuId(listsCart.getSkuId());
                    listingParam.setInkindId(listsCart.getInkindId());
                    listingParam.setPositionsId(listsCart.getStorehousePositionsId());
                    if (ToolUtil.isNotEmpty(listsCart.getBrandId()) || listsCart.getBrandId().equals(0L)) {
                        listingParam.setBrandId(listsCart.getBrandId());
                    }
                    InstockLogDetail log = new InstockLogDetail();
                    ToolUtil.copyProperties(listingParam, log);
                    log.setSource("pick_lists");
                    log.setSourceId(listsCart.getPickListsId());
                    logDetails.add(log);
                    listings.add(listingParam);
                }
            }

            for (ProductionPickListsCart listsCart : newCarts) {
                if (listsCart.getStatus() == 99 && listsCart.getStorehouseId().equals(stockId)) {
                    OutstockListingParam listingParam = new OutstockListingParam();
                    listingParam.setNumber(Long.valueOf(listsCart.getNumber()));
                    listingParam.setSkuId(listsCart.getSkuId());
                    listingParam.setPositionsId(listsCart.getStorehousePositionsId());
                    listingParam.setInkindId(listsCart.getInkindId());
                    if (ToolUtil.isNotEmpty(listsCart.getBrandId())) {
                        listingParam.setBrandId(listsCart.getBrandId());
                    }
                    InstockLogDetail log = new InstockLogDetail();
                    ToolUtil.copyProperties(listsCarts, log);
                    log.setSource("pick_lists");
                    log.setSourceId(listsCart.getPickListsId());
                    logDetails.add(log);
                    listings.add(listingParam);
                }
            }
            if (ToolUtil.isNotEmpty(listings) || listings.size() > 0) {
                instockLogDetailService.saveBatch(logDetails);
                outstockOrder.setListingParams(listings);
                outstockOrderService.saveOutStockOrderByPickLists(outstockOrder);
                outstockOrder.setSource("pickLists");
                Map<Long, Long> longLongMap = outstockOrderService.outBoundByLists(listings);
                oldAndNewInkindIds.putAll(longLongMap);
            }
        }

        this.updateBatchById(pickLists);
        for (ProductionPickListsCart listsCart : newCarts) {
            listsCart.setInkindId(oldAndNewInkindIds.get(listsCart.getInkindId()));
        }
        //如果购物车有数量拆分情况  则新增一条购物车
        pickListsCartService.updateBatchById(listsCarts);

        if (newCarts.size() > 0) {
            pickListsCartService.saveBatch(newCarts);
        }
        pickListsDetailService.updateBatchById(pickListsDetails);
        /**
         * 更新领料单状态
         * 如有任务 则更新任务
         */
        checkListsStatus(pickLists);
        return null;
    }


    private void allForOut(List<ProductionPickLists> pickLists, List<ProductionPickListsCart> listsCarts, List<ProductionPickListsCartParam> cartParams) {
        List<Long> stockIds = new ArrayList<>();
        List<Long> pickListsIds = new ArrayList<>();
        for (ProductionPickListsCartParam pickListsCartParam : cartParams) {
            pickListsIds.add(pickListsCartParam.getPickListsId());
        }

        List<ProductionPickListsCart> newCarts = new ArrayList<>();
        /**
         * 取出出库申请子表 未完成状态数据
         * 出库数据处理会更新此状态
         * 如子表全部数据 更新状态为完成  主表在最后更新数据
         */
        List<ProductionPickListsDetail> pickListsDetails = pickListsIds.size() == 0 ? new ArrayList<>() : pickListsDetailService.query().in("pick_lists_id", pickListsIds).eq("display", 1).eq("status", 0).list();
        for (ProductionPickListsCartParam pickListsCartParam : cartParams) {

            for (Long brandId : pickListsCartParam.getBrandIds()) {
                /**
                 * 判读申请中没有指定品牌的数据
                 * 将申请单详情中不包含的品牌拿出来
                 */
                for (ProductionPickListsCart listsCart : listsCarts) {
                    stockIds.add(listsCart.getStorehouseId());
                    if (pickListsCartParam.getNumber() > 0) {
                        /**
                         * 处理出库数量对应购物车
                         * 如部分出库 数量与购物车不符 会拆分购物车
                         * 原购物车  更改剩余领料数量
                         * 创建新购物车记录  存放出库数量
                         */
                        if (listsCart.getSkuId().equals(pickListsCartParam.getSkuId()) && listsCart.getBrandId().equals(brandId) && listsCart.getStatus() == 0) {
                            int lastNum = pickListsCartParam.getNumber();
//                            int lastNum = num;
//                            num -= listsCart.getNumber();
                            if (pickListsCartParam.getNumber() >= 0) {
                                listsCart.setDisplay(0);
                                listsCart.setStatus(99);
                                pickListsCartParam.setNumber(pickListsCartParam.getNumber() - listsCart.getNumber());
                                for (ProductionPickListsDetail pickListsDetail : pickListsDetails) {
                                    if (listsCart.getPickListsDetailId().equals(pickListsDetail.getPickListsDetailId())) {
                                        if (ToolUtil.isNotEmpty(pickListsDetail.getReceivedNumber())) {
                                            pickListsDetail.setReceivedNumber(pickListsDetail.getReceivedNumber() + listsCart.getNumber());
                                        } else {
                                            pickListsDetail.setReceivedNumber(lastNum);
                                        }
                                        if (Objects.equals(pickListsDetail.getNumber(), pickListsDetail.getReceivedNumber())) {
                                            pickListsDetail.setStatus(99);
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }


        Map<Long, Long> oldAndNewInkindIds = new HashMap<>();
        /**
         * 创建入库记录
         */
        stockIds = stockIds.stream().distinct().collect(Collectors.toList());
        for (Long stockId : stockIds) {
            OutstockOrderParam outstockOrder = new OutstockOrderParam();
            outstockOrder.setStorehouseId(stockId);

            outstockOrder.setUserId(LoginContextHolder.getContext().getUserId());
            List<OutstockListingParam> listings = new ArrayList<>();
            /**
             * 如果部分领取涉及到拆分购物车 老购物车保留 出库出新购物车
             */
            List<InstockLogDetail> logDetails = new ArrayList<>();
            for (ProductionPickListsCart listsCart : listsCarts) {
                if (listsCart.getStatus() == 99 && listsCart.getStorehouseId().equals(stockId)) {
                    OutstockListingParam listingParam = new OutstockListingParam();
                    listingParam.setNumber(Long.valueOf(listsCart.getNumber()));
                    listingParam.setSkuId(listsCart.getSkuId());
                    listingParam.setInkindId(listsCart.getInkindId());
                    listingParam.setPositionsId(listsCart.getStorehousePositionsId());
                    if (ToolUtil.isNotEmpty(listsCart.getBrandId()) || listsCart.getBrandId().equals(0L)) {
                        listingParam.setBrandId(listsCart.getBrandId());
                    }
                    InstockLogDetail log = new InstockLogDetail();
                    ToolUtil.copyProperties(listingParam, log);
                    log.setSource("pick_lists");
                    log.setSourceId(listsCart.getPickListsId());
                    logDetails.add(log);
                    listings.add(listingParam);
                }
            }

            for (ProductionPickListsCart listsCart : newCarts) {
                if (listsCart.getStatus() == 99 && listsCart.getStorehouseId().equals(stockId)) {
                    OutstockListingParam listingParam = new OutstockListingParam();
                    listingParam.setNumber(Long.valueOf(listsCart.getNumber()));
                    listingParam.setSkuId(listsCart.getSkuId());
                    listingParam.setPositionsId(listsCart.getStorehousePositionsId());
                    listingParam.setInkindId(listsCart.getInkindId());
                    if (ToolUtil.isNotEmpty(listsCart.getBrandId())) {
                        listingParam.setBrandId(listsCart.getBrandId());
                    }
                    InstockLogDetail log = new InstockLogDetail();
                    ToolUtil.copyProperties(listsCart, log);
                    log.setSource("pick_lists");
                    log.setSourceId(listsCart.getPickListsId());
                    logDetails.add(log);
                    listings.add(listingParam);
                }
            }

            if (ToolUtil.isNotEmpty(listings) || listings.size() > 0) {
                instockLogDetailService.saveBatch(logDetails);
                outstockOrder.setListingParams(listings);
                outstockOrderService.saveOutStockOrderByPickLists(outstockOrder);
                outstockOrder.setSource("pickLists");
                Map<Long, Long> longLongMap = outstockOrderService.outBoundByLists(listings);
                oldAndNewInkindIds.putAll(longLongMap);
            }
        }

        this.updateBatchById(pickLists);
        for (ProductionPickListsCart listsCart : newCarts) {
            listsCart.setInkindId(oldAndNewInkindIds.get(listsCart.getInkindId()));
        }
        //如果购物车有数量拆分情况  则新增一条购物车
        pickListsCartService.updateBatchById(listsCarts);

        if (newCarts.size() > 0) {
            pickListsCartService.saveBatch(newCarts);
        }
        pickListsDetailService.updateBatchById(pickListsDetails);
        /**
         * 更新领料单状态
         * 如有任务 则更新任务
         */
        checkListsStatus(pickLists);

    }

    /**
     * 部分领料
     *
     * @param listsCarts
     * @param
     * @return
     */
    private String partForOut(List<ProductionPickListsCart> listsCarts, List<ProductionPickListsCartParam> params) {
        List<ProductionPickListsCart> newCarts = new ArrayList<>();
        for (ProductionPickListsCartParam pickListsCartParam : params) {
            for (Long brandId : pickListsCartParam.getBrandIds()) {
                /**
                 * 判读申请中没有指定品牌的数据
                 * 将申请单详情中不包含的品牌拿出来
                 */
                for (ProductionPickListsCart listsCart : listsCarts) {
                    if (pickListsCartParam.getNumber() > 0) {
                        /**
                         * 处理出库数量对应购物车
                         * 如部分出库 数量与购物车不符 会拆分购物车
                         * 原购物车  更改剩余领料数量
                         * 创建新购物车记录  存放出库数量
                         */
                        if (listsCart.getSkuId().equals(pickListsCartParam.getSkuId()) && listsCart.getBrandId().equals(brandId) && listsCart.getStatus() == 0) {
                            int lastNum = pickListsCartParam.getNumber();
//                            int lastNum = num;
//                            num -= listsCart.getNumber();
                            if (pickListsCartParam.getNumber() >= 0) {
                                listsCart.setStatus(2);
                                pickListsCartParam.setNumber(pickListsCartParam.getNumber() - listsCart.getNumber());

                            } else {
                                listsCart.setNumber(listsCart.getNumber() - lastNum);
                                ProductionPickListsCart newCart = new ProductionPickListsCart();
                                ToolUtil.copyProperties(listsCart, newCart);
                                newCart.setPickListsCart(null);
                                newCart.setStatus(2);
                                newCart.setNumber(lastNum);
                                newCarts.add(newCart);
                            }
                        }
                    }
                }
            }
        }
        pickListsCartService.updateBatchById(listsCarts);
        pickListsCartService.saveBatch(newCarts);
        /**
         * 赋予领料码
         */
        Long code = 0L;
        ProductionPickCode productionPickCode = pickCodeService.query().eq("display", 1).eq("create_user", LoginContextHolder.getContext().getUserId()).one();
        if (ToolUtil.isNotEmpty(productionPickCode)) {
            return productionPickCode.getCode().toString();
        } else {
            code = Long.valueOf(RandomUtil.randomNumbers(4));
            Long finalCode = code;
            pickCodeService.save(new ProductionPickCode() {{
                setCode(finalCode);
            }});
            return code.toString();
        }
    }

    /**
     * 全部领料
     *
     * @param carts
     * @param params
     * @return
     */
    Boolean createAllOrPart(List<ProductionPickListsCart> carts, List<ProductionPickListsCartParam> params) {
        List<ProductionPickListsCart> totalList = new ArrayList<>();
        carts.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId(), Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new ProductionPickListsCart() {{
                        setSkuId(a.getSkuId());
                        setNumber(a.getNumber() + b.getNumber());
                    }}).ifPresent(totalList::add);
                }
        );

        List<ProductionPickListsCartParam> totalParamList = new ArrayList<>();
        params.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId(), Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new ProductionPickListsCartParam() {{
                        setSkuId(a.getSkuId());
                        setNumber(a.getNumber() + b.getNumber());
                    }}).ifPresent(totalParamList::add);
                }
        );

        for (ProductionPickListsCart listsDetail : totalList) {
            for (ProductionPickListsCartParam pickListsCartParam : totalParamList) {
                if (listsDetail.getSkuId().equals(pickListsCartParam.getSkuId()) && !Objects.equals(listsDetail.getNumber(), pickListsCartParam.getNumber())) {
                    return false;
                }
            }
        }
        if (totalList.size() > totalParamList.size()) {
            return false;
        }
        return true;


    }

    @Override
    public void outStockBySku(ProductionPickListsParam param) {
        if (ToolUtil.isEmpty(param.getCartsParams())) {
            throw new ServiceException(500, "请选择您所需要领取的物品");
        }
        List<Long> storehouseIds = new ArrayList<>();
        List<Long> skuIds = new ArrayList<>();
        List<Long> pickListsIds = new ArrayList<>();
        for (ProductionPickListsCartParam cartsParam : param.getCartsParams()) {
            skuIds.add(cartsParam.getSkuId());
            storehouseIds.add(cartsParam.getStorehouseId());
            pickListsIds.add(cartsParam.getPickListsId());
        }
        List<ProductionPickListsCart> pickListsCarts = new ArrayList<>();
        if ((storehouseIds.size() > 0 && skuIds.size() > 0 && pickListsIds.size() > 0)) {
            pickListsCarts = pickListsCartService.query().in("storehouse_id", storehouseIds.stream().distinct().collect(Collectors.toList())).in("sku_id", skuIds.stream().distinct().collect(Collectors.toList())).in("pick_lists_id", pickListsIds.stream().distinct().collect(Collectors.toList())).eq("display", 1).list();
        }
        List<Long> detailIds = new ArrayList<>();
        for (ProductionPickListsCart pickListsCart : pickListsCarts) {
            detailIds.add(pickListsCart.getPickListsDetailId());
        }
        List<ProductionPickListsDetail> listsDetails = detailIds.size() == 0 ? new ArrayList<>() : pickListsDetailService.listByIds(detailIds.stream().distinct().collect(Collectors.toList()));
        for (ProductionPickListsCartParam cartsParam : param.getCartsParams()) {
            for (ProductionPickListsCart pickListsCart : pickListsCarts) {
                if (pickListsCart.getStorehouseId().equals(cartsParam.getStorehouseId()) && pickListsCart.getSkuId().equals(cartsParam.getSkuId()) && pickListsCart.getPickListsId().equals(cartsParam.getPickListsId())) {
                    pickListsCart.setDisplay(0);
                    pickListsCart.setStatus(99);
                    for (ProductionPickListsDetail listsDetail : listsDetails) {
                        if (pickListsCart.getPickListsDetailId().equals(listsDetail.getPickListsDetailId())) {
                            listsDetail.setReceivedNumber(listsDetail.getReceivedNumber() + pickListsCart.getNumber());
                            if (listsDetail.getReceivedNumber() == listsDetail.getNumber()) {
                                listsDetail.setStatus(99);
                            }
                        }
                    }
                }
            }
        }
        pickListsDetailService.updateBatchById(listsDetails);
        pickListsCartService.updateBatchById(pickListsCarts);

        List<ProductionPickListsDetail> pickListsDetails = pickListsDetailService.query().in("pick_lists_id", pickListsIds).eq("display", 1).eq("status", 0).list();
        if (listsDetails.size() == 0) {
            for (Long pickListsId : pickListsIds) {
                this.checkAction(pickListsId);
            }
        } else {
            for (Long pickListsId : pickListsIds) {
                Boolean statusFlag = true;
                for (ProductionPickListsDetail listsDetail : pickListsDetails) {
                    if (pickListsId.equals(listsDetail.getPickListsId())) {
                        statusFlag = false;
                    }
                }
                if (statusFlag) {
                    checkAction(pickListsId);
                }
            }
        }
    }

    public void queryDetailStatusByIds(List<Long> ids) {

    }

    /**
     * 查询判断单据状态是否完成
     *
     * @param pickLists
     */
    private void checkListsStatus(List<ProductionPickLists> pickLists) {

        List<Long> pickListsIds = new ArrayList<>();
        for (ProductionPickLists pickList : pickLists) {
            pickListsIds.add(pickList.getPickListsId());
        }
        List<ProductionPickListsDetail> update = pickListsIds.size() == 0 ? new ArrayList<>() : pickListsDetailService.query().in("pick_lists_id", pickListsIds).ne("status", 99).list();
        if (update.size() == 0) {
            for (ProductionPickLists pickList : pickLists) {
                this.checkAction(pickList.getPickListsId());
            }
        } else {
            for (
                    ProductionPickLists pickList : pickLists) {
                if (update.stream().noneMatch(i -> i.getPickListsId().equals(pickList.getPickListsId()))) {
                    this.checkAction(pickList.getPickListsId());
                }
            }
        }
    }

    @Override
    public ProductionPickListsResult detail(Long id) {
        ProductionPickLists productionPickLists = this.getById(id);
        ProductionPickListsResult result = new ProductionPickListsResult();
        ToolUtil.copyProperties(productionPickLists, result);

        Map<Long, String> statusMap = new HashMap<>();
        List<DocumentsStatus> statuses = statusService.list();
        statusMap.put(0L, "开始");
        statusMap.put(99L, "完成");
        statusMap.put(50L, "拒绝");
        for (DocumentsStatus status : statuses) {
            statusMap.put(status.getDocumentsStatusId(), status.getName());
        }
        String statusName = statusMap.get(result.getStatus());
        result.setStatusName(statusName);

        this.format(new ArrayList<ProductionPickListsResult>() {{
            add(result);
        }});
        List<ProductionPickListsDetailResult> detailResults = pickListsDetailService.findListBySpec(new ProductionPickListsDetailParam() {{
            setPickListsId(id);
            setStatus(0);
        }});
        List<Long> skuIds = new ArrayList<>();
        List<Long> pickListsIds = new ArrayList<>();
        for (ProductionPickListsDetailResult productionPickListsDetailResult : detailResults) {
            pickListsIds.add(productionPickListsDetailResult.getPickListsId());
            skuIds.add(productionPickListsDetailResult.getSkuId());
        }
        /**
         * 取库存信息和品牌
         */
        List<StockDetails> stockDetails = skuIds.size() == 0 ? new ArrayList<>() : stockDetailsService.query().eq("display", 1).eq("stage", 1).in("sku_id", skuIds).list();
        for (StockDetails stockDetail : stockDetails) {
            if (ToolUtil.isEmpty(stockDetail.getBrandId())) {
                stockDetail.setBrandId(0L);
            }
        }
        /**
         * 按照维度对库存进行合并
         */
        List<StockDetails> totalList = new ArrayList<>();
        stockDetails.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + '_' + (ToolUtil.isEmpty(item.getBrandId()) ? 0L : item.getBrandId()) + "_" + item.getStorehousePositionsId(), Collectors.toList())).forEach(
                (itd, transfer) -> {
                    transfer.stream().reduce((a, b) -> new StockDetails() {{
                        setSkuId(a.getSkuId());
                        setNumber(a.getNumber() + b.getNumber());
                        setBrandId(ToolUtil.isEmpty(a.getBrandId()) ? 0L : a.getBrandId());
                        setStorehousePositionsId(a.getStorehousePositionsId());
                    }}).ifPresent(totalList::add);
                }
        );
        List<Long> storeHousePositionIds = new ArrayList<>();
        //取库位id
        for (StockDetails details : totalList) {
            storeHousePositionIds.add(details.getStorehousePositionsId());
        }


        this.pickListsDetailService.format(detailResults);


        result.setDetailResults(detailResults);


        return result;

    }

    @Override
    public void updateStatus(ActivitiProcessTask processTask) {
        /**
         *    更新表单状态
         */
        ProductionPickLists entity = this.getById(processTask.getFormId());
        entity.setStatus(OutStockActionEnum.done.getStatus());
        this.updateById(entity);
    }

    void checkAction(Long id) {


        /**
         * 如果有任务则更新任务单据动作状态
         */
        ActivitiProcessTask task = activitiProcessTaskService.query().eq("type", "OUTSTOCK").eq("form_id", id).one();
        if (ToolUtil.isNotEmpty(task)) {
            DocumentsAction action = actionService.query().eq("action", OutStockActionEnum.outStock.name()).eq("display", 1).one();
            activitiProcessLogService.checkAction(task.getFormId(), task.getType(), action.getDocumentsActionId(), LoginContextHolder.getContext().getUserId());
        }

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


        List<ProductionPickListsCartResult> totalList = new ArrayList<>();
        productionPickListsCartResults.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + '_' + (ToolUtil.isEmpty(item.getBrandId()) ? 0 : item.getBrandId()), Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new ProductionPickListsCartResult() {{
                        setSkuId(a.getSkuId());
                        setNumber(a.getNumber() + b.getNumber());
                        setBrandId(a.getBrandId());
                        setPickListsId(a.getPickListsId());
                        setPickListsDetailId(a.getPickListsDetailId());
                        setPickListsId(a.getPickListsId());
                    }}).ifPresent(totalList::add);
                }
        );
        pickListsCartService.format(totalList);


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
                for (ProductionPickListsCartResult productionPickListsCartResult : totalList) {
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

    @Override
    public void abortCode(String code) {
        ProductionPickCode pickCode = pickCodeService.query().eq("code", code).eq("dispaly", 1).last("limit 1").one();
        if (ToolUtil.isEmpty(pickCode)) {
            throw new ServiceException(500, "未查询到此验证码");
        }
        List<ProductionPickLists> productionPickLists = this.query().eq("user_id", pickCode.getCode()).eq("display", 1).ne("status", 99).list();
        List<Long> listsIds = new ArrayList<>();
        for (ProductionPickLists productionPickList : productionPickLists) {
            listsIds.add(productionPickList.getPickListsId());
        }
        List<ProductionPickListsCart> pickListsCarts = listsIds.size() == 0 ? new ArrayList<>() : pickListsCartService.query().in("pick_lists_id", listsIds).eq("status", 2).eq("display", 1).list();
        for (ProductionPickListsCart pickListsCart : pickListsCarts) {
            pickListsCart.setStatus(0);
        }
        pickListsCartService.updateBatchById(pickListsCarts);
        pickCode.setDisplay(0);
        pickCodeService.updateById(pickCode);

    }



    @Override
    public List<ProductionPickListsCartResult> listByCode(String code) {
        List<Object> list = redisSendCheck.getList(code);
        if (ToolUtil.isEmpty(list)) {
            throw new ServiceException(500, "领料码已失效");
        }
        List<ProductionPickListsCartResult> cartResults = BeanUtil.copyToList(list, ProductionPickListsCartResult.class);
        if (ToolUtil.isNotEmpty(cartResults)) {
            pickListsCartService.format(cartResults);
        }
        return cartResults;
    }

    @Override
    public List<Long> idsList(ProductionPickListsParam param) {
        return this.baseMapper.idsList(param);
    }

    @Override
    public void updateOutStockRefuseStatus(ActivitiProcessTask processTask) {
        ProductionPickLists lists = this.getById(processTask.getFormId());
        lists.setStatus(OutStockActionEnum.refuse.getStatus());
        this.updateById(lists);
    }
}
