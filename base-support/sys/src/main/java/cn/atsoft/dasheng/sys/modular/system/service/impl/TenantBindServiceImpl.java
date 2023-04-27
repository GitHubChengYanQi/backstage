package cn.atsoft.dasheng.sys.modular.system.service.impl;


import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.modular.system.entity.Tenant;
import cn.atsoft.dasheng.sys.modular.system.entity.TenantBind;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.mapper.TenantBindMapper;
import cn.atsoft.dasheng.sys.modular.system.model.params.TenantBindParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.TenantBindResult;
import cn.atsoft.dasheng.sys.modular.system.model.result.TenantResult;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import  cn.atsoft.dasheng.sys.modular.system.service.TenantBindService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Autowired
    private UserService userService;
    @Autowired
    private TenantServiceImpl tenantService;

    @Override
    public void add(TenantBindParam param){
        this.checkParam(param);
        TenantBind entity = this.lambdaQuery().eq(TenantBind::getTenantId, param.getTenantId()).eq(TenantBind::getUserId, param.getUserId()).one();
        if(entity!=null){
            switch (entity.getStatus()){
                case 99:
                    throw new ServiceException(500,"该用户已在企业中");
                case 0:
                    throw new ServiceException(500,"已提交申请，请等待管理员审核");
            }
        }
            entity = new TenantBind();
            entity.setTenantId(param.getTenantId());
            entity.setUserId(param.getUserId());
            this.save(entity);
    }
    @Override
    public Long getOrSave(TenantBindParam param){
        this.checkParam(param);
        TenantBind entity = this.lambdaQuery().eq(TenantBind::getTenantId, param.getTenantId()).eq(TenantBind::getUserId, param.getUserId()).one();
        if(entity!=null){
            return entity.getTenantBindId();
        }else{
            entity = new TenantBind();
            entity.setTenantId(param.getTenantId());
            entity.setUserId(param.getUserId());
            this.save(entity);
            return entity.getTenantBindId();
        }
    }
    //检查参数
    private void checkParam(TenantBindParam param) {
        if(ToolUtil.isEmpty(param.getUserId())){
            throw new ServiceException(500,"请选择人员");
        }
        if(ToolUtil.isEmpty(param.getTenantId())){
            throw new ServiceException(500,"请选择租户");
        }
    }
    @Override
    public void delete(TenantBindParam param){
        if (ToolUtil.isEmpty(param.getTenantBindId())) {
            throw new ServiceException(500,"租户用户绑定表id不能为空");
        }
        TenantBind tenantBind = getById(param);
        if (ToolUtil.isEmpty(tenantBind)) {
            throw new ServiceException(500,"数据不存在");
        }
        if(tenantBind.getUserId().equals(LoginContextHolder.getContext().getUserId())){
            throw new ServiceException(500,"您不能删除自己的绑定");
        }
        if (LoginContextHolder.getContext().getTenantId().equals(tenantBind.getTenantId())){
            Tenant tenant = tenantService.getById(tenantBind.getTenantId());
            if (LoginContextHolder.getContext().getUserId().equals(tenant.getCreateUser())){
                tenantBind.setDisplay(0);
                this.updateById(tenantBind);
                userService.update(new UpdateWrapper<User>(){{
                    eq("tenant_id",tenantBind.getTenantId());
                    eq("user_id",tenantBind.getUserId());
                    set("tenant_id",null);
                }});
            }else {
                throw new ServiceException(500,"您没有权限删除该数据");
            }
        }else {
            throw new ServiceException(500,"请切换到此租户内再进行操作");
        }
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
        param.setStatus(99);
        List<TenantBindResult> listBySpec = this.baseMapper.customList(param);
        format(listBySpec);
        return listBySpec;
    }

    @Override
    public PageInfo<TenantBindResult> findPageBySpec(TenantBindParam param){
        Page<TenantBindResult> pageContext = getPageContext();
        IPage<TenantBindResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }
    /**
     * 格式化数据
     * @param dataList
     */

    public void format(List<TenantBindResult> dataList){
        //取出参数集合中的userId和tenantId
        List<Long> userIds = dataList.stream().map(TenantBindResult::getUserId).distinct().collect(Collectors.toList());
        List<Long> tenantIds = dataList.stream().map(TenantBindResult::getTenantId).distinct().collect(Collectors.toList());

        //用mybatis-plus的listByIds查询租户和用户
        List<TenantResult> tenantResult = tenantService.getTenantResultsByIds(tenantIds);
        List<UserResult> userResult = userService.getUserResultsByIds(userIds);

        //将用户和租户放入map中，方便后续的关联
        Map<Long, UserResult> userMap = userResult.stream().collect(Collectors.toMap(UserResult::getUserId, Function.identity()));
        Map<Long, TenantResult> tenantMap = tenantResult.stream().collect(Collectors.toMap(TenantResult::getTenantId, Function.identity()));

        //循环dataList 将userId对应的userName和tenantId对应的tenantName放入
        dataList.forEach(item -> {
            //将userId对应的result放入
            item.setUserResult(userMap.get(item.getUserId()));
            //将tenantId对应的result放入
            item.setTenantResult(tenantMap.get(item.getTenantId()));
            if(ToolUtil.isNotEmpty(tenantMap.get(item.getTenantId())) && tenantMap.get(item.getTenantId()).getCreateUser().equals(item.getUserId())){
                item.setIsAdmin(1);
            }
        });
    }

    @Override
    public PageInfo<TenantBindResult> findPageBySpec(TenantBindParam param, DataScope dataScope){
        Page<TenantBindResult> pageContext = getPageContext();
        IPage<TenantBindResult> page = this.baseMapper.customPageList(pageContext, param,dataScope);
        format(page.getRecords());
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
