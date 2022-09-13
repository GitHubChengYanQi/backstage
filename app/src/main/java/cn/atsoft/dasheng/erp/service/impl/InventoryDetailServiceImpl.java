package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.SkuRequest;
import cn.atsoft.dasheng.app.pojo.AddStockParam;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.app.service.StockService;
import cn.atsoft.dasheng.appBase.service.MediaService;
import cn.atsoft.dasheng.base.auth.context.LoginContext;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.model.params.AnomalyOrderParam;
import cn.atsoft.dasheng.erp.model.params.AnomalyParam;
import cn.atsoft.dasheng.erp.model.result.InventoryResult;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.erp.mapper.InventoryDetailMapper;
import cn.atsoft.dasheng.erp.model.params.InventoryDetailParam;
import cn.atsoft.dasheng.erp.model.result.InventoryDetailResult;
import cn.atsoft.dasheng.erp.pojo.InventoryRequest;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.service.ActivitiProcessLogService;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
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
import java.util.stream.Stream;

/**
 * <p>
 * 盘点任务详情 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-27
 */
@Service
public class InventoryDetailServiceImpl extends ServiceImpl<InventoryDetailMapper, InventoryDetail> implements InventoryDetailService {

    @Autowired
    private InkindService inkindService;
    @Autowired
    private StockDetailsService detailsService;
    @Autowired
    private StorehousePositionsService positionsService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private MediaService mediaService;
    @Autowired
    private StockService stockService;
    @Autowired
    private AnomalyOrderService anomalyOrderService;
    @Autowired
    private ActivitiProcessTaskService taskService;
    @Autowired
    private ActivitiProcessLogService activitiProcessLogService;
    @Autowired
    private SpuClassificationService spuClassificationService;
    @Autowired
    private AnomalyService anomalyService;
    @Autowired
    private InventoryStockService inventoryStockService;
    @Autowired
    private SpuService spuService;

