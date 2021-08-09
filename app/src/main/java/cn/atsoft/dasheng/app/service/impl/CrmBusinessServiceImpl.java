package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.CrmBusinessSalesProcessParam;
import cn.atsoft.dasheng.app.model.params.CrmBusinessTrackParam;
import cn.atsoft.dasheng.app.model.result.*;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.mapper.CrmBusinessMapper;
import cn.atsoft.dasheng.app.model.params.CrmBusinessParam;
import cn.atsoft.dasheng.core.util.ToolUtil;
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
    private CrmBusinessTrackService crmBusinessTrackService;
    @Autowired
    private CrmBusinessSalesProcessService crmBusinessSalesProcessService;


    @Override
    public Long add(CrmBusinessParam param) {
        CrmBusiness entity = getEntity(param);
        this.save(entity);
        return entity.getBusinessId();

    }

    @Override
    public void delete(CrmBusinessParam param) {
        this.removeById(getKey(param));

    }

    @Override
    public void update(CrmBusinessParam param) {
        CrmBusiness oldEntity = getOldEntity(param);
        CrmBusiness newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public String UpdateStatus(CrmBusinessParam param) {
        CrmBusiness oldEntity = getOldEntity(param);
        CrmBusiness newEntity = getEntity(param);
        Page<CrmBusinessResult> pageContext = getPageContext();
        IPage<CrmBusinessResult> page = this.baseMapper.customPageList(pageContext, param);
        List<Long> processIds = new ArrayList<>();
        List<Long>personIds = new ArrayList<>();
        for (CrmBusinessResult record : page.getRecords()) {
            processIds.add(record.getProcessId());
            personIds.add(record.getPerson());
        }
        QueryWrapper<CrmBusinessSalesProcess> processQueryWrapper = new QueryWrapper<>();
        processQueryWrapper.in("sales_process_id", processIds);
        List<CrmBusinessSalesProcess> processList =  processIds.size()==0? new ArrayList<>() : crmBusinessSalesProcessService.list(processQueryWrapper);

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.in("user_id",personIds);
        List<User> userList = userService.list(userQueryWrapper);


        CrmBusinessTrackParam crmBusinessTrackParam = new CrmBusinessTrackParam();


        if (newEntity.getBusinessId().equals(oldEntity.getBusinessId())) {
            for (CrmBusinessResult record : page.getRecords()) {
                for (CrmBusinessSalesProcess crmBusinessSalesProcess : processList) {
                    for (User user : userList) {
                        if(user.getUserId().equals(record.getPerson())){
                            record.setPersonName(user.getName());
                        }

                    }
                    if (record.getProcessId().equals(crmBusinessSalesProcess.getSalesProcessId())) {
                        crmBusinessTrackParam.setBusinessId(newEntity.getBusinessId());
                        crmBusinessTrackParam.setNote(crmBusinessSalesProcess.getName());
                        crmBusinessTrackService.add(crmBusinessTrackParam);
//                        CrmBusinessSalesProcessParam crmBusinessSalesProcessParam = new CrmBusinessSalesProcessParam();
                        if(record.getStage().equals("赢率")){
                            param.setProcessId(5L);
                            CrmBusiness oldEntity1 = getOldEntity(param);
                            CrmBusiness newEntity1 = getEntity(param);
                            ToolUtil.copyProperties(newEntity1, oldEntity1);
                            this.updateById(newEntity1);
//                            crmBusinessSalesProcessParam.setWinRate(1L);
//                            crmBusinessSalesProcessService.update(crmBusinessSalesProcessParam);
                        }else {
                            param.setProcessId(0L);
                            CrmBusiness oldEntity1 = getOldEntity(param);
                            CrmBusiness newEntity1 = getEntity(param);
                            ToolUtil.copyProperties(newEntity1, oldEntity1);
                            this.updateById(newEntity1);
//                            crmBusinessSalesProcessParam.setSalesId(0L);
//                            crmBusinessSalesProcessService.update(crmBusinessSalesProcessParam);
                        }

                        ToolUtil.copyProperties(newEntity, oldEntity);
                        this.updateById(newEntity);
                    }
                }
            }
        }
        return  "状态已更新";
    }

    @Override
    public CrmBusinessResult findBySpec(CrmBusinessParam param) {
        Page<CrmBusinessResult> pageContext = getPageContext();
        IPage<CrmBusinessResult> page = this.baseMapper.customPageList(pageContext, param);
        this.format(page.getRecords());

        return this.format(page.getRecords());
    }

    @Override
    public List<CrmBusinessResult> findListBySpec(CrmBusinessParam param) {
        return null;
    }

    @Override
    public PageInfo<CrmBusinessResult> findPageBySpec(CrmBusinessParam param) {
        Page<CrmBusinessResult> pageContext = getPageContext();
        IPage<CrmBusinessResult> page = this.baseMapper.customPageList(pageContext, param);
        this.format(page.getRecords());

        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(CrmBusinessParam param) {
        return param.getBusinessId();
    }

    private Page<CrmBusinessResult> getPageContext() {
        return PageFactory.defaultPage();
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

        for (CrmBusinessResult item : data) {
            cids.add(item.getCustomerId());
            OriginIds.add(item.getOriginId());
            salesIds.add(item.getSalesId());
            userIds.add(item.getPerson());
            trackList.add(item.getTrackId());
            processIds.add(item.getProcessId());
        }
        /**
         * 获取负责人
         */
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();

        userQueryWrapper.in("user_id", userIds);
        List<User> userList = userIds.size() == 0 ? new ArrayList<>() : userService.list(userQueryWrapper);


        /**
         *货期商机详情id
         */
        QueryWrapper<CrmBusinessTrack> trackQueryWrapper = new QueryWrapper<>();
        trackQueryWrapper.in("track_id", trackList);
        List<CrmBusinessTrack> tracks = trackList.size() == 0 ? new ArrayList<>() : crmBusinessTrackService.list(trackQueryWrapper);

        /**
         *获取流程id
         */
        QueryWrapper<CrmBusinessSales> crmBusinessSalesQueryWrapper = new QueryWrapper();
        crmBusinessSalesQueryWrapper.in("sales_id", salesIds);
        List<CrmBusinessSales> salesList = salesIds.size() == 0 ? new ArrayList<>() : crmBusinessSalesService.list(crmBusinessSalesQueryWrapper);
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


        for (CrmBusinessResult item : data) {
            for (Customer customer : customerList) {
                if (item.getCustomerId().equals(customer.getCustomerId())) {
                    CustomerResult customerResult = new CustomerResult();
                    ToolUtil.copyProperties(customer, customerResult);
                    item.setCustomer(customerResult);
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
            for (CrmBusinessSales crmBusinessSales : salesList) {
                if (crmBusinessSales.getSalesId().equals(item.getSalesId())) {
                    CrmBusinessSalesResult crmBusinessSalesResult = new CrmBusinessSalesResult();
                    ToolUtil.copyProperties(crmBusinessSales, crmBusinessSalesResult);
                    item.setSales(crmBusinessSalesResult);
                    break;
                }

            }

            for (CrmBusinessTrack track : tracks) {
                if (track.getTrackId().equals(item.getTrackId())) {
                    CrmBusinessTrackResult crmBusinessTrackResult = new CrmBusinessTrackResult();
                    ToolUtil.copyProperties(track, crmBusinessTrackResult);
                    item.setTrack(crmBusinessTrackResult);
                    break;
                }

            }

            for (User user1 : userList) {
                if (user1.getUserId().equals(item.getPerson())) {
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
