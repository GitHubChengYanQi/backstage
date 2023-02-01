package cn.atsoft.dasheng.goods.classz.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.goods.classz.entity.RestAttribute;
import cn.atsoft.dasheng.goods.classz.entity.RestAttributeValues;
import cn.atsoft.dasheng.goods.classz.mapper.RestAttributeValuesMapper;
import cn.atsoft.dasheng.goods.classz.model.params.RestAttributeValuesParam;
import cn.atsoft.dasheng.goods.classz.model.result.RestAttributeResult;
import cn.atsoft.dasheng.goods.classz.model.result.RestAttributeValuesResult;
import cn.atsoft.dasheng.goods.classz.service.RestAttributeValuesService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
public class RestAttributeValuesServiceImpl extends ServiceImpl<RestAttributeValuesMapper, RestAttributeValues> implements RestAttributeValuesService {
    @Autowired
    private RestAttributeServiceImpl restAttributeService;

    @Override
    public Long add(RestAttributeValuesParam param) {
        Integer count = this.query().eq("attribute_id", param.getAttributeId()).eq("attribute_values", param.getAttributeValues()).eq("display", 1).count();
        if (count > 0) {
            throw new ServiceException(500, "请不要重复添加属性");
        }
        RestAttributeValues entity = getEntity(param);
        this.save(entity);
        return entity.getAttributeValuesId();
    }


    @Override
    public void delete(RestAttributeValuesParam param) {
//        this.removeById(getKey(param));
        RestAttributeValues attributeValues = new RestAttributeValues();
        attributeValues.setDisplay(0);
        QueryWrapper<RestAttributeValues> attributeValuesQueryWrapper = new QueryWrapper<>();
        attributeValuesQueryWrapper.in("attribute_values_id", param.getAttributeValuesId());
        this.update(attributeValues, attributeValuesQueryWrapper);
    }


    @Override
    public void update(RestAttributeValuesParam param) {
        RestAttributeValues oldEntity = getOldEntity(param);
        RestAttributeValues newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public RestAttributeValuesResult findBySpec(RestAttributeValuesParam param) {
        return null;
    }

    @Override
    public List<RestAttributeValuesResult> findListBySpec(RestAttributeValuesParam param) {
        return null;
    }

    @Override
    public PageInfo<RestAttributeValuesResult> findPageBySpec(RestAttributeValuesParam param) {
        Page<RestAttributeValuesResult> pageContext = getPageContext();
        IPage<RestAttributeValuesResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    @Override
    public List<RestAttributeValues> restAttributeValuesByAttributeId(List<Long> attributeId) {
        List<RestAttributeValues> restAttributeValues = this.baseMapper.restAttributeValuesByAttributeId(attributeId);
        return restAttributeValues;
    }

    private Serializable getKey(RestAttributeValuesParam param) {
        return param.getAttributeValuesId();
    }

    private Page<RestAttributeValuesResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private RestAttributeValues getOldEntity(RestAttributeValuesParam param) {
        return this.getById(getKey(param));
    }

    private RestAttributeValues getEntity(RestAttributeValuesParam param) {
        RestAttributeValues entity = new RestAttributeValues();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    public void format(List<RestAttributeValuesResult> data) {
        List<Long> ids = new ArrayList<>();
        for (RestAttributeValuesResult datum : data) {
            ids.add(datum.getAttributeId());
        }

        if (ToolUtil.isNotEmpty(ids)) {
            List<RestAttribute> itemAttributes = restAttributeService.lambdaQuery().in(RestAttribute::getAttributeId, ids).list();
            if (ToolUtil.isNotEmpty(itemAttributes)) {
                for (RestAttribute itemAttribute : itemAttributes) {
                    for (RestAttributeValuesResult datum : data) {
                        if (datum.getAttributeId().equals(itemAttribute.getAttributeId())) {
                            RestAttributeResult itemAttributeResult = new RestAttributeResult();
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
