package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.StorehousePositions;
import cn.atsoft.dasheng.erp.mapper.StorehousePositionsMapper;
import cn.atsoft.dasheng.erp.model.params.StorehousePositionsParam;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import  cn.atsoft.dasheng.erp.service.StorehousePositionsService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 仓库库位表 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-29
 */
@Service
public class StorehousePositionsServiceImpl extends ServiceImpl<StorehousePositionsMapper, StorehousePositions> implements StorehousePositionsService {

    @Override
    public void add(StorehousePositionsParam param){
        StorehousePositions entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(StorehousePositionsParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(StorehousePositionsParam param){
        StorehousePositions oldEntity = getOldEntity(param);
        StorehousePositions newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public StorehousePositionsResult findBySpec(StorehousePositionsParam param){
        return null;
    }

    @Override
    public List<StorehousePositionsResult> findListBySpec(StorehousePositionsParam param){
        return null;
    }

    @Override
    public PageInfo<StorehousePositionsResult> findPageBySpec(StorehousePositionsParam param){
        Page<StorehousePositionsResult> pageContext = getPageContext();
        IPage<StorehousePositionsResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(StorehousePositionsParam param){
        return param.getStorehousePositionsId();
    }

    private Page<StorehousePositionsResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private StorehousePositions getOldEntity(StorehousePositionsParam param) {
        return this.getById(getKey(param));
    }

    private StorehousePositions getEntity(StorehousePositionsParam param) {
        StorehousePositions entity = new StorehousePositions();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
