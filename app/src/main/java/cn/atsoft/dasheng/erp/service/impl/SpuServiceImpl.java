package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.mapper.SpuMapper;
import cn.atsoft.dasheng.erp.model.params.AttributeValuesParam;
import cn.atsoft.dasheng.erp.model.params.ItemAttributeParam;
import cn.atsoft.dasheng.erp.model.params.SpuParam;
import cn.atsoft.dasheng.erp.model.params.SpuRequest;
import cn.atsoft.dasheng.erp.model.result.CategoryRequest;
import cn.atsoft.dasheng.erp.model.result.SpuResult;
import cn.atsoft.dasheng.erp.service.CategoryService;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.SpuService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Transactional
    @Override
    public void add(SpuParam param) {
        Spu entity = getEntity(param);
        this.save(entity);
        List<List<String>> result = new ArrayList<List<String>>();

        descartes1(param.getSpuAttributes().getSpuRequests(),result,0,new ArrayList<String>());
        for (List<String> strings : result) {
            StringBuffer stringBuffer = new StringBuffer();
            for (String string : strings) {

            }
        }
        List<String> nameIdsList = new ArrayList<>();


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
                    list.add(dimvalue.get(layer).getAttributeValuesParams().get(i).getAttributeValuesId().toString());
                    descartes1(dimvalue, result, layer + 1, list);
                }
            }
        } else if (layer == dimvalue.size() - 1) {
            if (dimvalue.get(layer).getAttributeValuesParams().size() == 0) {
                result.add(curList);
            } else {
                for (int i = 0; i < dimvalue.get(layer).getAttributeValuesParams().size(); i++) {
                    List<String> list = new ArrayList<String>(curList);
                    list.add(dimvalue.get(layer).getAttributeValuesParams().get(i).getAttributeValuesId().toString());
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
        for (SpuResult spuResult : param) {
            categoryIds.add(spuResult.getCategoryId());
        }
        QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.lambda().in(Category::getCategoryId, categoryIds);
        List<Category> categoryList = categoryIds.size() == 0 ? new ArrayList<>() : categoryService.list(categoryQueryWrapper);

        for (SpuResult spuResult : param) {
            for (Category category : categoryList) {
                if (spuResult.getCategoryId().equals(category.getCategoryId())) {
                    spuResult.setCategory(category);
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

    public void addTest(SpuParam param) {
        //获取属性和多个属性值
        Map<Integer, List<AttributeValuesParam>> map = new HashMap<>();

        for (int i = 0; i < param.getSpuRequests().size(); i++) {

            SpuRequest spuRequest = param.getSpuRequests().get(i);
            List<AttributeValuesParam> list = new ArrayList<>();

            for (int j = 0; j < spuRequest.getAttributeValuesParams().size(); j++) {
                AttributeValuesParam valuesParam = spuRequest.getAttributeValuesParams().get(j);
                list.add(valuesParam);
            }
            map.put(spuRequest.getAttributeId(), list);
        }
        //处理属性值

        for (Map.Entry<Integer, List<AttributeValuesParam>> integerListEntry : map.entrySet()) {
            Integer key = integerListEntry.getKey();
            List<AttributeValuesParam> valuesParams = map.get(key);
            for (AttributeValuesParam valuesParam : valuesParams) {

            }
        }


    }

//    public List<CategoryRequest> getChildren(Long id, List<AttributeValues> values) {
//        List<CategoryRequest> categoryRequests = new ArrayList<>();
//
//        for (AttributeValues value : values) {
//            if (value.getAttributeId().equals(id)) {
//                CategoryRequest categoryRequest = new CategoryRequest();
//                categoryRequest.setAttributeId(id);
//                categoryRequest.setValue(values);
//            }
//        }
//        for (CategoryRequest categoryRequest : categoryRequests) {
//            List<AttributeValues> value = categoryRequest.getValue();
//            categoryRequest.setValue(getChildren(categoryRequest.getAttributeId(),categoryRequest.getValue()));
//        }
//        return categoryRequests;
//    }
}
