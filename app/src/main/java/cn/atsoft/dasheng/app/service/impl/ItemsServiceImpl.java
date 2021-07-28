package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Items;
import cn.atsoft.dasheng.app.mapper.ItemsMapper;
import cn.atsoft.dasheng.app.model.params.ItemsParam;
import cn.atsoft.dasheng.app.model.result.ItemsResult;
import  cn.atsoft.dasheng.app.service.ItemsService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 物品表 服务实现类
 * </p>
 *
 * @author 1
 * @since 2021-07-14
 */
@Service
public class ItemsServiceImpl extends ServiceImpl<ItemsMapper, Items> implements ItemsService {

    @Override
    public Long add(ItemsParam param){
        Items entity = getEntity(param);
        this.save(entity);
        return entity.getItemId();
    }

    @Override
    public void delete(ItemsParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ItemsParam param){
        Items oldEntity = getOldEntity(param);
        Items newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ItemsResult findBySpec(ItemsParam param){
        return null;
    }

    @Override
    public List<ItemsResult> findListBySpec(ItemsParam param){
        return null;
    }
    @Override
    public PageInfo<ItemsResult> findPageBySpec(ItemsParam param){
        Page<ItemsResult> pageContext = getPageContext();
        IPage<ItemsResult> page = this.baseMapper.customPageList(pageContext, param);

        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ItemsParam param){
        return param.getItemId();
    }

    private Page<ItemsResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Items getOldEntity(ItemsParam param) {
        return this.getById(getKey(param));
    }

    private Items getEntity(ItemsParam param) {
        Items entity = new Items();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
