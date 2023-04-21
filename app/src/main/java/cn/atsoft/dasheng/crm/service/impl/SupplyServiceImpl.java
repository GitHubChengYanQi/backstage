package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.BrandParam;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.crm.entity.Supply;
import cn.atsoft.dasheng.crm.mapper.SupplyMapper;
import cn.atsoft.dasheng.crm.model.params.OrderDetailParam;
import cn.atsoft.dasheng.crm.model.params.SupplyParam;
import cn.atsoft.dasheng.erp.entity.SkuBrandBind;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.crm.model.result.SupplyResult;
import cn.atsoft.dasheng.erp.service.SkuBrandBindService;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.crm.service.SupplyService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.purchase.entity.PurchaseListing;
import cn.atsoft.dasheng.purchase.service.PurchaseListingService;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 供应商供应物料 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-12-20
 */
@Service
public class SupplyServiceImpl extends ServiceImpl<SupplyMapper, Supply> implements SupplyService {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private CrmCustomerLevelService levelService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private StockDetailsService stockDetailsService;
    @Autowired
    private PurchaseListingService listingService;

    @Autowired
    private SkuBrandBindService skuBrandBindService;

    @Override
    @Transactional
    public void add(SupplyParam param) {
        // 通过 传过来的 brandIds 和 skuId 找到对应的集合
        List<SkuBrandBind> brandBindList = param.getBrandIds().size() == 0 ? new ArrayList<>() : skuBrandBindService.query().in("brand_id", param.getBrandIds()).eq("sku_id", param.getSkuId()).eq("display", 1).list();
        List<SkuBrandBind> skuBrandBindList = new ArrayList<>();
        //循环遍历 传过来的BrandIds
        for (Long brandId : param.getBrandIds()) {
            //如果 brandBindList 不包含 循环 传过来的BrandIds 那就新增
            if (brandBindList.stream().noneMatch(i -> i.getBrandId().equals(brandId))) {
                // 定义 skuBrandBindEntity 实体
                SkuBrandBind skuBrandBindEntity = new SkuBrandBind();
                skuBrandBindEntity.setBrandId(brandId);
                skuBrandBindEntity.setSkuId(param.getSkuId());
                //把实体加入到 skuBrandBindList 集合里面
                skuBrandBindList.add(skuBrandBindEntity);
            }
        }
        //把 skuBrandBindList 保存
        skuBrandBindService.saveBatch(skuBrandBindList);
        //把 skuBrandBind 添加到 brandBindList
        brandBindList.addAll(skuBrandBindList);

        //定义一个 skuBrandBindIds
        List<Long> skuBrandBindIds = new ArrayList<>();
        // 循环遍历 brandBindList
        for (SkuBrandBind skuBrandBind : brandBindList) {
            //把 brandBindList得到的 skuBrandBind 集合加入到定义的 skuBrandBindIds
            skuBrandBindIds.add(skuBrandBind.getSkuBrandBind());
        }

        List<Supply> supplyList = skuBrandBindIds.size() == 0 ? new ArrayList<>() : this.query().in("sku_brand_bind", skuBrandBindIds).eq("customer_id", param.getCustomerId()).eq("display", 1).list();
        List<Supply> supply = new ArrayList<>();

        // 循环遍历 brandBindList
        for (SkuBrandBind skuBrandBind : brandBindList) {
            //如果 supplyList 不包含 skuBrandBind  则新增
            if (supplyList.stream().noneMatch(i -> i.getSkuBrandBind().equals(skuBrandBind.getSkuBrandBind()))) {
                //定义一个 supplyEntity
                Supply supplyEntity = new Supply();
                supplyEntity.setSkuBrandBind(skuBrandBind.getSkuBrandBind());
                supplyEntity.setSkuId(skuBrandBind.getSkuId());
                supplyEntity.setCustomerId(param.getCustomerId());
                supplyEntity.setBrandId(skuBrandBind.getBrandId());
                //把 supplyEntity 加入到 supply
                supply.add(supplyEntity);
            }
        }
        this.saveBatch(supply);
    }


