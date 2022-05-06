package cn.atsoft.dasheng.Excel;

import cn.atsoft.dasheng.Excel.pojo.PositionBind;
import cn.atsoft.dasheng.Excel.pojo.SkuExcelItem;
import cn.atsoft.dasheng.Excel.pojo.SkuExcelResult;
import cn.atsoft.dasheng.Excel.pojo.SpuExcel;
import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.entity.Unit;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.app.service.UnitService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.orCode.pojo.InkindQrcode;
import cn.atsoft.dasheng.orCode.service.OrCodeService;
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
public class ExcelAsync {

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
    @Autowired
    private CodingRulesService codingRulesService;
    @Autowired
    private StorehousePositionsBindService bindService;
    @Autowired
    private StorehousePositionsService positionsService;
    @Autowired
    private StockDetailsService detailsService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private OrCodeService codeService;
    @Autowired
    private StorehousePositionsBindService positionsBindService;


    protected static final Logger logger = LoggerFactory.getLogger(ExcelAsync.class);


    @Async
    public void skuAdd(List<SkuExcelItem> skuExcelItemList) {

        //-------------------------------------------------------------------------------------------------------------
        List<Sku> skuList = new ArrayList<>();
        List<Sku> skus = skuService.query().eq("display", 1).list();  //所有sku
        List<SpuClassification> spuClassifications = classificationService.query().eq("display", 1).eq("type", 1).list(); //所有分类
        List<Spu> spuList = spuService.query().eq("display", 1).list();
        List<Unit> units = unitService.query().eq("display", 1).list();
        List<Category> categories = categoryService.query().eq("display", 1).list();
        CodingRules codingRules = codingRulesService.query().eq("module", "0").eq("state", 1).one();

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

                //物料编码-------------------------------------------------------------------------------------------------
                if (ToolUtil.isEmpty(skuExcelItem.getStandard())) {
                    String backCoding = codingRulesService.backCoding(codingRules.getCodingRulesId(), newItem.getSpuId());
                    backCoding = backCoding.replace("${skuClass}", ToolUtil.isEmpty(spuClass.getCodingClass()) ? "" : spuClass.getCodingClass());
                    newSku.setStandard(backCoding);
                } else {
                    skuExcelItem.setStandard(skuExcelItem.getStandard().replaceAll(" ", ""));
                    for (Sku sku : skus) {
                        if (ToolUtil.isNotEmpty(sku.getStandard()) && sku.getStandard().equals(skuExcelItem.getStandard())) {
                            SkuResult results = skuService.getDetail(sku.getSkuId());
                            skuExcelItem.setSimpleResult(results);
                            skuExcelItem.setErrorSkuId(sku.getSkuId());
                            skuExcelItem.setType("codingRepeat");
                            throw new ServiceException(500, "编码重复");
                        }
                    }
                    newSku.setStandard(skuExcelItem.getStandard());
                }

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
        asynTask.setStatus(99);
        asynTaskDetailService.saveBatch(asynTaskDetails);
        taskService.updateById(asynTask);
    }


