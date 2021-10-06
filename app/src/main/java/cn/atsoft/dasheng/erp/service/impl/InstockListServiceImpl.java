package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.Instock;
import cn.atsoft.dasheng.app.entity.Stock;
import cn.atsoft.dasheng.app.model.params.InstockParam;
import cn.atsoft.dasheng.app.model.params.StockDetailsParam;
import cn.atsoft.dasheng.app.model.params.StockParam;
import cn.atsoft.dasheng.app.service.InstockService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.app.service.StockService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.InstockList;
import cn.atsoft.dasheng.erp.mapper.InstockListMapper;
import cn.atsoft.dasheng.erp.model.params.InstockListParam;
import cn.atsoft.dasheng.erp.model.result.InstockListResult;
import cn.atsoft.dasheng.erp.service.InstockListService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 入库清单 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-06
 */
@Service
public class InstockListServiceImpl extends ServiceImpl<InstockListMapper, InstockList> implements InstockListService {
    @Autowired
    private InstockService instockService;
    @Autowired
    private StockService stockService;
    @Autowired
    private StockDetailsService stockDetailsService;

    @Override
    public void add(InstockListParam param) {
        InstockList entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(InstockListParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(InstockListParam param) {
        InstockList oldEntity = getOldEntity(param);
        InstockList newEntity = getEntity(param);
        if (oldEntity.getNumber() > 0) {
            long l = oldEntity.getNumber() - 1;
            newEntity.setNumber(l);
            ToolUtil.copyProperties(newEntity, oldEntity);
            this.updateById(newEntity);


            InstockParam instockParam = new InstockParam();
            instockParam.setState(1);
            instockParam.setBrandId(newEntity.getBrandId());
            instockParam.setItemId(newEntity.getItemId());
            instockParam.setStoreHouseId(newEntity.getStoreHouseId());
            instockService.add(instockParam);

            Stock stock = stockService.lambdaQuery().eq(Stock::getStorehouseId, newEntity.getStoreHouseId())
                    .and(i -> i.eq(Stock::getItemId, newEntity.getItemId()))
                    .and(i -> i.eq(Stock::getBrandId, newEntity.getBrandId()))
                    .one();
            StockParam stockParam = new StockParam();
            Long stockId = null;
            if (ToolUtil.isNotEmpty(stock)) {
                long number = stock.getInventory() + 1;
                stock.setInventory(number);
                ToolUtil.copyProperties(stock, stockParam);
                stockId = stockService.update(stockParam);
            } else {
                stockParam.setInventory(1L);
                stockParam.setItemId(newEntity.getItemId());
                stockParam.setBrandId(newEntity.getBrandId());
                stockParam.setStorehouseId(newEntity.getStoreHouseId());
                stockId = stockService.add(stockParam);
            }
            StockDetailsParam stockDetailsParam = new StockDetailsParam();
            stockDetailsParam.setStockId(stockId);
            stockDetailsParam.setStorehouseId(newEntity.getStoreHouseId());
            stockDetailsParam.setItemId(newEntity.getItemId());
            stockDetailsParam.setBrandId(newEntity.getBrandId());
            stockDetailsService.add(stockDetailsParam);

        } else {
            throw new ServiceException(500, "已经入库成功");
        }

    }

    @Override
    public InstockListResult findBySpec(InstockListParam param) {
        return null;
    }

    @Override
    public List<InstockListResult> findListBySpec(InstockListParam param) {
        return null;
    }

    @Override
    public PageInfo<InstockListResult> findPageBySpec(InstockListParam param) {
        Page<InstockListResult> pageContext = getPageContext();
        IPage<InstockListResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(InstockListParam param) {
        return param.getInstockListId();
    }

    private Page<InstockListResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private InstockList getOldEntity(InstockListParam param) {
        return this.getById(getKey(param));
    }

    private InstockList getEntity(InstockListParam param) {
        InstockList entity = new InstockList();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