    @Override
    public void delete(SupplyParam param) {
        Supply entity = getOldEntity(param);
        entity.setDisplay(0);
        this.updateById(entity);

    }

    @Override
    public void update(SupplyParam param) {
        Supply oldEntity = getOldEntity(param);
        Supply newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    /**
     * 修改绑定
     *
     * @param param
     */
    @Override
    @Transactional
    public void updateBind(SupplyParam param) {
        this.removeByIds(param.getSupplyIds());
        param.setSupplyId(null);
        add(param);
    }

    @Override

    public SupplyResult findBySpec(SupplyParam param) {
        return null;
    }

    @Override
    public List<SupplyResult> findListBySpec(SupplyParam param) {
        List<SupplyResult> supplyResults = this.baseMapper.customList(param);
        format(supplyResults);
        return supplyResults;
    }


    @Override
    public PageInfo<SupplyResult> findPageBySpec(SupplyParam param) {
        Page<SupplyResult> pageContext = getPageContext();
        IPage<SupplyResult> page = new Page<>();
        if (LoginContextHolder.getContext().isAdmin()) {
            page = this.baseMapper.customPageList(pageContext,param,null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope(),LoginContextHolder.getContext().getTenantId());
            page = this.baseMapper.customPageList(pageContext, param,dataScope);
        }
        format(page.getRecords());
        page.getRecords().removeIf(i -> ToolUtil.isEmpty(i.getSkuResult()));
        return PageFactory.createPageInfo(page);
    }

    /**
     * 批量增加供应物
     *
     * @param supplyParams
     */
    @Override
    public void addList(List<SupplyParam> supplyParams, Long customerId) {
        if (ToolUtil.isEmpty(supplyParams)) {
            throw new ServiceException(500, "请添加供应物料");
        }
        Customer customer = customerService.getById(customerId);
        if (ToolUtil.isEmpty(customer)) {
            throw new ServiceException(500, "客户错误");
        }
        if (customer.getSupply() != 1) {
            throw new ServiceException(500, "当前客户不是供应商");
        }
        List<Supply> supplies = new ArrayList<>();
        for (SupplyParam supplyParam : supplyParams) {
            supplyParam.setCustomerId(customerId);
            Supply supply = new Supply();
            ToolUtil.copyProperties(supplyParam, supply);
            supplies.add(supply);
        }
        this.saveBatch(supplies);
    }

    @Override
    public void customerAdd(List<SupplyParam> supplyParams, Long customerId) {
        List<Supply> supplies = new ArrayList<>();
        for (SupplyParam supplyParam : supplyParams) {
            for (BrandParam brandParam : supplyParam.getBrandParams()) {
                supplyParam.setCustomerId(customerId);
                supplyParam.setBrandId(brandParam.getBrandId());
                supplyParam.setSkuId(supplyParam.getSkuId());
                Supply supply = new Supply();
                ToolUtil.copyProperties(supplyParam, supply);
                supplies.add(supply);
            }
        }
        this.saveBatch(supplies);
    }


    @Override
    public List<SupplyResult> detail(Long customerId) {
        List<Supply> supplies = this.query().eq("customer_id", customerId).list();

        List<SupplyResult> supplyResults = new ArrayList<>();
        for (Supply supply : supplies) {
            SupplyResult supplyResult = new SupplyResult();
            ToolUtil.copyProperties(supply, supplyResult);
            supplyResults.add(supplyResult);
        }
        format(supplyResults);
        return supplyResults;
    }

    @Override
    public List<SupplyResult> getListByCustomerId(Long customerId) {
        List<SupplyResult> supplyResults = new ArrayList<>();
        if (ToolUtil.isNotEmpty(customerId)) {
            return supplyResults;
        }
        List<Supply> supplies = this.query().eq("customer_id", customerId).list();


        for (Supply supply : supplies) {
            SupplyResult supplyResult = new SupplyResult();
            ToolUtil.copyProperties(supply, supplyResult);
            supplyResults.add(supplyResult);
        }
        return supplyResults;
    }

    /**
     * 通过等级获取供应商
     *
     * @param levelId
     * @return
     */
    @Override
    public List<CustomerResult> getSupplyByLevel(Long levelId, List<Long> skuIds) {
        List<CrmCustomerLevel> levels = levelService.list();
        CrmCustomerLevel level = levelService.getById(levelId);
        //比较等级
        List<Long> levelIds = new ArrayList<>();

        if (ToolUtil.isEmpty(level)) {  //没有等级 全取
            for (CrmCustomerLevel crmCustomerLevel : levels) {
                levelIds.add(crmCustomerLevel.getCustomerLevelId());
            }
        } else {   //有等级 取大于等于
            for (CrmCustomerLevel crmCustomerLevel : levels) {
                if (level.getRank() <= crmCustomerLevel.getRank()) {
                    levelIds.add(crmCustomerLevel.getCustomerLevelId());
                }
            }
        }

        QueryWrapper<Customer> customerQueryWrapper = new QueryWrapper<>();
        customerQueryWrapper.eq("supply", 1);
        customerQueryWrapper.in("customer_level_id", levelIds);

        // sku条件查询
        if (ToolUtil.isNotEmpty(skuIds)) {
            List<Supply> supplies = this.query().in("sku_id", skuIds).eq("display", 1).list();
            List<Long> supplierIds = new ArrayList<>();
            for (Supply supply : supplies) {
                supplierIds.add(supply.getCustomerId());
            }
            if (supplierIds.size() > 0) {
                customerQueryWrapper.in("customer_id", supplierIds);
            }

        }

        //达到级别的供应商
        List<Customer> customers = customerService.list(customerQueryWrapper);
        List<Long> customerIds = new ArrayList<>();
        List<CustomerResult> customerResults = new ArrayList<>();

        for (Customer customer : customers) {
            customerIds.add(customer.getCustomerId());
            CustomerResult customerResult = new CustomerResult();
            ToolUtil.copyProperties(customer, customerResult);
            customerResults.add(customerResult);
        }
        List<Supply> supplies = customerIds.size() == 0 ? new ArrayList<>() : this.query().in("customer_id", customerIds).eq("display", 1).list();

        //组合数据
        for (CustomerResult customerResult : customerResults) {
            List<SupplyResult> supplyResults = new ArrayList<>();
            for (Supply supply : supplies) {
                if (supply.getCustomerId().equals(customerResult.getCustomerId())) {
                    SupplyResult supplyResult = new SupplyResult();
                    ToolUtil.copyProperties(supply, supplyResult);
                    supplyResults.add(supplyResult);
                }
            }
            customerResult.setSupplyResults(supplyResults);
        }
        return customerResults;
    }

    /**
     * 查询供应商
     *
     * @param skuId
     * @return
     */
    @Override
    public List<CustomerResult> getCustomerBySku(Long skuId) {
        if (ToolUtil.isEmpty(skuId)) {
            throw new ServiceException(500, "请确定物料");
        }
        List<Supply> supplyList = this.query().eq("sku_id", skuId).eq("display", 1).list();
        Set<Long> customerIds = new HashSet<>();
        for (Supply supply : supplyList) {
            customerIds.add(supply.getCustomerId());
        }
        return customerService.getResults(new ArrayList<>(customerIds));
    }

    @Override
    public List<CustomerResult> getSupplyBySku(List<Long> skuIds, Long supplierLevel) {
        if (ToolUtil.isEmpty(supplierLevel)) {
            throw new ServiceException(500, "请提交等级");
        }
        CrmCustomerLevel level = levelService.getById(supplierLevel);   //当前等级

        if (ToolUtil.isEmpty(level)) {
            throw new ServiceException(500, "请确定等级");
        }

        List<SupplyResult> supplyBySku = getSupplyBySku(skuIds);//查询物料供应商对应关系

        List<Long> customerIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();

        for (SupplyResult supply : supplyBySku) {
            customerIds.add(supply.getCustomerId());
            brandIds.add(supply.getBrandId());
        }

        List<CustomerResult> customerResults = customerService.getResults(customerIds);
        List<CustomerResult> levelCustomerResult = new ArrayList<>();  //取等级合格的供应商
        for (CustomerResult customerResult : customerResults) {
            if (ToolUtil.isNotEmpty(customerResult.getLevel()) && customerResult.getLevel().getRank() >= level.getRank()) {
                levelCustomerResult.add(customerResult);
            }
        }

        List<BrandResult> brandResults = brandService.getBrandResults(brandIds);
        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);

        //组合
        for (SupplyResult supply : supplyBySku) {
            for (SkuResult skuResult : skuResults) {
                if (skuResult.getSkuId().equals(supply.getSkuId())) {
                    supply.setSkuResult(skuResult);
                    break;
                }
            }
            for (BrandResult brandResult : brandResults) {
                if (brandResult.getBrandId().equals(supply.getBrandId())) {
                    supply.setBrandResult(brandResult);
                    break;
                }
            }
        }

        for (CustomerResult customerResult : levelCustomerResult) {
            List<SupplyResult> supplyResults = new ArrayList<>();
            for (SupplyResult supplyResult : supplyBySku) {
                if (supplyResult.getCustomerId().equals(customerResult.getCustomerId())) {
                    supplyResults.add(supplyResult);
                }
            }
            customerResult.setSupplyResults(supplyResults);
        }

        return levelCustomerResult;
    }

