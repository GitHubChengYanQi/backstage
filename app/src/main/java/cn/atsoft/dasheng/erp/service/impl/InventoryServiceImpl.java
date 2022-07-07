package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.action.Enum.ReceiptsEnum;
import cn.atsoft.dasheng.action.Enum.StocktakingEnum;
import cn.atsoft.dasheng.app.entity.ErpPartsDetail;
import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.StockDetailsResult;
import cn.atsoft.dasheng.app.service.*;
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
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.model.params.RemarksParam;
import cn.atsoft.dasheng.form.service.ActivitiProcessLogService;
import cn.atsoft.dasheng.form.service.ActivitiProcessService;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.form.service.RemarksService;
import cn.atsoft.dasheng.erp.mapper.InventoryMapper;
import cn.atsoft.dasheng.erp.pojo.InventoryRequest;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.message.enmu.AuditMessageType;
import cn.atsoft.dasheng.message.enmu.OperationType;
import cn.atsoft.dasheng.message.entity.AuditEntity;
import cn.atsoft.dasheng.message.entity.RemarksEntity;
import cn.atsoft.dasheng.message.producer.MessageProducer;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.*;

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
        submit(param);


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
        param.setCreateUser(entity.getCreateUser());

//        /**
//         * 清空购物车
//         */
//        ShopCart shopCart = new ShopCart();
//        shopCart.setDisplay(0);
//        shopCartService.update(shopCart, new QueryWrapper<ShopCart>() {{
//            eq("type", AnomalyType.timelyInventory.name());
//            eq("create_user", LoginContextHolder.getContext().getUserId());
//        }});

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
        }

        AnomalyOrderParam anomalyOrderParam = new AnomalyOrderParam();
        anomalyOrderParam.setType(AnomalyType.timelyInventory.name());
        anomalyOrderParam.setAnomalyParams(anomalyParams);
        anomalyOrderService.add(anomalyOrderParam);
        shopCartService.updateBatchById(shopCarts);
    }


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

    @Override
    public List<StorehousePositionsResult> timely(Long positionId) {
        QueryWrapper<StockDetails> queryWrapper = new QueryWrapper<>();
        List<Long> positionIds = positionsService.getEndChild(positionId);
        queryWrapper.in("storehouse_positions_id", positionIds);
        List<StockDetails> stockDetails = stockDetailsService.list(queryWrapper);
        return this.positionsResultList(stockDetails);

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
//        Map<Long,Long>
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
    private void submit(InventoryParam param) {
        ActivitiProcess activitiProcess = activitiProcessService.query().eq("type", ReceiptsEnum.Stocktaking.name()).eq("status", 99).eq("module", ReceiptsEnum.Stocktaking.name()).one();
        if (ToolUtil.isNotEmpty(activitiProcess)) {
            LoginUser user = LoginContextHolder.getContext().getUser();
            ActivitiProcessTaskParam activitiProcessTaskParam = new ActivitiProcessTaskParam();
            activitiProcessTaskParam.setTaskName(user.getName() + "的盘点任务");
            activitiProcessTaskParam.setQTaskId(param.getInventoryTaskId());
            activitiProcessTaskParam.setUserId(param.getCreateUser());
            activitiProcessTaskParam.setFormId(param.getInventoryTaskId());
            activitiProcessTaskParam.setType(ReceiptsEnum.Stocktaking.name());
            activitiProcessTaskParam.setProcessId(activitiProcess.getProcessId());
            Long taskId = activitiProcessTaskService.add(activitiProcessTaskParam);
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
                messageProducer.remarksServiceDo(new RemarksEntity() {{
                    setOperationType(OperationType.ADD);
                    setRemarksParam(remarksParam);
                }});
            }
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
        InventoryResult inventoryResult = new InventoryResult();
        BeanUtil.copyProperties(inventory, inventoryResult);
        format(new ArrayList<InventoryResult>() {{
            add(inventoryResult);
        }});
        Object taskList = inventoryDetailService.taskList(id);
        inventoryResult.setTaskList(taskList);
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
    public PageInfo<InventoryResult> findPageBySpec(InventoryParam param) {
        Page<InventoryResult> pageContext = getPageContext();
        IPage<InventoryResult> page = this.baseMapper.customPageList(pageContext, param);
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

                    } else if (detail.getNumber() < param.getNumber()) {  //修正入库

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

        List<Long> inventoryIds = new ArrayList<>();
        for (InventoryResult datum : data) {
            inventoryIds.add(datum.getInventoryTaskId());
        }

        List<InventoryDetailResult> details = inventoryDetailService.getDetails(inventoryIds);
        for (InventoryResult datum : data) {

            List<InventoryDetailResult> detailResults = new ArrayList<>();
            List<Long> skuIds = new ArrayList<>();

            for (InventoryDetailResult detail : details) {
                if (datum.getInventoryTaskId().equals(detail.getInventoryId())) {
                    detailResults.add(detail);
                }
                skuIds.add(detail.getSkuId());
            }
            Integer positionNum = storehousePositionsService.getPositionNum(skuIds);

            datum.setPositionSize(positionNum);
            datum.setSkuSize(detailResults.size());
//            datum.setDetailResults(detailResults);
        }
    }
}