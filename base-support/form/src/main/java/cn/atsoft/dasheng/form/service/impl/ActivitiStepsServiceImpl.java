package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.ActivitiAudit;
import cn.atsoft.dasheng.form.entity.ActivitiSteps;
import cn.atsoft.dasheng.form.mapper.ActivitiStepsMapper;
import cn.atsoft.dasheng.form.model.params.ActivitiAuditParam;
import cn.atsoft.dasheng.form.model.params.ActivitiStepsParam;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;
import cn.atsoft.dasheng.form.service.ActivitiAuditService;
import cn.atsoft.dasheng.form.service.ActivitiStepsService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
    @Transactional
    public Long add(ActivitiStepsParam param) {
        ActivitiSteps entity = getEntity(param);
        this.save(entity);


        if (ToolUtil.isNotEmpty(param.getSupper())) {
            List<ActivitiSteps> stepsList = this.query().in("setps_id", param.getSetpsId()).eq("display", 1).list();
            for (ActivitiSteps steps : stepsList) {
                JSONArray jsonArray = JSONUtil.parseArray(steps.getChildrens());
                List<Long> longs = JSONUtil.toList(jsonArray, Long.class);
                for (Long aLong : longs) {
                    if (param.getSupper().equals(aLong)) {
                        throw new ServiceException(500, "请勿循环添加");
                    }
                }
            }
        }

        // 更新当前节点，及下级
        Map<String, List<Long>> childrenMap = getChildrens(entity.getSupper());
        entity.setChildrens(JSON.toJSONString(childrenMap.get("childrens")));
        entity.setChildren(JSON.toJSONString(childrenMap.get("children")));
        QueryWrapper<ActivitiSteps> partsQueryWrapper = new QueryWrapper<>();
        partsQueryWrapper.eq("setps_id", entity.getSetpsId());
        this.update(entity, partsQueryWrapper);

        updateChildren(entity.getSetpsId());
        return entity.getSetpsId();
    }


    /**
     * 递归
     */
    public Map<String, List<Long>> getChildrens(Long id) {
        Map<String, List<Long>> result = new HashMap<String, List<Long>>() {
            {
                put("children", new ArrayList<>());
                put("childrens", new ArrayList<>());
            }
        };
        List<Long> childrensSetpIds = new ArrayList<>();
        List<Long> setpIds = new ArrayList<>();
        ActivitiSteps steps = this.query().eq("setps_id", id).eq("display", 1).one();
        if (ToolUtil.isNotEmpty(steps)) {
            List<ActivitiSteps> activitiSteps = this.query().eq("supper", steps.getSetpsId()).eq("display", 1).list();
            for (ActivitiSteps detail : activitiSteps) {
                setpIds.add(detail.getSetpsId());
                childrensSetpIds.add(detail.getSetpsId());
                Map<String, List<Long>> childrenMap = this.getChildrens(detail.getSetpsId());
                childrensSetpIds.addAll(childrenMap.get("childrens"));
            }
            result.put("children", setpIds);
            result.put("childrens", childrensSetpIds);
        }
        return result;
    }

    /**
     * 更新包含它的
     */
    public void updateChildren(Long stepIds) {
        List<ActivitiSteps> steps = this.query().like("children", stepIds).eq("display", 1).list();
        for (ActivitiSteps step : steps) {
            Map<String, List<Long>> childrenMap = getChildrens(stepIds);
            step.setChildren(JSON.toJSONString(childrenMap.get("children")));
            step.setChildrens(JSON.toJSONString(childrenMap.get("childrens")));
            // update
            QueryWrapper<ActivitiSteps> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("setps_id", step.getSetpsId());
            this.update(step, queryWrapper);
            updateChildren(step.getSetpsId());
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

}
