package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.BusinessDynamicParam;
import cn.atsoft.dasheng.app.model.params.ContractDetailParam;
import cn.atsoft.dasheng.app.model.params.CrmBusinessTrackParam;
import cn.atsoft.dasheng.app.model.result.*;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.log.FreedLog;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.mapper.CrmBusinessMapper;
import cn.atsoft.dasheng.app.model.params.CrmBusinessParam;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.entity.BusinessCompetition;
import cn.atsoft.dasheng.crm.entity.Competitor;
import cn.atsoft.dasheng.crm.model.params.BusinessCompetitionParam;
import cn.atsoft.dasheng.crm.model.params.CompetitorParam;
import cn.atsoft.dasheng.crm.model.result.CompetitorResult;
import cn.atsoft.dasheng.crm.service.BusinessCompetitionService;
import cn.atsoft.dasheng.crm.service.CompetitorService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 商机表 服务实现类
 * </p>
 *
 * @author
 * @since 2021-08-03
 */
@Service
public class CrmBusinessServiceImpl extends ServiceImpl<CrmBusinessMapper, CrmBusiness> implements CrmBusinessService {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private OriginService originService;
    @Autowired
    private CrmBusinessSalesService crmBusinessSalesService;
    @Autowired
    private UserService userService;
    @Autowired
    private CrmBusinessSalesProcessService crmBusinessSalesProcessService;
    @Autowired
    private BusinessDynamicService businessDynamicService;
    @Autowired
    private CompetitorService competitorService;
    @Autowired
    private BusinessCompetitionService businessCompetitionService;
    @Autowired
    private ContractService contractService;
    @Autowired
    private CrmBusinessDetailedService crmBusinessDetailedService;
    @Autowired
    private ContractDetailService contractDetailService;


    public CrmBusinessResult detail(Long id) {

        CrmBusiness crmBusiness = this.getById(id);
        CrmBusinessResult detail = new CrmBusinessResult();
        ToolUtil.copyProperties(crmBusiness, detail);
        List<CrmBusinessResult> crmBusinessResults = new ArrayList<CrmBusinessResult>() {{
            add(detail);
        }};
        this.format(crmBusinessResults);
        return crmBusinessResults.get(0);
    }

    @Override
    @FreedLog
    public CrmBusiness add(CrmBusinessParam param) {
        CrmBusiness entity = getEntity(param);
        this.save(entity);
        return entity;


    }

    @Override
    @FreedLog
    public CrmBusiness updateChargePerson(CrmBusinessParam param) {
        CrmBusiness crmBusiness = new CrmBusiness();
        crmBusiness.setBusinessId(param.getBusinessId());
        crmBusiness.setUserId(param.getUserId());
        this.updateById(crmBusiness);
        return crmBusiness;
    }

    @Override
    @FreedLog
    public CrmBusiness delete(CrmBusinessParam param) {
        CrmBusiness business = this.getById(param.getBusinessId());
        if (ToolUtil.isEmpty(business)) {
            throw new ServiceException(500, "数据不存在");
        } else {
            param.setDisplay(0);
            this.update(param);
            CrmBusiness entity = getEntity(param);
            return entity;
        }

    }


    @Override
    @FreedLog
    public CrmBusiness update(CrmBusinessParam param) {
        //获取合同id
        Long contractId = param.getContractId();
        //判断合同id是否为空
        if (ToolUtil.isNotEmpty(contractId) && ToolUtil.isNotEmpty(param.getBusinessId())) {
            //合同id不为空，查询商机明细
            QueryWrapper<CrmBusinessDetailed> crmBusinessDetailedQueryWrapper = new QueryWrapper<>();
            crmBusinessDetailedQueryWrapper.in("business_id",param.getBusinessId());
            List<CrmBusinessDetailed> list = crmBusinessDetailedService.list(crmBusinessDetailedQueryWrapper);

            //判断该商机明细是否为空
            if (list.size() > 0){
                //把商机明细添加到合同明细

                for (CrmBusinessDetailed crmBusinessDetailed : list) {
                    ContractDetailParam contractDetail = new ContractDetailParam();
                    contractDetail.setContractId(contractId);
                    contractDetail.setSkuId(crmBusinessDetailed.getItemId());
                    contractDetail.setBrandId(crmBusinessDetailed.getBrandId());
                    contractDetail.setQuantity(crmBusinessDetailed.getQuantity());
                    contractDetail.setSalePrice(crmBusinessDetailed.getSalePrice());
                    contractDetail.setTotalPrice(crmBusinessDetailed.getTotalPrice());

                    contractDetailService.add(contractDetail);
                }
            }
        }

        CrmBusiness oldEntity = getOldEntity(param);
        if (ToolUtil.isEmpty(oldEntity)) {
            throw new ServiceException(500, "数据不存在");
        } else {
            CrmBusiness newEntity = getEntity(param);
            ToolUtil.copyProperties(newEntity, oldEntity);
            this.updateById(oldEntity);
            return oldEntity;
        }


    }

