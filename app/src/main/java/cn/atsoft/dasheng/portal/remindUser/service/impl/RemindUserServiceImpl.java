package cn.atsoft.dasheng.portal.remindUser.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.remindUser.entity.RemindUser;
import cn.atsoft.dasheng.portal.remindUser.mapper.RemindUserMapper;
import cn.atsoft.dasheng.portal.remindUser.model.params.RemindUserParam;
import cn.atsoft.dasheng.portal.remindUser.model.result.RemindUserResult;
import  cn.atsoft.dasheng.portal.remindUser.service.RemindUserService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author c
 * @since 2021-08-27
 */
@Service
public class RemindUserServiceImpl extends ServiceImpl<RemindUserMapper, RemindUser> implements RemindUserService {

    @Override
    public void add(RemindUserParam param){
        RemindUser entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(RemindUserParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(RemindUserParam param){
        RemindUser oldEntity = getOldEntity(param);
        RemindUser newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public RemindUserResult findBySpec(RemindUserParam param){
        return null;
    }

    @Override
    public List<RemindUserResult> findListBySpec(RemindUserParam param){
        return null;
    }

    @Override
    public PageInfo<RemindUserResult> findPageBySpec(RemindUserParam param){
        Page<RemindUserResult> pageContext = getPageContext();
        IPage<RemindUserResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(RemindUserParam param){
        return param.getRemindUserId();
    }

    private Page<RemindUserResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private RemindUser getOldEntity(RemindUserParam param) {
        return this.getById(getKey(param));
    }

    private RemindUser getEntity(RemindUserParam param) {
        RemindUser entity = new RemindUser();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
