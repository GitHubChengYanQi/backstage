package cn.atsoft.dasheng.sys.modular.system.service.impl;


import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.modular.system.entity.*;
import cn.atsoft.dasheng.sys.modular.system.mapper.TenantBindLogMapper;
import cn.atsoft.dasheng.sys.modular.system.model.params.TenantBindLogParam;
import cn.atsoft.dasheng.sys.modular.system.model.params.TenantBindParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.*;
import cn.atsoft.dasheng.sys.modular.system.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 邀请记录  申请记录 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-05-09
 */
@Service
public class TenantBindLogServiceImpl extends ServiceImpl<TenantBindLogMapper, TenantBindLog> implements TenantBindLogService {

    @Autowired
    private TenantBindService tenantBindService;
    @Autowired
    private TenantService tenantService;
    @Autowired
    private UserService userService;
    @Autowired
    private DeptBindService deptBindService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private TenantInviteLogService tenantInviteLogService;

    @Override
    @Transactional
    public Long add(TenantBindLogParam param) {
        TenantBindLog entity = getEntity(param);
        if (ToolUtil.isEmpty(param.getTenantInviteLogId())) {
            throw new ServiceException(500, "邀请记录id不能为空");
        }
        Long tenantInviteLogId = param.getTenantInviteLogId();
        TenantInviteLog inviteLog = tenantInviteLogService.getById(tenantInviteLogId);
        entity.setStatus(1);
        entity.setType("申请");
        entity.setUserId(LoginContextHolder.getContext().getUserId());

        if (ToolUtil.isNotEmpty(inviteLog.getDeptId())) {
            entity.setInviteDeptId(inviteLog.getDeptId());
        }
        entity.setInviterUser(inviteLog.getInviterUser());
        entity.setTenantId(inviteLog.getTenantId());
        Integer count = tenantBindService.lambdaQuery().eq(TenantBind::getUserId, LoginContextHolder.getContext().getUserId()).eq(TenantBind::getTenantId, inviteLog.getTenantId()).eq(TenantBind::getStatus, 99).count();
        if (count > 0) {
            throw new ServiceException(500, "您已经在企业中了，不能重复申请");
        }
        Tenant tenant = tenantService.getById(inviteLog.getTenantId());
        if (tenant.getCreateUser().equals(LoginContextHolder.getContext().getUserId())) {
            throw new ServiceException(500, "您已经是企业管理员了，不能重复申请");
        }

        this.save(entity);
        return entity.getTenantBindLogId();

    }

    /**
     * 添加记录
     *
     * @param userId
     * @param tenantId
     * @param deptId
     * @param type
     */
    @Override
    public Long addLog(Long userId, Long tenantId, Long deptId, String type) {
        TenantBindLog entity = new TenantBindLog();
        entity.setUserId(userId);
        entity.setTenantId(tenantId);
//        entity.setDeptId(deptId);
        entity.setType(type);
        entity.setStatus(0);
        this.save(entity);
        return entity.getTenantBindLogId();
    }

    /**
     * 申请加入 更新状态
     *
     * @param tenantBindLogId
     * @param status
     */
    @Override
    @Transactional
    public void updateStatus(Long tenantBindLogId, Integer status) {
        Integer[] statusList = new Integer[]{50, 99};
        if (Arrays.stream(statusList).noneMatch(i -> i.equals(status))) {
            throw new RuntimeException("状态参数不正确");
        }

        TenantBindLog log = this.getById(tenantBindLogId);
        log.setStatus(status);
        log.setAuditUser(LoginContextHolder.getContext().getUserId());
        this.updateById(log);
        if (status.equals(99)) {
            tenantBindService.add(new TenantBindParam() {{
                setTenantId(log.getTenantId());
                setUserId(log.getUserId());
                setStatus(99);
                setDeptId(ToolUtil.isEmpty(log.getInviteDeptId())?0L:log.getInviteDeptId());
            }});
        }
    }


    @Override
    public void delete(TenantBindLogParam param) {
        //this.removeById(getKey(param));
        TenantBindLog entity = this.getOldEntity(param);
        entity.setDisplay(0);
        this.updateById(entity);
    }

