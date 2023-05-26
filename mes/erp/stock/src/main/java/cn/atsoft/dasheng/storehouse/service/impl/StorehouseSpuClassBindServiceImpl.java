package cn.atsoft.dasheng.storehouse.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.goods.category.entity.RestCategory;
import cn.atsoft.dasheng.goods.category.model.result.RestCategoryResult;
import cn.atsoft.dasheng.goods.category.service.RestCategoryService;
import cn.atsoft.dasheng.storehouse.entity.StorehouseSpuClassBind;
import cn.atsoft.dasheng.storehouse.mapper.StorehouseSpuClassBindMapper;
import cn.atsoft.dasheng.storehouse.model.params.StorehouseSpuClassBindParam;
import cn.atsoft.dasheng.storehouse.model.result.StorehouseSpuClassBindResult;
import  cn.atsoft.dasheng.storehouse.service.StorehouseSpuClassBindService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 仓库物料分类绑定表 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-05-15
 */
@Service
public class StorehouseSpuClassBindServiceImpl extends ServiceImpl<StorehouseSpuClassBindMapper, StorehouseSpuClassBind> implements StorehouseSpuClassBindService {

    @Autowired
    private RestCategoryService categoryService;

    @Override
    public void add(StorehouseSpuClassBindParam param){
        StorehouseSpuClassBind entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(StorehouseSpuClassBindParam param){
        //this.removeById(getKey(param));
        StorehouseSpuClassBind entity = this.getOldEntity(param);
        entity.setDisplay(0);
        this.updateById(entity);
    }

    @Override
    public void update(StorehouseSpuClassBindParam param){
        StorehouseSpuClassBind oldEntity = getOldEntity(param);
        StorehouseSpuClassBind newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public StorehouseSpuClassBindResult findBySpec(StorehouseSpuClassBindParam param){
        return null;
    }

    @Override
    public List<StorehouseSpuClassBindResult> findListBySpec(StorehouseSpuClassBindParam param){
        return null;
    }

    @Override
    public PageInfo<StorehouseSpuClassBindResult> findPageBySpec(StorehouseSpuClassBindParam param){
        Page<StorehouseSpuClassBindResult> pageContext = getPageContext();
        IPage<StorehouseSpuClassBindResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    @Override
    public PageInfo<StorehouseSpuClassBindResult> findPageBySpec(StorehouseSpuClassBindParam param, DataScope dataScope){
        Page<StorehouseSpuClassBindResult> pageContext = getPageContext();
        IPage<StorehouseSpuClassBindResult> page = this.baseMapper.customPageList(pageContext, param,dataScope);
        return PageFactory.createPageInfo(page);
    }
    @Override
    public void format(List<StorehouseSpuClassBindResult> dataList){
        List<Long> classIds = dataList.stream().map(StorehouseSpuClassBindResult::getSpuClassId).distinct().collect(Collectors.toList());
        List<RestCategory> restCategories = categoryService.listByIds(classIds);
        List<RestCategoryResult> restCategoryResults = BeanUtil.copyToList(restCategories, RestCategoryResult.class);
        dataList.forEach(item -> {
            restCategoryResults.forEach(restCategoryResult -> {
                if (item.getSpuClassId().equals(restCategoryResult.getSpuClassificationId())) {
                    item.setCategoryResult(restCategoryResult);
                }
            });
        });
    }

    private Serializable getKey(StorehouseSpuClassBindParam param){
        return param.getStorehouseSpuClassBindId();
    }

    private Page<StorehouseSpuClassBindResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private StorehouseSpuClassBind getOldEntity(StorehouseSpuClassBindParam param) {
        return this.getById(getKey(param));
    }

    private StorehouseSpuClassBind getEntity(StorehouseSpuClassBindParam param) {
        StorehouseSpuClassBind entity = new StorehouseSpuClassBind();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
