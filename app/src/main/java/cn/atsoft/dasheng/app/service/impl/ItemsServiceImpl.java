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
        List<ItemBrandBind> brandList = new ArrayList<>( );
        for (Long brandResult : param.getBrandResults()) {
            ItemBrandBind bind = new ItemBrandBind();
            bind.setBrandId(brandResult);
            bind.setItemId(entry.getItemId());
            brandList.add(bind);
        }
        itemBrandBindService.saveBatch(brandList);

//        Items entity = getEntity(param);
//        this.save(entity);
        return entry.getItemId();
    }

    @Override
    public void delete(ItemsParam param) {
      Items byId = this.getById(param.getItemId());
      if (ToolUtil.isEmpty(byId)){
        throw new ServiceException(500,"删除目标不存在");
      }
      param.setDisplay(0);
      this.update(param);
    }

    @Override
    public void update(ItemsParam param) {
        Items oldEntity = getOldEntity(param);
        Items newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
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
    public PageInfo<ItemsResult> findPageBySpec(ItemsParam param) {
        Page<ItemsResult> pageContext = getPageContext();
        IPage<ItemsResult> page = this.baseMapper.customPageList(pageContext, param);
            format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    @Override
    public void batchDelete(List<Long> ids) {
        Items items = new Items();
        items.setDisplay(0);
         QueryWrapper<Items> itemsQueryWrapper = new QueryWrapper<>();
         itemsQueryWrapper.in("item_id",ids);
        this.update(items,itemsQueryWrapper);
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
    public  void format(List<ItemsResult> data){
        List<Long> itemIds = new ArrayList<>();
        List<Long> materialIds = new ArrayList<>();
        List<Long> brandIds = new ArrayList<>();
        for (ItemsResult datum : data) {
            materialIds.add(datum.getMaterialId());
            itemIds.add(datum.getItemId());
        }
        //物品id 查询 绑定关系 品牌id
        QueryWrapper<ItemBrandBind> brandBindQueryWrapper = new QueryWrapper<>();
        brandBindQueryWrapper.lambda().in(ItemBrandBind::getItemId,itemIds);
        List<ItemBrandBind> brandIdsList = itemBrandBindService.list(brandBindQueryWrapper);

        for (ItemBrandBind brandId : brandIdsList) {
            brandIds.add(brandId.getBrandId());
        }
        //品牌id查询品牌名称
        QueryWrapper<Brand> brandQueryWrapper = new QueryWrapper<>();
        brandQueryWrapper.lambda().in(Brand::getBrandId,brandIds);
        List<Brand> brandList = brandIds.size() == 0 ? new ArrayList<>() : brandService.list(brandQueryWrapper);

        //材料id查询材料名称
        QueryWrapper<Material> materialQueryWrapper =  new QueryWrapper<>();
        materialQueryWrapper.in("material_id" , materialIds);
        List<Material> materialList = materialIds.size() == 0 ? new ArrayList<>() :  materialService.list(materialQueryWrapper);

        for (ItemsResult datum : data) {
            List<ItemBrandBindResult> itemBrandBindResults = new ArrayList<>();
            for (Material material : materialList) {
                if (datum.getMaterialId().equals(material.getMaterialId())) {
                    MaterialResult materialResult =new MaterialResult();
                    ToolUtil.copyProperties(material,materialResult);
                    datum.setMaterialResult(materialResult);
                    break;
                }
            }
            for (ItemBrandBind itemBrandBind : brandIdsList) {
                if (itemBrandBind.getItemId().equals(datum.getItemId())){
                    ItemBrandBindResult brandBind = new ItemBrandBindResult();
                    ToolUtil.copyProperties(itemBrandBind,brandBind);
                    for (Brand brand : brandList) {
                        if (itemBrandBind.getBrandId().equals(brand.getBrandId())) {
                            brandBind.setBrandName(brand.getBrandName());
                        }
                    }
                    itemBrandBindResults.add(brandBind);
                }
                datum.setBrandBindResults(itemBrandBindResults);
            }

        }

    }
}
