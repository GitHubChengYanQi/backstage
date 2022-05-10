package cn.atsoft.dasheng.inventory.service.impl;


import cn.atsoft.dasheng.app.entity.Stock;
import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.model.result.StockDetailsResult;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.app.service.StockService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Inkind;
import cn.atsoft.dasheng.erp.entity.InstockOrder;
import cn.atsoft.dasheng.erp.entity.StorehousePositions;
import cn.atsoft.dasheng.erp.model.result.InkindResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.erp.service.InkindService;
import cn.atsoft.dasheng.erp.service.InstockOrderService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsService;
import cn.atsoft.dasheng.inventory.entity.Inventory;
import cn.atsoft.dasheng.inventory.entity.InventoryDetail;
import cn.atsoft.dasheng.inventory.mapper.InventoryMapper;
import cn.atsoft.dasheng.inventory.model.params.InventoryParam;
import cn.atsoft.dasheng.inventory.model.result.InventoryResult;
import cn.atsoft.dasheng.inventory.pojo.InventoryRequest;
import cn.atsoft.dasheng.inventory.service.InventoryDetailService;
import cn.atsoft.dasheng.inventory.service.InventoryService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.bouncycastle.cms.PasswordRecipientId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * <p>
 * 盘点任务主表 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-27
 */
