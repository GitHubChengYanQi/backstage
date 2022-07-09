package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.appBase.service.MediaService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.config.MobileService;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.mapper.AnomalyMapper;
import cn.atsoft.dasheng.erp.model.params.AnomalyDetailParam;
import cn.atsoft.dasheng.erp.model.params.AnomalyParam;
import cn.atsoft.dasheng.erp.model.params.ShopCartParam;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.pojo.AnomalyType;
import cn.atsoft.dasheng.erp.pojo.PositionNum;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.form.entity.ActivitiAudit;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.entity.ActivitiSteps;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.model.params.RemarksParam;
import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.message.enmu.OperationType;
import cn.atsoft.dasheng.message.entity.RemarksEntity;
import cn.atsoft.dasheng.message.producer.MessageProducer;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.purchase.service.GetOrigin;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.sendTemplate.WxCpTemplate;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
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
import java.util.stream.Collectors;

import static cn.atsoft.dasheng.form.pojo.StepsType.START;


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


    @Transactional
    @Override
    public Anomaly add(AnomalyParam param) {

        switch (param.getAnomalyType()) {
            case InstockError:     //判断入库单
                InstockOrder order = instockOrderService.getById(param.getFormId());
                if (ToolUtil.isEmpty(order)) {
                    throw new ServiceException(500, "入库单不存在");
                }
                param.setType(param.getAnomalyType().toString());
                break;
            case StocktakingError:  //盘点:
                boolean normal = isNormal(param);  //判断有无异常件  没有异常件 不执行以下代码 直接退出
                if (normal) {
                    updateInventory(param);   //盘点详情 修改成正常状态
                    return null;
                }
                break;

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

        if (ToolUtil.isNotEmpty(param.getInstockListId())) {
            InstockList instockList = instockListService.getById(param.getInstockListId());
            instockList.setStatus(-1L);
            instockListService.updateById(instockList);
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
            shopCartParam.setFormId(entity.getAnomalyId());
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
        updateInventoryStatus(param, 2);
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
        for (InventoryDetail inventoryDetail : inventoryDetails) {
            if (inventoryDetail.getLockStatus() == 99) {
                throw new ServiceException(500, "当前状态不可更改");
            }
            inventoryDetail.setStatus(status);
            inventoryDetail.setAnomalyId(param.getAnomalyId());
            inventoryDetail.setLockStatus(0);
        }
        param.setType(param.getAnomalyType().toString());
        inventoryDetailService.updateBatchById(inventoryDetails);
    }

    /**
     * 添加详情(区分批量)
     *
     * @param param
     */
    public boolean addDetails(AnomalyParam param) {
        boolean t = true;

        if (param.getRealNumber() - param.getNeedNumber() == 0 && ToolUtil.isEmpty(param.getDetailParams())) {
            switch (param.getAnomalyType()) {
                case StocktakingError:
                    updateInventory(param);     //盘点正常
                    t = false;
                    break;
                case InstockError:
                    throw new ServiceException(500, "缺少异常信息");
            }
        } else {
            updateInventoryStatus(param, -1);
        }
        if (t) {   //添加异常信息
            for (AnomalyDetailParam detailParam : param.getDetailParams()) {
                AnomalyDetail detail = new AnomalyDetail();
                ToolUtil.copyProperties(detailParam, detail);
                detail.setAnomalyId(param.getAnomalyId());
                detail.setInkindId(param.getInkind());
                if (ToolUtil.isNotEmpty(detailParam.getPidInKind())) {
                    detail.setInkindId(detailParam.getPidInKind());
                }
                if (ToolUtil.isNotEmpty(detailParam.getNoticeIds())) {
                    String json = JSON.toJSONString(detailParam.getNoticeIds());
                    detail.setRemark(json);
                }
                detailService.save(detail);
            }
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
        if (param.getRealNumber() - param.getNeedNumber() == 0 && ToolUtil.isEmpty(param.getDetailParams())) {
            return true;
        }
        return false;
    }

    /**
     * 盘点  异常 数据 改成正常数据
     *
     * @param param
     */
    private void updateInventory(AnomalyParam param) {

        updateInventoryStatus(param, 1);    //数据正常  不添加异常数据


        ShopCart shopCart = new ShopCart();
        shopCart.setDisplay(0);
        shopCartService.update(shopCart, new QueryWrapper<ShopCart>() {{
            eq("form_id", param.getAnomalyId());
        }});

        //改为正常
        InventoryDetail inventoryDetail = new InventoryDetail();
        inventoryDetail.setAnomalyId(0L);
        inventoryDetailService.update(inventoryDetail, new QueryWrapper<InventoryDetail>() {{
            eq("anomaly_id", param.getAnomalyId());
        }});
    }


    @Override
    public void delete(AnomalyParam param) {
        Anomaly anomaly = this.getById(param.getAnomalyId());
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
            instockList.setStatus(0L);
            instockListService.updateById(instockList);
        }
        //盘点
        if (anomaly.getType().equals(AnomalyType.StocktakingError.name())) {
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
        Anomaly newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
        detailService.remove(new QueryWrapper<AnomalyDetail>() {{
            eq("anomaly_id", param.getAnomalyId());
        }});
        addDetails(param);
        String skuMessage = skuService.skuMessage(oldEntity.getSkuId());
        shopCartService.addDynamic(oldEntity.getFormId(), skuMessage + "修改了异常描述");
        return newEntity;
    }

    /**
     * 异常处理
     */
    @Override
    public void dealWithError(AnomalyParam param) {

        Anomaly oldEntity = getOldEntity(param);
        Anomaly newEntity = getEntity(param);

        ActivitiProcessTask task = taskService.getByFormId(oldEntity.getOrderId());
        List<Long> userIds = new ArrayList<>(auditService.getUserIds(task.getProcessTaskId()));
        userIds.add(param.getCreateUser());
        boolean power = this.power(userIds);

        if (power) {
            throw new ServiceException(500, "你没有操作权限");
        }


        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);

//        if (ToolUtil.isNotEmpty(param.getInstockNumber())) {
//            InstockList instockList = new InstockList();
//            instockList.setInstockListId(oldEntity.getSourceId());
//            instockList.setRealNumber(param.getInstockNumber());
//            instockListService.updateById(instockList);
//        }

        updateStatus(param.getAnomalyId()); //更新异常状态
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
        Integer count = detailService.lambdaQuery().eq(AnomalyDetail::getAnomalyId, anomalyId).eq(AnomalyDetail::getStauts, 0).count();
        if (ToolUtil.isEmpty(count) || count == 0) {
            anomaly.setStatus(99);
            this.updateById(anomaly);
        }
    }


    private boolean power(List<Long> userIds) {
        Long userId = LoginContextHolder.getContext().getUserId();
        for (Long id : userIds) {
            if (id.equals(userId)) {
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

        Brand brand = ToolUtil.isEmpty(result.getBrandId()) ? new Brand() : brandService.getById(result.getBrandId());
        Customer customer = ToolUtil.isEmpty(result.getCustomerId()) ? new Customer() : customerService.getById(result.getCustomerId());

        User user = ToolUtil.isEmpty(result.getCreateUser()) ? new User() : userService.getById(result.getCreateUser());
        result.setUser(user);

        if (ToolUtil.isNotEmpty(skuSimpleResults)) {
            result.setSkuSimpleResult(skuSimpleResults.get(0));
        }
        result.setBrand(brand);
        result.setCustomer(customer);

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
    }

    @Override
    public void format(List<AnomalyResult> data) {

        List<Long> skuIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        List<Long> customerIds = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        for (AnomalyResult datum : data) {
            skuIds.add(datum.getSkuId());
            brandIds.add(datum.getBrandId());
            customerIds.add(datum.getCustomerId());
            ids.add(datum.getAnomalyId());
        }

        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);
        List<Brand> brandList = brandIds.size() == 0 ? new ArrayList<>() : brandService.listByIds(brandIds);
        List<Customer> customers = customerIds.size() == 0 ? new ArrayList<>() : customerService.listByIds(customerIds);
        List<AnomalyDetail> details = ids.size() == 0 ? new ArrayList<>() : detailService.query().in("anomaly_id", ids).eq("display", 1).list();
        List<AnomalyDetailResult> anomalyDetailResults = BeanUtil.copyToList(details, AnomalyDetailResult.class, new CopyOptions());


        for (AnomalyResult datum : data) {
            for (SkuResult skuResult : skuResults) {
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
                }
                detailResults.add(detail);
            }
            datum.setDetails(detailResults);
            datum.setOtherNumber(otherNum);
        }

    }
}
