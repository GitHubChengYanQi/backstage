package cn.atsoft.dasheng.Excel;

import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.model.params.SkuParam;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.orCode.entity.OrCode;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.production.model.params.ProductionStationClassParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
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
@RequestMapping("/jazz")
@Api(tags = "")
public class SkuExportExcel extends BaseController {

    @Autowired
    private SkuService skuService;


    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public List<SkuResult> dasad(@RequestBody(required = false) SkuParam skuParam) {
        if (ToolUtil.isEmpty(skuParam)) {
            skuParam = new SkuParam();
        }
        List<SkuResult> listBySpec = skuService.findListBySpec(skuParam);
        return listBySpec;
    }

    @RequestMapping(value = "/skuExport", method = RequestMethod.GET)
    @ApiOperation("导出")
    public void qrCodetoExcel(HttpServletResponse response, Long type, String url) throws IOException {
        String title = "二维码导出表单";
        String[] header = {"物料编码", "分类", "产品", "型号", "单位", "是否批量", "规格"};


        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("物料导出");
        sheet.setDefaultColumnWidth(40);
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, 7);
        sheet.addMergedRegion(region);
//        sheet.setColumnWidth(0, 10);
        HSSFRow titleRow = sheet.createRow(0);
        HSSFCell ti = titleRow.createCell(0);
        ti.setCellValue(title);
        HSSFCellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        ti.setCellStyle(titleStyle);


        HSSFRow headRow = sheet.createRow(1);

        for (int i = 0; i < header.length; i++) {
            //创建一个单元格
            HSSFCell cell = headRow.createCell(i);

            //创建一个内容对象
            HSSFRichTextString text = new HSSFRichTextString(header[i]);


            HSSFCellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            //将内容对象的文字内容写入到单元格中
            cell.setCellValue(text);
            cell.setCellStyle(headerStyle);
        }


        List<SkuResult> skuResults = skuService.findListBySpec(new SkuParam());


        int i = 1;
//        for (Map<String, String> longStringMap : list) {
//
//            HSSFRow row1 = sheet.createRow(i);
//            HSSFCell id = row1.createCell(1);
//            HSSFCell url1 = row1.createCell(2);
//            HSSFRichTextString idValue = new HSSFRichTextString(longStringMap.get("id"));
//            HSSFRichTextString urlValue = new HSSFRichTextString(longStringMap.get("url"));
//            id.setCellValue(idValue);
//            url1.setCellValue(urlValue);
//            i++;
//
//        }
        for (SkuResult skuResult : skuResults) {
            i++;
            HSSFRow row1 = sheet.createRow(i);
            HSSFCell coding = row1.createCell(0);
            HSSFCell classes = row1.createCell(1);
            HSSFCell spuName = row1.createCell(2);
            HSSFCell sku = row1.createCell(3);
            HSSFCell unit = row1.createCell(4);
            HSSFCell isAll = row1.createCell(5);
            HSSFCell attributeValue = row1.createCell(6);

            HSSFRichTextString codingStr = new HSSFRichTextString(skuResult.getStandard());

            HSSFRichTextString classesStr = new HSSFRichTextString();

            HSSFRichTextString spuNameStr = ToolUtil.isEmpty(skuResult.getSpuResult()) ? new HSSFRichTextString() : new HSSFRichTextString(skuResult.getSpuResult().getName());
            HSSFRichTextString skuStr = new HSSFRichTextString();
            if (ToolUtil.isNotEmpty(skuResult.getSpuResult()) && ToolUtil.isNotEmpty(skuResult.getSpuResult().getSpuClassificationResult()) && ToolUtil.isNotEmpty(skuResult.getSpuResult().getSpuClassificationResult().getName())) {
                skuStr = new HSSFRichTextString(skuResult.getSpuResult().getSpuClassificationResult().getName());
            }
            HSSFRichTextString unitStr = ToolUtil.isEmpty(skuResult.getUnit()) || ToolUtil.isEmpty(skuResult.getUnit().getUnitName()) ? new HSSFRichTextString(" ") : new HSSFRichTextString(skuResult.getUnit().getUnitName());
            HSSFRichTextString isAllStr = new HSSFRichTextString();
            if (ToolUtil.isNotEmpty(skuResult.getBatch()) && skuResult.getBatch().equals(1)) {
                isAllStr = new HSSFRichTextString("是");
            } else {
                isAllStr = new HSSFRichTextString("否");
            }
            HSSFRichTextString attributeValueStr = new HSSFRichTextString();
            if (ToolUtil.isNotEmpty(skuResult.getSkuJsons()) && ToolUtil.isNotEmpty(skuResult.getSkuJsons().get(0)) && ToolUtil.isNotEmpty(skuResult.getSkuJsons().get(0).getValues()) && ToolUtil.isNotEmpty(skuResult.getSkuJsons().get(0).getValues().getAttributeValues())) {
                attributeValueStr = new HSSFRichTextString(skuResult.getSkuJsons().get(0).getValues().getAttributeValues());
            }

            coding.setCellValue(codingStr);
            classes.setCellValue(classesStr);
            spuName.setCellValue(spuNameStr);
            sku.setCellValue(skuStr);
            unit.setCellValue(unitStr);
            isAll.setCellValue(isAllStr);
            attributeValue.setCellValue(attributeValueStr);
        }

        //准备将Excel的输出流通过response输出到页面下载
        //八进制输出流
        response.setContentType("application/octet-stream");

        //这后面可以设置导出Excel的名称
        response.setHeader("Content-disposition", "attachment;filename=qrCode.xls");

        //刷新缓冲
        response.flushBuffer();

        //workbook将Excel写入到response的输出流中，供页面下载
        workbook.write(response.getOutputStream());
//        System.out.println(workbook.write(response.getOutputStream())); b6
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
