package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.*;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.mapper.OutstockOrderMapper;
import cn.atsoft.dasheng.app.model.result.OutstockOrderResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.ApplyDetails;
import cn.atsoft.dasheng.erp.entity.CodingRules;
import cn.atsoft.dasheng.erp.entity.OutstockListing;
import cn.atsoft.dasheng.erp.service.CodingRulesService;
import cn.atsoft.dasheng.erp.service.OutstockListingService;
import cn.atsoft.dasheng.erp.service.impl.OutstockSendTemplate;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.orCode.model.result.BackCodeRequest;
import cn.atsoft.dasheng.orCode.service.OrCodeService;
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
import java.util.List;

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
    private OutstockService outstockService;
    @Autowired
    private UserService userService;
    @Autowired
    private OutstockListingService outstockListingService;
    @Autowired
    private StorehouseService storehouseService;
    @Autowired
    private CodingRulesService codingRulesService;
    @Autowired
    private OutstockSendTemplate outstockSendTemplate;
    @Autowired
    private OrCodeService orCodeService;

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
                outstockListing.setOutstockOrderId(entity.getOutstockOrderId());
                outstockListings.add(outstockListing);
            }

            outstockListingService.saveBatch(outstockListings);
        }

        BackCodeRequest backCodeRequest = new BackCodeRequest();
        backCodeRequest.setId(entity.getOutstockOrderId());
        backCodeRequest.setSource("instock");
        Long aLong = orCodeService.backCode(backCodeRequest);
        String url = param.getUrl().replace("codeId", aLong.toString());
        outstockSendTemplate.setUserId(param.getUserId());
        outstockSendTemplate.setUrl(url);
        outstockSendTemplate.sendTemplate();
        return entity;
    }

    @Override
    public void delete(OutstockOrderParam param) {
        param.setDisplay(0);
        this.update(param);
    }


    @Override
    public void outStock(OutstockOrderParam param) {

        OutstockOrder entity = getEntity(param);
        Long outStockOrderId = entity.getOutstockOrderId();
        // 判断出库单对应出库明细数据有无
        QueryWrapper<Outstock> outstockQueryWrapper = new QueryWrapper<>();
        if (ToolUtil.isNotEmpty(outStockOrderId)) {
            outstockQueryWrapper.in("outstock_order_id", outStockOrderId).in("display", 1);
        }
        List<Outstock> outstockList = outstockService.list(outstockQueryWrapper);

        if (ToolUtil.isNotEmpty(outstockList)) {
            List<Stock> Stock = this.stockService.list();


            // 出库明细里进行出库
            for (int i = 0; i < outstockList.size(); i++) {
                Outstock outstock = outstockList.get(i);
                // 判断库存里数据是否存在
                if (ToolUtil.isEmpty(Stock)) {
                    throw new ServiceException(500, "仓库没有此产品或仓库库存不足！");
                } else {
                    for (int k = 0; k < Stock.size(); k++) {
                        Stock StockList = Stock.get(k);
                        // 匹配库存里对应的数据
                        if (StockList.getItemId().equals(outstock.getItemId())
                                && StockList.getBrandId().equals(outstock.getBrandId())
                                && StockList.getStorehouseId().equals(entity.getStorehouseId())
                        ) {
                            QueryWrapper<StockDetails> queryWrapper = new QueryWrapper<>();
                            queryWrapper.in("stock_id", StockList.getStockId()).in("stage", 1);
                            List<StockDetails> stockDetail = stockDetailsService.list(queryWrapper);
                            // 库存数据数量的判断
                            if (StockList.getInventory() == 0) {
                                throw new ServiceException(500, "此产品仓库库存不足！");
                            } else if (outstock.getNumber() > StockList.getInventory()) {
                                throw new ServiceException(500, "此产品仓库库存不足,请重新输入数量，最大数量为：" + StockList.getInventory());
                            } else {
                                StockParam stockParam = new StockParam();
                                stockParam.setStockId(StockList.getStockId());
                                stockParam.setItemId(StockList.getItemId());
                                stockParam.setBrandId(StockList.getBrandId());
                                stockParam.setStorehouseId(StockList.getStorehouseId());
                                stockParam.setInventory(StockList.getInventory() - outstock.getNumber());
                                // 减去出库数量
                                this.stockService.update(stockParam);


                                if (ToolUtil.isEmpty(stockDetail)) {
                                    throw new ServiceException(500, "库存明细里没有此产品或仓库库存不足！");
                                } else {
                                    List stockItemIds = new ArrayList<>();
                                    QueryWrapper<StockDetails> queryWrapper1 = new QueryWrapper<>();
                                    for (int j = 0; j < outstock.getNumber(); j++) {
                                        StockDetails stockDetailList = stockDetail.get(j);
                                        // 匹配库存明细里对应的数据
                                        if (stockDetailList.getStockId().equals(StockList.getStockId())) {
                                            // 减去出库明细产品
                                            StockDetails StockDetailsList = stockDetail.get(j);
                                            stockItemIds.add(StockDetailsList.getStockItemId());
                                        }
                                    }
                                    queryWrapper1.in("stock_item_id", stockItemIds);
                                    StockDetails stockDetails = new StockDetails();
                                    stockDetails.setStage(2);
                                    stockDetails.setOutStockOrderId(outStockOrderId);
                                    this.stockDetailsService.update(stockDetails, queryWrapper1);
                                }
                            }
                        }
                    }
                }
            }
            OutstockOrder oldEntity = getOldEntity(param);
            OutstockOrder newEntity = getEntity(param);
            ToolUtil.copyProperties(newEntity, oldEntity);
            this.updateById(newEntity);
        } else {
            throw new ServiceException(500, "仓库没有此产品！");
        }

    }


    @Override
    public OutstockOrder update(OutstockOrderParam param) {
        OutstockOrder oldEntity = getOldEntity(param);
        OutstockOrder newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
        return newEntity;
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
