package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.entity.Phone;
import cn.atsoft.dasheng.app.model.params.PhoneParam;
import cn.atsoft.dasheng.app.model.result.ContractResult;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.model.result.PhoneResult;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.app.service.PhoneService;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Contacts;
import cn.atsoft.dasheng.app.mapper.ContactsMapper;
import cn.atsoft.dasheng.app.model.params.ContactsParam;
import cn.atsoft.dasheng.app.model.result.ContactsResult;
import cn.atsoft.dasheng.app.service.ContactsService;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.entity.CompanyRole;
import cn.atsoft.dasheng.crm.model.result.CompanyRoleResult;
import cn.atsoft.dasheng.crm.service.CompanyRoleService;
import cn.atsoft.dasheng.model.exception.ServiceException;
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
 * 联系人表 服务实现类
 * </p>
 *
 * @author
 * @since 2021-07-23
 */
@Service
public class ContactsServiceImpl extends ServiceImpl<ContactsMapper, Contacts> implements ContactsService {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CompanyRoleService companyRoleService;
    @Autowired
    private PhoneService phoneService;

    @Override
    @BussinessLog
    public Contacts add(ContactsParam param) {
        Contacts one = this.query().eq("contacts_name", param.getContactsName()).and(i -> i.eq("customer_id", param.getCustomerId())).one();
        if (ToolUtil.isNotEmpty(one)) {
            throw new ServiceException(500, "联系人已存在");
        }
        Customer customer = customerService.getById(param.getCustomerId());
        // 添加联系人
        if (ToolUtil.isEmpty(customer)) {
            throw new ServiceException(500, "数据不存在");
        } else {
            Contacts entity = getEntity(param);
            this.save(entity);
            // 添加电话号码
            List<PhoneParam> phoneList = param.getPhoneParams();
            if (ToolUtil.isNotEmpty(phoneList)) {
                for (PhoneParam phone : phoneList) {
                    if (ToolUtil.isNotEmpty(phone.getPhoneNumber())){
                        phone.setContactsId(entity.getContactsId());
                        phoneService.add(phone);

                    }
                }
            }

            return entity;
        }
    }

    @Override
    @BussinessLog
    public Contacts delete(ContactsParam param) {
        Contacts contacts = this.getById(param.getContactsId());
        if (ToolUtil.isEmpty(contacts)) {
            throw new ServiceException(500, "数据不存在");
        } else {
            Contacts entity = getEntity(param);
            param.setDisplay(0);
            this.update(param);
            return entity;
        }
    }

    @Override
    @BussinessLog
    public Contacts update(ContactsParam param) {
        Contacts oldEntity = getOldEntity(param);
        // 添加联系人
        if (ToolUtil.isEmpty(oldEntity)) {
            throw new ServiceException(500, "数据不存在");
        } else {
            Contacts newEntity = getEntity(param);
            newEntity.setCustomerId(null);
            ToolUtil.copyProperties(newEntity, oldEntity);


            // 验证旧数据是否被删除
            QueryWrapper<Phone> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.lambda().eq(Phone::getContactsId, param.getContactsId());
            List<Phone> list1 = phoneService.list(queryWrapper1);
            List<Long> phoneIdAll = new ArrayList<>();
            List<Long> phoneIds = new ArrayList<>();
            for (Phone phones : list1) {
                phoneIdAll.add(phones.getPhoneId());
            }
            // 添加电话号码
            List<PhoneParam> phoneList = param.getPhoneParams();
            for (PhoneParam phone : phoneList) {
                // 删除数据
                phoneIds.add(phone.getPhoneId());

                // 旧数据->改变手机号即可
                if (ToolUtil.isNotEmpty(phone.getContactsId())) {
                    Phone entity = new Phone();
                    ToolUtil.copyProperties(phone, entity);
                    phoneService.updateById(entity);
                } else {
                    //查询电话号码是否已存在
                    QueryWrapper<Phone> queryWrapper = new QueryWrapper<>();
                    queryWrapper.lambda().eq(Phone::getPhoneNumber, phone.getPhoneNumber()).eq(Phone::getContactsId, param.getContactsId());
                    List<Phone> list = phoneService.list(queryWrapper);
                    if (ToolUtil.isNotEmpty(list)) {
                        throw new ServiceException(500, "此电话号码已存在");
                    }
                    // 新加数据->则新增手机号
                    phone.setContactsId(param.getContactsId());
                    phoneService.add(phone);
                }

            }
            // 删除数据
            phoneIdAll.removeAll(phoneIds);
            for (Long phone : phoneIdAll) {
                phoneService.removeById(phone);
            }
            this.updateById(oldEntity);
            return oldEntity;
        }
    }

