package cn.atsoft.dasheng.production.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionStation;
import cn.atsoft.dasheng.production.model.params.ProductionStationParam;
import cn.atsoft.dasheng.production.model.result.ProductionStationResult;
import cn.atsoft.dasheng.production.service.ProductionStationService;
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
 * 工位表控制器
 *
 * @author song
 * @Date 2021-10-29 18:03:45
 */
@RestController
@RequestMapping("/productionStation")
@Api(tags = "工位表")
public class ProductionStationController extends BaseController {

    @Autowired
    private ProductionStationService productionStationService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ProductionStationParam productionStationParam) {
        this.productionStationService.add(productionStationParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ProductionStationParam productionStationParam) {

        this.productionStationService.update(productionStationParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ProductionStationParam productionStationParam) {
        this.productionStationService.delete(productionStationParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<ProductionStationResult> detail(@RequestBody ProductionStationParam productionStationParam) {
        ProductionStation detail = this.productionStationService.getById(productionStationParam.getProductionStationId());
        ProductionStationResult result = new ProductionStationResult();
        ToolUtil.copyProperties(detail, result);

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ProductionStationResult> list(@RequestBody(required = false) ProductionStationParam productionStationParam) {
        if (ToolUtil.isEmpty(productionStationParam)) {
            productionStationParam = new ProductionStationParam();
        }
        return this.productionStationService.findPageBySpec(productionStationParam);
    }


}


