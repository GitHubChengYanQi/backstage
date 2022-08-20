package cn.atsoft.dasheng.production.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionPlanDetail;
import cn.atsoft.dasheng.production.model.params.ProductionPlanDetailParam;
import cn.atsoft.dasheng.production.model.result.ProductionPlanDetailResult;
import cn.atsoft.dasheng.production.service.ProductionPlanDetailService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 生产计划子表控制器
 *
 * @author 
 * @Date 2022-02-25 14:22:57
 */
@RestController
@RequestMapping("/planDetail")
@Api(tags = "生产计划子表")
public class ProductionDetailController extends BaseController {

    @Autowired
    private ProductionPlanDetailService productionPlanDetailService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2022-02-25
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ProductionPlanDetailParam productionPlanDetailParam) {
        this.productionPlanDetailService.add(productionPlanDetailParam);
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
    public ResponseData update(@RequestBody ProductionPlanDetailParam productionPlanDetailParam) {

        this.productionPlanDetailService.update(productionPlanDetailParam);
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
    public ResponseData delete(@RequestBody ProductionPlanDetailParam productionPlanDetailParam)  {
        this.productionPlanDetailService.delete(productionPlanDetailParam);
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
    public ResponseData detail(@RequestBody ProductionPlanDetailParam productionPlanDetailParam) {
        ProductionPlanDetail detail = this.productionPlanDetailService.getById(productionPlanDetailParam.getProductionPlanDetailId());
        ProductionPlanDetailResult result = new ProductionPlanDetailResult();
        ToolUtil.copyProperties(detail, result);

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
    public PageInfo<ProductionPlanDetailResult> list(@RequestBody(required = false) ProductionPlanDetailParam productionPlanDetailParam) {
        if(ToolUtil.isEmpty(productionPlanDetailParam)){
            productionPlanDetailParam = new ProductionPlanDetailParam();
        }
        return this.productionPlanDetailService.findPageBySpec(productionPlanDetailParam);
    }




}


