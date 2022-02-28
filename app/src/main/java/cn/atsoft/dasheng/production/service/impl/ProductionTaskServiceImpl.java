package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionTask;
import cn.atsoft.dasheng.production.mapper.ProductionTaskMapper;
import cn.atsoft.dasheng.production.model.params.ProductionTaskParam;
import cn.atsoft.dasheng.production.model.result.ProductionTaskResult;
import  cn.atsoft.dasheng.production.service.ProductionTaskService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 生产任务 服务实现类
 * </p>
 *
 * @author 
 * @since 2022-02-28
 */
@Service
public class ProductionTaskServiceImpl extends ServiceImpl<ProductionTaskMapper, ProductionTask> implements ProductionTaskService {

    @Override
    public void add(ProductionTaskParam param){
        ProductionTask entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ProductionTaskParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ProductionTaskParam param){
        ProductionTask oldEntity = getOldEntity(param);
        ProductionTask newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ProductionTaskResult findBySpec(ProductionTaskParam param){
        return null;
    }

    @Override
    public List<ProductionTaskResult> findListBySpec(ProductionTaskParam param){
        return null;
    }

    @Override
    public PageInfo<ProductionTaskResult> findPageBySpec(ProductionTaskParam param){
        Page<ProductionTaskResult> pageContext = getPageContext();
        IPage<ProductionTaskResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ProductionTaskParam param){
        return param.getProductionTaskId();
    }

    private Page<ProductionTaskResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ProductionTask getOldEntity(ProductionTaskParam param) {
        return this.getById(getKey(param));
    }

    private ProductionTask getEntity(ProductionTaskParam param) {
        ProductionTask entity = new ProductionTask();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
