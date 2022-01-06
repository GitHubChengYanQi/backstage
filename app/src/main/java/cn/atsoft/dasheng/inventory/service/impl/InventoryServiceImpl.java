package cn.atsoft.dasheng.inventory.service.impl;


import cn.atsoft.dasheng.app.entity.Stock;
import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.model.result.StockDetailsResult;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.app.service.StockService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Inkind;
import cn.atsoft.dasheng.erp.entity.StorehousePositions;
import cn.atsoft.dasheng.erp.model.result.InkindResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.erp.service.InkindService;
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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.bouncycastle.cms.PasswordRecipientId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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


    @Override
    public void add(InventoryParam param) {
        Inventory entity = getEntity(param);
        this.save(entity);

    }

    @Override
    public void delete(InventoryParam param) {
        this.removeById(getKey(param));
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



    }

    @Override
    public InkindResult inkindInventory(Long id) {
        InkindResult inkindResult = inkindService.getInkindResult(id);
        StockDetails stockDetails = detailsService.query().eq("inkind_id", inkindResult.getInkindId()).one();
        if (ToolUtil.isNotEmpty(stockDetails)) {
            StorehousePositionsResult positionsResult = positionsService.positionsResultById(stockDetails.getStorehousePositionsId());
            inkindResult.setPositionsResult(positionsResult);
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
                detailsResults.add(detailsResult);
            }

            List<InkindResult> kinds = inkindService.getInKinds(inkindIds);
            for (StockDetailsResult detailsResult : detailsResults) {
                for (InkindResult kind : kinds) {
                    if (detailsResult.getInkindId().equals(kind.getInkindId())) {
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
