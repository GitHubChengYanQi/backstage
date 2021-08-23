package cn.atsoft.dasheng.portal.repair.service.impl;


import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.entity.DeliveryDetails;

import cn.atsoft.dasheng.app.entity.Items;

import cn.atsoft.dasheng.app.model.result.CustomerResult;

import cn.atsoft.dasheng.app.model.result.DeliveryDetailsResult;
import cn.atsoft.dasheng.app.service.DeliveryDetailsService;
import cn.atsoft.dasheng.app.service.ItemsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.repair.entity.Repair;
import cn.atsoft.dasheng.portal.repair.mapper.RepairMapper;
import cn.atsoft.dasheng.portal.repair.model.params.RepairParam;
import cn.atsoft.dasheng.portal.repair.model.result.RepairResult;
import  cn.atsoft.dasheng.portal.repair.service.RepairService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 报修 服务实现类
 * </p>
 *
 * @author siqiang
 * @since 2021-08-20
 */
@Service
public class RepairServiceImpl extends ServiceImpl<RepairMapper, Repair> implements RepairService {

    @Autowired
    private DeliveryDetailsService deliveryDetailsService;
    @Autowired
    private ItemsService itemsService;

    @Override
    public Long add(RepairParam param){
        Repair entity = getEntity(param);
        this.save(entity);
        return entity.getRepairId();
    }

    @Override
    public void delete(RepairParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(RepairParam param){
        Repair oldEntity = getOldEntity(param);
        Repair newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public RepairResult findBySpec(RepairParam param){
        return null;
    }

    @Override
    public List<RepairResult> findListBySpec(RepairParam param){
        return null;
    }

    @Override
    public RepairResult detail(Long id) {
        Repair repair = this.getById(id);
        RepairResult repairResult = new RepairResult();
        ToolUtil.copyProperties(repair, repairResult);
        List<RepairResult> results = new ArrayList<RepairResult>() {{
            add(repairResult);
        }};
        this.format(results);
        return results.get(0);
    }

    @Override
    public PageInfo<RepairResult> findPageBySpec(RepairParam param) {
        Page<RepairResult> pageContext = getPageContext();
        IPage<RepairResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private RepairResult format(List<RepairResult> data){
        List<Long> ids = new ArrayList<>();

//        List<Long> itemIds = new ArrayList<>();
//
//        for (RepairResult record : page.getRecords()) {
//            ids.add(record.getItemId());
//            itemIds.add(record.getItemId());
//        }
//        QueryWrapper<Items> itemsQueryWrapper = new QueryWrapper<>();
//        itemsQueryWrapper.in("item_id", itemIds);
//        List<Items> items = itemsService.list(itemsQueryWrapper);
//        for (Items item : items) {
//              QueryWrapper<DeliveryDetails>detailsQueryWrapper = new QueryWrapper<>();
//              detailsQueryWrapper.in("item_id",item.getItemId());
//        }

        //映射发货详情

        for (RepairResult record : data) {
            ids.add(record.getItemId());
        }

        QueryWrapper<DeliveryDetails> detailsQueryWrapper = new QueryWrapper<>();
        detailsQueryWrapper.in("delivery_details_id", ids);
        List<DeliveryDetails> deliveryDetails = deliveryDetailsService.list(detailsQueryWrapper);
        for (RepairResult record : data) {
            for (DeliveryDetails deliveryDetail : deliveryDetails) {
                if (deliveryDetail.getDeliveryDetailsId().equals(record.getItemId())) {
                    DeliveryDetailsResult deliveryDetailsResult = new DeliveryDetailsResult();
                    ToolUtil.copyProperties(deliveryDetail, deliveryDetailsResult);
                    record.setDeliveryDetailsResult(deliveryDetailsResult);
                    break;
                }
            }
        }
        return data.size() == 0 ? null : data.get(0);
    }

    private Serializable getKey(RepairParam param){
        return param.getRepairId();
    }

    private Page<RepairResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Repair getOldEntity(RepairParam param) {
        return this.getById(getKey(param));
    }

    private Repair getEntity(RepairParam param) {
        Repair entity = new Repair();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}