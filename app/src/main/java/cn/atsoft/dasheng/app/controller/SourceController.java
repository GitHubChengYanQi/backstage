package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.wrapper.ClientSelectWrapper;
import cn.atsoft.dasheng.app.wrapper.SourceSelectWrapper;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Source;
import cn.atsoft.dasheng.app.model.params.SourceParam;
import cn.atsoft.dasheng.app.model.result.SourceResult;
import cn.atsoft.dasheng.app.service.SourceService;
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
 * 来源表控制器
 *
 * @author 
 * @Date 2021-07-19 17:59:08
 */
@RestController
@RequestMapping("/source")
@Api(tags = "来源表")
public class SourceController extends BaseController {

    @Autowired
    private SourceService sourceService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-07-19
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody SourceParam sourceParam) {
        this.sourceService.add(sourceParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2021-07-19
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody SourceParam sourceParam) {

        this.sourceService.update(sourceParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2021-07-19
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody SourceParam sourceParam)  {
        this.sourceService.delete(sourceParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2021-07-19
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<SourceResult> detail(@RequestBody SourceParam sourceParam) {
        Source detail = this.sourceService.getById(sourceParam.getSourceId());
        SourceResult result = new SourceResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-07-19
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<SourceResult> list(@RequestBody(required = false) SourceParam sourceParam) {
        if(ToolUtil.isEmpty(sourceParam)){
            sourceParam = new SourceParam();
        }
        return this.sourceService.findPageBySpec(sourceParam);
    }
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData<List<Map<String,Object>>> listSelect() {
        List<Map<String,Object>> list = this.sourceService.listMaps();
        SourceSelectWrapper factory = new SourceSelectWrapper(list);
        List<Map<String,Object>> result = factory.wrap();
        return ResponseData.success(result);
    }



}


