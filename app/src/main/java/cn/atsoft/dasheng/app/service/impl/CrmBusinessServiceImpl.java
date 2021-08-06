package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.result.*;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.mapper.CrmBusinessMapper;
import cn.atsoft.dasheng.app.model.params.CrmBusinessParam;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.sys.core.constant.dictmap.UserDict;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.atsoft.dasheng.app.model.result.SysUserResult;
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
    public CrmBusinessResult findBySpec(CrmBusinessParam param) {

        Page<CrmBusinessResult> pageContext = getPageContext();
        IPage<CrmBusinessResult> page = this.baseMapper.customPageList(pageContext, param);
        List<Long> list = new ArrayList<>();
        List<Long> list1 = new ArrayList<>();
        List<Long> list2 = new ArrayList<>();
        List<Long> list3 = new ArrayList<>();
        List<Long> tracklist = new ArrayList<>();
        List<Long> processlist = new ArrayList<>();

        for (CrmBusinessResult item : page.getRecords()) {
            list.add(item.getCustomerId());
            list1.add(item.getOriginId());
            list2.add(item.getSalesId());

            tracklist.add(item.getTrackId());
            processlist.add(item.getProcessId());
        }
        QueryWrapper<CrmBusinessSalesProcess> processQueryWrapper = new QueryWrapper<>();
        processQueryWrapper.in("sales_process_id",processlist);
        List<CrmBusinessSalesProcess> process = crmBusinessSalesProcessService.list(processQueryWrapper);


        QueryWrapper<CrmBusinessTrack> trackQueryWrapper =  new QueryWrapper<>();
        trackQueryWrapper.in("track_id",tracklist);
        List<CrmBusinessTrack> tracks = crmBusinessTrackService.list(trackQueryWrapper);

        //   QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        //    userQueryWrapper.in("user_id", list3);

        QueryWrapper<CrmBusinessSales> crmBusinessSalesQueryWrapper = new QueryWrapper();
        crmBusinessSalesQueryWrapper.in("sales_id", list2);

        //  List<User> userList = userService.list(userQueryWrapper);
        List<CrmBusinessSales> salesList = crmBusinessSalesService.list(crmBusinessSalesQueryWrapper);
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("customer_id", list);
        List<Customer> customerList = customerService.list(queryWrapper);
        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper.in("origin_id", list1);
        List<Origin> originList = originService.list(queryWrapper1);
        for (CrmBusinessResult item : page.getRecords()) {
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
                for (CrmBusinessTrack track : tracks) {
                    if(track.getTrackId().equals(item.getTrackId())){
                        CrmBusinessTrackResult crmBusinessTrackResult = new CrmBusinessTrackResult();
                        ToolUtil.copyProperties(track,crmBusinessTrackResult);
                        item.setTrack(crmBusinessTrackResult);
                        break;
                    }
                    for (CrmBusinessSalesProcess crmBusinessSalesProcess : process) {
                        if(item.getProcessId().equals(crmBusinessSalesProcess.getSalesProcessId())){
                            CrmBusinessSalesProcessResult crmBusinessSalesProcessResult = new CrmBusinessSalesProcessResult();
                            ToolUtil.copyProperties(crmBusinessSalesProcess,crmBusinessSalesProcessResult);
                            item.setProcess(crmBusinessSalesProcessResult);
                            break;
                        }
                    }
                }
            }
            //          for (User user : userList) {
            //                 User user1 = new User();
            //                  ToolUtil.copyProperties(user1, SysUserResults);
            //                 item.setUser(user1);
            //                break;
            //        }
        }


        return page.getRecords().get(0);
    }

    @Override
    public List<CrmBusinessResult> findListBySpec(CrmBusinessParam param) {
        return null;
    }

    @Override
    public PageInfo<CrmBusinessResult> findPageBySpec(CrmBusinessParam param) {
        Page<CrmBusinessResult> pageContext = getPageContext();
        IPage<CrmBusinessResult> page = this.baseMapper.customPageList(pageContext, param);
        List<Long> list = new ArrayList<>();
        List<Long> list1 = new ArrayList<>();
        List<Long> list2 = new ArrayList<>();
        List<Long> list3 = new ArrayList<>();
        List<Long> tracklist = new ArrayList<>();
        List<Long> processlist = new ArrayList<>();

        for (CrmBusinessResult item : page.getRecords()) {
            list.add(item.getCustomerId());
            list1.add(item.getOriginId());
            list2.add(item.getSalesId());

            tracklist.add(item.getTrackId());
            processlist.add(item.getProcessId());
        }
        QueryWrapper<CrmBusinessSalesProcess> processQueryWrapper = new QueryWrapper<>();
        processQueryWrapper.in("sales_process_id",processlist);
        List<CrmBusinessSalesProcess> process = crmBusinessSalesProcessService.list(processQueryWrapper);


        QueryWrapper<CrmBusinessTrack> trackQueryWrapper =  new QueryWrapper<>();
        trackQueryWrapper.in("track_id",tracklist);
        List<CrmBusinessTrack> tracks = crmBusinessTrackService.list(trackQueryWrapper);

     //   QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
    //    userQueryWrapper.in("user_id", list3);

        QueryWrapper<CrmBusinessSales> crmBusinessSalesQueryWrapper = new QueryWrapper();
        crmBusinessSalesQueryWrapper.in("sales_id", list2);

      //  List<User> userList = userService.list(userQueryWrapper);
        List<CrmBusinessSales> salesList = crmBusinessSalesService.list(crmBusinessSalesQueryWrapper);
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("customer_id", list);
        List<Customer> customerList = customerService.list(queryWrapper);
        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper.in("origin_id", list1);
        List<Origin> originList = originService.list(queryWrapper1);
        for (CrmBusinessResult item : page.getRecords()) {
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
                for (CrmBusinessTrack track : tracks) {
                    if(track.getTrackId().equals(item.getTrackId())){
                        CrmBusinessTrackResult crmBusinessTrackResult = new CrmBusinessTrackResult();
                        ToolUtil.copyProperties(track,crmBusinessTrackResult);
                        item.setTrack(crmBusinessTrackResult);
                        break;
                    }
                    for (CrmBusinessSalesProcess crmBusinessSalesProcess : process) {
                        if(item.getProcessId().equals(crmBusinessSalesProcess.getSalesProcessId())){
                            CrmBusinessSalesProcessResult crmBusinessSalesProcessResult = new CrmBusinessSalesProcessResult();
                            ToolUtil.copyProperties(crmBusinessSalesProcess,crmBusinessSalesProcessResult);
                            item.setProcess(crmBusinessSalesProcessResult);
                            break;
                        }
                    }
                }
            }
  //          for (User user : userList) {
   //                 User user1 = new User();
  //                  ToolUtil.copyProperties(user1, SysUserResults);
   //                 item.setUser(user1);
    //                break;
    //        }
        }


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

}
