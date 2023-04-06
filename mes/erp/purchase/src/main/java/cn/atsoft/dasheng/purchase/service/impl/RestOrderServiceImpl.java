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
    public RestOrder add(RestOrderParam param) {
        return null ;
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
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope());
            page = this.baseMapper.orderList(pageContext, restOrderParam,null);
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
