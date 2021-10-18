package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Category;
import cn.atsoft.dasheng.erp.mapper.CategoryMapper;
import cn.atsoft.dasheng.erp.model.params.CategoryParam;
import cn.atsoft.dasheng.erp.model.result.CategoryResult;
import  cn.atsoft.dasheng.erp.service.CategoryService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
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

    @Override
    public void add(CategoryParam param){
        Category entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(CategoryParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(CategoryParam param){
        Category oldEntity = getOldEntity(param);
        Category newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public CategoryResult findBySpec(CategoryParam param){
        return null;
    }

    @Override
    public List<CategoryResult> findListBySpec(CategoryParam param){
        return null;
    }

    @Override
    public PageInfo<CategoryResult> findPageBySpec(CategoryParam param){
        Page<CategoryResult> pageContext = getPageContext();
        IPage<CategoryResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(CategoryParam param){
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

}
