package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.AllocationCart;
import cn.atsoft.dasheng.erp.model.params.AllocationCartParam;
import cn.atsoft.dasheng.erp.model.result.AllocationCartResult;
import cn.atsoft.dasheng.erp.service.AllocationCartService;
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
 * @Date 2022-07-25 14:03:01
 */
@RestController
@RequestMapping("/allocationCart")
@Api(tags = "调拨子表")
public class AllocationCartController extends BaseController {

    @Autowired
    private AllocationCartService allocationCartService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2022-07-25
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody AllocationCartParam allocationCartParam) {
        this.allocationCartService.addBatch(allocationCartParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2022-07-25
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody AllocationCartParam allocationCartParam) {

        this.allocationCartService.update(allocationCartParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2022-07-25
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody AllocationCartParam allocationCartParam)  {
        this.allocationCartService.delete(allocationCartParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2022-07-25
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody AllocationCartParam allocationCartParam) {
        AllocationCart detail = this.allocationCartService.getById(allocationCartParam.getAllocationCartId());
        AllocationCartResult result = new AllocationCartResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-07-25
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<AllocationCartResult> list(@RequestBody(required = false) AllocationCartParam allocationCartParam) {
        if(ToolUtil.isEmpty(allocationCartParam)){
            allocationCartParam = new AllocationCartParam();
        }
        return this.allocationCartService.findPageBySpec(allocationCartParam);
    }
/**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-07-25
     */
    @RequestMapping(value = "/resultsByAllocationId", method = RequestMethod.GET)
    @ApiOperation("查看分派列表")
    public ResponseData list(@RequestParam(required = false) Long id) {
        return ResponseData.success(this.allocationCartService.resultsByAllocationId(id));
    }




}


