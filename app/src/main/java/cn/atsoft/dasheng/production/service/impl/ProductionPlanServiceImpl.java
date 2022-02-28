package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionPlan;
import cn.atsoft.dasheng.production.entity.ProductionPlanDetail;
import cn.atsoft.dasheng.production.mapper.ProductionPlanMapper;
import cn.atsoft.dasheng.production.model.params.ProductionPlanDetailParam;
import cn.atsoft.dasheng.production.model.params.ProductionPlanParam;
import cn.atsoft.dasheng.production.model.result.ProductionPlanResult;
import cn.atsoft.dasheng.production.service.ProductionPlanDetailService;
import cn.atsoft.dasheng.production.service.ProductionPlanService;
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
 *  生产计划主表 服务实现类
 * </p>
 *
 * @author 
 * @since 2022-02-25
 */
@Service
public class ProductionPlanServiceImpl extends ServiceImpl<ProductionPlanMapper, ProductionPlan> implements ProductionPlanService {

    @Autowired
    private ProductionPlanDetailService productionPlanDetailService;

    @Override
    public void add(ProductionPlanParam param){
        ProductionPlan entity = getEntity(param);
        this.save(entity);
        List<ProductionPlanDetail> details = new ArrayList<>();
        for (ProductionPlanDetailParam productionPlanDetailParam : param.getProductionPlanDetailParams()) {
            ProductionPlanDetail detail = new ProductionPlanDetail();
            ToolUtil.copyProperties(productionPlanDetailParam,detail);
            detail.setProductionPlanId(entity.getProductionPlanId());
            details.add(detail);
        }
        productionPlanDetailService.saveBatch(details);

    }

    @Override
    public void delete(ProductionPlanParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ProductionPlanParam param){
        ProductionPlan oldEntity = getOldEntity(param);
        ProductionPlan newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ProductionPlanResult findBySpec(ProductionPlanParam param){
        return null;
    }

    @Override
    public List<ProductionPlanResult> findListBySpec(ProductionPlanParam param){
        return null;
    }

    @Override
    public PageInfo<ProductionPlanResult> findPageBySpec(ProductionPlanParam param){
        Page<ProductionPlanResult> pageContext = getPageContext();
        IPage<ProductionPlanResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ProductionPlanParam param){
        return param.getProductionPlanId();
    }

    private Page<ProductionPlanResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ProductionPlan getOldEntity(ProductionPlanParam param) {
        return this.getById(getKey(param));
    }

    private ProductionPlan getEntity(ProductionPlanParam param) {
        ProductionPlan entity = new ProductionPlan();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
