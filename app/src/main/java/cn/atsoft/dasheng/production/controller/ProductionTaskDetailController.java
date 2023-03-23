package cn.atsoft.dasheng.production.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionTaskDetail;
import cn.atsoft.dasheng.production.model.params.ProductionTaskDetailParam;
import cn.atsoft.dasheng.production.model.result.ProductionTaskDetailResult;
import cn.atsoft.dasheng.production.service.ProductionTaskDetailService;
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
 * 控制器
 *
 * @author Captain_Jazz
 * @Date 2022-03-22 15:16:11
 */
@RestController
@RequestMapping("/productionTaskDetail")
@Api(tags = "")
public class ProductionTaskDetailController extends BaseController {

    @Autowired
    private ProductionTaskDetailService productionTaskDetailService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2022-03-22
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ProductionTaskDetailParam productionTaskDetailParam) {
        this.productionTaskDetailService.add(productionTaskDetailParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2022-03-22
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ProductionTaskDetailParam productionTaskDetailParam) {

        this.productionTaskDetailService.update(productionTaskDetailParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2022-03-22
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ProductionTaskDetailParam productionTaskDetailParam)  {
        this.productionTaskDetailService.delete(productionTaskDetailParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2022-03-22
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody ProductionTaskDetailParam productionTaskDetailParam) {
        ProductionTaskDetail detail = this.productionTaskDetailService.getById(productionTaskDetailParam.getProductionTaskDetailId());
        ProductionTaskDetailResult result = new ProductionTaskDetailResult();
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
     * @Date 2022-03-22
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ProductionTaskDetailResult> list(@RequestBody(required = false) ProductionTaskDetailParam productionTaskDetailParam) {
        if(ToolUtil.isEmpty(productionTaskDetailParam)){
            productionTaskDetailParam = new ProductionTaskDetailParam();
        }
        return this.productionTaskDetailService.findPageBySpec(productionTaskDetailParam);
    }




}


