package cn.atsoft.dasheng.purchase.service.impl;


import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.customer.entity.Customer;
import cn.atsoft.dasheng.customer.model.result.CustomerResult;
import cn.atsoft.dasheng.customer.service.RestCustomerService;
import cn.atsoft.dasheng.entity.RestOrderDetail;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.result.RestOrderDetailResult;
import cn.atsoft.dasheng.purchase.entity.RestOrder;
import cn.atsoft.dasheng.purchase.mapper.RestOrderMapper;
import cn.atsoft.dasheng.purchase.model.params.RestOrderDetailParam;
import cn.atsoft.dasheng.purchase.model.params.RestOrderParam;
import cn.atsoft.dasheng.purchase.model.result.RestOrderResult;
import cn.atsoft.dasheng.purchase.model.result.RestOrderSimpleResult;
import cn.atsoft.dasheng.purchase.service.RestOrderDetailService;
import cn.atsoft.dasheng.purchase.service.RestOrderService;
import cn.atsoft.dasheng.service.IErpBase;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-02-23
 */
@Service("RestOrderService")
public class RestOrderServiceImpl extends ServiceImpl<RestOrderMapper, RestOrder> implements RestOrderService, IErpBase {
    @Autowired
    private UserService userService;

    @Autowired
    private RestCustomerService customerService;

    @Autowired
    private RestOrderDetailService orderDetailService;


    @Override
    @Transactional
    public cn.atsoft.dasheng.entity.RestOrder add(Map<String, Object> paramMap) {
        RestOrderParam param = BeanUtil.mapToBean(paramMap, RestOrderParam.class, true);

        RestOrder entity = getEntity(param);

        String orderType = null;
        switch (param.getType()) {
            case 1:
                orderType = "采购";
                break;
            case 2:
                orderType = "销售";
                break;
        }
        String coding = entity.getCoding();
        String theme = entity.getTheme();

        LambdaQueryChainWrapper<RestOrder> orderLambdaQueryChainWrapper1 = this.lambdaQuery();
        orderLambdaQueryChainWrapper1.eq(RestOrder::getCoding, coding);
        orderLambdaQueryChainWrapper1.eq(RestOrder::getDisplay, 1);
        if (ToolUtil.isNotEmpty(param.getTenantId())) {
            orderLambdaQueryChainWrapper1.eq(RestOrder::getTenantId, param.getTenantId());
        }else {
            orderLambdaQueryChainWrapper1.eq(RestOrder::getTenantId, LoginContextHolder.getContext().getTenantId());
        }

        int codingCount = ToolUtil.isEmpty(coding) ? 0 : orderLambdaQueryChainWrapper1.count();
        LambdaQueryChainWrapper<RestOrder> orderLambdaQueryChainWrapper = this.lambdaQuery();
        orderLambdaQueryChainWrapper.eq(RestOrder::getTheme, theme).eq(RestOrder::getDisplay, 1);
        if (ToolUtil.isNotEmpty(param.getTenantId())) {
            orderLambdaQueryChainWrapper.eq(RestOrder::getTenantId, param.getTenantId());
        }else {
            orderLambdaQueryChainWrapper.eq(RestOrder::getTenantId, LoginContextHolder.getContext().getTenantId());
        }
        int themeCount = ToolUtil.isEmpty(theme) ? 0 : orderLambdaQueryChainWrapper.count();

        if (codingCount > 0) {
            throw new ServiceException(500, "编码不可重复");
        }
        if (themeCount > 0) {
            throw new ServiceException(500, "主题不可重复");
        }


        this.save(entity);

//        if (ToolUtil.isNotEmpty(param.getGenerateContract()) && param.getGenerateContract() == 1) {   //创建合同
//            Contract contract = contractService.orderAddContract(entity.getOrderId(), param.getContractParam(), param, orderType);
//            entity.setContractId(contract.getContractId());
//            if (ToolUtil.isNotEmpty(contract.getContractId())) {
//                entity.setContractId(contract.getContractId());
//            }
//        }

//        param.getPaymentParam().setOrderId(entity.getOrderId());
//        supplyService.OrdersBackFill(param.getSellerId(), param.getDetailParams());  //回填
        Integer totalAmount = orderDetailService.addList(entity.getOrderId(), param.getSellerId(), param.getDetailParams());    //返回添加所有物料总价
        if (ToolUtil.isNotEmpty(param.getPaymentParam()) && ToolUtil.isNotEmpty(param.getPaymentParam().getFloatingAmount())) {
            totalAmount = totalAmount + param.getPaymentParam().getFloatingAmount();
        }
        entity.setTotalAmount(totalAmount);  //订单总金额
        this.updateById(entity);
        return BeanUtil.copyProperties(entity, cn.atsoft.dasheng.entity.RestOrder.class);
    }

    @Override
    public void delete(RestOrderParam param) {
        param.setDisplay(0);
        this.updateById(this.getEntity(param));

    }

