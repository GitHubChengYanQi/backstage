package cn.atsoft.dasheng.stockLog.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.stockDetail.entity.RestStockDetails;
import cn.atsoft.dasheng.stockLog.entity.RestStockLog;
import cn.atsoft.dasheng.stockLog.entity.RestStockLogDetail;
import cn.atsoft.dasheng.stockLog.mapper.RestStockLogMapper;
import cn.atsoft.dasheng.stockLog.model.params.RestStockLogParam;
import cn.atsoft.dasheng.stockLog.model.result.RestStockLogResult;
import cn.atsoft.dasheng.stockLog.service.RestStockLogDetailService;
import cn.atsoft.dasheng.stockLog.service.RestStockLogService;
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
public class RestRestStockLogServiceImpl extends ServiceImpl<RestStockLogMapper, RestStockLog> implements RestStockLogService {
    @Autowired
    private RestStockLogDetailService stockLogDetailService;

    @Override
    public void add(RestStockLogParam param){
        RestStockLog entity = getEntity(param);
        this.save(entity);
    }
    @Override
    public void addBatch(List<RestStockLogParam> param){

        for (RestStockLogParam restStockLogParam : param) {
            RestStockLog entity = getEntity(restStockLogParam);
            int number = 0;
            for (RestStockLogDetail stockLogDetail : restStockLogParam.getStockLogDetails()) {
                number+=stockLogDetail.getNumber();
            }
            entity.setNumber(number);
            this.save(entity);
            for (RestStockLogDetail stockLogDetail : restStockLogParam.getStockLogDetails()) {
                stockLogDetail.setStockLogId(entity.getStockLogId());
            }
            stockLogDetailService.saveBatch(restStockLogParam.getStockLogDetails());
        }
    }
    @Override
    public void addBatch(List<RestStockDetails> param,String source ,String type){
        /**
         * TODO afterNumber beforeNumber
         */
        List<RestStockDetails> totalList = new ArrayList<>();
        param.parallelStream().collect(Collectors.groupingBy(item -> item.getSkuId() + "_" + item.getStorehousePositionsId(), Collectors.toList())).forEach(
                (id, transfer) -> {
                    transfer.stream().reduce((a, b) -> new RestStockDetails() {{
                        setSkuId(a.getSkuId());
                        setNumber(a.getNumber() + b.getNumber());
                        setStorehouseId(a.getStorehouseId());
                        setStorehousePositionsId(a.getStorehousePositionsId());
                    }}).ifPresent(totalList::add);
                }
        );
        List<RestStockLog> stockLogs = new ArrayList<>();


        for (RestStockDetails stockDetails : totalList) {
            stockLogs.add( new RestStockLog(){{
                setNumber(Math.toIntExact(stockDetails.getNumber()));
                setSkuId(stockDetails.getSkuId());
                setType("increase");
                setStorehousePositionsId(stockDetails.getStorehousePositionsId());
            }});

        }
        this.saveBatch(stockLogs);
        List<RestStockLogDetail> logDetails = new ArrayList<>();
        for (RestStockLog stockLog : stockLogs) {
            for (RestStockDetails stockDetails : param) {
                if (stockLog.getSkuId().equals(stockDetails.getSkuId()) && stockLog.getStorehousePositionsId().equals(stockDetails.getStorehousePositionsId())){
                    logDetails.add(new RestStockLogDetail(){{
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
    public void addByOutStock(List<RestStockLog> restStockLogs, List<RestStockLogDetail> restStockLogDetails){
        this.saveBatch(restStockLogs);
        for (RestStockLog restStockLog : restStockLogs) {
            for (RestStockLogDetail restStockLogDetail : restStockLogDetails) {
                if (restStockLog.getSkuId().equals(restStockLogDetail.getSkuId())){
                    restStockLogDetail.setStockLogId(restStockLog.getStockLogId());
                }
            }
        }
        stockLogDetailService.saveBatch(restStockLogDetails);
    }

    @Override
    public void delete(RestStockLogParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(RestStockLogParam param){
        RestStockLog oldEntity = getOldEntity(param);
        RestStockLog newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public RestStockLogResult findBySpec(RestStockLogParam param){
        return null;
    }

    @Override
    public List<RestStockLogResult> findListBySpec(RestStockLogParam param){
        return null;
    }

    @Override
    public PageInfo<RestStockLogResult> findPageBySpec(RestStockLogParam param){
        Page<RestStockLogResult> pageContext = getPageContext();
        IPage<RestStockLogResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(RestStockLogParam param){
        return param.getStockLogId();
    }

    private Page<RestStockLogResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private RestStockLog getOldEntity(RestStockLogParam param) {
        return this.getById(getKey(param));
    }

    private RestStockLog getEntity(RestStockLogParam param) {
        RestStockLog entity = new RestStockLog();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
