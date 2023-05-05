package cn.atsoft.dasheng.sys.modular.system.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.sys.modular.system.entity.MenuConfig;
import cn.atsoft.dasheng.sys.modular.system.mapper.MenuConfigMapper;
import cn.atsoft.dasheng.sys.modular.system.model.params.MenuConfigParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.MenuConfigResult;
import cn.atsoft.dasheng.sys.modular.system.service.MenuConfigService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 菜单显示配置表 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-04-28
 */
@Service
public class MenuConfigServiceImpl extends ServiceImpl<MenuConfigMapper, MenuConfig> implements MenuConfigService {

    @Override
    public void add(MenuConfigParam param){
        MenuConfig entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(MenuConfigParam param){
        //this.removeById(getKey(param));
        MenuConfig entity = this.getOldEntity(param);
        entity.setDisplay(0);
        this.updateById(entity);
    }

    @Override
    public void update(MenuConfigParam param){
        MenuConfig oldEntity = getOldEntity(param);
        MenuConfig newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public MenuConfigResult findBySpec(MenuConfigParam param){
        return null;
    }

    @Override
    public List<MenuConfigResult> findListBySpec(MenuConfigParam param){
        return null;
    }

    @Override
    public PageInfo<MenuConfigResult> findPageBySpec(MenuConfigParam param){
        Page<MenuConfigResult> pageContext = getPageContext();
        IPage<MenuConfigResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    @Override
    public PageInfo<MenuConfigResult> findPageBySpec(MenuConfigParam param, DataScope dataScope){
        Page<MenuConfigResult> pageContext = getPageContext();
        IPage<MenuConfigResult> page = this.baseMapper.customPageList(pageContext, param,dataScope);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(MenuConfigParam param){
        return param.getConfigId();
    }

    private Page<MenuConfigResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private MenuConfig getOldEntity(MenuConfigParam param) {
        return this.getById(getKey(param));
    }

    private MenuConfig getEntity(MenuConfigParam param) {
        MenuConfig entity = new MenuConfig();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
