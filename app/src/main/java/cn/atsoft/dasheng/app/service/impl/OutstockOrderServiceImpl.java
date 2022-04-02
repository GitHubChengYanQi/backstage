package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.*;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.app.pojo.FreeOutStockParam;
import cn.atsoft.dasheng.app.pojo.Listing;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.mapper.OutstockOrderMapper;
import cn.atsoft.dasheng.app.model.result.OutstockOrderResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.config.MobileService;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.model.params.OutstockListingParam;
import cn.atsoft.dasheng.erp.model.result.OutstockListingResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.erp.service.CodingRulesService;
import cn.atsoft.dasheng.erp.service.InkindService;
import cn.atsoft.dasheng.erp.service.OutstockListingService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.orCode.model.result.BackCodeRequest;
import cn.atsoft.dasheng.orCode.service.OrCodeService;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.sendTemplate.WxCpTemplate;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 出库单 服务实现类
 * </p>
 *
 * @author cheng
 * @since 2021-08-16
 */
@Service
public class OutstockOrderServiceImpl extends ServiceImpl<OutstockOrderMapper, OutstockOrder> implements OutstockOrderService {

    @Autowired
    private StockService stockService;
    @Autowired
    private StockDetailsService stockDetailsService;
    @Autowired
    private UserService userService;
    @Autowired
    private OutstockListingService outstockListingService;
    @Autowired
    private StorehouseService storehouseService;
    @Autowired
    private CodingRulesService codingRulesService;
    @Autowired
    private OrCodeService orCodeService;
    @Autowired
    private InkindService inkindService;
    @Autowired
    private WxCpSendTemplate wxCpSendTemplate;
    @Autowired
    private StorehousePositionsService positionsService;
    @Autowired
    private MobileService mobileService;

    @Override
    public OutstockOrder add(OutstockOrderParam param) {
        List<Long> ids = new ArrayList<>();
        for (ApplyDetails applyDetail : param.getApplyDetails()) {
            ids.add(applyDetail.getBrandId() + applyDetail.getSkuId());
        }
        long count = ids.stream().distinct().count();
        if (param.getApplyDetails().size() > count) {
            throw new ServiceException(500, "请勿添加重复数据");
        }
        CodingRules codingRules = codingRulesService.query().eq("coding_rules_id", param.getCoding()).one();
        if (ToolUtil.isNotEmpty(codingRules)) {
            String backCoding = codingRulesService.backCoding(codingRules.getCodingRulesId());
            Storehouse storehouse = storehouseService.query().eq("storehouse_id", param.getStorehouseId()).one();
            if (ToolUtil.isNotEmpty(storehouse)) {
                String replace = "";
                if (ToolUtil.isNotEmpty(storehouse.getCoding())) {
                    replace = backCoding.replace("${storehouse}", storehouse.getCoding());
                } else {
                    replace = backCoding.replace("${storehouse}", "");
                }
                param.setCoding(replace);
            }
        }

        OutstockOrder entity = getEntity(param);
        this.save(entity);

        if (ToolUtil.isNotEmpty(param.getApplyDetails()) && param.getApplyDetails().size() > 0) {
            List<ApplyDetails> applyDetails = param.getApplyDetails();

            List<OutstockListing> outstockListings = new ArrayList<>();
            for (ApplyDetails applyDetail : applyDetails) {
                OutstockListing outstockListing = new OutstockListing();
                if(ToolUtil.isNotEmpty(applyDetail.getBrandId())){
                    outstockListing.setBrandId(applyDetail.getBrandId());
                }
                outstockListing.setSkuId(applyDetail.getSkuId());
                outstockListing.setNumber(applyDetail.getNumber());
                outstockListing.setDelivery(applyDetail.getNumber());
                outstockListing.setOutstockOrderId(entity.getOutstockOrderId());
                outstockListings.add(outstockListing);
            }

            outstockListingService.saveBatch(outstockListings);
        }

        BackCodeRequest backCodeRequest = new BackCodeRequest();
        backCodeRequest.setId(entity.getOutstockOrderId());
        backCodeRequest.setSource("outstock");
        Long aLong = orCodeService.backCode(backCodeRequest);

        String url = mobileService.getMobileConfig().getUrl() + "/cp/#/Work/OrCode?id=" + aLong;
        User createUser = userService.getById(entity.getCreateUser());
        //新微信推送
        WxCpTemplate wxCpTemplate = new WxCpTemplate();
        wxCpTemplate.setUrl(url);
        wxCpTemplate.setTitle("新的出库提醒");
        wxCpTemplate.setDescription(createUser.getName() + "创建了新的出库库任务" + entity.getCoding());
        wxCpTemplate.setUserIds(new ArrayList<Long>() {{
            add(entity.getUserId());
        }});
        wxCpSendTemplate.setSource("outstockOrder");
        wxCpSendTemplate.setSourceId(aLong);
        wxCpTemplate.setType(0);
        wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
        wxCpSendTemplate.sendTemplate();
        return entity;
    }


