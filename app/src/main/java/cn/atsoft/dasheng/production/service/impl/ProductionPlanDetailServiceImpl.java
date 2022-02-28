package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.model.result.PartsResult;
import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.message.enmu.MicroServiceType;
import cn.atsoft.dasheng.message.enmu.OperationType;
import cn.atsoft.dasheng.message.entity.MicroServiceEntity;
import cn.atsoft.dasheng.message.producer.MessageProducer;
import cn.atsoft.dasheng.production.entity.ProcessRoute;
import cn.atsoft.dasheng.production.entity.ProductionPlanDetail;
import cn.atsoft.dasheng.production.mapper.ProductionPlanDetailMapper;
import cn.atsoft.dasheng.production.model.params.ProductionPlanDetailParam;
import cn.atsoft.dasheng.production.model.result.ProductionPlanDetailResult;
import cn.atsoft.dasheng.production.service.ProcessRouteService;
import cn.atsoft.dasheng.production.service.ProductionPlanDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
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
 * 生产计划子表 服务实现类
 * </p>
 *
 * @author
 * @since 2022-02-25
 */
@Service
public class ProductionPlanDetailServiceImpl extends ServiceImpl<ProductionPlanDetailMapper, ProductionPlanDetail> implements ProductionPlanDetailService {
    @Autowired
    private SkuService skuService;
    @Autowired
    private StockDetailsService stockDetailsService;
    @Autowired
    private PartsService partsService;
    @Autowired
    private ProcessRouteService routeService;
    @Autowired
    private MessageProducer messageProducer;


    @Override
    public void add(ProductionPlanDetailParam param) {
        ProductionPlanDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ProductionPlanDetailParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(ProductionPlanDetailParam param) {
        ProductionPlanDetail oldEntity = getOldEntity(param);
        ProductionPlanDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    /**
     * 生产计划产品对应当前的bom
     */
    public void getItemBom(List<ProductionPlanDetailResult> detailResults) {

        List<Parts> partsList = partsService.query().eq("display", 1).list();
        List<ProcessRoute> processRoutes = routeService.list();

        List<ProductionPlanDetailResult> ok = new ArrayList<>();
        for (ProductionPlanDetailResult detail : detailResults) {
            boolean productionBom = productionBom(detail, partsList, processRoutes);  //是否含有生产bom和工艺路线
            boolean bom = designBom(detail, partsList);//是否含有工艺bom
            if (bom && productionBom) {
                ok.add(detail);
            }
        }
        if (ok.size() > 0) {    //调用消息队列
            MicroServiceEntity serviceEntity = new MicroServiceEntity();
            serviceEntity.setType(MicroServiceType.WORK_ORDER);
            serviceEntity.setOperationType(OperationType.ADD);
            String jsonString = JSON.toJSONString(ok);
            serviceEntity.setJson(jsonString);
            messageProducer.microService(serviceEntity);
        }
    }

    /**
     * 当前产品是否含有生产bom 和工艺路线
     *
     * @param detailResult
     * @param partsList
     * @param processRoutes
     * @return
     */
    private boolean productionBom(ProductionPlanDetailResult detailResult, List<Parts> partsList, List<ProcessRoute> processRoutes) {
        for (Parts parts : partsList) {
            for (ProcessRoute processRoute : processRoutes) {
                if (detailResult.getSkuId().equals(parts.getSkuId())
                        && parts.getType().equals("2")
                        && processRoute.getPartsId().equals(parts.getPartsId())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否含有设计bom
     *
     * @param detailResult
     * @param partsList
     * @return
     */
    private boolean designBom(ProductionPlanDetailResult detailResult, List<Parts> partsList) {
        for (Parts parts : partsList) {
            if (parts.getType().equals("1") && parts.getSkuId().equals(detailResult.getSkuId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ProductionPlanDetailResult findBySpec(ProductionPlanDetailParam param) {
        return null;
    }

    @Override
    public List<ProductionPlanDetailResult> findListBySpec(ProductionPlanDetailParam param) {
        return null;
    }

    @Override
    public PageInfo<ProductionPlanDetailResult> findPageBySpec(ProductionPlanDetailParam param) {
        Page<ProductionPlanDetailResult> pageContext = getPageContext();
        IPage<ProductionPlanDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ProductionPlanDetailParam param) {
        return param.getProductionPlanDetailId();
    }

    private Page<ProductionPlanDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ProductionPlanDetail getOldEntity(ProductionPlanDetailParam param) {
        return this.getById(getKey(param));
    }

    private ProductionPlanDetail getEntity(ProductionPlanDetailParam param) {
        ProductionPlanDetail entity = new ProductionPlanDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    private void format(List<ProductionPlanDetailResult> data) {

        List<Long> skuIds = new ArrayList<>();
        for (ProductionPlanDetailResult datum : data) {
            skuIds.add(datum.getSkuId());
        }
        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);
        List<StockDetails> details = stockDetailsService.query().in("sku_id", skuIds).list();

        for (ProductionPlanDetailResult datum : data) {
            for (SkuResult skuResult : skuResults) {
                if (datum.getSkuId().equals(skuResult.getSkuId())) {
                    datum.setSkuResult(skuResult);
                    break;
                }
            }
            long stockNumber = 0;
            for (StockDetails detail : details) {
                if (detail.getSkuId().equals(datum.getSkuId())) {
                    stockNumber = stockNumber + detail.getNumber();
                }
            }
            datum.setStockNumber(stockNumber);
        }

    }

}
