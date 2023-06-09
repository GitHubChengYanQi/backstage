package cn.atsoft.dasheng.portal.repair.service.impl;


import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.model.result.DeliveryDetailsResult;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.app.service.DeliveryDetailsService;
import cn.atsoft.dasheng.base.log.FreedLog;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.commonArea.entity.CommonArea;
import cn.atsoft.dasheng.commonArea.model.result.CommonAreaResult;
import cn.atsoft.dasheng.commonArea.service.CommonAreaService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.portal.dispatChing.entity.Dispatching;
import cn.atsoft.dasheng.portal.dispatChing.model.result.DispatchingResult;
import cn.atsoft.dasheng.portal.dispatChing.service.DispatchingService;
import cn.atsoft.dasheng.portal.repair.entity.Repair;
import cn.atsoft.dasheng.portal.repair.mapper.RepairMapper;
import cn.atsoft.dasheng.portal.repair.model.params.RepairParam;
import cn.atsoft.dasheng.crm.region.RegionResult;
import cn.atsoft.dasheng.portal.repair.model.result.RepairResult;
import cn.atsoft.dasheng.portal.repair.service.RepairSendTemplate;
import cn.atsoft.dasheng.portal.repair.service.RepairService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.portal.repairDynamic.model.params.RepairDynamicParam;
import cn.atsoft.dasheng.portal.repairDynamic.service.RepairDynamicService;
import cn.atsoft.dasheng.portal.repairImage.entity.RepairImage;
import cn.atsoft.dasheng.portal.repairImage.model.params.RepairImageParam;
import cn.atsoft.dasheng.portal.repairImage.model.result.RepairImageResult;
import cn.atsoft.dasheng.portal.repairImage.service.RepairImageService;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 报修 服务实现类
 * </p>
 *
 * @author siqiang
 * @since 2021-08-20
 */
@Service
public class RepairServiceImpl extends ServiceImpl<RepairMapper, Repair> implements RepairService {

    @Autowired
    private DeliveryDetailsService deliveryDetailsService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private RepairDynamicService repairDynamicService;
    @Autowired
    private RepairImageService repairImageService;
    @Autowired
    private CommonAreaService commonAreaService;
    @Autowired
    private DispatchingService dispatchingService;
    @Resource
    private UserService userService;
    @Autowired
    private RepairSendTemplate repairSendTemplate;


    @FreedLog
    @Override
    @Transactional
    public Repair add(RepairParam param) {
        if (param.getArea() == null) {
            throw new ServiceException(500, "请选择地区");
        }else{
            QueryWrapper<CommonArea> AreaQueryWrapper = new QueryWrapper<>();
            AreaQueryWrapper.in("parentid", param.getArea());
            List<CommonArea> list = commonAreaService.list(AreaQueryWrapper);
            if (list.size() > 0) {
                throw new ServiceException(500, "请选择到区或县");
            }
        }
        Repair entity = getEntity(param);
        this.save(entity);
        param.setProgress(0L);
        param.setRepairId(entity.getRepairId());
        param.setCreateTime(entity.getCreateTime());
        param.setRepairId(entity.getRepairId());
        repairSendTemplate.setRepairParam(param);
        try{
            repairSendTemplate.send();
            repairSendTemplate.wxCpSend();
        }catch(WxErrorException e) {
            e.printStackTrace();
        }

        List<RepairImage> repairImages = param.getItemImgUrlList();
        for (RepairImage data : repairImages){
            if (data != null) {
                RepairImageParam repairImageParam = new RepairImageParam();
                repairImageParam.setRepairId(entity.getRepairId());
                repairImageParam.setImgUrl(data.getImgUrl());
                repairImageParam.setTitle(data.getTitle());
                this.repairImageService.add(repairImageParam);
            }

        }


        return entity;
    }

    @FreedLog
    @Override
    public void delete(RepairParam param) {
        this.removeById(getKey(param));
    }

    @FreedLog
    @Override
    public Repair update(RepairParam param) {


        QueryWrapper<CommonArea> AreaQueryWrapper = new QueryWrapper<>();
        AreaQueryWrapper.in("parentid", param.getArea());
        List<CommonArea> list = commonAreaService.list(AreaQueryWrapper);
        if (list.size() > 0) {
            throw new ServiceException(500, "请选择到区或县");
        }
        Repair oldEntity = getOldEntity(param);
        Repair newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
        return newEntity;

    }

