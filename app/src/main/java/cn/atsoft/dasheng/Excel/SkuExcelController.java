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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.midi.Soundbank;
import javax.transaction.Transactional;
import java.io.File;
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
    private CategoryService categoryService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private SpuClassificationService classificationService;

    /**
     * 上传excel填报
     */
    @RequestMapping("/importSku")
    @ResponseBody
    @Transactional
    public ResponseData uploadExcel(@RequestParam("file") MultipartFile file) {
        String name = file.getOriginalFilename();
        String fileSavePath = ConstantsContext.getFileUploadPath();
        try {
            File excelFile = new File(fileSavePath + name);
            file.transferTo(excelFile);
            try {
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

                if (skuExcelItems.size() > 50) {
                    throw new ServiceException(500, "最多只可导入50个");
                }
//判断重复---------------------------------------------------------------------------------------------------------------
                List<String> removal = new ArrayList<>();
                for (SkuExcelItem skuExcelItem : skuExcelItems) {
                    removal.add(skuExcelItem.get型号() + skuExcelItem.get物料名称());
                }
                if (removal.size() != skuExcelItems.size()) {
                    throw new ServiceException(500, "有重复数据");
                }
//----------------------------------------------------------------------------------------------------------------------
                List<Sku> skus = new ArrayList<>();
                Long spuId = null;
                for (SkuExcelItem skuExcelItem : skuExcelItems) {
                    //防止添加已有数据-------------------------------------------------------------------------------------
                    Sku sku1 = skuService.query().eq("sku_name", skuExcelItem.get型号()).inSql("spu_id", "select spu_id from goods_spu where name ='" + skuExcelItem.get物料名称() + "'").one();
                    if (ToolUtil.isNotEmpty(sku1)) {
                        break;
                    }
                    //型号
                    Sku sku = new Sku();
                    sku.setSkuName(skuExcelItem.get型号());
                    //编码-----------------------------------------------------------------------------------------------
                    if (ToolUtil.isEmpty(skuExcelItem.get编码())) {
                        throw new ServiceException(500, "请保证编码完整");
                    }
                    Sku one = skuService.query().eq("standard", skuExcelItem.get编码()).eq("display", 1).one();
                    if (ToolUtil.isNotEmpty(one)) {
                        throw new ServiceException(500, "编码 {" + skuExcelItem.get编码() + "}重复");
                    }
                    sku.setStandard(skuExcelItem.get编码());
                    //物料名称--------------------------------------------------------------------------------------------
                    if (ToolUtil.isEmpty(skuExcelItem.get物料名称())) {
                        throw new ServiceException(500, "请保证物料名称完整");
                    }
                    //分类----------------------------------------------------------------------------------------------
                    if (ToolUtil.isEmpty(skuExcelItem.get分类())) {
                        throw new ServiceException(500, "请保证分类完整");
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
                    Spu spu = spuService.query().eq("name", skuExcelItem.get物料名称()).eq("display", 1).one();
                    if (ToolUtil.isNotEmpty(spu)) {
                        sku.setSpuId(spu.getSpuId());
                        spuId = spu.getSpuId();
                    } else {
                        Spu newSpu = new Spu();
                        newSpu.setName(skuExcelItem.get物料名称());
                        newSpu.setSpuClassificationId(classId);
                        spuService.save(newSpu);
                        spuId = newSpu.getSpuId();
                        sku.setSpuId(newSpu.getSpuId());
                    }
                    //单位-----------------------------------------------------------------------------------------------
                    if (ToolUtil.isEmpty(skuExcelItem.get单位())) {
                        throw new ServiceException(500, "请保证单位完整");
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
                    skus.add(sku);
                }
                skuService.saveBatch(skus);
                return ResponseData.success();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            log.error("上传那文件出错！", e);
            throw new ServiceException(BizExceptionEnum.UPLOAD_ERROR);
        }
        return null;
    }
}
