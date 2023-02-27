package cn.atsoft.dasheng.outStock.controller;

import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.core.util.ToolUtil;
//import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
//import cn.atsoft.dasheng.erp.service.InventoryService;
//import cn.atsoft.dasheng.erp.service.StorehousePositionsBindService;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.pojo.ProcessType;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.model.response.ResponseData;
//import cn.atsoft.dasheng.production.entity.RestOutStockOrder;
//import cn.atsoft.dasheng.production.entity.RestOutStockOrderCart;
//import cn.atsoft.dasheng.production.entity.RestOutStockOrderDetail;
//import cn.atsoft.dasheng.production.entity.ProductionTask;
//import cn.atsoft.dasheng.production.model.params.RestOutStockOrderCartParam;
//import cn.atsoft.dasheng.production.model.params.RestOutStockOrderParam;
//import cn.atsoft.dasheng.production.model.result.RestOutStockOrderCartResult;
//import cn.atsoft.dasheng.production.model.result.RestOutStockOrderDetailResult;
//import cn.atsoft.dasheng.production.model.result.RestOutStockOrderResult;
//import cn.atsoft.dasheng.production.model.result.ProductionTaskResult;
//import cn.atsoft.dasheng.production.service.RestOutStockOrderCartService;
//import cn.atsoft.dasheng.production.service.RestOutStockOrderDetailService;
//import cn.atsoft.dasheng.production.service.RestOutStockOrderService;
//import cn.atsoft.dasheng.production.service.ProductionTaskService;
//import cn.atsoft.dasheng.sendTemplate.RedisSendCheck;
//import cn.atsoft.dasheng.sendTemplate.pojo.RedisTemplatePrefixEnum;
import cn.atsoft.dasheng.outStock.entity.RestOutStockOrder;
import cn.atsoft.dasheng.outStock.model.params.RestOutStockOrderParam;
import cn.atsoft.dasheng.outStock.service.RestOutStockCartService;
import cn.atsoft.dasheng.outStock.service.RestOutStockOrderDetailService;
import cn.atsoft.dasheng.outStock.service.RestOutStockOrderService;
import cn.atsoft.dasheng.sendTemplate.RestRedisSendCheck;
import cn.atsoft.dasheng.sendTemplate.pojo.RestRedisTemplatePrefixEnum;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ZipUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


/**
 * 领料单控制器
 *
 * @author Captain_Jazz
 * @Date 2022-03-25 16:18:02
 */
@RestController
@RequestMapping("/productionPickLists/{version}")
@Api(tags = "领料单")
@ApiVersion("2.0")
public class RestOutStockOrderController extends BaseController {

    @Autowired
    private RestOutStockOrderService productionPickListsService;
    @Autowired
    private RestOutStockOrderDetailService pickListsDetailService;
    @Autowired
    private RestOutStockCartService productionPickListsCartService;
    @Autowired
    private RestRedisSendCheck redisSendCheck;

//    @Autowired
//    private StorehousePositionsBindService storehousePositionsBindService;

//    @Autowired
//    private ProductionTaskService productionTaskService;
//    @Autowired
//    private RedisSendCheck redisSendCheck;
//
//    @Autowired
//    private InventoryService inventoryService;

    @Autowired
    private ActivitiProcessTaskService processTaskService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    @RequestMapping(value = "/createOutStockOrder", method = RequestMethod.POST)
    @ApiOperation("出库")
    public ResponseData outStock(@RequestBody RestOutStockOrderParam productionPickListsParam) {
//        inventoryService.staticState();
        if (ToolUtil.isEmpty(productionPickListsParam)) {
            productionPickListsParam = new RestOutStockOrderParam();
        }
        List<Object> list = redisSendCheck.getList(RestRedisTemplatePrefixEnum.LLM.getValue()+productionPickListsParam.getCode());
        List<Long> cartIds = new ArrayList<>();
        for (Object obj : list) {
            cartIds.add((Long) obj);
        }

        productionPickListsParam.setCartIds(cartIds);
        this.productionPickListsService.outStock(productionPickListsParam);
        redisSendCheck.deleteListOrObject(RestRedisTemplatePrefixEnum.LLM.getValue() + productionPickListsParam.getCode());
        redisSendCheck.deleteListOrObject(RestRedisTemplatePrefixEnum.LLJCM.getValue() + productionPickListsParam.getCode());
//        this.productionPickListsService.warnincg(productionPickListsParam);
        return ResponseData.success();
    }
    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody RestOutStockOrderParam productionPickListsParam) {
//        this.productionPickListsService.warning(productionPickListsParam);
        RestOutStockOrder pickLists = this.productionPickListsService.add(productionPickListsParam);
        return ResponseData.success(pickLists);
    }

