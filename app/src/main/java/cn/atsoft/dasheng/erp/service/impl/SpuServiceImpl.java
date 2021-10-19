package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Category;
import cn.atsoft.dasheng.erp.entity.Spu;
import cn.atsoft.dasheng.erp.mapper.SpuMapper;
import cn.atsoft.dasheng.erp.model.params.SpuParam;
import cn.atsoft.dasheng.erp.model.result.SpuResult;
import cn.atsoft.dasheng.erp.service.CategoryService;
import  cn.atsoft.dasheng.erp.service.SpuService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2021-10-18
 */
@Service
public class SpuServiceImpl extends ServiceImpl<SpuMapper, Spu> implements SpuService {
    @Autowired
    private CategoryService categoryService;
    @Override
    public void add(SpuParam param){
        Spu entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(SpuParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(SpuParam param){
        Spu oldEntity = getOldEntity(param);
        Spu newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public SpuResult findBySpec(SpuParam param){
        return null;
    }

    @Override
    public List<SpuResult> findListBySpec(SpuParam param){
        return null;
    }

    @Override
    public PageInfo<SpuResult> findPageBySpec(SpuParam param){
        Page<SpuResult> pageContext = getPageContext();
        IPage<SpuResult> page = this.baseMapper.customPageList(pageContext, param);
        this.format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }
    private void format(List<SpuResult> param){
        List<Long> categoryIds = new ArrayList<>();
        for (SpuResult spuResult : param) {
            categoryIds.add(spuResult.getCategoryId());
        }
        QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.lambda().in(Category::getCategoryId,categoryIds);
        List<Category> categoryList = categoryIds.size()>0 ? new ArrayList<>() : categoryService.list(categoryQueryWrapper);

        for (SpuResult spuResult : param) {
            for (Category category : categoryList) {
                if (spuResult.getCategoryId().equals(category.getCategoryId())) {
                    spuResult.setCategoryName(category.getCategoryName());
                }
            }
        }
    }

    private Serializable getKey(SpuParam param){
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