    @Override
    public void saveOutStockOrderByPickLists(OutstockOrderParam param){


        String encoding = codingRulesService.encoding(2);


        OutstockOrder entity = getEntity(param);
        entity.setCoding(encoding);
        this.save(entity);

        if (ToolUtil.isNotEmpty(param.getApplyDetails()) && param.getApplyDetails().size() > 0) {
            List<ApplyDetails> applyDetails = param.getApplyDetails();

            List<OutstockListing> outstockListings = new ArrayList<>();
            for (ApplyDetails applyDetail : applyDetails) {
                OutstockListing outstockListing = new OutstockListing();
                if(ToolUtil.isNotEmpty(applyDetail.getBrandId())){
                    outstockListing.setBrandId(applyDetail.getBrandId());
                }
                outstockListing.setSkuId(applyDetail.getSkuId());
                outstockListing.setNumber(applyDetail.getNumber());
                outstockListing.setDelivery(applyDetail.getNumber());
                outstockListing.setOutstockOrderId(entity.getOutstockOrderId());
                outstockListings.add(outstockListing);
            }

            outstockListingService.saveBatch(outstockListings);
        }

        BackCodeRequest backCodeRequest = new BackCodeRequest();
        backCodeRequest.setId(entity.getOutstockOrderId());
        backCodeRequest.setSource("outstock");
        Long aLong = orCodeService.backCode(backCodeRequest);

        String url = mobileService.getMobileConfig().getUrl() + "/cp/#/Work/OrCode?id=" + aLong;
        User createUser = userService.getById(entity.getCreateUser());
        //新微信推送
        WxCpTemplate wxCpTemplate = new WxCpTemplate();
        wxCpTemplate.setUrl(url);
        wxCpTemplate.setTitle("新的出库提醒");
        wxCpTemplate.setDescription(createUser.getName() + "创建了新的出库库任务" + entity.getCoding());
        wxCpTemplate.setUserIds(new ArrayList<Long>() {{
            add(entity.getUserId());
        }});
        wxCpSendTemplate.setSource("outstockOrder");
        wxCpSendTemplate.setSourceId(aLong);
        wxCpTemplate.setType(0);
        wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
        wxCpSendTemplate.sendTemplate();
    }

    /**
     * 出库单一键出库
     *
     * @param param
     */
    public void AkeyOutbound(OutstockOrderParam param) {
        OutstockOrder outstockOrder = this.getById(param.getOutstockOrderId());

        outBound(param.getListingParams()); //出库
    }

    @Override
    public OutstockOrderResult getOrder(Long id) {
        OutstockOrder outstockOrder = this.getById(id);
        if (ToolUtil.isEmpty(outstockOrder)) {
            throw new ServiceException(500, "当前数据不存在");
        }
        OutstockOrderResult outstockResult = new OutstockOrderResult();
        ToolUtil.copyProperties(outstockOrder, outstockResult);

        Storehouse outStockStorehouse = storehouseService.getById(outstockResult.getStorehouseId());
        if (ToolUtil.isNotEmpty(outStockStorehouse)) {
            StorehouseResult storehouseResult1 = new StorehouseResult();
            ToolUtil.copyProperties(outStockStorehouse, storehouseResult1);
            outstockResult.setStorehouseResult(storehouseResult1);
        }
        User outStockUser = userService.getById(outstockOrder.getUserId());
        if (ToolUtil.isNotEmpty(outStockUser)) {
            UserResult userResult = new UserResult();
            ToolUtil.copyProperties(outStockUser, userResult);
            outstockResult.setUserResult(userResult);
        }

        getListing(outstockResult);
        return outstockResult;
    }

