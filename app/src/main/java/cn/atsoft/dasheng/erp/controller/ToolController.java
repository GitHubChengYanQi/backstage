package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Spu;
import cn.atsoft.dasheng.erp.entity.Tool;
import cn.atsoft.dasheng.erp.model.params.SpuParam;
import cn.atsoft.dasheng.erp.model.params.ToolClassificationParam;
import cn.atsoft.dasheng.erp.model.params.ToolParam;
import cn.atsoft.dasheng.erp.model.result.ToolResult;
import cn.atsoft.dasheng.erp.service.ToolService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.wrapper.SpuSelectWrapper;
import cn.atsoft.dasheng.erp.wrapper.ToolSelectWrapper;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
    @BussinessLog(value = "修改工具", key = "name", dict = ToolParam.class)
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
    @BussinessLog(value = "删除工具", key = "name", dict = ToolParam.class)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ToolParam toolParam) {
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
    public ResponseData detail(@RequestBody ToolParam toolParam) {
        Tool detail = this.toolService.getById(toolParam.getToolId());
        ToolResult result = new ToolResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

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
        if (ToolUtil.isEmpty(toolParam)) {
            toolParam = new ToolParam();
        }
        return this.toolService.findPageBySpec(toolParam);
    }

    /**
     * 选择列表
     *
     * @author jazz
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData listSelect() {

        List<Map<String, Object>> list = this.toolService.listMaps();
        ToolSelectWrapper factory = new ToolSelectWrapper(list);
        List<Map<String, Object>> result = factory.wrap();
        return ResponseData.success(result);
    }


}