@Service
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory> implements InventoryService {

    @Autowired
    private InkindService inkindService;
    @Autowired
    private StockDetailsService detailsService;
    @Autowired
    private StorehousePositionsService positionsService;
    @Autowired
    private StockService stockService;

    @Autowired
    private OrCodeBindService codeBindService;
    @Autowired
    private InventoryDetailService inventoryDetailService;

    @Override
    public void add(InventoryParam param) {
        Inventory entity = getEntity(param);
        this.save(entity);

    }

    @Override
    public void delete(InventoryParam param) {
        Inventory entity = getEntity(param);
        entity.setDisplay(0);
        this.updateById(entity);
//        this.removeById(getKey(param));
    }

    @Override
    public void update(InventoryParam param) {
        Inventory oldEntity = getOldEntity(param);
        Inventory newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public InventoryResult findBySpec(InventoryParam param) {
        return null;
    }

    @Override
    public List<InventoryResult> findListBySpec(InventoryParam param) {
        return null;
    }

    @Override
    public PageInfo<InventoryResult> findPageBySpec(InventoryParam param) {
        Page<InventoryResult> pageContext = getPageContext();
        IPage<InventoryResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    /**
     * 盘点
     *
     * @param inventoryRequest
     */
    @Override
    public void inventory(InventoryRequest inventoryRequest) {
        List<InventoryRequest.InkindParam> params = inventoryRequest.getInkindParams();

        List<Long> inkindIds = new ArrayList<>();

        for (InventoryRequest.InkindParam param : params) {
            inkindIds.add(param.getInkindId());
            if (param.getNumber() == 0) {
                throw new ServiceException(500, "请确定数量");
            }
            Integer count = detailsService.query().eq("inkind_id", param.getInkindId()).count();
            if (count > 1) {
                throw new ServiceException(500, "已有相同实物");
            }
        }

        long count = inkindIds.stream().distinct().count();
        if (count != params.size()) {
            throw new ServiceException(500, "请勿重复添加相同物料");
        }

        List<StockDetails> details = inkindIds.size() == 0 ? new ArrayList<>() : detailsService.query().in("inkind_id", inkindIds).list();

        List<InventoryDetail> inventories = new ArrayList<>();
        List<Long> outInkind = new ArrayList<>();
        List<Long> sotckIds = new ArrayList<>();
        List<Long> stockInkinds = new ArrayList<>();
        InventoryDetail inventory = null;

        //添加盘点数据----------------------------------------------------------------------------------------------------

        for (StockDetails detail : details) {
            stockInkinds.add(detail.getInkindId());
            for (InventoryRequest.InkindParam param : params) {
                if (detail.getInkindId().equals(param.getInkindId())) {  //相同实物

                    sotckIds.add(detail.getStockId());

                    if (detail.getNumber() > param.getNumber()) {  //修正出库
                        inventory = new InventoryDetail();
                        inventory.setInkindId(param.getInkindId());
                        inventory.setStatus(2);
                        outInkind.add(param.getInkindId());
                        inventories.add(inventory);
                    } else if (detail.getNumber() < param.getNumber()) { //修正入库
                        inventory = new InventoryDetail();
                        inventory.setInkindId(param.getInkindId());
                        inventory.setStatus(1);
                        inventories.add(inventory);
                    }
                    detail.setNumber(param.getNumber());
                }
            }
        }


        List<Long> collect = inkindIds.stream().filter(item -> !stockInkinds.contains(item)).collect(toList());
//------------------------------------------------------------------------------------------------------------------------新入库
        if (ToolUtil.isNotEmpty(collect)) {
            List<StockDetails> stockDetails = detailsService.query().in("inkind_id", collect).list();
            if (ToolUtil.isNotEmpty(stockDetails)) {
                throw new ServiceException(500, "新入库的物料 在库存中已存在");
            }
            List<Inkind> inkindList = collect.size() == 0 ? new ArrayList<>() : inkindService.listByIds(collect);

            Map<Long, Long> numberMap = new HashMap<>();
            Map<Long, Long> codeMap = new HashMap<>();

            for (InventoryRequest.InkindParam inkindParam : inventoryRequest.getInkindParams()) {
                for (Long aLong : collect) {
                    if (aLong.equals(inkindParam.getInkindId())) {   //实物id 和 数量
                        numberMap.put(aLong, inkindParam.getNumber());
                    }
                }
            }
            List<OrCodeBind> codeBinds = collect.size() == 0 ? new ArrayList<>() : codeBindService.query().in("form_id", collect).list();

            for (Inkind inkind : inkindList) {
                for (OrCodeBind codeBind : codeBinds) {
                    if (inkind.getInkindId().equals(codeBind.getFormId())) {
                        codeMap.put(inkind.getInkindId(), codeBind.getOrCodeId());
                    }
                }
            }
            inkindInstock(inkindList, numberMap, codeMap, inventoryRequest.getStoreHouseId(), inventoryRequest.getPositionId());
        }
//--------------------------------------------------------------------------------------------------------------------------新出库
        if (ToolUtil.isNotEmpty(inventoryRequest.getOutStockInkindParams())) {
            List<Long> outStockInkind = new ArrayList<>();
            for (InventoryRequest.outStockInkindParam outStockInkindParam : inventoryRequest.getOutStockInkindParams()) {
                outStockInkind.add(outStockInkindParam.getInkindId());
            }
            inkindOutstock(outStockInkind); //出库
        }
        detailsService.updateBatchById(details);
        inventoryDetailService.saveBatch(inventories);
        if (sotckIds.size() > 0) {
            stockService.updateNumber(sotckIds);
        }


    }

    //盘点入库
    private void inkindInstock(List<Inkind> inkinds, Map<Long, Long> numberMap, Map<Long, Long> codeMap, Long storeHouseId, Long positionId) {

        List<InventoryDetail> inventoryDetails = new ArrayList<>();
        for (Inkind inkind : inkinds) {
            Stock stock = stockService.lambdaQuery().eq(Stock::getStorehouseId, storeHouseId)  //查询仓库
                    .eq(Stock::getSkuId, inkind.getSkuId())
                    .eq(Stock::getBrandId, inkind.getBrandId())
                    .one();
            Long stockId = null;
            if (ToolUtil.isNotEmpty(stock)) {  //有相同物料 叠加数量
                stock.setInventory(stock.getInventory() + numberMap.get(inkind.getInkindId()));
                stockService.updateById(stock);
                stockId = stock.getStockId();
            } else {                          //没有相同物料 创建新物料
                Stock newStock = new Stock();
                newStock.setInventory(numberMap.get(inkind.getInkindId()));
                newStock.setBrandId(inkind.getBrandId());
                newStock.setSkuId(inkind.getSkuId());
                newStock.setStorehouseId(storeHouseId);
                stockService.save(newStock);
                stockId = newStock.getStockId();
            }
            StockDetails stockDetails = new StockDetails();
            stockDetails.setStockId(stockId);
            stockDetails.setNumber(numberMap.get(inkind.getInkindId()));
            stockDetails.setStorehousePositionsId(positionId);
            stockDetails.setQrCodeId(codeMap.get(inkind.getInkindId()));
            stockDetails.setInkindId(inkind.getInkindId());
            stockDetails.setBrandId(inkind.getBrandId());
            stockDetails.setStorehouseId(storeHouseId);
            stockDetails.setSkuId(inkind.getSkuId());
            detailsService.save(stockDetails);
            inkind.setNumber(numberMap.get(inkind.getInkindId()));
            InventoryDetail inventoryDetail = new InventoryDetail();
            inventoryDetail.setInkindId(inkind.getInkindId());
            inventoryDetail.setStatus(1);
            inventoryDetails.add(inventoryDetail);
        }
        inkindService.updateBatchById(inkinds);
        inventoryDetailService.saveBatch(inventoryDetails);
    }

    //盘点出库
    private void inkindOutstock(List<Long> inkindIds) {

        List<Long> stockIds = new ArrayList<>();
        List<InventoryDetail> inventoryDetails = new ArrayList<>();
        List<StockDetails> outDetails = detailsService.query().in("inkind_id", inkindIds).list();
        for (StockDetails outDetail : outDetails) {
            stockIds.add(outDetail.getStockId());
        }

        detailsService.remove(new QueryWrapper<StockDetails>() {{
            in("inkind_id", inkindIds);
        }});

        List<Inkind> inkinds = inkindService.listByIds(inkindIds);
        List<Inkind> newInkinds = new ArrayList<>();
        for (Inkind inkind : inkinds) {
            Inkind newInkind = new Inkind();
            ToolUtil.copyProperties(inkind, newInkind);
            newInkind.setInkindId(null);
            newInkind.setSource("盘点出库");
            newInkinds.add(newInkind);
            inkind.setNumber(0L);
            InventoryDetail inventoryDetail = new InventoryDetail();
            inventoryDetail.setInkindId(inkind.getInkindId());
            inventoryDetail.setStatus(2);
            inventoryDetails.add(inventoryDetail);
        }
        inventoryDetailService.saveBatch(inventoryDetails);
        inkindService.updateBatchById(inkinds);
        inkindService.saveBatch(newInkinds);
        stockService.updateNumber(stockIds);
    }

    @Override
    public InkindResult inkindInventory(Long id) {
        InkindResult inkindResult = inkindService.getInkindResult(id);
        StockDetails stockDetails = detailsService.query().eq("inkind_id", inkindResult.getInkindId()).one();
        if (ToolUtil.isNotEmpty(stockDetails)) {
            StorehousePositionsResult positionsResult = positionsService.positionsResultById(stockDetails.getStorehousePositionsId());
            inkindResult.setPositionsResult(positionsResult);
            inkindResult.setInNotStock(true);
        } else {
            inkindResult.setInNotStock(false);
        }
        return inkindResult;
    }

    @Override
    public StorehousePositionsResult positionInventory(Long id) {
        StorehousePositionsResult positionsResult = positionsService.positionsResultById(id);

        List<StockDetails> stockDetails = detailsService.query().eq("storehouse_positions_id", positionsResult.getStorehousePositionsId()).list();
        if (ToolUtil.isNotEmpty(stockDetails)) {
            List<Long> inkindIds = new ArrayList<>();
            List<StockDetailsResult> detailsResults = new ArrayList<>();
            for (StockDetails stockDetail : stockDetails) {
                inkindIds.add(stockDetail.getInkindId());

                StockDetailsResult detailsResult = new StockDetailsResult();
                ToolUtil.copyProperties(stockDetail, detailsResult);
                detailsResult.setQrCodeId(stockDetail.getQrCodeId());
                detailsResults.add(detailsResult);
            }

            List<InkindResult> kinds = inkindService.getInKinds(inkindIds);
            for (StockDetailsResult detailsResult : detailsResults) {
                for (InkindResult kind : kinds) {
                    if (ToolUtil.isNotEmpty(detailsResult.getInkindId()) && detailsResult.getInkindId().equals(kind.getInkindId())) {
                        detailsResult.setInkindResult(kind);
                        break;
                    }
                }
            }
            positionsResult.setDetailsResults(detailsResults);
        }
        return positionsResult;
    }

    private Serializable getKey(InventoryParam param) {
        return param.getInventoryTaskId();
    }

    private Page<InventoryResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Inventory getOldEntity(InventoryParam param) {
        return this.getById(getKey(param));
    }

    private Inventory getEntity(InventoryParam param) {
        Inventory entity = new Inventory();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
