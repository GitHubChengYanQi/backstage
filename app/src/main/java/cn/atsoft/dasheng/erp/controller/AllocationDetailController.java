package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.AllocationDetail;
import cn.atsoft.dasheng.erp.model.params.AllocationDetailParam;
import cn.atsoft.dasheng.erp.model.result.AllocationDetailResult;
import cn.atsoft.dasheng.erp.service.AllocationDetailService;
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
 * 调拨子表控制器
 *
 * @author Captain_Jazz
 * @Date 2022-07-13 16:11:47
 */
@RestController
@RequestMapping("/allocationDetail")
@Api(tags = "调拨子表")
public class AllocationDetailController extends BaseController {

    @Autowired
    private AllocationDetailService allocationDetailService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2022-07-13
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody AllocationDetailParam allocationDetailParam) {
        this.allocationDetailService.add(allocationDetailParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2022-07-13
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody AllocationDetailParam allocationDetailParam) {

        this.allocationDetailService.update(allocationDetailParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2022-07-13
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody AllocationDetailParam allocationDetailParam)  {
        this.allocationDetailService.delete(allocationDetailParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2022-07-13
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody AllocationDetailParam allocationDetailParam) {
        AllocationDetail detail = this.allocationDetailService.getById(allocationDetailParam.getAllocationDetailId());
        AllocationDetailResult result = new AllocationDetailResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-07-13
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<AllocationDetailResult> list(@RequestBody(required = false) AllocationDetailParam allocationDetailParam) {
        if(ToolUtil.isEmpty(allocationDetailParam)){
            allocationDetailParam = new AllocationDetailParam();
        }
        return this.allocationDetailService.findPageBySpec(allocationDetailParam);
    }
/**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-07-13
     */
    @RequestMapping(value = "/listByAllocationIdAndSkuId", method = RequestMethod.POST)
    @ApiOperation("列表")
    public ResponseData listByAllocationIdAndSkuId(@RequestParam Long allocationId,@RequestParam Long skuId) {
        return ResponseData.success(this.allocationDetailService.listByAllocationIdAndSkuId(allocationId,skuId));
    }




}


