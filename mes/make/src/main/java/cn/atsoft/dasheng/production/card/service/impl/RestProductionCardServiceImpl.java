package cn.atsoft.dasheng.production.card.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.card.entity.ProductionCard;
import cn.atsoft.dasheng.production.card.mapper.RestProductionCardMapper;
import cn.atsoft.dasheng.production.card.model.params.ProductionCardParam;
import cn.atsoft.dasheng.production.card.model.result.ProductionCardResult;
import cn.atsoft.dasheng.production.card.service.RestProductionCardService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 生产卡片 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-03-10
 */
@Service
public class RestProductionCardServiceImpl extends ServiceImpl<RestProductionCardMapper, ProductionCard> implements RestProductionCardService {

    @Override
    public void add(ProductionCardParam param){
        ProductionCard entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ProductionCardParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ProductionCardParam param){
        ProductionCard oldEntity = getOldEntity(param);
        ProductionCard newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ProductionCardResult findBySpec(ProductionCardParam param){
        return null;
    }

    @Override
    public List<ProductionCardResult> findListBySpec(ProductionCardParam param){
        return null;
    }

    @Override
    public PageInfo<ProductionCardResult> findPageBySpec(ProductionCardParam param){
        Page<ProductionCardResult> pageContext = getPageContext();
        IPage<ProductionCardResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ProductionCardParam param){
        return param.getProductionCardId();
    }

    private Page<ProductionCardResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ProductionCard getOldEntity(ProductionCardParam param) {
        return this.getById(getKey(param));
    }

    private ProductionCard getEntity(ProductionCardParam param) {
        ProductionCard entity = new ProductionCard();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
