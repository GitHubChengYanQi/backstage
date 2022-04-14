package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Supply;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.mapper.InstockOrderMapper;
import cn.atsoft.dasheng.erp.model.params.InstockOrderParam;
import cn.atsoft.dasheng.erp.model.params.QualityTaskDetailParam;
import cn.atsoft.dasheng.erp.model.params.QualityTaskParam;
import cn.atsoft.dasheng.erp.model.request.InstockParams;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.pojo.FreeInStockParam;
import cn.atsoft.dasheng.erp.pojo.InstockListRequest;
import cn.atsoft.dasheng.erp.pojo.InStockByOrderParam;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.erp.model.result.InstockOrderResult;
import cn.atsoft.dasheng.erp.model.result.InstockRequest;
import cn.atsoft.dasheng.erp.pojo.FreeInStockParam;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.message.enmu.MicroServiceType;
import cn.atsoft.dasheng.message.enmu.OperationType;
import cn.atsoft.dasheng.message.entity.MicroServiceEntity;
import cn.atsoft.dasheng.message.producer.MessageProducer;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.model.result.BackCodeRequest;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.orCode.service.OrCodeService;
import cn.atsoft.dasheng.portal.repair.service.RepairSendTemplate;
import cn.atsoft.dasheng.purchase.pojo.ListingPlan;
import cn.atsoft.dasheng.purchase.service.GetOrigin;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.sendTemplate.WxCpTemplate;
import cn.atsoft.dasheng.sys.modular.rest.model.params.MobileUrl;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateTime;
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

/**
 * <p>
 * 入库单 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-06
 */
@Service
public class InstockOrderServiceImpl extends ServiceImpl<InstockOrderMapper, InstockOrder> implements InstockOrderService {
    @Autowired
    private InstockService instockService;
    @Autowired
    private UserService userService;
    @Autowired
    private StorehouseService storehouseService;
    @Autowired
    private InstockListService instockListService;
    @Autowired
    private InstockSendTemplate instockSendTemplate;
    @Autowired
    private OrCodeService orCodeService;
    @Autowired
    private CodingRulesService codingRulesService;
    @Autowired
    private InkindService inkindService;
    @Autowired
    private StockService stockService;
    @Autowired
    private StockDetailsService stockDetailsService;
    @Autowired
    private OrCodeBindService bindService;
    @Autowired
    private WxCpSendTemplate wxCpSendTemplate;
    @Autowired
    private GetOrigin getOrigin;
    @Autowired
    private StorehousePositionsService positionsService;
    @Autowired
    private StorehousePositionsBindService positionsBindService;
    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    private SkuService skuService;
    @Autowired
    private InstockLogService instockLogService;
    @Autowired
    private InstockLogDetailService instockLogDetailService;

