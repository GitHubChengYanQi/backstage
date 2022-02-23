package cn.atsoft.dasheng.crm.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.OrderDetail;
import cn.atsoft.dasheng.crm.model.params.OrderDetailParam;
import cn.atsoft.dasheng.crm.model.result.OrderDetailResult;
import cn.atsoft.dasheng.crm.service.OrderDetailService;
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
 * 订单明细表控制器
 *
 * @author song
 * @Date 2022-02-23 09:48:33
 */
@RestController
@RequestMapping("/orderDetail")
@Api(tags = "订单明细表")
public class OrderDetailController extends BaseController {

    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2022-02-23
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody OrderDetailParam orderDetailParam) {
        this.orderDetailService.add(orderDetailParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2022-02-23
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody OrderDetailParam orderDetailParam) {

        this.orderDetailService.update(orderDetailParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2022-02-23
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody OrderDetailParam orderDetailParam)  {
        this.orderDetailService.delete(orderDetailParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2022-02-23
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<OrderDetailResult> detail(@RequestBody OrderDetailParam orderDetailParam) {
        OrderDetail detail = this.orderDetailService.getById(orderDetailParam.getDetailId());
        OrderDetailResult result = new OrderDetailResult();
        ToolUtil.copyProperties(detail, result);


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-02-23
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<OrderDetailResult> list(@RequestBody(required = false) OrderDetailParam orderDetailParam) {
        if(ToolUtil.isEmpty(orderDetailParam)){
            orderDetailParam = new OrderDetailParam();
        }
        return this.orderDetailService.findPageBySpec(orderDetailParam);
    }




}


