package cn.atsoft.dasheng.Excel;


import cn.afterturn.easypoi.exception.word.enmus.WordExportEnum;
import cn.afterturn.easypoi.word.WordExportUtil;
import cn.atsoft.dasheng.Excel.pojo.LabelResult;
import cn.atsoft.dasheng.Excel.pojo.TempReplaceRule;
import cn.atsoft.dasheng.app.entity.Contract;
import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.entity.Template;
import cn.atsoft.dasheng.app.service.ContractService;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.app.service.TemplateService;
import cn.atsoft.dasheng.base.consts.ConstantsContext;
import cn.atsoft.dasheng.base.oshi.model.SysFileInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.entity.ContractTemplete;
import cn.atsoft.dasheng.crm.entity.ContractTempleteDetail;
import cn.atsoft.dasheng.crm.entity.Order;
import cn.atsoft.dasheng.crm.model.result.OrderDetailResult;
import cn.atsoft.dasheng.crm.model.result.PaymentDetailResult;
import cn.atsoft.dasheng.crm.pojo.ContractEnum;
import cn.atsoft.dasheng.crm.service.ContractTempleteDetailService;
import cn.atsoft.dasheng.crm.service.ContractTempleteService;
import cn.atsoft.dasheng.crm.service.OrderService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.sys.modular.system.entity.FileInfo;
import cn.atsoft.dasheng.sys.modular.system.service.FileInfoService;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.poi.word.DocUtil;
import cn.hutool.poi.word.Word07Writer;
import com.alibaba.fastjson.JSON;
import com.sun.org.apache.bcel.internal.generic.NEW;
import io.lettuce.core.dynamic.annotation.Param;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/Excel")
@Slf4j
public class ContractExcel {

    @Autowired
    private ContractService contractService;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private FileInfoService fileInfoService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerService customerService;


    @RequestMapping(value = "/exportContract", method = RequestMethod.GET)
    public void test(HttpServletRequest request, HttpServletResponse response, Long id) {
        try {
            Contract contract = contractService.getById(id);
            //word内容
            byte b[] = contract.getContent().getBytes(StandardCharsets.UTF_8);  //这里是必须要设置编码的，不然导出中文就会乱码。
            ByteArrayInputStream bais = new ByteArrayInputStream(b);//将字节数组包装到流中
            /*
             * 关键地方
             * 生成word格式 */
            POIFSFileSystem poifs = new POIFSFileSystem();
            DirectoryEntry directory = poifs.getRoot();
            DocumentEntry documentEntry = directory.createDocument("文档名称", bais);
            //输出文件
            request.setCharacterEncoding("utf-8");
            response.setContentType("application/contractWord");//导出word格式
            response.setHeader("Content-disposition", "attachment;filename=contract.doc");

            OutputStream ostream = response.getOutputStream();
            poifs.writeFilesystem(ostream);
            bais.close();
            ostream.close();
        } catch (Exception e) {
            //异常处理
        }

    }

