package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ShipSetpBind;
import cn.atsoft.dasheng.production.mapper.ShipSetpBindMapper;
import cn.atsoft.dasheng.production.model.params.ShipSetpBindParam;
import cn.atsoft.dasheng.production.model.result.ShipSetpBindResult;
import  cn.atsoft.dasheng.production.service.ShipSetpBindService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 工序关联绑定工具与设备表 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-02-10
 */
@Service
public class ShipSetpBindServiceImpl extends ServiceImpl<ShipSetpBindMapper, ShipSetpBind> implements ShipSetpBindService {

    @Override
    public void add(ShipSetpBindParam param){
        ShipSetpBind entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ShipSetpBindParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ShipSetpBindParam param){
        ShipSetpBind oldEntity = getOldEntity(param);
        ShipSetpBind newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ShipSetpBindResult findBySpec(ShipSetpBindParam param){
        return null;
    }

    @Override
    public List<ShipSetpBindResult> findListBySpec(ShipSetpBindParam param){
        return null;
    }

    @Override
    public PageInfo<ShipSetpBindResult> findPageBySpec(ShipSetpBindParam param){
        Page<ShipSetpBindResult> pageContext = getPageContext();
        IPage<ShipSetpBindResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ShipSetpBindParam param){
        return param.getShipSetpBindId();
    }

    private Page<ShipSetpBindResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ShipSetpBind getOldEntity(ShipSetpBindParam param) {
        return this.getById(getKey(param));
    }

    private ShipSetpBind getEntity(ShipSetpBindParam param) {
        ShipSetpBind entity = new ShipSetpBind();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
