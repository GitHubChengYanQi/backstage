package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.ApplyDetails;
import cn.atsoft.dasheng.erp.model.params.ApplyDetailsParam;
import cn.atsoft.dasheng.erp.model.result.ApplyDetailsResult;
import cn.atsoft.dasheng.erp.service.ApplyDetailsService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.sys.core.constant.dictmap.RoleDict;
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
 * @Date 2021-09-15 09:42:47
 */
@RestController
@RequestMapping("/applyDetails")
@Api(tags = "")
public class ApplyDetailsController extends BaseController {

    @Autowired
    private ApplyDetailsService applyDetailsService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-09-15
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")

    public ResponseData addItem(@RequestBody ApplyDetailsParam applyDetailsParam) {
        this.applyDetailsService.add(applyDetailsParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-09-15
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @BussinessLog(value = "修改发货申请详情", key = "name", dict = ApplyDetailsParam.class)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ApplyDetailsParam applyDetailsParam) {

        this.applyDetailsService.update(applyDetailsParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-09-15
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    @BussinessLog(value = "删除发货申请详情", key = "name", dict = ApplyDetailsParam.class)
    public ResponseData delete(@RequestBody ApplyDetailsParam applyDetailsParam) {
        this.applyDetailsService.delete(applyDetailsParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-09-15
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody ApplyDetailsParam applyDetailsParam) {
        ApplyDetails detail = this.applyDetailsService.getById(applyDetailsParam.getOutstockApplyDetailsId());
        ApplyDetailsResult result = new ApplyDetailsResult();
        ToolUtil.copyProperties(detail, result);

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-09-15
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ApplyDetailsResult> list(@RequestBody(required = false) ApplyDetailsParam applyDetailsParam) {
        if (ToolUtil.isEmpty(applyDetailsParam)) {
            applyDetailsParam = new ApplyDetailsParam();
        }
        return this.applyDetailsService.findPageBySpec(applyDetailsParam);
    }

}


