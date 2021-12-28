package cn.atsoft.dasheng.inventory.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.inventory.entity.InventoryDetail;
import cn.atsoft.dasheng.inventory.model.params.InventoryDetailParam;
import cn.atsoft.dasheng.inventory.model.result.InventoryDetailResult;
import cn.atsoft.dasheng.inventory.service.InventoryDetailService;
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
 * 盘点任务详情控制器
 *
 * @author Captain_Jazz
 * @Date 2021-12-27 09:27:27
 */
@RestController
@RequestMapping("/inventoryDetail")
@Api(tags = "盘点任务详情")
public class InventoryDetailController extends BaseController {

    @Autowired
    private InventoryDetailService inventoryDetailService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2021-12-27
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody InventoryDetailParam inventoryDetailParam) {
        this.inventoryDetailService.add(inventoryDetailParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2021-12-27
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody InventoryDetailParam inventoryDetailParam) {

        this.inventoryDetailService.update(inventoryDetailParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2021-12-27
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody InventoryDetailParam inventoryDetailParam)  {
        this.inventoryDetailService.delete(inventoryDetailParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2021-12-27
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<InventoryDetailResult> detail(@RequestBody InventoryDetailParam inventoryDetailParam) {
        InventoryDetail detail = this.inventoryDetailService.getById(inventoryDetailParam.getDetailId());
        InventoryDetailResult result = new InventoryDetailResult();
        ToolUtil.copyProperties(detail, result);


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2021-12-27
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<InventoryDetailResult> list(@RequestBody(required = false) InventoryDetailParam inventoryDetailParam) {
        if(ToolUtil.isEmpty(inventoryDetailParam)){
            inventoryDetailParam = new InventoryDetailParam();
        }
        return this.inventoryDetailService.findPageBySpec(inventoryDetailParam);
    }




}


