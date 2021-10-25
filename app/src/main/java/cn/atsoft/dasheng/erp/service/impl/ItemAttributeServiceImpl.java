package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.ItemAttribute;
import cn.atsoft.dasheng.erp.entity.Spu;
import cn.atsoft.dasheng.erp.mapper.ItemAttributeMapper;
import cn.atsoft.dasheng.erp.model.params.ItemAttributeParam;
import cn.atsoft.dasheng.erp.model.result.ItemAttributeResult;
import cn.atsoft.dasheng.erp.model.result.SpuResult;
import cn.atsoft.dasheng.erp.service.CodingRulesClassificationService;
import cn.atsoft.dasheng.erp.service.ItemAttributeService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.SpuService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.Action;
import java.io.Serializable;
import java.util.ArrayList;
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

    @Autowired
    private SpuService spuService;

    @Override
    public void add(ItemAttributeParam param) {
        ItemAttribute attribute = this.query().in("category_id", param.getCategoryId()).eq("attribute", param.getAttribute()).one();
        if (ToolUtil.isNotEmpty(attribute)) {
            throw new ServiceException(500, "请不要重复添加");
        }
        ItemAttribute entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ItemAttributeParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(ItemAttributeParam param) {
        ItemAttribute oldEntity = getOldEntity(param);
        ItemAttribute newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ItemAttributeResult findBySpec(ItemAttributeParam param) {
        return null;
    }

    @Override
    public List<ItemAttributeResult> findListBySpec(ItemAttributeParam param) {
        return null;
    }

    @Override
    public PageInfo<ItemAttributeResult> findPageBySpec(ItemAttributeParam param) {
        Page<ItemAttributeResult> pageContext = getPageContext();
        IPage<ItemAttributeResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ItemAttributeParam param) {
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

    public void format(List<ItemAttributeResult> data) {
        List<Long> itemIds = new ArrayList<>();
        for (ItemAttributeResult datum : data) {
            itemIds.add(datum.getItemId());
        }
        if (ToolUtil.isNotEmpty(itemIds)) {
            List<Spu> spus = spuService.lambdaQuery().in(Spu::getSpuId, itemIds).list();
            for (ItemAttributeResult datum : data) {
                for (Spu spu : spus) {
                    if (datum.getItemId().equals(spu.getSpuId())) {
                        SpuResult spuResult = new SpuResult();
                        ToolUtil.copyProperties(spu, spuResult);
                        datum.setSpuResult(spuResult);
                        break;
                    }
                }
            }
        }

    }
}
