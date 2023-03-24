package cn.atsoft.dasheng.audit.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.audit.entity.ActivitiAudit;
import cn.atsoft.dasheng.audit.model.params.ActivitiAuditParam;
import cn.atsoft.dasheng.audit.model.result.ActivitiAuditResult;
import cn.atsoft.dasheng.audit.service.ActivitiAuditService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 审批配置表控制器
 *
 * @author Sing
 * @Date 2021-11-10 16:53:55
 */
@RestController
@RequestMapping("/activitiAudit")
@Api(tags = "审批配置表")
public class ActivitiAuditController extends BaseController {

    @Autowired
    private ActivitiAuditService activitiAuditService;

    /**
     * 新增接口
     *
     * @author Sing
     * @Date 2021-11-10
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ActivitiAuditParam activitiAuditParam) {
        this.activitiAuditService.add(activitiAuditParam);
        return ResponseData.success();
    }


    /**
     * 编辑接口
     *
     * @author Sing
     * @Date 2021-11-10
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ActivitiAuditParam activitiAuditParam) {

        this.activitiAuditService.update(activitiAuditParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Sing
     * @Date 2021-11-10
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ActivitiAuditParam activitiAuditParam) {
        this.activitiAuditService.delete(activitiAuditParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Sing
     * @Date 2021-11-10
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody ActivitiAuditParam activitiAuditParam) {
        ActivitiAudit detail = this.activitiAuditService.getById(activitiAuditParam.getAuditId());
        ActivitiAuditResult result = new ActivitiAuditResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Sing
     * @Date 2021-11-10
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo list(@RequestBody(required = false) ActivitiAuditParam activitiAuditParam) {
        if (ToolUtil.isEmpty(activitiAuditParam)) {
            activitiAuditParam = new ActivitiAuditParam();
        }
        return this.activitiAuditService.findPageBySpec(activitiAuditParam);
    }


}


