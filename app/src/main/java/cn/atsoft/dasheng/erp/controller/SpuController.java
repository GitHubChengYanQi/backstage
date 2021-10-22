package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.app.entity.Unit;
import cn.atsoft.dasheng.app.model.result.UnitResult;
import cn.atsoft.dasheng.app.service.UnitService;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.AttributeValues;
import cn.atsoft.dasheng.erp.entity.Category;
import cn.atsoft.dasheng.erp.entity.ItemAttribute;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.entity.Spu;
import cn.atsoft.dasheng.erp.model.params.CategoryRequest;
import cn.atsoft.dasheng.erp.model.params.SkuJson;
import cn.atsoft.dasheng.erp.model.params.SkuValuesRequest;
import cn.atsoft.dasheng.erp.model.params.SpuParam;
import cn.atsoft.dasheng.erp.model.result.AttributeValuesResult;
import cn.atsoft.dasheng.erp.model.result.ItemAttributeResult;
import cn.atsoft.dasheng.erp.model.result.ItemAttributeValueResult;
import cn.atsoft.dasheng.erp.model.result.SpuResult;
import cn.atsoft.dasheng.erp.service.AttributeValuesService;
import cn.atsoft.dasheng.erp.service.CategoryService;
import cn.atsoft.dasheng.erp.service.ItemAttributeService;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.SpuService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.wrapper.SpuSelectWrapper;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.alibaba.fastjson.JSON;
import org.beetl.ext.fn.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
        SpuResult spuResult = new SpuResult();
        List<Sku> skus = skuService.query().in("spu_id", detail.getSpuId()).list();
        List<SkuJson> skuJsons = new ArrayList<>();
        for (Sku sku : skus) {
            SkuJson skuJson = JSON.parseObject(sku.getSkuValue(), SkuJson.class);
            skuJsons.add(skuJson);
        }
        List<Long> valueIds = new ArrayList<>();
        for (SkuJson skuJson : skuJsons) {
            for (SkuValuesRequest skuValuesRequest : skuJson.getSkuValuesRequests()) {
//                valueIds.add(skuValuesRequest.getAttributeValueId());

            }
        }
        List<AttributeValues> attributeValuesList = valueIds.size() == 0 ? new ArrayList<>() : attributeValuesService.query().in("attribute_values_id", valueIds).list();
        List<Long> attributeIds = new ArrayList<>();

        List<AttributeValuesResult> attributeValuesResultList = new ArrayList<>();

        for (AttributeValues attributeValues : attributeValuesList) {
            AttributeValuesResult attributeValuesResult = new AttributeValuesResult();
            ToolUtil.copyProperties(attributeValues, attributeValuesResult);
            attributeValuesResultList.add(attributeValuesResult);
            attributeIds.add(attributeValues.getAttributeId());

        }
        List<ItemAttribute> attributes = attributeIds.size() == 0 ? new ArrayList<>() : itemAttributeService.query().in("attribute_id", attributeIds).list();
        for (AttributeValuesResult attributeValuesResult : attributeValuesResultList) {
            for (ItemAttribute attribute : attributes) {
                if (attributeValuesResult.getAttributeId().equals(attribute.getAttributeId())) {
                    ItemAttributeResult itemAttributeResult = new ItemAttributeResult();
                    ToolUtil.copyProperties(attribute, itemAttributeResult);
                    attributeValuesResult.setItemAttributeResult(itemAttributeResult);
                }
            }
        }
        //---------------------------------------------------------------------------------------------------------
        List<CategoryRequest> categoryRequests = new ArrayList<>();
        if (ToolUtil.isNotEmpty(detail.getCategoryId())) {

            List<ItemAttribute> itemAttributes = itemAttributeService.lambdaQuery()
                    .in(ItemAttribute::getCategoryId, detail.getCategoryId())
                    .list();


            if (ToolUtil.isNotEmpty(itemAttributes)) {
                List<Long> attId = new ArrayList<>();
                for (ItemAttribute itemAttribute : itemAttributes) {
                    attId.add(itemAttribute.getAttributeId());
                }
                List<AttributeValues> attributeValues = attributeValuesService.lambdaQuery()
                        .in(AttributeValues::getAttributeId, attId)
                        .list();
                List<SkuValuesRequest> skuDetail = new ArrayList<>();
                for (ItemAttribute itemAttribute : itemAttributes) {
                    CategoryRequest categoryRequest = new CategoryRequest();
                    categoryRequest.setAttribute(itemAttribute);
                    List<AttributeValues> attributeValuesResult = new ArrayList<>();

                    for (AttributeValues attributeValue : attributeValues) {
                        if (itemAttribute.getAttributeId().equals(attributeValue.getAttributeId())) {
                            attributeValuesResult.add(attributeValue);
                        }
                    }
                    categoryRequest.setValue(attributeValuesResult);
                    categoryRequests.add(categoryRequest);
                }
            }
        }

        Category category = categoryService.getById(detail.getCategoryId());
        spuResult.setCategory(category);

        Unit unit = unitService.getById(detail.getUnitId());
        UnitResult unitResult = new UnitResult();
        ToolUtil.copyProperties(unit, unitResult);
        spuResult.setUnitResult(unitResult);

        ToolUtil.copyProperties(detail, spuResult);

        spuResult.setCategoryRequests(categoryRequests);
        spuResult.setItemAttributeResults(attributeValuesResultList);
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
    public ResponseData<List<Map<String, Object>>> listSelect() {
        List<Map<String, Object>> list = this.spuService.listMaps();

        SpuSelectWrapper factory = new SpuSelectWrapper(list);
        List<Map<String, Object>> result = factory.wrap();
        return ResponseData.success(result);
    }


}