    @Override
    public ContactsResult findBySpec(ContactsParam param) {
        return null;
    }

    @Override

    public List<ContactsResult> findListBySpec(ContactsParam param) {
        return null;
    }

    @Override
    public void format(List<ContactsResult> data) {
        List<Long> cIds = new ArrayList<>();
        List<Long> roleIds = new ArrayList<>();
        List<Long> contactsIds = new ArrayList<>();
        for (ContactsResult record : data) {
            contactsIds.add(record.getContactsId());
            cIds.add(record.getCustomerId());
            roleIds.add(record.getCompanyRole());
        }
        List<CompanyRole> companyRoleList = roleIds.size() == 0 ? new ArrayList<>() : companyRoleService.lambdaQuery().in(CompanyRole::getCompanyRoleId, roleIds).list();

        QueryWrapper<Customer> customerQueryWrapper = new QueryWrapper<>();
        customerQueryWrapper.in("customer_id", cIds);
        List<Customer> customerList = cIds.size() == 0 ? new ArrayList<>() : customerService.list(customerQueryWrapper);

        QueryWrapper<Phone> phoneQueryWrapper = new QueryWrapper<>();
        phoneQueryWrapper.in("contacts_id", contactsIds);
        List<Phone> phoneList = cIds.size() == 0 ? new ArrayList<>() : phoneService.list(phoneQueryWrapper);


        for (ContactsResult record : data) {
            for (Customer customer : customerList) {
                if (record.getCustomerId() != null && record.getCustomerId().equals(customer.getCustomerId())) {
                    CustomerResult customerResult = new CustomerResult();
                    ToolUtil.copyProperties(customer, customerResult);
                    record.setCustomerResult(customerResult);
                    break;
                }
            }
            for (CompanyRole companyRole : companyRoleList) {
                if (companyRole.getCompanyRoleId().equals(record.getCompanyRole())) {
                    CompanyRoleResult companyRoleResult = new CompanyRoleResult();
                    ToolUtil.copyProperties(companyRole, companyRoleResult);
                    record.setCompanyRoleResult(companyRoleResult);
                    break;
                }
            }
            List<PhoneResult> List = new ArrayList<>();
            for (Phone phone : phoneList) {
                if (phone.getContactsId().equals(record.getContactsId())) {
                    PhoneResult phoneResult = new PhoneResult();
                    ToolUtil.copyProperties(phone, phoneResult);
                    List.add(phoneResult);
                }
            }
            record.setPhoneParams(List);
        }

    }

    @Override
    public PageInfo<ContactsResult> findPageBySpec(DataScope dataScope,ContactsParam param) {
        Page<ContactsResult> pageContext = getPageContext();
        IPage<ContactsResult> page = this.baseMapper.customPageList(dataScope,pageContext, param);
        format(page.getRecords());

        return PageFactory.createPageInfo(page);
    }

    @Override
    public void batchDelete(List<Long> id) {
        Contacts contacts = new Contacts();
        contacts.setDisplay(0);
        QueryWrapper<Contacts> contactsQueryWrapper = new QueryWrapper<>();
        contactsQueryWrapper.in("contacts_id", id);
        this.update(contacts, contactsQueryWrapper);
    }

    private Serializable getKey(ContactsParam param) {
        return param.getContactsId();
    }

    private Page<ContactsResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Contacts getOldEntity(ContactsParam param) {
        return this.getById(getKey(param));
    }

    private Contacts getEntity(ContactsParam param) {
        Contacts entity = new Contacts();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
