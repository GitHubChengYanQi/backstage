package cn.atsoft.dasheng.purchase.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.purchase.entity.ProcurementPlan;
import cn.atsoft.dasheng.purchase.mapper.ProcurementPlanMapper;
import cn.atsoft.dasheng.purchase.model.params.ProcurementPlanParam;
import cn.atsoft.dasheng.purchase.model.result.ProcurementPlanResult;
import  cn.atsoft.dasheng.purchase.service.ProcurementPlanService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 采购计划主表 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-12-21
 */
@Service
public class ProcurementPlanServiceImpl extends ServiceImpl<ProcurementPlanMapper, ProcurementPlan> implements ProcurementPlanService {

    @Override
    public void add(ProcurementPlanParam param){
        ProcurementPlan entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ProcurementPlanParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ProcurementPlanParam param){
        ProcurementPlan oldEntity = getOldEntity(param);
        ProcurementPlan newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ProcurementPlanResult findBySpec(ProcurementPlanParam param){
        return null;
    }

    @Override
    public List<ProcurementPlanResult> findListBySpec(ProcurementPlanParam param){
        return null;
    }

    @Override
    public PageInfo<ProcurementPlanResult> findPageBySpec(ProcurementPlanParam param){
        Page<ProcurementPlanResult> pageContext = getPageContext();
        IPage<ProcurementPlanResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ProcurementPlanParam param){
        return param.getProcurementPlanId();
    }

    private Page<ProcurementPlanResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ProcurementPlan getOldEntity(ProcurementPlanParam param) {
        return this.getById(getKey(param));
    }

    private ProcurementPlan getEntity(ProcurementPlanParam param) {
        ProcurementPlan entity = new ProcurementPlan();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
