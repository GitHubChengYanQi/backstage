package cn.atsoft.dasheng.portal.wxUser.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.wxUser.entity.WxuserInfo;
import cn.atsoft.dasheng.portal.wxUser.mapper.WxuserInfoMapper;
import cn.atsoft.dasheng.portal.wxUser.model.params.WxuserInfoParam;
import cn.atsoft.dasheng.portal.wxUser.model.result.WxuserInfoResult;
import cn.atsoft.dasheng.portal.wxUser.service.WxuserInfoService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
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
 * 用户 小程序 关联 服务实现类
 * </p>
 *
 * @author
 * @since 2021-08-24
 */
@Service
public class WxuserInfoServiceImpl extends ServiceImpl<WxuserInfoMapper, WxuserInfo> implements WxuserInfoService {
    @Autowired
    private UserService userService;

    @Override
    public void add(WxuserInfoParam param) {
        WxuserInfo entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(WxuserInfoParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(WxuserInfoParam param) {
        WxuserInfo oldEntity = getOldEntity(param);
        WxuserInfo newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public WxuserInfoResult findBySpec(WxuserInfoParam param) {
        return null;
    }

    @Override
    public List<WxuserInfoResult> findListBySpec(WxuserInfoParam param) {
        return null;
    }

    @Override
    public PageInfo<WxuserInfoResult> findPageBySpec(WxuserInfoParam param) {
        Page<WxuserInfoResult> pageContext = getPageContext();
        IPage<WxuserInfoResult> page = this.baseMapper.customPageList(pageContext, param);

        List<Long> ids = new ArrayList<>();
        for (WxuserInfoResult record : page.getRecords()) {
            ids.add(record.getUserId());
        }
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        if(ToolUtil.isNotEmpty(ids)){
            userQueryWrapper.in("user_id", ids);
        }
        List<User> users = userService.list(userQueryWrapper);
        if(ToolUtil.isNotEmpty(users)){
            for (WxuserInfoResult record : page.getRecords()) {
                for (User user : users) {
                    if (user.getUserId().equals(record.getUserId())) {
                        UserResult userResult = new UserResult();
                        ToolUtil.copyProperties(user, userResult);
                        record.setUserResult(userResult);
                        break;
                    }

                }
            }
        }

        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(WxuserInfoParam param) {
        return param.getUserInfoId();
    }

    private Page<WxuserInfoResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private WxuserInfo getOldEntity(WxuserInfoParam param) {
        return this.getById(getKey(param));
    }

    private WxuserInfo getEntity(WxuserInfoParam param) {
        WxuserInfo entity = new WxuserInfo();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
