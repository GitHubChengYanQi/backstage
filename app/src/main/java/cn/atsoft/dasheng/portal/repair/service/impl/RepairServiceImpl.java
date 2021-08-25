package cn.atsoft.dasheng.portal.repair.service.impl;


import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.model.result.DeliveryDetailsResult;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.app.service.DeliveryDetailsService;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.common_area.entity.CommonArea;
import cn.atsoft.dasheng.common_area.model.result.CommonAreaResult;
import cn.atsoft.dasheng.common_area.service.CommonAreaService;
import cn.atsoft.dasheng.portal.banner.entity.Banner;
import cn.atsoft.dasheng.portal.banner.model.result.BannerResult;
import cn.atsoft.dasheng.portal.banner.service.BannerService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.portal.repair.entity.Repair;
import cn.atsoft.dasheng.portal.repair.mapper.RepairMapper;
import cn.atsoft.dasheng.portal.repair.model.params.RepairParam;
import cn.atsoft.dasheng.UserInfo.model.GetUser;
import cn.atsoft.dasheng.portal.repair.model.result.RegionResult;
import cn.atsoft.dasheng.portal.repair.model.result.RepairResult;
import cn.atsoft.dasheng.portal.repair.service.RepairService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.portal.repairdynamic.model.params.RepairDynamicParam;
import cn.atsoft.dasheng.portal.repairdynamic.service.RepairDynamicService;
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
    private BannerService bannerService;
    @Autowired
    private CommonAreaService commonAreaService;


    @BussinessLog
    @Override
    public Repair add(RepairParam param) {
        QueryWrapper<CommonArea> commonAreaQueryWrapper = new QueryWrapper<>();
        commonAreaQueryWrapper.in("id", param.getArea());
        List<CommonArea> commonAreas = commonAreaService.list(commonAreaQueryWrapper);
        for (CommonArea commonArea : commonAreas) {
            param.setArea(commonArea.getRegionCode());
        }


        Repair entity = getEntity(param);
        this.save(entity);
        return entity;
    }

    @BussinessLog
    @Override
    public void delete(RepairParam param) {
        this.removeById(getKey(param));
    }

    @BussinessLog
    @Override
    public Repair update(RepairParam param) {
        QueryWrapper<CommonArea> commonAreaQueryWrapper = new QueryWrapper<>();
        commonAreaQueryWrapper.in("id", param.getArea());
        List<CommonArea> commonAreas = commonAreaService.list(commonAreaQueryWrapper);
        for (CommonArea commonArea : commonAreas) {
            param.setArea(commonArea.getRegionCode());
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
        return null;
    }

    @Override
    public RepairResult detail(Long id) {
        Repair repair = this.getById(id);
        RepairResult repairResult = new RepairResult();
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
        areaQueryWrapper.in("region_code", comIds);
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

        QueryWrapper<Banner> bannerQueryWrapper = new QueryWrapper<>();
        if (ToolUtil.isNotEmpty(bIds)) {
            bannerQueryWrapper.in("difference", bIds);
        }

        List<Banner> banners = bannerService.list(bannerQueryWrapper);
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
            for (Banner banner : banners) {
                if (banner.getDifference().equals(record.getRepairId())) {
                    BannerResult bannerResult = new BannerResult();
                    ToolUtil.copyProperties(banner, bannerResult);
                    record.setBannerResult(bannerResult);
                    break;
                }
            }
            for (CommonArea commonArea : commonAreas) {
                RegionResult result = new RegionResult();
                if (commonArea.getRegionCode().equals(record.getArea())) {

                    QueryWrapper<CommonArea> AreaQueryWrapper = new QueryWrapper<>();
                    AreaQueryWrapper.in("parentid", commonArea.getId());
                    List<CommonArea> list = commonAreaService.list(AreaQueryWrapper);

                    if (list.size() > 0) {
                        throw new ServiceException(500, "请输入到区或县");
                    } else {
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
                                record.setRegionResult(result);
                            }
                        }
                        break;
                    }
                }


            }
        }
        return data.size() == 0 ? null : data.get(0);
    }

    @Override
    public String getuser(GetUser user) {

        return null;
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
