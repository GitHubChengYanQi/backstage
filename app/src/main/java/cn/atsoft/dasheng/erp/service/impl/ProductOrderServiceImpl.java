package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.Adress;
import cn.atsoft.dasheng.app.entity.Contacts;
import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.entity.Phone;
import cn.atsoft.dasheng.app.model.result.AdressResult;
import cn.atsoft.dasheng.app.model.result.ContactsResult;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.model.result.PhoneResult;
import cn.atsoft.dasheng.app.service.AdressService;
import cn.atsoft.dasheng.app.service.ContactsService;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.app.service.PhoneService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.ProductOrder;
import cn.atsoft.dasheng.erp.entity.ProductOrderDetails;
import cn.atsoft.dasheng.erp.mapper.ProductOrderMapper;
import cn.atsoft.dasheng.erp.model.params.ProductOrderDetailsParam;
import cn.atsoft.dasheng.erp.model.params.ProductOrderParam;
import cn.atsoft.dasheng.erp.model.params.ProductOrderRequest;
import cn.atsoft.dasheng.erp.model.result.ProductOrderResult;
import cn.atsoft.dasheng.erp.service.ProductOrderDetailsService;
import cn.atsoft.dasheng.erp.service.ProductOrderService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private ContactsService contactsService;
    @Autowired
    private AdressService adressService;
    @Autowired
    private PhoneService phoneService;

    @Override
    @Transactional
    public void add(ProductOrderParam param) {
        ProductOrderRequest productOrderRequest = new ProductOrderRequest();

        productOrderRequest.setAdressId(param.getAdressId());
        productOrderRequest.setContactsId(param.getContactsId());
        productOrderRequest.setPhoneId(param.getPhoneId());
        productOrderRequest.setCustomerId(param.getCustomerId());
        String toJsonStr = JSONUtil.toJsonStr(productOrderRequest);
        param.setCustomer(toJsonStr);

        ProductOrder entity = getEntity(param);
        this.save(entity);
        addOrderDetail(entity.getProductOrderId(), param.getOrderDetail());

    }

    public void addOrderDetail(Long orderId, List<ProductOrderDetailsParam> orderDetail) {
        Integer newMoney = 0;
        Long newNumber = 0L;

        List<ProductOrderDetails> productOrderDetailsList = new ArrayList<>();

        for (ProductOrderDetailsParam productOrderDetailsParam : orderDetail) {
            ProductOrderDetails productOrderDetails = new ProductOrderDetails();

            if (ToolUtil.isNotEmpty(productOrderDetailsParam.getSku())) {
                String toJSONString = JSON.toJSONString(productOrderDetailsParam.getSku());
                productOrderDetails.setSku(toJSONString);
            }


            productOrderDetails.setProductOrderId(orderId);
            productOrderDetails.setMoney(productOrderDetailsParam.getMoney());
            productOrderDetails.setNumber(productOrderDetailsParam.getNumber());

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
        queryWrapper.eq("product_order_id", orderId);
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

        QueryWrapper<ProductOrderDetails> productOrderDetailsQueryWrapper = new QueryWrapper<>();
        productOrderDetailsQueryWrapper.in("product_order_id", param.getProductOrderId());
        productOrderDetailsService.remove(productOrderDetailsQueryWrapper);

        addOrderDetail(param.getProductOrderId(), param.getOrderDetail());

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
        Map<Long, ProductOrderRequest> map = new HashMap<>();
        for (ProductOrderResult datum : data) {
            customerIds.add(datum.getCustomerId());
            ProductOrderRequest productOrderRequest = JSONUtil.toBean(datum.getCustomer(), ProductOrderRequest.class);
            map.put(datum.getProductOrderId(), productOrderRequest);
        }

        List<Long> adressIds = new ArrayList<>();
        List<Long> contactsIds = new ArrayList<>();
        List<Long> phoneIds = new ArrayList<>();

        for (Map.Entry<Long, ProductOrderRequest> entry : map.entrySet()) {
            ProductOrderRequest entryValue = entry.getValue();
            adressIds.add(entryValue.getAdressId());
            contactsIds.add(entryValue.getContactsId());
            phoneIds.add(entryValue.getPhoneId());
        }

        List<Adress> adresses = adressIds.size() == 0 ? new ArrayList<>() : adressService.query().in("adress_id", adressIds).list();

        List<Contacts> contacts = contactsIds.size() == 0 ? new ArrayList<>() : contactsService.query().in("contacts_id", contactsIds).list();

        List<Phone> phones = phoneIds.size() == 0 ? new ArrayList<>() : phoneService.query().in("phone_id", phoneIds).list();

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
            ProductOrderRequest productOrderRequest = map.get(datum.getProductOrderId());
            for (Adress adress : adresses) {
                if (productOrderRequest.getAdressId() != null && adress.getAdressId().equals(productOrderRequest.getAdressId())) {
                    AdressResult adressResult = new AdressResult();
                    ToolUtil.copyProperties(adress, adressResult);
                    datum.setAdressResult(adressResult);
                    break;
                }
            }
            for (Contacts contact : contacts) {
                if (productOrderRequest.getContactsId() != null && contact.getContactsId().equals(productOrderRequest.getContactsId())) {
                    ContactsResult contactsResult = new ContactsResult();
                    ToolUtil.copyProperties(contact, contactsResult);
                    datum.setContactsResult(contactsResult);
                    break;
                }
            }
            for (Phone phone : phones) {
                if (productOrderRequest.getPhoneId() != null && phone.getPhoneId().equals(productOrderRequest.getPhoneId())) {
                    PhoneResult phoneResult = new PhoneResult();
                    ToolUtil.copyProperties(phone, phoneResult);
                    datum.setPhoneResult(phoneResult);
                    break;
                }
            }
        }
    }
}
