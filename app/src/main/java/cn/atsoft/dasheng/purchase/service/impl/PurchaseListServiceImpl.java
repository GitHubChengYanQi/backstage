package cn.atsoft.dasheng.purchase.service.impl;


import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Supply;
import cn.atsoft.dasheng.crm.service.SupplyService;
import cn.atsoft.dasheng.erp.entity.SkuBrandBind;
import cn.atsoft.dasheng.erp.model.result.SkuListResult;
import cn.atsoft.dasheng.erp.service.SkuBrandBindService;
import cn.atsoft.dasheng.erp.service.SkuListService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.purchase.entity.PurchaseList;
import cn.atsoft.dasheng.purchase.mapper.PurchaseListMapper;
import cn.atsoft.dasheng.purchase.model.params.PurchaseListParam;
import cn.atsoft.dasheng.purchase.model.result.PurchaseListResult;
import cn.atsoft.dasheng.purchase.service.PurchaseListService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 预购管理 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-03-04
 */
@Service
public class PurchaseListServiceImpl extends ServiceImpl<PurchaseListMapper, PurchaseList> implements PurchaseListService {

    @Autowired
    private BrandService brandService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SkuListService skuListService;

    @Autowired
    private SkuBrandBindService skuBrandBindService;

    @Autowired
    private SupplyService supplyService;

    @Override
    public void add(PurchaseListParam param) {
        if (ToolUtil.isNotEmpty(param.getCustomerId())) {
            Customer customer = customerService.getById(param.getCustomerId());
            if (ToolUtil.isEmpty(customer)) {
                throw new ServiceException(500, "品牌数据错误");
            }
        }
        if (ToolUtil.isNotEmpty(param.getBrandId())) {
            Brand brand = brandService.getById(param.getBrandId());
            if (ToolUtil.isEmpty(brand)) {
                throw new ServiceException(500, "品牌数据错误");
            }
        }
        if (param.getNumber() <= 0) {
            throw new ServiceException(500, "数量需要大于0");
        }
        LambdaQueryChainWrapper<PurchaseList> wrapper = this.lambdaQuery();
        wrapper.eq(PurchaseList::getSkuId,param.getSkuId());
        wrapper.eq(PurchaseList::getStatus,0);
        if (ToolUtil.isEmpty(param.getBrandId())){
            wrapper.eq(PurchaseList::getBrandId,0);
        }else{
            wrapper.eq(PurchaseList::getBrandId,param.getBrandId());
        }

        if (ToolUtil.isEmpty(param.getCustomerId())){
            wrapper.isNull(PurchaseList::getCustomerId);
        }else {
            wrapper.eq(PurchaseList::getCustomerId,param.getCustomerId());
        }
        PurchaseList entity = wrapper.one();
        if (ToolUtil.isEmpty(entity)){
            entity = getEntity(param);
            this.save(entity);
        } else if (ToolUtil.isNotEmpty(entity) && (ToolUtil.isEmpty(param.getChecked()) || !param.getChecked())) {
            throw new ServiceException(1001,"已有此采购数据 是否追加数量?");

        } else if (ToolUtil.isNotEmpty(entity) && param.getChecked()){
            entity.setNumber(entity.getNumber()+param.getNumber());
            this.updateById(entity);
        }
    }

