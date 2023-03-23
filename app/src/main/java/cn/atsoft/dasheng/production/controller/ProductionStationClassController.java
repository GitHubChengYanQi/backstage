package cn.atsoft.dasheng.production.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionStationClass;
import cn.atsoft.dasheng.production.model.params.ProductionStationClassParam;
import cn.atsoft.dasheng.production.model.result.ProductionStationClassResult;
import cn.atsoft.dasheng.production.service.ProductionStationClassService;
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
 * @author song
 * @Date 2021-10-29 18:03:45
 */
@RestController
@RequestMapping("/productionStationClass")
@Api(tags = "工位绑定表")
public class ProductionStationClassController extends BaseController {

    @Autowired
    private ProductionStationClassService productionStationClassService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ProductionStationClassParam productionStationClassParam) {
        this.productionStationClassService.add(productionStationClassParam);
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
    public ResponseData update(@RequestBody ProductionStationClassParam productionStationClassParam) {

        this.productionStationClassService.update(productionStationClassParam);
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
    public ResponseData delete(@RequestBody ProductionStationClassParam productionStationClassParam)  {
        this.productionStationClassService.delete(productionStationClassParam);
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
    public ResponseData detail(@RequestBody ProductionStationClassParam productionStationClassParam) {
        ProductionStationClass detail = this.productionStationClassService.getById(productionStationClassParam.getProductionStationClassId());
        ProductionStationClassResult result = new ProductionStationClassResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }


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
    public PageInfo<ProductionStationClassResult> list(@RequestBody(required = false) ProductionStationClassParam productionStationClassParam) {
        if(ToolUtil.isEmpty(productionStationClassParam)){
            productionStationClassParam = new ProductionStationClassParam();
        }
        return this.productionStationClassService.findPageBySpec(productionStationClassParam);
    }




}


