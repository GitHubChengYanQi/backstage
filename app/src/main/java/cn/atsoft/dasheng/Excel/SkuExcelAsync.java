package cn.atsoft.dasheng.Excel;

import cn.atsoft.dasheng.Excel.pojo.SkuExcelItem;
import cn.atsoft.dasheng.Excel.pojo.SkuExcelResult;
import cn.atsoft.dasheng.app.entity.Unit;
import cn.atsoft.dasheng.app.service.UnitService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.serial.service.SerialNumberService;
import cn.atsoft.dasheng.sys.modular.system.service.FileInfoService;
import cn.atsoft.dasheng.task.entity.AsynTask;
import cn.atsoft.dasheng.task.entity.AsynTaskDetail;
import cn.atsoft.dasheng.task.service.AsynTaskDetailService;
import cn.atsoft.dasheng.task.service.AsynTaskService;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@jdk.nashorn.internal.runtime.logging.Logger
public class SkuExcelAsync {

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
    private AsynTaskService taskService;
    @Autowired
    private AsynTaskDetailService asynTaskDetailService;


    protected static final Logger logger = LoggerFactory.getLogger(SkuExcelController.class);


    @Async
    public void add(List<SkuExcelItem> skuExcelItemList) {

        //-------------------------------------------------------------------------------------------------------------
        List<Sku> skuList = new ArrayList<>();
        List<Sku> skus = skuService.query().eq("display", 1).list();  //所有sku
        List<SpuClassification> spuClassifications = classificationService.query().eq("display", 1).eq("type", 1).list(); //所有分类
        List<Spu> spuList = spuService.query().eq("display", 1).list();
        List<Unit> units = unitService.query().eq("display", 1).list();
        List<Category> categories = categoryService.query().eq("display", 1).list();


        AsynTask asynTask = new AsynTask();
        asynTask.setAllCount(skuExcelItemList.size());
        asynTask.setType("物料导入");
        asynTask.setStatus(0);
        taskService.save(asynTask);


        SkuExcelResult skuExcelResult = new SkuExcelResult();
        List<SkuExcelItem> errorList = new ArrayList<>();
        int successNum = 0;
        int i = 0;
        List<AsynTaskDetail> asynTaskDetails = new ArrayList<>();
        for (SkuExcelItem skuExcelItem : skuExcelItemList) {

            AsynTaskDetail asynTaskDetail = new AsynTaskDetail();
            asynTaskDetail.setTaskId(asynTask.getTaskId());
            asynTaskDetail.setType("物料导入");
            Sku newSku = new Sku();
            try {

                i++;
                asynTask.setCount(i);   //修改任务状态
                taskService.updateById(asynTask);


                //成品码-------------------------------------------------------------------------------------------------
                if (ToolUtil.isEmpty(skuExcelItem.getStandard())) {
                    throw new ServiceException(500, "物料编码不存在");
                } else {
                    skuExcelItem.setStandard(skuExcelItem.getStandard().replaceAll(" ", ""));
                    for (Sku sku : skus) {
                        if (sku.getStandard().equals(skuExcelItem.getStandard())) {
                            SkuResult results = skuService.getDetail(sku.getSkuId());
                            skuExcelItem.setSimpleResult(results);
                            skuExcelItem.setErrorSkuId(sku.getSkuId());
                            skuExcelItem.setType("codingRepeat");
                            throw new ServiceException(500, "编码重复");
                        }
                    }
                    newSku.setStandard(skuExcelItem.getStandard());
                }
                newSku.setSkuName(skuExcelItem.getSkuName());  //   型号

                Long unitId = null;
                for (Unit unit : units) {
                    if (unit.getUnitName().equals(skuExcelItem.getUnit())) {
                        unitId = unit.getUnitId();
                        break;
                    }
                }

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
                    skuExcelItem.setUnitId(unitId);
                    skuExcelItem.setType("noClass");
                    throw new ServiceException(500, "没有分类");
                }

                //产品--------------------------------------------------------------------------------------------
                if (ToolUtil.isEmpty(skuExcelItem.getClassItem())) {
                    throw new ServiceException(500, "产品不存在");
                }
                skuExcelItem.setClassItem(skuExcelItem.getClassItem().replaceAll(" ", ""));
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
                    if (skuExcelItem.getClassItem().equals(spu.getName()) && spu.getSpuClassificationId().equals(spuClass.getSpuClassificationId())) {
                        newItem = spu;
                        break;
                    }
                }
                if (ToolUtil.isEmpty(newItem)) {
                    skuExcelItem.setUnitId(unitId);
                    skuExcelItem.setClassId(skuExcelItem.getClassId());
                    skuExcelItem.setType("noSpu");
                    skuExcelItem.setClassId(spuClass.getSpuClassificationId());
                    throw new ServiceException(500, "没有当前产品");
                }
                newSku.setSpuId(newItem.getSpuId());

                //单位------------------------------------------------------------------------------------------------------
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
                //规格-----------------------------------------------------------------------------------------------
                newSku.setSpecifications(skuExcelItem.getSpecifications());

                //物料描述--------------------------------------------------------------------------------------------------
                String describe = skuExcelItem.getDescribe();
                List<AttributeValues> list = new ArrayList<>();
                if (ToolUtil.isNotEmpty(describe)) {
                    List<String> attributeAndValues = Arrays.stream(describe.split(",")).map(String::trim).collect(Collectors.toList());
                    if (ToolUtil.isNotEmpty(attributeAndValues)) {
                        for (String attributeAndValue : attributeAndValues) {

                            AttributeValues value = new AttributeValues();
                            List<String> keyAndValue = Arrays.stream(attributeAndValue.split(":")).map(String::trim).collect(Collectors.toList());
                            String attributeStr = keyAndValue.get(0);
                            String valueStr = keyAndValue.get(1);
                            if (ToolUtil.isNotEmpty(attributeStr) && ToolUtil.isNotEmpty(valueStr)) {
                                Long attributeId = 0L;
                                ItemAttribute attribute = attributeService.query().eq("attribute", attributeStr).eq("category_id", categoryId).eq("display", 1).one();
                                if (ToolUtil.isEmpty(attribute)) {
                                    ItemAttribute itemAttribute = new ItemAttribute();
                                    itemAttribute.setAttribute(attributeStr);
                                    itemAttribute.setCategoryId(categoryId);
                                    attributeService.save(itemAttribute);
                                    attributeId = itemAttribute.getAttributeId();
                                } else {
                                    attributeId = attribute.getAttributeId();
                                }

                                //
                                Long valueId = 0L;
                                List<AttributeValues> attributeValuesList = valuesService.query().eq("attribute_values", valueStr).eq("attribute_id", attributeId).eq("display", 1).list();
                                if (ToolUtil.isEmpty(attributeValuesList)) {
                                    attributeValuesList = new ArrayList<>();
                                    attributeValuesList.add(null);
                                }
                                AttributeValues attributeValues = attributeValuesList.get(0);
                                if (ToolUtil.isEmpty(attributeValues)) {
                                    AttributeValues values = new AttributeValues();
                                    values.setAttributeValues(valueStr);
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
                    }
                }

                String json = null;
                if (ToolUtil.isNotEmpty(list) && list.size() > 0) {
                    list.sort(Comparator.comparing(AttributeValues::getAttributeId));
                    json = JSON.toJSONString(list);
                    newSku.setSkuValue(json);
                }

                //判断 分类 产品 型号 --------------------------------------------------------------------------------------
                for (Sku sku : skus) {
                    if (sku.getSkuName().equals(skuExcelItem.getSkuName()) && sku.getSpuId().equals(newItem.getSpuId())) {
                        for (Spu spu : spuList) {
                            if (spu.getSpuId().equals(newItem.getSpuId()) && spu.getSpuClassificationId().equals(spuClass.getSpuClassificationId())) {
                                skuExcelItem.setType("spuRepeat");
                                SkuResult results = skuService.getDetail(sku.getSkuId());
                                skuExcelItem.setUnitId(unitId);
                                skuExcelItem.setClassId(spuClass.getSpuClassificationId());
                                skuExcelItem.setSimpleResult(results);
                                skuExcelItem.setErrorSkuId(sku.getSkuId());
                                throw new ServiceException(500, "分类，产品，型号 重复");
                            }
                        }
                    }

                }

                //判断分类 产品 型号 描述-------------------------------------------------------------------------------------
                String md5 = SecureUtil.md5(newSku.getSkuValue() + newSku.getSpuId().toString() + newSku.getSkuName() + spuClass.getSpuClassificationId());
                for (Sku sku : skus) {
                    if (md5.equals(sku.getSkuValueMd5())) {
                        skuExcelItem.setType("skuRepeat");
                        skuExcelItem.setUnitId(unitId);
                        skuExcelItem.setClassId(spuClass.getSpuClassificationId());
                        skuExcelItem.setErrorSkuId(sku.getSkuId());
                        SkuResult results = skuService.getDetail(sku.getSkuId());
                        skuExcelItem.setSimpleResult(results);
                        throw new ServiceException(500, "分类，产品，型号，描述 重复");
                    }
                }
                newSku.setSkuValueMd5(md5);

                if (skuList.stream().noneMatch(item -> item.getStandard().equals(newSku.getStandard()))) {  //excel 重复数据
                    successNum++;
                    asynTaskDetail.setContentJson(JSON.toJSONString(skuExcelItem));
                    asynTaskDetail.setStatus(99);
                    asynTaskDetails.add(asynTaskDetail);
                    skuList.add(newSku);
                }

            } catch (Exception e) {
                skuExcelItem.setError(e.getMessage());
                asynTaskDetail.setStatus(50);
                asynTaskDetail.setContentJson(JSON.toJSONString(skuExcelItem));
                asynTaskDetails.add(asynTaskDetail);
                e.printStackTrace();
                logger.error("写入异常:" + "第" + skuExcelItem.getLine() + "行" + skuExcelItem + "错误" + e);   //错误异常
                errorList.add(skuExcelItem);
            }
        }
        skuService.saveBatch(skuList);
        skuExcelResult.setErrorList(errorList);
        skuExcelResult.setSuccessNum(successNum);
        asynTask.setContent(JSON.toJSONString(skuExcelResult));
        asynTask.setStatus(99);
        asynTaskDetailService.saveBatch(asynTaskDetails);
        taskService.updateById(asynTask);
    }
}
