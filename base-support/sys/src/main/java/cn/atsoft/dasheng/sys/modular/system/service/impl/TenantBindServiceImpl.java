package cn.atsoft.dasheng.sys.modular.system.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.sys.modular.system.entity.TenantBind;
import cn.atsoft.dasheng.sys.modular.system.mapper.TenantBindMapper;
import cn.atsoft.dasheng.sys.modular.system.model.params.TenantBindParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.TenantBindResult;
import  cn.atsoft.dasheng.sys.modular.system.service.TenantBindService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 租户用户绑定表 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-04-19
 */
@Service
public class TenantBindServiceImpl extends ServiceImpl<TenantBindMapper, TenantBind> implements TenantBindService {

    @Override
    public void add(TenantBindParam param){
        TenantBind entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(TenantBindParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(TenantBindParam param){
        TenantBind oldEntity = getOldEntity(param);
        TenantBind newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public TenantBindResult findBySpec(TenantBindParam param){
        return null;
    }

    @Override
    public List<TenantBindResult> findListBySpec(TenantBindParam param){
        return null;
    }

    @Override
    public PageInfo<TenantBindResult> findPageBySpec(TenantBindParam param){
        Page<TenantBindResult> pageContext = getPageContext();
        IPage<TenantBindResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    @Override
    public PageInfo<TenantBindResult> findPageBySpec(TenantBindParam param, DataScope dataScope){
        Page<TenantBindResult> pageContext = getPageContext();
        IPage<TenantBindResult> page = this.baseMapper.customPageList(pageContext, param,dataScope);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(TenantBindParam param){
        return param.getTenantId();
    }

    private Page<TenantBindResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private TenantBind getOldEntity(TenantBindParam param) {
        return this.getById(getKey(param));
    }

    private TenantBind getEntity(TenantBindParam param) {
        TenantBind entity = new TenantBind();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
