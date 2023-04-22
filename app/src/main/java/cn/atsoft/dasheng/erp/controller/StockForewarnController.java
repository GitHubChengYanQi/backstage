package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.StockForewarn;
import cn.atsoft.dasheng.erp.model.params.StockForewarnParam;
import cn.atsoft.dasheng.erp.model.result.StockForewarnResult;
import cn.atsoft.dasheng.erp.service.StockForewarnService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
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
 * 库存预警设置控制器
 *
 * @author sjl
 * @Date 2022-12-05 09:50:26
 */
@RestController
@RequestMapping("/stockForewarn")
@Api(tags = "库存预警设置")
public class StockForewarnController extends BaseController {


    @Autowired
    private StockForewarnService stockForewarnService;

    /**
     * 新增接口
     *
     * @author sjl
     * @Date 2022-12-05
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody StockForewarnParam stockForewarnParam) {
        this.stockForewarnService.add(stockForewarnParam);
        return ResponseData.success();
    }
    /**
     * 新增接口
     *
     * @author sjl
     * @Date 2022-12-05
     */
    @RequestMapping(value = "/view", method = RequestMethod.POST)
    @ApiOperation("统计")
    public ResponseData view() {
        return ResponseData.success(this.stockForewarnService.view(LoginContextHolder.getContext().getTenantId()));
    }
    /**
     * 新增接口
     *
     * @author sjl
     * @Date 2022-12-05
     */
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData saveOrUpdate(@RequestBody StockForewarnParam stockForewarnParam) {
        this.stockForewarnService.saveOrUpdateByat(stockForewarnParam.getParams());
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author sjl
     * @Date 2022-12-05
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody StockForewarnParam stockForewarnParam) {

        this.stockForewarnService.update(stockForewarnParam);
        return ResponseData.success();
    }

    /**
     * 批量保存接口
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ApiOperation("批量保存")
    public ResponseData saveBatch(@RequestBody StockForewarnParam stockForewarnParam) {
        if (ToolUtil.isNotEmpty(stockForewarnParam.getInventoryFloor()) && ToolUtil.isNotEmpty(stockForewarnParam.getInventoryCeiling()) && stockForewarnParam.getInventoryFloor() >= stockForewarnParam.getInventoryCeiling()){
            throw new ServiceException(500, "预警上限值应大于下限值");
        }
        if (ToolUtil.isEmpty(stockForewarnParam.getBomId())) {
            throw new ServiceException(500, "请传入bomId");
        } else {
            this.stockForewarnService.saveBatchByBomId(stockForewarnParam.getBomId(), stockForewarnParam);
        }
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author sjl
     * @Date 2022-12-05
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody StockForewarnParam stockForewarnParam) {
        this.stockForewarnService.delete(stockForewarnParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author sjl
     * @Date 2022-12-05
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<StockForewarnResult> detail(@RequestBody StockForewarnParam stockForewarnParam) {
        StockForewarn detail = this.stockForewarnService.getById(stockForewarnParam.getForewarnId());
        StockForewarnResult result = new StockForewarnResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author sjl
     * @Date 2022-12-05
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<StockForewarnResult> list(@RequestBody(required = false) StockForewarnParam stockForewarnParam) {
        if (ToolUtil.isEmpty(stockForewarnParam)) {
            stockForewarnParam = new StockForewarnParam();
        }

        return this.stockForewarnService.findPageBySpec(stockForewarnParam);
    }

    /**
     * 查询列表
     *
     * @author sjl
     * @Date 2022-12-05
     */
    @RequestMapping(value = "/warningSku", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo warningSku(@RequestBody(required = false) StockForewarnParam stockForewarnParam) {
        if (ToolUtil.isEmpty(stockForewarnParam)) {
            stockForewarnParam = new StockForewarnParam();
        }

        return this.stockForewarnService.showWaring(stockForewarnParam);
    }


}


