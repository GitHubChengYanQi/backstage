package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.result.*;
import cn.atsoft.dasheng.app.service.ItemsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.mapper.OrderDetailsMapper;
import cn.atsoft.dasheng.app.model.params.OrderDetailsParam;
import  cn.atsoft.dasheng.app.service.OrderDetailsService;
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
 * 订单明细表 服务实现类
 * </p>
 *
 * @author siqiang
 * @since 2021-08-18
 */
@Service
public class OrderDetailsServiceImpl extends ServiceImpl<OrderDetailsMapper, OrderDetails> implements OrderDetailsService {
    @Autowired
    private ItemsService itemsService;

    @Override
    public void add(OrderDetailsParam param){
        OrderDetails entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(OrderDetailsParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(OrderDetailsParam param){
        OrderDetails oldEntity = getOldEntity(param);
        OrderDetails newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public OrderDetailsResult findBySpec(OrderDetailsParam param){
        return null;
    }

    @Override
    public List<OrderDetailsResult> findListBySpec(OrderDetailsParam param){
        return null;
    }

    @Override
    public PageInfo<OrderDetailsResult> findPageBySpec(OrderDetailsParam param, DataScope dataScope){
        Page<OrderDetailsResult> pageContext = getPageContext();
        IPage<OrderDetailsResult> page = this.baseMapper.customPageList(pageContext, param,dataScope);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(OrderDetailsParam param){
        return param.getId();
    }

    private Page<OrderDetailsResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private OrderDetails getOldEntity(OrderDetailsParam param) {
        return this.getById(getKey(param));
    }

    private OrderDetails getEntity(OrderDetailsParam param) {
        OrderDetails entity = new OrderDetails();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }
    public void format(List<OrderDetailsResult> data) {
        List<Long> itemIds = new ArrayList<>();

        for (OrderDetailsResult datum : data) {
            itemIds.add(datum.getItemId());
        }

        QueryWrapper<Items> itemsQueryWrapper = new QueryWrapper<>();
        if (!itemIds.isEmpty()) {
            itemsQueryWrapper.in("item_id", itemIds);
        }

        List<Items> itemsList = itemsService.list(itemsQueryWrapper);
        for (OrderDetailsResult datum : data) {
            if (ToolUtil.isNotEmpty(itemsList)) {
                for (Items items : itemsList) {
                    if (items.getItemId().equals(datum.getItemId())) {
                        ItemsResult itemsResult = new ItemsResult();
                        ToolUtil.copyProperties(items, itemsResult);
                        datum.setItemsResult(itemsResult);
                        break;
                    }
                }
            }

        }
    }

}
