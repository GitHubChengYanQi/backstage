package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.AttributeValues;
import cn.atsoft.dasheng.erp.entity.Category;
import cn.atsoft.dasheng.erp.entity.ItemAttribute;
import cn.atsoft.dasheng.erp.mapper.CategoryMapper;
import cn.atsoft.dasheng.erp.model.params.CategoryParam;

import cn.atsoft.dasheng.erp.model.result.CategoryResult;
import cn.atsoft.dasheng.erp.service.AttributeValuesService;
import cn.atsoft.dasheng.erp.service.CategoryService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.ItemAttributeService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
            throw new ServiceException(500, "**重复添加**");
        }
        Category entity = getEntity(param);
        this.save(entity);
        return entity.getCategoryId();
    }

    @Override
    @BussinessLog
    @Transactional
    public void delete(CategoryParam param) {
        Category category = new Category();
        category.setDisplay(0);
        QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.in("category_id", param.getCategoryId());
        this.update(category, categoryQueryWrapper);
//        List<ItemAttribute> attributes = param.getCategoryId() == null ? new ArrayList<>() : itemAttributeService.lambdaQuery().in(ItemAttribute::getCategoryId, param.getCategoryId())
//                .in(ItemAttribute::getDisplay, 1)
//                .list();
//        List<Long> values = new ArrayList<>();
//        List<ItemAttribute> itemAttributes = new ArrayList<>();
//        if (ToolUtil.isNotEmpty(attributes)) {
//            for (ItemAttribute attribute : attributes) {
//                attribute.setDisplay(0);
//                itemAttributes.add(attribute);
//                values.add(attribute.getAttributeId());
//            }
//        }
//        itemAttributeService.updateBatchById(itemAttributes);
//        List<AttributeValues> valuesList = values.size() == 0 ? new ArrayList<>() : attributeValuesService.lambdaQuery().in(AttributeValues::getAttributeId, values)
//                .in(AttributeValues::getDisplay, 1).list();
//
//        if (ToolUtil.isNotEmpty(valuesList)) {
//            List<AttributeValues> attributeValuesList = new ArrayList<>();
//            for (AttributeValues attributeValues : valuesList) {
//                attributeValues.setDisplay(0);
//                attributeValuesList.add(attributeValues);
//            }
//            attributeValuesService.updateBatchById(attributeValuesList);
//        }
//        this.removeById(getKey(param));
    }

    @Override
    @BussinessLog
    @Transactional
    public void update(CategoryParam param) {
        Category category = this.getById(param.getCategoryId());
        if (!category.getCategoryName().equals(param.getCategoryName())) {
            Integer count = this.query().in("display", 1).in("category_name", param.getCategoryName()).count();
            if (count > 0) {
                throw new ServiceException(500, "名字以重复");
            }
        }
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
}
