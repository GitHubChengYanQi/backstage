package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.QualityPlan;
import cn.atsoft.dasheng.erp.entity.QualityPlanDetail;
import cn.atsoft.dasheng.erp.mapper.QualityPlanMapper;
import cn.atsoft.dasheng.erp.model.params.QualityPlanDetailParam;
import cn.atsoft.dasheng.erp.model.params.QualityPlanParam;
import cn.atsoft.dasheng.erp.model.result.QualityPlanResult;
import cn.atsoft.dasheng.erp.service.CodingRulesService;
import cn.atsoft.dasheng.erp.service.QualityPlanDetailService;
import cn.atsoft.dasheng.erp.service.QualityPlanService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.entity.FormData;
import cn.atsoft.dasheng.form.service.FormDataService;
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

    @Autowired
    private CodingRulesService codingRulesService;

    @Autowired
    private FormDataService formDataService;


    @Transactional
    @Override
    public void add(QualityPlanParam param) {

        Integer rulesId = codingRulesService.query().in("coding_rules_id", param.getPlanCoding()).count();
        if (rulesId > 0) {
            String coding = codingRulesService.backCoding(Long.valueOf(param.getPlanCoding()));
            param.setPlanCoding(coding);
        }

        Integer count = this.query().in("plan_name", param.getPlanName()).count();
        if (count > 0) {
            throw new ServiceException(500, "名称已重复");
        }

        Integer planCoding = this.query().in("plan_coding", param.getPlanCoding()).count();
        if (planCoding > 0) {
            throw new ServiceException(500, "编码已重复");
        }

        List<QualityPlanDetailParam> planDetailParams = param.getQualityPlanDetailParams();
        if (ToolUtil.isEmpty(planDetailParams)) {
            throw new ServiceException(500, "请确定质检项");
        }
        QualityPlan entity = getEntity(param);
        this.save(entity);
        List<QualityPlanDetail> qualityPlanDetails = new ArrayList<>();
        List<Long> ids = new ArrayList<>();

        for (QualityPlanDetailParam planDetailParam : planDetailParams) {
            ids.add(planDetailParam.getQualityCheckId());
        }
        long l = ids.stream().distinct().count();

        if (planDetailParams.size() > l) {
            throw new ServiceException(500, "不可以填写重复质检项");
        }


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
    @Transactional

    public void delete(QualityPlanParam param) {
        this.removeById(getKey(param));
        QueryWrapper<QualityPlanDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("plan_id", param.getQualityPlanId());
        qualityPlanDetailService.remove(queryWrapper);
    }

    @Override
    @Transactional

    public void update(QualityPlanParam param) {
        //判断方案名称
        QualityPlan qualityPlan = this.getById(param.getQualityPlanId());
        if (!qualityPlan.getPlanName().equals(param.getPlanName())) {
            Integer count = this.query().eq("plan_name", param.getPlanName()).count();
            if (count > 0) {
                throw new ServiceException(500, "方案名称已存在");
            }
        }
        qualityPlanDetailService.list();

        //修改质检项
        QueryWrapper<QualityPlanDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("plan_id", param.getQualityPlanId());

        List<QualityPlanDetail> list = qualityPlanDetailService.list(queryWrapper);
        List<Long> inTask = new ArrayList<>();
        for (QualityPlanDetail qualityPlanDetail : list) {
            inTask.add(qualityPlanDetail.getPlanDetailId());
        }
        List<FormData> list1 = formDataService.lambdaQuery().in(FormData::getFormId, inTask).list();
        if (ToolUtil.isNotEmpty(list1)) {
            throw new ServiceException(500, "此计划正在被使用中，无法进行更改");
        }

        qualityPlanDetailService.remove(queryWrapper);

        List<QualityPlanDetail> details = new ArrayList<>();
        for (QualityPlanDetailParam qualityPlanDetailParam : param.getQualityPlanDetailParams()) {
            QualityPlanDetail detail = new QualityPlanDetail();
            ToolUtil.copyProperties(qualityPlanDetailParam, detail);
            detail.setPlanId(qualityPlan.getQualityPlanId());
            details.add(detail);
        }
        qualityPlanDetailService.saveBatch(details);

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

        return PageFactory.createPageInfo(page);
    }

    @Override
    public List<QualityPlanResult> getPlanResults(List<Long> planIds) {

        List<QualityPlanResult> planResults = new ArrayList<>();
        if (ToolUtil.isEmpty(planIds)) {
            return planResults;
        }
        List<QualityPlan> qualityPlans = this.query().in("quality_plan_id", planIds).list();
        if (ToolUtil.isEmpty(qualityPlans)) {
            return planResults;
        }

        for (QualityPlan qualityPlan : qualityPlans) {
            QualityPlanResult planResult = new QualityPlanResult();
            ToolUtil.copyProperties(qualityPlan, planResult);
            planResults.add(planResult);
        }
        return planResults;
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


}
