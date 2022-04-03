package cn.atsoft.dasheng.erp.controller;


import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.QualityTask;
import cn.atsoft.dasheng.erp.entity.QualityTaskBind;
import cn.atsoft.dasheng.erp.entity.QualityTaskDetail;
import cn.atsoft.dasheng.erp.model.params.InstockOrderParam;
import cn.atsoft.dasheng.erp.model.params.QualityTaskParam;
import cn.atsoft.dasheng.erp.model.params.SelfQualityParam;
import cn.atsoft.dasheng.erp.model.request.FormDataPojo;
import cn.atsoft.dasheng.erp.model.request.FormValues;
import cn.atsoft.dasheng.erp.model.result.InstockRequest;
import cn.atsoft.dasheng.erp.model.result.QualityTaskResult;
import cn.atsoft.dasheng.erp.model.result.TaskCount;
import cn.atsoft.dasheng.erp.pojo.FormDataRequest;
import cn.atsoft.dasheng.erp.pojo.QualityTaskChild;
import cn.atsoft.dasheng.erp.pojo.TaskComplete;
import cn.atsoft.dasheng.erp.service.InstockOrderService;
import cn.atsoft.dasheng.erp.service.QualityTaskBindService;
import cn.atsoft.dasheng.erp.service.QualityTaskDetailService;
import cn.atsoft.dasheng.erp.service.QualityTaskService;
import cn.atsoft.dasheng.form.entity.FormData;
import cn.atsoft.dasheng.form.model.result.FormDataResult;
import cn.atsoft.dasheng.form.service.FormDataService;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.orCode.BindParam;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.util.NumberUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.DecimalFormat;
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
    private InstockOrderService instockOrderService;

    @Autowired
    private FormDataService formDataService;

    @Autowired
    private QualityTaskBindService qualityTaskBindService;

    @Autowired
    private UserService userService;


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
     * 查询task
     *
     * @author
     * @Date 2021-11-16
     */
    @RequestMapping(value = "/getTask", method = RequestMethod.GET)
    public ResponseData getTask(@Param("id") Long id) {
        QualityTaskResult qualityTaskResult = this.qualityTaskService.getTask(id);
        return ResponseData.success(qualityTaskResult);
    }

    /**
     * 修改dataValue
     *
     * @author
     * @Date 2021-11-16
     */
    @RequestMapping(value = "/updateDataValue", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData updateDataValue(@RequestBody FormValues formValues) {
        this.qualityTaskService.updateDataValue(formValues);
        return ResponseData.success();
    }


    /**
     * 子任务拒绝
     *
     * @author
     * @Date 2021-11-16
     */
    @RequestMapping(value = "/childRefuse", method = RequestMethod.POST)
    @ApiOperation("子任务拒绝")
    public ResponseData childRefuse(@RequestBody QualityTaskParam qualityTaskParam) {
        this.qualityTaskService.childRefuse(qualityTaskParam);
        return ResponseData.success();
    }


    /**
     * 入库显示
     *
     * @author
     * @Date 2021-11-16
     */
    @RequestMapping(value = "/inStockDetail", method = RequestMethod.POST)
    public ResponseData inStockDetail(@RequestBody BindParam bindParam) {
        List<FormDataResult> dataResults = this.qualityTaskService.inStockDetail(bindParam);
        return ResponseData.success(dataResults);
    }

    /**
     * 详情
     *
     * @author
     * @Date 2021-11-16
     */
    @RequestMapping(value = "/getDetail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData getDetail(@RequestBody BindParam bindParam) {
        List<FormDataResult> dataResults = this.qualityTaskService.getDetail(bindParam);
        return ResponseData.success(dataResults);
    }


    @RequestMapping(value = "/getChilds", method = RequestMethod.GET)
    public ResponseData getChilds(@Param("id") Long id) {
        List<QualityTaskResult> childs = qualityTaskService.getChilds(id);
        return ResponseData.success(childs);
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


    @RequestMapping(value = "/taskComplete", method = RequestMethod.POST)
    @ApiOperation("质检完成接口")
    public ResponseData taskComplete(@RequestBody @Valid TaskComplete taskComplete) {
        List<Long> longs = this.qualityTaskService.taskComplete(taskComplete);
        return ResponseData.success(longs);
    }

    /**
     * 添加子任务
     *
     * @author
     * @Date 2021-11-16
     */
    @RequestMapping(value = "/addChild", method = RequestMethod.POST)

    @ApiOperation("添加子任务")
    public ResponseData addChild(@RequestBody QualityTaskChild child) {
        this.qualityTaskService.addChild(child);
        return ResponseData.success();
    }


    /**
     * 查询子任务
     *
     * @author
     * @Date 2021-11-16
     */
    @RequestMapping(value = "/backDataValue", method = RequestMethod.GET)

    @ApiOperation("查询子任务")
    public ResponseData backDataValue(@Param("id") Long id, @Param("type") String type) {
        FormDataRequest formDataRequest = this.qualityTaskService.valueResults(id, type);
        return ResponseData.success(formDataRequest);
    }

    /**
     * 返回子任务
     *
     * @author
     * @Date 2021-11-16
     */
    @RequestMapping(value = "/backChildTask", method = RequestMethod.GET)
    @ApiOperation("返回子任务")
    public ResponseData backChildTask(@Param("id") Long id) {

        QualityTaskResult childTask = this.qualityTaskService.backChildTask(id);
        return ResponseData.success(childTask);
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
        if (ToolUtil.isEmpty(details)) {
            return null;
        }
        QualityTaskResult result = new QualityTaskResult();
        ToolUtil.copyProperties(details, result);
        User createUser = userService.getById(result.getCreateUser());
        result.setCreateName(createUser.getName());
        if (ToolUtil.isNotEmpty(result.getUserIds()) && !result.getUserIds().equals("")) {
            String[] split = result.getUserIds().split(",");

            List<User> users = userService.query().in("user_id", split).list();
            List<String> userName = new ArrayList<>();

            for (User user : users) {
                userName.add(user.getName());
            }

            result.setNames(userName);
        }
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


    /**
     * 更新子任务
     *
     * @author
     * @Date 2021-11-16
     */
    @RequestMapping(value = "/updateChildTask", method = RequestMethod.GET)
    public ResponseData updateChildTask(@Param("id") Long id, @Param("state") Integer state) {
        this.qualityTaskService.updateChildTask(id, state);
        return ResponseData.success();
    }

    /**
     * 质检入库
     */
    @RequestMapping(value = "/qualityDetailInstock", method = RequestMethod.POST)
    public ResponseData qualityDetailInstock(@RequestBody InstockOrderParam instockOrderParam) {

        List<QualityTaskDetail> qualityTaskDetails = new ArrayList<>();
        // 记录已经入库的数量
        for (InstockRequest instockRequest : instockOrderParam.getInstockRequest()) {
            QualityTaskDetail qualityTaskDetail = new QualityTaskDetail();
            qualityTaskDetail.setQualityTaskDetailId(instockRequest.getQualityTaskDetailId());
            qualityTaskDetail.setInstockNumber(instockRequest.getInstockNumber() + instockRequest.getNumber());
            qualityTaskDetails.add(qualityTaskDetail);
        }
        qualityTaskDetailService.updateBatchById(qualityTaskDetails);

        instockOrderService.add(instockOrderParam);

        return ResponseData.success();
    }
    @RequestMapping(value = "/selfQuality", method = RequestMethod.POST)
    public ResponseData selfQuality(@RequestBody SelfQualityParam selfQualityParam) {

        return ResponseData.success(this.qualityTaskService.selfQuality(selfQualityParam));
    }
 @RequestMapping(value = "/saveSelfQuality", method = RequestMethod.POST)
    public ResponseData saveSelfQuality(@RequestBody FormDataPojo formDataPojo) {
     this.qualityTaskService.saveMyQuality(formDataPojo);
        return ResponseData.success();
    }


}


