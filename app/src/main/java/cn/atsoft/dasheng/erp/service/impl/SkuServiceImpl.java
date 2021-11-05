package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.ErpPartsDetail;
import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.model.params.Attribute;
import cn.atsoft.dasheng.app.model.params.Values;
import cn.atsoft.dasheng.app.service.ErpPartsDetailService;
import cn.atsoft.dasheng.app.service.PartsService;
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

    @Transactional
    @Override
    public void add(SkuParam param) {
        /**
         * type=1 是整机添加
         */
        if (param.getType() == 0) {
            Long itemAttributeId = null;
            Long spuId = param.getSpu().getSpuId();
            if (ToolUtil.isEmpty(spuId)) {
                Spu spu = spuService.lambdaQuery().eq(Spu::getName, param.getSpu().getName()).and(i -> i.eq(Spu::getDisplay, 1)).one();
                if (ToolUtil.isNotEmpty(spu)) {
                    spuId = spu.getSpuId();
                }
            }
            if (spuId == null) {
                SpuParam spu = new SpuParam();
                spu.setName(param.getSpu().getName());
                spu.setSpuClassificationId(param.getSpuClassificationId());
                spu.setSpuStandard(param.getSpuStandard());
                spu.setType(0);
                spu.setIsHidden(true);
                spuId = spuService.add(spu);

            }
            Spu byId = spuService.lambdaQuery().eq(Spu::getSpuId,spuId).and(i->i.eq(Spu::getDisplay,1)).one();
            //判断是否有已存在的分类
            Long categoryId = categoryService.lambdaQuery().eq(Category::getCategoryName, byId.getName()).and(i -> i.eq(Category::getDisplay, 1)).one().getCategoryId();
            if (ToolUtil.isNotEmpty(categoryId)) {
                //查询出属性id

                ItemAttribute one = itemAttributeService.lambdaQuery().eq(ItemAttribute::getCategoryId, categoryId).and(i -> i.eq(ItemAttribute::getDisplay, 1)).one();
                if (ToolUtil.isNotEmpty(one)){
                    itemAttributeId =one.getAttributeId();
                }else {
                    ItemAttributeParam attributeParam = new ItemAttributeParam();
                    attributeParam.setCategoryId(categoryId);
                    attributeParam.setAttribute("规格");
                    attributeParam.setStandard(param.getSpuStandard());
                    itemAttributeId = itemAttributeService.add(attributeParam);
                }
            }
            //根据分类查询出属性新建属性值

            AttributeValuesParam attributeValues = new AttributeValuesParam();
            attributeValues.setAttributeValues(param.getSpecifications());
            attributeValues.setAttributeId(itemAttributeId);
            Long add = attributeValuesService.add(attributeValues);
            Sku entity = getEntity(param);
            List<AttributeValues> list = new ArrayList<>();
            AttributeValues attributeValue = new AttributeValues();
            attributeValue.setAttributeId(attributeValues.getAttributeId());
            attributeValue.setAttributeValuesId(add);
            list.add(attributeValue);
            list.sort(Comparator.comparing(AttributeValues::getAttributeId));
            String json = JSON.toJSONString(list);

            entity.setSpuId(spuId);
            entity.setSkuValue(json);
//            entity.setSkuValue(spuId + "," + json);
            String md5 = SecureUtil.md5(entity.getSkuValue());
            entity.setSkuValueMd5(md5);
            List<Spu> spuName = spuService.query().eq("name", param.getSpu().getName()).and(i -> i.eq("display", 1)).list();
            List<Sku> sku = skuService.lambdaQuery().eq(Sku::getSkuValueMd5, md5).and(i -> i.eq(Sku::getDisplay, 1)).list();

            List<Sku> skuName = skuService.query().eq("sku_name", param.getSkuName()).and(i -> i.eq("display", 1)).list();
            if ((ToolUtil.isNotEmpty(spuName) && ToolUtil.isNotEmpty(skuName)) && (ToolUtil.isNotEmpty(sku))) {
                throw new ServiceException(500, "此物料在产品中已存在");
            } else {
                this.save(entity);
            }
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
                String md5 = SecureUtil.md5(entity.getSkuValue());
                entity.setSkuValueMd5(md5);
                Spu spu = spuService.query().eq("name", param.getSpu().getName()).and(i -> i.eq("display", 1)).one();
                Sku sku = skuService.lambdaQuery().eq(Sku::getSkuValueMd5, md5).and(i -> i.eq(Sku::getDisplay, 1)).one();
                if (ToolUtil.isNotEmpty(sku) || ToolUtil.isNotEmpty(spu)) {
                    throw new ServiceException(500, "此物料在产品中已存在");
                } else {
                    this.save(entity);
                }
            }

        }
    }

    @Override
    @BussinessLog
    public void delete(SkuParam param) {
        List<Long> id = new ArrayList<>();
        id.add(param.getSkuId());
        param.setId(id);
        this.deleteBatch(param);
    }


    @Transactional
    @Override
    @BussinessLog
    public void deleteBatch(SkuParam param) {
//        List<Long> skuIds = param.getId();
//        List<Sku> skuList = param.getId().size() ==0 ? new ArrayList<>() : skuService.lambdaQuery().in(Sku::getSkuId,skuIds).and(i->i.eq(Sku::getDisplay,1)).list();
//        List<Long> spuIds = new ArrayList<>();
//        List<Long> attributeValuesIds = new ArrayList<>();
//        List<Long> attributeIds = new ArrayList<>();
//        List<Long> categoryIds = new ArrayList<>();
//        List<Category> categoryList = new ArrayList<>();
//
//        for (Sku sku : skuList) {
//            spuIds.add(sku.getSpuId());//获取skuid
//            JSONArray jsonArray = JSONUtil.parseArray(sku.getSkuValue());
//            List<AttributeValues> valuesRequests = JSONUtil.toList(jsonArray, AttributeValues.class);//skuValue解析
//            for (AttributeValues valuesRequest : valuesRequests) {
//                attributeValuesIds.add(valuesRequest.getAttributeValuesId());//获取sku属性值
//                attributeIds.add(valuesRequest.getAttributeId());//获取sku属性
//            }
//            sku.setDisplay(0);
//        }
//
//        skuService.updateBatchById(skuList);
//        List<Long>hsSpuIds = new ArrayList<>();
//        List<Sku> beforSkuList = param.getId().size() ==0 ? new ArrayList<>() : skuService.lambdaQuery().in(Sku::getSpuId,spuIds).and(i->i.eq(Sku::getDisplay,1)).list();
//        for (Sku sku : beforSkuList) {
//            hsSpuIds.add(sku.getSpuId());
//        }
//        spuIds.removeAll(hsSpuIds);
//        List<AttributeValues> attributeValuesList = attributeValuesService.lambdaQuery().in(AttributeValues::getAttributeValuesId, attributeValuesIds).and(i->i.eq(AttributeValues::getDisplay,1)).list();
//        for (AttributeValues attributeValues : attributeValuesList) {
//            attributeValues.setDisplay(0);
//        }
//        attributeValuesService.updateBatchById(attributeValuesList);
//        List<Spu> spuList =spuIds.size() == 0 ? new ArrayList<>(): spuService.lambdaQuery().in(Spu::getSpuId, spuIds).and(i->i.eq(Spu::getDisplay,1)).list();
//        for (Spu spu : spuList) {
//            spu.setDisplay(0);
//            categoryIds.add(spu.getCategoryId());
//        }
//
//
//        spuService.updateBatchById(spuList);
//        List<Spu> afterSkuList = spuIds.size() == 0 ? new ArrayList<>() : spuService.lambdaQuery().in(Spu::getSpuId, hsSpuIds).and(i->i.eq(Spu::getDisplay,1)).list();
//        for (Spu spu : afterSkuList) {
//            categoryIds.remove(spu.getCategoryId());
//        }
//        List<Spu> beforspuList = spuIds.size() == 0 ? new ArrayList<>() : spuService.lambdaQuery().in(Spu::getCategoryId, categoryIds).and(i->i.eq(Spu::getDisplay,1)).list();
//        if (beforspuList.size()>1){
//            for (Long categoryId : categoryIds) {
//                Category category = new Category();
//                category.setCategoryId(categoryId);
//                category.setDisplay(0);
//                categoryList.add(category);
//            }
//        }
//        categoryService.updateBatchById(categoryList);
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
        Page<SkuResult> pageContext = getPageContext();
        List<Long> spuIds = new ArrayList<>();
        if (ToolUtil.isNotEmpty(param.getSpuClass())) {
            List<Spu> spuList = spuService.query().eq("spu_classification_id", param.getSpuClass()).list();
            for (Spu spu : spuList) {
                spuIds.add(spu.getSpuId());
            }
        }
        IPage<SkuResult> page = this.baseMapper.customPageList(spuIds, pageContext, param);
        this.format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }


    private void format(List<SkuResult> param) {

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

        for (SkuResult skuResult : param) {
            if (ToolUtil.isNotEmpty(spus)) {
                for (Spu spu : spus) {
                    if (spu.getSpuId() != null && skuResult.getSpuId() != null && spu.getSpuId().equals(skuResult.getSpuId())) {
                        SpuResult spuResult = new SpuResult();
                        ToolUtil.copyProperties(spu, spuResult);
                        skuResult.setSpuResult(spuResult);
                        break;
                    }
                }
            }

            JSONArray jsonArray = JSONUtil.parseArray(skuResult.getSkuValue());
            List<AttributeValues> valuesRequests = JSONUtil.toList(jsonArray, AttributeValues.class);
            List<SkuJson> list = new ArrayList<>();
            for (AttributeValues valuesRequest : valuesRequests) {
                valuesRequest.getAttributeValuesId();
                valuesRequest.getAttributeId();
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
                        AttributeValuesParam attributeValuesParam = new AttributeValuesParam();
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
        List<Sku> skuList = skuService.lambdaQuery().in(Sku::getSkuId, skuIds).list();
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
        List<ItemAttribute> attList = itemAttributeService.lambdaQuery().in(ItemAttribute::getAttributeId, attIds).list();
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
    public Map<Long, List<BackSku>> sendSku(List<Long> skuiIds) {
        if (skuiIds.size() == 0) {
            throw new ServiceException(500, "请确认sku");
        }
        Map<Long, List<BackSku>> skuMap = new HashMap<>();
        List<Long> values = new ArrayList<>();
        List<Long> attIds = new ArrayList<>();
        List<Sku> skus = this.query().in("sku_id", skuiIds).list();
        Map<Long, List<AttributeValues>> map = new HashMap<>();
        if (ToolUtil.isNotEmpty(skus)) {
            for (Sku sku : skus) {
                JSONArray jsonArray = JSONUtil.parseArray(sku.getSkuValue());
                List<AttributeValues> valuesRequests = JSONUtil.toList(jsonArray, AttributeValues.class);
                map.put(sku.getSkuId(), valuesRequests);
            }
            for (Long skuiId : skuiIds) {
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
        for (Long skuiId : skuiIds) {
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
