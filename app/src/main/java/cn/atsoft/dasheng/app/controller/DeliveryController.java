package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.model.params.StockDetailsParam;
import cn.atsoft.dasheng.app.model.params.StockParam;
import cn.atsoft.dasheng.app.model.result.StockDetailsResult;
import cn.atsoft.dasheng.app.model.result.StockResult;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.app.service.StockService;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Delivery;
import cn.atsoft.dasheng.app.model.params.DeliveryParam;
import cn.atsoft.dasheng.app.model.result.DeliveryResult;
import cn.atsoft.dasheng.app.service.DeliveryService;
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
 * 出库表控制器
 *
 * @author song
 * @Date 2021-07-17 10:46:08
 */
@RestController
@RequestMapping("/delivery")
@Api(tags = "出库表")
public class DeliveryController extends BaseController {

    @Autowired
    private DeliveryService deliveryService;

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
    public ResponseData addItem(@RequestBody DeliveryParam deliveryParam) {



        StockParam stockParam = new StockParam();

        PageInfo<StockResult> pageBySpec = this.stockService.findPageBySpec(stockParam);

            for (int i = 0 ; i < pageBySpec.getData().size() ; i++) {
                if (pageBySpec.getData().get(i).getStockId().equals(deliveryParam.getStockId())) {
                    if (pageBySpec.getData().get(i).getInventory()>=deliveryParam.getNumber()){
                        stockParam.setStockId(pageBySpec.getData().get(i).getStockId());
                        stockParam.setItemId(pageBySpec.getData().get(i).getItemId());
                        stockParam.setBrandId(pageBySpec.getData().get(i).getBrandId());
                        stockParam.setPalceId(pageBySpec.getData().get(i).getPalceId());
                        stockParam.setInventory(pageBySpec.getData().get(i).getInventory()-deliveryParam.getNumber());
                        this.stockService.update(stockParam);
                        this.deliveryService.add(deliveryParam);


                        StockDetailsParam stockDetailsParam = new StockDetailsParam();
                        stockDetailsParam.setStockId(pageBySpec.getData().get(i).getStockId());

                        PageInfo<StockDetailsResult> pageBySpec1 = this.stockDetailsService.findPageBySpec(stockDetailsParam);




                        for (int j = 0 ; j < deliveryParam.getNumber() ; j++ ){
                            stockDetailsParam.setStockItemId(pageBySpec1.getData().get(j).getStockItemId());
                            this.stockDetailsService.delete(stockDetailsParam);
                        }




                        return ResponseData.success();
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
    public ResponseData update(@RequestBody DeliveryParam deliveryParam) {

        this.deliveryService.update(deliveryParam);
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
    public ResponseData delete(@RequestBody DeliveryParam deliveryParam)  {
        this.deliveryService.delete(deliveryParam);
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
    public ResponseData<DeliveryResult> detail(@RequestBody DeliveryParam deliveryParam) {
        System.err.println(deliveryParam);
        Delivery detail = this.deliveryService.getById(deliveryParam.getDeliveryId());
        DeliveryResult result = new DeliveryResult();
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
    public PageInfo<DeliveryResult> list(@RequestBody(required = false) DeliveryParam deliveryParam) {
        if(ToolUtil.isEmpty(deliveryParam)){
            deliveryParam = new DeliveryParam();
        }
        return this.deliveryService.findPageBySpec(deliveryParam);
    }




}


