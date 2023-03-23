package cn.atsoft.dasheng.production.controller;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionPickCode;
import cn.atsoft.dasheng.production.entity.ProductionPickLists;
import cn.atsoft.dasheng.production.entity.ProductionTask;
import cn.atsoft.dasheng.production.model.params.ProductionTaskParam;
import cn.atsoft.dasheng.production.model.result.ProductionTaskResult;
import cn.atsoft.dasheng.production.service.ProductionPickCodeService;
import cn.atsoft.dasheng.production.service.ProductionPickListsService;
import cn.atsoft.dasheng.production.service.ProductionTaskService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.RandomUtil;
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

    @Autowired
    private ProductionPickCodeService pickCodeService;

    @Autowired
    private ProductionPickListsService pickListsService;

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
    public ProductionTask Receive(@RequestBody ProductionTaskParam productionTaskParam) {

        ProductionTask receive = this.productionTaskService.Receive(productionTaskParam);
        return receive;
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
    public ResponseData detail(@RequestBody ProductionTaskParam productionTaskParam) {
        ProductionTask detail = this.productionTaskService.getById(productionTaskParam.getProductionTaskId());
        ProductionTaskResult result = new ProductionTaskResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }
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


    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-22
     */
    @RequestMapping(value = "/getPickCode", method = RequestMethod.POST)
    @ApiOperation("列表")
    public ResponseData getPickCode(@RequestBody(required = false) ProductionTaskParam productionTaskParam) {
        if (ToolUtil.isEmpty(productionTaskParam)) {
            productionTaskParam = new ProductionTaskParam();
        }
        ProductionPickLists one = pickListsService.query().eq("source", "productionTask").eq("source_id", productionTaskParam.getProductionTaskId()).one();
        ProductionPickCode pickListsId = pickCodeService.query().eq("pick_lists_id", one.getPickListsId()).one();
        if (ToolUtil.isEmpty(pickListsId)){
            ProductionPickCode codeEntity = new ProductionPickCode();
            long code = RandomUtil.randomLong(1000,9999);
            codeEntity.setCode(code);
            codeEntity.setProductionTaskId(productionTaskParam.getProductionTaskId());
            codeEntity.setPickListsId(one.getPickListsId());
            codeEntity.setUserId(LoginContextHolder.getContext().getUserId());
            pickCodeService.save(codeEntity);
            return ResponseData.success(codeEntity.getCode());
        }
        return ResponseData.success(pickListsId.getCode()) ;
    }


}