    public String updatedynamic(RepairParam param) {

        QueryWrapper<Repair> repairQueryWrapper = new QueryWrapper<>();
        repairQueryWrapper.in("repair_id", param.getRepairId());
        List<Repair> repairs = this.list(repairQueryWrapper);
        for (Repair repair : repairs) {
            if (repair.getProgress() != 5) {
                RepairDynamicParam repairDynamicParam = new RepairDynamicParam();
                if (param.getProgress() == 0) {
                    repairDynamicParam.setContent("待派工....");
                    repairDynamicParam.setRepairId(param.getRepairId());
                    repairDynamicService.add(repairDynamicParam);
                } else if (param.getProgress() == 1) {
                    repairDynamicParam.setContent("询价中....");
                    repairDynamicParam.setRepairId(param.getRepairId());
                    repairDynamicService.add(repairDynamicParam);
                } else if (param.getProgress() == 2) {
                    repairDynamicParam.setContent("待支付....");
                    repairDynamicParam.setRepairId(param.getRepairId());
                    repairDynamicService.add(repairDynamicParam);
                } else if (param.getProgress() == 3) {
                    repairDynamicParam.setContent("实施中....");
                    repairDynamicParam.setRepairId(param.getRepairId());
                    repairDynamicService.add(repairDynamicParam);
                } else if (param.getProgress() == 4) {
                    repairDynamicParam.setContent("待回访....");
                    repairDynamicParam.setRepairId(param.getRepairId());
                    repairDynamicService.add(repairDynamicParam);
                } else if (param.getProgress() == 5) {
                    repairDynamicParam.setContent("已完成");
                    repairDynamicParam.setRepairId(param.getRepairId());
                    repairDynamicService.add(repairDynamicParam);
                }
            } else {
                throw new ServiceException(500, "售后已完成");


            }
        }


        Repair oldEntity = getOldEntity(param);
        Repair newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
        return "修改成功";
    }

    @Override
    public RepairResult findBySpec(RepairParam param) {
        return null;
    }

    @Override
    public List<RepairResult> findListBySpec(RepairParam param) {
        List<RepairResult> repairResults = this.baseMapper.customList(param);
        format(repairResults);
        return repairResults;
    }

    @Override
    public RepairResult detail(Long id) {
        Repair repair = this.getById(id);
        RepairResult repairResult = new RepairResult();

        QueryWrapper<RepairImage> bannerQueryWrapper = new QueryWrapper<>();
        bannerQueryWrapper.in("repair_id", id);
        List<RepairImage> list = repairImageService.list(bannerQueryWrapper);
        List<RepairImageResult> repairImageResults = new ArrayList<>();
        for (RepairImage repairImage : list) {
            RepairImageResult result = new RepairImageResult();
            ToolUtil.copyProperties(repairImage, result);
            repairImageResults.add(result);
        }
        repairResult.setBannerResult(repairImageResults);

        ToolUtil.copyProperties(repair, repairResult);
        List<RepairResult> results = new ArrayList<RepairResult>() {{
            add(repairResult);
        }};


        this.format(results);
        return results.get(0);
    }

