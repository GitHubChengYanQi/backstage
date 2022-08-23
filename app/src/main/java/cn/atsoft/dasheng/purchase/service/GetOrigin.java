package cn.atsoft.dasheng.purchase.service;

import cn.atsoft.dasheng.app.entity.OutstockOrder;
import cn.atsoft.dasheng.app.service.OutstockOrderService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.entity.Order;
import cn.atsoft.dasheng.crm.service.OrderService;
import cn.atsoft.dasheng.erp.entity.InstockOrder;
import cn.atsoft.dasheng.erp.service.InstockOrderService;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.production.entity.ProductionPickLists;
import cn.atsoft.dasheng.production.entity.ProductionPlan;
import cn.atsoft.dasheng.production.entity.ProductionWorkOrder;
import cn.atsoft.dasheng.production.model.result.ProductionPlanResult;
import cn.atsoft.dasheng.production.service.ProductionPickListsService;
import cn.atsoft.dasheng.production.service.ProductionPlanService;
import cn.atsoft.dasheng.production.service.ProductionWorkOrderService;
import cn.atsoft.dasheng.purchase.entity.ProcurementOrder;
import cn.atsoft.dasheng.purchase.entity.ProcurementPlan;
import cn.atsoft.dasheng.purchase.entity.PurchaseAsk;
import cn.atsoft.dasheng.purchase.model.params.SourceEnum;
import cn.atsoft.dasheng.purchase.model.result.ProcurementPlanResult;
import cn.atsoft.dasheng.purchase.model.result.PurchaseAskResult;
import cn.atsoft.dasheng.purchase.pojo.ThemeAndOrigin;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.transform.Source;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GetOrigin {

    @Autowired
    private PurchaseAskService purchaseAskService;
    @Autowired
    private ProcurementPlanService procurementPlanService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private InstockOrderService instockOrderService;
    @Autowired
    private OutstockOrderService outstockOrderService;
    @Autowired
    private ProductionPlanService productionPlanService;

    @Autowired
    private ProcurementOrderService procurementOrderService;

    @Autowired
    private ProductionWorkOrderService workOrderService;

    @Autowired
    private ProductionPickListsService pickListsService;


    @Autowired
    private ActivitiProcessTaskService taskService;

    public ThemeAndOrigin getOrigin(ThemeAndOrigin themeAndOrigin) {
//        ThemeAndOrigin themeAndOrigin = JSON.parseObject(Origin, ThemeAndOrigin.class); //将字段中的JSON解析出对象
        List<ThemeAndOrigin> querryList = new ArrayList<>();   //创建一个空对象用于将树形结构处理成一个平面结构  用于调用查询与格式化树形结构数据
        //递归将数据处理
        List<ThemeAndOrigin> parent = getQuerryList(querryList, new ArrayList<ThemeAndOrigin>() {{
            add(themeAndOrigin);
        }});

        List<ThemeAndOrigin> querry = this.querry(querryList);
        this.format(querry, parent);
        return parent.get(0);

    }

    /**
     * 递归
     * 将属性结构 处理出一个 平面结构
     *
     * @param querryList 处理后的平面结构
     * @param parent     单据来源字段解析出的  树形结构
     * @return
     */
    public List<ThemeAndOrigin> getQuerryList(List<ThemeAndOrigin> querryList, List<ThemeAndOrigin> parent) {
        if (ToolUtil.isNotEmpty(parent)) {
            for (ThemeAndOrigin themeAndOrigin : parent) {
                if (ToolUtil.isNotEmpty(themeAndOrigin.getSourceId()) && ToolUtil.isNotEmpty(themeAndOrigin.getSource())) {
                    ThemeAndOrigin querry = new ThemeAndOrigin();
                    querry.setSource(themeAndOrigin.getSource());
                    querry.setSourceId(themeAndOrigin.getSourceId());
                    querryList.add(querry);
                }
                this.getQuerryList(querryList, themeAndOrigin.getParent());
            }
        } else {
            return parent;
        }

        return parent;

    }

    /**
     * 批量查询来源以及相关信息
     *
     * @param param 入参把属性结构 展开成同级List结构方便查询
     * @return
     */
    private List<ThemeAndOrigin> querry(List<ThemeAndOrigin> param) {
        List<Long> askIds = new ArrayList<>();
        List<Long> planIds = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        List<Long> productionPlanIds = new ArrayList<>();
        for (ThemeAndOrigin themeAndOrigin : param) {
            //TODO 可增加表单类型
            switch (themeAndOrigin.getSource()) {
                case "purchaseAsk":
                    askIds.add(themeAndOrigin.getSourceId());
                    break;
                case "ProcurementPlan":
                    planIds.add(themeAndOrigin.getSourceId());
                    break;
                case "productionPlan":
                    productionPlanIds.add(themeAndOrigin.getSourceId());
                    break;
            }
        }
        List<ProductionPlanResult> productionPlanResults = productionPlanService.resultsByIds(productionPlanIds);
        for (ProductionPlanResult productionPlanResult : productionPlanResults) {
            userIds.add(productionPlanResult.getCreateUser());
        }

        List<PurchaseAskResult> purchaseAsks = purchaseAskService.listResultByIds(askIds);
        for (PurchaseAskResult purchaseAsk : purchaseAsks) {
            userIds.add(purchaseAsk.getCreateUser());
        }

        List<ProcurementPlanResult> procurementPlans = procurementPlanService.listResultByIds(planIds);
        for (ProcurementPlanResult procurementPlan : procurementPlans) {
            userIds.add(procurementPlan.getCreateUser());
        }


        List<UserResult> userResults = userService.getUserResultsByIds(userIds.stream().distinct().collect(Collectors.toList()));
        for (ThemeAndOrigin themeAndOrigin : param) {
            for (PurchaseAskResult purchaseAsk : purchaseAsks) {
                if (themeAndOrigin.getSource().equals("purchaseAsk") && themeAndOrigin.getSourceId().equals(purchaseAsk.getPurchaseAskId())) {
                    //返回创建人Result对象
                    for (UserResult userResult : userResults) {
                        if (purchaseAsk.getCreateUser().equals(userResult.getUserId())) {
                            this.copy2Ret(themeAndOrigin, purchaseAsk, themeAndOrigin.getSource(), userResult);
                            break;
                        }
                    }
                }
            }
            for (ProductionPlanResult productionPlanResult : productionPlanResults) {
                if (themeAndOrigin.getSource().equals("productionPlan") && themeAndOrigin.getSourceId().equals(productionPlanResult.getProductionPlanId())) {
                    for (UserResult userResult : userResults) {
                        if (productionPlanResult.getCreateUser().equals(userResult.getUserId())) {
                            this.copy2Ret(themeAndOrigin, productionPlanResult, themeAndOrigin.getSource(), userResult);
                            break;
                        }
                    }
                }
            }


        }
        return param;
    }

    /**
     * 递归
     * 格式化来源
     * 通过查询结果 格式化树形来源
     *
     * @param querryList 来源 查询结果集合
     * @param parent     来源的树形结构  为方便起见未在代码中重构结构   采用来源字段转换出的对象(原来JSON中排列好的结构) 根据查询结果的集合 querryList来匹配数据
     */
    public void format(List<ThemeAndOrigin> querryList, List<ThemeAndOrigin> parent) {
        for (ThemeAndOrigin themeAndOrigin : parent) {
            for (ThemeAndOrigin andOrigin : querryList) {
                if (ToolUtil.isNotEmpty(andOrigin) && ToolUtil.isNotEmpty(themeAndOrigin.getSource()) && ToolUtil.isNotEmpty(themeAndOrigin.getSourceId()) && themeAndOrigin.getSource().equals(andOrigin.getSource()) && themeAndOrigin.getSourceId().equals(andOrigin.getSourceId())) {
                    themeAndOrigin.setRet(andOrigin.getRet());//TODO 非空判断
                }
                if (ToolUtil.isNotEmpty(themeAndOrigin.getParent())) {
                    this.format(querryList, themeAndOrigin.getParent());
                }
            }
        }
    }

    /**
     * 格式化填入Ret内容
     *
     * @param result
     * @param param
     * @param source
     * @param createUser
     */
    public void copy2Ret(ThemeAndOrigin result, Object param, String source, UserResult createUser) {
        Long fromId = null;
        String coding = null;
        String title = null;
        Date createTime = null;
        switch (source) {
            //TODO 增加switch类型
            case "purchaseAsk":
                PurchaseAsk ask = JSONObject.parseObject(JSONObject.toJSONString(param), PurchaseAsk.class);
                fromId = ask.getPurchaseAskId();
                coding = ask.getCoding();
                createTime = ask.getCreateTime();
                break;
            case "procurementOrder":
                ProcurementOrder procurementOrder = JSONObject.parseObject(JSONObject.toJSONString(param), ProcurementOrder.class);
                fromId = procurementOrder.getProcurementOrderId();
                coding = procurementOrder.getCoding();
                createTime = procurementOrder.getCreateTime();
                break;

            case "workOrder":
                ProductionWorkOrder productionWorkOrder = JSONObject.parseObject(JSONObject.toJSONString(param), ProductionWorkOrder.class);
                fromId = productionWorkOrder.getWorkOrderId();
//                coding = productionWorkOrder.getCoding();
                createTime = productionWorkOrder.getCreateTime();
                break;
            case "instockOrder":
                InstockOrder instockOrder = JSONObject.parseObject(JSONObject.toJSONString(param), InstockOrder.class);
                fromId = instockOrder.getInstockOrderId();
                coding = instockOrder.getCoding();
                createTime = instockOrder.getCreateTime();
                break;
            case "OUTSTOCK":
            case "pickLists":
            case "outstockOrder":
                ProductionPickLists pickLists = JSONObject.parseObject(JSONObject.toJSONString(param), ProductionPickLists.class);
                fromId = pickLists.getPickListsId();
                coding = pickLists.getCoding();
                createTime = pickLists.getCreateTime();
                break;
            case "order":
                Order order = JSONObject.parseObject(JSONObject.toJSONString(param), Order.class);
                fromId = order.getOrderId();
//                coding = order.getCoding();
                createTime = order.getCreateTime();
                break;
            case "processTask":
                JSONObject.toJSONString(param);
                ActivitiProcessTask activitiProcessTask = JSON.parseObject(JSONObject.toJSONString(param), ActivitiProcessTask.class);
                fromId = activitiProcessTask.getProcessTaskId();
//                coding = activitiProcessTas
                createTime = activitiProcessTask.getCreateTime();
                break;
        }
        Long finalFromId = fromId;
        String finalCoding = coding;
        Date finalCreateTime = createTime;
        result.setRet(new ThemeAndOrigin.Ret() {{
            setCreateUser(createUser);
            setCreateTime(finalCreateTime);
            setFrom(source);
            setFromId(finalFromId);
            setCoding(finalCoding);
            setTitle(title);
        }});

    }

    public String newThemeAndOrigin(String self, Long selfId, String source, Long sourceId) {
        ThemeAndOrigin themeAndOrigin = originFormat(source, sourceId);
        themeAndOrigin.setSource(self);
        themeAndOrigin.setSourceId(selfId);
        return JSON.toJSONString(themeAndOrigin);
    }

    public ThemeAndOrigin originFormat(String source, Long sourceId) {
        if (ToolUtil.isEmpty(source) || ToolUtil.isEmpty(sourceId)) {
            return new ThemeAndOrigin();
        }
        ThemeAndOrigin themeAndOrigin = new ThemeAndOrigin();
        ThemeAndOrigin parent = new ThemeAndOrigin();
        String json = null;
        switch (source) {
            case "purchaseAsk":
                PurchaseAsk byId = purchaseAskService.getById(sourceId);
                json = byId.getOrigin();
                break;
            case "order":
                Order order = orderService.getById(sourceId);
                json = order.getOrigin();
                break;
            case "instockOrder":
                InstockOrder instockOrder = instockOrderService.getById(sourceId);
                json = instockOrder.getOrigin();
                break;
            case "outstockOrder":
                OutstockOrder outstockOrder = outstockOrderService.getById(sourceId);
                json = outstockOrder.getOrigin();
                break;
            case "procurementPlan":
                ProcurementPlan procurementPlan = procurementPlanService.getById(sourceId);
                json = procurementPlan.getOrigin();
                break;
            case "procurementOrder":
                ProcurementOrder procurementOrder = procurementOrderService.getById(sourceId);
                json = procurementOrder.getOrigin();
                break;
            case "workOrder":
                ProductionWorkOrder workOrder = workOrderService.getById(sourceId);
                json = workOrder.getOrigin();
                break;
            case "productionPlan":
                ProductionPlan productionPlan = productionPlanService.getById(sourceId);
                json = productionPlan.getOrigin();
                break;
            case "processTask":
                ActivitiProcessTask task = taskService.getById(sourceId);
                json = task.getOrigin();
                break;
            case "pickLists":
                ProductionPickLists pickLists = pickListsService.getById(sourceId);
                json = pickLists.getOrigin();
                break;
            default:
        }
        if (ToolUtil.isNotEmpty(json) && !json.equals("")) {
            parent = JSON.parseObject(json, ThemeAndOrigin.class);
        }
        List<ThemeAndOrigin> parents = new ArrayList<>();
        parents.add(parent);
        themeAndOrigin.setParent(parents);
        return themeAndOrigin;
    }
}
