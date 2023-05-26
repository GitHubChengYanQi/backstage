package cn.atsoft.dasheng.sys.modular.system.service.impl;


import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.service.AuthService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.media.model.result.MediaUrlResult;
import cn.atsoft.dasheng.media.service.RestMediaService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.modular.system.entity.Tenant;
import cn.atsoft.dasheng.sys.modular.system.entity.TenantBind;
import cn.atsoft.dasheng.sys.modular.system.entity.TenantBindLog;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.mapper.TenantMapper;
import cn.atsoft.dasheng.sys.modular.system.model.params.TenantParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.TenantResult;
import cn.atsoft.dasheng.sys.modular.system.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Autowired
    private TenantBindService tenantBindService;

    @Autowired
    private UserService userService;

    @Resource
    private AuthService authService;

    @Autowired
    private RestMediaService mediaService;
    @Autowired
    private RestInitTenantService initTenantService;
    @Autowired
    private TenantBindLogService tenantBindLogService;
    @Override
    public String  add(TenantParam param){
//        isAdmin();
//        checkPhone(param.getTelephone());
//        checkName(param.getName());
        Tenant entity = getEntity(param);
        this.save(entity);
        param.setTenantId(entity.getTenantId());
        tenantBindService.save(new TenantBind(){{
            setTenantId(entity.getTenantId());
            setUserId(LoginContextHolder.getContext().getUserId());
            setStatus(99);
        }});
        String token = changeTenant(param);
        initTenantService.init(entity.getTenantId());
        tenantBindLogService.save(new TenantBindLog(){{
            setTenantId(entity.getTenantId());
            setUserId(LoginContextHolder.getContext().getUserId());
            setType("申请");
            setStatus(99);
        }});
        return token;

    }

    @Override
    public String changeTenant(TenantParam param){
        TenantBind bind = tenantBindService.lambdaQuery().eq(TenantBind::getTenantId, param.getTenantId()).eq(TenantBind::getUserId, LoginContextHolder.getContext().getUserId()).eq(TenantBind::getDisplay,1).eq(TenantBind::getStatus,99).one();
        if (ToolUtil.isEmpty(bind)){
            throw new ServiceException(500,"您未在此租户下");
        }
        if (!bind.getStatus().equals(99)){
            throw new ServiceException(500,"您在此租户下已被禁用");
        }
        User user = new User();
        user.setUserId(LoginContextHolder.getContext().getUserId());
        user.setTenantId(param.getTenantId());
        userService.updateById(user);
        return authService.login(LoginContextHolder.getContext().getUser().getAccount());
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
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("tenant_id",oldEntity.getTenantId());
        updateWrapper.set("tenant_id",null);
        userService.update(updateWrapper);

    }

    @Override
    @Transactional
    public String  update(TenantParam param){
        if (ToolUtil.isEmpty(param.getTenantId())){
            throw new ServiceException(500,"参数错误");
        }
//        isAdmin();
        Tenant oldEntity = getOldEntity(param);
        if (ToolUtil.isEmpty(oldEntity)){
            throw new ServiceException(500,"数据未找到");
        }
        Tenant newEntity = getEntity(param);
//        if (!oldEntity.getTelephone().equals(newEntity.getTelephone())){
//            checkPhone(param.getTelephone());
//        }
//        if (!oldEntity.getName().equals(newEntity.getName())){
//            checkName(param.getName());
//        }
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
        return authService.login(LoginContextHolder.getContext().getUser().getAccount());
    }

    @Override
    public TenantResult findBySpec(TenantParam param){
        return null;
    }

    @Override
    public List<TenantResult> findListBySpec(TenantParam param){
        List<TenantResult> tenantResults = this.baseMapper.customList(param);
        this.format(tenantResults);
        return tenantResults;
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
        this.format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }
    @Override
    public void format(List<TenantResult> dataList){
        //取出集合中的logo
        List<Long> logoList = dataList.stream().map(TenantResult::getLogo).collect(Collectors.toList());
        //通过logoList去restMediaService中getMediaUrlResults方法获取url
        List<MediaUrlResult> mediaUrlResults = mediaService.getMediaUrlResults(logoList);
        //把mediaUrlResults转换成Map<Long, MediaUrlResult> key为mediaId value则是对象
        Map<Long, MediaUrlResult> logoMap = mediaUrlResults.stream().collect(Collectors.toMap(MediaUrlResult::getMediaId, mediaUrlResult -> mediaUrlResult));

        //遍历dataList 将logMap匹配到dataList中
        dataList.forEach(data -> {
            if (logoMap.containsKey(data.getLogo())){
                data.setLogoResult(logoMap.get(data.getLogo()));
            }
        });
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
    //写一个根据ids 取出result的方法
    @Override
    public List<TenantResult> getTenantResultsByIds(List<Long> ids) {
        //如果ids为空 返回新集合
        if (ids == null || ids.size() == 0) {
            return new ArrayList<>();
        }
        return BeanUtil.copyToList(this.listByIds(ids), TenantResult.class);
    }

    @Override
    public void changeUser(Long tenantId, Long userId){
        Tenant tenant = this.getById(tenantId);
        if(ToolUtil.isEmpty(tenant)){
            throw new ServiceException(500,"租户不存在");
        }
        if(!tenant.getCreateUser().equals(LoginContextHolder.getContext().getUserId())){
            throw new ServiceException(500,"您没有权力操作");
        }

        User user = userService.getById(userId);
        if(ToolUtil.isEmpty(user)){
            throw new ServiceException(500,"用户不存在");
        }

        //如果用户不在此团队里不操作
        tenantBindService.lambdaQuery().eq(TenantBind::getTenantId,tenantId).eq(TenantBind::getUserId,userId).eq(TenantBind::getStatus,99).oneOpt().orElseThrow(()->new ServiceException(500,"用户未在此租户下"));
        //更新数据
        tenant.setCreateUser(userId);
        this.updateById(tenant);


    }

}
