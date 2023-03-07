package cn.atsoft.dasheng.purchase.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.purchase.entity.RestOrderDetail;
import cn.atsoft.dasheng.purchase.model.params.RestOrderDetailParam;
import cn.atsoft.dasheng.purchase.model.result.RestOrderDetailResult;
import cn.atsoft.dasheng.purchase.service.RestOrderDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * 订单明细表控制器
 *
 * @author song
 * @Date 2022-02-23 09:48:33
 */
@RestController
@RequestMapping("/orderDetail/{version}")
@ApiVersion("2.0")
@Api(tags = "订单明细表")
public class RestOrderDetailController extends BaseController {

    @Autowired
    private RestOrderDetailService orderDetailService;


    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2022-02-23
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody RestOrderDetailParam orderDetailParam) {
        RestOrderDetail detail = this.orderDetailService.getById(orderDetailParam.getDetailId());
        RestOrderDetailResult result = new RestOrderDetailResult();
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
    public PageInfo<RestOrderDetailResult> list(@RequestBody(required = false) RestOrderDetailParam orderDetailParam) {
        if (ToolUtil.isEmpty(orderDetailParam)) {
            orderDetailParam = new RestOrderDetailParam();
        }
        return this.orderDetailService.findPageBySpec(orderDetailParam);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-02-23
     */
    @RequestMapping(value = "/historyList", method = RequestMethod.POST)
    @ApiOperation("历史记录")
    public ResponseData historyList(@RequestBody(required = false) RestOrderDetailParam orderDetailParam) {
        if (ToolUtil.isEmpty(orderDetailParam)) {
            orderDetailParam = new RestOrderDetailParam();
        }

        return ResponseData.success( this.orderDetailService.historyList(orderDetailParam) ) ;
    }


}


