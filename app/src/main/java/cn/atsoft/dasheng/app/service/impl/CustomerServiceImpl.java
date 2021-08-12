package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.result.*;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.mapper.CustomerMapper;
import cn.atsoft.dasheng.app.model.params.CustomerParam;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
 * 客户管理表 服务实现类
 * </p>
 *
 * @author
 * @since 2021-07-23
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

    @Autowired
    private OriginService originService;
    @Autowired
    private CrmCustomerLevelService crmCustomerLevelService;
    @Autowired
    private UserService userService;
    @Autowired
    private CrmIndustryService crmIndustryService;

    @Autowired
    private CustomerDynamicService customerDynamicService;

    @Override
    @BussinessLog
    public Customer add(CustomerParam param) {
        Customer entity = getEntity(param);
        this.save(entity);
        return entity;

    }

    @Override
    @BussinessLog
    public Customer delete(CustomerParam param) {
        Customer customer = this.getById(param.getCustomerId());
        if (ToolUtil.isEmpty(customer)) {
            throw new ServiceException(500, "数据不存在");
        }else {
            param.setDisplay(0);
            this.update(param);
            Customer entity = getEntity(param);
            return  entity;
        }
    }

    @Override
    @BussinessLog
    public Customer update(CustomerParam param) {
        Customer oldEntity = getOldEntity(param);
        if (ToolUtil.isEmpty(oldEntity)){
            throw new ServiceException(500, "数据不存在");
        }else {
            Customer newEntity = getEntity(param);
            ToolUtil.copyProperties(newEntity, oldEntity);
            this.updateById(oldEntity);
            return oldEntity;
        }

    }

    @Override
    public CustomerResult findBySpec(CustomerParam param) {


        return null;

    }

    @Override
    public List<CustomerResult> findListBySpec(CustomerParam param) {
        return null;
    }

    @Override
    public PageInfo<CustomerResult> findPageBySpec(CustomerParam param) {
        Page<CustomerResult> pageContext = getPageContext();
        IPage<CustomerResult> page = this.baseMapper.customPageList(pageContext, param);

        format(page.getRecords());

        return PageFactory.createPageInfo(page);
    }

    public CustomerResult format(List<CustomerResult> data) {
        for (CustomerResult record : data) {
            Integer classification = record.getClassification();
            if (classification == 1) {
                record.setClassificationName("终端用户");
            } else {
                record.setClassificationName("代理商");
            }
        }
        List<Long> dycustomerIds = new ArrayList<>();
        List<Long> originIds = new ArrayList<>();
        List<Long> levelIds = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        List<Long> industryIds = new ArrayList<>();

        for (CustomerResult record : data) {
            originIds.add(record.getOriginId());
            levelIds.add(record.getCustomerLevelId());
            userIds.add(record.getUserId());
            industryIds.add(record.getIndustryId());
            dycustomerIds.add(record.getCustomerId());
        }

        /**
         * 获取originId
         * */
        QueryWrapper<Origin> originQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Origin> origin_id = originQueryWrapper.in("origin_id", originIds);
        List<Origin> originList = originIds.size() == 0 ? new ArrayList<>() : originService.list(origin_id);


        /**
         * 获取LevelId
         * */
        QueryWrapper<CrmCustomerLevel> levelQueryWrapper = new QueryWrapper<>();
        QueryWrapper<CrmCustomerLevel> customerLevelId = levelQueryWrapper.in("customer_level_id", levelIds);
        List<CrmCustomerLevel> levelList = levelIds.size() == 0 ? new ArrayList<>() : crmCustomerLevelService.list(customerLevelId);
        /**
         * 获取userId
         * */
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.in("user_id", userIds);
        List<User> userList = userIds.size() == 0 ? new ArrayList<>() : userService.list(userQueryWrapper);

        QueryWrapper<CrmIndustry> industryQueryWrapper = new QueryWrapper<>();
        industryQueryWrapper.in("industry_id", industryIds);
        List<CrmIndustry> industryList = industryIds.size() == 0 ? new ArrayList<>() : crmIndustryService.list(industryQueryWrapper);


        for (CustomerResult record : data) {
            for (Origin origin : originList) {
                if (origin.getOriginId().equals(record.getOriginId())) {
                    OriginResult originResult = new OriginResult();
                    ToolUtil.copyProperties(origin, originResult);
                    record.setOriginResult(originResult);
                    break;
                }
            }
            for (CrmCustomerLevel crmCustomerLevel : levelList) {
                if (crmCustomerLevel.getCustomerLevelId().equals(record.getCustomerLevelId())) {
                    CrmCustomerLevelResult crmCustomerLevelResult = new CrmCustomerLevelResult();
                    ToolUtil.copyProperties(crmCustomerLevel, crmCustomerLevelResult);
                    record.setCrmCustomerLevelResult(crmCustomerLevelResult);
                    break;
                }
            }
            for (User user : userList) {
                if (user.getUserId().equals(record.getUserId())) {
                    UserResult userResult = new UserResult();
                    ToolUtil.copyProperties(user, userResult);
                    record.setUserResult(userResult);
                    break;
                }
            }
            for (CrmIndustry crmIndustry : industryList) {
                if (crmIndustry.getIndustryId().equals(record.getIndustryId())) {
                    CrmIndustryResult crmIndustryResult = new CrmIndustryResult();
                    ToolUtil.copyProperties(crmIndustry, crmIndustryResult);
                    record.setCrmIndustryResult(crmIndustryResult);
                    break;
                }
            }
        }
        return data.size() == 0 ? null : data.get(0);
    }


    private Serializable getKey(CustomerParam param) {
        return param.getCustomerId();
    }

    private Page<CustomerResult> getPageContext() {
        List<String> fields = new ArrayList<String>() {{
            add("createTime");
        }};
        return PageFactory.defaultPage(fields);
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
    public void batchDelete(List<Long> customerId) {
        Customer customer = new Customer();
        customer.setDisplay(0);
        UpdateWrapper<Customer> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("customer_id", customerId);
        this.update(customer, updateWrapper);
    }

    @Override
    public void updateStatus(CustomerParam customerParam) {
    }

    @Override
    public CustomerResult detail(Long id) {
        Customer customer = this.getById(id);
        CustomerResult customerResult = new CustomerResult();
        ToolUtil.copyProperties(customer, customerResult);
        List<CustomerResult> results = new ArrayList<CustomerResult>() {{
            add(customerResult);
        }};
        this.format(results);
        return results.get(0);
    }



}
