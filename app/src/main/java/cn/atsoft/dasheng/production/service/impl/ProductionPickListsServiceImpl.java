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
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.ApplyDetails;
import cn.atsoft.dasheng.erp.entity.StorehousePositions;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsService;
import cn.atsoft.dasheng.form.service.ActivitiSetpSetService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.production.entity.*;
import cn.atsoft.dasheng.production.mapper.ProductionPickListsMapper;
import cn.atsoft.dasheng.production.model.params.ProductionPickListsDetailParam;
import cn.atsoft.dasheng.production.model.params.ProductionPickListsParam;
import cn.atsoft.dasheng.production.model.request.SavePickListsObject;
import cn.atsoft.dasheng.production.model.request.StockSkuTotal;
import cn.atsoft.dasheng.production.model.result.ProductionPickListsDetailResult;
import cn.atsoft.dasheng.production.model.result.ProductionPickListsResult;
import cn.atsoft.dasheng.production.model.result.ProductionTaskResult;
import cn.atsoft.dasheng.production.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
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
    private ProductionWorkOrderService  workOrderService;

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


    @Override
    public void add(ProductionPickListsParam param){
        ProductionPickLists entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ProductionPickListsParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ProductionPickListsParam param){
        ProductionPickLists oldEntity = getOldEntity(param);
        ProductionPickLists newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ProductionPickListsResult findBySpec(ProductionPickListsParam param){
        return null;
    }

    @Override
    public List<ProductionPickListsResult> findListBySpec(ProductionPickListsParam param){
        return null;
    }

    @Override
    public PageInfo<ProductionPickListsResult> findPageBySpec(ProductionPickListsParam param){
        Page<ProductionPickListsResult> pageContext = getPageContext();
        IPage<ProductionPickListsResult> page = this.baseMapper.customPageList(pageContext, param);
        if (ToolUtil.isNotEmpty(page.getRecords())){
            this.format(page.getRecords());
        }
        return PageFactory.createPageInfo(page);
    }
    @Override
    public void format(List<ProductionPickListsResult> results){
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
        List<ProductionPickListsDetailResult> detailResults = pickListsDetailService.findListBySpec(new ProductionPickListsDetailParam());
        List<Long> skuIds = new ArrayList<>();
        for (ProductionPickListsDetailResult detailResult : detailResults) {
            skuIds.add(detailResult.getSkuId());
        }
        List<SkuResult> skuResults = skuIds.size() == 0 ? new ArrayList<>() : skuService.formatSkuResult(skuIds);
        for (ProductionPickListsDetailResult detailResult : detailResults) {
            for (SkuResult skuResult : skuResults) {
                if (skuResult.getSkuId().equals(detailResult.getSkuId())){
                    detailResult.setSkuResult(skuResult);
                    break;
                }
            }
        }




        List<ProductionTaskResult> productionTaskResults = productionTaskIds.size() == 0 ? new ArrayList<>() : productionTaskService.resultsByIds(productionTaskIds);

        for (ProductionPickListsResult result : results) {
            for (ProductionTaskResult productionTaskResult : productionTaskResults) {
                if (result.getSource().equals("productionTask") && result.getSourceId().equals(productionTaskResult.getProductionTaskId())){
                    result.setProductionTaskResult(productionTaskResult);
                }
            }
            List<ProductionPickListsDetailResult> detailResultList = new ArrayList<>();
            for (ProductionPickListsDetailResult detailResult : detailResults) {
                if (result.getPickListsId().equals(detailResult.getPickListsId())){
                    detailResultList.add(detailResult);
                }
            }
            result.setDetailResults(detailResultList);
            for (UserResult userResultsById : userResultsByIds) {
                if (result.getUserId().equals(userResultsById.getUserId())){
                    result.setUserResult(userResultsById);
                }
                if (result.getCreateUser().equals(userResultsById.getUserId())){
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
        productionPickLists.setCode(radomCode);
        productionPickLists.setStatus(97);
        productionPickLists.setSourceId(taskId);
        productionPickLists.setSource("productionTask");
        productionPickLists.setUserId(savePickListsObject.getProductionTask().getUserId());
        this.save(productionPickLists);
        List<Long> skuIds = new ArrayList<>();
        for (ProductionTaskDetail detail : savePickListsObject.getDetails()) {
            skuIds.add(detail.getOutSkuId());
        }
        List<Parts> parts = skuIds.size() == 0 ? new ArrayList<>() : partsService.query().in("sku_id", skuIds).eq("status", 99).list();
        List<Long> partIds = new ArrayList<>();
        List<PartsResult> partsResults = new ArrayList<>();
        for (Parts part : parts) {
            partIds.add(part.getPartsId());
            PartsResult partsResult = new PartsResult();
            ToolUtil.copyProperties(part,partsResult);
            partsResults.add(partsResult);
        }
        List<ErpPartsDetailResult> partsDetailResults = partsDetailService.getDetails(partIds);
        for (PartsResult partsResult : partsResults) {
            List<ErpPartsDetailResult> partsDetailResultList = new ArrayList<>();
            for (ErpPartsDetailResult partsDetailResult : partsDetailResults) {
                if (partsDetailResult.getPartsId().equals(partsResult.getPartsId())){
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
                        productionPickListsDetail.setNumber(detail.getNumber()*part.getNumber());
                        productionPickListsDetail.setSkuId(part.getSkuId());
                        productionPickListsDetail.setPickListsId(productionPickLists.getPickListsId());
                        details.add(productionPickListsDetail);
                    }
                    break;
                }
            }
        }


        pickListsDetailService.saveBatch(details);

        return null;
    }

    public void takePick(ProductionPickListsParam param){
        ProductionPickLists pickLists = this.getById(param.getPickListsId());
        if (!param.getCode().equals(pickLists.getCode())){
            throw new ServiceException(500,"您的验证码与单据不符");
        }
        if (!pickLists.getStatus().equals(98)){
            throw new ServiceException(500,"您的领料单还没有准备好");
        }
        pickLists.setStatus(99);
        this.updateById(pickLists);

    }

    private Serializable getKey(ProductionPickListsParam param){
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
    public List<StorehouseResult> getStockSkus(List<Long> skuIds){
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
        stockDetails.parallelStream().collect(Collectors.groupingBy(item->item.getSkuId()+'_'+item.getStorehousePositionsId(),Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new StockSkuTotal(a.getSkuId(),a.getStorehousePositionsId(), a.getNumber() + b.getNumber(),new StorehousePositionsResult())).ifPresent(totalList::add);
                }
        );

        /**
         * 查找库位
         */
        List<StorehousePositions> storehousePositions = storehousePositionsService.list();
        List<StorehousePositionsResult> storehousePositionsResults = new ArrayList<>();
        for (StorehousePositions storehousePosition : storehousePositions) {
            StorehousePositionsResult storehousePositionsResult = new StorehousePositionsResult();
            ToolUtil.copyProperties(storehousePosition,storehousePositionsResult);
            storehousePositionsResults.add(storehousePositionsResult);
        }

        return  null;
    }

    @Override
    public void outStock(ProductionPickListsParam param){
        List<Long> stockIds = new ArrayList<>();
        List<Long> pickListsIds = new ArrayList<>();
        for (ProductionPickListsDetailParam pickListsDetailParam : param.getPickListsDetailParams()) {
            stockIds.add(pickListsDetailParam.getStorehouseId());
            pickListsIds.add(pickListsDetailParam.getPickListsId());
        }
        List<ProductionPickCode> productionPickCodes = new ArrayList<>();
        for (Long pickListsId : pickListsIds) {
            ProductionPickCode productionPickCode = new ProductionPickCode();
            productionPickCode.setCode(param.getCode());
            productionPickCode.setPickListsId(pickListsId);
            productionPickCodes.add(productionPickCode);
        }
        pickCodeService.saveBatch(productionPickCodes);


        List<ProductionPickLists> pickLists = pickListsIds.size() == 0 ? new ArrayList<>() : this.query().in("pick_lists_id", pickListsIds).list();
        List<ProductionPickListsDetail> pickListsDetails = pickListsIds.size() == 0 ? new ArrayList<>() : pickListsDetailService.query().in("pick_lists_id", pickLists).list();
        for (ProductionPickListsDetail pickListsDetail : pickListsDetails) {
            for (ProductionPickListsDetailParam pickListsDetailParam : param.getPickListsDetailParams()) {
                if (pickListsDetailParam.getPickListsId().equals(pickListsDetail.getPickListsId()) && pickListsDetailParam.getSkuId().equals(pickListsDetail.getSkuId()) && pickListsDetailParam.getNumber().equals(pickListsDetail.getNumber())){
                    pickListsDetail.setStatus(99);
                }
            }
        }
        pickListsDetailService.updateBatchById(pickListsDetails);

        stockIds = stockIds.stream().distinct().collect(Collectors.toList());
        for (Long stockId : stockIds) {
            OutstockOrderParam outstockOrder = new OutstockOrderParam();
            outstockOrder.setStorehouseId(stockId);
            List<ApplyDetails> applyDetails = new ArrayList<>();
            for (ProductionPickListsDetailParam pickListsDetailParam : param.getPickListsDetailParams()) {
                if (pickListsDetailParam.getStorehouseId().equals(stockId)){
                    ApplyDetails applyDetail = new ApplyDetails();
                    applyDetail.setNumber(Long.valueOf(pickListsDetailParam.getNumber()));
                    applyDetail.setSkuId(pickListsDetailParam.getSkuId());
                    applyDetails.add(applyDetail);
                }
            }
            outstockOrder.setApplyDetails(applyDetails);
            outstockOrderService.add(outstockOrder);
        }
    }




}
