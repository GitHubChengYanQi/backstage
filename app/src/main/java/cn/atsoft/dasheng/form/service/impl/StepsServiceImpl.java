package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.form.entity.*;
import cn.atsoft.dasheng.form.mapper.ActivitiStepsMapper;
import cn.atsoft.dasheng.form.model.params.ActivitiSetpSetDetailParam;
import cn.atsoft.dasheng.form.model.params.ActivitiSetpSetParam;
import cn.atsoft.dasheng.form.model.params.ActivitiStepsParam;
import cn.atsoft.dasheng.form.model.result.ActivitiSetpSetDetailResult;
import cn.atsoft.dasheng.form.model.result.ActivitiSetpSetResult;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;
import cn.atsoft.dasheng.form.pojo.ViewUpdate;
import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.production.entity.ProcessRoute;
import cn.atsoft.dasheng.production.entity.ShipSetp;
import cn.atsoft.dasheng.production.model.params.ProcessRouteParam;
import cn.atsoft.dasheng.production.model.result.ProcessRouteResult;
import cn.atsoft.dasheng.production.model.result.ShipSetpResult;
import cn.atsoft.dasheng.production.service.ProcessRouteService;
import cn.atsoft.dasheng.production.service.ShipSetpService;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
    @Autowired
    private ShipSetpService shipSetpService;
    @Autowired
    private ActivitiProcessTaskService processTaskService;
    @Autowired
    private ActivitiProcessLogService processLogService;
    @Autowired
    private UserService userService;
    @Autowired
    private StepProcessService processService;
    @Autowired
    private StepProcessService stepProcessService;


    @Override
    @Transactional
    public Long add(ActivitiStepsParam param) {

        ActivitiSteps entity = getEntity(param);
        entity.setType(START);
        this.save(entity);
        Long processRouteId = null;
        if (!"shipStart".equals(param.getStepType())) {
            throw new ServiceException(500, "最顶级格式不对");
        }
        /**
         * 顶级
         */
        if (ToolUtil.isNotEmpty(param.getProcessRoute().getProcessRouteId())) {    //删除之前步骤
            List<ActivitiSteps> steps = this.query().eq("form_id", param.getProcessRoute().getProcessRouteId()).list();
            ArrayList<Long> list = new ArrayList<Long>() {{
                for (ActivitiSteps step : steps) {
                    if (!"ship".equals(step.getStepType())) {
                        add(step.getSetpsId());
                    }
                }
            }};
            setpSetService.remove(new QueryWrapper<ActivitiSetpSet>() {{
                in("setps_id", list);
            }});
            setpSetDetailService.remove(new QueryWrapper<ActivitiSetpSetDetail>() {{
                in("setps_id", list);
            }});

            this.removeByIds(list);
            processRouteId = param.getProcessRoute().getProcessRouteId();
            ProcessRoute route = processRouteService.getEntity(param.getProcessRoute());   //修改工艺
            route.setChildrens("");
            processRouteService.updateById(route);
        } else {
            processRouteId = addProcessRoute(param.getProcessRoute());
        }


        entity.setFormId(processRouteId);
        this.updateById(entity);

        //添加节点
        if (ToolUtil.isNotEmpty(param.getChildNode())) {
            luYou(processRouteId, param.getChildNode(), entity.getSetpsId(), entity.getFormId());
        }
        return processRouteId;
    }


    public Long addProcessRoute(ProcessRouteParam param) {
        param.setProcessRouteId(null);
        Integer count = processRouteService.query().eq("sku_id", param.getSkuId()).count();
        if (count > 1) {
            throw new ServiceException(500, "已有相同工艺路线");
        }
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
    public void luYou(Long processRouteId, ActivitiStepsParam node, Long supper, Long formId) {
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
            case "ship":
                if (ToolUtil.isEmpty(node.getProcessRoute())) {   //step更换工艺类型
                    throw new ServiceException(500, "请确定子路线");
                }
                updateSuperior(processRouteId, node.getProcessRoute().getProcessRouteId());  //跟新上级状态
                activitiSteps.setFormId(node.getProcessRoute().getProcessRouteId());

                setDetailaddProcessRoute(activitiSteps.getSetpsId(), node.getProcessRoute());  //添加子工艺产出 和 数量
                this.updateById(activitiSteps);
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
            luYou(processRouteId, node.getChildNode(), activitiSteps.getSetpsId(), formId);
        }
        //添加分支
        if (ToolUtil.isNotEmpty(node.getConditionNodeList())) {
            recursiveAdd(processRouteId, node.getConditionNodeList(), activitiSteps.getSetpsId(), formId);
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
    public void recursiveAdd(Long processRouteId, List<ActivitiStepsParam> stepsParams, Long supper, Long formId) {
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
                case "ship":
                    if (ToolUtil.isEmpty(stepsParam.getProcessRoute())) {
                        throw new ServiceException(500, "请确定子路线");
                    }
                    activitiSteps.setFormId(stepsParam.getProcessRoute().getProcessRouteId());
                    this.updateById(activitiSteps);
                    updateSuperior(processRouteId, stepsParam.getProcessRoute().getProcessRouteId());  //跟新上级状态

                    setDetailaddProcessRoute(activitiSteps.getSetpsId(), stepsParam.getProcessRoute());
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
                luYou(processRouteId, stepsParam.getChildNode(), activitiSteps.getSetpsId(), formId);
            }

        }

    }

    /**
     * 更新上级工艺child
     *
     * @param supperId
     * @param presentId
     */
    private void updateSuperior(Long supperId, Long presentId) {
        if (ToolUtil.isEmpty(presentId)) {
            throw new ServiceException(500, "子路线id为空");
        }
        ProcessRoute father = processRouteService.getById(supperId);
        ProcessRoute child = processRouteService.getById(presentId);
        List<Long> fatherList = JSON.parseArray(father.getChildrens(), Long.class);
        if (ToolUtil.isEmpty(fatherList)) {
            fatherList = new ArrayList<>();
        }
        List<Long> childList = JSON.parseArray(child.getChildrens(), Long.class);
        if (ToolUtil.isEmpty(childList)) {
            childList = new ArrayList<>();
        }
        for (Long aLong : childList) {
            if (aLong.equals(supperId)) {
                throw new ServiceException(500, "请检查路线，请勿循环添加");
            }
        }
        fatherList.add(presentId);
        fatherList.addAll(childList);
        String childrens = JSON.toJSONString(fatherList);
        father.setChildrens(childrens);
        processRouteService.updateById(father);

        updateFather(father, fatherList);
    }

    /**
     * 更新所有上级
     *
     * @param father
     * @param childrensList
     */
    private void updateFather(ProcessRoute father, List<Long> childrensList) {

        List<ProcessRoute> fathers = processRouteService.query().like("childrens", father.getProcessRouteId()).list();
        for (ProcessRoute children : fathers) {
            List<Long> list = JSON.parseArray(children.getChildrens(), Long.class);
            list.addAll(childrensList);
            String string = JSON.toJSONString(list);
            children.setChildrens(string);
            processRouteService.updateById(children);
            updateFather(children, list);
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
                switch (stepsResult.getStepType()) {
                    case "ship":
                        ProcessRouteResult result = getRoute(stepsResult.getFormId());
                        stepsResult.setProcessRoute(result);
                        break;
                    default:
                        stepsResult.setSetpSet(setpSetResult);
                }


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
        return new ActivitiStepsResult();
    }


    /**
     * 返回当前工艺下所有步骤
     *
     * @param formId
     * @return
     */
    @Override
    public List<ActivitiStepsResult> getStepsResultByFormId(Long formId) {
        List<ActivitiSteps> steps = this.query().eq("form_id", formId).or().eq("step_type", "ship").list();
        return BeanUtil.copyToList(steps, ActivitiStepsResult.class, new CopyOptions());
    }


    /**
     * 添加子工艺产出和产出数量
     */
    private void setDetailaddProcessRoute(Long stepId, ProcessRouteParam routeParam) {
        ActivitiSetpSetDetail setDetail = new ActivitiSetpSetDetail();
        setDetail.setSetpsId(stepId);
        setDetail.setType("out");
        setDetail.setSkuId(routeParam.getSkuId());
//        setDetail.setNum(routeParam.getShipNumber());
        setpSetDetailService.save(setDetail);
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

        if (ToolUtil.isNotEmpty(setpSetResult.getShipSetpId())) {
            ShipSetp shipSetp = shipSetpService.getById(setpSetResult.getShipSetpId());
            ShipSetpResult shipSetpResult = new ShipSetpResult();
            ToolUtil.copyProperties(shipSetp, shipSetpResult);
            setpSetResult.setShipSetpResult(shipSetpResult);
        }

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

    /**
     * 获取流程最后的处理时间
     *
     * @param fromId
     * @return
     */
    @Override
    public ViewUpdate getProcessTime(Long fromId) {

        ActivitiProcessTask processTask = processTaskService.query().eq("form_id", fromId).one();
        if (ToolUtil.isEmpty(processTask)) {
            return null;
        }

        List<ActivitiProcessLog> processLogs = processLogService.query().eq("task_id", processTask.getProcessTaskId()).orderByDesc("update_time").list();
        if (ToolUtil.isEmpty(processLogs)) {
            return null;
        }
        ActivitiProcessLog processLog = processLogs.get(0);
        User user = userService.getById(processLog.getUpdateUser());
        ViewUpdate viewUpdate = new ViewUpdate();

        viewUpdate.setUpdateTime(processLog.getUpdateTime());
        viewUpdate.setUpdateUser(user);
        return viewUpdate;
    }

    /**
     * 子工艺路线结构
     *
     * @param id
     * @return
     */
    private ProcessRouteResult getRoute(Long id) {
        ProcessRoute processRoute = processRouteService.getById(id);
        ProcessRouteResult routeResult = new ProcessRouteResult();
        ToolUtil.copyProperties(processRoute, routeResult);
        routeResult.setType("ship");
        return routeResult;
    }

    /**
     * 启用
     *
     * @param processRouteId
     */
    public void invoke(Long processRouteId) {

    }
}



