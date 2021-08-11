package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Contacts;
import cn.atsoft.dasheng.app.mapper.ContactsMapper;
import cn.atsoft.dasheng.app.model.params.ContactsParam;
import cn.atsoft.dasheng.app.model.result.ContactsResult;
import  cn.atsoft.dasheng.app.service.ContactsService;
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
 * 联系人表 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-07-23
 */
@Service
public class ContactsServiceImpl extends ServiceImpl<ContactsMapper, Contacts> implements ContactsService {
@Autowired
private CustomerService customerService;

    @Override
    @BussinessLog
    public Long add(ContactsParam param){
        Contacts entity = getEntity(param);
        this.save(entity);
        return entity.getContactsId();
    }

    @Override
    @BussinessLog
    public void delete(ContactsParam param){
        this.removeById(getKey(param));
    }

    @Override
    @BussinessLog
    public void update(ContactsParam param){
        Contacts oldEntity = getOldEntity(param);
        Contacts newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ContactsResult findBySpec(ContactsParam param){
        return null;
    }

    @Override
    public List<ContactsResult> findListBySpec(ContactsParam param){
        return null;
    }

    @Override
    public PageInfo<ContactsResult> findPageBySpec(ContactsParam param){
        Page<ContactsResult> pageContext = getPageContext();
        IPage<ContactsResult> page = this.baseMapper.customPageList(pageContext, param);
        List<Long> cIds =  new ArrayList<>();
        for (ContactsResult record : page.getRecords()) {
            cIds.add(record.getCustomerId());
        }
        QueryWrapper<Customer> customerQueryWrapper = new QueryWrapper<>();
        customerQueryWrapper.in("customer_id",cIds);
        List<Customer> customerList = customerService.list(customerQueryWrapper);
        for (ContactsResult record : page.getRecords()) {
            for (Customer customer : customerList) {
                if(record.getCustomerId()!=null&&record.getCustomerId().equals(customer.getCustomerId())){
                    CustomerResult customerResult = new CustomerResult();
                    ToolUtil.copyProperties(customer,customerResult);
                    record.setCustomerResult(customerResult);
                    break;
                }
            }
        }
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ContactsParam param){
        return param.getContactsId();
    }

    private Page<ContactsResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Contacts getOldEntity(ContactsParam param) {
        return this.getById(getKey(param));
    }

    private Contacts getEntity(ContactsParam param) {
        Contacts entity = new Contacts();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
