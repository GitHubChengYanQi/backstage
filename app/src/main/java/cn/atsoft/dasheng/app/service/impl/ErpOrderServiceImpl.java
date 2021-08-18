package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.result.*;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.mapper.ErpOrderMapper;
import cn.atsoft.dasheng.app.model.params.ErpOrderParam;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.portal.model.exception.ServiceException;
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
 * 订单表 服务实现类
 * </p>
 *
 * @author ta
 * @since 2021-07-26
 */
@Service
public class ErpOrderServiceImpl extends ServiceImpl<ErpOrderMapper, ErpOrder> implements ErpOrderService {
    @Autowired
    private ContactsService contactsService;
    @Autowired
    private ItemsService itemsService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AdressService adressService;
    @Autowired
    private PhoneService phoneService;

    @BussinessLog
    @Override
    public ErpOrder add(ErpOrderParam param) {
        ErpOrder entity = getEntity(param);
        this.save(entity);
        return entity;
    }

    @BussinessLog
    @Override
    public void delete(ErpOrderParam param) {

        ErpOrder byId = this.getById(param.getOrderId());
        if (ToolUtil.isEmpty(byId)) {
            throw new ServiceException(500, "删除目标不存在");
        }
        param.setDisplay(0);
        ErpOrder oldEntity = getOldEntity(param);
        ErpOrder newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @BussinessLog
    @Override
    public void update(ErpOrderParam param) {
        ErpOrder oldEntity = getOldEntity(param);
        ErpOrder newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(oldEntity);

    }



    @Override
    public ErpOrderResult findBySpec(ErpOrderParam param) {
        return null;
    }

    @Override
    public List<ErpOrderResult> findListBySpec(ErpOrderParam param) {
        return null;
    }

    @Override
    public PageInfo<ErpOrderResult> findPageBySpec(ErpOrderParam param) {
        Page<ErpOrderResult> pageContext = getPageContext();
        IPage<ErpOrderResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ErpOrderParam param) {
        return param.getOrderId();
    }

    private Page<ErpOrderResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ErpOrder getOldEntity(ErpOrderParam param) {
        return this.getById(getKey(param));
    }

    private ErpOrder getEntity(ErpOrderParam param) {
        ErpOrder entity = new ErpOrder();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    public void format(List<ErpOrderResult> data) {
        List<Long> itemIds = new ArrayList<>();
        List<Long> partA = new ArrayList<>();
        List<Long> partB = new ArrayList<>();
        List<Long> contactsIdsA = new ArrayList<>();
        List<Long> contactsIdsB = new ArrayList<>();
        List<Long> adressIdsA = new ArrayList<>();
        List<Long> adressIdsB = new ArrayList<>();
        List<Long> phoneAIds = new ArrayList<>();
        List<Long> phoneBIds = new ArrayList<>();

        for (ErpOrderResult datum : data) {

            itemIds.add(datum.getItemId());
            partA.add(datum.getPartyA());
            partB.add(datum.getPartyB());
            contactsIdsA.add(datum.getPartyAContactsId());
            contactsIdsB.add(datum.getPartyBContactsId());
            adressIdsA.add(datum.getPartyAAdressId());
            adressIdsB.add(datum.getPartyBAdressId());
            phoneAIds.add(datum.getPartyAPhone());
            phoneBIds.add(datum.getPartyBPhone());
        }

        QueryWrapper<Customer> customerQueryWrapper = new QueryWrapper<>();
        if(ToolUtil.isNotEmpty(partA)){
            customerQueryWrapper.in("customer_id", partA);
        }
        List<Customer> partAList = customerService.list(customerQueryWrapper);




        QueryWrapper<Customer> customerQueryWrapperB = new QueryWrapper<>();
        if(ToolUtil.isNotEmpty(partB)){
            customerQueryWrapperB.in("customer_id", partB);
        }
        List<Customer> partBList = customerService.list(customerQueryWrapperB);

        QueryWrapper<Contacts> contactsA = new QueryWrapper<>();
        if(ToolUtil.isNotEmpty(contactsIdsA)){
            contactsA.in("contacts_id", contactsIdsA);
        }
        List<Contacts> contactsAList = contactsService.list(contactsA);

        QueryWrapper<Contacts> contactsB = new QueryWrapper<>();
        if(ToolUtil.isNotEmpty(contactsIdsB)){
            contactsB.in("contacts_id", contactsIdsB);
        }
        List<Contacts> contactsBList = contactsService.list(contactsB);

        QueryWrapper<Adress> adressA = new QueryWrapper<>();
        if(ToolUtil.isNotEmpty(adressIdsA)){
            adressA.in("adress_id", adressIdsA);
        }
        List<Adress> adressAList = adressService.list(adressA);

        QueryWrapper<Adress> adressB = new QueryWrapper<>();
        if(ToolUtil.isNotEmpty(adressIdsB)){
            adressB.in("adress_id", adressIdsB);
        }
        List<Adress> adressBList = adressService.list(adressB);

        QueryWrapper<Phone> phoneAwapper = new QueryWrapper<>();
        if(ToolUtil.isNotEmpty(phoneAIds)){
            phoneAwapper.in("phone_id", phoneAIds);
        }

        List<Phone> phoneAlist = phoneService.list(phoneAwapper);

        QueryWrapper<Phone> phoneBwapper = new QueryWrapper<>();
        if(ToolUtil.isNotEmpty(phoneBIds)){
            contactsA.in("contacts_id", phoneBIds);
        }
        List<Phone> phoneBlist = phoneService.list(phoneBwapper);

        QueryWrapper<Items> itemsQueryWrapper = new QueryWrapper<>();
        if (!itemIds.isEmpty()) {
            itemsQueryWrapper.in("item_id", itemIds);
        }

        List<Items> itemsList = itemsService.list(itemsQueryWrapper);


        for (ErpOrderResult datum : data) {
            if(ToolUtil.isNotEmpty(partAList)){
                for (Customer customer : partAList) {
                    if (datum.getPartyA().equals(customer.getCustomerId())) {
                        CustomerResult customerResult = new CustomerResult();
                        ToolUtil.copyProperties(customer, customerResult);
                        datum.setPartA(customerResult);
                    }

                }
            }
            if(ToolUtil.isNotEmpty(partBList)) {
                for (Customer customer : partBList) {
                    if (customer.getCustomerId().equals(datum.getPartyB())) {
                        CustomerResult customerResult = new CustomerResult();
                        ToolUtil.copyProperties(customer, customerResult);
                        datum.setPartB(customerResult);
                    }
                }
            }
            if(ToolUtil.isNotEmpty(contactsAList)) {
                for (Contacts contacts : contactsAList) {
                    if (contacts.getContactsId().equals(datum.getPartyAContactsId())) {
                        ContactsResult contactsResult = new ContactsResult();
                        ToolUtil.copyProperties(contacts, contactsResult);
                        datum.setPartyAContacts(contactsResult);
                        break;
                    }
                }
            }
            if(ToolUtil.isNotEmpty(contactsBList)) {
                for (Contacts contacts : contactsBList) {
                    if (contacts.getContactsId().equals(datum.getPartyBContactsId())) {
                        ContactsResult contactsResult = new ContactsResult();
                        ToolUtil.copyProperties(contacts, contactsResult);
                        datum.setPartyBContacts(contactsResult);
                        break;
                    }
                }
            }
            if(ToolUtil.isNotEmpty(adressAList)) {
                for (Adress adress : adressAList) {
                    if (adress.getAdressId().equals(datum.getPartyAAdressId())) {
                        AdressResult adressResult = new AdressResult();
                        ToolUtil.copyProperties(adress, adressResult);
                        datum.setPartyAAdress(adressResult);
                        break;
                    }
                }
            }
            if(ToolUtil.isNotEmpty(adressBList)) {
                for (Adress adress : adressBList) {

                    if (adress.getAdressId().equals(datum.getPartyBAdressId())) {
                        adress.getCustomerId();
                        AdressResult adressResult = new AdressResult();
                        ToolUtil.copyProperties(adress, adressResult);
                        datum.setPartyBAdress(adressResult);
                        break;
                    }
                }
            }
            if(ToolUtil.isNotEmpty(phoneAlist)) {
                for (Phone phone : phoneAlist) {
                    if (phone.getPhoneId().equals(datum.getPartyAPhone())) {
                        PhoneResult phoneResult = new PhoneResult();
                        ToolUtil.copyProperties(phone, phoneResult);
                        datum.setPhoneA(phoneResult);
                        break;
                    }
                }
            }
            if(ToolUtil.isNotEmpty(phoneBlist)) {
                for (Phone phone : phoneBlist) {
                    if (phone.getPhoneId().equals(datum.getPartyBPhone())) {
                        PhoneResult phoneResult = new PhoneResult();
                        ToolUtil.copyProperties(phone, phoneResult);
                        datum.setPhoneB(phoneResult);
                        break;
                    }
                }
            }
            if (ToolUtil.isNotEmpty(itemsList)) {
                for (Items items : itemsList) {
                    if (items.getItemId().equals(datum.getItemId())) {
                        ItemsResult itemsResult = new ItemsResult();
                        ToolUtil.copyProperties(items, itemsResult);
                        datum.setItemsResult(itemsResult);
                        break;
                    }
                }
            }

        }
    }
}
