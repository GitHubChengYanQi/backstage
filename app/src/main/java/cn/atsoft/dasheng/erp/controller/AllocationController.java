package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Allocation;
import cn.atsoft.dasheng.erp.model.params.AllocationParam;
import cn.atsoft.dasheng.erp.model.result.AllocationResult;
import cn.atsoft.dasheng.erp.service.AllocationService;
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
 * 调拨主表控制器
 *
 * @author Captain_Jazz
 * @Date 2022-07-13 16:11:47
 */
@RestController
@RequestMapping("/allocation")
@Api(tags = "调拨主表")
public class AllocationController extends BaseController {

    @Autowired
    private AllocationService allocationService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2022-07-13
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody AllocationParam allocationParam) {
        this.allocationService.add(allocationParam);
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
    public ResponseData update(@RequestBody AllocationParam allocationParam) {

        this.allocationService.update(allocationParam);
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
    public ResponseData delete(@RequestBody AllocationParam allocationParam)  {
        this.allocationService.delete(allocationParam);
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
    public ResponseData<AllocationResult> detail(@RequestBody AllocationParam allocationParam) {
        Allocation detail = this.allocationService.getById(allocationParam.getAllocationId());
        AllocationResult result = new AllocationResult();
        ToolUtil.copyProperties(detail, result);

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
    public PageInfo<AllocationResult> list(@RequestBody(required = false) AllocationParam allocationParam) {
        if(ToolUtil.isEmpty(allocationParam)){
            allocationParam = new AllocationParam();
        }
        return this.allocationService.findPageBySpec(allocationParam);
    }




}


