package cn.atsoft.dasheng.production.card.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.card.entity.ProductionCardBind;
import cn.atsoft.dasheng.production.card.mapper.RestProductionCardBindMapper;
import cn.atsoft.dasheng.production.card.model.params.ProductionCardBindParam;
import cn.atsoft.dasheng.production.card.model.result.ProductionCardBindResult;
import cn.atsoft.dasheng.production.card.service.RestProductionCardBindService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 生产卡片绑定 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-03-10
 */
@Service
public class RestProductionCardBindServiceImpl extends ServiceImpl<RestProductionCardBindMapper, ProductionCardBind> implements RestProductionCardBindService {

    @Override
    public void add(ProductionCardBindParam param){
        ProductionCardBind entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ProductionCardBindParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ProductionCardBindParam param){
        ProductionCardBind oldEntity = getOldEntity(param);
        ProductionCardBind newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ProductionCardBindResult findBySpec(ProductionCardBindParam param){
        return null;
    }

    @Override
    public List<ProductionCardBindResult> findListBySpec(ProductionCardBindParam param){
        return null;
    }

    @Override
    public PageInfo<ProductionCardBindResult> findPageBySpec(ProductionCardBindParam param){
        Page<ProductionCardBindResult> pageContext = getPageContext();
        IPage<ProductionCardBindResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ProductionCardBindParam param){
        return param.getProductionCardBindId();
    }

    private Page<ProductionCardBindResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ProductionCardBind getOldEntity(ProductionCardBindParam param) {
        return this.getById(getKey(param));
    }

    private ProductionCardBind getEntity(ProductionCardBindParam param) {
        ProductionCardBind entity = new ProductionCardBind();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
