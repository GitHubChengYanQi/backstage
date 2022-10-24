package cn.atsoft.dasheng.Excel.service.impl;

import cn.atsoft.dasheng.Excel.service.OutStockViewExcel;
import cn.atsoft.dasheng.app.model.request.InstockView;
import cn.atsoft.dasheng.app.model.request.OutStockView;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.base.consts.ConstantsContext;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.InstockOrder;
import cn.atsoft.dasheng.erp.model.params.DataStatisticsViewParam;
import cn.atsoft.dasheng.erp.model.result.AnomalyResult;
import cn.atsoft.dasheng.erp.model.result.InstockListResult;
import cn.atsoft.dasheng.erp.model.result.InstockLogDetailResult;
import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.impl.OrderUpload;
import cn.atsoft.dasheng.production.entity.ProductionPickLists;
import cn.atsoft.dasheng.production.entity.ProductionPickListsCart;
import cn.atsoft.dasheng.production.entity.ProductionPickListsDetail;
import cn.atsoft.dasheng.production.service.ProductionPickListsCartService;
import cn.atsoft.dasheng.production.service.ProductionPickListsDetailService;
import cn.atsoft.dasheng.production.service.ProductionPickListsService;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class OutStockViewExcelImpl implements OutStockViewExcel {
    @Autowired
    private ProductionPickListsService pickListsService;
    @Autowired
    private ProductionPickListsCartService cartService;
    @Autowired
    private ProductionPickListsDetailService detailService;
    @Autowired
    private OrderUpload orderUpload;
    @Autowired
    private UserService userService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private BrandService brandService;

    @Override
    public void excel(HttpServletResponse response, DataStatisticsViewParam param) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();

        sheet1(workbook,param);
        sheet2(workbook,param);
        sheet3(workbook,param);
        sheet4(workbook,param);


        OutputStream os = response.getOutputStream();
        workbook.write(os);


        String uploadPath = ConstantsContext.getFileUploadPath();  //读取系统文件路径位置
        uploadPath = uploadPath.replace("\\", "");
        String filePath = uploadPath + "出库统计" + DateUtil.today() + ".xlsx";
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

    private void sheet1(XSSFWorkbook workbook, DataStatisticsViewParam param) {
        String[] header = {"物料编码", "分类", "物料描述", "数量", "品牌", "领料人", "出库人", "时间", "单据", "任务编号"};
        List<OutStockView> pickListsView =pickListsService.outStockView(param);
        List<ProductionPickLists> pickLists =pickListsView.size() == 0 ? new ArrayList<>() : pickListsService.listByIds(pickListsView.stream().map(OutStockView::getPickListsId).collect(Collectors.toList()));
        List<ProductionPickListsCart> carts = pickLists.size() == 0 ? new ArrayList<>() : cartService.lambdaQuery().in(ProductionPickListsCart::getPickListsId, pickLists.stream().map(ProductionPickLists::getPickListsId).collect(Collectors.toList())).eq(ProductionPickListsCart::getStatus, 99).list();

        List<UserResult> userResultsByIds = pickLists.size() == 0 ? new ArrayList<>() : userService.getUserResultsByIds(pickLists.stream().map(ProductionPickLists::getUserId).collect(Collectors.toList()));
        List<SkuSimpleResult> skuResults = pickLists.size() == 0 ? new ArrayList<>() : skuService.simpleFormatSkuResult(carts.stream().map(ProductionPickListsCart::getSkuId).collect(Collectors.toList()));
        List<BrandResult> brandResults = pickLists.size() == 0 ? new ArrayList<>() : brandService.getBrandResults(carts.stream().map(ProductionPickListsCart::getBrandId).collect(Collectors.toList()));

        XSSFSheet sheet = createSheet(workbook, "物料明细表");

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

        int index = 1;

        for (ProductionPickLists pickList : pickLists) {
            int numCount = 0;
            List<Long>skuIds = new ArrayList<>();
            for (ProductionPickListsCart cart : carts) {
                if (cart.getPickListsId().equals(pickList.getPickListsId())) {
                    numCount += cart.getNumber();
                    XSSFRow row = sheet.createRow(index);
                    index += 1;
                    for (SkuSimpleResult skuResult : skuResults) {
                        if (cart.getSkuId().equals(skuResult.getSkuId())) {
                            row.createCell(0).setCellValue(skuResult.getStandard());
                            row.createCell(1).setCellValue(skuResult.getSpuResult().getSpuClassificationResult().getName());
                            row.createCell(2).setCellValue(skuMessage(skuResult));
                            skuIds.add(skuResult.getSkuId());
                            break;
                        }
                    }
                    row.createCell(3).setCellValue(cart.getNumber());
                    if (cart.getBrandId().equals(0L)) {
                        row.createCell(4).setCellValue("无品牌");
                    } else {
                        for (BrandResult brandResult : brandResults) {
                            if (brandResult.getBrandId().equals(cart.getBrandId())) {
                                row.createCell(4).setCellValue(brandResult.getBrandName());
                                break;
                            }
                        }
                    }
                    for (UserResult userResultsById : userResultsByIds) {
                        if (userResultsById.getUserId().equals(pickList.getUserId())) {
                            row.createCell(5).setCellValue(userResultsById.getName());
                        }
                        if (userResultsById.getUserId().equals(cart.getCreateUser())) {
                            row.createCell(6).setCellValue(userResultsById.getName());
                        }
                    }
                    row.createCell(7).setCellValue(DateUtil.format(cart.getCreateTime(), "yyyy-MM-dd"));
                    row.createCell(8).setCellValue(pickList.getTheme() == null ? "" : pickList.getTheme());
                    row.createCell(9).setCellValue(pickList.getCoding());
                }
            }
            if (numCount>0){
                XSSFRow row = sheet.createRow(index);
                row.createCell(0).setCellValue("合计出库" + skuIds.stream().distinct().collect(Collectors.toList()).size() + "类" + numCount + "件");
                index += 2;
            }
        }
    }

    private void sheet2(XSSFWorkbook workbook, DataStatisticsViewParam param) {
        String[] header = {"出库人", "分类", "物料描述", "数量", "品牌", "时间", "单据", "任务编号"};
        List<OutStockView> pickListsView =pickListsService.outStockView(param);
        List<ProductionPickLists> pickLists =pickListsView.size() == 0 ? new ArrayList<>() : pickListsService.listByIds(pickListsView.stream().map(OutStockView::getPickListsId).collect(Collectors.toList()));
        List<ProductionPickListsCart> carts = pickLists.size() == 0 ? new ArrayList<>() : cartService.lambdaQuery().in(ProductionPickListsCart::getPickListsId, pickLists.stream().map(ProductionPickLists::getPickListsId).collect(Collectors.toList())).eq(ProductionPickListsCart::getStatus, 99).list();

        List<UserResult> userResultsByIds = pickLists.size() == 0 ? new ArrayList<>() : userService.getUserResultsByIds(pickLists.stream().map(ProductionPickLists::getUserId).collect(Collectors.toList()));
        List<SkuSimpleResult> skuResults = pickLists.size() == 0 ? new ArrayList<>() : skuService.simpleFormatSkuResult(carts.stream().map(ProductionPickListsCart::getSkuId).collect(Collectors.toList()));
        List<BrandResult> brandResults = pickLists.size() == 0 ? new ArrayList<>() : brandService.getBrandResults(carts.stream().map(ProductionPickListsCart::getBrandId).collect(Collectors.toList()));

        XSSFSheet sheet = createSheet(workbook, "出库明细");

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

        int index = 1;
        for (UserResult userResultsById : userResultsByIds) {
            int numCount = 0;
            List<Long >skuIds= new ArrayList<>();
            for (ProductionPickLists pickList : pickLists) {
                if (userResultsById.getUserId().equals(pickList.getUserId())) {
                    for (ProductionPickListsCart cart : carts) {
                        if (cart.getPickListsId().equals(pickList.getPickListsId())) {
                            numCount += cart.getNumber();
                            XSSFRow row = sheet.createRow(index);
                            index += 1;

                            if (userResultsById.getUserId().equals(cart.getCreateUser())) {
                                row.createCell(0).setCellValue(userResultsById.getName());
                            }
                            for (SkuSimpleResult skuResult : skuResults) {
                                if (cart.getSkuId().equals(skuResult.getSkuId())) {
                                    row.createCell(1).setCellValue(skuResult.getSpuResult().getSpuClassificationResult().getName());
                                    row.createCell(2).setCellValue(skuMessage(skuResult));
                                    skuIds.add(skuResult.getSkuId());
                                    break;
                                }
                            }
                            row.createCell(3).setCellValue(cart.getNumber());
                            if (cart.getBrandId().equals(0L)) {
                                row.createCell(4).setCellValue("无品牌");
                            } else {
                                for (BrandResult brandResult : brandResults) {
                                    if (brandResult.getBrandId().equals(cart.getBrandId())) {
                                        row.createCell(4).setCellValue(brandResult.getBrandName());
                                        break;
                                    }
                                }
                            }

                            row.createCell(5).setCellValue(DateUtil.format(cart.getCreateTime(), "yyyy-MM-dd"));
                            row.createCell(6).setCellValue(pickList.getTheme() == null ? "" : pickList.getTheme());
                            row.createCell(7).setCellValue(pickList.getCoding());
                        }
                    }
                }
            }

            if (numCount>0){
                XSSFRow row = sheet.createRow(index);
                row.createCell(0).setCellValue("合计出库" + skuIds.stream().distinct().collect(Collectors.toList()).size() + "类" + numCount + "件");
                index += 2;
            }
        }

    }

    private void sheet3(XSSFWorkbook workbook, DataStatisticsViewParam param) {
        String[] header = {"出库人", "分类", "物料描述", "数量", "品牌", "时间", "单据", "任务编号"};
        List<OutStockView> pickListsView =pickListsService.outStockView(param);
        List<ProductionPickLists> pickLists =pickListsView.size() == 0 ? new ArrayList<>() : pickListsService.listByIds(pickListsView.stream().map(OutStockView::getPickListsId).collect(Collectors.toList()));
        List<ProductionPickListsCart> carts = pickLists.size() == 0 ? new ArrayList<>() : cartService.lambdaQuery().in(ProductionPickListsCart::getPickListsId, pickLists.stream().map(ProductionPickLists::getPickListsId).collect(Collectors.toList())).eq(ProductionPickListsCart::getStatus, 99).list();

        List<UserResult> userResultsByIds = pickLists.size() == 0 ? new ArrayList<>() : userService.getUserResultsByIds(pickLists.stream().map(ProductionPickLists::getUserId).collect(Collectors.toList()));
        List<SkuSimpleResult> skuResults = pickLists.size() == 0 ? new ArrayList<>() : skuService.simpleFormatSkuResult(carts.stream().map(ProductionPickListsCart::getSkuId).collect(Collectors.toList()));
        List<BrandResult> brandResults = pickLists.size() == 0 ? new ArrayList<>() : brandService.getBrandResults(carts.stream().map(ProductionPickListsCart::getBrandId).collect(Collectors.toList()));

        XSSFSheet sheet = createSheet(workbook, "领料明细");

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

        int index = 1;
        for (UserResult userResultsById : userResultsByIds) {
            int numCount = 0;
            List<Long> skuIds = new ArrayList<>();
            for (ProductionPickListsCart cart : carts) {
                if (userResultsById.getUserId().equals(cart.getCreateUser())) {
                    for (ProductionPickLists pickList : pickLists) {
                        if (cart.getPickListsId().equals(pickList.getPickListsId())) {
                            numCount += cart.getNumber();
                            XSSFRow row = sheet.createRow(index);
                            index += 1;

                            if (userResultsById.getUserId().equals(cart.getCreateUser())) {
                                row.createCell(0).setCellValue(userResultsById.getName());
                            }
                            for (SkuSimpleResult skuResult : skuResults) {
                                if (cart.getSkuId().equals(skuResult.getSkuId())) {
                                    row.createCell(1).setCellValue(skuResult.getSpuResult().getSpuClassificationResult().getName());
                                    row.createCell(2).setCellValue(skuMessage(skuResult));
                                    skuIds.add(skuResult.getSkuId());
                                    break;
                                }
                            }
                            row.createCell(3).setCellValue(cart.getNumber());
                            if (cart.getBrandId().equals(0L)) {
                                row.createCell(4).setCellValue("无品牌");
                            } else {
                                for (BrandResult brandResult : brandResults) {
                                    if (brandResult.getBrandId().equals(cart.getBrandId())) {
                                        row.createCell(4).setCellValue(brandResult.getBrandName());
                                        break;
                                    }
                                }
                            }

                            row.createCell(5).setCellValue(DateUtil.format(cart.getCreateTime(), "yyyy-MM-dd"));
                            row.createCell(6).setCellValue(pickList.getTheme() == null ? "" : pickList.getTheme());
                            row.createCell(7).setCellValue(pickList.getCoding());
                        }
                    }

                }

            }
            if (numCount>0){
                XSSFRow row = sheet.createRow(index);
                row.createCell(0).setCellValue("合计出库" + skuIds.stream().distinct().collect(Collectors.toList()).size() + "类" + numCount + "件");
                index += 2;
            }
        }


    }

    private void sheet4(XSSFWorkbook workbook, DataStatisticsViewParam param) {
        String[] header = {"人员", "出库数量", "领料数量"};
        List<OutStockView> pickListsView =pickListsService.outStockView(param);
        List<ProductionPickLists> pickLists =pickListsView.size() == 0 ? new ArrayList<>() : pickListsService.listByIds(pickListsView.stream().map(OutStockView::getPickListsId).collect(Collectors.toList()));
        List<ProductionPickListsCart> carts = pickLists.size() == 0 ? new ArrayList<>() : cartService.lambdaQuery().in(ProductionPickListsCart::getPickListsId, pickLists.stream().map(ProductionPickLists::getPickListsId).collect(Collectors.toList())).eq(ProductionPickListsCart::getStatus, 99).list();

        List<UserResult> userResultsByIds = pickLists.size() == 0 ? new ArrayList<>() :BeanUtil.copyToList( userService.lambdaQuery().eq(User::getStatus,"ENABLE").list(),UserResult.class);
        XSSFSheet sheet = createSheet(workbook, "汇总表");

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

        int index = 1;


        for (UserResult userResultsById : userResultsByIds) {
            XSSFRow row = sheet.createRow(index);
            index += 1;

            row.createCell(0).setCellValue(userResultsById.getName());
            int outNumCount = 0;
            int outSkuCount = 0;
            int pickSkuCount = 0;
            int pickNumCount = 0;
            List<Long>outSkuIds = new ArrayList<>();
            List<Long>pickSkuIds = new ArrayList<>();

            for (ProductionPickLists pickList : pickLists) {
                if (pickList.getUserId().equals(userResultsById.getUserId())) {
                    for (ProductionPickListsCart cart : carts) {
                        if (pickList.getPickListsId().equals(cart.getPickListsId())) {
                            pickSkuIds.add(cart.getSkuId());
                            pickNumCount+=cart.getNumber();
                        }
                    }
                }

            }
            for (ProductionPickListsCart cart : carts) {
                if (cart.getCreateUser().equals(userResultsById.getUserId())){
                    outSkuIds.add(cart.getSkuId());
                    outNumCount+=cart.getNumber();
                }
            }
            outSkuCount = outSkuIds.stream().distinct().collect(Collectors.toList()).size();
            pickSkuCount = pickSkuIds.stream().distinct().collect(Collectors.toList()).size();
            row.createCell(1).setCellValue(outSkuCount+"类"+outNumCount+"件");
            row.createCell(2).setCellValue(pickSkuCount+"类"+pickNumCount+"件");


        }
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