    @Override
    public String UpdateStatus(CrmBusinessParam param) {
        CrmBusiness oldEntity = getOldEntity(param);
        CrmBusiness newEntity = getEntity(param);
        Page<CrmBusinessResult> pageContext = getPageContext();
        IPage<CrmBusinessResult> page = this.baseMapper.customPageList(pageContext, param, null);
        List<Long> processIds = new ArrayList<>();

        for (CrmBusinessResult record : page.getRecords()) {
            processIds.add(record.getProcessId());

        }
        QueryWrapper<CrmBusinessSalesProcess> processQueryWrapper = new QueryWrapper<>();
        processQueryWrapper.in("sales_process_id");


        List<CrmBusinessSalesProcess> list = crmBusinessSalesProcessService.list();
        List<BusinessDynamic> businessDynamics = businessDynamicService.list();

        CrmBusinessTrackParam crmBusinessTrackParam = new CrmBusinessTrackParam();
        if (param.getState() != null && param.getState().equals("赢单")) {
            param.setProcessId(100L);
            oldEntity = getOldEntity(param);
            newEntity = getEntity(param);
            ToolUtil.copyProperties(newEntity, oldEntity);
            this.updateById(newEntity);

        } else if (param.getState() != null && param.getState().equals("输单")) {
            param.setProcessId(0L);
            oldEntity = getOldEntity(param);
            newEntity = getEntity(param);
            ToolUtil.copyProperties(newEntity, oldEntity);
            this.updateById(newEntity);
        }

        if (newEntity.getBusinessId().equals(oldEntity.getBusinessId())) {
            for (CrmBusinessSalesProcess crmBusinessSalesProcess : list) {
                if (param.getProcessId().equals(crmBusinessSalesProcess.getSalesProcessId())) {
                    crmBusinessTrackParam.setBusinessId(newEntity.getBusinessId());
                    crmBusinessTrackParam.setTrackId(newEntity.getTrackId());
//                    crmBusinessTrackParam.setNote("状态已更新：" + crmBusinessSalesProcess.getName());
//                    crmBusinessTrackService.add(crmBusinessTrackParam);
                    BusinessDynamicParam businessDynamicParam = new BusinessDynamicParam();
                    businessDynamicParam.setBusinessId(newEntity.getBusinessId());
                    businessDynamicParam.setContent("状态已更新" + crmBusinessSalesProcess.getName());
                    businessDynamicService.add(businessDynamicParam);
                }
            }


        }
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
        return "状态已更新";
    }

    @Override
    public CrmBusinessResult findBySpec(CrmBusinessParam param) {
        Page<CrmBusinessResult> pageContext = getPageContext();
        IPage<CrmBusinessResult> page = this.baseMapper.customPageList(pageContext, param, null);
        this.format(page.getRecords());

        return this.format(page.getRecords());
    }

    @Override
    public List<CrmBusinessResult> findListBySpec(CrmBusinessParam param) {
        List<CrmBusinessResult> crmBusinessResults = this.baseMapper.customList(param);
        return crmBusinessResults;
    }

    @Override
    public PageInfo<CrmBusinessResult> findPageBySpec(DataScope dataScope, CrmBusinessParam param) {
        Page<CrmBusinessResult> pageContext = getPageContext();
        IPage<CrmBusinessResult> page = this.baseMapper.customPageList(pageContext, param, dataScope);
        this.format(page.getRecords());

        return PageFactory.createPageInfo(page);
    }

    @Override
    public void batchDelete(List<Long> businessId) {
        CrmBusiness business = new CrmBusiness();
        business.setDisplay(0);
        QueryWrapper<CrmBusiness> businessQueryWrapper = new QueryWrapper<>();
        businessQueryWrapper.in("business_id", businessId);
        this.update(business, businessQueryWrapper);

    }

    private Serializable getKey(CrmBusinessParam param) {
        return param.getBusinessId();
    }

    private Page<CrmBusinessResult> getPageContext() {
        List<String> fields = new ArrayList<>();
        fields.add("businessName");
        fields.add("time");
        fields.add("state");
        fields.add("opportunityAmount");
        return PageFactory.defaultPage(fields);
    }

    private CrmBusiness getOldEntity(CrmBusinessParam param) {
        return this.getById(getKey(param));
    }

