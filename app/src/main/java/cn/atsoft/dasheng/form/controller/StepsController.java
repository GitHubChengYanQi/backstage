package cn.atsoft.dasheng.form.controller;

import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.form.model.params.ActivitiStepsParam;
import cn.atsoft.dasheng.form.model.result.ActivitiSetpSetResult;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;
import cn.atsoft.dasheng.form.pojo.MoneyTypeEnum;
import cn.atsoft.dasheng.form.service.StepProcessService;
import cn.atsoft.dasheng.form.service.StepsService;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.util.EnumUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.bouncycastle.cms.PasswordRecipientId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


/**
 * 流程步骤表控制器
 *
 * @author Sing
 * @Date 2021-11-10 16:53:55
 */
@RestController
@RequestMapping("/shipRoute")
@Api(tags = "流程步骤表")
public class StepsController extends BaseController {

    @Autowired
    private StepsService activitiStepsService;
    @Autowired
    private StepProcessService stepProcessService;

    /**
     * 新增接口
     *
     * @author Sing
     * @Date 2021-11-10
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ActivitiStepsParam activitiStepsParam) {
        Long id = this.activitiStepsService.add(activitiStepsParam);
        return ResponseData.success(id);
    }

    @RequestMapping(value = "/shipDetail", method = RequestMethod.GET)
    public ResponseData shipDetail(@Param("id") Long id) {
        ActivitiStepsResult detail = this.activitiStepsService.detail(id);
        return ResponseData.success(detail);
    }



}


