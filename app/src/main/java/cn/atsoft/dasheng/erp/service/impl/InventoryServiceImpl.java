package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.action.Enum.ReceiptsEnum;
import cn.atsoft.dasheng.action.Enum.StocktakingEnum;
import cn.atsoft.dasheng.app.entity.ErpPartsDetail;
import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.StockDetailsResult;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.appBase.service.MediaService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.config.MobileService;
import cn.atsoft.dasheng.erp.entity.Inkind;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.model.params.*;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.pojo.AnomalyType;
import cn.atsoft.dasheng.erp.pojo.SkuBind;
import cn.atsoft.dasheng.erp.pojo.SkuBindParam;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.model.params.RemarksParam;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessTaskResult;
import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.erp.mapper.InventoryMapper;
import cn.atsoft.dasheng.erp.pojo.InventoryRequest;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.message.enmu.AuditMessageType;
import cn.atsoft.dasheng.message.enmu.OperationType;
import cn.atsoft.dasheng.message.entity.AuditEntity;
import cn.atsoft.dasheng.message.entity.RemarksEntity;
import cn.atsoft.dasheng.message.producer.MessageProducer;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.orCode.entity.OrCode;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.model.params.OrCodeBindParam;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.orCode.service.OrCodeService;
import cn.atsoft.dasheng.production.entity.ProductionPickListsCart;
import cn.atsoft.dasheng.production.service.ProductionPickListsCartService;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.bouncycastle.tsp.TSPUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.*;

import static com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isNull;
import static java.util.stream.Collectors.toList;

/**
 * <p>
 * 盘点任务主表 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-27
 */
