package cn.atsoft.dasheng.production.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.WorkingProcedure;
import cn.atsoft.dasheng.production.model.params.WorkingProcedureParam;
import cn.atsoft.dasheng.production.model.result.WorkingProcedureResult;
import cn.atsoft.dasheng.production.service.WorkingProcedureService;
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
 * 工序表控制器
 *
 * @author 
 * @Date 2021-10-29 18:21:07
 */
@RestController
@RequestMapping("/workingProcedure")
@Api(tags = "工序表")
public class WorkingProcedureController extends BaseController {

    @Autowired
    private WorkingProcedureService workingProcedureService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody WorkingProcedureParam workingProcedureParam) {
        this.workingProcedureService.add(workingProcedureParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody WorkingProcedureParam workingProcedureParam) {

        this.workingProcedureService.update(workingProcedureParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody WorkingProcedureParam workingProcedureParam)  {
        this.workingProcedureService.delete(workingProcedureParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<WorkingProcedureResult> detail(@RequestBody WorkingProcedureParam workingProcedureParam) {
        WorkingProcedure detail = this.workingProcedureService.getById(workingProcedureParam.getWorkingProcedureId());
        WorkingProcedureResult result = new WorkingProcedureResult();
        ToolUtil.copyProperties(detail, result);
        result.getWorkingProcedureId();

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<WorkingProcedureResult> list(@RequestBody(required = false) WorkingProcedureParam workingProcedureParam) {
        if(ToolUtil.isEmpty(workingProcedureParam)){
            workingProcedureParam = new WorkingProcedureParam();
        }
        return this.workingProcedureService.findPageBySpec(workingProcedureParam);
    }




}


