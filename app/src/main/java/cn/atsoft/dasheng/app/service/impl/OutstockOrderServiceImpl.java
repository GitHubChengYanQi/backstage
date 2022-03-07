package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.*;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.app.pojo.FreeOutStockParam;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.mapper.OutstockOrderMapper;
import cn.atsoft.dasheng.app.model.result.OutstockOrderResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.model.params.OutstockListingParam;
import cn.atsoft.dasheng.erp.service.CodingRulesService;
import cn.atsoft.dasheng.erp.service.InkindService;
import cn.atsoft.dasheng.erp.service.OutstockListingService;
import cn.atsoft.dasheng.erp.service.impl.OutstockSendTemplate;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.orCode.model.result.BackCodeRequest;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.orCode.service.OrCodeService;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.sendTemplate.WxCpTemplate;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
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
                outstockListing.setBrandId(applyDetail.getBrandId());
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

//        String url = param.getUrl().replace("codeId", aLong.toString());
//        outstockSendTemplate.setSourceId(entity.getOutstockOrderId());
//        outstockSendTemplate.setUserId(param.getUserId());
//        outstockSendTemplate.setUrl(url);

        User createUser = userService.getById(entity.getCreateUser());
        //新微信推送
        WxCpTemplate wxCpTemplate = new WxCpTemplate();
//        wxCpTemplate.setUrl(url);
        wxCpTemplate.setTitle("新的出库提醒");
        wxCpTemplate.setDescription(createUser.getName() + "创建了新的入库任务" + entity.getCoding());
        wxCpTemplate.setUserIds(new ArrayList<Long>() {{
            add(entity.getUserId());
        }});
        wxCpSendTemplate.setSource("出库");
        wxCpSendTemplate.setSourceId(entity.getOutstockOrderId());
        wxCpTemplate.setType(0);
        wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
        wxCpSendTemplate.sendTemplate();
        return entity;
    }

    /**
     * 出库单一键出库
     *
     * @param param
     */
    public void AkeyOutbound(OutstockOrderParam param) {
        outBound(param.getListingParams()); //出库
    }


    private void outBound(List<OutstockListingParam> listings) {

        List<StockDetails> details = stockDetailsService.query().orderByDesc("create_time").list();
        Map<Long, List<StockDetails>> listMap = sameSkuWith(details);
        for (OutstockListingParam listing : listings) {
            List<StockDetails> stockDetails = listMap.get(listing.getSkuId() + listing.getBrandId() + listing.getPositionsId());
            long number = 0L;
            if (ToolUtil.isEmpty(stockDetails)) {
                throw new ServiceException(500, "库存中没有" + listing.getSkuId() + "此物品");
            }
            for (StockDetails stockDetail : stockDetails) {
                number = number + stockDetail.getNumber();
            }
            if (listing.getNumber() > number) {
                throw new ServiceException(500, listing.getSkuId() + "此物品,数量不足");
            }
            for (StockDetails stockDetail : stockDetails) {
                long inkind;
                if (stockDetail.getNumber() - listing.getNumber() > 0) {
                    number = stockDetail.getNumber() - listing.getNumber();
                    stockDetail.setNumber(number);
                    stockDetailsService.updateById(stockDetail);
                    inkind = stockDetail.getInkindId();
                    listing.setNumber(stockDetail.getNumber() - listing.getNumber());
                    break;
                } else {
                    number = listing.getNumber() - stockDetail.getNumber();
                    listing.setNumber(number);
                    stockDetailsService.removeById(stockDetail);
                    inkind = stockDetail.getInkindId();
                }
                Inkind ink = inkindService.getById(inkind);   //出库之后更新实物的数量
                ink.setNumber(ink.getNumber() - number);
                inkindService.updateById(ink);
            }
        }
    }

    /**
     * 库存相同物料合并
     *
     * @param details
     * @return
     */
    private Map<Long, List<StockDetails>> sameSkuWith(List<StockDetails> details) {
        Map<Long, List<StockDetails>> listMap = new HashMap<>();

        for (StockDetails detail : details) {
            List<StockDetails> stockDetails = new ArrayList<>();
            stockDetails.add(detail);
            if (ToolUtil.isNotEmpty(detail.getSkuId()) && ToolUtil.isNotEmpty(detail.getBrandId()) && ToolUtil.isNotEmpty(detail.getStorehousePositionsId())) {
                List<StockDetails> detailsList = listMap.get(detail.getSkuId() + detail.getBrandId());
                if (ToolUtil.isEmpty(detailsList)) {
                    listMap.put(detail.getSkuId() + detail.getBrandId() + detail.getStorehousePositionsId(), stockDetails);
                } else {
                    detailsList.addAll(stockDetails);
                    listMap.put(detail.getSkuId() + detail.getBrandId() + detail.getStorehousePositionsId(), detailsList);
                }
            }
        }
        return listMap;
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
