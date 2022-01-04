package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.ErpPartsDetail;
import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.entity.Unit;
import cn.atsoft.dasheng.app.model.params.Attribute;
import cn.atsoft.dasheng.app.model.params.Values;
import cn.atsoft.dasheng.app.model.result.UnitResult;
import cn.atsoft.dasheng.app.service.ErpPartsDetailService;
import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.app.service.UnitService;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.mapper.SkuMapper;
import cn.atsoft.dasheng.erp.model.params.*;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.print.AttributeException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * sku表	 服务实现类
 * </p>
 *
 * @author
 * @since 2021-10-18
 */
@Service
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku> implements SkuService {
    @Autowired
    private SpuService spuService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AttributeValuesService attributeValuesService;
    @Autowired
    private ItemAttributeService itemAttributeService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private PartsService partsService;
    @Autowired
    private ErpPartsDetailService partsDetailService;
    @Autowired
    private CodingRulesService codingRulesService;
    @Autowired
    private SpuClassificationService spuClassificationService;
    @Autowired
    private UnitService unitService;

    @Transactional
    @Override
    public void add(SkuParam param) {
        //生成编码

        CodingRules codingRules = codingRulesService.query().eq("coding_rules_id", param.getStandard()).one();
        if (ToolUtil.isNotEmpty(codingRules)) {
            String backCoding = codingRulesService.backCoding(codingRules.getCodingRulesId());
            SpuClassification classification = spuClassificationService.query().eq("spu_classification_id", param.getSpuClassificationId()).one();
            if (ToolUtil.isNotEmpty(classification) && ToolUtil.isNotEmpty(classification.getCodingClass()) && classification.getDisplay() != 0) {
                String replace = backCoding.replace("${skuClass}", classification.getCodingClass());
                param.setStandard(replace);
            } else {
                throw new ServiceException(500, "请选择分类！");
            }
        }

        /**
         * type=1 是整机添加
         */
        if (param.getType() == 0) {
            /**
             * 查询分类  添加分类
             */
            Category one1 = categoryService.lambdaQuery().eq(Category::getCategoryName, param.getSpu().getName()).and(i -> i.eq(Category::getDisplay, 1)).one();
            Long categoryId = null;
            if (ToolUtil.isNotEmpty(one1)) {
                categoryId = one1.getCategoryId();
            } else {
                Category category = new Category();
                category.setCategoryName(param.getSpu().getName().replace(" ", ""));
                categoryService.save(category);
                categoryId = category.getCategoryId();
            }

            /**
             * 查询产品，添加产品
             */
            Long spuId = param.getSpu().getSpuId();
            Spu spuEntity = new Spu();
            spuEntity.setName(param.getSpu().getName());
            spuEntity.setSpuClassificationId(param.getSpuClassificationId());
            spuEntity.setCategoryId(categoryId);
            spuEntity.setType(0);
            spuEntity.setUnitId(param.getUnitId());
            
            /**
             * sku名称（skuName）加型号(spuName)判断防止重复
             */
            Spu spu = spuService.lambdaQuery().eq(Spu::getName, param.getSpu().getName()).and(i -> i.eq(Spu::getDisplay, 1)).one();
            List<Sku> skuName = skuService.query().eq("sku_name", param.getSkuName()).and(i -> i.eq("display", 1)).list();
            if (ToolUtil.isNotEmpty(spu) && ToolUtil.isNotEmpty(skuName)) {
                throw new ServiceException(500, "此物料在产品中已存在");
            }



            if (ToolUtil.isEmpty(spuId)) {
                if (ToolUtil.isNotEmpty(spu)) {
                    spuId = spu.getSpuId();
                } else {
                    spuService.save(spuEntity);
                    spuId = spuEntity.getSpuId();
                }
            }else {
                spuEntity.setSpuId(spuId);
                spuService.updateById(spuEntity);
            }

            /**
             * 查询属性，添加属性
             */
            Long itemAttributeId = null;
            if (ToolUtil.isNotEmpty(categoryId)) {
                //查询出属性id
                ItemAttribute one = itemAttributeService.lambdaQuery().eq(ItemAttribute::getCategoryId, categoryId).and(i -> i.eq(ItemAttribute::getDisplay, 1)).one();
                /**
                 * 如果已经创建过产品  但是 没有物料属性  创建物料属性后  创建属性值  最后绑定创建物料
                 */

                if (ToolUtil.isNotEmpty(one)) {
                    itemAttributeId = one.getAttributeId();
                } else {
                    ItemAttribute attribute = new ItemAttribute();
                    attribute.setCategoryId(categoryId);
                    attribute.setAttribute("规格");
                    attribute.setStandard(param.getSpuStandard());
                    itemAttributeService.save(attribute);
                    itemAttributeId = attribute.getAttributeId();
                }
            }
            Long attributeValuesId = null;
            //根据分类查询出属性新建属性值
            /**
             * 查询属性值  添加属性值
             */
            AttributeValues one = attributeValuesService.lambdaQuery().eq(AttributeValues::getAttributeId, itemAttributeId).eq(AttributeValues::getAttributeValues, param.getSpecifications()).eq(AttributeValues::getDisplay, 1).one();
            if (ToolUtil.isNotEmpty(one)) {
                attributeValuesId = one.getAttributeValuesId();
            } else {
                AttributeValues attributeValues = new AttributeValues();
                attributeValues.setAttributeValues(param.getSpecifications());
                attributeValues.setAttributeId(itemAttributeId);
                attributeValuesService.save(attributeValues);
                attributeValuesId = attributeValues.getAttributeValuesId();
            }
            Sku entity = getEntity(param);
            List<AttributeValues> list = new ArrayList<>();
            AttributeValues attributeValue = new AttributeValues();
            attributeValue.setAttributeId(itemAttributeId);
            attributeValue.setAttributeValuesId(attributeValuesId);
            list.add(attributeValue);
            list.sort(Comparator.comparing(AttributeValues::getAttributeId));
            String json = JSON.toJSONString(list);

            entity.setSpuId(spuId);
            entity.setSkuValue(json);
//            entity.setSkuValue(spuId + "," + json);
            String md5 = SecureUtil.md5(categoryId + spuId + entity.getSkuValue());
            String oldMd51 = SecureUtil.md5(entity.getSkuValue());
            String oldMd52 = SecureUtil.md5(spuId + entity.getSkuValue());

            entity.setSkuValueMd5(md5);

//
            /**
             * //TODO 原 SKU防重复判断
             *
             * List<Sku> sku = skuService.lambdaQuery().eq(Sku::getSkuValueMd5, md5).and(i -> i.eq(Sku::getDisplay, 1)).list();
             * List<Sku> oldsku1 = skuService.lambdaQuery().eq(Sku::getSkuValueMd5, oldMd51).and(i -> i.eq(Sku::getDisplay, 1)).list();
             * List<Sku> oldsku2 = skuService.lambdaQuery().eq(Sku::getSkuValueMd5, oldMd52).and(i -> i.eq(Sku::getDisplay, 1)).list();
             * Sku sku = skuService.lambdaQuery().eq(Sku::getSkuValueMd5, md5).and(i -> i.eq(Sku::getDisplay, 1)).one();
             *  if (ToolUtil.isNotEmpty(sku) || ToolUtil.isNotEmpty(oldsku1) || ToolUtil.isNotEmpty(oldsku2)) {
             *    throw new ServiceException(500, "此物料在产品中已存在");
             *} else {
             *    this.save(entity);
             *}
             *
             *  ↓为新sku防止重复判断  以名称加型号 做数据库比对判断
             */

            this.save(entity);
        } else if (param.getType() == 1) {
            Long spuId = param.getSpu().getSpuId();
            if (ToolUtil.isEmpty(spuId)) {
                spuId = spuService.lambdaQuery().eq(Spu::getName, param.getSpu().getName()).and(i -> i.eq(Spu::getDisplay, 1)).one().getSpuId();
            }

            if (spuId == null) {
                SpuParam spu = new SpuParam();
                spu.setName(param.getSpu().getName());
                spu.setSpuClassificationId(param.getSpuClassificationId());
                spu.setIsHidden(false);
                spu.setType(0);
                spuId = spuService.add(spu);
            } else {
                param.getSpuAttributes().getSpuRequests().sort(Comparator.comparing(Attribute::getAttributeId));
                List<AttributeValues> list = new ArrayList<>();
                for (Attribute spuRequest : param.getSpuAttributes().getSpuRequests()) {
                    AttributeValues attributeValueResult = new AttributeValues();
                    attributeValueResult.setAttributeId(Long.valueOf(spuRequest.getAttributeId()));
                    for (Values attributeValue : spuRequest.getAttributeValues()) {
                        attributeValueResult.setAttributeValuesId(Long.valueOf(attributeValue.getAttributeValuesId()));
                    }
                    list.add(attributeValueResult);
                }
                Sku entity = getEntity(param);
                entity.setSpuId(spuId);
                String json = JSON.toJSONString(list);
                entity.setSkuValue(json);
//                entity.setSkuValue(spuId + "," + json);
                String md5 = SecureUtil.md5(entity.getSpuId() + entity.getSkuValue());
                entity.setSkuValueMd5(md5);
                /**
                 * //TODO 原 SKU防重复判断
                 *
                 * Sku sku = skuService.lambdaQuery().eq(Sku::getSkuValueMd5, md5).and(i -> i.eq(Sku::getDisplay, 1)).one();
                 *  if (ToolUtil.isNotEmpty(sku) || ToolUtil.isNotEmpty(spu)) {
                 *    throw new ServiceException(500, "此物料在产品中已存在");
                 *} else {
                 *    this.save(entity);
                 *}
                 *
                 *  ↓为新sku防止重复判断  以名称加型号 做数据库比对判断
                 */
                List<Spu> spu = spuService.query().eq("name", param.getSpu().getName()).and(i -> i.eq("display", 1)).list();
                List<Sku> skuName = skuService.query().eq("sku_name", param.getSkuName()).and(i -> i.eq("display", 1)).list();
                if (ToolUtil.isNotEmpty(spu) && ToolUtil.isNotEmpty(skuName)) {
                    throw new ServiceException(500, "此物料在产品中已存在");
                } else {
                    this.save(entity);
                }

            }

        }
    }

    @Override

    public void delete(SkuParam param) {
        List<Long> id = new ArrayList<>();
        id.add(param.getSkuId());
        param.setId(id);
        this.deleteBatch(param);
    }

    @Transactional
    @Override
    public void deleteBatch(SkuParam param) {
        List<Long> skuIds = param.getId();

        List<ErpPartsDetail> partsDetailList = partsDetailService.lambdaQuery().in(ErpPartsDetail::getSkuId, skuIds).list();
        List<Parts> partList = partsService.lambdaQuery().in(Parts::getSkuId, skuIds).and(i -> i.eq(Parts::getDisplay, 1)).list();
        if (ToolUtil.isNotEmpty(partsDetailList) || ToolUtil.isNotEmpty(partList)) {
            throw new ServiceException(500, "清单中有此物品数据,删除终止");
        }
        List<Long> attributeValuesIds = new ArrayList<>();
        List<Long> attributeIds = new ArrayList<>();
        List<Long> spuIds = new ArrayList<>();
        List<Sku> skuList = skuIds.size() == 0 ? new ArrayList<>() : skuService.lambdaQuery().in(Sku::getSkuId, skuIds).and(i -> i.eq(Sku::getDisplay, 1)).list();
        for (Sku sku : skuList) {
            //获取spuId
            spuIds.add(sku.getSpuId());
            //循环设置逻辑删除值
            sku.setDisplay(0);
            JSONArray jsonArray = JSONUtil.parseArray(sku.getSkuValue());
            List<AttributeValues> valuesRequests = JSONUtil.toList(jsonArray, AttributeValues.class);//skuValue解析
            for (AttributeValues valuesRequest : valuesRequests) {
                attributeValuesIds.add(valuesRequest.getAttributeValuesId());//获取sku属性值
                attributeIds.add(valuesRequest.getAttributeId());//获取sku属性
            }
        }
        skuService.updateBatchById(skuList);
        List<AttributeValues> attributeValuesList = attributeValuesIds.size() == 0 ? new ArrayList<>() : attributeValuesService.lambdaQuery().in(AttributeValues::getAttributeValuesId, attributeValuesIds).and(i -> i.eq(AttributeValues::getDisplay, 1)).list();
        for (AttributeValues attributeValues : attributeValuesList) {
            attributeValues.setDisplay(0);
        }
        attributeValuesService.updateBatchById(attributeValuesList);
        List<AttributeValues> afterDeleteValues = attributeValuesList.size() == 0 ? new ArrayList<>() : attributeValuesService.lambdaQuery().in(AttributeValues::getAttributeId, attributeIds).and(i -> i.eq(AttributeValues::getDisplay, 1)).list();
        List<ItemAttribute> itemAttributes = attributeIds.size() == 0 ? new ArrayList<>() : itemAttributeService.lambdaQuery().in(ItemAttribute::getAttributeId, attributeIds).and(i -> i.eq(ItemAttribute::getDisplay, 1)).list();
        for (ItemAttribute itemAttribute : itemAttributes) {
            itemAttribute.setDisplay(0);
            for (AttributeValues afterDeleteValue : afterDeleteValues) {
                if (itemAttribute.getAttributeId().equals(afterDeleteValue.getAttributeId())) {
                    itemAttribute.setDisplay(1);
                }
            }
        }
        itemAttributeService.updateBatchById(itemAttributes);

        //获取分类id
        List<Long> categoryIds = new ArrayList<>();
        List<Spu> spuList = spuIds.size() == 0 ? new ArrayList<>() : spuService.lambdaQuery().in(Spu::getSpuId, spuIds).and(i -> i.eq(Spu::getDisplay, 1)).list();
        List<Sku> afterDeleteSkuList = spuIds.size() == 0 ? new ArrayList<>() : skuService.lambdaQuery().in(Sku::getSpuId, spuIds).and(i -> i.eq(Sku::getDisplay, 1)).list();
        for (Spu spu : spuList) {
            spu.setDisplay(0);
            for (Sku sku : afterDeleteSkuList) {
                if (sku.getSpuId().equals(spu.getSpuId())) {
                    spu.setDisplay(1);
                }
            }
            categoryIds.add(spu.getCategoryId());
        }
        spuService.updateBatchById(spuList);

        List<Category> categoryList = categoryIds.size() == 0 ? new ArrayList<>() : categoryService.lambdaQuery().in(Category::getCategoryId, categoryIds).and(i -> i.eq(Category::getDisplay, 1)).list();
        List<Spu> afterDeleteSpuList = spuIds.size() == 0 ? new ArrayList<>() : spuService.lambdaQuery().in(Spu::getSpuId, spuIds).and(i -> i.eq(Spu::getDisplay, 1)).list();
        for (Category category : categoryList) {
            category.setDisplay(0);
            for (Spu spu : afterDeleteSpuList) {
                if (category.getCategoryId().equals(spu.getCategoryId())) {
                    category.setDisplay(1);
                }
            }
        }
        categoryService.updateBatchById(categoryList);

    }


    @Override
    @BussinessLog
    public void update(SkuParam param) {
        Sku oldEntity = getOldEntity(param);
        Sku newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        Spu spuEntity = new Spu();
        spuEntity.setSpuClassificationId(param.getSpuClassificationId());
        spuEntity.setUnitId(param.getUnitId());
        spuEntity.setSpuId(param.getSpuId());
        spuService.updateById(spuEntity);
        this.updateById(newEntity);
    }

    @Override
    public SkuResult findBySpec(SkuParam param) {
        return null;
    }

    @Override
    public List<SkuResult> findListBySpec(SkuParam param) {
        return null;
    }

    @Override
    public PageInfo<SkuResult> findPageBySpec(SkuParam param) {
        if (param.getSkuIds() != null) {
            if (param.getSkuIds().size() == 0) {
                return null;
            }
        }
        Page<SkuResult> pageContext = getPageContext();
        List<Long> spuIds = null;
        if (ToolUtil.isNotEmpty(param.getSpuClass())) {
            spuIds = new ArrayList<>();
            List<Spu> spuList = spuService.query().eq("spu_classification_id", param.getSpuClass()).list();
            for (Spu spu : spuList) {
                spuIds.add(spu.getSpuId());
            }
            if (ToolUtil.isEmpty(spuList)) {
                spuIds.add(0L);
            }
        } else {
            spuIds = new ArrayList<>();
        }


        IPage<SkuResult> page = this.baseMapper.customPageList(spuIds, pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    @Override
    public void format(List<SkuResult> param) {

        List<Long> spuIds = new ArrayList<>();
        List<Long> valuesIds = new ArrayList<>();
        List<Long> attributeIds = new ArrayList<>();
        for (SkuResult skuResult : param) {
            spuIds.add(skuResult.getSpuId());
            JSONArray jsonArray = JSONUtil.parseArray(skuResult.getSkuValue());
            List<AttributeValues> valuesRequests = JSONUtil.toList(jsonArray, AttributeValues.class);
            for (AttributeValues valuesRequest : valuesRequests) {
                valuesIds.add(valuesRequest.getAttributeValuesId());
                attributeIds.add(valuesRequest.getAttributeId());
            }
        }
        List<ItemAttribute> itemAttributes = itemAttributeService.lambdaQuery().list();

        List<AttributeValues> attributeValues = attributeIds.size() == 0 ? new ArrayList<>() : attributeValuesService.lambdaQuery()
                .in(AttributeValues::getAttributeId, attributeIds)
                .list();


        List<Spu> spus = spuIds.size() == 0 ? new ArrayList<>() : spuService.query().in("spu_id", spuIds).list();
        List<Long> unitIds = new ArrayList<>();
        Map<Long, UnitResult> unitMaps = new HashMap<>();

        for (Spu spu : spus) {
            if (ToolUtil.isNotEmpty(spu.getUnitId())) {
                unitIds.add(spu.getUnitId());
            }
        }

        List<Unit> units = unitIds.size() == 0 ? new ArrayList<>() : unitService.query().in("unit_id", unitIds).list();

        for (Spu spu : spus) {
            if (ToolUtil.isNotEmpty(units)) {
                for (Unit unit : units) {
                    if (spu.getUnitId() != null && spu.getUnitId().equals(unit.getUnitId())) {
                        UnitResult unitResult = new UnitResult();
                        ToolUtil.copyProperties(unit, unitResult);
                        unitMaps.put(spu.getSpuId(), unitResult);
                    }
                }
            }
        }


        for (SkuResult skuResult : param) {
            if (ToolUtil.isNotEmpty(spus)) {
                for (Spu spu : spus) {
                    if (spu.getSpuId() != null && skuResult.getSpuId() != null && spu.getSpuId().equals(skuResult.getSpuId())) {
                        SpuResult spuResult = new SpuResult();
                        ToolUtil.copyProperties(spu, spuResult);
                        UnitResult unitResult = unitMaps.get(spu.getSpuId());
                        if (ToolUtil.isNotEmpty(unitResult)) {
                            spuResult.setUnitResult(unitResult);
                        }
                        skuResult.setSpuResult(spuResult);
                        break;
                    }
                }
            }

            JSONArray jsonArray = JSONUtil.parseArray(skuResult.getSkuValue());
            List<AttributeValues> valuesRequests = JSONUtil.toList(jsonArray, AttributeValues.class);
            List<SkuJson> list = new ArrayList<>();
            for (AttributeValues valuesRequest : valuesRequests) {
                SkuJson skuJson = new SkuJson();
                for (ItemAttribute itemAttribute : itemAttributes) {
                    if (itemAttribute.getAttributeId().equals(valuesRequest.getAttributeId())) {
                        Attribute attribute = new Attribute();
                        attribute.setAttributeId(itemAttribute.getAttributeId().toString());
                        attribute.setAttribute(itemAttribute.getAttribute());
                        skuJson.setAttribute(attribute);
                    }
                }
                for (AttributeValues attributeValue : attributeValues) {
                    if (valuesRequest.getAttributeValuesId().equals(attributeValue.getAttributeValuesId())) {
                        Values values = new Values();
                        values.setAttributeValuesId(valuesRequest.getAttributeValuesId().toString());
                        values.setAttributeValues(attributeValue.getAttributeValues());
                        skuJson.setValues(values);
                    }
                }
                list.add(skuJson);
            }
            skuResult.setSkuJsons(list);
        }


    }
//    public void formatSku(SkuResult skuResult){
//        List<Long> valuesIds = new ArrayList<>();
//        List<Long> attributeIds = new ArrayList<>();
//        JSONArray jsonArray = JSONUtil.parseArray(skuResult.getSkuValue());
//        List<AttributeValues> valuesRequests = JSONUtil.toList(jsonArray, AttributeValues.class);
//        for (AttributeValues valuesRequest : valuesRequests) {
//            valuesIds.add(valuesRequest.getAttributeValuesId());
//            attributeIds.add(valuesRequest.getAttributeId());
//        }
//        List<ItemAttribute> itemAttributes = itemAttributeService.lambdaQuery().list();
//
//        List<AttributeValues> attributeValues = attributeIds.size() == 0 ? new ArrayList<>() : attributeValuesService.lambdaQuery()
//                .in(AttributeValues::getAttributeId, attributeIds)
//                .list();
//        Spu spu = ToolUtil.isEmpty(skuResult.getSpu())? new Spu() : spuService.query().eq("spu_id", skuResult.getSpu()).one();
//        SpuResult spuResult = new SpuResult();
//        ToolUtil.copyProperties(spu,spuResult);
//        Unit unit = unitService.getById(spu.getUnitId());
//        UnitResult unitResult = new UnitResult();
//        ToolUtil.copyProperties(unit,unitResult);
//
//        spuResult.setUnitResult(unitResult);
//        skuResult.setSpuResult(spuResult);
//        List<SkuJson> list = new ArrayList<>();
//        for (AttributeValues valuesRequest : valuesRequests) {
//            SkuJson skuJson = new SkuJson();
//            for (ItemAttribute itemAttribute : itemAttributes) {
//                if (itemAttribute.getAttributeId().equals(valuesRequest.getAttributeId())) {
//                    Attribute attribute = new Attribute();
//                    attribute.setAttributeId(itemAttribute.getAttributeId().toString());
//                    attribute.setAttribute(itemAttribute.getAttribute());
//                    skuJson.setAttribute(attribute);
//                }
//            }
//            for (AttributeValues attributeValue : attributeValues) {
//                if (valuesRequest.getAttributeValuesId().equals(attributeValue.getAttributeValuesId())) {
//                    Values values = new Values();
//                    values.setAttributeValuesId(valuesRequest.getAttributeValuesId().toString());
//                    values.setAttributeValues(attributeValue.getAttributeValues());
//                    skuJson.setValues(values);
//                }
//            }
//            list.add(skuJson);
//        }
//        skuResult.setSkuJsons(list);
//
//    }

    @Override
    public SkuResult getSku(Long id) {
        Sku sku = this.getById(id);
        SkuResult skuResult = new SkuResult();
        ToolUtil.copyProperties(sku, skuResult);


        Spu spu = spuService.getById(skuResult.getSpuId());
        SpuResult spuResult = new SpuResult();
        ToolUtil.copyProperties(spu, spuResult);
        skuResult.setSpuResult(spuResult);
        JSONArray jsonArray = JSONUtil.parseArray(skuResult.getSkuValue());
        List<AttributeValues> valuesRequests = JSONUtil.toList(jsonArray, AttributeValues.class);
        List<Long> attIds = new ArrayList<>();
        List<Long> valueIds = new ArrayList<>();
        for (AttributeValues valuesRequest : valuesRequests) {
            attIds.add(valuesRequest.getAttributeId());
            valueIds.add(valuesRequest.getAttributeValuesId());
        }
        List<ItemAttribute> itemAttributes = itemAttributeService.query().in("attribute_id", attIds).list();
        List<AttributeValues> valuesList = attributeValuesService.query().in("attribute_values_id", valueIds).list();
        List<AttributeValuesResult> valuesResults = new ArrayList<>();

        for (AttributeValues valuesRequest : valuesList) {
            for (ItemAttribute itemAttribute : itemAttributes) {
                if (valuesRequest.getAttributeId().equals(itemAttribute.getAttributeId())) {
                    AttributeValuesResult valuesResult = new AttributeValuesResult();
                    ToolUtil.copyProperties(valuesRequest, valuesResult);
                    ItemAttributeResult itemAttributeResult = new ItemAttributeResult();
                    ToolUtil.copyProperties(itemAttribute, itemAttributeResult);
                    valuesResult.setItemAttributeResult(itemAttributeResult);
                    valuesResults.add(valuesResult);
                }
            }
        }
        skuResult.setList(valuesResults);
        return skuResult;
    }

    @Override
    public List<SkuResult> formatSkuResult(List<Long> skuIds) {
        if (ToolUtil.isEmpty(skuIds)) {
            return new ArrayList<>();
        }
        List<Sku> skus = this.listByIds(skuIds);
        List<SkuResult> skuResults = new ArrayList<>();
        for (Sku sku : skus) {
            SkuResult skuResult = new SkuResult();
            ToolUtil.copyProperties(sku, skuResult);
            skuResults.add(skuResult);
        }
        this.format(skuResults);
        return skuResults;
    }

    private Serializable getKey(SkuParam param) {
        return param.getSkuId();
    }

    private Page<SkuResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Sku getOldEntity(SkuParam param) {
        return this.getById(getKey(param));
    }

    private Sku getEntity(SkuParam param) {
        Sku entity = new Sku();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public List<BackSku> backSku(Long ids) {

        Sku sku = this.query().eq("sku_id", ids).one();
        if (ToolUtil.isNotEmpty(sku)) {
            JSONArray jsonArray = JSONUtil.parseArray(sku.getSkuValue());
            List<AttributeValues> valuesRequests = JSONUtil.toList(jsonArray, AttributeValues.class);
            List<Long> atrIds = new ArrayList<>();
            List<Long> atrValueIds = new ArrayList<>();
            for (AttributeValues valuesRequest : valuesRequests) {
                atrIds.add(valuesRequest.getAttributeId());
                atrValueIds.add(valuesRequest.getAttributeValuesId());
            }

            List<AttributeValues> values = attributeValuesService.query().in("attribute_values_id", atrValueIds).list();
            List<ItemAttribute> attributes = itemAttributeService.query().in("attribute_id", atrIds).list();

            List<BackSku> backSkus = new ArrayList<>();
            for (AttributeValues valuesRequest : valuesRequests) {
                for (AttributeValues value : values) {
                    if (value.getAttributeValuesId().equals(valuesRequest.getAttributeValuesId())) {
                        BackSku backSku = new BackSku();
                        backSku.setAttributeValues(value);
                        for (ItemAttribute attribute : attributes) {
                            if (attribute.getAttributeId().equals(valuesRequest.getAttributeId())) {
                                backSku.setItemAttribute(attribute);
                                backSkus.add(backSku);
                            }
                        }
                    }
                }
            }
            return backSkus;
        }

        return new ArrayList<>();

    }

    @Override
    public List<SkuResult> backSkuList(List<Long> skuIds) {
        List<Sku> skuList = skuIds.size() == 0 ? new ArrayList<>() : skuService.lambdaQuery().in(Sku::getSkuId, skuIds).list();
        List<Long> attIds = new ArrayList<>();
        List<SkuResult> results = new ArrayList<>();
        for (Sku sku : skuList) {
            String skuValue = sku.getSkuValue();
            JSONArray jsonArray = JSONUtil.parseArray(skuValue);
            List<AttributeValues> valuesRequests = JSONUtil.toList(jsonArray, AttributeValues.class);
            for (AttributeValues valuesRequest : valuesRequests) {
                attIds.add(valuesRequest.getAttributeId());
            }
        }
        List<AttributeValues> attValuesList = attributeValuesService.lambdaQuery().in(AttributeValues::getAttributeId, attIds).list();
        for (Sku sku : skuList) {
            SkuResult skuResult = new SkuResult();
            ToolUtil.copyProperties(sku, skuResult);
            String skuValue = sku.getSkuValue();
            JSONArray jsonArray = JSONUtil.parseArray(skuValue);
            List<AttributeValues> valuesRequests = JSONUtil.toList(jsonArray, AttributeValues.class);
            StringBuffer sb = new StringBuffer();
            for (AttributeValues valuesRequest : valuesRequests) {
                for (AttributeValues values : attValuesList) {
                    if (valuesRequest.getAttributeValuesId().equals(values.getAttributeValuesId())) {
                        sb.append(values.getAttributeValues() + ",");
                    }
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            skuResult.setSkuTextValue(sb.toString());
            results.add(skuResult);
        }
        return results;
    }


    @Override
    public SpuResult backSpu(Long skuId) {
        Sku sku = skuService.query().eq("sku_id", skuId).one();
        if (ToolUtil.isNotEmpty(sku)) {
            Spu spu = spuService.query().eq("spu_id", sku.getSpuId()).one();
            SpuResult spuResult = new SpuResult();
            if (ToolUtil.isNotEmpty(spu)) {
                ToolUtil.copyProperties(spu, spuResult);
            }
            return spuResult;
        }
        return null;
    }

    @Override
    public Map<Long, List<BackSku>> sendSku(List<Long> skuIds) {
        if (skuIds.size() == 0) {
            return null;
        }
        Map<Long, List<BackSku>> skuMap = new HashMap<>();
        List<Long> values = new ArrayList<>();
        List<Long> attIds = new ArrayList<>();
        List<Sku> skus = this.query().in("sku_id", skuIds).list();
        Map<Long, List<AttributeValues>> map = new HashMap<>();
        if (ToolUtil.isNotEmpty(skus)) {
            for (Sku sku : skus) {
                JSONArray jsonArray = JSONUtil.parseArray(sku.getSkuValue());
                List<AttributeValues> valuesRequests = JSONUtil.toList(jsonArray, AttributeValues.class);
                map.put(sku.getSkuId(), valuesRequests);
            }
            for (Long skuiId : skuIds) {
                List<AttributeValues> attributeValues = map.get(skuiId);
                if (ToolUtil.isNotEmpty(attributeValues)) {
                    for (AttributeValues attributeValue : attributeValues) {
                        values.add(attributeValue.getAttributeValuesId());
                        attIds.add(attributeValue.getAttributeId());
                    }
                }
            }
        }
        List<ItemAttribute> attributeList = attIds.size() == 0 ? new ArrayList<>() :
                itemAttributeService.query().in("attribute_id", attIds).list();
        List<AttributeValues> valuesList = values.size() == 0 ? new ArrayList<>() :
                attributeValuesService.query().in("attribute_values_id", values).list();
        for (Long skuiId : skuIds) {
            List<BackSku> backSkus = new ArrayList<>();
            List<AttributeValues> list = map.get(skuiId);
            if (ToolUtil.isNotEmpty(list)) {
                for (AttributeValues attributeValues : list) {
                    for (ItemAttribute itemAttribute : attributeList) {
                        if (itemAttribute.getAttributeId().equals(attributeValues.getAttributeId())) {
                            BackSku backSku = new BackSku();
                            backSku.setItemAttribute(itemAttribute);
                            for (AttributeValues Values : valuesList) {
                                if (Values.getAttributeValuesId().equals(attributeValues.getAttributeValuesId())) {
                                    backSku.setAttributeValues(Values);
                                }
                            }
                            backSkus.add(backSku);
                        }
                    }
                }
                skuMap.put(skuiId, backSkus);
            }

        }
        return skuMap;
    }
}
