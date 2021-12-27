package cn.atsoft.dasheng.inventory.service.impl;


import cn.atsoft.dasheng.app.entity.Stock;
import cn.atsoft.dasheng.app.model.result.StockDetailsResult;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.app.service.StockService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Inkind;
import cn.atsoft.dasheng.erp.service.InkindService;
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
    private OrCodeBindService codeBindService;
    @Autowired
    private InkindService inkindService;
    @Autowired
    private StockService stockService;
    @Autowired
    private StockDetailsService detailsService;
    @Autowired
    private InventoryDetailService inventoryDetailService;

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
        OrCodeBind codeBind = codeBindService.query().eq("qr_code_id", inventoryRequest.getQrcodeId()).one();
        if (ToolUtil.isEmpty(codeBind) && !codeBind.getSource().equals("item")) {
            throw new ServiceException(500, "二维码不合法");
        }
        //获取实物
        Inkind inkind = inkindService.getById(codeBind.getFormId());
        //查询仓库
        Stock stock = stockService.query().eq("storehouse_id", inventoryRequest.getStoreHouseId())
                .eq("brand_id", inkind.getBrandId())
                .eq("sku_id", inkind.getSkuId()).one();
        //查看仓库是否有次实物
        List<StockDetailsResult> stockDetails = detailsService.getStockDetails(stock.getStockId());
        InventoryDetail inventoryDetail = null;
        for (StockDetailsResult stockDetail : stockDetails) {
            if (stockDetail.getInkindId().equals(inkind.getInkindId())) {  //有次无正常记录
                inventoryDetail = new InventoryDetail();
                inventoryDetail.setInkindId(inkind.getInkindId());
                inventoryDetail.setStatus(1);
                break;
            }
        }
        if (ToolUtil.isEmpty(inventoryDetail)) {  //没有此物 异常处理
            inventoryDetail = new InventoryDetail();
            inventoryDetail.setInkindId(inkind.getInkindId());
            inventoryDetail.setStatus(2);
        }
        inventoryDetailService.save(inventoryDetail);
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
