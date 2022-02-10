package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ShipSetp;
import cn.atsoft.dasheng.production.mapper.ShipSetpMapper;
import cn.atsoft.dasheng.production.model.params.ShipSetpParam;
import cn.atsoft.dasheng.production.model.result.ShipSetpResult;
import cn.atsoft.dasheng.production.service.ShipSetpService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 工序表 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-02-10
 */
@Service
public class ShipSetpServiceImpl extends ServiceImpl<ShipSetpMapper, ShipSetp> implements ShipSetpService {

    @Override
    public void add(ShipSetpParam param){
        ShipSetp entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ShipSetpParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ShipSetpParam param){
        ShipSetp oldEntity = getOldEntity(param);
        ShipSetp newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ShipSetpResult findBySpec(ShipSetpParam param){
        return null;
    }

    @Override
    public List<ShipSetpResult> findListBySpec(ShipSetpParam param){
        return null;
    }

    @Override
    public PageInfo<ShipSetpResult> findPageBySpec(ShipSetpParam param){
        Page<ShipSetpResult> pageContext = getPageContext();
        IPage<ShipSetpResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ShipSetpParam param){
        return param.getShipSetpId();
    }

    private Page<ShipSetpResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ShipSetp getOldEntity(ShipSetpParam param) {
        return this.getById(getKey(param));
    }

    private ShipSetp getEntity(ShipSetpParam param) {
        ShipSetp entity = new ShipSetp();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
