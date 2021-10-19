package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.SkuValues;
import cn.atsoft.dasheng.erp.mapper.SkuValuesMapper;
import cn.atsoft.dasheng.erp.model.params.SkuValuesParam;
import cn.atsoft.dasheng.erp.model.result.SkuValuesResult;
import  cn.atsoft.dasheng.erp.service.SkuValuesService;
import cn.atsoft.dasheng.core.util.ToolUtil;
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
 * sku详情表 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-10-18
 */
@Service
public class SkuValuesServiceImpl extends ServiceImpl<SkuValuesMapper, SkuValues> implements SkuValuesService {



    @Override
    public void add(SkuValuesParam param){
        SkuValues entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(SkuValuesParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(SkuValuesParam param){
        SkuValues oldEntity = getOldEntity(param);
        SkuValues newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public SkuValuesResult findBySpec(SkuValuesParam param){
        return null;
    }

    @Override
    public List<SkuValuesResult> findListBySpec(SkuValuesParam param){
        return null;
    }

    @Override
    public PageInfo<SkuValuesResult> findPageBySpec(SkuValuesParam param){
        Page<SkuValuesResult> pageContext = getPageContext();
        IPage<SkuValuesResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(SkuValuesParam param){
        return param.getSkuDetailId();
    }

    private Page<SkuValuesResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private SkuValues getOldEntity(SkuValuesParam param) {
        return this.getById(getKey(param));
    }

    private SkuValues getEntity(SkuValuesParam param) {
        SkuValues entity = new SkuValues();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
