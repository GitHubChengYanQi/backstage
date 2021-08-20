package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.result.*;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.mapper.DeliveryDetailsMapper;
import cn.atsoft.dasheng.app.model.params.DeliveryDetailsParam;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author
 * @since 2021-08-20
 */
@Service
public class DeliveryDetailsServiceImpl extends ServiceImpl<DeliveryDetailsMapper, DeliveryDetails> implements DeliveryDetailsService {
    @Autowired
    private DeliveryService deliveryService;
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

    @Override
    public DeliveryDetails add(DeliveryDetailsParam param) {
        DeliveryDetails entity = getEntity(param);
        this.save(entity);
        return entity;
    }

    @Override
    public void delete(DeliveryDetailsParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(DeliveryDetailsParam param) {
        DeliveryDetails oldEntity = getOldEntity(param);
        DeliveryDetails newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public DeliveryDetailsResult findBySpec(DeliveryDetailsParam param) {
        return null;
    }

    @Override
    public List<DeliveryDetailsResult> findListBySpec(DeliveryDetailsParam param) {
        return null;
    }

    @Override
    public PageInfo<DeliveryDetailsResult> findPageBySpec(DeliveryDetailsParam param) {
        Page<DeliveryDetailsResult> pageContext = getPageContext();
        IPage<DeliveryDetailsResult> page = this.baseMapper.customPageList(pageContext, param);
        List<Long> dids = new ArrayList<>();
        List<Long> Iids = new ArrayList<>();
        List<Long> cIds = new ArrayList<>();
        List<Long> aIds = new ArrayList<>();
        List<Long> contactsIds = new ArrayList<>();
        List<Long> pIds = new ArrayList<>();
        for (DeliveryDetailsResult record : page.getRecords()) {
            dids.add(record.getDeliveryId());
            Iids.add(record.getItemId());
            cIds.add(record.getCustomerId());
            aIds.add(record.getAdressId());
            contactsIds.add(record.getContactsId());
            pIds.add(record.getPhoneId());

        }
        QueryWrapper<Delivery> deliveryQueryWrapper = new QueryWrapper<>();
        deliveryQueryWrapper.in("delivery_id", dids);
        List<Delivery> deliveryList = dids.size() == 0 ? new ArrayList<>() : deliveryService.list(deliveryQueryWrapper);


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

        for (DeliveryDetailsResult record : page.getRecords()) {
            for (Delivery delivery : deliveryList) {
                if (record.getDeliveryId().equals(delivery.getDeliveryId())) {
                    DeliveryResult deliveryResult = new DeliveryResult();
                    ToolUtil.copyProperties(delivery, deliveryResult);
                    record.setDeliveryResult(deliveryResult);
                    break;
                }
            }
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
                    ContactsRequest contactsRequest = new ContactsRequest();
                    ToolUtil.copyProperties(contacts, contactsRequest);
                    record.setContactsRequest(contactsRequest);
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
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(DeliveryDetailsParam param) {
        return param.getDeliveryDetailsId();
    }

    private Page<DeliveryDetailsResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private DeliveryDetails getOldEntity(DeliveryDetailsParam param) {
        return this.getById(getKey(param));
    }

    private DeliveryDetails getEntity(DeliveryDetailsParam param) {
        DeliveryDetails entity = new DeliveryDetails();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