    @Override
    public PageInfo<RepairResult> findPageBySpec(RepairParam param) {
        Page<RepairResult> pageContext = getPageContext();
        IPage<RepairResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    @Override
    public RepairResult format(List<RepairResult> data) {
        List<Long> ids = new ArrayList<>();
        List<Long> cIds = new ArrayList<>();
        List<Long> bIds = new ArrayList<>();
        List<String> comIds = new ArrayList<>();
        //映射发货详情

        for (RepairResult record : data) {
            ids.add(record.getItemId());
            cIds.add(record.getCustomerId());
            bIds.add(record.getRepairId());
            comIds.add(record.getArea());
        }
        QueryWrapper<CommonArea> areaQueryWrapper = new QueryWrapper<>();
        areaQueryWrapper.in("id", comIds);
        List<CommonArea> commonAreas = comIds.size() == 0 ? new ArrayList<>() : commonAreaService.list(areaQueryWrapper);

        List<DeliveryDetailsResult> byIds = new ArrayList<>();
        if (ToolUtil.isNotEmpty(ids)) {
            byIds = deliveryDetailsService.getByIds(ids);
        }

        QueryWrapper<Customer> customerQueryWrapper = new QueryWrapper<>();
        if (ToolUtil.isNotEmpty(cIds)) {
            customerQueryWrapper.in("customer_id", cIds);
        }
        List<Customer> customers = customerService.list(customerQueryWrapper);

        QueryWrapper<RepairImage> bannerQueryWrapper = new QueryWrapper<>();
        if (ToolUtil.isNotEmpty(bIds)) {
            bannerQueryWrapper.in("repair_id", bIds);
        }

        List<RepairImage> repairImages = repairImageService.list(bannerQueryWrapper);


        QueryWrapper<Dispatching> dispatchingQueryWrapper = new QueryWrapper<>();
        if (ToolUtil.isNotEmpty(bIds)) {
            dispatchingQueryWrapper.in("repair_id", bIds);
        }
        List<Dispatching> dispatchings = dispatchingService.list(dispatchingQueryWrapper);

        List<RepairImageResult> repairImages1 = new ArrayList<>();
        List<DispatchingResult> dispatchingList = new ArrayList<>();
        for (RepairResult record : data) {
            for (DeliveryDetailsResult byId : byIds) {
                if (byId.getDeliveryDetailsId().equals(record.getItemId())) {
                    record.setDeliveryDetailsResult(byId);
                }
            }
            for (Customer customer : customers) {
                if (customer.getCustomerId().equals(record.getCustomerId())) {
                    CustomerResult customerResult = new CustomerResult();
                    ToolUtil.copyProperties(customer, customerResult);
                    record.setCustomerResult(customerResult);
                    break;
                }
            }

            for (RepairImage repairImage : repairImages) {
                if (repairImage.getRepairId().equals(record.getRepairId())) {
                    RepairImageResult result = new RepairImageResult();
                    ToolUtil.copyProperties(repairImage, result);
                    repairImages1.add(result);
                }
            }

            for (Dispatching dispatching : dispatchings) {
                if (dispatching.getRepairId().equals(record.getRepairId())) {
                    User userInfo = userService.getById(dispatching.getName());
                    DispatchingResult dispatchingResult = new DispatchingResult();
                    ToolUtil.copyProperties(dispatching, dispatchingResult);
                    dispatchingResult.setUserName(userInfo.getName());
                    dispatchingList.add(dispatchingResult);
                }
            }
            record.setBannerResult(repairImages1);
            record.setDispatchingResults(dispatchingList);
            List<RegionResult> regionList = new ArrayList<>();
            for (CommonArea commonArea : commonAreas) {
                Long recordArea = record.getArea() == null ? null : Long.valueOf(record.getArea());
                RegionResult result = new RegionResult();
                Long id = Long.valueOf(commonArea.getId());
                if (id == recordArea) {

                    QueryWrapper<CommonArea> AreaQueryWrapper = new QueryWrapper<>();
                    AreaQueryWrapper.in("parentid", commonArea.getId());
                    List<CommonArea> list = commonAreaService.list(AreaQueryWrapper);

                    CommonAreaResult commonAreaResult = new CommonAreaResult();
                    ToolUtil.copyProperties(commonArea, commonAreaResult);
                    result.setArea(commonAreaResult.getTitle());

                    QueryWrapper<CommonArea> commonAreaQueryWrapper = new QueryWrapper<>();
                    commonAreaQueryWrapper.in("id", commonAreaResult.getParentid());
                    List<CommonArea> cityList = commonAreaService.list(commonAreaQueryWrapper);

                    for (CommonArea area : cityList) {
                        CommonAreaResult city = new CommonAreaResult();
                        ToolUtil.copyProperties(area, city);
                        result.setCity(city.getTitle());

                        QueryWrapper<CommonArea> commonAreaQueryWrapper1 = new QueryWrapper<>();
                        commonAreaQueryWrapper1.in("id", city.getParentid());
                        List<CommonArea> provinceList = commonAreaService.list(commonAreaQueryWrapper1);

                        for (CommonArea commonArea1 : provinceList) {
                            CommonAreaResult province = new CommonAreaResult();
                            ToolUtil.copyProperties(commonArea1, province);
                            result.setProvince(province.getTitle());
                            regionList.add(result);
                        }
                        record.setRegionResult(regionList);
                    }
                    break;

                }


            }
        }
        return data.size() == 0 ? null : data.get(0);
    }

    @Override
    public PageInfo<RepairResult> findMyPageBySpec(RepairParam param) {
        Page<RepairResult> pageContext = getPageContext();
        IPage<RepairResult> page = this.baseMapper.customMyPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }


    private Serializable getKey(RepairParam param) {
        return param.getRepairId();
    }

    private Page<RepairResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Repair getOldEntity(RepairParam param) {
        return this.getById(getKey(param));
    }

    private Repair getEntity(RepairParam param) {
        Repair entity = new Repair();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
