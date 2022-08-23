package cn.atsoft.dasheng.crm.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Order;
import cn.atsoft.dasheng.crm.model.params.OrderParam;
import cn.atsoft.dasheng.crm.model.result.OrderResult;
import cn.atsoft.dasheng.crm.service.OrderService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 订单表控制器
 *
 * @author song
 * @Date 2022-02-23 09:48:33
 */
@RestController
@RequestMapping("/order")
@Api(tags = "订单表")
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2022-02-23
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody @Valid OrderParam orderParam) {
        if (ToolUtil.isEmpty(orderParam.getCoding())) {
            throw new ServiceException(500, "请填写编号");
        }
        Order order = this.orderService.add(orderParam);
        return ResponseData.success(order);
    }


    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2022-02-23
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody OrderParam orderParam) {
        OrderResult detail = this.orderService.getDetail(orderParam.getOrderId());
        return ResponseData.success(detail);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-02-23
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<OrderResult> list(@RequestBody(required = false) OrderParam orderParam) {
        if (ToolUtil.isEmpty(orderParam)) {
            orderParam = new OrderParam();
        }
        return this.orderService.findPageBySpec(orderParam);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-02-23
     */
    @RequestMapping(value = "/pendingProductionPlan", method = RequestMethod.POST)
    @ApiOperation("列表")
    public ResponseData pendingProductionPlan(@RequestBody(required = false) OrderParam orderParam) {
        if (ToolUtil.isEmpty(orderParam)) {
            orderParam = new OrderParam();
        }
        return ResponseData.success(this.orderService.pendingProductionPlan(orderParam));
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-02-23
     */
    @RequestMapping(value = "/pendingProductionPlanByContracts", method = RequestMethod.POST)
    @ApiOperation("列表")
    public ResponseData pendingProductionPlanByContracts(@RequestBody(required = false) OrderParam orderParam) {
        if (ToolUtil.isEmpty(orderParam)) {
            orderParam = new OrderParam();
        }
        return ResponseData.success(this.orderService.pendingProductionPlanByContracts(orderParam));
    }


}


