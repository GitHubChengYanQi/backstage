package cn.atsoft.dasheng.coderule.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.coderule.entity.RestCodingRulesCategory;
import cn.atsoft.dasheng.coderule.mapper.RestCodingRulesCategoryMapper;
import cn.atsoft.dasheng.coderule.model.params.RestCodingRulesCategoryParam;
import cn.atsoft.dasheng.coderule.model.result.RestCodingRulesCategoryResult;
import cn.atsoft.dasheng.coderule.service.RestCodingRulesCategoryService;
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
public class RestCodingRulesCategroyServiceImpl extends ServiceImpl<RestCodingRulesCategoryMapper, RestCodingRulesCategory> implements RestCodingRulesCategoryService {

    @Override
    public void add(RestCodingRulesCategoryParam param){
        RestCodingRulesCategory entity = getEntity(param);
        this.save(entity);
    }

    @Override

    public void delete(RestCodingRulesCategoryParam param){
        this.removeById(getKey(param));
    }

    @Override

    public void update(RestCodingRulesCategoryParam param){
        RestCodingRulesCategory oldEntity = getOldEntity(param);
        RestCodingRulesCategory newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public RestCodingRulesCategoryResult findBySpec(RestCodingRulesCategoryParam param){
        return null;
    }

    @Override
    public List<RestCodingRulesCategoryResult> findListBySpec(RestCodingRulesCategoryParam param){
        return null;
    }

    @Override
    public PageInfo<RestCodingRulesCategoryResult> findPageBySpec(RestCodingRulesCategoryParam param){
        Page<RestCodingRulesCategoryResult> pageContext = getPageContext();
        IPage<RestCodingRulesCategoryResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(RestCodingRulesCategoryParam param){
        return param.getCodingRulesClassificationId();
    }

    private Page<RestCodingRulesCategoryResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private RestCodingRulesCategory getOldEntity(RestCodingRulesCategoryParam param) {
        return this.getById(getKey(param));
    }

    private RestCodingRulesCategory getEntity(RestCodingRulesCategoryParam param) {
        RestCodingRulesCategory entity = new RestCodingRulesCategory();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
