package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.QualityTaskDetail;
import cn.atsoft.dasheng.erp.model.params.QualityTaskDetailParam;
import cn.atsoft.dasheng.erp.model.result.QualityTaskDetailResult;
import cn.atsoft.dasheng.erp.pojo.TaskDetail;
import cn.atsoft.dasheng.erp.service.QualityTaskDetailService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.model.result.FormDataValueResult;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;


/**
 * 质检任务详情控制器
 *
 * @author
 * @Date 2021-11-16 09:54:41
 */
@RestController
@RequestMapping("/qualityTaskDetail")
@Api(tags = "质检任务详情")
public class QualityTaskDetailController extends BaseController {

    @Autowired
    private QualityTaskDetailService qualityTaskDetailService;

    /**
     * 新增接口
     *
     * @author
     * @Date 2021-11-16
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody QualityTaskDetailParam qualityTaskDetailParam) {
        this.qualityTaskDetailService.add(qualityTaskDetailParam);
        return ResponseData.success();
    }




    /**
     * 编辑接口
     *
     * @author
     * @Date 2021-11-16
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @BussinessLog(value = "修改质检任务详情", key = "name", dict = QualityTaskDetailParam.class)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody QualityTaskDetailParam qualityTaskDetailParam) {

        this.qualityTaskDetailService.update(qualityTaskDetailParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author
     * @Date 2021-11-16
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @BussinessLog(value = "删除质检任务详情", key = "name", dict = QualityTaskDetailParam.class)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody QualityTaskDetailParam qualityTaskDetailParam) {
        this.qualityTaskDetailService.delete(qualityTaskDetailParam);
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
    public ResponseData detail(@RequestBody QualityTaskDetailParam qualityTaskDetailParam) {
        QualityTaskDetail detail = this.qualityTaskDetailService.getById(qualityTaskDetailParam.getQualityTaskDetailId());
        QualityTaskDetailResult result = new QualityTaskDetailResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

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
    public PageInfo<QualityTaskDetailResult> list(@RequestBody(required = false) QualityTaskDetailParam qualityTaskDetailParam) {
        if (ToolUtil.isEmpty(qualityTaskDetailParam)) {
            qualityTaskDetailParam = new QualityTaskDetailParam();
        }
        return this.qualityTaskDetailService.findPageBySpec(qualityTaskDetailParam);
    }


    /**
     * 分派任务
     *
     * @author
     * @Date 2021-11-16
     */
    @RequestMapping(value = "/addDetails", method = RequestMethod.POST)
    @ApiOperation("分派任务")
    public ResponseData addDetails(@RequestBody QualityTaskDetailParam qualityTaskDetailParam) {
        this.qualityTaskDetailService.addDetails(qualityTaskDetailParam);
        return ResponseData.success();
    }


    /**
     * 查看当前详情所有任务
     *
     * @author
     * @Date 2021-11-16
     */
    @RequestMapping(value = "/getDetailResults", method = RequestMethod.GET)
    @ApiOperation("分派任务")
    public ResponseData getDetailResults(@Param("id") Long id) {
        TaskDetail detailResults = this.qualityTaskDetailService.getDetailResults(id);
        return ResponseData.success(detailResults);
    }

    /**
     * @author
     * @Date 2021-11-16
     */
    @RequestMapping(value = "/backValue", method = RequestMethod.GET)
    @ApiOperation("详情")
    public ResponseData backValue(@Param("inkind") Long inkind) {
        List<FormDataValueResult> dataValueResults = this.qualityTaskDetailService.backData(inkind);

        return ResponseData.success(dataValueResults);
    }
}


