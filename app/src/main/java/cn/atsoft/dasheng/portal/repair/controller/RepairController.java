package cn.atsoft.dasheng.portal.repair.controller;

import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.repair.entity.Repair;
import cn.atsoft.dasheng.portal.repair.model.params.RepairParam;
import cn.atsoft.dasheng.portal.repair.model.result.RepairResult;
import cn.atsoft.dasheng.portal.repair.service.RepairService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 报修控制器
 *
 * @author siqiang
 * @Date 2021-08-20 17:11:20
 */
@RestController
@RequestMapping("/repair")
@Api(tags = "报修")
public class RepairController extends BaseController {

    @Autowired
    private RepairService repairService;


    /**
     * 新增接口
     *
     * @author siqiang
     * @Date 2021-08-20
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    @Permission
    public ResponseData addItem(@RequestBody RepairParam repairParam) throws WxErrorException {
        Repair add = this.repairService.add(repairParam);
        return ResponseData.success(add);
    }

    /**
     * 编辑接口
     *
     * @author siqiang
     * @Date 2021-08-20
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    @Permission
    public ResponseData update(@RequestBody RepairParam repairParam) throws WxErrorException {
        Repair update = this.repairService.update(repairParam);
        return ResponseData.success(update);
    }

    @RequestMapping(value = "/editdy", method = RequestMethod.POST)
    @ApiOperation("编辑")
    @Permission
    public ResponseData updatedynamic(@RequestBody RepairParam repairParam) {
        String updatedynamic = repairService.updatedynamic(repairParam);
        return ResponseData.success(updatedynamic);
    }


    /**
     * 删除接口
     *
     * @author siqiang
     * @Date 2021-08-20
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    @Permission
    public ResponseData delete(@RequestBody RepairParam repairParam) {
        this.repairService.delete(repairParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author siqiang
     * @Date 2021-08-20
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    @Permission
    public ResponseData detail(@RequestBody RepairParam repairParam) {
        Long result = repairParam.getRepairId();
        RepairResult repairResult = repairService.detail(result);
        return ResponseData.success(repairResult);
    }

    /**
     * 查询列表
     *
     * @author siqiang
     * @Date 2021-08-20
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    @Permission
    public PageInfo<RepairResult> list(@RequestBody(required = false) RepairParam repairParam) {
        if (ToolUtil.isEmpty(repairParam)) {
            repairParam = new RepairParam();
        }
        return this.repairService.findPageBySpec(repairParam);
    }



}


