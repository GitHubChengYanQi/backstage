package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.StorehousePositionsBind;
import cn.atsoft.dasheng.erp.mapper.StorehousePositionsBindMapper;
import cn.atsoft.dasheng.erp.model.params.StorehousePositionsBindParam;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsBindResult;
import  cn.atsoft.dasheng.erp.service.StorehousePositionsBindService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 库位绑定物料表 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-01-20
 */
@Service
public class StorehousePositionsBindServiceImpl extends ServiceImpl<StorehousePositionsBindMapper, StorehousePositionsBind> implements StorehousePositionsBindService {

    @Override
    public void add(StorehousePositionsBindParam param){
        StorehousePositionsBind entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(StorehousePositionsBindParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(StorehousePositionsBindParam param){
        StorehousePositionsBind oldEntity = getOldEntity(param);
        StorehousePositionsBind newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public StorehousePositionsBindResult findBySpec(StorehousePositionsBindParam param){
        return null;
    }

    @Override
    public List<StorehousePositionsBindResult> findListBySpec(StorehousePositionsBindParam param){
        return null;
    }

    @Override
    public PageInfo<StorehousePositionsBindResult> findPageBySpec(StorehousePositionsBindParam param){
        Page<StorehousePositionsBindResult> pageContext = getPageContext();
        IPage<StorehousePositionsBindResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(StorehousePositionsBindParam param){
        return param.getBindId();
    }

    private Page<StorehousePositionsBindResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private StorehousePositionsBind getOldEntity(StorehousePositionsBindParam param) {
        return this.getById(getKey(param));
    }

    private StorehousePositionsBind getEntity(StorehousePositionsBindParam param) {
        StorehousePositionsBind entity = new StorehousePositionsBind();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
