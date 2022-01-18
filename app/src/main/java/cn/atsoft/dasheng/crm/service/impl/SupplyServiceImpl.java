package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.BrandParam;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.ContactsBind;
import cn.atsoft.dasheng.crm.entity.Supply;
import cn.atsoft.dasheng.crm.mapper.SupplyMapper;
import cn.atsoft.dasheng.crm.model.params.SupplyParam;
import cn.atsoft.dasheng.crm.service.ContactsBindService;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.entity.SkuBrandBind;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.crm.model.result.SupplyResult;
import cn.atsoft.dasheng.erp.service.SkuBrandBindService;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.crm.service.SupplyService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.purchase.entity.PurchaseQuotation;
import cn.atsoft.dasheng.purchase.service.PurchaseQuotationService;
import cn.atsoft.dasheng.supplier.entity.SupplierBrand;
import cn.atsoft.dasheng.supplier.service.SupplierBrandService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private SupplierBrandService supplierBrandService;
    @Autowired
    private ContactsService contactsService;
    @Autowired
    private PhoneService phoneService;
    @Autowired
    private AdressService adressService;
    @Autowired
    private SkuBrandBindService brandBindService;

    @Override
    public void add(SupplyParam param) {
        Supply entity = getEntity(param);
        this.save(entity);
        List<SkuBrandBind> skuBrandBinds = brandBindService.query().eq("sku_id", param.getSkuId()).list();
        List<SkuBrandBind> skuBrandBindList = new ArrayList<>();
        for (BrandParam brandParam : param.getBrandParams()) {
            if (skuBrandBinds.stream().noneMatch(i -> i.getBrandId().equals(brandParam.getBrandId()) && i.getSkuId().equals(param.getSkuId()))) {
                SkuBrandBind skuBrandBind = new SkuBrandBind();
                skuBrandBind.setSkuId(param.getSkuId());
                skuBrandBind.setBrandId(brandParam.getBrandId());
                skuBrandBindList.add(skuBrandBind);
            }
        }
        brandBindService.saveBatch(skuBrandBindList);
    }

    /**
     * 当前物料下所有的供应商
     *
     * @param skuId
     * @return
     */
    @Override
    public List<CustomerResult> getSupplierBySku(Long skuId) {
        if (ToolUtil.isEmpty(skuId)) {
            throw new ServiceException(500, "请选择物料");
        }

        List<Supply> supplies = this.query().eq("sku_id", skuId).list();
        List<Long> custoemrIds = new ArrayList<>();
        for (Supply supply : supplies) {
            custoemrIds.add(supply.getCustomerId());
        }

        List<CustomerResult> results = customerService.getResults(custoemrIds);
        supplierBrandService.getBrand(results);
        return results;
    }


    @Override
    public void delete(SupplyParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(SupplyParam param) {
        Supply oldEntity = getOldEntity(param);
        Supply newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public SupplyResult findBySpec(SupplyParam param) {
        return null;
    }

    @Override
    public List<SupplyResult> findListBySpec(SupplyParam param) {
        return null;
    }

    @Override
    public PageInfo<SupplyResult> findPageBySpec(SupplyParam param) {
        Page<SupplyResult> pageContext = getPageContext();
        IPage<SupplyResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    /**
     * 批量增加供应物
     *
     * @param supplyParams
     */
    @Override
    public void addList(List<SupplyParam> supplyParams, Long customerId) {
        if (ToolUtil.isNotEmpty(supplyParams)) {
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
    public List<CustomerResult> getSupplyByLevel(Long levelId) {
        List<CrmCustomerLevel> levels = levelService.list();
        CrmCustomerLevel level = levelService.getById(levelId);
        //比较等级
        List<Long> levelIds = new ArrayList<>();
        for (CrmCustomerLevel crmCustomerLevel : levels) {
            if (ToolUtil.isNotEmpty(level)) {
                if (level.getRank() <= crmCustomerLevel.getRank()) {
                    levelIds.add(crmCustomerLevel.getCustomerLevelId());
                }
            } else {
                levelIds.add(crmCustomerLevel.getCustomerLevelId());
            }

        }

        //达到级别的供应商
        List<Customer> customers = customerService.query().eq("supply", 1).in("customer_level_id", levelIds).list();
        List<Long> customerIds = new ArrayList<>();
        List<CustomerResult> customerResults = new ArrayList<>();

        for (Customer customer : customers) {
            customerIds.add(customer.getCustomerId());
            CustomerResult customerResult = new CustomerResult();
            ToolUtil.copyProperties(customer, customerResult);
            customerResults.add(customerResult);
        }
        List<Supply> supplies = customerIds.size() == 0 ? new ArrayList<>() : this.query().in("customer_id", customerIds).list();

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

    @Override
    public List<CustomerResult> getSupplyBySku(List<Long> skuIds, Long supplierLevel) {

        List<Supply> supplies = skuIds.size() == 0 ? new ArrayList<>() : this.query().in("sku_id", skuIds).list();  //查询物料供应商对应关系

        List<Long> customerIds = new ArrayList<>();
        for (Supply supply : supplies) {
            customerIds.add(supply.getCustomerId());
        }

        List<CustomerResult> customerResults = customerService.getResults(customerIds);

        List<CustomerResult> levelCustomerResult = new ArrayList<>();  //过滤等级
        for (CustomerResult customerResult : customerResults) {
            if (ToolUtil.isNotEmpty(customerResult.getLevel()) && customerResult.getLevel().getRank() >= supplierLevel) {
                levelCustomerResult.add(customerResult);
            }
        }
        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);


        //取供应商的供应的物料
        Map<Long, List<SkuResult>> map = new HashMap<>();
        for (Supply supply : supplies) {
            List<SkuResult> skuResultList = new ArrayList<>();
            for (SkuResult skuResult : skuResults) {
                if (skuResult.getSkuId().equals(supply.getSkuId())) {
                    skuResultList.add(skuResult);
                }
            }
            map.put(supply.getCustomerId(), skuResultList);
        }

        List<Long> contactsIds = new ArrayList<>();
        List<Long> adressIds = new ArrayList<>();
        for (CustomerResult customerResult : levelCustomerResult) {
            List<SkuResult> skuResultList = map.get(customerResult.getCustomerId());
            customerResult.setSkuResultList(skuResultList);
            contactsIds.add(customerResult.getDefaultContacts());
            adressIds.add(customerResult.getDefaultAddress());
        }

        List<Contacts> contacts = contactsIds.size() == 0 ? new ArrayList<>() : contactsService.lambdaQuery()
                .in(Contacts::getContactsId, contactsIds).eq(Contacts::getDisplay, 1).list();

        List<Adress> adresses = adressService.lambdaQuery().in(Adress::getAdressId, adressIds).eq(Adress::getDisplay, 1).list();

        for (CustomerResult customerResult : levelCustomerResult) {

            for (Contacts contact : contacts) {
                if (ToolUtil.isNotEmpty(customerResult.getDefaultContacts())
                        && customerResult.getDefaultContacts()
                        .equals(contact.getContactsId())) {
                    Phone phone = phoneService.lambdaQuery().eq(Phone::getContactsId, contact.getContactsId()).one();
                    customerResult.setPhone(phone);
                    customerResult.setContact(contact);

                }
            }
            for (Adress adress : adresses) {
                if (ToolUtil.isNotEmpty(customerResult.getDefaultAddress()) && customerResult.getDefaultAddress().equals(adress.getAdressId())) {
                    customerResult.setAddress(adress);
                }
            }

        }

        return levelCustomerResult;
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
        for (SupplyResult datum : data) {
            skuIds.add(datum.getSkuId());
        }
        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);
        for (SupplyResult datum : data) {
            for (SkuResult skuResult : skuResults) {
                if (datum.getSkuId().equals(skuResult.getSkuId())) {
                    datum.setSkuResult(skuResult);
                }
            }
        }

    }
}
