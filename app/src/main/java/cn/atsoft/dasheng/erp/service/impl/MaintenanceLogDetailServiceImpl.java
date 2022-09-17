package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.mapper.MaintenanceLogDetailMapper;
import cn.atsoft.dasheng.erp.model.params.MaintenanceLogDetailParam;
import cn.atsoft.dasheng.erp.model.params.MaintenanceLogParam;
import cn.atsoft.dasheng.erp.model.result.InkindResult;
import cn.atsoft.dasheng.erp.model.result.MaintenanceLogDetailResult;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.service.StepsService;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-08-11
 */
@Service
public class MaintenanceLogDetailServiceImpl extends ServiceImpl<MaintenanceLogDetailMapper, MaintenanceLogDetail> implements MaintenanceLogDetailService {

    @Autowired
    private MaintenanceDetailService maintenanceDetailService;
    @Autowired
    private MaintenanceService maintenanceService;

    @Autowired
    private MaintenanceLogDetailService MaintenanceLogDetailService;

    @Autowired
    private SkuService skuService;

    @Autowired
    private SpuService spuService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private StockDetailsService stockDetailsService;
    @Autowired
    private StorehousePositionsService storehousePositionsService;
    @Autowired
    private InkindService inkindService;
    @Autowired
    private MaintenanceCycleService maintenanceCycleService;
    @Autowired
    private UserService userService;
    @Autowired
    private StepsService stepsService;


    @Override
    public void add(MaintenanceLogDetailParam param) {
        MaintenanceLogDetail entity = this.getEntity(param);
        this.save(entity);
    }
    @Override
    public void processingData(MaintenanceLogParam param, List<StockDetails> stockDetails, List<MaintenanceDetail> maintenanceDetails, List<MaintenanceLogDetail> logs) {
        for (MaintenanceLogDetailParam MaintenanceLogDetailParam : param.getMaintenanceLogDetailParams()) {
            List<StockDetails> need = new ArrayList<>();
            for (StockDetails stockDetail : stockDetails) {
                if (MaintenanceLogDetailParam.getSkuId().equals(stockDetail.getSkuId()) && MaintenanceLogDetailParam.getBrandId().equals(stockDetail.getBrandId()) && MaintenanceLogDetailParam.getStorehousePositionsId().equals(stockDetail.getStorehousePositionsId())) {
                    need.add(stockDetail);
                }
            }
            List<Long> inkindIds = new ArrayList<>();
            List<Long> skuIds = new ArrayList<>();
            int num = MaintenanceLogDetailParam.getNumber();
            for (StockDetails details : need) {
                if (num>0){
                    int lastNumber = num;
                    num -= details.getNumber();
                    if(num>=0){
                        inkindIds.add(details.getInkindId());
                        MaintenanceLogDetail log = new MaintenanceLogDetail();
                        log.setNumber(Math.toIntExact(details.getNumber()));
                        log.setBrandId(details.getBrandId());
                        log.setSkuId(details.getSkuId());
                        log.setInkindId(details.getInkindId());
                        log.setEnclosure(MaintenanceLogDetailParam.getEnclosure());
                        logs.add(log);
                    }else {
                        inkindIds.add(details.getInkindId());
                        MaintenanceLogDetail log = new MaintenanceLogDetail();
                        log.setNumber(lastNumber);
                        log.setBrandId(details.getBrandId());
                        log.setSkuId(details.getSkuId());
                        log.setInkindId(details.getInkindId());
                        log.setEnclosure(MaintenanceLogDetailParam.getEnclosure());
                        logs.add(log);
                    }
                }

            }




            if (num > 0) {
                for (StockDetails details : need) {
                    inkindIds.add(details.getInkindId());
                    MaintenanceLogDetail log = new MaintenanceLogDetail();
                    log.setNumber(Math.toIntExact(details.getNumber()));
                    log.setBrandId(details.getBrandId());
                    log.setSkuId(details.getSkuId());
                    log.setInkindId(details.getInkindId());
                    log.setEnclosure(MaintenanceLogDetailParam.getEnclosure());
                    logs.add(log);
                }
            }
            List<Inkind> inkinds = inkindIds.size() == 0 ? new ArrayList<>() : inkindService.listByIds(inkindIds);
            for (Inkind inkind : inkinds) {
                skuIds.add(inkind.getSkuId());
            }
            List<MaintenanceCycle> cycles = skuIds.size() == 0 ? new ArrayList<>() : maintenanceCycleService.query().in("sku_id", skuIds).eq("display", 1).list();
            //实物添加下次养护时间
            for (Inkind inkind : inkinds) {
                for (MaintenanceCycle cycle : cycles) {
                    if (inkind.getSkuId().equals(cycle.getSkuId())) {
                        Calendar calendar = Calendar.getInstance();
                        if (ToolUtil.isNotEmpty(inkind.getLastMaintenanceTime())) {
                            calendar.setTime(inkind.getLastMaintenanceTime());
                        } else {
                            calendar.setTime(new Date());
                        }
                        calendar.add(Calendar.DATE, cycle.getMaintenancePeriod());
                        inkind.setLastMaintenanceTime(calendar.getTime());
                    }
                }
            }
            for (MaintenanceDetail detail : maintenanceDetails) {
                if (detail.getSkuId().equals(MaintenanceLogDetailParam.getSkuId()) && detail.getStorehousePositionsId().equals(MaintenanceLogDetailParam.getStorehousePositionsId()) && detail.getBrandId().equals(MaintenanceLogDetailParam.getBrandId())) {
                    detail.setDoneNumber(detail.getDoneNumber() + MaintenanceLogDetailParam.getNumber());
                    if (detail.getDoneNumber() >= detail.getNumber()) {
                        detail.setStatus(99);
                    }
                }
            }
        }
    }

