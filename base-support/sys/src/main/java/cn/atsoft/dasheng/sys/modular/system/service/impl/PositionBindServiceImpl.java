package cn.atsoft.dasheng.sys.modular.system.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.sys.modular.system.entity.PositionBind;
import cn.atsoft.dasheng.sys.modular.system.mapper.PositionBindMapper;
import cn.atsoft.dasheng.sys.modular.system.model.params.PositionBindParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.PositionBindResult;
import  cn.atsoft.dasheng.sys.modular.system.service.PositionBindService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 租户用户位置绑定表 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-05-09
 */
@Service
public class PositionBindServiceImpl extends ServiceImpl<PositionBindMapper, PositionBind> implements PositionBindService {

    @Override
    public void add(PositionBindParam param){
        PositionBind entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(PositionBindParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(PositionBindParam param){
        PositionBind oldEntity = getOldEntity(param);
        PositionBind newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public PositionBindResult findBySpec(PositionBindParam param){
        return null;
    }

    @Override
    public List<PositionBindResult> findListBySpec(PositionBindParam param){
        return null;
    }

    @Override
    public PageInfo<PositionBindResult> findPageBySpec(PositionBindParam param){
        Page<PositionBindResult> pageContext = getPageContext();
        IPage<PositionBindResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    @Override
    public PageInfo<PositionBindResult> findPageBySpec(PositionBindParam param, DataScope dataScope){
        Page<PositionBindResult> pageContext = getPageContext();
        IPage<PositionBindResult> page = this.baseMapper.customPageList(pageContext, param,dataScope);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(PositionBindParam param){
        return param.getPositionBindId();
    }

    private Page<PositionBindResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private PositionBind getOldEntity(PositionBindParam param) {
        return this.getById(getKey(param));
    }

    private PositionBind getEntity(PositionBindParam param) {
        PositionBind entity = new PositionBind();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
