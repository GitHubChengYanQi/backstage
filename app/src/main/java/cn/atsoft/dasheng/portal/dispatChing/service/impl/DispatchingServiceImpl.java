package cn.atsoft.dasheng.portal.dispatChing.service.impl;


import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.portal.dispatChing.entity.Dispatching;
import cn.atsoft.dasheng.portal.dispatChing.mapper.DispatchingMapper;
import cn.atsoft.dasheng.portal.dispatChing.model.params.DispatchingParam;
import cn.atsoft.dasheng.portal.dispatChing.model.result.DispatchingResult;
import cn.atsoft.dasheng.portal.dispatChing.service.DispatchingService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.portal.repair.model.params.RepairParam;
import cn.atsoft.dasheng.portal.repair.service.RepairSendTemplate;
import cn.atsoft.dasheng.portal.repair.entity.Repair;
import cn.atsoft.dasheng.portal.repair.service.RepairService;
import cn.atsoft.dasheng.binding.wxUser.service.WxuserInfoService;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 派工表 服务实现类
 * </p>
 *
 * @author
 * @since 2021-08-23
 */
@Service
public class DispatchingServiceImpl extends ServiceImpl<DispatchingMapper, Dispatching> implements DispatchingService {

    @Autowired
    private UserService userService;

    @Autowired
    private RepairService repairService;
    @Autowired
    private RepairSendTemplate repairSendTemplate;

    @Autowired
    private WxuserInfoService wxuserInfoService;


    @Override
    public void add(DispatchingParam param) {
        Dispatching entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(DispatchingParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(DispatchingParam param) {
        Dispatching oldEntity = getOldEntity(param);
        Dispatching newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);

    }

    @Override
    public DispatchingResult findBySpec(DispatchingParam param) {
        return null;
    }

    @Override
    public List<DispatchingResult> findListBySpec(DispatchingParam param) {
        return null;
    }

    @Override
    public PageInfo<DispatchingResult> findPageBySpec(DispatchingParam param) {
        Page<DispatchingResult> pageContext = getPageContext();
        IPage<DispatchingResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    /**
     * 发送订阅消息
     *
     * @param param
     */
    @Override
    @BussinessLog
    public void addwx(DispatchingParam param) {
        Dispatching entity = getEntity(param);
        LoginUser user = LoginContextHolder.getContext().getUser();
        //判断权限
        Boolean aBoolean = wxuserInfoService.sendPermissions(1L, user.getId());
        if (aBoolean == true) {
            this.save(entity);
            QueryWrapper<Repair> repairQueryWrapper = new QueryWrapper<>();
            repairQueryWrapper.in("repair_id", param.getRepairId());
            Repair repair = repairService.getOne(repairQueryWrapper);
            RepairParam repairParam = new RepairParam();
            ToolUtil.copyProperties(repair, repairParam);
            repairParam.setProgress(1L);
            repairParam.setCreateTime(entity.getCreateTime());
            repairSendTemplate.setRepairParam(repairParam);
            try {
                repairSendTemplate.send();
            } catch (WxErrorException e) {
                e.printStackTrace();
            }
        } else {
            throw new ServiceException(500, "当前用户没有权限");
        }


    }

    @Override
    public List<DispatchingResult> getAll(DispatchingParam param) {
        List<DispatchingResult> dispatchingResults = this.baseMapper.customList(param);
        List<Long> ids = new ArrayList<>();
        for (DispatchingResult dispatchingResult : dispatchingResults) {
            ids.add(dispatchingResult.getName());
        }
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.in("user_id", ids);
        List<User> users = userService.list(userQueryWrapper);

        for (DispatchingResult dispatchingResult : dispatchingResults) {
            for (User user : users) {
                if (user.getUserId().equals(dispatchingResult.getName())) {
                    UserResult userResult = new UserResult();
                    ToolUtil.copyProperties(user, userResult);
                    dispatchingResult.setUserResult(userResult);
                    break;
                }
            }
        }

        return dispatchingResults;
    }


    private Serializable getKey(DispatchingParam param) {
        return param.getDispatchingId();
    }

    private Page<DispatchingResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Dispatching getOldEntity(DispatchingParam param) {
        return this.getById(getKey(param));
    }

    private Dispatching getEntity(DispatchingParam param) {
        Dispatching entity = new Dispatching();
        ToolUtil.copyProperties(param, entity);
        return entity;

    }

}
