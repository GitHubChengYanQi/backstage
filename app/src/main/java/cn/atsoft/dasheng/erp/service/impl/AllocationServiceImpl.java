package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Allocation;
import cn.atsoft.dasheng.erp.entity.AllocationDetail;
import cn.atsoft.dasheng.erp.mapper.AllocationMapper;
import cn.atsoft.dasheng.erp.model.params.AllocationParam;
import cn.atsoft.dasheng.erp.model.params.InstockListParam;
import cn.atsoft.dasheng.erp.model.params.InstockOrderParam;
import cn.atsoft.dasheng.erp.model.result.AllocationResult;
import cn.atsoft.dasheng.erp.service.AllocationDetailService;
import  cn.atsoft.dasheng.erp.service.AllocationService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.InstockOrderService;
import cn.atsoft.dasheng.production.model.params.ProductionPickListsDetailParam;
import cn.atsoft.dasheng.production.model.params.ProductionPickListsParam;
import cn.atsoft.dasheng.production.service.ProductionPickListsService;
import cn.atsoft.dasheng.purchase.service.GetOrigin;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
 * 调拨主表 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-07-13
 */
@Service
public class AllocationServiceImpl extends ServiceImpl<AllocationMapper, Allocation> implements AllocationService {
    @Autowired
    private AllocationDetailService allocationDetailService;
    @Autowired
    private ProductionPickListsService productionPickListsService;
    @Autowired
    private InstockOrderService instockOrderService;
    @Autowired
    private GetOrigin getOrigin;
    @Override
    public void add(AllocationParam param){
        Allocation entity = getEntity(param);
        this.save(entity);
        if (ToolUtil.isNotEmpty(param.getDetailParams())) {
            List<AllocationDetail> allocationDetails = BeanUtil.copyToList(param.getDetailParams(), AllocationDetail.class);
            for (AllocationDetail allocationDetail : allocationDetails) {
                allocationDetail.setToStorehouseId(param.getStorehouseId());
                allocationDetail.setAllocationId(entity.getAllocationId());
            }
            allocationDetailService.saveBatch(allocationDetails);
        }

    }

    /**
     * 创建出库单
     * @param allocationId
     */
    @Override
   public void createPickListsAndInStockOrder(Long allocationId){
        Allocation allocation = this.getById(allocationId);
        List<AllocationDetail> allocationDetails = allocationDetailService.query().eq("display", 1).eq("allocation_id", allocationId).list();
        List<Long> storehouseIds = new ArrayList<>();
        for (AllocationDetail allocationDetail : allocationDetails) {
            storehouseIds.add(allocationDetail.getStorehouseId());
        }

        for (Long storehouseId : storehouseIds.stream().distinct().collect(Collectors.toList())) {
            ProductionPickListsParam listsParam = new ProductionPickListsParam();
            InstockOrderParam instockOrderParam = new InstockOrderParam();
            listsParam.setPickListsName(allocation.getAllocationName());
            listsParam.setUserId(allocation.getUserId());
            listsParam.setSource("ALLOCATION");
            listsParam.setSourceId(allocationId);
            List<ProductionPickListsDetailParam> details = new ArrayList<>();
            List<InstockListParam> listParams = new ArrayList<>();
            for (AllocationDetail allocationDetail : allocationDetails) {
                if (allocationDetail.getStorehouseId().equals(storehouseId)){
                    ProductionPickListsDetailParam listsDetailParam = new ProductionPickListsDetailParam();
                    ToolUtil.copyProperties(allocationDetail,listsDetailParam);
                    details.add(listsDetailParam);
                    InstockListParam instockListParam = new InstockListParam();
                    ToolUtil.copyProperties(listsDetailParam,instockListParam);
                    listParams.add(instockListParam);
                }
            }
           if (details.size()>0){
               instockOrderParam.setListParams(listParams);
               productionPickListsService.add(listsParam);
               instockOrderService.add(instockOrderParam);
           }
        }



    }

    @Override
    public void delete(AllocationParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(AllocationParam param){
        Allocation oldEntity = getOldEntity(param);
        Allocation newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public AllocationResult findBySpec(AllocationParam param){
        return null;
    }

    @Override
    public List<AllocationResult> findListBySpec(AllocationParam param){
        return null;
    }

    @Override
    public PageInfo<AllocationResult> findPageBySpec(AllocationParam param){
        Page<AllocationResult> pageContext = getPageContext();
        IPage<AllocationResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(AllocationParam param){
        return param.getAllocationId();
    }

    private Page<AllocationResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Allocation getOldEntity(AllocationParam param) {
        return this.getById(getKey(param));
    }

    private Allocation getEntity(AllocationParam param) {
        Allocation entity = new Allocation();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
