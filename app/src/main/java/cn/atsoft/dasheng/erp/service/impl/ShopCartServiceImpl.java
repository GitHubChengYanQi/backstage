package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
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
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.entity.ActivitiAudit;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.model.params.RemarksParam;
import cn.atsoft.dasheng.form.service.ActivitiAuditService;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.message.config.DirectQueueConfig;
import cn.atsoft.dasheng.message.enmu.OperationType;
import cn.atsoft.dasheng.message.entity.RemarksEntity;
import cn.atsoft.dasheng.message.producer.MessageProducer;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Data;
import lombok.Synchronized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    protected static final Logger logger = LoggerFactory.getLogger(ShopCartServiceImpl.class);

    @Override
    @Transactional
    public Long add(ShopCartParam param) {

        Date date = new DateTime();
        logger.info(date + ": 添加带入购物车--->" + param.getInstockListId());

        if (ToolUtil.isNotEmpty(param.getInstockListId())) {
            Integer count = this.query().eq("form_id", param.getInstockListId()).eq("display", 1).count();
            if (ToolUtil.isNotEmpty(count) && count > 0) {
                throw new ServiceException(500, "当前已操作");
            }
        }

        if (ToolUtil.isNotEmpty(param.getPositionNums())) {
            String json = JSON.toJSONString(param.getPositionNums());
            param.setStorehousePositionsId(json);
        }
        ShopCart entity = getEntity(param);
        this.save(entity);

        if (ToolUtil.isNotEmpty(param.getInstockListId())) {
            updateInStockListStatus(param.getInstockListId(), param.getFormStatus(), entity.getNumber());
            InstockList instockList = instockListService.getById(param.getInstockListId());


            Long taskId = activitiProcessTaskService.getTaskIdByFormId(instockList.getInstockOrderId());
            RemarksParam remarksParam = new RemarksParam();
            remarksParam.setTaskId(taskId);
            remarksParam.setType("dynamic");
            remarksParam.setContent(LoginContextHolder.getContext().getUser().getName() + "添加了待入购物车");
            messageProducer.remarksServiceDo(new RemarksEntity() {{
                setOperationType(OperationType.ADD);
                setRemarksParam(remarksParam);
            }});
        }

        return entity.getCartId();
    }

    /**
     * 购物车退回
     *
     * @param ids
     */
    @Override
    public void sendBack(List<Long> ids) {

        List<ShopCart> shopCarts = ids.size() == 0 ? new ArrayList<>() : this.listByIds(ids);
        for (ShopCart shopCart : shopCarts) {
            shopCart.setDisplay(0);

            InstockList instockList = null;

            switch (shopCart.getType()) {
                case "InstockError":
                    Anomaly anomaly = anomalyService.getById(shopCart.getFormId());
                    anomaly.setDisplay(0);
                    anomalyService.updateById(anomaly);
                    instockList = instockListService.getById(anomaly.getSourceId());
                    break;
                case "waitInStock":
                    instockList = instockListService.getById(shopCart.getFormId());
                    instockList.setRealNumber(shopCart.getNumber());
                    break;
                case "instockByAnomaly":
                    AnomalyDetail anomalyDetail = anomalyDetailService.getById(shopCart.getCartId());
                    Anomaly error = anomalyService.getById(anomalyDetail.getAnomalyId());
                    error.setDisplay(0);
                    anomalyService.updateById(error);
                    instockList = instockListService.getById(error.getSourceId());
                    break;
            }
            if (instockList != null) {
                instockList.setStatus(0L);
                instockListService.updateById(instockList);
            }
            this.updateById(shopCart);
        }


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
    public void addList(List<ShopCartParam> params) {
        List<ShopCart> shopCarts = new ArrayList<>();
        for (ShopCartParam param : params) {
            ShopCart entity = getEntity(param);
            shopCarts.add(entity);
        }
        this.saveBatch(shopCarts);
    }


    @Override
    public List<Long> delete(ShopCartParam param) {

        if (ToolUtil.isNotEmpty(param.getIds()) && param.getIds().size() > 0) {
            List<ShopCart> shopCarts = this.listByIds(param.getIds());
            for (ShopCart shopCart : shopCarts) {
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

    @Override
    public Long update(ShopCartParam param) {
        ShopCart oldEntity = getOldEntity(param);
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
        if (ToolUtil.isNotEmpty(param.getSourceId())) {
            InstockOrder instockOrder = instockOrderService.getById(param.getSourceId());
            ActivitiProcessTask processTask = activitiProcessTaskService.getByFormId(instockOrder.getInstockOrderId());
            List<Long> userIds = auditService.getUserIds(processTask.getProcessTaskId());
            Long id = LoginContextHolder.getContext().getUserId();
            for (Long userId : userIds) {
                if (userId.equals(id)) {
                    shopCartResults = this.baseMapper.customList(param);
                    break;
                }
            }
        } else if (ToolUtil.isNotEmpty(param.getType())) {
            shopCartResults = this.baseMapper.customList(param);
        }

        format(shopCartResults);
        return shopCartResults;
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

    private void format(List<ShopCartResult> data) {
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

            if (ToolUtil.isNotEmpty(datum.getType()) && datum.getType().equals(AnomalyType.InstockError.name())) {
                anomalyIds.add(datum.getFormId());
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
                        if (positionNum.getPositionId().equals(position.getStorehousePositionsId())) {
                            position.setNumber(positionNum.getNum());
                            positionsResultList.add(position);
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
