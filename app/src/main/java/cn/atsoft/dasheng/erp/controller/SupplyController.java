package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Supply;
import cn.atsoft.dasheng.erp.model.params.SupplyParam;
import cn.atsoft.dasheng.erp.model.result.SupplyResult;
import cn.atsoft.dasheng.erp.service.SupplyService;
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
 * 供应商供应物料控制器
 *
 * @author song
 * @Date 2021-12-20 10:08:44
 */
@RestController
@RequestMapping("/supply")
@Api(tags = "供应商供应物料")
public class SupplyController extends BaseController {

    @Autowired
    private SupplyService supplyService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-12-20
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody SupplyParam supplyParam) {
        this.supplyService.add(supplyParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-12-20
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody SupplyParam supplyParam) {

        this.supplyService.update(supplyParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-12-20
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody SupplyParam supplyParam)  {
        this.supplyService.delete(supplyParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-12-20
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<SupplyResult> detail(@RequestBody SupplyParam supplyParam) {
        Supply detail = this.supplyService.getById(supplyParam.getSupplyId());
        SupplyResult result = new SupplyResult();
        ToolUtil.copyProperties(detail, result);


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-12-20
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<SupplyResult> list(@RequestBody(required = false) SupplyParam supplyParam) {
        if(ToolUtil.isEmpty(supplyParam)){
            supplyParam = new SupplyParam();
        }
        return this.supplyService.findPageBySpec(supplyParam);
    }




}


