package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Tool;
import cn.atsoft.dasheng.erp.model.params.ToolParam;
import cn.atsoft.dasheng.erp.model.result.ToolResult;
import cn.atsoft.dasheng.erp.service.ToolService;
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
 * 工具表控制器
 *
 * @author song
 * @Date 2021-10-23 10:40:17
 */
@RestController
@RequestMapping("/tool")
@Api(tags = "工具表")
public class ToolController extends BaseController {

    @Autowired
    private ToolService toolService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-10-23
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ToolParam toolParam) {
        this.toolService.add(toolParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-10-23
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ToolParam toolParam) {

        this.toolService.update(toolParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-10-23
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ToolParam toolParam)  {
        this.toolService.delete(toolParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-10-23
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<ToolResult> detail(@RequestBody ToolParam toolParam) {
        Tool detail = this.toolService.getById(toolParam.getToolId());
        ToolResult result = new ToolResult();
        ToolUtil.copyProperties(detail, result);

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-10-23
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ToolResult> list(@RequestBody(required = false) ToolParam toolParam) {
        if(ToolUtil.isEmpty(toolParam)){
            toolParam = new ToolParam();
        }
        return this.toolService.findPageBySpec(toolParam);
    }




}


