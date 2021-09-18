package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.BusinessTrack;
import cn.atsoft.dasheng.app.mapper.BusinessTrackMapper;
import cn.atsoft.dasheng.app.model.params.BusinessTrackParam;
import cn.atsoft.dasheng.app.model.result.BusinessTrackResult;
import cn.atsoft.dasheng.app.service.BusinessTrackService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
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
 * 跟进内容 服务实现类
 * </p>
 *
 * @author cheng
 * @since 2021-09-17
 */
@Service
public class BusinessTrackServiceImpl extends ServiceImpl<BusinessTrackMapper, BusinessTrack> implements BusinessTrackService {
    @Autowired
    private UserService userService;

    @Override
    public void add(BusinessTrackParam param) {
        BusinessTrack entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(BusinessTrackParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(BusinessTrackParam param) {
        BusinessTrack oldEntity = getOldEntity(param);
        BusinessTrack newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public BusinessTrackResult findBySpec(BusinessTrackParam param) {
        return null;
    }

    @Override
    public List<BusinessTrackResult> findListBySpec(BusinessTrackParam param) {
        return null;
    }

    @Override
    public PageInfo<BusinessTrackResult> findPageBySpec(BusinessTrackParam param) {
        Page<BusinessTrackResult> pageContext = getPageContext();
        IPage<BusinessTrackResult> page = this.baseMapper.customPageList(pageContext, param);
        List<Long> ids = new ArrayList<>();
        for (BusinessTrackResult record : page.getRecords()) {
            ids.add(record.getUserId());
        }

        List<User> users = userService.list();
        for (BusinessTrackResult record : page.getRecords()) {
            for(User user: users){
                if (ToolUtil.isNotEmpty(record.getUserId())){
                    if (record.getUserId().equals(user.getUserId())) {
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

    private Serializable getKey(BusinessTrackParam param) {
        return param.getTrackId();
    }

    private Page<BusinessTrackResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private BusinessTrack getOldEntity(BusinessTrackParam param) {
        return this.getById(getKey(param));
    }

    private BusinessTrack getEntity(BusinessTrackParam param) {
        BusinessTrack entity = new BusinessTrack();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
