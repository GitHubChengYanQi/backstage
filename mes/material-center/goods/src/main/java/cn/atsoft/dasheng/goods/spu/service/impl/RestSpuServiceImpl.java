package cn.atsoft.dasheng.goods.spu.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.goods.category.entity.RestCategory;
import cn.atsoft.dasheng.goods.category.model.result.RestCategoryResult;
import cn.atsoft.dasheng.goods.category.service.RestCategoryService;
import cn.atsoft.dasheng.goods.classz.entity.RestAttribute;
import cn.atsoft.dasheng.goods.classz.entity.RestAttributeValues;
import cn.atsoft.dasheng.goods.classz.entity.RestClass;
import cn.atsoft.dasheng.goods.classz.model.RestAttributeInSpu;
import cn.atsoft.dasheng.goods.classz.model.RestAttributeValueInSpu;
import cn.atsoft.dasheng.goods.classz.service.RestAttributeService;
import cn.atsoft.dasheng.goods.classz.service.RestAttributeValuesService;
import cn.atsoft.dasheng.goods.classz.service.RestClassService;
import cn.atsoft.dasheng.goods.sku.entity.RestSku;
import cn.atsoft.dasheng.goods.sku.mapper.RestSkuMapper;
import cn.atsoft.dasheng.goods.sku.model.RestSkuResultByRab;
import cn.atsoft.dasheng.goods.sku.model.result.RestSkuResult;
import cn.atsoft.dasheng.goods.sku.service.RestSkuService;
import cn.atsoft.dasheng.goods.spu.entity.RestSpu;
import cn.atsoft.dasheng.goods.spu.mapper.RestSpuMapper;
import cn.atsoft.dasheng.goods.spu.model.params.RestSpuParam;
import cn.atsoft.dasheng.goods.spu.model.result.RestSpuResult;
import cn.atsoft.dasheng.goods.spu.service.RestSpuService;
import cn.atsoft.dasheng.goods.texture.entity.RestTextrue;
import cn.atsoft.dasheng.goods.texture.service.RestTextrueService;
import cn.atsoft.dasheng.goods.unit.entity.RestUnit;
import cn.atsoft.dasheng.goods.unit.model.result.RestUnitResult;
import cn.atsoft.dasheng.goods.unit.service.RestUnitService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author
 * @since 2021-10-18
 */
@Service
public class RestSpuServiceImpl extends ServiceImpl<RestSpuMapper, RestSpu> implements RestSpuService {
    @Autowired
    private RestSpuService spuService;

    @Autowired
    private RestSkuService skuService;
    @Autowired
    private RestAttributeService itemAttributeService;

    @Autowired
    private RestAttributeValuesService attributeValuesService;

    @Autowired
    private RestClassService categoryService;

    @Autowired
    private RestUnitService unitService;

    @Autowired
    private RestTextrueService materialService;
    @Autowired
    private RestCategoryService spuClassificationService;


    //    backBatchCode
    @Transactional
    @Override
    public Long add(RestSpuParam param) {


        RestSpu entity = getEntity(param);
        //查询判断是否有相同名称spu
        Integer count = this.query().eq("name", param.getName()).eq("display", 1).count();
        if (count > 0) {
            throw new ServiceException(500, "产品名称重复,请更换");
        }
        this.save(entity);
        List<List<String>> result = new ArrayList<List<String>>();
        return entity.getSpuId();


    }

    @Override
    public ResponseData detail(RestSpuParam restSpuParam) {

        RestSpu detail = this.spuService.getById(restSpuParam.getSpuId());
        RestSkuResultByRab skuRequest = new RestSkuResultByRab();

        List<RestAttributeInSpu> attributeResults = new ArrayList<>();
        List<RestAttributeValueInSpu> attributeValuesResults = new ArrayList<>();
        List<Map<String, Object>> list = new ArrayList<>();

        RestSpuResult spuResult = new RestSpuResult();

        //通过spuId 找到 skuResult
        List<RestSku> skus = detail.getSpuId() == null ? new ArrayList<>() : skuService.skuResultBySpuId(detail.getSpuId());

        List<RestSkuResult> skuResultList = new ArrayList<>();
        if (ToolUtil.isNotEmpty(detail.getCategoryId())) {
            List<RestAttribute> itemAttributes = detail.getCategoryId() == null ? new ArrayList<>() : itemAttributeService.restAttributeByCategoryId(detail.getCategoryId());
            List<Long> attId = new ArrayList<>();
            for (RestAttribute itemAttribute : itemAttributes) {
                attId.add(itemAttribute.getAttributeId());
            }
//            List<RestAttributeValues> attributeValues = attId.size() == 0 ? new ArrayList<>() : attributeValuesService.lambdaQuery()
//                    .in(RestAttributeValues::getAttributeId, attId)
//                    .list();
            List<RestAttributeValues> attributeValues = attId.size() == 0 ? new ArrayList<>() : attributeValuesService.restAttributeValuesByAttributeId(attId);
            if (ToolUtil.isNotEmpty(itemAttributes)) {
                for (RestSku sku : skus) {
                    //list
                    JSONArray jsonArray = JSONUtil.parseArray(sku.getSkuValue());
                    List<RestAttributeValues> valuesRequests = JSONUtil.toList(jsonArray, RestAttributeValues.class);
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
                            skuValueMap.put("s" + valuesRequest.getAttributeId().toString(), valuesRequest.getAttributeValuesId().toString());
                        }

                    }
                    list.add(skuValueMap);
                    skuResultList.add(skuResult);

                }
            }
        }
