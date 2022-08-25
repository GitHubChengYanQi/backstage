package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.mapper.InventoryStockMapper;
import cn.atsoft.dasheng.erp.model.params.AnomalyParam;
import cn.atsoft.dasheng.erp.model.params.InventoryDetailParam;
import cn.atsoft.dasheng.erp.model.params.InventoryStockParam;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.pojo.AnomalyType;
import cn.atsoft.dasheng.erp.pojo.SkuBind;
import cn.atsoft.dasheng.erp.pojo.SkuBindParam;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.service.StepsService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.production.pojo.QuerryLockedParam;
import cn.atsoft.dasheng.production.service.ProductionPickListsCartService;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 库存盘点处理 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-07-15
 */
@Service
public class InventoryStockServiceImpl extends ServiceImpl<InventoryStockMapper, InventoryStock> implements InventoryStockService {

    @Autowired
    private SkuService skuService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private StorehousePositionsService positionsService;
    @Autowired
    private InventoryDetailService inventoryDetailService;
    @Autowired
    private StockDetailsService stockDetailsService;
    @Autowired
    private AnomalyDetailService anomalyDetailService;
    @Autowired
    private AnomalyService anomalyService;
    @Autowired
    private ShopCartService shopCartService;
    @Autowired
    private UserService userService;
    @Autowired
    private StepsService stepsService;
    @Autowired
    private InstockHandleService instockHandleService;
    @Autowired
    private ProductionPickListsCartService pickListsCartService;


