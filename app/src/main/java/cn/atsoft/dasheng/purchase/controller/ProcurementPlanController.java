package cn.atsoft.dasheng.purchase.controller;

import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.purchase.entity.ProcurementPlan;
import cn.atsoft.dasheng.purchase.entity.ProcurementPlanDetal;
import cn.atsoft.dasheng.purchase.model.params.ProcurementPlanParam;
import cn.atsoft.dasheng.purchase.model.result.ProcurementPlanDetalResult;
import cn.atsoft.dasheng.purchase.model.result.ProcurementPlanResult;
import cn.atsoft.dasheng.purchase.model.result.PurchaseQuotationResult;
import cn.atsoft.dasheng.purchase.pojo.ThemeAndOrigin;
import cn.atsoft.dasheng.purchase.service.GetOrigin;
import cn.atsoft.dasheng.purchase.service.ProcurementPlanDetalService;
import cn.atsoft.dasheng.purchase.service.ProcurementPlanService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.purchase.service.PurchaseQuotationService;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
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
    private UserService userService;
    @Autowired
    private GetOrigin getOrigin;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-12-21
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    @Permission
    public ResponseData addItem(@RequestBody ProcurementPlanParam procurementPlanParam) {
        this.procurementPlanService.add(procurementPlanParam);
        return ResponseData.success();
    }


    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-12-21
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    @Permission
    public ResponseData detail(@RequestBody ProcurementPlanParam procurementPlanParam) {
        ProcurementPlan detail = this.procurementPlanService.getById(procurementPlanParam.getProcurementPlanId());
        if (ToolUtil.isEmpty(detail)) {
            return null;
        }
        ProcurementPlanResult result = new ProcurementPlanResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

        User user = userService.getById(result.getCreateUser());
        result.setFounder(user);
        procurementPlanService.detail(result);
        ThemeAndOrigin themeAndOrigin = JSON.parseObject(result.getOrigin(), ThemeAndOrigin.class);
        ThemeAndOrigin origin = getOrigin.getOrigin(themeAndOrigin);
        result.setThemeAndOrigin(origin);
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
    @Permission
    public PageInfo<ProcurementPlanResult> list(@RequestBody(required = false) ProcurementPlanParam procurementPlanParam) {
        if (ToolUtil.isEmpty(procurementPlanParam)) {
            procurementPlanParam = new ProcurementPlanParam();
        }
        return this.procurementPlanService.findPageBySpec(procurementPlanParam);
    }


}


