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
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
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

    @Transactional
    @Override
    public void add(SkuParam param) {
        List<Long> ids = new ArrayList<>();

        StringBuffer stringBuffer = new StringBuffer();
        for (SkuValues skuValue : param.getSkuValues()) {
            ids.add(skuValue.getAttributeValuesId());
        }
        Collections.sort(ids);
        for (Long id : ids) {
            stringBuffer.append(id + ",");
        }
        if (stringBuffer.length() > 1) {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }
        Sku entity = getEntity(param);
        entity.setSkuName(stringBuffer.toString());
        this.save(entity);
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
            JSONArray jsonArray = JSONUtil.parseArray(skuResult.getSkuValue());
            List<AttributeValues> valuesRequests = JSONUtil.toList(jsonArray, AttributeValues.class);
            for (AttributeValues valuesRequest : valuesRequests) {
                valuesIds.add(valuesRequest.getAttributeValuesId());
                attributeIds.add(valuesRequest.getAttributeId());
            }
        }
        List<ItemAttribute> itemAttributes = itemAttributeService.lambdaQuery().list();

        List<AttributeValues> attributeValues = attributeValuesService.lambdaQuery()
                .in(AttributeValues::getAttributeId, attributeIds)
                .list();

        for (SkuResult skuResult : param) {
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
    public Map<Long, List<BackSku>> backSku(List<Long> ids) {
        List<ItemAttribute> itemAttributes = itemAttributeService.query().list();
        List<AttributeValues> valuesList = attributeValuesService.list();
        Map<Long, String> map = new HashMap<>();
        Map<Long, List<BackSku>> backSkuMap = new HashMap<>();

        List<Sku> skus = this.query().in("sku_id", ids).list();
        for (Sku sku : skus) {
            map.put(sku.getSkuId(), sku.getSkuValue());
        }
        Map<Long, List<AttributeValues>> listMap = new HashMap<>();
        for (Map.Entry<Long, String> longStringEntry : map.entrySet()) {
            JSONArray jsonArray = JSONUtil.parseArray(longStringEntry.getValue());
            List<AttributeValues> valuesRequests = JSONUtil.toList(jsonArray, AttributeValues.class);
            listMap.put(longStringEntry.getKey(), valuesRequests);
        }
        for (Map.Entry<Long, List<AttributeValues>> longListEntry : listMap.entrySet()) {

            Long key = longListEntry.getKey();

            List<AttributeValues> longListEntryValue = longListEntry.getValue();
            for (AttributeValues attributeValues : longListEntryValue) {
                List<BackSku> backSkus = new ArrayList<>();
                for (ItemAttribute itemAttribute : itemAttributes) {

                    for (AttributeValues values : valuesList) {

                        if (itemAttribute.getAttributeId().equals(attributeValues.getAttributeId()) && values.getAttributeValuesId().equals(attributeValues.getAttributeValuesId())) {
                            BackSku backSku = new BackSku();
                            backSku.setAttributeValues(values);
                            backSku.setItemAttribute(itemAttribute);
                            backSkus.add(backSku);
                            backSkuMap.put(key, backSkus);

                        }

                    }
                }

            }
        }
        return backSkuMap;

    }


}
