package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.entity.Items;
import cn.atsoft.dasheng.app.pojo.FreeOutStockParam;
import cn.atsoft.dasheng.app.wrapper.ItemsSelectWrapper;
import cn.atsoft.dasheng.app.wrapper.OutstockOrderSelectWrapper;
import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.OutstockOrder;
import cn.atsoft.dasheng.app.model.params.OutstockOrderParam;
import cn.atsoft.dasheng.app.model.result.OutstockOrderResult;
import cn.atsoft.dasheng.app.service.OutstockOrderService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;


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
        OutstockOrder add = this.outstockOrderService.add(outstockOrderParam);
        return ResponseData.success(add);
    }

    /**
     * 出库单一键出库
     *
     * @param param
     */
    @RequestMapping(value = "/AkeyOutbound", method = RequestMethod.POST)
    public ResponseData AkeyOutbound(@RequestBody OutstockOrderParam param) {
        this.outstockOrderService.outBound(param.getListingParams());
        return ResponseData.success();
    }

    /**
     * 自由出库
     *
     * @author cheng
     * @Date 2021-08-16
     */
    @RequestMapping(value = "/freeOutStock", method = RequestMethod.POST)
    @ApiOperation("自由出库")
    @Permission
    public ResponseData freeOutStock(@Valid @RequestBody FreeOutStockParam freeOutStockParam) {
        this.outstockOrderService.freeOutStock(freeOutStockParam);
        return ResponseData.success();
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
        OutstockOrder update = this.outstockOrderService.update(outstockOrderParam);
        return ResponseData.success(update);
    }

    /**
     * 删除接口
     *
     * @author cheng
     * @Date 2021-08-16
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody OutstockOrderParam outstockOrderParam) {
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
        if (ToolUtil.isEmpty(outstockOrderParam)) {
            outstockOrderParam = new OutstockOrderParam();
        }
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.outstockOrderService.findPageBySpec(outstockOrderParam, null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope());
            return this.outstockOrderService.findPageBySpec(outstockOrderParam, dataScope);
        }
    }


    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    public ResponseData<List<Map<String, Object>>> listSelect() {
        QueryWrapper<OutstockOrder> itemsQueryWrapper = new QueryWrapper<>();
        itemsQueryWrapper.in("display", 1).in("state", 1);
        List<Map<String, Object>> list = this.outstockOrderService.listMaps(itemsQueryWrapper);
        OutstockOrderSelectWrapper itemsSelectWrapper = new OutstockOrderSelectWrapper(list);
        List<Map<String, Object>> result = itemsSelectWrapper.wrap();
        return ResponseData.success(result);
    }


}


