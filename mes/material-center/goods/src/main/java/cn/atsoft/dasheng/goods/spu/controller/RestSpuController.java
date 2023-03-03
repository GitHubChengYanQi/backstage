package cn.atsoft.dasheng.goods.spu.controller;


import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.goods.category.entity.RestCategory;
import cn.atsoft.dasheng.goods.category.model.result.RestCategoryResult;
import cn.atsoft.dasheng.goods.category.service.RestCategoryService;
import cn.atsoft.dasheng.goods.classz.entity.RestAttribute;
import cn.atsoft.dasheng.goods.classz.entity.RestAttributeValues;
import cn.atsoft.dasheng.goods.classz.entity.RestClass;
import cn.atsoft.dasheng.goods.classz.model.*;
import cn.atsoft.dasheng.goods.classz.service.RestAttributeService;
import cn.atsoft.dasheng.goods.classz.service.RestAttributeValuesService;
import cn.atsoft.dasheng.goods.classz.service.RestClassService;
import cn.atsoft.dasheng.goods.sku.entity.RestSku;
import cn.atsoft.dasheng.goods.sku.model.RestSkuJson;
import cn.atsoft.dasheng.goods.sku.model.RestSkuResultByRab;
import cn.atsoft.dasheng.goods.sku.model.result.RestSkuResult;
import cn.atsoft.dasheng.goods.sku.service.RestSkuService;
import cn.atsoft.dasheng.goods.spu.entity.RestSpu;
import cn.atsoft.dasheng.goods.spu.model.params.RestSpuParam;
import cn.atsoft.dasheng.goods.spu.model.result.RestSpuResult;
import cn.atsoft.dasheng.goods.spu.service.RestSpuService;
import cn.atsoft.dasheng.goods.spu.wrapper.RestSpuSelectWrapper;
import cn.atsoft.dasheng.goods.texture.entity.RestTextrue;
import cn.atsoft.dasheng.goods.texture.service.RestTextrueService;
import cn.atsoft.dasheng.goods.unit.entity.RestUnit;
import cn.atsoft.dasheng.goods.unit.model.result.RestUnitResult;
import cn.atsoft.dasheng.goods.unit.service.RestUnitService;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 控制器
 *
 * @author
 * @Date 2021-10-18 14:14:21
 */
@RestController
@RequestMapping("/spu/{version}")
@ApiVersion("2.0")
@Api(tags = "spu 管理")
public class RestSpuController extends BaseController {
    @Autowired
    private RestSpuService restSpuService;

    @Autowired
    private RestSkuService skuService;
    @Autowired
    private RestAttributeService restAttributeService;

    @Autowired
    private RestAttributeValuesService restAttributeValuesService;

    @Autowired
    private RestClassService restClassService;
    @Autowired
    private RestUnitService restUnitService;

    @Autowired
    private RestTextrueService restTextrueService;
    @Autowired
    private RestCategoryService restCategoryService;


