package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.mapper.ShopCartMapper;
import cn.atsoft.dasheng.erp.model.params.ShopCartParam;
import cn.atsoft.dasheng.erp.model.result.AnomalyResult;
import cn.atsoft.dasheng.erp.model.result.ShopCartResult;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.erp.pojo.AnomalyType;
import cn.atsoft.dasheng.erp.pojo.PositionNum;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.entity.Remarks;
import cn.atsoft.dasheng.form.model.params.RemarksParam;
import cn.atsoft.dasheng.form.service.ActivitiAuditService;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.form.service.RemarksService;
import cn.atsoft.dasheng.message.producer.MessageProducer;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.*;

/**
 * <p>
 * 购物车 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-06-06
 */
@Service
public class ShopCartServiceImpl extends ServiceImpl<ShopCartMapper, ShopCart> implements ShopCartService {
    @Autowired
    private SkuService skuService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private InstockListService instockListService;
    @Autowired
    private AnomalyService anomalyService;
    @Autowired
    private StorehousePositionsService positionsService;
    @Autowired
    private AnomalyDetailService anomalyDetailService;
    @Autowired
    private StorehousePositionsService storehousePositionsService;
    @Autowired
    private ActivitiProcessTaskService activitiProcessTaskService;
    @Autowired
    private MessageProducer messageProducer;
    @Autowired
    private ActivitiAuditService auditService;
    @Autowired
    private InstockOrderService instockOrderService;
    @Autowired
    private ActivitiProcessTaskService taskService;
    @Autowired
    private RemarksService remarksService;
    @Autowired
    private SkuBrandBindService brandBindService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private InventoryDetailService inventoryDetailService;

    protected static final Logger logger = LoggerFactory.getLogger(ShopCartServiceImpl.class);

    @Override
    @Transactional
    public Long add(ShopCartParam param) {
        judgeBrand(param.getSkuId(), param.getBrandId());  //判断物料品牌绑定
        Date date = new DateTime();
        logger.info(date + ": 添加带入购物车--->" + param.getInstockListId());

        if (ToolUtil.isNotEmpty(param.getInstockListId())) {
            Integer count = this.query().eq("form_id", param.getInstockListId()).eq("display", 1).count();
            if (ToolUtil.isNotEmpty(count) && count > 0) {
                throw new ServiceException(500, "当前已操作");
            }
        }

        if (ToolUtil.isNotEmpty(param.getPositionNums())) {     //多个库位
            String json = JSON.toJSONString(param.getPositionNums());
            param.setStorehousePositionsId(json);
        }
        ShopCart entity = getEntity(param);
        this.save(entity);

        if (ToolUtil.isNotEmpty(param.getInstockListId())) {
            inventoryService.staticState();  //静态盘点判断
            updateInStockListStatus(param.getInstockListId(), param.getFormStatus(), entity.getNumber());
            InstockList instockList = instockListService.getById(param.getInstockListId());
            String skuMessage = skuService.skuMessage(instockList.getSkuId());
            addDynamic(instockList.getInstockOrderId(), skuMessage + "核实完成准备入库");
        }

        return entity.getCartId();
    }

    /**
     * 判断物料品牌绑定关系
     */
    private void judgeBrand(Long skuId, Long brandId) {
        if (ToolUtil.isNotEmpty(brandId) && brandId != 0) {
            Integer count = brandBindService.query().eq("sku_id", skuId).eq("brand_id", brandId).eq("display", 1).count();
            if (count < 0) {
                throw new ServiceException(500, "当前物料 未与 品牌绑定");
            }
        }
    }

