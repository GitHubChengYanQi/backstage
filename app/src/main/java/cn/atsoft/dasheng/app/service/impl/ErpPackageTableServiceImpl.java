package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.entity.Items;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.ItemsResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.ItemsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.ErpPackageTable;
import cn.atsoft.dasheng.app.mapper.ErpPackageTableMapper;
import cn.atsoft.dasheng.app.model.params.ErpPackageTableParam;
import cn.atsoft.dasheng.app.model.result.ErpPackageTableResult;
import  cn.atsoft.dasheng.app.service.ErpPackageTableService;
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
 * 套餐分表 服务实现类
 * </p>
 *
 * @author qr
 * @since 2021-08-04
 */
@Service
public class ErpPackageTableServiceImpl extends ServiceImpl<ErpPackageTableMapper, ErpPackageTable> implements ErpPackageTableService {
    @Autowired
  private ItemsService itemsService;
  @Autowired
    private BrandService brandService;
    @Override
    public void add(ErpPackageTableParam param){
        ErpPackageTable entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ErpPackageTableParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ErpPackageTableParam param){
        ErpPackageTable oldEntity = getOldEntity(param);
        ErpPackageTable newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ErpPackageTableResult findBySpec(ErpPackageTableParam param){
        return null;
    }

    @Override
    public List<ErpPackageTableResult> findListBySpec(ErpPackageTableParam param){
        return null;
    }

    @Override
    public PageInfo<ErpPackageTableResult> findPageBySpec(ErpPackageTableParam param){
        Page<ErpPackageTableResult> pageContext = getPageContext();
        IPage<ErpPackageTableResult> page = this.baseMapper.customPageList(pageContext, param);
        List<Long> wp=new ArrayList<>();
        List<Long> wp1=new ArrayList<>();
      for (ErpPackageTableResult record : page.getRecords()) {
            wp.add(record.getItemId());
            wp1.add(record.getBrandId());
      }
      QueryWrapper<Items> queryWrapper = new QueryWrapper<>();
      QueryWrapper<Brand> queryWrapper1=new QueryWrapper<>();
      queryWrapper.in("item_id",wp);
      queryWrapper1.in("brand_id",wp1);
      List<Items> list=itemsService.list(queryWrapper);
      List<Brand> list1=brandService.list(queryWrapper1);
      for (ErpPackageTableResult record : page.getRecords()) {
        for (Items items : list) {
            if(items.getItemId().equals(record.getItemId())){
              ItemsResult itemsResult =new ItemsResult();
              ToolUtil.copyProperties(items,itemsResult);
              record.setItemsResult(itemsResult);
              break;
            }
        }
        for (Brand brand : list1) {
            BrandResult brandResult=new BrandResult();
            ToolUtil.copyProperties(brand,brandResult);
            record.setBrandResult(brandResult);
            break;
        }


      }

        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ErpPackageTableParam param){
        return param.getId();
    }

    private Page<ErpPackageTableResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ErpPackageTable getOldEntity(ErpPackageTableParam param) {
        return this.getById(getKey(param));
    }

    private ErpPackageTable getEntity(ErpPackageTableParam param) {
        ErpPackageTable entity = new ErpPackageTable();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
