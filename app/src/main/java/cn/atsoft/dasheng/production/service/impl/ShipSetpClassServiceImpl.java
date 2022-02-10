package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ShipSetpClass;
import cn.atsoft.dasheng.production.mapper.ShipSetpClassMapper;
import cn.atsoft.dasheng.production.model.params.ShipSetpClassParam;
import cn.atsoft.dasheng.production.model.result.ShipSetpClassResult;
import cn.atsoft.dasheng.production.service.ShipSetpClassService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 工序分类表 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-02-10
 */
@Service
public class ShipSetpClassServiceImpl extends ServiceImpl<ShipSetpClassMapper, ShipSetpClass> implements ShipSetpClassService {

    @Override
    public void add(ShipSetpClassParam param){
        ShipSetpClass entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ShipSetpClassParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ShipSetpClassParam param){
        ShipSetpClass oldEntity = getOldEntity(param);
        ShipSetpClass newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ShipSetpClassResult findBySpec(ShipSetpClassParam param){
        return null;
    }

    @Override
    public List<ShipSetpClassResult> findListBySpec(ShipSetpClassParam param){
        return null;
    }

    @Override
    public PageInfo<ShipSetpClassResult> findPageBySpec(ShipSetpClassParam param){
        Page<ShipSetpClassResult> pageContext = getPageContext();
        IPage<ShipSetpClassResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ShipSetpClassParam param){
        return param.getShipSetpClassId();
    }

    private Page<ShipSetpClassResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ShipSetpClass getOldEntity(ShipSetpClassParam param) {
        return this.getById(getKey(param));
    }

    private ShipSetpClass getEntity(ShipSetpClassParam param) {
        ShipSetpClass entity = new ShipSetpClass();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
