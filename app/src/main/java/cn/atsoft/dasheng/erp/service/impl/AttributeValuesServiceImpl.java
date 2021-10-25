package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.AttributeValues;
import cn.atsoft.dasheng.erp.entity.ItemAttribute;
import cn.atsoft.dasheng.erp.mapper.AttributeValuesMapper;
import cn.atsoft.dasheng.erp.model.params.AttributeValuesParam;
import cn.atsoft.dasheng.erp.model.result.AttributeValuesResult;
import cn.atsoft.dasheng.erp.model.result.ItemAttributeResult;
import cn.atsoft.dasheng.erp.service.AttributeValuesService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.ItemAttributeService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 产品属性数据表 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-18
 */
@Service
public class AttributeValuesServiceImpl extends ServiceImpl<AttributeValuesMapper, AttributeValues> implements AttributeValuesService {
    @Autowired
    private ItemAttributeService itemAttributeService;

    @Override
    public void add(AttributeValuesParam param) {
        Integer count = this.query().in("attribute_id", param.getAttributeId()).in("attribute_values", param.getAttributeValues()).count();
        if (count > 0) {
            throw new ServiceException(500, "不要重复添加");
        }
        AttributeValues entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(AttributeValuesParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(AttributeValuesParam param) {
        AttributeValues oldEntity = getOldEntity(param);
        AttributeValues newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public AttributeValuesResult findBySpec(AttributeValuesParam param) {
        return null;
    }

    @Override
    public List<AttributeValuesResult> findListBySpec(AttributeValuesParam param) {
        return null;
    }

    @Override
    public PageInfo<AttributeValuesResult> findPageBySpec(AttributeValuesParam param) {
        Page<AttributeValuesResult> pageContext = getPageContext();
        IPage<AttributeValuesResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(AttributeValuesParam param) {
        return param.getAttributeValuesId();
    }

    private Page<AttributeValuesResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private AttributeValues getOldEntity(AttributeValuesParam param) {
        return this.getById(getKey(param));
    }

    private AttributeValues getEntity(AttributeValuesParam param) {
        AttributeValues entity = new AttributeValues();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    public void format(List<AttributeValuesResult> data) {
        List<Long> ids = new ArrayList<>();
        for (AttributeValuesResult datum : data) {
            ids.add(datum.getAttributeId());
        }

        if (ToolUtil.isNotEmpty(ids)) {
            List<ItemAttribute> itemAttributes = itemAttributeService.lambdaQuery().in(ItemAttribute::getAttributeId, ids).list();
            if (ToolUtil.isNotEmpty(itemAttributes)) {
                for (ItemAttribute itemAttribute : itemAttributes) {
                    for (AttributeValuesResult datum : data) {
                        if (datum.getAttributeId().equals(itemAttribute.getAttributeId())) {
                            ItemAttributeResult itemAttributeResult = new ItemAttributeResult();
                            ToolUtil.copyProperties(itemAttribute, itemAttributeResult);
                            datum.setItemAttributeResult(itemAttributeResult);
                            break;
                        }
                    }
                }
            }
        }
    }
}
