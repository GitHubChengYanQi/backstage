package cn.atsoft.dasheng.portal.repair.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.repair.entity.Repair;
import cn.atsoft.dasheng.portal.repair.model.params.RepairParam;
import cn.atsoft.dasheng.portal.repair.model.result.RepairResult;
import cn.atsoft.dasheng.portal.repair.service.RepairService;
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
    public ResponseData addItem(@RequestBody RepairParam repairParam) {
        Long add = this.repairService.add(repairParam);
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
    public ResponseData update(@RequestBody RepairParam repairParam) {

        this.repairService.update(repairParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author siqiang
     * @Date 2021-08-20
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody RepairParam repairParam)  {
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
    public ResponseData<RepairResult> detail(@RequestBody RepairParam repairParam) {
        Repair detail = this.repairService.getById(repairParam.getRepairId());
        RepairResult result = new RepairResult();
        ToolUtil.copyProperties(detail, result);

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author siqiang
     * @Date 2021-08-20
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<RepairResult> list(@RequestBody(required = false) RepairParam repairParam) {
        if(ToolUtil.isEmpty(repairParam)){
            repairParam = new RepairParam();
        }
        return this.repairService.findPageBySpec(repairParam);
    }




}