    @Override
    @Transactional
    public void add(InstockOrderParam param) {


        //防止添加重复数据
//        List<Long> judge = new ArrayList<>();
//        for (InstockRequest instockRequest : param.getInstockRequest()) {
//            Long skuId = instockRequest.getSkuId();
//            judge.add(skuId);
//        }
//        long count = judge.stream().distinct().count();
//        if (param.getInstockRequest().size() > count) {
//            throw new ServiceException(500, "请勿重复添加");
//        }
        InstockOrder entity = getEntity(param);

        this.save(entity);
        List<Long> skuIds = new ArrayList<>();
        if (ToolUtil.isNotEmpty(param.getInstockRequest())) {
            for (InstockRequest instockRequest : param.getInstockRequest()) {
                skuIds.add(instockRequest.getSkuId());
            }
            List<Sku> skus = skuIds.size() == 0 ? new ArrayList<>() : skuService.listByIds(skuIds);
            List<InstockList> instockLists = new ArrayList<>();
            for (InstockRequest instockRequest : param.getInstockRequest()) {
                if (ToolUtil.isNotEmpty(instockRequest)) {
                    for (Sku sku : skus) {
                        if (ToolUtil.isEmpty(sku.getQualityPlanId()) && sku.getSkuId().equals(instockRequest.getSkuId())) {
                            InstockList instockList = new InstockList();
                            instockList.setSkuId(instockRequest.getSkuId());
                            instockList.setNumber(instockRequest.getNumber());
                            instockList.setRealNumber(instockRequest.getNumber());
                            instockList.setInstockOrderId(entity.getInstockOrderId());
                            instockList.setInstockNumber(instockRequest.getNumber());
                            instockList.setBrandId(instockRequest.getBrandId());
                            instockList.setLotNumber(instockRequest.getLotNumber());
                            instockList.setSerialNumber(instockRequest.getSerialNumber());
                            instockList.setReceivedDate(instockRequest.getReceivedDate());
                            instockList.setManufactureDate(instockRequest.getManufactureDate());
                            instockList.setEffectiveDate(instockRequest.getEffectiveDate());
                            if (ToolUtil.isNotEmpty(instockRequest.getCostprice())) {
                                instockList.setCostPrice(instockRequest.getCostprice());
                            }
                            instockList.setStoreHouseId(param.getStoreHouseId());
                            if (ToolUtil.isNotEmpty(instockRequest.getSellingPrice())) {
                                instockList.setSellingPrice(instockRequest.getSellingPrice());
                            }
                            instockLists.add(instockList);
                            break;
                        }
                    }

                }
            }
            if (ToolUtil.isNotEmpty(instockLists)) {
                instockListService.saveBatch(instockLists);
            }

            //更新主题与来源
            List<SkuSimpleResult> skuSimpleResults = skuService.simpleFormatSkuResult(skuIds);
            StringBuffer stringBuffer = new StringBuffer();
            if (ToolUtil.isEmpty(entity.getTheme())) {

                if (ToolUtil.isNotEmpty(param.getCustomerName())) {
                    stringBuffer.append(param.getCustomerName());
                }
                for (SkuSimpleResult skuSimpleResult : skuSimpleResults) {
                    stringBuffer.append(skuSimpleResult.getSkuName());
                    if (skuSimpleResults.size() > 1) {
                        stringBuffer.append("等");
                        break;
                    }
                }
                stringBuffer.append("的入库单");
                entity.setTheme(stringBuffer.toString());
            }

            entity.setOrigin(getOrigin.newThemeAndOrigin("instockOrder", entity.getInstockOrderId(), entity.getSource(), entity.getSourceId()));
            this.updateById(entity);


            /**
             * 内部调用创建质检
             */
            this.createQualityTask(param, skus);

//            BackCodeRequest backCodeRequest = new BackCodeRequest();
//            backCodeRequest.setId(entity.getInstockOrderId());
//            backCodeRequest.setSource("instock");
//            Long aLong = orCodeService.backCode(backCodeRequest);
//            //           String url = param.getUrl().replace("codeId", aLong.toString());
//            String prefix = MobileUrl.prefix;
//            String url = prefix + "#/OrCode?id=" + aLong.toString();
//            User createUser = userService.getById(entity.getCreateUser());
//            //新微信推送
//            WxCpTemplate wxCpTemplate = new WxCpTemplate();
//            wxCpTemplate.setUrl(url);
//            wxCpTemplate.setTitle("新的入库提醒");
//            wxCpTemplate.setDescription(createUser.getName() + "您有新的入库任务" + entity.getCoding());
//            wxCpTemplate.setUserIds(new ArrayList<Long>() {{
//                add(entity.getUserId());
//            }});
//            wxCpSendTemplate.setSource("instockOrder");
//            wxCpSendTemplate.setSourceId(aLong);
//            wxCpTemplate.setType(0);
//            wxCpSendTemplate.setWxCpTemplate(wxCpTemplate);
//            wxCpSendTemplate.sendTemplate();
        }

    }

