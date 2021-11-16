package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.QualityTask;
import cn.atsoft.dasheng.erp.model.params.QualityTaskParam;
import cn.atsoft.dasheng.erp.model.result.QualityTaskResult;
import cn.atsoft.dasheng.erp.service.QualityTaskService;
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
 * 质检任务控制器
 *
 * @author 
 * @Date 2021-11-16 09:54:41
 */
@RestController
@RequestMapping("/qualityTask")
@Api(tags = "质检任务")
public class QualityTaskController extends BaseController {

    @Autowired
    private QualityTaskService qualityTaskService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-11-16
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody QualityTaskParam qualityTaskParam) {
        this.qualityTaskService.add(qualityTaskParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2021-11-16
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody QualityTaskParam qualityTaskParam) {

        this.qualityTaskService.update(qualityTaskParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2021-11-16
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody QualityTaskParam qualityTaskParam)  {
        this.qualityTaskService.delete(qualityTaskParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2021-11-16
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<QualityTaskResult> detail(@RequestBody QualityTaskParam qualityTaskParam) {
        QualityTask detail = this.qualityTaskService.getById(qualityTaskParam.getQualityTaskId());
        QualityTaskResult result = new QualityTaskResult();
        ToolUtil.copyProperties(detail, result);
        qualityTaskService.detailFormat(result);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-11-16
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<QualityTaskResult> list(@RequestBody(required = false) QualityTaskParam qualityTaskParam) {
        if(ToolUtil.isEmpty(qualityTaskParam)){
            qualityTaskParam = new QualityTaskParam();
        }
        return this.qualityTaskService.findPageBySpec(qualityTaskParam);
    }




}


