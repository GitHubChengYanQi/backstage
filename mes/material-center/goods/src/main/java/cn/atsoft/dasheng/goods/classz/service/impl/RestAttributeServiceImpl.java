package cn.atsoft.dasheng.goods.classz.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.goods.classz.entity.RestAttribute;
import cn.atsoft.dasheng.goods.classz.mapper.RestAttributeMapper;
import cn.atsoft.dasheng.goods.classz.model.params.RestAttributeParam;
import cn.atsoft.dasheng.goods.classz.model.result.RestAttributeResult;
import cn.atsoft.dasheng.goods.classz.service.RestAttributeService;
import cn.atsoft.dasheng.goods.spu.entity.RestSpu;
import cn.atsoft.dasheng.goods.spu.model.result.RestSpuResult;
import cn.atsoft.dasheng.goods.spu.service.RestSpuService;
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
 * 产品属性表 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-18
 */
@Service
public class RestAttributeServiceImpl extends ServiceImpl<RestAttributeMapper, RestAttribute> implements RestAttributeService {

    @Autowired
    private RestSpuService spuService;

    @Override
    public Long add(RestAttributeParam param) {
        Integer count = this.query().eq("category_id", param.getCategoryId()).eq("attribute", param.getAttribute())
                .in("display", 1)
                .count();
        if (count > 0) {
            throw new ServiceException(500, "请不要重复添加属性");
        }
        RestAttribute entity = getEntity(param);
        this.save(entity);
        return entity.getAttributeId();
    }

    @Override

    public void delete(RestAttributeParam param) {
        RestAttribute itemAttribute = new RestAttribute();
        itemAttribute.setDisplay(0);
        QueryWrapper<RestAttribute> itemAttributeQueryWrapper = new QueryWrapper<>();
        itemAttributeQueryWrapper.in("attribute_id", param.getAttributeId());
//        this.removeById(getKey(param));
        this.update(itemAttribute, itemAttributeQueryWrapper);
    }

    @Override

    public void update(RestAttributeParam param) {
        RestAttribute oldEntity = getOldEntity(param);
        RestAttribute newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public RestAttributeResult findBySpec(RestAttributeParam param) {
        return null;
    }

    @Override
    public List<RestAttributeResult> findListBySpec(RestAttributeParam param) {
        return null;
    }

    @Override
    public PageInfo<RestAttributeResult> findPageBySpec(RestAttributeParam param) {
        Page<RestAttributeResult> pageContext = getPageContext();
        IPage<RestAttributeResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(RestAttributeParam param) {
        return param.getAttributeId();
    }

    private Page<RestAttributeResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private RestAttribute getOldEntity(RestAttributeParam param) {
        return this.getById(getKey(param));
    }

    private RestAttribute getEntity(RestAttributeParam param) {
        RestAttribute entity = new RestAttribute();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    public void format(List<RestAttributeResult> data) {
        List<Long> itemIds = new ArrayList<>();
        for (RestAttributeResult datum : data) {
            itemIds.add(datum.getItemId());
        }
        if (ToolUtil.isNotEmpty(itemIds)) {
            List<RestSpu> spus = spuService.lambdaQuery().in(RestSpu::getSpuId, itemIds).list();
            for (RestAttributeResult datum : data) {
                for (RestSpu spu : spus) {
                    if (datum.getItemId().equals(spu.getSpuId())) {
                        RestSpuResult spuResult = new RestSpuResult();
                        ToolUtil.copyProperties(spu, spuResult);
                        datum.setSpuResult(spuResult);
                        break;
                    }
                }
            }
        }

    }
}
