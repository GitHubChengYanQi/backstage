package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.audit.entity.ActivitiAudit;
import cn.atsoft.dasheng.audit.entity.ActivitiAuditV2;
import cn.atsoft.dasheng.audit.model.result.ActivitiAuditResultV2;
import cn.atsoft.dasheng.audit.service.ActivitiAuditService;
import cn.atsoft.dasheng.audit.service.ActivitiAuditServiceV2;
import cn.atsoft.dasheng.audit.service.ActivitiProcessFormLogService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.*;
import cn.atsoft.dasheng.form.mapper.ActivitiStepsMapper;
import cn.atsoft.dasheng.form.model.FormFieldParam;
import cn.atsoft.dasheng.form.model.params.ActivitiStepsParam;
import cn.atsoft.dasheng.audit.model.result.ActivitiAuditResult;
import cn.atsoft.dasheng.audit.model.result.ActivitiProcessLogResult;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResultV2;
import cn.atsoft.dasheng.form.pojo.*;
import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class ActivitiStepsServiceImpl extends ServiceImpl<ActivitiStepsMapper, ActivitiSteps> implements ActivitiStepsService {
    @Autowired
    private ActivitiAuditService auditService;
    @Autowired
    private ActivitiProcessService processService;
    @Autowired
    private ActivitiProcessFormLogService activitiProcessFormLogService;
    @Autowired
    private UserService userService;
    @Autowired
    private ActivitiSetpSetService setpSetService;
    @Autowired
    private ActivitiSetpSetDetailService setpSetDetailService;
    @Autowired
    private DocumentsActionService actionService;
    @Autowired
    private ModelService modelService;

    @Autowired
    private ActivitiAuditServiceV2 auditServiceV2;

    @Override
    @Transactional
    public void addProcess(ActivitiStepsParam param) {
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
        //TODO 需要添加生产产品

        //添加配置
        if (ToolUtil.isEmpty(param.getAuditType())) {
            throw new ServiceException(500, "请设置正确的配置");
        }
        /**
         * 判断是否是工艺路线
         */

        addAudit(param.getAuditType(), param.getAuditRule(), entity.getSetpsId());

        //添加节点
        if (ToolUtil.isNotEmpty(param.getChildNode())) {
            luYou(param.getChildNode(), entity.getSetpsId(), entity.getProcessId());
        } else {
            throw new ServiceException(500, "配置流程有误");
        }
    }

    @Override
    public void addProcessV2(ActivitiStepsParam param, Long parentStepId, Long processId) {


        /**
         * 添加节点
         */
        ActivitiSteps entity = getEntity(param);
        entity.setType(StepsType.getByType(param.getType()));
        entity.setProcessId(processId);
        entity.setSupper(parentStepId);
        this.save(entity);
        Long setpsId = entity.getSetpsId();
        /**
         * 添加节点规则
         */
        ActivitiAuditV2 activitiAudit = new ActivitiAuditV2();
        activitiAudit.setSetpsId(setpsId);

//        if (ToolUtil.isNotEmpty(auditRule)) {
//            activitiAudit.setRule(auditRule);
//            activitiAudit.setDocumentsStatusId(auditRule.getDocumentsStatusId());
//            activitiAudit.setFormType(auditRule.getFormType());
//            if (ToolUtil.isNotEmpty(auditRule.getActionStatuses())) {
////                actionService.combination(auditRule.getActionStatuses());
//                String action = JSON.toJSONString(auditRule.getActionStatuses());
//                activitiAudit.setAction(action);
//            }
//        }
        if (ToolUtil.isNotEmpty(param.getRoleList())) {
            for (FormFieldParam formFieldParam : param.getRoleList()) {
                if (ToolUtil.isNotEmpty(formFieldParam.getName()) && ToolUtil.isNotEmpty(formFieldParam.getValue())) {
                    switch (formFieldParam.getName()) {
                        case "documentsStatusId":
                            activitiAudit.setDocumentsStatusId(Long.valueOf(formFieldParam.getValue().toString()));
                            break;
                        case "actionStatuses":
                            activitiAudit.setAction((String) formFieldParam.getValue());
                            break;
                    }
                }
            }
        }
        activitiAudit.setRule(param.getRoleList());


        activitiAudit.setType(String.valueOf(param.getAuditType()));

        auditServiceV2.save(activitiAudit);


        //更新父级
        ActivitiSteps parent = this.query().eq("setps_id", parentStepId).one();
        //修改父级
        if(ToolUtil.isNotEmpty(parent)){
            if (ToolUtil.isEmpty(parent.getConditionNodes())) {
                parent.setConditionNodes(setpsId.toString());
            } else {
                parent.setConditionNodes(parent.getConditionNodes() + "," + setpsId);
            }
            parent.setChildren(setpsId.toString());
            this.updateById(parent);
        }
        parentStepId = entity.getSetpsId();
        //添加ChildNode
        if (ToolUtil.isNotEmpty(param.getChildNode())) {
            this.addProcessV2(param.getChildNode(), parentStepId, processId);
        } else if (ToolUtil.isNotEmpty(param.getConditionNodeList())) {
            for (ActivitiStepsParam activitiStepsParam : param.getConditionNodeList()) {
                this.addProcessV2(activitiStepsParam, parentStepId, processId);
            }
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
        if (node.getType().equals("4")) {
            activitiSteps.setStepType("路由");
        }
        //判断配置

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
     * 添加节点
     *
     * @param node
     * @param supper
     * @param processId
     */
    public void luYouV2(ActivitiStepsParam node, Long supper, Long processId) {
        //添加路由
        ActivitiSteps activitiSteps = new ActivitiSteps();
        //判断配置
        activitiSteps.setType(StepsType.getByType(node.getType()));


        activitiSteps.setSupper(supper);
        activitiSteps.setStepType(node.getStepType());
        activitiSteps.setProcessId(processId);
        this.save(activitiSteps);
        //添加配置
        addAuditV2(node.getAuditType(), node.getAuditRule(), activitiSteps.getSetpsId());
        //修改父级
        ActivitiSteps parentSteps = new ActivitiSteps();
        parentSteps.setChildren(activitiSteps.getSetpsId().toString());
        QueryWrapper<ActivitiSteps> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("setps_id", supper);
        this.update(parentSteps, queryWrapper);

        //添加ChildNode
        if (ToolUtil.isNotEmpty(node.getChildNode())) {
            luYouV2(node.getChildNode(), activitiSteps.getSetpsId(), processId);
        }
        //添加分支
        if (ToolUtil.isNotEmpty(node.getConditionNodeList())) {
            recursiveAddV2(node.getConditionNodeList(), activitiSteps.getSetpsId(), processId);
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
     * 递归添加
     *
     * @param stepsParams
     * @param supper
     */
    public void recursiveAddV2(List<ActivitiStepsParam> stepsParams, Long supper, Long processId) {
        //分支遍历
        for (ActivitiStepsParam stepsParam : stepsParams) {
            //获取super
            stepsParam.setSupper(supper);
            //存分支
            ActivitiSteps activitiSteps = new ActivitiSteps();
            ToolUtil.copyProperties(stepsParam, activitiSteps);
            activitiSteps.setType(branch);
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
                luYouV2(stepsParam.getChildNode(), activitiSteps.getSetpsId(), processId);
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
            case branch:
                activitiAudit.setRule(auditRule);
                break;
            case route:
                break;
        }
        if (ToolUtil.isNotEmpty(auditRule)) {
            activitiAudit.setDocumentsStatusId(auditRule.getDocumentsStatusId());
            activitiAudit.setFormType(auditRule.getFormType());
            if (ToolUtil.isNotEmpty(auditRule.getActionStatuses())) {
                actionService.combination(auditRule.getActionStatuses());
                String action = JSON.toJSONString(auditRule.getActionStatuses());
                activitiAudit.setAction(action);
            }
        }
        activitiAudit.setType(String.valueOf(auditType));
        auditService.save(activitiAudit);
    }

    /**
     * 添加数据配置
     *
     * @param auditType
     * @param auditRule
     * @param id
     */
    public void addAuditV2(AuditType auditType, AuditRule auditRule, Long id) {
        ActivitiAudit activitiAudit = new ActivitiAudit();
        activitiAudit.setSetpsId(id);
//        switch (auditType) {
//            case start:
//            case process:
//            case send:
//                if (ToolUtil.isEmpty(auditRule)) {
//                    throw new ServiceException(500, "配置数据错误");
//                }
//                activitiAudit.setRule(auditRule);
//                break;
//            case branch:
//                activitiAudit.setRule(auditRule);
//                break;
//            case route:
//                break;
//        }
        if (ToolUtil.isNotEmpty(auditRule)) {
            activitiAudit.setRule(auditRule);
            activitiAudit.setDocumentsStatusId(auditRule.getDocumentsStatusId());
            activitiAudit.setFormType(auditRule.getFormType());
            if (ToolUtil.isNotEmpty(auditRule.getActionStatuses())) {
                actionService.combination(auditRule.getActionStatuses());
                String action = JSON.toJSONString(auditRule.getActionStatuses());
                activitiAudit.setAction(action);
            }
        }
        activitiAudit.setType(String.valueOf(auditType));
        auditService.save(activitiAudit);
    }


    @Override
    public void delete(ActivitiStepsParam param) {
        ActivitiSteps entity = this.getEntity(param);
        entity.setDisplay(0);
        this.updateById(entity);
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
    public PageInfo findPageBySpec(ActivitiStepsParam param) {
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

    /**
     * 返回发起人
     *
     * @param id
     * @return
     */
    @Override
    public ActivitiStepsResultV2 backStepsResultV2(Long id) {
        //通过流程id查询
        ActivitiSteps activitiSteps = this.query().eq("process_id", id).eq("supper", 0).one();
        if (ToolUtil.isEmpty(activitiSteps)) {
            return null;
        }
        ActivitiStepsResultV2 activitiStepsResult = new ActivitiStepsResultV2();
        ToolUtil.copyProperties(activitiSteps, activitiStepsResult);
        //查询详情
        ActivitiAuditV2 audit = auditServiceV2.query().eq("setps_id", activitiSteps.getSetpsId()).one();
        if (ToolUtil.isNotEmpty(audit)) {
            if (ToolUtil.isNotEmpty(audit.getRule())) {
                activitiStepsResult.setAuditType(audit.getType());
                activitiStepsResult.setRoleList(audit.getRule());
            }
        }

        if (ToolUtil.isNotEmpty(activitiStepsResult.getChildren())) {
            ActivitiStepsResultV2 childrenNode = getChildrenNodeV2(Long.valueOf(activitiStepsResult.getChildren()));
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
     * 递归取分支
     *
     * @param stepIds
     * @return
     */
    public List<ActivitiStepsResultV2> conditionNodeListV2(List<Long> stepIds) {
        //查询分支
        List<ActivitiSteps> activitiSteps = this.query().in("setps_id", stepIds).list();
        List<ActivitiStepsResultV2> activitiStepsResults = new ArrayList<>();
        for (ActivitiSteps activitiStep : activitiSteps) {
            ActivitiStepsResultV2 activitiStepsResult = new ActivitiStepsResultV2();
            ToolUtil.copyProperties(activitiStep, activitiStepsResult);

            ActivitiAuditV2 audit = auditServiceV2.query().eq("setps_id", activitiStep.getSetpsId()).one();
            if (ToolUtil.isNotEmpty(audit)) {
                activitiStepsResult.setAuditType(audit.getType());
                if (ToolUtil.isNotEmpty(audit.getRule())) {
//                    activitiStepsResult.setAuditRule(audit.getRule());
                }
            }


            //查询节点
            if (ToolUtil.isNotEmpty(activitiStepsResult.getChildren())) {
                ActivitiStepsResultV2 childrenNode = getChildrenNodeV2(Long.valueOf(activitiStep.getChildren()));
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

    /**
     * 查询节点
     *
     * @param id
     * @return
     */
    public ActivitiStepsResultV2 getChildrenNodeV2(Long id) {
        //可能是路由可能是节点
        ActivitiSteps childrenNode = this.query().eq("setps_id", id).one();
        ActivitiStepsResultV2 luyou = new ActivitiStepsResultV2();
        ToolUtil.copyProperties(childrenNode, luyou);
        //查询配置
        if (ToolUtil.isNotEmpty(childrenNode)) {
            ActivitiAuditV2 audit = auditServiceV2.query().eq("setps_id", childrenNode.getSetpsId()).one();
            if (!ToolUtil.isEmpty(audit)) {
                luyou.setAuditType(audit.getType());
                if (ToolUtil.isNotEmpty(audit.getRule())) {
                    luyou.setRoleList(audit.getRule());
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
            List<ActivitiStepsResultV2> activitiStepsResults = conditionNodeListV2(nodeIds);
            luyou.setConditionNodeList(activitiStepsResults);
        }
        if (ToolUtil.isNotEmpty(luyou.getChildren())) {   //查节点
            ActivitiStepsResultV2 node = getChildrenNodeV2(Long.valueOf(luyou.getChildren()));
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

        for (AuditRule.Rule rule : starUser.getRules()) {
            switch (rule.getType()) {
                case AppointUsers:
                    for (AppointUser appointUser : rule.getAppointUsers()) {
                        users.add(Long.valueOf(appointUser.getKey()));
                    }
                    break;
                case AllPeople:
                    List<Long> allUsersId = userService.getAllUsersId();
                    users.addAll(allUsersId);
                    break;
                case DeptPositions:
                    for (DeptPosition deptPosition : rule.getDeptPositions()) {
                        List<Long> positionIds = new ArrayList<>();
                        for (DeptPosition.Position position : deptPosition.getPositions()) {
                            if (ToolUtil.isNotEmpty(position.getValue())) {
                                positionIds.add(Long.valueOf(position.getValue()));
                            }
                        }
                        List<User> userByPositionAndDept = userService.getUserByPositionAndDept(Long.valueOf(deptPosition.getKey()), positionIds);
                        for (User user : userByPositionAndDept) {
                            users.add(user.getUserId());
                        }
                    }

                    break;
            }
        }

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

    /**
     * 树形结构
     *
     * @param processId
     * @return
     */
    @Override
    public ActivitiStepsResult getStepResult(Long processId) {
        List<ActivitiStepsResult> steps = getStepsByProcessId(processId);
        if (ToolUtil.isEmpty(steps)) {
            return null;
        }
        List<Long> stepIds = new ArrayList<>();
        ActivitiStepsResult top = new ActivitiStepsResult();
        for (ActivitiStepsResult step : steps) {
            stepIds.add(step.getSetpsId());
            if (step.getSupper() == 0) {
                top = step;
            }
        }
        //取出所有步骤
        List<ActivitiAuditResult> auditResults = auditService.backAudits(stepIds);
        for (ActivitiAuditResult auditResult : auditResults) {
            AuditRule rule = auditResult.getRule();
            if (ToolUtil.isNotEmpty(rule) && ToolUtil.isNotEmpty(rule.getActionStatuses())) {
                for (ActionStatus actionStatus : rule.getActionStatuses()) {
                    DocumentsAction action = ToolUtil.isEmpty(actionStatus.getActionId()) ? new DocumentsAction() : actionService.getById(actionStatus.getActionId());
                    actionStatus.setActionName(action.getActionName());
                }
            }
        }

        return groupSteps(steps, auditResults, top);
    }

    /**
     * 树形结构
     *
     * @param processId
     * @return
     */
    @Override
    public ActivitiStepsResultV2 getStepResultV2(Long processId) {
        List<ActivitiStepsResultV2> steps = getStepsByProcessIdV2(processId);
        if (ToolUtil.isEmpty(steps)) {
            return null;
        }
        List<Long> stepIds = new ArrayList<>();
        ActivitiStepsResultV2 top = new ActivitiStepsResultV2();
        for (ActivitiStepsResultV2 step : steps) {
            stepIds.add(step.getSetpsId());
            if (step.getSupper() == 0) {
                top = step;
            }
        }
        //取出所有步骤
        List<ActivitiAuditResultV2> auditResults = auditService.backAuditsV2(stepIds);
//        for (ActivitiAuditResultV2 auditResult : auditResults) {
//            if (ToolUtil.isNotEmpty(rule) && ToolUtil.isNotEmpty(rule.getActionStatuses())) {
//                for (ActionStatus actionStatus : rule.getActionStatuses()) {
//                    DocumentsAction action = ToolUtil.isEmpty(actionStatus.getActionId()) ? new DocumentsAction() : actionService.getById(actionStatus.getActionId());
//                    actionStatus.setActionName(action.getActionName());
//                }
//            }
//        }

        ActivitiStepsResultV2 activitiStepsResultV2 = groupStepsV2(steps, auditResults, top);
        return activitiStepsResultV2;
    }

    /**
     * 组合数据
     *
     * @param steps
     * @return
     */
    ActivitiStepsResult groupSteps(List<ActivitiStepsResult> steps, List<ActivitiAuditResult> auditResults, ActivitiStepsResult stepsResult) {

        //获取当前规则
        getAudit(auditResults, stepsResult);
        //路由或节点
        if (ToolUtil.isNotEmpty(stepsResult.getChildren())) {
            //获取下一级
            ActivitiStepsResult childStep = getChildStep(steps, stepsResult);
            ActivitiStepsResult result = groupSteps(steps, auditResults, childStep);
            stepsResult.setChildNode(result);
        }
        //分支
        if (ToolUtil.isNotEmpty(stepsResult.getConditionNodes())) {
            //取下级分支
            List<ActivitiStepsResult> childBranch = getChildBranch(steps, stepsResult);
            List<ActivitiStepsResult> childList = new ArrayList<>();
            for (ActivitiStepsResult branchStep : childBranch) {
                List<ActivitiStepsResult> branch = getBranch(steps, auditResults, branchStep);
                childList.addAll(branch);
            }
            stepsResult.setConditionNodeList(childList);
        }
        return stepsResult;
    }

    /**
     * 组合数据
     *
     * @param steps
     * @return
     */
    ActivitiStepsResultV2 groupStepsV2(List<ActivitiStepsResultV2> steps, List<ActivitiAuditResultV2> auditResults, ActivitiStepsResultV2 stepsResult) {

        //获取当前规则
        getAuditV2(auditResults, stepsResult);
        if (ToolUtil.isNotEmpty(stepsResult)) {
            //路由或节点
            if (ToolUtil.isNotEmpty(stepsResult.getChildren())) {
                //获取下一级
                ActivitiStepsResultV2 childStep = getChildStepV2(steps, stepsResult);
                ActivitiStepsResultV2 result = groupStepsV2(steps, auditResults, childStep);
                stepsResult.setChildNode(result);
            }
            //分支
            if (ToolUtil.isNotEmpty(stepsResult.getConditionNodes())) {
                //取下级分支
                List<ActivitiStepsResultV2> childBranch = getChildBranchV2(steps, stepsResult);
                List<ActivitiStepsResultV2> childList = new ArrayList<>();
                for (ActivitiStepsResultV2 branchStep : childBranch) {
                    List<ActivitiStepsResultV2> branch = getBranchV2(steps, auditResults, branchStep);
                    childList.addAll(branch);
                }
                stepsResult.setConditionNodeList(childList);
            }
        }
        return stepsResult;
    }


    /**
     * 取出下一级
     */
    ActivitiStepsResult getChildStep(List<ActivitiStepsResult> steps, ActivitiStepsResult stepsResult) {
        for (ActivitiStepsResult step : steps) {
            if (step.getSetpsId().toString().equals(stepsResult.getChildren())) {
                return step;
            }
        }
        return null;
    }

    /**
     * 取出下一级
     */
    ActivitiStepsResultV2 getChildStepV2(List<ActivitiStepsResultV2> steps, ActivitiStepsResultV2 stepsResult) {
        for (ActivitiStepsResultV2 step : steps) {
            if (step.getSetpsId().toString().equals(stepsResult.getChildren())) {
                return step;
            }
        }
        return null;
    }

    /**
     * 取出下级分支
     */
    List<ActivitiStepsResult> getChildBranch(List<ActivitiStepsResult> steps, ActivitiStepsResult stepsResult) {
        List<ActivitiStepsResult> childBranch = new ArrayList<>();
        String[] ids = stepsResult.getConditionNodes().split(",");
        for (ActivitiStepsResult step : steps) {
            for (String id : ids) {
                if (step.getSetpsId().toString().equals(id)) {
                    childBranch.add(step);
                }
            }
        }
        return childBranch;
    }

    /**
     * 取出下级分支
     */
    List<ActivitiStepsResultV2> getChildBranchV2(List<ActivitiStepsResultV2> steps, ActivitiStepsResultV2 stepsResult) {
        List<ActivitiStepsResultV2> childBranch = new ArrayList<>();
        String[] ids = stepsResult.getConditionNodes().split(",");
        for (ActivitiStepsResultV2 step : steps) {
            for (String id : ids) {
                if (step.getSetpsId().toString().equals(id)) {
                    childBranch.add(step);
                }
            }
        }
        return childBranch;
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
        List<ActivitiStepsResult> branchList = new ArrayList<>();
        ActivitiStepsResult branch = new ActivitiStepsResult();
        for (ActivitiStepsResult step : steps) {
            if (branchStep.getSetpsId().equals(step.getSetpsId())) {
                branch = step;
                getAudit(auditResults, branch);
                branchList.add(step);
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

        return branchList;
    }

    /**
     * 取分支
     *
     * @param steps
     * @param auditResults
     * @param branchStep
     * @return
     */
    List<ActivitiStepsResultV2> getBranchV2(List<ActivitiStepsResultV2> steps, List<ActivitiAuditResultV2> auditResults, ActivitiStepsResultV2 branchStep) {
        List<ActivitiStepsResultV2> branchList = new ArrayList<>();
        ActivitiStepsResultV2 branch = new ActivitiStepsResultV2();
        for (ActivitiStepsResultV2 step : steps) {
            if (branchStep.getSetpsId().equals(step.getSetpsId())) {
                branch = step;
                getAuditV2(auditResults, branch);
                branchList.add(step);
                break;
            }
        }
        //判断节点是否有下一级
        for (ActivitiStepsResultV2 step : steps) {
            if (step.getSetpsId().toString().equals(branch.getChildren())) {
                ActivitiStepsResultV2 result = groupStepsV2(steps, auditResults, step);
                branch.setChildNode(result);
                break;
            }
        }

        return branchList;
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
                    stepsResult.setServiceAudit(auditResult);
                }
            }
        }
    }

    /**
     * 获取当前规则
     */
    void getAuditV2(List<ActivitiAuditResultV2> auditResults, ActivitiStepsResultV2 stepsResult) {
        if (ToolUtil.isNotEmpty(stepsResult)) {
            for (ActivitiAuditResultV2 auditResult : auditResults) {
                if (auditResult.getSetpsId().equals(stepsResult.getSetpsId())) {
                    stepsResult.setRoleList(auditResult.getRule());
                    stepsResult.setAuditType(auditResult.getType());
                    stepsResult.setServiceAudit(auditResult);
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
    @Override
    public List<ActivitiStepsResult> getStepsByProcessId(Long processId) {
        List<ActivitiStepsResult> stepsResults = new ArrayList<>();
        List<ActivitiSteps> steps = this.query().eq("process_id", processId).list();
        for (ActivitiSteps step : steps) {
            ActivitiStepsResult stepsResult = new ActivitiStepsResult();
            ToolUtil.copyProperties(step, stepsResult);
            stepsResults.add(stepsResult);
        }
        return stepsResults;
    }

    /**
     * 返回当前processId 所有steps
     *
     * @param processId
     * @return
     */
    @Override
    public List<ActivitiStepsResultV2> getStepsByProcessIdV2(Long processId) {
        List<ActivitiStepsResultV2> stepsResults = new ArrayList<>();
        List<ActivitiSteps> steps = this.query().eq("process_id", processId).list();
        for (ActivitiSteps step : steps) {
            ActivitiStepsResultV2 stepsResult = new ActivitiStepsResultV2();
            ToolUtil.copyProperties(step, stepsResult);
            stepsResults.add(stepsResult);
        }
        return stepsResults;
    }


    /**
     * 比对log
     *
     * @param stepResult
     * @param logs
     * @return
     */
    @Override
    public void getStepLog(ActivitiStepsResult stepResult, List<ActivitiProcessLogResult> logs) {
        if (ToolUtil.isEmpty(stepResult)) {
            return;
        }
        List<ActivitiProcessLogResult> logResults = new ArrayList<>();
        for (ActivitiProcessLogResult logResult : logs) {
            if (logResult.getSetpsId().equals(stepResult.getSetpsId())) {

                if (ToolUtil.isNotEmpty(logResult.getUpdateUser()) && logResult.getUpdateUser() != -100) {     //判断节点审核人
                    List<AuditRule.Rule> rules = stepResult.getAuditRule().getRules();
                    for (AuditRule.Rule rule : rules) {

                        switch (rule.getType()) {
                            case AllPeople:
                            case DeptPositions:
                            case MasterDocumentPromoter:
                            case Director:
                                Long updateUser = logResult.getUpdateUser();
                                User user = userService.getById(updateUser);
                                List<AppointUser> appointUsers = rule.getAppointUsers();
                                if (ToolUtil.isEmpty(appointUsers)) {
                                    appointUsers = new ArrayList<>();
                                }
                                appointUsers.add(new AppointUser() {{
                                    setKey(user.getUserId().toString());
                                    setTitle(user.getName());
                                    if (logResult.getStatus() == 1) {
                                        setAuditStatus(99);
                                    } else if (logResult.getStatus() == 0) {
                                        setAuditStatus(50);
                                    }
                                }});
                                rule.setAppointUsers(appointUsers);
                                break;
                        }


                        for (AppointUser appointUser : rule.getAppointUsers()) {
                            if (appointUser.getKey().equals(logResult.getUpdateUser().toString())) {
                                appointUser.setAuditStatus(99);
                            }
                        }
                    }
                }
                logResults.add(logResult);
                stepResult.setLogResult(logResult);

            }
            stepResult.setLogResults(logResults);
        }

        if (ToolUtil.isNotEmpty(stepResult.getChildNode())) {
            getStepLog(stepResult.getChildNode(), logs);
        }
        if (ToolUtil.isNotEmpty(stepResult.getConditionNodeList())) {
            for (ActivitiStepsResult activitiStepsResult : stepResult.getConditionNodeList()) {
                getStepLog(activitiStepsResult, logs);
            }
        }


    }

    @Override
    public ActivitiStepsResult getStepResultByType(String type) {
        ActivitiProcess process = processService.query().eq("module", type).eq("status", 99).eq("display", 1).one();
        if (ToolUtil.isNotEmpty(process)) {
            return getStepResult(process.getProcessId());
        }
        return null;
    }


}
