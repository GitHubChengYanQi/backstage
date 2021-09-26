package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.ItemBrand;
import cn.atsoft.dasheng.app.mapper.ItemBrandMapper;
import cn.atsoft.dasheng.app.model.params.ItemBrandParam;
import cn.atsoft.dasheng.app.model.result.ItemBrandResult;
import  cn.atsoft.dasheng.app.service.ItemBrandService;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 商品品牌绑定表 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-09-23
 */
@Service
public class ItemBrandServiceImpl extends ServiceImpl<ItemBrandMapper, ItemBrand> implements ItemBrandService {

    @Override
    public void add(ItemBrandParam param){
        ItemBrand entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ItemBrandParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ItemBrandParam param){
        ItemBrand oldEntity = getOldEntity(param);
        ItemBrand newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ItemBrandResult findBySpec(ItemBrandParam param){
        return null;
    }

    @Override
    public List<ItemBrandResult> findListBySpec(ItemBrandParam param){
        return null;
    }

    @Override
    public PageInfo<ItemBrandResult> findPageBySpec(ItemBrandParam param, DataScope dataScope){
        Page<ItemBrandResult> pageContext = getPageContext();
        IPage<ItemBrandResult> page = this.baseMapper.customPageList(pageContext, param,dataScope);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ItemBrandParam param){
        return param.getItemId();
    }

    private Page<ItemBrandResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ItemBrand getOldEntity(ItemBrandParam param) {
        return this.getById(getKey(param));
    }

    private ItemBrand getEntity(ItemBrandParam param) {
        ItemBrand entity = new ItemBrand();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
