package cn.atsoft.dasheng.Excel;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.atsoft.dasheng.Excel.pojo.SkuExcelItem;
import cn.atsoft.dasheng.Excel.pojo.Specifications;
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
import cn.atsoft.dasheng.serial.model.params.SerialNumberParam;
import cn.atsoft.dasheng.serial.service.SerialNumberService;
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
import javax.tools.Tool;
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
    @Autowired
    private CodingRulesService codingRulesService;
    @Autowired
    private SerialNumberService serialNumberService;

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
        ExcelReader excelReader = ExcelUtil.getReader(excelFile, 0);
        List<Object> readRow = excelReader.readRow(0);
        List<List<Object>> read = excelReader.read(1);
        List<SkuExcelItem> skuExcelItemList = new ArrayList<>();

        for (int i = 0; i < read.size(); i++) {
            List<Object> hang = read.get(i);
            SkuExcelItem skuExcelItem = new SkuExcelItem();
            List<Specifications> specificationsList = new ArrayList<>();
            skuExcelItem.setLine(i + 1);

            for (int j = 0; j < readRow.size() + hang.size(); j++) {
                try {
                    Object header = readRow.get(j);
                    String data = hang.get(j).toString();
                    switch (header.toString()) {
                        case "物料编码":
                            if (ToolUtil.isEmpty(data)) {
                                data = null;
                                skuExcelItem.setStandard(null);
                            } else {
                                skuExcelItem.setStandard(data);
                            }
                            break;
                        case "分类":
                            skuExcelItem.setSpuClass(data);
                            break;
                        case "产品":
                            skuExcelItem.setClassItem(data);
                            break;
                        case "型号":
                            skuExcelItem.setSpuName(data);
                            break;
                        case "单位":
                            skuExcelItem.setUnit(data);
                            break;
                        case "是否批量":
                            skuExcelItem.setIsNotBatch(data);
                            break;
                        case "规则名称":
                            skuExcelItem.setItemRule(data);
                            break;
                        default:
                            if (ToolUtil.isNotEmpty(header) && ToolUtil.isNotEmpty(data)) {
                                Specifications specifications = new Specifications();
                                specifications.setAttribute(header.toString());
                                specifications.setValue(data);
                                specificationsList.add(specifications);
                            }
                    }
                } catch (Exception e) {
                    logger.error("读取异常:" + e.toString());
                }
            }
            skuExcelItem.setSpecifications(specificationsList);
            skuExcelItemList.add(skuExcelItem);
        }
