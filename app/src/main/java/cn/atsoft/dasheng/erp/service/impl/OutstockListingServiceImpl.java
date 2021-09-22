package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.entity.Items;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.ItemsResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.ItemsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.ApplyDetails;
import cn.atsoft.dasheng.erp.entity.OutstockListing;
import cn.atsoft.dasheng.erp.mapper.OutstockListingMapper;
import cn.atsoft.dasheng.erp.model.params.ApplyDetailsParam;
import cn.atsoft.dasheng.erp.model.params.OutstockListingParam;
import cn.atsoft.dasheng.erp.model.result.ApplyDetailsResult;
import cn.atsoft.dasheng.erp.model.result.OutstockApplyResult;
import cn.atsoft.dasheng.erp.model.result.OutstockListingResult;
import  cn.atsoft.dasheng.erp.service.OutstockListingService;
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
import java.util.List;

/**
 * <p>
 * 出库清单 服务实现类
 * </p>
 *
 * @author cheng
 * @since 2021-09-15
 */
@Service
public class OutstockListingServiceImpl extends ServiceImpl<OutstockListingMapper, OutstockListing> implements OutstockListingService {
    @Autowired
    private BrandService brandService;
    @Autowired
    private ItemsService itemsService;
    @Override
    public void add(OutstockListingParam param){
        OutstockListing entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(OutstockListingParam param){
        OutstockListing byId = this.getById(param.getOutstockListingId());
        if (ToolUtil.isEmpty(byId)){
            throw new ServiceException(500,"所删除目标不存在");
        }else {
            param.setDisplay(0);
            this.update(param);
        }
    }

    @Override
    public void update(OutstockListingParam param){
        OutstockListing oldEntity = getOldEntity(param);
        OutstockListing newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public OutstockListingResult findBySpec(OutstockListingParam param){
        return null;
    }

    @Override
    public List<OutstockListingResult> findListBySpec(OutstockListingParam param){
        return null;
    }

    @Override
    public PageInfo<OutstockListingResult> findPageBySpec(OutstockListingParam param){
        Page<OutstockListingResult> pageContext = getPageContext();
        IPage<OutstockListingResult> page = this.baseMapper.customPageList(pageContext, param);
        List<Long> brandIds = new ArrayList<>();
        List<Long> itemIds=new ArrayList<>();
        for (OutstockListingResult record : page.getRecords()) {
            brandIds.add(record.getBrandId());
            itemIds.add(record.getItemId());
        }
        QueryWrapper<Brand> brandQueryWrapper = new QueryWrapper<>();
        brandQueryWrapper.lambda().in(Brand::getBrandId,brandIds);
        List<Brand> brandList =brandIds.size()==0? new ArrayList<>():brandService.list(brandQueryWrapper);
        QueryWrapper<Items> itemsQueryWrapper = new QueryWrapper<>();
        itemsQueryWrapper.lambda().in(Items::getItemId,itemIds);
        List<Items> itemsList =itemIds.size()==0 ?new ArrayList<>() :  itemsService.list(itemsQueryWrapper);

        for (OutstockListingResult record : page.getRecords()) {
            for (Brand brand : brandList) {
                if (record.getBrandId()!=null && record.getBrandId().equals(brand.getBrandId())){
                    BrandResult brandResult = new BrandResult();
                    ToolUtil.copyProperties(brand,brandResult);
                    record.setBrandResult(brandResult);
                    break;
                }
            }
            for (Items items : itemsList) {
                if (record.getItemId() != null && record.getItemId().equals(items.getItemId())){
                    ItemsResult itemsResult = new ItemsResult();
                    ToolUtil.copyProperties(items,itemsResult);
                    record.setItemsResult(itemsResult);
                    break;
                }
            }
        }

        return PageFactory.createPageInfo(page);
    }

    @Override
    public void addList(List<ApplyDetailsParam> applyDetails) {
//        for (ApplyDetailsParam applyDetail : applyDetails) {
//            ApplyDetails details = new ApplyDetails();
//            int number = Math.toIntExact(applyDetail.getNumber());
//            for (int i = 0; i < number; i++) {
//                details.setItemId(applyDetail.getItemId());
//                details.setBrandId(details.getBrandId());
//                details.setNumber(1L);
//            }
//        }
    }

    private Serializable getKey(OutstockListingParam param){
        return param.getOutstockListingId();
    }

    private Page<OutstockListingResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private OutstockListing getOldEntity(OutstockListingParam param) {
        return this.getById(getKey(param));
    }

    private OutstockListing getEntity(OutstockListingParam param) {
        OutstockListing entity = new OutstockListing();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
