package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.ErpPackage;
import cn.atsoft.dasheng.app.entity.ErpPackageTable;
import cn.atsoft.dasheng.app.entity.Items;
import cn.atsoft.dasheng.app.model.result.ItemsResult;
import cn.atsoft.dasheng.app.service.ErpPackageTableService;
import cn.atsoft.dasheng.app.service.ItemsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.CrmBusinessDetailed;
import cn.atsoft.dasheng.app.mapper.CrmBusinessDetailedMapper;
import cn.atsoft.dasheng.app.model.params.CrmBusinessDetailedParam;
import cn.atsoft.dasheng.app.model.result.CrmBusinessDetailedResult;
import cn.atsoft.dasheng.app.service.CrmBusinessDetailedService;
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
 * 商机明细表 服务实现类
 * </p>
 *
 * @author qr
 * @since 2021-08-04
 */
@Service
public class CrmBusinessDetailedServiceImpl extends ServiceImpl<CrmBusinessDetailedMapper, CrmBusinessDetailed> implements CrmBusinessDetailedService {
    @Autowired
    private ItemsService itemsService;
    @Autowired
    private ErpPackageTableService erpPackageTableService;

    @Override
    public void add(CrmBusinessDetailedParam param) {
        CrmBusinessDetailed entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void addAll(CrmBusinessDetailedParam param) {
        List<CrmBusinessDetailed> list = new ArrayList<>();
        for (Long itemId : param.getItemIds()) {
            CrmBusinessDetailed newEntity = new CrmBusinessDetailed();
            newEntity.setBusinessId(param.getBusinessId());
            newEntity.setItemId(itemId);
            list.add(newEntity);
        }
        this.saveBatch(list);
    }

    @Override
    public void addAllPackages(CrmBusinessDetailedParam param) {
        //创建保存数据的集合
        List<CrmBusinessDetailed> newEntryList = new ArrayList<>();


        //根据传入套餐id数组 查询套餐明细中物品的itemId
        QueryWrapper<ErpPackageTable> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(ErpPackageTable::getPackageId,param.getPackagesIds());
        //将itemId存入list
        List<ErpPackageTable> itemId= erpPackageTableService.list(queryWrapper);
        //遍历itemId 存入单独对象中 保存在 newEntryList之后存入数据库

        for (ErpPackageTable table : itemId) {
            CrmBusinessDetailed newEntity = new CrmBusinessDetailed();
            newEntity.setItemId(table.getItemId());
            newEntity.setBusinessId(param.getBusinessId());
            newEntryList.add(newEntity);
        }
        //newEntryList{CrmBusinessDerailed实体对象(多个)}
        this.saveBatch(newEntryList);
    }


    @Override
    public void delete(CrmBusinessDetailedParam param) {
      CrmBusinessDetailed byId = this.getById(param.getId());
      if (ToolUtil.isEmpty(byId)){
        throw new ServiceException(500,"删除目标不存在");
      }
      param.setDisplay(0);
      this.update(param);
    }

    @Override
    public void update(CrmBusinessDetailedParam param) {
        CrmBusinessDetailed oldEntity = getOldEntity(param);
        CrmBusinessDetailed newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public CrmBusinessDetailedResult findBySpec(CrmBusinessDetailedParam param) {
        return null;
    }

    @Override
    public List<CrmBusinessDetailedResult> findListBySpec(CrmBusinessDetailedParam param) {
        return null;
    }

    @Override
    public PageInfo<CrmBusinessDetailedResult> findPageBySpec(CrmBusinessDetailedParam param) {
        Page<CrmBusinessDetailedResult> pageContext = getPageContext();
        IPage<CrmBusinessDetailedResult> page = this.baseMapper.customPageList(pageContext, param);
        List<Long> detailIds = new ArrayList<>();
        for (CrmBusinessDetailedResult record : page.getRecords()) {
            detailIds.add(record.getItemId());
        }
        QueryWrapper<Items> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("item_id", detailIds);
        List<Items> list = detailIds.size() == 0 ? new ArrayList<>() : itemsService.list(queryWrapper);
        for (CrmBusinessDetailedResult record : page.getRecords()) {
            for (Items items : list) {
                if (items.getItemId().equals(record.getItemId())) {
                    ItemsResult itemsResult = new ItemsResult();
                    ToolUtil.copyProperties(items, itemsResult);
                    record.setItemsResult(itemsResult);
                    break;
                }
            }
        }
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(CrmBusinessDetailedParam param) {
        return param.getId();
    }

    private Page<CrmBusinessDetailedResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private CrmBusinessDetailed getOldEntity(CrmBusinessDetailedParam param) {
        return this.getById(getKey(param));
    }

    private CrmBusinessDetailed getEntity(CrmBusinessDetailedParam param) {
        CrmBusinessDetailed entity = new CrmBusinessDetailed();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
