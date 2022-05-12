package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.ItemsResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.ItemBrandBind;
import cn.atsoft.dasheng.app.mapper.ItemBrandBindMapper;
import cn.atsoft.dasheng.app.model.params.ItemBrandBindParam;
import cn.atsoft.dasheng.app.model.result.ItemBrandBindResult;
import  cn.atsoft.dasheng.app.service.ItemBrandBindService;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
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
 * 商品品牌绑定表 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-09-24
 */
@Service
public class ItemBrandBindServiceImpl extends ServiceImpl<ItemBrandBindMapper, ItemBrandBind> implements ItemBrandBindService {
    @Autowired
    private BrandService brandService;
    @Override
    public void add(ItemBrandBindParam param){
        ItemBrandBind entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ItemBrandBindParam param){
        param.setDisplay(0);
        this.updateById(getEntity(param));
    }

    @Override
    public void update(ItemBrandBindParam param){
        ItemBrandBind oldEntity = getOldEntity(param);
        ItemBrandBind newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ItemBrandBindResult findBySpec(ItemBrandBindParam param){
        return null;
    }

    @Override
    public List<ItemBrandBindResult> findListBySpec(ItemBrandBindParam param){
        return null;
    }

    @Override
    public PageInfo<ItemBrandBindResult> findPageBySpec(ItemBrandBindParam param, DataScope dataScope){
        Page<ItemBrandBindResult> pageContext = getPageContext();
        IPage<ItemBrandBindResult> page = this.baseMapper.customPageList(pageContext, param,dataScope);
        formatResult(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ItemBrandBindParam param){
        return param.getItemBrandBindId();
    }

    private Page<ItemBrandBindResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ItemBrandBind getOldEntity(ItemBrandBindParam param) {
        return this.getById(getKey(param));
    }

    private ItemBrandBind getEntity(ItemBrandBindParam param) {
        ItemBrandBind entity = new ItemBrandBind();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }
    private void formatResult(List<ItemBrandBindResult> data){
        List<Long> brandIds = new ArrayList<>();
        for (ItemBrandBindResult datum : data) {
            brandIds.add(datum.getBrandId());
        }

        //品牌id查询品牌名称
        QueryWrapper<Brand> brandQueryWrapper = new QueryWrapper<>();
        brandQueryWrapper.lambda().in(Brand::getBrandId,brandIds);
        List<Brand> list = brandIds.size() == 0 ? new ArrayList<>() : brandService.list(brandQueryWrapper);
        for (ItemBrandBindResult datum : data) {
            BrandResult brandResult = new BrandResult();
            for (Brand brand : list) {
                if (datum.getBrandId().equals(brand.getBrandId())) {
                    ToolUtil.copyProperties(brand,brandResult);
                }
            }
            datum.setBrandResult(brandResult);
        }



    }

}
