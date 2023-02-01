package cn.atsoft.dasheng.coderule.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.coderule.entity.RestCodeRuleCategory;
import cn.atsoft.dasheng.coderule.mapper.RestCodeRuleCategoryMapper;
import cn.atsoft.dasheng.coderule.model.params.RestCodeRuleCategoryParam;
import cn.atsoft.dasheng.coderule.model.result.RestCodeRuleCategoryResult;
import cn.atsoft.dasheng.coderule.service.RestCodeRuleCategoryService;
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
public class RestCodeRuleCategoryServiceImpl extends ServiceImpl<RestCodeRuleCategoryMapper, RestCodeRuleCategory> implements RestCodeRuleCategoryService {

    @Override
    public void add(RestCodeRuleCategoryParam param){
        RestCodeRuleCategory entity = getEntity(param);
        this.save(entity);
    }

    @Override

    public void delete(RestCodeRuleCategoryParam param){
        this.removeById(getKey(param));
    }

    @Override

    public void update(RestCodeRuleCategoryParam param){
        RestCodeRuleCategory oldEntity = getOldEntity(param);
        RestCodeRuleCategory newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public RestCodeRuleCategoryResult findBySpec(RestCodeRuleCategoryParam param){
        return null;
    }

    @Override
    public List<RestCodeRuleCategoryResult> findListBySpec(RestCodeRuleCategoryParam param){
        return null;
    }

    @Override
    public PageInfo<RestCodeRuleCategoryResult> findPageBySpec(RestCodeRuleCategoryParam param){
        Page<RestCodeRuleCategoryResult> pageContext = getPageContext();
        IPage<RestCodeRuleCategoryResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(RestCodeRuleCategoryParam param){
        return param.getCodingRulesClassificationId();
    }

    private Page<RestCodeRuleCategoryResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private RestCodeRuleCategory getOldEntity(RestCodeRuleCategoryParam param) {
        return this.getById(getKey(param));
    }

    private RestCodeRuleCategory getEntity(RestCodeRuleCategoryParam param) {
        RestCodeRuleCategory entity = new RestCodeRuleCategory();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