    /**
     * 购物车退回
     *
     * @param ids
     */
    @Override
    public List<ShopCart> sendBack(List<Long> ids) {

        List<ShopCart> shopCarts = ids.size() == 0 ? new ArrayList<>() : this.listByIds(ids);
        for (ShopCart shopCart : shopCarts) {
            shopCart.setDisplay(0);

            InstockList instockList = null;
            String skuMessage;
            switch (shopCart.getType()) {
                case "InstockError":
                case "StocktakingError":
                    Anomaly anomaly = anomalyService.getById(shopCart.getFormId());
                    anomaly.setDisplay(0);
                    anomalyService.updateById(anomaly);
                    if (anomaly.getType().equals("InstockError")) {
                        instockList = instockListService.getById(anomaly.getSourceId());
                        skuMessage = skuService.skuMessage(instockList.getSkuId());
                        instockList = instockListService.getById(anomaly.getSourceId());
                        addDynamic(instockList.getInstockOrderId(), skuMessage + "删除了异常描述");
                    }
                    //盘点的撤回
                    if (anomaly.getType().equals("StocktakingError")) {
                        List<InventoryDetail> inventoryDetails = inventoryDetailService.query().eq("anomaly_id", anomaly.getAnomalyId()).eq("display", 1).list();
                        for (InventoryDetail inventoryDetail : inventoryDetails) {
                            inventoryDetail.setAnomalyId(0L);
                            inventoryDetail.setStatus(0);
                        }
                        inventoryDetailService.updateBatchById(inventoryDetails);
                    }
                    break;
                case "waitInStock":
                    instockList = instockListService.getById(shopCart.getFormId());
                    instockList.setRealNumber(shopCart.getNumber());
                    skuMessage = skuService.skuMessage(instockList.getSkuId());
                    addDynamic(instockList.getInstockOrderId(), skuMessage + "取消入库重新核验");
                    break;
                case "instockByAnomaly":
                    throw new ServiceException(500, "异常件不可退回");
//                    Anomaly error = anomalyService.getById(shopCart.getFormId());
//                    error.setDisplay(0);
//                    anomalyService.updateById(error);
//                    instockList = instockListService.getById(error.getSourceId());
//                    break;
                default:
                    throw new ServiceException(500,"错误");
            }
            if (instockList != null) {
                instockList.setStatus(0L);
                instockListService.updateById(instockList);
            }
            this.updateById(shopCart);
        }
        return shopCarts;

    }

    /**
     * 动态
     */
    @Override
    public void addDynamic(Long fromId, String content) {
        if (ToolUtil.isEmpty(fromId)) {
            return;
        }
        Long taskId = taskService.getTaskIdByFormId(fromId);
        if (ToolUtil.isNotEmpty(taskId)) {
            RemarksParam remarksParam = new RemarksParam();
            remarksParam.setTaskId(taskId);
            remarksParam.setType("dynamic");
            remarksParam.setCreateUser(LoginContextHolder.getContext().getUserId());
            remarksParam.setContent(content);

            Remarks entity = new Remarks();
            ToolUtil.copyProperties(remarksParam, entity);
            remarksService.save(entity);
        }


        /**
         * 消息队列获取不到当前人  不用这个了
         */
//        messageProducer.remarksServiceDo(new RemarksEntity() {{
//            setOperationType(OperationType.SAVE);
//            setRemarksParam(remarksParam);
//        }});
    }

    /**
     * 修改入库清单状态
     *
     * @param id
     * @param status
     */
    @Transactional
    public void updateInStockListStatus(Long id, Long status, Long number) {
        InstockList instockList = instockListService.getById(id);
        if (instockList.getStatus() != 0) {
            throw new ServiceException(500, "当前已操作");
        }
        if (instockList.getRealNumber() - number == 0) {
            instockList.setStatus(status);
            this.instockListService.updateById(instockList);
        }
    }

    @Override
    public Set<String> backType(List<String> types) {
        Long userId = LoginContextHolder.getContext().getUserId();
        List<ShopCart> shopCartList = this.query().eq("display", 1).eq("create_user", userId).list();

        Set<String> set = new HashSet<>();
        for (ShopCart shopCart : shopCartList) {
            for (String type : types) {
                if (type.equals(shopCart.getType())) {
                    set.add(type);
                }
            }
        }

        return set;
    }


    @Override
    public void addList(List<ShopCartParam> params) {
        List<ShopCart> shopCarts = new ArrayList<>();
        for (ShopCartParam param : params) {
            ShopCart entity = getEntity(param);
            shopCarts.add(entity);
        }
        this.saveBatch(shopCarts);
    }


    @Override
    @Transactional
    public List<Long> delete(ShopCartParam param) {

        Long userId = LoginContextHolder.getContext().getUserId();

        if (ToolUtil.isNotEmpty(param.getIds()) && param.getIds().size() > 0) {
            List<ShopCart> shopCarts = this.listByIds(param.getIds());
            for (ShopCart shopCart : shopCarts) {
                if (!shopCart.getCreateUser().equals(userId)) {
                    throw new ServiceException(500, "错误");
                }
                shopCart.setDisplay(0);
            }
            this.updateBatchById(shopCarts);
            return param.getIds();
        }

//        ShopCart entity = getEntity(param);
//        entity.setDisplay(0);
//        this.updateById(entity);
        return null;
    }

    /**
     * 购物车修改
     *
     * @param param
     * @return
     */
    @Override
    public Long update(ShopCartParam param) {

        ShopCart oldEntity = getOldEntity(param);
        if (ToolUtil.isEmpty(oldEntity)) {
            throw new ServiceException(500, "购物车不存在");
        }
        switch (oldEntity.getType()) {
            case "allocation":
                if (!LoginContextHolder.getContext().getUserId().equals(oldEntity.getCreateUser())){
                    throw new ServiceException(500, "不可更改他人购物车");
                }
                break;
            case "outStock":
            case "inStock":
            case "stocktaking":
            case "curing":
            case "directInStock":
                throw new ServiceException(500, "购物车不可修改");
        }
        ShopCart newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);