//    /**
//     * 生成code
//     *
//     * @author Captain_Jazz
//     * @Date 2022-03-25
//     */
//    @RequestMapping(value = "/createCode", method = RequestMethod.POST)
//    @ApiOperation("新增")
//    public ResponseData createCode(@RequestBody RestOutStockOrderParam productionPickListsParam) {
//        String code = this.productionPickListsService.createCode(productionPickListsParam);
//        return ResponseData.success(code);
//    }
//
//
//    @RequestMapping(value = "/abortCode", method = RequestMethod.GET)
//    @ApiOperation("部分领料取消")
//    public ResponseData abortCode(@RequestParam String code) {
//        this.productionPickListsService.abortCode(code);
//        return ResponseData.success();
//    }
//
//    /**
//     * 编辑接口
//     *
//     * @author Captain_Jazz
//     * @Date 2022-03-25
//     */
//    @RequestMapping(value = "/edit", method = RequestMethod.POST)
//    @ApiOperation("编辑")
//    public ResponseData update(@RequestBody RestOutStockOrderParam productionPickListsParam) {
//
//        this.productionPickListsService.update(productionPickListsParam);
//        return ResponseData.success();
//    }
//
//    /**
//     * 删除接口
//     *
//     * @author Captain_Jazz
//     * @Date 2022-03-25
//     */
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    @ApiOperation("删除")
//    public ResponseData delete(@RequestBody RestOutStockOrderParam productionPickListsParam) {
//        this.productionPickListsService.delete(productionPickListsParam);
//        return ResponseData.success();
//    }
//
//    /**
//     * 查看详情接口
//     *
//     * @author Captain_Jazz
//     * @Date 2022-03-25
//     */
//    @RequestMapping(value = "/detail", method = RequestMethod.POST)
//    @ApiOperation("详情")
//    public ResponseData detail(@RequestBody RestOutStockOrderParam productionPickListsParam) {
//        RestOutStockOrder detail = new RestOutStockOrder();
//        RestOutStockOrderResult result = new RestOutStockOrderResult();
//        if (ToolUtil.isNotEmpty(productionPickListsParam.getPickListsId())) {
//            detail = this.productionPickListsService.getById(productionPickListsParam.getPickListsId());
//
//            ToolUtil.copyProperties(detail, result);
//        }
//        productionPickListsService.format(new ArrayList<RestOutStockOrderResult>() {{
//            add(result);
//        }});
//        result.setPositionIds(pickListsDetailService.getPisitionIds(result.getPickListsId()));
//
////        List<Long> skuIds = pickListsDetailService.getSkuIdsByPickLists(result.getPickListsId());
////        List<StorehousePositionsResult> storehousePositionsResults = skuIds.size() == 0 ? new ArrayList<>() : storehousePositionsBindService.treeView(skuIds);
////        result.setStorehousePositionsResults(storehousePositionsResults);
//        return ResponseData.success(result);
//    }
//
//    @RequestMapping(value = "/mergeDetail", method = RequestMethod.POST)
//    @ApiOperation("详情")
//    public ResponseData merge(@RequestBody RestOutStockOrderParam productionPickListsParam) {
//
//        List<RestOutStockOrder> productionPickLists = productionPickListsService.listByIds(productionPickListsParam.getPickListsIds());
//        List<Long> sourceIds = new ArrayList<>();
//        for (RestOutStockOrder productionPickList : productionPickLists) {
//            sourceIds.add(productionPickList.getSourceId());
//        }
//        /**
//         * 返回任务信息
//         */
//        List<ProductionTask> productionTasks = sourceIds.size() == 0 ? new ArrayList<>() : productionTaskService.listByIds(sourceIds);
//        List<ProductionTaskResult> productionTaskResults = new ArrayList<>();
//        for (ProductionTask productionTask : productionTasks) {
//            ProductionTaskResult productionTaskResult = new ProductionTaskResult();
//            ToolUtil.copyProperties(productionTask, productionTaskResult);
//            productionTaskResults.add(productionTaskResult);
//        }
//        /**
//         * 查询备料信息
//         */
//        List<RestOutStockOrderCart> productionPickListsCarts = productionPickListsCartService.query().in("pick_lists_id", productionPickListsParam.getPickListsIds()).list();
//        List<RestOutStockOrderCartResult> pickListsCartResults = new ArrayList<>();
//        for (RestOutStockOrderCart productionPickListsCart : productionPickListsCarts) {
//            RestOutStockOrderCartResult productionPickListsCartResult = new RestOutStockOrderCartResult();
//            ToolUtil.copyProperties(productionPickListsCart, productionPickListsCartResult);
//            pickListsCartResults.add(productionPickListsCartResult);
//        }
//        productionPickListsCartService.format(pickListsCartResults);
//
//        List<RestOutStockOrderResult> results = new ArrayList<>();
//        for (RestOutStockOrder productionPickList : productionPickLists) {
//            RestOutStockOrderResult result = new RestOutStockOrderResult();
//            ToolUtil.copyProperties(productionPickList, result);
//            results.add(result);
//        }
//        productionPickListsService.format(results);
//        RestOutStockOrderResult result = new RestOutStockOrderResult();
//        List<RestOutStockOrderDetailResult> detailResults = new ArrayList<>();
//        for (RestOutStockOrderResult productionPickListsResult : results) {
//            if (ToolUtil.isNotEmpty(productionPickListsResult.getDetailResults())) {
//                detailResults.addAll(productionPickListsResult.getDetailResults());
//            }
//        }
//        result.setDetailResults(detailResults);
//
//        List<Long> skuIds = new ArrayList<>();
//        QueryWrapper<RestOutStockOrderDetail> detailQueryWrapper = new QueryWrapper<>();
//        detailQueryWrapper.in("pick_lists_id", productionPickListsParam.getPickListsIds());
//        detailQueryWrapper.eq("status", 0);
//        List<RestOutStockOrderDetail> detailList = pickListsDetailService.list(detailQueryWrapper);
//        for (RestOutStockOrderDetail productionPickListsDetail : detailList) {
//            skuIds.add(productionPickListsDetail.getSkuId());
//        }
//
//        List<StorehousePositionsResult> storehousePositionsResults = skuIds.size() == 0 ? new ArrayList<>() : storehousePositionsBindService.treeView(skuIds);
//        result.setStorehousePositionsResults(storehousePositionsResults);
//        result.setProductionTaskResults(productionTaskResults);
//        result.setCartResults(pickListsCartResults);
////         JSON.toJSONString(result);
//        String toJSONString = JSON.toJSONString(result, JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask());
//
//
//        byte[] gzip = ZipUtil.gzip(toJSONString, CharsetUtil.UTF_8);
//
//        String base64 = Base64.encode(gzip);
//
//        return ResponseData.success(base64);
//
//
//    }
//
//    /**
//     * 查询列表
//     *
//     * @author Captain_Jazz
//     * @Date 2022-03-25
//     */
//    @RequestMapping(value = "/list", method = RequestMethod.POST)
//    @ApiOperation("列表")
//    public PageInfo list(@RequestBody(required = false) RestOutStockOrderParam productionPickListsParam) {
//        if (ToolUtil.isEmpty(productionPickListsParam)) {
//            productionPickListsParam = new RestOutStockOrderParam();
//        }
//        return this.productionPickListsService.findPageBySpec(productionPickListsParam);
//    }
//
//    /**
//     * 查询列表
//     *
//     * @author Captain_Jazz
//     * @Date 2022-03-25
//     */
//    @RequestMapping(value = "/sendPersonPick", method = RequestMethod.POST)
//    @ApiOperation("通知领料")
//    public ResponseData sendPersonPick(@RequestBody(required = false) RestOutStockOrderParam productionPickListsParam) {
//        this.productionPickListsService.sendPersonPick(productionPickListsParam);
//        return ResponseData.success();
//    }
//
//    /**
//     * 领料中心
//     *
//     * @author Captain_Jazz
//     * @Date 2022-03-25
//     */
//    @RequestMapping(value = "/selfPickTasks", method = RequestMethod.POST)
//    @ApiOperation("列表")
//    public PageInfo selfPickTasks(@RequestBody(required = false) ActivitiProcessTaskParam activitiProcessTaskParam) {
//
//
//        if (ToolUtil.isEmpty(activitiProcessTaskParam)) {
//            activitiProcessTaskParam = new ActivitiProcessTaskParam();
//        }
//        activitiProcessTaskParam.setUserId(LoginContextHolder.getContext().getUserId());
//        activitiProcessTaskParam.setType(ProcessType.OUTSTOCK.name());
//        return processTaskService.selfPickTasks(activitiProcessTaskParam);
//
//    }
//
//    @RequestMapping(value = "/createOutStockOrder", method = RequestMethod.POST)
//    @ApiOperation("列表")
//    @Permission
//    public ResponseData createOutStockOrder(@RequestBody(required = false) RestOutStockOrderParam productionPickListsParam) {
//        inventoryService.staticState();
//        if (ToolUtil.isEmpty(productionPickListsParam)) {
//            productionPickListsParam = new RestOutStockOrderParam();
//        }
//        List<Object> list = redisSendCheck.getList(RedisTemplatePrefixEnum.LLM.getValue()+productionPickListsParam.getCode());
//        List<RestOutStockOrderCartParam> productionPickListsCartParams = BeanUtil.copyToList(list, RestOutStockOrderCartParam.class);
//        productionPickListsParam.setCartsParams(productionPickListsCartParams);
//        this.productionPickListsService.outStock(productionPickListsParam);
//        redisSendCheck.deleteListOrObject(RedisTemplatePrefixEnum.LLM.getValue() + productionPickListsParam.getCode());
//        redisSendCheck.deleteListOrObject(RedisTemplatePrefixEnum.LLJCM.getValue() + productionPickListsParam.getCode());
//        return ResponseData.success();
//    }
//    @RequestMapping(value = "/checkCode", method = RequestMethod.GET)
//    @ApiOperation("列表")
//    public ResponseData checkCode(@RequestParam String code) {
//        Object object = redisSendCheck.getObject(RedisTemplatePrefixEnum.LLJCM.getValue() + code);
//        return ResponseData.success(ToolUtil.isNotEmpty(object) && object.equals((LoginContextHolder.getContext().getUserId())));
//    }
//
//
//    @RequestMapping(value = "/createOutStockOrderBySku", method = RequestMethod.POST)
//    @ApiOperation("列表")
//    public ResponseData outStockBySku(@RequestBody(required = false) RestOutStockOrderParam productionPickListsParam) {
//        if (ToolUtil.isEmpty(productionPickListsParam)) {
//            productionPickListsParam = new RestOutStockOrderParam();
//        }
//
//        this.productionPickListsService.outStockBySku(productionPickListsParam);
//        return ResponseData.success();
//    }
//
//
//
//    @RequestMapping(value = "/listByUser", method = RequestMethod.POST)
//    @ApiOperation("详情")
//    public ResponseData listByUser(@RequestBody RestOutStockOrderParam productionPickListsParam) {
//        List<Map<String, Object>> maps = productionPickListsService.listByUser(productionPickListsParam);
//        return ResponseData.success(maps);
//    }
//
//    @RequestMapping(value = "/listByCode", method = RequestMethod.GET)
//    @ApiOperation("详情")
//    @Permission
//    public ResponseData listByUser(@RequestParam String code) {
//
//        return ResponseData.success(productionPickListsService.listByCode(code));
//    }


}


