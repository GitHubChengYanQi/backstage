package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Contract;
import cn.atsoft.dasheng.app.mapper.ContractMapper;
import cn.atsoft.dasheng.app.model.params.ContractParam;
import cn.atsoft.dasheng.app.model.result.ContractResult;
import cn.atsoft.dasheng.app.service.ContractService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
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
 * 合同表 服务实现类
 * </p>
 *
 * @author
 * @since 2021-07-21
 */
@Service
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements ContractService {
    @Autowired
    private CustomerService customerService;

    @Override
    @BussinessLog
    public Contract add(ContractParam param) {
        QueryWrapper<Customer> queryWrapperA = new QueryWrapper<>();
        queryWrapperA.in("customer_id", param.getPartyA());
        List<Customer> aList = customerService.list(queryWrapperA);
        for (Customer customer1 : aList) {
            if (customer1.getCustomerId().equals(param.getPartyA())) {
                Contract entity = getEntity(param);
                this.save(entity);
                return entity;
            }
        }
        throw new ServiceException(500, "数据不存在");
    }

    @Override
    @BussinessLog
    public Contract delete(ContractParam param) {
        QueryWrapper<Customer> queryWrapperA = new QueryWrapper<>();
        queryWrapperA.in("customer_id", param.getPartyA());
        List<Customer> aList = customerService.list(queryWrapperA);
        for (Customer customer1 : aList) {
            if (customer1.getCustomerId().equals(param.getPartyA())) {
                this.removeById(getKey(param));
                Contract entity = getEntity(param);
                return entity;
            }
        }
        throw new ServiceException(500, "数据不存在");


    }

    @Override
    @BussinessLog
    public Contract update(ContractParam param) {
        Contract oldEntity = getOldEntity(param);
        if (ToolUtil.isEmpty(oldEntity)){
            throw new ServiceException(500, "数据不存在");
        }
        Contract newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(oldEntity);
        return oldEntity;
    }

    @Override
    public ContractResult findBySpec(ContractParam param) {
        return null;
    }

    @Override
    public List<ContractResult> findListBySpec(ContractParam param) {
        return null;
    }

    @Override
    public PageInfo<ContractResult> findPageBySpec(ContractParam param) {
        Page<ContractResult> pageContext = getPageContext();
        IPage<ContractResult> page = this.baseMapper.customPageList(pageContext, param);
        List<Long> partA = new ArrayList<>();
        List<Long> partB = new ArrayList<>();
        for (ContractResult record : page.getRecords()) {
            partA.add(record.getPartyA());
            partB.add(record.getPartyB());
        }
        QueryWrapper<Customer> customerQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Customer> customerQueryWrapper1 = new QueryWrapper<>();
        QueryWrapper<Customer> customerQueryWrapper1= new QueryWrapper<>();
        QueryWrapper<Customer> partAWapper = customerQueryWrapper.in("customer_id", partA);
        QueryWrapper<Customer> partBWapper = customerQueryWrapper1.in("customer_id", partB);
        List<Customer> partAList = partA.size() == 0 ? new ArrayList<>() : customerService.list(partAWapper);
        List<Customer> partBList = partA.size() == 0 ? new ArrayList<>() : customerService.list(partBWapper);
        ContractResult contractResult = new ContractResult();
        for (ContractResult record : page.getRecords()) {
            for (Customer customer : partAList) {

                if (record.getPartyA().equals(customer.getCustomerId())) {
                    record.setPartAName(customer.getCustomerName());

                }
                for (Customer customer1 : partBList) {
                    if (record.getPartyB().equals(customer1.getCustomerId())) {
                        record.setPartBName(customer1.getCustomerName());

                    }
                }
            }
        }
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ContractParam param) {
        return param.getContractId();
    }

    private Page<ContractResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Contract getOldEntity(ContractParam param) {
        return this.getById(getKey(param));
    }

    private Contract getEntity(ContractParam param) {
        Contract entity = new Contract();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public void batchDelete(List<Long> contractId) {
        Contract contract = new Contract();
        contract.setDisplay(0);
        UpdateWrapper<Contract> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("contract_id", contractId);
        this.update(contract, updateWrapper);
    }

}
