package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.app.entity.Unit;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.app.service.UnitService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Order;
import cn.atsoft.dasheng.crm.entity.OrderDetail;
import cn.atsoft.dasheng.crm.mapper.OrderDetailMapper;
import cn.atsoft.dasheng.crm.model.params.OrderDetailParam;
import cn.atsoft.dasheng.crm.model.params.OrderParam;
import cn.atsoft.dasheng.crm.model.result.OrderDetailResult;
import cn.atsoft.dasheng.crm.model.result.OrderResult;
import cn.atsoft.dasheng.crm.service.OrderDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.service.OrderService;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.service.ActivitiProcessService;
import cn.atsoft.dasheng.taxRate.entity.TaxRate;
import cn.atsoft.dasheng.taxRate.service.TaxRateService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 订单明细表 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-02-23
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
    @Autowired
    private SkuService skuService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private TaxRateService rateService;
    @Autowired
    private ActivitiProcessService processService;
    @Override
    public void add(OrderDetailParam param) {
        OrderDetail entity = getEntity(param);
        this.save(entity);
    }

    /**
     * 批量添加
     *
     * @param orderId
     * @param params
     */

    @Override
    public void addList(Long orderId, Long customerId, List<OrderDetailParam> params) {
        List<OrderDetail> details = new ArrayList<>();
        for (OrderDetailParam param : params) {
            OrderDetail orderDetail = new OrderDetail();
            ToolUtil.copyProperties(param, orderDetail);
            orderDetail.setOrderId(orderId);
            orderDetail.setCustomerId(customerId);
            details.add(orderDetail);
        }
        this.saveBatch(details);
    }

    @Override
    public void delete(OrderDetailParam param) {
        param.setDisplay(0);
        this.updateById(this.getEntity(param));
    }

    @Override
    public void update(OrderDetailParam param) {
        OrderDetail oldEntity = getOldEntity(param);
        OrderDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public OrderDetailResult findBySpec(OrderDetailParam param) {
        return null;
    }

    @Override
    public List<OrderDetailResult> findListBySpec(OrderDetailParam param) {
        return null;
    }

    @Override
    public PageInfo<OrderDetailResult> findPageBySpec(OrderDetailParam param) {
        Page<OrderDetailResult> pageContext = getPageContext();
        IPage<OrderDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(OrderDetailParam param) {
        return param.getDetailId();
    }

    private Page<OrderDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private OrderDetail getOldEntity(OrderDetailParam param) {
        return this.getById(getKey(param));
    }

    private OrderDetail getEntity(OrderDetailParam param) {
        OrderDetail entity = new OrderDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    /**
     * 详情
     *
     * @param orderId
     * @return
     */
    @Override
    public List<OrderDetailResult> getDetails(Long orderId) {
        if (ToolUtil.isEmpty(orderId)) {
            return new ArrayList<>();
        }
        List<OrderDetail> details = this.query().eq("order_id", orderId).list();
        if (ToolUtil.isEmpty(details)) {
            return new ArrayList<>();
        }
        List<OrderDetailResult> detailResults = BeanUtil.copyToList(details, OrderDetailResult.class, new CopyOptions());
        List<Long> skuIds = new ArrayList<>();
        List<Long> customerIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        for (OrderDetailResult detailResult : detailResults) {
            skuIds.add(detailResult.getSkuId());
            customerIds.add(detailResult.getCustomerId());
            brandIds.add(detailResult.getBrandId());
        }
        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);
        List<CustomerResult> customerResults = customerService.getResults(customerIds);
        List<BrandResult> brandResults = brandService.getBrandResults(brandIds);

        for (OrderDetailResult detailResult : detailResults) {
            for (SkuResult skuResult : skuResults) {
                if (skuResult.getSkuId().equals(detailResult.getSkuId())) {
                    detailResult.setSkuResult(skuResult);
                    break;
                }
            }
            for (CustomerResult customerResult : customerResults) {
                if (customerResult.getCustomerId().equals(detailResult.getCustomerId())) {
                    detailResult.setCustomerResult(customerResult);
                    break;
                }
            }
            for (BrandResult brandResult : brandResults) {
                if (brandResult.getBrandId().equals(detailResult.getBrandId())) {
                    detailResult.setBrandResult(brandResult);
                    break;
                }
            }
        }

        return detailResults;
    }

    @Override
    public void format(List<OrderDetailResult> param) {
        List<Long> skuIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        List<Long> customerIds = new ArrayList<>();
        List<Long> unitIds = new ArrayList<>();
        List<Long> taxIds = new ArrayList<>();

        for (OrderDetailResult orderDetailResult : param) {
            skuIds.add(orderDetailResult.getSkuId());
            brandIds.add(orderDetailResult.getBrandId());
            customerIds.add(orderDetailResult.getCustomerId());
            unitIds.add(orderDetailResult.getUnitId());
            taxIds.add(orderDetailResult.getRate());
        }
        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);
        List<BrandResult> brandResults = brandService.getBrandResults(brandIds);
        List<CustomerResult> customerResults = customerService.getResults(customerIds);
        List<Unit> unitList = unitIds.size() == 0 ? new ArrayList<>() : unitService.listByIds(unitIds);
        List<TaxRate> taxRates = taxIds.size() == 0 ? new ArrayList<>() : rateService.listByIds(taxIds);
        List<OrderResult> orderResults = orderService.findListBySpec(new OrderParam());
        for (OrderDetailResult orderDetailResult : param) {
            for (OrderResult orderResult : orderResults) {
                if (orderResult.getOrderId().equals(orderDetailResult.getOrderId())){
                    orderDetailResult.setOrderResult(orderResult);
                    break;
                }
            }
            for (SkuResult skuResult : skuResults) {
                if (orderDetailResult.getSkuId().equals(skuResult.getSkuId())) {
                    orderDetailResult.setSkuResult(skuResult);
                    break;
                }
            }
            for (BrandResult brandResult : brandResults) {
                if (orderDetailResult.getBrandId().equals(brandResult.getBrandId())) {
                    orderDetailResult.setBrandResult(brandResult);
                    break;
                }
            }
            for (CustomerResult customerResult : customerResults) {
                if (orderDetailResult.getCustomerId().equals(customerResult.getCustomerId())) {
                    orderDetailResult.setCustomerResult(customerResult);
                    break;
                }
            }
            for (Unit unit : unitList) {
                if (ToolUtil.isNotEmpty(orderDetailResult.getUnitId()) && orderDetailResult.getUnitId().equals(unit.getUnitId())) {
                    orderDetailResult.setUnit(unit);
                    break;
                }
            }
            for (TaxRate taxRate : taxRates) {
                if (ToolUtil.isNotEmpty(orderDetailResult.getRate()) && orderDetailResult.getRate().equals(taxRate.getTaxRateId())) {
                    orderDetailResult.setTaxRate(taxRate);
                    break;
                }
            }
        }

    }
    @Override
    public  List<OrderDetailResult> getOrderDettailProductionIsNull( OrderDetailParam paramCondition){
        if (ToolUtil.isEmpty(paramCondition)) {
            return new ArrayList<>();
        }
        List<OrderDetailResult> detailResults = this.baseMapper.pendingProductionPlanByOrder(paramCondition);
        return detailResults;
    }

}
