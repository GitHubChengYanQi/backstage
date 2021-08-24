package cn.atsoft.dasheng.api.protal.controller;

import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.entity.Delivery;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.app.service.DeliveryService;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.portal.banner.entity.Banner;
import cn.atsoft.dasheng.portal.banner.model.params.BannerParam;
import cn.atsoft.dasheng.portal.banner.service.BannerService;
import cn.atsoft.dasheng.portal.dispatching.entity.Dispatching;
import cn.atsoft.dasheng.portal.dispatching.model.params.DispatchingParam;
import cn.atsoft.dasheng.portal.dispatching.model.result.DispatchingResult;
import cn.atsoft.dasheng.portal.dispatching.service.DispatchingService;
import cn.atsoft.dasheng.portal.repair.entity.Repair;
import cn.atsoft.dasheng.portal.repair.model.params.RepairParam;
import cn.atsoft.dasheng.portal.repair.model.result.RepairResult;
import cn.atsoft.dasheng.portal.repair.service.RepairService;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiRepairController {

    @Autowired
    private RepairService repairService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private BannerService bannerService;
    @Autowired
    private DispatchingService dispatchingService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/saveRepair", method = RequestMethod.POST)
    public ResponseData saveRepair(@RequestBody RepairParam repairParam) {
        Repair repair = this.repairService.add(repairParam);
        List<Banner> banner = repairParam.getItemImgUrlList();
        for (Banner data : banner) {
            BannerParam bannerParam = new BannerParam();
            bannerParam.setDifference(repair.getRepairId());
            bannerParam.setImgUrl(data.getImgUrl());
            this.bannerService.add(bannerParam);
        }
        return ResponseData.success(repair.getRepairId());
    }

    @RequestMapping(value = "/customerList", method = RequestMethod.POST)
    public List<Customer> list() {
        QueryWrapper<Customer> customerQueryWrapper = new QueryWrapper<>();
        customerQueryWrapper.in("display", 1);
        List<Customer> list = customerService.list(customerQueryWrapper);
        return list;
    }

    @RequestMapping(value = "/getRepair", method = RequestMethod.POST)
    public ResponseData getRepair(@RequestBody(required = false) DispatchingParam dispatchingParam) {

        Long name = dispatchingParam.getName().longValue();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.in("user_id", name);
        List<User> users = userService.list(userQueryWrapper);


        QueryWrapper<Dispatching> dispatchingQueryWrapper = new QueryWrapper<>();
        dispatchingQueryWrapper.in("name", name);
        List<Dispatching> list = this.dispatchingService.list(dispatchingQueryWrapper);
        List<RepairResult> res = new ArrayList<>();
        if (ToolUtil.isNotEmpty(list)) {
            for (Dispatching data : list) {

                Repair repair = this.repairService.getById(data.getRepairId());
                RepairResult result = new RepairResult();
                result.setRepairId(repair.getRepairId());
                result.setCompanyId(repair.getCompanyId());
                result.setItemId(repair.getItemId());
                result.setServiceType(repair.getServiceType());
                result.setExpectTime(repair.getExpectTime());
                result.setProgress(repair.getProgress());
                result.setMoney(repair.getMoney());
                result.setQualityType(repair.getQualityType());
                result.setContractType(repair.getContractType());
                result.setNumber(repair.getNumber());
                result.setProvince(repair.getProvince());
                result.setCity(repair.getCity());
                result.setArea(repair.getArea());
                result.setAddress(repair.getAddress());
                result.setPeople(repair.getPeople());
                result.setPosition(repair.getPosition());
                result.setTelephone(repair.getTelephone());
                result.setComment(repair.getComment());
                result.setDispatchingResult(data);
                for (User user : users) {
                    if (user.getUserId().equals(data.getName())) {
                        UserResult userResult = new UserResult();
                        ToolUtil.copyProperties(user, userResult);
                        result.setUserResult(userResult);
                        break;
                    }
                }
                res.add(result);

            }
        }

        this.repairService.format(res);

        return ResponseData.success(res);
    }

}