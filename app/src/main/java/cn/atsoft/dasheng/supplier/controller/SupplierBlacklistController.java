package cn.atsoft.dasheng.supplier.controller;

import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.supplier.entity.SupplierBlacklist;
import cn.atsoft.dasheng.supplier.model.params.SupplierBlacklistParam;
import cn.atsoft.dasheng.supplier.model.result.SupplierBlacklistResult;
import cn.atsoft.dasheng.supplier.service.SupplierBlacklistService;
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
 * 供应商黑名单控制器
 *
 * @author Captian_Jazz
 * @Date 2021-12-20 11:20:05
 */
@RestController
@RequestMapping("/supplierBlacklist")
@Api(tags = "供应商黑名单")
public class SupplierBlacklistController extends BaseController {

    @Autowired
    private SupplierBlacklistService supplierBlacklistService;

    /**
     * 新增接口
     *
     * @author Captian_Jazz
     * @Date 2021-12-20
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    @Permission
    public ResponseData addItem(@RequestBody SupplierBlacklistParam supplierBlacklistParam) {
        this.supplierBlacklistService.add(supplierBlacklistParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captian_Jazz
     * @Date 2021-12-20
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    @Permission
    public ResponseData update(@RequestBody SupplierBlacklistParam supplierBlacklistParam) {

        this.supplierBlacklistService.update(supplierBlacklistParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captian_Jazz
     * @Date 2021-12-20
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    @Permission
    public ResponseData delete(@RequestBody SupplierBlacklistParam supplierBlacklistParam)  {
        this.supplierBlacklistService.delete(supplierBlacklistParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captian_Jazz
     * @Date 2021-12-20
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    @Permission
    public ResponseData detail(@RequestBody SupplierBlacklistParam supplierBlacklistParam) {
        SupplierBlacklist detail = this.supplierBlacklistService.getById(supplierBlacklistParam.getBlackListId());
        SupplierBlacklistResult result = new SupplierBlacklistResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captian_Jazz
     * @Date 2021-12-20
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    @Permission
    public PageInfo<SupplierBlacklistResult> list(@RequestBody(required = false) SupplierBlacklistParam supplierBlacklistParam) {
        if(ToolUtil.isEmpty(supplierBlacklistParam)){
            supplierBlacklistParam = new SupplierBlacklistParam();
        }
        return this.supplierBlacklistService.findPageBySpec(supplierBlacklistParam);
    }




}


