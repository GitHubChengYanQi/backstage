package cn.atsoft.dasheng.Word;


import cn.atsoft.dasheng.Excel.ContractExcel;
import cn.atsoft.dasheng.Excel.WordUtils;
import cn.atsoft.dasheng.Excel.pojo.LabelResult;
import cn.atsoft.dasheng.Excel.pojo.TempReplaceRule;
import cn.atsoft.dasheng.app.entity.Contract;
import cn.atsoft.dasheng.app.entity.Template;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.model.result.OrderDetailResult;
import cn.atsoft.dasheng.crm.model.result.PaymentDetailResult;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.modular.system.entity.FileInfo;
import cn.atsoft.dasheng.sys.modular.system.service.FileInfoService;
import com.alibaba.fastjson.JSON;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/Word")
public class OrderReplace {
    @Autowired
    private FileInfoService fileInfoService;

    @RequestMapping(value = "/exportWord", method = RequestMethod.GET)
    public void exportContract(HttpServletResponse response, Long id) {


        FileInfo fileInfo = fileInfoService.getById(id);
        if (ToolUtil.isEmpty(fileInfo)) {
            throw new ServiceException(500, "请确定合同模板");
        }
        try {
            XWPFDocument document = formatDocument(fileInfo.getFilePath());  //读取word

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

    public XWPFDocument formatDocument(String url) throws InvalidFormatException, IOException {

        InputStream inputStream = new FileInputStream(url);
        XWPFDocument document = new XWPFDocument(inputStream);
        replaceInTable(document);

        return document;

    }

    public void replaceInTable(XWPFDocument doc) {


//        for (int i = 0; i < doc.getTables().size(); i++) {

        XWPFTable xwpfTable = doc.getTableArray(0);   //表

        int size = xwpfTable.getRows().size();
        for (int i = 0; i < xwpfTable.getRows().size(); i++) {   //表格里循环 行
            if (i == size) {
                break;
            }

            XWPFTableRow row = xwpfTable.getRow(i);         //获取当期行
            XWPFTableRow newRow = xwpfTable.createRow();        //创建新行


            for (XWPFTableCell tableCell : row.getTableCells()) {
                String text = tableCell.getText();

                XWPFTableCell cell = newRow.addNewTableCell();  //创建新列
//                XWPFTableCell cell = newRow.createCell();

//                cell.getParagraphs().get(0).getCTP().setPPr(tableCell.getParagraphs().get(0).getCTP().getPPr());
//                if (tableCell.getParagraphs().get(0).getRuns() != null && tableCell.getParagraphs().get(0).getRuns().size() > 0) {
//                    XWPFRun cellR = cell.getParagraphs().get(0).createRun();
//                    cellR.setText(text);
//                    cellR.setBold(tableCell.getParagraphs().get(0).getRuns().get(0).isBold());
//                } else {
                cell.setText(text);
//                }
            }
        }
        setTableLocation(xwpfTable, "center");
    }

    /**
     * 设置表格位置
     *
     * @param xwpfTable
     * @param location  整个表格居中center,left居左，right居右，both两端对齐
     */
    public static void setTableLocation(XWPFTable xwpfTable, String location) {

        CTTbl cttbl = xwpfTable.getCTTbl();
        CTTblPr tblpr = cttbl.getTblPr() == null ? cttbl.addNewTblPr() : cttbl.getTblPr();
        CTJc cTJc = tblpr.addNewJc();


        cTJc.setVal(STJc.Enum.forString(location));
    }


}
