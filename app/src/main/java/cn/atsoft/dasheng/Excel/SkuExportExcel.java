package cn.atsoft.dasheng.Excel;

import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.model.params.SkuJson;
import cn.atsoft.dasheng.erp.model.params.SkuParam;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.SpuClassificationService;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.orCode.entity.OrCode;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.production.model.params.ProductionStationClassParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/skuExcel")
@Api(tags = "")
public class SkuExportExcel extends BaseController {

    @Autowired
    private SkuService skuService;

    @Autowired
    private SkuExcelService skuExcelService;

    @RequestMapping(value = "/skuExport", method = RequestMethod.GET)
    @ApiOperation("导出")
    public void qrCodetoExcel(HttpServletResponse response, Long type, String url) throws IOException {
        List<SkuResult> skuResults = skuService.findListBySpec(new SkuParam());
        String[] header = {"物料编码", "分类", "产品", "型号", "单位", "是否批量","规格","物料描述"};



        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = workbook.createSheet("物料导出");
        HSSFSheet sheet = skuExcelService.dataEffective(hssfSheet,skuResults.size());

        sheet.setDefaultColumnWidth(30);
//        sheet.setColumnWidth(7,50);



        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//上下居中
        cellStyle.setAlignment(HorizontalAlignment.LEFT);//居左对齐
        cellStyle.setWrapText(true);//自动换行

        //设置边框样式
        cellStyle.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle.setBorderLeft(BorderStyle.MEDIUM);
        cellStyle.setBorderRight(BorderStyle.MEDIUM);
        cellStyle.setBorderTop(BorderStyle.MEDIUM);

        HSSFRow headRow = sheet.createRow(0);

        for (int i = 0; i < header.length; i++) {
            //创建一个单元格
            HSSFCell cell = headRow.createCell(i);

            //创建一个内容对象
            HSSFRichTextString text = new HSSFRichTextString(header[i]);
            //样式
            HSSFCellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.MEDIUM);
            headerStyle.setBorderLeft(BorderStyle.MEDIUM);
            headerStyle.setBorderRight(BorderStyle.MEDIUM);
            headerStyle.setBorderTop(BorderStyle.MEDIUM);

            //将内容对象的文字内容写入到单元格中
            cell.setCellValue(text);
            cell.setCellStyle(headerStyle);
        }

        int i = 0;

        for (SkuResult skuResult : skuResults) {
            i++;

            HSSFRow row1 = sheet.createRow(i);
            HSSFCell coding = row1.createCell(0);
            coding.setCellStyle(cellStyle);
            HSSFCell classes = row1.createCell(1);
            classes.setCellStyle(cellStyle);
            HSSFCell spuName = row1.createCell(2);
            spuName.setCellStyle(cellStyle);
            HSSFCell sku = row1.createCell(3);
            sku.setCellStyle(cellStyle);
            HSSFCell unit = row1.createCell(4);
            unit.setCellStyle(cellStyle);
            HSSFCell isAll = row1.createCell(5);
            isAll.setCellStyle(cellStyle);
            HSSFCell specifications = row1.createCell(6);
            specifications.setCellStyle(cellStyle);
            HSSFCell attributeAndValues = row1.createCell(7);
            attributeAndValues.setCellStyle(cellStyle);

            HSSFRichTextString codingStr = new HSSFRichTextString(skuResult.getStandard());

            HSSFRichTextString classesStr = new HSSFRichTextString(skuResult.getSpuResult().getSpuClassificationResult().getName());

            HSSFRichTextString spuNameStr = ToolUtil.isEmpty(skuResult.getSpuResult()) ? new HSSFRichTextString() : new HSSFRichTextString(skuResult.getSpuResult().getName());
            HSSFRichTextString skuStr = new HSSFRichTextString();
            if (ToolUtil.isNotEmpty(skuResult) && ToolUtil.isNotEmpty(skuResult.getSkuName())) {
                skuStr = new HSSFRichTextString(skuResult.getSkuName());
            }
            HSSFRichTextString unitStr = ToolUtil.isEmpty(skuResult.getSpuResult()) || ToolUtil.isEmpty(skuResult.getSpuResult().getUnitResult())|| ToolUtil.isEmpty(skuResult.getSpuResult().getUnitResult().getUnitName()) ? new HSSFRichTextString(" ") : new HSSFRichTextString(skuResult.getSpuResult().getUnitResult().getUnitName());
            HSSFRichTextString isAllStr = new HSSFRichTextString();
            if (ToolUtil.isNotEmpty(skuResult.getBatch()) && skuResult.getBatch().equals(1)) {
                isAllStr = new HSSFRichTextString("是");
            } else {
                isAllStr = new HSSFRichTextString("否");
            }
            HSSFRichTextString specificationsStr = new HSSFRichTextString();
            if (ToolUtil.isNotEmpty(skuResult.getSpecifications())) {
                specificationsStr = new HSSFRichTextString(skuResult.getSpecifications());
            }
            String attributeAndValuesStr = "";
            StringBuffer sb = new StringBuffer();
            if(ToolUtil.isNotEmpty(skuResult.getSkuJsons())){
                for (SkuJson skuJson : skuResult.getSkuJsons()) {
                    sb.append(skuJson.getAttribute().getAttribute()).append(":").append(skuJson.getValues().getAttributeValues()).append(",");
                }
            }
            if(sb.length()>1){
                attributeAndValuesStr = sb.substring(0, sb.length() - 1);
            }

            coding.setCellValue(codingStr);
            classes.setCellValue(classesStr);
            spuName.setCellValue(spuNameStr);
            sku.setCellValue(skuStr);
            unit.setCellValue(unitStr);
            isAll.setCellValue(isAllStr);
            specifications.setCellValue(specificationsStr);
            attributeAndValues.setCellValue(attributeAndValuesStr);
        }

        //准备将Excel的输出流通过response输出到页面下载
        //八进制输出流
        response.setContentType("application/octet-stream");

        //这后面可以设置导出Excel的名称
        response.setHeader("Content-disposition", "attachment;filename=skuExport.xls");

        //刷新缓冲
        response.flushBuffer();

        //workbook将Excel写入到response的输出流中，供页面下载
        workbook.write(response.getOutputStream());
    }

    @RequestMapping(value = "/exportTemplate", method = RequestMethod.GET)
    @ApiOperation("导出")
    public File jarExcel(HttpServletResponse response) throws IOException {
//        Resource res = new ClassPathResource("static/sku.xlsx");
//        InputStream stream = res.getInputStream();
//        int read = stream.read();


        return null;
    }
}
