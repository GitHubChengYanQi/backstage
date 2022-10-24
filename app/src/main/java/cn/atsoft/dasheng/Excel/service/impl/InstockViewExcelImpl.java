package cn.atsoft.dasheng.Excel.service.impl;

import cn.atsoft.dasheng.Excel.service.InstockViewExcel;
import cn.atsoft.dasheng.app.model.request.InstockView;
import cn.atsoft.dasheng.base.consts.ConstantsContext;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.InstockLogDetail;
import cn.atsoft.dasheng.erp.entity.InstockOrder;
import cn.atsoft.dasheng.erp.model.params.DataStatisticsViewParam;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.service.InstockOrderService;
import cn.atsoft.dasheng.erp.service.impl.OrderUpload;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.net.URLEncodeUtil;
import me.chanjar.weixin.common.api.WxConsts;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;

@Service
public class InstockViewExcelImpl implements InstockViewExcel {
    @Autowired
    private InstockOrderService instockOrderService;

    @Autowired
    private OrderUpload orderUpload;

    @Override
    public void excel(HttpServletResponse response, DataStatisticsViewParam param) throws IOException {
        List<InstockView> instockViews = instockOrderService.instockViewExcel(param);
        XSSFWorkbook workbook = new XSSFWorkbook();

        sheet1(workbook, instockViews);
        sheet2(workbook, instockViews);
        sheet3(workbook, instockViews);
        sheet4(workbook, instockViews);
        sheet5(workbook, instockViews);


        OutputStream os = response.getOutputStream();
        workbook.write(os);





        String uploadPath = ConstantsContext.getFileUploadPath();  //读取系统文件路径位置
        uploadPath = uploadPath.replace("\\", "");
        String filePath =uploadPath+"入库统计" + DateUtil.today() + ".xlsx";
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        workbook.write(bao);
//        InputStream inputStream =  new ByteArrayInputStream(bao.toByteArray());
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        fileOutputStream.write(bao.toByteArray());
        File file = new File(filePath);
//        orderUpload.upload(WxConsts.KefuMsgType.FILE,"xlsx",inputStream);
        orderUpload.upload(file);
        file.deleteOnExit();
        fileOutputStream.close();
        bao.close();
    }


