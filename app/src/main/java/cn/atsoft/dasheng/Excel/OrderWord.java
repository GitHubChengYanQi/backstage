package cn.atsoft.dasheng.Excel;

import cn.atsoft.dasheng.Excel.pojo.LabelResult;
import cn.atsoft.dasheng.Excel.pojo.TempReplaceRule;
import cn.atsoft.dasheng.app.entity.Contract;
import cn.atsoft.dasheng.app.entity.Template;
import cn.atsoft.dasheng.app.service.ContractService;
import cn.atsoft.dasheng.app.service.TemplateService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.model.result.OrderDetailResult;
import cn.atsoft.dasheng.crm.model.result.PaymentDetailResult;
import cn.atsoft.dasheng.crm.service.OrderService;
import cn.atsoft.dasheng.erp.entity.InstockOrder;
import cn.atsoft.dasheng.erp.entity.InventoryStock;
import cn.atsoft.dasheng.erp.service.InstockOrderService;
import cn.atsoft.dasheng.erp.service.InventoryStockService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.modular.system.entity.FileInfo;
import cn.atsoft.dasheng.sys.modular.system.service.FileInfoService;
import com.alibaba.fastjson.JSON;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Controller
@RequestMapping("/OrderWord")
public class OrderWord {


    @Autowired
    private TemplateService templateService;
    @Autowired
    private FileInfoService fileInfoService;
    @Autowired
    private InstockOrderService instockOrderService;
    @Autowired
    private InventoryStockService inventoryStockService;


