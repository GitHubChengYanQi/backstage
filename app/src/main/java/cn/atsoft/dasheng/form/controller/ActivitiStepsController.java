package cn.atsoft.dasheng.form.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.model.params.ActivitiStepsParam;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;
import cn.atsoft.dasheng.form.service.ActivitiStepsService;
import cn.atsoft.dasheng.model.response.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * 流程步骤表控制器
 *
 * @author Sing
 * @Date 2021-11-10 16:53:55
 */
@RestController
@RequestMapping("/activitiStepsS")
@Api(tags = "流程步骤表")
public class ActivitiStepsController extends BaseController {

    @Autowired
    private ActivitiStepsService activitiStepsService;

    /**
     * 新增接口
     *
     * @author Sing
     * @Date 2021-11-10
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ActivitiStepsParam activitiStepsParam) {
        this.activitiStepsService.add(activitiStepsParam);
        return ResponseData.success();
    }
//    @RequestMapping(value = "/addS", method = RequestMethod.POST)
//    @ApiOperation("新增")
//    public ResponseData addS(@RequestBody ActivitiStepsParam activitiStepsParam) {
//        this.activitiStepsService.addSetp(activitiStepsParam);
//        return ResponseData.success();
//    }
//
//
//    /**
//     * 编辑接口
//     *
//     * @author Sing
//     * @Date 2021-11-10
//     */
//    @RequestMapping(value = "/edit", method = RequestMethod.POST)
//    @ApiOperation("编辑")
//    public ResponseData update(@RequestBody ActivitiStepsParam activitiStepsParam) {
//
//        this.activitiStepsService.update(activitiStepsParam);
//        return ResponseData.success();
//    }
//
//    /**
//     * 删除接口
//     *
//     * @author Sing
//     * @Date 2021-11-10
//     */
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    @ApiOperation("删除")
//    public ResponseData delete(@RequestBody ActivitiStepsParam activitiStepsParam) {
//        this.activitiStepsService.delete(activitiStepsParam);
//        return ResponseData.success();
//    }
//
//    /**
//     * 查看详情接口
//     *
//     * @author Sing
//     * @Date 2021-11-10
//     */
//    @RequestMapping(value = "/detail", method = RequestMethod.POST)
//    @ApiOperation("详情")
//    public ResponseData<ActivitiStepsResult> detail(@RequestBody ActivitiStepsParam activitiStepsParam) {
//        ActivitiStepsResult steps = activitiStepsService.getStepResult(activitiStepsParam.getProcessId());
////        ActivitiStepsResult stepsResult = this.activitiStepsService.backStepsResult(activitiStepsParam.getProcessId());
//        return ResponseData.success(steps);
//    }
//
//    /**
//     * 查询列表,
//     *
//     * @author Sing
//     * @Date 2021-11-10
//     */
//    @RequestMapping(value = "/list", method = RequestMethod.POST)
//    @ApiOperation("列表")
//    public PageInfo<ActivitiStepsResult> list(@RequestBody(required = false) ActivitiStepsParam activitiStepsParam) {
//        if (ToolUtil.isEmpty(activitiStepsParam)) {
//            activitiStepsParam = new ActivitiStepsParam();
//        }
//        return this.activitiStepsService.findPageBySpec(activitiStepsParam);
//    }


}


