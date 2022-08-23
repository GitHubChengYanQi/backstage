package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.appBase.service.MediaService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.mapper.AnomalyMapper;
import cn.atsoft.dasheng.erp.model.params.AnomalyDetailParam;
import cn.atsoft.dasheng.erp.model.params.AnomalyParam;
import cn.atsoft.dasheng.erp.model.params.ShopCartParam;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.pojo.AnomalyCensus;
import cn.atsoft.dasheng.erp.pojo.AnomalyCustomerNum;
import cn.atsoft.dasheng.erp.pojo.AnomalyType;
import cn.atsoft.dasheng.erp.pojo.CheckNumber;
import cn.atsoft.dasheng.erp.pojo.PositionNum;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.service.ActivitiAuditService;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.form.service.RemarksService;
import cn.atsoft.dasheng.message.producer.MessageProducer;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.production.pojo.QuerryLockedParam;
import cn.atsoft.dasheng.production.service.ProductionPickListsCartService;
import cn.atsoft.dasheng.production.service.ProductionPickListsService;
import cn.atsoft.dasheng.purchase.service.GetOrigin;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateTime;
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
import java.util.stream.Collectors;


/**
 * <p>
 * 异常单据 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-04-12
 */
@Service
public class AnomalyServiceImpl extends ServiceImpl<AnomalyMapper, Anomaly> implements AnomalyService {


    @Autowired
    private InstockOrderService instockOrderService;
    @Autowired
    private AnomalyDetailService detailService;
    @Autowired
    private InstockListService instockListService;
    @Autowired
    private MediaService mediaService;
    @Autowired
    private GetOrigin getOrigin;
    @Autowired
    private ShopCartService shopCartService;
    @Autowired
    private InkindService inkindService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService userService;
    @Autowired
    private ActivitiAuditService auditService;
    @Autowired
    private ActivitiProcessTaskService taskService;
    @Autowired
    private RemarksService remarksService;
    @Autowired
    private AnomalyOrderService anomalyOrderService;
    @Autowired
    private MessageProducer messageProducer;
    @Autowired
    private InventoryDetailService inventoryDetailService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private InventoryStockService inventoryStockService;
    @Autowired
    private AnomalyBindService anomalyBindService;
    @Autowired
    private StorehousePositionsService positionsService;
    @Autowired
    private AnomalyDetailService anomalyDetailService;
    @Autowired
    private InstockLogDetailService logDetailService;
    @Autowired
    private StockDetailsService stockDetailsService;
    @Autowired
    private ProductionPickListsCartService listsCartService;


    @Transactional
    @Override
    public Anomaly add(AnomalyParam param) {

        switch (param.getAnomalyType()) {
            case InstockError:     //判断入库单
                inventoryService.staticState();  //静态盘点判断
                InstockOrder order = instockOrderService.getById(param.getFormId());
                if (ToolUtil.isEmpty(order)) {
                    throw new ServiceException(500, "入库单不存在");
                }
                Integer count = this.query().eq("source_id", param.getSourceId()).eq("display", 1).count();
                if (count > 0) {
                    throw new ServiceException(500, "当前物料已添加异常");
                }
                param.setType(param.getAnomalyType().toString());
                break;
            case StocktakingError:  //盘点:
            case timelyInventory:
                boolean normal = isNormal(param);   //判断有无异常件
                if (!normal) {    //无异常
                    updateInventory(param);   //盘点详情 修改成正常状态
                } else {
                    inventoryStockService.updateInventoryStatus(param, -1);
                }
                break;
        }
        /**
         *
         * 供应商符合数
         */
        if (ToolUtil.isNotEmpty(param.getCustomerNums())) {
            String customerJSON = JSON.toJSONString(param.getCustomerNums());
            param.setCustomerJson(customerJSON);
        }

        param.setType(param.getAnomalyType().name());
        Anomaly entity = this.getEntity(param);
        this.save(entity);
        /**
         * 来源
         */
        if (ToolUtil.isNotEmpty(param.getSource()) && ToolUtil.isNotEmpty(param.getSourceId())) {
            String origin = getOrigin.newThemeAndOrigin("anomaly", entity.getAnomalyId(), entity.getSource(), entity.getSourceId());
            entity.setOrigin(origin);
            this.updateById(entity);
        }

        /**
         * 添加异常明细
         */
        param.setAnomalyId(entity.getAnomalyId());
        boolean b = addDetails(param);

        /**
         * 添加异常购物车
         */
        if (b) {
            ShopCartParam shopCartParam = new ShopCartParam();
            shopCartParam.setSkuId(param.getSkuId());
            shopCartParam.setBrandId(param.getBrandId());
            shopCartParam.setCustomerId(param.getCustomerId());
            shopCartParam.setType(param.getAnomalyType().name());
            shopCartParam.setFormStatus(-1L);
            shopCartParam.setFormId(entity.getAnomalyId());
            shopCartParam.setInstockListId(param.getInstockListId());
            shopCartParam.setNumber(param.getNeedNumber());

            List<PositionNum> list = new ArrayList<PositionNum>() {{  //库位解json 兼容之前结构
                add(new PositionNum(param.getPositionId(), 0L));
            }};
            shopCartParam.setStorehousePositionsId(JSON.toJSONString(list));
            shopCartService.add(shopCartParam);
        }
        return entity;
    }


