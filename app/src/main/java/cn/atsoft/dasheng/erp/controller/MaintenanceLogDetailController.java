package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.MaintenanceLogDetail;
import cn.atsoft.dasheng.erp.model.params.MaintenanceLogDetailParam;
import cn.atsoft.dasheng.erp.model.result.MaintenanceLogDetailResult;
import cn.atsoft.dasheng.erp.service.MaintenanceLogDetailService;
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
 * @author Captain_Jazz
 * @Date 2022-08-11 13:42:31
 */
@RestController
@RequestMapping("/maintenanceLogDetail")
@Api(tags = "")
public class MaintenanceLogDetailController extends BaseController {

    @Autowired
    private MaintenanceLogDetailService maintenanceLogDetailService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody MaintenanceLogDetailParam maintenanceLogDetailParam) {
        this.maintenanceLogDetailService.add(maintenanceLogDetailParam);
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
    public ResponseData update(@RequestBody MaintenanceLogDetailParam maintenanceLogDetailParam) {

        this.maintenanceLogDetailService.update(maintenanceLogDetailParam);
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
    public ResponseData delete(@RequestBody MaintenanceLogDetailParam maintenanceLogDetailParam)  {
        this.maintenanceLogDetailService.delete(maintenanceLogDetailParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<MaintenanceLogDetailResult> detail(@RequestBody MaintenanceLogDetailParam maintenanceLogDetailParam) {
        MaintenanceLogDetail detail = this.maintenanceLogDetailService.getById(maintenanceLogDetailParam.getMaintenanceLogDetailId());
        MaintenanceLogDetailResult result = new MaintenanceLogDetailResult();
        ToolUtil.copyProperties(detail, result);

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-08-11
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<MaintenanceLogDetailResult> list(@RequestBody(required = false) MaintenanceLogDetailParam maintenanceLogDetailParam) {
        if(ToolUtil.isEmpty(maintenanceLogDetailParam)){
            maintenanceLogDetailParam = new MaintenanceLogDetailParam();
        }
        return this.maintenanceLogDetailService.findPageBySpec(maintenanceLogDetailParam);
    }




}


