package cn.atsoft.dasheng.purchase.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.purchase.entity.ProcurementPlanDetal;
import cn.atsoft.dasheng.purchase.model.params.ProcurementPlanDetalParam;
import cn.atsoft.dasheng.purchase.model.result.ProcurementPlanDetalResult;
import cn.atsoft.dasheng.purchase.service.ProcurementPlanDetalService;
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
 * 采购计划单子表整合数据后的子表控制器
 *
 * @author song
 * @Date 2021-12-21 15:18:41
 */
@RestController
@RequestMapping("/procurementPlanDetal")
@Api(tags = "采购计划单子表整合数据后的子表")
public class ProcurementPlanDetalController extends BaseController {

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
    public ResponseData addItem(@RequestBody ProcurementPlanDetalParam procurementPlanDetalParam) {
        this.procurementPlanDetalService.add(procurementPlanDetalParam);
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
//    public ResponseData update(@RequestBody ProcurementPlanDetalParam procurementPlanDetalParam) {
//
//        this.procurementPlanDetalService.update(procurementPlanDetalParam);
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
//    public ResponseData delete(@RequestBody ProcurementPlanDetalParam procurementPlanDetalParam)  {
//        this.procurementPlanDetalService.delete(procurementPlanDetalParam);
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
    public ResponseData detail(@RequestBody ProcurementPlanDetalParam procurementPlanDetalParam) {
        ProcurementPlanDetal detail = this.procurementPlanDetalService.getById(procurementPlanDetalParam.getDetailId());
        ProcurementPlanDetalResult result = new ProcurementPlanDetalResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }


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
    public PageInfo<ProcurementPlanDetalResult> list(@RequestBody(required = false) ProcurementPlanDetalParam procurementPlanDetalParam) {
        if(ToolUtil.isEmpty(procurementPlanDetalParam)){
            procurementPlanDetalParam = new ProcurementPlanDetalParam();
        }
        return this.procurementPlanDetalService.findPageBySpec(procurementPlanDetalParam);
    }




}


