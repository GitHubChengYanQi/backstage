package cn.atsoft.dasheng.sys.modular.rest.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.sys.modular.rest.entity.DBFieldConfig;
import cn.atsoft.dasheng.sys.modular.rest.mapper.RestFieldConfigMapper;
import cn.atsoft.dasheng.sys.modular.rest.model.params.FieldConfigParam;
import cn.atsoft.dasheng.sys.modular.rest.model.result.FieldConfigResult;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.sys.modular.rest.service.FieldConfigService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 字段配置 服务实现类
 * </p>
 *
 * @author Sing
 * @since 2020-12-12
 */
@Service
public class FieldConfigServiceImpl extends ServiceImpl<RestFieldConfigMapper, DBFieldConfig> implements FieldConfigService {

    @Override
    public void add(FieldConfigParam param){
        DBFieldConfig entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(FieldConfigParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(FieldConfigParam param){
        DBFieldConfig oldEntity = getOldEntity(param);
        DBFieldConfig newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public FieldConfigResult findBySpec(FieldConfigParam param){
        return null;
    }

    @Override
    public List<FieldConfigResult> findListBySpec(FieldConfigParam param){
        return null;
    }

    @Override
    public PageInfo<FieldConfigResult> findPageBySpec(FieldConfigParam param){
        Page<FieldConfigResult> pageContext = getPageContext();
        IPage<FieldConfigResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(FieldConfigParam param){
        return param.getFieldId();
    }

    private Page<FieldConfigResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private DBFieldConfig getOldEntity(FieldConfigParam param) {
        return this.getById(getKey(param));
    }

    private DBFieldConfig getEntity(FieldConfigParam param) {
        DBFieldConfig entity = new DBFieldConfig();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
