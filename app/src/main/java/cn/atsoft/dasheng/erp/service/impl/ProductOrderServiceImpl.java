package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.ProductOrder;
import cn.atsoft.dasheng.erp.entity.ProductOrderDetails;
import cn.atsoft.dasheng.erp.mapper.ProductOrderMapper;
import cn.atsoft.dasheng.erp.model.params.ProductOrderDetailsParam;
import cn.atsoft.dasheng.erp.model.params.ProductOrderParam;
import cn.atsoft.dasheng.erp.model.result.ProductOrderResult;
import cn.atsoft.dasheng.erp.service.ProductOrderDetailsService;
import cn.atsoft.dasheng.erp.service.ProductOrderService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 产品订单 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-20
 */
@Service
public class ProductOrderServiceImpl extends ServiceImpl<ProductOrderMapper, ProductOrder> implements ProductOrderService {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProductOrderDetailsService productOrderDetailsService;

    @Override
    @Transactional
    public void add(ProductOrderParam param) {
        ProductOrder entity = getEntity(param);
        this.save(entity);
        Integer newMoney = 0;
        Long newNumber = 0L;
        List<ProductOrderDetails> productOrderDetailsList = new ArrayList<>();
        if (ToolUtil.isEmpty(param.getOrderDetail())) {
            throw  new ServiceException(500,"请选择规格");
        }
        for (ProductOrderDetailsParam productOrderDetailsParam : param.getOrderDetail()) {
            ProductOrderDetails productOrderDetails = new ProductOrderDetails();
            productOrderDetails.setProductOrderId(entity.getProductOrderId());
            productOrderDetails.setMoney(productOrderDetailsParam.getMoney());
            productOrderDetails.setNumber(productOrderDetailsParam.getNumber());
            productOrderDetails.setSkuId(productOrderDetailsParam.getSkuId());
            productOrderDetails.setSpuId(productOrderDetailsParam.getSpuId());
            productOrderDetailsList.add(productOrderDetails);

            //计算总金额
            Integer money = productOrderDetailsParam.getMoney();
            Long number = productOrderDetailsParam.getNumber();
            newMoney = newMoney + Math.toIntExact(money * number);
            //计算数量
            newNumber = newNumber + productOrderDetailsParam.getNumber();
        }
        productOrderDetailsService.saveBatch(productOrderDetailsList);
        ProductOrder productOrder = new ProductOrder();
        productOrder.setMoney(newMoney);
        productOrder.setNumber(newNumber);
        QueryWrapper<ProductOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_order_id", entity.getProductOrderId());
        this.update(productOrder, queryWrapper);


    }

    @Override
    public void delete(ProductOrderParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(ProductOrderParam param) {
        ProductOrder oldEntity = getOldEntity(param);
        ProductOrder newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ProductOrderResult findBySpec(ProductOrderParam param) {
        return null;
    }

    @Override
    public List<ProductOrderResult> findListBySpec(ProductOrderParam param) {
        return null;
    }

    @Override
    public PageInfo<ProductOrderResult> findPageBySpec(ProductOrderParam param) {
        Page<ProductOrderResult> pageContext = getPageContext();
        IPage<ProductOrderResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ProductOrderParam param) {
        return param.getProductOrderId();
    }

    private Page<ProductOrderResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ProductOrder getOldEntity(ProductOrderParam param) {
        return this.getById(getKey(param));
    }

    private ProductOrder getEntity(ProductOrderParam param) {
        ProductOrder entity = new ProductOrder();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    public void format(List<ProductOrderResult> data) {
        List<Long> customerIds = new ArrayList<>();
        for (ProductOrderResult datum : data) {
            customerIds.add(datum.getCustomerId());
        }
        List<Customer> customers = customerIds.size() == 0 ? new ArrayList<>() : customerService.lambdaQuery()
                .in(Customer::getCustomerId, customerIds)
                .list();

        for (ProductOrderResult datum : data) {
            for (Customer customer : customers) {
                if (datum.getCustomerId() != null && datum.getCustomerId().equals(customer.getCustomerId())) {
                    CustomerResult customerResult = new CustomerResult();
                    ToolUtil.copyProperties(customer, customerResult);
                    datum.setCustomerResult(customerResult);
                    break;
                }
            }
        }
    }
}
