package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.MaintenanceLog;
import cn.atsoft.dasheng.erp.model.params.MaintenanceLogParam;
import cn.atsoft.dasheng.erp.model.result.MaintenanceLogResult;
import cn.atsoft.dasheng.erp.service.MaintenanceLogService;
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
 * 养护记录控制器
 *
 * @author Captain_Jazz
 * @Date 2022-08-11 13:42:31
 */
@RestController
@RequestMapping("/maintenanceLog")
@Api(tags = "养护记录")
public class MaintenanceLogController extends BaseController {

    @Autowired
    private MaintenanceLogService maintenanceLogService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody MaintenanceLogParam maintenanceLogParam) {
        this.maintenanceLogService.add(maintenanceLogParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody MaintenanceLogParam maintenanceLogParam) {

        this.maintenanceLogService.update(maintenanceLogParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody MaintenanceLogParam maintenanceLogParam)  {
        this.maintenanceLogService.delete(maintenanceLogParam);
        return ResponseData.success();
    }

//    /**
//     * 查看详情接口
//     *
//     * @author Captain_Jazz
//     * @Date 2022-08-11
//     */
//    @RequestMapping(value = "/detail", method = RequestMethod.POST)
//    @ApiOperation("详情")
//    public ResponseData detail(@RequestBody MaintenanceLogParam maintenanceLogParam) {
//        MaintenanceLog detail = this.maintenanceLogService.getById(maintenanceLogParam.getMaintenanceLogId());
//        MaintenanceLogResult result = new MaintenanceLogResult();
//        ToolUtil.copyProperties(detail, result);
//
//        return ResponseData.success(result);
//    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<MaintenanceLogResult> list(@RequestBody(required = false) MaintenanceLogParam maintenanceLogParam) {
        if(ToolUtil.isEmpty(maintenanceLogParam)){
            maintenanceLogParam = new MaintenanceLogParam();
        }
        return this.maintenanceLogService.findPageBySpec(maintenanceLogParam);
    }
    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @ApiOperation("列表")
    public MaintenanceLogResult detail(@RequestParam Long id) {

        return this.maintenanceLogService.detail(id);
    }




}


