package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.service.StockDetailsService;
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
import cn.atsoft.dasheng.form.pojo.ProcessType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private StockDetailsService stockDetailsService;

    @Override
    public StockLog add(StockLogParam param){
        StockLog entity = getEntity(param);
        this.save(entity);
        return entity;
    }
    @Override
    public void addBatch(List<StockDetails> param, String source, String type, ProcessType numberType){
        List<Long> skuIds = param.stream().map(StockDetails::getSkuId).distinct().collect(Collectors.toList());
        List<StockDetails> stockNumberList = stockDetailsService.getNumberCountEntityBySkuIds(skuIds);
        /**
         * TODO afterNumber beforeNumber
         */
        List<StockDetails> totalList = new ArrayList<>();
        param.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + "_" + item.getStorehousePositionsId(), Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new StockDetails() {{
                        setSkuId(a.getSkuId());
                        setNumber(a.getNumber() + b.getNumber());
                        setStorehouseId(a.getStorehouseId());
                        setStorehousePositionsId(a.getStorehousePositionsId());
                    }}).ifPresent(totalList::add);
                }
        );
        List<StockLog> stockLogs = new ArrayList<>();


        for (StockDetails stockDetails : totalList) {
            stockLogs.add( new StockLog(){{
                for (StockDetails details : stockNumberList) {

                    if (details.getSkuId().equals(stockDetails.getSkuId())){
                        setBeforeNumber(Math.toIntExact(details.getNumber()));
                        if (numberType.equals(ProcessType.OUTSTOCK)){
                            setAfterNumber((int) (details.getNumber()-stockDetails.getNumber()));
                        }else {
                            setAfterNumber((int) (details.getNumber()+stockDetails.getNumber()));
                        }

                    }
                }
                setNumber(Math.toIntExact(stockDetails.getNumber()));
                setSkuId(stockDetails.getSkuId());
                setType(type);
                setStorehousePositionsId(stockDetails.getStorehousePositionsId());
            }});

        }
        this.saveBatch(stockLogs);
        List<StockLogDetail> logDetails = new ArrayList<>();
        for (StockLog stockLog : stockLogs) {
            for (StockDetails stockDetails : param) {
                if (stockLog.getSkuId().equals(stockDetails.getSkuId()) && stockLog.getStorehousePositionsId().equals(stockDetails.getStorehousePositionsId())){
                    logDetails.add(new StockLogDetail(){{
                        for (StockDetails details : stockNumberList) {
                            if (details.getSkuId().equals(stockDetails.getSkuId())){
                                setBeforeNumber(Math.toIntExact(details.getNumber()));
                                if (numberType.equals(ProcessType.OUTSTOCK)){
                                    setAfterNumber((int) (details.getNumber()-stockDetails.getNumber()));
                                    details.setNumber(details.getNumber()-stockDetails.getNumber());
                                }else {
                                    setAfterNumber((int) (details.getNumber()+stockDetails.getNumber()));
                                    details.setNumber(details.getNumber()+stockDetails.getNumber());
                                }

                            }
                        }
                        setSkuId(stockDetails.getSkuId());
                        setInkindId(stockDetails.getInkindId());
                        setNumber(Math.toIntExact(stockDetails.getNumber()));
                        setStorehouseId(stockDetails.getStorehouseId());
                        setStorehousePositionsId(stockDetails.getStorehousePositionsId());
                        setType(type);
                        setSource(source);
                        setStockLogId(stockLog.getStockLogId());
                    }});
                }
            }
        }

        stockLogDetailService.saveBatch(logDetails);
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

    @Override
    public Integer todayInStockNumber(){
        return this.baseMapper.todayInStockNumber();
    }

    @Override
    public Integer todayOutStockNumber(){
        return this.baseMapper.todayOutStockNumber();
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