    @Override
    public void delete(MaintenanceLogDetailParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(MaintenanceLogDetailParam param) {
        MaintenanceLogDetail oldEntity = getOldEntity(param);
        MaintenanceLogDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public MaintenanceLogDetailResult findBySpec(MaintenanceLogDetailParam param) {
        return null;
    }

    @Override
    public List<MaintenanceLogDetailResult> findListBySpec(MaintenanceLogDetailParam param) {
        return this.baseMapper.customList(param);
    }

    @Override
    public List<MaintenanceLogDetailResult> lastLogByInkindIds(List<Long> inkindIds) {
        List<MaintenanceLogDetail> MaintenanceLogDetails = inkindIds.size() == 0 ? new ArrayList<>() : this.query().in("inkind_id", inkindIds).eq("display", 1).groupBy("inkind_id").orderByDesc("create_time").list();
        List<MaintenanceLogDetailResult> MaintenanceLogDetailResults = BeanUtil.copyToList(MaintenanceLogDetails, MaintenanceLogDetailResult.class);
        this.format(MaintenanceLogDetailResults);
        return MaintenanceLogDetailResults;
    }
@Override
public void format(List<MaintenanceLogDetailResult> data){
        List<Long> inkindIds = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        for (MaintenanceLogDetailResult datum : data) {
            inkindIds.add(datum.getInkindId());
            userIds.add(datum.getCreateUser());
        }
    List<InkindResult> inKinds = inkindService.getInKinds(inkindIds);
    List<UserResult> userResultsByIds = userService.getUserResultsByIds(userIds);
        for (MaintenanceLogDetailResult datum : data) {
            for (UserResult userResult : userResultsByIds) {
                if (datum.getCreateUser().equals(userResult.getUserId())){
                    userResult.setAvatar(stepsService.imgUrl(userResult.getUserId().toString()));
                    datum.setUserResult(userResult);
                }
            }
            for (InkindResult inKind : inKinds) {
                if(datum.getInkindId().equals(inKind.getInkindId())){
                    datum.setInkindResult(inKind);
                }
            }
        }
    }
    @Override
    public PageInfo<MaintenanceLogDetailResult> findPageBySpec(MaintenanceLogDetailParam param) {
        Page<MaintenanceLogDetailResult> pageContext = getPageContext();
        IPage<MaintenanceLogDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(MaintenanceLogDetailParam param) {
        return param.getMaintenanceLogDetailId();
    }

    private Page<MaintenanceLogDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private MaintenanceLogDetail getOldEntity(MaintenanceLogDetailParam param) {
        return this.getById(getKey(param));
    }

    private MaintenanceLogDetail getEntity(MaintenanceLogDetailParam param) {
        MaintenanceLogDetail entity = new MaintenanceLogDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }
}
