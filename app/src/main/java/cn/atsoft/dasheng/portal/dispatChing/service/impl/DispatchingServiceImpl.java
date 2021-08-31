package cn.atsoft.dasheng.portal.dispatChing.service.impl;


import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.service.CustomerService;
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
import cn.atsoft.dasheng.portal.remind.entity.Remind;
import cn.atsoft.dasheng.portal.remind.model.params.RemindParam;
import cn.atsoft.dasheng.portal.remind.service.RemindService;
import cn.atsoft.dasheng.userInfo.controller.WxTemplate;
import cn.atsoft.dasheng.portal.repair.entity.Repair;
import cn.atsoft.dasheng.portal.repair.service.RepairService;
import cn.atsoft.dasheng.portal.wxUser.entity.WxuserInfo;
import cn.atsoft.dasheng.portal.wxUser.service.WxuserInfoService;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.atsoft.dasheng.uc.entity.UcOpenUserInfo;
import cn.atsoft.dasheng.uc.service.UcOpenUserInfoService;
import cn.atsoft.dasheng.userInfo.service.UserInfoService;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.system.UserInfo;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
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
    private WxTemplate wxTemplate;
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
        this.save(entity);
        String reateTime = String.valueOf(param.getTime());
        DateTime parse = DateUtil.parse(reateTime);
        String time = String.valueOf(parse);
        QueryWrapper<WxuserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.in("user_id", entity.getCreateUser());
        List<WxuserInfo> list = wxuserInfoService.list(userInfoQueryWrapper);
        if (list.size() > 0) {
            wxTemplate.template(param.getType(), entity.getCreateUser(), time, param.getRepair().getComment(), param.getRepair().getServiceType());
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