    @Override
    public List<AnomalyCensus> anomalyCensus(AnomalyParam param) {

        List<AnomalyResult> anomalyResults = this.baseMapper.customList(param);
        Map<Integer, Integer> map = new HashMap<>();

        map.put(1, 0);
        map.put(2, 0);
        map.put(3, 0);
        map.put(4, 0);
        map.put(5, 0);
        map.put(6, 0);
        map.put(7, 0);
        map.put(8, 0);
        map.put(9, 0);
        map.put(10, 0);
        map.put(11, 0);
        map.put(12, 0);

        for (AnomalyResult anomalyResult : anomalyResults) {
            DateTime dateTime = new DateTime(anomalyResult.getCreateTime());
            int month = dateTime.month();
            Integer num = map.get(month);
            num++;
            map.put(month, num);
        }

        List<AnomalyCensus> anomalyCensuses = new ArrayList<>();
        for (Integer month : map.keySet()) {
            Integer number = map.get(month);
            AnomalyCensus anomalyCensus = new AnomalyCensus();
            anomalyCensus.setMonth(month);
            anomalyCensus.setNumber(number);
            anomalyCensuses.add(anomalyCensus);
        }

        return anomalyCensuses;
    }

    @Override
    public Map<Integer, List<AnomalyResult>> detailed(AnomalyParam param) {
        Map<Integer, List<AnomalyResult>> map = new HashMap<>();
        List<AnomalyResult> anomalyResults = this.baseMapper.customList(param);
        this.format(anomalyResults);

        for (AnomalyResult anomalyResult : anomalyResults) {
            DateTime dateTime = new DateTime(anomalyResult.getCreateTime());
            int month = dateTime.month();
            List<AnomalyResult> results = map.get(month);
            if (ToolUtil.isEmpty(results)) {
                results = new ArrayList<>();
            }
            results.add(anomalyResult);
            map.put(month, results);
        }
        return map;
    }


    @Override
    public AnomalyResult detail(Long id) {
        Anomaly anomaly = this.getById(id);
        AnomalyResult anomalyResult = new AnomalyResult();
        ToolUtil.copyProperties(anomaly, anomalyResult);
        detailFormat(anomalyResult);
        return anomalyResult;
    }

    /**
     * 暂存
     */
    @Override
    @Transactional
    public Long temporary(AnomalyParam param) {
        if (ToolUtil.isNotEmpty(param.getAnomalyId())) {
            detailService.remove(new QueryWrapper<AnomalyDetail>() {{
                eq("anomaly_id", param.getAnomalyId());
            }});
        } else {
            param.setType(param.getAnomalyType().name());
            Anomaly entity = getEntity(param);
            this.save(entity);
            param.setAnomalyId(entity.getAnomalyId());
        }

        /**
         * 添加异常明细
         */
        for (AnomalyDetailParam detailParam : param.getDetailParams()) {
            AnomalyDetail detail = new AnomalyDetail();
            ToolUtil.copyProperties(detailParam, detail);
            detail.setAnomalyId(param.getAnomalyId());
            if (ToolUtil.isNotEmpty(detailParam.getNoticeIds())) {
                String json = JSON.toJSONString(detailParam.getNoticeIds());
                detail.setRemark(json);
            }
            detailService.save(detail);
        }
        param.setAnomalyId(param.getAnomalyId());
        inventoryStockService.updateInventoryStatus(param, 2);
//        updateInventoryStatus(param, 2);
        return param.getAnomalyId();
    }

