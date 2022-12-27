package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.StockLog;
import cn.atsoft.dasheng.erp.model.params.StockLogParam;
import cn.atsoft.dasheng.erp.model.result.StockLogResult;
import cn.atsoft.dasheng.erp.service.StockLogService;
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
 * 库存操作记录主表控制器
 *
 * @author Captain_Jazz
 * @Date 2022-12-22 10:43:55
 */
@RestController
@RequestMapping("/stockLog")
@Api(tags = "库存操作记录主表")
public class StockLogController extends BaseController {

    @Autowired
    private StockLogService stockLogService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2022-12-22
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody StockLogParam stockLogParam) {
        this.stockLogService.add(stockLogParam);
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
    public ResponseData update(@RequestBody StockLogParam stockLogParam) {

        this.stockLogService.update(stockLogParam);
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
    public ResponseData delete(@RequestBody StockLogParam stockLogParam)  {
        this.stockLogService.delete(stockLogParam);
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
    public ResponseData<StockLogResult> detail(@RequestBody StockLogParam stockLogParam) {
        StockLog detail = this.stockLogService.getById(stockLogParam.getStockLogId());
        StockLogResult result = new StockLogResult();
        ToolUtil.copyProperties(detail, result);

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
    public PageInfo<StockLogResult> list(@RequestBody(required = false) StockLogParam stockLogParam) {
        if(ToolUtil.isEmpty(stockLogParam)){
            stockLogParam = new StockLogParam();
        }
        return this.stockLogService.findPageBySpec(stockLogParam);
    }




}


