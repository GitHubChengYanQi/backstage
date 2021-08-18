package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.OrderDetails;
import cn.atsoft.dasheng.app.model.params.OrderDetailsParam;
import cn.atsoft.dasheng.app.model.result.OrderDetailsResult;
import cn.atsoft.dasheng.app.service.OrderDetailsService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.portal.banner.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 订单明细表控制器
 *
 * @author siqiang
 * @Date 2021-08-18 13:26:29
 */
@RestController
@RequestMapping("/orderDetails")
@Api(tags = "订单明细表")
public class OrderDetailsController extends BaseController {

    @Autowired
    private OrderDetailsService orderDetailsService;

    /**
     * 新增接口
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody OrderDetailsParam orderDetailsParam) {
        this.orderDetailsService.add(orderDetailsParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody OrderDetailsParam orderDetailsParam) {

        this.orderDetailsService.update(orderDetailsParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody OrderDetailsParam orderDetailsParam)  {
        this.orderDetailsService.delete(orderDetailsParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<OrderDetailsResult> detail(@RequestBody OrderDetailsParam orderDetailsParam) {
        OrderDetails detail = this.orderDetailsService.getById(orderDetailsParam.getId());
        OrderDetailsResult result = new OrderDetailsResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author siqiang
     * @Date 2021-08-18
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<OrderDetailsResult> list(@RequestBody(required = false) OrderDetailsParam orderDetailsParam) {
        if(ToolUtil.isEmpty(orderDetailsParam)){
            orderDetailsParam = new OrderDetailsParam();
        }
        return this.orderDetailsService.findPageBySpec(orderDetailsParam);
    }




}


