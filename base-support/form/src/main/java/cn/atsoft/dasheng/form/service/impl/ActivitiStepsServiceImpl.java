package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.ActivitiSteps;
import cn.atsoft.dasheng.form.mapper.ActivitiStepsMapper;
import cn.atsoft.dasheng.form.model.params.ActivitiStepsParam;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;
import cn.atsoft.dasheng.form.service.ActivitiStepsService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    @Transactional
    @Override
    public void add(ActivitiStepsParam param) {

        ActivitiSteps entity = getEntity(param);
        this.save(entity);
        if (ToolUtil.isNotEmpty(param.getLuYou())) {
            luYou(param.getLuYou(), entity.getSetpsId());
        } else if (ToolUtil.isNotEmpty(param.getChildNode())) {
            children(param.getChildNode(), entity.getSetpsId());
        }

    }

    //路由
    public void luYou(ActivitiStepsParam node, Long supper) {
        if (ToolUtil.isNotEmpty(node.getChildNode())) {
            ActivitiSteps activitiSteps = new ActivitiSteps();
            activitiSteps.setSupper(supper);
            this.save(activitiSteps);
            ActivitiSteps fatherSteps = new ActivitiSteps();
            fatherSteps.setChildren(activitiSteps.getSetpsId().toString());
            QueryWrapper<ActivitiSteps> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("setps_id", supper);
            this.update(fatherSteps, queryWrapper);
        }
        if (ToolUtil.isNotEmpty(node.getConditionNodeList())) {
            recursiveAdd(node.getConditionNodeList(), supper);
        }
    }

    /**
     * 递归添加
     *
     * @param stepsParams
     * @param supper
     */
    public void recursiveAdd(List<ActivitiStepsParam> stepsParams, Long supper) {
        //长度不唯一 就不是添加分支
        for (ActivitiStepsParam stepsParam : stepsParams) {
            //获取super
            stepsParam.setSupper(supper);
            //存分支
            ActivitiSteps activitiSteps = new ActivitiSteps();
            ToolUtil.copyProperties(stepsParam, activitiSteps);
            this.save(activitiSteps);
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
                children(stepsParam.getChildNode(), activitiSteps.getSetpsId());
            }
            if (ToolUtil.isNotEmpty(stepsParam.getLuYou())) {
                luYou(stepsParam.getLuYou(), activitiSteps.getSetpsId());
            }
        }

    }

    //添加子节点
    public void children(ActivitiStepsParam children, Long supper) {
        ActivitiSteps activitiSteps = new ActivitiSteps();
        ToolUtil.copyProperties(children, activitiSteps);
        activitiSteps.setSupper(supper);
        this.save(activitiSteps);
        ActivitiSteps fatherSteps = new ActivitiSteps();
        fatherSteps.setChildren(activitiSteps.getSetpsId().toString());
        QueryWrapper<ActivitiSteps> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("setps_id", supper);
        this.update(fatherSteps, queryWrapper);
        if (ToolUtil.isNotEmpty(children.getLuYou())) {
            luYou(children.getLuYou(), activitiSteps.getSetpsId());
        }
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

    @Override
    public ActivitiStepsResult backStepsResult(Long id) {
        List<ActivitiSteps> stepsList = this.query().eq("process_id", id).list();
        for (ActivitiSteps activitiSteps : stepsList) {
            if (activitiSteps.getSupper() == 0) {
                ActivitiStepsResult activitiStepsResult = new ActivitiStepsResult();
                ToolUtil.copyProperties(activitiSteps, activitiStepsResult);
                return activitiStepsResult;
            }
        }

        return null;
    }

    public ActivitiStepsResult conditionNodeList(List<Long> stepIds) {
        ActivitiStepsResult luYou = new ActivitiStepsResult();
        List<ActivitiStepsResult> conditionNodeList = new ArrayList<>();

        List<ActivitiSteps> activitiSteps = this.query().in("setps_id", stepIds).list();
        for (ActivitiSteps activitiStep : activitiSteps) {
            ActivitiStepsResult activitiStepsResult = new ActivitiStepsResult();

        }
        return luYou;
    }

}
