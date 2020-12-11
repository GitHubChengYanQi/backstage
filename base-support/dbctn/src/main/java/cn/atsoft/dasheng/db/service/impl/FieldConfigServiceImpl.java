package cn.atsoft.dasheng.db.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.db.entity.FieldConfig;
import cn.atsoft.dasheng.db.mapper.FieldConfigMapper;
import cn.atsoft.dasheng.db.model.params.FieldConfigParam;
import cn.atsoft.dasheng.db.model.result.FieldConfigResult;
import  cn.atsoft.dasheng.db.service.FieldConfigService;
import cn.atsoft.dasheng.core.util.ToolUtil;
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
 * @since 2020-12-11
 */
@Service
public class FieldConfigServiceImpl extends ServiceImpl<FieldConfigMapper, FieldConfig> implements FieldConfigService {

    @Override
    public void add(FieldConfigParam param){
        FieldConfig entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(FieldConfigParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(FieldConfigParam param){
        FieldConfig oldEntity = getOldEntity(param);
        FieldConfig newEntity = getEntity(param);
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
        return param.getFieldName();
    }

    private Page<FieldConfigResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private FieldConfig getOldEntity(FieldConfigParam param) {
        return this.getById(getKey(param));
    }

    private FieldConfig getEntity(FieldConfigParam param) {
        FieldConfig entity = new FieldConfig();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
