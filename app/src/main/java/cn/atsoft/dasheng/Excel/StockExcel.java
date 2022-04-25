package cn.atsoft.dasheng.Excel;

import cn.atsoft.dasheng.Excel.pojo.StockDetailExcel;
import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.entity.Storehouse;
import cn.atsoft.dasheng.app.model.result.StockDetailsResult;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.app.service.StockService;
import cn.atsoft.dasheng.app.service.StorehouseService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.StorehousePositions;
import cn.atsoft.dasheng.erp.model.params.SkuJson;
import cn.atsoft.dasheng.erp.model.params.SkuParam;
import cn.atsoft.dasheng.erp.model.params.StorehousePositionsParam;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.erp.service.InkindService;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsService;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/stockExcel")

public class StockExcel {
    @Autowired
    private StockService stockService;

    @Autowired
    private StockDetailsService stockDetailsService;

    @Autowired
    private InkindService inkindService;

    @Autowired
    private SkuService skuService;

    @Autowired
    private StorehousePositionsService storehousePositionsService;


    @Autowired
    private StorehouseService storehouseService;
    /**
     * 库存Excel导出
     */


    @RequestMapping(value = "/stockExport", method = RequestMethod.GET)
    @ApiOperation("导出")
    public void stockExport(HttpServletResponse response, Long type, String url) throws IOException {
         List<Storehouse> storehouses = storehouseService.list();
         List<StorehousePositionsResult> storehousePositionsList = storehousePositionsService.findListBySpec(new StorehousePositionsParam(),null);
        String title = "库存EXCEL";
        String[] header = {"分类","物料编码", "名称","型号","规格","库位","数量","单位"};


        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("库存EXCEL");
        sheet.setDefaultColumnWidth(40);
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, 5);
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
        List<StockDetailExcel> stockDetail = stockDetailsService.getStockDetail();


        int i = 1;

        for (StockDetailExcel detail : stockDetail) {
            i++;
            HSSFRow row1 = sheet.createRow(i);

            HSSFCell spuClass = row1.createCell(0);
            HSSFCell coding = row1.createCell(1);
            HSSFCell spuName = row1.createCell(2);
            HSSFCell skuName = row1.createCell(3);
            HSSFCell attribute = row1.createCell(4);
            HSSFCell storeHousePositionName = row1.createCell(5);
            HSSFCell num = row1.createCell(6);
            HSSFCell unit = row1.createCell(7);
            unit.setCellValue(detail.getSkuResult().getSpuResult().getUnitResult().getUnitName());
            spuClass.setCellValue(new HSSFRichTextString(detail.getSkuResult().getSpuResult().getSpuClassificationResult().getName()));
            if (ToolUtil.isNotEmpty(detail.getSkuResult())) {
                coding.setCellValue(new HSSFRichTextString(detail.getSkuResult().getStandard()));
                spuName.setCellValue(new HSSFRichTextString(detail.getSkuResult().getSpuResult().getName()));
                skuName.setCellValue(new HSSFRichTextString(detail.getSkuResult().getSkuName()));
                StringBuffer sb = new StringBuffer();
                for (SkuJson skuJson : detail.getSkuResult().getSkuJsons()) {
                    sb.append(skuJson.getAttribute().getAttribute()).append(":").append(skuJson.getValues().getAttributeValues()).append(",");
                }
                if(sb.length()>1){
                    attribute.setCellValue(new HSSFRichTextString(sb.substring(0, sb.length() - 1)));
                }
            }
            if(ToolUtil.isNotEmpty(detail.getStorehousePositionsResult()) && ToolUtil.isNotEmpty(detail.getStorehousePositionsResult().getStorehousePositionsId())){
                storeHousePositionName.setCellValue( new HSSFRichTextString(this.getParent(detail.getStorehousePositionsResult(),storehouses,storehousePositionsList)));
            }
            num.setCellValue(detail.getNumber());


        }

        //准备将Excel的输出流通过response输出到页面下载
        //八进制输出流
        response.setContentType("application/octet-stream");

        //这后面可以设置导出Excel的名称
        response.setHeader("Content-disposition", "attachment;filename=stock.xls");

        //刷新缓冲
        response.flushBuffer();

        //workbook将Excel写入到response的输出流中，供页面下载
        workbook.write(response.getOutputStream());
//        System.out.println(workbook.write(response.getOutputStream()));
    }

    private String getParent(StorehousePositionsResult positions,List<Storehouse> storehouses, List<StorehousePositionsResult> storehousePositionsList) {

        StringBuffer stringBuffer = this.formatParentStringBuffer(positions, storehousePositionsList, new StringBuffer());
//
        for (Storehouse storehouse : storehouses) {
            if(positions.getStorehouseId().equals(storehouse.getStorehouseId())){
                stringBuffer = new StringBuffer().append(storehouse.getName()).append("/").append(stringBuffer);
                break;
            }
        }
        return stringBuffer.toString();
    }

    private StringBuffer formatParentStringBuffer(StorehousePositionsResult positions, List<StorehousePositionsResult> storehousePositionsList, StringBuffer stringBuffer) {
        if (!positions.getPid().equals(0L)) {
            for (StorehousePositionsResult storehousePositions : storehousePositionsList) {
                if (positions.getPid().equals(storehousePositions.getStorehousePositionsId())) {
                    StringBuffer now = new StringBuffer().append(positions.getName()).append("/").append(stringBuffer);
                    stringBuffer = now;
                    stringBuffer = this.formatParentStringBuffer(storehousePositions, storehousePositionsList, stringBuffer);
                }

            }
        } else {
            StringBuffer now = new StringBuffer().append(positions.getName()).append("/").append(stringBuffer);
            stringBuffer = now;
        }
        return stringBuffer;
    }
}
