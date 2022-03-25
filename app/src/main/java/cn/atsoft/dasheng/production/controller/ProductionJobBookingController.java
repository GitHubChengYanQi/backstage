package cn.atsoft.dasheng.production.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionJobBooking;
import cn.atsoft.dasheng.production.model.params.ProductionJobBookingParam;
import cn.atsoft.dasheng.production.model.result.ProductionJobBookingResult;
import cn.atsoft.dasheng.production.service.ProductionJobBookingService;
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
 * 报工表控制器
 *
 * @author Captain_Jazz
 * @Date 2022-03-23 09:17:11
 */
@RestController
@RequestMapping("/productionJobBooking")
@Api(tags = "报工表")
public class ProductionJobBookingController extends BaseController {

    @Autowired
    private ProductionJobBookingService productionJobBookingService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2022-03-23
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ProductionJobBookingParam productionJobBookingParam) {
        this.productionJobBookingService.add(productionJobBookingParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2022-03-23
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ProductionJobBookingParam productionJobBookingParam) {

        this.productionJobBookingService.update(productionJobBookingParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2022-03-23
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ProductionJobBookingParam productionJobBookingParam)  {
        this.productionJobBookingService.delete(productionJobBookingParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2022-03-23
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<ProductionJobBookingResult> detail(@RequestBody ProductionJobBookingParam productionJobBookingParam) {
        ProductionJobBooking detail = this.productionJobBookingService.getById(productionJobBookingParam.getJobBookingId());
        ProductionJobBookingResult result = new ProductionJobBookingResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-23
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ProductionJobBookingResult> list(@RequestBody(required = false) ProductionJobBookingParam productionJobBookingParam) {
        if(ToolUtil.isEmpty(productionJobBookingParam)){
            productionJobBookingParam = new ProductionJobBookingParam();
        }
        return this.productionJobBookingService.findPageBySpec(productionJobBookingParam);
    }




}


