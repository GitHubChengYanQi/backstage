package cn.atsoft.dasheng.Excel;

import cn.atsoft.dasheng.Excel.pojo.ExcelPositionResult;
import cn.atsoft.dasheng.Excel.pojo.PositionBind;
import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.base.consts.ConstantsContext;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.model.params.InkindParam;
import cn.atsoft.dasheng.erp.service.InkindService;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsBindService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.orCode.pojo.BatchAutomatic;
import cn.atsoft.dasheng.orCode.pojo.InkindQrcode;
import cn.atsoft.dasheng.orCode.service.OrCodeService;
import cn.atsoft.dasheng.sys.modular.system.entity.FileInfo;
import cn.atsoft.dasheng.sys.modular.system.service.FileInfoService;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/Excel")
@Slf4j
public class PositionBindExcel {

    @Autowired
    private FileInfoService fileInfoService;
    @Autowired
    private ExcelAsync excelAsync;
    @Autowired
    private SkuExcelService skuExcelService;

    protected static final Logger logger = LoggerFactory.getLogger(SkuExcelController.class);

    @RequestMapping("/importPositionBind")
    @ResponseBody
    public ResponseData uploadExcel(@RequestParam("fileId") Long fileId) {

        FileInfo fileInfo = fileInfoService.getById(fileId);
        File excelFile = new File(fileInfo.getFilePath());

        ExcelReader reader = ExcelUtil.getReader(excelFile);
        reader.addHeaderAlias("分类", "spuClass");
        reader.addHeaderAlias("物料编码", "strand");
        reader.addHeaderAlias("产品", "item");
        reader.addHeaderAlias("型号", "spuName");
        reader.addHeaderAlias("库位", "position");
        reader.addHeaderAlias("库存余额", "stockNumber");
        reader.addHeaderAlias("上级库位", "supperPosition");
        reader.addHeaderAlias("品牌", "brand");


        List<PositionBind> excels = reader.readAll(PositionBind.class);
        /**
         * 调用异步
         */
        excelAsync.positionAdd(excels);

        return ResponseData.success("ok");
    }

    @RequestMapping(value = "/positionTemp", method = RequestMethod.GET)
    public void positionTemp(HttpServletResponse response) {

        String[] header = {"物料编码", "分类", "产品", "型号","库存余额","上级库位","库位","品牌"};

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = workbook.createSheet("库位绑定模板");
        HSSFSheet sheet = skuExcelService.dataEffective(hssfSheet, 300);
        HSSFRow titleRow = sheet.createRow(0);
        HSSFCell ti = titleRow.createCell(0);

        HSSFCellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFillForegroundColor((short) 10);

        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        ti.setCellStyle(titleStyle);

        HSSFRow headRow = sheet.createRow(0);

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
        //准备将Excel的输出流通过response输出到页面下载
        //八进制输出流
        response.setContentType("application/octet-stream");

        //这后面可以设置导出Excel的名称
        response.setHeader("Content-disposition", "attachment;filename=PositionTemp.xls");


        try {
            //刷新缓冲
            response.flushBuffer();
            //workbook将Excel写入到response的输出流中，供页面下载
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
