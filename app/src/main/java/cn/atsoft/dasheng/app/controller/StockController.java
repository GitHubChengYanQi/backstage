package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.model.result.StockRequest;
import cn.atsoft.dasheng.app.wrapper.StockSelectWrapper;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Stock;
import cn.atsoft.dasheng.app.model.params.StockParam;
import cn.atsoft.dasheng.app.model.result.StockResult;
import cn.atsoft.dasheng.app.service.StockService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.Map;


/**
 * 仓库总表控制器
 *
 * @author
 * @Date 2021-07-15 11:13:02
 */
@RestController
@RequestMapping("/stock")
@Api(tags = "仓库总表")
public class StockController extends BaseController {

    @Autowired
    private StockService stockService;

    /**
     * 新增接口
     *
     * @author
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody StockParam stockParam) {
        Long add = this.stockService.add(stockParam);
        return ResponseData.success(add);

    }

    /**
     * 编辑接口
     *
     * @author
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody StockParam stockParam) {

        this.stockService.update(stockParam);
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
    public ResponseData delete(@RequestBody StockParam stockParam) {
        this.stockService.delete(stockParam);
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
    public ResponseData<StockResult> detail(@RequestBody StockParam stockParam) {
        Stock detail = this.stockService.getById(stockParam.getStockId());
        StockResult result = new StockResult();
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
    public PageInfo<StockResult> list(@RequestBody(required = false) StockParam stockParam) {
        if (ToolUtil.isEmpty(stockParam)) {
            stockParam = new StockParam();
        }
//        return this.stockService.findPageBySpec(stockParam);
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.stockService.findPageBySpec(stockParam, null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope());
            return this.stockService.findPageBySpec(stockParam, dataScope);
        }
    }
    @RequestMapping(value = "/listAll", method = RequestMethod.POST)
    @ApiOperation("所有列表")
    public List<StockResult> listAll(@RequestBody(required = false) StockParam stockParam) {
        if (ToolUtil.isEmpty(stockParam)) {
            stockParam = new StockParam();
        }
        return this.stockService.findListBySpec(stockParam);
    }

    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData<List<Map<String, Object>>> listSelect() {
        QueryWrapper<Stock> stockQueryWrapper = new QueryWrapper<>();
        stockQueryWrapper.in("display", 1);
        List<Map<String, Object>> list = this.stockService.listMaps(stockQueryWrapper);
        StockSelectWrapper stockSelectWrapper = new StockSelectWrapper(list);
        List<Map<String, Object>> result = stockSelectWrapper.wrap();
        return ResponseData.success(result);
    }

    @RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
    @ApiOperation("批量删除")
    public ResponseData batchDelete(@RequestBody StockRequest stockRequest) {
        stockService.batchDelete(stockRequest.getStockId());
        return ResponseData.success();
    }
}


