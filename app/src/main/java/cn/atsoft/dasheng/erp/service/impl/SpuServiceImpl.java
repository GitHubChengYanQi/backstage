package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.Unit;
import cn.atsoft.dasheng.app.model.result.UnitResult;
import cn.atsoft.dasheng.app.service.UnitService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.mapper.SpuMapper;
import cn.atsoft.dasheng.erp.model.params.ItemAttributeParam;
import cn.atsoft.dasheng.erp.model.params.SkuJson;
import cn.atsoft.dasheng.erp.model.params.SkuValuesRequest;
import cn.atsoft.dasheng.erp.model.params.SpuParam;
import cn.atsoft.dasheng.erp.model.result.AttributeValuesResult;
import cn.atsoft.dasheng.erp.model.result.SpuResult;
import cn.atsoft.dasheng.erp.service.CategoryService;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.SpuService;
import cn.atsoft.dasheng.core.util.ToolUtil;
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

    @Transactional
    @Override
    public void add(SpuParam param) {
        Spu entity = getEntity(param);
        this.save(entity);
        List<List<String>> result = new ArrayList<List<String>>();

        descartes1(param.getSpuAttributes().getSpuRequests(), result, 0, new ArrayList<String>());

        List<String> nameIdsList = new ArrayList<>();
        List<Sku> skuList = new ArrayList<>();
        SkuJson skuJson = new SkuJson();
        for (List<String> attributeValues : result) {
            List<SkuValuesRequest> skuValuesRequests = new ArrayList<>();

            for (String attributeValue : attributeValues) {
                List<String> skuName = Arrays.asList(attributeValue.split(":"));
                SkuValuesRequest valuesResult = new SkuValuesRequest();
                valuesResult.setAttributeId(Long.valueOf(skuName.get(0)));
                valuesResult.setAttributeValueId(Long.valueOf(skuName.get(1)));
                skuValuesRequests.add(valuesResult);
            }
            skuJson.setSkuValuesRequests(skuValuesRequests);
            Sku sku = new Sku();
            sku.setSkuName(JSON.toJSONString(skuJson));
            sku.setSpuId(param.getSpuId());
            skuList.add(sku);
        }


        skuService.saveBatch(skuList);

//        skuService.saveBatch(skuList);
        System.out.println(result.toString());

    }

    static void descartes1(List<ItemAttributeParam> dimvalue, List<List<String>> result, int layer, List<String> curList) {
        if (layer < dimvalue.size() - 1) {
            if (dimvalue.get(layer).getAttributeValuesParams().size() == 0) {
                descartes1(dimvalue, result, layer + 1, curList);
            } else {
                for (int i = 0; i < dimvalue.get(layer).getAttributeValuesParams().size(); i++) {
                    List<String> list = new ArrayList<String>(curList);
                    list.add(dimvalue.get(layer).getAttributeValuesParams().get(i).getAttributeId().toString()+":"+dimvalue.get(layer).getAttributeValuesParams().get(i).getAttributeValuesId().toString());
                    descartes1(dimvalue, result, layer + 1, list);
                }
            }
        } else if (layer == dimvalue.size() - 1) {
            if (dimvalue.get(layer).getAttributeValuesParams().size() == 0) {
                result.add(curList);
            } else {
                for (int i = 0; i < dimvalue.get(layer).getAttributeValuesParams().size(); i++) {
                    List<String> list = new ArrayList<String>(curList);
                    list.add(dimvalue.get(layer).getAttributeValuesParams().get(i).getAttributeId().toString()+":"+dimvalue.get(layer).getAttributeValuesParams().get(i).getAttributeValuesId().toString());
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
        for (SpuResult spuResult : param) {
            categoryIds.add(spuResult.getCategoryId());
            unitIds.add(spuResult.getUnitId());
        }
        QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.lambda().in(Category::getCategoryId, categoryIds);
        List<Category> categoryList = categoryIds.size() == 0 ? new ArrayList<>() : categoryService.list(categoryQueryWrapper);

        List<Unit> units = unitIds.size() == 0 ? new ArrayList<>() : unitService.lambdaQuery().in(Unit::getUnitId, unitIds).list();

        for (SpuResult spuResult : param) {
            for (Category category : categoryList) {
                if (spuResult.getCategoryId().equals(category.getCategoryId())) {
                    spuResult.setCategory(category);
                }
            }
            for (Unit unit : units) {
                if (spuResult.getUnitId() !=null && spuResult.getUnitId().equals(unit.getUnitId())) {
                    UnitResult unitResult = new UnitResult();
                    ToolUtil.copyProperties(unit, unitResult);
                    spuResult.setUnitResult(unitResult);
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
