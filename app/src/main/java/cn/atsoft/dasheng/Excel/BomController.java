package cn.atsoft.dasheng.Excel;

import cn.atsoft.dasheng.Excel.pojo.Bom;
import cn.atsoft.dasheng.app.entity.ErpPartsDetail;
import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.entity.Unit;
import cn.atsoft.dasheng.app.model.params.ErpPartsDetailParam;
import cn.atsoft.dasheng.app.model.params.PartsParam;
import cn.atsoft.dasheng.app.service.ErpPartsDetailService;
import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.app.service.UnitService;
import cn.atsoft.dasheng.base.consts.ConstantsContext;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.model.params.SkuParam;
import cn.atsoft.dasheng.erp.model.params.SpuParam;
import cn.atsoft.dasheng.erp.service.CategoryService;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.SpuClassificationService;
import cn.atsoft.dasheng.erp.service.SpuService;
import cn.atsoft.dasheng.model.exception.ServiceException;
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
import java.rmi.ServerException;
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
    @Autowired
    private UnitService unitService;
    @Autowired
    private SpuService spuService;
    @Autowired
    private SpuClassificationService classificationService;
    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/importBom")
    @ResponseBody
    public ResponseData uploadExcel(@RequestParam("file") MultipartFile file) throws IOException {

        List<String> errorList = new ArrayList<>();
        XSSFWorkbook workbook = null;

        String name = file.getOriginalFilename();
        String fileSavePath = ConstantsContext.getFileUploadPath();
        File excelFile = new File(fileSavePath + name);

        } catch (IOException | InvalidFormatException e) {
        try {
            file.transferTo(excelFile);
            workbook = new XSSFWorkbook(excelFile.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseData.success();
        if (ToolUtil.isEmpty(workbook)) {
            workbook = new XSSFWorkbook();
        }
        for (Sheet sheet : workbook) {
            try {
                String sheetName = sheet.getSheetName();
                Parts newParts = new Parts();
                Sku sku = skuService.query().eq("standard", sheetName).eq("display", 1).one();
                if (ToolUtil.isEmpty(sku)) {
                    throw new ServiceException(500, "没有当前此物料:   " + sheetName);
                }
                Parts parts = partsService.query().eq("sku_id", sku.getSkuId()).eq("status", 99).eq("type", 1).one();
                //如果当前物料有bom 不添加
                if (ToolUtil.isEmpty(parts)) {
                    newParts.setSkuId(sku.getSkuId());
                    newParts.setSpuId(sku.getSpuId());

                    ExcelReader reader = ExcelUtil.getReader(excelFile, sheetName);
                    reader.addHeaderAlias("序号", "line");
                    reader.addHeaderAlias("物料编号", "strand");
                    reader.addHeaderAlias("名称", "spuName");
                    reader.addHeaderAlias("规格", "spc");
                    reader.addHeaderAlias("数量", "num");
                    reader.addHeaderAlias("单位", "unit");
                    reader.addHeaderAlias("型号", "skuName");
                    reader.addHeaderAlias("分类", "spuClass");
                    reader.addHeaderAlias("是否批量", "batch");

                    List<ErpPartsDetail> details = new ArrayList<>();
                    List<Bom> boms = reader.readAll(Bom.class);
                    for (Bom bom : boms) {

                        ErpPartsDetail detail = new ErpPartsDetail();
                        Sku detailSku = skuService.query().eq("standard", bom.getStrand()).eq("display", 1).one();

                        if (ToolUtil.isEmpty(detailSku)) {    //没有sku添加
                            detailSku = new Sku();
                            detailSku.setSkuName(bom.getSkuName());
                            detailSku.setSpecifications(bom.getSpc());
                            detailSku.setStandard(bom.getStrand());
                            /**
                             * 批量
                             */
                            if (ToolUtil.isNotEmpty(bom.getBatch()) && bom.getBatch().equals("是")) {
                                detailSku.setBatch(1);
                            }
                            /**
                             * 单位
                             */
                            Long unitId;
                            Unit unit = unitService.query().eq("unit_name", bom.getUnit()).eq("display", 1).one();
                            if (ToolUtil.isEmpty(unit)) {
                                Unit newUnit = new Unit();
                                newUnit.setUnitName(bom.getUnit());
                                unitService.save(newUnit);
                                unitId = newUnit.getUnitId();
                            } else {
                                unitId = unit.getUnitId();
                            }
                            /**
                             * 分类
                             */
                            SpuClassification classification = classificationService.query().eq("name", bom.getSpuClass()).eq("display", 1).one();
                            if (ToolUtil.isEmpty(classification)) {
                                throw new ServiceException(500, sheetName + "物料下的" + bom.getStrand() + "分类不存在");
                            }
                            Spu spu = spuService.query().eq("name", bom.getSpuName()).eq("display", 1).one();
                            if (ToolUtil.isNotEmpty(spu)) {
                                detailSku.setSpuId(spu.getSpuId());
                            } else {
                                Category category = new Category();
                                category.setCategoryName(bom.getSpuName());
                                categoryService.save(category);

                                spu = new Spu();
                                spu.setName(bom.getSpuName());
                                spu.setUnitId(unitId);
                                spu.setSpuClassificationId(classification.getSpuClassificationId());
                                spu.setCategoryId(category.getCategoryId());
                                spuService.save(spu);
                                detailSku.setSpuId(spu.getSpuId());
                            }
                            skuService.save(detailSku);
                            detail.setSkuId(detailSku.getSkuId());
                        } else {
                            detail.setSkuId(detailSku.getSkuId());
                        }
                        if (ToolUtil.isEmpty(bom.getNum())) {
                            throw new ServiceException(500, "第" + bom.getLine() + "行 缺少数量");
                        }
                        detail.setNumber(bom.getNum());

                        if (details.stream().noneMatch(i -> i.getSkuId().equals(detail.getSkuId()))) {
                            details.add(detail);
                        }
                    }
                    newParts.setStatus(99);
                    partsService.save(newParts);
                    for (ErpPartsDetail detail : details) {
                        detail.setPartsId(newParts.getPartsId());
                    }
                    detailService.saveBatch(details);
                }
            } catch (Exception e) {
                errorList.add(e.getMessage());
            }
        }
        workbook.close();
        return ResponseData.success(errorList);
    }


}
