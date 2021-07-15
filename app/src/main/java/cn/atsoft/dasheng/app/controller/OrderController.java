package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.wrapper.ItemsSelectWrapper;
import cn.atsoft.dasheng.app.wrapper.OrderSelectWrapper;
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
 * 发货表控制器
 *
 * @author 
 * @Date 2021-07-15 17:41:40
 */
@RestController
@RequestMapping("/order")
@Api(tags = "发货表")
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-07-15
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
     * @author 
     * @Date 2021-07-15
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
     * @author 
     * @Date 2021-07-15
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
     * @author 
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<OrderResult> detail(@RequestBody OrderParam orderParam) {
        Order detail = this.orderService.getById(orderParam.getOrderId());
        OrderResult result = new OrderResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<OrderResult> list(@RequestBody(required = false) OrderParam orderParam) {
        if(ToolUtil.isEmpty(orderParam)){
            orderParam = new OrderParam();
        }
        return this.orderService.findPageBySpec(orderParam);
    }


    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)

    public ResponseData<List<Map<String, Object>>> listSelect() {
        List<Map<String, Object>> list = this.orderService.listMaps();
//        BrandSelectWrapper factory = new BrandSelectWrapper(list);
        OrderSelectWrapper orderSelectWrapper = new OrderSelectWrapper(list);
//        ItemsSelectWrapper itemsSelectWrapper =new ItemsSelectWrapper(list);
        List<Map<String, Object>> result = orderSelectWrapper.wrap();
        return ResponseData.success(result);
    }

}