    @RequestMapping(value = "/exportContractWord", method = RequestMethod.GET)
    public void exportContract(HttpServletResponse response, Long id) {

        Contract contract = contractService.getById(id);
        if (ToolUtil.isEmpty(contract)) {
            throw new ServiceException(500, "请确定合同");
        }
        Template template = templateService.getById(contract.getTemplateId());
        if (ToolUtil.isEmpty(template)) {
            throw new ServiceException(500, "请确定合同模板");
        }
        FileInfo fileInfo = fileInfoService.getById(template.getFileId());
        if (ToolUtil.isEmpty(fileInfo)) {
            throw new ServiceException(500, "请确定合同模板");
        }
        try {
            XWPFDocument document = formatDocument(contract, template, fileInfo.getFilePath());  //读取word


            Order order = orderService.getById(contract.getSourceId());
            Long customerId;
            if (ToolUtil.isNotEmpty(order.getType()) && order.getType().equals(1)) {
                customerId = order.getSellerId();
            } else {
                customerId = order.getBuyerId();
            }
            Customer customer = customerService.getById(customerId);

            if (ToolUtil.isEmpty(contract.getCoding())) {
                contract.setCoding("");
            }

            String fileName = contract.getCoding() + customer.getCustomerName() ;
            String encode = URLEncoder.encode(fileName, "utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + encode + ".docx");
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            OutputStream os = response.getOutputStream();
            document.write(os);
        } catch (Exception e) {
            //异常处理
            e.printStackTrace();
        }

    }

    /**
     * 返回表格
     */
    @RequestMapping(value = "/getWordTables", method = RequestMethod.GET)
    public ResponseData getWordTables(@RequestParam("fileId") Long fileId) {
        FileInfo fileInfo = fileInfoService.getById(fileId);
        XWPFDocument document = DocUtil.create(new File(fileInfo.getFilePath()));
        List<XWPFTable> documentTables = document.getTables();
        return ResponseData.success(documentTables);
    }


    /**
     * 获取word
     *
     * @param contract
     * @param url
     * @return
     */
    public XWPFDocument formatDocument(Contract contract, Template template, String url) throws InvalidFormatException, IOException {

        List<LabelResult> results = JSON.parseArray(contract.getContent(), LabelResult.class);
        if (ToolUtil.isEmpty(results)) {
            results = new ArrayList<>();
        }
        Map<String, Object> map = new HashMap<>();
        for (LabelResult result : results) {
            if (!result.type.equals("sku") && !result.getType().equals("pay")) {
                map.put(result.getName(), ToolUtil.isEmpty(result.getValue()) ? "" : result.getValue());
            }
        }


        List<OrderDetailResult> list = contractService.skuReplaceList(contract.getSourceId());
        List<PaymentDetailResult> replaceList = contractService.paymentDetailsReplaceList(contract.getSourceId());

        map.put("sku", list);
        map.put("pay", replaceList);
        map.putAll(orderService.mapFormat(contract.getContractId()));

        TempReplaceRule tempReplaceRule = JSON.parseObject(template.getReplaceRule(), TempReplaceRule.class);
        List<TempReplaceRule.ReplaceRule> replaceRules = tempReplaceRule.getReplaceRules();

        WordUtils wordUtils = new WordUtils();

        InputStream inputStream = new FileInputStream(url);
        XWPFDocument document = new XWPFDocument(inputStream);

//        XWPFDocument document  = DocUtil.create(new File(url));

        wordUtils.replaceInPara(document, map);

        wordUtils.replaceInTable(document, map, replaceRules);


        return document;

    }


    @RequestMapping(value = "/getWordLables", method = RequestMethod.GET)
    public ResponseData getWordLable(@RequestParam("id") Long id) {
        List<String> list = new ArrayList<>();

        Template template = templateService.getById(id);

        if (ToolUtil.isNotEmpty(template) && ToolUtil.isNotEmpty(template.getFileId())) {
            FileInfo fileInfo = fileInfoService.getById(template.getFileId());
            WordUtils wordUtils = new WordUtils();
            XWPFDocument document = DocUtil.create(new File(fileInfo.getFilePath()));
            list = wordUtils.getLabel(document);
        }


        return ResponseData.success(list);
    }


    public static Map<String, Object> payFormat(PaymentDetailResult result) {
        Map<String, Object> map = new HashMap<>();

        for (ContractEnum value : ContractEnum.values()) {
            switch (value) {
                case payMoney:
                    if (ToolUtil.isNotEmpty(result.getMoney())) {
                        map.put(ContractEnum.payMoney.getDetail(), result.getMoney());
                    } else {
                        map.put(ContractEnum.payMoney.getDetail(), "");
                    }
                case DateWay:
                    if (ToolUtil.isNotEmpty(result.getDateWay())) {
                        map.put(ContractEnum.DateWay.getDetail(), "");
                    } else {
                        String dateWay = "";
                        switch (result.getDateWay()) {
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
                        map.put(ContractEnum.DateWay.getDetail(), dateWay);
                    }

                case PaymentProportion:
                    if (ToolUtil.isNotEmpty(result.getPercentum())) {
                        map.put(ContractEnum.PaymentProportion.getDetail(), result.getPercentum().toString());
                    } else {
                        map.put(ContractEnum.PaymentProportion.getDetail(), "");
                    }
                case PaymentDate:
                    if (ToolUtil.isNotEmpty(result.getPayTime())) {
                        DateTime date = DateUtil.date(result.getPayTime());
                        map.put(ContractEnum.PaymentDate.getDetail(), date.toString());
                    } else {
                        map.put(ContractEnum.PaymentDate.getDetail(), "");
                    }

            }

        }

        return map;
    }

    public static Map<String, Object> orderFormat(OrderDetailResult results) {

        Map<String, Object> map = new HashMap<>();

        int i = 0;
        for (ContractEnum value : ContractEnum.values()) {
            i++;
            switch (value) {
                case skuStrand:
                    if (ToolUtil.isNotEmpty(results.getSkuResult()) && ToolUtil.isNotEmpty(results.getSkuResult().getStandard())) {
                        map.put(ContractEnum.skuStrand.getDetail(), results.getSkuResult().getStandard());
                    } else {
                        map.put(ContractEnum.skuStrand.getDetail(), "");
                    }
                case spuName:
                    map.put(ContractEnum.spuName.getDetail(), results.getSkuResult().getSpuResult().getName());
                case skuName:
                    map.put(ContractEnum.skuName.getDetail(), results.getSkuResult().getSkuName());
                case brand:
                    if (ToolUtil.isNotEmpty(results.getBrandResult()) && ToolUtil.isNotEmpty(results.getBrandResult().getBrandName())) {
                        map.put(ContractEnum.brand.getDetail(), results.getBrandResult().getBrandName());
                    } else {
                        map.put(ContractEnum.brand.getDetail(), "");
                    }
                case Line:
                    map.put(ContractEnum.Line.getDetail(), i + "");
                case number:
                    if (ToolUtil.isNotEmpty(results.getPurchaseNumber())) {
                        map.put(ContractEnum.number.getDetail(), results.getPurchaseNumber() + "");
                    } else {
                        map.put(ContractEnum.number.getDetail(), "");
                    }
                case unit:
                    if (ToolUtil.isNotEmpty(results.getUnit()) && ToolUtil.isNotEmpty(results.getUnit().getUnitName())) {
                        map.put(ContractEnum.unit.getDetail(), results.getUnit().getUnitName());
                    } else {
                        map.put(ContractEnum.unit.getDetail(), "");
                    }

                case UnitPrice:
                    if (ToolUtil.isNotEmpty(results.getOnePrice())) {
                        map.put(ContractEnum.UnitPrice.getDetail(), results.getOnePrice() + "");
                    } else {
                        map.put(ContractEnum.UnitPrice.getDetail(), "");
                    }
                case TotalPrice:
                    if (ToolUtil.isNotEmpty(results.getTotalPrice())) {
                        map.put(ContractEnum.TotalPrice.getDetail(), results.getTotalPrice() + "");
                    } else {
                        map.put(ContractEnum.TotalPrice.getDetail(), "");
                    }
                case invoiceType:
                    String paperType = "";
                    if (ToolUtil.isNotEmpty(results.getPaperType()) && results.getPaperType() == 0) {
                        paperType = "普票";
                    } else {
                        paperType = "专票";
                    }
                    map.put(ContractEnum.invoiceType.getDetail(), paperType);
                case remakr:
                    if (ToolUtil.isNotEmpty(results.getRemark())) {
                        map.put(ContractEnum.remakr.getDetail(), results.getRemark() + "");
                    } else {
                        map.put(ContractEnum.remakr.getDetail(), "");
                    }

            }
        }
        return map;
    }
}