@Service
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory> implements InventoryService {

    @Autowired
    private InkindService inkindService;
    @Autowired
    private StockDetailsService detailsService;
    @Autowired
    private StorehousePositionsService positionsService;
    @Autowired
    private StockService stockService;
    @Autowired
    private OrCodeBindService codeBindService;
    @Autowired
    private InventoryDetailService inventoryDetailService;
    @Autowired
    private InstockOrderService instockOrderService;
    @Autowired
    private OutstockOrderService outstockOrderService;
    @Autowired
    private ActivitiProcessService activitiProcessService;
    @Autowired
    private ActivitiProcessTaskService activitiProcessTaskService;
    @Autowired
    private ActivitiProcessLogService activitiProcessLogService;
    @Autowired
    private WxCpSendTemplate wxCpSendTemplate;
    @Autowired
    private MessageProducer messageProducer;
    @Autowired
    private RemarksService remarksService;
    @Autowired
    private MobileService mobileService;
    @Autowired
    private CodingRulesService codingRulesService;
    @Autowired
    private StockDetailsService stockDetailsService;
    @Autowired
    private ShopCartService shopCartService;
    @Autowired
    private PartsService partsService;
    @Autowired
    private ErpPartsDetailService partsDetailService;
    @Autowired
    private StorehousePositionsService storehousePositionsService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private AnomalyOrderService anomalyOrderService;
    @Autowired
    private UserService userService;
    @Autowired
    private InventoryStockService inventoryStockService;
    @Autowired
    private StepsService stepsService;
    @Autowired
    private AnnouncementsService announcementsService;
    @Autowired
    private MediaService mediaService;
    @Autowired
    private OrCodeService orCodeService;
    @Autowired
    private OrCodeBindService orCodeBindService;
    @Autowired
    private ProductionPickListsCartService listsCartService;
    @Autowired
    private TaskParticipantService taskParticipantService;
    @Autowired
    private AnomalyService anomalyService;

    @Override
    @Transactional
    public Inventory add(InventoryParam param) {

        if (ToolUtil.isEmpty(param.getCoding())) {
            CodingRules codingRules = codingRulesService.query().eq("module", "6").eq("state", 1).one();
            if (ToolUtil.isNotEmpty(codingRules)) {
                String coding = codingRulesService.backCoding(codingRules.getCodingRulesId());
                param.setCoding(coding);
            } else {
                throw new ServiceException(500, "请配置入库单据自动生成编码规则");
            }
        }

        Inventory entity = getEntity(param);
        this.save(entity);

        if (ToolUtil.isEmpty(param.getDetailParams())) {
            throw new ServiceException(500, "物料不存在");
        }
        List<InventoryDetail> inventoryDetails = BeanUtil.copyToList(param.getDetailParams(), InventoryDetail.class, new CopyOptions());
        for (InventoryDetail inventoryDetail : inventoryDetails) {
            inventoryDetail.setInventoryId(entity.getInventoryTaskId());
            inventoryDetail.setRealNumber(inventoryDetail.getNumber());
        }
        inventoryDetailService.saveBatch(inventoryDetails);

        param.setInventoryTaskId(entity.getInventoryTaskId());
        param.setCreateUser(entity.getCreateUser());
        Long taskId = submit(param);
        entity.setTaskId(taskId);
        this.updateById(entity);

        /**
         * 清空购物车
         */
        ShopCart shopCart = new ShopCart();
        shopCart.setDisplay(0);
        shopCartService.update(shopCart, new QueryWrapper<ShopCart>() {{
            eq("type", "stocktaking");
            eq("create_user", LoginContextHolder.getContext().getUserId());
        }});
        return entity;
    }

    @Override
    public List<InventoryResult> listByTime() {
        InventoryParam param = new InventoryParam();
        DateTime date = DateUtil.date();
        param.setCurrentTime(date);
        return this.baseMapper.listByTime(param);
    }


    @Override
    @Transactional
    public void timelyAdd(InventoryParam param) {
        if (ToolUtil.isEmpty(param.getCoding())) {
            CodingRules codingRules = codingRulesService.query().eq("module", "6").eq("state", 1).one();
            if (ToolUtil.isNotEmpty(codingRules)) {
                String coding = codingRulesService.backCoding(codingRules.getCodingRulesId());
                param.setCoding(coding);
            } else {
                throw new ServiceException(500, "请配置入库单据自动生成编码规则");
            }
        }
        param.setComplete(99);  //提交直接完成
        param.setHandleUser(LoginContextHolder.getContext().getUserId());
        Inventory entity = getEntity(param);
        this.save(entity);

        List<InventoryStock> inventoryStocks = BeanUtil.copyToList(param.getStockParams(), InventoryStock.class);
        for (InventoryStock inventoryStock : inventoryStocks) {
            if (ToolUtil.isEmpty(inventoryStock.getCustomerId())) {
                inventoryStock.setCustomerId(0L);
            }
            inventoryStock.setInventoryId(entity.getInventoryTaskId());
//            inventoryStock.setRealNumber(inventoryStock.getNumber());
        }

        inventoryStockService.saveBatch(inventoryStocks);
        param.setCreateUser(entity.getCreateUser());
        List<ShopCart> shopCarts = shopCartService.query()
                .eq("type", AnomalyType.timelyInventory.name())
                .eq("create_user", LoginContextHolder.getContext().getUserId())
                .eq("status", 0)
                .eq("display", 1).list();


        List<AnomalyParam> anomalyParams = new ArrayList<>();
        for (ShopCart cart : shopCarts) {
            cart.setDisplay(0);
            AnomalyParam anomalyParam = new AnomalyParam();
            anomalyParam.setAnomalyId(cart.getFormId());
            anomalyParams.add(anomalyParam);
        }

        if (ToolUtil.isNotEmpty(anomalyParams)) {
            AnomalyOrderParam anomalyOrderParam = new AnomalyOrderParam();
            anomalyOrderParam.setType(AnomalyType.timelyInventory.name());
            anomalyOrderParam.setAnomalyParams(anomalyParams);
            anomalyOrderService.add(anomalyOrderParam);
        }


        shopCartService.updateBatchById(shopCarts);

        /**
         * 同步盘点任务的物料
         */
        List<InventoryResult> inventoryResults = this.listByTime();
        List<Long> inventoryIds = new ArrayList<>();
        for (InventoryResult inventoryResult : inventoryResults) {
            inventoryIds.add(inventoryResult.getInventoryTaskId());
        }

        List<InventoryStock> stockList = inventoryStockService.query().in("inventory_id", inventoryIds)
                .eq("display", 1)
                .ne("lock_status", 99)
                .list();


        for (InventoryStock time : inventoryStocks) {
            updateInventoryStock(time, stockList);
        }

        inventoryStockService.updateBatchById(stockList);

    }

    /**
     * 比对 盘点任务的 物料 进行同步
     *
     * @param time
     * @param tasks
     */
    private void updateInventoryStock(InventoryStock time, List<InventoryStock> tasks) {
        for (InventoryStock task : tasks) {
            if (time.getSkuId().equals(task.getSkuId())
                    && time.getBrandId().equals(task.getBrandId())
                    && time.getCustomerId().equals(task.getCustomerId())
                    && time.getPositionId().equals(task.getPositionId())
            ) {

                task.setStatus(time.getStatus());
                task.setAnomalyId(time.getAnomalyId());
                task.setDisplay(1);
                task.setRealNumber(time.getRealNumber());

                if (time.getStatus() == 1) {   //正常物料    清楚异常id
                    task.setAnomalyId(0L);
                }
                if (time.getStatus() == -1) {
                    task.setLockStatus(99);
                }

            }
        }
    }


    /**
     * 静态限制
     */
    @Override
    public void staticState() {
        List<InventoryResult> inventoryResults = this.listByTime();  //时间范围内 所有未完成的盘点任务
        for (InventoryResult inventoryResult : inventoryResults) {
            if (ToolUtil.isNotEmpty(inventoryResult.getMode()) && inventoryResult.getMode().equals("staticState")) {   //如果 有静态  抛出异常 不可操作
                throw new ServiceException(500, "仓库正在盘点中，盘点结束后可继续执行任务");
            }
        }
    }

//    /**
//     * 静态开始时间  退回所有备料购物车
//     */
//    @Scheduled(cron = "0 */15 * * * ?")
//    public void darkDiskUpdateCart() {
//        System.err.println("静态盘点定时任务-----------(锁库)----------->" + new DateTime());
//        DateTime dateTime = new DateTime();
//        Integer count = this.query().eq("mode", "staticState")
//                .eq("begin_time", dateTime)
//                .eq("display", 1)
//                .count();
//        if (count > 0) {
//            QueryWrapper<ProductionPickListsCart> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("display", 1);
//            queryWrapper.ne("type", "frmLoss");
//            queryWrapper.eq("status", 0);
//            ProductionPickListsCart pickListsCart = new ProductionPickListsCart();
//            pickListsCart.setDisplay(0);
//            listsCartService.update(pickListsCart, queryWrapper);
//        }
//    }

    /**
     * 条件盘点
     *
     * @param param
     */
    @Override
    public void selectCondition(InventoryParam param) {

        if (param.getAllSku()) {       //全局盘点

        }

        QueryWrapper<StockDetails> queryWrapper = new QueryWrapper<>();
        if (ToolUtil.isNotEmpty(param.getBrandId())) {  //品牌盘点
            queryWrapper.eq("brand_id", param.getBrandId());
        }

        if (ToolUtil.isNotEmpty(param.getPositionId())) {   //库位盘点
            List<Long> positionIds = positionsService.getEndChild(param.getPositionId());
            queryWrapper.in("storehouse_positions_id", positionIds);
        }


        if (param.getAllBom()) {   //全局Bom
            Set<Long> skuIds = new HashSet<>();
            List<Parts> parts = partsService.query().eq("display", 1).eq("status", 99).list();
            List<Long> partIds = new ArrayList<>();
            for (Parts part : parts) {
                partIds.add(part.getPartsId());
            }
            List<ErpPartsDetail> partsDetails = partIds.size() == 0 ? new ArrayList<>() : partsDetailService.query().in("parts_id", partIds).eq("display", 1).list();
            for (ErpPartsDetail partsDetail : partsDetails) {
                skuIds.add(partsDetail.getSkuId());
            }
            queryWrapper.in("sku_id", skuIds);
        }

        List<StockDetails> stockDetails = stockDetailsService.list(queryWrapper);
        List<InventoryDetailParam> detailParams = new ArrayList<>();
        for (StockDetails stockDetail : stockDetails) {
            InventoryDetailParam detailParam = new InventoryDetailParam();
            detailParam.setSkuId(stockDetail.getSkuId());
            detailParam.setBrandId(stockDetail.getBrandId());
            detailParam.setInkindId(stockDetail.getInkindId());
            detailParam.setPositionId(stockDetail.getStorehousePositionsId());
            detailParam.setNumber(stockDetail.getNumber());
            detailParams.add(detailParam);
        }

        param.setDetailParams(detailParams);
        this.add(param);
    }

    /**
     * 盘点申请
     *
     * @param param
     */
    @Override
    public Inventory InventoryApply(InventoryParam param) {

        if (ToolUtil.isEmpty(param.getCoding())) {
            CodingRules codingRules = codingRulesService.query().eq("module", "6").eq("state", 1).one();
            if (ToolUtil.isNotEmpty(codingRules)) {
                String coding = codingRulesService.backCoding(codingRules.getCodingRulesId());
                param.setCoding(coding);
            } else {
                throw new ServiceException(500, "请配置入库单据自动生成编码规则");
            }
        }


        Inventory entity = getEntity(param);
        this.save(entity);

        for (InventoryDetailParam detailParam : param.getDetailParams()) {
            if ((ToolUtil.isEmpty(detailParam.getType()) || !detailParam.getType().equals("all")) && param.getMode().equals("staticState")) {
                throw new ServiceException(500, "仅可在全局盘点下进行静态盘点");
            }

            List<Long> bomIds = detailParam.getBomIds();
            List<Long> brandIds = detailParam.getBrandIds();
            List<Long> positionIds = detailParam.getPositionIds();
            List<Long> classIds = detailParam.getClassIds();
            if (ToolUtil.isNotEmpty(bomIds)) {
                String partJson = JSON.toJSONString(bomIds);
                detailParam.setPartCondition(partJson);
            }
            if (ToolUtil.isNotEmpty(brandIds)) {
                String brandJson = JSON.toJSONString(brandIds);
                detailParam.setBrandCondition(brandJson);
            }
            if (ToolUtil.isNotEmpty(positionIds)) {
                String positionJson = JSON.toJSONString(positionIds);
                detailParam.setPositionCondition(positionJson);
            }
            if (ToolUtil.isNotEmpty(classIds)) {
                String classJson = JSON.toJSONString(classIds);
                detailParam.setClassCondition(classJson);
            }
            if (ToolUtil.isNotEmpty(detailParam.getSpuIds())) {
                List<Long> spuIds = detailParam.getSpuIds();
                detailParam.setSpuId(spuIds.get(0));
            }
            detailParam.setInventoryId(entity.getInventoryTaskId());

            InventoryDetail inventoryDetail = new InventoryDetail();
            ToolUtil.copyProperties(detailParam, inventoryDetail);
            inventoryDetailService.save(inventoryDetail);
            detailParam.setDetailId(inventoryDetail.getDetailId());
        }

        inventoryStockService.addList(param.getDetailParams());
        param.setInventoryTaskId(entity.getInventoryTaskId());
        Long taskId = submit(param);
        entity.setTaskId(taskId);
        this.updateById(entity);
        return entity;

    }


    @Override
    public List<InventoryStock> condition(InventoryDetailParam detailParam) {

        QueryWrapper<StockDetails> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("display", 1);
        if (ToolUtil.isNotEmpty(detailParam.getInkindIds())) {
            queryWrapper.in("inkind_id", detailParam.getInkindIds());
        }
        if (ToolUtil.isNotEmpty(detailParam.getSpuIds())) {    //产品
            List<Sku> skus = skuService.query().in("spu_id", detailParam.getSpuIds()).eq("display", 1).list();
            queryWrapper.in("sku_id", new ArrayList<Long>() {{
                for (Sku skus : skus) {
                    add(skus.getSkuId());
                }
            }});
        }

        if (ToolUtil.isNotEmpty(detailParam.getBrandIds())) {    //品牌盘点
            queryWrapper.in("brand_id", detailParam.getBrandIds());
        }

        if (ToolUtil.isNotEmpty(detailParam.getPositionIds())) {   //库位盘点
            List<StorehousePositions> positions = positionsService.query().eq("display", 1).list();
            List<Long> positionIds = new ArrayList<>();
            positionIds.add(0L);
            for (Long positionId : detailParam.getPositionIds()) {
                positionIds.addAll(positionsService.getEndChild(positionId, positions));
            }
            queryWrapper.in("storehouse_positions_id", positionIds);
        }

        if (ToolUtil.isNotEmpty(detailParam.getClassIds())) {  //分类盘点
            List<Long> skuIds = inventoryDetailService.getSkuIds(detailParam.getClassIds());
            skuIds.add(0L);
            queryWrapper.in("sku_id", skuIds);
        }

        if (ToolUtil.isNotEmpty(detailParam.getBomIds())) {   //bom
            List<Long> skuIds = new ArrayList<>();
            for (Long bomId : detailParam.getBomIds()) {
                skuIds.addAll(partsService.getSkuIdsByBom(bomId));
            }
            skuIds.add(0L);
            queryWrapper.in("sku_id", skuIds);
        }

        if (ToolUtil.isNotEmpty(detailParam.getSkuIds())) {   //指定物料
            queryWrapper.in("sku_id", detailParam.getSkuIds());
        }

        List<StockDetails> stockDetails = stockDetailsService.list(queryWrapper);
        List<InventoryStock> details = new ArrayList<>();

        for (StockDetails stockDetail : stockDetails) {
            InventoryStock inventoryStock = new InventoryStock();
            inventoryStock.setSkuId(stockDetail.getSkuId());
            inventoryStock.setBrandId(stockDetail.getBrandId());
            inventoryStock.setCustomerId(stockDetail.getCustomerId());
            inventoryStock.setPositionId(stockDetail.getStorehousePositionsId());
            inventoryStock.setInkindId(stockDetail.getInkindId());
            inventoryStock.setNumber(stockDetail.getNumber());
            details.add(inventoryStock);
        }
        return details;
    }

    /**
     * 通过条件查库存呢  默认获取一个
     *
     * @param detailParam
     * @return
     */
    @Override
    public InventoryDetailResult conditionGetOne(InventoryDetailParam detailParam) {


        InventoryDetailResult inventoryDetailResult = null;

        if (ToolUtil.isNotEmpty(detailParam.getPositionIds())) {
            List<Long> positionIds = new ArrayList<>();
            for (Long positionId : detailParam.getPositionIds()) {
                positionIds.addAll(positionsService.getEndChild(positionId));
            }
            detailParam.setPositionIds(positionIds);
        }

        List<SkuBind> skuBinds = getSkuBinds(detailParam);  //获取物料绑定的信息
        List<InventoryDetailResult> detailResults = new ArrayList<>();
        for (SkuBind skuBind : skuBinds) {
            InventoryDetailResult result = new InventoryDetailResult();
            result.setSkuId(skuBind.getSkuId());
            result.setBrandId(skuBind.getBrandId());
            result.setPositionId(skuBind.getPositionId());
            detailResults.add(result);
        }

        Set<Long> skuNum = new HashSet<>();
        for (InventoryDetailResult detailResult : detailResults) {
            skuNum.add(detailResult.getSkuId());
        }
        if (ToolUtil.isNotEmpty(detailResults)) {
            inventoryDetailService.format(detailResults);
            inventoryDetailResult = detailResults.get(0);
            inventoryDetailResult.setSkuNum(skuNum.size());
        }
        return inventoryDetailResult;
    }

    @Override
    public List<SkuBind> getSkuBinds(InventoryDetailParam detailParam) {
        SkuBindParam skuBindParam = new SkuBindParam();
        ToolUtil.copyProperties(detailParam, skuBindParam);
        List<SkuBind> skuBinds = skuService.skuBindList(skuBindParam);
        for (SkuBind skuBind : skuBinds) {
            if (ToolUtil.isEmpty(skuBind.getBrandId())) {
                skuBind.setBrandId(0L);
            }
        }
        return skuBinds;
    }

    @Override
    public Object timely(Long positionId) {
        ShopCart shopCart = new ShopCart();
        shopCart.setDisplay(0);
        shopCartService.update(shopCart, new QueryWrapper<ShopCart>() {{
            eq("type", AnomalyType.timelyInventory.name());
            eq("create_user", LoginContextHolder.getContext().getUserId());
        }});


        List<Long> positionIds = positionsService.getEndChild(positionId);
        InventoryDetailParam inventoryDetailParam = new InventoryDetailParam();
        inventoryDetailParam.setPositionIds(positionIds);


        List<InventoryStock> all = new ArrayList<>();
        List<InventoryStock> condition = condition(inventoryDetailParam);   //从库存中取物料
        InventoryDetailParam detailParam = new InventoryDetailParam();
        detailParam.setPositionIds(new ArrayList<Long>() {{
            add(positionId);
        }});

        List<SkuBind> skuBinds = this.getSkuBinds(detailParam);  //从物料绑定取
        List<Long> skuIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        List<Long> storePositionIds = new ArrayList<>();

        for (SkuBind skuBind : skuBinds) {
            InventoryStock inventoryStock = new InventoryStock();
            inventoryStock.setSkuId(skuBind.getSkuId());
            inventoryStock.setBrandId(skuBind.getBrandId());
            inventoryStock.setPositionId(skuBind.getPositionId());
            storePositionIds.add(skuBind.getPositionId());
            skuIds.add(skuBind.getSkuId());
            brandIds.add(skuBind.getBrandId());
            condition.add(inventoryStock);
        }

        for (InventoryStock inventoryStock : condition) {
            if (all.stream().noneMatch(i -> i.getSkuId().equals(inventoryStock.getSkuId())
                    && i.getBrandId().equals(inventoryStock.getBrandId())
                    && i.getPositionId().equals(inventoryStock.getPositionId()))
            ) {
                all.add(inventoryStock);
            }
        }

        List<InventoryStockResult> inventoryStockResults = BeanUtil.copyToList(all, InventoryStockResult.class, new CopyOptions());
        List<AnomalyResult> anomalyResults = anomalyService.anomalyIsComplete(skuIds, brandIds, storePositionIds);

        /**
         * 比对未处理完成的异常件
         */
        for (InventoryStockResult inventoryStock : inventoryStockResults) {
            for (AnomalyResult anomalyResult : anomalyResults) {
                if (inventoryStock.getSkuId().equals(anomalyResult.getSkuId())
                        && inventoryStock.getBrandId().equals(anomalyResult.getBrandId())
                        && inventoryStock.getPositionId().equals(anomalyResult.getPositionId())) {
                    inventoryStock.setLockStatus(99);
                    inventoryStock.setAnomalyId(anomalyResult.getAnomalyId());
                    inventoryStock.setStatus(-1);
                    break;
                }
            }
        }

        inventoryStockService.format(inventoryStockResults);
        return inventoryStockResults;
    }


    private List<StorehousePositionsResult> positionsResultList(List<StockDetails> stockDetails) {
        if (ToolUtil.isEmpty(stockDetails)) {
            return new ArrayList<>();
        }
        Set<Long> positionIds = new HashSet<>();
        for (StockDetails detail : stockDetails) {
            if (ToolUtil.isEmpty(detail.getBrandId())) {
                detail.setBrandId(0L);
            }
            if (ToolUtil.isEmpty(detail.getStorehousePositionsId())) {
                detail.setStorehousePositionsId(0L);
            }
            positionIds.add(detail.getStorehousePositionsId());
        }

        /**
         * 通过库位组合
         */
        Map<Long, List<StockDetails>> map = new HashMap<>();
        for (Long positionId : positionIds) {
            List<StockDetails> details = new ArrayList<>();
            for (StockDetails detail : stockDetails) {
                if (detail.getStorehousePositionsId().equals(positionId)) {
                    details.add(detail);
                }
            }
            map.put(positionId, details);
        }

        List<StorehousePositionsResult> positionsResultList = new ArrayList<>();
        for (Long positionId : positionIds) {
            List<StockDetails> detailResultList = map.get(positionId);
            Set<Long> skuIds = new HashSet<>();


            for (StockDetails inventoryDetailResult : detailResultList) {
                skuIds.add(inventoryDetailResult.getSkuId());
            }


            Map<Long, List<BrandResult>> brandMap = new HashMap<>();
            for (Long skuId : skuIds) {
                List<BrandResult> brandResults = new ArrayList<>();
                for (StockDetails inventoryDetailResult : detailResultList) {
                    if (inventoryDetailResult.getSkuId().equals(skuId)) {
                        BrandResult brandResult = new BrandResult();
                        brandResult.setBrandId(inventoryDetailResult.getBrandId());
                        brandResult.setNumber(inventoryDetailResult.getNumber());
                        brandResult.setInkind(inventoryDetailResult.getInkindId());
                        if (inventoryDetailService.mergeBrand(brandResults, brandResult)) {
                            brandResults.add(brandResult);
                        }
                    }
                }
                inventoryDetailService.brandFormat(brandResults);
                brandMap.put(skuId, brandResults);
            }

            List<SkuResult> list = skuService.formatSkuResult(new ArrayList<>(skuIds));
            for (SkuResult skuResult : list) {
                skuResult.setBrandResults(brandMap.get(skuResult.getSkuId()));
            }
            StorehousePositionsResult positionsResult = new StorehousePositionsResult();
            positionsResult.setSkuResultList(list);
            positionsResult.setStorehousePositionsId(positionId);
            positionsResultList.add(positionsResult);

        }

        inventoryDetailService.positionFormat(positionsResultList);
        positionsService.format(positionsResultList);
        return positionsResultList;
    }

    /**
     * 通过物料找 盘点条件
     *
     * @param param
     */
    @Override
    public void bySku(InventoryParam param) {

        List<Long> skuIds = new ArrayList<>();
        for (InventoryDetailParam detailParam : param.getDetailParams()) {
            skuIds.add(detailParam.getSkuId());
        }
        List<StockDetails> stockDetails = stockDetailsService.query().in("sku_id", skuIds).list();

        List<InventoryDetailParam> detailParams = new ArrayList<>();
        for (StockDetails stockDetail : stockDetails) {
            InventoryDetailParam detailParam = new InventoryDetailParam();
            detailParam.setSkuId(stockDetail.getSkuId());
            detailParam.setBrandId(stockDetail.getBrandId());
            detailParam.setInkindId(stockDetail.getInkindId());
            detailParam.setPositionId(stockDetail.getStorehousePositionsId());
            detailParam.setNumber(stockDetail.getNumber());
            detailParams.add(detailParam);
        }
        param.setDetailParams(detailParams);
    }

    /**
     * 创建任务
     *
     * @param param
     */
    private Long submit(InventoryParam param) {
        ActivitiProcess activitiProcess = activitiProcessService.query().eq("type", ReceiptsEnum.Stocktaking.name()).eq("status", 99).eq("module", ReceiptsEnum.Stocktaking.name()).one();
        if (ToolUtil.isNotEmpty(activitiProcess)) {
            LoginUser user = LoginContextHolder.getContext().getUser();
            ActivitiProcessTaskParam activitiProcessTaskParam = new ActivitiProcessTaskParam();
            activitiProcessTaskParam.setTaskName(user.getName() + "的盘点任务");
            activitiProcessTaskParam.setQTaskId(param.getInventoryTaskId());
            activitiProcessTaskParam.setUserId(param.getUserId());
            activitiProcessTaskParam.setRemark(param.getRemark());
            activitiProcessTaskParam.setUserIds(param.getParticipants());
            activitiProcessTaskParam.setFormId(param.getInventoryTaskId());
            activitiProcessTaskParam.setNoticeId(announcementsService.toList(param.getNotice()));
            activitiProcessTaskParam.setType(ReceiptsEnum.Stocktaking.name());
            activitiProcessTaskParam.setProcessId(activitiProcess.getProcessId());
            Long taskId = activitiProcessTaskService.add(activitiProcessTaskParam);
            //任务参与人
            if (ToolUtil.isNotEmpty(param.getParticipants())) {
                List<Long> userIds = JSON.parseArray(param.getParticipants(), Long.class);
                taskParticipantService.addTaskPerson(taskId, userIds);
            }
            //添加小铃铛
            wxCpSendTemplate.setSource("processTask");
            wxCpSendTemplate.setSourceId(taskId);
            //添加log
            messageProducer.auditMessageDo(new AuditEntity() {{
                setMessageType(AuditMessageType.CREATE_TASK);
                setActivitiProcess(activitiProcess);
                setTaskId(taskId);
                setTimes(0);
                setMaxTimes(1);
            }});

            List<Long> userIds = new ArrayList<>();
            if (ToolUtil.isNotEmpty(param.getParticipants())) {
                userIds.addAll(JSON.parseArray(param.getParticipants(), Long.class));
            }
            String name = LoginContextHolder.getContext().getUser().getName();
            if (ToolUtil.isNotEmpty(userIds)) {
                /**
                 * 评论
                 */
                RemarksParam remarksParam = new RemarksParam();
                remarksParam.setTaskId(taskId);
                remarksParam.setType("remark");
                StringBuffer userIdStr = new StringBuffer();
                for (Long userId : userIds) {
                    userIdStr.append(userId).append(",");
                }
                String userStrtoString = userIdStr.toString();
                if (userIdStr.length() > 1) {
                    userStrtoString = userStrtoString.substring(0, userStrtoString.length() - 1);
                }
                remarksParam.setUserIds(userStrtoString);
                remarksParam.setContent(param.getRemark());
                remarksService.addByMQ(remarksParam);
            }
            return taskId;
        } else {
            throw new ServiceException(500, "请先设置或启用盘点流程");
        }
    }


    @Override
    public void updateStatus(ActivitiProcessTask processTask) {
        Inventory inventory = this.getById(processTask.getFormId());
        inventory.setStatus(StocktakingEnum.done.getStatus());
        this.updateById(inventory);
    }

    @Override
    public InventoryResult detail(Long id) {
        Inventory inventory = this.getById(id);
        if (ToolUtil.isEmpty(inventory)) {
            return null;
        }
        InventoryResult inventoryResult = new InventoryResult();
        BeanUtil.copyProperties(inventory, inventoryResult);
        format(new ArrayList<InventoryResult>() {{
            add(inventoryResult);
        }});
        List<InventoryDetailResult> details = inventoryDetailService.details(id);
        inventoryResult.setTaskList(details);
        inventoryResult.setDetailResults(details);

        if (ToolUtil.isNotEmpty(inventoryResult.getParticipants())) {   //参与人员
            List<Long> userIds = JSON.parseArray(inventoryResult.getParticipants(), Long.class);
            List<User> users = userIds.size() == 0 ? new ArrayList<>() : userService.listByIds(userIds);
            for (User user : users) {
                String imgUrl = stepsService.imgUrl(user.getUserId().toString());
                user.setAvatar(imgUrl);
            }
            inventoryResult.setParticipantList(users);
        }

        //负责人
        User user = ToolUtil.isEmpty(inventoryResult.getUserId()) ? new User() : userService.getById(inventoryResult.getUserId());
        if (ToolUtil.isNotEmpty(user) && ToolUtil.isNotEmpty(user.getUserId())) {
            String imgUrl = stepsService.imgUrl(user.getUserId().toString());
            user.setAvatar(imgUrl);
            inventoryResult.setPrincipal(user);
        }


        if (ToolUtil.isNotEmpty(inventoryResult.getNotice())) {
            List<Long> noticeIds = JSON.parseArray(inventoryResult.getNotice(), Long.class);
            List<Announcements> announcements = noticeIds.size() == 0 ? new ArrayList<>() : announcementsService.listByIds(noticeIds);
            inventoryResult.setAnnouncements(announcements);
        }

        if (ToolUtil.isNotEmpty(inventoryResult.getEnclosure())) {
            List<Long> medias = JSON.parseArray(inventoryResult.getEnclosure(), Long.class);
            List<String> mediaUrls = mediaService.getMediaUrls(medias, null);
            inventoryResult.setMediaUrls(mediaUrls);
        }

        User createUser = userService.getById(inventoryResult.getCreateUser());
        inventoryResult.setCreateUserName(createUser.getName());

        if (ToolUtil.isNotEmpty(inventoryResult.getHandleUser())) {
            User handleUser = userService.getById(inventoryResult.getHandleUser());
            inventoryResult.setHandleUserName(handleUser.getName());
        }

        Map<String, Integer> map = inventoryStockService.speedProgress(id);
        inventoryResult.setTotal(map.get("total"));
        inventoryResult.setHandle(map.get("handle"));
        inventoryResult.setPositionSize(map.get("positionNum"));
        inventoryResult.setSkuSize(map.get("skuNum"));
        return inventoryResult;
    }

    @Override
    public void delete(InventoryParam param) {
        Inventory entity = getEntity(param);
        entity.setDisplay(0);
        this.updateById(entity);
//        this.removeById(getKey(param));
    }

    @Override
    public void update(InventoryParam param) {
        Inventory oldEntity = getOldEntity(param);
        Inventory newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public InventoryResult findBySpec(InventoryParam param) {
        return null;
    }

    @Override
    public List<InventoryResult> findListBySpec(InventoryParam param) {
        return null;
    }

    @Override
    public PageInfo findPageBySpec(InventoryParam param) {
        Page<InventoryResult> pageContext = getPageContext();
        IPage<InventoryResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }


    @Override
    public PageInfo pageList(InventoryParam param) {

        /**
         * 通过库位查询
         */
        if (ToolUtil.isNotEmpty(param.getPositionId())) {
            List<Long> positionChild = positionsService.getEndChild(param.getPositionId());
            List<InventoryStock> inventoryStocks = positionChild.size() == 0 ? new ArrayList<>() : inventoryStockService.query().in("position_id", positionChild).eq("display", 1).list();
            List<Long> inventoryIds = new ArrayList<>();
            for (InventoryStock inventoryStock : inventoryStocks) {
                inventoryIds.add(inventoryStock.getInventoryId());
            }
            inventoryIds.add(0L);
            param.setInventoryIds(inventoryIds);
        }

        Page<InventoryResult> pageContext = getPageContext();
        IPage<InventoryResult> page = this.baseMapper.pageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    /**
     * 盘点
     *
     * @param inventoryRequest
     */
    @Override
    public void inventory(InventoryRequest inventoryRequest) {
        List<InventoryRequest.InkindParam> params = inventoryRequest.getInkindParams();

        List<Long> inkindIds = new ArrayList<>();

        for (InventoryRequest.InkindParam param : params) {
            inkindIds.add(param.getInkindId());
            if (param.getNumber() == 0) {
                throw new ServiceException(500, "请确定数量");
            }
            Integer count = detailsService.query().eq("inkind_id", param.getInkindId()).eq("display", 1).count();
            if (count > 1) {
                throw new ServiceException(500, "已有相同实物");
            }
        }

        long count = inkindIds.stream().distinct().count();
        if (count != params.size()) {
            throw new ServiceException(500, "请勿重复添加相同物料");
        }

        List<StockDetails> details = inkindIds.size() == 0 ? new ArrayList<>() : detailsService.query().in("inkind_id", inkindIds)
                .eq("display", 1).list();

        List<InventoryDetail> inventories = new ArrayList<>();

        List<Long> sotckIds = new ArrayList<>();
        List<Long> stockInkinds = new ArrayList<>();

        Inventory invent = new Inventory();
        invent.setInventoryTaskName(new Date() + "盘点任务");
        this.save(invent);

        InventoryDetail inventory = null;

        //添加盘点数据----------------------------------------------------------------------------------------------------

        List<OutstockListingParam> outParams = new ArrayList<>();
        List<InstockListParam> inListParams = new ArrayList<>();


        for (StockDetails detail : details) {
            stockInkinds.add(detail.getInkindId());
            for (InventoryRequest.InkindParam param : params) {
                if (detail.getInkindId().equals(param.getInkindId())) {  //相同实物

                    sotckIds.add(detail.getStockId());

                    if (detail.getNumber() > param.getNumber()) {  //修正出库

                        OutstockListingParam outstockListingParam = new OutstockListingParam();  //添加记录
                        outstockListingParam.setNumber(detail.getNumber() - param.getNumber());
                        outstockListingParam.setSkuId(detail.getSkuId());
                        outParams.add(outstockListingParam);

                        inventory = new InventoryDetail();
                        inventory.setInventoryId(invent.getInventoryTaskId());
                        inventory.setInkindId(param.getInkindId());
                        inventory.setStatus(2);
                        inventories.add(inventory);

                    } else if (detail.getNumber() < param.getNumber()) {   //修正入库

                        InstockListParam instockListParam = new InstockListParam();//添加记录
                        instockListParam.setSkuId(detail.getSkuId());
                        instockListParam.setNumber(param.getNumber() - detail.getNumber());
                        inListParams.add(instockListParam);

                        inventory = new InventoryDetail();
                        inventory.setInventoryId(invent.getInventoryTaskId());
                        inventory.setInkindId(param.getInkindId());
                        inventory.setStatus(1);
                        inventories.add(inventory);
                    }
                    detail.setNumber(param.getNumber());
                }
            }
        }


        List<Long> collect = inkindIds.stream().filter(item -> !stockInkinds.contains(item)).collect(toList());
//------------------------------------------------------------------------------------------------------------------------新入库
        if (ToolUtil.isNotEmpty(collect)) {
            List<StockDetails> stockDetails = detailsService.query().in("inkind_id", collect).eq("display", 1).list();
            if (ToolUtil.isNotEmpty(stockDetails)) {
                throw new ServiceException(500, "新入库的物料 在库存中已存在");
            }
            List<Inkind> inkindList = collect.size() == 0 ? new ArrayList<>() : inkindService.listByIds(collect);

            Map<Long, Long> numberMap = new HashMap<>();
            Map<Long, Long> codeMap = new HashMap<>();

            for (InventoryRequest.InkindParam inkindParam : inventoryRequest.getInkindParams()) {
                for (Long aLong : collect) {
                    if (aLong.equals(inkindParam.getInkindId())) {   //实物id 和 数量
                        numberMap.put(aLong, inkindParam.getNumber());
                    }
                }
            }
            List<OrCodeBind> codeBinds = collect.size() == 0 ? new ArrayList<>() : codeBindService.query().in("form_id", collect).list();

            for (Inkind inkind : inkindList) {
                for (OrCodeBind codeBind : codeBinds) {
                    if (inkind.getInkindId().equals(codeBind.getFormId())) {
                        codeMap.put(inkind.getInkindId(), codeBind.getOrCodeId());
                    }
                }
            }
            inkindInstock(inkindList, numberMap, codeMap, inventoryRequest.getStoreHouseId(), inventoryRequest.getPositionId(), inListParams);
        }
//--------------------------------------------------------------------------------------------------------------------------新出库
        if (ToolUtil.isNotEmpty(inventoryRequest.getOutStockInkindParams())) {
            List<Long> outStockInkind = new ArrayList<>();
            for (InventoryRequest.outStockInkindParam outStockInkindParam : inventoryRequest.getOutStockInkindParams()) {
                outStockInkind.add(outStockInkindParam.getInkindId());
            }
            inkindOutstock(outStockInkind, outParams);//出库
        }
        detailsService.updateBatchById(details);
        inventoryDetailService.saveBatch(inventories);

        if (inListParams.size() > 0) {  //入库记录添加
            instockOrderService.addInStockRecord(inListParams, "盘点入库记录");
        }
        if (outParams.size() > 0) {    //出库记录添加
            outstockOrderService.addOutStockRecord(outParams, "盘点出库记录");
        }

        if (sotckIds.size() > 0) {
            stockService.updateNumber(sotckIds);
        }


    }

    //盘点入库
    private void inkindInstock(List<Inkind> inkinds, Map<Long, Long> numberMap, Map<Long, Long> codeMap, Long storeHouseId, Long positionId
            , List<InstockListParam> instockListParams
    ) {


        List<InventoryDetail> inventoryDetails = new ArrayList<>();

        for (Inkind inkind : inkinds) {
            //入库记录详情
            InstockListParam instockListParam = new InstockListParam();
            instockListParam.setSkuId(inkind.getSkuId());
            instockListParam.setNumber(numberMap.get(inkind.getInkindId()));
            if (ToolUtil.isNotEmpty(inkind.getBrandId())) {
                instockListParam.setBrandId(inkind.getBrandId());
            }
            instockListParams.add(instockListParam);

            //库存添加
            StockDetails stockDetails = new StockDetails();
            stockDetails.setNumber(numberMap.get(inkind.getInkindId()));
            stockDetails.setStorehousePositionsId(positionId);
            stockDetails.setQrCodeId(codeMap.get(inkind.getInkindId()));
            stockDetails.setInkindId(inkind.getInkindId());
            stockDetails.setBrandId(inkind.getBrandId());
            stockDetails.setStorehouseId(storeHouseId);
            stockDetails.setSkuId(inkind.getSkuId());
            detailsService.save(stockDetails);
            inkind.setNumber(numberMap.get(inkind.getInkindId()));
            InventoryDetail inventoryDetail = new InventoryDetail();
            inventoryDetail.setInkindId(inkind.getInkindId());
            inventoryDetail.setStatus(1);
            inventoryDetails.add(inventoryDetail);
        }


        inkindService.updateBatchById(inkinds);
        inventoryDetailService.saveBatch(inventoryDetails);

    }


    //盘点出库
    private void inkindOutstock(List<Long> inkindIds, List<OutstockListingParam> outstockListingParams) {

        List<Long> stockIds = new ArrayList<>();
        List<InventoryDetail> inventoryDetails = new ArrayList<>();
        List<StockDetails> outDetails = detailsService.query().in("inkind_id", inkindIds).eq("display", 1).list();
        for (StockDetails outDetail : outDetails) {
            stockIds.add(outDetail.getStockId());
        }

        QueryWrapper<StockDetails> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("inkind_id", inkindIds);
        StockDetails stockDetails = new StockDetails();
        stockDetails.setDisplay(0);
        stockDetails.setNumber(0L);
        detailsService.update(stockDetails, queryWrapper);

        List<Inkind> inkinds = inkindService.listByIds(inkindIds);


        for (Inkind inkind : inkinds) {
            OutstockListingParam listingParam = new OutstockListingParam();
            listingParam.setSkuId(inkind.getSkuId());
            listingParam.setBrandId(inkind.getBrandId());
            outstockListingParams.add(listingParam);
            inkind.setNumber(0L);
            InventoryDetail inventoryDetail = new InventoryDetail();
            inventoryDetail.setInkindId(inkind.getInkindId());
            inventoryDetail.setStatus(2);
            inventoryDetails.add(inventoryDetail);
        }


        inkindService.updateBatchById(inkinds);
        inventoryDetailService.saveBatch(inventoryDetails);
        stockService.updateNumber(stockIds);

    }

    @Override
    public InkindResult inkindInventory(Long id) {
        InkindResult inkindResult = inkindService.getInkindResult(id);
        StockDetails stockDetails = detailsService.query().eq("inkind_id", inkindResult.getInkindId()).eq("display", 1).one();
        if (ToolUtil.isNotEmpty(stockDetails)) {
            StorehousePositionsResult positionsResult = positionsService.positionsResultById(stockDetails.getStorehousePositionsId());
            inkindResult.setPositionsResult(positionsResult);
            inkindResult.setInNotStock(true);
        } else {
            inkindResult.setInNotStock(false);
        }
        return inkindResult;
    }

    @Override
    public StorehousePositionsResult positionInventory(Long id) {
        StorehousePositionsResult positionsResult = positionsService.positionsResultById(id);

        List<StockDetails> stockDetails = detailsService.query().eq("storehouse_positions_id", id).eq("display", 1).list();
        if (ToolUtil.isNotEmpty(stockDetails)) {
            List<Long> inkindIds = new ArrayList<>();
            List<StockDetailsResult> detailsResults = new ArrayList<>();
            for (StockDetails stockDetail : stockDetails) {
                inkindIds.add(stockDetail.getInkindId());

                StockDetailsResult detailsResult = new StockDetailsResult();
                ToolUtil.copyProperties(stockDetail, detailsResult);
                detailsResult.setQrCodeId(stockDetail.getQrCodeId());
                detailsResults.add(detailsResult);
            }

            List<InkindResult> kinds = inkindService.getInKinds(inkindIds);
            for (StockDetailsResult detailsResult : detailsResults) {
                for (InkindResult kind : kinds) {
                    if (ToolUtil.isNotEmpty(detailsResult.getInkindId()) && detailsResult.getInkindId().equals(kind.getInkindId())) {
                        detailsResult.setInkindResult(kind);
                        break;
                    }
                }
            }
            positionsResult.setDetailsResults(detailsResults);
        }
        return positionsResult;
    }

    /**
     * 异常提交处理 更新库存数量
     *
     * @param skuId
     * @param brandId
     * @param
     * @param realNumber
     */
    @Override
    public void outUpdateStockDetail(Long skuId, Long brandId, Long realNumber) {

        QueryWrapper<StockDetails> queryWrapper = new QueryWrapper();
        queryWrapper.eq("sku_id", skuId);
        queryWrapper.eq("brand_id", brandId);
        queryWrapper.orderByAsc("create_time");
        queryWrapper.eq("display", 1);
        List<StockDetails> stockDetails = stockDetailsService.list(queryWrapper);

        long stockNum = 0;  // 计算当前物料库存数
        for (StockDetails stockDetail : stockDetails) {
            stockNum = stockNum + stockDetail.getNumber();
        }
        if (realNumber == stockNum) {  //数量相同不需要更新库存数
            return;
        }
        /**
         * ture :需要从库存出库
         * false:需要从库存入库
         */
        boolean outOrIn = false;
        if (stockNum > realNumber) {    //库存数 大于 复核数  出库
            outOrIn = true;
        }
        /**
         * 需要进行出库
         */
        if (outOrIn) {
            realNumber = stockNum - realNumber;  //需要出库的数量
            for (StockDetails stockDetail : stockDetails) {
                if (realNumber == 0) {   //复核数为0  直接退出   不进行操作
                    break;
                }
                long num = stockDetail.getNumber() - realNumber;   //库存数 - 复核数
                if (num <= 0) {   //当前实物数量不足出库  出去当前实物数量 继续循环 找下一个满足条件的实物进行出库
                    realNumber = realNumber - stockDetail.getNumber();
                    stockDetail.setNumber(0L);
                    stockDetail.setDisplay(0);
                } else {    //当前实物数量满足需要出库的数量 修正库存数  退出方法;
                    stockDetail.setNumber(num);
                    break;
                }
            }
        }
        stockDetailsService.updateBatchById(stockDetails);
    }


    @Override
    public void inStockUpdateStock(Long skuId, Long brandId, Long customerId, Long positionId, Long realNumber) {
        /**
         * 需要进行入库
         */
        Sku sku = skuService.getById(skuId);  //先判断是不是批量
        if (sku.getBatch() == 1) {  //批量
            instock(skuId, brandId, customerId, positionId, realNumber);
        } else {
            inStockBatch(skuId, brandId, customerId, positionId, realNumber);
        }
    }


    /**
     * 盘点超时
     */
    @Override
    public List<Long> timeOut(boolean timeOut) {
        DateTime dateTime = new DateTime();
        QueryWrapper<Inventory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("display", 1);
        if (timeOut) {
            queryWrapper.ne("status", 99);
            queryWrapper.lt("end_time", dateTime);
        } else {
            queryWrapper.ge("end_time", dateTime);
        }

        List<Inventory> inventories = this.list(queryWrapper);
        List<Long> fromIds = new ArrayList<>();
        for (Inventory inventory : inventories) {
            fromIds.add(inventory.getInventoryTaskId());
        }
        List<ActivitiProcessTask> activitiProcessTasks = fromIds.size() == 0 ? new ArrayList<>() : activitiProcessTaskService.query()
                .eq("display", 1)
                .in("form_id", fromIds)
                .select("process_task_id AS processTaskId ").list();

        List<Long> taskIds = new ArrayList<>();
        for (ActivitiProcessTask activitiProcessTask : activitiProcessTasks) {
            taskIds.add(activitiProcessTask.getProcessTaskId());
        }
        return taskIds;
    }

    /**
     * 盘点入库
     *
     * @param skuId
     * @param brandId
     * @param positionId
     * @param number
     */

    private Long instock(Long skuId, Long brandId, Long customerId, Long positionId, Long number) {
        Inkind inkind = new Inkind();
        inkind.setNumber(number);
        inkind.setSkuId(skuId);
        inkind.setSource("盘点入库");
        inkind.setType("1");
        inkind.setCustomerId(customerId);
        inkind.setBrandId(brandId);
        inkindService.save(inkind);

        OrCode orCode = new OrCode();
        orCode.setState(1);
        orCode.setType("item");
        orCodeService.save(orCode);

        OrCodeBindParam bindParam = new OrCodeBindParam();
        bindParam.setOrCodeId(orCode.getOrCodeId());
        bindParam.setFormId(inkind.getInkindId());
        bindParam.setSource("item");
        orCodeBindService.add(bindParam);

        StockDetails stockDetails = new StockDetails();
        stockDetails.setSkuId(skuId);
        stockDetails.setBrandId(brandId);
        stockDetails.setStorehousePositionsId(positionId);
        stockDetails.setNumber(number);
        stockDetails.setCustomerId(customerId);
        stockDetails.setInkindId(inkind.getInkindId());
        StorehousePositions storehousePositions = positionsService.getById(positionId);
        stockDetails.setStorehouseId(storehousePositions.getStorehouseId());

        stockDetailsService.save(stockDetails);

        return inkind.getInkindId();
    }


    /**
     * 循环入库
     */
    private void inStockBatch(Long skuId, Long brandId, Long customerId, Long positionId, Long number) {

        List<OrCode> orCodes = new ArrayList<>();
        List<Inkind> inkinds = new ArrayList<>();   //先创建实物

        for (int i = 0; i < number; i++) {
            Inkind inkind = new Inkind();
            inkind.setNumber(1L);
            inkind.setSkuId(skuId);
            inkind.setSource("盘点入库");
            inkind.setType("1");
            inkind.setBrandId(brandId);
            inkind.setCustomerId(customerId);
            inkinds.add(inkind);

            OrCode orCode = new OrCode();    //创建二维码
            orCode.setState(1);
            orCode.setType("item");
            orCodes.add(orCode);
        }
        inkindService.saveBatch(inkinds);
        orCodeService.saveBatch(orCodes);
        StorehousePositions storehousePositions = positionsService.getById(positionId);

        List<OrCodeBind> binds = new ArrayList<>();
        List<StockDetails> stockDetailList = new ArrayList<>();

        for (int i = 0; i < number; i++) {

            OrCode orCode = orCodes.get(i);
            Inkind inkind = inkinds.get(i);

            OrCodeBind bind = new OrCodeBind();   //添加绑定
            bind.setOrCodeId(orCode.getOrCodeId());
            bind.setFormId(inkind.getInkindId());
            bind.setSource("item");
            binds.add(bind);

            StockDetails stockDetails = new StockDetails();
            stockDetails.setSkuId(skuId);
            stockDetails.setBrandId(brandId);
            stockDetails.setInkindId(inkind.getInkindId());
            stockDetails.setCustomerId(customerId);
            stockDetails.setStorehousePositionsId(positionId);
            stockDetails.setStorehouseId(storehousePositions.getStorehouseId());
            stockDetails.setNumber(inkind.getNumber());
            stockDetailList.add(stockDetails);

        }
        stockDetailsService.saveBatch(stockDetailList);
        orCodeBindService.saveBatch(binds);
    }

    private Serializable getKey(InventoryParam param) {
        return param.getInventoryTaskId();
    }

    private Page<InventoryResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Inventory getOldEntity(InventoryParam param) {
        return this.getById(getKey(param));
    }

    private Inventory getEntity(InventoryParam param) {
        Inventory entity = new Inventory();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public void format(List<InventoryResult> data) {

        List<Long> positionIds = new ArrayList<>();
        List<Long> inventoryIds = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();

        for (InventoryResult datum : data) {
            inventoryIds.add(datum.getInventoryTaskId());
            positionIds.add(datum.getPositionId());
            userIds.add(datum.getCreateUser());
        }

        List<User> userList = userIds.size() == 0 ? new ArrayList<>() : userService.listByIds(userIds);
        List<StorehousePositions> positions = positionIds.size() == 0 ? new ArrayList<>() : storehousePositionsService.listByIds(positionIds);
        List<StorehousePositionsResult> positionsResultList = BeanUtil.copyToList(positions, StorehousePositionsResult.class, new CopyOptions());

        List<InventoryStock> inventoryStocks = inventoryIds.size() == 0 ? new ArrayList<>() : inventoryStockService.query().in("inventory_id", inventoryIds).eq("display", 1).last("limit 2").list();
        List<InventoryStockResult> inventoryStockResults = BeanUtil.copyToList(inventoryStocks, InventoryStockResult.class);
        inventoryStockService.format(inventoryStockResults);

        for (InventoryResult datum : data) {

            List<InventoryStockResult> stockResults = new ArrayList<>();
            for (InventoryStockResult inventoryStockResult : inventoryStockResults) {
                if (inventoryStockResult.getInventoryId().equals(datum.getInventoryTaskId())) {
                    stockResults.add(inventoryStockResult);
                }
            }
            datum.setStockResults(stockResults);

            for (StorehousePositionsResult positionsResult : positionsResultList) {
                if (ToolUtil.isNotEmpty(datum.getPositionId()) && datum.getPositionId().equals(positionsResult.getStorehousePositionsId())) {
                    datum.setPositionsResult(positionsResult);
                    break;
                }
            }

            for (User user : userList) {
                if (ToolUtil.isNotEmpty(datum.getCreateUser()) && user.getUserId().equals(datum.getCreateUser())) {
                    datum.setUser(user);
                    break;
                }
            }

            Map<String, Integer> map = inventoryStockService.speedProgress(datum.getInventoryTaskId());
            datum.setTotal(map.get("total"));
            datum.setHandle(map.get("handle"));
            datum.setPositionSize(map.get("positionNum"));
            datum.setSkuSize(map.get("skuNum"));

        }
    }
}
