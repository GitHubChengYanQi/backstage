package cn.atsoft.dasheng.purchase.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.purchase.entity.ProcurementPlanBind;
import cn.atsoft.dasheng.purchase.model.params.ProcurementPlanBindParam;
import cn.atsoft.dasheng.purchase.model.result.ProcurementPlanBindResult;
import cn.atsoft.dasheng.purchase.service.ProcurementPlanBindService;
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
 * @Date 2021-12-21 15:18:41
 */
@RestController
@RequestMapping("/procurementPlanBind")
@Api(tags = "")
public class ProcurementPlanBindController extends BaseController {

    @Autowired
    private ProcurementPlanBindService procurementPlanBindService;


    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-12-21
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody ProcurementPlanBindParam procurementPlanBindParam) {
        ProcurementPlanBind detail = this.procurementPlanBindService.getById(procurementPlanBindParam.getDetailId());
        ProcurementPlanBindResult result = new ProcurementPlanBindResult();
        ToolUtil.copyProperties(detail, result);

;
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
    public PageInfo<ProcurementPlanBindResult> list(@RequestBody(required = false) ProcurementPlanBindParam procurementPlanBindParam) {
        if(ToolUtil.isEmpty(procurementPlanBindParam)){
            procurementPlanBindParam = new ProcurementPlanBindParam();
        }
        return this.procurementPlanBindService.findPageBySpec(procurementPlanBindParam);
    }




}


