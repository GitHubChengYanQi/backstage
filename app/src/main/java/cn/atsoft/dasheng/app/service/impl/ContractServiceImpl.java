package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.*;


import cn.atsoft.dasheng.app.model.request.ContractDetailSetRequest;
import cn.atsoft.dasheng.app.model.result.*;
import cn.atsoft.dasheng.app.pojo.ContractReplace;
import cn.atsoft.dasheng.app.pojo.CycleReplace;
import cn.atsoft.dasheng.app.pojo.PayReplace;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.log.FreedLog;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.mapper.ContractMapper;
import cn.atsoft.dasheng.app.model.params.ContractParam;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.entity.Bank;
import cn.atsoft.dasheng.crm.entity.ContractClass;
import cn.atsoft.dasheng.crm.entity.Invoice;
import cn.atsoft.dasheng.crm.model.params.OrderDetailParam;
import cn.atsoft.dasheng.crm.model.params.OrderParam;
import cn.atsoft.dasheng.crm.model.params.PaymentDetailParam;
import cn.atsoft.dasheng.crm.model.result.ContractClassResult;
import cn.atsoft.dasheng.crm.model.result.OrderDetailResult;
import cn.atsoft.dasheng.crm.service.BankService;
import cn.atsoft.dasheng.crm.service.CompanyRoleService;
import cn.atsoft.dasheng.crm.service.ContractClassService;
import cn.atsoft.dasheng.crm.service.InvoiceService;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.SkuService;
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
import cn.atsoft.dasheng.taxRate.entity.TaxRate;
import cn.atsoft.dasheng.taxRate.service.TaxRateService;
import cn.atsoft.dasheng.template.entity.PaymentTemplate;
import cn.atsoft.dasheng.template.service.PaymentTemplateService;
import cn.hutool.core.convert.NumberChineseFormatter;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.unit.DataUnit;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    private GetOrigin getOrigin;
    @Autowired
    private SkuService skuService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private BankService bankService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private TaxRateService rateService;
    @Autowired
    private PaymentTemplateService paymentTemplateService;

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
            if (ToolUtil.isNotEmpty(param.getSource()) && ToolUtil.isNotEmpty(param.getSourceId())) {
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
            if (ToolUtil.isNotEmpty(param.getContractReplaces())) {
                for (ContractReplace contractReplace : param.getContractReplaces()) {    //替换
                    if (content.contains(contractReplace.getOldText())) {
                        content = content.replace(contractReplace.getOldText(), contractReplace.getNewText());
                    }
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
            content = replace(content, orderParam);
            String materialList = materialList(content, orderParam, param.getCycleReplaces());  //替换sku
            String replace = replace(materialList, orderParam); //全局替换
            String payList = payList(replace, orderParam, param.getPayReplaces());  //替换付款方式
            contract.setContent(payList);
            this.save(contract);
            createContractDetail(contract.getContractId(), orderParam);
        }
    }

    /**
     * 循环替换
     *
     * @param content
     * @param orderParam
     * @param cycleReplaces
     * @return
     */
    private String materialList(String content, OrderParam orderParam, List<CycleReplace> cycleReplaces) {
        String regStr = "\\<tr(.*?)\\>([\\w\\W]+?)<\\/tr>";
        Pattern pattern = Pattern.compile(regStr);
        Matcher m = pattern.matcher(content);
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer append = stringBuffer.append(content);
        while (m.find()) {
            String skuGroup = m.group(0);
            if (skuGroup.contains("sku")) {
                StringBuilder all = new StringBuilder();
                List<OrderDetailParam> detailParams = orderParam.getDetailParams();
                for (int i = 0; i < detailParams.size(); i++) {
                    StringBuilder group = new StringBuilder(m.group(0));
                    OrderDetailParam detailParam = detailParams.get(i);
                    //-------------------------------------------------------------
                    List<SkuResult> skuResults = skuService.formatSkuResult(new ArrayList<Long>() {{
                        add(detailParam.getSkuId());
                    }});
                    //------------------------------------------------------------
                    SkuResult skuResult = skuResults.get(0);
                    if (group.toString().contains("${{coding}}") && ToolUtil.isNotEmpty(skuResult)) {
                        group = new StringBuilder(group.toString().replace("${{coding}}", skuResult.getStandard()));
                    }
                    if (group.toString().contains("${{spuName}}") && ToolUtil.isNotEmpty(skuResult)) {
                        group = new StringBuilder(group.toString().replace("${{spuName}}", skuResult.getSpuResult().getName()));
                    }
                    if (group.toString().contains("${{skuName}}") && ToolUtil.isNotEmpty(skuResult)) {
                        group = new StringBuilder(group.toString().replace("${{skuName}}", skuResult.getSkuName() + "/" + skuResult.getSpecifications()));
                    }
                    if (group.toString().contains("${{skuClass}}") && ToolUtil.isNotEmpty(skuResult)) {
                        group = new StringBuilder(group.toString().replace("${{skuClass}}", skuResult.getSpuResult().getSpuClassificationResult().getName()));
                    }
                    if (group.toString().contains("${{brand}}") && ToolUtil.isNotEmpty(detailParam.getBrandId())) {
                        Brand brand = brandService.getById(detailParam.getBrandId());
                        group = new StringBuilder(group.toString().replace("${{brand}}", brand.getBrandName()));
                    }
                    if (group.toString().contains("${{skuNumber}}") && ToolUtil.isNotEmpty(detailParam.getPurchaseNumber())) {
                        group = new StringBuilder(group.toString().replace("${{skuNumber}}", detailParam.getPurchaseNumber().toString()));
                    }
                    if (group.toString().contains("${{price}}") && ToolUtil.isNotEmpty(detailParam.getOnePrice())) {
                        group = new StringBuilder(group.toString().replace("${{price}}", detailParam.getOnePrice().toString()));
                    }
                    if (group.toString().contains("${{unit}}") && ToolUtil.isNotEmpty(detailParam.getUnitId())) {        //单位
                        Unit unit = unitService.getById(detailParam.getUnitId());
                        group = new StringBuilder(group.toString().replace("${{unit}}", unit.getUnitName()));
                    }
                    if (group.toString().contains("${{totalPrice}}") && ToolUtil.isNotEmpty(detailParam.getTotalPrice())) {  //总价
                        group = new StringBuilder(group.toString().replace("${{totalPrice}}", detailParam.getTotalPrice().toString()));
                    }
                    if (group.toString().contains("${{preOrder}}") && ToolUtil.isNotEmpty(detailParam.getPreordeNumber())) {  //预购数量
                        group = new StringBuilder(group.toString().replace("${{preOrder}}", detailParam.getPreordeNumber().toString()));
                    }
                    if (group.toString().contains("${{rate}}") && ToolUtil.isNotEmpty(detailParam.getRate())) {  //税率
                        TaxRate taxRate = rateService.getById(detailParam.getRate());
                        group = new StringBuilder(group.toString().replace("${{rate}}", taxRate.getTaxRateValue().toString()));
                    }
                    if (group.toString().contains("${{deliveryDate}}") && ToolUtil.isNotEmpty(detailParam.getDeliveryDate())) {  //交货期
                        group = new StringBuilder(group.toString().replace("${{deliveryDate}}", detailParam.getDeliveryDate().toString()));
                    }
                    if (group.toString().contains("${{paperType}}")) {  //票据类型
                        String paperType;
                        if (detailParam.getPaperType() == 0) {
                            paperType = "普票";
                        } else {
                            paperType = "专票";
                        }
                        group = new StringBuilder(group.toString().replace("${{paperType}}", paperType));
                    }

                    if (group.toString().contains("${{deliveryDate}}")) {
                        if (ToolUtil.isEmpty(detailParam.getDeliveryDate())) {
                            group = new StringBuilder(group.toString().replace("${{deliveryDate}}", ""));
                        } else {
                            group = new StringBuilder(group.toString().replace("${{deliveryDate}}", detailParam.getDeliveryDate().toString()));
                        }
                    }
                    if (ToolUtil.isNotEmpty(cycleReplaces)) {
                        CycleReplace cycleReplace = cycleReplaces.get(i);
                        for (CycleReplace.Cycle cycle : cycleReplace.getCycles()) {
                            group = new StringBuilder(group.toString().replace(cycle.getOldText(), cycle.getNewText()));
                        }
                    }
                    all.append(group);
                }
                String toString = all.toString();
                String group = m.group(0);
                String string = append.toString();
                return string.replace(group, toString);
            }
        }

        return content;
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
            if (ToolUtil.isNotEmpty(detailParam.getPurchaseNumber())) {
                contractDetail.setQuantity(Math.toIntExact(detailParam.getPurchaseNumber()));
            }
            details.add(contractDetail);
        }
        contractDetailService.saveBatch(details);
    }

    /**
     * 替换
     *
     * @param content
     * @param orderParam
     * @return
     */
    private String replace(String content, OrderParam orderParam) {
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

        if (content.contains("${{ABank}}") && ToolUtil.isNotEmpty(orderParam.getPartyABankId())) {
            Bank bank = bankService.getById(orderParam.getPartyABankId());
            content = content.replace("${{ABank}}", bank.getBankName());
        }
        if (content.contains("${{BBank}}") && ToolUtil.isNotEmpty(orderParam.getPartyBBankId())) {
            Bank bank = bankService.getById(orderParam.getPartyBBankId());
            content = content.replace("${{BBank}}", bank.getBankName());
        }
        if (content.contains("${{AAccount}}") && ToolUtil.isNotEmpty(orderParam.getPartyABankAccount())) {
            Invoice invoice = invoiceService.getById(orderParam.getPartyABankAccount());
            content = content.replace("${{AAccount}}", invoice.getBankAccount());
        }
        if (content.contains("${{BAccount}}") && ToolUtil.isNotEmpty(orderParam.getPartyBBankAccount())) {
            Invoice invoice = invoiceService.getById(orderParam.getPartyBBankAccount());
            content = content.replace("${{BAccount}}", invoice.getBankAccount());
        }
        if (content.contains("${{amount}}")) {  //总计
            content = content.replace("${{amount}}", orderParam.getPaymentParam().getMoney().toString());
        }
        if (content.contains("${{amountStr}}")) {  //总计
            Integer money = orderParam.getPaymentParam().getMoney();
            String format = NumberChineseFormatter.format(money, true, true);
            content = content.replace("${{amountStr}}", format);
        }
        if (content.contains("${{askCoding}}") && ToolUtil.isNotEmpty(orderParam.getCoding())) {  //采购编号
            content = content.replace("${{askCoding}}", orderParam.getCoding());
        }
        if (content.contains("${{askDate}}") && ToolUtil.isNotEmpty(orderParam.getDate())) {  //采购日期
            DateTime date = DateUtil.date(orderParam.getDate());
            content = content.replace("${{askDate}}", date.toString());
        }
        if (content.contains("${{askRemake}}") && ToolUtil.isNotEmpty(orderParam.getRemark())) {  //采购日期
            content = content.replace("${{askRemake}}", orderParam.getRemark());
        }
        if (content.contains("${{Alegal}}") && ToolUtil.isNotEmpty(orderParam.getPartyALegalPerson())) {  //A法定代表人
            content = content.replace("${{Alegal}}", orderParam.getPartyALegalPerson());
        }
        if (content.contains("${{Blegal}}") && ToolUtil.isNotEmpty(orderParam.getPartyBLegalPerson())) {  //B法定代表人
            content = content.replace("${{Blegal}}", orderParam.getPartyBLegalPerson());
        }
        if (content.contains("${{ABankNo}}") && ToolUtil.isNotEmpty(orderParam.getPartyABankNo())) {  //A开户行号
            content = content.replace("${{ABankNo}}", orderParam.getPartyABankNo().toString());
        }
        if (content.contains("${{BBankNo}}") && ToolUtil.isNotEmpty(orderParam.getPartyBBankNo())) {  //B开户行号
            content = content.replace("${{BBankNo}}", orderParam.getPartyBBankNo().toString());
        }
        if (content.contains("${{AFax}}") && ToolUtil.isNotEmpty(orderParam.getPartyAFax())) {  //A传真
            content = content.replace("${{AFax}}", orderParam.getPartyAFax());
        }
        if (content.contains("${{BFax}}") && ToolUtil.isNotEmpty(orderParam.getPartyBFax())) {  //B传真
            content = content.replace("${{BFax}}", orderParam.getPartyBFax());
        }
        if (content.contains("${{AZipCOde}}") && ToolUtil.isNotEmpty(orderParam.getPartyAZipcode())) {  //A邮编
            content = content.replace("${{AZipCOde}}", orderParam.getPartyAZipcode());
        }
        if (content.contains("${{BZipCOde}}") && ToolUtil.isNotEmpty(orderParam.getPartyBZipcode())) {  //B邮编
            content = content.replace("${{BZipCOde}}", orderParam.getPartyBZipcode());
        }
        if (content.contains("${{ACompanyPhone}}") && ToolUtil.isNotEmpty(orderParam.getPartyACompanyPhone())) {  //A公司电话
            content = content.replace("${{ACompanyPhone}}", orderParam.getPartyACompanyPhone());
        }
        if (content.contains("${{BCompanyPhone}}") && ToolUtil.isNotEmpty(orderParam.getPartyBCompanyPhone())) {  //B公司电话
            content = content.replace("${{BCompanyPhone}}", orderParam.getPartyBCompanyPhone());
        }
        if (content.contains("${{freight}}") && ToolUtil.isNotEmpty(orderParam.getPaymentParam())) {  //是否含运费
            if (ToolUtil.isNotEmpty(orderParam.getPaymentParam().getFreight())) {
                if (orderParam.getPaymentParam().getFreight() == 1) {
                    content = content.replace("${{freight}}", "含运");
                } else {
                    content = content.replace("${{freight}}", "不含运");
                }
            }
        }
        if (content.contains("${{payMethod}}") && ToolUtil.isNotEmpty(orderParam.getPaymentParam())) {  //结算方式
            if (ToolUtil.isNotEmpty(orderParam.getPaymentParam().getPayMethod())) {
                content = content.replace("${{payMethod}}", orderParam.getPaymentParam().getPayMethod());
            }
        }
        if (content.contains("${{deliveryWay}}") && ToolUtil.isNotEmpty(orderParam.getPaymentParam())) {  //交货方式
            if (ToolUtil.isNotEmpty(orderParam.getPaymentParam().getDeliveryWay())) {
                content = content.replace("${{deliveryWay}}", orderParam.getPaymentParam().getDeliveryWay());
            }
        }
        if (content.contains("${{deliveryAddress}}") && ToolUtil.isNotEmpty(orderParam.getPaymentParam())) {  //交货地址
            if (ToolUtil.isNotEmpty(orderParam.getPaymentParam().getAdressId())) {
                Adress adress = adressService.getById(orderParam.getPaymentParam().getAdressId());
                content = content.replace("${{deliveryAddress}}", adress.getLocation());
            }
        }
        if (content.contains("${{payPlan}}") && ToolUtil.isNotEmpty(orderParam.getPaymentParam())) {  //付款计划
            if (ToolUtil.isNotEmpty(orderParam.getPaymentParam().getPayPlan())) {
                PaymentTemplate paymentTemplate = paymentTemplateService.getById(orderParam.getPaymentParam().getPayPlan());
                content = content.replace("${{payPlan}}", paymentTemplate.getName());
            }
        }
        if (content.contains("${{PaymentRemark}}") && ToolUtil.isNotEmpty(orderParam.getPaymentParam())) {  //财务备注
            if (ToolUtil.isNotEmpty(orderParam.getPaymentParam().getRemark())) {
                content = content.replace("${{PaymentRemark}}", orderParam.getPaymentParam().getRemark());
            }
        }
        return content;
    }

    /**
     * 付款详情替换
     *
     * @param content
     * @param orderParam
     * @return
     */
    private String payList(String content, OrderParam orderParam, List<PayReplace> payReplaces) {
        String regStr = "\\<tr(.*?)\\>([\\w\\W]+?)<\\/tr>";
        Pattern pattern = Pattern.compile(regStr);
        Matcher m = pattern.matcher(content);
        while (m.find()) {
            String Group = m.group(0);
            if (Group.contains("pay")) {
                StringBuffer stringBuffer = new StringBuffer();
                int i = 0;
                for (PaymentDetailParam detailParam : orderParam.getPaymentParam().getDetailParams()) {
                    String payGroup = m.group(0);
                    if (payGroup.contains("${{detailMoney}}") && ToolUtil.isNotEmpty(detailParam.getMoney())) {      //金额
                        payGroup = payGroup.replace("${{detailMoney}}", detailParam.getMoney().toString());
                    }
                    if (payGroup.contains("${{detailPayType}}") && ToolUtil.isNotEmpty(detailParam.getPayType())) {    //财务详情方式
                        String payType = "";
                        switch (detailParam.getPayType()) {
                            case 0:
                                payType = "订单创建后";
                                break;
                            case 1:
                                payType = "合同签订后";
                                break;
                            case 2:
                                payType = "订单发货前";
                                break;
                            case 3:
                                payType = "订单发货后";
                                break;
                            case 4:
                                payType = "入库后";
                                break;
                        }
                        payGroup = payGroup.replace("${{detailPayType}}", payType);
                    }
                    if (payGroup.contains("${{detailDateWay}}") && ToolUtil.isNotEmpty(detailParam.getDateWay())) {     //日期方式
                        String dateWay = "";
                        switch (detailParam.getDateWay()) {
                            case 0:
                                dateWay = "天";
                                break;
                            case 1:
                                dateWay = "月";
                                break;
                            case 2:
                                dateWay = "年";
                                break;
                        }
                        payGroup = payGroup.replace("${{detailDateWay}}", dateWay);
                    }
                    if (payGroup.contains("${{percentum}}") && ToolUtil.isNotEmpty(detailParam.getPercentum())) {    //比例
                        payGroup = payGroup.replace("${{percentum}}", detailParam.getPercentum().toString());
                    }
                    if (payGroup.contains("${{DetailPayRemark}}") && ToolUtil.isNotEmpty(detailParam.getRemark())) {    //款项说明
                        payGroup = payGroup.replace("${{DetailPayRemark}}", detailParam.getRemark());
                    }
                    if (payGroup.contains("${{DetailPayDate}}")) {    //付款时间
                        if (ToolUtil.isNotEmpty(detailParam.getPayTime())) {
                            DateTime date = DateUtil.date(detailParam.getPayTime());
                            payGroup = payGroup.replace("${{DetailPayDate}}", date.toString());
                        } else {
                            payGroup = payGroup.replace("${{DetailPayDate}}", "");
                        }

                    }
                    if (ToolUtil.isNotEmpty(payReplaces)) {

                        PayReplace payReplace = payReplaces.get(i);
                        for (PayReplace.PayCycle cycle : payReplace.cycles) {
                            payGroup = payGroup.replace(cycle.getOldText(), cycle.getNewText());
                        }

                    }
                    stringBuffer.append(payGroup);
                    i++;
                }
                String string = stringBuffer.toString();
                content = content.replace(Group, string);
                return content;
            }
        }
        return content;
    }
}
