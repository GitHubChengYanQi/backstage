package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.QualityPlan;
import cn.atsoft.dasheng.erp.entity.QualityPlanDetail;
import cn.atsoft.dasheng.erp.mapper.QualityPlanMapper;
import cn.atsoft.dasheng.erp.model.params.QualityPlanDetailParam;
import cn.atsoft.dasheng.erp.model.params.QualityPlanParam;
import cn.atsoft.dasheng.erp.model.result.QualityPlanResult;
import cn.atsoft.dasheng.erp.service.QualityPlanDetailService;
import cn.atsoft.dasheng.erp.service.QualityPlanService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
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

    @Autowired
    private QualityPlanDetailService qualityPlanDetailService;

    @Transactional
    @Override
    public void add(QualityPlanParam param) {

        Integer count = this.query().in("plan_name", param.getPlanName()).count();
        if (count > 0) {
            throw new ServiceException(500, "名称以重复");
        }

        Integer planCoding = this.query().in("plan_coding", param.getPlanCoding()).count();
        if (planCoding > 0) {
            throw new ServiceException(500, "编码以重复");
        }

        List<QualityPlanDetailParam> planDetailParams = param.getQualityPlanDetailParams();
        if (ToolUtil.isEmpty(planDetailParams)) {
            throw new ServiceException(500, "请确定质检项");
        }
        QualityPlan entity = getEntity(param);
        this.save(entity);
        List<QualityPlanDetail> qualityPlanDetails = new ArrayList<>();
        for (QualityPlanDetailParam planDetailParam : planDetailParams) {
            if (ToolUtil.isEmpty(planDetailParam.getQualityCheckId())) {
                throw new ServiceException(500, "请选择质检项");
            }
            QualityPlanDetail qualityPlanDetail = new QualityPlanDetail();
            ToolUtil.copyProperties(planDetailParam, qualityPlanDetail);
            qualityPlanDetail.setPlanId(entity.getQualityPlanId());
            qualityPlanDetails.add(qualityPlanDetail);
        }

        qualityPlanDetailService.saveBatch(qualityPlanDetails);

    }

    @Override
    public void delete(QualityPlanParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(QualityPlanParam param) {
//        Integer count = this.query().in("plan_name", param.getPlanName()).count();
//        if (count > 0) {
//            throw new ServiceException(500, "名称已存在");
//        }

        List<QualityPlanDetailParam> planDetailParams = param.getQualityPlanDetailParams();
        if (ToolUtil.isEmpty(planDetailParams)) {
            throw new ServiceException(500, "请确定质检项");
        }


        QueryWrapper<QualityPlanDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("plan_id", param.getQualityPlanId());
        qualityPlanDetailService.remove(queryWrapper);


        List<QualityPlanDetail> qualityPlanDetails = new ArrayList<>();
        for (QualityPlanDetailParam planDetailParam : planDetailParams) {
            if (ToolUtil.isEmpty(planDetailParam.getQualityCheckId())) {
                throw new ServiceException(500, "请选择质检项");
            }
            QualityPlanDetail qualityPlanDetail = new QualityPlanDetail();
            ToolUtil.copyProperties(planDetailParam, qualityPlanDetail);
            qualityPlanDetail.setPlanId(param.getQualityPlanId());
            qualityPlanDetails.add(qualityPlanDetail);
        }
        qualityPlanDetailService.saveBatch(qualityPlanDetails);


        QualityPlan oldEntity = getOldEntity(param);
        QualityPlan newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public QualityPlanResult findBySpec(QualityPlanParam param) {
        return null;
    }

    @Override
    public List<QualityPlanResult> findListBySpec(QualityPlanParam param) {
        return null;
    }

    @Override
    public PageInfo<QualityPlanResult> findPageBySpec(QualityPlanParam param) {
        Page<QualityPlanResult> pageContext = getPageContext();
        IPage<QualityPlanResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(QualityPlanParam param) {
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

    public void format(List<QualityPlanResult> data) {
        for (QualityPlanResult datum : data) {

        }
    }
}
