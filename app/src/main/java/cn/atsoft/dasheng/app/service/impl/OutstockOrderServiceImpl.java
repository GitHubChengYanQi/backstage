package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.StockParam;
import cn.atsoft.dasheng.app.service.OutstockService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.app.service.StockService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.mapper.OutstockOrderMapper;
import cn.atsoft.dasheng.app.model.params.OutstockOrderParam;
import cn.atsoft.dasheng.app.model.result.OutstockOrderResult;
import  cn.atsoft.dasheng.app.service.OutstockOrderService;
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


    @Override
    public Long add(OutstockOrderParam param){
        OutstockOrder entity = getEntity(param);
        this.save(entity);
        return entity.getOutstockOrderId();
    }

    @Override
    public void delete(OutstockOrderParam param){
        this.removeById(getKey(param));
    }


    @Override
    public void outStock(OutstockOrderParam param){

        OutstockOrder entity = getEntity(param);
        Long outStockOrderId = entity.getOutstockOrderId();
        // 判断出库单对应出库明细数据有无
        QueryWrapper<Outstock> outstockQueryWrapper = new QueryWrapper<>();
        if(ToolUtil.isNotEmpty(outStockOrderId)){
            outstockQueryWrapper.in("outstock_order_id" , outStockOrderId);
        }
        List<Outstock> outstockList = outstockService.list(outstockQueryWrapper);

        if(ToolUtil.isNotEmpty(outstockList)){
            List<Stock> Stock = this.stockService.list();
            List<StockDetails> stockDetail = this.stockDetailsService.list();
            // 出库明细里进行出库
            for(int i = 0; i < outstockList.size(); i++){
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
                                    for (int j = 0; j < outstock.getNumber(); j++) {
                                        StockDetails stockDetailList = stockDetail.get(j);
                                        // 匹配库存明细里对应的数据
                                        if (stockDetailList.getStockId().equals(StockList.getStockId())) {
                                            // 减去出库明细产品
                                            StockDetails StockDetailsList = stockDetail.get(j);
                                            stockItemIds.add(StockDetailsList.getStockItemId());
                                        }
                                    }
                                    this.stockDetailsService.removeByIds(stockItemIds);
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
        }else{
            throw new ServiceException(500, "仓库没有此产品！");
        }
    }

    @Override
    public void update(OutstockOrderParam param){

        OutstockOrder oldEntity = getOldEntity(param);
        OutstockOrder newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public OutstockOrderResult findBySpec(OutstockOrderParam param){
        return null;
    }

    @Override
    public List<OutstockOrderResult> findListBySpec(OutstockOrderParam param){
        return null;
    }

    @Override
    public PageInfo<OutstockOrderResult> findPageBySpec(OutstockOrderParam param){
        Page<OutstockOrderResult> pageContext = getPageContext();
        IPage<OutstockOrderResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(OutstockOrderParam param){
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