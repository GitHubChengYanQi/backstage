package cn.atsoft.dasheng.portal.remind.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.remind.entity.Remind;
import cn.atsoft.dasheng.portal.remind.mapper.RemindMapper;
import cn.atsoft.dasheng.portal.remind.model.params.RemindParam;
import cn.atsoft.dasheng.portal.remind.model.result.RemindResult;
import cn.atsoft.dasheng.portal.remind.service.RemindService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.portal.remindUser.entity.RemindUser;
import cn.atsoft.dasheng.portal.remindUser.service.RemindUserService;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.atsoft.dasheng.userInfo.controller.WxTemplate;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 提醒表 服务实现类
 * </p>
 *
 * @author cheng
 * @since 2021-08-26
 */
@Service
public class RemindServiceImpl extends ServiceImpl<RemindMapper, Remind> implements RemindService {

    @Autowired
    private UserService userService;

    @Autowired
    private RemindUserService remindUserService;
    @Autowired
    private WxTemplate wxTemplate;

    @Override
    public void add(RemindParam param) {


        Long type = param.getType();
        QueryWrapper<Remind> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("type", type);
        List<Remind> list = this.list(queryWrapper);

        if (list.size() > 0) {
            QueryWrapper<RemindUser> remindUserQueryWrapper = new QueryWrapper<>();
            remindUserQueryWrapper.in("remind_id", list.get(0).getRemindId());
            List<RemindUser> remindUsers1 = remindUserService.list(remindUserQueryWrapper);
            List<Long> ids = new ArrayList<>();
            for (RemindUser remindUser : remindUsers1) {
                ids.add(remindUser.getRemindUserId());
            }
            remindUserService.removeByIds(ids);

            List<Long> users = param.getUsers();
            List<RemindUser> remindUsers = new ArrayList<>();
            for (Long user : users) {
                RemindUser remind = new RemindUser();
                remind.setRemindId(list.get(0).getRemindId());
                remind.setUserId(user);
                remindUsers.add(remind);
            }
            remindUserService.saveBatch(remindUsers);
            return;
        }


        Remind entity = getEntity(param);
        this.save(entity);
        List<Long> users = param.getUsers();
        List<RemindUser> lists = new ArrayList<>();
        for (Long user : users) {
            RemindUser remind = new RemindUser();
            remind.setRemindId(entity.getRemindId());
            remind.setUserId(user);
            lists.add(remind);
        }
        remindUserService.saveBatch(lists);
    }

    @Override
    public void delete(RemindParam param) {
        this.removeById(getKey(param));
        QueryWrapper<RemindUser> remindUserQueryWrapper = new QueryWrapper<>();
        remindUserQueryWrapper.in("remind_id", param.getRemindId());
        List<RemindUser> remindUsers1 = remindUserService.list(remindUserQueryWrapper);
        List<Long> ids = new ArrayList<>();
        for (RemindUser remindUser : remindUsers1) {
            ids.add(remindUser.getRemindUserId());
        }
        remindUserService.removeByIds(ids);
    }

    @Override
    public void update(RemindParam param) {
        param.getTemplate();


        param.setTemplateType(JSON.toJSONString(param.getTemplate()));


        QueryWrapper<RemindUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("remind_id", param.getRemindId());
        List<RemindUser> list = remindUserService.list(queryWrapper);

        List<Long> ids = new ArrayList<>();

        for (RemindUser remindUser : list) {
            ids.add(remindUser.getRemindUserId());
        }
        remindUserService.removeByIds(ids);

        List<Long> users = param.getUsers();
        List<RemindUser> remindUsers = new ArrayList<>();
        for (Long user : users) {
            RemindUser remind = new RemindUser();
            remind.setRemindId(param.getRemindId());
            remind.setUserId(user);
            remindUsers.add(remind);
        }
        remindUserService.saveBatch(remindUsers);

        Remind oldEntity = getOldEntity(param);
        Remind newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public RemindResult findBySpec(RemindParam param) {
        return null;
    }

    @Override
    public List<RemindResult> findListBySpec(RemindParam param) {
        return null;
    }

    @Override
    public PageInfo<RemindResult> findPageBySpec(RemindParam param) {
        Page<RemindResult> pageContext = getPageContext();
        IPage<RemindResult> page = this.baseMapper.customPageList(pageContext, param);


        for (RemindResult record : page.getRecords()) {
            QueryWrapper<RemindUser> remindQueryWrapper = new QueryWrapper<>();
            remindQueryWrapper.in("remind_id", record.getRemindId());
            List<RemindUser> list = remindUserService.list(remindQueryWrapper);
            List<Long> longs = new ArrayList<>();
            for (RemindUser remindUser : list) {
                longs.add(remindUser.getUserId());
            }
            QueryWrapper<User> differenceQueryWrapper = new QueryWrapper<>();
            differenceQueryWrapper.in("user_id", longs);
            List<User> lists = longs.size() == 0 ? new ArrayList<>() : userService.list(differenceQueryWrapper);
            record.setUsers(lists);
        }


        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(RemindParam param) {
        return param.getRemindId();
    }

    private Page<RemindResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Remind getOldEntity(RemindParam param) {
        return this.getById(getKey(param));
    }

    private Remind getEntity(RemindParam param) {
        Remind entity = new Remind();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }


}
