package cn.atsoft.dasheng.form.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.form.entity.ActivitiSetpSetDetail;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessParam;
import cn.atsoft.dasheng.form.model.params.StepsParam;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessResult;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;

import cn.atsoft.dasheng.form.service.ActivitiProcessService;
import cn.atsoft.dasheng.form.service.StepProcessService;
import cn.atsoft.dasheng.form.service.StepsService;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private ActivitiProcessService processService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private UserService userService;

    /**
     * 新增接口
     *
     * @author Sing
     * @Date 2021-11-10
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody StepsParam stepsParam) {
        Long id = this.activitiStepsService.add(stepsParam);
        return ResponseData.success(id);
    }

    @RequestMapping(value = "/shipDetail", method = RequestMethod.GET)
    public ResponseData shipDetail(@Param("id") Long id) {
        ActivitiStepsResult detail = this.activitiStepsService.detail(id);
        return ResponseData.success(detail);
    }

    @RequestMapping(value = "/getSetDetailSByRouId", method = RequestMethod.GET)
    public ResponseData getSetDetailSByRouId(@RequestParam Long id) {
        List<ActivitiSetpSetDetail> setDetailSByRouId = stepProcessService.getSetDetailSByRouId(id);
        return ResponseData.success(setDetailSByRouId);
    }

    @RequestMapping(value = "/shipList", method = RequestMethod.POST)
    public PageInfo<ActivitiProcessResult> shipDetail(@RequestBody(required = false) ActivitiProcessParam processParam) {
        if (ToolUtil.isEmpty(processParam)) {
            processParam = new ActivitiProcessParam();
        }
        processParam.setType("ship");
        PageInfo<ActivitiProcessResult> page = processService.findShipPageBySpec(processParam);
        List<Long> skuIds = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        for (ActivitiProcessResult datum : page.getData()) {
            skuIds.add(datum.getFormId());
            userIds.add(datum.getCreateUser());
        }
        List<User> users = userIds.size() == 0 ? new ArrayList<>() : userService.listByIds(userIds);
        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);
        for (ActivitiProcessResult datum : page.getData()) {
            for (SkuResult skuResult : skuResults) {
                if (skuResult.getSkuId().equals(datum.getFormId())) {
                    datum.setSkuResult(skuResult);
                    break;
                }
            }
            for (User user : users) {
                if (datum.getCreateUser().equals(user.getUserId())) {
                    datum.setUser(user);
                    break;
                }
            }
        }
        return page;
    }

}


