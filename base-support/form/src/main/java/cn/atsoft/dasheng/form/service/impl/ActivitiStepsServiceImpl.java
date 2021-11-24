package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.ActivitiAudit;
import cn.atsoft.dasheng.form.entity.ActivitiSteps;
import cn.atsoft.dasheng.form.mapper.ActivitiStepsMapper;
import cn.atsoft.dasheng.form.model.params.ActivitiStepsParam;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;
import cn.atsoft.dasheng.form.model.result.StartUsers;
import cn.atsoft.dasheng.form.service.ActivitiAuditService;
import cn.atsoft.dasheng.form.service.ActivitiStepsService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void add(ActivitiStepsParam param) {

        //修改就删除
        QueryWrapper<ActivitiSteps> stepsQueryWrapper = new QueryWrapper<>();
        stepsQueryWrapper.eq("process_id", param.getProcessId());
        List<ActivitiSteps> activitiSteps = this.list(stepsQueryWrapper);
        List<Long> ids = new ArrayList<>();
        for (ActivitiSteps activitiStep : activitiSteps) {
            ids.add(activitiStep.getSetpsId());
        }
        this.remove(stepsQueryWrapper);
        QueryWrapper<ActivitiAudit> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("setps_id", ids);
        auditService.remove(queryWrapper);

        ActivitiSteps entity = getEntity(param);
        this.save(entity);
        //添加配置
        String jsonStr = JSONUtil.toJsonStr(param.getRule());
        addAudit(param.getAuditType(), jsonStr, entity.getSetpsId());
        //添加节点
        if (ToolUtil.isNotEmpty(param.getChildNode())) {
            luYou(param.getChildNode(), entity.getSetpsId(), entity.getProcessId());
        }

    }

    //路由
    public void luYou(ActivitiStepsParam node, Long supper, Long processId) {
        //添加路由
        ActivitiSteps activitiSteps = new ActivitiSteps();
        if (node.getType().equals("4")) {
            activitiSteps.setStepType("路由");
        }
        activitiSteps.setType(node.getType());
        activitiSteps.setSupper(supper);
        activitiSteps.setStepType(node.getStepType());
        activitiSteps.setProcessId(processId);
        this.save(activitiSteps);

        if (ToolUtil.isNotEmpty(node.getAuditType())) {
            if ("指定人".equals(node.getAuditType())) {
                String jsonStr = JSONUtil.toJsonStr(node.getRule());
                addAudit(node.getAuditType(), jsonStr, activitiSteps.getSetpsId());
            } else {
                addAudit(node.getAuditType(), null, activitiSteps.getSetpsId());
            }
        }

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
            activitiSteps.setProcessId(processId);
            this.save(activitiSteps);
            //添加配置
            if (ToolUtil.isNotEmpty(stepsParam.getRule())) {
                String jsonStr = JSONUtil.toJsonStr(stepsParam.getRule());
                addAudit(stepsParam.getAuditType(), jsonStr, activitiSteps.getSetpsId());
            }
            //修改父级节点
            ActivitiSteps steps = this.query().eq("setps_id", supper).one();
            //修改父级分支
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

    //添加配置数据
    public void addAudit(String type, String Json, Long id) {
        ActivitiAudit activitiAudit = new ActivitiAudit();
        activitiAudit.setRule(Json);
        activitiAudit.setSetpsId(id);
        activitiAudit.setType(type);
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
     * 返回顶级
     *
     * @param id
     * @return
     */
    @Override
    public ActivitiStepsResult backStepsResult(Long id) {
        //通过流程id查询
        ActivitiSteps activitiSteps = this.query().eq("process_id", id).eq("supper", 0).one();
        ActivitiStepsResult activitiStepsResult = new ActivitiStepsResult();
        ToolUtil.copyProperties(activitiSteps, activitiStepsResult);
        //查询详情
        ActivitiAudit audit = auditService.query().eq("setps_id", activitiSteps.getSetpsId()).one();
        if (ToolUtil.isNotEmpty(audit.getRule())) {
            StartUsers startUsers = JSONUtil.toBean(audit.getRule(), StartUsers.class);
            activitiStepsResult.setStartUsers(startUsers);
            activitiStepsResult.setAuditType(audit.getType());
        }
        if (ToolUtil.isNotEmpty(activitiStepsResult.getChildren())) {
            ActivitiStepsResult childrenNode = getChildrenNode(Long.valueOf(activitiStepsResult.getChildren()));
            activitiStepsResult.setChildNode(childrenNode);
        }
        return activitiStepsResult;
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
            if (ToolUtil.isNotEmpty(audit) && ToolUtil.isNotEmpty(audit.getRule())) {
                StartUsers startUsers = JSONUtil.toBean(audit.getRule(), StartUsers.class);
                activitiStepsResult.setStartUsers(startUsers);
                activitiStepsResult.setAuditType(audit.getType());
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


    public ActivitiStepsResult getChildrenNode(Long id) {
        //可能是路由可能是节点
        ActivitiSteps childrenNode = this.query().eq("setps_id", id).one();
        ActivitiStepsResult luyou = new ActivitiStepsResult();
        ToolUtil.copyProperties(childrenNode, luyou);
        //查询配置
        if (ToolUtil.isNotEmpty(childrenNode)) {
            ActivitiAudit audit = auditService.query().eq("setps_id", childrenNode.getSetpsId()).one();
            if (ToolUtil.isNotEmpty(audit) && ToolUtil.isNotEmpty(audit.getRule())) {
                StartUsers startUsers = JSONUtil.toBean(audit.getRule(), StartUsers.class);
                luyou.setStartUsers(startUsers);
                luyou.setAuditType(audit.getType());
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
}
