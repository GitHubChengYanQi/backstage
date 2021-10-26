package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.Unit;
import cn.atsoft.dasheng.app.model.result.UnitResult;
import cn.atsoft.dasheng.app.service.UnitService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Tool;
import cn.atsoft.dasheng.erp.entity.ToolClassification;
import cn.atsoft.dasheng.erp.mapper.ToolMapper;
import cn.atsoft.dasheng.erp.model.params.ToolParam;
import cn.atsoft.dasheng.erp.model.result.ToolClassificationResult;
import cn.atsoft.dasheng.erp.model.result.ToolResult;
import cn.atsoft.dasheng.erp.service.CodingRulesService;
import cn.atsoft.dasheng.erp.service.ToolClassificationService;
import cn.atsoft.dasheng.erp.service.ToolService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 工具表 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-23
 */
@Service
public class ToolServiceImpl extends ServiceImpl<ToolMapper, Tool> implements ToolService {
    @Autowired
    private CodingRulesService codingRulesService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private ToolClassificationService toolClassificationService;

    @Transactional
    @Override
    public void add(ToolParam param) {
        Integer rulesId = codingRulesService.query().in("coding_rules_id", param.getCoding()).count();
        if (rulesId > 0) {
            String coding = codingRulesService.backCoding(Long.valueOf(param.getCoding()));
            param.setCoding(coding);
        }
        Integer count = this.query().in("coding", param.getCoding()).count();
        if (count > 0) {
            throw new ServiceException(500, "编码重复，请严谨设置编码规则");
        }

        Tool entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ToolParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(ToolParam param) {
        Integer rulesId = codingRulesService.query().in("coding_rules_id", param.getCoding()).count();
        if (rulesId > 0) {
            String coding = codingRulesService.backCoding(Long.valueOf(param.getCoding()));
            param.setCoding(coding);
        }
        Tool oldEntity = getOldEntity(param);
        Tool newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ToolResult findBySpec(ToolParam param) {
        return null;
    }

    @Override
    public List<ToolResult> findListBySpec(ToolParam param) {
        return null;
    }

    @Override
    public PageInfo<ToolResult> findPageBySpec(ToolParam param) {
        Page<ToolResult> pageContext = getPageContext();
        IPage<ToolResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ToolParam param) {
        return param.getToolId();
    }

    private Page<ToolResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Tool getOldEntity(ToolParam param) {
        return this.getById(getKey(param));
    }

    private Tool getEntity(ToolParam param) {
        Tool entity = new Tool();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    public void format(List<ToolResult> data) {
        List<Long> unitIds = new ArrayList<>();
        List<Long> differentIds = new ArrayList<>();
        for (ToolResult datum : data) {
            unitIds.add(datum.getUnitId());
            differentIds.add(datum.getToolClassificationId());
        }
        List<Unit> units = unitIds.size() == 0 ? new ArrayList<>() : unitService.lambdaQuery().in(Unit::getUnitId, unitIds).list();

        List<ToolClassification> toolClassifications = differentIds.size() == 0 ? new ArrayList<>() : toolClassificationService.lambdaQuery().in(ToolClassification::getToolClassificationId, differentIds).list();

        for (ToolResult datum : data) {
            if (ToolUtil.isNotEmpty(units)) {
                for (Unit unit : units) {
                    if (unit.getUnitId().equals(datum.getUnitId())) {
                        UnitResult unitResult = new UnitResult();
                        ToolUtil.copyProperties(unit, unitResult);
                        datum.setUnitResult(unitResult);
                        break;
                    }
                }
            }
            if (ToolUtil.isNotEmpty(toolClassifications)) {
                for (ToolClassification toolClassification : toolClassifications) {
                    if (toolClassification.getToolClassificationId().equals(datum.getToolClassificationId())) {
                        ToolClassificationResult toolClassificationResult = new ToolClassificationResult();
                        ToolUtil.copyProperties(toolClassification, toolClassificationResult);
                        datum.setToolClassificationResult(toolClassificationResult);
                        break;
                    }
                }
            }

        }
    }
}
