package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.ItemBrandBindResult;
import cn.atsoft.dasheng.app.model.result.MaterialResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.ItemBrandBindService;
import cn.atsoft.dasheng.app.service.MaterialService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.mapper.ItemsMapper;
import cn.atsoft.dasheng.app.model.params.ItemsParam;
import cn.atsoft.dasheng.app.model.result.ItemsResult;
import cn.atsoft.dasheng.app.service.ItemsService;
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
import java.util.List;

/**
 * <p>
 * 物品表 服务实现类
 * </p>
 *
 * @author 1
 * @since 2021-07-14
 */
@Service
public class ItemsServiceImpl extends ServiceImpl<ItemsMapper, Items> implements ItemsService {
    @Autowired
    private MaterialService materialService;
    @Autowired
    ItemBrandBindService itemBrandBindService;
    @Autowired
    private BrandService brandService;

    @Override
    public Long add(ItemsParam param) {
        Items entry = getEntity(param);
        this.save(entry);
        List<ItemBrandBind> brandList = new ArrayList<>();
        for (Long brandResult : param.getBrandResults()) {
            ItemBrandBind bind = new ItemBrandBind();
            bind.setBrandId(brandResult);
            bind.setItemId(entry.getItemId());
            brandList.add(bind);
        }
        itemBrandBindService.saveBatch(brandList);

        return entry.getItemId();
    }

    @Override
    public void delete(ItemsParam param) {

        Items items = new Items();
        items.setDisplay(0);
        QueryWrapper<Items> itemsQueryWrapper = new QueryWrapper<>();
        itemsQueryWrapper.eq("item_id", param.getItemId());
        this.update(items, itemsQueryWrapper);
    }

    @Override
    public void update(ItemsParam param) {
        Items oldEntity = getOldEntity(param);
        Items newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
//        QueryWrapper<ItemBrandBind> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("item_id",param.getItemId());

        List<ItemBrandBind> brandList = new ArrayList<>();
//        if(ToolUtil.isEmpty(param.getBrandResults())){
//            throw new ServiceException(500,"请选择品牌");
//        }
//        for (Long brandResult : param.getBrandResults()) {
//            ItemBrandBind bind = new ItemBrandBind();
//            bind.setBrandId(brandResult);
//            bind.setItemId(newEntity.getItemId());
//            brandList.add(bind);
//        }

//        itemBrandBindService.remove(queryWrapper);
//        itemBrandBindService.saveBatch(brandList);
    }

    @Override
    public ItemsResult findBySpec(ItemsParam param) {
        return null;
    }

    @Override
    public List<ItemsResult> findListBySpec(ItemsParam param) {
        return null;
    }

