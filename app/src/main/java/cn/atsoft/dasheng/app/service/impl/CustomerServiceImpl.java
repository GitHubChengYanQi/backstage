package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.*;
import cn.atsoft.dasheng.app.model.result.*;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.log.FreedLog;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.mapper.CustomerMapper;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.entity.ContactsBind;
import cn.atsoft.dasheng.crm.entity.Invoice;
import cn.atsoft.dasheng.crm.entity.TrackMessage;
import cn.atsoft.dasheng.crm.model.params.ContactsBindParam;
import cn.atsoft.dasheng.crm.model.params.InvoiceParam;
import cn.atsoft.dasheng.crm.model.result.InvoiceResult;
import cn.atsoft.dasheng.crm.model.result.SupplyResult;
import cn.atsoft.dasheng.crm.service.ContactsBindService;
import cn.atsoft.dasheng.crm.service.InvoiceService;
import cn.atsoft.dasheng.crm.service.TrackMessageService;
import cn.atsoft.dasheng.crm.service.SupplyService;
import cn.atsoft.dasheng.message.enmu.MicroServiceType;
import cn.atsoft.dasheng.message.enmu.OperationType;
import cn.atsoft.dasheng.message.entity.MicroServiceEntity;
import cn.atsoft.dasheng.message.producer.MessageProducer;
import cn.atsoft.dasheng.model.exception.ServiceException;

