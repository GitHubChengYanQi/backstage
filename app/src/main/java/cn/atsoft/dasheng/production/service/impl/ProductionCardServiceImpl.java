package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.bom.model.result.RestBomResult;
import cn.atsoft.dasheng.erp.entity.CodingRules;
import cn.atsoft.dasheng.erp.service.CodingRulesService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.production.entity.ProductionCard;
import cn.atsoft.dasheng.production.entity.ProductionPlan;
import cn.atsoft.dasheng.production.entity.ProductionPlanDetail;
import cn.atsoft.dasheng.production.entity.ProductionTask;
import cn.atsoft.dasheng.production.mapper.ProductionCardMapper;
import cn.atsoft.dasheng.production.model.params.ProductionCardParam;
import cn.atsoft.dasheng.production.model.result.ProductionCardResult;
import cn.atsoft.dasheng.production.model.result.ProductionTaskResult;
import  cn.atsoft.dasheng.production.service.ProductionCardService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.production.service.ProductionPlanDetailService;
import cn.atsoft.dasheng.production.service.ProductionPlanService;
import cn.atsoft.dasheng.production.service.ProductionTaskService;
import cn.atsoft.dasheng.purchase.service.GetOrigin;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private CodingRulesService codingRulesService;

    @Autowired
    private ProductionPlanService productionPlanService;

    @Autowired
    private ProductionPlanDetailService planDetailService;
    @Autowired
    private PartsService partsService;
    @Autowired
    private ProductionTaskService productionTaskService;

    @Override
    public void add(ProductionCardParam param){
        ProductionCard entity = getEntity(param);
        this.save(entity);
    }
    @Override
    @Transactional
    public List<ProductionCard> addBatch(List<ProductionCard> cardList){
        //更新来源字段
        for (ProductionCard card : cardList) {
                CodingRules codingRules = codingRulesService.query().eq("module", "20").eq("state", 1).one();
                if (ToolUtil.isNotEmpty(codingRules)) {
                    String coding = codingRulesService.backCoding(codingRules.getCodingRulesId());
                    card.setCardCoding(coding);
                }else {
                    card.setCardCoding(DateUtil.today()+ RandomUtil.randomNumbers(4));
                }

        }
        this.saveBatch(cardList);
        for (ProductionCard card : cardList) {
            String origin = "";
            if (ToolUtil.isNotEmpty(card.getSource())&&ToolUtil.isNotEmpty(card.getSourceId())){
                origin = getOrigin.newThemeAndOrigin("productionCard", card.getProductionCardId(), card.getSource(), card.getSourceId());

            }else {
                origin = getOrigin.newThemeAndOrigin("productionCard", card.getProductionCardId(), null, null);

            }
            card.setOrigin(origin);
        }
        this.updateBatchById(cardList);
        return cardList;
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
        this.format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }
    @Override
    public void format(List<ProductionCardResult> dataList){
        List<Long> cardIds = dataList.stream().map(ProductionCardResult::getProductionCardId).collect(Collectors.toList());
        List<ProductionTask> productionTasks =cardIds.size() == 0 ? new ArrayList<>() : productionTaskService.lambdaQuery().in(ProductionTask::getProductionCardId, cardIds).eq(ProductionTask::getStatus,99).list();


        for (ProductionCardResult productionCardResult : dataList) {
            int doneTaskCount = 0;
            for (ProductionTask productionTask : productionTasks) {
                if (productionCardResult.getProductionCardId().equals(productionTask.getProductionCardId())){
                    doneTaskCount++;
                }
            }
            productionCardResult.setDoneBomCount(doneTaskCount);
        }
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
        if (ToolUtil.isEmpty(productionPlanId) || productionPlanId.size() == 0){
            return  new ArrayList<>();
        }
        List<ProductionCardResult> productionCards = this.baseMapper.grupByProductionPlan(new ProductionCardParam(){{
            setProductionPlanIds(productionPlanId);
        }});



        return  productionCards;
    }
    @Override
    public List<RestBomResult> getBomListByCardId(Long cardId){
        ProductionCard card = this.getById(cardId);
        if (ToolUtil.isEmpty(card)){
            throw new ServiceException(500,"参数错误");
        }
        if (card.getSource().equals("productionPlan")) {
            ProductionPlan productionPlan = productionPlanService.getById(card.getSourceId());
            List<ProductionTask> productionTasks = productionTaskService.lambdaQuery().eq(ProductionTask::getProductionCardId, card.getProductionCardId()).list();
            List<ProductionTaskResult> productionTaskResults = BeanUtil.copyToList(productionTasks, ProductionTaskResult.class);
            return productionTaskService.formatBomList(productionTaskResults);
        }
        return new ArrayList<>();
    }

}
