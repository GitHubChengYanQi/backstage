package cn.atsoft.dasheng.sys.modular.system.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.sys.modular.system.entity.TenantOperationLog;
import cn.atsoft.dasheng.sys.modular.system.mapper.TenantOperationLogMapper;
import cn.atsoft.dasheng.sys.modular.system.model.params.TenantOperationLogParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.TenantOperationLogResult;
import  cn.atsoft.dasheng.sys.modular.system.service.TenantOperationLogService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 部门造作记录表 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-05-09
 */
@Service
public class TenantOperationLogServiceImpl extends ServiceImpl<TenantOperationLogMapper, TenantOperationLog> implements TenantOperationLogService {

    @Override
    public void add(TenantOperationLogParam param){
        TenantOperationLog entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(TenantOperationLogParam param){
        //this.removeById(getKey(param));
        TenantOperationLog entity = this.getOldEntity(param);
        entity.setDisplay(0);
        this.updateById(entity);
    }

    @Override
    public void update(TenantOperationLogParam param){
        TenantOperationLog oldEntity = getOldEntity(param);
        TenantOperationLog newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public TenantOperationLogResult findBySpec(TenantOperationLogParam param){
        return null;
    }

    @Override
    public List<TenantOperationLogResult> findListBySpec(TenantOperationLogParam param){
        return null;
    }

    @Override
    public PageInfo<TenantOperationLogResult> findPageBySpec(TenantOperationLogParam param){
        Page<TenantOperationLogResult> pageContext = getPageContext();
        IPage<TenantOperationLogResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    @Override
    public PageInfo<TenantOperationLogResult> findPageBySpec(TenantOperationLogParam param, DataScope dataScope){
        Page<TenantOperationLogResult> pageContext = getPageContext();
        IPage<TenantOperationLogResult> page = this.baseMapper.customPageList(pageContext, param,dataScope);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(TenantOperationLogParam param){
        return param.getTenantOperationLogId();
    }

    private Page<TenantOperationLogResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private TenantOperationLog getOldEntity(TenantOperationLogParam param) {
        return this.getById(getKey(param));
    }

    private TenantOperationLog getEntity(TenantOperationLogParam param) {
        TenantOperationLog entity = new TenantOperationLog();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
