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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


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


        ExcelReader reader = ExcelUtil.getReader(excelFile);
        List<SkuExcelItem> skuExcelItems = reader.readAll(SkuExcelItem.class);
        List<List<Object>> read = reader.read(1);
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
//判断重复---------------------------------------------------------------------------------------------------------------
        List<String> removal = new ArrayList<>();
        for (SkuExcelItem skuExcelItem : skuExcelItems) {
            removal.add(skuExcelItem.get成品码());
        }
        if (removal.size() != skuExcelItems.size()) {
            throw new ServiceException(500, "有重复数据");
        }
//----------------------------------------------------------------------------------------------------------------------
        List<Sku> skus = new ArrayList<>();
        Integer i = 0;

        List<SkuExcelItem> errorList = new ArrayList<>();
//对比相同数据取重复数据-----------------------------------------------------------------------------------------------------------
        List<String> repeat = new ArrayList<>();
        for (SkuExcelItem skuExcelItem : skuExcelItems) {
            repeat.add(skuExcelItem.get成品码());
        }
        for (int j = 0; j < repeat.size(); j++) {
            for (SkuExcelItem skuExcelItem : skuExcelItems) {
                if (repeat.get(j).equals(skuExcelItem.get成品码())) {
                    repeat.remove(j);
                    skuExcelItems.remove(skuExcelItem);
                    errorList.add(skuExcelItem);
                    break;
                }
            }
        }


        //-------------------------------------------------------------------------------------------------------------
        List<Sku> skuList = new ArrayList<>();
        for (SkuExcelItem skuExcelItem : skuExcelItems) {
            i++;
            try {
                Sku one = skuService.query().eq("standard", skuExcelItem.get成品码()).eq("display", 1).one();
                if (ToolUtil.isEmpty(one)) {
                    Sku sku = new Sku();
                    //防止添加已有数据-------------------------------------------------------------------------------------
                    if (ToolUtil.isEmpty(skuExcelItem.get型号())) {
                        throw new ServiceException(500, "第" + i + "行 数据不完整");
                    }
                    if (ToolUtil.isEmpty(skuExcelItem.get产品())) {
                        throw new ServiceException(500, "第" + i + "行 数据不完整");
                    }

                    //编码-----------------------------------------------------------------------------------------------
                    if (ToolUtil.isEmpty(skuExcelItem.get成品码())) {
                        throw new ServiceException(500, "请保证" + i + "行" + "编码完整");
                    }
                    sku.setStandard(skuExcelItem.get成品码());


                    //分类----------------------------------------------------------------------------------------------
                    if (ToolUtil.isEmpty(skuExcelItem.get分类())) {
                        throw new ServiceException(500, "请保证" + i + "行分类完整");
                    }
                    SpuClassification classification = classificationService.query().eq("name", skuExcelItem.get分类()).eq("display", 1).one();
                    Long classId = null;
                    if (ToolUtil.isNotEmpty(classification)) {
                        classId = classification.getSpuClassificationId();
                    } else {
                        SpuClassification spuClassification = new SpuClassification();
                        spuClassification.setName(skuExcelItem.get分类());
                        classificationService.save(spuClassification);
                        classId = spuClassification.getSpuClassificationId();
                    }
                    //产品--------------------------------------------------------------------------------------------
                    if (ToolUtil.isEmpty(skuExcelItem.get产品())) {
                        throw new ServiceException(500, "请保证产品名称" + i + "行完整");
                    }
                    SpuClassification spuClassification = classificationService.query().eq("name", skuExcelItem.get产品()).eq("type", 2).one();
                    Long itemId = null;
                    if (ToolUtil.isNotEmpty(spuClassification)) {
                        itemId = spuClassification.getSpuClassificationId();
                    } else {
                        SpuClassification newClass = new SpuClassification();
                        newClass.setName(skuExcelItem.get产品());
                        newClass.setType(2L);
                        newClass.setPid(classId);
                        classificationService.save(newClass);
                        itemId = newClass.getSpuClassificationId();
                    }
                    Long spuId = null;
                    //型号
                    Spu spu = spuService.query().eq("name", skuExcelItem.get型号()).one();
                    if (ToolUtil.isNotEmpty(spu)) {
                        sku.setSpuId(spu.getSpuId());
                        spu.setSpuClassificationId(itemId);
                        spuService.updateById(spu);
                        spuId = spu.getSpuId();
                    } else {
                        Category category = new Category();
                        category.setCategoryName(skuExcelItem.get型号());
                        categoryService.save(category);

                        Spu newSpu = new Spu();
                        newSpu.setSpuClassificationId(itemId);
                        newSpu.setName(skuExcelItem.get型号());
                        newSpu.setCategoryId(category.getCategoryId());
                        spuService.save(newSpu);
                        sku.setSpuId(newSpu.getSpuId());
                        spuId = newSpu.getSpuId();
                    }
                    //单位------------------------------------------------------------------------------------------------------
                    if (ToolUtil.isEmpty(skuExcelItem.get单位())) {
                        throw new ServiceException(500, "请保证" + i + "行单位完整");
                    }
                    Unit unit = unitService.query().eq("unit_name", skuExcelItem.get单位()).eq("display", 1).one();
                    Spu spuById = spuService.getById(spuId);
                    if (ToolUtil.isNotEmpty(unit)) {
                        spuById.setUnitId(unit.getUnitId());
                    } else {
                        Unit newUnit = new Unit();
                        newUnit.setUnitName(skuExcelItem.get单位());
                        unitService.save(newUnit);
                        spuById.setUnitId(newUnit.getUnitId());
                    }
                    spuService.updateById(spuById);
                    //批量-----------------------------------------------------------------------------------------------
                    if (skuExcelItem.get是否批量().equals("是")) {
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
                    skuList.add(sku);
                }

            } catch (Exception e) {
                errorList.add(skuExcelItem);
                logger.error("第" + i + "行" + skuExcelItem + "错误");
            }
        }
        skuService.saveBatch(skuList);
        return ResponseData.success(errorList);
    }


}