    private CrmBusiness getEntity(CrmBusinessParam param) {
        CrmBusiness entity = new CrmBusiness();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    private CrmBusinessResult format(List<CrmBusinessResult> data) {

        List<Long> cids = new ArrayList<>();
        List<Long> OriginIds = new ArrayList<>();
        List<Long> salesIds = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        List<Long> trackList = new ArrayList<>();
        List<Long> processIds = new ArrayList<>();
        List<Long> businessIds = new ArrayList<>();

        List<Long> contractIds = new ArrayList<>();

        for (CrmBusinessResult item : data) {
            cids.add(item.getCustomerId());
            OriginIds.add(item.getOriginId());
            salesIds.add(item.getSalesId());
            userIds.add(item.getUserId());
            trackList.add(item.getTrackId());
            processIds.add(item.getProcessId());
            businessIds.add(item.getBusinessId());
            contractIds.add(item.getContractId());
        }


        /**
         * 获取负责人
         */
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();

        userQueryWrapper.in("user_id", userIds);
        List<User> userList = userIds.size() == 0 ? new ArrayList<>() : userService.list(userQueryWrapper);


        /**
         *获取流程id
         */

        List<CrmBusinessSalesResult> salesList = salesIds.size() == 0 ? new ArrayList<>() : crmBusinessSalesService.getByIds(salesIds);
        /**
         *获取客户id
         */
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("customer_id", cids);
        List<Customer> customerList = cids.size() == 0 ? new ArrayList<>() : customerService.list(queryWrapper);
        /**
         * 获取来源id
         * */
        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper.in("origin_id", OriginIds);
        List<Origin> originList = OriginIds.size() == 0 ? new ArrayList<>() : originService.list(queryWrapper1);

        QueryWrapper<CrmBusinessSalesProcess> processQueryWrapper = new QueryWrapper<>();
        processQueryWrapper.in("sales_process_id", processIds);
        List<CrmBusinessSalesProcess> processList = processIds.size() == 0 ? new ArrayList<>() : crmBusinessSalesProcessService.list(processQueryWrapper);

        QueryWrapper<Contract> contractQueryWrapper = new QueryWrapper<>();
        contractQueryWrapper.in("contract_id", contractIds);
        List<Contract> contractList = contractIds.size() == 0 ? new ArrayList<>() : contractService.list(contractQueryWrapper);


        for (CrmBusinessResult item : data) {
            if (item.getBusinessId() != null) {
                List<Long> competitorIds = new ArrayList<>();
                List<BusinessCompetition> businessCompetitionList = businessCompetitionService.lambdaQuery().in(BusinessCompetition::getBusinessId, item.getBusinessId()).list();
                for (BusinessCompetition businessCompetition : businessCompetitionList) {
                    competitorIds.add(businessCompetition.getCompetitorId());
                }
                if (ToolUtil.isNotEmpty(competitorIds)) {
                    List<Competitor> competitorList = competitorService.lambdaQuery().in(Competitor::getCompetitorId, competitorIds).list();
                    List<CompetitorResult> competitorResults = new ArrayList<>();
                    for (Competitor competitor : competitorList) {
                        CompetitorResult competitorResult = new CompetitorResult();
                        ToolUtil.copyProperties(competitor, competitorResult);
                        competitorResults.add(competitorResult);
                    }
                    item.setCompetitorResults(competitorResults);
                }

            }


            for (Customer customer : customerList) {
                if (item.getCustomerId() != null && item.getCustomerId().equals(customer.getCustomerId())) {
                    CustomerResult customerResult = new CustomerResult();
                    ToolUtil.copyProperties(customer, customerResult);
                    item.setCustomer(customerResult);
                    break;
                }
            }
            for (Contract contract : contractList) {
                if (item.getContractId() != null && item.getContractId().equals(contract.getContractId())) {
                    ContractResult contractResult = new ContractResult();
                    ToolUtil.copyProperties(contract, contractResult);
                    item.setContractResult(contractResult);
                    break;
                }
            }
            for (Origin origin : originList) {
                if (origin.getOriginId().equals(item.getOriginId())) {
                    OriginResult originResult = new OriginResult();
                    ToolUtil.copyProperties(origin, originResult);
                    item.setOrigin(originResult);
                    break;
                }
            }
            for (CrmBusinessSalesResult crmBusinessSales : salesList) {
                if (crmBusinessSales.getSalesId().equals(item.getSalesId())) {
                    item.setSales(crmBusinessSales);
                    break;
                }

            }

            for (User user1 : userList) {
                if (user1.getUserId().equals(item.getUserId())) {
                    UserResult userResult = new UserResult();
                    ToolUtil.copyProperties(user1, userResult);
                    item.setUser(userResult);
                    break;
                }
            }
            for (CrmBusinessSalesProcess crmBusinessSalesProcess : processList) {
                if (crmBusinessSalesProcess.getSalesProcessId().equals(item.getProcessId())) {
                    CrmBusinessSalesProcessResult crmBusinessSalesProcessResult = new CrmBusinessSalesProcessResult();
                    ToolUtil.copyProperties(crmBusinessSalesProcess, crmBusinessSalesProcessResult);
                    item.setProcess(crmBusinessSalesProcessResult);
                    break;
                }
            }

        }
        return data.size() == 0 ? null : data.get(0);
    }

}