    @Override
    public void update(TenantBindLogParam param) {
        TenantBindLog oldEntity = getOldEntity(param);
        TenantBindLog newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public TenantBindLogResult findBySpec(TenantBindLogParam param) {
        return null;
    }

    @Override
    public List<TenantBindLogResult> findListBySpec(TenantBindLogParam param) {
        return null;
    }

    @Override
    public PageInfo<TenantBindLogResult> findPageBySpec(TenantBindLogParam param) {
        Page<TenantBindLogResult> pageContext = getPageContext();
        IPage<TenantBindLogResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    @Override
    public PageInfo<TenantBindLogResult> findPageBySpec(TenantBindLogParam param, DataScope dataScope) {
        Page<TenantBindLogResult> pageContext = getPageContext();
        IPage<TenantBindLogResult> page = this.baseMapper.customPageList(pageContext, param, dataScope);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    public void format(List<TenantBindLogResult> dataList) {
        List<Long> userIds = dataList.stream().map(TenantBindLogResult::getUserId).distinct().collect(Collectors.toList());
        List<Long> tenantIds = dataList.stream().map(TenantBindLogResult::getTenantId).distinct().collect(Collectors.toList());
        userIds.addAll(dataList.stream().map(TenantBindLogResult::getUpdateUser).distinct().collect(Collectors.toList()));
        userIds.addAll(dataList.stream().map(TenantBindLogResult::getInviterUser).collect(Collectors.toList()));
        userIds = userIds.stream().distinct().collect(Collectors.toList());
        //用mybatis-plus的listByIds查询租户和用户
        List<TenantResult> tenantResult = tenantService.getTenantResultsByIds(tenantIds);
        List<UserResult> userResult = userService.getUserResultsByIds(userIds);
        List<DeptBindResult> deptList = new ArrayList<>();
        //dataList中取出deptId集合
        List<Long> deptIds = dataList.stream().map(TenantBindLogResult::getInviteDeptId).distinct().collect(Collectors.toList());
        List<Dept> depts = deptIds.size() == 0 ? new ArrayList<>() : deptService.listByIds(deptIds);
        //转换成map
        Map<Long, Dept> deptMap = depts.stream().collect(Collectors.toMap(Dept::getDeptId, Function.identity()));

        if (userIds.size() > 0 && tenantIds.size() > 0) {
            deptList = BeanUtil.copyToList(deptBindService.lambdaQuery().in(DeptBind::getUserId, userIds).in(DeptBind::getTenantId, tenantIds).list(), DeptBindResult.class);
            deptBindService.format(deptList);
        }
        //将部门放入map中，一个userId会对应多个部门，所以用list
        Map<Long, List<DeptBindResult>> deptBindMap = deptList.stream().collect(Collectors.groupingBy(DeptBindResult::getUserId));


        //将用户和租户放入map中，方便后续的关联
        Map<Long, UserResult> userMap = userResult.stream().collect(Collectors.toMap(UserResult::getUserId, Function.identity()));
        Map<Long, TenantResult> tenantMap = tenantResult.stream().collect(Collectors.toMap(TenantResult::getTenantId, Function.identity()));

        //循环dataList 将userId对应的userName和tenantId对应的tenantName放入
        dataList.forEach(item -> {
            //将userId对应的result放入
            item.setUserResult(userMap.get(item.getUserId()));
            //将tenantId对应的result放入
            item.setTenantResult(tenantMap.get(item.getTenantId()));
            if (ToolUtil.isNotEmpty(tenantMap.get(item.getTenantId())) && tenantMap.get(item.getTenantId()).getCreateUser().equals(item.getUserId())) {
                item.setIsAdmin(1);
            }
            if (ToolUtil.isNotEmpty(item.getUpdateUser())) {
                item.setUpdateUserResult(userMap.get(item.getUpdateUser()));
            }
            //将部门放入
            if (ToolUtil.isNotEmpty(deptMap.get(item.getUserId()))) {
                item.setDeptList(deptBindMap.get(item.getUserId()).stream().map(DeptBindResult::getDept).collect(Collectors.toList()));
            }
            if (ToolUtil.isNotEmpty(item.getInviterUser())) {
                item.setInviterUserResult(userMap.get(item.getInviterUser()));
            }

            if (ToolUtil.isNotEmpty(item.getInviteDeptId())) {
                item.setDept(deptMap.get(item.getInviteDeptId()));
            }

        });
    }

    private Serializable getKey(TenantBindLogParam param) {
        return param.getTenantBindLogId();
    }

    private Page<TenantBindLogResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private TenantBindLog getOldEntity(TenantBindLogParam param) {
        return this.getById(getKey(param));
    }

    private TenantBindLog getEntity(TenantBindLogParam param) {
        TenantBindLog entity = new TenantBindLog();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
