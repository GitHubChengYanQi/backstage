package cn.atsoft.dasheng.task.controller;

import cn.atsoft.dasheng.Excel.pojo.SkuExcelResult;
import cn.atsoft.dasheng.app.pojo.AllBomResult;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.task.entity.AsynTask;
import cn.atsoft.dasheng.task.model.params.AsynTaskParam;
import cn.atsoft.dasheng.task.model.result.AsynTaskResult;
import cn.atsoft.dasheng.task.service.AsynTaskService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 等待任务表控制器
 *
 * @author song
 * @Date 2022-04-01 10:57:49
 */
@RestController
@RequestMapping("/asynTask")
@Api(tags = "等待任务表")
public class AsynTaskController extends BaseController {

    @Autowired
    private AsynTaskService asynTaskService;


    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2022-04-01
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<AsynTaskResult> detail(@RequestBody AsynTaskParam asynTaskParam) {
        AsynTask detail = this.asynTaskService.getById(asynTaskParam.getTaskId());
        AsynTaskResult result = new AsynTaskResult();
        ToolUtil.copyProperties(detail, result);
        switch (result.getType()){
            case "物料分析":
                AllBomResult allBomResult = JSON.parseObject(result.getContent(), AllBomResult.class);
                result.setAllBomResult(allBomResult);
                break;
            case "物料导入":
                SkuExcelResult skuExcelResult = JSON.parseObject(result.getContent(), SkuExcelResult.class);
                result.setSkuExcelResult(skuExcelResult);
                break;
        }
        result.setContent(null);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-04-01
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<AsynTaskResult> list(@RequestBody(required = false) AsynTaskParam asynTaskParam) {
        if (ToolUtil.isEmpty(asynTaskParam)) {
            asynTaskParam = new AsynTaskParam();
        }
        return this.asynTaskService.findPageBySpec(asynTaskParam);
    }


}


