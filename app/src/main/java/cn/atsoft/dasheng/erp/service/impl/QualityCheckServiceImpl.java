package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.mapper.QualityCheckMapper;
import cn.atsoft.dasheng.erp.model.params.QualityCheckParam;
import cn.atsoft.dasheng.erp.model.result.QualityCheckClassificationResult;
import cn.atsoft.dasheng.erp.model.result.QualityCheckResult;
import cn.atsoft.dasheng.erp.model.result.ToolClassificationResult;
import cn.atsoft.dasheng.erp.model.result.ToolResult;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.ss.formula.functions.T;
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
 * 质检表 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-27
 */
@Service
public class QualityCheckServiceImpl extends ServiceImpl<QualityCheckMapper, QualityCheck> implements QualityCheckService {

    @Autowired
    private ToolService toolService;
    @Autowired
    private QualityPlanDetailService qualityPlanDetailService;
    @Autowired
    private QualityCheckClassificationService qualityCheckClassificationService;

    @Transactional
    @Override
    public void add(QualityCheckParam param) {
        String jsonStr = JSON.toJSONString(param.getTools());
        Integer count = this.query().eq("name", param.getName()).eq("tool", jsonStr).eq("quality_check_classification_id", param.getQualityCheckClassificationId()).count();
        if (count > 0) {
            throw new ServiceException(500, "已有相同质检");
        }
        param.setTool(jsonStr);
        QualityCheck entity = getEntity(param);
        this.save(entity);
    }


    @Override
    public void delete(QualityCheckParam param) {
        this.removeById(getKey(param));
    }


    @Override
    @Transactional
    public void update(QualityCheckParam param) {
        String jsonStr = JSON.toJSONString(param.getTools());
        param.setTool(jsonStr);
//        Integer count = this.query().eq("name", param.getName()).eq("tool", jsonStr).eq("quality_check_classification_id", param.getQualityCheckClassificationId()).count();
//        if (count > 0) {
//            throw new ServiceException(500, "已有相同质检");
//        }
        QueryWrapper<QualityPlanDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("quality_check_id", param.getQualityCheckId());
        int count = qualityPlanDetailService.count(queryWrapper);

        QualityCheck oldEntity = getOldEntity(param);
        QualityCheck newEntity = getEntity(param);
        if (count > 0 && oldEntity != newEntity) {
            throw new ServiceException(500, "该方案已被使用无法修改");
        }
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public QualityCheckResult findBySpec(QualityCheckParam param) {
        return null;
    }

    @Override
    public List<QualityCheckResult> findListBySpec(QualityCheckParam param) {
        return null;
    }

    @Override
    public PageInfo<QualityCheckResult> findPageBySpec(QualityCheckParam param) {
        Page<QualityCheckResult> pageContext = getPageContext();
        IPage<QualityCheckResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    @Override
    public List<QualityCheckResult> getChecks(List<Long> ids) {
        if (ToolUtil.isEmpty(ids)) {
            return new ArrayList<>();
        }
        List<QualityCheck> checks = this.listByIds(ids);
        List<QualityCheckResult> checkResults = new ArrayList<>();

        for (QualityCheck check : checks) {
            QualityCheckResult checkResult = new QualityCheckResult();
            ToolUtil.copyProperties(check, checkResult);
            checkResults.add(checkResult);
        }
        return checkResults;
    }

    private Serializable getKey(QualityCheckParam param) {
        return param.getQualityCheckId();
    }

    private Page<QualityCheckResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private QualityCheck getOldEntity(QualityCheckParam param) {
        return this.getById(getKey(param));
    }

    private QualityCheck getEntity(QualityCheckParam param) {
        QualityCheck entity = new QualityCheck();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    public void format(List<QualityCheckResult> data) {
        List<String> jsonids = new ArrayList<>();
        List<Long> classIds = new ArrayList<>();
        List<String> strings = new ArrayList<>();
        for (QualityCheckResult datum : data) {
            if (ToolUtil.isNotEmpty(datum.getTool())) {
                jsonids.add(datum.getTool());
                strings.add(datum.getTool());
            }
            classIds.add(datum.getQualityCheckClassificationId());
        }
        List<Long> toolIds = new ArrayList<>();
        for (String string : strings) {
            if (!string.equals("null")) {
                JSONArray jsonArray = JSONUtil.parseArray(string);
                List<Long> longs = JSONUtil.toList(jsonArray, Long.class);
                for (Long aLong : longs) {
                    toolIds.add(aLong);
                }
            }
        }
        List<Tool> tools = toolIds.size() == 0 ? new ArrayList<>() : toolService.query().in("tool_id", toolIds).list();

        List<QualityCheckClassification> qualityCheckClassifications = classIds.size() == 0 ? new ArrayList<>() : qualityCheckClassificationService.query().in("quality_check_classification_id", classIds).list();


        for (QualityCheckResult datum : data) {
            if (!datum.getTool().equals("null")){
                JSONArray jsonArray = JSONUtil.parseArray(datum.getTool());
                List<Long> list = JSONUtil.toList(jsonArray, Long.class);
                List<ToolResult> toolResults = new ArrayList<>();
                for (Long aLong : list) {
                    if (ToolUtil.isNotEmpty(tools)) {
                        for (Tool tool : tools) {
                            if (tool.getToolId().equals(aLong)) {
                                ToolResult toolResult = new ToolResult();
                                ToolUtil.copyProperties(tool, toolResult);
                                toolResults.add(toolResult);
                            }
                        }
                    }
                }
                datum.setTools(toolResults);
            }
            if (ToolUtil.isNotEmpty(qualityCheckClassifications)) {
                for (QualityCheckClassification qualityCheckClassification : qualityCheckClassifications) {
                    if (qualityCheckClassification.getQualityCheckClassificationId().equals(datum.getQualityCheckClassificationId())) {
                        QualityCheckClassificationResult qualityCheckClassificationResult = new QualityCheckClassificationResult();
                        ToolUtil.copyProperties(qualityCheckClassification, qualityCheckClassificationResult);
                        datum.setQualityCheckClassificationResult(qualityCheckClassificationResult);
                    }
                }

            }
        }
    }
}