    @Override
    public void update(RestOrderParam param) {
        RestOrder oldEntity = getOldEntity(param);
        RestOrder newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public RestOrderResult findBySpec(RestOrderParam param) {
        return null;
    }

    @Override
    public List<RestOrderResult> findListBySpec(RestOrderParam param) {
        List<RestOrderResult> orderResults = this.baseMapper.customList(param);
        format(orderResults);
        return orderResults;
    }

    @Override
    public PageInfo<RestOrderResult> findPageBySpec(RestOrderParam param) {
        Page<RestOrderResult> pageContext = getPageContext();
        IPage<RestOrderResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    @Override
    public void format(List<RestOrderResult> data) {

    }

     private Serializable getKey(RestOrderParam param) {
        return param.getOrderId();
    }

    private Page<RestOrderResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private RestOrder getOldEntity(RestOrderParam param) {
        return this.getById(getKey(param));
    }

    private RestOrder getEntity(RestOrderParam param) {
        RestOrder entity = new RestOrder();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public PageInfo getOrderList(Map<String, Object> param) {
        RestOrderParam restOrderParam = BeanUtil.mapToBean(param, RestOrderParam.class, true);
        Page<RestOrderResult> pageContext = getPageContext();
        IPage<RestOrderSimpleResult> page = new Page<>();

        if (LoginContextHolder.getContext().isAdmin()) {
            page = this.baseMapper.orderList(pageContext, restOrderParam,null);
        } else {
            DataScope dataScope = new DataScope(null,LoginContextHolder.getContext().getTenantId());
            page = this.baseMapper.orderList(pageContext, restOrderParam,dataScope);
        }
        List<Long> userIds = page.getRecords().stream().map(RestOrderSimpleResult::getCreateUser).collect(Collectors.toList());
        List<Long> customerIds = page.getRecords().stream().map(RestOrderSimpleResult::getSellerId).collect(Collectors.toList());
        List<UserResult> userResultsByIds = userIds.size() == 0 ? new ArrayList<>() : userService.getUserResultsByIds(userIds);
        List<Customer> customers = customerIds.size() == 0 ? new ArrayList<>() : customerService.listByIds(customerIds);
        List<CustomerResult> customerResults = BeanUtil.copyToList(customers, CustomerResult.class);
        List<Long> orderIds = page.getRecords().stream().map(RestOrderSimpleResult::getOrderId).distinct().collect(Collectors.toList());
        List<cn.atsoft.dasheng.purchase.entity.RestOrderDetail> detailList =orderIds.size() == 0 ? new ArrayList<>() : orderDetailService.lambdaQuery().in(cn.atsoft.dasheng.purchase.entity.RestOrderDetail::getOrderId, orderIds).list();
        for (RestOrderSimpleResult record : page.getRecords()) {
            for (UserResult userResultsById : userResultsByIds) {
                if (record.getCreateUser().equals(userResultsById.getUserId())){
                    record.setCreateUserResult(userResultsById);
                    break;
                }
            }


            int arrivalNumber = 0;
            int purchaseNumber = 0;
            int inStockNumber = 0;
            List<cn.atsoft.dasheng.purchase.entity.RestOrderDetail> details = new ArrayList<>();
            for (cn.atsoft.dasheng.purchase.entity.RestOrderDetail restOrderDetail : detailList) {
                if (record.getOrderId().equals(restOrderDetail.getOrderId())){
                    details.add(restOrderDetail);
                }
            }
            for (cn.atsoft.dasheng.purchase.entity.RestOrderDetail detail : details) {
                arrivalNumber+=detail.getArrivalNumber();
                purchaseNumber+=detail.getPurchaseNumber();
                inStockNumber += detail.getInStockNumber();
            }
            record.setInStockNumber(inStockNumber);
            record.setArrivalNumber(arrivalNumber);
            record.setPurchaseNumber(purchaseNumber);
            for (CustomerResult customerResult : customerResults) {
                if (record.getSellerId().equals(customerResult.getCustomerId())) {
                    record.setSellerResult(customerResult);
                    break;
                }
            }
        }
        return PageFactory.createPageInfo(page);
    }
    @Override
    public List<RestOrderDetailResult> getOrderDetailList(Map<String, Object> param) {
        return null;
    }

    @Override
    public List<RestOrderDetail> getDetailListByOrderId(Long orderId) {
        return null;
    }

    @Override
    public List<RestOrderDetail> getDetailListByOrderDetailIds(List<Long> detailIds) {
        return null;
    }

    @Override
    public void updateDetailList(List<RestOrderDetail> dataList) {

    }

    @Override
    public cn.atsoft.dasheng.entity.RestOrder getOrderById(Long orderId) {
        RestOrder order = this.getById(orderId);
        if (ToolUtil.isNotEmpty(order)) {
            return BeanUtil.copyProperties(order, cn.atsoft.dasheng.entity.RestOrder.class);
        }
        return null;
    }

    @Override
    public void checkStatus(Long orderId){
        RestOrder order = this.getById(orderId);
        if (ToolUtil.isEmpty(order)) {
            throw new ServiceException(500,"参数错误");
        }
        List<cn.atsoft.dasheng.purchase.entity.RestOrderDetail> orderDetailList = orderDetailService.lambdaQuery().eq(cn.atsoft.dasheng.purchase.entity.RestOrderDetail::getOrderId, order.getOrderId()).eq(cn.atsoft.dasheng.purchase.entity.RestOrderDetail::getDisplay, 1).list();
        int doneNum = 0;
        for (cn.atsoft.dasheng.purchase.entity.RestOrderDetail orderDetail : orderDetailList) {
            if (orderDetail.getPurchaseNumber()<= orderDetail.getInStockNumber()){
                doneNum++;
            }
        }
        if (doneNum == orderDetailList.size()){
            order.setStatus(99);
            this.updateById(order);
        }
    }
}
