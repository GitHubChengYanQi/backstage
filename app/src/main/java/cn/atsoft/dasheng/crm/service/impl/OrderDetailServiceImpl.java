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
import cn.atsoft.dasheng.crm.model.result.OrderDetailResult;
import cn.atsoft.dasheng.crm.model.result.OrderResult;
import cn.atsoft.dasheng.crm.service.OrderDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.service.OrderService;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.form.service.ActivitiProcessService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.taxRate.entity.TaxRate;
import cn.atsoft.dasheng.taxRate.service.TaxRateService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
    @Autowired
    private OrderDetailService detailService;

    @Override
    public void add(OrderDetailParam param) {
        OrderDetail entity = getEntity(param);
        this.save(entity);
    }

    /**
     * 取上次供应商这个物料的订录
     *
     * @return
     */
    @Override
    public OrderDetailResult record(OrderDetailParam param) {
        if (ToolUtil.isEmpty(param.getCustomerId()) || ToolUtil.isEmpty(param.getSkuId())) {
            throw new ServiceException(500, "缺少参数");
        }
        QueryWrapper<OrderDetail> detailQueryWrapper = new QueryWrapper<>();
        detailQueryWrapper.eq("customer_id", param.getCustomerId());
        detailQueryWrapper.eq("sku_id", param.getSkuId());
        if (ToolUtil.isNotEmpty(param.getBrandId())) {
            detailQueryWrapper.eq("brand_id", param.getBrandId());
        }
        detailQueryWrapper.orderByDesc("create_time");
        detailQueryWrapper.last("limit 1");

        OrderDetail orderDetail = this.getOne(detailQueryWrapper);
        if (ToolUtil.isEmpty(orderDetail)) {
            return null;
        }

        OrderDetailResult result = new OrderDetailResult();
        ToolUtil.copyProperties(orderDetail, result);
        return result;
    }


    /**
     * 批量添加
     *
     * @param orderId
     * @param params
     */

    @Override

    public Long addList(Long orderId, Long customerId, List<OrderDetailParam> params) {
        List<OrderDetail> details = new ArrayList<>();
        long totalAmount = 0;   //所有物料总价
        if (ToolUtil.isEmpty(params)) {
            return totalAmount;
        }
        for (OrderDetailParam param : params) {
            OrderDetail orderDetail = new OrderDetail();
            ToolUtil.copyProperties(param, orderDetail);
            orderDetail.setDetailId(null);
            int detailAmount = Math.toIntExact(orderDetail.getOnePrice() * orderDetail.getPurchaseNumber());
            orderDetail.setOrderId(orderId);
            orderDetail.setCustomerId(customerId);
            orderDetail.setTotalPrice(detailAmount);
            orderDetail.setInStockNumber(0);
            orderDetail.setArrivalNumber(0);
            totalAmount = totalAmount + detailAmount;
            details.add(orderDetail);
        }
        this.saveBatch(details);
        return totalAmount;
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
        return PageFactory.defaultPage( new ArrayList<String>(){{
            add("totalPrice");
            add("arrivalNumber");
            add("inStockNumber");
        }});
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
        List<Long> orderIds = new ArrayList<>();

        long orderId = 0;
        for (OrderDetailResult orderDetailResult : param) {
            skuIds.add(orderDetailResult.getSkuId());
            brandIds.add(orderDetailResult.getBrandId());
            customerIds.add(orderDetailResult.getCustomerId());
            unitIds.add(orderDetailResult.getUnitId());
            taxIds.add(orderDetailResult.getRate());
            orderIds.add(orderDetailResult.getOrderId());
            orderId = orderDetailResult.getOrderId();
        }

        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);
        List<BrandResult> brandResults = brandService.getBrandResults(brandIds);
        List<CustomerResult> customerResults = customerService.getResults(customerIds);
        List<Unit> unitList = unitIds.size() == 0 ? new ArrayList<>() : unitService.listByIds(unitIds);
        List<TaxRate> taxRates = taxIds.size() == 0 ? new ArrayList<>() : rateService.listByIds(taxIds);

        List<Order> orderList = orderIds.size() == 0 ? new ArrayList<>() : orderService.listByIds(orderIds);
        List<OrderResult> orderResults = BeanUtil.copyToList(orderList,OrderResult.class,new CopyOptions());
        String sign = getSign(orderId);

        for (OrderDetailResult orderDetailResult : param) {
            orderDetailResult.setSign(sign);

            for (SkuResult skuResult : skuResults) {
                if (orderDetailResult.getSkuId().equals(skuResult.getSkuId())) {
                    orderDetailResult.setSkuResult(skuResult);
                    break;
                }
            }
            for (BrandResult brandResult : brandResults) {
                if (ToolUtil.isNotEmpty(orderDetailResult.getBrandId()) && ToolUtil.isNotEmpty(brandResult.getBrandId()) && orderDetailResult.getBrandId().equals(brandResult.getBrandId())) {
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
    public List<OrderDetailResult> getOrderDettailProductionIsNull(OrderDetailParam paramCondition) {
        if (ToolUtil.isEmpty(paramCondition)) {
            return new ArrayList<>();
        }
        return this.baseMapper.pendingProductionPlanByOrder(paramCondition);
    }

    @Override
    public String getSign(Long orderId) {
        Order order = orderService.getById(orderId);
        String sign = "";
        if (ToolUtil.isNotEmpty(order)) {
            switch (order.getCurrency()) {
                case "人民币":
                    sign = "¥";
                    break;
                case "美元":
                    sign = "$";
                    break;
                case "欧元":
                    sign = "€";
                    break;
            }
        }
        return sign;
    }
}
