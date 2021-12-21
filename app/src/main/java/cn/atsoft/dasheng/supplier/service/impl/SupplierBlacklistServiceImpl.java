package cn.atsoft.dasheng.supplier.service.impl;


import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.supplier.entity.SupplierBlacklist;
import cn.atsoft.dasheng.supplier.mapper.SupplierBlacklistMapper;
import cn.atsoft.dasheng.supplier.model.params.SupplierBlacklistParam;
import cn.atsoft.dasheng.supplier.model.result.SupplierBlacklistResult;
import  cn.atsoft.dasheng.supplier.service.SupplierBlacklistService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 供应商黑名单 服务实现类
 * </p>
 *
 * @author Captian_Jazz
 * @since 2021-12-20
 */
@Service
public class SupplierBlacklistServiceImpl extends ServiceImpl<SupplierBlacklistMapper, SupplierBlacklist> implements SupplierBlacklistService {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService userService;
    @Override
    public void add(SupplierBlacklistParam param){
        SupplierBlacklist entity = getEntity(param);
        this.save(entity);
        //TODO 将客户表中的所选拉入黑名单的 客户信息 添加一个黑名单字段  并且 更新状态
        Customer customer = new Customer();
        customer.setCustomerId(param.getSupplierId());
        customer.setBlacklist(1);
        customerService.updateById(customer);
    }

    @Override
    public void delete(SupplierBlacklistParam param){
        param.setDisplay(0);
        SupplierBlacklist entity = getEntity(param);
        this.updateById(entity);

        SupplierBlacklist byId = this.getById(param.getBlackListId());
        Customer customer = new Customer();
        customer.setCustomerId(byId.getSupplierId());
        customer.setBlacklist(0);
        customerService.updateById(customer);
    }

    @Override
    public void update(SupplierBlacklistParam param){
        SupplierBlacklist oldEntity = getOldEntity(param);
        SupplierBlacklist newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public SupplierBlacklistResult findBySpec(SupplierBlacklistParam param){
        return null;
    }

    @Override
    public List<SupplierBlacklistResult> findListBySpec(SupplierBlacklistParam param){
        return null;
    }

    @Override
    public PageInfo<SupplierBlacklistResult> findPageBySpec(SupplierBlacklistParam param){
        Page<SupplierBlacklistResult> pageContext = getPageContext();
        IPage<SupplierBlacklistResult> page = this.baseMapper.customPageList(pageContext, param);
        this.format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }
    public void format(List<SupplierBlacklistResult> param) {
        List<Long> userIds = new ArrayList<>();
        List<Long> customerIds = new ArrayList<>();
        for (SupplierBlacklistResult supplierBlacklistResult : param) {
            userIds.add(supplierBlacklistResult.getCreateUser());
            customerIds.add(supplierBlacklistResult.getSupplierId());
        }
        List<User> userList = userIds.size() > 0 ? userService.listByIds(userIds) : new ArrayList<>();
        List<Customer> customerList = customerIds.size() > 0 ? customerService.listByIds(customerIds) : new ArrayList<>();

        for (SupplierBlacklistResult supplierBlacklistResult : param) {
            for (Customer customer : customerList) {
                if (supplierBlacklistResult.getSupplierId().equals(customer.getCustomerId())) {
                    CustomerResult customerResult = new CustomerResult();
                    ToolUtil.copyProperties(customer,customerResult);
                    supplierBlacklistResult.setCustomerResult(customerResult);
                }
            }
            for (User user : userList) {
                if (supplierBlacklistResult.getCreateUser().equals(user.getUserId())) {
//                    UserResult userResult = new UserResult();
//                    ToolUtil.copyProperties(user,userResult);
                    supplierBlacklistResult.setCreateUserName(user.getName());
                }
            }
        }
    }

    private Serializable getKey(SupplierBlacklistParam param){
        return param.getBlackListId();
    }

    private Page<SupplierBlacklistResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private SupplierBlacklist getOldEntity(SupplierBlacklistParam param) {
        return this.getById(getKey(param));
    }

    private SupplierBlacklist getEntity(SupplierBlacklistParam param) {
        SupplierBlacklist entity = new SupplierBlacklist();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
