package cn.atsoft.dasheng.crm.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Plan;
import cn.atsoft.dasheng.crm.model.params.PlanParam;
import cn.atsoft.dasheng.crm.model.result.PlanResult;
import cn.atsoft.dasheng.crm.service.PlanService;
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
 * 控制器
 *
 * @author song
 * @Date 2021-09-14 14:36:34
 */
@RestController
@RequestMapping("/plan")
@Api(tags = "")
public class PlanController extends BaseController {

    @Autowired
    private PlanService planService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-09-14
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody PlanParam planParam) {
        this.planService.add(planParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-09-14
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody PlanParam planParam) {

        this.planService.update(planParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-09-14
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody PlanParam planParam)  {
        this.planService.delete(planParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-09-14
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody PlanParam planParam) {
        Plan detail = this.planService.getById(planParam.getSalesProcessPlanId());
        PlanResult result = new PlanResult();
        ToolUtil.copyProperties(detail, result);


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-09-14
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<PlanResult> list(@RequestBody(required = false) PlanParam planParam) {
        if(ToolUtil.isEmpty(planParam)){
            planParam = new PlanParam();
        }
        return this.planService.findPageBySpec(planParam);
    }




}


