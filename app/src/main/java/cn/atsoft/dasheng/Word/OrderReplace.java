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
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;
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

        XWPFTable table = doc.getTableArray(0);
        for (int i = 0; i < table.getRows().size(); i++) {
            XWPFTableRow row = table.getRow(i);
        }


    }


    public void copy(XWPFTable table, XWPFTableRow sourceRow, int rowIndex) {
        //在表格指定位置新增一行
        XWPFTableRow targetRow = table.insertNewTableRow(rowIndex);
        //复制行属性
        targetRow.getCtRow().setTrPr(sourceRow.getCtRow().getTrPr());
        List<XWPFTableCell> cellList = sourceRow.getTableCells();
        if (null == cellList) {
            return;
        }
        //复制列及其属性和内容
        XWPFTableCell targetCell = null;
        for (XWPFTableCell sourceCell : cellList) {
            targetCell = targetRow.addNewTableCell();
            //列属性
            targetCell.getCTTc().setTcPr(sourceCell.getCTTc().getTcPr());
            //段落属性
            if (sourceCell.getParagraphs() != null && sourceCell.getParagraphs().size() > 0) {
                targetCell.getParagraphs().get(0).getCTP().setPPr(sourceCell.getParagraphs().get(0).getCTP().getPPr());
                if (sourceCell.getParagraphs().get(0).getRuns() != null && sourceCell.getParagraphs().get(0).getRuns().size() > 0) {
                    XWPFRun cellR = targetCell.getParagraphs().get(0).createRun();
                    cellR.setText(sourceCell.getText());
                    cellR.setBold(sourceCell.getParagraphs().get(0).getRuns().get(0).isBold());
                } else {
                    targetCell.setText(sourceCell.getText());
                }
            } else {
                targetCell.setText(sourceCell.getText());
            }
        }
    }

}
