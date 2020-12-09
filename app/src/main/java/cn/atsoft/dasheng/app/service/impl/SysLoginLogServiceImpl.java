package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.SysLoginLog;
import cn.atsoft.dasheng.app.mapper.SysLoginLogMapper;
import cn.atsoft.dasheng.app.model.params.SysLoginLogParam;
import cn.atsoft.dasheng.app.model.result.SysLoginLogResult;
import  cn.atsoft.dasheng.app.service.SysLoginLogService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 登录记录 服务实现类
 * </p>
 *
 * @author sing
 * @since 2020-12-09
 */
@Service
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements SysLoginLogService {

    @Override
    public void add(SysLoginLogParam param){
        SysLoginLog entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(SysLoginLogParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(SysLoginLogParam param){
        SysLoginLog oldEntity = getOldEntity(param);
        SysLoginLog newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public SysLoginLogResult findBySpec(SysLoginLogParam param){
        return null;
    }

    @Override
    public List<SysLoginLogResult> findListBySpec(SysLoginLogParam param){
        return null;
    }

    @Override
    public PageInfo findPageBySpec(SysLoginLogParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(SysLoginLogParam param){
        return param.getLoginLogId();
    }

    private Page getPageContext() {
        return PageFactory.defaultPage();
    }

    private SysLoginLog getOldEntity(SysLoginLogParam param) {
        return this.getById(getKey(param));
    }

    private SysLoginLog getEntity(SysLoginLogParam param) {
        SysLoginLog entity = new SysLoginLog();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