    public void createQualityTask(InstockOrderParam param, List<Sku> skus) {
        QualityTaskParam qualityTaskParam = new QualityTaskParam();
        List<QualityTaskDetailParam> qualityTaskDetailParams = new ArrayList<>();
        for (InstockRequest instockRequest : param.getInstockRequest()) {
            if (ToolUtil.isNotEmpty(instockRequest)) {
                for (Sku sku : skus) {
                    if (ToolUtil.isNotEmpty(sku.getQualityPlanId()) || sku.getSkuId().equals(instockRequest.getSkuId())) {
                        QualityTaskDetailParam qualityTaskDetailParam = new QualityTaskDetailParam();
                        qualityTaskDetailParam.setQualityPlanId(sku.getQualityPlanId());
                        qualityTaskDetailParam.setNumber(Math.toIntExact(instockRequest.getNumber()));
                        qualityTaskDetailParam.setSkuId(instockRequest.getSkuId());
                        qualityTaskDetailParam.setBrandId(instockRequest.getBrandId());
                        qualityTaskDetailParams.add(qualityTaskDetailParam);
                        break;
                    }
                }
            }
        }
        qualityTaskParam.setDetails(qualityTaskDetailParams);
        qualityTaskParam.setMicroUserId(LoginContextHolder.getContext().getUserId());
        MicroServiceEntity serviceEntity = new MicroServiceEntity();
        serviceEntity.setType(MicroServiceType.QUALITY_TASK);
        serviceEntity.setOperationType(OperationType.ADD);
        String jsonString = JSON.toJSONString(qualityTaskParam);
        serviceEntity.setObject(jsonString);
        serviceEntity.setMaxTimes(2);
        serviceEntity.setTimes(0);
        messageProducer.microService(serviceEntity);


    }

    @Override
    public void checkNumberTrue(Long id, Integer status) {
        InstockOrder instockOrder = this.getById(id);
        if (status != 98) {
            throw new ServiceException(500, "您传入的状态不正确");
        } else {
            instockOrder.setState(status);
            this.updateById(instockOrder);
        }
    }

    @Override
    public void checkNumberFalse(Long id, Integer status) {
        InstockOrder instockOrder = this.getById(id);
        if (status != 49) {
            throw new ServiceException(500, "您传入的状态不正确");
        } else {
            instockOrder.setState(status);
            this.updateById(instockOrder);
        }
    }

    @Override

    public void delete(InstockOrderParam param) {
        this.removeById(getKey(param));
    }

    /**
     * 自由入库
     *
     * @param param
     */
    @Override
    @Transactional
    public void update(InstockOrderParam param) {
    }

    @Override
    public InstockOrderResult findBySpec(InstockOrderParam param) {
        return null;
    }

    @Override
    public List<InstockOrderResult> findListBySpec(InstockOrderParam param) {
        return null;
    }

