package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.SysUser;
import cn.atsoft.dasheng.app.mapper.SysUserMapper;
import cn.atsoft.dasheng.app.model.params.SysUserParam;
import cn.atsoft.dasheng.app.model.result.SysUserResult;
import  cn.atsoft.dasheng.app.service.SysUserService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-08-06
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public void add(SysUserParam param){
        SysUser entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(SysUserParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(SysUserParam param){
        SysUser oldEntity = getOldEntity(param);
        SysUser newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public SysUserResult findBySpec(SysUserParam param){
        return null;
    }

    @Override
    public List<SysUserResult> findListBySpec(SysUserParam param){
        return null;
    }

    @Override
    public PageInfo<SysUserResult> findPageBySpec(SysUserParam param){
        Page<SysUserResult> pageContext = getPageContext();
        IPage<SysUserResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(SysUserParam param){
        return param.getUserId();
    }

    private Page<SysUserResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private SysUser getOldEntity(SysUserParam param) {
        return this.getById(getKey(param));
    }

    private SysUser getEntity(SysUserParam param) {
        SysUser entity = new SysUser();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
