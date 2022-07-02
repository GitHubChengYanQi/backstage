package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.action.Enum.ReceiptsEnum;
import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.model.result.StockDetailsResult;
import cn.atsoft.dasheng.app.service.OutstockOrderService;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.app.service.StockService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.config.MobileService;
import cn.atsoft.dasheng.erp.entity.Inkind;
import cn.atsoft.dasheng.erp.model.params.InstockListParam;
import cn.atsoft.dasheng.erp.model.params.OutstockListingParam;
import cn.atsoft.dasheng.erp.model.result.InkindResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.erp.service.InkindService;
import cn.atsoft.dasheng.erp.service.InstockOrderService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsService;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.model.params.RemarksParam;
import cn.atsoft.dasheng.form.service.ActivitiProcessLogService;
import cn.atsoft.dasheng.form.service.ActivitiProcessService;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.form.service.RemarksService;
import cn.atsoft.dasheng.erp.entity.Inventory;
import cn.atsoft.dasheng.erp.entity.InventoryDetail;
import cn.atsoft.dasheng.erp.mapper.InventoryMapper;
import cn.atsoft.dasheng.erp.model.params.InventoryParam;
import cn.atsoft.dasheng.erp.model.result.InventoryResult;
import cn.atsoft.dasheng.erp.pojo.InventoryRequest;
import cn.atsoft.dasheng.erp.service.InventoryDetailService;
import cn.atsoft.dasheng.erp.service.InventoryService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.message.enmu.AuditMessageType;
import cn.atsoft.dasheng.message.enmu.OperationType;
import cn.atsoft.dasheng.message.entity.AuditEntity;
import cn.atsoft.dasheng.message.entity.MarkDownTemplate;
import cn.atsoft.dasheng.message.entity.RemarksEntity;
import cn.atsoft.dasheng.message.producer.MessageProducer;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.*;

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
    @Autowired
    private InstockOrderService instockOrderService;
    @Autowired
    private OutstockOrderService outstockOrderService;
    @Autowired
    private ActivitiProcessService activitiProcessService;
    @Autowired
    private ActivitiProcessTaskService activitiProcessTaskService;
    @Autowired
    private ActivitiProcessLogService activitiProcessLogService;
    @Autowired
    private WxCpSendTemplate wxCpSendTemplate;
    @Autowired
    private MessageProducer messageProducer;
    @Autowired
    private RemarksService remarksService;
    @Autowired
    private MobileService mobileService;

    @Override
    @Transactional
    public Inventory add(InventoryParam param) {
        Inventory entity = getEntity(param);
        this.save(entity);

        if (ToolUtil.isEmpty(param.getDetailParams())) {
            throw new ServiceException(500, "缺少参数");
        }
        List<InventoryDetail> inventoryDetails = BeanUtil.copyToList(param.getDetailParams(), InventoryDetail.class, new CopyOptions());
        for (InventoryDetail inventoryDetail : inventoryDetails) {
            inventoryDetail.setInventoryId(entity.getInventoryTaskId());
        }
        inventoryDetailService.saveBatch(inventoryDetails);

        param.setInventoryTaskId(entity.getInventoryTaskId());
        param.setCreateUser(entity.getCreateUser());
        submit(param);
        return entity;
    }

    /**
     * 创建任务
     *
     * @param param
     */
    private void submit(InventoryParam param) {
        ActivitiProcess activitiProcess = activitiProcessService.query().eq("type", ReceiptsEnum.Stocktaking.name()).eq("status", 99).eq("module", "").one();
        if (ToolUtil.isNotEmpty(activitiProcess)) {
            LoginUser user = LoginContextHolder.getContext().getUser();
            ActivitiProcessTaskParam activitiProcessTaskParam = new ActivitiProcessTaskParam();
            activitiProcessTaskParam.setTaskName(user.getName() + "的盘点任务");
            activitiProcessTaskParam.setQTaskId(param.getInventoryTaskId());
            activitiProcessTaskParam.setUserId(param.getCreateUser());
            activitiProcessTaskParam.setFormId(param.getInventoryTaskId());
            activitiProcessTaskParam.setType(ReceiptsEnum.Stocktaking.name());
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

            List<Long> userIds = new ArrayList<>();
            if (ToolUtil.isNotEmpty(param.getParticipants())) {
                userIds.addAll(JSON.parseArray(param.getParticipants(), Long.class));
            }
            String name = LoginContextHolder.getContext().getUser().getName();
            if (ToolUtil.isNotEmpty(userIds)) {
                /**
                 * 评论
                 */
                RemarksParam remarksParam = new RemarksParam();
                remarksParam.setTaskId(taskId);
                remarksParam.setType("remark");
                StringBuffer userIdStr = new StringBuffer();
                for (Long userId : userIds) {
                    userIdStr.append(userId).append(",");
                }
                String userStrtoString = userIdStr.toString();
                if (userIdStr.length() > 1) {
                    userStrtoString = userStrtoString.substring(0, userStrtoString.length() - 1);
                }
                remarksParam.setUserIds(userStrtoString);
                remarksParam.setContent(param.getRemark());
                messageProducer.remarksServiceDo(new RemarksEntity() {{
                    setOperationType(OperationType.ADD);
                    setRemarksParam(remarksParam);
                }});
            }
        }
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
            Integer count = detailsService.query().eq("inkind_id", param.getInkindId()).eq("display", 1).count();
            if (count > 1) {
                throw new ServiceException(500, "已有相同实物");
            }
        }

        long count = inkindIds.stream().distinct().count();
        if (count != params.size()) {
            throw new ServiceException(500, "请勿重复添加相同物料");
        }

        List<StockDetails> details = inkindIds.size() == 0 ? new ArrayList<>() : detailsService.query().in("inkind_id", inkindIds)
                .eq("display", 1).list();

        List<InventoryDetail> inventories = new ArrayList<>();

        List<Long> sotckIds = new ArrayList<>();
        List<Long> stockInkinds = new ArrayList<>();

        Inventory invent = new Inventory();
        invent.setInventoryTaskName(new Date() + "盘点任务");
        this.save(invent);

        InventoryDetail inventory = null;

        //添加盘点数据----------------------------------------------------------------------------------------------------

        List<OutstockListingParam> outParams = new ArrayList<>();
        List<InstockListParam> inListParams = new ArrayList<>();


        for (StockDetails detail : details) {
            stockInkinds.add(detail.getInkindId());
            for (InventoryRequest.InkindParam param : params) {
                if (detail.getInkindId().equals(param.getInkindId())) {  //相同实物

                    sotckIds.add(detail.getStockId());

                    if (detail.getNumber() > param.getNumber()) {  //修正出库

                        OutstockListingParam outstockListingParam = new OutstockListingParam();  //添加记录
                        outstockListingParam.setNumber(detail.getNumber() - param.getNumber());
                        outstockListingParam.setSkuId(detail.getSkuId());
                        outParams.add(outstockListingParam);

                        inventory = new InventoryDetail();
                        inventory.setInventoryId(invent.getInventoryTaskId());
                        inventory.setInkindId(param.getInkindId());
                        inventory.setStatus(2);
                        inventories.add(inventory);

                    } else if (detail.getNumber() < param.getNumber()) {  //修正入库

                        InstockListParam instockListParam = new InstockListParam();//添加记录
                        instockListParam.setSkuId(detail.getSkuId());
                        instockListParam.setNumber(param.getNumber() - detail.getNumber());
                        inListParams.add(instockListParam);

                        inventory = new InventoryDetail();
                        inventory.setInventoryId(invent.getInventoryTaskId());
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
            List<StockDetails> stockDetails = detailsService.query().in("inkind_id", collect).eq("display", 1).list();
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
            inkindInstock(inkindList, numberMap, codeMap, inventoryRequest.getStoreHouseId(), inventoryRequest.getPositionId(), inListParams);
        }
//--------------------------------------------------------------------------------------------------------------------------新出库
        if (ToolUtil.isNotEmpty(inventoryRequest.getOutStockInkindParams())) {
            List<Long> outStockInkind = new ArrayList<>();
            for (InventoryRequest.outStockInkindParam outStockInkindParam : inventoryRequest.getOutStockInkindParams()) {
                outStockInkind.add(outStockInkindParam.getInkindId());
            }
            inkindOutstock(outStockInkind, outParams);//出库
        }
        detailsService.updateBatchById(details);
        inventoryDetailService.saveBatch(inventories);

        if (inListParams.size() > 0) {  //入库记录添加
            instockOrderService.addInStockRecord(inListParams, "盘点入库记录");
        }
        if (outParams.size() > 0) {    //出库记录添加
            outstockOrderService.addOutStockRecord(outParams, "盘点出库记录");
        }

        if (sotckIds.size() > 0) {
            stockService.updateNumber(sotckIds);
        }


    }

    //盘点入库
    private void inkindInstock(List<Inkind> inkinds, Map<Long, Long> numberMap, Map<Long, Long> codeMap, Long storeHouseId, Long positionId
            , List<InstockListParam> instockListParams
    ) {


        List<InventoryDetail> inventoryDetails = new ArrayList<>();

        for (Inkind inkind : inkinds) {
            //入库记录详情
            InstockListParam instockListParam = new InstockListParam();
            instockListParam.setSkuId(inkind.getSkuId());
            instockListParam.setNumber(numberMap.get(inkind.getInkindId()));
            if (ToolUtil.isNotEmpty(inkind.getBrandId())) {
                instockListParam.setBrandId(inkind.getBrandId());
            }
            instockListParams.add(instockListParam);

            //库存添加
            StockDetails stockDetails = new StockDetails();
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
    private void inkindOutstock(List<Long> inkindIds, List<OutstockListingParam> outstockListingParams) {

        List<Long> stockIds = new ArrayList<>();
        List<InventoryDetail> inventoryDetails = new ArrayList<>();
        List<StockDetails> outDetails = detailsService.query().in("inkind_id", inkindIds).eq("display", 1).list();
        for (StockDetails outDetail : outDetails) {
            stockIds.add(outDetail.getStockId());
        }

        QueryWrapper<StockDetails> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("inkind_id", inkindIds);
        StockDetails stockDetails = new StockDetails();
        stockDetails.setDisplay(0);
        stockDetails.setNumber(0L);
        detailsService.update(stockDetails, queryWrapper);

        List<Inkind> inkinds = inkindService.listByIds(inkindIds);


        for (Inkind inkind : inkinds) {
            OutstockListingParam listingParam = new OutstockListingParam();
            listingParam.setSkuId(inkind.getSkuId());
            listingParam.setBrandId(inkind.getBrandId());
            outstockListingParams.add(listingParam);
            inkind.setNumber(0L);
            InventoryDetail inventoryDetail = new InventoryDetail();
            inventoryDetail.setInkindId(inkind.getInkindId());
            inventoryDetail.setStatus(2);
            inventoryDetails.add(inventoryDetail);
        }


        inkindService.updateBatchById(inkinds);
        inventoryDetailService.saveBatch(inventoryDetails);
        stockService.updateNumber(stockIds);

    }

    @Override
    public InkindResult inkindInventory(Long id) {
        InkindResult inkindResult = inkindService.getInkindResult(id);
        StockDetails stockDetails = detailsService.query().eq("inkind_id", inkindResult.getInkindId()).eq("display", 1).one();
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

        List<StockDetails> stockDetails = detailsService.query().eq("storehouse_positions_id", id).eq("display", 1).list();
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

    private void format(List<InventoryResult> data) {

        List<Long> inventoryIds = new ArrayList<>();

        for (InventoryResult datum : data) {
            inventoryIds.add(datum.getInventoryTaskId());
        }


    }
}
