package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.entity.CrmBusinessDetailed;
import cn.atsoft.dasheng.app.entity.Items;
import cn.atsoft.dasheng.app.model.params.BusinessDetailedParam;
import cn.atsoft.dasheng.app.model.params.CrmBusinessDetailedParam;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.CrmBusinessDetailedResult;
import cn.atsoft.dasheng.app.model.result.ItemsResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.ItemsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.ContractDetail;
import cn.atsoft.dasheng.app.mapper.ContractDetailMapper;
import cn.atsoft.dasheng.app.model.params.ContractDetailParam;
import cn.atsoft.dasheng.app.model.result.ContractDetailResult;
import  cn.atsoft.dasheng.app.service.ContractDetailService;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
 * 合同产品明细 服务实现类
 * </p>
 *
 * @author sb
 * @since 2021-09-18
 */
@Service
public class ContractDetailServiceImpl extends ServiceImpl<ContractDetailMapper, ContractDetail> implements ContractDetailService {


    @Autowired
    private ItemsService itemsService;
    @Autowired
    private BrandService brandService;

    @Override
    public void add(ContractDetailParam param){

        ContractDetail entity = getEntity(param);
        this.save(entity);
    }

    Map<Long, ContractDetail> map;
    @Override
    public void addAll(BusinessDetailedParam param) {
        map = new HashMap<>();
        if (ToolUtil.isNotEmpty(param.getContractDetailParams())) {
            List<ContractDetail> updateOrAdd = new ArrayList<>();
            for (ContractDetailParam detailedParam : param.getContractDetailParams()) {
                if (detailedParam.getQuantity() == 0) {
                    throw new ServiceException(500, "注意产品数量");
                }
                map = judge(param.getContractId(), detailedParam.getSkuId(), detailedParam.getBrandId(), detailedParam.getQuantity(), detailedParam.getSalePrice());


            }
            for (Map.Entry<Long, ContractDetail> longCrmBusinessDetailedEntry : map.entrySet()) {
                ContractDetail value = longCrmBusinessDetailedEntry.getValue();
                updateOrAdd.add(value);
            }

            this.saveOrUpdateBatch(updateOrAdd);
        }
    }

    Map<Long, ContractDetail> judge(Long contractId, Long itemIds, Long brandIds, int number, int money) {
        List<ContractDetail> businessDetaileds = this.lambdaQuery().in(ContractDetail::getContractId, contractId)
                .list();
        //判断当前商机详情是否有这个商品  没有直接添加
        if (ToolUtil.isEmpty(businessDetaileds)) {
            ContractDetail businessDetailedByMap = new ContractDetail();
            businessDetailedByMap.setContractId(contractId);
            businessDetailedByMap.setQuantity(number);
            businessDetailedByMap.setBrandId(brandIds);
            businessDetailedByMap.setSkuId(itemIds);
            businessDetailedByMap.setSalePrice(money);
            businessDetailedByMap.setTotalPrice(money * number);
            businessDetailedByMap.setDisplay(1);
            map.put(itemIds + brandIds, businessDetailedByMap);
            return map;
        }

        //判断商机详情是否有粗存在物品  有的直接叠加数量
        for (ContractDetail businessDetailed : businessDetaileds) {
            if (businessDetailed.getSkuId().equals(itemIds) && businessDetailed.getBrandId().equals(brandIds)) {
                int i = businessDetailed.getQuantity() + number;
                int newMoney = i * money;
                businessDetailed.setSalePrice(money);
                businessDetailed.setTotalPrice(newMoney);
                businessDetailed.setQuantity(i);
                businessDetailed.setDisplay(1);
                map.put(businessDetailed.getSkuId() + businessDetailed.getBrandId(), businessDetailed);
                break;
            }

        }

        //通过map判段这个商品是否存在  没有直接添加
        ContractDetail BusinessDetailed = map.get(itemIds + brandIds);
        if (ToolUtil.isEmpty(BusinessDetailed)) {
            ContractDetail businessDetailed = new ContractDetail();
            businessDetailed.setContractId(contractId);
            businessDetailed.setQuantity(number);
            businessDetailed.setBrandId(brandIds);
            businessDetailed.setSkuId(itemIds);
            businessDetailed.setSalePrice(money);
            businessDetailed.setTotalPrice(money * number);
            businessDetailed.setDisplay(1);
            map.put(itemIds + brandIds, businessDetailed);

        }

        return map;
    }





    @Override
    public void delete(ContractDetailParam param){
        param.setDisplay(0);
        this.update(param);
    }

    @Override
    public void update(ContractDetailParam param){
        ContractDetail oldEntity = getOldEntity(param);
        ContractDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ContractDetailResult findBySpec(ContractDetailParam param){
        return null;
    }

    @Override
    public List<ContractDetailResult> findListBySpec(ContractDetailParam param){
        return null;
    }

    @Override
    public PageInfo<ContractDetailResult> findPageBySpec(ContractDetailParam param, DataScope dataScope ){
        Page<ContractDetailResult> pageContext = getPageContext();
        IPage<ContractDetailResult> page = this.baseMapper.customPageList(pageContext, param,dataScope);


        List<Long> detailIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        for (ContractDetailResult record : page.getRecords()) {
            detailIds.add(record.getSkuId());
            brandIds.add(record.getBrandId());
        }
        QueryWrapper<Items> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("item_id", detailIds);
        List<Items> list = detailIds.size() == 0 ? new ArrayList<>() : itemsService.list(queryWrapper);

        QueryWrapper<Brand> brandQueryWrapper = new QueryWrapper<>();
        brandQueryWrapper.in("brand_id", brandIds);
        List<Brand> brandList = brandIds.size() == 0 ? new ArrayList<>() : brandService.list(brandQueryWrapper);

        for (ContractDetailResult record : page.getRecords()) {
            for (Items items : list) {
                if (items.getItemId().equals(record.getSkuId())) {
                    ItemsResult itemsResult = new ItemsResult();
                    ToolUtil.copyProperties(items, itemsResult);
                    record.setItemsResult(itemsResult);
                    break;
                }
            }
            for (Brand brands : brandList) {
                if (brands.getBrandId().equals(record.getBrandId())) {
                    BrandResult brandsResult = new BrandResult();
                    ToolUtil.copyProperties(brands, brandsResult);
                    record.setBrandResult(brandsResult);
                    break;
                }
            }
        }

        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ContractDetailParam param){
        return param.getId();
    }

    private Page<ContractDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ContractDetail getOldEntity(ContractDetailParam param) {
        return this.getById(getKey(param));
    }

    private ContractDetail getEntity(ContractDetailParam param) {
        ContractDetail entity = new ContractDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