    /**
     * 获取异常数量
     *
     * @param ids
     * @return
     */
    @Override
    public Map<Long, AnomalyResult> getMap(List<Long> ids) {
        if (ToolUtil.isEmpty(ids)) {
            return new HashMap<>();
        }

        Map<Long, AnomalyResult> map = new HashMap<>();
        List<Anomaly> anomalies = this.lambdaQuery().in(Anomaly::getAnomalyId, ids).eq(Anomaly::getStatus, 0).eq(Anomaly::getDisplay, 1).list();

        List<AnomalyResult> anomalyResults = BeanUtil.copyToList(anomalies, AnomalyResult.class, new CopyOptions());
        List<AnomalyDetail> details = detailService.query().in("anomaly_id", ids).eq("display", 1).list();

        for (AnomalyResult anomalyResult : anomalyResults) {
            anomalyResult.setErrorNumber(anomalyResult.getRealNumber() - anomalyResult.getNeedNumber());
            long otherNum = 0L;
            for (AnomalyDetail detail : details) {
                if (detail.getAnomalyId().equals(anomalyResult.getAnomalyId())) {
                    otherNum = otherNum + detail.getNumber();
                }
            }
            anomalyResult.setOtherNumber(otherNum);
            map.put(anomalyResult.getAnomalyId(), anomalyResult);
        }
        return map;
    }


    /**
     * 修改盘点明细状态
     *
     * @param param
     */
    private void updateInventoryStatus(AnomalyParam param, int status) {
        QueryWrapper<InventoryDetail> queryWrapper = new QueryWrapper<>();
        /**
         * 同一时间段   统一修改
         */
        List<Long> inventoryIds = new ArrayList<>();
        List<InventoryResult> inventoryResults = inventoryService.listByTime();
        if (ToolUtil.isNotEmpty(inventoryResults)) {
            for (InventoryResult inventoryResult : inventoryResults) {
                inventoryIds.add(inventoryResult.getInventoryTaskId());
            }
        }
        inventoryIds.add(param.getFormId());
        queryWrapper.in("inventory_id", inventoryIds);
        queryWrapper.eq("sku_id", param.getSkuId());
        queryWrapper.eq("brand_id", param.getBrandId());
        queryWrapper.eq("position_id", param.getPositionId());

        List<InventoryDetail> inventoryDetails = inventoryDetailService.list(queryWrapper);

        for (InventoryDetail inventoryDetail : inventoryDetails) {    //保留之前记录
            if (inventoryDetail.getLockStatus() == 99) {
                throw new ServiceException(500, "当前状态不可更改");
            }
            inventoryDetail.setStatus(status);
            inventoryDetail.setAnomalyId(param.getAnomalyId());
            inventoryDetail.setLockStatus(0);
            inventoryDetail.setDisplay(0);
        }

        List<InventoryDetail> detailList = BeanUtil.copyToList(inventoryDetails, InventoryDetail.class, new CopyOptions());
        for (InventoryDetail inventoryDetail : detailList) {
            inventoryDetail.setDetailId(null);
            inventoryDetail.setDisplay(1);
        }

        param.setType(param.getAnomalyType().toString());
        inventoryDetailService.updateBatchById(inventoryDetails);
        inventoryDetailService.saveBatch(detailList);
    }

