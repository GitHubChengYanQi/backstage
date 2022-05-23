package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.Material;
import cn.atsoft.dasheng.app.entity.Unit;
import cn.atsoft.dasheng.app.model.params.Attribute;
import cn.atsoft.dasheng.app.model.params.Values;
import cn.atsoft.dasheng.app.model.result.UnitResult;
import cn.atsoft.dasheng.app.service.MaterialService;
import cn.atsoft.dasheng.app.service.UnitService;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.mapper.SpuMapper;
import cn.atsoft.dasheng.erp.model.params.*;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.orCode.service.OrCodeService;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.sf.jsqlparser.statement.select.ValuesList;
import org.beetl.ext.fn.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class SpuServiceImpl extends ServiceImpl<SpuMapper, Spu> implements SpuService {
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
    private OrCodeService orCodeService;


    //    backBatchCode
    @Transactional
    @Override
    public Long add(SpuParam param) {


        Spu entity = getEntity(param);
        //查询判断是否有相同名称spu
        Integer count = this.query().eq("name", param.getName()).eq("display", 1).count();
        if (count > 0) {
            throw new ServiceException(500, "产品名称重复,请更换");
        }
//        if (ToolUtil.isNotEmpty(param.getSpuAttributes()) && ToolUtil.isNotEmpty(param.getSpuAttributes().getSpuRequests())) {
//            String toJSONString = JSON.toJSONString(param.getSpuAttributes().getSpuRequests());
//            entity.setAttribute(toJSONString);
//        } else {
//            throw new ServiceException(500, "请配置属性！");
//        }

        /**
         * 绑定产品
         */
//        Long spuClassificationId = this.getSpuClass(param);
//        entity.setSpuClassificationId(spuClassificationId);
        this.save(entity);
        List<List<String>> result = new ArrayList<List<String>>();
//        param.getSpuAttributes().getSpuRequests().sort(Comparator.comparing(Attribute::getAttributeId));

//        if (ToolUtil.isNotEmpty(param.getSpuAttributes().getSpuRequests())) {
//            descartes1(param.getSpuAttributes().getSpuRequests(), result, 0, new ArrayList<String>());
//            List<Sku> skuList = new ArrayList<>();
//            List<String> toJsonSkuValue = new ArrayList<>();
//            List<String> skuValues = new ArrayList<>();
//            for (List<String> attributeValues : result) {
//                List<AttributeValues> valuesList = new ArrayList<>();
//                for (String attributeValue : attributeValues) {
//                    List<String> skuName = Arrays.asList(attributeValue.split(":"));
//                    AttributeValues values = new AttributeValues();
//                    values.setAttributeId(Long.valueOf(skuName.get(0)));
//                    values.setAttributeValuesId(Long.valueOf(skuName.get(1)));
//                    valuesList.add(values);
//                }
//                toJsonSkuValue.add(JSON.toJSONString(valuesList));
//
//            }
//
//            for (SkuParam sku : param.getSpuAttributes().getValues()) {
//                List<AttributeValues> valuesList = new ArrayList<>();
//                sku.getAttributeValues().sort(Comparator.comparing(AttributeValuesParam::getAttributeId));
//                for (AttributeValuesParam values : sku.getAttributeValues()) {
//                    AttributeValues value = new AttributeValues();
//                    value.setAttributeId(values.getAttributeId());
//                    value.setAttributeValuesId(values.getAttributeValuesId());
//                    valuesList.add(value);
//                }
//                String s = JSON.toJSONString(valuesList);
//                for (String s1 : toJsonSkuValue) {
//                    if (s1.equals(s)) {
//                        Sku skuEntry = new Sku();
//                        skuEntry.setSkuValue(s);
//                        skuEntry.setSkuValueMd5(SecureUtil.md5(entity.getSpuId() + sku.getSkuValue()));
//                        skuEntry.setSpuId(entity.getSpuId());
//                        if (ToolUtil.isNotEmpty(sku.getIsBan())) {
//                            skuEntry.setIsBan(sku.getIsBan());
//                        }
//                        skuEntry.setSpuId(entity.getSpuId());
//                        if (ToolUtil.isNotEmpty(sku.getSkuName())) {
//                            skuEntry.setSkuName(sku.getSkuName());
//                        }
//
//                        skuList.add(skuEntry);
//                    }
//                }
//            }
//            if (toJsonSkuValue.size() == skuList.size()) {
////                    skuService.saveBatch(skuList);
//            } else {
//                throw new ServiceException(500, "计算有误请重试");
//            }
//            List<Sku> list = skuService.lambdaQuery().in(Sku::getSpuId, entity.getSpuId()).list();
//            List<Long> skuIds = new ArrayList<>();
//            for (Sku sku : list) {
//                skuIds.add(sku.getSkuId());
//            }
////                orCodeService.backBatchCode(skuIds, "sku");
//        }
        return entity.getSpuId();


    }

    @Override
    public ResponseData<SpuResult> detail(SpuParam spuParam) {

        Spu detail = this.spuService.getById(spuParam.getSpuId());
        SkuRequest skuRequest = new SkuRequest();

        List<AttributeInSpu> attributeResults = new ArrayList<>();
        List<AttributeValueInSpu> attributeValuesResults = new ArrayList<>();
        List<Map<String, Object>> list = new ArrayList<>();

        SpuResult spuResult = new SpuResult();
        List<Sku> skus = detail.getSpuId() == null ? new ArrayList<>() :
                skuService.query().in("spu_id", detail.getSpuId()).list();
        List<List<SkuJson>> requests = new ArrayList<>();
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
                    Map<String, Object> skuValueMap = new HashMap<>();
                    skuValueMap.put("id", sku.getSkuId().toString());
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
            }
            List<AttributeInSpu> tree = attributeResults.stream().collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingLong(AttributeInSpu::getK_s))), ArrayList::new));
            List<AttributeValueInSpu> treeValue = attributeValuesResults.stream().collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingLong(AttributeValueInSpu::getId))), ArrayList::new));
            for (AttributeValueInSpu attributeValuesResult : treeValue) {
                for (AttributeValues attributeValue : attributeValues) {
                    if (attributeValuesResult.getId().equals(attributeValue.getAttributeValuesId())) {
                        attributeValuesResult.setName(attributeValue.getAttributeValues());
                    }
                }
            }
            for (AttributeInSpu itemAttributeResult : tree) {
                for (ItemAttribute itemAttribute : itemAttributes) {
                    if (itemAttributeResult.getK_s().equals(itemAttribute.getAttributeId())) {
                        itemAttributeResult.setK(itemAttribute.getAttribute());
                        itemAttributeResult.setSort(itemAttribute.getSort());
                    }
                }
                List<AttributeValueInSpu> results = new ArrayList<>();
                for (AttributeValueInSpu attributeValuesResult : treeValue) {
                    if (attributeValuesResult.getAttributeId().equals(itemAttributeResult.getK_s())) {
                        results.add(attributeValuesResult);
                    }
                }
                itemAttributeResult.setV(results);
            }
            Collections.sort(tree);
            skuRequest.setList(list);
            skuRequest.setTree(tree);
        }


        spuResult.setSku(skuRequest);


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
        return ResponseData.success(spuResult);
    }

    static void descartes1(List<Attribute> dimvalue, List<List<String>> result, int layer, List<String> curList) {
        if (layer < dimvalue.size() - 1) {
            if (dimvalue.get(layer).getAttributeValues().size() == 0) {
                descartes1(dimvalue, result, layer + 1, curList);
            } else {
                for (int i = 0; i < dimvalue.get(layer).getAttributeValues().size(); i++) {
                    List<String> list = new ArrayList<String>(curList);
                    list.add(dimvalue.get(layer).getAttributeId() + ":" + dimvalue.get(layer).getAttributeValues().get(i).getAttributeValuesId());
                    descartes1(dimvalue, result, layer + 1, list);
                }
            }
        } else if (layer == dimvalue.size() - 1) {
            if (dimvalue.get(layer).getAttributeValues().size() == 0) {
                result.add(curList);
            } else {
                for (int i = 0; i < dimvalue.get(layer).getAttributeValues().size(); i++) {
                    List<String> list = new ArrayList<String>(curList);
                    list.add(dimvalue.get(layer).getAttributeId() + ":" + dimvalue.get(layer).getAttributeValues().get(i).getAttributeValuesId());
                    result.add(list);
                }
            }
        }
    }

    @Override

    public void delete(SpuParam param) {
        param.setDisplay(0);
        Spu entity = this.getEntity(param);
        spuService.updateById(entity);
    }

    @Override
    public void update(SpuParam param) {
        if (ToolUtil.isNotEmpty(param.getSpuAttributes()) && ToolUtil.isNotEmpty(param.getSpuAttributes().getSpuRequests())) {
            String toJSONString = JSON.toJSONString(param.getSpuAttributes().getSpuRequests());
            param.setAttribute(toJSONString);
        }

        Spu oldEntity = getOldEntity(param);
        Spu newEntity = getEntity(param);
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
    private Long getSpuClass(SpuParam param) {
        Long spuClassificationId = 0L;
        SpuClassification spuClassification = spuClassificationService.lambdaQuery().eq(SpuClassification::getName, param.getSpuClassification().getName()).and(i -> i.eq(SpuClassification::getDisplay, 1)).one();
        if (ToolUtil.isEmpty(spuClassification)) {
            SpuClassification spuClassificationEntity = new SpuClassification();
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
    public SpuResult findBySpec(SpuParam param) {
        return null;
    }

    @Override
    public List<SpuResult> findListBySpec(SpuParam param) {
        return null;
    }

    @Override
    public PageInfo<SpuResult> findPageBySpec(SpuParam param) {
        Page<SpuResult> pageContext = getPageContext();
        IPage<SpuResult> page = this.baseMapper.customPageList(pageContext, param);
        this.format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private void format(List<SpuResult> param) {
        List<Long> categoryIds = new ArrayList<>();
        List<Long> unitIds = new ArrayList<>();
        List<Long> classIds = new ArrayList<>();
        for (SpuResult spuResult : param) {
            categoryIds.add(spuResult.getCategoryId());
            unitIds.add(spuResult.getUnitId());
            classIds.add(spuResult.getSpuClassificationId());
        }
        QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.lambda().in(Category::getCategoryId, categoryIds).in(Category::getDisplay, 1);
        List<Category> categoryList = categoryIds.size() == 0 ? new ArrayList<>() : categoryService.list(categoryQueryWrapper);

        List<Unit> units = unitIds.size() == 0 ? new ArrayList<>() : unitService.lambdaQuery().in(Unit::getUnitId, unitIds).list();


        List<SpuClassification> spuClassifications = classIds.size() == 0 ? new ArrayList<>() : spuClassificationService.query().in("spu_classification_id", classIds).list();

        for (SpuResult spuResult : param) {
            if (ToolUtil.isNotEmpty(spuResult.getCategoryId())) {
                for (Category category : categoryList) {
                    if (spuResult.getCategoryId().equals(category.getCategoryId())) {
                        spuResult.setCategory(category);
                        break;
                    }
                }
            }
            for (Unit unit : units) {
                if (spuResult.getUnitId() != null && spuResult.getUnitId().equals(unit.getUnitId())) {
                    UnitResult unitResult = new UnitResult();
                    ToolUtil.copyProperties(unit, unitResult);
                    spuResult.setUnitResult(unitResult);
                    break;
                }
            }

            for (SpuClassification spuClassification : spuClassifications) {
                if (spuClassification.getSpuClassificationId().equals(spuResult.getSpuClassificationId())) {
                    SpuClassificationResult spuClassificationResult = new SpuClassificationResult();
                    ToolUtil.copyProperties(spuClassification, spuClassificationResult);
                    spuResult.setSpuClassificationResult(spuClassificationResult);
                    break;
                }
            }
        }
    }

    private Serializable getKey(SpuParam param) {
        return param.getSpuId();
    }

    private Page<SpuResult> getPageContext() {
        List<String> list = new ArrayList<>();
        list.add("createTime");
        list.add("spuName");
        return PageFactory.defaultPage(list);
    }

    private Spu getOldEntity(SpuParam param) {
        return this.getById(getKey(param));
    }

    private Spu getEntity(SpuParam param) {
        Spu entity = new Spu();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public void skuFormat(List<SkuResult> skuResults) {
        List<Long> spuIds = new ArrayList<>();
        for (SkuResult skuResult : skuResults) {
            spuIds.add(skuResult.getSpuId());
        }
        List<Spu> spus = spuIds.size() == 0 ? new ArrayList<>() : this.listByIds(spuIds);

        for (SkuResult skuResult : skuResults) {
            for (Spu spu : spus) {
                if (skuResult.getSpuId().equals(spu.getSpuId())) {
                    skuResult.setSpuName(spu.getName());
                    break;
                }
            }
        }
    }

}
