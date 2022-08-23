package cn.atsoft.dasheng.production.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProcessRoute;
import cn.atsoft.dasheng.production.model.params.ProcessRouteParam;
import cn.atsoft.dasheng.production.model.result.ProcessRouteResult;
import cn.atsoft.dasheng.production.service.ProcessRouteService;
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
 * 工艺路线列表控制器
 *
 * @author Captain_Jazz
 * @Date 2022-02-15 14:12:28
 */
@RestController
@RequestMapping("/processRoute")
@Api(tags = "工艺路线列表")
public class ProcessRouteController extends BaseController {

    @Autowired
    private ProcessRouteService processRouteService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2022-02-15
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ProcessRouteParam processRouteParam) {
        this.processRouteService.add(processRouteParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2022-02-15
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ProcessRouteParam processRouteParam) {

        this.processRouteService.update(processRouteParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2022-02-15
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ProcessRouteParam processRouteParam)  {
        this.processRouteService.delete(processRouteParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2022-02-15
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody ProcessRouteParam processRouteParam) {
        ProcessRoute detail = this.processRouteService.getById(processRouteParam.getProcessRouteId());
        ProcessRouteResult result = new ProcessRouteResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }
    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2022-02-15
     */
    @RequestMapping(value = "/getRouteBySkuId", method = RequestMethod.GET)
    @ApiOperation("详情")
    public ResponseData getRouteBySkuId(@RequestParam Long id) {
       return ResponseData.success(this.processRouteService.getRouteBySkuId(id));
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-02-15
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ProcessRouteResult> list(@RequestBody(required = false) ProcessRouteParam processRouteParam) {
        if(ToolUtil.isEmpty(processRouteParam)){
            processRouteParam = new ProcessRouteParam();
        }
        return this.processRouteService.findPageBySpec(processRouteParam);
    }




}


