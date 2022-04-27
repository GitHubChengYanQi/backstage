package cn.atsoft.dasheng.Excel;

import cn.atsoft.dasheng.Excel.pojo.SpuExcel;
import cn.atsoft.dasheng.app.service.UnitService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.Category;
import cn.atsoft.dasheng.erp.entity.Spu;
import cn.atsoft.dasheng.erp.entity.SpuClassification;
import cn.atsoft.dasheng.erp.model.result.CategoryResult;
import cn.atsoft.dasheng.erp.service.CategoryService;
import cn.atsoft.dasheng.erp.service.SpuClassificationService;
import cn.atsoft.dasheng.erp.service.SpuService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.sys.modular.system.entity.FileInfo;
import cn.atsoft.dasheng.sys.modular.system.service.FileInfoService;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/spuExcel")
public class SpuExcelController {

    @Autowired
    private FileInfoService fileInfoService;
    @Autowired
    private SkuExcelService skuExcelService;
    @Autowired
    private SpuService spuService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SpuClassificationService classificationService;
    @Autowired
    private UnitService unitService;


    @RequestMapping(value = "/spuImport", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData spuImport(@RequestParam Long fileId) {

        FileInfo fileInfo = fileInfoService.getById(fileId);
        File excelFile = new File(fileInfo.getFilePath());

        ExcelReader reader = ExcelUtil.getReader(excelFile);

        reader.addHeaderAlias("产品编码", "spuCoding");
        reader.addHeaderAlias("产品分类", "spuClass");
        reader.addHeaderAlias("产品名称", "spuName");
        reader.addHeaderAlias("单位", "unit");
        List<SpuExcel> spuExcels = reader.readAll(SpuExcel.class);


        List<Spu> spuList = spuService.query().eq("display", 1).list();
        List<Category> categoryList = categoryService.query().eq("display", 1).list();
        List<SpuClassification> spuClassList = classificationService.query().eq("display", 1).list();

        List<SpuExcel> errorList = new ArrayList<>();
        List<Spu> spus = new ArrayList<>();
        int i = 0;
        for (SpuExcel spuExcel : spuExcels) {
            i++;
            spuExcel.setLine(i + "");
            try {
                Spu newSpu = new Spu();
                Long classId = null;
                for (SpuClassification spuClassification : spuClassList) {
                    if (spuClassification.getName().equals(spuExcel.getSpuClass())) {
                        classId = spuClassification.getSpuClassificationId();
                        break;
                    }
                }
                if (ToolUtil.isEmpty(classId)) {
                    throw new ServiceException(500, "产品分类不存在");
                }

                for (Spu spu : spuList) {
                    if (spu.getCoding().equals(spuExcel.getSpuCoding())) {
                        throw new ServiceException(500, "产品编码已存在");
                    }
                }
                //------------------------------------------------------------------------------
                Category cate = null;
                for (Category category : categoryList) {
                    if (category.getCategoryName().equals(spuExcel.getSpuName())) {
                        cate = category;
                        break;
                    }
                }
                if (ToolUtil.isEmpty(cate)) {
                    cate = new Category();
                    cate.setCategoryName(spuExcel.getSpuName());
                    categoryService.save(cate);
                }
                newSpu.setCategoryId(cate.getCategoryId());
                newSpu.setName(spuExcel.getSpuName());
                newSpu.setSpuClassificationId(classId);
                spus.add(newSpu);
            } catch (Exception e) {
                errorList.add(spuExcel);
                e.printStackTrace();
            }

        }
        spuService.saveBatch(spus);

        return ResponseData.success();
    }


    @RequestMapping(value = "/spuTemp", method = RequestMethod.GET)
    public void spuTemp(HttpServletResponse response) {

        String[] header = {"产品编码", "产品分类", "产品名称", "单位"};

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = workbook.createSheet("产品模板");
        HSSFSheet sheet = skuExcelService.dataEffective(hssfSheet,300);
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
        response.setHeader("Content-disposition", "attachment;filename=spuTemp.xls");


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
