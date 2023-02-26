package cn.atsoft.dasheng.api.protal.controller;

import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.appBase.entity.Media;
import cn.atsoft.dasheng.appBase.service.MediaService;
import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.auth.context.LoginContext;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.commonArea.entity.CommonArea;
import cn.atsoft.dasheng.commonArea.model.result.CommonAreaResult;
import cn.atsoft.dasheng.commonArea.service.CommonAreaService;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.entity.DataClassification;
import cn.atsoft.dasheng.crm.model.params.DataClassificationParam;
import cn.atsoft.dasheng.crm.model.params.DataParam;
import cn.atsoft.dasheng.crm.model.result.DataClassificationResult;
import cn.atsoft.dasheng.crm.model.result.DataResult;
import cn.atsoft.dasheng.crm.service.DataClassificationService;
import cn.atsoft.dasheng.crm.service.DataService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.portal.dispatChing.model.params.DispatchingParam;
import cn.atsoft.dasheng.portal.dispatChing.model.result.DispatchingResult;
import cn.atsoft.dasheng.portal.dispatChing.service.DispatchingService;
import cn.atsoft.dasheng.portal.repair.entity.Repair;
import cn.atsoft.dasheng.portal.repair.model.params.RepairParam;
import cn.atsoft.dasheng.crm.region.RegionResult;
import cn.atsoft.dasheng.portal.repair.model.result.RepairResult;
import cn.atsoft.dasheng.portal.repair.service.RepairSendTemplate;
import cn.atsoft.dasheng.portal.repair.service.RepairService;
import cn.atsoft.dasheng.portal.repairImage.entity.RepairImage;
import cn.atsoft.dasheng.portal.repairImage.model.params.RepairImageParam;
import cn.atsoft.dasheng.portal.repairImage.model.result.RepairImageResult;
import cn.atsoft.dasheng.portal.repairImage.service.RepairImageService;
import cn.atsoft.dasheng.binding.wxUser.entity.WxuserInfo;
import cn.atsoft.dasheng.binding.wxUser.service.WxuserInfoService;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.atsoft.dasheng.uc.jwt.UcJwtPayLoad;
import cn.atsoft.dasheng.uc.utils.UserUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.ibatis.annotations.Param;
import org.jacoco.agent.rt.internal_035b120.core.internal.flow.IProbeIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiRepairController {

    @Autowired
    private RepairService repairService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private DispatchingService dispatchingService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommonAreaService commonAreaService;

    @Autowired
    private MediaService mediaService;
    @Autowired
    private WxuserInfoService wxuserInfoService;
    @Autowired
    private RepairImageService repairImageService;
    @Autowired
    private RepairSendTemplate repairSendTemplate;



    public Long getWxUser(Long memberId) {

        UcJwtPayLoad ucJwtPayLoad = UserUtils.getPayLoad();
        String type = ucJwtPayLoad.getType();

        QueryWrapper<WxuserInfo> wxuserInfoQueryWrapper = new QueryWrapper<>();

        if (ToolUtil.isEmpty(type)) {
//            wxuserInfoQueryWrapper.in("source", "wxCp").in("user_id", memberId);
            wxuserInfoQueryWrapper.in("user_id", memberId);
        } else {
            wxuserInfoQueryWrapper.in("source", "wxMp").in("member_id", memberId);
        }

        List<WxuserInfo> userList = wxuserInfoService.list(wxuserInfoQueryWrapper);
        for (WxuserInfo data : userList) {
            return data.getUserId();
        }

        return null;
    }

    @RequestMapping(value = "/getRepairOrder", method = RequestMethod.POST)
    public ResponseData getRepairOrder() {
        Long userId = getWxUser(UserUtils.getUserId());
        if (ToolUtil.isEmpty(userId)) {
            throw new ServiceException(403, "此账户未绑定，请先进行绑定!");
        }

        Boolean permission = false;

        permission = wxuserInfoService.sendPermissions(0L, userId);
        if (!permission) {
            return ResponseData.success();
        } else {
            RepairParam repairParam = new RepairParam();
            PageInfo repairResult = repairService.findMyPageBySpec(repairParam);
            return ResponseData.success(repairResult);

        }
    }

    @RequestMapping(value = "/getMyRepair", method = RequestMethod.POST)
    public ResponseData getMyRepair() {
        Long userId = getWxUser(UserUtils.getUserId());
        if (ToolUtil.isEmpty(userId)) {
            throw new ServiceException(403, "此账户未绑定，请先进行绑定!");
        }


        RepairParam repairParam = new RepairParam();
        repairParam.setCreateUser(userId);
        repairParam.setName(UserUtils.getUserId());
        PageInfo repairList = repairService.findPageBySpec(repairParam);
        return ResponseData.success(repairList);
    }

    @RequestMapping(value = "/getRepairAll", method = RequestMethod.POST)
    public ResponseData getRepairAll() {
        Long userId = getWxUser(UserUtils.getUserId());
        if (ToolUtil.isEmpty(userId)) {
            throw new ServiceException(403, "此账户未绑定，请先进行绑定!");
        }
        Boolean permission = false;
        for (int i = 0; i < 5; i++) {
            if (i != 1) {
                permission = wxuserInfoService.sendPermissions((long) i, userId);
            }
            if (!permission) {
                break;
            }
        }
        if (permission) {
            RepairParam repairParam = new RepairParam();
            PageInfo repairList = repairService.findPageBySpec(repairParam);
            return ResponseData.success(repairList);
        } else {
            return ResponseData.success();
        }
    }

    @RequestMapping(value = "/dispatchingUpdate", method = RequestMethod.POST)
    public ResponseData dispatchingUpdate(@RequestBody DispatchingParam dispatchingParam) {
        this.dispatchingService.update(dispatchingParam);
        return ResponseData.success();
    }

    @RequestMapping(value = "/repairUpdate", method = RequestMethod.POST)
    public ResponseData repairUpdate(@RequestBody RepairParam repairParam) throws WxErrorException {
        this.repairService.update(repairParam);
        return ResponseData.success();
    }

    @RequestMapping(value = "/saveRepair", method = RequestMethod.POST)
    public ResponseData saveRepair(@RequestBody RepairParam repairParam) throws WxErrorException {

        repairParam.setName(UserUtils.getUserId());

        Repair entity = getEntity(repairParam);
        this.repairService.save(entity);
        List<RepairImage> repairImages = repairParam.getItemImgUrlList();
        for (RepairImage data : repairImages) {
            RepairImageParam repairImageParam = new RepairImageParam();
            repairImageParam.setRepairId(entity.getRepairId());
            repairImageParam.setImgUrl(data.getImgUrl());
            repairImageParam.setTitle(data.getTitle());
            this.repairImageService.add(repairImageParam);
        }

        repairParam.setRepairId(entity.getRepairId());
        repairParam.setCreateTime(entity.getCreateTime());
        repairParam.setProgress(0L);
        repairSendTemplate.setRepairParam(repairParam);
        repairSendTemplate.send();
        return ResponseData.success(entity.getRepairId());
    }

    @RequestMapping(value = "/updateRepair", method = RequestMethod.POST)
    @Transactional
    public ResponseData updateRepair(@RequestBody RepairParam repairParam) throws WxErrorException {

        Long userId = getWxUser(UserUtils.getUserId());
        if (ToolUtil.isEmpty(userId)) {
            throw new ServiceException(403, "此账户未绑定，请先进行绑定!");
        }

        Repair oldEntity = getOldEntity(repairParam);
        Repair newEntity = getEntity(repairParam);
        ToolUtil.copyProperties(newEntity, oldEntity);
        Boolean permission = false;
        // 判断权限
        permission = wxuserInfoService.sendPermissions((long) oldEntity.getProgress() - 1, userId);
        if (!permission) {
            throw new ServiceException(403, "当前用户没有此权限!");
        }
        this.repairService.updateById(newEntity);
        RepairParam Param = new RepairParam();
        Param.setProgress(repairParam.getProgress());
        Repair data = this.repairService.getById(repairParam.getRepairId());
        ToolUtil.copyProperties(Param, data);
        Param.setArea(data.getArea());
        Param.setName(UserUtils.getUserId());
        Param.setRepairId(repairParam.getRepairId());
        Param.setProgress(repairParam.getType());
        if (repairParam.getType() != null) {
            try {
                repairSendTemplate.setRepairParam(Param);
                repairSendTemplate.send();
                repairSendTemplate.wxCpSend();
            } catch (Exception e) {
                throw e;
            }
        }


        return ResponseData.success(newEntity);
    }

    @RequestMapping(value = "/customerList", method = RequestMethod.POST)
    public ResponseData list() {
        QueryWrapper<Customer> customerQueryWrapper = new QueryWrapper<>();
        customerQueryWrapper.in("display", 1);
        List<Customer> list = customerService.list(customerQueryWrapper);
        return ResponseData.success(list);
    }

    @RequestMapping(value = "/getRepair", method = RequestMethod.POST)
    public ResponseData getRepair() {
        Long userId = getWxUser(UserUtils.getUserId());
        if (ToolUtil.isEmpty(userId)) {
            throw new ServiceException(403, "此账户未绑定，请先进行绑定!");
        }
        Boolean permission = false;
        permission = wxuserInfoService.sendPermissions(1L, userId);
        if (!permission) {
            return ResponseData.success();
        }
        //查询工程师
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.in("user_id", userId);
        List<User> users = userService.list(userQueryWrapper);

        DispatchingParam dispatchingParam = new DispatchingParam();
        dispatchingParam.setName(userId);
        dispatchingParam.setState(0);

        PageInfo<DispatchingResult> dispatchingList = dispatchingService.findPageBySpec(dispatchingParam);
        List<RepairResult> res = new ArrayList<>();
        PageInfo resList = new PageInfo<>();

        //公司id
        List<Long> companyIds = new ArrayList<>();
        if (ToolUtil.isNotEmpty(dispatchingList)) {
            for (DispatchingResult data : dispatchingList.getData()) {

                Repair repair = this.repairService.getById(data.getRepairId());
                if (ToolUtil.isEmpty(repair)) {
                    continue;
                }
                RepairResult result = new RepairResult();
                result.setRepairId(repair.getRepairId());

                //查询报修获取公司id
                QueryWrapper<Repair> repairQueryWrapper = new QueryWrapper<>();
                repairQueryWrapper.in("repair_id", data.getRepairId()).in("progress", 2);
                List<Repair> repairs = repairService.list(repairQueryWrapper);
                repairQueryWrapper.in("repair_id", data.getRepairId()).in("progress", 3);
                List<Repair> repairs2 = repairService.list(repairQueryWrapper);
                repairs.addAll(repairs2);
                for (Repair repair1 : repairs) {
                    companyIds.add(repair1.getCompanyId());
                }
                //查询公司
                QueryWrapper<Customer> customerQueryWrapper = new QueryWrapper<>();
                customerQueryWrapper.in("customer_id", companyIds);
                List<Customer> customers = companyIds.size() == 0 ? new ArrayList<>() : customerService.list(customerQueryWrapper);
                for (Customer customer : customers) {
                    CustomerResult customerResult = new CustomerResult();
                    ToolUtil.copyProperties(customer, customerResult);
                    result.setCompany(customerResult);
                    break;
                }
                // 查询图片
                QueryWrapper<RepairImage> repairImageQueryWrapper = new QueryWrapper<>();
                repairImageQueryWrapper.in("repair_id", data.getRepairId());
                List<RepairImage> repairImages = repairImageService.list(repairImageQueryWrapper);
                List<RepairImageResult> bannerList = new ArrayList<>();
                for (RepairImage repairImage : repairImages) {
                    RepairImageResult repairImageResult = new RepairImageResult();
                    ToolUtil.copyProperties(repairImage, repairImageResult);
                    bannerList.add(repairImageResult);
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
                result.setProgress(repair.getProgress());
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
        for (int i = 0; i < res.size(); i++) {
            resList.setData(res);
        }
        return ResponseData.success(resList);
    }

    private Repair getEntity(RepairParam param) {
        Repair entity = new Repair();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    private Repair getOldEntity(RepairParam param) {
        return this.repairService.getById(getKey(param));
    }

    private Serializable getKey(RepairParam param) {
        return param.getRepairId();
    }

    @RequestMapping(value = "/getRepairById", method = RequestMethod.POST)
    public ResponseData getRepairById(@RequestBody(required = false) RepairParam repairParam) {
        if (ToolUtil.isEmpty(repairParam) || ToolUtil.isEmpty(repairParam.getRepairId())) {
            throw new ServiceException(500, "未选择报修数据");
        }
        Repair repair = this.repairService.getById(repairParam.getRepairId());
        Long userId = getWxUser(UserUtils.getUserId());
        Boolean permission = false;
        if (repair.getProgress() != 5L) {
            permission = wxuserInfoService.sendPermissions(repair.getProgress(), userId);
        }
        if (permission) {
            repair.setPower(1);
        } else {
            repair.setPower(0);
        }

        RepairResult repairResult = new RepairResult();
        ToolUtil.copyProperties(repair, repairResult);
        List<RepairResult> results = new ArrayList<RepairResult>() {{
            add(repairResult);
        }};
        this.repairService.format(results);

        return ResponseData.success(results.get(0));
    }

    @RequestMapping(value = "/getToken", method = RequestMethod.GET)
    @ApiOperation("获取阿里云OSS临时上传token")
    public ResponseData getToken(@Param("type") String type) {

        Long userId = getWxUser(UserUtils.getUserId());
        Media media = mediaService.getMediaId(type, userId);

        return ResponseData.success(mediaService.getOssToken(media));
    }
}
