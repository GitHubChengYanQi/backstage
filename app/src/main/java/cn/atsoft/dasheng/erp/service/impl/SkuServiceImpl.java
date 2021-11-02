package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.model.params.Attribute;
import cn.atsoft.dasheng.app.model.params.Values;
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

    @Transactional
    @Override
    public void add(SkuParam param) {
        /**
         * type=1 是整机添加
         */
        if (param.getType() == 1) {
            ItemAttribute one1 = new ItemAttribute();
            Long spuId = param.getSpu().getSpuId();
            if (spuId == null) {
                SpuParam spu = new SpuParam();
                spu.setName(param.getSpu().getName());
                spu.setCategoryId(param.getSpu().getCategoryId());
                spuId = spuService.add(spu);
            }
            Spu byId = spuService.getById(spuId);
            byId.getName();
            Category one = categoryService.lambdaQuery().in(Category::getCategoryName, byId.getName()).one();
            if (ToolUtil.isNotEmpty(one)) {
                one1 = itemAttributeService.lambdaQuery().in(ItemAttribute::getCategoryId, one.getCategoryId()).one();
            }
            AttributeValuesParam attributeValues = new AttributeValuesParam();
            attributeValues.setAttributeValues(param.getSpecifications());
            attributeValues.setAttributeId(one1.getAttributeId());
            Long add = attributeValuesService.add(attributeValues);
            Sku entity = getEntity(param);
            List<AttributeValues> list = new ArrayList<>();
            AttributeValues attributeValue = new AttributeValues();
            attributeValue.setAttributeId(attributeValues.getAttributeId());
            attributeValue.setAttributeValuesId(add);
            list.add(attributeValue);
            String Json = JSON.toJSONString(list);
            String md5 = SecureUtil.md5(Json);
            entity.setSpuId(spuId);
            entity.setSkuValueMd5(md5);
            entity.setSkuValue(Json);

            Sku sku = skuService.lambdaQuery().in(Sku::getSkuValueMd5, md5).one();
            if (ToolUtil.isEmpty(sku)) {
                this.save(entity);
            }else {
                throw new ServiceException(500,"此物料已存在");
            }
        } else if (param.getType() == 0) {
            Long spuId = param.getSpu().getSpuId();
            if (spuId == null) {
                SpuParam spu = new SpuParam();
                spu.setName(param.getSpu().getName());
                spu.setSpuClassificationId(param.getSpuClassificationId());
                spuId = spuService.add(spu);
            }
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
            String Json = JSON.toJSONString(list);
            String md5 = SecureUtil.md5(Json);
            entity.setSkuValueMd5(md5);
            entity.setSkuValue(Json);
            Sku one = skuService.lambdaQuery().in(Sku::getSkuValueMd5, md5).one();
            if (ToolUtil.isEmpty(one)) {
                this.save(entity);
            }else {
                throw new ServiceException(500,"此物料已存在");
            }
        }
    }

    @Override
    public void delete(SkuParam param) {
        this.removeById(getKey(param));
    }

    @Override
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
        IPage<SkuResult> page = this.baseMapper.customPageList(pageContext, param);
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
        return new SpuResult();

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
                for (AttributeValues attributeValue : attributeValues) {
                    values.add(attributeValue.getAttributeValuesId());
                    attIds.add(attributeValue.getAttributeId());
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
        return skuMap;
    }
}
