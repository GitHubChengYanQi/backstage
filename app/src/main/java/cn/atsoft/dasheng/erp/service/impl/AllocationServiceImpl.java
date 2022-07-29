package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.action.Enum.AllocationActionEnum;
import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.mapper.AllocationMapper;
import cn.atsoft.dasheng.erp.model.params.AllocationDetailParam;
import cn.atsoft.dasheng.erp.model.params.AllocationParam;
import cn.atsoft.dasheng.erp.model.params.InstockListParam;
import cn.atsoft.dasheng.erp.model.params.InstockOrderParam;
import cn.atsoft.dasheng.erp.model.result.AllocationCartResult;
import cn.atsoft.dasheng.erp.model.result.AllocationDetailResult;
import cn.atsoft.dasheng.erp.model.result.AllocationResult;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.entity.DocumentsAction;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.model.params.RemarksParam;
import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.message.enmu.OperationType;
import cn.atsoft.dasheng.message.entity.RemarksEntity;
import cn.atsoft.dasheng.message.producer.MessageProducer;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.production.entity.ProductionPickListsCart;
import cn.atsoft.dasheng.production.entity.ProductionPickListsDetail;
import cn.atsoft.dasheng.production.model.params.ProductionPickListsDetailParam;
import cn.atsoft.dasheng.production.model.params.ProductionPickListsParam;
import cn.atsoft.dasheng.production.service.ProductionPickListsService;
import cn.atsoft.dasheng.purchase.service.GetOrigin;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 调拨主表 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-07-13
 */
@Service
public class AllocationServiceImpl extends ServiceImpl<AllocationMapper, Allocation> implements AllocationService {

    @Autowired
    private AllocationDetailService allocationDetailService;
    @Autowired
    private AllocationCartService allocationCartService;

    @Autowired
    private ProductionPickListsService productionPickListsService;

    @Autowired
    private InstockOrderService instockOrderService;

    @Autowired
    private GetOrigin getOrigin;

    @Autowired
    private StockDetailsService stockDetailsService;

    @Autowired
    private AllocationLogService allocationLogService;

    @Autowired
    private StorehousePositionsBindService storehousePositionsBindService;
    @Autowired
    private CodingRulesService codingRulesService;

    @Autowired
    private ActivitiProcessService activitiProcessService;

    @Autowired
    private ActivitiProcessTaskService activitiProcessTaskService;

    @Autowired
    private ActivitiProcessLogService activitiProcessLogService;
    @Autowired
    private DocumentsActionService documentsActionService;


    @Autowired
    private ActivitiAuditService auditService;
    @Autowired
    private MessageProducer messageProducer;
    @Autowired
    private InkindService inkindService;