    @RequestMapping(value = "/exportOrderWord", method = RequestMethod.GET)
    public void exportContract(HttpServletResponse response, Long id) {


        Template template = templateService.getById(id);
        if (ToolUtil.isEmpty(template)) {
            throw new ServiceException(500, "请确定模板");
        }
        FileInfo fileInfo = fileInfoService.getById(template.getFileId());
        if (ToolUtil.isEmpty(fileInfo)) {
            throw new ServiceException(500, "请确定模板");
        }
        try {
            XWPFDocument document = formatDocument(template, fileInfo.getFilePath());  //读取word

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
     * 获取word
     *
     * @param
     * @param url
     * @return
     */
    public XWPFDocument formatDocument(Template template, String url) throws InvalidFormatException, IOException {

        Long id = 1544617669762859010L;
        Map<String, Object> map = inventoryStockService.orderDetail(id);


        TempReplaceRule tempReplaceRule = JSON.parseObject(template.getReplaceRule(), TempReplaceRule.class);
        List<TempReplaceRule.ReplaceRule> replaceRules = tempReplaceRule.getReplaceRules();


        InputStream inputStream = new FileInputStream(url);
        XWPFDocument document = new XWPFDocument(inputStream);


        replaceInPara(document, map);
        replaceInTable(document, map, replaceRules);


        return document;

    }


    /**
     * 替换文档中的内容
     *
     * @param doc    Word的文档
     * @param params 待填充的数据
     *               params.put("key",value) 文档中对应为 ${key}
     */
    public void replaceInPara(XWPFDocument doc, Map<String, Object> params) {
        Iterator<XWPFParagraph> iterator = doc.getParagraphsIterator();
        while (iterator.hasNext()) {
            replaceInPara(iterator.next(), params);
        }
    }


    private void replaceInPara(XWPFParagraph para, Map<String, Object> params) {
        for (String key : params.keySet()) {
            params.putIfAbsent(key, "");
        }

        List<XWPFRun> runs;
        StringBuilder runText = new StringBuilder();

        if (matcher(para.getParagraphText()).find()) {
            runs = para.getRuns();
            int j = runs.size();
            for (int i = 0; i < j; i++) {

                if (ToolUtil.isNotEmpty(runs.get(0).getText(0))) {
//                    runText = new StringBuilder((runs.get(i).getText(0)));
                    runText.append(runs.get(0).getText(0));
                }
//                else {
//                    runText = new StringBuilder("");
//                }


                //保留最后一个段落，在这段落中替换值，保留原有段落样式
                if (!((j - 1) == i)) {
                    para.removeRun(0);
                }
            }
            String text = runText.toString();
            Matcher matcher;
            while ((matcher = matcher(text)).find()) {
                String group = matcher.group(1);
                if (group.equals("${sku}") || group.equals("${pay}")) {
                    text = matcher.replaceFirst("");
                } else {
                    text = matcher.replaceFirst(String.valueOf(params.get(group)));
                }
            }
            runs.get(0).setText(text, 0);

        }
    }

    /**
     * 替换表格中的变量
     *
     * @param table
     * @param params
     */
    public void replaceInTable(XWPFTable table, Map<String, Object> params) {
        List<XWPFTableRow> rows;
        List<XWPFTableCell> cells;
        List<XWPFParagraph> paras;
        //判断表格中是否有 ${} 有就表示需要替换值
        if (matcher(table.getText()).find()) {
            rows = table.getRows();
            for (XWPFTableRow row : rows) {
                cells = row.getTableCells();
                for (XWPFTableCell cell : cells) {
                    paras = cell.getParagraphs();
                    for (XWPFParagraph para : paras) {
                        replaceInPara(para, params);
                    }
                }
            }
        }
    }

    /**
     * 正则匹配字符串
     *
     * @param str
     * @return
     */
    public static Matcher matcher(String str) {
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher;
    }

    /**
     * 替换指定表格中的变量
     *
     * @param doc
     * @param tabIndex 表格下标
     * @param params   替换参数
     */
    public void replaceInTable(XWPFDocument doc, Map<String, Object> params, List<TempReplaceRule.ReplaceRule> replaceRules) {


        List<XWPFTable> tables = doc.getTables();

        for (int i = 0; i < tables.size(); i++) {
            XWPFTable xwpfTable = tables.get(i);   //表

            List<XWPFTableRow> rows = xwpfTable.getRows();

            for (int j = 0; j < rows.size(); j++) {    //行
                XWPFTableRow tableRow = rows.get(j);


//                xwpfTable.addRow()

//                for (XWPFTableCell tableCell : tableRow.getTableCells()) {
//
//                }

                String rule = getRule(i, j, replaceRules);
                if (ToolUtil.isNotEmpty(rule)) {
                    switch (rule) {
                        case "sku":
                            List<OrderDetailResult> results = (List<OrderDetailResult>) params.get(rule);
                            int i1 = 1;
                            for (OrderDetailResult result : results) {

                                XWPFTableRow xwpfTableRow = xwpfTable.insertNewTableRow(j + i1);  //新行

                                Map<String, Object> orderFormat = ContractExcel.orderFormat(result);
                                List<XWPFTableCell> cells = tableRow.getTableCells();
                                for (XWPFTableCell cell : cells) {          //段落

                                    XWPFTableCell xwpfTableCell = xwpfTableRow.addNewTableCell();
                                    //列属性
                                    xwpfTableCell.getCTTc().setTcPr(cell.getCTTc().getTcPr());
                                    //段落属性
                                    if (cell.getParagraphs() != null && cell.getParagraphs().size() > 0) {
                                        xwpfTableCell.getParagraphs().get(0).getCTP().setPPr(cell.getParagraphs().get(0).getCTP().getPPr());
                                        if (cell.getParagraphs().get(0).getRuns() != null && cell.getParagraphs().get(0).getRuns().size() > 0) {
                                            XWPFRun cellR = xwpfTableCell.getParagraphs().get(0).createRun();
                                            cellR.setText(cell.getText());
                                            cellR.setBold(cell.getParagraphs().get(0).getRuns().get(0).isBold());
                                        } else {
                                            xwpfTableCell.setText(cell.getText());
                                        }
                                    } else {
                                        xwpfTableCell.setText(cell.getText());
                                    }
                                    List<XWPFParagraph> paras = xwpfTableCell.getParagraphs();
                                    for (XWPFParagraph para : paras) {
                                        replaceInPara(para, orderFormat);
                                    }
                                }

                                i1++;
                            }
                            xwpfTable.removeRow(j);
                            break;
                        case "pay":
                            List<PaymentDetailResult> payList = (List<PaymentDetailResult>) params.get(rule);
                            int i2 = 1;
                            for (PaymentDetailResult paymentDetailResult : payList) {
                                XWPFTableRow xwpfTableRow = xwpfTable.insertNewTableRow(j + i2);  //新行

                                Map<String, Object> orderFormat = ContractExcel.payFormat(paymentDetailResult);

                                List<XWPFTableCell> cells = tableRow.getTableCells();
                                for (XWPFTableCell cell : cells) {          //段落

                                    XWPFTableCell xwpfTableCell = xwpfTableRow.addNewTableCell();
                                    //列属性
                                    xwpfTableCell.getCTTc().setTcPr(cell.getCTTc().getTcPr());
                                    //段落属性
                                    if (cell.getParagraphs() != null && cell.getParagraphs().size() > 0) {
                                        xwpfTableCell.getParagraphs().get(0).getCTP().setPPr(cell.getParagraphs().get(0).getCTP().getPPr());
                                        if (cell.getParagraphs().get(0).getRuns() != null && cell.getParagraphs().get(0).getRuns().size() > 0) {
                                            XWPFRun cellR = xwpfTableCell.getParagraphs().get(0).createRun();
                                            cellR.setText(cell.getText());
                                            cellR.setBold(cell.getParagraphs().get(0).getRuns().get(0).isBold());
                                        } else {
                                            xwpfTableCell.setText(cell.getText());
                                        }
                                    } else {
                                        xwpfTableCell.setText(cell.getText());
                                    }

                                    List<XWPFParagraph> paras = xwpfTableCell.getParagraphs();
                                    for (XWPFParagraph para : paras) {
                                        replaceInPara(para, orderFormat);
                                    }
                                }

                                i2++;
                            }
                            break;
                    }
                }

            }
            replaceInTable(tables.get(i), params);

        }

//        for (int index : tabIndex) {
//            replaceInTable(tables.get(index), params);
//        }
    }

    public String getRule(int table, int tr, List<TempReplaceRule.ReplaceRule> replaceRules) {
        for (TempReplaceRule.ReplaceRule replaceRule : replaceRules) {
            if (replaceRule.getTableIndex().equals(table) && replaceRule.getTrIndex().equals(tr)) {
                return replaceRule.getType();
            }
        }
        return null;
    }

}
