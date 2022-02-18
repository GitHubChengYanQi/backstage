package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.form.entity.*;
import cn.atsoft.dasheng.form.mapper.ActivitiStepsMapper;
import cn.atsoft.dasheng.form.model.params.ActivitiSetpSetDetailParam;
import cn.atsoft.dasheng.form.model.params.ActivitiSetpSetParam;

import cn.atsoft.dasheng.form.model.params.ActivitiStepsParam;
import cn.atsoft.dasheng.form.model.result.*;
import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.production.entity.ProcessRoute;
import cn.atsoft.dasheng.production.model.params.ProcessRouteParam;
import cn.atsoft.dasheng.production.model.result.ProcessRouteResult;
import cn.atsoft.dasheng.production.service.ProcessRouteService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static cn.atsoft.dasheng.form.pojo.StepsType.*;

/**
 * <p>
 * 流程步骤表 服务实现类
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
@Service
public class StepsServiceImpl extends ServiceImpl<ActivitiStepsMapper, ActivitiSteps> implements StepsService {

    @Autowired
    private ActivitiSetpSetService setpSetService;
    @Autowired
    private ActivitiSetpSetDetailService setpSetDetailService;
    @Autowired
    private ProcessRouteService processRouteService;

    @Autowired
    private SkuService skuService;


    @Override
    @Transactional
    public void add(ActivitiStepsParam param) {


        ActivitiSteps entity = getEntity(param);
        entity.setType(START);
        this.save(entity);
        //TODO 需要添加生产产品

        /**
         * 判断是否是工艺路线
         */
        switch (param.getStepType()) {
            case "shipStart":
                if (ToolUtil.isNotEmpty(param.getProcessRoute().getProcessRouteId())) {
                    List<ActivitiSteps> steps = this.query().eq("form_id", param.getProcessRoute().getProcessRouteId()).list();
                    ArrayList<Long> list = new ArrayList<Long>() {{
                        for (ActivitiSteps step : steps) {
                            add(step.getSetpsId());
                        }
                    }};
                    setpSetService.remove(new QueryWrapper<ActivitiSetpSet>() {{
                        in("setps_id", list);
                    }});
                    setpSetDetailService.remove(new QueryWrapper<ActivitiSetpSetDetail>() {{
                        in("setps_id", list);
                    }});
                    this.removeByIds(steps);
                }
                Long route = addProcessRoute(param.getProcessRoute());
                entity.setFormId(route);
                this.updateById(entity);
                break;
        }
        //添加节点
        if (ToolUtil.isNotEmpty(param.getChildNode())) {
            luYou(param.getChildNode(), entity.getSetpsId(), entity.getFormId());
        }
    }


    public Long addProcessRoute(ProcessRouteParam param) {
        param.setProcessRouteId(null);
        ProcessRoute routeEntity = new ProcessRoute();
        ToolUtil.copyProperties(param, routeEntity);
        processRouteService.save(routeEntity);
        return routeEntity.getProcessRouteId();
    }


    /**
     * 添加节点
     *
     * @param node
     * @param supper
     */
    public void luYou(ActivitiStepsParam node, Long supper, Long formId) {
        //添加路由
        ActivitiSteps activitiSteps = new ActivitiSteps();

        switch (node.getType()) {
            case "1":
                activitiSteps.setType(AUDIT);
                break;
            case "2":
                activitiSteps.setType(SEND);
                break;
            case "4":
                activitiSteps.setType(ROUTE);
                activitiSteps.setStepType("路由");
                break;
        }
        activitiSteps.setSupper(supper);
        activitiSteps.setStepType(node.getStepType());
        activitiSteps.setFormId(formId);
        this.save(activitiSteps);
        //判断配置
        switch (node.getStepType()) {
            case "setp":
                addSetpSet(node.getSetpSet(), activitiSteps.getSetpsId());
                break;
            case "route":
                break;
            default:
                throw new ServiceException(500, "请确定类型");
        }

        //修改父级
        ActivitiSteps fatherSteps = this.getById(supper);
        fatherSteps.setChildren(activitiSteps.getSetpsId().toString());
        this.updateById(fatherSteps);


        //添加ChildNode
        if (ToolUtil.isNotEmpty(node.getChildNode())) {
            luYou(node.getChildNode(), activitiSteps.getSetpsId(), formId);
        }
        //添加分支
        if (ToolUtil.isNotEmpty(node.getConditionNodeList())) {
            recursiveAdd(node.getConditionNodeList(), activitiSteps.getSetpsId(), formId);
        }

    }

    private void addSetpSet(ActivitiSetpSetParam param, Long stepId) {
        ActivitiSetpSet activitiSetpSet = new ActivitiSetpSet();
        ToolUtil.copyProperties(param, activitiSetpSet);
        activitiSetpSet.setSetpsId(stepId);
        setpSetService.save(activitiSetpSet);
        List<ActivitiSetpSetDetail> details = new ArrayList<>();
        for (ActivitiSetpSetDetailParam activitiSetpSetDetailParam : param.getSetpSetDetails()) {
            ActivitiSetpSetDetail detail = new ActivitiSetpSetDetail();
            ToolUtil.copyProperties(activitiSetpSetDetailParam, detail);
            detail.setType(activitiSetpSetDetailParam.getEquals());
            detail.setSetpsId(stepId);
            details.add(detail);
        }
        setpSetDetailService.saveBatch(details);

    }

    /**
     * 递归添加
     *
     * @param stepsParams
     * @param supper
     */
    public void recursiveAdd(List<ActivitiStepsParam> stepsParams, Long supper, Long formId) {
        //分支遍历
        for (ActivitiStepsParam stepsParam : stepsParams) {
            //获取super
            stepsParam.setSupper(supper);
            //存分支
            ActivitiSteps activitiSteps = new ActivitiSteps();
            ToolUtil.copyProperties(stepsParam, activitiSteps);
            activitiSteps.setType(BRANCH);
            activitiSteps.setFormId(formId);
            this.save(activitiSteps);
            switch (stepsParam.getStepType()) {
                case "setp":
                    addSetpSet(stepsParam.getSetpSet(), activitiSteps.getSetpsId());
                    break;
                default:
                    throw new ServiceException(500, "请确定类型");
            }

            //修改父级节点
            ActivitiSteps steps = this.query().eq("setps_id", supper).one();

            if (ToolUtil.isEmpty(steps.getConditionNodes())) {
                steps.setConditionNodes(activitiSteps.getSetpsId().toString());
            } else {
                String branch = steps.getConditionNodes();
                steps.setConditionNodes(branch + "," + activitiSteps.getSetpsId());
            }

            QueryWrapper<ActivitiSteps> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("setps_id", supper);
            steps.setConditionNodes(steps.getConditionNodes());
            this.update(steps, queryWrapper);
            //继续递归添加
            if (ToolUtil.isNotEmpty(stepsParam.getChildNode())) {
                luYou(stepsParam.getChildNode(), activitiSteps.getSetpsId(), formId);
            }

        }

    }


    private Serializable getKey(ActivitiStepsParam param) {
        return param.getSetpsId();
    }


    private ActivitiSteps getEntity(ActivitiStepsParam param) {
        ActivitiSteps entity = new ActivitiSteps();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    /**
     * 顶级
     *
     * @param formId
     * @return
     */
    @Override
    public ActivitiStepsResult detail(Long formId) {
        List<ActivitiStepsResult> stepsResults = getStepsResultByFormId(formId);

        for (ActivitiStepsResult stepsResult : stepsResults) {
            if (stepsResult.getSupper() == 0) {
                ProcessRouteResult routeResult = processRouteService.detail(formId);
                stepsResult.setProcessRoute(routeResult);
                if (ToolUtil.isNotEmpty(stepsResult.getChildren())) {
                    ActivitiStepsResult children = getChildren(stepsResult.getChildren(), stepsResults);
                    stepsResult.setChildNode(children);
                }
                return stepsResult;
            }
        }
        return null;
    }

    /**
     * 子节点
     *
     * @param children
     * @param stepsResults
     * @return
     */
    private ActivitiStepsResult getChildren(String children, List<ActivitiStepsResult> stepsResults) {

        for (ActivitiStepsResult stepsResult : stepsResults) {
            if (stepsResult.getSetpsId().toString().equals(children)) {

                ActivitiSetpSetResult setpSetResult = setpSetResult(stepsResult.getSetpsId());
                stepsResult.setSetpSet(setpSetResult);

                if (ToolUtil.isNotEmpty(stepsResult.getChildren())) {        //下级是节点或者路由
                    ActivitiStepsResult result = getChildren(stepsResult.getChildren(), stepsResults);
                    stepsResult.setChildNode(result);
                }
                if (ToolUtil.isNotEmpty(stepsResult.getConditionNodes())) {   //下级是分支
                    List<ActivitiStepsResult> childBranch = new ArrayList<>();
                    String[] ids = stepsResult.getConditionNodes().split(",");
                    for (String id : ids) {
                        ActivitiStepsResult result = getChildren(id, stepsResults);
                        childBranch.add(result);
                    }
                    stepsResult.setConditionNodeList(childBranch);
                }

                return stepsResult;
            }
        }
        return null;
    }


    /**
     * 返回所有步骤
     *
     * @param formId
     * @return
     */
    private List<ActivitiStepsResult> getStepsResultByFormId(Long formId) {
        List<ActivitiSteps> activitiSteps = this.query().eq("form_id", formId).list();
        List<ActivitiStepsResult> stepsResults = BeanUtil.copyToList(activitiSteps, ActivitiStepsResult.class, new CopyOptions());
        return stepsResults;
    }

    /**
     * 工序步骤
     *
     * @param stepId
     * @return
     */
    private ActivitiSetpSetResult setpSetResult(Long stepId) {
        ActivitiSetpSet setpSet = setpSetService.query().eq("setps_id", stepId).one();
        if (ToolUtil.isEmpty(setpSet)) {
            return new ActivitiSetpSetResult();
        }
        ActivitiSetpSetResult setpSetResult = new ActivitiSetpSetResult();
        ToolUtil.copyProperties(setpSet, setpSetResult);
        setpSetResult.setSetpSetDetails(setpSetDetailResult(stepId));
        return setpSetResult;
    }

    /**
     * 工序步骤详情
     *
     * @param stepId
     * @return
     */
    private List<ActivitiSetpSetDetailResult> setpSetDetailResult(Long stepId) {
        List<ActivitiSetpSetDetail> setpSetDetails = setpSetDetailService.query().eq("setps_id", stepId).list();
        List<ActivitiSetpSetDetailResult> detailResults = BeanUtil.copyToList(setpSetDetails, ActivitiSetpSetDetailResult.class, new CopyOptions());
        List<Long> skuIds = new ArrayList<>();
        for (ActivitiSetpSetDetailResult setpSetDetailResult : detailResults) {
            if (ToolUtil.isNotEmpty(setpSetDetailResult.getSetpsId())) {
                skuIds.add(setpSetDetailResult.getSkuId());
            }
        }
        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);

        for (ActivitiSetpSetDetailResult detailResult : detailResults) {
            for (SkuResult skuResult : skuResults) {
                if (ToolUtil.isNotEmpty(detailResult.getSkuId()) && detailResult.getSkuId().equals(skuResult.getSkuId())) {
                    detailResult.setSkuResult(skuResult);
                    break;
                }
            }
        }

        return detailResults;
    }


}



