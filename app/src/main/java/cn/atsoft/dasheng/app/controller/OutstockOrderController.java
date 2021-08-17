package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.OutstockOrder;
import cn.atsoft.dasheng.app.model.params.OutstockOrderParam;
import cn.atsoft.dasheng.app.model.result.OutstockOrderResult;
import cn.atsoft.dasheng.app.service.OutstockOrderService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.portal.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 出库单控制器
 *
 * @author cheng
 * @Date 2021-08-16 10:51:46
 */
@RestController
@RequestMapping("/outstockOrder")
@Api(tags = "出库单")
public class OutstockOrderController extends BaseController {

    @Autowired
    private OutstockOrderService outstockOrderService;

    /**
     * 新增接口
     *
     * @author cheng
     * @Date 2021-08-16
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody OutstockOrderParam outstockOrderParam) {
        Long add = this.outstockOrderService.add(outstockOrderParam);
        return ResponseData.success(add);
    }

    /**
     * 编辑接口
     *
     * @author cheng
     * @Date 2021-08-16
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody OutstockOrderParam outstockOrderParam) {

        this.outstockOrderService.update(outstockOrderParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author cheng
     * @Date 2021-08-16
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody OutstockOrderParam outstockOrderParam)  {
        this.outstockOrderService.delete(outstockOrderParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author cheng
     * @Date 2021-08-16
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<OutstockOrderResult> detail(@RequestBody OutstockOrderParam outstockOrderParam) {
        OutstockOrder detail = this.outstockOrderService.getById(outstockOrderParam.getOutstockOrderId());
        OutstockOrderResult result = new OutstockOrderResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author cheng
     * @Date 2021-08-16
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<OutstockOrderResult> list(@RequestBody(required = false) OutstockOrderParam outstockOrderParam) {
        if(ToolUtil.isEmpty(outstockOrderParam)){
            outstockOrderParam = new OutstockOrderParam();
        }
        return this.outstockOrderService.findPageBySpec(outstockOrderParam);
    }

    /**
     * 编辑接口
     *
     * @author cheng
     * @Date 2021-08-16
     */
    @RequestMapping(value = "/outStock", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData outStock(@RequestBody OutstockOrderParam outstockOrderParam) {

        this.outstockOrderService.outStock(outstockOrderParam);
        return ResponseData.success();
    }


}


