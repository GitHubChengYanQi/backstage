package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.model.params.ErpPartsDetailParam;
import cn.atsoft.dasheng.app.model.params.PartsParam;
import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.form.entity.*;
import cn.atsoft.dasheng.form.mapper.ActivitiStepsMapper;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessParam;
import cn.atsoft.dasheng.form.model.params.ActivitiSetpSetDetailParam;
import cn.atsoft.dasheng.form.model.params.ActivitiSetpSetParam;
import cn.atsoft.dasheng.form.model.params.ActivitiStepsParam;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessResult;
import cn.atsoft.dasheng.form.model.result.ActivitiSetpSetDetailResult;
import cn.atsoft.dasheng.form.model.result.ActivitiSetpSetResult;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;
import cn.atsoft.dasheng.form.pojo.ProcessParam;
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
import jodd.util.ProcessRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.*;

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
    private ActivitiProcessService processService;
    @Autowired
    private PartsService partsService;


    @Override
    @Transactional
    public Long add(ActivitiStepsParam param) {

        Long processId = null;
        if (!"shipStart".equals(param.getStepType())) {
            throw new ServiceException(500, "最顶级格式不对");
        }
        /**
         * 顶级
         */
        if (ToolUtil.isNotEmpty(param.getProcess().getProcessId())) {    //删除之前步骤
            List<ActivitiSteps> steps = this.query().eq("process_id", param.getProcess().getProcessId()).list();
            ArrayList<Long> stepIds = new ArrayList<Long>() {{
                for (ActivitiSteps step : steps) {
                    add(step.getSetpsId());
                }
            }};
            setpSetService.remove(new QueryWrapper<ActivitiSetpSet>() {{
                in("setps_id", stepIds);
            }});
            setpSetDetailService.remove(new QueryWrapper<ActivitiSetpSetDetail>() {{
                in("setps_id", stepIds);
            }});

            this.removeByIds(stepIds);
            processId = param.getProcess().getProcessId();
        } else {
            processId = addProcess(param.getProcess());
        }

        ActivitiSteps entity = getEntity(param);
        entity.setType(START);
        entity.setSupper(0L);
        entity.setProcessId(processId);
        this.save(entity);


        Map<Long, Integer> skuNum = new HashMap<>();


        //添加节点
        if (ToolUtil.isNotEmpty(param.getChildNode())) {
            skuNum = luYou(processId, param.getChildNode(), entity.getSetpsId(), entity.getFormId());
        }

        /**
         * 生成bom
         */
        if (ToolUtil.isNotEmpty(skuNum)) {
            PartsParam parts = new PartsParam();
            parts.setSkuId(param.getProcess().getSkuId());
            Sku sku = skuService.getById(param.getProcess().getSkuId());
            if (ToolUtil.isEmpty(sku)) {
                throw new ServiceException(500, "物料不存在");
            }
            parts.setSpuId(sku.getSpuId());
            parts.setType("1");
            Set<Long> longSet = filterBom(entity.getProcessId(), skuNum.keySet());

            List<ErpPartsDetailParam> params = new ArrayList<>();
            for (Long aLong : longSet) {
                ErpPartsDetailParam detailParam = new ErpPartsDetailParam();
                Integer integer = skuNum.get(aLong);
                detailParam.setNumber(integer);
                detailParam.setSkuId(aLong);
                params.add(detailParam);
            }
            parts.setParts(params);
            Parts one = partsService.query().eq("sku_id", parts.getSkuId())
                    .eq("type", 1)
                    .eq("status", 99).eq("display", 1).one();
            if (ToolUtil.isEmpty(one)) {
                partsService.add(parts);
            }

        }
        return processId;
    }

    @Override
    public Long addProcessRoute(ProcessRouteParam param) {
        return null;
    }


    public Long addProcess(ActivitiProcessParam param) {
        Integer count = processService.query().eq("form_id", param.getSkuId()).count();
        if (count > 0) {
            throw new ServiceException(500, "当前物料已有工艺路线");
        }
        ActivitiProcess process = new ActivitiProcess();
        ToolUtil.copyProperties(param, process);
        process.setType("ship");
        process.setFormId(param.getSkuId());
        processService.save(process);
        return process.getProcessId();
    }


    /**
     * 添加节点
     *
     * @param node
     * @param supper
     */
    public Map<Long, Integer> luYou(Long processId, ActivitiStepsParam node, Long supper, Long formId) {
        Map<Long, Integer> skuNum = new HashMap<>();
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
        activitiSteps.setProcessId(processId);
        activitiSteps.setSupper(supper);
        activitiSteps.setStepType(node.getStepType());
        this.save(activitiSteps);
        //判断配置
        switch (node.getStepType()) {
            case "setp":

                Map<Long, Integer> map = addSetpSet(node.getSetpSet(), activitiSteps.getSetpsId());
                skuNum.putAll(map);

                break;
            case "route":
                break;
            case "ship":
                if (ToolUtil.isEmpty(node.getProcess())) {   //step更换工艺类型
                    throw new ServiceException(500, "请确定子路线");
                }
                activitiSteps.setFormId(node.getProcess().getProcessId());
                Map<Long, Integer> shipMap = setDetailaddProcess(activitiSteps.getSetpsId(), node.getProcess());//添加子工艺产出 和 数量
                skuNum.putAll(shipMap);

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
            Map<Long, Integer> map = luYou(processId, node.getChildNode(), activitiSteps.getSetpsId(), formId);
            skuNum.putAll(map);
        }
        //添加分支
        if (ToolUtil.isNotEmpty(node.getConditionNodeList())) {
            Map<Long, Integer> branchMap = recursiveAdd(processId, node.getConditionNodeList(), activitiSteps.getSetpsId(), formId);
            skuNum.putAll(branchMap);
        }
        return skuNum;
    }

    private Map<Long, Integer> addSetpSet(ActivitiSetpSetParam param, Long stepId) {
        Map<Long, Integer> map = new HashMap<>();

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
            map.put(detail.getSkuId(), detail.getNum());
        }
        setpSetDetailService.saveBatch(details);

        return map;
    }

    /**
     * 结构判断
     */
    private void judgeDiedLoop(Long supper, Long children) {
        List<ActivitiSteps> steps = this.query().eq("process_id", children).list();
        for (ActivitiSteps step : steps) {
            if (ToolUtil.isNotEmpty(step.getFormId()) && step.getFormId().equals(supper)) {
                throw new ServiceException(500, "请检查路线，请勿画圈");
            }
        }

    }


    /**
     * 递归添加
     *
     * @param stepsParams
     * @param supper
     */
    public Map<Long, Integer> recursiveAdd(Long processId, List<ActivitiStepsParam> stepsParams, Long supper, Long formId) {
        Map<Long, Integer> skuNum = new HashMap<>();
        //分支遍历
        for (ActivitiStepsParam stepsParam : stepsParams) {
            //获取super
            stepsParam.setSupper(supper);
            //存分支
            ActivitiSteps activitiSteps = new ActivitiSteps();
            ToolUtil.copyProperties(stepsParam, activitiSteps);
            activitiSteps.setType(BRANCH);
            activitiSteps.setProcessId(processId);
            this.save(activitiSteps);
            switch (stepsParam.getStepType()) {
                case "setp":
                    Map<Long, Integer> setpMap = addSetpSet(stepsParam.getSetpSet(), activitiSteps.getSetpsId());
                    skuNum.putAll(setpMap);
                    break;
                case "ship":
                    if (ToolUtil.isEmpty(stepsParam.getProcess())) {
                        throw new ServiceException(500, "请确定子路线");
                    }
                    activitiSteps.setFormId(stepsParam.getProcess().getProcessId());
                    this.updateById(activitiSteps);
                    Map<Long, Integer> shipMap = setDetailaddProcess(activitiSteps.getSetpsId(), stepsParam.getProcess());
                    skuNum.putAll(shipMap);
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
                luYou(processId, stepsParam.getChildNode(), activitiSteps.getSetpsId(), formId);
            }

        }
        return skuNum;
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

                ActivitiProcess process = processService.getById(formId);
                ActivitiProcessResult processResult = new ActivitiProcessResult();
                ToolUtil.copyProperties(process, processResult);
                processResult.setSkuId(processResult.getFormId());
                stepsResult.setProcess(processResult);
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
                        ActivitiProcessResult process = getProcess(stepsResult.getFormId(), stepsResult.getSetpsId());
                        stepsResult.setProcess(process);
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
        List<ActivitiSteps> steps = this.query().eq("process_id", formId).list();
        return BeanUtil.copyToList(steps, ActivitiStepsResult.class, new CopyOptions());
    }


    /**
     * 添加子工艺产出和产出数量
     */
    private Map<Long, Integer> setDetailaddProcess(Long stepId, ActivitiProcessParam param) {
        Map<Long, Integer> map = new HashMap<>();

        ActivitiProcess process = processService.getById(param.getProcessId());
        if (ToolUtil.isEmpty(process)) {
            throw new ServiceException(500, "子工艺不存在");
        }
        ActivitiSetpSetDetail setDetail = new ActivitiSetpSetDetail();
        setDetail.setSetpsId(stepId);
        setDetail.setType("out");
        setDetail.setSkuId(process.getFormId());
        setDetail.setNum(param.getNum());
        setpSetDetailService.save(setDetail);

        map.put(setDetail.getSkuId(), setDetail.getNum());
        return map;
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

    private ActivitiProcessResult getProcess(Long id, Long stepId) {

        ActivitiProcess process = processService.getById(id);
        if (ToolUtil.isEmpty(process)) {
            return new ActivitiProcessResult();
        }
        ActivitiSetpSetDetail setDetail = setpSetDetailService.query().eq("setps_id", stepId).one();
        ActivitiProcessResult processResult = new ActivitiProcessResult();
        ToolUtil.copyProperties(process, processResult);
        processResult.setSkuId(process.getFormId());
        if (ToolUtil.isNotEmpty(setDetail)) {
            processResult.setNum(setDetail.getNum());
        }
        return processResult;
    }

    /**
     * 过滤bom
     */
    private Set<Long> filterBom(Long processId, Set<Long> skuIds) {
        List<ActivitiSteps> steps = this.query().eq("process_id", processId).isNotNull("form_id").list();

        List<Long> processIds = new ArrayList<>();
        for (ActivitiSteps step : steps) {
            processIds.add(step.getFormId());
        }
        List<ActivitiProcess> processes = processIds.size() == 0 ? new ArrayList<>() : processService.listByIds(processIds);
        List<Long> skus = new ArrayList<>();
        for (ActivitiProcess process : processes) {
            skus.add(process.getFormId());
        }
        List<Parts> parts = skus.size() == 0 ? new ArrayList<>() : partsService.query().in("sku_id", skus).eq("status", 99).list();
        for (Parts part : parts) {
            List<Long> longs = JSON.parseArray(part.getChildrens(), Long.class);
            for (Long aLong : longs) {
                skuIds.removeIf(i -> i.equals(aLong));
            }
        }
        return skuIds;
    }
}