    /**
     * 添加详情(区分批量)
     *
     * @param param
     */
    public boolean addDetails(AnomalyParam param) {
        boolean t = true;

        switch (param.getAnomalyType()) {
            case InstockError:
                if (param.getRealNumber() - param.getNeedNumber() == 0 && ToolUtil.isEmpty(param.getDetailParams())) {
                    throw new ServiceException(500, "缺少异常信息");
                }
            case timelyInventory:
            case StocktakingError:
                boolean normal = isNormal(param);   //判断有无异常件
                if (!normal) {    //无异常
                    updateInventory(param);   //盘点详情 修改成正常状态
                    t = false;
                } else {
                    inventoryStockService.updateInventoryStatus(param, -1);
                }
        }

        if (t) {   //添加异常信息
            List<Inkind> inkinds = new ArrayList<>();

            long inkindNum = 0L;
            for (AnomalyDetailParam detailParam : param.getDetailParams()) {
                AnomalyDetail detail = new AnomalyDetail();
                ToolUtil.copyProperties(detailParam, detail);
                detail.setAnomalyId(param.getAnomalyId());
                detail.setInkindId(detailParam.getInkindId());
                if (ToolUtil.isNotEmpty(detailParam.getNoticeIds())) {
                    String json = JSON.toJSONString(detailParam.getNoticeIds());
                    detail.setRemark(json);
                }
                detailService.save(detail);
                /**
                 * 实物绑定
                 */
                AnomalyBind anomalyBind = new AnomalyBind();
                anomalyBind.setInkindId(detail.getInkindId());
                anomalyBind.setDetailId(detail.getDetailId());
                anomalyBind.setAnomalyId(param.getAnomalyId());
                anomalyBindService.save(anomalyBind);


                Inkind inkind = new Inkind();
                inkind.setInkindId(detailParam.getInkindId());
                inkind.setNumber(detailParam.getNumber());
                inkinds.add(inkind);
                inkindNum = inkindNum + detailParam.getNumber();
            }
            if (inkindNum > param.getRealNumber()) {
                throw new ServiceException(500, "异常数量不能大于盘点数量");
            }
            inkindService.updateBatchById(inkinds);
        }
        return t;
    }

    /**
     * 判断是不是异常件
     *
     * @param param
     * @return
     */
    private boolean isNormal(AnomalyParam param) {
        //判断盘点操作权限
        inventoryDetailService.jurisdiction(param.getFormId());

        return anomalyOrderService.check(param.getSkuId(), param.getBrandId(), param.getPositionId(), Math.toIntExact(param.getRealNumber()));
    }

    /**
     * 盘点记录
     *
     * @param param
     * @param status
     */
    @Override
    public void addInventoryRecord(AnomalyParam param, Long inventoryId, int status) {

        if (ToolUtil.isEmpty(param.getFormId())) {
            Anomaly anomaly = this.getById(param.getAnomalyId());
            param.setFormId(anomaly.getFormId());
        }

        QueryWrapper<InstockLogDetail> queryWrapper = new QueryWrapper<>();    //先删除之前记录
        queryWrapper.eq("sku_id", param.getSkuId());
        queryWrapper.eq("brand_id", param.getBrandId());
        queryWrapper.eq("storehouse_positions_id", param.getPositionId());
        queryWrapper.eq("source", "inventory");
        queryWrapper.eq("source_id", inventoryId);
        logDetailService.remove(queryWrapper);

        InstockLogDetail instockLogDetail = new InstockLogDetail();
        instockLogDetail.setSkuId(param.getSkuId());
        if (status == 1) {
            instockLogDetail.setType("normal");
        } else if (status == -1) {
            instockLogDetail.setType("error");
        }

//        Anomaly anomaly = this.getById(param.getAnomalyId());
//        anomaly

        instockLogDetail.setBrandId(param.getBrandId());
        instockLogDetail.setCustomerId(param.getCustomerId());
        instockLogDetail.setSource("inventory");
        instockLogDetail.setRealNumber(param.getRealNumber());
        instockLogDetail.setAnomalyId(param.getAnomalyId());
        instockLogDetail.setSourceId(inventoryId);
        instockLogDetail.setStorehousePositionsId(param.getPositionId());
        logDetailService.save(instockLogDetail);
    }