    @Override
    public void delete(PurchaseListParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(PurchaseListParam param) {
        PurchaseList oldEntity = getOldEntity(param);
        PurchaseList newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public PurchaseListResult findBySpec(PurchaseListParam param) {
        return null;
    }

    @Override
    public List<PurchaseListResult> findListBySpec(PurchaseListParam param) {
        List<PurchaseListResult> purchaseListResults = new ArrayList<>();
        if(ToolUtil.isEmpty(param.getCustomerId())){
            purchaseListResults = this.baseMapper.customList(param);
        }else {
            purchaseListResults = this.baseMapper.leftJoinList(param);
        }
        this.format(purchaseListResults);
        return purchaseListResults;
    }

    @Override
    public List<PurchaseListResult> listBySkuId(Long skuId) {
        List<PurchaseList> list = this.lambdaQuery().eq(PurchaseList::getSkuId, skuId).eq(PurchaseList::getStatus, 0).eq(PurchaseList::getDisplay, 1).list();
        List<PurchaseListResult> purchaseListResults = BeanUtil.copyToList(list,PurchaseListResult.class);
        this.format(purchaseListResults);
        return purchaseListResults;
    }

    @Override
    public PageInfo<PurchaseListResult> findPageBySpec(PurchaseListParam param) {
        Page<PurchaseListResult> pageContext = getPageContext();
        IPage<PurchaseListResult> page = this.baseMapper.customPageList(pageContext, param);
        this.format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    public void format(List<PurchaseListResult> dataList) {
        List<Long> customerIdList = dataList.stream().map(PurchaseListResult::getCustomerId).distinct().collect(Collectors.toList());
        List<Long> brandIdList = dataList.stream().map(PurchaseListResult::getBrandId).distinct().collect(Collectors.toList());
        List<Long> skuIdList = dataList.stream().map(PurchaseListResult::getSkuId).distinct().collect(Collectors.toList());

        List<SkuBrandBind> skuBrandBinds = skuIdList.size() == 0 ? new ArrayList<>() : skuBrandBindService.lambdaQuery().in(SkuBrandBind::getSkuId, skuIdList).list();

        List<Supply> supplyList = skuBrandBinds.size() == 0 ? new ArrayList<>() : supplyService.lambdaQuery().in(Supply::getSkuBrandBind, skuBrandBinds.stream().map(SkuBrandBind::getSkuBrandBind).distinct().collect(Collectors.toList())).list();


        customerIdList.addAll(supplyList.stream().map(Supply::getCustomerId).distinct().collect(Collectors.toList()));
        List<CustomerResult> customerResultList = customerService.getResults(customerIdList);

        List<BrandResult> brandResultList = brandService.getBrandResults(brandIdList);

        List<SkuListResult> skuListResults = skuListService.resultByIds(skuIdList);

        for (PurchaseListResult purchaseListResult : dataList) {
            if (ToolUtil.isNotEmpty(purchaseListResult.getCustomerId())) {
                for (CustomerResult customerResult : customerResultList) {
                    if (customerResult.getCustomerId().equals(purchaseListResult.getCustomerId())){
                        purchaseListResult.setCustomerResult(customerResult);
                    }
                }
            }
            if (ToolUtil.isNotEmpty(purchaseListResult.getBrandId())) {
                for (BrandResult brandResult : brandResultList) {
                    if (brandResult.getBrandId().equals(purchaseListResult.getBrandId())){
                        purchaseListResult.setBrandResult(brandResult);
                    }
                }
            }

            for (SkuListResult skuListResult : skuListResults) {
                if (skuListResult.getSkuId().equals(purchaseListResult.getSkuId())){
                    purchaseListResult.setSkuResult(skuListResult);
                }
            }
            List<Long> skuBrandBindList = new ArrayList<>();
            for (SkuBrandBind skuBrandBind : skuBrandBinds) {
//                if (ToolUtil.isNotEmpty(purchaseListResult.getBrandId()) && purchaseListResult.getBrandId().equals(skuBrandBind.getBrandId()) && purchaseListResult.getSkuId().equals(skuBrandBind.getSkuId())){
                if ( purchaseListResult.getSkuId().equals(skuBrandBind.getSkuId())){
                    skuBrandBindList.add(skuBrandBind.getSkuBrandBind());
                }
            }
            List<Long> customerIds = new ArrayList<>();
            List<CustomerResult> customerResults = new ArrayList<>();
            for (Supply supply : supplyList) {
                for (Long skuBrandBind : skuBrandBindList) {
                    if (supply.getSkuBrandBind().equals(skuBrandBind)){
                        customerIds.add(supply.getCustomerId());
                    }
                }
            }
            for (Long customerId : customerIds) {
                for (CustomerResult customerResult : customerResultList) {
                    if (customerId.equals(customerResult.getCustomerId())){
                        customerResults.add(customerResult);
                    }
                }
            }
            purchaseListResult.setBindCustomerResultList(customerResults);
        }
    }

    private Serializable getKey(PurchaseListParam param) {
        return param.getPurchaseListId();
    }

    private Page<PurchaseListResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private PurchaseList getOldEntity(PurchaseListParam param) {
        return this.getById(getKey(param));
    }

    private PurchaseList getEntity(PurchaseListParam param) {
        PurchaseList entity = new PurchaseList();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }
    @Override
    public void updateStatusByIds(List<Long> ids){
        if (ToolUtil.isEmpty(ids) || ids.size() == 0){
            return ;
        }
        this.update(new PurchaseList(){{
            setStatus(99);
        }},new QueryWrapper<PurchaseList>(){{
            in("purchase_list_id",ids  );
        }});
    }
}