    /**
     * 通过sku查询对应关系
     *
     * @param skuIds
     * @return
     */
    public List<SupplyResult> getSupplyBySku(List<Long> skuIds) {
        List<Supply> supplies = skuIds.size() == 0 ? new ArrayList<>() : this.query().in("sku_id", skuIds).list();  //查询物料供应商对应关系

        List<SupplyResult> supplyResults = new ArrayList<>();
        for (Supply supply : supplies) {
            SupplyResult supplyResult = new SupplyResult();
            ToolUtil.copyProperties(supply, supplyResult);
            supplyResults.add(supplyResult);
        }
        return supplyResults;
    }

    @Override
    public List<SupplyResult> getSupplyBySupplierId(Long supplierId) {
        if (ToolUtil.isEmpty(supplierId)) {
            return new ArrayList<>();
        }
        List<Supply> supplies = this.query().eq("customer_id", supplierId).eq("display", 1).list();
        if (ToolUtil.isEmpty(supplies)) {
            return new ArrayList<>();
        }
        List<Long> skuIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        List<SupplyResult> supplyResults = new ArrayList<>();

        for (Supply supply : supplies) {
            SupplyResult supplyResult = new SupplyResult();
            ToolUtil.copyProperties(supply, supplyResult);
            supplyResults.add(supplyResult);
            skuIds.add(supply.getSkuId());
            brandIds.add(supply.getBrandId());
        }

        List<BrandResult> brandResults = brandService.getBrandResults(brandIds);
        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);

