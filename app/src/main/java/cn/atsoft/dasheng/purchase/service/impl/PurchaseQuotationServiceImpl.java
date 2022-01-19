package cn.atsoft.dasheng.purchase.service.impl;


import cn.atsoft.dasheng.app.entity.Contract;
import cn.atsoft.dasheng.app.entity.CrmCustomerLevel;
import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.ContractService;
import cn.atsoft.dasheng.app.service.CrmCustomerLevelService;
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
import cn.atsoft.dasheng.purchase.entity.InquiryTask;
import cn.atsoft.dasheng.purchase.entity.ProcurementPlan;
import cn.atsoft.dasheng.purchase.entity.PurchaseQuotation;
import cn.atsoft.dasheng.purchase.mapper.PurchaseQuotationMapper;
import cn.atsoft.dasheng.purchase.model.params.PurchaseQuotationParam;
import cn.atsoft.dasheng.purchase.model.result.PurchaseQuotationResult;
import cn.atsoft.dasheng.purchase.pojo.QuotationParam;
import cn.atsoft.dasheng.purchase.pojo.SupplierResult;
import cn.atsoft.dasheng.purchase.service.ProcurementPlanService;
import cn.atsoft.dasheng.purchase.service.PurchaseQuotationService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.supplier.service.SupplierBrandService;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hibernate.validator.internal.util.Contracts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import oshi.jna.platform.mac.SystemB;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private UserService userService;
    @Autowired
    private ProcurementPlanService planService;
    @Autowired
    private InquiryTaskServiceImpl taskService;
    @Autowired
    private CrmCustomerLevelService levelService;
    @Autowired
    private SupplierBrandService supplierBrandService;


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
        List<PurchaseQuotationResult> purchaseQuotationResults = this.baseMapper.customList(param);
        format(purchaseQuotationResults);
        return purchaseQuotationResults;
    }

    @Override
    public PageInfo<PurchaseQuotationResult> findPageBySpec(PurchaseQuotationParam param) {
        Page<PurchaseQuotationResult> pageContext = getPageContext();
        IPage<PurchaseQuotationResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    /**
     * 批量 增加
     *
     * @param param
     */
    @Override
    @Transactional
    public void batchAdd(QuotationParam param) {
        List<PurchaseQuotationParam> params = param.getQuotationParams();

        List<PurchaseQuotation> purchaseQuotations = new ArrayList<>();


        for (PurchaseQuotationParam purchaseQuotationParam : params) {
            if (ToolUtil.isEmpty(purchaseQuotationParam.getCustomerId())) {
                throw new ServiceException(500, "请添加供应商");
            }
            judgeSupplier(purchaseQuotationParam.getCustomerId());
            PurchaseQuotation purchaseQuotation = new PurchaseQuotation();
            ToolUtil.copyProperties(purchaseQuotationParam, purchaseQuotation);
            purchaseQuotations.add(purchaseQuotation);

        }
        this.saveBatch(purchaseQuotations);
        //回填供应商绑定供应物料表  如果有新物料供应  则新增
        List<Long> customerIds = new ArrayList<>();
        for (PurchaseQuotation purchaseQuotation : purchaseQuotations) {
            customerIds.add(purchaseQuotation.getCustomerId());
        }
//        List<Customer> customerList = customerService.query().in("customer_id", customerIds).eq("display", 1).list();
        customerService.list(new QueryWrapper<Customer>(){{
            eq("display",1);
        }});
        List<Supply> supplyList =    supplyService.query().in("customer_id",customerIds).eq("display", 1).list();
        List<Supply> saveEntity = new ArrayList<>();
        for (PurchaseQuotation purchaseQuotation : purchaseQuotations) {
            if (supplyList.stream().noneMatch(supply -> supply.getCustomerId().equals(purchaseQuotation.getCustomerId()) && supply.getSkuId().equals(purchaseQuotation.getSkuId()))) {
                Supply supply = new Supply();
                supply.setCustomerId(purchaseQuotation.getCustomerId());
                supply.setSkuId(purchaseQuotation.getSkuId());
                saveEntity.add(supply);
            }
        }
        this.supplyService.saveBatch(saveEntity);

    }

    /**
     * 判断是否是供应商
     */
    private void judgeSupplier(Long id) {
        Customer supplier = customerService.getById(id);
        if (ToolUtil.isEmpty(supplier)) {
            throw new ServiceException(500, "当前供应商不存在");
        }
        if (supplier.getSupply() != 1) {
            throw new ServiceException(500, "请选择正确供应商");
        }
    }


    @Override
    public void addList(QuotationParam param) {
        switch (param.getSource()) {
            case "toBuyPlan":
                break;
            case "purchasePlan":
                ProcurementPlan plan = planService.getById(param.getSourceId());
                if (ToolUtil.isEmpty(plan)) {
                    throw new ServiceException(500, "采购计划不存在");
                }
                break;
            case "inquiryTask":
                InquiryTask inquiryTask = taskService.getById(param.getSourceId());
                if (ToolUtil.isEmpty(inquiryTask)) {
                    throw new ServiceException(500, "当前询价任务不存在");
                }
                Customer customer = customerService.getById(param.getCustomerId());
                Boolean level = judgeLevel(inquiryTask.getSupplierLevel(), customer);
                if (!level) {
                    throw new ServiceException(500, "当前供应商等级不够");
                }
                break;

            default:
                throw new ServiceException(500, "请确定方式");
        }

        Customer customer = customerService.getById(param.getCustomerId());
        if (ToolUtil.isEmpty(customer) && customer.getSupply() != 1) {
            throw new ServiceException(500, "请选择供应商");
        }

        List<Supply> supplies = supplyService.query().eq("customer_id", customer.getCustomerId()).list();
        //过滤相同sku
        HashSet<Long> skuSst = new HashSet<>();
        for (Supply supply : supplies) {
            skuSst.add(supply.getSkuId());
        }

        List<PurchaseQuotation> quotations = new ArrayList<>();

        for (PurchaseQuotationParam quotationParam : param.getQuotationParams()) {
            PurchaseQuotation quotation = new PurchaseQuotation();
            ToolUtil.copyProperties(quotationParam, quotation);
            quotation.setSource(param.getSource());
            quotation.setSourceId(param.getSourceId());
            quotations.add(quotation);
            quotation.setCustomerId(customer.getCustomerId());
            skuSst.add(quotationParam.getSkuId());
        }

        supplyService.remove(new QueryWrapper<Supply>() {{
            eq("customer_id", customer.getCustomerId());
        }});
        //添加供应商绑定物料
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
        //
        List<PurchaseQuotation> quotationList = this.listByIds(ids);
        List<PurchaseQuotationResult> results = new ArrayList<>();
        List<Long> skuIds = new ArrayList<>();
        for (PurchaseQuotation quotation : quotationList) {
            PurchaseQuotationResult quotationResult = new PurchaseQuotationResult();
            ToolUtil.copyProperties(quotation, quotationResult);
            results.add(quotationResult);
            skuIds.add(quotation.getSkuId());
        }


        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);
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
        if (ToolUtil.isEmpty(customer) && customer.getSupply() != 1) {
            throw new ServiceException(500, "当前供应商不存在");
        }

        List<SupplyResult> supplyResults = supplyService.getListByCustomerId(customer.getCustomerId());

        List<Long> skuIds = new ArrayList<>();
        for (SupplyResult supply : supplyResults) {
            skuIds.add(supply.getSkuId());
        }
        List<PurchaseQuotation> quotations = skuIds.size() == 0 ? new ArrayList<>() : this.query().in("sku_id", skuIds).list();

        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);

        List<PurchaseQuotationResult> quotationResults = new ArrayList<>();

        skuIds.clear();

        for (PurchaseQuotation quotation : quotations) {
            PurchaseQuotationResult quotationResult = new PurchaseQuotationResult();
            ToolUtil.copyProperties(quotation, quotationResult);
            quotationResults.add(quotationResult);
            skuIds.add(quotation.getSkuId());
        }


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

    @Override
    public List<PurchaseQuotationResult> getListBySource(Long sourceId) {
        List<PurchaseQuotation> quotations = this.query().eq("source_id", sourceId).list();

        List<PurchaseQuotationResult> quotationResults = new ArrayList<>();
        List<Long> customerIds = new ArrayList<>();
        List<Long> skuIds = new ArrayList<>();

        for (PurchaseQuotation quotation : quotations) {
            PurchaseQuotationResult quotationResult = new PurchaseQuotationResult();
            ToolUtil.copyProperties(quotation, quotationResult);
            quotationResults.add(quotationResult);
            customerIds.add(quotation.getCustomerId());
            skuIds.add(quotation.getSkuId());
        }

        List<Customer> customerList = customerIds.size() == 0 ? new ArrayList<>() : customerService.listByIds(customerIds);
        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);
        List<CustomerResult> customerResults = new ArrayList<>();

        for (Customer customer : customerList) {
            CustomerResult customerResult = new CustomerResult();
            ToolUtil.copyProperties(customer, customerResult);
            customerResults.add(customerResult);
        }

        for (PurchaseQuotationResult quotationResult : quotationResults) {
            for (Customer customer : customerList) {
                if (customer.getCustomerId().equals(quotationResult.getCustomerId())) {
                    CustomerResult customerResult = new CustomerResult();
                    ToolUtil.copyProperties(customer, customerResult);
                    quotationResult.setCustomerResult(customerResult);
                    break;
                }
            }
            for (SkuResult skuResult : skuResults) {
                if (skuResult.getSkuId().equals(quotationResult.getSkuId())) {
                    quotationResult.setSkuResult(skuResult);
                    break;
                }
            }
        }


        return quotationResults;
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
        List<Long> userIds = new ArrayList<>();
        List<Long> customerIds = new ArrayList<>();

        for (PurchaseQuotationResult datum : data) {
            skuIds.add(datum.getSkuId());
            userIds.add(datum.getCreateUser());
            customerIds.add(datum.getCustomerId());
        }

        List<User> users = userIds.size() == 0 ? new ArrayList<>() : userService.listByIds(userIds);
        List<Customer> customers = customerIds.size() == 0 ? new ArrayList<>() : customerService.listByIds(customerIds);
        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);

        for (PurchaseQuotationResult datum : data) {
            for (SkuResult skuResult : skuResults) {
                if (skuResult.getSkuId().equals(datum.getSkuId())) {
                    datum.setSkuResult(skuResult);
                    break;
                }
            }
            for (User user : users) {
                if (user.getUserId().equals(datum.getCreateUser())) {
                    datum.setUser(user);
                    break;
                }
            }
            for (Customer customer : customers) {
                if (ToolUtil.isNotEmpty(datum.getCustomerId()) && customer.getCustomerId().equals(datum.getCustomerId())) {
                    CustomerResult customerResult = new CustomerResult();
                    ToolUtil.copyProperties(customer, customerResult);
                    datum.setCustomerResult(customerResult);
                    break;
                }
            }
        }
    }

    private Boolean judgeLevel(Long levelId, Customer customer) {
        if (ToolUtil.isEmpty(customer.getCustomerLevelId())) {
            throw new ServiceException(500, "请先设置供应商等级");
        }
        CrmCustomerLevel level = levelService.getById(levelId);
        if (ToolUtil.isEmpty(level)) {
            throw new ServiceException(500, "当前供应商等级不合法");
        }
        List<CrmCustomerLevel> customerLevels = levelService.list();

        List<Long> levelIds = new ArrayList<>();

        for (CrmCustomerLevel customerLevel : customerLevels) {
            if (level.getRank() <= customerLevel.getRank()) {
                levelIds.add(customerLevel.getCustomerLevelId());
            }
        }
        for (Long id : levelIds) {
            if (customer.getCustomerLevelId().equals(id)) {
                return true;
            }
        }
        return false;
    }
}
