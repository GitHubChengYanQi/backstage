package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Storehouse;
import cn.atsoft.dasheng.app.mapper.StorehouseMapper;
import cn.atsoft.dasheng.app.model.params.StorehouseParam;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.app.service.StorehouseService;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 地点表 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-07-15
 */
@Service
public class StorehouseServiceImpl extends ServiceImpl<StorehouseMapper, Storehouse> implements StorehouseService {

    @Override
    public Long add(StorehouseParam param){
        Storehouse entity = getEntity(param);
        this.save(entity);
        return entity.getStorehouseId();
    }

    @Override
    public void delete(StorehouseParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(StorehouseParam param){
        Storehouse oldEntity = getOldEntity(param);
        Storehouse newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public StorehouseResult findBySpec(StorehouseParam param){
        return null;
    }

    @Override
    public List<StorehouseResult> findListBySpec(StorehouseParam param){
        return null;
    }

    @Override
    public PageInfo<StorehouseResult> findPageBySpec(StorehouseParam param, DataScope dataScope){
        Page<StorehouseResult> pageContext = getPageContext();
        IPage<StorehouseResult> page = this.baseMapper.customPageList(pageContext, param,dataScope);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(StorehouseParam param){
        return param.getStorehouseId();
    }

    private Page<StorehouseResult> getPageContext() {
        List<String> fields = new ArrayList<String>(){{
            add("itemId");
            add("brandId");
            add("name");
            add("capacity");

        }};
        return PageFactory.defaultPage(fields);
    }

    private Storehouse getOldEntity(StorehouseParam param) {
        return this.getById(getKey(param));
    }

    private Storehouse getEntity(StorehouseParam param) {
        Storehouse entity = new Storehouse();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
