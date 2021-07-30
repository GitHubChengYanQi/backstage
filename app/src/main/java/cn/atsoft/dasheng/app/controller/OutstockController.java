package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.model.params.StockDetailsParam;
import cn.atsoft.dasheng.app.model.params.StockParam;
import cn.atsoft.dasheng.app.model.result.StockDetailsResult;
import cn.atsoft.dasheng.app.model.result.StockResult;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.app.service.StockService;
import cn.atsoft.dasheng.app.wrapper.BrandSelectWrapper;
import cn.atsoft.dasheng.app.wrapper.OutstockSelectWrapper;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Outstock;
import cn.atsoft.dasheng.app.model.params.OutstockParam;
import cn.atsoft.dasheng.app.model.result.OutstockResult;
import cn.atsoft.dasheng.app.service.OutstockService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.Map;


/**
 * 出库表控制器
 *
 * @author song
 * @Date 2021-07-17 10:46:08
 */
@RestController
@RequestMapping("/outstock")
@Api(tags = "出库表")
public class OutstockController extends BaseController {

    @Autowired
    private OutstockService outstockService;

    @Autowired
    private StockService stockService;

    @Autowired
    private StockDetailsService stockDetailsService;


    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-07-17
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody OutstockParam outstockParam) {



        StockParam stockParam = new StockParam();

        PageInfo<StockResult> pageBySpec = this.stockService.findPageBySpec(stockParam);

            for (int i = 0 ; i < pageBySpec.getData().size() ; i++) {
                if (pageBySpec.getData().get(i).getStockId().equals(outstockParam.getStockId())) {
                    if (pageBySpec.getData().get(i).getInventory()>= outstockParam.getNumber()){
                        stockParam.setStockId(pageBySpec.getData().get(i).getStockId());
                        stockParam.setItemId(pageBySpec.getData().get(i).getItemId());
                        stockParam.setBrandId(pageBySpec.getData().get(i).getBrandId());
                        stockParam.setStorehouseId(pageBySpec.getData().get(i).getStorehouseId());
                        stockParam.setInventory(pageBySpec.getData().get(i).getInventory()- outstockParam.getNumber());
                        this.stockService.update(stockParam);
                        Long add = this.outstockService.add(outstockParam);


                        StockDetailsParam stockDetailsParam = new StockDetailsParam();
                        stockDetailsParam.setStockId(pageBySpec.getData().get(i).getStockId());

                        List<StockDetailsResult> listBySpec = this.stockDetailsService.findListBySpec(stockDetailsParam);


                        for (int j = 0; j < outstockParam.getNumber() ; j++ ){
                            stockDetailsParam.setStockItemId(listBySpec.get(j).getStockItemId());
                            this.stockDetailsService.delete(stockDetailsParam);
                        }




                        return ResponseData.success(add);
                    }

                }
            }



        return ResponseData.error("出库失败!");
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-07-17
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody OutstockParam outstockParam) {

        this.outstockService.update(outstockParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-07-17
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody OutstockParam outstockParam)  {
        this.outstockService.delete(outstockParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-07-17
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<OutstockResult> detail(@RequestBody OutstockParam outstockParam) {
        System.err.println(outstockParam);
        Outstock detail = this.outstockService.getById(outstockParam.getOutstockId());
        OutstockResult result = new OutstockResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-07-17
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<OutstockResult> list(@RequestBody(required = false) OutstockParam outstockParam) {
        if(ToolUtil.isEmpty(outstockParam)){
            outstockParam = new OutstockParam();
        }
        return this.outstockService.findPageBySpec(outstockParam);
    }


  /**
   * 选择列表
   *
   * @author 1
   * @Date 2021-07-14
   */
  @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
  @ApiOperation("Select数据接口")
  public ResponseData<List<Map<String,Object>>> listSelect() {
    List<Map<String,Object>> list = this.outstockService.listMaps();
    OutstockSelectWrapper factory = new OutstockSelectWrapper(list);
    List<Map<String,Object>> result = factory.wrap();
    return ResponseData.success(result);
  }


}

