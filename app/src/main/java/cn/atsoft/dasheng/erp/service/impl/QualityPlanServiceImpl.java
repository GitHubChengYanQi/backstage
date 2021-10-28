package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.QualityPlan;
import cn.atsoft.dasheng.erp.mapper.QualityPlanMapper;
import cn.atsoft.dasheng.erp.model.params.QualityPlanParam;
import cn.atsoft.dasheng.erp.model.result.QualityPlanResult;
import  cn.atsoft.dasheng.erp.service.QualityPlanService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 质检方案 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-10-28
 */
@Service
public class QualityPlanServiceImpl extends ServiceImpl<QualityPlanMapper, QualityPlan> implements QualityPlanService {

    @Override
    public void add(QualityPlanParam param){
        QualityPlan entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(QualityPlanParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(QualityPlanParam param){
        QualityPlan oldEntity = getOldEntity(param);
        QualityPlan newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public QualityPlanResult findBySpec(QualityPlanParam param){
        return null;
    }

    @Override
    public List<QualityPlanResult> findListBySpec(QualityPlanParam param){
        return null;
    }

    @Override
    public PageInfo<QualityPlanResult> findPageBySpec(QualityPlanParam param){
        Page<QualityPlanResult> pageContext = getPageContext();
        IPage<QualityPlanResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(QualityPlanParam param){
        return param.getQualityPlanId();
    }

    private Page<QualityPlanResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private QualityPlan getOldEntity(QualityPlanParam param) {
        return this.getById(getKey(param));
    }

    private QualityPlan getEntity(QualityPlanParam param) {
        QualityPlan entity = new QualityPlan();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
