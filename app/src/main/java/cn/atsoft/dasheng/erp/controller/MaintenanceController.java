package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Maintenance;
import cn.atsoft.dasheng.erp.model.params.MaintenanceParam;
import cn.atsoft.dasheng.erp.model.result.MaintenanceResult;
import cn.atsoft.dasheng.erp.service.MaintenanceService;
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
 * 养护申请主表控制器
 *
 * @author Captain_Jazz
 * @Date 2022-06-28 09:41:30
 */
@RestController
@RequestMapping("/maintenance")
@Api(tags = "养护申请主表")
public class MaintenanceController extends BaseController {

    @Autowired
    private MaintenanceService maintenanceService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2022-06-28
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody MaintenanceParam maintenanceParam) {
        this.maintenanceService.add(maintenanceParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2022-06-28
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody MaintenanceParam maintenanceParam) {

        this.maintenanceService.update(maintenanceParam);
        return ResponseData.success();
    }

//    /**
//     * 删除接口
//     *
//     * @author Captain_Jazz
//     * @Date 2022-06-28
//     */
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    @ApiOperation("删除")
//    public ResponseData delete(@RequestBody MaintenanceParam maintenanceParam)  {
//        this.maintenanceService.delete(maintenanceParam);
//        return ResponseData.success();
//    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2022-06-28
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<MaintenanceResult> detail(@RequestBody MaintenanceParam maintenanceParam) {
        Maintenance detail = this.maintenanceService.getById(maintenanceParam.getMaintenanceId());
        MaintenanceResult result = new MaintenanceResult();
        ToolUtil.copyProperties(detail, result);

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-06-28
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<MaintenanceResult> list(@RequestBody(required = false) MaintenanceParam maintenanceParam) {
        if(ToolUtil.isEmpty(maintenanceParam)){
            maintenanceParam = new MaintenanceParam();
        }
        return this.maintenanceService.findPageBySpec(maintenanceParam);
    }




}


