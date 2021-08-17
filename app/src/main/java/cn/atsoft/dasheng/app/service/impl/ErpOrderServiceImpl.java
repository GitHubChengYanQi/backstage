package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.result.*;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.mapper.ErpOrderMapper;
import cn.atsoft.dasheng.app.model.params.ErpOrderParam;
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
 * 订单表 服务实现类
 * </p>
 *
 * @author ta
 * @since 2021-07-26
 */
@Service
public class ErpOrderServiceImpl extends ServiceImpl<ErpOrderMapper, ErpOrder> implements ErpOrderService {
    @Autowired
    private OutstockService outstockService;
    @Autowired
    private ContactsService contactsService;
    @Autowired
    private ItemsService itemsService;
    @Autowired
    private CustomerService customerService;

    @BussinessLog
    @Override
    public ErpOrder add(ErpOrderParam param) {
        ErpOrder entity = getEntity(param);
        this.save(entity);
        return entity;
    }

    @BussinessLog
    @Override
    public void delete(ErpOrderParam param) {

        ErpOrder byId = this.getById(param.getOrderId());
        if (ToolUtil.isEmpty(byId)) {
            throw new ServiceException(500, "删除目标不存在");
        }
        param.setDisplay(0);
        ErpOrder oldEntity = getOldEntity(param);
        ErpOrder newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @BussinessLog
    @Override
    public void update(ErpOrderParam param) {
        ErpOrder oldEntity = getOldEntity(param);
        ErpOrder newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(oldEntity);

    }



    @Override
    public ErpOrderResult findBySpec(ErpOrderParam param) {
        return null;
    }

    @Override
    public List<ErpOrderResult> findListBySpec(ErpOrderParam param) {
        return null;
    }

    @Override
    public PageInfo<ErpOrderResult> findPageBySpec(ErpOrderParam param) {
        Page<ErpOrderResult> pageContext = getPageContext();
        IPage<ErpOrderResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ErpOrderParam param) {
        return param.getOrderId();
    }

    private Page<ErpOrderResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ErpOrder getOldEntity(ErpOrderParam param) {
        return this.getById(getKey(param));
    }

    private ErpOrder getEntity(ErpOrderParam param) {
        ErpOrder entity = new ErpOrder();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    public void format(List<ErpOrderResult> data) {
        List<Long> outstockIds = new ArrayList<>();
        List<Long> contactsIds = new ArrayList<>();
        List<Long> itemIds = new ArrayList<>();
        List<Long> customerIds = new ArrayList<>();
        for (ErpOrderResult datum : data) {
//            outstockIds.add(datum.getOutstockId());
//            contactsIds.add(datum.getContactsId());
            itemIds.add(datum.getItemId());
            customerIds.add(datum.getCustomerId());
        }

        QueryWrapper<Outstock> outstockQueryWrapper = new QueryWrapper<>();
        if (!outstockIds.isEmpty()) {
            outstockQueryWrapper.in("outstock_id", outstockIds);
        }
        List<Outstock> outstockList = outstockService.list(outstockQueryWrapper);


        QueryWrapper<Contacts> contactsQueryWrapper = new QueryWrapper<>();
        if (!contactsIds.isEmpty()) {
            contactsQueryWrapper.in("contacts_id", contactsIds);
        }
        List<Contacts> conList = contactsService.list(contactsQueryWrapper);

        QueryWrapper<Items> itemsQueryWrapper = new QueryWrapper<>();
        if (!itemIds.isEmpty()) {
            itemsQueryWrapper.in("item_id", itemIds);
        }

        List<Items> itemsList = itemsService.list(itemsQueryWrapper);

        QueryWrapper<Customer> customerQueryWrapper = new QueryWrapper<>();
        if (!customerIds.isEmpty()) {
            customerQueryWrapper.in("customer_id", customerIds);
        }
        List<Customer> cuList = customerService.list(customerQueryWrapper);
        for (ErpOrderResult datum : data) {
//            if (!outstockList.isEmpty()) {
//                for (Outstock outstock : outstockList) {
//                    if (datum.getOutstockId().equals(outstock.getOutstockId())) {
//                        OutstockResult outstockResult = new OutstockResult();
//                        ToolUtil.copyProperties(outstock, outstockResult);
//                        datum.setOutstockResult(outstockResult);
//                        break;
//                    }
//                }
//            }
//            if (!conList.isEmpty()) {
//                for (Contacts contacts : conList) {
//                    if (contacts.getContactsId().equals(datum.getContactsId())) {
//                        ContactsResult contactsResult = new ContactsResult();
//                        ToolUtil.copyProperties(contacts, contactsResult);
//                        datum.setContactsResult(contactsResult);
//                        break;
//                    }
//                }
//            }
            if (!itemsList.isEmpty()) {
                for (Items items : itemsList) {
                    if (items.getItemId().equals(datum.getItemId())) {
                        ItemsResult itemsResult = new ItemsResult();
                        ToolUtil.copyProperties(items, itemsResult);
                        datum.setItemsResult(itemsResult);
                        break;
                    }
                }
            }
            if (!cuList.isEmpty()) {
                for (Customer customer : cuList) {
                    if (customer.getCustomerId().equals(datum.getCustomerId())) {
                        CustomerResult customerResult = new CustomerResult();
                        ToolUtil.copyProperties(customer, customerResult);
                        datum.setCustomerResult(customerResult);
                        break;
                    }
                }
            }
        }
    }
}
