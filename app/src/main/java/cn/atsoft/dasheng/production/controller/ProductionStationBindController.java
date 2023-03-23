package cn.atsoft.dasheng.production.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionStationBind;
import cn.atsoft.dasheng.production.model.params.ProductionStationBindParam;
import cn.atsoft.dasheng.production.model.result.ProductionStationBindResult;
import cn.atsoft.dasheng.production.service.ProductionStationBindService;
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
 * 工位绑定表控制器
 *
 * @author Captain_Jazz
 * @Date 2022-02-15 10:03:24
 */
@RestController
@RequestMapping("/productionStationBind")
@Api(tags = "工位绑定表")
public class ProductionStationBindController extends BaseController {

    @Autowired
    private ProductionStationBindService productionStationBindService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2022-02-15
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ProductionStationBindParam productionStationBindParam) {
        this.productionStationBindService.add(productionStationBindParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2022-02-15
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ProductionStationBindParam productionStationBindParam) {

        this.productionStationBindService.update(productionStationBindParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2022-02-15
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ProductionStationBindParam productionStationBindParam)  {
        this.productionStationBindService.delete(productionStationBindParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2022-02-15
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody ProductionStationBindParam productionStationBindParam) {
        ProductionStationBind detail = this.productionStationBindService.getById(productionStationBindParam.getProductionStationBindId());
        ProductionStationBindResult result = new ProductionStationBindResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-02-15
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ProductionStationBindResult> list(@RequestBody(required = false) ProductionStationBindParam productionStationBindParam) {
        if(ToolUtil.isEmpty(productionStationBindParam)){
            productionStationBindParam = new ProductionStationBindParam();
        }
        return this.productionStationBindService.findPageBySpec(productionStationBindParam);
    }




}


