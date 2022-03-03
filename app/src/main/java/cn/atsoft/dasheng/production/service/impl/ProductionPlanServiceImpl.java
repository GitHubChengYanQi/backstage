package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.message.enmu.MicroServiceType;
import cn.atsoft.dasheng.message.enmu.OperationType;
import cn.atsoft.dasheng.message.entity.MicroServiceEntity;
import cn.atsoft.dasheng.message.producer.MessageProducer;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.production.entity.ProductionPlan;
import cn.atsoft.dasheng.production.entity.ProductionPlanDetail;
import cn.atsoft.dasheng.production.mapper.ProductionPlanMapper;
import cn.atsoft.dasheng.production.model.params.ProductionPlanDetailParam;
import cn.atsoft.dasheng.production.model.params.ProductionPlanParam;
import cn.atsoft.dasheng.production.model.result.ProductionPlanResult;
import cn.atsoft.dasheng.production.service.ProductionPlanDetailService;
import cn.atsoft.dasheng.production.service.ProductionPlanService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.alibaba.fastjson.JSON;
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
 *  生产计划主表 服务实现类
 * </p>
 *
 * @author 
 * @since 2022-02-25
 */
@Service
public class ProductionPlanServiceImpl extends ServiceImpl<ProductionPlanMapper, ProductionPlan> implements ProductionPlanService {

    @Autowired
    private ProductionPlanDetailService productionPlanDetailService;

    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    private PartsService partsService;

    @Override
    public void add(ProductionPlanParam param){
        ProductionPlan entity = getEntity(param);
        this.save(entity);
        List<Long> skuIds = new ArrayList<>();

        List<ProductionPlanDetail> details = new ArrayList<>();
        for (ProductionPlanDetailParam productionPlanDetailParam : param.getProductionPlanDetailParams()) {
            ProductionPlanDetail detail = new ProductionPlanDetail();
            ToolUtil.copyProperties(productionPlanDetailParam,detail);
            detail.setProductionPlanId(entity.getProductionPlanId());
            skuIds.add(detail.getSkuId());
            details.add(detail);
        }
//        Integer designParts = partsService.query().in("sku_id", skuIds).eq("type", 1).eq("display", 1).eq("status", 99).count();
//        if (designParts<skuIds.size()) {
//            int i = skuIds.size() - designParts;
//            throw new ServiceException(500, "有"+i+"个物品没有设计bom,请先创建设计bom");
//        }
//        Integer productionParts = partsService.query().in("sku_id", skuIds).eq("type", 2).eq("display", 1).eq("status", 99).count();
//        if (productionParts<skuIds.size()) {
//            int i = skuIds.size() - productionParts;
//            throw new ServiceException(500, "有"+i+"个物品你没有生产bom,请先创建生产bom");
//        }
        productionPlanDetailService.saveBatch(details);
        if (details.size() > 0) {    //调用消息队列
            MicroServiceEntity serviceEntity = new MicroServiceEntity();
            serviceEntity.setType(MicroServiceType.WORK_ORDER);
            serviceEntity.setOperationType(OperationType.ADD);
            String jsonString = JSON.toJSONString(details);
            serviceEntity.setObject(jsonString);
            serviceEntity.setMaxTimes(2);
            serviceEntity.setTimes(0);
            messageProducer.microService(serviceEntity);
        }

    }

    @Override
    public void delete(ProductionPlanParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ProductionPlanParam param){
        ProductionPlan oldEntity = getOldEntity(param);
        ProductionPlan newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ProductionPlanResult findBySpec(ProductionPlanParam param){
        return null;
    }

    @Override
    public List<ProductionPlanResult> findListBySpec(ProductionPlanParam param){
        return null;
    }

    @Override
    public PageInfo<ProductionPlanResult> findPageBySpec(ProductionPlanParam param){
        Page<ProductionPlanResult> pageContext = getPageContext();
        IPage<ProductionPlanResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ProductionPlanParam param){
        return param.getProductionPlanId();
    }

    private Page<ProductionPlanResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ProductionPlan getOldEntity(ProductionPlanParam param) {
        return this.getById(getKey(param));
    }

    private ProductionPlan getEntity(ProductionPlanParam param) {
        ProductionPlan entity = new ProductionPlan();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
