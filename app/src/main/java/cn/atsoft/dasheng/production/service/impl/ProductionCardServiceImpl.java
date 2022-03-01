package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionCard;
import cn.atsoft.dasheng.production.entity.ProductionPlanDetail;
import cn.atsoft.dasheng.production.mapper.ProductionCardMapper;
import cn.atsoft.dasheng.production.model.params.ProductionCardParam;
import cn.atsoft.dasheng.production.model.params.ProductionWorkOrderParam;
import cn.atsoft.dasheng.production.model.result.ProductionCardResult;
import  cn.atsoft.dasheng.production.service.ProductionCardService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 生产卡片 服务实现类
 * </p>
 *
 * @author 
 * @since 2022-02-28
 */
@Service
public class ProductionCardServiceImpl extends ServiceImpl<ProductionCardMapper, ProductionCard> implements ProductionCardService {

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
    @Override
    public void addBatchCardByProductionPlan(Object param){
        List<ProductionPlanDetail> productionPlanDetails = JSON.parseArray(param.toString(), ProductionPlanDetail.class);
        List<ProductionCard> cardList = new ArrayList<>();
        for (ProductionPlanDetail productionPlanDetail : productionPlanDetails) {
            for (int i = 0; i < productionPlanDetail.getPlanNumber(); i++) {
                ProductionCard card = new ProductionCard();
                card.setSkuId(productionPlanDetail.getSkuId());
                card.setSource("productionPlan");
                card.setSourceId(productionPlanDetail.getProductionPlanId());
                //TODO 增加来源JSON
                cardList.add(card);
            }
        }
        this.saveBatch(cardList);
    }

}
