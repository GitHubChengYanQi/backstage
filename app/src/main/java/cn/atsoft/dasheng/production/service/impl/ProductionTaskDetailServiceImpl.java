package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionTaskDetail;
import cn.atsoft.dasheng.production.mapper.ProductionTaskDetailMapper;
import cn.atsoft.dasheng.production.model.params.ProductionTaskDetailParam;
import cn.atsoft.dasheng.production.model.result.ProductionTaskDetailResult;
import  cn.atsoft.dasheng.production.service.ProductionTaskDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2022-02-28
 */
@Service
public class ProductionTaskDetailServiceImpl extends ServiceImpl<ProductionTaskDetailMapper, ProductionTaskDetail> implements ProductionTaskDetailService {

    @Override
    public void add(ProductionTaskDetailParam param){
        ProductionTaskDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ProductionTaskDetailParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ProductionTaskDetailParam param){
        ProductionTaskDetail oldEntity = getOldEntity(param);
        ProductionTaskDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ProductionTaskDetailResult findBySpec(ProductionTaskDetailParam param){
        return null;
    }

    @Override
    public List<ProductionTaskDetailResult> findListBySpec(ProductionTaskDetailParam param){
        return null;
    }

    @Override
    public PageInfo<ProductionTaskDetailResult> findPageBySpec(ProductionTaskDetailParam param){
        Page<ProductionTaskDetailResult> pageContext = getPageContext();
        IPage<ProductionTaskDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ProductionTaskDetailParam param){
        return param.getProductionTaskDetailId();
    }

    private Page<ProductionTaskDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ProductionTaskDetail getOldEntity(ProductionTaskDetailParam param) {
        return this.getById(getKey(param));
    }

    private ProductionTaskDetail getEntity(ProductionTaskDetailParam param) {
        ProductionTaskDetail entity = new ProductionTaskDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
