package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.entity.Phone;
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
        Customer customer = customerService.getById(param.getCustomerId());
        if (ToolUtil.isEmpty(customer)) {
            throw new ServiceException(500, "数据不存在");
        } else {
            Contacts entity = getEntity(param);
            this.save(entity);
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
        if (ToolUtil.isEmpty(oldEntity)) {
            throw new ServiceException(500, "数据不存在");
        } else {
            Contacts newEntity = getEntity(param);
            newEntity.setCustomerId(null);
            ToolUtil.copyProperties(newEntity, oldEntity);
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
    public PageInfo<ContactsResult> findPageBySpec(ContactsParam param) {
        Page<ContactsResult> pageContext = getPageContext();
        IPage<ContactsResult> page = this.baseMapper.customPageList(pageContext, param);
        List<Long> cIds = new ArrayList<>();
        List<Long> roleIds = new ArrayList<>();
        List<Long> contactsIds = new ArrayList<>();
        for (ContactsResult record : page.getRecords()) {
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


        for (ContactsResult record : page.getRecords()) {
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
