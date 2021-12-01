package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.config.MobileConfig;
import cn.atsoft.dasheng.erp.config.MobileService;
import cn.atsoft.dasheng.erp.entity.OutstockListing;
import cn.atsoft.dasheng.erp.entity.QualityTask;
import cn.atsoft.dasheng.erp.entity.QualityTaskBind;
import cn.atsoft.dasheng.erp.entity.QualityTaskDetail;
import cn.atsoft.dasheng.erp.model.params.OutstockListingParam;
import cn.atsoft.dasheng.erp.model.params.QualityTaskDetailParam;
import cn.atsoft.dasheng.erp.model.params.QualityTaskParam;
import cn.atsoft.dasheng.erp.model.request.FormDataPojo;
import cn.atsoft.dasheng.erp.model.result.OutstockListingResult;
import cn.atsoft.dasheng.erp.model.result.QualityTaskDetailResult;
import cn.atsoft.dasheng.erp.model.result.QualityTaskResult;
import cn.atsoft.dasheng.erp.model.result.TaskCount;
import cn.atsoft.dasheng.erp.service.QualityTaskBindService;
import cn.atsoft.dasheng.erp.service.QualityTaskDetailService;
import cn.atsoft.dasheng.erp.service.QualityTaskService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.entity.*;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessTaskResult;
import cn.atsoft.dasheng.form.model.result.FormDataResult;
import cn.atsoft.dasheng.form.service.*;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;


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

    @Autowired
    private QualityTaskDetailService qualityTaskDetailService;

    @Autowired
    private FormDataService formDataService;

    @Autowired
    private QualityTaskBindService qualityTaskBindService;


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
     * 编辑接口
     *
     * @author
     * @Date 2021-11-16
     */
    @RequestMapping(value = "/checkOver", method = RequestMethod.POST)

    @ApiOperation("编辑")
    public ResponseData checkOver(@RequestBody QualityTaskParam qualityTaskParam) {

        this.qualityTaskService.checkOver(qualityTaskParam);
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
    public ResponseData delete(@RequestBody QualityTaskParam qualityTaskParam) {
        this.qualityTaskService.delete(qualityTaskParam);
        return ResponseData.success();
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
        if (ToolUtil.isEmpty(qualityTaskParam)) {
            qualityTaskParam = new QualityTaskParam();
        }
        return this.qualityTaskService.findPageBySpec(qualityTaskParam);
    }

    /**
     * 查询列表
     *
     * @author
     * @Date 2021-11-16
     */
    @RequestMapping(value = "/formDetail", method = RequestMethod.POST)
    @ApiOperation("列表")
    public List<FormDataResult> formDetail(@RequestBody(required = false) QualityTaskParam param) {
        if (ToolUtil.isEmpty(param)) {
            param = new QualityTaskParam();
        }
        List<QualityTaskBind> binds = qualityTaskBindService.lambdaQuery().eq(QualityTaskBind::getQualityTaskId, param.getQualityTaskId()).and(i -> i.eq(QualityTaskBind::getDisplay, 1)).list();
        List<Long> inkindIds = new ArrayList<>();
        for (QualityTaskBind bind : binds) {
            inkindIds.add(bind.getInkindId());
        }
        List<FormData> formDatas = inkindIds.size() == 0 ? new ArrayList<>() : formDataService.lambdaQuery().in(FormData::getFormId, inkindIds).and(i -> i.eq(FormData::getDisplay, 1)).list();
        List<FormDataResult> formDataResults = new ArrayList<>();
        if (ToolUtil.isNotEmpty(formDatas)) {
            for (FormData formData : formDatas) {
                FormDataResult formDataResult = new FormDataResult();
                ToolUtil.copyProperties(formData, formDataResult);
//            qualityTaskService.formDataFormat(formDataResult);
                formDataResults.add(formDataResult);
            }
            qualityTaskService.formDataFormat1(formDataResults);
        }


        return formDataResults;
    }


    /**
     * 查看详情接口
     *
     * @author cheng
     * @Date 2021-09-15
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<QualityTaskResult> detail(@RequestBody QualityTaskParam qualityTaskParam) {
        QualityTask details = this.qualityTaskService.getById(qualityTaskParam.getQualityTaskId());
        QualityTaskResult result = new QualityTaskResult();
        ToolUtil.copyProperties(details, result);
        return ResponseData.success(result);
    }


    /**
     * 添加data
     *
     * @author
     * @Date 2021-11-16
     */
    @RequestMapping(value = "/addData", method = RequestMethod.POST)
    public ResponseData addData(@RequestBody FormDataPojo formDataPojo) {


    this.qualityTaskService.addFormData(formDataPojo);
        return ResponseData.success();
    }

    /**
     * 判断质检项数量
     *
     * @author
     * @Date 2021-11-16
     */
    @RequestMapping(value = "/backInkind", method = RequestMethod.GET)
    public ResponseData backInkind(@RequestParam Long id) {
        List<TaskCount> taskCounts = this.qualityTaskService.backIkind(id);
        return ResponseData.success(taskCounts);
    }


}


