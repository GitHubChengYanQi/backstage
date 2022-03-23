package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionCard;
import cn.atsoft.dasheng.production.entity.ProductionPlan;
import cn.atsoft.dasheng.production.entity.ProductionPlanDetail;
import cn.atsoft.dasheng.production.mapper.ProductionCardMapper;
import cn.atsoft.dasheng.production.model.params.ProductionCardParam;
import cn.atsoft.dasheng.production.model.params.ProductionWorkOrderParam;
import cn.atsoft.dasheng.production.model.result.ProductionCardResult;
import  cn.atsoft.dasheng.production.service.ProductionCardService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.production.service.ProductionPlanService;
import cn.atsoft.dasheng.purchase.pojo.ThemeAndOrigin;
import cn.atsoft.dasheng.purchase.service.GetOrigin;
import com.alibaba.fastjson.JSON;
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
 * 生产卡片 服务实现类
 * </p>
 *
 * @author 
 * @since 2022-02-28
 */
@Service
public class ProductionCardServiceImpl extends ServiceImpl<ProductionCardMapper, ProductionCard> implements ProductionCardService {

    @Autowired
    private GetOrigin getOrigin;

    @Autowired
    private ProductionPlanService productionPlanService;

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
    public  List<ProductionCard> addBatchCardByProductionPlan(Object param){
        List<ProductionPlanDetail> productionPlanDetails = JSON.parseArray(param.toString(), ProductionPlanDetail.class);
        List<ProductionCard> cardList = new ArrayList<>();
        Long productionPlanId = 0L;
        for (ProductionPlanDetail productionPlanDetail : productionPlanDetails) {
            productionPlanId = productionPlanDetail.getProductionPlanId();
            for (int i = 0; i < productionPlanDetail.getPlanNumber(); i++) {
                ProductionCard card = new ProductionCard();
                card.setSkuId(productionPlanDetail.getSkuId());
                card.setSource("productionPlan");
                card.setSourceId(productionPlanDetail.getProductionPlanId());
                cardList.add(card);
            }
        }
        this.saveBatch(cardList);
        //更新来源字段
        for (ProductionCard card : cardList) {
            String origin = getOrigin.newThemeAndOrigin("productionCard", card.getProductionCardId(), "productionPlan", productionPlanId);
            card.setOrigin(origin);
        }
        this.updateBatchById(cardList);

        return cardList;
    }
    @Override
    public List<ProductionCardResult> resultsByProductionPlanId(List<Long> productionPlanId){
        if (ToolUtil.isNotEmpty(productionPlanId) || productionPlanId.size() == 0){
            return  new ArrayList<>();
        }
        List<ProductionCardResult> productionCards = this.baseMapper.grupByProductionPlan(new ProductionCardParam(){{
            setProductionPlanIds(productionPlanId);
        }});



        return  productionCards;
    }


}
