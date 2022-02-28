package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionPlanDetail;
import cn.atsoft.dasheng.production.mapper.ProductionPlanDetailMapper;
import cn.atsoft.dasheng.production.model.params.ProductionPlanDetailParam;
import cn.atsoft.dasheng.production.model.result.ProductionPlanDetailResult;
import cn.atsoft.dasheng.production.service.ProductionPlanDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 生产计划子表 服务实现类
 * </p>
 *
 * @author 
 * @since 2022-02-25
 */
@Service
public class ProductionPlanDetailServiceImpl extends ServiceImpl<ProductionPlanDetailMapper, ProductionPlanDetail> implements ProductionPlanDetailService {

    @Override
    public void add(ProductionPlanDetailParam param){
        ProductionPlanDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ProductionPlanDetailParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ProductionPlanDetailParam param){
        ProductionPlanDetail oldEntity = getOldEntity(param);
        ProductionPlanDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ProductionPlanDetailResult findBySpec(ProductionPlanDetailParam param){
        return null;
    }

    @Override
    public List<ProductionPlanDetailResult> findListBySpec(ProductionPlanDetailParam param){
        return null;
    }

    @Override
    public PageInfo<ProductionPlanDetailResult> findPageBySpec(ProductionPlanDetailParam param){
        Page<ProductionPlanDetailResult> pageContext = getPageContext();
        IPage<ProductionPlanDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ProductionPlanDetailParam param){
        return param.getProductionPlanDetailId();
    }

    private Page<ProductionPlanDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ProductionPlanDetail getOldEntity(ProductionPlanDetailParam param) {
        return this.getById(getKey(param));
    }

    private ProductionPlanDetail getEntity(ProductionPlanDetailParam param) {
        ProductionPlanDetail entity = new ProductionPlanDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
