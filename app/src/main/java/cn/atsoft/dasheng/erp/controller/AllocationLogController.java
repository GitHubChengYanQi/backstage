package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.AllocationLog;
import cn.atsoft.dasheng.erp.model.params.AllocationLogParam;
import cn.atsoft.dasheng.erp.model.result.AllocationLogResult;
import cn.atsoft.dasheng.erp.service.AllocationLogService;
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
 * @Date 2022-08-11 16:41:36
 */
@RestController
@RequestMapping("/allocationLog")
@Api(tags = "")
public class AllocationLogController extends BaseController {

    @Autowired
    private AllocationLogService allocationLogService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody AllocationLogParam allocationLogParam) {
        this.allocationLogService.add(allocationLogParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody AllocationLogParam allocationLogParam) {

        this.allocationLogService.update(allocationLogParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody AllocationLogParam allocationLogParam)  {
        this.allocationLogService.delete(allocationLogParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody AllocationLogParam allocationLogParam) {
        AllocationLog detail = this.allocationLogService.getById(allocationLogParam.getAllocationLogId());
        AllocationLogResult result = new AllocationLogResult();
if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<AllocationLogResult> list(@RequestBody(required = false) AllocationLogParam allocationLogParam) {
        if(ToolUtil.isEmpty(allocationLogParam)){
            allocationLogParam = new AllocationLogParam();
        }
        return this.allocationLogService.findPageBySpec(allocationLogParam);
    }




}


