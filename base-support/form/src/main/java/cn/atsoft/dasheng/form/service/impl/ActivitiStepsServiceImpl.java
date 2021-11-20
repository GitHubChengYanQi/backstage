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

import java.io.Serializable;
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


    @Override
    public void add(ActivitiStepsParam param) {
        ActivitiSteps entity = getEntity(param);
        this.save(entity);
        //递归添加
        recursiveAdd(param.getConditionNodes(), entity.getSetpsId());

        //添加子节点
        ActivitiSteps childNode = param.getChildNode();
        childNode.setSupper(entity.getSetpsId());
        this.save(childNode);

        //修改顶级节点
        QueryWrapper<ActivitiSteps> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("setps_id", entity.getSetpsId());
        entity.setChildren(childNode.getSetpsId().toString());
        this.update(entity, queryWrapper);


    }

    /**
     * 递归添加
     *
     * @param stepsParams
     * @param supper
     */
    public void recursiveAdd(List<ActivitiStepsParam> stepsParams, Long supper) {

        for (ActivitiStepsParam stepsParam : stepsParams) {
            //获取super
            stepsParam.setSupper(supper);
            //存分支
            ActivitiSteps activitiSteps = new ActivitiSteps();
            ToolUtil.copyProperties(stepsParam, activitiSteps);
            this.save(activitiSteps);
            if (ToolUtil.isNotEmpty(stepsParam.getConditionNodes())) {
                recursiveAdd(stepsParam.getConditionNodes(), activitiSteps.getSetpsId());
            }

        }

    }
//
//    /**
//     * 递归
//     */
//    public Map<String, List<Long>> getChildrens(Long id) {
//        Map<String, List<Long>> result = new HashMap<String, List<Long>>() {
//            {
//                put("children", new ArrayList<>());
//            }
//        };
//        List<Long> childrensSetpIds = new ArrayList<>();
//        List<Long> setpIds = new ArrayList<>();
//        ActivitiSteps steps = this.query().eq("setps_id", id).eq("display", 1).one();
//        if (ToolUtil.isNotEmpty(steps)) {
//            List<ActivitiSteps> activitiSteps = this.query().eq("supper", steps.getSetpsId()).eq("display", 1).list();
//            for (ActivitiSteps detail : activitiSteps) {
//                setpIds.add(detail.getSetpsId());
//                childrensSetpIds.add(detail.getSetpsId());
//                Map<String, List<Long>> childrenMap = this.getChildrens(detail.getSetpsId());
//
//            }
//            result.put("children", setpIds);
//
//        }
//        return result;
//    }
//
//    /**
//     * 更新包含它的
//     */
//    public void updateChildren(Long supper) {
//        ActivitiSteps steps = this.query().eq("setps_id", supper).eq("display", 1).one();
//        if (ToolUtil.isNotEmpty(steps)) {
//            Map<String, List<Long>> childrenMap = getChildrens(steps.getSetpsId());
//            steps.setChildren(JSON.toJSONString(childrenMap.get("children")));
//            // update
//            QueryWrapper<ActivitiSteps> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("setps_id", steps.getSetpsId());
//            this.update(steps, queryWrapper);
//            updateChildren(steps.getSupper());
//        }
//    }


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


    @Override
    public void addBatch(List<ActivitiStepsParam> params) {
        recursiveAdd(params, 0L);
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

}