//            List<AttributeInSpu> tree = attributeResults.stream().collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingLong(AttributeInSpu::getK_s))), ArrayList::new));
//            List<AttributeValueInSpu> treeValue = attributeValuesResults.stream().collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingLong(AttributeValueInSpu::getId))), ArrayList::new));
//            for (AttributeValueInSpu attributeValuesResult : treeValue) {
//                for (RestAttributeValues attributeValue : attributeValues) {
//                    if (attributeValuesResult.getId().equals(attributeValue.getAttributeValuesId())) {
//                        attributeValuesResult.setName(attributeValue.getAttributeValues());
//                    }
//                }
//            }
//            for (AttributeInSpu itemAttributeResult : tree) {
//                for (RestAttribute itemAttribute : itemAttributes) {
//                    if (itemAttributeResult.getK_s().equals(itemAttribute.getAttributeId())) {
//                        itemAttributeResult.setK(itemAttribute.getAttribute());
//                        itemAttributeResult.setSort(itemAttribute.getSort());
//                    }
//                }
//                List<AttributeValueInSpu> results = new ArrayList<>();
//                for (AttributeValueInSpu attributeValuesResult : treeValue) {
//                    if (attributeValuesResult.getAttributeId().equals(itemAttributeResult.getK_s())) {
//                        results.add(attributeValuesResult);
//                    }
//                }
//                itemAttributeResult.setV(results);
//            }
//            Collections.sort(tree);
//            skuRequest.setList(list);
//            skuRequest.setTree(tree);
//        }


//        spuResult.setSku(skuRequest);


            //映射材质对象
//        if (ToolUtil.isNotEmpty(detail.getMaterialId())) {
//            RestTextrue material = materialService.getById(detail.getMaterialId());
//            spuResult.setMaterial(material);
//        }
//
//        RestClass category = categoryService.getById(detail.getCategoryId());
//        spuResult.setCategory(category);
//
//        RestUnit unit = unitService.getById(detail.getUnitId());
//        RestUnitResult unitResult = new RestUnitResult();
//        ToolUtil.copyProperties(unit, unitResult);
//        spuResult.setUnitResult(unitResult);
//
//        ToolUtil.copyProperties(detail, spuResult);

//

        return ResponseData.success(spuResult);
    }

