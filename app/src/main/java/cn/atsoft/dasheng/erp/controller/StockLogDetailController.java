package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.StockLogDetail;
import cn.atsoft.dasheng.erp.model.params.StockLogDetailParam;
import cn.atsoft.dasheng.erp.model.result.StockLogDetailResult;
import cn.atsoft.dasheng.erp.service.StockLogDetailService;
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
 * 库存操作记录子表控制器
 *
 * @author Captain_Jazz
 * @Date 2022-12-22 10:43:55
 */
@RestController
@RequestMapping("/stockLogDetail")
@Api(tags = "库存操作记录子表")
public class StockLogDetailController extends BaseController {

    @Autowired
    private StockLogDetailService stockLogDetailService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody StockLogDetailParam stockLogDetailParam) {
        this.stockLogDetailService.add(stockLogDetailParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody StockLogDetailParam stockLogDetailParam) {

        this.stockLogDetailService.update(stockLogDetailParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody StockLogDetailParam stockLogDetailParam)  {
        this.stockLogDetailService.delete(stockLogDetailParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<StockLogDetailResult> detail(@RequestBody StockLogDetailParam stockLogDetailParam) {
        StockLogDetail detail = this.stockLogDetailService.getById(stockLogDetailParam.getStockLogDetailId());
        StockLogDetailResult result = new StockLogDetailResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<StockLogDetailResult> list(@RequestBody(required = false) StockLogDetailParam stockLogDetailParam) {
        if(ToolUtil.isEmpty(stockLogDetailParam)){
            stockLogDetailParam = new StockLogDetailParam();
        }
        return this.stockLogDetailService.findPageBySpec(stockLogDetailParam);
    }




}


