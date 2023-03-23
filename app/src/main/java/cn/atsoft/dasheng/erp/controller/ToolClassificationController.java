package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.ToolClassification;
import cn.atsoft.dasheng.erp.model.params.StorehousePositionsParam;
import cn.atsoft.dasheng.erp.model.params.ToolClassificationParam;
import cn.atsoft.dasheng.erp.model.result.ToolClassificationResult;
import cn.atsoft.dasheng.erp.service.ToolClassificationService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.wrapper.CodingRulesClassificationSelectWrapper;
import cn.atsoft.dasheng.erp.wrapper.ToolClassificationSelectWrapper;
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
 * 工具分类表控制器
 *
 * @author song
 * @Date 2021-10-23 10:40:17
 */
@RestController
@RequestMapping("/toolClassification")
@Api(tags = "工具分类表")
public class ToolClassificationController extends BaseController {

    @Autowired
    private ToolClassificationService toolClassificationService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-10-23
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ToolClassificationParam toolClassificationParam) {
        this.toolClassificationService.add(toolClassificationParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-10-23
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @BussinessLog(value = "修改工具分类", key = "name", dict = ToolClassificationParam.class)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ToolClassificationParam toolClassificationParam) {

        this.toolClassificationService.update(toolClassificationParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-10-23
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @BussinessLog(value = "删除工具分类", key = "name", dict = ToolClassificationParam.class)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ToolClassificationParam toolClassificationParam)  {
        this.toolClassificationService.delete(toolClassificationParam);
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
    public ResponseData detail(@RequestBody ToolClassificationParam toolClassificationParam) {
        ToolClassification detail = this.toolClassificationService.getById(toolClassificationParam.getToolClassificationId());
        ToolClassificationResult result = new ToolClassificationResult();
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
    public PageInfo<ToolClassificationResult> list(@RequestBody(required = false) ToolClassificationParam toolClassificationParam) {
        if(ToolUtil.isEmpty(toolClassificationParam)){
            toolClassificationParam = new ToolClassificationParam();
        }
        return this.toolClassificationService.findPageBySpec(toolClassificationParam);
    }
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("编码规则下拉列表")
    public ResponseData listSelect() {
        List<Map<String, Object>> list = this.toolClassificationService.listMaps();
        ToolClassificationSelectWrapper toolClassificationSelectWrapper = new ToolClassificationSelectWrapper(list);
        List<Map<String, Object>> result = toolClassificationSelectWrapper.wrap();
        return ResponseData.success(result);
    }

}