    @Override
    public void add(InventoryStockParam param) {
        InventoryStock entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(InventoryStockParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(InventoryStockParam param) {
        InventoryStock oldEntity = getOldEntity(param);
        InventoryStock newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    @Async
    public void addList(List<InventoryDetailParam> detailParams) {
        List<InventoryStock> all = new ArrayList<>();
        for (InventoryDetailParam detailParam : detailParams) {

            List<SkuBind> skuBinds = inventoryService.getSkuBinds(detailParam);  //从物料绑定取
            for (SkuBind skuBind : skuBinds) {
                if (ToolUtil.isNotEmpty(skuBind.getPositionId()) &&
                        all.stream().noneMatch(i -> i.getSkuId().equals(skuBind.getSkuId())
                                && i.getBrandId().equals(skuBind.getBrandId())
                                && i.getPositionId().equals(skuBind.getPositionId())
                        )) {
                    InventoryStock inventoryStock = new InventoryStock();
                    inventoryStock.setSkuId(skuBind.getSkuId());
                    inventoryStock.setBrandId(skuBind.getBrandId());
                    inventoryStock.setPositionId(skuBind.getPositionId());
                    inventoryStock.setInventoryId(detailParam.getInventoryId());
                    inventoryStock.setDetailId(detailParam.getDetailId());
                    all.add(inventoryStock);
                }
            }
//----------------------------------------------------------------------------------------------------------------------
            List<InventoryStock> condition = inventoryService.condition(detailParam);   //从库存取
            for (InventoryStock inventoryStock : condition) {
                if (all.stream().noneMatch(i -> i.getSkuId().equals(inventoryStock.getSkuId())
                        && i.getBrandId().equals(inventoryStock.getBrandId())
                        && i.getPositionId().equals(inventoryStock.getPositionId())
                )) {
                    inventoryStock.setInventoryId(detailParam.getInventoryId());
                    inventoryStock.setDetailId(detailParam.getDetailId());
                    all.add(inventoryStock);
                }
            }
        }
        if (all.size() == 0) {
            throw new ServiceException(500, "没有可盘点的物料");
        }
        this.saveBatch(all);
    }

    /**
     * 同步 盘点所需的物料
     */
    public void matchingData(Long inventoryId) {

        List<InventoryStock> all = this.query().eq("inventory_id", inventoryId).eq("display", 1).list();

        List<InventoryResult> inventoryResults = inventoryService.listByTime();    //满足现在时间段的任务
        inventoryResults.removeIf(i -> i.getStatus() == 99);  //找出符合当前时间段 并且未完成 的盘点任务

        boolean b = false;    //判断当前任务时间是否满足 同步
        for (InventoryResult inventoryResult : inventoryResults) {
            if (inventoryResult.getInventoryTaskId().equals(inventoryId)) {
                b = true;
                break;
            }
        }
        if (b) {
            List<Long> inventoryIds = new ArrayList<>();
            for (InventoryResult inventoryResult : inventoryResults) {
                inventoryIds.add(inventoryResult.getInventoryTaskId());
            }

            List<InventoryStock> inventoryStocks = inventoryIds.size() == 0 ? new ArrayList<>() : this.query()  //找出 当前时间段 执行过程中的物料
                    .in("inventory_id", inventoryIds)
                    .eq("display", 1).ne("status", 0)
                    .list();

            for (InventoryStock inventoryStock : all) {
                updateList(inventoryStock, inventoryStocks);
            }
            this.updateBatchById(all);
        }

    }

    /**
     * 同步数据
     *
     * @param applyAdd
     * @param inventoryStocks
     */
    private void updateList(InventoryStock applyAdd, List<InventoryStock> inventoryStocks) {

        for (InventoryStock inventoryStock : inventoryStocks) {
            if (applyAdd.getSkuId().equals(inventoryStock.getSkuId()) &&
                    applyAdd.getBrandId().equals(inventoryStock.getBrandId()) &&
                    applyAdd.getPositionId().equals(inventoryStock.getPositionId())) {
                applyAdd.setNumber(inventoryStock.getNumber());
                applyAdd.setRealNumber(inventoryStock.getRealNumber());
                applyAdd.setStatus(inventoryStock.getStatus());
                applyAdd.setAnomalyId(inventoryStock.getAnomalyId());
                applyAdd.setLockStatus(inventoryStock.getLockStatus());
            }
        }
    }

    public List<InventoryStockResult> details(Long inventoryId) {

        List<InventoryStock> inventoryStocks = this.query().eq("inventory_id", inventoryId).eq("display", 1).list();
        List<InventoryStockResult> inventoryStockResults = BeanUtil.copyToList(inventoryStocks, InventoryStockResult.class);
        this.format(inventoryStockResults);
        return inventoryStockResults;
    }

    @Override
    public InventoryStockResult findBySpec(InventoryStockParam param) {
        return null;
    }


    @Override
    public Object taskList(Long inventoryId) {
        List<InventoryStock> inventoryStocks = this.query().eq("inventory_id", inventoryId).eq("display", 1).list();
        List<InventoryStockResult> inventoryStockResults = BeanUtil.copyToList(inventoryStocks, InventoryStockResult.class);
        this.format(inventoryStockResults);
        return inventoryStockResults;
    }

    /**
     * 进度条
     *
     * @param inventoryId
     * @return
     */
    @Override
    public Map<String, Integer> speedProgress(Long inventoryId) {

        Inventory inventory = inventoryService.getById(inventoryId);

        List<InventoryStock> inventoryStocks = this.query().eq("inventory_id", inventoryId).eq("display", 1).list();
        int size = inventoryStocks.size();
        int operation = 0;
        Set<Long> positionIds = new HashSet<>();
        Set<Long> skuIds = new HashSet<>();
        int shopCartNum = 0;


        for (InventoryStock inventoryStock : inventoryStocks) {
            if (inventoryStock.getStatus() != 0) {
                operation = operation + 1;
            }
            positionIds.add(inventoryStock.getPositionId());
            skuIds.add(inventoryStock.getSkuId());
            //   shopCartService.query().eq("type","StocktakingError").eq("")
        }

        List<Long> anomalyIds = new ArrayList<>();
        if (ToolUtil.isNotEmpty(inventory.getMethod()) && inventory.getMethod().equals("OpenDisc")) {
            for (InventoryStock inventoryStock : inventoryStocks) {         //明盘购物车角标数量
                if (inventoryStock.getLockStatus() != 99 && ToolUtil.isNotEmpty(inventoryStock.getAnomalyId()) && inventoryStock.getAnomalyId() != 0) {
                    anomalyIds.add(inventoryStock.getAnomalyId());
                }
            }
            shopCartNum = anomalyIds.size() == 0 ? 0 : anomalyService.query().in("anomaly_id", anomalyIds).eq("status", 0).count();
        } else {
            //TODO 暗盘购物车角标 不显示 数量异常
            for (InventoryStock inventoryStock : inventoryStocks) {
                if (inventoryStock.getLockStatus() != 99 && ToolUtil.isNotEmpty(inventoryStock.getAnomalyId()) && inventoryStock.getAnomalyId() != 0) {
                    anomalyIds.add(inventoryStock.getAnomalyId());
                }
            }
            shopCartNum = anomalyIds.size() == 0 ? 0 : anomalyService.query().in("anomaly_id", anomalyIds).eq("status", 0).count();
        }


        Map<String, Integer> map = new HashMap<>();
        map.put("total", size);
        map.put("handle", operation);
        map.put("positionNum", positionIds.size());
        map.put("skuNum", skuIds.size());
        map.put("shopCartNum", shopCartNum);
        return map;
    }


    @Override
    public Map<String, Object> orderDetail(Long instockOrderId) {

        List<InstockHandle> instockHandles = this.instockHandleService.query().eq("instock_order_id", instockOrderId).eq("display", 1).list();
        List<InstockHandleResult> instockHandleResults = BeanUtil.copyToList(instockHandles, InstockHandleResult.class);
        List<InstockHandleResult> detailTotalList = new ArrayList<>();

        instockHandleResults.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + "_" + item.getBrandId() + "_" + item.getType() + "_" + item.getCustomerId(), Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new InstockHandleResult() {{
                        setNumber(a.getNumber() + b.getNumber());
                        setSkuId(a.getSkuId());
                        setBrandId(a.getBrandId());
                        setType(a.getType());
                        setCustomerId(a.getCustomerId());
                    }}).ifPresent(detailTotalList::add);
                }
        );

        /**
         * 排序
         */
        List<InstockHandleResult> collect = detailTotalList.stream().sorted(Comparator.comparing(InstockHandleResult::getSkuId).reversed()).collect(Collectors.toList());
        this.instockHandleService.format(collect);

        Map<String, Object> map = new HashMap<>();

        for (InstockHandleResult instockHandleResult : collect) {
            map.put("${供方公司名称}", instockHandleResult.getCustomerResult().getCustomerName());
            map.put("${产品名称}", instockHandleResult.getSkuResult().getSpuResult().getName());
            map.put("${型号规格}", instockHandleResult.getSkuResult().getSkuName());

            if (ToolUtil.isEmpty(instockHandleResult.getBrandResult())) {
                BrandResult brandResult = new BrandResult();
                brandResult.setBrandName("无品牌");
                instockHandleResult.setBrandResult(brandResult);
            }
            map.put("${品牌}", instockHandleResult.getBrandResult().getBrandName());
            map.put("${数量}", instockHandleResult.getNumber());
        }

        return map;

    }