    @Override
    public void add(InventoryDetailParam param) {
        InventoryDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(InventoryDetailParam param) {
        InventoryDetail entity = getEntity(param);
        entity.setDisplay(0);
        this.updateById(entity);
        /*this.removeById(getKey(param));*/
    }


    public void inventory(InventoryRequest inventoryRequest) {
        List<InventoryRequest.InkindParam> params = inventoryRequest.getInkindParams();
        List<Long> inkindIds = new ArrayList<>();

        for (InventoryRequest.InkindParam param : params) {
            inkindIds.add(param.getInkindId());
        }
        List<StockDetails> details = detailsService.query().in("inkind_id", inkindIds).list();

        List<InventoryDetail> inventories = new ArrayList<>();
        InventoryDetail inventory = null;

        //添加盘点数据----------------------------------------------------------------------------------------------------
        for (StockDetails detail : details) {
            for (InventoryRequest.InkindParam param : params) {
                if (detail.getInkindId().equals(param.getInkindId())) {  //相同实物
                    if (detail.getNumber() > param.getNumber()) {  //出库
                        inventory = new InventoryDetail();
                        inventory.setInkindId(param.getInkindId());
                        inventory.setStatus(2);
                        inventories.add(inventory);
                    } else {                                       //入库
                        inventory = new InventoryDetail();
                        inventory.setInkindId(param.getInkindId());
                        inventory.setStatus(1);
                        inventories.add(inventory);
                    }
                    detail.setNumber(param.getNumber());
                }
            }
        }
        this.saveBatch(inventories);

    }


    @Override
    public Object taskList(Long inventoryTaskId) {
        List<InventoryDetail> inventoryDetails = this.query().eq("inventory_id", inventoryTaskId).eq("display", 1).list();
        List<InventoryDetailResult> detailResults = BeanUtil.copyToList(inventoryDetails, InventoryDetailResult.class, new CopyOptions());
        return positionsResultList(detailResults);
    }

    @Override
    public List<InventoryDetailResult> details(Long inventoryTaskId) {
        List<InventoryDetail> inventoryDetails = this.query().eq("inventory_id", inventoryTaskId).eq("display", 1).list();
        List<InventoryDetailResult> detailResults = BeanUtil.copyToList(inventoryDetails, InventoryDetailResult.class, new CopyOptions());
        this.format(detailResults);
        detailList(detailResults);
        return detailResults;
    }


    private void detailList(List<InventoryDetailResult> detailResults) {
        for (InventoryDetailResult detailResult : detailResults) {
            if (detailResult.getStatus() == 0) {
                Integer number = detailsService.getNumberByStock(detailResult.getSkuId(), detailResult.getBrandId(), detailResult.getPositionId());
                if (ToolUtil.isEmpty(number)) {
                    number = 0;
                }
                detailResult.setSkuNum(number);
            }
        }
    }

    /**
     * 组合结构
     *
     * @return
     */
    private List<StorehousePositionsResult> positionsResultList(List<InventoryDetailResult> detailResults) {
        Set<Long> positionIds = new HashSet<>();
        for (InventoryDetailResult inventoryDetail : detailResults) {
            if (ToolUtil.isEmpty(inventoryDetail.getBrandId())) {
                inventoryDetail.setBrandId(0L);
            }
            if (ToolUtil.isEmpty(inventoryDetail.getPositionId())) {
                inventoryDetail.setPositionId(0L);
            }
            positionIds.add(inventoryDetail.getPositionId());
        }

        /**
         * 通过库位组合
         */
        Map<Long, List<InventoryDetailResult>> positionMap = new HashMap<>();

        for (Long positionId : positionIds) {
            List<InventoryDetailResult> details = new ArrayList<>();
            for (InventoryDetailResult inventoryDetail : detailResults) {
                if (inventoryDetail.getPositionId().equals(positionId)) {
                    details.add(inventoryDetail);
                }
            }
            positionMap.put(positionId, details);
        }

        List<StorehousePositionsResult> positionsResultList = new ArrayList<>();
        for (Long positionId : positionIds) {
            List<InventoryDetailResult> detailResultList = positionMap.get(positionId);
            Set<Long> skuIds = new HashSet<>();
            Map<Long, List<Long>> skuMap = new HashMap<>();
            Map<Long, Integer> lock = new HashMap<>();

            for (InventoryDetailResult inventoryDetailResult : detailResultList) {
                skuIds.add(inventoryDetailResult.getSkuId());
                if (ToolUtil.isNotEmpty(inventoryDetailResult.getEnclosure())) {
                    skuMap.put(inventoryDetailResult.getSkuId(), JSON.parseArray(inventoryDetailResult.getEnclosure(), Long.class));
                }
                lock.put(inventoryDetailResult.getSkuId(), inventoryDetailResult.getLockStatus());
            }

//            Map<Long, List<BrandResult>> brandMap = new HashMap<>();
//            for (Long skuId : skuIds) {
//                List<BrandResult> brandResults = new ArrayList<>();
//                for (InventoryDetailResult inventoryDetailResult : detailResultList) {
//                    if (inventoryDetailResult.getSkuId().equals(skuId)) {
//                        BrandResult brandResult = new BrandResult();
//                        brandResult.setBrandId(inventoryDetailResult.getBrandId());
//                        brandResult.setNumber(inventoryDetailResult.getNumber());
//                        brandResult.setInkind(inventoryDetailResult.getInkindId());
//                        brandResult.setAnomalyId(inventoryDetailResult.getAnomalyId());
//                        brandResult.setInventoryStatus(inventoryDetailResult.getStatus());
//                        if (mergeBrand(brandResults, brandResult)) {
//                            brandResults.add(brandResult);
//                        }
//                    }
//                }
//                brandFormat(brandResults);
//                brandMap.put(skuId, brandResults);
//            }

            List<SkuResult> list = skuService.formatSkuResult(new ArrayList<>(skuIds));
            for (SkuResult skuResult : list) {

                List<Long> mediaIds = skuMap.get(skuResult.getSkuId());
                if (ToolUtil.isNotEmpty(mediaIds)) {
                    List<String> mediaUrls = mediaService.getMediaUrls(mediaIds, null);
                    skuResult.setInventoryUrls(mediaUrls);
                    skuResult.setMediaIds(mediaIds);
                }
                Integer lockStatus = lock.get(skuResult.getSkuId());
                skuResult.setLockStatus(lockStatus);
//                skuResult.setBrandResults(brandMap.get(skuResult.getSkuId()));
            }
            StorehousePositionsResult positionsResult = new StorehousePositionsResult();
            positionsResult.setSkuResultList(list);
            positionsResult.setStorehousePositionsId(positionId);
            positionsResultList.add(positionsResult);
        }

        positionFormat(positionsResultList);
        return positionsResultList;
    }

    /**
     * 相同品牌累加数量
     *
     * @param brandResults
     * @param brandResult
     * @return
     */
    @Override
    public boolean mergeBrand(List<BrandResult> brandResults, BrandResult brandResult) {
        for (BrandResult result : brandResults) {
            if (brandResult.getBrandId().equals(result.getBrandId())) {
                result.setNumber(result.getNumber() + brandResult.getNumber());
                return false;
            }
        }
        return true;
    }

    @Override
    @Transactional
    public void addPhoto(InventoryDetailParam inventoryDetailParam) {
        if (ToolUtil.isEmpty(inventoryDetailParam.getPositionId()) || ToolUtil.isEmpty(inventoryDetailParam.getSkuId())) {
            throw new ServiceException(500, "缺少参数");
        }
        List<InventoryResult> inventoryResults = inventoryService.listByTime();
        List<Long> inventoryIds = new ArrayList<>();
        for (InventoryResult inventoryResult : inventoryResults) {
            inventoryIds.add(inventoryResult.getInventoryTaskId());
        }


        inventoryIds.add(inventoryDetailParam.getInventoryId());
        List<InventoryDetail> inventoryDetails = this.query().eq("position_id", inventoryDetailParam.getPositionId())
                .eq("sku_id", inventoryDetailParam.getSkuId())
                .in("inventory_id", inventoryIds)
                .eq("display", 1).list();

        for (InventoryDetail inventoryDetail : inventoryDetails) {
            inventoryDetail.setEnclosure(inventoryDetailParam.getEnclosure());
        }
        this.updateBatchById(inventoryDetails);
    }

    /**
     * 临时锁状态
     */
    @Override
    public void temporaryLock(InventoryDetailParam param) {

        List<InventoryResult> inventoryResults = inventoryService.listByTime();
        List<Long> inventoryIds = new ArrayList<>();
        for (InventoryResult inventoryResult : inventoryResults) {
            inventoryIds.add(inventoryResult.getInventoryTaskId());
        }
        inventoryIds.add(param.getInventoryId());

        List<InventoryDetail> inventoryDetails = this.lambdaQuery()
                .eq(InventoryDetail::getPositionId, param.getPositionId())
                .in(InventoryDetail::getInventoryId, inventoryIds)
                .eq(InventoryDetail::getSkuId, param.getSkuId())
                .eq(InventoryDetail::getDisplay, 1).list();

        for (InventoryDetail inventoryDetail : inventoryDetails) {
            if (inventoryDetail.getLockStatus() == 99) {
                throw new ServiceException(500, "当前状态不可更改");
            }
            inventoryDetail.setLockStatus(98);
        }
        this.updateBatchById(inventoryDetails);
    }

    /**
     * 盘点入库
     *
     * @param
     */
    @Override
    public void inventoryInstock(InventoryDetailParam inventoryDetailParam) {
        Inkind inkind = inkindService.getById(inventoryDetailParam.getInkindId());
        if (ToolUtil.isEmpty(inkind)) {
            throw new ServiceException(500, "无此物");
        }
        StockDetails details = detailsService.query().eq("inkind_id", inkind.getInkindId()).one();
        if (ToolUtil.isEmpty(details)) {
            stockService.addDetails(new AddStockParam(inkind.getSkuId(), inkind.getBrandId(), inventoryDetailParam.getStoreHouseId(),
                    inventoryDetailParam.getPositionId(), inventoryDetailParam.getNumber(), inventoryDetailParam.getQrcodeId(),
                    inkind.getInkindId()
            ));

            //修改实物数量

            inkind.setNumber(inventoryDetailParam.getNumber());
            inkind.setType("1");
            inkindService.updateById(inkind);

            InventoryDetail inventoryDetail = new InventoryDetail();
            inventoryDetail.setInkindId(inkind.getInkindId());
            inventoryDetail.setStatus(1);
            this.save(inventoryDetail);
        } else {   //已在库存 修改数量
            StockDetails stockDetails = new StockDetails();
            stockDetails.setNumber(inventoryDetailParam.getNumber());
            detailsService.update(stockDetails, new QueryWrapper<StockDetails>() {{
                eq("inkind_id", inkind.getInkindId());
            }});
            stockService.updateNumber(new ArrayList<Long>() {{
                add(details.getStockId());
            }});
            inkind.setNumber(inventoryDetailParam.getNumber());
            inkind.setType("1");
            inkindService.updateById(inkind);
        }

    }

    @Transactional
    @Override
    public void complete(Long inventoryId) {

        jurisdiction(inventoryId);   //判断权限

        List<InventoryStock> inventoryStocks = inventoryStockService.query().eq("inventory_id", inventoryId).eq("display", 1).list();
        Inventory inventory = inventoryService.getById(inventoryId);

        if (ToolUtil.isEmpty(inventory)) {
            throw new ServiceException(500, "盘点不存在");
        }

        List<Long> anomalyIds = new ArrayList<>();
        for (InventoryStock inventoryStock : inventoryStocks) {
            if (inventoryStock.getStatus() == 0) {
                throw new ServiceException(500, "有未操作物料");
            }
            if (ToolUtil.isNotEmpty(inventoryStock.getAnomalyId())) {
                anomalyIds.add(inventoryStock.getAnomalyId());
            }
        }

        if (ToolUtil.isNotEmpty(inventory.getMethod()) && inventory.getMethod().equals("OpenDisc")) {
            List<Anomaly> anomalies = anomalyIds.size() == 0 ? new ArrayList<>() : anomalyService.listByIds(anomalyIds);
            for (Anomaly anomaly : anomalies) {
                if (anomaly.getStatus() == 0) {
                    throw new ServiceException(500, "请先提交异常");
                }
            }
        } else {            //暗盘 自动提交异常
            List<Anomaly> anomalies = anomalyService.query().eq("form_id", inventoryId).eq("display", 1)
                    .list();   //除去正常的物料
            List<AnomalyParam> anomalyParams = new ArrayList<>();
            for (Anomaly anomaly : anomalies) {
                if (!anomaly.getRealNumber().equals(anomaly.getNeedNumber())) {
                    AnomalyParam anomalyParam = new AnomalyParam();
                    anomalyParam.setAnomalyId(anomaly.getAnomalyId());
                    anomalyParams.add(anomalyParam);
                }
            }
            if (anomalyParams.size() > 0) {
                AnomalyOrderParam anomalyOrderParam = new AnomalyOrderParam();
                anomalyOrderParam.setType("Stocktaking");
                anomalyOrderParam.setAnomalyParams(anomalyParams);
                anomalyOrderParam.setMessage("盘点");
                anomalyOrderService.addByInventory(anomalyOrderParam);
            }
        }

        inventory.setComplete(99);
        inventoryService.updateById(inventory);

        /**
         * 流程
         */
        ActivitiProcessTask processTask = taskService.getByFormId(inventory.getInventoryTaskId());
        activitiProcessLogService.autoAudit(processTask.getProcessTaskId(), 1, LoginContextHolder.getContext().getUserId());
    }

    /**
     * 盘点操作权限
     *
     * @param inventoryId
     */
    @Override
    public void jurisdiction(Long inventoryId) {
        List<Long> userIds = new ArrayList<>();
        Inventory inventory = inventoryService.getById(inventoryId);
        if (ToolUtil.isEmpty(inventory)) {    //及时盘点
            return;
        }
        if (ToolUtil.isNotEmpty(inventory.getParticipants())) {
            userIds.addAll(JSON.parseArray(inventory.getParticipants(), Long.class));
        }
        userIds.add(inventory.getUserId());

        boolean t = true;
        Long id = LoginContextHolder.getContext().getUserId();
        for (Long userId : userIds) {
            if (ToolUtil.isNotEmpty(userId) && userId.equals(id)) {
                t = false;
                break;
            }
        }
        if (t) {
            throw new ServiceException(500, "你没有盘点资格");
        }
    }

    @Override
    public void update(InventoryDetailParam param) {
        InventoryDetail oldEntity = getOldEntity(param);
        InventoryDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public InventoryDetailResult findBySpec(InventoryDetailParam param) {
        return null;
    }

    @Override
    public List<InventoryDetailResult> findListBySpec(InventoryDetailParam param) {
        return null;
    }

    @Override
    public PageInfo<InventoryDetailResult> findPageBySpec(InventoryDetailParam param) {
        Page<InventoryDetailResult> pageContext = getPageContext();
        IPage<InventoryDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(InventoryDetailParam param) {
        return param.getDetailId();
    }

    private Page<InventoryDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private InventoryDetail getOldEntity(InventoryDetailParam param) {
        return this.getById(getKey(param));
    }

    private InventoryDetail getEntity(InventoryDetailParam param) {
        InventoryDetail entity = new InventoryDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public void brandFormat(List<BrandResult> brandResults) {
        List<Long> brandIds = new ArrayList<>();
        for (BrandResult brandResult : brandResults) {
            if (ToolUtil.isEmpty(brandResult.getBrandId())) {
                brandResult.setBrandId(0L);
            }
            brandIds.add(brandResult.getBrandId());
        }
        List<Brand> brandList = brandIds.size() == 0 ? new ArrayList<>() : brandService.listByIds(brandIds);
        for (BrandResult brandResult : brandResults) {
            for (Brand brand : brandList) {
                if (brandResult.getBrandId().equals(brand.getBrandId())) {
                    brandResult.setBrandName(brand.getBrandName());
                    break;
                }
            }
        }
    }

    @Override
    public List<Long> getSkuIds(List<Long> classIds) {
        return this.baseMapper.getSkuIds(classIds);
    }

    @Override
    public void positionFormat(List<StorehousePositionsResult> positionsResults) {
        List<Long> ids = new ArrayList<>();
        for (StorehousePositionsResult positionsResult : positionsResults) {
            ids.add(positionsResult.getStorehousePositionsId());
        }
        List<StorehousePositions> positions = ids.size() == 0 ? new ArrayList<>() : positionsService.listByIds(ids);
        for (StorehousePositions position : positions) {
            for (StorehousePositionsResult positionsResult : positionsResults) {
                if (position.getStorehousePositionsId().equals(positionsResult.getStorehousePositionsId())) {
                    positionsResult.setName(position.getName());
                    positionsResult.setStorehouseId(position.getStorehouseId());
                    break;
                }
            }
        }
    }

    @Override
    public List<InventoryDetailResult> getDetails(List<Long> inventoryIds) {
        List<InventoryDetail> inventoryDetails = inventoryIds.size() == 0 ? new ArrayList<>() : this.query().in("inventory_id", inventoryIds).eq("display", 1).list();
        List<InventoryDetailResult> detailResults = BeanUtil.copyToList(inventoryDetails, InventoryDetailResult.class, new CopyOptions());
        this.format(detailResults);
        return detailResults;
    }

    @Override
    public void format(List<InventoryDetailResult> data) {

        List<Long> skuIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        List<Long> positionIds = new ArrayList<>();
        List<Long> classIds = new ArrayList<>();
        List<Long> spuIds = new ArrayList<>();
        for (InventoryDetailResult datum : data) {
            skuIds.add(datum.getSkuId());
            brandIds.add(datum.getBrandId());
            positionIds.add(datum.getPositionId());

            if (ToolUtil.isNotEmpty(datum.getClassCondition())) {
                datum.setClassIds(JSON.parseArray(datum.getClassCondition(), Long.class));
                classIds.addAll(datum.getClassIds());
            }
            if (ToolUtil.isNotEmpty(datum.getPositionCondition())) {
                datum.setPositionIds(JSON.parseArray(datum.getPositionCondition(), Long.class));
                positionIds.addAll(datum.getPositionIds());
            }
            if (ToolUtil.isNotEmpty(datum.getBrandCondition())) {
                datum.setBrandIds(JSON.parseArray(datum.getBrandCondition(), Long.class));
                brandIds.addAll(datum.getBrandIds());
            }
            spuIds.add(datum.getSpuId());
        }

        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);
        List<BrandResult> brandResults = brandService.getBrandResults(brandIds);
        List<StorehousePositions> positions = positionIds.size() == 0 ? new ArrayList<>() : positionsService.listByIds(positionIds);
        List<StorehousePositionsResult> positionsResultList = BeanUtil.copyToList(positions, StorehousePositionsResult.class);
        List<SpuClassification> spuClassifications = classIds.size() == 0 ? new ArrayList<>() : spuClassificationService.listByIds(classIds);
        List<Spu> spus = spuIds.size() == 0 ? new ArrayList<>() : spuService.listByIds(spuIds);

        for (InventoryDetailResult datum : data) {


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

            for (StorehousePositionsResult positionsResult : positionsResultList) {
                if (ToolUtil.isNotEmpty(datum.getPositionId()) && datum.getPositionId().equals(positionsResult.getStorehousePositionsId())) {
                    datum.setPositionsResult(positionsResult);
                    break;
                }
            }

            List<String> condition = new ArrayList<>();
            if (ToolUtil.isNotEmpty(datum.getBrandIds())) {
                for (Long brandId : datum.getBrandIds()) {
                    for (BrandResult brandResult : brandResults) {
                        if (brandResult.getBrandId().equals(brandId)) {
                            condition.add(brandResult.getBrandName());
                        }
                    }
                }
            }

            if (ToolUtil.isNotEmpty(datum.getPositionIds())) {
                for (Long positionId : datum.getPositionIds()) {
                    for (StorehousePositionsResult positionsResult : positionsResultList) {
                        if (positionId.equals(positionsResult.getStorehousePositionsId())) {
                            condition.add(positionsResult.getName());
                        }
                    }
                }
            }

            if (ToolUtil.isNotEmpty(datum.getClassIds())) {
                for (Long classId : datum.getClassIds()) {
                    for (SpuClassification spuClassification : spuClassifications) {
                        if (classId.equals(spuClassification.getSpuClassificationId())) {
                            condition.add(spuClassification.getName());
                        }
                    }
                }
            }

            for (Spu spu : spus) {
                if (ToolUtil.isNotEmpty(datum.getSpuId()) && datum.getSpuId().equals(spu.getSpuId())) {
                    condition.add(spu.getName());
                }
            }

            datum.setCondition(condition);
        }

    }
}
