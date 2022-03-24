package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.production.entity.ProductionTask;
import cn.atsoft.dasheng.production.entity.ProductionTaskDetail;
import cn.atsoft.dasheng.production.mapper.ProductionTaskDetailMapper;
import cn.atsoft.dasheng.production.model.params.ProductionTaskDetailParam;
import cn.atsoft.dasheng.production.model.result.ProductionTaskDetailResult;
import  cn.atsoft.dasheng.production.service.ProductionTaskDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.production.service.ProductionTaskService;
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
 * @author Captain_Jazz
 * @since 2022-03-22
 */
@Service
public class ProductionTaskDetailServiceImpl extends ServiceImpl<ProductionTaskDetailMapper, ProductionTaskDetail> implements ProductionTaskDetailService {
    @Autowired
    private ProductionTaskService productionTaskService;
    @Override
    public void add(ProductionTaskDetailParam param){
        List<ProductionTaskDetail> productionTaskDetails = this.query().eq("production_task_id", param.getProductionTaskId()).eq("display", 1).list();
        ProductionTask productionTask = productionTaskService.getById(param.getProductionTaskId());
        if (productionTask.getNumber().equals(productionTaskDetails.size())){

            throw  new ServiceException(500,"此工单报工数量已经饱和，不可再次报工");
        }
        if(productionTask.getNumber() == productionTaskDetails.size()+1){
            //TODO 更新任务状态
            ProductionTaskDetail entity = getEntity(param);
            this.save(entity);
        }

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
    @Override
    public List<ProductionTaskDetailResult> resultsByTaskId(Long taskId){
        List<ProductionTaskDetail> productionTaskDetails = this.query().eq("production_task_id", taskId).list();
        List<ProductionTaskDetailResult> results = new ArrayList<>();
        for (ProductionTaskDetail productionTaskDetail : productionTaskDetails) {
            ProductionTaskDetailResult result = new ProductionTaskDetailResult();
            ToolUtil.copyProperties(productionTaskDetail,result);
            results.add(result);
        }
        return results;
    }
}
