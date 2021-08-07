package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.mapper.CustomerMapper;
import cn.atsoft.dasheng.app.model.params.CustomerParam;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户管理表 服务实现类
 * </p>
 *
 * @author
 * @since 2021-07-23
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {
    private CustomerResult customerResult;

    @Override
    public Long add(CustomerParam param) {
        Customer entity = getEntity(param);
        this.save(entity);
        return entity.getCustomerId();
    }

    @Override
    public void delete(CustomerParam param) {
        Customer oldEntity = getOldEntity(param);
        Customer newEntity = getEntity(param);
        boolean equals = oldEntity.getCustomerId().equals(newEntity.getCustomerId());
        if (equals) {
            newEntity.setDisplay(1);
            ToolUtil.copyProperties(newEntity, oldEntity);
            this.updateById(newEntity);
        }
//        this.removeById(getKey(param));
    }

    @Override
    public void update(CustomerParam param) {
        Customer oldEntity = getOldEntity(param);
        Customer newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public CustomerResult findBySpec(CustomerParam param) {
        Page<CustomerResult> pageContext = getPageContext();
        IPage<CustomerResult> page = this.baseMapper.customPageList(pageContext, param);
        PageInfo<CustomerResult> pageInfo = PageFactory.createPageInfo(page);
        return pageInfo.getData().get(0);
    }

    @Override
    public List<CustomerResult> findListBySpec(CustomerParam param) {
        return null;
    }

    @Override
    public PageInfo<CustomerResult> findPageBySpec(CustomerParam param) {
        Page<CustomerResult> pageContext = getPageContext();
        IPage<CustomerResult> page = this.baseMapper.customPageList(pageContext, param);
        for (CustomerResult record : page.getRecords()) {
            Integer classification = record.getClassification();
            if (classification == 1) {
                record.setClassificationName("代理商");
            } else {
                record.setClassificationName("终端客户");
            }
        }
        return PageFactory.createPageInfo(page);
    }


    private Serializable getKey(CustomerParam param) {
        return param.getCustomerId();
    }

    private Page<CustomerResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Customer getOldEntity(CustomerParam param) {
        return this.getById(getKey(param));
    }

    private Customer getEntity(CustomerParam param) {
        Customer entity = new Customer();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public Long addCustomer(CustomerParam param) {
        Customer entity = getEntity(param);
        this.save(entity);
        return entity.getCustomerId();
    }

    @Override
    public void batchDelete(List<Long> paramList) {
        Customer customer = new Customer();
        customer.setDisplay(0);
        UpdateWrapper<Customer> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("customerResult", paramList);
        this.update(customer,updateWrapper);
    }

}