    @Override
    public PageInfo<ItemsResult> findPageBySpec(ItemsParam param, DataScope dataScope) {
        Page<ItemsResult> pageContext = getPageContext();
        IPage<ItemsResult> page = this.baseMapper.customPageList(pageContext, param, dataScope);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    @Override
    public void batchDelete(List<Long> ids) {
        Items items = new Items();
        items.setDisplay(0);
        QueryWrapper<Items> itemsQueryWrapper = new QueryWrapper<>();
        itemsQueryWrapper.in("item_id", ids);
        this.update(items, itemsQueryWrapper);
    }

    private Serializable getKey(ItemsParam param) {
        return param.getItemId();
    }

    private Page<ItemsResult> getPageContext() {
        List<String> fields = new ArrayList<String>() {{
            add("createTime");
            add("materialName");
            add("inventory");
            add("productionTime");
            add("name");
            add("important");
            add("weight");
        }};
        return PageFactory.defaultPage(fields);
    }

    private Items getOldEntity(ItemsParam param) {
        return this.getById(getKey(param));
    }

    private Items getEntity(ItemsParam param) {
        Items entity = new Items();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public void formatResult(ItemsResult data) {
        List<Long> brandIds = new ArrayList<>();
        List<ItemBrandBindResult> itemBrandBindResults = new ArrayList<>();
        //物品id 查询 绑定关系 品牌id
        QueryWrapper<ItemBrandBind> brandBindQueryWrapper = new QueryWrapper<>();
        brandBindQueryWrapper.lambda().in(ItemBrandBind::getItemId, data.getItemId()).and(i -> i.in(ItemBrandBind::getDisplay, 1));
        List<ItemBrandBind> brandIdsList = itemBrandBindService.list(brandBindQueryWrapper);
        for (ItemBrandBind brandId : brandIdsList) {
            brandIds.add(brandId.getBrandId());
        }
        //品牌id查询品牌名称
        QueryWrapper<Brand> brandQueryWrapper = new QueryWrapper<>();
        brandQueryWrapper.lambda().in(Brand::getBrandId, brandIds);
        List<Brand> brandList = brandIds.size() == 0 ? new ArrayList<>() : brandService.list(brandQueryWrapper);

        for (ItemBrandBind brandBind : brandIdsList) {
            if (brandBind.getItemId().equals(data.getItemId())) {
                ItemBrandBindResult brandBindResult = new ItemBrandBindResult();
                ToolUtil.copyProperties(brandBind, brandBindResult);
                for (Brand brand : brandList) {
                    if (brandBind.getBrandId().equals(brand.getBrandId())) {
                        brandBindResult.setBrandName(brand.getBrandName());
                    }
                }
                itemBrandBindResults.add(brandBindResult);
            }
        }
        data.setBrandResults(itemBrandBindResults);

    }

    public void format(List<ItemsResult> data) {
        List<Long> itemIds = new ArrayList<>();
        List<Long> materialIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        for (ItemsResult datum : data) {
            materialIds.add(datum.getMaterialId());
            itemIds.add(datum.getItemId());
        }
        //物品id 查询 绑定关系 品牌id
        List<ItemBrandBind> brandIdsList =null;
        QueryWrapper<ItemBrandBind> brandBindQueryWrapper = new QueryWrapper<>();
        if (ToolUtil.isNotEmpty(itemIds)) {
            brandBindQueryWrapper.lambda().in(ItemBrandBind::getItemId, itemIds);
            brandIdsList = itemBrandBindService.list(brandBindQueryWrapper);
            for (ItemBrandBind brandId : brandIdsList) {
                brandIds.add(brandId.getBrandId());
            }
        }

        //品牌id查询品牌名称
        QueryWrapper<Brand> brandQueryWrapper = new QueryWrapper<>();
        brandQueryWrapper.lambda().in(Brand::getBrandId, brandIds);
        List<Brand> brandList = brandIds.size() == 0 ? new ArrayList<>() : brandService.list(brandQueryWrapper);

        //材料id查询材料名称
        QueryWrapper<Material> materialQueryWrapper = new QueryWrapper<>();
        materialQueryWrapper.in("material_id", materialIds);
        List<Material> materialList = materialIds.size() == 0 ? new ArrayList<>() : materialService.list(materialQueryWrapper);

        for (ItemsResult datum : data) {
            List<ItemBrandBindResult> itemBrandBindResults = new ArrayList<>();
            for (Material material : materialList) {
                if (datum.getMaterialId().equals(material.getMaterialId())) {
                    MaterialResult materialResult = new MaterialResult();
                    ToolUtil.copyProperties(material, materialResult);
                    datum.setMaterialResult(materialResult);
                    break;
                }
            }
            if (ToolUtil.isNotEmpty(brandIdsList)) {
                for (ItemBrandBind brandBind : brandIdsList) {
                    if (brandBind.getItemId().equals(datum.getItemId())) {
                        ItemBrandBindResult brandBindResult = new ItemBrandBindResult();
                        ToolUtil.copyProperties(brandBind, brandBindResult);
                         for (Brand brand : brandList) {
                            if (brandBind.getBrandId().equals(brand.getBrandId())) {
                                brandBindResult.setBrandName(brand.getBrandName());
                            }
                        }
                        itemBrandBindResults.add(brandBindResult);
                    }
                }
                datum.setBrandResults(itemBrandBindResults);
            }

        }


    }
}
