package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.ErpOrder;
import cn.atsoft.dasheng.app.entity.Items;
import cn.atsoft.dasheng.app.entity.Outstock;
import cn.atsoft.dasheng.app.model.result.ErpOrderResult;
import cn.atsoft.dasheng.app.model.result.ItemsResult;
import cn.atsoft.dasheng.app.model.result.OutstockResult;
import cn.atsoft.dasheng.app.service.ErpOrderService;
import cn.atsoft.dasheng.app.service.ItemsService;
import cn.atsoft.dasheng.app.service.OutstockService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.CrmPayment;
import cn.atsoft.dasheng.app.mapper.CrmPaymentMapper;
import cn.atsoft.dasheng.app.model.params.CrmPaymentParam;
import cn.atsoft.dasheng.app.model.result.CrmPaymentResult;
import cn.atsoft.dasheng.app.service.CrmPaymentService;
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
 * 付款信息表 服务实现类
 * </p>
 *
 * @author ta
 * @since 2021-07-26
 */
@Service
public class CrmPaymentServiceImpl extends ServiceImpl<CrmPaymentMapper, CrmPayment> implements CrmPaymentService {
    @Autowired
    private ItemsService itemsService;
    @Autowired
    public ErpOrderService orderService;
    @Autowired
    private OutstockService outstockService;

    @Override
    public void add(CrmPaymentParam param) {
        CrmPayment entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(CrmPaymentParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(CrmPaymentParam param) {
        CrmPayment oldEntity = getOldEntity(param);
        CrmPayment newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public CrmPaymentResult findBySpec(CrmPaymentParam param) {
        return null;
    }

    @Override
    public List<CrmPaymentResult> findListBySpec(CrmPaymentParam param) {
        return null;
    }

    @Override
    public PageInfo<CrmPaymentResult> findPageBySpec(CrmPaymentParam param) {
        Page<CrmPaymentResult> pageContext = getPageContext();
        IPage<CrmPaymentResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(CrmPaymentParam param) {
        return param.getPaymentId();
    }

    private Page<CrmPaymentResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private CrmPayment getOldEntity(CrmPaymentParam param) {
        return this.getById(getKey(param));
    }

    private CrmPayment getEntity(CrmPaymentParam param) {
        CrmPayment entity = new CrmPayment();
        ToolUtil.copyProperties(param, entity);

        return entity;
    }

    public void format(List<CrmPaymentResult> data) {
        List<Long> oriderIds = new ArrayList<>();
        List<Long> itemIds = new ArrayList<>();
        List<Long> outstockIds = new ArrayList<>();
        for (CrmPaymentResult datum : data) {
            oriderIds.add(datum.getOrderId());
            itemIds.add(datum.getItemId());
            outstockIds.add(datum.getOutstockId());

        }
        QueryWrapper<ErpOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("order_id", oriderIds);
        List<ErpOrder> orderList =oriderIds.size()==0? new ArrayList<>(): orderService.list(queryWrapper);

        QueryWrapper<Items> itemsQueryWrapper = new QueryWrapper<>();
        itemsQueryWrapper.in("item_id", itemIds);
        List<Items> itemList =itemIds.size()==0?new ArrayList<>(): itemsService.list(itemsQueryWrapper);

        QueryWrapper<Outstock> outstockQueryWrapper = new QueryWrapper<>();
        outstockQueryWrapper.in("outstock_id",outstockIds);
        List<Outstock> outList =outstockIds.size()==0?new ArrayList<>(): outstockService.list(outstockQueryWrapper);

        for (CrmPaymentResult datum : data) {
            for (ErpOrder erpOrder : orderList) {
                if (datum.getOrderId().equals(erpOrder.getOrderId())) {
                    ErpOrderResult erpOrderResult = new ErpOrderResult();
                    ToolUtil.copyProperties(erpOrder, erpOrderResult);
                    datum.setOrderResult(erpOrderResult);
                    break;
                }
            }
            for (Items items : itemList) {
                if (datum.getItemId().equals(items.getItemId())) {
                    ItemsResult itemsResult = new ItemsResult();
                    ToolUtil.copyProperties(items, itemsResult);
                    datum.setItemsResult(itemsResult);
                    break;
                }
            }
            for (Outstock outstock : outList) {
                if (outstock.getOutstockId().equals(datum.getOutstockId())) {
                    OutstockResult outstockResult = new OutstockResult();
                    ToolUtil.copyProperties(outstock,outstockResult);
                    datum.setOutstockResult(outstockResult);
                    break;
                }
            }
        }
    }
}
