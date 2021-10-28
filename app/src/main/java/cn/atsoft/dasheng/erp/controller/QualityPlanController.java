package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.QualityPlan;
import cn.atsoft.dasheng.erp.entity.QualityPlanDetail;
import cn.atsoft.dasheng.erp.model.params.QualityPlanParam;
import cn.atsoft.dasheng.erp.model.result.QualityPlanDetailResult;
import cn.atsoft.dasheng.erp.model.result.QualityPlanResult;
import cn.atsoft.dasheng.erp.service.QualityPlanDetailService;
import cn.atsoft.dasheng.erp.service.QualityPlanService;
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
 * 质检方案控制器
 *
 * @author Captain_Jazz
 * @Date 2021-10-28 10:29:56
 */
@RestController
@RequestMapping("/qualityPlan")
@Api(tags = "质检方案")
public class QualityPlanController extends BaseController {

    @Autowired
    private QualityPlanService qualityPlanService;
    @Autowired
    private QualityPlanDetailService qualityPlanDetailService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2021-10-28
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody QualityPlanParam qualityPlanParam) {
        this.qualityPlanService.add(qualityPlanParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2021-10-28
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody QualityPlanParam qualityPlanParam) {

        this.qualityPlanService.update(qualityPlanParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2021-10-28
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody QualityPlanParam qualityPlanParam) {
        this.qualityPlanService.delete(qualityPlanParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2021-10-28
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<QualityPlanResult> detail(@RequestBody QualityPlanParam qualityPlanParam) {
        QualityPlan detail = this.qualityPlanService.getById(qualityPlanParam.getQualityPlanId());
        List<QualityPlanDetail> qualityPlanDetails = qualityPlanDetailService.query().in("plan_id", detail.getQualityPlanId()).list();
        List<QualityPlanDetailResult> planDetailResults = new ArrayList<>();
        for (QualityPlanDetail qualityPlanDetail : qualityPlanDetails) {
            QualityPlanDetailResult qualityPlanDetailResult = new QualityPlanDetailResult();
            ToolUtil.copyProperties(qualityPlanDetail, qualityPlanDetailResult);
            planDetailResults.add(qualityPlanDetailResult);
        }

        QualityPlanResult result = new QualityPlanResult();
        ToolUtil.copyProperties(detail, result);
        result.setPlanDetailResults(planDetailResults);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2021-10-28
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<QualityPlanResult> list(@RequestBody(required = false) QualityPlanParam qualityPlanParam) {
        if (ToolUtil.isEmpty(qualityPlanParam)) {
            qualityPlanParam = new QualityPlanParam();
        }
        return this.qualityPlanService.findPageBySpec(qualityPlanParam);
    }


}


