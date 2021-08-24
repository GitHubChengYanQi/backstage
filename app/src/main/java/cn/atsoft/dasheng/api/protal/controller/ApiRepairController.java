package cn.atsoft.dasheng.api.protal.controller;

import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.portal.banner.entity.Banner;
import cn.atsoft.dasheng.portal.banner.model.params.BannerParam;
import cn.atsoft.dasheng.portal.banner.service.BannerService;
import cn.atsoft.dasheng.portal.repair.entity.Repair;
import cn.atsoft.dasheng.portal.repair.model.params.RepairParam;
import cn.atsoft.dasheng.portal.repair.service.RepairService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "/saveRepair", method = RequestMethod.POST)
    public ResponseData saveRepair(@RequestBody RepairParam repairParam) {
        Repair repair = this.repairService.add(repairParam);
        List<Banner> banner = repairParam.getItemImgUrlList();
        for (Banner data : banner){
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
        customerQueryWrapper.in("display",1);
        List<Customer> list = customerService.list(customerQueryWrapper);
        return list;
    }
}