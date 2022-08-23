package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.AllocationLogDetail;
import cn.atsoft.dasheng.erp.model.params.AllocationLogDetailParam;
import cn.atsoft.dasheng.erp.model.result.AllocationLogDetailResult;
import cn.atsoft.dasheng.erp.service.AllocationLogDetailService;
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
@RequestMapping("/allocationLogDetail")
@Api(tags = "")
public class AllocationLogDetailController extends BaseController {

    @Autowired
    private AllocationLogDetailService allocationLogDetailService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody AllocationLogDetailParam allocationLogDetailParam) {
        this.allocationLogDetailService.add(allocationLogDetailParam);
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
    public ResponseData update(@RequestBody AllocationLogDetailParam allocationLogDetailParam) {

        this.allocationLogDetailService.update(allocationLogDetailParam);
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
    public ResponseData delete(@RequestBody AllocationLogDetailParam allocationLogDetailParam)  {
        this.allocationLogDetailService.delete(allocationLogDetailParam);
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
    public ResponseData detail(@RequestBody AllocationLogDetailParam allocationLogDetailParam) {
        AllocationLogDetail detail = this.allocationLogDetailService.getById(allocationLogDetailParam.getAllocationLogDetailId());
        AllocationLogDetailResult result = new AllocationLogDetailResult();
        ToolUtil.copyProperties(detail, result);

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
    public PageInfo<AllocationLogDetailResult> list(@RequestBody(required = false) AllocationLogDetailParam allocationLogDetailParam) {
        if(ToolUtil.isEmpty(allocationLogDetailParam)){
            allocationLogDetailParam = new AllocationLogDetailParam();
        }
        return this.allocationLogDetailService.findPageBySpec(allocationLogDetailParam);
    }




}