        if (ToolUtil.isNotEmpty(param.getPositionNums())) {     //多个库位
            String json = JSON.toJSONString(param.getPositionNums());
            newEntity.setStorehousePositionsId(json);
        }

        this.updateById(newEntity);
        return param.getCartId();
    }

    /**
     * 单据修改
     *
     * @param param
     * @return
     */
    @Override
    public Long applyUpdate(ShopCartParam param) {

        ShopCart oldEntity = getOldEntity(param);
        if (ToolUtil.isEmpty(oldEntity)) {
            throw new ServiceException(500, "购物车不存在");
        }
        switch (oldEntity.getType()) {
            case "outStock":
            case "inStock":
            case "stocktaking":
            case "allocation":
            case "curing":
            case "directInStock":
                Long userId = LoginContextHolder.getContext().getUserId();
                if (!oldEntity.getCreateUser().equals(userId)) {
                    throw new ServiceException(500, "不可修改他人申请");
                }
                break;
            default:
                throw new ServiceException(500, "购物车错误");
        }
        ShopCart newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);

        this.updateById(newEntity);
        return param.getCartId();
    }


    @Override
    public List<ShopCartResult> allList(ShopCartParam param) {
        List<ShopCartResult> shopCartResults = new ArrayList<>();
        /**
         * 查看权限
         */
        Long LoginUserId = LoginContextHolder.getContext().getUserId();
        if (ToolUtil.isNotEmpty(param.getSourceId())) {
            ActivitiProcessTask processTask = null;
            Long formId = null;
            switch (param.getReceiptsEnum()) {
                case INSTOCK:
                    InstockOrder instockOrder = instockOrderService.getById(param.getSourceId());
                    formId = instockOrder.getInstockOrderId();
                    break;
                case Stocktaking:
                    Inventory inventory = inventoryService.getById(param.getSourceId());
                    formId = inventory.getInventoryTaskId();
                    break;
            }
            processTask = activitiProcessTaskService.getByFormId(formId);
            List<Long> userIds = auditService.getUserIds(processTask.getProcessTaskId());
            for (Long userId : userIds) {
                if (userId.equals(LoginUserId)) {
                    shopCartResults = this.baseMapper.customList(param);
                    break;
                }
            }
        } else {
            param.setCreateUser(LoginUserId);
            shopCartResults = this.baseMapper.customList(param);
        }
        format(shopCartResults);
        return shopCartResults;
    }


    @Override
    public List<ShopCartResult> merge(Long inventoryId) {
        List<InventoryDetail> inventoryDetailList = inventoryDetailService.query()
                .select("sku_id as skuId ,brand_id as brandId").eq("inventory_id", inventoryId).groupBy("sku_id", "brand_id").eq("display", 1).list();

        List<Long> skuIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        for (InventoryDetail inventoryDetail : inventoryDetailList) {
            skuIds.add(inventoryDetail.getSkuId());
            brandIds.add(inventoryDetail.getBrandId());
        }

        List<ShopCart> shopCarts = this.query().eq("type", AnomalyType.allStocktaking.name())
                .in("sku_id", skuIds)
                .in("brand_id", brandIds)
                .eq("status", 0)
                .eq("display", 1).list();

        return BeanUtil.copyToList(shopCarts, ShopCartResult.class, new CopyOptions());
    }


    /**
     * 申请购物车
     *
     * @param param
     * @return
     */
    @Override
    public List<ShopCartResult> applyList(ShopCartParam param) {
        Long userId = LoginContextHolder.getContext().getUserId();
        param.setCreateUser(userId);
        List<ShopCartResult> shopCartResults = this.baseMapper.applyList(param);
        format(shopCartResults);
        return shopCartResults;
    }

    @Override
    public void clearAllocationShopCart() {
        Long userId = LoginContextHolder.getContext().getUserId();

        List<ShopCart> shopCarts = this.query().eq("display", 1).eq("status", 0).eq("create_user", userId).eq("type", "allocation").list();
        for (ShopCart shopCart : shopCarts) {
            shopCart.setDisplay(0);
        }
        this.updateBatchById(shopCarts);


    }

    @Override
    public ShopCartResult findBySpec(ShopCartParam param) {
        return null;
    }

    @Override
    public List<ShopCartResult> findListBySpec(ShopCartParam param) {
        return null;
    }

    @Override
    public PageInfo<ShopCartResult> findPageBySpec(ShopCartParam param) {
        Page<ShopCartResult> pageContext = getPageContext();
        IPage<ShopCartResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ShopCartParam param) {
        return param.getCartId();
    }

    private Page<ShopCartResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ShopCart getOldEntity(ShopCartParam param) {
        return this.getById(getKey(param));
    }

    private ShopCart getEntity(ShopCartParam param) {
        ShopCart entity = new ShopCart();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    private ShopCartResult getResult(ShopCartParam param) {
        ShopCartResult result = new ShopCartResult();
        ToolUtil.copyProperties(param, result);
        format(new ArrayList<ShopCartResult>() {{
            add(result);
        }});
        return result;
    }

    @Override
    public void format(List<ShopCartResult> data) {
        List<Long> skuIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        List<Long> customerIds = new ArrayList<>();
        List<Long> anomalyIds = new ArrayList<>();
        List<Long> positionIds = new ArrayList<>();

        for (ShopCartResult datum : data) {
            customerIds.add(datum.getCustomerId());
            brandIds.add(datum.getBrandId());
            skuIds.add(datum.getSkuId());

            if (ToolUtil.isNotEmpty(datum.getStorehousePositionsId())) {   //库位
                List<PositionNum> positionNums = JSON.parseArray(datum.getStorehousePositionsId(), PositionNum.class);
                List<Long> ids = new ArrayList<>();
                for (PositionNum positionNum : positionNums) {
                    ids.add(positionNum.getPositionId());
                }
                datum.setPositionNums(positionNums);
                positionIds.addAll(ids);
            }

            if (ToolUtil.isNotEmpty(datum.getType())) {
                anomalyIds.add(datum.getFormId());
            }
            if (ToolUtil.isNotEmpty(datum.getPositionNums())) {
                for (PositionNum positionNum : datum.getPositionNums()) {
                    if (ToolUtil.isNotEmpty(positionNum.getPositionId())) {
                        positionIds.add(positionNum.getPositionId());
                    }
                    if (ToolUtil.isNotEmpty(positionNum.getToPositionId())) {
                        positionIds.add(positionNum.getToPositionId());
                    }
                }
            }
        }


        Map<Long, List<StorehousePositionsResult>> positionMap = positionsService.getMap(skuIds);
        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);
        List<BrandResult> brandResults = brandService.getBrandResults(brandIds);
        List<Customer> customerList = customerIds.size() == 0 ? new ArrayList<>() : customerService.listByIds(customerIds);
        Map<Long, AnomalyResult> map = anomalyService.getMap(anomalyIds);
        List<StorehousePositions> positions = positionIds.size() == 0 ? new ArrayList<>() : storehousePositionsService.listByIds(positionIds);
        List<StorehousePositionsResult> positionsResults = BeanUtil.copyToList(positions, StorehousePositionsResult.class, new CopyOptions());

        for (ShopCartResult datum : data) {
            if (ToolUtil.isNotEmpty(datum.getFormId())) {
                AnomalyResult result = map.get(datum.getFormId());
                datum.setAnomalyResult(result);
            }

            if (ToolUtil.isNotEmpty(datum.getPositionNums())) {

                List<StorehousePositionsResult> positionsResultList = new ArrayList<>();
                for (PositionNum positionNum : datum.getPositionNums()) {
                    for (StorehousePositionsResult position : positionsResults) {
                        if (ToolUtil.isNotEmpty(positionNum.getPositionId()) && positionNum.getPositionId().equals(position.getStorehousePositionsId())) {
                            position.setNumber(positionNum.getNum());
                            positionsResultList.add(position);
                            positionNum.setPositionsResult(position);
                        }
                        if (ToolUtil.isNotEmpty(positionNum.getToPositionId()) && positionNum.getToPositionId().equals(position.getStorehousePositionsId())) {
                            positionNum.setToPositionsResult(position);
                        }

                    }
                }
                datum.setStorehousePositions(positionsResultList);
            }


            for (SkuResult skuResult : skuResults) {
                List<StorehousePositionsResult> results = positionMap.get(skuResult.getSkuId());
                skuResult.setPositionsResult(results);
                if (datum.getSkuId().equals(skuResult.getSkuId())) {
                    datum.setSkuResult(skuResult);
                    break;
                }
            }
            for (BrandResult brandResult : brandResults) {
                if (ToolUtil.isNotEmpty(datum.getBrandId()) && datum.getBrandId().equals(brandResult.getBrandId())) {
                    datum.setBrandResult(brandResult);
                    break;
                }
            }
            for (Customer customer : customerList) {
                if (ToolUtil.isNotEmpty(datum.getCustomerId()) && datum.getCustomerId().equals(customer.getCustomerId())) {
                    datum.setCustomer(customer);
                    break;
                }
            }
        }


    }


}
