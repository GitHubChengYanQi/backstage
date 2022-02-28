package cn.atsoft.dasheng.production.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionWorkOrder;
import cn.atsoft.dasheng.production.model.params.ProductionWorkOrderParam;
import cn.atsoft.dasheng.production.model.result.ProductionWorkOrderResult;
import cn.atsoft.dasheng.production.service.ProductionWorkOrderService;
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
 * 工单控制器
 *
 * @author 
 * @Date 2022-02-28 13:51:24
 */
@RestController
@RequestMapping("/productionWorkOrder")
@Api(tags = "工单")
public class ProductionWorkOrderController extends BaseController {

    @Autowired
    private ProductionWorkOrderService productionWorkOrderService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2022-02-28
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ProductionWorkOrderParam productionWorkOrderParam) {
        this.productionWorkOrderService.add(productionWorkOrderParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2022-02-28
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ProductionWorkOrderParam productionWorkOrderParam) {

        this.productionWorkOrderService.update(productionWorkOrderParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2022-02-28
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ProductionWorkOrderParam productionWorkOrderParam)  {
        this.productionWorkOrderService.delete(productionWorkOrderParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2022-02-28
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<ProductionWorkOrderResult> detail(@RequestBody ProductionWorkOrderParam productionWorkOrderParam) {
        ProductionWorkOrder detail = this.productionWorkOrderService.getById(productionWorkOrderParam.getWorkOrderId());
        ProductionWorkOrderResult result = new ProductionWorkOrderResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2022-02-28
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ProductionWorkOrderResult> list(@RequestBody(required = false) ProductionWorkOrderParam productionWorkOrderParam) {
        if(ToolUtil.isEmpty(productionWorkOrderParam)){
            productionWorkOrderParam = new ProductionWorkOrderParam();
        }
        return this.productionWorkOrderService.findPageBySpec(productionWorkOrderParam);
    }




}


