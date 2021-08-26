package cn.atsoft.dasheng.api.protal.controller;

import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.entity.Delivery;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.app.service.DeliveryService;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.common_area.entity.CommonArea;
import cn.atsoft.dasheng.common_area.model.result.CommonAreaResult;
import cn.atsoft.dasheng.common_area.service.CommonAreaService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.portal.banner.entity.Banner;
import cn.atsoft.dasheng.portal.banner.model.params.BannerParam;
import cn.atsoft.dasheng.portal.banner.model.result.BannerResult;
import cn.atsoft.dasheng.portal.banner.service.BannerService;
import cn.atsoft.dasheng.portal.dispatching.entity.Dispatching;
import cn.atsoft.dasheng.portal.dispatching.model.params.DispatchingParam;
import cn.atsoft.dasheng.portal.dispatching.model.result.DispatchingResult;
import cn.atsoft.dasheng.portal.dispatching.service.DispatchingService;
import cn.atsoft.dasheng.portal.repair.entity.Repair;
import cn.atsoft.dasheng.portal.repair.model.params.RepairParam;
import cn.atsoft.dasheng.portal.repair.model.result.RegionResult;
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
    @Autowired
    private CommonAreaService commonAreaService;

    @RequestMapping(value = "/RepairistAll", method = RequestMethod.POST)
    @ApiOperation("列表")
    public List<RepairResult> listAll(@RequestBody(required = false) RepairParam repairParam) {
        if (ToolUtil.isEmpty(repairParam)) {
            repairParam = new RepairParam();
        }
        return this.repairService.findListBySpec(repairParam);
    }

    @RequestMapping(value = "/dispatchingUpdate", method = RequestMethod.POST)
    public ResponseData dispatchingUpdate(@RequestBody DispatchingParam dispatchingParam) {
        this.dispatchingService.update(dispatchingParam);
        return ResponseData.success();
    }

    @RequestMapping(value = "/repairUpdate", method = RequestMethod.POST)
    public ResponseData repairUpdate(@RequestBody RepairParam repairParam) {
        this.repairService.update(repairParam);
        return ResponseData.success();
    }

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

    @RequestMapping(value = "/updateRepair", method = RequestMethod.POST)
    public ResponseData updateRepair(@RequestBody RepairParam repairParam) {
        Repair update = this.repairService.update(repairParam);
        return ResponseData.success(update);
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
        //查询工程师
        Long name = dispatchingParam.getName().longValue();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.in("user_id", name);
        List<User> users = userService.list(userQueryWrapper);


        QueryWrapper<Dispatching> dispatchingQueryWrapper = new QueryWrapper<>();
        dispatchingQueryWrapper.in("name", name).in("state",0).orderByAsc("create_time");
        List<Dispatching> list = this.dispatchingService.list(dispatchingQueryWrapper);
        List<RepairResult> res = new ArrayList<>();
        //公司id
        List<Long> companyIds = new ArrayList<>();
        if (ToolUtil.isNotEmpty(list)) {
            for (Dispatching data : list) {

                Repair repair = this.repairService.getById(data.getRepairId());
                RepairResult result = new RepairResult();
                result.setRepairId(repair.getRepairId());

                //查询报修获取公司id
                QueryWrapper<Repair> repairQueryWrapper = new QueryWrapper<>();
                repairQueryWrapper.in("repair_id", data.getRepairId());
                List<Repair> repairs = repairService.list(repairQueryWrapper);
                for (Repair repair1 : repairs) {
                    companyIds.add(repair1.getCompanyId());
                }
                //查询公司
                QueryWrapper<Customer> customerQueryWrapper = new QueryWrapper<>();
                customerQueryWrapper.in("customer_id", companyIds);
                List<Customer> customers = customerService.list(customerQueryWrapper);
                for (Customer customer : customers) {
                    CustomerResult customerResult = new CustomerResult();
                    ToolUtil.copyProperties(customer, customerResult);
                    result.setCompany(customerResult);
                    break;
                }
                // 查询图片
                QueryWrapper<Banner> bannerQueryWrapper = new QueryWrapper<>();
                bannerQueryWrapper.in("difference", data.getRepairId());
                List<Banner> banners = bannerService.list(bannerQueryWrapper);
                List<BannerResult> bannerList = new ArrayList<>();
                for (Banner banner : banners) {
                    BannerResult bannerResult = new BannerResult();
                    ToolUtil.copyProperties(banner, bannerResult);
                    bannerList.add(bannerResult);
                }
                result.setBannerResult(bannerList);

                //获取地址
                QueryWrapper<CommonArea> areaQueryWrapper = new QueryWrapper<>();
                areaQueryWrapper.in("id", repair.getArea());
                List<CommonArea> commonAreas = this.commonAreaService.list(areaQueryWrapper);
                List<RegionResult> regionList = new ArrayList<>();
                for (CommonArea commonArea : commonAreas) {
                    Long recordArea = repair.getArea() == null ? null : Long.valueOf(repair.getArea());
                    RegionResult regionResult = new RegionResult();
                    Long id = Long.valueOf(commonArea.getId());
                    if (id == recordArea) {

                        QueryWrapper<CommonArea> AreaQueryWrapper = new QueryWrapper<>();
                        AreaQueryWrapper.in("parentid", commonArea.getId());

                        CommonAreaResult commonAreaResult = new CommonAreaResult();
                        ToolUtil.copyProperties(commonArea, commonAreaResult);
                        regionResult.setArea(commonAreaResult.getTitle());

                        QueryWrapper<CommonArea> commonAreaQueryWrapper = new QueryWrapper<>();
                        commonAreaQueryWrapper.in("id", commonAreaResult.getParentid());
                        List<CommonArea> cityList = commonAreaService.list(commonAreaQueryWrapper);

                        for (CommonArea area : cityList) {
                            CommonAreaResult city = new CommonAreaResult();
                            ToolUtil.copyProperties(area, city);
                            regionResult.setCity(city.getTitle());

                            QueryWrapper<CommonArea> commonAreaQueryWrapper1 = new QueryWrapper<>();
                            commonAreaQueryWrapper1.in("id", city.getParentid());
                            List<CommonArea> provinceList = commonAreaService.list(commonAreaQueryWrapper1);

                            for (CommonArea commonArea1 : provinceList) {
                                CommonAreaResult province = new CommonAreaResult();
                                ToolUtil.copyProperties(commonArea1, province);
                                regionResult.setProvince(province.getTitle());
                                regionList.add(regionResult);
                            }
                        }
                        result.setRegionResult(regionList);
                    }
                }

                result.setCompanyId(repair.getCompanyId());
                result.setItemId(repair.getItemId());
                result.setCustomerId(repair.getCustomerId());
                result.setCustomerResult(result.getCustomerResult());
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



    @RequestMapping(value = "/getRepairAll", method = RequestMethod.POST)
    public ResponseData getRepairAll() {
        //查询工程师
        List<User> users = userService.list();
        List<Dispatching> list = this.dispatchingService.list();
        List<RepairResult> res = new ArrayList<>();
        //公司id
        List<Long> companyIds = new ArrayList<>();
        if (ToolUtil.isNotEmpty(list)) {
            for (Dispatching data : list) {

                Repair repair = this.repairService.getById(data.getRepairId());
                RepairResult result = new RepairResult();
                result.setRepairId(repair.getRepairId());
                //查询报修获取公司id
                QueryWrapper<Repair> repairQueryWrapper = new QueryWrapper<>();
                repairQueryWrapper.in("repair_id", data.getRepairId());
                List<Repair> repairs = repairService.list(repairQueryWrapper);
                for (Repair repair1 : repairs) {
                    companyIds.add(repair1.getCompanyId());
                }
                //查询公司
                QueryWrapper<Customer> customerQueryWrapper = new QueryWrapper<>();
                customerQueryWrapper.in("customer_id", companyIds);
                List<Customer> customers = customerService.list(customerQueryWrapper);
                for (Customer customer : customers) {
                    CustomerResult customerResult = new CustomerResult();
                    ToolUtil.copyProperties(customer, customerResult);
                    result.setCompany(customerResult);
                    break;
                }

                // 查询图片
                QueryWrapper<Banner> bannerQueryWrapper = new QueryWrapper<>();
                bannerQueryWrapper.in("difference", data.getRepairId());
                List<Banner> banners = bannerService.list(bannerQueryWrapper);
                List<BannerResult> bannerList = new ArrayList<>();
                for (Banner banner : banners) {
                    BannerResult bannerResult = new BannerResult();
                    ToolUtil.copyProperties(banner, bannerResult);
                    bannerList.add(bannerResult);
                }
                result.setBannerResult(bannerList);

                //获取地址
                QueryWrapper<CommonArea> areaQueryWrapper = new QueryWrapper<>();
                areaQueryWrapper.in("id", repair.getArea());
                List<CommonArea> commonAreas = this.commonAreaService.list(areaQueryWrapper);
                List<RegionResult> regionList = new ArrayList<>();
                for (CommonArea commonArea : commonAreas) {
                    Long recordArea = repair.getArea() == null ? null : Long.valueOf(repair.getArea());
                    RegionResult regionResult = new RegionResult();
                    Long id = Long.valueOf(commonArea.getId());
                    if (id == recordArea) {

                        QueryWrapper<CommonArea> AreaQueryWrapper = new QueryWrapper<>();
                        AreaQueryWrapper.in("parentid", commonArea.getId());

                        CommonAreaResult commonAreaResult = new CommonAreaResult();
                        ToolUtil.copyProperties(commonArea, commonAreaResult);
                        regionResult.setArea(commonAreaResult.getTitle());

                        QueryWrapper<CommonArea> commonAreaQueryWrapper = new QueryWrapper<>();
                        commonAreaQueryWrapper.in("id", commonAreaResult.getParentid());
                        List<CommonArea> cityList = commonAreaService.list(commonAreaQueryWrapper);

                        for (CommonArea area : cityList) {
                            CommonAreaResult city = new CommonAreaResult();
                            ToolUtil.copyProperties(area, city);
                            regionResult.setCity(city.getTitle());

                            QueryWrapper<CommonArea> commonAreaQueryWrapper1 = new QueryWrapper<>();
                            commonAreaQueryWrapper1.in("id", city.getParentid());
                            List<CommonArea> provinceList = commonAreaService.list(commonAreaQueryWrapper1);

                            for (CommonArea commonArea1 : provinceList) {
                                CommonAreaResult province = new CommonAreaResult();
                                ToolUtil.copyProperties(commonArea1, province);
                                regionResult.setProvince(province.getTitle());
                                regionList.add(regionResult);
                            }
                        }
                        result.setRegionResult(regionList);
                    }
                }

                result.setCompanyId(repair.getCompanyId());
                result.setItemId(repair.getItemId());
                result.setCustomerId(repair.getCustomerId());
                result.setCustomerResult(result.getCustomerResult());
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