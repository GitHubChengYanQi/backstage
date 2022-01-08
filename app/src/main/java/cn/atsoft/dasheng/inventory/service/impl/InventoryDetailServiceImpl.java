package cn.atsoft.dasheng.inventory.service.impl;


import cn.atsoft.dasheng.app.entity.Stock;
import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.app.service.StockService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Inkind;
import cn.atsoft.dasheng.erp.model.result.InkindResult;
import cn.atsoft.dasheng.erp.service.InkindService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsService;
import cn.atsoft.dasheng.inventory.entity.InventoryDetail;
import cn.atsoft.dasheng.inventory.mapper.InventoryDetailMapper;
import cn.atsoft.dasheng.inventory.model.params.InventoryDetailParam;
import cn.atsoft.dasheng.inventory.model.result.InventoryDetailResult;
import cn.atsoft.dasheng.inventory.pojo.InventoryRequest;
import cn.atsoft.dasheng.inventory.service.InventoryDetailService;
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
 * 盘点任务详情 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-27
 */
@Service
public class InventoryDetailServiceImpl extends ServiceImpl<InventoryDetailMapper, InventoryDetail> implements InventoryDetailService {

    @Autowired
    private InkindService inkindService;
    @Autowired
    private StockDetailsService detailsService;
    @Autowired
    private StorehousePositionsService positionsService;

    @Autowired
    private StockService stockService;

    @Override
    public void add(InventoryDetailParam param) {

        InventoryDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(InventoryDetailParam param) {
        this.removeById(getKey(param));
    }


    public void inventory(InventoryRequest inventoryRequest) {
        List<InventoryRequest.InkindParam> params = inventoryRequest.getInkindParams();
        List<Long> inkindIds = new ArrayList<>();

        for (InventoryRequest.InkindParam param : params) {
            inkindIds.add(param.getInkindId());
        }
        List<StockDetails> details = detailsService.query().in("inkind_id", inkindIds).list();

        List<InventoryDetail> inventories = new ArrayList<>();
        List<Long> outInkind = new ArrayList<>();
        InventoryDetail inventory = null;

        //添加盘点数据----------------------------------------------------------------------------------------------------
        for (StockDetails detail : details) {
            for (InventoryRequest.InkindParam param : params) {
                if (detail.getInkindId().equals(param.getInkindId())) {  //相同实物

                    if (detail.getNumber() > param.getNumber()) {  //出库
                        inventory = new InventoryDetail();
                        inventory.setInkindId(param.getInkindId());
                        inventory.setStatus(2);
                        outInkind.add(param.getInkindId());
                        inventories.add(inventory);
                    } else {                                       //入库
                        inventory = new InventoryDetail();
                        inventory.setInkindId(param.getInkindId());
                        inventory.setStatus(1);
                        inventories.add(inventory);
                    }
                    detail.setNumber(param.getNumber());
                }
            }
        }
        this.saveBatch(inventories);

    }

    /**
     * 盘点入库
     *
     * @param
     */
    @Override
    public void inventoryInstock(InventoryDetailParam inventoryDetailParam) {
        Inkind inkind = inkindService.getById(inventoryDetailParam.getInkindId());
        StockDetails details = detailsService.query().eq("inkind_id", inkind.getInkindId()).one();
        if (ToolUtil.isEmpty(details)) {
            Stock stock = stockService.lambdaQuery().eq(Stock::getStorehouseId, inventoryDetailParam.getStoreHouseId())  //查询仓库
                    .eq(Stock::getSkuId, inkind.getSkuId())
                    .eq(Stock::getBrandId, inkind.getBrandId())
                    .one();
            Long stockId = 0L;
            if (ToolUtil.isNotEmpty(stock)) {  //有相同物料 叠加数量
                stockId = stock.getStockId();
            } else {                          //没有相同物料 创建新物料
                Stock newStock = new Stock();
                newStock.setInventory(inventoryDetailParam.getNumber());
                newStock.setBrandId(inkind.getBrandId());
                newStock.setSkuId(inkind.getSkuId());
                newStock.setStorehouseId(inventoryDetailParam.getStoreHouseId());
                stockService.save(newStock);
                stockId = newStock.getStockId();
            }
            StockDetails stockDetails = new StockDetails();
            stockDetails.setStockId(stockId);
            stockDetails.setNumber(inventoryDetailParam.getNumber());
            stockDetails.setStorehousePositionsId(inventoryDetailParam.getPositionId());
            if (ToolUtil.isEmpty(inventoryDetailParam.getPositionId())) {
                throw new ServiceException(500, "请选择库位");
            }
            stockDetails.setQrCodeid(inventoryDetailParam.getQrcodeId());
            stockDetails.setInkindId(inkind.getInkindId());
            stockDetails.setBrandId(inkind.getBrandId());
            stockDetails.setStorehouseId(inventoryDetailParam.getStoreHouseId());
            stockDetails.setSkuId(inkind.getSkuId());
            detailsService.save(stockDetails);

            //修改实物数量

            inkind.setNumber(inventoryDetailParam.getNumber());
            inkindService.updateById(inkind);

            //跟新库存数量

            stockService.updateNumber(new ArrayList<Long>() {{
                add(stockDetails.getStockId());
            }});

            InventoryDetail inventoryDetail = new InventoryDetail();
            inventoryDetail.setInkindId(inkind.getInkindId());
            inventoryDetail.setStatus(1);
            this.save(inventoryDetail);
        } else {
            StockDetails stockDetails = new StockDetails();
            stockDetails.setNumber(inventoryDetailParam.getNumber());
            detailsService.update(stockDetails, new QueryWrapper<StockDetails>() {{
                eq("inkind_id", inkind.getInkindId());
            }});
            stockService.updateNumber(new ArrayList<Long>() {{
                add(details.getStockId());
            }});
            inkind.setNumber(inventoryDetailParam.getNumber());
            inkindService.updateById(inkind);
        }

    }

    @Override
    public void update(InventoryDetailParam param) {
        InventoryDetail oldEntity = getOldEntity(param);
        InventoryDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public InventoryDetailResult findBySpec(InventoryDetailParam param) {
        return null;
    }

    @Override
    public List<InventoryDetailResult> findListBySpec(InventoryDetailParam param) {
        return null;
    }

    @Override
    public PageInfo<InventoryDetailResult> findPageBySpec(InventoryDetailParam param) {
        Page<InventoryDetailResult> pageContext = getPageContext();
        IPage<InventoryDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(InventoryDetailParam param) {
        return param.getDetailId();
    }

    private Page<InventoryDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private InventoryDetail getOldEntity(InventoryDetailParam param) {
        return this.getById(getKey(param));
    }

    private InventoryDetail getEntity(InventoryDetailParam param) {
        InventoryDetail entity = new InventoryDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }


}
