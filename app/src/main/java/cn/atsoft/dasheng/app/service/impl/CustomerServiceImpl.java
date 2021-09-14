package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.*;
import cn.atsoft.dasheng.app.model.result.*;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.mapper.CustomerMapper;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.region.GetRegionService;
import cn.atsoft.dasheng.crm.region.RegionResult;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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

    @Override
    @BussinessLog
    public Customer add(CustomerParam param) {

        QueryWrapper<Customer> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(Customer ::getCustomerName,param.getCustomerName());

        List<Customer> list =baseMapper.selectList(queryWrapper);
        if (ToolUtil.isEmpty(list)) {
            Customer entity = getEntity(param);
            this.save(entity);
            if (param.getContactsParams() != null) {
                for (ContactsParam contactsParam : param.getContactsParams()) {
                    if (ToolUtil.isNotEmpty(contactsParam.getContactsName())) {
                        contactsParam.setCustomerId(entity.getCustomerId());
                        Contacts contacts = contactsService.add(contactsParam);
                        if (contactsParam.getPhoneParams() != null) {
                            for (PhoneParam phoneParam : contactsParam.getPhoneParams()) {
                                if (ToolUtil.isNotEmpty(phoneParam.getPhoneNumber())) {
                                    phoneParam.setContactsId(contacts.getContactsId());
                                    phoneService.add(phoneParam);
                                }

                            }
                        }
                    }


                }
            }
            if (ToolUtil.isNotEmpty(param.getAdressParams())) {
                for (AdressParam adressParam : param.getAdressParams()) {
                    if (ToolUtil.isNotEmpty(adressParam)) {
                        if (ToolUtil.isNotEmpty(adressParam.getMap())) {
                            adressParam.setCustomerId(entity.getCustomerId());
                            adressService.add(adressParam);
                        }

                    }
                }
            }

            return entity;

        }else {
            throw new ServiceException(500,"已有当前客户");
        }
    }

    @Override
    @BussinessLog
    public Customer delete(CustomerParam param) {
        param.setDisplay(0);
        Long customerId = param.getCustomerId();
        Customer oldEntity = getOldEntity(param);
        Customer newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);

        QueryWrapper<Contacts> contactsQueryWrapper = new QueryWrapper<>();
        contactsQueryWrapper.in("customer_id", customerId);
        List<Contacts> list = contactsService.list(contactsQueryWrapper);
        List<Long> contactsId = new ArrayList<>();
        for (Contacts contacts : list) {
            contactsId.add(contacts.getContactsId());
            QueryWrapper<Phone> phoneQueryWrapper = new QueryWrapper<>();
            phoneQueryWrapper.in("contacts_id", contacts.getContactsId());
            List<Phone> phoneList = phoneService.list(phoneQueryWrapper);
            List<Long> phoneId = new ArrayList<>();
            for (Phone phone : phoneList) {
                phoneId.add(phone.getPhoneId());
            }
            phoneService.removeByIds(phoneId);
        }
        contactsService.removeByIds(contactsId);

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
    @BussinessLog
    public Customer update(CustomerParam param) {
        Customer oldEntity = getOldEntity(param);
        if (ToolUtil.isEmpty(oldEntity)) {
            throw new ServiceException(500, "数据不存在");
        } else {
            Customer newEntity = getEntity(param);
            ToolUtil.copyProperties(newEntity, oldEntity);
            this.updateById(oldEntity);
//            contactsService.lambdaQuery().in(Contacts::getContactsId, param.getContactsParams().get(0));

            Long customerId = param.getCustomerId();


            QueryWrapper<Contacts> contactsQueryWrapper = new QueryWrapper<>();
            contactsQueryWrapper.in("customer_id", customerId);
            List<Contacts> list = contactsService.list(contactsQueryWrapper);
            List<Long> contactsId = new ArrayList<>();
            for (Contacts contacts : list) {
                contactsId.add(contacts.getContactsId());
                QueryWrapper<Phone> phoneQueryWrapper = new QueryWrapper<>();
                phoneQueryWrapper.in("contacts_id", contacts.getContactsId());
                List<Phone> phoneList = phoneService.list(phoneQueryWrapper);
                List<Long> phoneId = new ArrayList<>();
                for (Phone phone : phoneList) {
                    phoneId.add(phone.getPhoneId());
                }
                phoneService.removeByIds(phoneId);
            }
            contactsService.removeByIds(contactsId);

            QueryWrapper<Adress> adressQueryWrapper = new QueryWrapper<>();
            adressQueryWrapper.in("customer_id", customerId);
            List<Adress> list1 = adressService.list(adressQueryWrapper);
            List<Long> adressId = new ArrayList<>();
            for (Adress adress : list1) {
                adressId.add(adress.getAdressId());
            }
            adressService.removeByIds(adressId);

            if (param.getContactsParams() != null) {
                for (ContactsParam contactsParam : param.getContactsParams()) {
                    if (ToolUtil.isNotEmpty(contactsParam.getContactsName())) {
                        contactsParam.setCustomerId(customerId);
                        Contacts contacts = contactsService.add(contactsParam);

                        if (ToolUtil.isEmpty(contactsParam.getPhoneParams())) {
                            for (PhoneParam phoneParam : contactsParam.getPhoneParams()) {
                                if (ToolUtil.isNotEmpty(phoneParam.getPhoneNumber())) {
                                    phoneParam.setContactsId(contacts.getContactsId());
                                    phoneService.add(phoneParam);
                                }

                            }


                        }
                    }


                }
            }
            if (ToolUtil.isNotEmpty(param.getAdressParams())) {
                for (AdressParam adressParam : param.getAdressParams()) {
                    if (ToolUtil.isNotEmpty(adressParam)) {
                        if (ToolUtil.isNotEmpty(adressParam.getMap())) {
                            adressParam.setCustomerId(param.getCustomerId());
                            adressService.add(adressParam);
                        }

                    }
                }
            }


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
    public PageInfo<CustomerResult> findPageBySpec(CustomerParam param) {
        Page<CustomerResult> pageContext = getPageContext();
        IPage<CustomerResult> page = this.baseMapper.customPageList(pageContext, param);

        format(page.getRecords());

        return PageFactory.createPageInfo(page);
    }

    public CustomerResult format(List<CustomerResult> data) {

        List<Long> dycustomerIds = new ArrayList<>();
        List<Long> originIds = new ArrayList<>();
        List<Long> levelIds = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        List<Long> industryIds = new ArrayList<>();
        List<Long> contactsIds = new ArrayList<>();
        Long customerId = null;


        for (CustomerResult record : data) {
            originIds.add(record.getOriginId());
            levelIds.add(record.getCustomerLevelId());
            userIds.add(record.getUserId());
            industryIds.add(record.getIndustryId());
            dycustomerIds.add(record.getCustomerId());
            contactsIds.add(record.getCustomerId());
            customerId = record.getCustomerId();

        }
        /**
         * 获取联系人
         */
        QueryWrapper<Contacts> contactsQueryWrapper = new QueryWrapper<>();
        contactsQueryWrapper.in("customer_id", contactsIds);
        List<Contacts> contactsList = contactsIds.size() == 0 ? new ArrayList<>() : contactsService.list(contactsQueryWrapper);

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
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.in("user_id", userIds);
        List<User> userList = userIds.size() == 0 ? new ArrayList<>() : userService.list(userQueryWrapper);

        QueryWrapper<CrmIndustry> industryQueryWrapper = new QueryWrapper<>();
        industryQueryWrapper.in("industry_id", industryIds);
        List<CrmIndustry> industryList = industryIds.size() == 0 ? new ArrayList<>() : crmIndustryService.list(industryQueryWrapper);
        List<Adress> adressList = null;

        if (customerId != null) {
            adressList = adressService.lambdaQuery().eq(Adress::getCustomerId, customerId).list();
        }


        for (CustomerResult record : data) {
//            RegionResult region = getRegionService.getRegion(record.getRegion());
//            record.setRegionResult(region);


            if (record.getClassification() == 1) {
                record.setClassificationName("终端用户");
            } else if (record.getClassification() == 0) {
                record.setClassificationName("代理商");
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
            for (User user : userList) {
                if (user.getUserId().equals(record.getUserId())) {
                    UserResult userResult = new UserResult();
                    ToolUtil.copyProperties(user, userResult);
                    record.setUserResult(userResult);
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

            for (Contacts contacts : contactsList) {
                if (record.getCustomerId().equals(contacts.getCustomerId())) {
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
            }
            record.setContactsParams(contactsResults);
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
        return results.get(0);
    }


}
