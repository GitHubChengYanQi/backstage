package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.MaintenanceCycle;
import cn.atsoft.dasheng.erp.model.params.MaintenanceCycleParam;
import cn.atsoft.dasheng.erp.model.result.MaintenanceCycleResult;
import cn.atsoft.dasheng.erp.service.MaintenanceCycleService;
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
 * 物料维护周期控制器
 *
 * @author Captain_Jazz
 * @Date 2022-07-08 09:53:34
 */
@RestController
@RequestMapping("/maintenanceCycle")
@Api(tags = "物料维护周期")
public class MaintenanceCycleController extends BaseController {

    @Autowired
    private MaintenanceCycleService maintenanceCycleService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2022-07-08
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody MaintenanceCycleParam maintenanceCycleParam) {
        this.maintenanceCycleService.add(maintenanceCycleParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2022-07-08
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody MaintenanceCycleParam maintenanceCycleParam) {

        this.maintenanceCycleService.update(maintenanceCycleParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2022-07-08
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody MaintenanceCycleParam maintenanceCycleParam)  {
        this.maintenanceCycleService.delete(maintenanceCycleParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2022-07-08
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody MaintenanceCycleParam maintenanceCycleParam) {
        MaintenanceCycle detail = this.maintenanceCycleService.getById(maintenanceCycleParam.getMaintenanceCycleId());
        MaintenanceCycleResult result = new MaintenanceCycleResult();
        ToolUtil.copyProperties(detail, result);

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-07-08
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<MaintenanceCycleResult> list(@RequestBody(required = false) MaintenanceCycleParam maintenanceCycleParam) {
        if(ToolUtil.isEmpty(maintenanceCycleParam)){
            maintenanceCycleParam = new MaintenanceCycleParam();
        }
        return this.maintenanceCycleService.findPageBySpec(maintenanceCycleParam);
    }




}