    private void getListing(OutstockOrderResult result) {
        List<OutstockListing> outstockListings = outstockListingService.query().eq("outstock_order_id", result.getOutstockOrderId()).list();
        List<OutstockListingResult> resultList = BeanUtil.copyToList(outstockListings, OutstockListingResult.class, new CopyOptions());
        outstockListingService.format(resultList);


        Map<Long, List<StorehousePositionsResult>> supperMap = new HashMap<>();
        for (OutstockListingResult outstockListingResult : resultList) {
            List<StorehousePositionsResult> supper = positionsService.getSupperBySkuId(outstockListingResult.getSkuId());

            supperMap.put(outstockListingResult.getSkuId(), supper);
        }


        List<Listing> list = new ArrayList<>();
        for (OutstockListingResult outstockListingResult : resultList) {
            Listing listing = new Listing();
            List<StorehousePositionsResult> positionsResults = supperMap.get(outstockListingResult.getSkuId());
            listing.setListingResult(outstockListingResult);
            listing.setPositionsResults(positionsResults);
            list.add(listing);
        }

        result.setListing(list);
    }

    private void mergePosition(List<Long> positionIds) {
    
    }


    @Override
    public void outBound(List<OutstockListingParam> listings) {
        List<StockDetails> details = stockDetailsService.query().orderByAsc("create_time").list();
        for (OutstockListingParam listing : listings) {
            if (ToolUtil.isEmpty(listing.getBrandId())) {
                AnyBrandOutBound(listing, details); //任意品牌
            } else {
                SkuBrandOutBound(listing, details);
            }
        }
        stockDetailsService.updateBatchById(details);
        stockDetailsService.remove(new QueryWrapper<StockDetails>() {{
            eq("stage", 2);
        }});
    }

    /**
     * 任意品牌出库
     *
     * @param listingParam
     * @param details
     */
    private void AnyBrandOutBound(OutstockListingParam listingParam, List<StockDetails> details) {
        long number = 0;

        for (StockDetails detail : details) {
            if (listingParam.getSkuId().equals(detail.getSkuId()) && listingParam.getPositionsId().equals(detail.getStorehousePositionsId())) {
                number = number + detail.getNumber();
            }
        }
        if (listingParam.getNumber() > number) {
            throw new ServiceException(500, "数量不足");
        }
        number = 0;
        for (StockDetails detail : details) {
            if (detail.getStage() == 1) {
                if (listingParam.getSkuId().equals(detail.getSkuId()) && detail.getStorehousePositionsId().equals(listingParam.getPositionsId())) {
                    number = detail.getNumber() - listingParam.getNumber();
                    if (number > 0) {
                        detail.setNumber(number);
                        break;
                    } else {
                        listingParam.setNumber(listingParam.getNumber() - detail.getNumber());
                        detail.setStage(2);
                    }
                }
            }
        }
    }

    private void SkuBrandOutBound(OutstockListingParam listingParam, List<StockDetails> details) {
        long number = 0;
        for (StockDetails detail : details) {
            if (listingParam.getSkuId().equals(detail.getSkuId())
                    && listingParam.getBrandId().equals(detail.getBrandId())
                    && listingParam.getPositionsId().equals(detail.getStorehousePositionsId())) {
                number = number + detail.getNumber();
            }
        }
        if (listingParam.getNumber() > number) {
            throw new ServiceException(500, "数量不足");
        }
        number = 0;
        for (StockDetails detail : details) {
            if (detail.getStage() == 1) {
                if (detail.getSkuId().equals(listingParam.getSkuId()) && detail.getBrandId().equals(listingParam.getBrandId())
                        && detail.getStorehousePositionsId().equals(listingParam.getPositionsId())) {
                    number = detail.getNumber() - listingParam.getNumber();
                    if (number > 0) {
                        detail.setNumber(number);
                        break;
                    } else {
                        listingParam.setNumber(listingParam.getNumber() - detail.getNumber());
                        detail.setStage(2);
                    }
                }
            }
        }
    }


