package cn.atsoft.dasheng.production.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.WorkingProcedureClass;
import cn.atsoft.dasheng.production.model.params.WorkingProcedureClassParam;
import cn.atsoft.dasheng.production.model.result.WorkingProcedureClassResult;
import cn.atsoft.dasheng.production.service.WorkingProcedureClassService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 工序分类表控制器
 *
 * @author 
 * @Date 2021-10-29 20:58:49
 */
@RestController
@RequestMapping("/workingProcedureClass")
@Api(tags = "工序分类表")
public class WorkingProcedureClassController extends BaseController {

    @Autowired
    private WorkingProcedureClassService workingProcedureClassService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody WorkingProcedureClassParam workingProcedureClassParam) {
        this.workingProcedureClassService.add(workingProcedureClassParam);
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
    public ResponseData update(@RequestBody WorkingProcedureClassParam workingProcedureClassParam) {

        this.workingProcedureClassService.update(workingProcedureClassParam);
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
    public ResponseData delete(@RequestBody WorkingProcedureClassParam workingProcedureClassParam)  {
        this.workingProcedureClassService.delete(workingProcedureClassParam);
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
    public ResponseData<WorkingProcedureClassResult> detail(@RequestBody WorkingProcedureClassParam workingProcedureClassParam) {
        WorkingProcedureClass detail = this.workingProcedureClassService.getById(workingProcedureClassParam.getWpClassId());
        WorkingProcedureClassResult result = new WorkingProcedureClassResult();
        ToolUtil.copyProperties(detail, result);

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
    public PageInfo<WorkingProcedureClassResult> list(@RequestBody(required = false) WorkingProcedureClassParam workingProcedureClassParam) {
        if(ToolUtil.isEmpty(workingProcedureClassParam)){
            workingProcedureClassParam = new WorkingProcedureClassParam();
        }
        return this.workingProcedureClassService.findPageBySpec(workingProcedureClassParam);
    }




}


