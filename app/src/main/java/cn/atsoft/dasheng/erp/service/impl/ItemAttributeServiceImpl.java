package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.ItemAttribute;
import cn.atsoft.dasheng.erp.mapper.ItemAttributeMapper;
import cn.atsoft.dasheng.erp.model.params.ItemAttributeParam;
import cn.atsoft.dasheng.erp.model.result.ItemAttributeResult;
import  cn.atsoft.dasheng.erp.service.ItemAttributeService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 产品属性表 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-18
 */
@Service
public class ItemAttributeServiceImpl extends ServiceImpl<ItemAttributeMapper, ItemAttribute> implements ItemAttributeService {

    @Override
    public void add(ItemAttributeParam param){
        ItemAttribute entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ItemAttributeParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ItemAttributeParam param){
        ItemAttribute oldEntity = getOldEntity(param);
        ItemAttribute newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ItemAttributeResult findBySpec(ItemAttributeParam param){
        return null;
    }

    @Override
    public List<ItemAttributeResult> findListBySpec(ItemAttributeParam param){
        return null;
    }

    @Override
    public PageInfo<ItemAttributeResult> findPageBySpec(ItemAttributeParam param){
        Page<ItemAttributeResult> pageContext = getPageContext();
        IPage<ItemAttributeResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ItemAttributeParam param){
        return param.getAttributeId();
    }

    private Page<ItemAttributeResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ItemAttribute getOldEntity(ItemAttributeParam param) {
        return this.getById(getKey(param));
    }

    private ItemAttribute getEntity(ItemAttributeParam param) {
        ItemAttribute entity = new ItemAttribute();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
