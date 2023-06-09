package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.Excel.ContractExcel;
import cn.atsoft.dasheng.Excel.WordUtils;
import cn.atsoft.dasheng.Excel.pojo.ContractLabel;
import cn.atsoft.dasheng.Excel.pojo.LabelResult;
import cn.atsoft.dasheng.app.entity.*;


import cn.atsoft.dasheng.app.model.request.ContractDetailSetRequest;
import cn.atsoft.dasheng.app.model.result.*;
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
import cn.atsoft.dasheng.crm.entity.*;
import cn.atsoft.dasheng.crm.model.params.OrderDetailParam;
import cn.atsoft.dasheng.crm.model.params.OrderParam;
import cn.atsoft.dasheng.crm.model.params.PaymentDetailParam;
import cn.atsoft.dasheng.crm.model.result.*;
import cn.atsoft.dasheng.crm.pojo.ContractEnum;
import cn.atsoft.dasheng.crm.service.*;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.purchase.service.GetOrigin;
import cn.atsoft.dasheng.sys.modular.system.entity.FileInfo;
import cn.atsoft.dasheng.sys.modular.system.service.FileInfoService;
import cn.atsoft.dasheng.taxRate.service.TaxRateService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.convert.NumberChineseFormatter;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HtmlUtil;
import cn.hutool.poi.word.DocUtil;
import cn.hutool.poi.word.WordUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private BankService bankService;
    @Autowired
    private ContractService contractService;
    @Autowired
    private InvoiceService invoiceService;
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
    private UnitService unitService;
    @Autowired
    private TaxRateService rateService;
    @Autowired
    private FileInfoService fileInfoService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private PaymentDetailService paymentDetailService;
    @Autowired
    private ContractTempleteService contractTempleteService;
    @Autowired
    private ContractTempleteDetailService templeteDetailService;
    @Autowired
    private ContractExcel contractExcel;


    @Override
    public ContractResult detail(Long id) {
        Contract contract = this.getById(id);
        ContractResult contractResult = new ContractResult();
        ToolUtil.copyProperties(contract, contractResult);


        format(new ArrayList<ContractResult>() {{
            add(contractResult);
        }});


        return contractResult;
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
    public PageInfo findPageBySpec(ContractParam param, DataScope dataScope) {
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
    public Contract orderAddContract(Long orderId, ContractParam param, OrderParam orderParam, String orderType) {
        if (ToolUtil.isEmpty(param)) {
            throw new ServiceException(500, "合同对象为空");
        }
        Contract contract = new Contract();
        ToolUtil.copyProperties(param, contract);
        contract.setSource(orderType);
        contract.setSourceId(orderId);
        contract.setContent(JSON.toJSONString(param.getLabelResults()));

        if (ToolUtil.isEmpty(param.getTemplateId())) {
            throw new ServiceException(500, "请选择合同模板");
        }

        contract.setPartyA(orderParam.getBuyerId());
        contract.setPartyB(orderParam.getSellerId());
        contract.setPartyAPhone(orderParam.getPartyAPhone());
        contract.setPartyBPhone(orderParam.getPartyBPhone());
        contract.setPartyAAdressId(orderParam.getPartyAAdressId());
        contract.setPartyBAdressId(orderParam.getPartyBAdressId());
        contract.setPartyAContactsId(orderParam.getPartyAContactsId());
        contract.setPartyBContactsId(orderParam.getPartyBContactsId());
        this.save(contract);

        return contract;
    }

    @Override
    public String wordToHtml(Long id) {

        Contract contract = this.getById(id);
        Template template = templateService.getById(contract.getTemplateId());
        FileInfo fileInfo = fileInfoService.getById(template.getFileId());
        XWPFDocument document = null;
        try {
            document = contractExcel.formatDocument(contract, template, fileInfo.getFilePath());
        } catch (InvalidFormatException | IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            document.write(os);


//            Options options = Options.getFrom(DocumentKind.DOCX).to(ConverterTypeTo.PDF);

//            IConverter converter = ConverterRegistry.getRegistry().getConverter(options);

            OutputStream out = new ByteArrayOutputStream();
//            InputStream in = new FileInputStream(new File("D:/tmp/\\1517011925641457666.docx"));

            XHTMLOptions options = XHTMLOptions.create();

            XHTMLConverter.getInstance().convert(document, out, options);

//            converter.convert(in, out, options);
            System.out.println("aaa");
        } catch (Exception e) {
            e.printStackTrace();
        }


        //转化数据流，替换特殊字符
        return "";
    }

    @Override
    public List<LabelResult> getLabelResults(Long id) {

        List<String> list = new ArrayList<>();
        Template template = templateService.getById(id);

        if (ToolUtil.isNotEmpty(template) && ToolUtil.isNotEmpty(template.getFileId())) {
            FileInfo fileInfo = fileInfoService.getById(template.getFileId());
            WordUtils wordUtils = new WordUtils();
            XWPFDocument document = DocUtil.create(new File(fileInfo.getFilePath()));
            list.addAll(wordUtils.getLabel(document));
            // 替换表格中的参数
            list.addAll(wordUtils.getLabelInTable(document, new int[]{0}));
        }


        List<String> newStrings = new ArrayList<>();
        for (String s : list) {
            boolean judge = judge(s);
            if (judge) {
                newStrings.add(s);
            }
        }


        List<ContractTemplete> templetes = newStrings.size() == 0 ? new ArrayList<>() : contractTempleteService.query().in("name", newStrings).list();
        List<ContractTempleteResult> contractTempleteResults = BeanUtil.copyToList(templetes, ContractTempleteResult.class, new CopyOptions());
        List<Long> ids = new ArrayList<>();
        for (ContractTemplete templete : templetes) {
            ids.add(templete.getContractTemplateId());
        }
        List<ContractTempleteDetail> templeteDetails = ids.size() == 0 ? new ArrayList<>() : templeteDetailService.query().in("contract_templete_id", ids).list();
        List<ContractTempleteDetailResult> templeteDetailResults = BeanUtil.copyToList(templeteDetails, ContractTempleteDetailResult.class, new CopyOptions());


        List<LabelResult> results = new ArrayList<>();
        for (ContractTempleteResult contractTempleteResult : contractTempleteResults) {

            LabelResult labelResult = new LabelResult();
            labelResult.setName(contractTempleteResult.getName());
            labelResult.setTitle(contractTempleteResult.getName());
            labelResult.setType(contractTempleteResult.getType());
            labelResult.setIsHidden(contractTempleteResult.getIsHidden());

            List<LabelResult.LabelDetail> detail = new ArrayList<>();

            for (ContractTempleteDetailResult templeteDetailResult : templeteDetailResults) {
                if (contractTempleteResult.getContractTemplateId().equals(templeteDetailResult.getContractTempleteId())) {
                    LabelResult.LabelDetail labelDetail = new LabelResult.LabelDetail();
                    labelDetail.setName(templeteDetailResult.getValue());
                    labelDetail.setIsDefault(templeteDetailResult.getIsDefault());
                    detail.add(labelDetail);
                }
            }

            labelResult.setDetail(detail);
            results.add(labelResult);
        }
        return results;
    }


    private boolean judge(String s) {
        for (String label : ContractLabel.labels) {
            if (s.equals(label)) {
                return false;
            }
        }
        return true;
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


    @Override
    public List<OrderDetailResult> skuReplaceList(Long orderId) {
        List<OrderDetail> details = orderDetailService.query().in("order_id", orderId).list();
        List<OrderDetailResult> results = BeanUtil.copyToList(details, OrderDetailResult.class, new CopyOptions());
        orderDetailService.format(results);
        return results;
    }

    @Override
    public List<PaymentDetailResult> paymentDetailsReplaceList(Long orderId) {
        if (ToolUtil.isEmpty(orderId)) {
            return new ArrayList<>();
        }
        Payment payment = paymentService.query().eq("order_id", orderId).one();
        List<PaymentDetail> details = ToolUtil.isEmpty(payment.getPaymentId()) ? new ArrayList<>() : paymentDetailService.query().eq("payment_id", payment.getPaymentId()).list();
        return BeanUtil.copyToList(details, PaymentDetailResult.class, new CopyOptions());

    }

    @Override
    public String skuReplace(String content, Long orderId) {
        List<OrderDetail> details = orderDetailService.query().in("order_id", orderId).list();
        List<OrderDetailResult> results = BeanUtil.copyToList(details, OrderDetailResult.class, new CopyOptions());
        orderDetailService.format(results);

        String regStr = "\\<tr(.*?)\\>([\\w\\W]+?)<\\/tr>";
        Pattern pattern = Pattern.compile(regStr);
        Matcher m;

        String trText = "";
        while ((m = pattern.matcher(content)).find()) {
            String skuGroup = m.group(0);
            Matcher tr;

            String TrRegStr = "<tr(.+?)data-group(.+?)=(.+?)(\"|')sku(\"|')>([\\w\\W]+?)</tr>";
            Pattern TrPattern = Pattern.compile(TrRegStr);
            if ((tr = TrPattern.matcher(skuGroup)).find()) {


                int i = 0;
                for (OrderDetailResult result : results) {
                    String trString = tr.group(0);
                    i++;
                    Matcher tdm;
                    while ((tdm = WordUtils.matcher(trString)).find()) {
                        String group = tdm.group(1);
                        ContractEnum contractEnum = ContractEnum.fromString(group);
                        if (contractEnum != null) {
                            switch (contractEnum) {
                                case skuStrand:
                                    trString = trString.replace(ContractEnum.skuStrand.getDetail(), result.getSkuResult().getStandard());
                                    break;
                                case spuName:
                                    trString = trString.replace(ContractEnum.spuName.getDetail(), result.getSkuResult().getSpuResult().getName());
                                    break;
                                case skuName:
                                    trString = trString.replace(ContractEnum.skuName.getDetail(), result.getSkuResult().getSkuName() + "/" + result.getSkuResult().getSpecifications());
                                    break;
                                case brand:
                                    trString = trString.replace(ContractEnum.brand.getDetail(), result.getBrandResult().getBrandName());
                                    break;
                                case Line:
                                    trString = trString.replace(ContractEnum.Line.getDetail(), i + "");
                                    break;
                                case number:
                                    trString = trString.replace(ContractEnum.number.getDetail(), result.getPurchaseNumber() + "");
                                    break;
                                case unit:
                                    trString = trString.replace(ContractEnum.unit.getDetail(), result.getUnit().getUnitName());
                                    break;
                                case UnitPrice:

                                    trString = trString.replace(ContractEnum.UnitPrice.getDetail(), result.getOnePrice() + "");
                                    break;
                                case TotalPrice:
                                    trString = trString.replace(ContractEnum.TotalPrice.getDetail(), result.getTotalPrice() + "");
                                    break;
                                case invoiceType:
                                    String paperType = "";
                                    if (result.getPaperType() == 0) {
                                        paperType = "普票";
                                    } else {
                                        paperType = "专票";
                                    }
                                    trString = trString.replace(ContractEnum.invoiceType.getDetail(), paperType + "");
                                    break;
                                case DeliveryDate:
                                    trString = trString.replace(ContractEnum.DeliveryDate.getDetail(), result.getDeliveryDate() + "");
                                    break;
                                default:

                            }
                        }
                    }
                    trText = trText + trString;
                }
            }
            content = content.replace(skuGroup, trText);
        }
        return content;
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
                    if (group.toString().contains("${{物料编码}}")) {
                        group = new StringBuilder(group.toString().replace("${{物料编码}}", skuResult.getStandard()));
                    }
                    if (group.toString().contains("${{产品名称}}")) {
                        group = new StringBuilder(group.toString().replace("${{产品名称}}", skuResult.getSpuResult().getName()));
                    }
                    if (group.toString().contains("${{型号规格}}")) {
                        group = new StringBuilder(group.toString().replace("${{型号规格}}", skuResult.getSkuName() + "/" + skuResult.getSpecifications()));
                    }
                    if (group.toString().contains("${{品牌厂家}}")) {
                        Brand brand = brandService.getById(detailParam.getBrandId());
                        group = new StringBuilder(group.toString().replace("${{品牌厂家}}", brand.getBrandName()));
                    }
                    if (group.toString().contains("${{序号}}")) {
                        group = new StringBuilder(group.toString().replace("${{序号}}", i + 1 + ""));
                    }
                    if (group.toString().contains("${{数量}}")) {
                        if (ToolUtil.isNotEmpty(detailParam.getPurchaseNumber())) {
                            group = new StringBuilder(group.toString().replace("${{数量}}", detailParam.getPurchaseNumber().toString()));
                        } else {
                            group = new StringBuilder(group.toString().replace("${{数量}}", ""));
                        }

                    }
                    if (group.toString().contains("${{单位}}")) {        //单位
                        if (ToolUtil.isNotEmpty(detailParam.getUnitId())) {
                            Unit unit = unitService.getById(detailParam.getUnitId());
                            group = new StringBuilder(group.toString().replace("${{单位}}", unit.getUnitName()));
                        } else {
                            group = new StringBuilder(group.toString().replace("${{单位}}", ""));
                        }

                    }
                    if (group.toString().contains("${{单价}}")) {
                        if (ToolUtil.isEmpty(detailParam.getOnePrice())) {
                            group = new StringBuilder(group.toString().replace("${{单价}}", ""));
                        } else {
                            group = new StringBuilder(group.toString().replace("${{单价}}", detailParam.getOnePrice().toString()));
                        }

                    }
                    if (group.toString().contains("${{总价}}")) {  //总价
                        if (ToolUtil.isNotEmpty(detailParam.getTotalPrice())) {
                            group = new StringBuilder(group.toString().replace("${{总价}}", detailParam.getTotalPrice().toString()));
                        } else {
                            group = new StringBuilder(group.toString().replace("${{总价}}", ""));
                        }

                    }
                    if (group.toString().contains("${{发票类型}}")) {  //票据类型
                        String paperType = "";
                        if (detailParam.getPaperType() == 0) {
                            paperType = "普票";
                        } else {
                            paperType = "专票";
                        }
                        group = new StringBuilder(group.toString().replace("${{发票类型}}", paperType));
                    }

                    if (group.toString().contains("${{交货日期}}")) {
                        if (ToolUtil.isEmpty(detailParam.getDeliveryDate())) {
                            group = new StringBuilder(group.toString().replace("${{交货日期}}", ""));
                        } else {
                            group = new StringBuilder(group.toString().replace("${{交货日期}}", detailParam.getDeliveryDate().toString()));
                        }
                    }
                    if (ToolUtil.isNotEmpty(cycleReplaces)) {
                        CycleReplace cycleReplace = cycleReplaces.get(i);
                        for (CycleReplace.Cycle cycle : cycleReplace.getCycles()) {
                            if (cycle.getOldText().equals(cycle.getNewText())) {
                                group = new StringBuilder(group.toString().replace(cycle.getOldText(), ""));
                            } else {
                                group = new StringBuilder(group.toString().replace(cycle.getOldText(), cycle.getNewText()));
                            }
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
     * 替换
     *
     * @param content
     * @param orderParam
     * @return
     */
    private String replace(String content, OrderParam orderParam) {
        Customer buyer = customerService.getById(orderParam.getBuyerId());  //甲方
        Customer seller = customerService.getById(orderParam.getSellerId());  //乙方
        if (ToolUtil.isEmpty(buyer)) {
            throw new ServiceException(500, "请确定需方存在");
        }
        if (ToolUtil.isEmpty(seller)) {
            throw new ServiceException(500, "请确定乙方存在");
        }
        if (content.contains("${{需方委托代表}}")) {
            Contacts contacts = orderParam.getPartyAContactsId() == null ? new Contacts() : contactsService.getById(orderParam.getPartyAContactsId());
            if (ToolUtil.isEmpty(contacts) || ToolUtil.isEmpty(contacts.getContactsName())) {
                content = content.replace("${{需方委托代表}}", "");
            } else {
                content = content.replace("${{需方委托代表}}", contacts.getContactsName());
            }
        }
        if (content.contains("${{供方委托代表}}")) {
            Contacts contacts = orderParam.getPartyBContactsId() == null ? new Contacts() : contactsService.getById(orderParam.getPartyAContactsId());
            if (ToolUtil.isEmpty(contacts) || ToolUtil.isEmpty(contacts.getContactsName())) {
                content = content.replace("${{供方委托代表}}", "");
            } else {
                content = content.replace("${{供方委托代表}}", contacts.getContactsName());
            }
        }
        if (content.contains("${{需方公司地址}}")) {
            Adress adress = orderParam.getPartyAAdressId() == null ? new Adress() : adressService.getById(orderParam.getPartyAAdressId());
            if (ToolUtil.isEmpty(adress) || ToolUtil.isEmpty(adress.getLocation())) {
                content = content.replace("${{需方公司地址}}", "");
            } else {
                content = content.replace("${{需方公司地址}}", (adress.getLocation()));
            }
        }
        if (content.contains("${{供方公司地址}}")) {
            Adress adress = orderParam.getPartyBAdressId() == null ? new Adress() : adressService.getById(orderParam.getPartyBAdressId());
            if (ToolUtil.isEmpty(adress) || ToolUtil.isEmpty(adress.getLocation())) {
                content = content.replace("${{供方公司地址}}", "");
            } else {
                content = content.replace("${{供方公司地址}}", adress.getLocation());
            }

        }
        if (content.contains("${{需方公司电话}}")) {
            Phone phone = orderParam.getPartyAPhone() == null ? new Phone() : phoneService.getById(orderParam.getPartyAPhone());
            if (ToolUtil.isEmpty(phone) || ToolUtil.isEmpty(phone.getPhoneNumber())) {
                content = content.replace("${{需方公司电话}}", "");
            } else {
                content = content.replace("${{需方公司电话}}", phone.getPhoneNumber().toString());
            }
        }
        if (content.contains("${{供方公司电话}}")) {
            Phone phone = orderParam.getPartyBPhone() == null ? new Phone() : phoneService.getById(orderParam.getPartyBPhone());
            if (ToolUtil.isEmpty(phone) || ToolUtil.isEmpty(phone.getPhoneNumber())) {
                content = content.replace("${{供方公司电话}}", "");
            } else {
                content = content.replace("${{供方公司电话}}", phone.getPhoneNumber().toString());
            }
        }
        //-----------------------------------------------需要改动-----------------------------------------------------------------------
        if (content.contains("${{提取(交付)地点}}")) {
            Adress adress = orderParam.getPartyAAdressId() == null ? new Adress() : adressService.getById(orderParam.getAdressId());
            if (ToolUtil.isEmpty(adress) || ToolUtil.isEmpty(adress.getLocation())) {
                content = content.replace("${{提取(交付)地点}}", "");
            } else {
                content = content.replace("${{提取(交付)地点}}", (adress.getLocation() + (adress.getDetailLocation() == null ? "" : adress.getDetailLocation())));
                adress.setType("收获地址");
                adressService.updateById(adress);
            }
        }
        if (content.contains("${{接货人员}}")) {
            Contacts contacts = orderParam.getPartyAContactsId() == null ? new Contacts() : contactsService.getById(orderParam.getUserId());
            if (ToolUtil.isEmpty(contacts) || ToolUtil.isEmpty(contacts.getContactsName())) {
                content = content.replace("${{接货人员}}", "");
            } else {
                content = content.replace("${{接货人员}}", contacts.getContactsName());

            }
        }
        if (content.contains("${{接货人电话}}")) {
            Contacts contacts = orderParam.getUserId() == null ? new Contacts() : contactsService.getById(orderParam.getUserId());
            List<Phone> phones = contacts.getContactsId() == null ? new ArrayList<>() : phoneService.query().eq("contacts_id", contacts.getContactsId()).list();
            Phone phone = phones.get(0);

            if (ToolUtil.isNotEmpty(phone) || ToolUtil.isNotEmpty(phone.getPhoneNumber())) {
                content = content.replace("${{接货人电话}}", phone.getPhoneNumber().toString());
            } else {
                content = content.replace("${{接货人电话}}", "");
            }
        }
//-------------------------------------------------------------------------------------------------------------------------
        if (content.contains("${{需方公司名称}}")) {
            Customer customer = orderParam.getBuyerId() == null ? new Customer() : customerService.getById(orderParam.getBuyerId());
            if (ToolUtil.isEmpty(customer)) {
                throw new ServiceException(500, "请确认买方");
            }
            content = content.replace("${{需方公司名称}}", customer.getCustomerName());
        }
        if (content.contains("${{供方公司名称}}")) {
            Customer customer = orderParam.getSellerId() == null ? new Customer() : customerService.getById(orderParam.getSellerId());
            if (ToolUtil.isEmpty(customer)) {
                throw new ServiceException(500, "请确认供方公司");
            }
            content = content.replace("${{供方公司名称}}", customer.getCustomerName());
        }

        if (content.contains("${{需方开户银行}}")) {
            Bank bank = orderParam.getPartyABankId() == null ? new Bank() : bankService.getById(orderParam.getPartyABankId());
            if (ToolUtil.isEmpty(bank) || ToolUtil.isEmpty(bank.getBankName())) {
                content = content.replace("${{需方开户银行}}", "");
            } else {
                content = content.replace("${{需方开户银行}}", bank.getBankName());
            }
        }
        if (content.contains("${{供方开户银行}}")) {
            Bank bank = orderParam.getPartyBBankId() == null ? new Bank() : bankService.getById(orderParam.getPartyBBankId());
            if (ToolUtil.isEmpty(bank) || ToolUtil.isEmpty(bank.getBankName())) {
                content = content.replace("${{供方开户银行}}", "");
            } else {
                content = content.replace("${{供方开户银行}}", bank.getBankName());
            }
        }
        if (content.contains("${{需方银行账号}}")) {
            Invoice invoice = orderParam.getPartyABankAccount() == null ? new Invoice() : invoiceService.getById(orderParam.getPartyABankAccount());
            if (ToolUtil.isEmpty(invoice) || ToolUtil.isEmpty(invoice.getBankAccount())) {
                content = content.replace("${{需方银行账号}}", "");
            } else {
                content = content.replace("${{需方银行账号}}", invoice.getBankAccount());
            }
        }
        if (content.contains("${{供方银行账号}}")) {
            Invoice invoice = orderParam.getPartyBBankAccount() == null ? new Invoice() : invoiceService.getById(orderParam.getPartyBBankAccount());
            if (ToolUtil.isEmpty(invoice) || ToolUtil.isEmpty(invoice.getBankAccount())) {
                content = content.replace("${{供方银行账号}}", "");
            } else {
                content = content.replace("${{供方银行账号}}", invoice.getBankAccount());
            }
        }
        if (content.contains("${{合计金额小写}}")) {  //总计
            if (ToolUtil.isEmpty(orderParam.getPaymentParam().getMoney())) {
                content = content.replace("${{合计金额小写}}", "");
            } else {
                content = content.replace("${{合计金额小写}}", orderParam.getPaymentParam().getMoney().toString());
            }
        }
        if (content.contains("${{合计金额大写}}")) {  //总计
            if (ToolUtil.isEmpty(orderParam.getPaymentParam().getMoney())) {
                content = content.replace("${{合计金额大写}}", "");
            } else {
                Long money = orderParam.getPaymentParam().getMoney();
                String format = NumberChineseFormatter.format(money, true, true);
                content = content.replace("${{合计金额大写}}", format);
            }
        }
        if (content.contains("${{需方法人代表}}")) {  //A法定代表人
            if (ToolUtil.isNotEmpty(orderParam.getPartyALegalPerson())) {
                content = content.replace("${{需方法人代表}}", orderParam.getPartyALegalPerson());
            } else {
                content = content.replace("${{需方法人代表}}", "");
            }
        }
        if (content.contains("${{需方法人电话}}")) {  //A法定代表人
            content = content.replace("${{需方法人电话}}", "");
        }
        if (content.contains("${{供方法人电话}}")) {  //A法定代表人
            content = content.replace("${{供方法人电话}}", "");
        }

        if (content.contains("${{供方法人代表}}")) {  //B法定代表人
            if (ToolUtil.isNotEmpty(orderParam.getPartyBLegalPerson())) {
                content = content.replace("${{供方法人代表}}", orderParam.getPartyBLegalPerson());
            } else {
                content = content.replace("${{供方法人代表}}", "");
            }

        }
        if (content.contains("${{需方开户行号}}")) {  //A开户行号
            if (ToolUtil.isNotEmpty(orderParam.getPartyABankNo())) {
                content = content.replace("${{需方开户行号}}", orderParam.getPartyABankNo().toString());
            } else {
                content = content.replace("${{需方开户行号}}", "");
            }

        }
        if (content.contains("${{供方开户行号}}")) {  //B开户行号
            if (ToolUtil.isNotEmpty(orderParam.getPartyBBankNo())) {
                content = content.replace("${{供方开户行号}}", orderParam.getPartyBBankNo().toString());
            } else {
                content = content.replace("${{供方开户行号}}", "");
            }

        }
        if (content.contains("${{需方代表电话}}")) {
            if (ToolUtil.isNotEmpty(buyer.getTelephone())) {
                content = content.replace("${{需方代表电话}}", buyer.getTelephone());
            } else {
                content = content.replace("${{需方代表电话}}", "");
            }
        }
        if (content.contains("${{供方代表电话}}")) {
            if (ToolUtil.isNotEmpty(seller.getTelephone())) {
                content = content.replace("${{供方代表电话}}", seller.getTelephone());
            } else {
                content = content.replace("${{供方代表电话}}", "");
            }
        }
        if (content.contains("${{需方公司电邮}}")) {
            if (ToolUtil.isNotEmpty(buyer.getEmall())) {
                content = content.replace("${{需方公司电邮}}", buyer.getEmall());
            } else {
                content = content.replace("${{需方公司电邮}}", "");
            }
        }
        if (content.contains("${{供方公司电邮}}")) {
            if (ToolUtil.isNotEmpty(seller.getEmall())) {
                content = content.replace("${{供方公司电邮}}", seller.getEmall());
            } else {
                content = content.replace("${{供方公司电邮}}", "");
            }
        }
        if (content.contains("${{需方税号}}")) {
            content = content.replace("${{需方税号}}", "");
        }
        if (content.contains("${{供方税号}}")) {
            content = content.replace("${{供方税号}}", "");
        }


        if (content.contains("${{需方公司传真}}")) {  //A传真
            if (ToolUtil.isNotEmpty(orderParam.getPartyAFax())) {
                content = content.replace("${{需方公司传真}}", orderParam.getPartyAFax());
            } else {
                content = content.replace("${{需方公司传真}}", orderParam.getPartyAFax());
            }
        }

        if (content.contains("${{供方公司传真}}")) {  //B传真
            if (ToolUtil.isNotEmpty(orderParam.getPartyBFax())) {
                content = content.replace("${{供方公司传真}}", orderParam.getPartyBFax());
            } else {
                content = content.replace("${{供方公司传真}}", "");
            }

        }
        if (content.contains("${{需方邮政编码}}")) {  //A邮编
            if (ToolUtil.isNotEmpty(orderParam.getPartyAZipcode())) {
                content = content.replace("${{需方邮政编码}}", orderParam.getPartyAZipcode());
            } else {
                content = content.replace("${{需方邮政编码}}", "");
            }

        }
        if (content.contains("${{供方邮政编码}}")) {  //B邮编
            if (ToolUtil.isNotEmpty(orderParam.getPartyBZipcode())) {
                content = content.replace("${{供方邮政编码}}", orderParam.getPartyBZipcode());
            } else {
                content = content.replace("${{供方邮政编码}}", "");
            }

        }
        if (content.contains("${{需方公司电话}}")) {  //A公司电话
            if (ToolUtil.isNotEmpty(orderParam.getPartyACompanyPhone())) {
                content = content.replace("${{需方公司电话}}", orderParam.getPartyACompanyPhone());
            } else {
                content = content.replace("${{需方公司电话}}", "");
            }

        }
        if (content.contains("${{供方公司电话}}")) {  //B公司电话
            if (ToolUtil.isNotEmpty(orderParam.getPartyBCompanyPhone())) {
                content = content.replace("${{供方公司电话}}", orderParam.getPartyBCompanyPhone());
            } else {
                content = content.replace("${{供方公司电话}}", "");
            }

        }
        if (content.contains("${{运费承担}}")) {  //是否含运费
            if (ToolUtil.isNotEmpty(orderParam.getPaymentParam().getFreight())) {
                if (orderParam.getPaymentParam().getFreight() == 1) {
                    content = content.replace("${{运费承担}}", "含运");
                } else {
                    content = content.replace("${{运费承担}}", "不含运");
                }
            } else {
                content = content.replace("${{运费承担}}", "");
            }
        }

        if (content.contains("${{运输方式}}")) {  //交货方式
            if (ToolUtil.isNotEmpty(orderParam.getPaymentParam().getDeliveryWay())) {
                content = content.replace("${{运输方式}}", orderParam.getPaymentParam().getDeliveryWay());
            } else {
                content = content.replace("${{运输方式}}", "");
            }
        }
        if (content.contains("${{提货人员}}")) {
            content = content.replace("${{提货人员}}", "");
        }
        if (content.contains("${{接货人电话}}")) {
            content = content.replace("${{接货人电话}}", "");
        }
        if (content.contains("${{质量标准}}")) {
            content = content.replace("${{质量标准}}", "");
        }
        if (content.contains("${{质量要求}}")) {
            content = content.replace("${{质量要求}}", "");
        }
        if (content.contains("${{质保起始方式}}")) {
            content = content.replace("${{质保起始方式}}", "");
        }
        if (content.contains("${{质保周期}}")) {
            content = content.replace("${{质保周期}}", "");
        }
        if (content.contains("${{其他约定事项}}")) {
            content = content.replace("${{其他约定事项}}", "");
        }
        if (content.contains("${{提取(交付)地点}}")) {  //交货地址
            if (ToolUtil.isNotEmpty(orderParam.getPaymentParam().getAdressId())) {
                Adress adress = adressService.getById(orderParam.getPaymentParam().getAdressId());
                if (ToolUtil.isEmpty(adress)) {
                    content = content.replace("${{提取(交付)地点}}", "");
                } else {
                    content = content.replace("${{提取(交付)地点}}", ToolUtil.isNotEmpty(adress.getLocation()) ? adress.getLocation() : "");
                }
            }
        }

        if (content.contains("${{采购合同编号}}")) {
            if (ToolUtil.isNotEmpty(orderParam.getContractParam().getContractCoding())) {
                content = content.replace("${{采购合同编号}}", orderParam.getContractParam().getContractCoding());
            } else {
                content = content.replace("${{采购合同编号}}", "");
            }
        }

        if (content.contains("${{合同签订时间}}")) {
            Date date = new DateTime();
            content = content.replace("${{合同签订时间}}", date.toString());
        }
        if (content.contains("${{合同签订地点}}")) {
            content = content.replace("${{合同签订地点}}", "");
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
                    if (payGroup.contains("${{付款金额}}") && ToolUtil.isNotEmpty(detailParam.getMoney())) {      //金额
                        payGroup = payGroup.replace("${{付款金额}}", detailParam.getMoney().toString());
                    }
                    if (payGroup.contains("${{日期方式}}") && ToolUtil.isNotEmpty(detailParam.getDateWay())) {     //日期方式
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
                        payGroup = payGroup.replace("${{日期方式}}", dateWay);
                    }
                    if (payGroup.contains("${{付款比例}}") && ToolUtil.isNotEmpty(detailParam.getPercentum())) {    //比例
                        payGroup = payGroup.replace("${{付款比例}}", detailParam.getPercentum().toString());
                    }

                    if (payGroup.contains("${{付款日期}}")) {    //付款时间
                        if (ToolUtil.isNotEmpty(detailParam.getPayTime())) {
                            DateTime date = DateUtil.date(detailParam.getPayTime());
                            payGroup = payGroup.replace("${{付款日期}}", date.toString());
                        } else {
                            payGroup = payGroup.replace("${{付款日期}}", "");
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
