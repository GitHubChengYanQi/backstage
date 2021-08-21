package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.*;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.mapper.OutstockOrderMapper;
import cn.atsoft.dasheng.app.model.result.OutstockOrderResult;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
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
    private DeliveryService deliveryService;
    @Autowired
    private DeliveryDetailsService deliveryDetailsService;


    @Override
    public Long add(OutstockOrderParam param) {
        OutstockOrder entity = getEntity(param);
        this.save(entity);
        return entity.getOutstockOrderId();
    }

    @Override
    public void delete(OutstockOrderParam param) {
        this.removeById(getKey(param));
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
            QueryWrapper<StockDetails> queryWrapper = new QueryWrapper<>();

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
                                && StockList.getStorehouseId().equals(outstock.getStorehouseId())
                        ) {
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
    public void update(OutstockOrderParam param) {

        OutstockOrder oldEntity = getOldEntity(param);
        OutstockOrder newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
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
    public PageInfo<OutstockOrderResult> findPageBySpec(OutstockOrderParam param) {
        Page<OutstockOrderResult> pageContext = getPageContext();
        IPage<OutstockOrderResult> page = this.baseMapper.customPageList(pageContext, param);
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

}