//---------------------------------------------以上是读取excel数据----别动！！！---------------------------------------------


        //-------------------------------------------------------------------------------------------------------------
        List<Sku> skuList = new ArrayList<>();
        List<Sku> skus = skuService.query().eq("display", 1).list();  //所有sku
        List<SpuClassification> spuClassifications = classificationService.query().eq("display", 1).eq("type", 1).list(); //所有分类
        List<Spu> spuList = spuService.query().eq("display", 1).list();
        List<Unit> units = unitService.query().eq("display", 1).list();
        List<Category> categories = categoryService.query().eq("display", 1).list();

        List<SkuExcelItem> errorList = new ArrayList<>();
        for (SkuExcelItem skuExcelItem : skuExcelItemList) {
            Sku newSku = new Sku();
            try {
                //成品码-------------------------------------------------------------------------------------------------
                if (ToolUtil.isEmpty(skuExcelItem.getStandard())) {
                    throw new ServiceException(500, "物料编码不存在");
                } else {
                    skuExcelItem.setStandard(skuExcelItem.getStandard().replaceAll(" ", ""));
                    for (Sku sku : skus) {
                        if (sku.getStandard().equals(skuExcelItem.getStandard())) {
                            throw new ServiceException(500, "编码以重复");
                        }
                    }
                    newSku.setStandard(skuExcelItem.getStandard());
                    skus.add(newSku);
                }
                newSku.setSkuName(skuExcelItem.getSpuName());  //   型号
                // 分类----------------------------------------------------------------------------------------------
                if ("".equals(skuExcelItem.getSpuClass())) {
                    throw new ServiceException(500, "参数错误");
                }
                SpuClassification spuClass = null;
                for (SpuClassification spuClassification : spuClassifications) {
                    if (spuClassification.getName().equals(skuExcelItem.getSpuClass())) {
                        spuClass = spuClassification;
                        break;
                    }
                }
                if (ToolUtil.isEmpty(spuClass)) {
                    throw new ServiceException(500, "没有分类");
                }

                //产品--------------------------------------------------------------------------------------------
                if (ToolUtil.isEmpty(skuExcelItem.getClassItem())) {
                    throw new ServiceException(500, "产品不存在");
                }
                skuExcelItem.setSpuName(skuExcelItem.getClassItem().replaceAll(" ", ""));
                Long categoryId = null;
                for (Category category : categories) {
                    if (skuExcelItem.getClassItem().equals(category.getCategoryName())) {
                        categoryId = category.getCategoryId();
                        break;
                    }
                }
                if (ToolUtil.isEmpty(categoryId)) {
                    Category category = new Category();
                    category.setCategoryName(skuExcelItem.getClassItem());
                    categoryService.save(category);
                    categories.add(category);
                    categoryId = category.getCategoryId();
                }

                Spu newItem = null;
                for (Spu spu : spuList) {
                    if (skuExcelItem.getClassItem().equals(spu.getName())) {
                        newItem = spu;
                        break;
                    }
                }
                if (ToolUtil.isEmpty(newItem)) {
                    newItem = new Spu();
                    newItem.setName(skuExcelItem.getClassItem());
                    newItem.setSpuClassificationId(spuClass.getSpuClassificationId());
                    newItem.setCategoryId(categoryId);
                    //TODO 产品替换编码
                    spuService.save(newItem);
                    spuList.add(newItem);
                }
                newSku.setSpuId(newItem.getSpuId());
                //单位------------------------------------------------------------------------------------------------------
                if (ToolUtil.isEmpty(skuExcelItem.getUnit())) {
                    throw new ServiceException(500, "参数错误");
                }
                skuExcelItem.setUnit(skuExcelItem.getUnit().replaceAll(" ", ""));
                Long unitId = null;
                for (Unit unit : units) {
                    if (unit.getUnitName().equals(skuExcelItem.getUnit())) {
                        unitId = unit.getUnitId();
                        break;
                    }
                }
                if (ToolUtil.isEmpty(unitId)) {
                    Unit newUnit = new Unit();
                    newUnit.setUnitName(skuExcelItem.getUnit());
                    unitService.save(newUnit);
                    units.add(newUnit);
                    unitId = newUnit.getUnitId();
                }
                newItem.setUnitId(unitId);
                spuService.updateById(newItem);
                //批量-----------------------------------------------------------------------------------------------
                if ("".equals(skuExcelItem.getIsNotBatch())) {
                    throw new ServiceException(500, "参数错误");
                }
                if (skuExcelItem.getIsNotBatch().equals("是")) {
                    newSku.setBatch(1);
                }

                //属性-----------------------------------------------------------------------------------------------
                List<AttributeValues> list = new ArrayList<>();

                for (Specifications specifications : skuExcelItem.getSpecifications()) {
                    if (ToolUtil.isNotEmpty(specifications.getAttribute()) && ToolUtil.isNotEmpty(specifications.getValue())) {
                        AttributeValues value = new AttributeValues();
                        Long attributeId = 0L;
                        ItemAttribute attribute = attributeService.query().eq("attribute", specifications.getAttribute()).eq("category_id", categoryId).eq("display", 1).one();
                        if (ToolUtil.isEmpty(attribute)) {
                            ItemAttribute itemAttribute = new ItemAttribute();
                            itemAttribute.setAttribute(specifications.getAttribute());
                            itemAttribute.setCategoryId(categoryId);
                            attributeService.save(itemAttribute);
                            attributeId = itemAttribute.getAttributeId();
                        } else {
                            attributeId = attribute.getAttributeId();
                        }

                        Long valueId = 0L;
                        AttributeValues attributeValues = valuesService.query().eq("attribute_values", specifications.getAttribute()).eq("attribute_values", specifications.getValue()).eq("display", 1).one();
                        if (ToolUtil.isEmpty(attributeValues)) {
                            AttributeValues values = new AttributeValues();
                            values.setAttributeValues(specifications.getValue());
                            values.setAttributeId(attributeId);
                            valuesService.save(values);
                            valueId = values.getAttributeValuesId();
                        } else {
                            valueId = attributeValues.getAttributeValuesId();
                        }
                        value.setAttributeId(attributeId);
                        value.setAttributeValuesId(valueId);
                        list.add(value);
                    }
                }
                if (ToolUtil.isNotEmpty(list) && list.size() > 0) {
                    list.sort(Comparator.comparing(AttributeValues::getAttributeId));
                    String json = JSON.toJSONString(list);
                    newSku.setSkuValue(json);
                }

                if (skuList.stream().noneMatch(item -> item.getStandard().equals(newSku.getStandard()))) {  //excel 重复数据
                    skuList.add(newSku);
                }

            } catch (Exception e) {
                logger.error("写入异常:" + "第" + skuExcelItem.getLine() + "行" + skuExcelItem + "错误" + e);   //错误异常
                skuExcelItem.setError(e.getMessage());
                errorList.add(skuExcelItem);
            }
        }
        skuService.saveBatch(skuList);
        return ResponseData.success(errorList);
    }
}
