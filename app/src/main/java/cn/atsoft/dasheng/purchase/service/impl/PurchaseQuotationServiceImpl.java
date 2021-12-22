package cn.atsoft.dasheng.purchase.service.impl;


import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Supply;
import cn.atsoft.dasheng.crm.model.result.SupplyResult;
import cn.atsoft.dasheng.crm.service.SupplyService;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.purchase.entity.PurchaseQuotation;
import cn.atsoft.dasheng.purchase.mapper.PurchaseQuotationMapper;
import cn.atsoft.dasheng.purchase.model.params.PurchaseQuotationParam;
import cn.atsoft.dasheng.purchase.model.result.PurchaseQuotationResult;
import cn.atsoft.dasheng.purchase.pojo.QuotationParam;
import cn.atsoft.dasheng.purchase.service.PurchaseQuotationService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import oshi.jna.platform.mac.SystemB;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * <p>
 * 采购报价表单 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-22
 */
@Service
public class PurchaseQuotationServiceImpl extends ServiceImpl<PurchaseQuotationMapper, PurchaseQuotation> implements PurchaseQuotationService {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private SupplyService supplyService;
    @Autowired
    private SkuService skuService;

    @Override
    public void add(PurchaseQuotationParam param) {
        PurchaseQuotation entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(PurchaseQuotationParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(PurchaseQuotationParam param) {
        PurchaseQuotation oldEntity = getOldEntity(param);
        PurchaseQuotation newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public PurchaseQuotationResult findBySpec(PurchaseQuotationParam param) {
        return null;
    }

    @Override
    public List<PurchaseQuotationResult> findListBySpec(PurchaseQuotationParam param) {
        return null;
    }

    @Override
    public PageInfo<PurchaseQuotationResult> findPageBySpec(PurchaseQuotationParam param) {
        Page<PurchaseQuotationResult> pageContext = getPageContext();
        IPage<PurchaseQuotationResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    @Override
    public void addList(QuotationParam param) {
        Customer customer = customerService.getById(param.getCustomerId());
        if (ToolUtil.isEmpty(customer)) {
            throw new ServiceException(500, "请选择供应商");
        }
        if (customer.getSupply() != 1) {
            throw new ServiceException(500, "请选择正确的供应商");
        }

        List<PurchaseQuotation> quotations = new ArrayList<>();

        List<Supply> supplies = supplyService.query().eq("customer_id", customer.getCustomerId()).list();
        HashSet<Long> skuSst = new HashSet<>();
        for (Supply supply : supplies) {
            skuSst.add(supply.getSkuId());
        }
        for (PurchaseQuotationParam quotationParam : param.getQuotationParams()) {
            PurchaseQuotation quotation = new PurchaseQuotation();
            ToolUtil.copyProperties(quotationParam, quotation);
            quotations.add(quotation);
            quotation.setCustomerId(customer.getCustomerId());
            skuSst.add(quotationParam.getSkuId());
        }
        supplyService.remove(new QueryWrapper<Supply>() {{
            eq("customer_id", customer.getCustomerId());
        }});

        List<Supply> supplyList = new ArrayList<>();
        for (Long aLong : skuSst) {
            Supply supply = new Supply();
            supply.setSkuId(aLong);
            supply.setCustomerId(customer.getCustomerId());
            supplyList.add(supply);
        }
        supplyService.saveBatch(supplyList);
        this.saveBatch(quotations);
    }

    @Override
    public List<PurchaseQuotationResult> getList(Long customerId) {

        List<Supply> supplies = supplyService.query().eq("customer_id", customerId).list();

        List<PurchaseQuotation> quotations = this.list();

        //组合数据取交集
        List<Long> ids = new ArrayList<>();
        for (PurchaseQuotation quotation : quotations) {
            for (Supply supply : supplies) {
                if (!supply.getSkuId().equals(quotation.getSkuId())) {
                    ids.add(supply.getSupplyId());
                }
            }
        }
        List<PurchaseQuotation> quotationList = this.listByIds(ids);
        List<PurchaseQuotationResult> results = new ArrayList<>();
        List<Long> skuIds = new ArrayList<>();
        for (PurchaseQuotation quotation : quotationList) {
            PurchaseQuotationResult quotationResult = new PurchaseQuotationResult();
            ToolUtil.copyProperties(quotation, quotationResult);
            results.add(quotationResult);
            skuIds.add(quotation.getSkuId());
        }
        List<Sku> skus = skuService.listByIds(skuIds);

        List<SkuResult> skuResults = new ArrayList<>();
        for (Sku sku : skus) {
            SkuResult skuResult = new SkuResult();
            ToolUtil.copyProperties(sku, skuResult);
            skuResults.add(skuResult);
        }
        skuService.format(skuResults);

        for (PurchaseQuotationResult result : results) {
            for (SkuResult skuResult : skuResults) {
                if (skuResult.getSkuId().equals(result.getSkuId())) {
                    result.setSkuResult(skuResult);
                    break;
                }
            }
        }
        return results;
    }

    /**
     * 通过供应商查看
     *
     * @param customerId
     * @return
     */
    @Override
    public List<PurchaseQuotationResult> getListBySupply(Long customerId) {
        Customer customer = customerService.getById(customerId);
        if (ToolUtil.isEmpty(customer)) {
            throw new ServiceException(500, "当前供应商不存在");
        }
        if (customer.getSupply() != 1) {
            throw new ServiceException(500, "当前不是供应商");
        }

        List<SupplyResult> supplyResults = supplyService.getListByCustomerId(customer.getCustomerId());

        List<Long> skuIds = new ArrayList<>();
        for (SupplyResult supply : supplyResults) {
            skuIds.add(supply.getSkuId());
        }
        List<PurchaseQuotation> quotations = skuIds.size() == 0 ? new ArrayList<>() : this.query().in("sku_id", skuIds).list();

        List<PurchaseQuotationResult> quotationResults = new ArrayList<>();

        skuIds.clear();
        for (PurchaseQuotation quotation : quotations) {
            PurchaseQuotationResult quotationResult = new PurchaseQuotationResult();
            ToolUtil.copyProperties(quotation, quotationResult);
            quotationResults.add(quotationResult);
            skuIds.add(quotation.getSkuId());
        }

        List<Sku> skus = skuIds.size() == 01 ? new ArrayList<>() : skuService.listByIds(skuIds);

        List<SkuResult> skuResults = new ArrayList<>();
        for (Sku sku : skus) {
            SkuResult skuResult = new SkuResult();
            ToolUtil.copyProperties(sku, skuResult);
            skuResults.add(skuResult);
        }
        skuService.format(skuResults);

        for (PurchaseQuotationResult quotationResult : quotationResults) {
            for (SkuResult skuResult : skuResults) {
                if (skuResult.getSkuId().equals(quotationResult.getSkuId())) {
                    quotationResult.setSkuResult(skuResult);
                    break;
                }
            }
        }

        return quotationResults;
    }

    @Override
    public List<PurchaseQuotationResult> getListBySku(Long skuId) {
        List<PurchaseQuotation> purchaseQuotations = this.query().eq("sku_id", skuId).list();
        List<Long> customerId = new ArrayList<>();
        List<PurchaseQuotationResult> purchaseQuotationResults = new ArrayList<>();

        for (PurchaseQuotation purchaseQuotation : purchaseQuotations) {
            customerId.add(purchaseQuotation.getCustomerId());
            PurchaseQuotationResult purchaseQuotationResult = new PurchaseQuotationResult();
            ToolUtil.copyProperties(purchaseQuotation, purchaseQuotationResult);
            purchaseQuotationResults.add(purchaseQuotationResult);
        }
        List<Customer> customers = customerId.size() == 0 ? new ArrayList<>() : customerService.listByIds(customerId);

        for (PurchaseQuotationResult purchaseQuotationResult : purchaseQuotationResults) {
            for (Customer customer : customers) {
                if (ToolUtil.isNotEmpty(purchaseQuotationResult.getCustomerId()) && purchaseQuotationResult.getCustomerId().equals(customer.getCustomerId())) {
                    CustomerResult customerResult = new CustomerResult();
                    ToolUtil.copyProperties(customer, customerResult);
                    purchaseQuotationResult.setCustomerResult(customerResult);
                    break;
                }
            }
        }

        return purchaseQuotationResults;
    }

    private Serializable getKey(PurchaseQuotationParam param) {
        return param.getPurchaseQuotationId();
    }

    private Page<PurchaseQuotationResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private PurchaseQuotation getOldEntity(PurchaseQuotationParam param) {
        return this.getById(getKey(param));
    }

    private PurchaseQuotation getEntity(PurchaseQuotationParam param) {
        PurchaseQuotation entity = new PurchaseQuotation();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    private void format(List<PurchaseQuotationResult> data) {
        List<Long> skuIds = new ArrayList<>();
        for (PurchaseQuotationResult datum : data) {
            skuIds.add(datum.getSkuId());
        }
        List<Sku> skus = skuIds.size() == 0 ? new ArrayList<>() : skuService.listByIds(skuIds);

        List<SkuResult> skuResults = new ArrayList<>();
        for (Sku sku : skus) {
            SkuResult skuResult = new SkuResult();
            ToolUtil.copyProperties(sku, skuResult);
            skuResults.add(skuResult);
        }
        skuService.format(skuResults);
        for (PurchaseQuotationResult datum : data) {
            for (SkuResult skuResult : skuResults) {
                if (skuResult.getSkuId().equals(datum.getSkuId())) {
                    datum.setSkuResult(skuResult);
                    break;
                }
            }
        }
    }
}
