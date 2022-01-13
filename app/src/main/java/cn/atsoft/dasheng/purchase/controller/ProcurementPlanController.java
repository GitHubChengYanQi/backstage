package cn.atsoft.dasheng.purchase.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.purchase.entity.ProcurementPlan;
import cn.atsoft.dasheng.purchase.entity.ProcurementPlanDetal;
import cn.atsoft.dasheng.purchase.model.params.ProcurementPlanParam;
import cn.atsoft.dasheng.purchase.model.result.ProcurementPlanDetalResult;
import cn.atsoft.dasheng.purchase.model.result.ProcurementPlanResult;
import cn.atsoft.dasheng.purchase.service.ProcurementPlanDetalService;
import cn.atsoft.dasheng.purchase.service.ProcurementPlanService;
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
 * 采购计划主表控制器
 *
 * @author song
 * @Date 2021-12-21 15:18:41
 */
@RestController
@RequestMapping("/procurementPlan")
@Api(tags = "采购计划主表")
public class ProcurementPlanController extends BaseController {

    @Autowired
    private ProcurementPlanService procurementPlanService;

    @Autowired
    private ProcurementPlanDetalService procurementPlanDetalService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-12-21
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ProcurementPlanParam procurementPlanParam) {
        this.procurementPlanService.add(procurementPlanParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-12-21
     */
//    @RequestMapping(value = "/edit", method = RequestMethod.POST)
//    @ApiOperation("编辑")
//    public ResponseData update(@RequestBody ProcurementPlanParam procurementPlanParam) {
//
//        this.procurementPlanService.update(procurementPlanParam);
//        return ResponseData.success();
//    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-12-21
     */
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    @ApiOperation("删除")
//    public ResponseData delete(@RequestBody ProcurementPlanParam procurementPlanParam)  {
//        this.procurementPlanService.delete(procurementPlanParam);
//        return ResponseData.success();
//    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-12-21
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<ProcurementPlanResult> detail(@RequestBody ProcurementPlanParam procurementPlanParam) {
        ProcurementPlan detail = this.procurementPlanService.getById(procurementPlanParam.getProcurementPlanId());
        if (ToolUtil.isEmpty(detail)){
            return null;
        }
        ProcurementPlanResult result = new ProcurementPlanResult();
        ToolUtil.copyProperties(detail, result);

        List<ProcurementPlanDetal> procurementPlanDetals = procurementPlanDetalService.lambdaQuery().eq(ProcurementPlanDetal::getPlanId, detail.getProcurementPlanId()).list();
        List<ProcurementPlanDetalResult> detalResultList = new ArrayList<>();
        for (ProcurementPlanDetal procurementPlanDetal : procurementPlanDetals) {
            ProcurementPlanDetalResult procurementPlanDetalResult = new ProcurementPlanDetalResult();
            ToolUtil.copyProperties(procurementPlanDetal,procurementPlanDetalResult);
            detalResultList.add(procurementPlanDetalResult);
        }

        result.setDetalResults(detalResultList);

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-12-21
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ProcurementPlanResult> list(@RequestBody(required = false) ProcurementPlanParam procurementPlanParam) {
        if(ToolUtil.isEmpty(procurementPlanParam)){
            procurementPlanParam = new ProcurementPlanParam();
        }
        return this.procurementPlanService.findPageBySpec(procurementPlanParam);
    }




}