        for (SupplyResult supplyResult : supplyResults) {

            for (BrandResult brandResult : brandResults) {
                if (ToolUtil.isNotEmpty(supplyResult.getBrandId()) && supplyResult.getBrandId().equals(brandResult.getBrandId())) {
                    supplyResult.setBrandResult(brandResult);
                    break;
                }
            }
            for (SkuResult skuResult : skuResults) {
                if (skuResult.getSkuId().equals(supplyResult.getSkuId())) {
                    supplyResult.setSkuResult(skuResult);
                    break;
                }
            }

        }
        return supplyResults;
    }

    private Serializable getKey(SupplyParam param) {
        return param.getSupplyId();
    }

    private Page<SupplyResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Supply getOldEntity(SupplyParam param) {
        return this.getById(getKey(param));
    }

    private Supply getEntity(SupplyParam param) {
        Supply entity = new Supply();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    private void format(List<SupplyResult> data) {
        List<Long> skuIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        List<Long> customerIds = new ArrayList<>();
        for (SupplyResult datum : data) {
            skuIds.add(datum.getSkuId());
            brandIds.add(datum.getBrandId());
            customerIds.add(datum.getCustomerId());
        }
        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);
        List<BrandResult> brandResults = brandService.getBrandResults(brandIds);
        List<CustomerResult> customerResults = customerService.getResults(customerIds);
        List<StockDetails> stockDetails = stockDetailsService.list();
        List<PurchaseListing> purchaseListings = listingService.query().ne("status", 99).list();


        for (SupplyResult datum : data) {
            for (SkuResult skuResult : skuResults) {
                if (datum.getSkuId().equals(skuResult.getSkuId())) {
                    datum.setSkuResult(skuResult);
                }
            }

            for (BrandResult brandResult : brandResults) {
                if (brandResult.getBrandId().equals(datum.getBrandId())) {
                    datum.setBrandResult(brandResult);
                }
            }

            for (CustomerResult customerResult : customerResults) {
                if (customerResult.getCustomerId().equals(datum.getCustomerId())) {
                    datum.setCustomerResult(customerResult);
                }
            }
            long stockNumber = 0L;
            long applyNumber = 0L;
            for (StockDetails stockDetail : stockDetails) {
                if (ToolUtil.isNotEmpty(stockDetail.getBrandId()) && stockDetail.getSkuId().equals(datum.getSkuId()) && stockDetail.getBrandId().equals(datum.getBrandId())) {
                    stockNumber = stockNumber + stockDetail.getNumber();
                }
            }
            for (PurchaseListing purchaseListing : purchaseListings) {
                if (purchaseListing.getSkuId().equals(datum.getSkuId()) && purchaseListing.getBrandId().equals(datum.getBrandId())) {
                    applyNumber = applyNumber + purchaseListing.getApplyNumber();
                }
            }
            datum.setStockNumber(stockNumber);
            datum.setApplyNumber(applyNumber);
        }
        data.removeIf(i -> ToolUtil.isEmpty(i.getCustomerResult()));

    }

    /**
     * 根据供应商ids 查询绑定物料品牌返回
     *
     * @param customerIds
     * @return
     **/

    @Override
    public List<SupplyResult> getSupplyByCustomerIds(List<Long> customerIds) {
        if (ToolUtil.isEmpty(customerIds)) {
            return null;
        }
        List<Supply> supplies = this.query().in("customer_id", customerIds).eq("display", 1).list();
        List<Long> skuIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        for (Supply supply : supplies) {
            skuIds.add(supply.getSkuId());
            brandIds.add(supply.getBrandId());
        }
        List<SkuResult> skuResults = skuIds.size() == 0 ? new ArrayList<>() : skuService.formatSkuResult(skuIds);
        List<BrandResult> brandResults = brandIds.size() == 0 ? new ArrayList<>() : brandService.getBrandResults(brandIds);
        List<SupplyResult> results = new ArrayList<>();
        for (Supply supply : supplies) {
            SupplyResult supplyResult = new SupplyResult();
            ToolUtil.copyProperties(supply, supplyResult);
            for (SkuResult skuResult : skuResults) {
                if (supplyResult.getSkuId().equals(skuResult.getSkuId())) {
                    supplyResult.setSkuResult(skuResult);
                }
            }
            for (BrandResult brandResult : brandResults) {
                if (supplyResult.getBrandId().equals(brandResult.getBrandId())) {
                    supplyResult.setBrandResult(brandResult);
                }
            }
            results.add(supplyResult);
        }
        return results;
    }

    /**
     * 订单回填绑定
     *
     * @param customerId
     * @param params
     */
    @Override
    public void OrdersBackFill(Long customerId, List<OrderDetailParam> params) {
        List<Supply> supplies = this.list();
        if (ToolUtil.isEmpty(params)) {
            return;
        }
        for (OrderDetailParam param : params) {
            boolean judge = judge(customerId, param, supplies);
            if (judge) {
                Supply supply = new Supply();
                supply.setCustomerId(customerId);
                supply.setBrandId(param.getBrandId());
                supply.setSkuId(param.getSkuId());
                this.save(supply);
                supplies.add(supply);
            }
        }

    }

    private boolean judge(Long customerId, OrderDetailParam param, List<Supply> supplies) {
        for (Supply supply : supplies) {
            if (param.getSkuId().equals(supply.getSkuId()) && supply.getCustomerId().equals(customerId) && supply.getBrandId().equals(param.getBrandId())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 通过物料查询供应商
     *
     * @param skuIds
     * @return
     */
    @Override
    public List<CustomerResult> getCustomerBySkuIds(List<Long> skuIds) {


        List<Supply> supplies = skuIds.size() == 0 ? new ArrayList<>() : this.query().in("sku_id", skuIds).eq("display", 1).list();
        List<Long> customerIds = new ArrayList<>();
        for (Supply supply : supplies) {
            customerIds.add(supply.getCustomerId());
        }
        List<Customer> customerList = customerIds.size() == 0 ? new ArrayList<>() : customerService.listByIds(customerIds);

        return BeanUtil.copyToList(customerList, CustomerResult.class, new CopyOptions());
    }

    /**
     * 通过物料查询供应商
     *
     * @param skuIds
     * @return
     */
    @Override
    public List<SupplyResult> listByPurchase(SupplyParam param) {
        List<SupplyResult> supplyResultPage = this.baseMapper.listByPurchase( param);
        this.format(supplyResultPage);
        return supplyResultPage;
    }

    /**
     * 通过物料查询供应商
     *
     * @param skuIds
     * @return
     */
    @Override
    public List<SupplyResult> listBySkuIds(SupplyParam param) {
        List<SupplyResult> supplyResultPage = this.baseMapper.listBySkuIds(param);
        this.format(supplyResultPage);
        return supplyResultPage;
    }

    /**
     * 获取物料绑定的品牌
     *
     * @param skuIds
     * @return
     */
    @Override
    public List<BrandResult> getBrandBySkuIds(List<Long> skuIds) {

        List<Supply> supplies = skuIds.size() == 0 ? new ArrayList<>() : this.query().in("sku_id", skuIds).eq("display", 1).list();
        List<Long> brandIds = new ArrayList<>();
        for (Supply supply : supplies) {
            brandIds.add(supply.getBrandId());
        }
        return brandService.getBrandResults(brandIds);
    }
    @Override
    public Long getanyOneCustomerIdBySkuIdAndBrandId(Long skuId,Long brandId){
        List<SkuBrandBind> skuBrandBindList = skuBrandBindService.lambdaQuery().eq(SkuBrandBind::getSkuId, skuId).eq(SkuBrandBind::getBrandId, brandId).list();
        if (skuBrandBindList.size() == 0){
            return null ;
        }
        List<Long> skuBrandBinds = skuBrandBindList.stream().map(SkuBrandBind::getSkuBrandBind).distinct().collect(Collectors.toList());

        Supply supply = this.lambdaQuery().in(Supply::getSkuBrandBind, skuBrandBinds).orderByDesc(Supply::getCreateTime).last(" limit 0,1").one();
        if (ToolUtil.isEmpty(supply)){
            return null ;
        }
        return supply.getCustomerId();
    }
}
