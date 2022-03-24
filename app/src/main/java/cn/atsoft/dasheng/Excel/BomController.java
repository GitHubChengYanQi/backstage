package cn.atsoft.dasheng.Excel;

import cn.atsoft.dasheng.Excel.pojo.Bom;
import cn.atsoft.dasheng.app.entity.ErpPartsDetail;
import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.model.params.ErpPartsDetailParam;
import cn.atsoft.dasheng.app.model.params.PartsParam;
import cn.atsoft.dasheng.app.service.ErpPartsDetailService;
import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.base.consts.ConstantsContext;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.entity.Tool;
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
import java.util.Iterator;
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


        XSSFWorkbook workbook = null;
        try {
            String name = file.getOriginalFilename();
            String fileSavePath = ConstantsContext.getFileUploadPath();
            File excelFile = new File(fileSavePath + name);
            file.transferTo(excelFile);
            workbook = new XSSFWorkbook(excelFile);


            for (Sheet sheet : workbook) {
                String sheetName = sheet.getSheetName();
                PartsParam param = new PartsParam();
                Sku sku = skuService.query().eq("standard", sheetName).one();
                param.setSkuId(sku.getSkuId());

                ExcelReader reader = ExcelUtil.getReader(excelFile, sheetName);
                reader.addHeaderAlias("序号", "line");
                reader.addHeaderAlias("物料编号", "strand");
                reader.addHeaderAlias("名称", "spuName");
                reader.addHeaderAlias("规格", "spc");
                reader.addHeaderAlias("数量", "num");
                reader.addHeaderAlias("单位", "unit");

                List<ErpPartsDetailParam> details = new ArrayList<>();
                List<Bom> boms = reader.readAll(Bom.class);
                for (Bom bom : boms) {
                    ErpPartsDetailParam detailParam = new ErpPartsDetailParam();
                    Sku skus = skuService.query().eq("standard", bom.getStrand()).one();
                    if (ToolUtil.isEmpty(sku)) {

                    }
                    detailParam.setSkuId(sku.getSkuId());
                    detailParam.setNumber(Integer.valueOf(bom.getNum()));
                    details.add(detailParam);
                }
                param.setParts(details);
                partsService.add(param);
            }

        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
        return ResponseData.success();
    }


}
