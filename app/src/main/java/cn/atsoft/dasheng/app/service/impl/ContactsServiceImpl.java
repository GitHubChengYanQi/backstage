package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.entity.Phone;
import cn.atsoft.dasheng.app.model.params.PhoneParam;
import cn.atsoft.dasheng.app.model.result.ContractResult;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.model.result.PhoneResult;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.app.service.PhoneService;
import cn.atsoft.dasheng.base.log.FreedLog;
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
import cn.atsoft.dasheng.crm.entity.ContactsBind;
import cn.atsoft.dasheng.crm.model.params.ContactsBindParam;
import cn.atsoft.dasheng.crm.model.result.CompanyRoleResult;
import cn.atsoft.dasheng.crm.service.CompanyRoleService;
import cn.atsoft.dasheng.crm.service.ContactsBindService;
import cn.atsoft.dasheng.daoxin.entity.DaoxinDept;
import cn.atsoft.dasheng.daoxin.entity.DaoxinPosition;
import cn.atsoft.dasheng.daoxin.model.result.DaoxinDeptResult;
import cn.atsoft.dasheng.daoxin.service.DaoxinDeptService;
import cn.atsoft.dasheng.daoxin.service.DaoxinPositionService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.modular.system.entity.Position;
import cn.atsoft.dasheng.sys.modular.system.model.result.PositionResult;
import cn.atsoft.dasheng.sys.modular.system.service.PositionService;
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
    @Autowired
    private ContactsBindService contactsBindService;
    @Autowired
    private DaoxinDeptService daoxinDeptService;
    @Autowired
    private CompanyRoleService roleService;


    @Override
    @FreedLog
    public Contacts add(ContactsParam param) {

        Contacts entity = getEntity(param);
        this.save(entity);


        List<Long> phoneNumber = new ArrayList<>();
        for (PhoneParam phoneParam : param.getPhoneParams()) {
            if (ToolUtil.isNotEmpty(phoneParam.getPhoneNumber())) {
                phoneNumber.add(phoneParam.getPhoneNumber());
            }
        }

        int count = phoneNumber.size() == 0 ? 0 : phoneService.lambdaQuery().in(Phone::getPhoneNumber, phoneNumber).count();
        if (count > 0) {
            throw new ServiceException(500, "电话已经重复");
        }

        // 添加电话号码
        for (PhoneParam phone : param.getPhoneParams()) {
            if (ToolUtil.isNotEmpty(phone)) {
                if (phone.getPhoneNumber() != null) {
                    phone.setContactsId(entity.getContactsId());
                    phoneService.add(phone);
                } else {
                    throw new ServiceException(500, "请填写正确联系人电话");
                }
            }

        }

        if (ToolUtil.isNotEmpty(param.getDeptName())) {   //部门
            DaoxinDept daoxinDept = daoxinDeptService.query().eq("full_name", param.getDeptName()).one();
            if (ToolUtil.isNotEmpty(daoxinDept)) {
                entity.setDeptId(daoxinDept.getDeptId());
            } else {
                DaoxinDept newDept = new DaoxinDept();
                newDept.setFullName(param.getDeptName());
                daoxinDeptService.save(newDept);
                entity.setDeptId(newDept.getDeptId());
            }
        }

        if (ToolUtil.isNotEmpty(param.getPositionName())) {
            CompanyRole position = roleService.query().eq("position", param.getPositionName()).one();
            if (ToolUtil.isNotEmpty(position)) {
                entity.setCompanyRole(position.getCompanyRoleId());
            } else {
                CompanyRole newRole = new CompanyRole();
                newRole.setPosition(param.getPositionName());
                roleService.save(newRole);
                entity.setCompanyRole(newRole.getCompanyRoleId());
            }
        }

        if (ToolUtil.isNotEmpty(param.getCustomerId())) {
            Customer customer = customerService.getById(param.getCustomerId());
            if (ToolUtil.isEmpty(customer)) {
                throw new ServiceException(500, "绑定的客户不存在");
            }
            customer.setDefaultContacts(entity.getContactsId());
            customerService.updateById(customer);

            ContactsBindParam contactsBindParam = new ContactsBindParam();
            contactsBindParam.setCustomerId(param.getCustomerId());
            contactsBindParam.setContactsId(entity.getContactsId());
            contactsBindService.add(contactsBindParam);
        }

        this.updateById(entity);
        return entity;

    }

    @Override
    public Long insert(ContactsParam param) {
        Contacts contacts;
        DaoxinDept daoxinDept = null;
        CompanyRole companyRole = null;
        contacts = this.query().eq("contacts_id", param.getContactsName()).one();   //联系人
        if (ToolUtil.isNotEmpty(contacts)) {
            return contacts.getContactsId();
        }
        contacts = new Contacts();
        contacts.setContactsName(param.getContactsName());
        this.save(contacts);

        if (ToolUtil.isNotEmpty(param.getDeptName())) {                             //部门
            daoxinDept = daoxinDeptService.query().eq("full_name", param.getDeptName()).one();
            if (ToolUtil.isEmpty(daoxinDept)) {
                daoxinDept = new DaoxinDept();
                daoxinDept.setFullName(param.getDeptName());
                daoxinDeptService.save(daoxinDept);
            }
        }
        if (ToolUtil.isNotEmpty(param.getPositionName())) {                        //职位
            companyRole = roleService.query().eq("position", param.getPositionName()).one();
            if (ToolUtil.isEmpty(companyRole)) {
                List<CompanyRole> positions = roleService.query().eq("position", param.getPositionName()).list();
                if (ToolUtil.isNotEmpty(positions)) {
                    throw new ServiceException(500, "当前职位以存在");
                }
                companyRole = new CompanyRole();
                companyRole.setPosition(param.getPositionName());
                roleService.save(companyRole);
            }
        }
        // 添加电话号码
        for (PhoneParam phone : param.getPhoneParams()) {
            if (ToolUtil.isNotEmpty(phone)) {
                if (phone.getPhoneNumber() != null) {
                    phone.setContactsId(contacts.getContactsId());
                    phoneService.add(phone);
                }
            }
        }
        if (ToolUtil.isNotEmpty(daoxinDept)) {
            contacts.setDeptId(daoxinDept.getDeptId());
        }
        if (ToolUtil.isNotEmpty(companyRole)) {
            contacts.setCompanyRole(companyRole.getCompanyRoleId());
        }


        this.updateById(contacts);
        return contacts.getContactsId();
    }


    @Override
    @FreedLog
    public Contacts delete(ContactsParam param) {
        Contacts contacts = this.getById(param.getContactsId());
        if (ToolUtil.isEmpty(contacts)) {
            throw new ServiceException(500, "数据不存在");
        } else {

            Contacts entity = getEntity(param);
            param.setDisplay(0);
            QueryWrapper<Phone> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("contacts_id", param.getContactsId());
            phoneService.remove(queryWrapper);
            this.update(param);
            return entity;
        }
    }

    @Override
    @FreedLog
    public Contacts update(ContactsParam param) {

        Long customerId = param.getCustomerId();
        QueryWrapper<ContactsBind> contactsQueryWrapper = new QueryWrapper<>();
        contactsQueryWrapper.in("contacts_id", param.getContactsId());
        List<ContactsBind> contactsBinds = contactsBindService.list(contactsQueryWrapper);

        ContactsBindParam contactsBindParam = new ContactsBindParam();

        if (contactsBinds.size() > 0) {
            contactsBindParam.setContactsBindId(contactsBinds.get(0).getContactsBindId());
            contactsBindParam.setContactsId(contactsBinds.get(0).getContactsId());
            contactsBindParam.setCustomerId(customerId);
            contactsBindService.update(contactsBindParam);
        } else {
            contactsBindParam.setContactsId(param.getContactsId());
            contactsBindParam.setCustomerId(customerId);
            contactsBindService.add(contactsBindParam);
        }

        Contacts oldEntity = getOldEntity(param);
        // 添加联系人
        if (ToolUtil.isEmpty(oldEntity)) {
            throw new ServiceException(500, "数据不存在");
        } else {
            Contacts newEntity = getEntity(param);
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
    public PageInfo findPageBySpec(DataScope dataScope, ContactsParam param) {

        Page<ContactsResult> pageContext = getPageContext();
        List<Long> ids = new ArrayList<>();
        if (ToolUtil.isNotEmpty(param.getCustomerId())) {
            List<Long> contactsIds = this.baseMapper.queryContactsId(param.getCustomerId());
            if (ToolUtil.isNotEmpty(contactsIds)) {
                ids.addAll(contactsIds);
            }
        }

        IPage<ContactsResult> page = this.baseMapper.customPageList(dataScope, pageContext, param, ids);
        format(page.getRecords());

        if (ToolUtil.isNotEmpty(param.getCustomerId())) {
            contactsBindService.ContractsFormat(page.getRecords(), param.getCustomerId());
        }

        return PageFactory.createPageInfo(page);
    }

    @Override
    public void batchDelete(List<Long> id) {
        throw new ServiceException(500, "不可以删除联系人");

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

    @Override
    public void format(List<ContactsResult> data) {
        List<Long> customerIds = new ArrayList<>();
        List<Long> roleIds = new ArrayList<>();
        List<Long> contactsIds = new ArrayList<>();
        List<Long> deptIds = new ArrayList<>();

        for (ContactsResult record : data) {
            contactsIds.add(record.getContactsId());
            customerIds.add(record.getCustomerId());
            roleIds.add(record.getCompanyRole());
            deptIds.add(record.getDeptId());

        }
        //查询部门
        List<DaoxinDept> daoxinDepts = deptIds.size() == 0 ? new ArrayList<>() : daoxinDeptService.listByIds(deptIds);
        List<DaoxinDeptResult> deptResults = new ArrayList<>();
        for (DaoxinDept daoxinDept : daoxinDepts) {
            DaoxinDeptResult daoxinDeptResult = new DaoxinDeptResult();
            ToolUtil.copyProperties(daoxinDept, daoxinDeptResult);
            deptResults.add(daoxinDeptResult);
        }

        //查询职位
        List<CompanyRole> companyRoleList = roleIds.size() == 0 ? new ArrayList<>() : companyRoleService.lambdaQuery().in(CompanyRole::getCompanyRoleId, roleIds).list();
        List<Long> ids = new ArrayList<>();
        List<ContactsBind> contactsBinds = new ArrayList<>();
        if (ToolUtil.isNotEmpty(contactsIds)) {
            contactsBinds = contactsBindService.lambdaQuery()
                    .in(ContactsBind::getContactsId, contactsIds)
                    .eq(ContactsBind::getDisplay, 1)
                    .list();

            for (ContactsBind contactsBind : contactsBinds) {
                ids.add(contactsBind.getCustomerId());
            }
        }

        QueryWrapper<Customer> customerQueryWrapper = new QueryWrapper<>();
        customerQueryWrapper.in("customer_id", ids);
        List<Customer> customerList = ids.size() == 0 ? new ArrayList<>() : customerService.list(customerQueryWrapper);

        QueryWrapper<Phone> phoneQueryWrapper = new QueryWrapper<>();
        phoneQueryWrapper.in("contacts_id", contactsIds);
        List<Phone> phoneList = customerIds.size() == 0 ? new ArrayList<>() : phoneService.list(phoneQueryWrapper);


        for (ContactsResult record : data) {
            List<CustomerResult> customerResults = new ArrayList<>();
            for (ContactsBind contactsBind : contactsBinds) {
                if (record.getContactsId().equals(contactsBind.getContactsId())) {
                    for (Customer customer : customerList) {
                        if (ToolUtil.isNotEmpty(contactsBind.getCustomerId()) && contactsBind.getCustomerId().equals(customer.getCustomerId())) {
                            CustomerResult customerResult = new CustomerResult();
                            ToolUtil.copyProperties(customer, customerResult);
                            customerResults.add(customerResult);
                        }
                    }
                    record.setCustomerResults(customerResults);
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
                    String phoneNumber = ToolUtil.isEmpty(phoneResult.getPhoneNumber()) ? new String() : phoneResult.getPhoneNumber().toString().substring(0, 3) + "****" + phoneResult.getPhoneNumber().toString().substring(7);
                    phoneResult.setPhone(phoneNumber);
                    List.add(phoneResult);
                }
            }
            record.setPhoneParams(List);

            for (DaoxinDeptResult deptResult : deptResults) {
                if (ToolUtil.isNotEmpty(record.getDeptId()) && record.getDeptId().equals(deptResult.getDeptId())) {
                    record.setDeptResult(deptResult);
                }
            }

        }
    }


}
