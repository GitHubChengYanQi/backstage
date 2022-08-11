package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.action.Enum.ReceiptsEnum;
import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.*;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.app.pojo.FreeOutStockParam;
import cn.atsoft.dasheng.app.pojo.Listing;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.mapper.OutstockOrderMapper;
import cn.atsoft.dasheng.app.model.result.OutstockOrderResult;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.config.MobileService;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.model.params.InkindParam;
import cn.atsoft.dasheng.erp.model.params.OutstockListingParam;
import cn.atsoft.dasheng.erp.model.result.OutstockListingResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.erp.service.CodingRulesService;
import cn.atsoft.dasheng.erp.service.InkindService;
import cn.atsoft.dasheng.erp.service.OutstockListingService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsService;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.service.ActivitiProcessService;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.message.enmu.AuditMessageType;
import cn.atsoft.dasheng.message.enmu.MicroServiceType;
import cn.atsoft.dasheng.message.enmu.OperationType;
import cn.atsoft.dasheng.message.entity.AuditEntity;
import cn.atsoft.dasheng.message.entity.MicroServiceEntity;
import cn.atsoft.dasheng.message.producer.MessageProducer;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.orCode.model.result.BackCodeRequest;
import cn.atsoft.dasheng.orCode.service.OrCodeService;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.sendTemplate.WxCpTemplate;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private UserService userService;
    @Autowired
    private OutstockListingService outstockListingService;
    @Autowired
    private StorehouseService storehouseService;
    @Autowired
    private CodingRulesService codingRulesService;
    @Autowired
    private OrCodeService orCodeService;
    @Autowired
    private InkindService inkindService;
    @Autowired
    private WxCpSendTemplate wxCpSendTemplate;
    @Autowired
    private StorehousePositionsService positionsService;
    @Autowired
    private MobileService mobileService;
    @Autowired
    private MessageProducer messageProducer;
    @Autowired
    private ActivitiProcessService activitiProcessService;
    @Autowired
    private ActivitiProcessTaskService activitiProcessTaskService;

    @Override
    public OutstockOrder add(OutstockOrderParam param) {

        if (ToolUtil.isEmpty(param.getCoding())) {
            CodingRules codingRules = codingRulesService.query().eq("module", "2").eq("state", 1).one();
            if (ToolUtil.isNotEmpty(codingRules)) {
                String coding = codingRulesService.backCoding(codingRules.getCodingRulesId());
                param.setCoding(coding);
            } else {
                throw new ServiceException(500, "请配置出库库单据自动生成编码规则");
            }
        }

        OutstockOrder entity = getEntity(param);
        this.save(entity);

        if (ToolUtil.isNotEmpty(param.getListingParams()) && param.getListingParams().size() > 0) {
            List<OutstockListingParam> listingParams = param.getListingParams();

            List<OutstockListing> outstockListings = new ArrayList<>();
            for (OutstockListingParam listingParam : listingParams) {
                OutstockListing outstockListing = new OutstockListing();
                ToolUtil.copyProperties(listingParam, outstockListing);
                outstockListing.setDelivery(listingParam.getNumber());
                outstockListing.setOutstockOrderId(entity.getOutstockOrderId());
                outstockListings.add(outstockListing);
            }

            outstockListingService.saveBatch(outstockListings);
        }

        ActivitiProcess activitiProcess = activitiProcessService.query().eq("type", ReceiptsEnum.OUTSTOCK.name()).eq("status", 99).eq("module", "createOutstock").one();
        if (ToolUtil.isNotEmpty(activitiProcess)) {
            LoginUser user = LoginContextHolder.getContext().getUser();
            ActivitiProcessTaskParam activitiProcessTaskParam = new ActivitiProcessTaskParam();
            activitiProcessTaskParam.setTaskName(user.getName() + "提交的出库 (" + param.getCoding() + ")");
            activitiProcessTaskParam.setQTaskId(entity.getOutstockOrderId());
            activitiProcessTaskParam.setUserId(param.getCreateUser());
            activitiProcessTaskParam.setFormId(entity.getOutstockOrderId());
            activitiProcessTaskParam.setType(ReceiptsEnum.OUTSTOCK.name());
            activitiProcessTaskParam.setProcessId(activitiProcess.getProcessId());
            Long taskId = activitiProcessTaskService.add(activitiProcessTaskParam);
            //添加小铃铛
            wxCpSendTemplate.setSource("processTask");
            wxCpSendTemplate.setSourceId(taskId);
            //添加log

            messageProducer.auditMessageDo(new AuditEntity() {{
                setMessageType(AuditMessageType.CREATE_TASK);
                setActivitiProcess(activitiProcess);
                setTaskId(taskId);
                setTimes(0);
                setMaxTimes(1);
            }});
//                activitiProcessLogService.addLog(activitiProcess.getProcessId(), taskId);
//                activitiProcessLogService.autoAudit(taskId, 1);
        } else {
            entity.setState(1);
            this.updateById(entity);
        }
        return entity;
    }




    /**
     * 出库单添加出库记录
     *
     * @param param
     */
    @Override
    public void addRecord(OutstockOrderParam param) {

        if (ToolUtil.isNotEmpty(param)) {
            OutstockOrder entity = new OutstockOrder();
            ToolUtil.copyProperties(param, entity);
            entity.setDisplay(0);
            this.save(entity);
            List<OutstockListing> listings = BeanUtil.copyToList(param.getListingParams(), OutstockListing.class, new CopyOptions());
            for (OutstockListing listing : listings) {
                listing.setOutstockOrderId(entity.getOutstockOrderId());
            }
            outstockListingService.saveBatch(listings);
        }
    }

    /**
     * 添加出库记录
     *
     * @param outstockListingParams
     */
    @Override
    public void addOutStockRecord(List<OutstockListingParam> outstockListingParams, String source) {

        OutstockOrderParam param = new OutstockOrderParam();
        param.setSource(source);
        param.setState(60);
        param.setDisplay(0);
        param.setCreateUser(LoginContextHolder.getContext().getUserId());
        param.setListingParams(outstockListingParams);

        /**
         * 调用消息队列
         */
        MicroServiceEntity microServiceEntity = new MicroServiceEntity();
        microServiceEntity.setOperationType(OperationType.SAVE);
        microServiceEntity.setType(MicroServiceType.OUTSTOCKORDER);
        microServiceEntity.setObject(param);
        microServiceEntity.setMaxTimes(2);
        microServiceEntity.setTimes(0);
        messageProducer.microService(microServiceEntity);
    }

    @Override
    public void saveOutStockOrderByPickLists(OutstockOrderParam param) {


        String encoding = codingRulesService.encoding(2);
        OutstockOrder entity = getEntity(param);
        entity.setCoding(encoding);
        this.save(entity);

//        if (ToolUtil.isNotEmpty(param.getApplyDetails()) && param.getApplyDetails().size() > 0) {
//            List<ApplyDetails> applyDetails = param.getApplyDetails();
//
//            List<OutstockListing> outstockListings = new ArrayList<>();
//            for (ApplyDetails applyDetail : applyDetails) {
//                OutstockListing outstockListing = new OutstockListing();
//                if (ToolUtil.isNotEmpty(applyDetail.getBrandId())) {
//                    outstockListing.setBrandId(applyDetail.getBrandId());
//                }
//                outstockListing.setSkuId(applyDetail.getSkuId());
//                outstockListing.setNumber(applyDetail.getNumber());
//                outstockListing.setDelivery(applyDetail.getNumber());
//                outstockListing.setOutstockOrderId(entity.getOutstockOrderId());
//                outstockListings.add(outstockListing);
//            }
//
//            outstockListingService.saveBatch(outstockListings);
//        }
        List<OutstockListing> listings = new ArrayList<>();
        if(ToolUtil.isNotEmpty(param.getListingParams())){
            for (OutstockListingParam listingParam : param.getListingParams()) {
                OutstockListing outstockListing = new OutstockListing();
                ToolUtil.copyProperties(listingParam,outstockListing);
                outstockListing.setOutstockOrderId(entity.getOutstockOrderId());
                listings.add(outstockListing);
            }
            outstockListingService.saveBatch(listings);

        }

        BackCodeRequest backCodeRequest = new BackCodeRequest();
        backCodeRequest.setId(entity.getOutstockOrderId());
        backCodeRequest.setSource("outstock");
        Long aLong = orCodeService.backCode(backCodeRequest);

    }

    /**
     * 出库单一键出库
     *
     * @param param
     */
    public void AkeyOutbound(OutstockOrderParam param) {
        outBound(param.getListingParams()); //出库
    }

    @Override
    public OutstockOrderResult getOrder(Long id) {
        OutstockOrder outstockOrder = this.getById(id);
        if (ToolUtil.isEmpty(outstockOrder)) {
            throw new ServiceException(500, "当前数据不存在");
        }
        OutstockOrderResult outstockResult = new OutstockOrderResult();
        ToolUtil.copyProperties(outstockOrder, outstockResult);

        Storehouse outStockStorehouse = storehouseService.getById(outstockResult.getStorehouseId());
        if (ToolUtil.isNotEmpty(outStockStorehouse)) {
            StorehouseResult storehouseResult1 = new StorehouseResult();
            ToolUtil.copyProperties(outStockStorehouse, storehouseResult1);
            outstockResult.setStorehouseResult(storehouseResult1);
        }
        User outStockUser = userService.getById(outstockOrder.getUserId());
        if (ToolUtil.isNotEmpty(outStockUser)) {
            UserResult userResult = new UserResult();
            ToolUtil.copyProperties(outStockUser, userResult);
            outstockResult.setUserResult(userResult);
        }

        getListing(outstockResult);
        return outstockResult;
    }

    private void getListing(OutstockOrderResult result) {
        List<OutstockListing> outstockListings = outstockListingService.query().eq("outstock_order_id", result.getOutstockOrderId()).list();
        List<OutstockListingResult> resultList = BeanUtil.copyToList(outstockListings, OutstockListingResult.class, new CopyOptions());
        outstockListingService.format(resultList);


        Map<Long, List<StorehousePositionsResult>> supperMap = new HashMap<>();
        for (OutstockListingResult outstockListingResult : resultList) {
            List<StorehousePositionsResult> supper = positionsService.getSupperBySkuId(outstockListingResult.getSkuId());

            supperMap.put(outstockListingResult.getSkuId(), supper);
        }


        List<Listing> list = new ArrayList<>();
        for (OutstockListingResult outstockListingResult : resultList) {
            Listing listing = new Listing();
            List<StorehousePositionsResult> positionsResults = supperMap.get(outstockListingResult.getSkuId());
            listing.setListingResult(outstockListingResult);
            listing.setPositionsResults(positionsResults);
            list.add(listing);
        }

        result.setListing(list);
    }

    private void mergePosition(List<Long> positionIds) {

    }


    @Override
    public void outBound(List<OutstockListingParam> listings) {
        List<StockDetails> details = stockDetailsService.query().orderByAsc("create_time").list();
        for (OutstockListingParam listing : listings) {
            if (ToolUtil.isEmpty(listing.getBrandId())) {
                AnyBrandOutBound(listing, details); //任意品牌
            } else {
                SkuBrandOutBound(listing, details);
            }
        }
        stockDetailsService.updateBatchById(details);

        StockDetails stockDetails = new StockDetails();
        stockDetails.setDisplay(0);
        stockDetailsService.update(stockDetails, new QueryWrapper<StockDetails>() {{
            eq("stage", 2);
        }});
//        stockDetailsService.remove(new QueryWrapper<StockDetails>() {{
//            eq("stage", 2);
//        }});
        addOutStockRecord(listings, "自由出库记录");  //添加记录
    }
    @Override
    public Map<Long, Long>  outBoundByLists(List<OutstockListingParam> listings) {

        Map<Long, Long> longLongMap = this.outStockByLists(listings);


//        addOutStockRecord(listings, "出库记录");
        return longLongMap;
    }
    public Map<Long,Long>  outStockByLists(List<OutstockListingParam> listings){
        List<Long> inkindIds = new ArrayList<>();
        for (OutstockListingParam listing : listings) {
            inkindIds.add(listing.getInkindId());
        }
        List<StockDetails> stockDetails =inkindIds.size() == 0 ? new ArrayList<>() : stockDetailsService.query().eq("display", 1).eq("stage", 1).in("inkind_id", inkindIds).orderByAsc("create_time").list();
        Map<Long,Long> updateInkind = new HashMap<>();
        List<Inkind> newInkinds = new ArrayList<>();
        List<Inkind> oldInkinds = new ArrayList<>();
        for (StockDetails stockDetail : stockDetails) {
            for (OutstockListingParam listing : listings) {
                if (stockDetail.getInkindId().equals(listing.getInkindId())){
                    long number = stockDetail.getNumber() - listing.getNumber();
                    if (number > 0){
                        Inkind newInkind = new Inkind();
                        newInkind.setSkuId(stockDetail.getSkuId());
                        newInkind.setSource("inkind");
                        newInkind.setSourceId(stockDetail.getInkindId());
                        if (ToolUtil.isNotEmpty(stockDetail.getBrandId())){
                            newInkind.setBrandId(stockDetail.getBrandId());
                        }if (ToolUtil.isNotEmpty(stockDetail.getCustomerId())){
                            newInkind.setCustomerId(stockDetail.getCustomerId());
                        }
                        newInkind.setSkuId(stockDetail.getSkuId());
                        newInkind.setNumber(listing.getNumber());
                        newInkinds.add(newInkind);
                        Inkind oldInkind = new Inkind();
                        oldInkind.setInkindId(stockDetail.getInkindId());
                        stockDetail.setNumber(number);
                        oldInkind.setNumber(number);
                        oldInkinds.add(oldInkind);
                        inkindService.save(newInkind);
                        updateInkind.put(oldInkind.getInkindId(),newInkind.getInkindId());
                    }else if (number == 0){
                        stockDetail.setNumber(0L);
                        stockDetail.setStage(2);
                        stockDetail.setDisplay(0);
                        updateInkind.put(stockDetail.getInkindId(),stockDetail.getInkindId());
                    }
                }
            }
        }

        stockDetailsService.updateBatchById(stockDetails);
        inkindService.updateBatchById(oldInkinds);

        return updateInkind;
    }
    /**
     * 任意品牌出库
     *
     * @param listingParam
     * @param details
     */
    private void AnyBrandOutBound(OutstockListingParam listingParam, List<StockDetails> details) {
        long number = 0;

        for (StockDetails detail : details) {
            if (listingParam.getSkuId().equals(detail.getSkuId()) && listingParam.getPositionsId().equals(detail.getStorehousePositionsId())) {
                number = number + detail.getNumber();
            }
        }
        if (listingParam.getNumber() > number) {
            throw new ServiceException(500, "数量不足");
        }

        for (StockDetails detail : details) {
            if (detail.getStage() == 1) {
                if (listingParam.getSkuId().equals(detail.getSkuId()) && detail.getStorehousePositionsId().equals(listingParam.getPositionsId())) {
                    number = detail.getNumber() - listingParam.getNumber();
                    if (number > 0) {
                        detail.setNumber(number);
                        Long newInkindId = inkindService.add(new InkindParam() {{
                            setSkuId(listingParam.getSkuId());
                            setNumber(listingParam.getNumber());
                            setSource("pick_lists");
                        }});
                        if (ToolUtil.isEmpty(listingParam.getInkindIds())) {
                            listingParam.setInkindIds(JSON.toJSONString(new ArrayList<Long>(){{
                                add(newInkindId);
                            }}));
                        }else {
                            List<Long> longs = JSON.parseArray(listingParam.getInkindIds(), Long.class);
                            longs.add(newInkindId);
                            listingParam.setInkindIds(JSON.toJSONString(longs));

                        }


                        break;
                    } else {
                        listingParam.setNumber(listingParam.getNumber() - detail.getNumber());
                        detail.setNumber(0L);
                        detail.setStage(2);
                        if (ToolUtil.isEmpty(listingParam.getInkindIds())) {
                            listingParam.setInkindIds(JSON.toJSONString(new ArrayList<Long>(){{
                                add(detail.getInkindId());
                            }}));
                        }else {
                            List<Long> longs = JSON.parseArray(listingParam.getInkindIds(), Long.class);
                            longs.add(detail.getInkindId());
                            listingParam.setInkindIds(JSON.toJSONString(longs));

                        }
                    }
                }
            }
        }
    }

    private void SkuBrandOutBound(OutstockListingParam listingParam, List<StockDetails> details) {
        long number = 0;
        for (StockDetails detail : details) {
            if (ToolUtil.isNotEmpty(detail.getBrandId()) && listingParam.getSkuId().equals(detail.getSkuId())
                    && listingParam.getBrandId().equals(detail.getBrandId())
                    && listingParam.getPositionsId().equals(detail.getStorehousePositionsId())) {
                number = number + detail.getNumber();
            }
        }
        if (listingParam.getNumber() > number) {
            throw new ServiceException(500, "数量不足");
        }

        for (StockDetails detail : details) {
            if (detail.getStage() == 1) {
                if (ToolUtil.isNotEmpty(detail.getBrandId()) && detail.getSkuId().equals(listingParam.getSkuId()) && detail.getBrandId().equals(listingParam.getBrandId())
                        && detail.getStorehousePositionsId().equals(listingParam.getPositionsId())) {
                    number = detail.getNumber() - listingParam.getNumber();
                    if (number > 0) {

                        detail.setNumber(number);
                        Long newInkindId = inkindService.add(new InkindParam() {{
                            setSkuId(listingParam.getSkuId());
                            setNumber(listingParam.getNumber());
                            setSource("pick_lists");
                        }});
                        if (ToolUtil.isEmpty(listingParam.getInkindIds())) {
                            listingParam.setInkindIds(JSON.toJSONString(new ArrayList<Long>(){{
                                add(newInkindId);
                            }}));
                        }else {
                            List<Long> longs = JSON.parseArray(listingParam.getInkindIds(), Long.class);
                            longs.add(newInkindId);
                            listingParam.setInkindIds(JSON.toJSONString(longs));

                        }
                        break;
                    } else {
                        listingParam.setNumber(listingParam.getNumber() - detail.getNumber());
                        detail.setNumber(0L);
                        detail.setStage(2);
                        if (ToolUtil.isEmpty(listingParam.getInkindIds())) {
                            listingParam.setInkindIds(JSON.toJSONString(new ArrayList<Long>(){{
                                add(detail.getInkindId());
                            }}));
                        }else {
                            List<Long> longs = JSON.parseArray(listingParam.getInkindIds(), Long.class);
                            longs.add(detail.getInkindId());
                            listingParam.setInkindIds(JSON.toJSONString(longs));

                        }
                    }
                }
            }
        }
    }


    @Override
    public void delete(OutstockOrderParam param) {
//        param.setDisplay(0);
//        this.update(param);
        throw new ServiceException(500, "不可以删除");
    }

    /**
     * 自由出库
     *
     * @param freeOutStockParam
     */
    @Override
    public void freeOutStock(FreeOutStockParam freeOutStockParam) {


        StockDetails stockDetails = stockDetailsService.query().eq("inkind_id", freeOutStockParam.getInkindId()).one();
        if (ToolUtil.isEmpty(stockDetails)) {
            throw new ServiceException(500, "库存没有此物料");
        }

        if (stockDetails.getNumber() < freeOutStockParam.getNumber()) {
            throw new ServiceException(500, "数量不足");
        }
        long newNumber = stockDetails.getNumber() - freeOutStockParam.getNumber();
        stockDetails.setNumber(newNumber);
        if (stockDetails.getNumber() == 0) {
            stockDetails.setDisplay(0);
        }
        stockDetailsService.updateById(stockDetails);


        //修改库存数量
        Inkind instockInkind = inkindService.getById(stockDetails.getInkindId());
        instockInkind.setNumber(instockInkind.getNumber() - freeOutStockParam.getNumber());
        inkindService.updateById(instockInkind);

        //创建实物
        Inkind inkind = new Inkind();
        inkind.setSource(freeOutStockParam.getType());
        inkind.setSkuId(stockDetails.getSkuId());
        inkind.setBrandId(stockDetails.getBrandId());
        inkind.setType("2");
        inkind.setCustomerId(stockDetails.getCustomerId());
        inkind.setNumber(freeOutStockParam.getNumber());
        inkindService.save(inkind);

        //更新库存数量
        stockService.updateNumber(new ArrayList<Long>() {{
            add(stockDetails.getStockId());
        }});
    }


    @Override
    public OutstockOrder update(OutstockOrderParam param) {
        throw new ServiceException(500, "不可以修改");
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
    public PageInfo<OutstockOrderResult> findPageBySpec(OutstockOrderParam param, DataScope dataScope) {
        Page<OutstockOrderResult> pageContext = getPageContext();
        IPage<OutstockOrderResult> page = this.baseMapper.customPageList(pageContext, param, dataScope);
        format(page.getRecords());
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

    public void format(List<OutstockOrderResult> data) {
        List<Long> ids = new ArrayList<>();
        List<Long> stockHouseIds = new ArrayList<>();

        for (OutstockOrderResult datum : data) {
            ids.add(datum.getUserId());
            stockHouseIds.add(datum.getStorehouseId());
        }
        List<User> users = ids.size() == 0 ? new ArrayList<>() : userService.lambdaQuery().in(User::getUserId, ids).list();

        List<Storehouse> storehouses = stockHouseIds.size() == 0 ? new ArrayList<>() : storehouseService.lambdaQuery().in(Storehouse::getStorehouseId, stockHouseIds).list();

        for (OutstockOrderResult datum : data) {


            for (User user : users) {
                if (datum.getUserId() != null && user.getUserId().equals(datum.getUserId())) {
                    UserResult userResult = new UserResult();
                    ToolUtil.copyProperties(user, userResult);
                    datum.setUserResult(userResult);
                    break;
                }
            }
            for (Storehouse storehouse : storehouses) {
                if (datum.getStorehouseId() != null && storehouse.getStorehouseId().equals(datum.getStorehouseId())) {
                    StorehouseResult storehouseResult = new StorehouseResult();
                    ToolUtil.copyProperties(storehouse, storehouseResult);
                    datum.setStorehouseResult(storehouseResult);
                    break;
                }
            }
        }
    }
}
