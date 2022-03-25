package cn.atsoft.dasheng.production.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionTask;
import cn.atsoft.dasheng.production.model.params.ProductionTaskParam;
import cn.atsoft.dasheng.production.model.result.ProductionTaskResult;
import cn.atsoft.dasheng.production.service.ProductionTaskService;
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
 * 生产任务控制器
 *
 * @author Captain_Jazz
 * @Date 2022-03-22 15:16:11
 */
@RestController
@RequestMapping("/productionTask")
@Api(tags = "生产任务")
public class ProductionTaskController extends BaseController {

    @Autowired
    private ProductionTaskService productionTaskService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2022-03-22
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ProductionTaskParam productionTaskParam) {
        this.productionTaskService.add(productionTaskParam);
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
    public ProductionTask update(@RequestBody ProductionTaskParam productionTaskParam) {

        ProductionTask productionTask = this.productionTaskService.update(productionTaskParam);
        return productionTask;
    }
    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2022-03-22
     */
    @RequestMapping(value = "/receive", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData Receive(@RequestBody ProductionTaskParam productionTaskParam) {

        this.productionTaskService.Receive(productionTaskParam);
        return ResponseData.success();
    }

//    /**
//     * 删除接口
//     *
//     * @author Captain_Jazz
//     * @Date 2022-03-22
//     */
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    @ApiOperation("删除")
//    public ResponseData delete(@RequestBody ProductionTaskParam productionTaskParam) {
//        this.productionTaskService.delete(productionTaskParam);
//        return ResponseData.success();
//    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2022-03-22
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<ProductionTaskResult> detail(@RequestBody ProductionTaskParam productionTaskParam) {
        ProductionTask detail = this.productionTaskService.getById(productionTaskParam.getProductionTaskId());
        ProductionTaskResult result = new ProductionTaskResult();
        ToolUtil.copyProperties(detail, result);
        List<ProductionTaskResult> list = new ArrayList<>();
        list.add(result);
        this.productionTaskService.format(list);
//        result.setValue(parentValue);
        return ResponseData.success(list.size() > 0 ? list.get(0) : null);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-22
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ProductionTaskResult> list(@RequestBody(required = false) ProductionTaskParam productionTaskParam) {
        if (ToolUtil.isEmpty(productionTaskParam)) {
            productionTaskParam = new ProductionTaskParam();
        }
        return this.productionTaskService.findPageBySpec(productionTaskParam);
    }


}


