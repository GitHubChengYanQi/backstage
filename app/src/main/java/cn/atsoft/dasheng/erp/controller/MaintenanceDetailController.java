package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.MaintenanceDetail;
import cn.atsoft.dasheng.erp.model.params.MaintenanceDetailParam;
import cn.atsoft.dasheng.erp.model.result.MaintenanceDetailResult;
import cn.atsoft.dasheng.erp.service.MaintenanceDetailService;
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
 * 养护申请子表控制器
 *
 * @author Captain_Jazz
 * @Date 2022-06-28 09:51:55
 */
@RestController
@RequestMapping("/maintenanceDetail")
@Api(tags = "养护申请子表")
public class MaintenanceDetailController extends BaseController {

    @Autowired
    private MaintenanceDetailService maintenanceDetailService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2022-06-28
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody MaintenanceDetailParam maintenanceDetailParam) {
        this.maintenanceDetailService.add(maintenanceDetailParam);
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
    public ResponseData update(@RequestBody MaintenanceDetailParam maintenanceDetailParam) {

        this.maintenanceDetailService.update(maintenanceDetailParam);
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
//    public ResponseData delete(@RequestBody MaintenanceDetailParam maintenanceDetailParam)  {
//        this.maintenanceDetailService.delete(maintenanceDetailParam);
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
    public ResponseData<MaintenanceDetailResult> detail(@RequestBody MaintenanceDetailParam maintenanceDetailParam) {
        MaintenanceDetail detail = this.maintenanceDetailService.getById(maintenanceDetailParam.getMaintenanceDetailId());
        MaintenanceDetailResult result = new MaintenanceDetailResult();
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
    public PageInfo<MaintenanceDetailResult> list(@RequestBody(required = false) MaintenanceDetailParam maintenanceDetailParam) {
        if(ToolUtil.isEmpty(maintenanceDetailParam)){
            maintenanceDetailParam = new MaintenanceDetailParam();
        }
        return this.maintenanceDetailService.findPageBySpec(maintenanceDetailParam);
    }
    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-06-28
     */
    @RequestMapping(value = "/getNeed", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<MaintenanceDetailResult> getNeed(@RequestBody(required = false) MaintenanceDetailParam maintenanceDetailParam) {
        if(ToolUtil.isEmpty(maintenanceDetailParam)){
            maintenanceDetailParam = new MaintenanceDetailParam();
        }
//        return this.maintenanceDetailService.needMaintenance(maintenanceDetailParam);
        return null;
    }





}