    /**
     * 组合结构
     *
     * @return
     */
    @Override

    public List<StorehousePositionsResult> positionsResultList(List<InventoryStockResult> detailResults) {
        Set<Long> positionIds = new HashSet<>();

        for (InventoryStockResult result : detailResults) {
            if (ToolUtil.isEmpty(result.getBrandId())) {
                result.setBrandId(0L);
            }
            if (ToolUtil.isEmpty(result.getPositionId())) {
                result.setPositionId(0L);
            }
            positionIds.add(result.getPositionId());
        }

        /**
         * 通过库位组合
         */
        Map<Long, List<InventoryStockResult>> positionMap = new HashMap<>();

        for (Long positionId : positionIds) {
            List<InventoryStockResult> details = new ArrayList<>();
            for (InventoryStockResult result : detailResults) {
                if (result.getPositionId().equals(positionId)) {
                    details.add(result);
                }
            }
            positionMap.put(positionId, details);
        }

        List<StorehousePositionsResult> positionsResultList = new ArrayList<>();
        for (Long positionId : positionIds) {
            List<InventoryStockResult> detailResultList = positionMap.get(positionId);
            Set<Long> skuIds = new HashSet<>();

            Map<Long, Integer> lock = new HashMap<>();
            Map<Long, Integer> status = new HashMap<>();
            Map<Long, Long> anomalyId = new HashMap<>();
            Map<Long, Long> inventoryStockId = new HashMap<>();
            Map<Long, Integer> number = new HashMap<>();

            for (InventoryStockResult result : detailResultList) {
                skuIds.add(result.getSkuId());
                lock.put(result.getSkuId(), result.getLockStatus());
                status.put(result.getSkuId(), result.getStatus());
                anomalyId.put(result.getSkuId(), result.getAnomalyId());
                inventoryStockId.put(result.getSkuId(), result.getInventoryStockId());
                if (result.getStatus().equals(0)) {
                    Integer stockNum = stockDetailsService.getNumberByStock(result.getSkuId(), null, positionId);
                    if (ToolUtil.isEmpty(stockNum)) {
                        stockNum = 0;
                    }
                    result.setRealNumber(Long.valueOf(stockNum));
                }
                number.put(result.getSkuId(), Math.toIntExact(result.getRealNumber()));
            }

            List<SkuResult> list = skuService.formatSkuResult(new ArrayList<>(skuIds));
            for (SkuResult skuResult : list) {
                Integer lockStatus = lock.get(skuResult.getSkuId());
                skuResult.setInventoryStatus(status.get(skuResult.getSkuId()));
                skuResult.setLockStatus(lockStatus);
                skuResult.setAnomalyId(anomalyId.get(skuResult.getSkuId()));
                skuResult.setStockNumber(number.get(skuResult.getSkuId()));
                skuResult.setInventoryStockId(inventoryStockId.get(skuResult.getSkuId()));
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
     * 通过异常id  修改盘点状态
     *
     * @param ids
     */
    @Override
    public void updateStatus(List<Long> ids) {

        List<InventoryStock> inventoryStocks = ids.size() == 0 ? new ArrayList<>() :
                this.query().in("anomaly_id", ids)
                        .eq("display", 1).list();

        Set<Long> inventoryIds = new HashSet<>();
        for (InventoryStock inventoryStock : inventoryStocks) {
            inventoryStock.setLockStatus(99);
            inventoryIds.add(inventoryStock.getInventoryId());
        }
        this.updateBatchById(inventoryStocks);

        for (Long inventoryId : inventoryIds) {    //添加动态
            shopCartService.addDynamic(inventoryId, "提交了异常描述");
        }
    }

    @Override
    public void updateInventoryStatus(AnomalyParam param, int status) {
        QueryWrapper<InventoryStock> queryWrapper = new QueryWrapper<>();
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
        queryWrapper.eq("display", 1);
        queryWrapper.in("inventory_id", inventoryIds);
        queryWrapper.eq("sku_id", param.getSkuId());
        if (ToolUtil.isNotEmpty(param.getBrandId())) {
            queryWrapper.eq("brand_id", param.getBrandId());
        }
        queryWrapper.eq("position_id", param.getPositionId());
        queryWrapper.ne("lock_status", 99);


        List<InventoryStock> inventoryStocks = this.list(queryWrapper);
        List<InventoryStock> stockList = BeanUtil.copyToList(inventoryStocks, InventoryStock.class, new CopyOptions());

        if (ToolUtil.isEmpty(param.getFormId())) {    // formId 为空  : 及时盘点提交异常
            anomalyService.addInventoryRecord(param, 0L, status);   //及时盘点添加记录
        }

        for (InventoryStock inventoryStock : inventoryStocks) {
            //保留之前记录
            if (inventoryStock.getLockStatus() == 99) {                                                             //普通盘点
                throw new ServiceException(500, "当前状态不可更改");
            }
            inventoryStock.setStatus(status);
            inventoryStock.setAnomalyId(param.getAnomalyId());
            inventoryStock.setDisplay(1);
            inventoryStock.setRealNumber(param.getRealNumber());
            if (status == 1) {   //正常物料    清楚异常id
                inventoryStock.setAnomalyId(0L);    //添加盘点记录
                anomalyService.addInventoryRecord(param, inventoryStock.getInventoryId(), status);
            } else {
                anomalyService.addInventoryRecord(param, inventoryStock.getInventoryId(), -1);
            }
        }

        for (InventoryStock inventoryStock : stockList) {
            inventoryStock.setInventoryStockId(null);
            inventoryStock.setDisplay(0);
        }

        this.updateBatchById(inventoryStocks);
        this.saveBatch(stockList);

        //添加动态
        String skuMessage = skuService.skuMessage(param.getSkuId());
        String content = "";
        if (status == 2) {
            content = "暂存了" + skuMessage + "的盘点结果";
        } else {
            content = "对" + skuMessage + "进行了盘点";
        }
        Set<Long> inventoryIdsSet = new HashSet<>(inventoryIds);
        for (Long inventoryId : inventoryIdsSet) {
            shopCartService.addDynamic(inventoryId, content);
        }

    }


    @Override
    public List<InventoryStockResult> findListBySpec(InventoryStockParam param) {
        return null;
    }

    @Override
    public PageInfo<InventoryStockResult> findPageBySpec(InventoryStockParam param) {
        matchingData(param.getInventoryId());  //开始盘点 同步数据

        if (ToolUtil.isNotEmpty(param.getPositionId())) {  //库位查询
            List<Long> child = positionsService.getEndChild(param.getPositionId());
            param.setPositionIds(child);
        }

        Page<InventoryStockResult> pageContext = getPageContext();
        pageContext.setOrders(null);
        IPage<InventoryStockResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());

        List<InventoryStockResult> inventoryStockResults = this.baseMapper.customList(param);
        List<Long> positionIds = new ArrayList<>();
        for (InventoryStockResult inventoryStockResult : inventoryStockResults) {
            positionIds.add(inventoryStockResult.getPositionId());
        }
        PageInfo<InventoryStockResult> pageInfo = PageFactory.createPageInfo(page);
        pageInfo.setSearch(positionIds);
        return pageInfo;
    }

    private Serializable getKey(InventoryStockParam param) {
        return param.getInventoryStockId();
    }

    private Page<InventoryStockResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private InventoryStock getOldEntity(InventoryStockParam param) {
        return this.getById(getKey(param));
    }

    private InventoryStock getEntity(InventoryStockParam param) {
        InventoryStock entity = new InventoryStock();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public void format(List<InventoryStockResult> data) {

        List<Long> skuIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        List<Long> positionIds = new ArrayList<>();
        List<Long> anomalyIds = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();


        for (InventoryStockResult datum : data) {
            skuIds.add(datum.getSkuId());
            brandIds.add(datum.getBrandId());
            positionIds.add(datum.getPositionId());
            anomalyIds.add(datum.getAnomalyId());
            userIds.add(datum.getUpdateUser());
        }

        List<User> userList = userIds.size() == 0 ? new ArrayList<>() : userService.listByIds(userIds);
        List<AnomalyDetail> anomalyDetails = anomalyIds.size() == 0 ? new ArrayList<>() : anomalyDetailService.query().in("anomaly_id", anomalyIds).eq("display", 1).list();
        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);
        List<BrandResult> brandResults = brandService.getBrandResults(brandIds);
        List<StorehousePositionsResult> positionsResults = positionsService.details(positionIds);

        positionsService.format(positionsResults);

        for (InventoryStockResult datum : data) {

            Integer lockNumber = pickListsCartService.getLockNumber(new QuerryLockedParam() {{
                setBrandId(datum.getBrandId());
                setPositionId(datum.getPositionId());
                setSkuId(datum.getSkuId());
            }});

            datum.setLockNumber(lockNumber);

            Integer number = stockDetailsService.getNumberByStock(datum.getSkuId(), datum.getBrandId(), datum.getPositionId());
            if (ToolUtil.isEmpty(number)) {
                number = 0;
            }

            datum.setNumber(Long.valueOf(number));

            for (SkuResult skuResult : skuResults) {
                if (datum.getSkuId().equals(skuResult.getSkuId())) {
                    datum.setSkuResult(skuResult);
                    break;
                }
            }

            for (BrandResult brandResult : brandResults) {
                if (ToolUtil.isNotEmpty(datum.getBrandId()) && brandResult.getBrandId().equals(datum.getBrandId())) {
                    datum.setBrandResult(brandResult);
                    break;
                }
            }


            for (StorehousePositionsResult positionsResult : positionsResults) {
                if (ToolUtil.isNotEmpty(datum.getPositionId()) && datum.getPositionId().equals(positionsResult.getStorehousePositionsId())) {
                    datum.setPositionsResult(positionsResult);
                    break;
                }
            }

            int errorNum = 0;
            for (AnomalyDetail anomalyDetail : anomalyDetails) {
                if (ToolUtil.isNotEmpty(datum.getAnomalyId()) && datum.getAnomalyId().equals(anomalyDetail.getAnomalyId())) {
                    errorNum = errorNum + 1;
                }
            }
            datum.setErrorNum(errorNum);


            for (User user : userList) {    //这个地方取修改人  取最后执行的人   当做记录
                if (ToolUtil.isNotEmpty(datum.getUpdateUser()) && datum.getUpdateUser().equals(user.getUserId())) {
                    String imgUrl = stepsService.imgUrl(user.getUserId().toString());
                    user.setAvatar(imgUrl);
                    datum.setUser(user);
                    break;
                }
            }
        }

    }
}
