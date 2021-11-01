package cn.atsoft.dasheng.production.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.WorkingPorcedureClass;
import cn.atsoft.dasheng.production.model.params.WorkingPorcedureClassParam;
import cn.atsoft.dasheng.production.model.result.WorkingPorcedureClassResult;
import cn.atsoft.dasheng.production.service.WorkingPorcedureClassService;
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
 * 工序分类表控制器
 *
 * @author 
 * @Date 2021-10-29 20:58:49
 */
@RestController
@RequestMapping("/workingPorcedureClass")
@Api(tags = "工序分类表")
public class WorkingPorcedureClassController extends BaseController {

    @Autowired
    private WorkingPorcedureClassService workingPorcedureClassService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody WorkingPorcedureClassParam workingPorcedureClassParam) {
        this.workingPorcedureClassService.add(workingPorcedureClassParam);
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
    public ResponseData update(@RequestBody WorkingPorcedureClassParam workingPorcedureClassParam) {

        this.workingPorcedureClassService.update(workingPorcedureClassParam);
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
    public ResponseData delete(@RequestBody WorkingPorcedureClassParam workingPorcedureClassParam)  {
        this.workingPorcedureClassService.delete(workingPorcedureClassParam);
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
    public ResponseData<WorkingPorcedureClassResult> detail(@RequestBody WorkingPorcedureClassParam workingPorcedureClassParam) {
        WorkingPorcedureClass detail = this.workingPorcedureClassService.getById(workingPorcedureClassParam.getWpClassId());
        WorkingPorcedureClassResult result = new WorkingPorcedureClassResult();
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
    public PageInfo<WorkingPorcedureClassResult> list(@RequestBody(required = false) WorkingPorcedureClassParam workingPorcedureClassParam) {
        if(ToolUtil.isEmpty(workingPorcedureClassParam)){
            workingPorcedureClassParam = new WorkingPorcedureClassParam();
        }
        return this.workingPorcedureClassService.findPageBySpec(workingPorcedureClassParam);
    }




}


