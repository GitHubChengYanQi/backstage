package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.wrapper.PlaceSelectWrapper;
import cn.atsoft.dasheng.app.wrapper.StockDetailsSelectWrapper;
import cn.atsoft.dasheng.app.wrapper.StockSelectWrapper;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.model.params.StockDetailsParam;
import cn.atsoft.dasheng.app.model.result.StockDetailsResult;
import cn.atsoft.dasheng.app.service.StockDetailsService;
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
 * 仓库物品明细表控制器
 *
 * @author
 * @Date 2021-07-15 11:13:02
 */
@RestController
@RequestMapping("/stockDetails")
@Api(tags = "仓库物品明细表")
public class StockDetailsController extends BaseController {

    @Autowired
    private StockDetailsService stockDetailsService;

    /**
     * 新增接口
     *
     * @author
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody StockDetailsParam stockDetailsParam) {
        this.stockDetailsService.add(stockDetailsParam);
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
    public ResponseData update(@RequestBody StockDetailsParam stockDetailsParam) {

        this.stockDetailsService.update(stockDetailsParam);
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
    public ResponseData delete(@RequestBody StockDetailsParam stockDetailsParam) {
        this.stockDetailsService.delete(stockDetailsParam);
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
    public ResponseData<StockDetailsResult> detail(@RequestBody StockDetailsParam stockDetailsParam) {
        StockDetails detail = this.stockDetailsService.getById(stockDetailsParam.getStockItemId());
        StockDetailsResult result = new StockDetailsResult();
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
    public PageInfo<StockDetailsResult> list(@RequestBody(required = false) StockDetailsParam stockDetailsParam) {
        if (ToolUtil.isEmpty(stockDetailsParam)) {
            stockDetailsParam = new StockDetailsParam();
        }
        return this.stockDetailsService.findPageBySpec(stockDetailsParam);
    }




}


