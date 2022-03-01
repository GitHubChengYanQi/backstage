package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.*;


import cn.atsoft.dasheng.app.model.request.ContractDetailSetRequest;
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
import cn.atsoft.dasheng.crm.model.params.OrderDetailParam;
import cn.atsoft.dasheng.crm.model.params.OrderParam;
import cn.atsoft.dasheng.crm.model.result.ContractClassResult;
import cn.atsoft.dasheng.crm.model.result.OrderDetailResult;
import cn.atsoft.dasheng.crm.service.CompanyRoleService;
import cn.atsoft.dasheng.crm.service.ContractClassService;
import cn.atsoft.dasheng.message.enmu.MicroServiceType;
import cn.atsoft.dasheng.message.enmu.OperationType;
import cn.atsoft.dasheng.message.entity.MicroServiceEntity;
import cn.atsoft.dasheng.message.producer.MessageProducer;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.purchase.model.params.SourceEnum;
import cn.atsoft.dasheng.purchase.model.request.ProcurementDetailSkuTotal;
import cn.atsoft.dasheng.purchase.pojo.ListingPlan;
import cn.atsoft.dasheng.purchase.pojo.ThemeAndOrigin;
import cn.atsoft.dasheng.purchase.service.GetOrigin;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    private GetOrigin getOrigin;

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
            if (ToolUtil.isNotEmpty(param.getSource()) && ToolUtil.isNotEmpty(param.getSourceId())){
                String origin = getOrigin.newThemeAndOrigin("contract", entity.getContractId(), entity.getSource(), entity.getSourceId());
                entity.setOrigin(origin);
                this.updateById(entity);
            }
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

    @Override
    public Set<ContractDetailSetRequest> pendingProductionPlan() {
//        List<Contract> contracts = this.query().eq("source", "销售").eq("display", 1).list();
//        List<Long> contractIds = new ArrayList<>();
//        for (Contract contract : contracts) {
//            contractIds.add(contract.getContractId());
//        }
//        List<ContractDetail> contractDetails = contractIds.size() == 0 ? new ArrayList<>() : contractDetailService.query().eq("display", 1).in("contract_id", contractIds).list();
//        List<ContractDetailResult> contractDetailResults = new ArrayList<>();
//
//        /**
//         * 返回合并后的数据
//         */
//        Set<ContractDetailSetRequest> contractDetailSet = new HashSet<>();
//        for (ContractDetail contractDetail : contractDetails) {
//            ContractDetailSetRequest request = new ContractDetailSetRequest();
//            ToolUtil.copyProperties(contractDetail, request);
//            contractDetailSet.add(request);
//        }
//        for (ContractDetail contractDetail : contractDetails) {
//            ContractDetailResult contractDetailResult = new ContractDetailResult();
//            ToolUtil.copyProperties(contractDetail, contractDetailResult);
//            contractDetailResults.add(contractDetailResult);
//        }
//        this.contractDetailService.format(contractDetailResults);
//        for (ContractDetailSetRequest request : contractDetailSet) {
//            Long quantity = 0L;
//            List<OrderDetailResult> results = new ArrayList<>();
//            for (OrderDetailResult contractDetailResult : contractDetailResults) {
//                if (
//                        ToolUtil.isNotEmpty(contractDetailResult.getBrandId()) && ToolUtil.isNotEmpty(contractDetailResult.getSkuId()) && ToolUtil.isNotEmpty(contractDetailResult.getCustomerId()) &&
//                                ToolUtil.isNotEmpty(request.getBrandId()) && ToolUtil.isNotEmpty(request.getSkuId()) && ToolUtil.isNotEmpty(request.getCustomerId()) &&
//                                contractDetailResult.getBrandId().equals(request.getBrandId()) && contractDetailResult.getSkuId().equals(request.getSkuId()) && contractDetailResult.getCustomerId().equals(request.getCustomerId())
//                ) {
//                    quantity += contractDetailResult.getQuantity();
//                    results.add(contractDetailResult);
//                    request.setSkuId(contractDetailResult.getSkuId());
//                    request.setBrandId(contractDetailResult.getBrandId());
//                    request.setCustomerId(contractDetailResult.getCustomerId());
//                }
//                request.setChildren(results);
//                request.setQuantity(quantity);
//            }
//        }

        return null;

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
    public void orderAddContract(Long orderId, ContractParam param, OrderParam orderParam, String orderType) {
        if (ToolUtil.isEmpty(param)) {
            throw new ServiceException(500, "合同对象为空");
        }
        Contract contract = new Contract();
        ToolUtil.copyProperties(param, contract);
        contract.setSource(orderType);
        contract.setSourceId(orderId);
        if (ToolUtil.isEmpty(param.getTemplateId())) {
            throw new ServiceException(500, "请选择合同模板");
        }
        Template template = templateService.getById(param.getTemplateId());
        if (ToolUtil.isNotEmpty(template)) {
            String content = template.getContent();
            for (ContractReplace contractReplace : param.getContractReplaces()) {    //替换
                if (content.contains(contractReplace.getOldText())) {
                    content = content.replace(contractReplace.getOldText(), contractReplace.getNewText());
                }
            }
            contract.setPartyA(orderParam.getBuyerId());
            contract.setPartyB(orderParam.getSellerId());

            contract.setPartyAPhone(orderParam.getPartyAPhone());
            contract.setPartyBPhone(orderParam.getPartyBPhone());

            contract.setPartyAAdressId(orderParam.getPartyAAdressId());
            contract.setPartyBAdressId(orderParam.getPartyBAdressId());


            contract.setPartyAContactsId(orderParam.getPartyAContactsId());
            contract.setPartyBContactsId(orderParam.getPartyBContactsId());
            try {
                if (content.contains("${{Acontacts}}") && ToolUtil.isNotEmpty(orderParam.getPartyAContactsId())) {
                    Contacts contacts = contactsService.getById(orderParam.getPartyAContactsId());
                    content = content.replace("${{Acontacts}}", contacts.getContactsName());
                }
                if (content.contains("${{Bcontacts}}") && ToolUtil.isNotEmpty(orderParam.getPartyBContactsId())) {
                    Contacts contacts = contactsService.getById(orderParam.getPartyAContactsId());
                    content = content.replace("${{Bcontacts}}", contacts.getContactsName());
                }
                if (content.contains("${{AAddress}}") && ToolUtil.isNotEmpty(orderParam.getPartyAAdressId())) {
                    Adress adress = adressService.getById(orderParam.getPartyAAdressId());
                    content = content.replace("${{AAddress}}", adress.getLocation());
                }
                if (content.contains("${{BAddress}}") && ToolUtil.isNotEmpty(orderParam.getPartyBAdressId())) {
                    Adress adress = adressService.getById(orderParam.getPartyBAdressId());
                    content = content.replace("${{BAddress}}", adress.getLocation());
                }
                if (content.contains("${{APhone}}") && ToolUtil.isNotEmpty(orderParam.getPartyAPhone())) {
                    Phone phone = phoneService.getById(orderParam.getPartyAPhone());
                    content = content.replace("${{APhone}}", phone.getPhoneNumber().toString());
                }
                if (content.contains("${{BPhone}}") && ToolUtil.isNotEmpty(orderParam.getPartyBPhone())) {
                    Phone phone = phoneService.getById(orderParam.getPartyBPhone());
                    content = content.replace("${{BPhone}}", phone.getPhoneNumber().toString());
                }

                if (content.contains("${{ACustomer}}") && ToolUtil.isNotEmpty(orderParam.getBuyerId())) {
                    Customer customer = customerService.getById(orderParam.getBuyerId());
                    content = content.replace("${{ACustomer}}", customer.getCustomerName());
                }
                if (content.contains("${{BCustomer}}") && ToolUtil.isNotEmpty(orderParam.getSellerId())) {
                    Customer customer = customerService.getById(orderParam.getSellerId());
                    content = content.replace("${{BCustomer}}", customer.getCustomerName());
                }
            } catch (Exception e) {

            }
            contract.setContent(content);
            this.save(contract);
            createContractDetail(contract.getContractId(), orderParam);
        }
    }

    /**
     * 添加合同详情
     *
     * @param contractId
     * @param
     */
    private void createContractDetail(Long contractId, OrderParam param) {

        List<ContractDetail> details = new ArrayList<>();
        for (OrderDetailParam detailParam : param.getDetailParams()) {
            ContractDetail contractDetail = new ContractDetail();
            contractDetail.setContractId(contractId);
            contractDetail.setContractId(param.getSellerId());
            contractDetail.setSkuId(detailParam.getSkuId());
            contractDetail.setBrandId(detailParam.getBrandId());
            contractDetail.setQuantity(Math.toIntExact(detailParam.getPurchaseNumber()));
            details.add(contractDetail);
        }
        contractDetailService.saveBatch(details);
    }

}
