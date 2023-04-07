package cn.atsoft.dasheng.sys.modular.system.service.impl;


import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.modular.system.entity.Tenant;
import cn.atsoft.dasheng.sys.modular.system.mapper.TenantMapper;
import cn.atsoft.dasheng.sys.modular.system.model.params.TenantParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.TenantResult;
import  cn.atsoft.dasheng.sys.modular.system.service.TenantService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 系统租户表 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-04-07
 */
@Service
public class TenantServiceImpl extends ServiceImpl<TenantMapper, Tenant> implements TenantService {

    @Override
    public void add(TenantParam param){
        isAdmin();
        checkPhone(param.getTelephone());
        checkName(param.getName());
        Tenant entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(TenantParam param){
        isAdmin();
        Tenant oldEntity = this.getOldEntity(param);
        if (ToolUtil.isEmpty(oldEntity)){
            throw new ServiceException(500,"数据未找到");
        }
        oldEntity.setDisplay(0);
        this.updateById(oldEntity);
    }

    @Override
    @Transactional
    public void update(TenantParam param){
        if (ToolUtil.isEmpty(param.getTenantId())){
            throw new ServiceException(500,"参数错误");
        }
        isAdmin();
        Tenant oldEntity = getOldEntity(param);
        if (ToolUtil.isEmpty(oldEntity)){
            throw new ServiceException(500,"数据未找到");
        }
        Tenant newEntity = getEntity(param);
        if (!oldEntity.getTelephone().equals(newEntity.getTelephone())){
            checkPhone(param.getTelephone());
        }
        if (!oldEntity.getName().equals(newEntity.getName())){
            checkName(param.getName());
        }
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public TenantResult findBySpec(TenantParam param){
        return null;
    }

    @Override
    public List<TenantResult> findListBySpec(TenantParam param){
        return null;
    }

    @Override
    public PageInfo<TenantResult> findPageBySpec(TenantParam param){
        Page<TenantResult> pageContext = getPageContext();
        IPage<TenantResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    @Override
    public PageInfo<TenantResult> findPageBySpec(TenantParam param, DataScope dataScope){
        Page<TenantResult> pageContext = getPageContext();
        IPage<TenantResult> page = this.baseMapper.customPageList(pageContext, param,dataScope);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(TenantParam param){
        return param.getTenantId();
    }

    private Page<TenantResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Tenant getOldEntity(TenantParam param) {
        return this.getById(getKey(param));
    }

    private Tenant getEntity(TenantParam param) {
        Tenant entity = new Tenant();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }
    private void isAdmin(){
        if(!LoginContextHolder.getContext().isAdmin()){
            throw new ServiceException(500,"非管理员 不可操作此功能");
        }
    }
    private void checkPhone(String telephone){
        Integer count = this.lambdaQuery().eq(Tenant::getTelephone, telephone).count();
        if (count>0){
            throw new ServiceException(500,"平台中已有此联系方式的其他租户");
        }
    }
    private void checkName(String name){
        Integer count = this.lambdaQuery().eq(Tenant::getName, name).count();
        if (count>0){
            throw new ServiceException(500,"平台中已有此名称的其他租户");
        }
    }

}
