package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.CodingRulesClassification;
import cn.atsoft.dasheng.erp.mapper.CodingRulesClassificationMapper;
import cn.atsoft.dasheng.erp.model.params.CodingRulesClassificationParam;
import cn.atsoft.dasheng.erp.model.result.CodingRulesClassificationResult;
import  cn.atsoft.dasheng.erp.service.CodingRulesClassificationService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 编码规则分类 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-22
 */
@Service
public class CodingRulesClassificationServiceImpl extends ServiceImpl<CodingRulesClassificationMapper, CodingRulesClassification> implements CodingRulesClassificationService {

    @Override
    public void add(CodingRulesClassificationParam param){
        CodingRulesClassification entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(CodingRulesClassificationParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(CodingRulesClassificationParam param){
        CodingRulesClassification oldEntity = getOldEntity(param);
        CodingRulesClassification newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public CodingRulesClassificationResult findBySpec(CodingRulesClassificationParam param){
        return null;
    }

    @Override
    public List<CodingRulesClassificationResult> findListBySpec(CodingRulesClassificationParam param){
        return null;
    }

    @Override
    public PageInfo<CodingRulesClassificationResult> findPageBySpec(CodingRulesClassificationParam param){
        Page<CodingRulesClassificationResult> pageContext = getPageContext();
        IPage<CodingRulesClassificationResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(CodingRulesClassificationParam param){
        return param.getCodingRulesClassificationId();
    }

    private Page<CodingRulesClassificationResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private CodingRulesClassification getOldEntity(CodingRulesClassificationParam param) {
        return this.getById(getKey(param));
    }

    private CodingRulesClassification getEntity(CodingRulesClassificationParam param) {
        CodingRulesClassification entity = new CodingRulesClassification();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