import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.aliyuncs.ram.model.v20150501.CreateUserRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 客户管理表 服务实现类
 * </p>
 *
 * @author
 * @since 2021-07-23
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

    @Autowired
    private OriginService originService;
    @Autowired
    private CrmCustomerLevelService crmCustomerLevelService;
    @Autowired
    private UserService userService;
    @Autowired
    private CrmIndustryService crmIndustryService;
    @Autowired
    private ContactsService contactsService;
    @Autowired
    private AdressService adressService;
    @Autowired
    private PhoneService phoneService;
    @Autowired
    private ContactsBindService contactsBindService;
    @Autowired
    private CrmBusinessService crmBusinessService;
    @Autowired
    private ContractService contractService;
    @Autowired
    private TrackMessageService trackMessageService;
    @Autowired
    private SupplyService supplyService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private BrandService brandService;

    @Autowired
    private CrmCustomerLevelService levelService;

    @Autowired
    private MessageProducer messageProducer;

    @Override
    @FreedLog
    @Transactional
    public Customer add(CustomerParam param) {
        //查询数据库是否已有同名客户
        List<Customer> customerList = this.query().eq("display", 1).eq("customer_name", param.getCustomerName()).list();

        //有同名客户 阻止添加
        if (!ToolUtil.isEmpty(customerList)) {
            throw new ServiceException(500, "已有当前客户");
        }
        param.setCustomerId(null);
        Customer entity = getEntity(param);
        this.save(entity);

        //添加联系人
        if (ToolUtil.isNotEmpty(param.getContactsParams())) {
            for (int i = 0; i < param.getContactsParams().size(); i++) {
                if (ToolUtil.isNotEmpty(param.getContactsParams().get(i).getContactsName()) && !param.getContactsParams().get(i).getContactsName().equals("")) {
                    Long conId = contactsService.insert(param.getContactsParams().get(i));
                    ContactsBindParam contactsBindParam = new ContactsBindParam();
                    contactsBindParam.setCustomerId(entity.getCustomerId());
                    contactsBindParam.setContactsId(conId);
                    contactsBindService.add(contactsBindParam);
                    //添加默认联系人
                    if (i == 0) {
                        entity.setDefaultContacts(conId);
                    }
                }
            }
        }

        //添加地址
        if (ToolUtil.isNotEmpty(param.getAdressParams())) {
            for (int i = 0; i < param.getAdressParams().size(); i++) {
                if (ToolUtil.isNotEmpty(param.getAdressParams().get(i).getMap()) && !param.getAdressParams().get(i).getMap().getAddress().equals("")) {
                    param.getAdressParams().get(i).setCustomerId(entity.getCustomerId());
                    Adress adress = adressService.add(param.getAdressParams().get(i));
                    entity.setDefaultAddress(adress.getAdressId());
                    if (i == 0) {
                        entity.setDefaultAddress(adress.getAdressId());
                    }
                }
            }

        }

        for (int i = 0; i < param.getInvoiceParams().size(); i++) {
            param.getInvoiceParams().get(i).setCustomerId(entity.getCustomerId());
            Long add = invoiceService.add(param.getInvoiceParams().get(i));
            if (i == 0) {
                entity.setInvoiceId(add);
            }
        }

        if (param.getSupply() == 1) {   //供应商
            supplyService.addList(param.getSupplyParams(), entity.getCustomerId());
            brandService.save(new Brand() {{
                setBrandName(entity.getCustomerName());
            }});
        }

        this.updateById(entity);
        return entity;

    }

    @Override
    @FreedLog
    public Customer updateChargePerson(CustomerParam param) {
        Customer customer = new Customer();
        customer.setCustomerId(param.getCustomerId());
        customer.setUserId(param.getUserId());
        this.updateById(customer);
        return customer;
    }


    @Override
    @FreedLog
    public Customer delete(CustomerParam param) {
        param.setDisplay(0);
        Long customerId = param.getCustomerId();
        Customer oldEntity = getOldEntity(param);
        Customer newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);

        QueryWrapper<ContactsBind> contactsBindQueryWrapper = new QueryWrapper<>();
        contactsBindQueryWrapper.in("customer_id", customerId);
        List<ContactsBind> list = contactsBindService.list(contactsBindQueryWrapper);
        List<Long> contactsBindId = new ArrayList<>();
        for (ContactsBind contactsBind : list) {
            contactsBindId.add(contactsBind.getContactsBindId());
            QueryWrapper<Phone> phoneQueryWrapper = new QueryWrapper<>();
            phoneQueryWrapper.in("contacts_id", contactsBind.getContactsId());
            List<Phone> phoneList = phoneService.list(phoneQueryWrapper);
            List<Long> phoneId = new ArrayList<>();
            for (Phone phone : phoneList) {
                phoneId.add(phone.getPhoneId());
            }
            phoneService.removeByIds(phoneId);
        }
        contactsBindService.removeByIds(contactsBindId);

        QueryWrapper<Adress> adressQueryWrapper = new QueryWrapper<>();
        adressQueryWrapper.in("customer_id", customerId);
        List<Adress> list1 = adressService.list(adressQueryWrapper);
        List<Long> adressId = new ArrayList<>();
        for (Adress adress : list1) {
            adressId.add(adress.getAdressId());
        }
        adressService.removeByIds(adressId);

        return newEntity;
    }

    @Override
    @FreedLog
    public Customer update(CustomerParam param) {
        Customer oldEntity = getOldEntity(param);
        if (ToolUtil.isEmpty(oldEntity)) {
            throw new ServiceException(500, "数据不存在");
        } else {
            Customer newEntity = getEntity(param);
            ToolUtil.copyProperties(newEntity, oldEntity);
            this.updateById(oldEntity);
            return oldEntity;
        }

    }

    @Override
    public CustomerResult findBySpec(CustomerParam param) {
        return null;
    }

    @Override
    public List<CustomerResult> findListBySpec(CustomerParam param) {
        return null;
    }

    @Override
    public PageInfo<CustomerResult> findPageBySpec(DataScope dataScope, CustomerParam param) {
        Page<CustomerResult> pageContext = getPageContext();
        IPage<CustomerResult> page = this.baseMapper.customPageList(pageContext, param, dataScope);
        format(page.getRecords());

        return PageFactory.createPageInfo(page);
    }

    public CustomerResult format(List<CustomerResult> data) {

//        List<Long> dycustomerIds = new ArrayList<>();
        List<Long> originIds = new ArrayList<>();
        List<Long> levelIds = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        List<Long> industryIds = new ArrayList<>();
        List<Long> contactsIds = new ArrayList<>();
        List<Long> customerIds = new ArrayList<>();
        List<Long> invoiceIds = new ArrayList<>();
        List<Long> adressIds = new ArrayList<>();
        Long customerId = null;


        for (CustomerResult record : data) {
            originIds.add(record.getOriginId());
            levelIds.add(record.getCustomerLevelId());
            userIds.add(record.getUserId());
            userIds.add(record.getCreateUser());
            industryIds.add(record.getIndustryId());
//            dycustomerIds.add(record.getCustomerId());
            customerIds.add(record.getCustomerId());
            customerId = record.getCustomerId();
            invoiceIds.add(record.getInvoiceId());
            adressIds.add(record.getDefaultAddress());
            if (ToolUtil.isNotEmpty(record.getDefaultContacts())) {
                contactsIds.add(record.getDefaultContacts());
            }
        }

        List<ContactsBind> contactsBinds = contactsBindService.lambdaQuery()
                .in(ContactsBind::getCustomerId, customerId)
                .and(i -> i.in(ContactsBind::getDisplay, 1))
                .list();

        for (ContactsBind contactsBind : contactsBinds) {
            contactsIds.add(contactsBind.getContactsId());
        }

        List<Invoice> invoices = invoiceIds.size() == 0 ? new ArrayList<>() : invoiceService.query().in("customer_id",customerIds).eq("display",1).list();
        /***
         * 默认地址
         */
        List<Adress> adresses = adressIds.size() == 0 ? new ArrayList<>() : adressService.query().in("adress_id", adressIds).eq("display", 1).list();

        /**
         * 获取联系人
         */
        QueryWrapper<Contacts> contactsQueryWrapper = new QueryWrapper<>();
        contactsQueryWrapper.in("contacts_id", contactsIds);
        List<Contacts> contactsList = contactsIds.size() == 0 ? new ArrayList<>() : contactsService.list(contactsQueryWrapper);
        List<ContactsResult> contactsResultList = new ArrayList<>();
        for (Contacts contacts : contactsList) {
            ContactsResult contactsResult = new ContactsResult();
            ToolUtil.copyProperties(contacts, contactsResult);
            contactsResultList.add(contactsResult);
        }
        contactsService.format(contactsResultList);

        /**
         * 获取originId
         * */
        QueryWrapper<Origin> originQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Origin> origin_id = originQueryWrapper.in("origin_id", originIds);
        List<Origin> originList = originIds.size() == 0 ? new ArrayList<>() : originService.list(origin_id);


        /**
         * 获取LevelId
         * */
        QueryWrapper<CrmCustomerLevel> levelQueryWrapper = new QueryWrapper<>();
        QueryWrapper<CrmCustomerLevel> customerLevelId = levelQueryWrapper.in("customer_level_id", levelIds);
        List<CrmCustomerLevel> levelList = levelIds.size() == 0 ? new ArrayList<>() : crmCustomerLevelService.list(customerLevelId);
        /**
         * 获取userId
         * */
        List<Long> collectUserIds = userIds.stream().distinct().collect(Collectors.toList());
//        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
//        userQueryWrapper.in("user_id", collectUserIds);
//        List<User> userList = userIds.size() == 0 ? new ArrayList<>() : userService.list(userQueryWrapper);
        List<UserResult> userResults = userIds.size() == 0 ? new ArrayList<>() : userService.getUserResultsByIds(userIds);
        QueryWrapper<CrmIndustry> industryQueryWrapper = new QueryWrapper<>();
        industryQueryWrapper.in("industry_id", industryIds);
        List<CrmIndustry> industryList = industryIds.size() == 0 ? new ArrayList<>() : crmIndustryService.list(industryQueryWrapper);
        List<Adress> adressList = null;

        if (customerId != null) {
            adressList = adressService.lambdaQuery().eq(Adress::getCustomerId, customerId).list();
        }


        for (CustomerResult record : data) {

            for (Long id : customerIds) {
                if (record.getCustomerId().equals(id)) {

                    Integer businessCount = crmBusinessService.lambdaQuery().in(CrmBusiness::getCustomerId, id)
                            .and(i -> i.in(CrmBusiness::getDisplay, 1)).count();

                    Integer contracrCount = contractService.lambdaQuery().in(Contract::getPartyA, id)
                            .and(i -> i.in(Contract::getDisplay, 1))
                            .count();

                    Integer dynamicCount = trackMessageService.lambdaQuery().in(TrackMessage::getCustomerId, id)
                            .and(i -> i.in(TrackMessage::getDisplay, 1))
                            .count();

                    Integer contactsCount = contactsBindService.lambdaQuery()
                            .and(i -> i.in(ContactsBind::getDisplay, 1))
                            .in(ContactsBind::getCustomerId, id).count();

                    record.setBusinessCount(businessCount);
                    record.setContactsCount(contactsCount);
                    record.setDynamicCount(dynamicCount);
                    record.setContracrCount(contracrCount);
                }
            }


            if (ToolUtil.isNotEmpty(record.getClassification()) && record.getClassification() == 1) {
                record.setClassificationName("终端用户");
            } else if (ToolUtil.isNotEmpty(record.getClassification()) && record.getClassification() == 0) {
                record.setClassificationName("代理商");
            }
            List<InvoiceResult> invoiceResults = new ArrayList<>();
            for (Invoice invoice : invoices) {      //对比开票
                if (invoice.getCustomerId().equals(record.getCustomerId())) {
                    InvoiceResult invoiceResult = new InvoiceResult();
                    ToolUtil.copyProperties(invoice, invoiceResult);
//                    record.setInvoiceResult(invoiceResult);
                    invoiceResults.add(invoiceResult);
                    record.setInvoiceResults(invoiceResults);
//                    break;
                }
            }
            record.setInvoiceResults(invoiceResults);



            for (Invoice invoice : invoices) {      //对比开票
                if (invoice.getInvoiceId().equals(record.getInvoiceId())) {
                    InvoiceResult invoiceResult = new InvoiceResult();
                    ToolUtil.copyProperties(invoice, invoiceResult);
                    record.setInvoiceResult(invoiceResult);
//                    invoiceResults.add(invoiceResult);
                    break;
                }
            }
//            record.setInvoiceResults(invoiceResults);
            for (Adress adress : adresses) {
                if (adress.getAdressId().equals(record.getDefaultAddress())) {
                    record.setAddress(adress);
                    break;
                }
            }

            for (Origin origin : originList) {
                if (origin.getOriginId().equals(record.getOriginId())) {
                    OriginResult originResult = new OriginResult();
                    ToolUtil.copyProperties(origin, originResult);
                    record.setOriginResult(originResult);
                    break;
                }
            }
            for (CrmCustomerLevel crmCustomerLevel : levelList) {
                if (crmCustomerLevel.getCustomerLevelId().equals(record.getCustomerLevelId())) {
                    CrmCustomerLevelResult crmCustomerLevelResult = new CrmCustomerLevelResult();
                    ToolUtil.copyProperties(crmCustomerLevel, crmCustomerLevelResult);
                    record.setCrmCustomerLevelResult(crmCustomerLevelResult);
                    break;
                }
            }
            for (UserResult user : userResults) {
                if (user.getUserId().equals(record.getUserId())) {
                    record.setUserResult(user);
                    break;
                }


            }
            for (UserResult user : userResults) {
                if (record.getCreateUser().equals(user.getUserId())){
                    record.setCreateUserResult(user);
                    break;
                }
            }
            for (CrmIndustry crmIndustry : industryList) {
                if (crmIndustry.getIndustryId().equals(record.getIndustryId())) {
                    CrmIndustryResult crmIndustryResult = new CrmIndustryResult();
                    ToolUtil.copyProperties(crmIndustry, crmIndustryResult);
                    record.setCrmIndustryResult(crmIndustryResult);
                    break;
                }
            }
            List<AdressResult> adressResults = new ArrayList<>();
            if (adressList != null) {
                for (Adress adress : adressList) {
                    if (record.getCustomerId().equals(adress.getCustomerId())) {
                        AdressResult adressResult = new AdressResult();
                        ToolUtil.copyProperties(adress, adressResult);

                        Double longitude = adressResult.getLongitude();
                        Double latitude = adressResult.getLatitude();
                        CustomerMap customerMap = new CustomerMap();
                        List<Double> list = new ArrayList<>();
                        list.add(longitude);
                        list.add(latitude);
                        customerMap.setAddress(adress.getLocation());
                        customerMap.setMap(list);
                        adressResult.setMap(customerMap);

                        adressResults.add(adressResult);
                    }
                }
                record.setAdressParams(adressResults);
            }

            List<ContactsResult> contactsResults = new ArrayList<>();

            for (ContactsBind contactsBind : contactsBinds) {
                if (record.getCustomerId().equals(contactsBind.getCustomerId())) {
                    for (Contacts contacts : contactsList) {
                        if (contactsBind.getContactsId() != null && contacts.getContactsId() != null && contactsBind.getContactsId().equals(contacts.getContactsId())) {
                            List<PhoneResult> phoneResults = new ArrayList<>();
                            ContactsResult contactsResult = new ContactsResult();
                            ToolUtil.copyProperties(contacts, contactsResult);
                            contactsResults.add(contactsResult);
                            List<Phone> phones = phoneService.lambdaQuery().eq(Phone::getContactsId, contactsResult.getContactsId()).list();
                            for (Phone phone : phones) {
                                PhoneResult phoneResult = new PhoneResult();
                                ToolUtil.copyProperties(phone, phoneResult);
                                phoneResults.add(phoneResult);
                            }

                            contactsResult.setPhoneParams(phoneResults);
                        }
                        record.setContactsParams(contactsResults);
                    }
                }
            }
            for (ContactsResult contacts : contactsResultList) {
                if (ToolUtil.isNotEmpty(record.getDefaultContacts()) && record.getDefaultContacts().equals(contacts.getContactsId())) {
                    record.setDefaultContactsResult(contacts);
                }
            }
        }


        return data.size() == 0 ? null : data.get(0);
    }


    private Serializable getKey(CustomerParam param) {
        return param.getCustomerId();
    }

    private Page<CustomerResult> getPageContext() {
        List<String> fields = new ArrayList<String>() {{
            add("createTime");
        }};
        return PageFactory.defaultPage(fields);
    }

    private Customer getOldEntity(CustomerParam param) {
        return this.getById(getKey(param));
    }

    private Customer getEntity(CustomerParam param) {
        Customer entity = new Customer();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public Long addCustomer(CustomerParam param) {
        Customer entity = getEntity(param);
        this.save(entity);
        return entity.getCustomerId();
    }

    @Override
    public void batchDelete(List<Long> customerId) {
        Customer customer = new Customer();
        customer.setDisplay(0);
        UpdateWrapper<Customer> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("customer_id", customerId);
        this.update(customer, updateWrapper);
    }

    @Override
    public void updateStatus(CustomerParam customerParam) {
    }

    @Override
    public CustomerResult detail(Long id) {
        Customer customer = this.getById(id);
        CustomerResult customerResult = new CustomerResult();
        ToolUtil.copyProperties(customer, customerResult);
        List<CustomerResult> results = new ArrayList<CustomerResult>() {{
            add(customerResult);
        }};
        this.format(results);

        Contacts contacts = contactsService.getById(customer.getDefaultContacts());
        Adress adress = adressService.getById(customer.getDefaultAddress());
        //返回品牌
        if (customerResult.getSupply().equals(1)) {
            List<SupplyResult> supply = supplyService.getSupplyBySupplierId(customerResult.getCustomerId());
            customerResult.setSupplyResults(supply);
        }

        customerResult.setAddress(adress);
        customerResult.setContact(contacts);
        return results.get(0);
    }

    @Override
    public List<CustomerResult> getResults(List<Long> ids) {
        if (ToolUtil.isEmpty(ids)) {
            return new ArrayList<>();
        }
        List<Customer> customerList = this.listByIds(ids);

        if (ToolUtil.isEmpty(customerList)) {
            return new ArrayList<>();
        }
        List<CustomerResult> results = new ArrayList<>();
        List<Long> levelIds = new ArrayList<>();
        List<Long> adressIds = new ArrayList<>();
        List<Long> contactsIds = new ArrayList<>();

        for (Customer customer : customerList) {
            CustomerResult customerResult = new CustomerResult();
            ToolUtil.copyProperties(customer, customerResult);
            results.add(customerResult);
            levelIds.add(customer.getCustomerLevelId());
            adressIds.add(customer.getDefaultAddress());
            contactsIds.add(customer.getDefaultContacts());
        }


        List<CrmCustomerLevel> levels = levelIds.size() == 0 ? new ArrayList<>() : levelService.listByIds(levelIds);  //映射等级
        List<Adress> adresses = adressIds.size() == 0 ? new ArrayList<>() : adressService.listByIds(adressIds); //映射地址
        List<Contacts> contacts = contactsIds.size() == 0 ? new ArrayList<>() : contactsService.listByIds(contactsIds); //映射联系人
        List<Phone> phones = contactsIds.size() == 0 ? new ArrayList<>() : phoneService.query().in("contacts_id", contactsIds).list();//映射电话

        Map<Long, Phone> phoneMap = new HashMap<>();
        for (Phone phone : phones) {
            phoneMap.put(phone.getContactsId(), phone);
        }
        for (CustomerResult result : results) {
            for (CrmCustomerLevel level : levels) {
                if (ToolUtil.isNotEmpty(result.getCustomerLevelId()) && result.getCustomerLevelId().equals(level.getCustomerLevelId())) {
                    result.setLevel(level);
                }
            }
            for (Adress adress : adresses) {
                if (ToolUtil.isNotEmpty(result.getDefaultAddress()) && result.getDefaultAddress().equals(adress.getAdressId())) {
                    result.setAddress(adress);
                }
            }
            for (Contacts contact : contacts) {
                if (ToolUtil.isNotEmpty(result.getDefaultContacts()) && result.getDefaultContacts().equals(contact.getContactsId())) {
                    result.setContact(contact);
                    result.setPhone(phoneMap.get(contact.getContactsId()));
                }
            }
        }


        return results;
    }


    /**
     * 按级别获取供应商
     *
     * @param levelId
     * @return
     */
    @Override
    public List<CustomerResult> getSuppliers(Long levelId) {
        CrmCustomerLevel level = crmCustomerLevelService.getById(levelId);
        Long rank = level.getRank();
        List<CrmCustomerLevel> levels = crmCustomerLevelService.list();

        List<Long> levelIds = new ArrayList<>();
        for (CrmCustomerLevel crmCustomerLevel : levels) {
            if (crmCustomerLevel.getRank() >= rank) {
                levelIds.add(crmCustomerLevel.getCustomerLevelId());
            }
        }

        List<Customer> customers = this.query().eq("supply", 1).in("customer_level_id", levelIds).list();

        List<CustomerResult> customerResults = new ArrayList<>();
        for (Customer customer : customers) {
            CustomerResult customerResult = new CustomerResult();
            ToolUtil.copyProperties(customer, customerResult);
            customerResults.add(customerResult);
        }
        return customerResults;
    }

    public void addContacts(Long customerId, List<ContactsParam> contactsParams) {
        List<ContactsBind> contactsBinds = new ArrayList<>();
        for (ContactsParam contactsParam : contactsParams) {
            Contacts contacts = contactsService.add(contactsParam);
            ContactsBind contactsBind = new ContactsBind();
            contactsBind.setContactsId(contacts.getContactsId());
            contactsBind.setCustomerId(customerId);
            contactsBinds.add(contactsBind);
        }
        contactsBindService.saveBatch(contactsBinds);

    }

    public void addAdress(Long customerId, List<AdressParam> adressParams) {
        for (AdressParam adressParam : adressParams) {
            adressParam.setCustomerId(customerId);
            adressService.add(adressParam);
        }
    }

}
