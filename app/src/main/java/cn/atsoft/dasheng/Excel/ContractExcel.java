package cn.atsoft.dasheng.Excel;


import cn.afterturn.easypoi.exception.word.enmus.WordExportEnum;
import cn.afterturn.easypoi.word.WordExportUtil;
import cn.atsoft.dasheng.Excel.pojo.LabelResult;
import cn.atsoft.dasheng.Excel.pojo.TempReplaceRule;
import cn.atsoft.dasheng.app.entity.Contract;
import cn.atsoft.dasheng.app.entity.Template;
import cn.atsoft.dasheng.app.service.ContractService;
import cn.atsoft.dasheng.app.service.TemplateService;
import cn.atsoft.dasheng.base.consts.ConstantsContext;
import cn.atsoft.dasheng.base.oshi.model.SysFileInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.entity.ContractTemplete;
import cn.atsoft.dasheng.crm.entity.ContractTempleteDetail;
import cn.atsoft.dasheng.crm.model.result.OrderDetailResult;
import cn.atsoft.dasheng.crm.pojo.ContractEnum;
import cn.atsoft.dasheng.crm.service.ContractTempleteDetailService;
import cn.atsoft.dasheng.crm.service.ContractTempleteService;
import cn.atsoft.dasheng.crm.service.OrderService;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.sys.modular.system.entity.FileInfo;
import cn.atsoft.dasheng.sys.modular.system.service.FileInfoService;
import cn.hutool.core.lang.Assert;
import cn.hutool.poi.word.DocUtil;
import cn.hutool.poi.word.Word07Writer;
import com.alibaba.fastjson.JSON;
import io.lettuce.core.dynamic.annotation.Param;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
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
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
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
    private ContractTempleteService templeteService;
    @Autowired
    private ContractTempleteDetailService templeteDetailService;


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
        try {
            Contract contract = contractService.getById(id);
            Template template = templateService.getById(contract.getTemplateId());
            FileInfo fileInfo = fileInfoService.getById(template.getFileId());

            XWPFDocument document = formatDocument(contract, template, fileInfo.getFilePath());  //读取word

            String fileName = "test.docx";
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
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
    public ResponseData<List<XWPFTable>> getWordTables(@RequestParam("fileId") Long fileId) {
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
    private XWPFDocument formatDocument(Contract contract, Template template, String url) {

        List<LabelResult> results = JSON.parseArray(contract.getContent(), LabelResult.class);
        Map<String, Object> map = new HashMap<>();
        for (LabelResult result : results) {
            if (!result.type.equals("sku") && !result.getType().equals("pay")) {
                map.put(result.getName(), result.getValue());
            }
//            if (result.getType().equals("sku")) {
//                ContractTempleteDetail templeteDetail = templeteDetailService.query().eq("contract_templete_id", template.getTemplateId()).one();
//                String skuReplace = contractService.skuReplace(templeteDetail.getValue(), contract.getSourceId());
//                map.put(result.getName(), skuReplace);
//            }
//
//            if (result.getType().equals("pay")) {
//
//            }

        }

//        ContractTempleteDetail templateDetail = templeteDetailService.query().eq("contract_template_id", template.getTemplateId()).one();
//        String skuReplace = contractService.skuReplace(templateDetail.getValue(), contract.getSourceId());

        List<OrderDetailResult> list = contractService.skuReplaceList(contract.getSourceId());

        map.put("sku", list);

        map.putAll(orderService.mapFormat(contract.getContractId()));

        TempReplaceRule tempReplaceRule = JSON.parseObject(template.getReplaceRule(), TempReplaceRule.class);
        List<TempReplaceRule.ReplaceRule> replaceRules = tempReplaceRule.getReplaceRules();

        WordUtils wordUtils = new WordUtils();

        XWPFDocument document = DocUtil.create(new File(url));


        wordUtils.replaceInPara(document, map);
        // 替换表格中的参数
        wordUtils.replaceInTable(document, new int[]{0}, map, replaceRules);

        return document;
    }

    private void wordToHtml(Long id) {

        Contract contract = contractService.getById(id);
        Template template = templateService.getById(contract.getTemplateId());
        FileInfo fileInfo = fileInfoService.getById(template.getFileId());

        XWPFDocument document = formatDocument(contract, template, fileInfo.getFilePath());

        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter((org.w3c.dom.Document) document);


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

    public static Map<String, Object> orderFormat(OrderDetailResult results) {

        Map<String, Object> map = new HashMap<>();

        int i = 0;
        for (ContractEnum value : ContractEnum.values()) {
            i++;
            switch (value) {
                case skuStrand:
                    map.put(ContractEnum.skuStrand.getDetail(), results.getSkuResult().getStandard());
                case spuName:
                    map.put(ContractEnum.spuName.getDetail(), results.getSkuResult().getSpuName());
                case skuName:
                    map.put(ContractEnum.skuName.getDetail(), results.getSkuResult().getSkuName());
                case brand:
                    map.put(ContractEnum.brand.getDetail(), results.getBrandResult().getBrandName());
                case Line:
                    map.put(ContractEnum.Line.getDetail(), i + "");
                case number:
                    map.put(ContractEnum.number.getDetail(), results.getPurchaseNumber() + "");
                case unit:
                    map.put(ContractEnum.unit.getDetail(), results.getUnit().getUnitName());
                case UnitPrice:
                    map.put(ContractEnum.UnitPrice.getDetail(), results.getOnePrice() + "");
                case TotalPrice:
                    map.put(ContractEnum.TotalPrice.getDetail(), results.getTotalPrice() + "");
                case invoiceType:
                    String paperType = "";
                    if (ToolUtil.isNotEmpty(results.getPaperType()) && results.getPaperType() == 0) {
                        paperType = "普票";
                    } else {
                        paperType = "专票";
                    }
                    map.put(ContractEnum.invoiceType.getDetail(), paperType);
                case DeliveryDate:
                    map.put(ContractEnum.invoiceType.getDetail(), results.getDeliveryDate() + "");

            }
        }
        return map;
    }
}