    /**
     * 盘点  异常 数据 改成正常数据
     *
     * @param param
     */
    private void updateInventory(AnomalyParam param) {


        inventoryStockService.updateInventoryStatus(param, 1);//数据正常  不添加异常数据
        /**
         * 更新购物车
         */
        ShopCart shopCart = new ShopCart();
        shopCart.setDisplay(0);
        switch (param.getAnomalyType()) {
            case allStocktaking:
                shopCartService.update(shopCart, new QueryWrapper<ShopCart>() {{
                    eq("type", param.getAnomalyType().name());
                }});
                break;
            case StocktakingError:
                shopCartService.update(shopCart, new QueryWrapper<ShopCart>() {{
                    eq("form_id", param.getAnomalyId());
                }});
                break;
        }

    }


    @Override
    public void delete(AnomalyParam param) {
        Anomaly anomaly = this.getById(param.getAnomalyId());
        if (ToolUtil.isEmpty(anomaly)) {
            return;
        }
        anomaly.setDisplay(0);
        this.updateById(anomaly);

        AnomalyDetail detail = new AnomalyDetail();
        detail.setDisplay(0);
        detailService.update(detail, new QueryWrapper<AnomalyDetail>() {{
            eq("anomaly_id", anomaly.getAnomalyId());
        }});

        ShopCart shopCart = new ShopCart();
        shopCart.setDisplay(0);
        shopCartService.update(shopCart, new QueryWrapper<ShopCart>() {{
            eq("form_id", anomaly.getAnomalyId());
        }});


        if (ToolUtil.isNotEmpty(anomaly.getSourceId())) {
            InstockList instockList = instockListService.getById(anomaly.getSourceId());
            if (ToolUtil.isNotEmpty(instockList)) {
                instockList.setStatus(0L);
                instockListService.updateById(instockList);
            }
        }
        //盘点
        if (anomaly.getType().equals(AnomalyType.StocktakingError.name()) || anomaly.getType().equals(AnomalyType.allStocktaking.name())) {
            InventoryDetail inventoryDetail = new InventoryDetail();
            inventoryDetail.setStatus(0);
            inventoryDetail.setAnomalyId(0L);
            inventoryDetailService.update(inventoryDetail, new QueryWrapper<InventoryDetail>() {{
                eq("anomaly_id", anomaly.getAnomalyId());
            }});
        }

    }


    @Override
    @Transactional
    public Anomaly update(AnomalyParam param) {
        Anomaly oldEntity = getOldEntity(param);
        param.setType(oldEntity.getType());
        param.setFormId(oldEntity.getFormId());

        /**
         *
         * 供应商符合数
         */
        if (ToolUtil.isNotEmpty(param.getCustomerNums())) {
            String customerJSON = JSON.toJSONString(param.getCustomerNums());
            param.setCustomerJson(customerJSON);
        }

        if (ToolUtil.isNotEmpty(oldEntity.getOrderId())) {
            AnomalyOrder anomalyOrder = anomalyOrderService.getById(oldEntity.getOrderId());
            if (anomalyOrder.getComplete() == 99) {
                throw new ServiceException(500, "當前狀態不可更改");
            }
        }
        Anomaly newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);


        deleteBind(param.getAnomalyId());   //删除绑定数据
        for (AnomalyType value : AnomalyType.values()) {
            if (value.name().equals(oldEntity.getType())) {
                param.setAnomalyType(value);
            }
        }

        boolean b = addDetails(param);
        if (b) {    //添加购物车
            shopCartService.remove(new QueryWrapper<ShopCart>() {{
                eq("form_id", oldEntity.getAnomalyId());
            }});
            ShopCartParam shopCartParam = new ShopCartParam();
            shopCartParam.setSkuId(param.getSkuId());
            shopCartParam.setBrandId(param.getBrandId());
            shopCartParam.setCustomerId(param.getCustomerId());
            shopCartParam.setType(param.getType());
            shopCartParam.setFormId(oldEntity.getAnomalyId());
            shopCartParam.setNumber(param.getNeedNumber());
            List<PositionNum> list = new ArrayList<PositionNum>() {{  //库位解json 兼容之前结构
                add(new PositionNum(param.getPositionId(), 0L));
            }};
            shopCartParam.setStorehousePositionsId(JSON.toJSONString(list));
            shopCartService.add(shopCartParam);
        }

