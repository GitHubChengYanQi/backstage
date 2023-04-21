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
import cn.atsoft.dasheng.erp.entity.OutstockListing;
import cn.atsoft.dasheng.erp.model.result.OutstockListingResult;
import cn.atsoft.dasheng.erp.service.OutstockListingService;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;
import java.util.ArrayList;
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
    @Autowired
    private OutstockListingService outstockListingService;

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
        this.outstockOrderService.AkeyOutbound(param);
        return ResponseData.success();
    }


    @RequestMapping(value = "/getOrder", method = RequestMethod.POST)
    public ResponseData getOrder(@RequestBody OutstockOrderParam param) {
        OutstockOrderResult order = this.outstockOrderService.getOrder(param.getOutstockOrderId());
        return ResponseData.success(order);
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public ResponseData getOrder(@RequestParam Long outStockOrderId) {
        OutstockOrderResult order = this.outstockOrderService.detail(outStockOrderId);
        return ResponseData.success(order);
    }


    /**
     * @param param
     */
    @RequestMapping(value = "/outBound", method = RequestMethod.POST)
    public ResponseData outBound(@RequestBody @Valid OutstockOrderParam param) {
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
//
//    /**
//     * 查看详情接口
//     *
//     * @author cheng
//     * @Date 2021-08-16
//     */
//    @RequestMapping(value = "/detail", method = RequestMethod.POST)
//    @ApiOperation("详情")
//    public ResponseData detail(@RequestBody OutstockOrderParam outstockOrderParam) {
//        OutstockOrder detail = this.outstockOrderService.getById(outstockOrderParam.getOutstockOrderId());
//        OutstockOrderResult result = new OutstockOrderResult();
//        if (ToolUtil.isNotEmpty(detail)) {
//            ToolUtil.copyProperties(detail, result);
//        }
//
////        result.setValue(parentValue);
//        return ResponseData.success(result);
//    }

    /**
     * 查询列表
     *
     * @author cheng
     * @Date 2021-08-16
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo list(@RequestBody(required = false) OutstockOrderParam outstockOrderParam) {
        if (ToolUtil.isEmpty(outstockOrderParam)) {
            outstockOrderParam = new OutstockOrderParam();
        }
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.outstockOrderService.findPageBySpec(outstockOrderParam, null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope(),LoginContextHolder.getContext().getTenantId());
            return this.outstockOrderService.findPageBySpec(outstockOrderParam, dataScope);
        }
    }
    /**
     * 查询列表
     *
     * @author cheng
     * @Date 2021-08-16
     */
    @RequestMapping(value = "/getOutStockListByPickListsId", method = RequestMethod.GET)
    @ApiOperation("列表")
    public List<OutstockListingResult> list(@RequestParam Long pickListsId) {
        List<OutstockOrder> list = this.outstockOrderService.query().eq("source", "pickLists").eq("source_id", pickListsId).list();
        List<Long> orderIds = new ArrayList<>();
        for (OutstockOrder outstockOrder : list) {
            orderIds.add(outstockOrder.getOutstockOrderId());
        }
        List<OutstockListing> listings = outstockListingService.query().in("outstock_order_id", orderIds).eq("display", 1).list();
        List<OutstockListingResult> outstockListingResults = BeanUtil.copyToList(listings, OutstockListingResult.class);
        outstockListingService.format(outstockListingResults);
        return outstockListingResults;
    }


    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    public ResponseData listSelect() {
        QueryWrapper<OutstockOrder> itemsQueryWrapper = new QueryWrapper<>();
        itemsQueryWrapper.in("display", 1).in("state", 1);
        List<Map<String, Object>> list = this.outstockOrderService.listMaps(itemsQueryWrapper);
        OutstockOrderSelectWrapper itemsSelectWrapper = new OutstockOrderSelectWrapper(list);
        List<Map<String, Object>> result = itemsSelectWrapper.wrap();
        return ResponseData.success(result);
    }


}


