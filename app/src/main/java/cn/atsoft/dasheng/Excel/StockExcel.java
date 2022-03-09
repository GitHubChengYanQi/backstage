//package cn.atsoft.dasheng.Excel;
//
//import cn.atsoft.dasheng.app.service.StockDetailsService;
//import cn.atsoft.dasheng.app.service.StockService;
//import cn.atsoft.dasheng.core.util.ToolUtil;
//import cn.atsoft.dasheng.erp.model.params.SkuParam;
//import cn.atsoft.dasheng.erp.model.result.SkuResult;
//import cn.atsoft.dasheng.erp.service.InkindService;
//import cn.atsoft.dasheng.erp.service.SkuService;
//import io.swagger.annotations.ApiOperation;
//import org.apache.poi.hssf.usermodel.*;
//import org.apache.poi.ss.usermodel.FillPatternType;
//import org.apache.poi.ss.usermodel.HorizontalAlignment;
//import org.apache.poi.ss.usermodel.IndexedColors;
//import org.apache.poi.ss.usermodel.VerticalAlignment;
//import org.apache.poi.ss.util.CellRangeAddress;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.List;
//
//@Controller
//@RequestMapping("/stockExcel")
//
//public class StockExcel {
//    @Autowired
//    private StockService stockService;
//
//    @Autowired
//    private StockDetailsService stockDetailsService;
//
//    @Autowired
//    private InkindService inkindService;
//
//    @Autowired
//    private SkuService skuService;
//    /**
//     * 库存Excel导出
//     */
//    @RequestMapping(value = "/stockExport", method = RequestMethod.GET)
//    @ApiOperation("导出")
//    public void stockExport(HttpServletResponse response, Long type, String url) throws IOException {
//        String title = "库存EXCEL";
//        String[] header = {"物料编码", "分类", "产品", "型号", "单位", "是否批量", "规格"};
//
//
//        HSSFWorkbook workbook = new HSSFWorkbook();
//        HSSFSheet sheet = workbook.createSheet("库存EXCEL");
//        sheet.setDefaultColumnWidth(40);
//        CellRangeAddress region = new CellRangeAddress(0, 0, 0, 7);
//        sheet.addMergedRegion(region);
////        sheet.setColumnWidth(0, 10);
//        HSSFRow titleRow = sheet.createRow(0);
//        HSSFCell ti = titleRow.createCell(0);
//        ti.setCellValue(title);
//        HSSFCellStyle titleStyle = workbook.createCellStyle();
//        titleStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
//        titleStyle.setAlignment(HorizontalAlignment.CENTER);
//        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
//        ti.setCellStyle(titleStyle);
//
//
//        HSSFRow headrow = sheet.createRow(1);
//
//        for (int i = 0; i < header.length; i++) {
//            //创建一个单元格
//            HSSFCell cell = headrow.createCell(i);
//
//            //创建一个内容对象
//            HSSFRichTextString text = new HSSFRichTextString(header[i]);
//
//
//            HSSFCellStyle headerStyle = workbook.createCellStyle();
//            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
//            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//
//            //将内容对象的文字内容写入到单元格中
//            cell.setCellValue(text);
//            cell.setCellStyle(headerStyle);
//        }
//
//
//
//        int i = 1;
//
//        for (SkuResult skuResult : skuResults) {
//            i++;
//            HSSFRow row1 = sheet.createRow(i);
//            HSSFCell coding = row1.createCell(0);
//            HSSFCell classes = row1.createCell(1);
//            HSSFCell spuName = row1.createCell(2);
//            HSSFCell sku = row1.createCell(3);
//            HSSFCell unit = row1.createCell(4);
//            HSSFCell isAll = row1.createCell(5);
//            HSSFCell attributeValue = row1.createCell(6);
//        }
//
//        //准备将Excel的输出流通过response输出到页面下载
//        //八进制输出流
//        response.setContentType("application/octet-stream");
//
//        //这后面可以设置导出Excel的名称
//        response.setHeader("Content-disposition", "attachment;filename=qrCode.xls");
//
//        //刷新缓冲
//        response.flushBuffer();
//
//        //workbook将Excel写入到response的输出流中，供页面下载
//        workbook.write(response.getOutputStream());
////        System.out.println(workbook.write(response.getOutputStream()));
//    }
//}