        String skuMessage = skuService.skuMessage(oldEntity.getSkuId());
        shopCartService.addDynamic(oldEntity.getFormId(), skuMessage + "修改了异常描述");
        return newEntity;
    }

    /**
     * 异常修改为正常 删除 异常 绑定的数据
     *
     * @param anomalyId
     */
    @Override
    public void deleteBind(Long anomalyId) {
        detailService.remove(new QueryWrapper<AnomalyDetail>() {{
            eq("anomaly_id", anomalyId);
        }});

        anomalyBindService.remove(new QueryWrapper<AnomalyBind>() {{
            eq("anomaly_id", anomalyId);
        }});
    }

    /**
     * 异常处理
     */
    @Override
    public void dealWithError(AnomalyParam param) {


        Anomaly oldEntity = getOldEntity(param);

        /**
         *
         * 供应商符合数
         */
        if (ToolUtil.isNotEmpty(param.getCustomerNums())) {
            String customerJSON = JSON.toJSONString(param.getCustomerNums());
            param.setCustomerJson(customerJSON);
        }

        Anomaly newEntity = getEntity(param);
        detailService.allowEdit(oldEntity);

        ActivitiProcessTask task = taskService.getByFormId(oldEntity.getOrderId());
        List<Long> userIds = new ArrayList<>(auditService.getUserIds(task.getProcessTaskId()));
        userIds.add(param.getCreateUser());
        boolean power = this.power(userIds);

        if (power) {
            throw new ServiceException(500, "你没有操作权限");
        }

        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);

        if (ToolUtil.isNotEmpty(param.getInstockNumber())) {
            InstockList instockList = new InstockList();
            instockList.setInstockListId(oldEntity.getSourceId());
            instockList.setRealNumber(param.getInstockNumber());
            instockListService.updateById(instockList);
        }

        if (param.getComplete()) {
            updateStatus(param.getAnomalyId()); //更新异常状态
        }

        if (ToolUtil.isNotEmpty(param.getCheckNumber()) && !LoginContextHolder.getContext().getUserId().equals(oldEntity.getCreateUser())) {
            detailService.pushPeople(oldEntity.getCreateUser(), oldEntity.getAnomalyId());
        }

        String skuMessage = skuService.skuMessage(oldEntity.getSkuId());
        if (ToolUtil.isNotEmpty(param.getCheckNumber())) {
            shopCartService.addDynamic(oldEntity.getOrderId(), "对" + skuMessage + "数量进行复核");
        }
        shopCartService.addDynamic(oldEntity.getOrderId(), "对" + skuMessage + "确认了处理意见");
    }

    /**
     * 更新异常状态
     */
    private void updateStatus(Long anomalyId) {
        Anomaly anomaly = this.getById(anomalyId);
        anomaly.setStatus(99);
        this.updateById(anomaly);
    }


    private boolean power(List<Long> userIds) {
        Long userId = LoginContextHolder.getContext().getUserId();
        for (Long id : userIds) {
            if (ToolUtil.isNotEmpty(id) && id.equals(userId)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Long> createInkind(AnomalyParam param, AnomalyDetailParam detailParam) {
        List<Long> inkindIds = new ArrayList<>();
        Inkind oldInkind = inkindService.getById(detailParam.getPidInKind());
        switch (oldInkind.getSource()) {
            case "inErrorSingle":
                for (long i = 0; i < detailParam.getNumber(); i++) {
                    Inkind inkind = new Inkind();
                    inkind.setNumber(detailParam.getNumber());
                    inkind.setCustomerId(param.getCustomerId());
                    inkind.setBrandId(param.getBrandId());
                    inkind.setSkuId(param.getSkuId());
                    inkind.setPid(detailParam.getPidInKind());
                    inkind.setSource(AnomalyType.InstockError.getName());
                    inkind.setSourceId(param.getFormId());
                    inkindService.save(inkind);
                    inkindIds.add(inkind.getInkindId());
                }
                break;
            case "inErrorBatch":    //批量
                inkindIds.add(oldInkind.getInkindId());
                oldInkind.setNumber(detailParam.getNumber());
                oldInkind.setCustomerId(param.getCustomerId());
                oldInkind.setBrandId(param.getBrandId());
                oldInkind.setSkuId(param.getSkuId());
                oldInkind.setSourceId(param.getFormId());
                inkindService.updateById(oldInkind);
                break;
        }
        return inkindIds;
    }

    @Override
    public AnomalyResult findBySpec(AnomalyParam param) {
        return null;
    }

    @Override
    public List<AnomalyResult> findListBySpec(AnomalyParam param) {
        return null;
    }

    @Override
    public PageInfo<AnomalyResult> findPageBySpec(AnomalyParam param) {
        Page<AnomalyResult> pageContext = getPageContext();
        IPage<AnomalyResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(AnomalyParam param) {
        return param.getAnomalyId();
    }

    private Page<AnomalyResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Anomaly getOldEntity(AnomalyParam param) {
        return this.getById(getKey(param));
    }

    private Anomaly getEntity(AnomalyParam param) {
        Anomaly entity = new Anomaly();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public void getOrder(List<AnomalyResult> data) {

        List<Long> orderIds = new ArrayList<>();

        for (AnomalyResult datum : data) {
            orderIds.add(datum.getOrderId());
        }

        List<AnomalyOrder> anomalyOrders = orderIds.size() == 0 ? new ArrayList<>() : anomalyOrderService.listByIds(orderIds);
        List<AnomalyOrderResult> orderResults = BeanUtil.copyToList(anomalyOrders, AnomalyOrderResult.class, new CopyOptions());

        for (AnomalyResult datum : data) {
            for (AnomalyOrderResult orderResult : orderResults) {
                if (ToolUtil.isNotEmpty(datum.getOrderId()) && datum.getOrderId().equals(orderResult.getOrderId())) {
                    datum.setOrderResult(orderResult);
                    break;
                }
            }
        }
    }


    @Override
    public void detailFormat(AnomalyResult result) {

        List<AnomalyDetailResult> details = detailService.getDetails(result.getAnomalyId());
        result.setDetails(details);

        List<SkuSimpleResult> skuSimpleResults = skuService.simpleFormatSkuResult(new ArrayList<Long>() {{
            add(result.getSkuId());
        }});

        if (ToolUtil.isNotEmpty(result.getCustomerJson())) {
            List<AnomalyCustomerNum> customerNums = JSON.parseArray(result.getCustomerJson(), AnomalyCustomerNum.class);
            result.setCustomerNums(customerNums);
        }

        AnomalyOrder anomalyOrder = ToolUtil.isEmpty(result.getOrderId()) ? new AnomalyOrder() : anomalyOrderService.getById(result.getOrderId());
        AnomalyOrderResult anomalyOrderResult = new AnomalyOrderResult();
        ToolUtil.copyProperties(anomalyOrder, anomalyOrderResult);
        result.setOrderResult(anomalyOrderResult);

        Brand brand = ToolUtil.isEmpty(result.getBrandId()) ? new Brand() : brandService.getById(result.getBrandId());
        Customer customer = ToolUtil.isEmpty(result.getCustomerId()) ? new Customer() : customerService.getById(result.getCustomerId());

        User user = ToolUtil.isEmpty(result.getCreateUser()) ? new User() : userService.getById(result.getCreateUser());
        result.setUser(user);

        StorehousePositions storehousePositions = ToolUtil.isEmpty(result.getPositionId()) ? new StorehousePositions() : positionsService.getById(result.getPositionId());
        StorehousePositionsResult storehousePositionsResult = new StorehousePositionsResult();
        ToolUtil.copyProperties(storehousePositions, storehousePositionsResult);

        if (ToolUtil.isNotEmpty(skuSimpleResults)) {
            result.setSkuResult(skuSimpleResults.get(0));
        }
        result.setBrand(brand);
        result.setCustomer(customer);
        result.setPositionsResult(storehousePositionsResult);


        //返回附件图片等
        if (ToolUtil.isNotEmpty(result.getEnclosure())) {
            List<Long> filedIds = Arrays.stream(result.getEnclosure().split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            List<String> filedUrls = new ArrayList<>();
            for (Long filedId : filedIds) {
                String mediaUrl = mediaService.getMediaUrl(filedId, 0L);
                filedUrls.add(mediaUrl);
            }
            result.setFiledUrls(filedUrls);
        }

        if (ToolUtil.isNotEmpty(result.getCheckNumber())) {
            List<CheckNumber> checkNumbers = JSON.parseArray(result.getCheckNumber(), CheckNumber.class);
            for (CheckNumber checkNumber : checkNumbers) {
                if (ToolUtil.isNotEmpty(checkNumber.getMediaIds())) {
                    List<String> mediaUrls = mediaService.getMediaUrls(checkNumber.getMediaIds(), null);
                    checkNumber.setMediaUrls(mediaUrls);
                }
            }
            result.setCheckNumbers(checkNumbers);
        }

    }

    @Override
    public void format(List<AnomalyResult> data) {

        List<Long> skuIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        List<Long> customerIds = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        List<Long> positionIds = new ArrayList<>();
        for (AnomalyResult datum : data) {
            skuIds.add(datum.getSkuId());
            brandIds.add(datum.getBrandId());
            customerIds.add(datum.getCustomerId());
            ids.add(datum.getAnomalyId());
            positionIds.add(datum.getPositionId());
        }

        List<SkuSimpleResult> skuSimpleResultList = skuService.simpleFormatSkuResult(skuIds);
        List<Brand> brandList = brandIds.size() == 0 ? new ArrayList<>() : brandService.listByIds(brandIds);
        List<Customer> customers = customerIds.size() == 0 ? new ArrayList<>() : customerService.listByIds(customerIds);
        List<AnomalyDetail> details = ids.size() == 0 ? new ArrayList<>() : detailService.query().in("anomaly_id", ids).eq("display", 1).list();
        List<AnomalyDetailResult> anomalyDetailResults = BeanUtil.copyToList(details, AnomalyDetailResult.class, new CopyOptions());
        List<StorehousePositionsResult> positionsResults = positionsService.details(positionIds);

        for (AnomalyResult datum : data) {

            if (ToolUtil.isNotEmpty(datum.getCustomerJson())) {
                List<AnomalyCustomerNum> customerNums = JSON.parseArray(datum.getCustomerJson(), AnomalyCustomerNum.class);
                datum.setCustomerNums(customerNums);
            }

            for (StorehousePositionsResult positionsResult : positionsResults) {
                if (ToolUtil.isNotEmpty(datum.getPositionId()) && datum.getPositionId().equals(positionsResult.getStorehousePositionsId())) {
                    datum.setPositionsResult(positionsResult);
                    break;
                }
            }

            for (SkuSimpleResult skuResult : skuSimpleResultList) {
                if (skuResult.getSkuId().equals(datum.getSkuId())) {
                    datum.setSkuResult(skuResult);
                    break;
                }
            }
            for (Brand brand : brandList) {
                if (ToolUtil.isNotEmpty(datum.getBrandId()) && datum.getBrandId().equals(brand.getBrandId())) {
                    datum.setBrand(brand);
                    break;
                }
            }

            for (Customer customer : customers) {
                if (ToolUtil.isNotEmpty(datum.getCustomerId()) && datum.getCustomerId().equals(customer.getCustomerId())) {
                    datum.setCustomer(customer);
                    break;
                }
            }

            datum.setErrorNumber(datum.getRealNumber() - datum.getNeedNumber());
            long otherNum = 0L;

            List<AnomalyDetailResult> detailResults = new ArrayList<>();
            for (AnomalyDetailResult detail : anomalyDetailResults) {
                if (datum.getAnomalyId().equals(detail.getAnomalyId())) {
                    otherNum = otherNum + detail.getNumber();
                    detailResults.add(detail);
                }
            }
            datum.setDetails(detailResults);
            datum.setOtherNumber(otherNum);
        }

    }
}
