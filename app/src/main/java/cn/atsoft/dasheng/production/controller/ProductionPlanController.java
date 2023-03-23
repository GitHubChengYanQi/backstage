package cn.atsoft.dasheng.production.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionPlan;
import cn.atsoft.dasheng.production.model.params.ProductionPlanParam;
import cn.atsoft.dasheng.production.model.result.ProductionPlanResult;
import cn.atsoft.dasheng.production.service.ProductionPlanService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;


/**
 * 生产计划主表控制器
 *
 * @author 
 * @Date 2022-02-25 14:22:57
 */
@RestController
@RequestMapping("/productionPlan")
@Api(tags = "生产计划主表")
public class ProductionPlanController extends BaseController {

    @Autowired
    private ProductionPlanService productionPlanService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2022-02-25
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ProductionPlanParam productionPlanParam) {
        this.productionPlanService.add(productionPlanParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2022-02-25
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ProductionPlanParam productionPlanParam) {

        this.productionPlanService.update(productionPlanParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2022-02-25
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ProductionPlanParam productionPlanParam)  {
        this.productionPlanService.delete(productionPlanParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2022-02-25
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody ProductionPlanParam productionPlanParam) {
        ProductionPlan detail = this.productionPlanService.getById(productionPlanParam.getProductionPlanId());
        ProductionPlanResult result = new ProductionPlanResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }
        productionPlanService.format(new ArrayList<ProductionPlanResult>(){{
            add(result);
        }});

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2022-02-25
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ProductionPlanResult> list(@RequestBody(required = false) ProductionPlanParam productionPlanParam) {
        if(ToolUtil.isEmpty(productionPlanParam)){
            productionPlanParam = new ProductionPlanParam();
        }
        return this.productionPlanService.findPageBySpec(productionPlanParam);
    }




}


