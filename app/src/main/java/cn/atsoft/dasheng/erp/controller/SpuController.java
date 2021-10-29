package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.app.entity.Material;
import cn.atsoft.dasheng.app.entity.Unit;
import cn.atsoft.dasheng.app.model.params.Attribute;
import cn.atsoft.dasheng.app.model.params.Values;
import cn.atsoft.dasheng.app.model.result.UnitResult;
import cn.atsoft.dasheng.app.service.MaterialService;
import cn.atsoft.dasheng.app.service.UnitService;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.AttributeValues;
import cn.atsoft.dasheng.erp.entity.Category;
import cn.atsoft.dasheng.erp.entity.ItemAttribute;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.entity.Spu;
import cn.atsoft.dasheng.erp.model.params.*;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.service.AttributeValuesService;
import cn.atsoft.dasheng.erp.service.CategoryService;
import cn.atsoft.dasheng.erp.service.ItemAttributeService;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.SpuService;
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

import java.util.*;

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

        List<ItemAttributeResult> attributeResults = new ArrayList<>();
        List<AttributeValuesResult> attributeValuesResults=new ArrayList<>();

        SpuResult spuResult = new SpuResult();
        List<Sku> skus = detail.getSpuId() == null ? new ArrayList<>() :
                skuService.query().in("spu_id", detail.getSpuId()).list();
        List<List<SkuJson>> requests = new ArrayList<>();
        SkuRequest skuRequests = new SkuRequest();
        List<SkuResult> skuResultList = new ArrayList<>();
        List<CategoryRequest> categoryRequests = new ArrayList<>();
        if (ToolUtil.isNotEmpty(detail.getCategoryId())) {
            List<ItemAttribute> itemAttributes = detail.getCategoryId() == null ? new ArrayList<>() : itemAttributeService.lambdaQuery()
                    .in(ItemAttribute::getCategoryId, detail.getCategoryId())
                    .list();
            List<Long> attId = new ArrayList<>();
            for (ItemAttribute itemAttribute : itemAttributes) {
                attId.add(itemAttribute.getAttributeId());
            }
            List<AttributeValues> attributeValues = attId.size() == 0 ? new ArrayList<>() : attributeValuesService.lambdaQuery()
                    .in(AttributeValues::getAttributeId, attId)
                    .list();
            if (ToolUtil.isNotEmpty(itemAttributes)) {
                for (Sku sku : skus) {
                    //list
                    JSONArray jsonArray = JSONUtil.parseArray(sku.getSkuValue());
                    List<AttributeValues> valuesRequests = JSONUtil.toList(jsonArray, AttributeValues.class);
                    SkuResult skuResult = new SkuResult();
                    skuResult.setSkuId(sku.getSkuId());
                    Map<String,String> skuValueMap = new HashMap<>();
                    if (ToolUtil.isNotEmpty(valuesRequests)) {
                        for (AttributeValues valuesRequest : valuesRequests) {
                            ItemAttributeResult itemAttributeResult = new ItemAttributeResult();
                            itemAttributeResult.setAttributeId(valuesRequest.getAttributeId());
                            attributeResults.add(itemAttributeResult);
                            AttributeValuesResult attributeValuesResult = new AttributeValuesResult();
                            attributeValuesResult.setAttributeValuesId(valuesRequest.getAttributeValuesId());
                            attributeValuesResult.setAttributeId(valuesRequest.getAttributeId());
                            attributeValuesResults.add(attributeValuesResult);
                            skuValueMap.put(valuesRequest.getAttributeId().toString(),valuesRequest.getAttributeValuesId().toString());
                        }

                    }
                    skuResult.setSkuValuesMap(skuValueMap);
                    skuResultList.add(skuResult);

                }
                skuRequests.setSpuRequests(requests);
            }
            List<ItemAttributeResult> tree = attributeResults.stream().collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingLong(ItemAttributeResult::getAttributeId))), ArrayList::new));
            List<AttributeValuesResult> treeValue = attributeValuesResults.stream().collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingLong(AttributeValuesResult::getAttributeValuesId))), ArrayList::new));
            for (AttributeValuesResult attributeValuesResult : treeValue) {
                for (AttributeValues attributeValue : attributeValues) {
                    if (attributeValuesResult.getAttributeValuesId().equals(attributeValue.getAttributeValuesId())) {
                        attributeValuesResult.setAttributeValues(attributeValue.getAttributeValues());
                    }
                }
            }
            for (ItemAttributeResult itemAttributeResult : tree) {
                for (ItemAttribute itemAttribute : itemAttributes) {
                    if(itemAttributeResult.getAttributeId().equals(itemAttribute.getAttributeId())){
                        itemAttributeResult.setAttribute(itemAttribute.getAttribute());
                    }
                }


            }
        }
        spuResult.setSkuResult(skuResultList);





        //映射材质对象
        if (ToolUtil.isNotEmpty(detail.getMaterialId())) {
            Material material = materialService.getById(detail.getMaterialId());
            spuResult.setMaterial(material);
        }

        Category category = categoryService.getById(detail.getCategoryId());
        spuResult.setCategory(category);

        Unit unit = unitService.getById(detail.getUnitId());
        UnitResult unitResult = new UnitResult();
        ToolUtil.copyProperties(unit, unitResult);
        spuResult.setUnitResult(unitResult);

        ToolUtil.copyProperties(detail, spuResult);

        spuResult.setCategoryRequests(categoryRequests);
        spuResult.setSpuAttributes(skuRequests);
        return ResponseData.success(spuResult);
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

        if (ToolUtil.isNotEmpty(spuParam)){
            if (ToolUtil.isNotEmpty(spuParam.getSpuClassificationId())){
                spuQueryWrapper.in("spu_classification_id",spuParam.getSpuClassificationId());
            }
            if (ToolUtil.isNotEmpty(spuParam.getProductionType())){
                spuQueryWrapper.in("production_type",spuParam.getProductionType());
            }
        }

        List<Map<String, Object>> list = this.spuService.listMaps(spuQueryWrapper);
        SpuSelectWrapper factory = new SpuSelectWrapper(list);
        List<Map<String, Object>> result = factory.wrap();
        return ResponseData.success(result);
    }


}