    @Override
    public Allocation add(AllocationParam param) {
        if (ToolUtil.isEmpty(param.getCoding())) {
            CodingRules codingRules = codingRulesService.query().eq("module", "17").eq("state", 1).one();
            if (ToolUtil.isNotEmpty(codingRules)) {
                String coding = codingRulesService.backCoding(codingRules.getCodingRulesId());
                param.setCoding(coding);
            } else {
                throw new ServiceException(500, "请配置养护单据自动生成编码规则");
            }
        }


        Allocation entity = getEntity(param);
        this.save(entity);
        param.setAllocationId(entity.getAllocationId());
        allocationDetailService.add(param);
//        if (ToolUtil.isNotEmpty(param.getDetailParams())) {
//            List<AllocationDetail> allocationDetails = BeanUtil.copyToList(param.getDetailParams(), AllocationDetail.class);
//            for (AllocationDetail allocationDetail : allocationDetails) {
//                allocationDetail.setAllocationId(entity.getAllocationId());
//            }
//            allocationDetailService.saveBatch(allocationDetails);
//        }
        ActivitiProcess activitiProcess = activitiProcessService.query().eq("type", "ALLOCATION").eq("status", 99).eq("module", "allocation").one();
        if (ToolUtil.isNotEmpty(activitiProcess)) {
            activitiProcessTaskService.checkStartUser(activitiProcess.getProcessId());
            auditService.power(activitiProcess);//检查创建权限
            ActivitiProcessTaskParam activitiProcessTaskParam = new ActivitiProcessTaskParam();
            String name = LoginContextHolder.getContext().getUser().getName();
            activitiProcessTaskParam.setTaskName(name + "调拨申请 ");
            activitiProcessTaskParam.setUserId(param.getUserId());
            activitiProcessTaskParam.setFormId(entity.getAllocationId());
            activitiProcessTaskParam.setType("ALLOCATION");
            activitiProcessTaskParam.setProcessId(activitiProcess.getProcessId());
            ActivitiProcessTask activitiProcessTask = new ActivitiProcessTask();
            ToolUtil.copyProperties(activitiProcessTaskParam, activitiProcessTask);
            Long taskId = activitiProcessTaskService.add(activitiProcessTaskParam);

            //添加log
            activitiProcessLogService.addLog(activitiProcess.getProcessId(), taskId);
            activitiProcessLogService.autoAudit(taskId, 1, LoginContextHolder.getContext().getUserId());
            if (ToolUtil.isNotEmpty(param.getUserIds())) {
                /**
                 * 评论
                 */
                RemarksParam remarksParam = new RemarksParam();
                remarksParam.setTaskId(taskId);
                remarksParam.setType("remark");
                StringBuffer userIdStr = new StringBuffer();
                for (Long userId : param.getUserIds()) {
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
        } else {
            throw new ServiceException(500, "请创建质检流程！");
        }
        return entity;
    }

    @Override
    public void checkCart(Long allocation) {
        DocumentsAction action = documentsActionService.query().eq("action", AllocationActionEnum.assign.name()).eq("display", 1).one();
        activitiProcessLogService.checkAction(allocation, "ALLOCATION", action.getDocumentsActionId(), LoginContextHolder.getContext().getUserId());
    }

    /**
     * 创建出库单
     *
     * @param allocationId
     */
    @Override
    public void createPickListsAndInStockOrder(Long allocationId) {
        Allocation allocation = this.getById(allocationId);
        List<AllocationCart> allocationCarts = allocationCartService.query().eq("display", 1).eq("allocation_id", allocationId).eq("type", "carry").list();
        List<AllocationDetail> allocationDetails = allocationDetailService.query().eq("display", 1).eq("allocation_id", allocationId).eq("type", "carry").list();
        List<Long> storehouseIds = new ArrayList<>();
        for (AllocationCart allocationCart : allocationCarts) {
            storehouseIds.add(allocationCart.getStorehouseId());
        }

        if (allocation.getType().equals("allocation")) {
            if (allocation.getAllocationType() == 1) {
                List<Long> stockIds = new ArrayList<>();
                for (AllocationCart allocationCart : allocationCarts) {
                    stockIds.add(allocationCart.getStorehouseId());
                }
                //入库单
                InstockOrderParam instockOrderParam = new InstockOrderParam();
                instockOrderParam.setSource("ALLOCATION");
                instockOrderParam.setSourceId(allocationId);
                List<InstockListParam> listParams = new ArrayList<>();

                for (Long stockId : stockIds) {
                    ProductionPickListsParam listsParam = new ProductionPickListsParam();
                    listsParam.setPickListsName(allocation.getAllocationName());
                    listsParam.setUserId(allocation.getUserId());
                    listsParam.setSource("ALLOCATION");
                    listsParam.setSourceId(allocationId);
                    List<ProductionPickListsDetailParam> details = new ArrayList<>();
                    for (AllocationCart allocationCart : allocationCarts) {
                        if (allocationCart.getStorehouseId().equals(stockId) && ToolUtil.isEmpty(allocationCart.getStorehousePositionsId())) {
                            ProductionPickListsDetailParam listsDetailParam = new ProductionPickListsDetailParam();
                            ToolUtil.copyProperties(allocationCart, listsDetailParam);
                            details.add(listsDetailParam);
                            InstockListParam instockListParam = new InstockListParam();
                            ToolUtil.copyProperties(listsDetailParam, instockListParam);
                            instockListParam.setStoreHouseId(allocation.getStorehouseId());
                            listParams.add(instockListParam);
                            allocationCart.setStatus(98);
                        }
                    }
                    if (details.size() > 0) {
                        productionPickListsService.add(listsParam);
                    }
                }
                instockOrderParam.setListParams(listParams);
                if (listParams.size() > 0) {
                    instockOrderService.add(instockOrderParam);
                }
            } else if (allocation.getAllocationType() == 2) {
                List<Long> stockIds = new ArrayList<>();
                for (AllocationCart allocationCart : allocationCarts) {
                    stockIds.add(allocationCart.getStorehouseId());
                }
                ProductionPickListsParam listsParam = new ProductionPickListsParam();
                listsParam.setPickListsName(allocation.getAllocationName());
                listsParam.setUserId(allocation.getUserId());
                listsParam.setSource("ALLOCATION");
                listsParam.setSourceId(allocationId);
                List<ProductionPickListsDetailParam> details = new ArrayList<>();
                //入库单

                for (Long stockId : stockIds) {
                    InstockOrderParam instockOrderParam = new InstockOrderParam();
                    instockOrderParam.setSource("ALLOCATION");
                    instockOrderParam.setSourceId(allocationId);
                    List<InstockListParam> listParams = new ArrayList<>();
                    for (AllocationCart allocationCart : allocationCarts) {
                        if (allocationCart.getStorehouseId().equals(stockId) && ToolUtil.isEmpty(allocationCart.getStorehousePositionsId())) {
                            ProductionPickListsDetailParam listsDetailParam = new ProductionPickListsDetailParam();
                            listsDetailParam.setStorehouseId(allocation.getStorehouseId());
                            ToolUtil.copyProperties(allocationCart, listsDetailParam);
                            details.add(listsDetailParam);
                            InstockListParam instockListParam = new InstockListParam();
                            ToolUtil.copyProperties(listsDetailParam, instockListParam);
                            listParams.add(instockListParam);
                            allocationCart.setStatus(98);
                        }
                    }
                    instockOrderParam.setListParams(listParams);
                    if (listParams.size() > 0) {
                        instockOrderService.add(instockOrderParam);
                    }

                }
                if (details.size() > 0) {
                    productionPickListsService.add(listsParam);
                }
            }

        }

        /**
         * 如果是移库操作  审批通过删除库位绑定
         */
        if (allocation.getType().equals("transfer")) {
            for (AllocationCart allocationCart : allocationCarts) {
                List<StorehousePositionsBind> list = storehousePositionsBindService.query().eq("sku_id", allocationCart.getSkuId()).eq("storehouse_positions_id", allocationCart.getStorehousePositionsId()).list();
                if (ToolUtil.isNotEmpty(list)) {
                    for (StorehousePositionsBind storehousePositionsBind : list) {
                        storehousePositionsBind.setDisplay(0);
                    }
                    storehousePositionsBindService.updateBatchById(list);
                }
            }
        }


    }

    void format() {

    }

    @Override
    public void delete(AllocationParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public AllocationResult detail(Long allocationId) {
        Allocation allocation = this.getById(allocationId);
        AllocationResult result = BeanUtil.copyProperties(allocation, AllocationResult.class);
        List<AllocationDetailResult> allocationDetailResults = allocationDetailService.resultsByAllocationId(allocationId);
        result.setDetailResults(allocationDetailResults);
        List<AllocationCartResult> allocationCartResults = allocationCartService.resultsByAllocationId(allocationId);
        result.setAllocationCartResults(allocationCartResults);
        return result;

    }

    /**
     * 库间调拨接口
     * 更改实物库位
     * 保存调拨记录
     *
     * @param param
     */
    @Override
    public void transferInStorehouse(AllocationDetailParam param) {

        Long skuId = param.getSkuId();
        Long brandId = param.getBrandId();
        Integer number = param.getNumber();
        Long storehousePositionsId = param.getStorehousePositionsId();
        List<StockDetails> stockDetails = stockDetailsService.query().eq("sku_id", skuId).eq("brand_id", brandId).eq("sorehouse_positions_id", storehousePositionsId).eq("display", 1).list();
        List<AllocationLog> allocationLogs = new ArrayList<>();

        if (number > 0) {
            int kickNum = number;
            for (StockDetails stockDetail : stockDetails) {
                number = Math.toIntExact(number - stockDetail.getNumber());
                if (number >= 0) {

                    number = Math.toIntExact(number - stockDetail.getNumber());
                    AllocationLog allocationLog = new AllocationLog();
                    allocationLog.setInKindId(stockDetail.getInkindId());
                    allocationLog.setStorehousePositionsId(stockDetail.getStorehousePositionsId());
                    allocationLog.setToStorehousePositionsId(param.getToStorehousePositionsId());
                    allocationLog.setSkuId(stockDetail.getSkuId());
                    allocationLog.setBrandId(stockDetail.getBrandId());
                    allocationLog.setNumber(Math.toIntExact(stockDetail.getNumber()));
                    allocationLogs.add(allocationLog);
                    stockDetail.setStorehousePositionsId(param.getToStorehousePositionsId());
                    stockDetailsService.updateById(stockDetail);
                } else {
                    AllocationLog allocationLog = new AllocationLog();
                    Inkind inkind = new Inkind();
                    stockDetail.setNumber(stockDetail.getNumber() - kickNum);
                    ToolUtil.copyProperties(stockDetail, inkind);
                    inkind.setInkindId(null);
                    inkind.setSource("inkind");
                    inkind.setSourceId(stockDetail.getInkindId());
                    inkindService.save(inkind);
                    StockDetails stockDetailEntity = new StockDetails();
                    stockDetailEntity.setInkindId(inkind.getInkindId());
                    stockDetailEntity.setStorehouseId(param.getToStorehouseId());
                    stockDetailEntity.setStorehousePositionsId(param.getToStorehousePositionsId());
                    stockDetailEntity.setNumber((long) kickNum);
                    stockDetailEntity.setBrandId(stockDetail.getBrandId());
                    stockDetailsService.save(stockDetailEntity);
                    stockDetailsService.updateById(stockDetail);
                }
            }
        }


        stockDetailsService.updateBatchById(stockDetails);
        allocationLogService.saveBatch(allocationLogs);

    }

    @Override
    public void update(AllocationParam param) {
        Allocation oldEntity = getOldEntity(param);
        Allocation newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public AllocationResult findBySpec(AllocationParam param) {
        return null;
    }

    @Override
    public List<AllocationResult> findListBySpec(AllocationParam param) {
        return null;
    }

    @Override
    public PageInfo<AllocationResult> findPageBySpec(AllocationParam param) {
        Page<AllocationResult> pageContext = getPageContext();
        IPage<AllocationResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(AllocationParam param) {
        return param.getAllocationId();
    }

    private Page<AllocationResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Allocation getOldEntity(AllocationParam param) {
        return this.getById(getKey(param));
    }

    private Allocation getEntity(AllocationParam param) {
        Allocation entity = new Allocation();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
