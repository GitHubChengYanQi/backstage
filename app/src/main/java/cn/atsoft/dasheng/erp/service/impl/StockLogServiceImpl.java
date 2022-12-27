package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.StockLog;
import cn.atsoft.dasheng.erp.entity.StockLogDetail;
import cn.atsoft.dasheng.erp.mapper.StockLogMapper;
import cn.atsoft.dasheng.erp.model.params.StockLogParam;
import cn.atsoft.dasheng.erp.model.result.StockLogResult;
import cn.atsoft.dasheng.erp.service.StockLogDetailService;
import  cn.atsoft.dasheng.erp.service.StockLogService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 库存操作记录主表 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-12-22
 */
@Service
public class StockLogServiceImpl extends ServiceImpl<StockLogMapper, StockLog> implements StockLogService {
    @Autowired
    private StockLogDetailService stockLogDetailService;

    @Override
    public void add(StockLogParam param){
        StockLog entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void addByOutStock(List<StockLog> stockLogs, List<StockLogDetail> stockLogDetails){
        this.saveBatch(stockLogs);
        for (StockLog stockLog : stockLogs) {
            for (StockLogDetail stockLogDetail : stockLogDetails) {
                if (stockLog.getSkuId().equals(stockLogDetail.getSkuId())){
                    stockLogDetail.setStockLogId(stockLog.getStockLogId());
                }
            }
        }
        stockLogDetailService.saveBatch(stockLogDetails);
    }

    @Override
    public void delete(StockLogParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(StockLogParam param){
        StockLog oldEntity = getOldEntity(param);
        StockLog newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public StockLogResult findBySpec(StockLogParam param){
        return null;
    }

    @Override
    public List<StockLogResult> findListBySpec(StockLogParam param){
        return null;
    }

    @Override
    public PageInfo<StockLogResult> findPageBySpec(StockLogParam param){
        Page<StockLogResult> pageContext = getPageContext();
        IPage<StockLogResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(StockLogParam param){
        return param.getStockLogId();
    }

    private Page<StockLogResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private StockLog getOldEntity(StockLogParam param) {
        return this.getById(getKey(param));
    }

    private StockLog getEntity(StockLogParam param) {
        StockLog entity = new StockLog();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
