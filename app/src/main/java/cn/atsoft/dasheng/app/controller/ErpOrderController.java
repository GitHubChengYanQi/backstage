package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.ErpOrder;
import cn.atsoft.dasheng.app.model.params.ErpOrderParam;
import cn.atsoft.dasheng.app.model.result.ErpOrderResult;
import cn.atsoft.dasheng.app.service.ErpOrderService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.portal.banner.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 订单表控制器
 *
 * @author ta
 * @Date 2021-07-26 13:48:54
 */
@RestController
@RequestMapping("/erpOrder")
@Api(tags = "订单表")
public class ErpOrderController extends BaseController {

    @Autowired
    private ErpOrderService erpOrderService;

    /**
     * 新增接口
     *
     * @author ta
     * @Date 2021-07-26
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ErpOrderParam erpOrderParam) {
        this.erpOrderService.add(erpOrderParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author ta
     * @Date 2021-07-26
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ErpOrderParam erpOrderParam) {

        this.erpOrderService.update(erpOrderParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author ta
     * @Date 2021-07-26
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ErpOrderParam erpOrderParam)  {
        this.erpOrderService.delete(erpOrderParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author ta
     * @Date 2021-07-26
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<ErpOrderResult> detail(@RequestBody ErpOrderParam erpOrderParam) {
        ErpOrder detail = this.erpOrderService.getById(erpOrderParam.getOrderId());
        ErpOrderResult result = new ErpOrderResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author ta
     * @Date 2021-07-26
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ErpOrderResult> list(@RequestBody(required = false) ErpOrderParam erpOrderParam) {
        if(ToolUtil.isEmpty(erpOrderParam)){
            erpOrderParam = new ErpOrderParam();
        }
        return this.erpOrderService.findPageBySpec(erpOrderParam);
    }




}


