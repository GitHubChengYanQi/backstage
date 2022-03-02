package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.app.entity.Material;
import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.entity.Unit;
import cn.atsoft.dasheng.app.model.params.Attribute;
import cn.atsoft.dasheng.app.model.params.Values;
import cn.atsoft.dasheng.app.model.result.PartsResult;
import cn.atsoft.dasheng.app.model.result.UnitResult;
import cn.atsoft.dasheng.app.service.MaterialService;
import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.app.service.UnitService;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Data;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.model.params.*;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.wrapper.SpuSelectWrapper;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.beetl.ext.fn.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.awt.*;
import java.util.*;
import java.util.List;

import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;


/**
 * 控制器
 *
 * @author
 * @Date 2021-10-18 14:14:21
 */
@RestController
@RequestMapping("/spu")
@Api(tags = "")
public class SpuController extends BaseController {
    @Autowired
    private SpuService spuService;

    @Autowired
    private SkuService skuService;
    @Autowired
    private ItemAttributeService itemAttributeService;

    @Autowired
    private AttributeValuesService attributeValuesService;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UnitService unitService;

    @Autowired
    private MaterialService materialService;
    @Autowired
    private SpuClassificationService spuClassificationService;

    @Autowired
    private PartsService partsService;

    /**
     * 新增接口
     *
     * @Autowired
     * @author
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody SpuParam spuParam) {
        this.spuService.add(spuParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @BussinessLog(value = "修改spu", key = "name", dict = SpuParam.class)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody SpuParam spuParam) {

        this.spuService.update(spuParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @BussinessLog(value = "删除spu", key = "name", dict = SpuParam.class)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody SpuParam spuParam) {
        this.spuService.delete(spuParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<SpuResult> detail(@RequestBody SpuParam spuParam) {
        Spu detail = this.spuService.getById(spuParam.getSpuId());

        if (ToolUtil.isNotEmpty(detail)) {
            SkuRequest skuRequest = new SkuRequest();


            List<AttributeInSpu> attributeResults = new ArrayList<>();
            List<AttributeValueInSpu> attributeValuesResults = new ArrayList<>();
            List<Map<String, Object>> list = new ArrayList<>();

            SpuClassification spuClassification = detail.getSpuClassificationId() == null ? new SpuClassification() : spuClassificationService
                    .query().in("spu_classification_id", detail.getSpuClassificationId()).and(i -> i.eq("display", 1)).one();


            SpuResult spuResult = new SpuResult();
            List<Sku> skus = detail.getSpuId() == null ? new ArrayList<>() :
                    skuService.query().in("spu_id", detail.getSpuId()).and(i -> i.eq("display", 1)).list();
            //取skuIds 去清单查找是否存在bom
            List<Long> skuIds = new ArrayList<>();
            for (Sku sku : skus) {
                skuIds.add(sku.getSkuId());
            }
            List<Parts> parts = partsService.query().in("sku_id", skuIds).eq("status", 99).eq("display", 1).list();

            List<List<SkuJson>> requests = new ArrayList<>();
            List<SkuResult> skuResultList = new ArrayList<>();
            List<CategoryRequest> categoryRequests = new ArrayList<>();
            if (ToolUtil.isNotEmpty(detail.getCategoryId())) {
                List<ItemAttribute> itemAttributes = detail.getCategoryId() == null ? new ArrayList<>() : itemAttributeService.lambdaQuery()
                        .in(ItemAttribute::getCategoryId, detail.getCategoryId()).and(i -> i.eq(ItemAttribute::getDisplay, 1))
                        .list();
                List<Long> attId = new ArrayList<>();
                for (ItemAttribute itemAttribute : itemAttributes) {
                    attId.add(itemAttribute.getAttributeId());
                }
                List<AttributeValues> attributeValues = attId.size() == 0 ? new ArrayList<>() : attributeValuesService.lambdaQuery()
                        .in(AttributeValues::getAttributeId, attId).and(i -> i.eq(AttributeValues::getDisplay, 1))
                        .list();
                if (ToolUtil.isNotEmpty(itemAttributes)) {
                    for (Sku sku : skus) {
                        //list
                        JSONArray jsonArray = new JSONArray();
                        List<AttributeValues> valuesRequests = new ArrayList<>();
                        if (ToolUtil.isNotEmpty(sku.getSkuValue())) {
                            jsonArray = JSONUtil.parseArray(sku.getSkuValue());
                            valuesRequests = JSONUtil.toList(jsonArray, AttributeValues.class);
                        }


                        SkuResult skuResult = new SkuResult();
                        skuResult.setSkuId(sku.getSkuId());
                        Map<String, Object> skuValueMap = new HashMap<>();
                        skuValueMap.put("id", sku.getSkuId().toString());

                        for (Parts part : parts) {
                            if (sku.getSkuId().equals(part.getSkuId()) && part.getType().equals("1")) {
                                PartsResult partsResult = new PartsResult();
                                ToolUtil.copyProperties(part, partsResult);
                                skuValueMap.put("designParts", partsResult);

                            } else if (sku.getSkuId().equals(part.getSkuId()) && part.getType().equals("2")) {
                                PartsResult partsResult = new PartsResult();
                                ToolUtil.copyProperties(part, partsResult);
                                skuValueMap.put("productionParts", partsResult);
                            }
                        }
                        if (ToolUtil.isNotEmpty(valuesRequests)) {
                            for (AttributeValues valuesRequest : valuesRequests) {
                                AttributeInSpu itemAttributeResult = new AttributeInSpu();
                                itemAttributeResult.setK_s(valuesRequest.getAttributeId());
                                attributeResults.add(itemAttributeResult);
                                AttributeValueInSpu attributeValuesResult = new AttributeValueInSpu();
                                attributeValuesResult.setId(valuesRequest.getAttributeValuesId());
                                attributeValuesResult.setAttributeId(valuesRequest.getAttributeId());
                                attributeValuesResults.add(attributeValuesResult);
                                skuValueMap.put("s" + valuesRequest.getAttributeId().toString(), valuesRequest.getAttributeValuesId().toString());
                            }

                        }
                        list.add(skuValueMap);
                        skuResultList.add(skuResult);


                    }
                    List<Attribute> attributes = new ArrayList<>();
                    List<AttributeInSpu> tree = new ArrayList<>();
                    for (ItemAttribute attribute : itemAttributes) {
                        attributes.add(new Attribute() {{
                            setAttribute(attribute.getAttribute());
                            setId(attribute.getAttributeId());
                            setAttributeId(attribute.getAttributeId().toString());
                        }});

//                        AttributeInSpu attributeInSpu = new AttributeInSpu();
//                        attributeInSpu.setK_s(Long.valueOf(attribute.getAttributeId()));
//                        attributeInSpu.setK(attribute.getAttribute());
//                        List<AttributeValueInSpu> v = new ArrayList<>();
//                        for (Values attributeValue : attribute.getAttributeValues()) {
//                            AttributeValueInSpu attributeValueInSpu = new AttributeValueInSpu();
//                            attributeValueInSpu.setId(Long.valueOf(attributeValue.getAttributeValuesId()));
//                            attributeValueInSpu.setName(attributeValue.getAttributeValues());
//                            attributeValueInSpu.setAttributeId(Long.valueOf(attribute.getAttributeId()));
//                            v.add(attributeValueInSpu);
//                        }
//                        attributeInSpu.setV(v);
//                        tree.add(attributeInSpu);
                    }
                    for (Attribute attribute : attributes) {
                        List<Values> valuesList = new ArrayList<>();
                        AttributeInSpu attributeInSpu = new AttributeInSpu();
                        attributeInSpu.setK_s(Long.valueOf(attribute.getAttributeId()));
                        attributeInSpu.setK(attribute.getAttribute());
                        List<AttributeValueInSpu> v = new ArrayList<>();
                        for (AttributeValues attributeValuesResult : attributeValues) {
                            if (attribute.getId().equals(attributeValuesResult.getAttributeId())) {
                                Values values = new Values();
                                values.setAttributeValues(attributeValuesResult.getAttributeValues());
                                values.setAttributeValuesId(attributeValuesResult.getAttributeValuesId().toString());
                                values.setAttributeId(attribute.getId());
                                valuesList.add(values);

                                AttributeValueInSpu attributeValueInSpu = new AttributeValueInSpu();
                                attributeValueInSpu.setId(Long.valueOf(attributeValuesResult.getAttributeValuesId()));
                                attributeValueInSpu.setName(attributeValuesResult.getAttributeValues());
                                attributeValueInSpu.setAttributeId(Long.valueOf(attribute.getAttributeId()));
                                v.add(attributeValueInSpu);

                            }

                        }
                        attributeInSpu.setV(v);
                        tree.add(attributeInSpu);
                        attribute.setAttributeValues(valuesList);


                    }

                    skuRequest.setTree(tree);
                }
                JSONArray jsonArray = new JSONArray();
                List<Attribute> attributes = new ArrayList<>();
                if (ToolUtil.isNotEmpty(detail.getAttribute()) && detail.getAttribute() != "" && detail.getAttribute() != null) {
                    jsonArray = JSONUtil.parseArray(detail.getAttribute());
                    attributes = JSONUtil.toList(jsonArray, Attribute.class);
                }


                /**
                 * 废弃笛卡尔积计算出的tree改用上方的valuesRequests
                 */
//                List<AttributeInSpu> tree = attributeResults.stream().collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingLong(AttributeInSpu::getK_s))), ArrayList::new));
//                List<AttributeValueInSpu> treeValue = attributeValuesResults.stream().collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingLong(AttributeValueInSpu::getId))), ArrayList::new));
//                for (AttributeValueInSpu attributeValuesResult : treeValue) {
//                    for (AttributeValues attributeValue : attributeValues) {
//                        if (attributeValuesResult.getId().equals(attributeValue.getAttributeValuesId())) {
//                            attributeValuesResult.setName(attributeValue.getAttributeValues());
//                        }
//                    }
//                }
//                for (AttributeInSpu itemAttributeResult : tree) {
//                    for (ItemAttribute itemAttribute : itemAttributes) {
//                        if (itemAttributeResult.getK_s().equals(itemAttribute.getAttributeId())) {
//                            itemAttributeResult.setK(itemAttribute.getAttribute());
//                        }
//                    }
//                    List<AttributeValueInSpu> results = new ArrayList<>();
//                    for (AttributeValueInSpu attributeValuesResult : treeValue) {
//                        if (attributeValuesResult.getAttributeId().equals(itemAttributeResult.getK_s())) {
//                            results.add(attributeValuesResult);
//                        }
//                    }
//                    itemAttributeResult.setV(results);
//                }
                skuRequest.setList(list);
//                skuRequest.setTree(tree);

            }
            if (ToolUtil.isNotEmpty(spuClassification)) {
                SpuClassificationResult spuClassificationResult = new SpuClassificationResult();
                ToolUtil.copyProperties(spuClassification, spuClassificationResult);
                spuResult.setSpuClassificationResult(spuClassificationResult);
            }

            spuResult.setSku(skuRequest);


            //映射材质对象
            if (ToolUtil.isNotEmpty(detail.getMaterialId())) {
                Material material = materialService.getById(detail.getMaterialId());
                spuResult.setMaterial(material);
            }

            Category category = categoryService.getById(detail.getCategoryId());
            spuResult.setCategory(category);

            if (ToolUtil.isNotEmpty(spuClassification)) {
                SpuClassificationResult spuClassificationResult = new SpuClassificationResult();
                ToolUtil.copyProperties(spuClassification, spuClassificationResult);
                spuResult.setSpuClassificationResult(spuClassificationResult);
            }

            Unit unit = unitService.getById(detail.getUnitId());
            UnitResult unitResult = new UnitResult();
            ToolUtil.copyProperties(unit, unitResult);
            spuResult.setUnitResult(unitResult);

            ToolUtil.copyProperties(detail, spuResult);

            spuResult.setCategoryRequests(categoryRequests);

            return ResponseData.success(spuResult);
        } else {
            return null;
        }

    }

    /**
     * 查询列表
     *
     * @author
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<SpuResult> list(@RequestBody(required = false) SpuParam spuParam) {
        if (ToolUtil.isEmpty(spuParam)) {
            spuParam = new SpuParam();
        }
        return this.spuService.findPageBySpec(spuParam);
    }

    /**
     * 选择列表
     *
     * @author jazz
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData<List<Map<String, Object>>> listSelect(@RequestBody(required = false) SpuParam spuParam) {

        QueryWrapper<Spu> spuQueryWrapper = new QueryWrapper<>();

        if (ToolUtil.isNotEmpty(spuParam)) {
            if (ToolUtil.isNotEmpty(spuParam.getSpuClassificationId())) {
                spuQueryWrapper.in("spu_classification_id", spuParam.getSpuClassificationId());
            }
            if (ToolUtil.isNotEmpty(spuParam.getProductionType())) {
                spuQueryWrapper.in("production_type", spuParam.getProductionType());
            }
            if (ToolUtil.isNotEmpty(spuParam.getName())) {
                spuQueryWrapper.like("name", spuParam.getName());
            }
            if (ToolUtil.isNotEmpty(spuParam.getType())) {
                spuQueryWrapper.in("type", spuParam.getType());
            }

        }
        spuQueryWrapper.in("display", 1);
        List<Map<String, Object>> list = this.spuService.listMaps(spuQueryWrapper);
        SpuSelectWrapper factory = new SpuSelectWrapper(list);
        List<Map<String, Object>> result = factory.wrap();
        return ResponseData.success(result);
    }


}
