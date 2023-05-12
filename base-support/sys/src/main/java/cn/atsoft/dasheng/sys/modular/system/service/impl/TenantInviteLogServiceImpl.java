package cn.atsoft.dasheng.sys.modular.system.service.impl;


import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.sys.modular.system.entity.TenantInviteLog;
import cn.atsoft.dasheng.sys.modular.system.mapper.TenantInviteLogMapper;
import cn.atsoft.dasheng.sys.modular.system.model.params.TenantInviteLogParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.TenantInviteLogResult;
import  cn.atsoft.dasheng.sys.modular.system.service.TenantInviteLogService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 邀请记录  申请记录 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-05-12
 */
@Service
public class TenantInviteLogServiceImpl extends ServiceImpl<TenantInviteLogMapper, TenantInviteLog> implements TenantInviteLogService {

    @Override
    public Long add(TenantInviteLogParam param){
        TenantInviteLog entity = getEntity(param);
        entity.setInviterUser(LoginContextHolder.getContext().getUserId());
        this.save(entity);
        return entity.getTenantInviteLogId();
    }

    @Override
    public void delete(TenantInviteLogParam param){
        //this.removeById(getKey(param));
        TenantInviteLog entity = this.getOldEntity(param);
        entity.setDisplay(0);
        this.updateById(entity);
    }

    @Override
    public void update(TenantInviteLogParam param){
        TenantInviteLog oldEntity = getOldEntity(param);
        TenantInviteLog newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public TenantInviteLogResult findBySpec(TenantInviteLogParam param){
        return null;
    }

    @Override
    public List<TenantInviteLogResult> findListBySpec(TenantInviteLogParam param){
        return null;
    }

    @Override
    public PageInfo<TenantInviteLogResult> findPageBySpec(TenantInviteLogParam param){
        Page<TenantInviteLogResult> pageContext = getPageContext();
        IPage<TenantInviteLogResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    @Override
    public PageInfo<TenantInviteLogResult> findPageBySpec(TenantInviteLogParam param, DataScope dataScope){
        Page<TenantInviteLogResult> pageContext = getPageContext();
        IPage<TenantInviteLogResult> page = this.baseMapper.customPageList(pageContext, param,dataScope);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(TenantInviteLogParam param){
        return param.getTenantInviteLogId();
    }

    private Page<TenantInviteLogResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private TenantInviteLog getOldEntity(TenantInviteLogParam param) {
        return this.getById(getKey(param));
    }

    private TenantInviteLog getEntity(TenantInviteLogParam param) {
        TenantInviteLog entity = new TenantInviteLog();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
