package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.RulesRelation;
import cn.atsoft.dasheng.erp.mapper.RulesRelationMapper;
import cn.atsoft.dasheng.erp.model.params.RulesRelationParam;
import cn.atsoft.dasheng.erp.model.result.RulesRelationResult;
import cn.atsoft.dasheng.erp.service.RulesRelationService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 编码规则和模块的对应关系 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-25
 */
@Service
public class RulesRelationServiceImpl extends ServiceImpl<RulesRelationMapper, RulesRelation> implements RulesRelationService {

    @Override
    public void add(RulesRelationParam param) {
        RulesRelation rulesRelation = this.query().eq("module_id", param.getModuleId()).one();
        if (ToolUtil.isEmpty(rulesRelation)) {
            RulesRelation entity = getEntity(param);
            this.save(entity);
        } else {
            param.setRulesRelationId(rulesRelation.getRulesRelationId());
            update(param);
        }


    }

    @Override
    public void delete(RulesRelationParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(RulesRelationParam param) {

        RulesRelation oldEntity = getOldEntity(param);
        RulesRelation newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public RulesRelationResult findBySpec(RulesRelationParam param) {
        return null;
    }

    @Override
    public List<RulesRelationResult> findListBySpec(RulesRelationParam param) {
        return null;
    }

    @Override
    public PageInfo<RulesRelationResult> findPageBySpec(RulesRelationParam param) {
        Page<RulesRelationResult> pageContext = getPageContext();
        IPage<RulesRelationResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(RulesRelationParam param) {
        return param.getRulesRelationId();
    }

    private Page<RulesRelationResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private RulesRelation getOldEntity(RulesRelationParam param) {
        return this.getById(getKey(param));
    }

    private RulesRelation getEntity(RulesRelationParam param) {
        RulesRelation entity = new RulesRelation();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