    @Override
    public void delete(OutstockOrderParam param) {
//        param.setDisplay(0);
//        this.update(param);
        throw new ServiceException(500, "不可以删除");
    }

    /**
     * 自由出库
     *
     * @param freeOutStockParam
     */
    @Override
    public void freeOutStock(FreeOutStockParam freeOutStockParam) {


        StockDetails stockDetails = stockDetailsService.query().eq("inkind_id", freeOutStockParam.getInkindId()).one();
        if (ToolUtil.isEmpty(stockDetails)) {
            throw new ServiceException(500, "库存没有此物料");
        }

        if (stockDetails.getNumber() < freeOutStockParam.getNumber()) {
            throw new ServiceException(500, "数量不足");
        }
        long newNumber = stockDetails.getNumber() - freeOutStockParam.getNumber();
        stockDetails.setNumber(newNumber);
        if (stockDetails.getNumber() == 0) {
            stockDetailsService.removeById(stockDetails);
        } else {
            stockDetailsService.updateById(stockDetails);
        }

        //修改库存数量
        Inkind instockInkind = inkindService.getById(stockDetails.getInkindId());
        instockInkind.setNumber(instockInkind.getNumber() - freeOutStockParam.getNumber());
        inkindService.updateById(instockInkind);

        //创建实物
        Inkind inkind = new Inkind();
        inkind.setSource(freeOutStockParam.getType());
        inkind.setSkuId(stockDetails.getSkuId());
        inkind.setBrandId(stockDetails.getBrandId());
        inkind.setType("2");
        inkind.setCustomerId(stockDetails.getCustomerId());
        inkind.setNumber(freeOutStockParam.getNumber());
        inkindService.save(inkind);

        //更新库存数量
        stockService.updateNumber(new ArrayList<Long>() {{
            add(stockDetails.getStockId());
        }});
    }


    @Override
    public OutstockOrder update(OutstockOrderParam param) {
        throw new ServiceException(500, "不可以修改");
    }

    @Override
    public OutstockOrderResult findBySpec(OutstockOrderParam param) {
        return null;
    }

    @Override
    public List<OutstockOrderResult> findListBySpec(OutstockOrderParam param) {
        return null;
    }

    @Override
    public PageInfo<OutstockOrderResult> findPageBySpec(OutstockOrderParam param, DataScope dataScope) {
        Page<OutstockOrderResult> pageContext = getPageContext();
        IPage<OutstockOrderResult> page = this.baseMapper.customPageList(pageContext, param, dataScope);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(OutstockOrderParam param) {
        return param.getOutstockOrderId();
    }

    private Page<OutstockOrderResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private OutstockOrder getOldEntity(OutstockOrderParam param) {
        return this.getById(getKey(param));
    }

    private OutstockOrder getEntity(OutstockOrderParam param) {
        OutstockOrder entity = new OutstockOrder();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    public void format(List<OutstockOrderResult> data) {
        List<Long> ids = new ArrayList<>();
        List<Long> stockHouseIds = new ArrayList<>();

        for (OutstockOrderResult datum : data) {
            ids.add(datum.getUserId());
            stockHouseIds.add(datum.getStorehouseId());
        }
        List<User> users = ids.size() == 0 ? new ArrayList<>() : userService.lambdaQuery().in(User::getUserId, ids).list();

        List<Storehouse> storehouses = stockHouseIds.size() == 0 ? new ArrayList<>() : storehouseService.lambdaQuery().in(Storehouse::getStorehouseId, stockHouseIds).list();

        for (OutstockOrderResult datum : data) {


            for (User user : users) {
                if (datum.getUserId() != null && user.getUserId().equals(datum.getUserId())) {
                    UserResult userResult = new UserResult();
                    ToolUtil.copyProperties(user, userResult);
                    datum.setUserResult(userResult);
                    break;
                }
            }
            for (Storehouse storehouse : storehouses) {
                if (datum.getStorehouseId() != null && storehouse.getStorehouseId().equals(datum.getStorehouseId())) {
                    StorehouseResult storehouseResult = new StorehouseResult();
                    ToolUtil.copyProperties(storehouse, storehouseResult);
                    datum.setStorehouseResult(storehouseResult);
                    break;
                }
            }
        }
    }
}
