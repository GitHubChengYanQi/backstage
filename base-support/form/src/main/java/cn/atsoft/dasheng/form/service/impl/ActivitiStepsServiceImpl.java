package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.ActivitiAudit;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.entity.ActivitiSteps;
import cn.atsoft.dasheng.form.mapper.ActivitiStepsMapper;
import cn.atsoft.dasheng.form.model.params.ActivitiStepsParam;
import cn.atsoft.dasheng.form.model.result.ActivitiAuditResult;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;
import cn.atsoft.dasheng.form.pojo.AuditRule;
import cn.atsoft.dasheng.form.pojo.AuditType;
import cn.atsoft.dasheng.form.pojo.QualityRules;
import cn.atsoft.dasheng.form.pojo.StepsType;
import cn.atsoft.dasheng.form.service.ActivitiAuditService;
import cn.atsoft.dasheng.form.service.ActivitiProcessLogService;
import cn.atsoft.dasheng.form.service.ActivitiProcessService;
import cn.atsoft.dasheng.form.service.ActivitiStepsService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.json.JSONUtil;
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

import static cn.atsoft.dasheng.form.pojo.StepsType.AUDIT;
import static cn.atsoft.dasheng.form.pojo.StepsType.START;
import static cn.atsoft.dasheng.form.pojo.StepsType.SEND;
import static cn.atsoft.dasheng.form.pojo.StepsType.BRANCH;
import static cn.atsoft.dasheng.form.pojo.StepsType.ROUTE;

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
    private ActivitiAuditService auditService;
    @Autowired
    private ActivitiProcessService processService;
    @Autowired
    private ActivitiProcessLogService activitiProcessLogService;
    @Autowired
    private UserService userService;


    @Override
    @Transactional
    public void add(ActivitiStepsParam param) {
        ActivitiProcess process = processService.getById(param.getProcessId());
        if (process.getStatus() >= 98) {
            throw new ServiceException(500, "当前流程已经发布,不可以修改步骤");
        }
        //修改 删除之间数据
        QueryWrapper<ActivitiSteps> stepsQueryWrapper = new QueryWrapper<>();
        stepsQueryWrapper.eq("process_id", param.getProcessId());
        List<ActivitiSteps> activitiSteps = this.list(stepsQueryWrapper);
        List<Long> ids = new ArrayList<>();
        for (ActivitiSteps activitiStep : activitiSteps) {
            ids.add(activitiStep.getSetpsId());
        }
        this.remove(stepsQueryWrapper);
        QueryWrapper<ActivitiAudit> queryWrapper = new QueryWrapper<>();
        if (ToolUtil.isNotEmpty(ids)) {
            queryWrapper.in("setps_id", ids);
            auditService.remove(queryWrapper);
        }
        ActivitiSteps entity = getEntity(param);
        entity.setType(START);
        this.save(entity);
        //添加配置
        if (ToolUtil.isEmpty(param.getAuditType())) {
            throw new ServiceException(500, "请设置正确的配置");
        }
        addAudit(param.getAuditType(), param.getAuditRule(), entity.getSetpsId());
        //添加节点
        if (ToolUtil.isNotEmpty(param.getChildNode())) {
            luYou(param.getChildNode(), entity.getSetpsId(), entity.getProcessId());
        } else {
            throw new ServiceException(500, "配置流程有误");
        }


    }

    /**
     * 添加节点
     *
     * @param node
     * @param supper
     * @param processId
     */
    public void luYou(ActivitiStepsParam node, Long supper, Long processId) {
        //添加路由
        ActivitiSteps activitiSteps = new ActivitiSteps();
        if (node.getType().toString().equals("4")) {
            activitiSteps.setStepType("路由");
        }
        //判断配置
        if (ToolUtil.isEmpty(node.getAuditType())) {
            throw new ServiceException(500, "请设置正确的配置");
        }

        if (node.getAuditType().toString().equals("send")) {
            ActivitiSteps steps = this.getById(supper);
            if (steps.getType().toString().equals("0")) {
                throw new ServiceException(500, "不可以直接抄送");
            }
        }
        switch (node.getType()) {
            case "1":
                activitiSteps.setType(AUDIT);
                break;
            case "2":
                activitiSteps.setType(SEND);
                break;
            case "4":
                activitiSteps.setType(ROUTE);
                break;
        }


        activitiSteps.setSupper(supper);
        activitiSteps.setStepType(node.getStepType());
        activitiSteps.setProcessId(processId);
        this.save(activitiSteps);
        //添加配置
        addAudit(node.getAuditType(), node.getAuditRule(), activitiSteps.getSetpsId());
        //修改父级
        ActivitiSteps fatherSteps = new ActivitiSteps();
        fatherSteps.setChildren(activitiSteps.getSetpsId().toString());
        QueryWrapper<ActivitiSteps> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("setps_id", supper);
        this.update(fatherSteps, queryWrapper);

        //添加ChildNode
        if (ToolUtil.isNotEmpty(node.getChildNode())) {
            luYou(node.getChildNode(), activitiSteps.getSetpsId(), processId);
        }
        //添加分支
        if (ToolUtil.isNotEmpty(node.getConditionNodeList())) {
            recursiveAdd(node.getConditionNodeList(), activitiSteps.getSetpsId(), processId);
        }

    }

    /**
     * 递归添加
     *
     * @param stepsParams
     * @param supper
     */
    public void recursiveAdd(List<ActivitiStepsParam> stepsParams, Long supper, Long processId) {
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
            //添加配置
            if (ToolUtil.isEmpty(stepsParam.getAuditType())) {
                throw new ServiceException(500, "请设置正确的配置");
            }
            addAudit(stepsParam.getAuditType(), stepsParam.getAuditRule(), activitiSteps.getSetpsId());
            //修改父级节点
            ActivitiSteps steps = this.query().eq("setps_id", supper).one();
            //修改父级分支

            if (ToolUtil.isEmpty(stepsParam.getChildNode())) {
                throw new ServiceException(500, "请在条件下添加动作");
            }
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
                luYou(stepsParam.getChildNode(), activitiSteps.getSetpsId(), processId);
            }

        }

    }

    /**
     * 添加数据配置
     *
     * @param auditType
     * @param auditRule
     * @param id
     */
    public void addAudit(AuditType auditType, AuditRule auditRule, Long id) {
        ActivitiAudit activitiAudit = new ActivitiAudit();
        activitiAudit.setSetpsId(id);
        switch (auditType) {
            case start:
            case process:
            case send:
                if (ToolUtil.isEmpty(auditRule)) {
                    throw new ServiceException(500, "配置数据错误");
                }
                activitiAudit.setRule(auditRule);
                break;

            case route:
            case branch:
                break;
        }
        activitiAudit.setType(String.valueOf(auditType));
        auditService.save(activitiAudit);
    }

    @Override
    public void delete(ActivitiStepsParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(ActivitiStepsParam param) {
        ActivitiSteps oldEntity = getOldEntity(param);
        ActivitiSteps newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    private Serializable getKey(ActivitiStepsParam param) {
        return param.getSetpsId();
    }

    private Page<ActivitiStepsResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ActivitiSteps getOldEntity(ActivitiStepsParam param) {
        return this.getById(getKey(param));
    }

    private ActivitiSteps getEntity(ActivitiStepsParam param) {
        ActivitiSteps entity = new ActivitiSteps();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public ActivitiStepsResult findBySpec(ActivitiStepsParam param) {
        return null;
    }

    @Override
    public List<ActivitiStepsResult> findListBySpec(ActivitiStepsParam param) {
        return null;
    }

    @Override
    public PageInfo<ActivitiStepsResult> findPageBySpec(ActivitiStepsParam param) {
        Page<ActivitiStepsResult> pageContext = getPageContext();
        IPage<ActivitiStepsResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    /**
     * 返回发起人
     *
     * @param id
     * @return
     */
    @Override
    public ActivitiStepsResult backStepsResult(Long id) {
        //通过流程id查询
        ActivitiSteps activitiSteps = this.query().eq("process_id", id).eq("supper", 0).one();
        if (ToolUtil.isEmpty(activitiSteps)) {
            return null;
        }
        ActivitiStepsResult activitiStepsResult = new ActivitiStepsResult();
        ToolUtil.copyProperties(activitiSteps, activitiStepsResult);
        //查询详情
        ActivitiAudit audit = auditService.query().eq("setps_id", activitiSteps.getSetpsId()).one();
        if (ToolUtil.isNotEmpty(audit)) {
            if (ToolUtil.isNotEmpty(audit.getRule())) {
                activitiStepsResult.setAuditType(audit.getType());
                activitiStepsResult.setAuditRule(audit.getRule());
            }
        }

        if (ToolUtil.isNotEmpty(activitiStepsResult.getChildren())) {
            ActivitiStepsResult childrenNode = getChildrenNode(Long.valueOf(activitiStepsResult.getChildren()));
            activitiStepsResult.setChildNode(childrenNode);
        }
        return activitiStepsResult;
    }

    @Override
    public ActivitiStepsResult getSteps(Long id) {
        ActivitiSteps steps = this.getById(id);
        ActivitiStepsResult stepsResult = new ActivitiStepsResult();
        ToolUtil.copyProperties(steps, stepsResult);
        ActivitiAuditResult auditResult = auditService.getAudit(stepsResult.getSetpsId());
        stepsResult.setServiceAudit(auditResult);
        return stepsResult;
    }

    /**
     * 递归取分支
     *
     * @param stepIds
     * @return
     */
    public List<ActivitiStepsResult> conditionNodeList(List<Long> stepIds) {
        //查询分支
        List<ActivitiSteps> activitiSteps = this.query().in("setps_id", stepIds).list();
        List<ActivitiStepsResult> activitiStepsResults = new ArrayList<>();
        for (ActivitiSteps activitiStep : activitiSteps) {
            ActivitiStepsResult activitiStepsResult = new ActivitiStepsResult();
            ToolUtil.copyProperties(activitiStep, activitiStepsResult);

            ActivitiAudit audit = auditService.query().eq("setps_id", activitiStep.getSetpsId()).one();
            if (ToolUtil.isNotEmpty(audit)) {
                activitiStepsResult.setAuditType(audit.getType());
                if (ToolUtil.isNotEmpty(audit.getRule())) {
                    activitiStepsResult.setAuditRule(audit.getRule());
                }
            }


            //查询节点
            if (ToolUtil.isNotEmpty(activitiStepsResult.getChildren())) {
                ActivitiStepsResult childrenNode = getChildrenNode(Long.valueOf(activitiStep.getChildren()));
                activitiStepsResult.setChildNode(childrenNode);
            }
            activitiStepsResults.add(activitiStepsResult);
        }
        return activitiStepsResults;
    }

    /**
     * 查询节点
     *
     * @param id
     * @return
     */
    public ActivitiStepsResult getChildrenNode(Long id) {
        //可能是路由可能是节点
        ActivitiSteps childrenNode = this.query().eq("setps_id", id).one();
        ActivitiStepsResult luyou = new ActivitiStepsResult();
        ToolUtil.copyProperties(childrenNode, luyou);
        //查询配置
        if (ToolUtil.isNotEmpty(childrenNode)) {
            ActivitiAudit audit = auditService.query().eq("setps_id", childrenNode.getSetpsId()).one();
            if (!ToolUtil.isEmpty(audit)) {
                luyou.setAuditType(audit.getType());
                if (ToolUtil.isNotEmpty(audit.getRule())) {
                    luyou.setAuditRule(audit.getRule());
                }
            }

        }
        //有分支走分支查询
        if (ToolUtil.isNotEmpty(luyou.getConditionNodes())) {
            String[] split = luyou.getConditionNodes().split(",");
            List<Long> nodeIds = new ArrayList<>();
            for (String s : split) {
                nodeIds.add(Long.valueOf(s));
            }
            List<ActivitiStepsResult> activitiStepsResults = conditionNodeList(nodeIds);
            luyou.setConditionNodeList(activitiStepsResults);
        }
        if (ToolUtil.isNotEmpty(luyou.getChildren())) {   //查节点
            ActivitiStepsResult node = getChildrenNode(Long.valueOf(luyou.getChildren()));
            luyou.setChildNode(node);
        }
        return luyou;
    }


    @Override
    public List<ActivitiStepsResult> backSteps(List<Long> ids) {

        LoginUser loginUser = LoginContextHolder.getContext().getUser();

        List<ActivitiStepsResult> stepsResults = new ArrayList<>();
        if (ids.size() > 0) {
            List<ActivitiSteps> steps = this.list(new QueryWrapper<ActivitiSteps>() {{
                in("setps_id", ids);
            }});

            List<Long> stepIds = new ArrayList<>();
            for (ActivitiSteps step : steps) {
                stepIds.add(step.getSetpsId());
            }


            List<ActivitiAuditResult> resultList = auditService.backAudits(stepIds);

            for (ActivitiSteps step : steps) {
                for (ActivitiAuditResult activitiAuditResult : resultList) {
                    if (step.getSetpsId().equals(activitiAuditResult.getSetpsId())) {
                        ActivitiStepsResult activitiStepsResult = new ActivitiStepsResult();
                        ToolUtil.copyProperties(step, activitiStepsResult);
                        activitiStepsResult.setServiceAudit(activitiAuditResult);
                        // 判断权限
                        if (ToolUtil.isNotEmpty(activitiAuditResult.getRule())) {

                            activitiStepsResult.setPermissions(this.checkUser(activitiAuditResult.getRule()));
                        }
                        stepsResults.add(activitiStepsResult);
                    }
                }

            }
        }
        return stepsResults;
    }


    public List<Long> selectUsers(AuditRule starUser) {
        List<Long> users = new ArrayList<>();
//        if (ToolUtil.isNotEmpty(starUser.getUsers())) {
//            for (AuditRule.Users user : starUser.getUsers()) {
//                users.add(Long.valueOf(user.getKey()));
//            }
//        }
//        if (ToolUtil.isNotEmpty(starUser.getDepts())) {
//            Long deptIds = 0L;
//            List<Long> positionIds = new ArrayList<>();
//            for (AuditRule.Depts dept : starUser.getDepts()) {
//                deptIds=Long.valueOf(dept.getKey());
//                for (AuditRule.Depts.Positions position : dept.getPositions()) {
//                    positionIds.add(Long.valueOf(position.getValue()));
//                }
//            }
//
//            List<User> userList = userService.getBaseMapper().listUserByPositionAndDept(positionIds,deptIds);
//            for (User user : userList) {
//                users.add(user.getUserId());
//            }
//        }
        return users;
    }


    @Override
    public Boolean checkUser(AuditRule starUser) {
        LoginUser user = LoginContextHolder.getContext().getUser();
        Long userId = user.getId();
        List<Long> users = this.selectUsers(starUser);
        for (Long aLong : users) {
            if (aLong.equals(userId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ActivitiStepsResult getStepResult(Long processId) {
        //TODO  查询步骤
        List<ActivitiStepsResult> steps = getStepsByProcessId(processId);

        List<Long> stepIds = new ArrayList<>();
        for (ActivitiStepsResult step : steps) {
            stepIds.add(step.getSetpsId());
        }
        //取出所有规则
        List<ActivitiAuditResult> auditResults = auditService.backAudits(stepIds);

        ActivitiStepsResult result = groupSteps(steps, auditResults, null);
        return result;
    }

    /**
     * 组合数据
     *
     * @param steps
     * @return
     */
    ActivitiStepsResult groupSteps(List<ActivitiStepsResult> steps, List<ActivitiAuditResult> auditResults, ActivitiStepsResult stepsResult) {
        //判断顶级
        if (ToolUtil.isEmpty(stepsResult)) {
            ActivitiStepsResult Top = new ActivitiStepsResult();
            //取出顶级
            for (ActivitiStepsResult step : steps) {
                if (step.getSupper() == 0) {
                    ToolUtil.copyProperties(step, Top);
                    //递归下一级
                    if (ToolUtil.isNotEmpty(Top.getChildren())) {
                        ActivitiStepsResult result = groupSteps(steps, auditResults, step);
                        Top.setChildNode(result);
                        getAudit(auditResults, Top);
                        return Top;
                    }
                }
            }
        } else {
            //获取当前规则
            getAudit(auditResults, stepsResult);
            //判断路由或节点
            if (ToolUtil.isNotEmpty(stepsResult.getChildren())) {
                for (ActivitiStepsResult step : steps) {
                    //对比下一级
                    if (step.getSetpsId().toString().equals(stepsResult.getChildren())) {
                        ActivitiStepsResult result = groupSteps(steps, auditResults, step);
                        stepsResult.setChildNode(result);
                    }
                }
            }
            //判断分支
            if (ToolUtil.isNotEmpty(stepsResult.getConditionNodes())) {
                String[] branchStepId = stepsResult.getConditionNodes().split(",");
                List<ActivitiStepsResult> branchSteps = new ArrayList<>();
                //取出当前分支集合
                for (ActivitiStepsResult step : steps) {
                    for (String s : branchStepId) {
                        if (step.getSetpsId().toString().equals(s)) {
                            branchSteps.add(step);
                        }
                    }
                }
                List<ActivitiStepsResult> childList = new ArrayList<>();
                for (ActivitiStepsResult branchStep : branchSteps) {
                    List<ActivitiStepsResult> branch = getBranch(steps, auditResults, branchStep);
                    childList.addAll(branch);
                }
                stepsResult.setConditionNodeList(childList);
            }

        }
        return stepsResult;
    }

    /**
     * 取分支
     *
     * @param steps
     * @param auditResults
     * @param branchStep
     * @return
     */
    List<ActivitiStepsResult> getBranch(List<ActivitiStepsResult> steps, List<ActivitiAuditResult> auditResults, ActivitiStepsResult branchStep) {
        List<ActivitiStepsResult> branchs = new ArrayList<>();
        ActivitiStepsResult branch = new ActivitiStepsResult();
        for (ActivitiStepsResult step : steps) {
            if (branchStep.getSetpsId().equals(step.getSetpsId())) {
                branch = step;
                getAudit(auditResults, branch);
                branchs.add(step);
                break;
            }
        }
        //判断节点是否有下一级
        for (ActivitiStepsResult step : steps) {
            if (step.getSetpsId().toString().equals(branch.getChildren())) {
                ActivitiStepsResult result = groupSteps(steps, auditResults, step);
                branch.setChildNode(result);
                break;
            }
        }

        return branchs;
    }


    /**
     * 获取当前规则
     */
    void getAudit(List<ActivitiAuditResult> auditResults, ActivitiStepsResult stepsResult) {
        if (ToolUtil.isNotEmpty(stepsResult)) {
            for (ActivitiAuditResult auditResult : auditResults) {
                if (auditResult.getSetpsId().equals(stepsResult.getSetpsId())) {
                    stepsResult.setAuditRule(auditResult.getRule());
                    stepsResult.setAuditType(auditResult.getType());
                }
            }
        }
    }

    /**
     * 返回当前processId 所有steps
     *
     * @param processId
     * @return
     */
    List<ActivitiStepsResult> getStepsByProcessId(Long processId) {
        List<ActivitiStepsResult> stepsResults = new ArrayList<>();
        List<ActivitiSteps> steps = this.query().eq("process_id", processId).list();
        for (ActivitiSteps step : steps) {
            ActivitiStepsResult stepsResult = new ActivitiStepsResult();
            ToolUtil.copyProperties(step, stepsResult);
            stepsResults.add(stepsResult);
        }
        return stepsResults;
    }

}