    /**
     * 新增接口
     *
     * @Autowired
     * @author
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody RestSpuParam restSpuParam) {
        this.restSpuService.add(restSpuParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @BussinessLog(value = "修改spu", key = "name", dict = RestSpuParam.class)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody RestSpuParam restSpuParam) {

        this.restSpuService.update(restSpuParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @BussinessLog(value = "删除spu", key = "name", dict = RestSpuParam.class)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody RestSpuParam restSpuParam) {
        this.restSpuService.delete(restSpuParam);
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
    public ResponseData detail(@RequestBody RestSpuParam restSpuParam) {
        RestSpu detail = this.restSpuService.getById(restSpuParam.getSpuId());

        if (ToolUtil.isNotEmpty(detail)) {
            RestSkuResultByRab skuRequest = new RestSkuResultByRab();

            List<RestAttributeInSpu> attributeResults = new ArrayList<>();
            List<RestAttributeValueInSpu> attributeValuesResults = new ArrayList<>();
            List<Map<String, Object>> list = new ArrayList<>();

            //spu分类
            RestCategory spuClassification = detail.getSpuClassificationId() == null ? new RestCategory() : restCategoryService.getById(detail.getSpuClassificationId());


            RestSpuResult spuResult = new RestSpuResult();

            //通过spuId 找到 RestSku
            List<RestSku> skus = detail.getSpuId() == null ? new ArrayList<>() : skuService.skuResultBySpuId(detail.getSpuId());


            List<RestSkuResult> skuResultList = new ArrayList<>();

            //类目id 不为空
            if (ToolUtil.isNotEmpty(detail.getCategoryId())) {
                //通过类目Id 查到 类目属性结合

//                List<ItemAttribute> itemAttributes = detail.getCategoryId() == null ? new ArrayList<>() : itemAttributeService.lambdaQuery().in(ItemAttribute::getCategoryId, detail.getCategoryId()).and(i -> i.eq(ItemAttribute::getDisplay, 1).list();
                List<RestAttribute> itemAttributes = detail.getCategoryId() == null ? new ArrayList<>() : restAttributeService.restAttributeByCategoryId(detail.getCategoryId());

                List<Long> attId = new ArrayList<>();

                for (RestAttribute itemAttribute : itemAttributes) {
                    attId.add(itemAttribute.getAttributeId());
                }

                //通过 类目属性id 得到类目属性数据 集合
//                List<RestAttributeValues> attributeValues = attId.size() == 0 ? new ArrayList<>() : restAttributeValuesService.lambdaQuery().in(RestAttributeValues::getAttributeId, attId).and(i -> i.eq(RestAttributeValues::getDisplay, 1)).list();

                List<RestAttributeValues> attributeValues = attId.size() == 0 ? new ArrayList<>() : restAttributeValuesService.restAttributeValuesByAttributeId(attId);
                if (ToolUtil.isNotEmpty(itemAttributes)) {
                    for (RestSku sku : skus) {
                        //list
                        JSONArray jsonArray = new JSONArray();
                        List<RestAttributeValues> valuesRequests = new ArrayList<>();
                        if (ToolUtil.isNotEmpty(sku.getSkuValue())) {
                            jsonArray = JSONUtil.parseArray(sku.getSkuValue());
                            valuesRequests = JSONUtil.toList(jsonArray, RestAttributeValues.class);
                        }


                        RestSkuResult skuResult = new RestSkuResult();
                        skuResult.setSkuId(sku.getSkuId());
                        Map<String, Object> skuValueMap = new HashMap<>();
                        skuValueMap.put("id", sku.getSkuId().toString());

                        if (ToolUtil.isNotEmpty(valuesRequests)) {
                            for (RestAttributeValues valuesRequest : valuesRequests) {
                                RestAttributeInSpu itemAttributeResult = new RestAttributeInSpu();
                                itemAttributeResult.setK_s(valuesRequest.getAttributeId());
                                attributeResults.add(itemAttributeResult);
                                RestAttributeValueInSpu attributeValuesResult = new RestAttributeValueInSpu();
                                attributeValuesResult.setId(valuesRequest.getAttributeValuesId());
                                attributeValuesResult.setAttributeId(valuesRequest.getAttributeId());
                                attributeValuesResults.add(attributeValuesResult);

                                skuValueMap.put(ToolUtil.isEmpty(valuesRequest.getAttributeId()) ? "" : "s" + valuesRequest.getAttributeId().toString(), ToolUtil.isEmpty(valuesRequest.getAttributeValuesId()) ? "" : valuesRequest.getAttributeValuesId().toString());
                            }

                        }
                        list.add(skuValueMap);
                        skuResultList.add(skuResult);


                    }
                    List<RestAttributes> attributes = new ArrayList<>();
                    List<RestAttributeInSpu> tree = new ArrayList<>();
                    for (RestAttribute attribute : itemAttributes) {
                        attributes.add(new RestAttributes() {{
                            setAttribute(attribute.getAttribute());
                            setId(attribute.getAttributeId());
                            setAttributeId(attribute.getAttributeId().toString());
                        }});

                    }
                    for (RestAttributes attribute : attributes) {
                        List<RestValues> valuesList = new ArrayList<>();
                        RestAttributeInSpu attributeInSpu = new RestAttributeInSpu();
                        attributeInSpu.setK_s(Long.valueOf(attribute.getAttributeId()));
                        attributeInSpu.setK(attribute.getAttribute());
                        List<RestAttributeValueInSpu> v = new ArrayList<>();
                        for (RestAttributeValues attributeValuesResult : attributeValues) {
                            if (attribute.getId().equals(attributeValuesResult.getAttributeId())) {
                                RestValues values = new RestValues();
                                values.setAttributeValues(attributeValuesResult.getAttributeValues());
                                values.setAttributeValuesId(attributeValuesResult.getAttributeValuesId().toString());
                                values.setAttributeId(attribute.getId());
                                valuesList.add(values);

                                RestAttributeValueInSpu attributeValueInSpu = new RestAttributeValueInSpu();
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
                JSONArray jsonArray;
                List<RestAttribute> attributes = new ArrayList<>();
                if (ToolUtil.isNotEmpty(detail.getAttribute()) && detail.getAttribute() != "" && detail.getAttribute() != null) {
                    jsonArray = JSONUtil.parseArray(detail.getAttribute());
                    attributes = JSONUtil.toList(jsonArray, RestAttribute.class);
                }

                skuRequest.setList(list);

            }
            if (ToolUtil.isNotEmpty(spuClassification)) {
                RestCategoryResult spuClassificationResult = new RestCategoryResult();
                ToolUtil.copyProperties(spuClassification, spuClassificationResult);
                spuResult.setRestCategoryResult(spuClassificationResult);
            }

            spuResult.setSku(skuRequest);


            //映射材质对象
            if (ToolUtil.isNotEmpty(detail.getMaterialId())) {
                RestTextrue material = restTextrueService.getById(detail.getMaterialId());
                spuResult.setMaterial(material);
            }

            RestClass category = restClassService.getById(detail.getCategoryId());
            spuResult.setRestClass(category);

            if (ToolUtil.isNotEmpty(spuClassification)) {
                RestCategoryResult spuClassificationResult = new RestCategoryResult();
                ToolUtil.copyProperties(spuClassification, spuClassificationResult);
                spuResult.setRestCategoryResult(spuClassificationResult);
            }

            RestUnit unit = restUnitService.getById(detail.getUnitId());
            RestUnitResult unitResult = new RestUnitResult();
            ToolUtil.copyProperties(unit, unitResult);
            spuResult.setUnitResult(unitResult);

            ToolUtil.copyProperties(detail, spuResult);


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
    public PageInfo<RestSpuResult> list(@RequestBody(required = false) RestSpuParam restSpuParam) {
        if (ToolUtil.isEmpty(restSpuParam)) {
            restSpuParam = new RestSpuParam();
        }
        return this.restSpuService.findPageBySpec(restSpuParam);
    }

    /**
     * 选择列表
     *
     * @author jazz
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData listSelect(@RequestBody(required = false) RestSpuParam spuParam) {

        QueryWrapper<RestSpu> spuQueryWrapper = new QueryWrapper<>();

        if (ToolUtil.isNotEmpty(spuParam) && ToolUtil.isNotEmpty(spuParam.getSpuClassificationId()) && ToolUtil.isNotEmpty(spuParam.getProductionType()) && ToolUtil.isNotEmpty(spuParam.getName()) && ToolUtil.isNotEmpty(spuParam.getType())) {
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

        } else {

        }
        spuQueryWrapper.in("display", 1);
        List<Map<String, Object>> list = this.restSpuService.listMaps(spuQueryWrapper);
        RestSpuSelectWrapper factory = new RestSpuSelectWrapper(list);
        List<Map<String, Object>> result = factory.wrap();
        return ResponseData.success(result);
    }


}
