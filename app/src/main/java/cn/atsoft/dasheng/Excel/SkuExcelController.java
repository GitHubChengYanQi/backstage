package cn.atsoft.dasheng.Excel;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.atsoft.dasheng.Excel.pojo.SkuExcelItem;
import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.entity.Unit;
import cn.atsoft.dasheng.app.service.UnitService;
import cn.atsoft.dasheng.base.consts.ConstantsContext;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.entity.excel.CustomerExcelItem;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.message.topic.TopicMessage;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.sys.core.exception.enums.BizExceptionEnum;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.print.DocFlavor;
import javax.sound.midi.Soundbank;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;


@Controller
@RequestMapping("/Excel")
@Slf4j
public class SkuExcelController {

    @Autowired
    private SpuService spuService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private ItemAttributeService attributeService;
    @Autowired
    private AttributeValuesService valuesService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private SpuClassificationService classificationService;
    @Autowired
    private CategoryService categoryService;

    protected static final Logger logger = LoggerFactory.getLogger(SkuExcelController.class);

    /**
     * 上传excel填报
     */
    @RequestMapping("/importSku")
    @ResponseBody
    public ResponseData uploadExcel(@RequestParam("file") MultipartFile file) {


        String name = file.getOriginalFilename();
        String fileSavePath = ConstantsContext.getFileUploadPath();

        File excelFile = new File(fileSavePath + name);
        try {
            file.transferTo(excelFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("文件名称——>" + name);
        logger.info("文件路径——>" + fileSavePath);
        logger.info("file 文件——>" + excelFile);


        ExcelReader excelReader = ExcelUtil.getReader(excelFile, 0);
        excelReader.addHeaderAlias("成品码", "standard");
        excelReader.addHeaderAlias("分类", "spuClass");
        excelReader.addHeaderAlias("产品", "classItem");
        excelReader.addHeaderAlias("型号", "spuName");
        excelReader.addHeaderAlias("单位", "unit");
        excelReader.addHeaderAlias("是否批量", "isNotBatch");

        List<SkuExcelItem> skuExcelItems = excelReader.readAll(SkuExcelItem.class);

        List<List<Object>> read = excelReader.read(1);
        for (int i = 0; i < read.size(); i++) {
            List<Object> objects = read.get(i);
            SkuExcelItem skuExcelItem = skuExcelItems.get(i);
            List<String> attributes = new ArrayList<>();
            for (int j = 6; j < objects.size(); j++) {
                Object o = objects.get(j);
                if (ToolUtil.isNotEmpty(o)) {
                    attributes.add(o.toString());
                }
            }
            skuExcelItem.setAttributes(attributes);
        }
//----------------------------------------------------------------------------------------------------------------------

        if (skuExcelItems.size() > 1000) {
            throw new ServiceException(500, "最多只可导入1000个");
        }

//----------------------------------------------------------------------------------------------------------------------

        Integer i = 0;

        List<SkuExcelItem> errorList = new ArrayList<>();

        //-------------------------------------------------------------------------------------------------------------
        List<Sku> skuList = new ArrayList<>();
        List<Sku> skus = skuService.query().eq("display", 1).isNotNull("standard").list();  //所有sku
        List<SpuClassification> spuClassifications = classificationService.query().eq("display", 1).list(); //所有分类
        List<SpuClassification> items = classificationService.query().eq("display", 1).eq("type", 2).list();//所有产品
        List<Spu> spuList = spuService.query().eq("display", 1).list();
        List<Unit> units = unitService.query().eq("display", 1).list();

        for (SkuExcelItem skuExcelItem : skuExcelItems) {
            i++;
            try {
                for (Sku sku : skus) {
                    if (sku.getStandard().equals(skuExcelItem.getStandard())) {
                        throw new ServiceException(500, "编码以重复");
                    }
                }

                Sku sku = new Sku();
                sku.setStandard(skuExcelItem.getStandard());
                //分类----------------------------------------------------------------------------------------------
                Long classId = null;
                for (SpuClassification spuClassification : spuClassifications) {
                    if (spuClassification.getName().equals(skuExcelItem.getSpuClass())) {
                        classId = spuClassification.getSpuClassificationId();
                    }
                }
                if (ToolUtil.isEmpty(classId)) {
                    SpuClassification spuClassification = new SpuClassification();
                    spuClassification.setName(skuExcelItem.getSpuClass());
                    classificationService.save(spuClassification);
                    classId = spuClassification.getSpuClassificationId();
                }
                //产品--------------------------------------------------------------------------------------------
                Long itemId = null;
                for (SpuClassification item : items) {
                    if (skuExcelItem.getClassItem().equals(item.getName())) {
                        itemId = item.getSpuClassificationId();
                        item.setPid(classId);
                        classificationService.updateById(item);
                    }
                }
                if (ToolUtil.isEmpty(itemId)) {
                    SpuClassification newClass = new SpuClassification();
                    newClass.setName(skuExcelItem.getClassItem());
                    newClass.setType(2L);
                    newClass.setPid(classId);
                    classificationService.save(newClass);
                    itemId = newClass.getSpuClassificationId();
                }
                //型号----------------------------------------------------------------------------------------------
                Long spuId = null;
                for (Spu spu : spuList) {
                    if (spu.getName().equals(skuExcelItem.getSpuName())) {
                        sku.setSpuId(spu.getSpuId());
                        spu.setSpuClassificationId(itemId);
                        spuService.updateById(spu);
                        spuId = spu.getSpuId();
                    }
                }
                if (ToolUtil.isEmpty(spuId)) {
                    Category category = new Category();
                    category.setCategoryName(skuExcelItem.getSpuName());
                    categoryService.save(category);

                    Spu newSpu = new Spu();
                    newSpu.setSpuClassificationId(itemId);
                    newSpu.setName(skuExcelItem.getSpuName());
                    newSpu.setCategoryId(category.getCategoryId());
                    spuService.save(newSpu);
                    sku.setSpuId(newSpu.getSpuId());
                    spuId = newSpu.getSpuId();
                }
                //单位------------------------------------------------------------------------------------------------------
                Spu spuById = spuService.getById(spuId);
                Long unitId = null;
                for (Unit unit : units) {
                    if (unit.getUnitName().equals(skuExcelItem.getUnit())) {
                        unitId = unit.getUnitId();
                    }
                }
                if (ToolUtil.isEmpty(unitId)) {
                    Unit newUnit = new Unit();
                    newUnit.setUnitName(skuExcelItem.getUnit());
                    unitService.save(newUnit);
                    unitId = newUnit.getUnitId();
                }
                spuById.setUnitId(unitId);
                spuService.updateById(spuById);
                //批量-----------------------------------------------------------------------------------------------
                if (skuExcelItem.getIsNotBatch().equals("是")) {
                    sku.setBatch(1);
                }
                //属性-----------------------------------------------------------------------------------------------
                List<AttributeValues> list = new ArrayList<>();

                for (String attribute : skuExcelItem.getAttributes()) {
                    String[] split = attribute.split(":");
                    String attr = split[0];
                    String value = split[1];

                    ItemAttribute itemAttribute = new ItemAttribute();
                    itemAttribute.setAttribute(attr);
                    attributeService.save(itemAttribute);

                    AttributeValues values = new AttributeValues();
                    values.setAttributeValues(value);
                    values.setAttributeId(itemAttribute.getAttributeId());
                    valuesService.save(values);

                    list.add(values);
                }
                if (ToolUtil.isNotEmpty(list) && list.size() > 0) {
                    list.sort(Comparator.comparing(AttributeValues::getAttributeId));
                    String json = JSON.toJSONString(list);
                    sku.setSkuValue(json);
                }
                String md5 = SecureUtil.md5(spuId + sku.getSkuValue());
                sku.setSkuValueMd5(md5);
                if (skuList.stream().anyMatch(item -> item.getStandard().equals(sku.getStandard()))) {
                    skuExcelItem.setLine(i);
                    errorList.add(skuExcelItem);
                } else {
                    skuList.add(sku);
                }

            } catch (Exception e) {
                skuExcelItem.setLine(i);
                errorList.add(skuExcelItem);
                logger.error("第" + i + "行" + skuExcelItem + "错误" + e);
            }
        }
        skuService.saveBatch(skuList);
        return ResponseData.success(errorList);
    }
}