    @Async
    public void spuAdd(List<SpuExcel> spuExcels) {
        List<Spu> spuList = spuService.query().eq("display", 1).list();
        List<Category> categoryList = categoryService.query().eq("display", 1).list();
        List<SpuClassification> spuClassList = classificationService.query().eq("display", 1).list();
        List<Unit> units = unitService.query().eq("display", 1).list();


        AsynTask asynTask = new AsynTask();
        asynTask.setType("产品导入");
        asynTask.setStatus(0);
        asynTask.setAllCount(spuExcels.size());

        taskService.save(asynTask);


        List<Spu> spus = new ArrayList<>();
        List<AsynTaskDetail> asynTaskDetails = new ArrayList<>();
        int i = 0;
        for (SpuExcel spuExcel : spuExcels) {
            i++;
            asynTask.setCount(i);
            taskService.updateById(asynTask);

            AsynTaskDetail asynTaskDetail = new AsynTaskDetail();
            asynTaskDetail.setTaskId(asynTask.getTaskId());
            asynTaskDetail.setType("产品导入");

            spuExcel.setLine(i + "");
            try {
                Spu newSpu = new Spu();
                newSpu.setCoding(spuExcel.getSpuCoding());
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
                    if (ToolUtil.isNotEmpty(spu.getCoding()) && spu.getCoding().equals(spuExcel.getSpuCoding())) {
                        throw new ServiceException(500, "产品编码已存在");
                    }
                }
                for (Spu spu : spuList) {
                    if (spuExcel.getSpuName().equals(spu.getName())&&spu.getSpuClassificationId().equals(classId)) {
                        throw new ServiceException(500, "产品名称已存在");
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
                for (Unit unit : units) {
                    if (unit.getUnitName().equals(spuExcel.getUnit())) {
                        newSpu.setUnitId(unit.getUnitId());
                        break;
                    }
                }
                if (ToolUtil.isEmpty(newSpu.getUnitId())) {
                    Unit unit = new Unit();
                    unit.setUnitName(spuExcel.getUnit());
                    unitService.save(unit);
                    newSpu.setUnitId(unit.getUnitId());
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
                asynTaskDetail.setStatus(99);
            } catch (Exception e) {
                asynTaskDetail.setStatus(50);
                spuExcel.setError(e.getMessage());
                e.printStackTrace();
            } finally {
                asynTaskDetail.setContentJson(JSON.toJSONString(spuExcel));
                asynTaskDetails.add(asynTaskDetail);
            }

        }
        asynTaskDetailService.saveBatch(asynTaskDetails);
        spuService.saveBatch(spus);
        asynTask.setStatus(99);
        taskService.updateById(asynTask);
    }


    @Async
    public void positionAdd(List<PositionBind> excels) {

        List<String> strands = new ArrayList<>();

        for (PositionBind excel : excels) {
            strands.add(excel.getStrand());
        }
        List<Sku> skuList = skuService.query().in("standard", strands).list();
        List<StorehousePositionsBind> positionsBinds = bindService.query().eq("display", 1).list();
        List<StorehousePositions> positions = positionsService.query().eq("display", 1).list();
        List<Brand> brands = brandService.list();
        List<StockDetails> stockDetailsList = new ArrayList<>();


        AsynTask asynTask = new AsynTask();
        asynTask.setType("库存导入");
        asynTask.setStatus(0);
        asynTask.setAllCount(excels.size());
        taskService.save(asynTask);

        int i = 0;

        List<AsynTaskDetail> asynTaskDetails = new ArrayList<>();
        for (PositionBind excel : excels) {
            i++;
            AsynTaskDetail asynTaskDetail = new AsynTaskDetail();
            asynTaskDetail.setTaskId(asynTask.getTaskId());
            asynTaskDetail.setType("库存导入");

            asynTask.setCount(i);
            taskService.updateById(asynTask);
            try {
                excel.setLine(i);
                //物料---------------------------------------------------------
                for (Sku sku : skuList) {
                    if (sku.getStandard().equals(excel.getStrand())) {
                        excel.setSkuId(sku.getSkuId());
                        break;
                    }
                }
                if (ToolUtil.isEmpty(excel.getSkuId())) {
                    throw new ServiceException(500, "当前物料不存在");
                }
                //  品牌-------------------------------------------------------------
                if (ToolUtil.isEmpty(excel.getBrand())) {
                    throw new ServiceException(500, "缺少品牌");
                }
                Brand brand = null;
                brand = positionsBindService.judgeBrand(excel.getBrand(), brands);
                if (ToolUtil.isEmpty(brand)) {
                    brand = new Brand();
                    brand.setBrandName(excel.getBrand());
                }
                //上级库位-------------------------------------------------------------
                StorehousePositions supper = positionsBindService.getPosition(excel.getSupperPosition(), positions);
                if (ToolUtil.isEmpty(supper)) {
                    throw new ServiceException(500, "没有上级库位");
                }
                if (ToolUtil.isEmpty(excel.getPosition())) {
                    excel.setPositionId(supper.getStorehousePositionsId());
                } else {
                    StorehousePositions position = positionsBindService.getPosition(excel.getPosition(), positions);    //库位
                    if (ToolUtil.isEmpty(position)) {
                        StorehousePositions end = new StorehousePositions();
                        end.setName(excel.getPosition());
                        end.setPid(supper.getStorehousePositionsId());
                        end.setStorehouseId(supper.getStorehouseId());
                        positionsService.save(end);
                        positions.add(end);
                        excel.setPositionId(end.getStorehousePositionsId());
                    } else {
                        excel.setPositionId(position.getStorehousePositionsId());
                    }
                }

                StorehousePositionsBind positionBind = positionsBindService.judge(excel, positionsBinds);
                if (ToolUtil.isEmpty(positionBind)) {
                    positionBind = new StorehousePositionsBind();
                    positionBind.setSkuId(excel.getSkuId());
                    positionBind.setPositionId(excel.getPositionId());
                    bindService.save(positionBind);
                    positionsBinds.add(positionBind);
                }

                if (ToolUtil.isNotEmpty(excel.getStockNumber()) && excel.getStockNumber() > 0) {
                    //库存
                    InkindQrcode inkindQrcode = codeService.ExcelBind(excel.getSkuId(), Long.valueOf(excel.getStockNumber()), brand.getBrandId());
                    StockDetails stockDetails = new StockDetails();
                    stockDetails.setSkuId(excel.getSkuId());
                    stockDetails.setNumber(Long.valueOf(excel.getStockNumber()));
                    stockDetails.setBrandId(brand.getBrandId());
                    stockDetails.setStorehouseId(supper.getStorehouseId());
                    stockDetails.setStorehousePositionsId(excel.getPositionId());
                    stockDetails.setInkindId(inkindQrcode.getInkindId());
                    stockDetails.setQrCodeId(inkindQrcode.getQrCodeId());
                    stockDetailsList.add(stockDetails);
                }

                asynTaskDetail.setStatus(99);
            } catch (Exception e) {
                logger.error(excel.getLine() + "------->" + e);
                excel.setError(e.getMessage());
                asynTaskDetail.setStatus(50);
            } finally {
                asynTaskDetail.setContentJson(JSON.toJSONString(excel));
                asynTaskDetails.add(asynTaskDetail);
            }
        }

        asynTask.setStatus(99);
        taskService.updateById(asynTask);

        detailsService.saveBatch(stockDetailsList);
        asynTaskDetailService.saveBatch(asynTaskDetails);
    }


}