//    static void descartes1(List<RestAttribute> dimvalue, List<List<String>> result, int layer, List<String> curList) {
//        if (layer < dimvalue.size() - 1) {
//            if (dimvalue.get(layer).getAttributeValues().size() == 0) {
//                descartes1(dimvalue, result, layer + 1, curList);
//            } else {
//                for (int i = 0; i < dimvalue.get(layer).getAttributeValues().size(); i++) {
//                    List<String> list = new ArrayList<String>(curList);
//                    list.add(dimvalue.get(layer).getAttributeId() + ":" + dimvalue.get(layer).getAttributeValues().get(i).getAttributeValuesId());
//                    descartes1(dimvalue, result, layer + 1, list);
//                }
//            }
//        } else if (layer == dimvalue.size() - 1) {
//            if (dimvalue.get(layer).getAttributeValues().size() == 0) {
//                result.add(curList);
//            } else {
//                for (int i = 0; i < dimvalue.get(layer).getAttributeValues().size(); i++) {
//                    List<String> list = new ArrayList<String>(curList);
//                    list.add(dimvalue.get(layer).getAttributeId() + ":" + dimvalue.get(layer).getAttributeValues().get(i).getAttributeValuesId());
//                    result.add(list);
//                }
//            }
//        }
//    }

    @Override

    public void delete(RestSpuParam param) {
        param.setDisplay(0);
        RestSpu entity = this.getEntity(param);
        spuService.updateById(entity);
    }

    @Override
    public void update(RestSpuParam param) {
        if (ToolUtil.isNotEmpty(param.getSpuAttributes()) && ToolUtil.isNotEmpty(param.getSpuAttributes().getSpuRequests())) {
            String toJSONString = JSON.toJSONString(param.getSpuAttributes().getSpuRequests());
            param.setAttribute(toJSONString);
        }

        RestSpu oldEntity = getOldEntity(param);
        RestSpu newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
//        Long spuClassificationId = this.getSpuClass(param);
//        newEntity.setSpuClassificationId(spuClassificationId);
        this.updateById(newEntity);
    }

    /**
     * 查询产品 新建或返回已有产品id
     *
     * @param param
     * @return
     */
    private Long getSpuClass(RestSpuParam param) {
        Long spuClassificationId = 0L;
        RestCategory spuClassification = spuClassificationService.lambdaQuery().eq(RestCategory::getName, param.getSpuClassification().getName()).and(i -> i.eq(RestCategory::getDisplay, 1)).one();
        if (ToolUtil.isEmpty(spuClassification)) {
            RestCategory spuClassificationEntity = new RestCategory();
            spuClassificationEntity.setName(param.getSpuClassification().getName());
            spuClassificationEntity.setType(2L);
            spuClassificationEntity.setPid(param.getSpuClass());
            spuClassificationService.save(spuClassificationEntity);
            spuClassificationId = spuClassificationEntity.getSpuClassificationId();
        } else {
            spuClassificationId = spuClassification.getSpuClassificationId();
        }
        return spuClassificationId;
    }

    @Override
    public RestSpuResult findBySpec(RestSpuParam param) {
        return null;
    }

    @Override
    public List<RestSpuResult> findListBySpec(RestSpuParam param) {
        return null;
    }

    @Override
    public PageInfo<RestSpuResult> findPageBySpec(RestSpuParam param) {
        Page<RestSpuResult> pageContext = getPageContext();
        IPage<RestSpuResult> page = this.baseMapper.customPageList(pageContext, param);
        this.format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private void format(List<RestSpuResult> param) {
        List<Long> categoryIds = new ArrayList<>();
        List<Long> unitIds = new ArrayList<>();
        List<Long> classIds = new ArrayList<>();
        for (RestSpuResult spuResult : param) {
            categoryIds.add(spuResult.getCategoryId());
            unitIds.add(spuResult.getUnitId());
            classIds.add(spuResult.getSpuClassificationId());
        }
        QueryWrapper<RestClass> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.lambda().in(RestClass::getCategoryId, categoryIds).in(RestClass::getDisplay, 1);
        List<RestClass> categoryList = categoryIds.size() == 0 ? new ArrayList<>() : categoryService.list(categoryQueryWrapper);

        List<RestUnit> units = unitIds.size() == 0 ? new ArrayList<>() : unitService.lambdaQuery().in(RestUnit::getUnitId, unitIds).list();


        List<RestCategory> spuClassifications = classIds.size() == 0 ? new ArrayList<>() : spuClassificationService.query().in("spu_classification_id", classIds).list();

        for (RestSpuResult spuResult : param) {
            if (ToolUtil.isNotEmpty(spuResult.getCategoryId())) {
                for (RestClass category : categoryList) {
                    if (spuResult.getCategoryId().equals(category.getCategoryId())) {
//                        spuResult.setCategory(category);
                        break;
                    }
                }
            }
            for (RestUnit unit : units) {
                if (spuResult.getUnitId() != null && spuResult.getUnitId().equals(unit.getUnitId())) {
                    RestUnitResult unitResult = new RestUnitResult();
                    ToolUtil.copyProperties(unit, unitResult);
                    spuResult.setUnitResult(unitResult);
                    break;
                }
            }

            for (RestCategory spuClassification : spuClassifications) {
                if (spuClassification.getSpuClassificationId().equals(spuResult.getSpuClassificationId())) {
                    RestCategoryResult spuClassificationResult = new RestCategoryResult();
                    ToolUtil.copyProperties(spuClassification, spuClassificationResult);
                    spuResult.setRestCategoryResult(spuClassificationResult);
                    break;
                }
            }
        }
    }

    private Serializable getKey(RestSpuParam param) {
        return param.getSpuId();
    }

    private Page<RestSpuResult> getPageContext() {
        List<String> list = new ArrayList<>();
        list.add("createTime");
        list.add("spuName");
        return PageFactory.defaultPage(list);
    }

    private RestSpu getOldEntity(RestSpuParam param) {
        return this.getById(getKey(param));
    }

    private RestSpu getEntity(RestSpuParam param) {
        RestSpu entity = new RestSpu();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public void skuFormat(List<RestSkuResult> skuResults) {
        List<Long> spuIds = new ArrayList<>();
        for (RestSkuResult skuResult : skuResults) {
            spuIds.add(skuResult.getSpuId());
        }
        List<RestSpu> spus = spuIds.size() == 0 ? new ArrayList<>() : this.listByIds(spuIds);

        for (RestSkuResult skuResult : skuResults) {
            for (RestSpu spu : spus) {
                if (skuResult.getSpuId().equals(spu.getSpuId())) {
                    skuResult.setSpuName(spu.getName());
                    break;
                }
            }
        }
    }

}
