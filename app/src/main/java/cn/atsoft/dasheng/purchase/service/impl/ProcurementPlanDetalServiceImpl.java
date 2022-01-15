package cn.atsoft.dasheng.purchase.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.purchase.entity.ProcurementPlan;
import cn.atsoft.dasheng.purchase.entity.ProcurementPlanDetal;
import cn.atsoft.dasheng.purchase.entity.PurchaseListing;
import cn.atsoft.dasheng.purchase.mapper.ProcurementPlanDetalMapper;
import cn.atsoft.dasheng.purchase.model.params.ProcurementPlanBindParam;
import cn.atsoft.dasheng.purchase.model.params.ProcurementPlanDetalParam;
import cn.atsoft.dasheng.purchase.model.params.ProcurementPlanParam;
import cn.atsoft.dasheng.purchase.model.request.ProcurementDetailSkuTotal;
import cn.atsoft.dasheng.purchase.model.result.ProcurementPlanBindResult;
import cn.atsoft.dasheng.purchase.model.result.ProcurementPlanDetalResult;
import cn.atsoft.dasheng.purchase.service.ProcurementPlanBindService;
import cn.atsoft.dasheng.purchase.service.ProcurementPlanDetalService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.purchase.service.ProcurementPlanService;
import cn.atsoft.dasheng.purchase.service.PurchaseListingService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.omg.CORBA.LongHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 采购计划单子表  整合数据后的 子表 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-12-21
 */
@Service
public class ProcurementPlanDetalServiceImpl extends ServiceImpl<ProcurementPlanDetalMapper, ProcurementPlanDetal> implements ProcurementPlanDetalService {
    @Autowired
    private PurchaseListingService purchaseListingService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private ProcurementPlanService planService;
    @Autowired
    private ProcurementPlanBindService bindService;

    @Override
    public void add(ProcurementPlanDetalParam param) {


        ProcurementPlanDetal entity = getEntity(param);
        this.save(entity);

    }

    @Override
    public void batchAdd(ProcurementPlanParam param) {

        List<PurchaseListing> purchaseListingList = purchaseListingService.listByIds(param.getListingIds());
        if (purchaseListingList.size() != param.getListingIds().size()) {
            throw new ServiceException(500, "申请单详情不合法");
        }
        List<ProcurementDetailSkuTotal> pdstList = new ArrayList<>();
        for (PurchaseListing purchaseListing : purchaseListingList) {
            ProcurementDetailSkuTotal pdst = new ProcurementDetailSkuTotal();
            pdst.setSkuId(purchaseListing.getSkuId());
            pdst.setTotal(purchaseListing.getApplyNumber());
            pdstList.add(pdst);
        }
        List<ProcurementDetailSkuTotal> totalList = new ArrayList<>();
        pdstList.parallelStream().collect(Collectors.groupingBy(ProcurementDetailSkuTotal::getSkuId, Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new ProcurementDetailSkuTotal(a.getSkuId(), a.getTotal() + b.getTotal())).ifPresent(totalList::add);
                }
        );
        List<ProcurementPlanDetal> entityList = new ArrayList<>();
        for (ProcurementDetailSkuTotal procurementDetailSkuTotal : totalList) {
            ProcurementPlanDetal entity = new ProcurementPlanDetal();
            entity.setPlanId(param.getProcurementPlanId());
            entity.setSkuId(procurementDetailSkuTotal.getSkuId());
            entity.setTotal(procurementDetailSkuTotal.getTotal());
            entityList.add(entity);
        }
        this.saveBatch(entityList);
    }

    @Override
    public void delete(ProcurementPlanDetalParam param) {
        param.setDisplay(0);
        ProcurementPlanDetal newEntity = getEntity(param);
        this.updateById(newEntity);
    }

    @Override
    public void update(ProcurementPlanDetalParam param) {
        ProcurementPlanDetal oldEntity = getOldEntity(param);
        ProcurementPlanDetal newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        if (oldEntity.getDisplay().equals(newEntity.getDisplay())) {
            this.updateById(newEntity);
        }
    }

    @Override
    public ProcurementPlanDetalResult findBySpec(ProcurementPlanDetalParam param) {
        return null;
    }

    @Override
    public List<ProcurementPlanDetalResult> findListBySpec(ProcurementPlanDetalParam param) {
        return null;
    }

    @Override
    public PageInfo<ProcurementPlanDetalResult> findPageBySpec(ProcurementPlanDetalParam param) {
        Page<ProcurementPlanDetalResult> pageContext = getPageContext();
        IPage<ProcurementPlanDetalResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    @Override
    public List<ProcurementPlanDetalResult> getDetailByPlanId(List<Long> ids) {
        List<ProcurementPlanDetalResult> detalResults = new ArrayList<>();
        if (ToolUtil.isNotEmpty(ids)) {
            return detalResults;
        }
        List<ProcurementPlanDetal> planDetals = this.query().in("plan_id", ids).list();

        List<Long> skuIds = new ArrayList<>();
        for (ProcurementPlanDetal planDetal : planDetals) {
            skuIds.add(planDetal.getSkuId());
            ProcurementPlanDetalResult planDetalResult = new ProcurementPlanDetalResult();
            ToolUtil.copyProperties(planDetal, planDetalResult);
            detalResults.add(planDetalResult);
        }
        List<Sku> skus = skuService.listByIds(skuIds);
        List<SkuResult> skuResults = new ArrayList<>();
        for (Sku sku : skus) {
            SkuResult skuResult = new SkuResult();
            ToolUtil.copyProperties(sku, skuResult);
            skuResults.add(skuResult);
        }
        skuService.format(skuResults);
        for (ProcurementPlanDetalResult detalResult : detalResults) {
            for (SkuResult skuResult : skuResults) {
                if (detalResult.getSkuId().equals(skuResult.getSkuId())) {
                    detalResult.setSkuResult(skuResult);
                    break;
                }
            }
        }

        return detalResults;
    }

    @Override
    public List<ProcurementPlanDetalResult> details(Long planId) {
        List<ProcurementPlanDetal> planDetals = this.query().eq("plan_id", planId).list();
        return null;
    }

    private Serializable getKey(ProcurementPlanDetalParam param) {
        return param.getDetailId();
    }

    private Page<ProcurementPlanDetalResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ProcurementPlanDetal getOldEntity(ProcurementPlanDetalParam param) {
        return this.getById(getKey(param));
    }

    private ProcurementPlanDetal getEntity(ProcurementPlanDetalParam param) {
        ProcurementPlanDetal entity = new ProcurementPlanDetal();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    private void format(List<ProcurementPlanDetalResult> data) {
        List<Long> skuIds = new ArrayList<>();
        List<Long> planIds = new ArrayList<>();
        for (ProcurementPlanDetalResult datum : data) {
            skuIds.add(datum.getSkuId());
            planIds.add(datum.getPlanId());
        }

        List<Sku> skus = skuService.listByIds(skuIds);

        List<SkuResult> skuResults = new ArrayList<>();
        for (Sku sku : skus) {
            SkuResult skuResult = new SkuResult();
            ToolUtil.copyProperties(sku, skuResult);
            skuResults.add(skuResult);
        }
        skuService.format(skuResults);
        for (ProcurementPlanDetalResult datum : data) {
            for (SkuResult skuResult : skuResults) {
                if (datum.getSkuId().equals(skuResult.getSkuId())) {
                    datum.setSkuResult(skuResult);
                    break;
                }
            }

        }
    }
}
