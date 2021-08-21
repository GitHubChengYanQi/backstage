package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.DeliveryDetailsParam;
import cn.atsoft.dasheng.app.model.params.StockDetailsParam;
import cn.atsoft.dasheng.app.model.result.*;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.mapper.DeliveryMapper;
import cn.atsoft.dasheng.app.model.params.DeliveryParam;
import cn.atsoft.dasheng.core.util.ToolUtil;
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
    private OutstockService outstockService;
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
        return null;
    }

    @Override
    public PageInfo<DeliveryResult> findPageBySpec(DeliveryParam param) {
        Page<DeliveryResult> pageContext = getPageContext();
        IPage<DeliveryResult> page = this.baseMapper.customPageList(pageContext, param);
        List<Long> Iids = new ArrayList<>();
        List<Long> cIds = new ArrayList<>();
        List<Long> aIds = new ArrayList<>();
        List<Long> contactsIds = new ArrayList<>();
        List<Long> pIds = new ArrayList<>();
        for (DeliveryResult record : page.getRecords()) {
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


        for (DeliveryResult record : page.getRecords()) {
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


        return PageFactory.createPageInfo(page);
    }

    @Override
    public void bulkShipment(OutstockRequest outstockRequest) {
        List<Long> ids = outstockRequest.getIds();
        List<Delivery> deliveryList = new ArrayList<>();
        //添加批量发货
            DeliveryParam deliveryParam = new DeliveryParam();
            deliveryParam.setAdressId(outstockRequest.getAdressId());
            deliveryParam.setContactsId(outstockRequest.getContactsId());
            deliveryParam.setCustomerId(outstockRequest.getCustomerId());
            deliveryParam.setPhoneId(outstockRequest.getPhoneId());
            Delivery entity = getEntity(deliveryParam);
            deliveryList.add(entity);

        for (Long id : ids) {
            QueryWrapper<StockDetails> stockDetailsQueryWrapper = new QueryWrapper<>();
            stockDetailsQueryWrapper.in("stock_item_id", id).orderByDesc("storage_time");
            StockDetails stockDetails = new StockDetails();
            stockDetails.setStage(3);
            stockDetailsService.update(stockDetails, stockDetailsQueryWrapper);
        }
        this.saveBatch(deliveryList);
        List<Delivery> deliveries = this.list();
        List<DeliveryDetails> deliveryDetails = new ArrayList<>();

        // 发表详情表添加发货id
        for (Delivery delivery : deliveries) {
            for (Long id : ids) {
                Long deliveryId = delivery.getDeliveryId();
                DeliveryDetails details = new DeliveryDetails();
                details.setDeliveryId(deliveryId);
                details.setStockItemId(id);
                deliveryDetails.add(details);
            }
        } deliveryDetailsService.saveBatch(deliveryDetails);


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
