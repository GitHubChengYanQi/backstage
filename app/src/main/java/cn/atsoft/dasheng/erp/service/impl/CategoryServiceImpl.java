package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.AttributeValues;
import cn.atsoft.dasheng.erp.entity.Category;
import cn.atsoft.dasheng.erp.entity.ItemAttribute;
import cn.atsoft.dasheng.erp.entity.SpuClassification;
import cn.atsoft.dasheng.erp.mapper.CategoryMapper;
import cn.atsoft.dasheng.erp.model.params.AttributeValuesParam;
import cn.atsoft.dasheng.erp.model.params.CategoryParam;

import cn.atsoft.dasheng.erp.model.params.ItemAttributeParam;
import cn.atsoft.dasheng.erp.model.result.CategoryResult;
import cn.atsoft.dasheng.erp.service.AttributeValuesService;
import cn.atsoft.dasheng.erp.service.CategoryService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.ItemAttributeService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 物品分类表 服务实现类
 * </p>
 *
 * @author jazz
 * @since 2021-10-18
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private ItemAttributeService itemAttributeService;
    @Autowired
    private AttributeValuesService attributeValuesService;

    @Override
    public Long add(CategoryParam param) {
        Integer count = this.query().in("category_name", param.getCategoryName())
                .in("display", 1)
                .count();
        if (count > 0) {
            throw new ServiceException(500, "分类不可重复添加");
        }
        Category entity = getEntity(param);
        this.save(entity);
        // 更新当前节点，及下级

        Category category = new Category();
        Map<String, List<Long>> childrenMap = getChildrens(entity.getPid());
        category.setChildrens(JSON.toJSONString(childrenMap.get("childrens")));
        category.setChildren(JSON.toJSONString(childrenMap.get("children")));
        QueryWrapper<Category> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("category_id", entity.getPid());
        this.update(category, QueryWrapper);

        updateChildren(entity.getPid());

        return entity.getCategoryId();
    }

    /**
     * 递归
     */
    public Map<String, List<Long>> getChildrens(Long id) {

        List<Long> childrensSkuIds = new ArrayList<>();
        Map<String, List<Long>> result = new HashMap<String, List<Long>>() {
            {
                put("children", new ArrayList<>());
                put("childrens", new ArrayList<>());
            }
        };

        List<Long> skuIds = new ArrayList<>();
        Category category = this.query().eq("category_id", id).eq("display", 1).one();
        if (ToolUtil.isNotEmpty(category)) {
            List<Category> details = this.query().eq("pid", category.getCategoryId()).eq("display", 1).list();
            for (Category detail : details) {
                skuIds.add(detail.getCategoryId());
                childrensSkuIds.add(detail.getCategoryId());
                Map<String, List<Long>> childrenMap = this.getChildrens(detail.getCategoryId());
                childrensSkuIds.addAll(childrenMap.get("childrens"));
            }
            result.put("children", skuIds);
            result.put("childrens", childrensSkuIds);
        }
        return result;
    }

    /**
     * 更新包含它的
     */
    public void updateChildren(Long id) {
        List<Category> categories = this.query().like("children", id).eq("display", 1).list();
        for (Category category : categories) {
            Map<String, List<Long>> childrenMap = getChildrens(id);
            JSONArray childrensjsonArray = JSONUtil.parseArray(category.getChildrens());
            List<Long> longs = JSONUtil.toList(childrensjsonArray, Long.class);
            List<Long> list = childrenMap.get("childrens");
            for (Long aLong : list) {
                longs.add(aLong);
            }
            category.setChildrens(JSON.toJSONString(longs));
            // update
            QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("category_id", category.getCategoryId());
            this.update(category, queryWrapper);
            updateChildren(category.getCategoryId());
        }
    }

    @Override
    @Transactional
    public void delete(CategoryParam param) {
        Category category = new Category();
        category.setDisplay(0);
        QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.in("category_id", param.getCategoryId());
        this.update(category, categoryQueryWrapper);
    }

    @Override
    @Transactional
    public void update(CategoryParam param) {
        //如果设为顶级 修改所有当前节点的父级
        if (param.getPid() == 0) {
            List<Category> categories = this.query().like("childrens", param.getCategoryId()).list();
            for (Category category : categories) {
                JSONArray jsonArray = JSONUtil.parseArray(category.getChildrens());
                JSONArray childrenJson = JSONUtil.parseArray(category.getChildren());
                List<Long> oldchildrenList = JSONUtil.toList(childrenJson, Long.class);
                List<Long> newChildrenList = new ArrayList<>();
                List<Long> longs = JSONUtil.toList(jsonArray, Long.class);
                longs.remove(param.getCategoryId());
                for (Long aLong : oldchildrenList) {
                    if (!aLong.equals(param.getCategoryId())) {
                        newChildrenList.add(aLong);
                    }
                }
                category.setChildren(JSON.toJSONString(newChildrenList));
                category.setChildrens(JSON.toJSONString(longs));
                this.update(category, new QueryWrapper<Category>().in("category_id", category.getCategoryId()));
            }

        }
        //防止循环添加
        if (ToolUtil.isNotEmpty(param.getPid())) {
            List<Category> categories = this.query().in("category_id", param.getCategoryId()).eq("display", 1).list();
            for (Category category : categories) {
                JSONArray jsonArray = JSONUtil.parseArray(category.getChildrens());
                List<Long> longs = JSONUtil.toList(jsonArray, Long.class);
                for (Long aLong : longs) {
                    if (param.getPid().equals(aLong)) {
                        throw new ServiceException(500, "请勿循环添加");
                    }
                }
            }
        }

        Category category = this.getById(param.getCategoryId());
        if (!category.getCategoryName().equals(param.getCategoryName())) {
            Integer count = this.query().in("display", 1).eq("tenant_id", LoginContextHolder.getContext().getTenantId()).in("category_name", param.getCategoryName()).count();
            if (count > 0) {
                throw new ServiceException(500, "名字已重复");
            }
        }

        // 更新当前节点，及下级
        Category newCategory = new Category();
        Map<String, List<Long>> childrenMap = getChildrens(param.getPid());
        List<Long> childrens = childrenMap.get("childrens");
        childrens.add(param.getCategoryId());
        newCategory.setChildrens(JSON.toJSONString(childrens));
        List<Long> children = childrenMap.get("children");
        children.add(param.getCategoryId());
        newCategory.setChildren(JSON.toJSONString(children));
        QueryWrapper<Category> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("category_id", param.getPid());
        this.update(newCategory, QueryWrapper);

        updateChildren(param.getPid());

        Category oldEntity = getOldEntity(param);
        Category newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public CategoryResult findBySpec(CategoryParam param) {
        return null;
    }

    @Override
    public List<CategoryResult> findListBySpec(CategoryParam param) {
        return null;
    }

    @Override
    public PageInfo<CategoryResult> findPageBySpec(CategoryParam param) {
        Page<CategoryResult> pageContext = getPageContext();
        IPage<CategoryResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(CategoryParam param) {
        return param.getCategoryId();
    }

    private Page<CategoryResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Category getOldEntity(CategoryParam param) {
        return this.getById(getKey(param));
    }

    private Category getEntity(CategoryParam param) {
        Category entity = new Category();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    public void format(List<CategoryResult> data) {
        List<Long> pids = new ArrayList<>();
        for (CategoryResult datum : data) {
            pids.add(datum.getPid());
        }
        if (ToolUtil.isNotEmpty(pids)) {
            List<Category> categories = pids.size() == 0 ? new ArrayList<>() :
                    this.lambdaQuery().in(Category::getCategoryId, pids).in(Category::getDisplay, 1).list();
            for (CategoryResult datum : data) {
                if (ToolUtil.isNotEmpty(categories)) {
                    for (Category category : categories) {
                        if (datum.getPid() != null && datum.getPid().equals(category.getCategoryId())) {
                            CategoryResult categoryResult = new CategoryResult();
                            ToolUtil.copyProperties(category, categoryResult);
                            datum.setPidCategoryResult(categoryResult);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 批量添加
     *
     * @param param
     */
    @Override
    @Transactional
    public void addList(CategoryParam param) {
        if (ToolUtil.isNotEmpty(param.getCategoryId())) {
            this.remove(new QueryWrapper<Category>() {{
                eq("category_id", param.getCategoryId());
            }});

            List<ItemAttribute> attributes = itemAttributeService.query().eq("category_id", param.getCategoryId()).list();
            if (attributes.size() > 0) {
                itemAttributeService.removeByIds(new ArrayList<Long>() {{
                    for (ItemAttribute attribute : attributes) {
                        add(attribute.getAttributeId());
                    }
                }});
                attributeValuesService.remove(new QueryWrapper<AttributeValues>() {{
                    in("attribute_id", new ArrayList<Long>() {{
                        for (ItemAttribute attribute : attributes) {
                            add(attribute.getAttributeId());
                        }
                    }});
                }});
            }


        }
        Category category = new Category();
        ToolUtil.copyProperties(param, category);
        this.save(category);

        for (ItemAttributeParam itemAttributeParam : param.getItemAttributeParams()) {
            ItemAttribute itemAttribute = new ItemAttribute();
            ToolUtil.copyProperties(itemAttributeParam, itemAttribute);
            itemAttribute.setCategoryId(category.getCategoryId());
            itemAttributeService.save(itemAttribute);

            for (AttributeValuesParam attributeValuesParam : itemAttributeParam.getAttributeValuesParams()) {
                AttributeValues attributeValues = new AttributeValues();
                ToolUtil.copyProperties(attributeValuesParam, attributeValues);
                attributeValues.setAttributeId(itemAttribute.getAttributeId());
                attributeValuesService.save(attributeValues);
            }

        }

    }
}
