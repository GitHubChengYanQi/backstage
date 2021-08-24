package cn.atsoft.dasheng.portal.dispatching.service.impl;


import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.portal.dispatching.entity.Dispatching;
import cn.atsoft.dasheng.portal.dispatching.mapper.DispatchingMapper;
import cn.atsoft.dasheng.portal.dispatching.model.params.DispatchingParam;
import cn.atsoft.dasheng.portal.dispatching.model.result.DispatchingResult;
import cn.atsoft.dasheng.portal.dispatching.service.DispatchingService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.portal.dispatching.service.WxTemplate;
import cn.atsoft.dasheng.portal.repair.entity.Repair;
import cn.atsoft.dasheng.portal.repair.service.RepairService;
import cn.atsoft.dasheng.portal.wxUser.entity.WxuserInfo;
import cn.atsoft.dasheng.portal.wxUser.service.WxuserInfoService;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
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
 * 派工表 服务实现类
 * </p>
 *
 * @author
 * @since 2021-08-23
 */
@Service
public class DispatchingServiceImpl extends ServiceImpl<DispatchingMapper, Dispatching> implements DispatchingService {
    @Autowired
    private RepairService repairService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private WxuserInfoService wxuserInfoService;
    @Autowired
    private WxTemplate wxTemplate;
    @Autowired
    private UserService userService;

    @Override
    public void add(DispatchingParam param) {
        Dispatching entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(DispatchingParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(DispatchingParam param) {
        Dispatching oldEntity = getOldEntity(param);
        Dispatching newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public DispatchingResult findBySpec(DispatchingParam param) {
        return null;
    }

    @Override
    public List<DispatchingResult> findListBySpec(DispatchingParam param) {
        return null;
    }

    @Override
    public PageInfo<DispatchingResult> findPageBySpec(DispatchingParam param) {
        Page<DispatchingResult> pageContext = getPageContext();
        IPage<DispatchingResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    @Override
    public String addwx(DispatchingParam param) {
        QueryWrapper<Repair> repairQueryWrapper = new QueryWrapper<>();
        repairQueryWrapper.in("repair_id", param.getRepairId());
        List<WxMaSubscribeMessage.MsgData> data = new ArrayList();
        List<Repair> repairs = repairService.list(repairQueryWrapper);
        //查询报修 获取报修数据
        for (Repair repair : repairs) {
            data.add(new WxMaSubscribeMessage.MsgData("name", repair.getPeople()));
            data.add(new WxMaSubscribeMessage.MsgData("address", repair.getAddress()));
            String telephone = String.valueOf(repair.getTelephone());
            data.add(new WxMaSubscribeMessage.MsgData("phone", telephone));
            String reateTime = String.valueOf(repair.getCreateTime());
            DateTime parse = DateUtil.parse(reateTime);
            String time = String.valueOf(parse);
            data.add(new WxMaSubscribeMessage.MsgData("time", time));
            //查询保修单位
            QueryWrapper<Customer> customerQueryWrapper = new QueryWrapper<>();
            customerQueryWrapper.in("customer_id", repair.getCustomerId());
            List<Customer> customers = customerService.list(customerQueryWrapper);
            String repairName = null;
            for (Customer customer : customers) {
                if (customer.getCustomerId().equals(repair.getCustomerId())) {
                    repairName = customer.getCustomerName();
                    data.add(new WxMaSubscribeMessage.MsgData("repairName", repairName));
                }
            }
        }
        //查询uuit
        String openid = null;
        QueryWrapper<WxuserInfo> wxuserInfoQueryWrapper = new QueryWrapper<>();
        wxuserInfoQueryWrapper.in("user_id", param.getName());
        List<WxuserInfo> wxuserInfoList = wxuserInfoService.list(wxuserInfoQueryWrapper);
        for (WxuserInfo wxuserInfo : wxuserInfoList) {
            String uuid = wxuserInfo.getUuid();
            openid = uuid;
        }
        if (openid != null && data.size() != 0) {
            //调用订阅消息方法

//        wxTemplate.send(openid, templateId, page, data);
            return "订阅成功";
        } else {
            throw new ServiceException(500, "订阅失败");
        }

    }

    @Override
    public List<DispatchingResult> getAll(DispatchingParam param) {
        List<DispatchingResult> dispatchingResults = this.baseMapper.customList(param);
        List<Long> ids = new ArrayList<>();
        for (DispatchingResult dispatchingResult : dispatchingResults) {
            ids.add(dispatchingResult.getName());
        }
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.in("user_id", ids);
        List<User> users = userService.list(userQueryWrapper);

        for (DispatchingResult dispatchingResult : dispatchingResults) {
            for (User user : users) {
                if (user.getUserId().equals(dispatchingResult.getName())) {
                    UserResult userResult = new UserResult();
                    ToolUtil.copyProperties(user, userResult);
                    dispatchingResult.setUserResult(userResult);
                    break;
                }
            }
        }

        return dispatchingResults;
    }


    private Serializable getKey(DispatchingParam param) {
        return param.getDispatchingId();
    }

    private Page<DispatchingResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Dispatching getOldEntity(DispatchingParam param) {
        return this.getById(getKey(param));
    }

    private Dispatching getEntity(DispatchingParam param) {
        Dispatching entity = new Dispatching();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
