package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Logistics;
import cn.atsoft.dasheng.app.model.params.LogisticsParam;
import cn.atsoft.dasheng.app.model.result.LogisticsResult;
import cn.atsoft.dasheng.app.service.LogisticsService;
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
 * 物流表控制器
 *
 * @author 
 * @Date 2021-07-15 17:19:17
 */
@RestController
@RequestMapping("/logistics")
@Api(tags = "物流表")
public class LogisticsController extends BaseController {

    @Autowired
    private LogisticsService logisticsService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody LogisticsParam logisticsParam) {
        this.logisticsService.add(logisticsParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody LogisticsParam logisticsParam) {

        this.logisticsService.update(logisticsParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody LogisticsParam logisticsParam)  {
        this.logisticsService.delete(logisticsParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<LogisticsResult> detail(@RequestBody LogisticsParam logisticsParam) {
        Logistics detail = this.logisticsService.getById(logisticsParam.getLogisticsId());
        LogisticsResult result = new LogisticsResult();
        ToolUtil.copyProperties(detail, result);

        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<LogisticsResult> list(@RequestBody(required = false) LogisticsParam logisticsParam) {
        if(ToolUtil.isEmpty(logisticsParam)){
            logisticsParam = new LogisticsParam();
        }
        return this.logisticsService.findPageBySpec(logisticsParam);
    }




}


