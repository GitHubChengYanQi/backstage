package cn.atsoft.dasheng.purchase.service;

import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.entity.Order;
import cn.atsoft.dasheng.crm.service.OrderService;
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
        for (ThemeAndOrigin themeAndOrigin : param) {
            //TODO 可增加表单类型
            switch (themeAndOrigin.getSource()) {
                case "purchaseAsk":
                    askIds.add(themeAndOrigin.getSourceId());
                    break;
                case "ProcurementPlan":
                    planIds.add(themeAndOrigin.getSourceId());
                    break;
            }
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
            case "order":
                Order order = JSONObject.parseObject(JSONObject.toJSONString(param), Order.class);
                fromId = order.getOrderId();
//                coding = order.getCoding();
                createTime = order.getCreateTime();
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
    public String newThemeAndOrigin(String self,Long selfId,String source,Long sourceId){
        ThemeAndOrigin themeAndOrigin = originFormat(source, sourceId);
        themeAndOrigin.setSource(self);
        themeAndOrigin.setSourceId(selfId);
        return JSON.toJSONString(themeAndOrigin);
    }
    public ThemeAndOrigin originFormat(String source, Long sourceId) {
        ThemeAndOrigin themeAndOrigin = new ThemeAndOrigin();
        ThemeAndOrigin parent = new ThemeAndOrigin();
        switch (source) {
            case "purchaseAsk":
                PurchaseAsk byId = purchaseAskService.getById(sourceId);
                parent =  JSON.parseObject(byId.getOrigin(), ThemeAndOrigin.class);
                break;
            case "order":
                Order order = orderService.getById(sourceId);
                parent =  JSON.parseObject(order.getOrigin(), ThemeAndOrigin.class);
            break;
        }
        List<ThemeAndOrigin> parents = new ArrayList<>();
        parents.add(parent);
        themeAndOrigin.setParent(parents);
        return themeAndOrigin;
    }
}
