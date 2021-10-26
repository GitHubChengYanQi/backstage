package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.Unit;
import cn.atsoft.dasheng.app.model.params.Attribute;
import cn.atsoft.dasheng.app.model.params.Values;
import cn.atsoft.dasheng.app.model.result.UnitResult;
import cn.atsoft.dasheng.app.service.UnitService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.mapper.SpuMapper;
import cn.atsoft.dasheng.erp.model.params.*;
import cn.atsoft.dasheng.erp.model.result.AttributeValuesResult;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.model.result.SpuClassificationResult;
import cn.atsoft.dasheng.erp.model.result.SpuResult;
import cn.atsoft.dasheng.erp.service.CategoryService;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.SpuClassificationService;
import cn.atsoft.dasheng.erp.service.SpuService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.sf.jsqlparser.statement.select.ValuesList;
import org.beetl.ext.fn.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

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
    private CategoryService categoryService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private SpuClassificationService spuClassificationService;

    @Transactional
    @Override
    public void add(SpuParam param) {
        Integer count = this.query().in("name", param.getName()).count();
        if (count > 0) {
            throw new ServiceException(500, "不可以添加重复产品");
        }

        Spu entity = getEntity(param);
        if (ToolUtil.isNotEmpty(param.getSpuAttributes().getSpuRequests())) {
            String toJSONString = JSON.toJSONString(param.getSpuAttributes().getSpuRequests());
            entity.setAttribute(toJSONString);
        }
        this.save(entity);
        List<List<String>> result = new ArrayList<List<String>>();
//        param.getSpuAttributes().getSpuRequests().sort((x, y) -> x.getAttributeId().compareTo(y.getAttributeId()));
        Collections.sort(param.getSpuAttributes().getSpuRequests());
//        param.getSpuAttributes().getSpuRequests().sort(null);

        //        Collections.sort(param.getSpuAttributes().getSpuRequests());
        if (ToolUtil.isNotEmpty(param.getSpuAttributes().getSpuRequests())) {

            descartes1(param.getSpuAttributes().getSpuRequests(), result, 0, new ArrayList<String>());
            List<Sku> skuList = new ArrayList<>();
            List<String> skuValues = new ArrayList<>();
            for (List<String> attributeValues : result) {
                List<AttributeValues> valuesList = new ArrayList<>();
                for (String attributeValue : attributeValues) {
                    List<String> skuName = Arrays.asList(attributeValue.split(":"));
                    AttributeValues values = new AttributeValues();
                    values.setAttributeId(Long.valueOf(skuName.get(0)));
                    values.setAttributeValuesId(Long.valueOf(skuName.get(1)));
                    valuesList.add(values);
                }
                Sku sku = new Sku();
                sku.setSkuValue(JSON.toJSONString(valuesList));
                sku.setSkuName(SecureUtil.md5(sku.getSkuValue()));
                skuValues.add(sku.getSkuValue());
                sku.setSpuId(entity.getSpuId());
                skuList.add(sku);
            }
            Integer skuValue = skuService.query().in("sku_value", skuValues).count();
            if (skuValue > 0) {
                throw new ServiceException(500, "不可以添加型号");
            }


            skuService.saveBatch(skuList);
        }


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
        this.removeById(getKey(param));
    }

    @Override
    public void update(SpuParam param) {
        if (ToolUtil.isNotEmpty(param.getSpuAttributes().getSpuRequests())) {
            String toJSONString = JSON.toJSONString(param.getSpuAttributes().getSpuRequests());
            param.setAttribute(toJSONString);
        }
        Spu oldEntity = getOldEntity(param);
        Spu newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
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
        categoryQueryWrapper.lambda().in(Category::getCategoryId, categoryIds);
        List<Category> categoryList = categoryIds.size() == 0 ? new ArrayList<>() : categoryService.list(categoryQueryWrapper);

        List<Unit> units = unitIds.size() == 0 ? new ArrayList<>() : unitService.lambdaQuery().in(Unit::getUnitId, unitIds).list();


        List<SpuClassification> spuClassifications = classIds.size() == 0 ? new ArrayList<>() : spuClassificationService.query().in("spu_classification_id", classIds).list();

        for (SpuResult spuResult : param) {
            for (Category category : categoryList) {
                if (spuResult.getCategoryId().equals(category.getCategoryId())) {
                    spuResult.setCategory(category);
                    break;
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
        return PageFactory.defaultPage();
    }

    private Spu getOldEntity(SpuParam param) {
        return this.getById(getKey(param));
    }

    private Spu getEntity(SpuParam param) {
        Spu entity = new Spu();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }


}
