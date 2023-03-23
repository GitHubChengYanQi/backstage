package cn.atsoft.dasheng.production.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionJobBookingDetail;
import cn.atsoft.dasheng.production.model.params.ProductionJobBookingDetailParam;
import cn.atsoft.dasheng.production.model.result.ProductionJobBookingDetailResult;
import cn.atsoft.dasheng.production.service.ProductionJobBookingDetailService;
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
 * 报工详情表控制器
 *
 * @author Captain_Jazz
 * @Date 2022-03-23 09:17:11
 */
@RestController
@RequestMapping("/productionJobBookingDetail")
@Api(tags = "报工详情表")
public class ProductionJobBookingDetailController extends BaseController {

    @Autowired
    private ProductionJobBookingDetailService productionJobBookingDetailService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2022-03-23
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ProductionJobBookingDetailParam productionJobBookingDetailParam) {
        this.productionJobBookingDetailService.add(productionJobBookingDetailParam);
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
    public ResponseData update(@RequestBody ProductionJobBookingDetailParam productionJobBookingDetailParam) {

        this.productionJobBookingDetailService.update(productionJobBookingDetailParam);
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
    public ResponseData delete(@RequestBody ProductionJobBookingDetailParam productionJobBookingDetailParam)  {
        this.productionJobBookingDetailService.delete(productionJobBookingDetailParam);
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
    public ResponseData detail(@RequestBody ProductionJobBookingDetailParam productionJobBookingDetailParam) {
        ProductionJobBookingDetail detail = this.productionJobBookingDetailService.getById(productionJobBookingDetailParam.getJobBookingDetailId());
        ProductionJobBookingDetailResult result = new ProductionJobBookingDetailResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

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
    public PageInfo<ProductionJobBookingDetailResult> list(@RequestBody(required = false) ProductionJobBookingDetailParam productionJobBookingDetailParam) {
        if(ToolUtil.isEmpty(productionJobBookingDetailParam)){
            productionJobBookingDetailParam = new ProductionJobBookingDetailParam();
        }
        return this.productionJobBookingDetailService.findPageBySpec(productionJobBookingDetailParam);
    }




}


