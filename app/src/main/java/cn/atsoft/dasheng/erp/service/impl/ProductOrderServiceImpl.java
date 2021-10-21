package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.ProductOrder;
import cn.atsoft.dasheng.erp.mapper.ProductOrderMapper;
import cn.atsoft.dasheng.erp.model.params.ProductOrderParam;
import cn.atsoft.dasheng.erp.model.result.ProductOrderResult;
import cn.atsoft.dasheng.erp.service.ProductOrderDetailsService;
import cn.atsoft.dasheng.erp.service.ProductOrderService;
import cn.atsoft.dasheng.core.util.ToolUtil;
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
    public void add(ProductOrderParam param) {
        long newMoney = param.getNumber() * param.getMoney();
        param.setMoney((int) newMoney);
        ProductOrder entity = getEntity(param);
        this.save(entity);
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
