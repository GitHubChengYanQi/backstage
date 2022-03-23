package cn.atsoft.dasheng.Excel;

import cn.atsoft.dasheng.Excel.pojo.Bom;
import cn.atsoft.dasheng.app.entity.ErpPartsDetail;
import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.service.ErpPartsDetailService;
import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.base.consts.ConstantsContext;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.lang.Console;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.sax.Excel07SaxReader;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import com.alibaba.fastjson.JSON;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/Excel")
@Slf4j
public class BomController {
    @Autowired
    private SkuService skuService;
    @Autowired
    private PartsService partsService;
    @Autowired
    private ErpPartsDetailService detailService;

    @RequestMapping("/importBom")
    @ResponseBody
    public ResponseData uploadExcel(@RequestParam("file") MultipartFile file) {

        List<Sku> skuList = skuService.list();

        XSSFWorkbook workbook = null;
        try {
            String name = file.getOriginalFilename();
            String fileSavePath = ConstantsContext.getFileUploadPath();
            File excelFile = new File(fileSavePath + name);
            file.transferTo(excelFile);
            workbook = new XSSFWorkbook(excelFile);

            Parts parts = null;

            for (Sheet sheet : workbook) {
                parts = new Parts();
                String sheetName = sheet.getSheetName();
                for (Sku sku : skuList) {
                    if (sku.getStandard().equals(sheetName)) {
                        parts.setSkuId(sku.getSkuId());
                        break;
                    }
                    partsService.save(parts);
                }
                List<ErpPartsDetail> details = new ArrayList<>();
                for (int i = 0; i < sheet.getLastRowNum() + 1; i++) {
                    Row sheetRow = sheet.getRow(i);
                    Cell cell = sheetRow.getCell(0);
                    cell.setCellType(CellType.STRING);
                    String value = cell.getStringCellValue();
                    for (Sku sku : skuList) {
                        if (sku.getStandard().equals(value)) {
                            ErpPartsDetail partsDetail = new ErpPartsDetail();
                            partsDetail.setSkuId(sku.getSkuId());
                            partsDetail.setPartsId(parts.getPartsId());
                            details.add(partsDetail);
                            break;
                        }
                    }
                }
                detailService.saveBatch(details);
            }

        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
        return ResponseData.success();
    }


}