package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.model.params.OutstockOrderParam;
import cn.atsoft.dasheng.app.model.result.ErpPartsDetailResult;
import cn.atsoft.dasheng.app.model.result.PartsResult;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.app.service.ErpPartsDetailService;
import cn.atsoft.dasheng.app.service.OutstockOrderService;
import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.config.MobileService;
import cn.atsoft.dasheng.erp.entity.ApplyDetails;
import cn.atsoft.dasheng.erp.entity.StorehousePositions;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.erp.service.CodingRulesService;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsService;
import cn.atsoft.dasheng.form.model.result.ActivitiSetpSetDetailResult;
import cn.atsoft.dasheng.form.service.ActivitiSetpSetDetailService;
import cn.atsoft.dasheng.form.service.ActivitiSetpSetService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.production.entity.*;
import cn.atsoft.dasheng.production.mapper.ProductionPickListsMapper;
import cn.atsoft.dasheng.production.model.params.ProductionPickListsCartParam;
import cn.atsoft.dasheng.production.model.params.ProductionPickListsDetailParam;
import cn.atsoft.dasheng.production.model.params.ProductionPickListsParam;
import cn.atsoft.dasheng.production.model.request.SavePickListsObject;
import cn.atsoft.dasheng.production.model.request.StockSkuTotal;
import cn.atsoft.dasheng.production.model.result.ProductionPickListsDetailResult;
import cn.atsoft.dasheng.production.model.result.ProductionPickListsResult;
import cn.atsoft.dasheng.production.model.result.ProductionTaskResult;
import cn.atsoft.dasheng.production.model.result.ShipSetpResult;
import cn.atsoft.dasheng.production.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.sendTemplate.WxCpTemplate;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.util.RandomUtil;
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


    @Override
    public void add(ProductionPickListsParam param) {
        ProductionPickLists entity = getEntity(param);
        this.save(entity);
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
        return null;
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
        List<ProductionPickListsDetailResult> detailResults = pickListsDetailService.findListBySpec(new ProductionPickListsDetailParam() {{
            setStatus(0);
        }});
        List<Long> skuIds = new ArrayList<>();
        for (ProductionPickListsDetailResult detailResult : detailResults) {
            skuIds.add(detailResult.getSkuId());
        }
        List<SkuResult> skuResults = skuIds.size() == 0 ? new ArrayList<>() : skuService.formatSkuResult(skuIds);
        for (ProductionPickListsDetailResult detailResult : detailResults) {
            for (SkuResult skuResult : skuResults) {
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
                if (result.getSource().equals("productionTask") && result.getSourceId().equals(productionTaskResult.getProductionTaskId())) {
                    for (ShipSetpResult shipSetpResult : shipSetpResults) {
                        if  (productionTaskResult.getShipSetpId().equals(shipSetpResult.getShipSetpId())){
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
                    result.setUserResult(userResultsById);
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
        List<SkuResult> skuResults = skuIds.size() == 0 ? new ArrayList<>() : skuService.formatSkuResult(skuIds);
        for (ProductionPickListsDetailResult detailResult : detailResults) {
            for (SkuResult skuResult : skuResults) {
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
        long radomCode = RandomUtil.randomLong(4);

        ProductionPickLists productionPickLists = new ProductionPickLists();
        productionPickLists.setCoding(codingRulesService.defaultEncoding());
        productionPickLists.setStatus(97);
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
                            productionPickListsDetail.setNumber(detail.getNumber() * part.getNumber());
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
        pickLists.setStatus(99);
        this.updateById(pickLists);

    }

    @Override
    public void sendPersonPick(ProductionPickListsParam param) {
        List<ProductionPickListsCart> carts = new ArrayList<>();
        for (ProductionPickListsCartParam cartsParam : param.getCartsParams()) {
            ProductionPickListsCart cart = new ProductionPickListsCart();
            ToolUtil.copyProperties(cartsParam, cart);
            cart.setStatus(99);
            carts.add(cart);
        }
        pickListsCartService.updateBatchById(carts);
        WxCpTemplate wxCpTemplate = new WxCpTemplate();
        wxCpTemplate.setUrl(mobileService.getMobileConfig().getUrl() + "/cp/#/Work/Production/MyCart");
        wxCpTemplate.setTitle("新的生产任务");
        wxCpTemplate.setDescription("库管那里有新的物料待领取");
        wxCpTemplate.setUserIds(param.getUserIds().stream().distinct().collect(Collectors.toList()));
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
        List<StockDetails> stockSkus = stockDetailsService.query().in("sku_id", skuIds).list();

        List<StockSkuTotal> stockDetails = new ArrayList<>();
        for (StockDetails skus : stockSkus) {
            StockSkuTotal stockSkuTotal = new StockSkuTotal();
            stockSkuTotal.setSkuId(skus.getSkuId());
            stockSkuTotal.setStorehousePositionsId(skus.getStorehousePositionsId());
            stockSkuTotal.setNumber(skus.getNumber());
            stockDetails.add(stockSkuTotal);
        }
        List<StockSkuTotal> totalList = new ArrayList<>();
        stockDetails.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + '_' + item.getStorehousePositionsId(), Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new StockSkuTotal(a.getSkuId(), a.getStorehousePositionsId(), a.getNumber() + b.getNumber(), new StorehousePositionsResult())).ifPresent(totalList::add);
                }
        );

        /**
         * 查找库位
         */
        List<StorehousePositions> storehousePositions = storehousePositionsService.list();
        List<StorehousePositionsResult> storehousePositionsResults = new ArrayList<>();
        for (StorehousePositions storehousePosition : storehousePositions) {
            StorehousePositionsResult storehousePositionsResult = new StorehousePositionsResult();
            ToolUtil.copyProperties(storehousePosition, storehousePositionsResult);
            storehousePositionsResults.add(storehousePositionsResult);
        }

        return null;
    }

    @Override
    public void outStock(ProductionPickListsParam param) {
        List<Long> stockIds = new ArrayList<>();
        List<Long> pickListsIds = new ArrayList<>();
        List<Long> pickListsCartIds = new ArrayList<>();
        for (ProductionPickListsDetailParam pickListsDetailParam : param.getPickListsDetailParams()) {
            stockIds.add(pickListsDetailParam.getStorehouseId());
            pickListsIds.add(pickListsDetailParam.getPickListsId());
            if (ToolUtil.isNotEmpty(pickListsDetailParam.getPickListsCart())) {
                pickListsCartIds.add(pickListsDetailParam.getPickListsCart());
            }
        }


        /**
         * 更新子表记录
         */
        List<ProductionPickLists> pickLists = pickListsIds.size() == 0 ? new ArrayList<>() : this.query().in("pick_lists_id", pickListsIds).list();
        List<Long> userIds = new ArrayList<>();
        for (ProductionPickLists pickList : pickLists) {
            userIds.add(pickList.getUserId());
        }
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

        List<ProductionPickListsDetail> pickListsDetails = pickListsIds.size() == 0 ? new ArrayList<>() : pickListsDetailService.query().in("pick_lists_id", pickListsIds).list();
        for (ProductionPickListsDetail pickListsDetail : pickListsDetails) {
            for (ProductionPickListsDetailParam pickListsDetailParam : param.getPickListsDetailParams()) {
                if (pickListsDetailParam.getPickListsId().equals(pickListsDetail.getPickListsId()) && pickListsDetailParam.getSkuId().equals(pickListsDetail.getSkuId()) && pickListsDetailParam.getNumber().equals(pickListsDetail.getNumber())) {
                    pickListsDetail.setStatus(99);
                }
            }
        }

        pickListsDetailService.updateBatchById(pickListsDetails);
        /**
         * 更新领料单状态
         */
        List<ProductionPickListsDetail> update = pickListsIds.size() == 0 ? new ArrayList<>() : pickListsDetailService.query().in("pick_lists_id", pickListsIds).ne("status", 99).list();
        for (ProductionPickLists pickList : pickLists) {
            if (update.stream().noneMatch(i -> i.getPickListsId().equals(pickList.getPickListsId()))) {
                pickList.setStatus(99);
            }
        }
        this.updateBatchById(pickLists);
        /**
         * 创建入库记录
         */
        stockIds = stockIds.stream().distinct().collect(Collectors.toList());
        for (Long stockId : stockIds) {
            OutstockOrderParam outstockOrder = new OutstockOrderParam();
            outstockOrder.setStorehouseId(stockId);

            outstockOrder.setUserId(LoginContextHolder.getContext().getUserId());
            List<ApplyDetails> applyDetails = new ArrayList<>();
            for (ProductionPickListsDetailParam pickListsDetailParam : param.getPickListsDetailParams()) {
                if (pickListsDetailParam.getStorehouseId().equals(stockId)) {
                    ApplyDetails applyDetail = new ApplyDetails();
                    applyDetail.setNumber(Long.valueOf(pickListsDetailParam.getNumber()));
                    applyDetail.setSkuId(pickListsDetailParam.getSkuId());
                    applyDetails.add(applyDetail);
                }
            }
            outstockOrder.setApplyDetails(applyDetails);
            outstockOrderService.saveOutStockOrderByPickLists(outstockOrder);
            pickListsCartService.removeByIds(pickListsCartIds);
        }
    }


}