    private XSSFCellStyle headerStyle(XSSFWorkbook workbook) {
        //头样式
        XSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.MEDIUM);
        headerStyle.setBorderLeft(BorderStyle.MEDIUM);
        headerStyle.setBorderRight(BorderStyle.MEDIUM);
        headerStyle.setBorderTop(BorderStyle.MEDIUM);
        return headerStyle;
    }


    private XSSFSheet createSheet(XSSFWorkbook workbook, String name) {
        XSSFSheet sheet = workbook.createSheet(name);

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//上下居中
        cellStyle.setAlignment(HorizontalAlignment.LEFT);//居左对齐
        cellStyle.setWrapText(true);//自动换行

        //设置边框样式
        cellStyle.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle.setBorderLeft(BorderStyle.MEDIUM);
        cellStyle.setBorderRight(BorderStyle.MEDIUM);
        cellStyle.setBorderTop(BorderStyle.MEDIUM);
        return sheet;
    }

    private void sheet1(XSSFWorkbook workbook, List<InstockView> instockViews) {
        String[] header = {"物料编码", "分类", "物料描述", "数量", "供应商", "状态", "时间", "执行人", "执行人", "单据", "单据编号"};


        XSSFSheet sheet = createSheet(workbook, "供应商明细表");

        XSSFRow headRow = sheet.createRow(0);

        for (int i = 0; i < header.length; i++) {
            //创建一个单元格
            XSSFCell cell = headRow.createCell(i);

            //创建一个内容对象
            XSSFRichTextString text = new XSSFRichTextString(header[i]);
            //将内容对象的文字内容写入到单元格中
            cell.setCellValue(text);
            cell.setCellStyle(headerStyle(workbook));
        }

        int rowNum = 1;
        for (InstockView instockView : instockViews) {
            for (InstockOrder instockOrder : instockView.getInstockOrders()) {
                if (instockView.getCustomerId().equals(instockOrder.getCustomerId())) {
                    for (InstockLogDetailResult instockLogDetail : instockView.getInstockLogDetails()) {
                        if (instockLogDetail.getInstockOrderId().equals(instockOrder.getInstockOrderId())) {
                            XSSFRow row = sheet.createRow(rowNum);
                            rowNum += 1;
                            row.createCell(0).setCellValue(instockLogDetail.getSkuResult().getStandard());
                            row.createCell(1).setCellValue(instockLogDetail.getSkuResult().getSpuResult().getSpuClassificationResult().getName());
                            row.createCell(2).setCellValue(skuMessage(instockLogDetail.getSkuResult()));
                            row.createCell(3).setCellValue(instockLogDetail.getNumber());
                            row.createCell(4).setCellValue(instockView.getCustomerName());
                            row.createCell(5).setCellValue(instockLogDetail.getBrandId() == null ? "无品牌" : instockLogDetail.getBrandId().equals(0L) ? "无品牌" : instockLogDetail.getBrandResult().getBrandName());
                            row.createCell(6).setCellValue("已入库");
                            row.createCell(7).setCellValue(instockLogDetail.getCreateTime());
                            row.createCell(8).setCellValue(instockLogDetail.getUser().getName());
                            row.createCell(9).setCellValue(instockOrder.getTheme() == null ? "" : instockOrder.getTheme());
                            row.createCell(10).setCellValue(instockOrder.getCoding());
                        }
                    }
                    for (AnomalyResult anomalyResult : instockView.getErrorList()) {
                        if (anomalyResult.getFormId().equals(instockOrder.getInstockOrderId())) {
                            XSSFRow row = sheet.createRow(rowNum);
                            rowNum += 1;
                            row.createCell(0).setCellValue(anomalyResult.getSkuResult().getStandard());
                            row.createCell(1).setCellValue(anomalyResult.getSkuResult().getSpuResult().getSpuClassificationResult().getName());
                            row.createCell(2).setCellValue(skuMessage(anomalyResult.getSkuResult()));
                            row.createCell(3).setCellValue(anomalyResult.getInstockNumber());
                            row.createCell(4).setCellValue(instockView.getCustomerName());
                            row.createCell(5).setCellValue(anomalyResult.getBrandId() == null ? "无品牌" : anomalyResult.getBrandId().equals(0L) ? "无品牌" : anomalyResult.getBrand().getBrandName());

                            row.createCell(6).setCellValue("终止入库");
                            row.createCell(7).setCellValue(anomalyResult.getCreateTime());
                            row.createCell(8).setCellValue(anomalyResult.getUser().getName());
                            row.createCell(9).setCellValue(instockOrder.getTheme() == null ? "" : instockOrder.getTheme());
                            row.createCell(10).setCellValue(instockOrder.getCoding());
                        }
                    }
                }
            }
        }
    }

    private void sheet2(XSSFWorkbook workbook, List<InstockView> instockViews) {
        XSSFSheet sheet2 = createSheet(workbook, "汇总表");
        String[] header2 = {"供应商", "收货总数", "入库总数", "退货总数",};

        XSSFRow headRow2 = sheet2.createRow(0);

        for (int i = 0; i < header2.length; i++) {
            //创建一个单元格
            XSSFCell cell = headRow2.createCell(i);

            //创建一个内容对象
            XSSFRichTextString text = new XSSFRichTextString(header2[i]);
            //将内容对象的文字内容写入到单元格中
            cell.setCellValue(text);
            cell.setCellStyle(headerStyle(workbook));
        }

        int rowNum2 = 1;
        int cell2Sum = 0;
        int cell3Sum = 0;
        int cell4Sum = 0;
        for (InstockView instockView : instockViews) {
            XSSFRow row = sheet2.createRow(rowNum2);
            rowNum2 += 1;
            cell2Sum += instockView.getDetailNumberCount();
            cell3Sum += instockView.getLogNumberCount();
            cell4Sum += instockView.getErrorNumberCount();
            row.createCell(0).setCellValue(instockView.getCustomerName());
            row.createCell(1).setCellValue(instockView.getDetailNumberCount());
            row.createCell(2).setCellValue(instockView.getLogNumberCount());
            row.createCell(3).setCellValue(instockView.getErrorNumberCount());
        }
        XSSFRow row = sheet2.createRow(rowNum2);
        row.createCell(0).setCellValue("合计");
        row.createCell(1).setCellValue(cell2Sum);
        row.createCell(2).setCellValue(cell3Sum);
        row.createCell(3).setCellValue(cell4Sum);
    }

    private void sheet3(XSSFWorkbook workbook, List<InstockView> instockViews) {
        XSSFSheet sheet2 = createSheet(workbook, "物料明细");
        String[] header2 = {"物料编码", "分类", "物料描述", "数量", "品牌", "时间", "执行人", "单据", "单据编号"};

        XSSFRow headRow2 = sheet2.createRow(0);

        for (int i = 0; i < header2.length; i++) {
            //创建一个单元格
            XSSFCell cell = headRow2.createCell(i);

            //创建一个内容对象
            XSSFRichTextString text = new XSSFRichTextString(header2[i]);
            //将内容对象的文字内容写入到单元格中
            cell.setCellValue(text);
            cell.setCellStyle(headerStyle(workbook));
        }

        int rowNum2 = 1;

        for (InstockView instockView : instockViews) {
            for (InstockOrder instockOrder : instockView.getInstockOrders()) {
                if (instockView.getCustomerId().equals(instockOrder.getCustomerId())) {
                    for (InstockListResult instockList : instockView.getInstockLists()) {
                        if (instockList.getInstockOrderId().equals(instockOrder.getInstockOrderId())) {
                            XSSFRow row = sheet2.createRow(rowNum2);
                            rowNum2 += 1;
                            row.createCell(0).setCellValue(instockList.getSkuResult().getCoding());
                            row.createCell(1).setCellValue(instockList.getSkuResult().getSpuResult().getSpuClassificationResult().getName());
                            row.createCell(2).setCellValue(skuMessage(BeanUtil.copyProperties(instockList.getSkuResult(), SkuSimpleResult.class)));
                            row.createCell(3).setCellValue(instockList.getNumber());
                            row.createCell(4).setCellValue(instockList.getBrandId() == null ? "无品牌" : instockList.getBrandId().equals(0L) ? "无品牌" : instockList.getBrandResult().getBrandName());
                            row.createCell(5).setCellValue(instockList.getCreateTime());
                            row.createCell(6).setCellValue(instockList.getCreateUser());
                            row.createCell(7).setCellValue(instockOrder.getTheme() == null ? "" :instockOrder.getTheme());
                            row.createCell(8).setCellValue(instockOrder.getCoding());
                        }

                    }
                }
            }
        }
        XSSFRow row = sheet2.createRow(rowNum2);
        row.createCell(1).setCellValue("合计");

    }

    private void sheet4(XSSFWorkbook workbook, List<InstockView> instockViews) {
        XSSFSheet sheet2 = createSheet(workbook, "入库明细");
        String[] header2 = {"物料编码", "分类", "物料描述", "数量", "品牌", "时间", "执行人", "单据", "单据编号"};

        XSSFRow headRow2 = sheet2.createRow(0);

        for (int i = 0; i < header2.length; i++) {
            //创建一个单元格
            XSSFCell cell = headRow2.createCell(i);

            //创建一个内容对象
            XSSFRichTextString text = new XSSFRichTextString(header2[i]);
            //将内容对象的文字内容写入到单元格中
            cell.setCellValue(text);
            cell.setCellStyle(headerStyle(workbook));
        }

        int rowNum2 = 1;
        for (InstockView instockView : instockViews) {
            for (InstockOrder instockOrder : instockView.getInstockOrders()) {
                if (instockView.getCustomerId().equals(instockOrder.getCustomerId())) {
                    for (InstockLogDetailResult instockList : instockView.getInstockLogDetails()) {
                        if (instockList.getInstockOrderId().equals(instockOrder.getInstockOrderId())) {
                            XSSFRow row = sheet2.createRow(rowNum2);
                            rowNum2 += 1;
                            row.createCell(0).setCellValue(instockList.getSkuResult().getStandard());
                            row.createCell(1).setCellValue(instockList.getSkuResult().getSpuResult().getSpuClassificationResult().getName());
                            row.createCell(2).setCellValue(skuMessage(BeanUtil.copyProperties(instockList.getSkuResult(), SkuSimpleResult.class)));
                            row.createCell(3).setCellValue(instockList.getNumber());
                            row.createCell(4).setCellValue(instockList.getBrandId() == null ? "无品牌" : instockList.getBrandId().equals(0L) ? "无品牌" : instockList.getBrandResult().getBrandName());
                            row.createCell(5).setCellValue(instockList.getCreateTime());
                            row.createCell(6).setCellValue(instockList.getCreateUser());
                            row.createCell(7).setCellValue(instockOrder.getTheme() == null ? "" :instockOrder.getTheme());
                            row.createCell(8).setCellValue(instockOrder.getCoding());
                        }

                    }
                }
            }
        }
        XSSFRow row = sheet2.createRow(rowNum2);
        row.createCell(1).setCellValue("合计");
    }

    private void sheet5(XSSFWorkbook workbook, List<InstockView> instockViews) {
        XSSFSheet sheet2 = createSheet(workbook, "退货明细");
        String[] header2 = {"物料编码", "分类", "物料描述", "数量", "品牌", "时间", "执行人", "单据", "单据编号"};

        XSSFRow headRow2 = sheet2.createRow(0);

        for (int i = 0; i < header2.length; i++) {
            //创建一个单元格
            XSSFCell cell = headRow2.createCell(i);

            //创建一个内容对象
            XSSFRichTextString text = new XSSFRichTextString(header2[i]);
            //将内容对象的文字内容写入到单元格中
            cell.setCellValue(text);
            cell.setCellStyle(headerStyle(workbook));
        }

        int rowNum2 = 1;
        int cell2Sum = 0;
        int cell3Sum = 0;
        int cell4Sum = 0;
        for (InstockView instockView : instockViews) {
            for (InstockOrder instockOrder : instockView.getInstockOrders()) {
                if (instockView.getCustomerId().equals(instockOrder.getCustomerId())) {
                    for (AnomalyResult instockList : instockView.getErrorList()) {
                        if (instockList.getFormId().equals(instockOrder.getInstockOrderId())) {
                            XSSFRow row = sheet2.createRow(rowNum2);
                            rowNum2 += 1;
                            row.createCell(0).setCellValue(instockList.getSkuResult().getStandard());
                            row.createCell(1).setCellValue(instockList.getSkuResult().getSpuResult().getSpuClassificationResult().getName());
                            row.createCell(2).setCellValue(skuMessage(BeanUtil.copyProperties(instockList.getSkuResult(), SkuSimpleResult.class)));
                            row.createCell(3).setCellValue(instockList.getErrorNumber());
                            row.createCell(4).setCellValue(instockList.getBrandId() == null ? "无品牌" : instockList.getBrandId().equals(0L) ? "无品牌" : instockList.getBrand().getBrandName());
                            row.createCell(5).setCellValue(instockList.getCreateTime());
                            row.createCell(6).setCellValue(instockList.getCreateUser());
                            row.createCell(7).setCellValue(instockOrder.getTheme() == null ? "" :instockOrder.getTheme());
                            row.createCell(8).setCellValue(instockOrder.getCoding());
                        }

                    }
                }
            }
        }
        XSSFRow row = sheet2.createRow(rowNum2);
    }

    private String skuMessage(SkuSimpleResult sku) {
        String message = "";
        try {
            if (ToolUtil.isNotEmpty(sku)) {
                String skuName = sku.getSkuName();
                String spuName = sku.getSpuResult().getName();
                if (ToolUtil.isEmpty(spuName)) {
                    spuName = "";
                }
                String spe = sku.getSpecifications();
                if (ToolUtil.isNotEmpty(spe)) {
                    spe = "/" + spe;
                } else {
                    spe = "";
                }
                message = spuName + "/" + skuName + spe;
            }
        } catch (Exception e) {
        }
        return message;
    }

}
