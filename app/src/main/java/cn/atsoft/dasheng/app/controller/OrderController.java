package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Order;
import cn.atsoft.dasheng.app.model.params.OrderParam;
import cn.atsoft.dasheng.app.model.result.OrderResult;
import cn.atsoft.dasheng.app.service.OrderService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 订单表控制器
 *
 * @author ta
 * @Date 2021-07-20 16:22:28
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
     * @author ta
     * @Date 2021-07-20
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody OrderParam orderParam) {
        this.orderService.add(orderParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author ta
     * @Date 2021-07-20
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody OrderParam orderParam) {

        this.orderService.update(orderParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author ta
     * @Date 2021-07-20
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody OrderParam orderParam)  {
        this.orderService.delete(orderParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author ta
     * @Date 2021-07-20
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<OrderResult> detail(@RequestBody OrderParam orderParam) {
        Order detail = this.orderService.getById(orderParam.getId());
        OrderResult result = new OrderResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author ta
     * @Date 2021-07-20
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<OrderResult> list(@RequestBody(required = false) OrderParam orderParam) {
        if(ToolUtil.isEmpty(orderParam)){
            orderParam = new OrderParam();
        }
        return this.orderService.findPageBySpec(orderParam);
    }




}

