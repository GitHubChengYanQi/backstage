package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.entity.*;
import cn.atsoft.dasheng.form.mapper.ActivitiStepsMapper;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.model.params.ActivitiSetpSetDetailParam;
import cn.atsoft.dasheng.form.model.params.ActivitiSetpSetParam;

import cn.atsoft.dasheng.form.model.params.ActivitiStepsParam;
import cn.atsoft.dasheng.form.model.result.*;
import cn.atsoft.dasheng.form.pojo.AppointUser;
import cn.atsoft.dasheng.form.pojo.AuditRule;
import cn.atsoft.dasheng.form.pojo.AuditType;
import cn.atsoft.dasheng.form.pojo.DeptPosition;
import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.production.entity.ProcessRoute;
import cn.atsoft.dasheng.production.model.params.ProcessRouteParam;
import cn.atsoft.dasheng.production.model.result.ProcessRouteResult;
import cn.atsoft.dasheng.production.service.ProcessRouteService;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class ActivitiStepsServiceImpl extends ServiceImpl<ActivitiStepsMapper, ActivitiSteps> implements ActivitiStepsService {

    @Autowired
    private ActivitiSetpSetService setpSetService;
    @Autowired
    private ActivitiSetpSetDetailService setpSetDetailService;
    @Autowired
    private ProcessRouteService processRouteService;


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
                Long route = addProcessRoute(param.getProcessRouteParam());
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
                addSetpSet(node.getSetpSetParam(), activitiSteps.getSetpsId());
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
        for (ActivitiSetpSetDetailParam activitiSetpSetDetailParam : param.getSetpSetDetailParam()) {
            ActivitiSetpSetDetail detail = new ActivitiSetpSetDetail();
            ToolUtil.copyProperties(activitiSetpSetDetailParam, detail);
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
                    addSetpSet(stepsParam.getSetpSetParam(), activitiSteps.getSetpsId());
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
                stepsResult.setProcessRouteResult(routeResult);
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
                stepsResult.setSetpSetResult(setpSetResult);

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


    private ActivitiSetpSetResult setpSetResult(Long stepId) {
        ActivitiSetpSet setpSet = setpSetService.query().eq("setps_id", stepId).one();
        ActivitiSetpSetResult setpSetResult = new ActivitiSetpSetResult();
        ToolUtil.copyProperties(setpSet, setpSetResult);
        setpSetResult.setSetpSetDetailResults(setpSetDetailResult(stepId));
        return setpSetResult;
    }


    private List<ActivitiSetpSetDetailResult> setpSetDetailResult(Long stepId) {
        List<ActivitiSetpSetDetail> setpSetDetails = setpSetDetailService.query().eq("setps_id", stepId).list();
        return BeanUtil.copyToList(setpSetDetails, ActivitiSetpSetDetailResult.class, new CopyOptions());
    }
}



