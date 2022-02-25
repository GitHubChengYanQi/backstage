package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.*;


import cn.atsoft.dasheng.app.model.result.*;
import cn.atsoft.dasheng.app.pojo.ContractReplace;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.log.FreedLog;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.mapper.ContractMapper;
import cn.atsoft.dasheng.app.model.params.ContractParam;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.entity.ContractClass;
import cn.atsoft.dasheng.crm.model.result.ContractClassResult;
import cn.atsoft.dasheng.crm.service.CompanyRoleService;
import cn.atsoft.dasheng.crm.service.ContractClassService;
import cn.atsoft.dasheng.model.exception.ServiceException;
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
 * 合同表 服务实现类
 * </p>
 *
 * @author
 * @since 2021-07-21
 */
@Service
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements ContractService {
    @Autowired
    private AdressService adressService;
    @Autowired
    private PhoneService phoneService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private ContactsService contactsService;
    @Autowired
    private ContractClassService contractClassService;
    @Autowired
    private ContractDetailService contractDetailService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private TemplateService templateService;


    @Override
    public ContractResult detail(Long id) {
        Contract contract = this.getById(id);
        ContractResult contractResult = new ContractResult();
        ToolUtil.copyProperties(contract, contractResult);
        List<ContractResult> results = new ArrayList<ContractResult>() {{
            add(contractResult);
        }};
        format(results);
        return results.get(0);
    }

    @Override
    public ContractResult addResult(ContractParam param) {
        Customer customer = customerService.getById(param.getPartyA());
        if (ToolUtil.isEmpty(customer)) {
            throw new ServiceException(500, "数据不存在");
        } else {
            Contract entity = getEntity(param);
            this.save(entity);
            Contract contract = this.getById(entity.getContractId());
            ContractResult contractResult = new ContractResult();
            ToolUtil.copyProperties(contract, contractResult);
            List<ContractResult> results = new ArrayList<ContractResult>() {{
                add(contractResult);
            }};
            format(results);

            if (ToolUtil.isNotEmpty(param.getContractDetailList()) && param.getContractDetailList().size() > 0) {
                for (ContractDetail contractDetail : param.getContractDetailList()) {
                    contractDetail.setContractId(entity.getContractId());
                    contractDetail.setTotalPrice(contractDetail.getSalePrice() * contractDetail.getQuantity());
                }
                contractDetailService.saveBatch(param.getContractDetailList());
            }


            return results.get(0);
        }
    }


    @FreedLog
    public Contract add(ContractParam param) {
        Customer customer = customerService.getById(param.getPartyA());
        if (ToolUtil.isEmpty(customer)) {
            throw new ServiceException(500, "数据不存在");
        } else {
            Contract entity = getEntity(param);
            this.save(entity);
            return entity;
        }
    }

    @Override
    @FreedLog
    public Contract delete(ContractParam param) {
        Contract contract = this.getById(param.getContractId());
        if (ToolUtil.isEmpty(contract)) {
            throw new ServiceException(500, "数据不存在");
        } else {
            Contract contractById = getById(param.getContractId());
            if (contractById.getAudit().equals(0)) {
                Contract entity = getEntity(param);
                param.setDisplay(0);
                this.update(param);
                return entity;
            } else {
                throw new ServiceException(500, "当前合同不可删除");
            }

        }
    }


    @Override
    @FreedLog
    public Contract update(ContractParam param) {
        Contract oldEntity = getOldEntity(param);
        if (ToolUtil.isEmpty(oldEntity)) {
            throw new ServiceException(500, "数据不存在");
        } else {
            Contract contract = this.contractService.getById(param.getContractId());
            Contract newEntity = getEntity(param);
            if (contract.getAudit() == 1) {


            }
            ToolUtil.copyProperties(newEntity, oldEntity);
            this.updateById(oldEntity);
            return oldEntity;
        }

    }

    @Override
    public ContractResult findBySpec(ContractParam param) {
        return null;
    }

    @Override
    public List<ContractResult> findListBySpec(ContractParam param) {
        return null;
    }

    @Override
    public PageInfo<ContractResult> findPageBySpec(ContractParam param, DataScope dataScope) {
        Page<ContractResult> pageContext = getPageContext();
        IPage<ContractResult> page = this.baseMapper.customPageList(pageContext, param, dataScope);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private void format(List<ContractResult> data) {
        List<Long> partA = new ArrayList<>();
        List<Long> partB = new ArrayList<>();
        List<Long> contactsIdsA = new ArrayList<>();
        List<Long> contactsIdsB = new ArrayList<>();
        List<Long> adressIdsA = new ArrayList<>();
        List<Long> adressIdsB = new ArrayList<>();
        List<Long> phoneAIds = new ArrayList<>();
        List<Long> phoneBIds = new ArrayList<>();
        List<Long> classIds = new ArrayList<>();

        for (ContractResult record : data) {
            partA.add(record.getPartyA());
            partB.add(record.getPartyB());
            contactsIdsA.add(record.getPartyAContactsId());
            contactsIdsB.add(record.getPartyBContactsId());
            adressIdsA.add(record.getPartyAAdressId());
            adressIdsB.add(record.getPartyBAdressId());
            phoneAIds.add(record.getPartyAPhone());
            phoneBIds.add(record.getPartyBPhone());
            classIds.add(record.getContractClassId());
        }

        List<ContractClass> contractClassList = classIds.size() == 0 ? new ArrayList<>() : contractClassService.query().in("contract_class_id", classIds).list();

        QueryWrapper<Customer> customerQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Customer> partAWapper = customerQueryWrapper.in("customer_id", partA);
        List<Customer> partAList = partA.size() == 0 ? new ArrayList<>() : customerService.list(partAWapper);


        QueryWrapper<Customer> customerQueryWrapperB = new QueryWrapper<>();
        customerQueryWrapperB.in("customer_id", partB);
        List<Customer> partBList = partA.size() == 0 ? new ArrayList<>() : customerService.list(customerQueryWrapperB);

        QueryWrapper<Contacts> contactsA = new QueryWrapper<>();
        contactsA.in("contacts_id", contactsIdsA);
        List<Contacts> contactsAList = contactsIdsA.size() == 0 ? new ArrayList<>() : contactsService.list(contactsA);

        QueryWrapper<Contacts> contactsB = new QueryWrapper<>();
        contactsB.in("contacts_id", contactsIdsB);
        List<Contacts> contactsBList = contactsIdsB.size() == 0 ? new ArrayList<>() : contactsService.list(contactsB);

        QueryWrapper<Adress> adressA = new QueryWrapper<>();
        adressA.in("adress_id", adressIdsA);
        List<Adress> adressAList = adressIdsA.size() == 0 ? new ArrayList<>() : adressService.list(adressA);

        QueryWrapper<Adress> adressB = new QueryWrapper<>();
        adressB.in("adress_id", adressIdsB);
        List<Adress> adressBList = adressIdsB.size() == 0 ? new ArrayList<>() : adressService.list(adressB);

        QueryWrapper<Phone> phoneAwapper = new QueryWrapper<>();
        phoneAwapper.in("phone_id", phoneAIds);
        List<Phone> phoneAlist = phoneAIds.size() == 0 ? new ArrayList<>() : phoneService.list(phoneAwapper);

        QueryWrapper<Phone> phoneBwapper = new QueryWrapper<>();
        phoneBwapper.in("phone_id", phoneBIds);
        List<Phone> phoneBlist = phoneBIds.size() == 0 ? new ArrayList<>() : phoneService.list(phoneBwapper);


        for (ContractResult record : data) {

            for (ContractClass contractClass : contractClassList) {
                if (ToolUtil.isNotEmpty(record.getContractClassId()) && record.getContractClassId().equals(contractClass.getContractClassId())) {
                    ContractClassResult classResult = new ContractClassResult();
                    ToolUtil.copyProperties(contractClass, classResult);
                    record.setClassResult(classResult);
                }
            }


            for (Customer customer : partAList) {
                if (customer.getCustomerId().equals(record.getPartyA())) {
                    CustomerResult customerResult = new CustomerResult();
                    ToolUtil.copyProperties(customer, customerResult);
                    record.setPartA(customerResult);
                }

            }
            for (Customer customer : partBList) {
                if (customer.getCustomerId().equals(record.getPartyB())) {
                    CustomerResult customerResult = new CustomerResult();
                    ToolUtil.copyProperties(customer, customerResult);
                    record.setPartB(customerResult);
                }
            }
            for (Contacts contacts : contactsAList) {
                if (contacts.getContactsId().equals(record.getPartyAContactsId())) {
                    ContactsResult contactsResult = new ContactsResult();
                    ToolUtil.copyProperties(contacts, contactsResult);
                    record.setPartyAContacts(contactsResult);
                    break;
                }
            }
            for (Contacts contacts : contactsBList) {
                if (contacts.getContactsId().equals(record.getPartyBContactsId())) {
                    ContactsResult contactsResult = new ContactsResult();
                    ToolUtil.copyProperties(contacts, contactsResult);
                    record.setPartyBContacts(contactsResult);
                    break;
                }
            }
            for (Adress adress : adressAList) {
                if (adress.getAdressId().equals(record.getPartyAAdressId())) {
                    AdressResult adressResult = new AdressResult();
                    ToolUtil.copyProperties(adress, adressResult);
                    record.setPartyAAdress(adressResult);
                    break;
                }
            }
            for (Adress adress : adressBList) {
                if (adress.getAdressId().equals(record.getPartyBAdressId())) {
                    AdressResult adressResult = new AdressResult();
                    ToolUtil.copyProperties(adress, adressResult);
                    record.setPartyBAdress(adressResult);
                    break;
                }
            }
            for (Phone phone : phoneAlist) {
                if (phone.getPhoneId().equals(record.getPartyAPhone())) {
                    PhoneResult phoneResult = new PhoneResult();
                    ToolUtil.copyProperties(phone, phoneResult);
                    record.setPhoneA(phoneResult);
                    break;
                }
            }
            for (Phone phone : phoneBlist) {
                if (phone.getPhoneId().equals(record.getPartyBPhone())) {
                    PhoneResult phoneResult = new PhoneResult();
                    ToolUtil.copyProperties(phone, phoneResult);
                    record.setPhoneB(phoneResult);
                    break;
                }
            }
        }
    }

    private Serializable getKey(ContractParam param) {
        return param.getContractId();
    }

    //    private Page<ContractResult> getPageContext() {
//        return PageFactory.defaultPage();
//    }
    private Page<ContractResult> getPageContext() {
        List<String> fields = new ArrayList<String>() {{
            add("createTime");
        }};
        return PageFactory.defaultPage(fields);
    }

    private Contract getOldEntity(ContractParam param) {
        return this.getById(getKey(param));
    }

    private Contract getEntity(ContractParam param) {
        Contract entity = new Contract();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public void batchDelete(List<Long> contractId) {
        Contract contract = new Contract();
        contract.setDisplay(0);
        UpdateWrapper<Contract> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("contract_id", contractId);
        this.update(contract, updateWrapper);
    }

    /**
     * 订单创建合同
     *
     * @param orderId
     * @param param
     */
    @Override
    public void orderAddContract(Long orderId, ContractParam param) {
        Contract contract = new Contract();
        ToolUtil.copyProperties(param, contract);
        contract.setSource("订单");
        contract.setSourceId(orderId);

        Template template = templateService.getById(param.getTemplateId());
        if (ToolUtil.isNotEmpty(template)) {
            String content = template.getContent();
            for (ContractReplace contractReplace : param.getContractReplaces()) {    //替换
                if (content.contains(contractReplace.getOldText())) {
                    content = content.replace(contractReplace.getOldText(), contractReplace.getNewText());
                }
            }
            contract.setContent(content);
            this.save(contract);
        }
    }
}
