package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.*;

import cn.atsoft.dasheng.app.model.params.OutstockParam;
import cn.atsoft.dasheng.app.model.params.StockDetailsParam;
import cn.atsoft.dasheng.app.model.result.*;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.mapper.DeliveryMapper;
import cn.atsoft.dasheng.app.model.params.DeliveryParam;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 发货表 服务实现类
 * </p>
 *
 * @author
 * @since 2021-08-20
 */
@Service
public class DeliveryServiceImpl extends ServiceImpl<DeliveryMapper, Delivery> implements DeliveryService {
    @Autowired
    private DeliveryDetailsService deliveryDetailsService;
    @Autowired
    private StockDetailsService stockDetailsService;
    @Autowired
    private ItemsService itemsService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AdressService adressService;
    @Autowired
    private ContactsService contactsService;
    @Autowired
    private PhoneService phoneService;
    @Autowired
    private OutstockService outStockService;



    @Override
    public Long add(DeliveryParam param) {
        Delivery entity = getEntity(param);
        this.save(entity);
        return entity.getDeliveryId();
    }

    @Override
    public void delete(DeliveryParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(DeliveryParam param) {
        Delivery oldEntity = getOldEntity(param);
        Delivery newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public DeliveryResult findBySpec(DeliveryParam param) {
        return null;
    }

    @Override
    public List<DeliveryResult> findListBySpec(DeliveryParam param) {
        List<DeliveryResult> deliveryResults = this.baseMapper.customList(param);
        return deliveryResults;
    }

    @Override
    public PageInfo<DeliveryResult> findPageBySpec(DeliveryParam param, DataScope dataScope ) {
        Page<DeliveryResult> pageContext = getPageContext();
        IPage<DeliveryResult> page = this.baseMapper.customPageList(pageContext, param,dataScope);
        format(page.getRecords());

        return PageFactory.createPageInfo(page);
    }

    @Override
    public void format(List<DeliveryResult> data){

        List<Long> Iids = new ArrayList<>();
        List<Long> cIds = new ArrayList<>();
        List<Long> aIds = new ArrayList<>();
        List<Long> contactsIds = new ArrayList<>();
        List<Long> pIds = new ArrayList<>();
        for (DeliveryResult record : data) {
            Iids.add(record.getItemId());
            cIds.add(record.getCustomerId());
            aIds.add(record.getAdressId());
            contactsIds.add(record.getContactsId());
            pIds.add(record.getPhoneId());
        }
        QueryWrapper<Items> itemsQueryWrapper = new QueryWrapper<>();
        itemsQueryWrapper.in("item_id", Iids);
        List<Items> itemsList = Iids.size() == 0 ? new ArrayList<>() : itemsService.list(itemsQueryWrapper);

        QueryWrapper<Customer> customerQueryWrapper = new QueryWrapper<>();
        customerQueryWrapper.in("customer_id", cIds);
        List<Customer> customerList = customerService.list(customerQueryWrapper);

        QueryWrapper<Adress> adressQueryWrapper = new QueryWrapper<>();
        adressQueryWrapper.in("adress_id", aIds);
        List<Adress> adressList = adressService.list(adressQueryWrapper);

        QueryWrapper<Contacts> contactsQueryWrapper = new QueryWrapper<>();
        contactsQueryWrapper.in("contacts_id", contactsIds);
        List<Contacts> contactsList = contactsService.list(contactsQueryWrapper);

        QueryWrapper<Phone> phoneQueryWrapper = new QueryWrapper<>();
        phoneQueryWrapper.in("phone_id", pIds);
        List<Phone> phoneList = phoneService.list(phoneQueryWrapper);


        for (DeliveryResult record : data) {
            for (Items items : itemsList) {
                if (items.getItemId().equals(record.getItemId())) {
                    ItemsResult itemsResult = new ItemsResult();
                    ToolUtil.copyProperties(items, itemsResult);
                    record.setItemsResult(itemsResult);
                    break;
                }
            }
            for (Customer customer : customerList) {
                if (customer.getCustomerId().equals(record.getCustomerId())) {
                    CustomerResult customerResult = new CustomerResult();
                    ToolUtil.copyProperties(customer, customerResult);
                    record.setCustomerResult(customerResult);
                    break;
                }
            }
            for (Adress adress : adressList) {
                if (adress.getAdressId().equals(record.getAdressId())) {
                    AdressResult adressResult = new AdressResult();
                    ToolUtil.copyProperties(adress, adressResult);
                    record.setAdressResult(adressResult);
                    break;
                }
            }
            for (Contacts contacts : contactsList) {
                if (contacts.getContactsId().equals(record.getContactsId())) {
                    ContactsResult contactsResult = new ContactsResult();
                    ToolUtil.copyProperties(contacts, contactsResult);
                    record.setContactsResult(contactsResult);
                    break;
                }
            }
            for (Phone phone : phoneList) {
                if (phone.getPhoneId().equals(record.getPhoneId())) {
                    PhoneResult phoneResult = new PhoneResult();
                    ToolUtil.copyProperties(phone, phoneResult);
                    record.setPhoneResult(phoneResult);
                    break;
                }
            }
        }

    }

    @Override
    public void bulkShipment(OutstockRequest outstockRequest) {
        List<Delivery> deliveryList = new ArrayList<>();
        List<OutstockParam> outstockParamList = outstockRequest.getIds();
        List<Long> ids = new ArrayList<>();

        for (OutstockParam deliveryDetailsParam : outstockParamList) {
            ids.add(deliveryDetailsParam.getStockItemId());
        }
        QueryWrapper<Outstock> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("stock_item_id", ids);
        List<Long> itemIds = new ArrayList<>();

        List<Outstock> list = outStockService.list(queryWrapper);

        for (Outstock stockDetails : list) {
            itemIds.add(stockDetails.getItemId());
        }


        //添加批量发货
        DeliveryParam deliveryParam = new DeliveryParam();
        deliveryParam.setAdressId(outstockRequest.getAdressId());
        deliveryParam.setContactsId(outstockRequest.getContactsId());
        deliveryParam.setCustomerId(outstockRequest.getCustomerId());
        deliveryParam.setPhoneId(outstockRequest.getPhoneId());
        Long add = this.add(deliveryParam);

        for (OutstockParam deliveryDetailsParam : outstockRequest.getIds()) {
            QueryWrapper<Outstock> stockDetailsQueryWrapper = new QueryWrapper<>();
            stockDetailsQueryWrapper.in("stock_item_id", deliveryDetailsParam.getStockItemId());
            Outstock stockDetails = new Outstock();
            stockDetails.setState(1L);
            outStockService.update(stockDetails, stockDetailsQueryWrapper);
        }
        this.saveBatch(deliveryList);

        List<DeliveryDetails> deliveryDetails = new ArrayList<>();

        // 发表详情表添加发货id

        for (OutstockParam deliveryDetailsParam : outstockRequest.getIds()) {
            DeliveryDetails details = new DeliveryDetails();
            details.setDeliveryId(add);
            details.setStockItemId(deliveryDetailsParam.getStockItemId());
            details.setItemId(deliveryDetailsParam.getItemId());
            details.setBrandId(deliveryDetailsParam.getBrandId());
            deliveryDetails.add(details);

        }

        deliveryDetailsService.saveBatch(deliveryDetails);


    }

    private Serializable getKey(DeliveryParam param) {
        return param.getDeliveryId();
    }

    private Page<DeliveryResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Delivery getOldEntity(DeliveryParam param) {
        return this.getById(getKey(param));
    }

    private Delivery getEntity(DeliveryParam param) {
        Delivery entity = new Delivery();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }


}