    @Override
    public PageInfo<InstockOrderResult> findPageBySpec(InstockOrderParam param) {
        Page<InstockOrderResult> pageContext = getPageContext();
        IPage<InstockOrderResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    /**
     * 通过质检创建入库单
     *
     * @param instockParams
     */
    @Override
    @Transactional
    public void addByQuality(InstockParams instockParams) {
        //生成入库编码
        String coding = backCoding(instockParams.getCoding(), instockParams.getStoreHouseId());
        //查询实物
        List<Inkind> inkinds = inkindService.query().in("inkind_id", instockParams.getInkinds()).list();
        //创建入库单
        InstockOrder instockOrder = new InstockOrder();
        instockOrder.setCoding(coding);
        instockOrder.setUserId(instockParams.getUserId());
        instockOrder.setStoreHouseId(instockParams.getStoreHouseId());
        this.save(instockOrder);
        //通过实物id查询sku和brand  创建入库清单
        List<InstockList> instockLists = new ArrayList<>();
        for (Inkind inkind : inkinds) {
            inkind.setSource("质检");
            inkind.setType("0");
            //创建入库清单
            InstockList instockList = new InstockList();
            instockList.setSkuId(inkind.getSkuId());
            instockList.setBrandId(inkind.getBrandId());
            instockList.setNumber(inkind.getNumber());
            instockList.setInstockNumber(inkind.getNumber());
            instockList.setInstockOrderId(instockOrder.getInstockOrderId());
            instockList.setStoreHouseId(instockParams.getStoreHouseId());
            instockLists.add(instockList);
        }
        instockListService.saveBatch(instockLists);
        inkindService.updateBatchById(inkinds);
        //推送消息
        BusinessTrack businessTrack = new BusinessTrack();
        businessTrack.setType("代办");
        businessTrack.setMessage("入库");
        businessTrack.setUserId(instockParams.getUserId());
        businessTrack.setNote("有物料需要入库");
        DateTime data = new DateTime();
        businessTrack.setTime(data);
        BackCodeRequest backCodeRequest = new BackCodeRequest();
        backCodeRequest.setId(instockOrder.getInstockOrderId());
        backCodeRequest.setSource("instock");
        Long aLong = orCodeService.backCode(backCodeRequest);
        String url = instockParams.getUrl().replace("codeId", aLong.toString());
        instockSendTemplate.setBusinessTrack(businessTrack);
        instockSendTemplate.setUrl(url);
        instockSendTemplate.sendTemplate();
    }

    /**
     * 通过物料自由入库
     *
     * @param freeInStockParam
     */
    @Override
    @Transactional
    public void freeInstock(FreeInStockParam freeInStockParam) {

        //判断库位绑定
        List<StorehousePositionsBind> positionsBinds = positionsBindService.query().eq("position_id", freeInStockParam.getPositionsId()).list();
        List<Inkind> inkinds = inkindService.listByIds(freeInStockParam.getInkindIds());
        Map<Long, Long> positionsMap = new HashMap<>();
        Map<Long, Long> houseMap = new HashMap<>();
        for (Inkind inkind : inkinds) {
            positionsMap.put(inkind.getInkindId(), freeInStockParam.getPositionsId());
            houseMap.put(inkind.getInkindId(), freeInStockParam.getStoreHouseId());
        }
        instock(inkinds, positionsMap, positionsBinds, houseMap);  //入库
    }

    @Override
    public void formatDetail(InstockOrderResult orderResult) {
        if (ToolUtil.isNotEmpty(orderResult)) {

            List<InstockList> instockLists = instockListService.query().eq("instock_order_id", orderResult.getInstockOrderId()).list();
            List<InstockListResult> listResults = BeanUtil.copyToList(instockLists, InstockListResult.class, new CopyOptions());
            instockListService.format(listResults);
            listResults.removeIf(i -> i.getRealNumber() == 0);
            orderResult.setInstockListResults(listResults);


            List<Long> skuIds = new ArrayList<>();
            for (InstockList instockList : instockLists) {
                skuIds.add(instockList.getSkuId());
            }
            List<StorehousePositionsResult> results = positionsBindService.bindTreeView(skuIds);  //返回树形结构
            orderResult.setBindTreeView(results);
        }


    }


    /**
     * 通过入库单入库
     */
    @Override
    public void inStockByOrder(InStockByOrderParam param) {
        List<InStockByOrderParam.SkuParam> skuParams = param.getSkuParams();


        List<Long> positionIds = new ArrayList<>();
        List<Long> instockListIds = new ArrayList<>();


        for (InStockByOrderParam.SkuParam skuParam : skuParams) {
            instockListIds.add(skuParam.getInStockListId());
            positionIds.add(skuParam.getPositionId());
        }

        List<InstockList> instockLists = instockListIds.size() == 0 ? new ArrayList<>() : instockListService.listByIds(instockListIds);
        Map<Long, Long> houseByPositionId = positionsService.getHouseByPositionId(positionIds);
        Map<Long, InstockList> instockListMap = new HashMap<>();


        InstockOrder instockOrder = this.getById(instockLists.get(0).getInstockOrderId());
        InstockLog instockLog = new InstockLog();
        instockLog.setInstockOrderId(instockOrder.getInstockOrderId());
        instockLogService.save(instockLog);

        for (InstockList instockList : instockLists) {
            instockListMap.put(instockList.getInstockListId(), instockList);
        }


        /**
         * 入库操作
         */
        List<StockDetails> stockDetailsList = new ArrayList<>();

        List<InstockLogDetail> instockLogDetails = new ArrayList<>();
        for (InStockByOrderParam.SkuParam skuParam : skuParams) {
            List<Inkind> inkinds = skuParam.getInkindIds().size() == 0 ? new ArrayList<>() : inkindService.listByIds(skuParam.getInkindIds());

            if (ToolUtil.isNotEmpty(skuParam.getStockNumber()) && skuParam.getStockNumber() > 0) {   //盘点
                List<StockDetails> stockDetails = stockDetailsService.query().eq("sku_id", skuParam.getSkuId()).eq("storehouse_positions_id", skuParam.getPositionId()).list();
                for (int i = 0; i < stockDetails.size(); i++) {
                    if (i == 0) {
                        StockDetails details = stockDetails.get(0);
                        details.setNumber(skuParam.getStockNumber());
                    } else {
                        StockDetails details = stockDetails.get(i);
                        details.setNumber(0L);
                    }
                }
                stockDetailsService.updateBatchById(stockDetails);
            }

            instockLogDetails.clear();
            long number = 0L;
            for (Inkind inKind : inkinds) {
                //修改清单数量
                InstockList instockList = instockListMap.get(skuParam.getInStockListId());
                instockList.setRealNumber(instockList.getRealNumber() - inKind.getNumber());
                instockLists.add(instockList);

                StockDetails stockDetails = new StockDetails();
                stockDetails.setNumber(inKind.getNumber());
                stockDetails.setStorehousePositionsId(skuParam.getPositionId());
                stockDetails.setInkindId(inKind.getInkindId());
                stockDetails.setStorehouseId(houseByPositionId.get(skuParam.getPositionId()));
                stockDetails.setCustomerId(inKind.getCustomerId() == null ? null : inKind.getCustomerId());
                stockDetails.setBrandId(inKind.getBrandId() == null ? null : inKind.getBrandId());
                stockDetails.setSkuId(inKind.getSkuId());
                stockDetailsList.add(stockDetails);
                inKind.setType("1");

                number = number + inKind.getNumber();
            }
            InstockLogDetail instockLogDetail = new InstockLogDetail();
            instockLogDetail.setSkuId(skuParam.getSkuId());
            instockLogDetail.setNumber(number);
            instockLogDetail.setInstockOrderId(instockOrder.getInstockOrderId());
            instockLogDetail.setInstockLogId(instockLog.getInstockLogId());
            instockLogDetails.add(instockLogDetail);

            inkindService.updateBatchById(inkinds);
        }
        instockLogDetailService.saveBatch(instockLogDetails);

        instockListService.updateBatchById(instockLists);
        stockDetailsService.saveBatch(stockDetailsList);

        instockOrderComplete(instockOrder.getInstockOrderId());
    }


    /**
     * 入库单完成状态
     *
     * @param orderId
     */
    private void instockOrderComplete(Long orderId) {
        if (ToolUtil.isEmpty(orderId)) {
            return;
        }

        List<InstockList> instockLists = instockListService.query().eq("instock_order_id", orderId).list();
        boolean t = true;

        for (InstockList instockList : instockLists) {
            if (instockList.getRealNumber() != 0) {
                t = false;
                break;
            }
        }
        if (t) {
            InstockOrder order = this.getById(orderId);
            order.setState(99);
            this.updateById(order);
        }
    }

    /**
     * 通过库位自由入库
     */
    @Override
    @Transactional
    public void freeInStockByPositions(FreeInStockParam stockParam) {

        List<FreeInStockParam.PositionsInStock> inStocks = stockParam.getInStocks();
        List<Long> inkindIds = new ArrayList<>();
        Map<Long, Long> positionsMap = new HashMap<>();
        Map<Long, Long> houseMap = new HashMap<>();

        for (FreeInStockParam.PositionsInStock inStock : inStocks) {  //先取实物
            inkindIds.add(inStock.getInkind());
            positionsMap.put(inStock.getInkind(), inStock.getPositionsId());  //实物对应的库位
            houseMap.put(inStock.getInkind(), inStock.getStoreHouseId());
        }

        List<Long> positionsIds = new ArrayList<Long>() {{
            for (FreeInStockParam.PositionsInStock inStock : inStocks) {
                add(inStock.getPositionsId());
            }
        }};

        List<StorehousePositionsBind> positionsBinds = positionsBindService.query().in("position_id", positionsIds).list();

        List<Inkind> inkinds = inkindService.listByIds(inkindIds);

        instock(inkinds, positionsMap, positionsBinds, houseMap);  //入库
    }

    /**
     * 入库操作逻辑
     *
     * @param inkinds   实物
     * @param positions 库位
     * @param binds     绑定关系
     * @param houseId   仓库
     */
    private void instock(List<Inkind> inkinds, Map<Long, Long> positions, List<StorehousePositionsBind> binds, Map<Long, Long> houseId) {

        List<StockDetails> stockDetailsList = new ArrayList<>();
        List<Long> inkindIds = new ArrayList<>();

        for (Inkind inkind : inkinds) {  //获取二维码
            inkindIds.add(inkind.getInkindId());
        }
        List<OrCodeBind> codeBinds = bindService.query().in("form_id", inkindIds).list();
        Map<Long, Long> qrMap = new HashMap<>();

        for (OrCodeBind bind : codeBinds) {
            for (Inkind inkind : inkinds) {
                if (bind.getFormId().equals(inkind.getInkindId())) {
                    qrMap.put(inkind.getInkindId(), bind.getOrCodeId());
                    break;
                }
            }
        }

        for (Inkind inkind : inkinds) {
//            if (judgePosition(binds, inkind)) {
//                throw new ServiceException(500, "入库的物料 未和库位绑定");
//            }
            StockDetails stockDetails = new StockDetails();
            stockDetails.setNumber(inkind.getNumber());
            stockDetails.setStorehousePositionsId(positions.get(inkind.getInkindId()));
            stockDetails.setQrCodeId(qrMap.get(inkind.getInkindId()));
            stockDetails.setInkindId(inkind.getInkindId());
            stockDetails.setStorehouseId(houseId.get(inkind.getInkindId()));
            stockDetails.setCustomerId(inkind.getCustomerId());
            stockDetails.setBrandId(inkind.getBrandId());
            stockDetails.setSkuId(inkind.getSkuId());
            stockDetailsList.add(stockDetails);
            inkind.setType("1");

        }
        stockDetailsService.saveBatch(stockDetailsList);
        inkindService.updateBatchById(inkinds);

    }

    /**
     * 判断实物有无库存
     *
     * @param inkind
     * @param stocks
     * @return
     */
    @Override
    public Stock judgeStockExist(Inkind inkind, List<Stock> stocks) {

        for (Stock stock : stocks) {
            if (stock.getSkuId().equals(inkind.getSkuId()) && stock.getBrandId().equals(inkind.getBrandId())) {
                return stock;
            }
        }
        return null;
    }

    /**
     * 判断物料 供应商 品牌 是否绑定
     *
     * @param inkind
     * @param supplies
     * @return
     */
    @Override
    public boolean judgeSkuBind(Inkind inkind, List<Supply> supplies) {
        try {
            for (Supply supply : supplies) {
                if (inkind.getSkuId().equals(supply.getSkuId()) && inkind.getBrandId().equals(supply.getBrandId()) && supply.getCustomerId().equals(inkind.getCustomerId())) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        return true;
    }

    /**
     * 当前物料和库位是否绑定
     *
     * @param positionsBinds
     * @param inkind
     * @return
     */
    @Override
    public Boolean judgePosition(List<StorehousePositionsBind> positionsBinds, Inkind inkind) {

        for (StorehousePositionsBind positionsBind : positionsBinds) {
            if (positionsBind.getSkuId().equals(inkind.getSkuId())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void updateStatus(ActivitiProcessTask processTask) {
        InstockOrder order = this.getById(processTask.getFormId());
        if (order.getState() != 50) {
            order.setState(98);
            this.updateById(order);
        }
    }

    @Override
    public void updateRefuseStatus(ActivitiProcessTask processTask) {
        InstockOrder order = this.getById(processTask.getFormId());
        order.setState(50);
        this.updateById(order);
    }


    private Serializable getKey(InstockOrderParam param) {
        return param.getInstockOrderId();
    }

    private Page<InstockOrderResult> getPageContext() {
        List<String> fields = new ArrayList<>();
        fields.add("storeHouseId");
        fields.add("createTime");
        fields.add("userId");
        return PageFactory.defaultPage(fields);
    }

    /**
     * 入库生成编码
     *
     * @param coding
     * @return
     */
    String backCoding(Long coding, Long storeHouseId) {
        String cod = null;
        //添加编码
        CodingRules codingRules = codingRulesService.query().eq("coding_rules_id", coding).one();
        if (ToolUtil.isNotEmpty(codingRules)) {
            String backCoding = codingRulesService.backCoding(codingRules.getCodingRulesId());
            Storehouse storehouse = storehouseService.query().eq("storehouse_id", storeHouseId).one();
            if (ToolUtil.isNotEmpty(storehouse)) {
                String replace = "";
                if (ToolUtil.isNotEmpty(storehouse.getCoding())) {
                    replace = backCoding.replace("${storehouse}", storehouse.getCoding());
                } else {
                    replace = backCoding.replace("${storehouse}", "");
                }
                cod = replace;
            }
        }
        return cod;
    }

    private InstockOrder getOldEntity(InstockOrderParam param) {
        return this.getById(getKey(param));
    }

    private InstockOrder getEntity(InstockOrderParam param) {
        InstockOrder entity = new InstockOrder();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    private void format(List<InstockOrderResult> data) {
        List<Long> userIds = new ArrayList<>();
        List<Long> storeIds = new ArrayList<>();

        for (InstockOrderResult datum : data) {
            userIds.add(datum.getUserId());
            storeIds.add(datum.getStoreHouseId());

        }
        List<User> users = userIds.size() == 0 ? new ArrayList<>() : userService.lambdaQuery().in(User::getUserId, userIds).list();
        List<Storehouse> storehouses = storeIds.size() == 0 ? new ArrayList<>() : storehouseService.lambdaQuery().in(Storehouse::getStorehouseId, storeIds).list();


        for (InstockOrderResult datum : data) {
            for (User user : users) {
                if (ToolUtil.isNotEmpty(datum.getUserId())) {
                    if (datum.getUserId().equals(user.getUserId())) {
                        UserResult userResult = new UserResult();
                        ToolUtil.copyProperties(user, userResult);
                        datum.setUserResult(userResult);
                        break;
                    }
                }

            }
            for (Storehouse storehouse : storehouses) {
                if (storehouse.getStorehouseId().equals(datum.getStoreHouseId())) {
                    StorehouseResult storehouseResult = new StorehouseResult();
                    ToolUtil.copyProperties(storehouse, storehouseResult);
                    datum.setStorehouseResult(storehouseResult);
                    break;
                }
            }
        }
    }
}
