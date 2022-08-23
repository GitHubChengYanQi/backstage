package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.model.result.PackageRequest;
import cn.atsoft.dasheng.app.wrapper.ErpPackageSelectWrapper;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.ErpPackage;
import cn.atsoft.dasheng.app.model.params.ErpPackageParam;
import cn.atsoft.dasheng.app.model.result.ErpPackageResult;
import cn.atsoft.dasheng.app.service.ErpPackageService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.Map;


/**
 * 套餐表控制器
 *
 * @author qr
 * @Date 2021-08-04 11:01:43
 */
@RestController
@RequestMapping("/erpPackage")
@Api(tags = "套餐表")
public class ErpPackageController extends BaseController {

    @Autowired
    private ErpPackageService erpPackageService;

    /**
     * 新增接口
     *
     * @author qr
     * @Date 2021-08-04
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ErpPackageParam erpPackageParam) {
        Long packageId = this.erpPackageService.add(erpPackageParam);
        return ResponseData.success(packageId);
    }

    /**
     * 编辑接口
     *
     * @author qr
     * @Date 2021-08-04
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ErpPackageParam erpPackageParam) {
        this.erpPackageService.update(erpPackageParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author qr
     * @Date 2021-08-04
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ErpPackageParam erpPackageParam) {
        this.erpPackageService.delete(erpPackageParam);
        return ResponseData.success();
    }

    /**
     * 批量删除接口
     *
     * @author qr
     * @Date 2021-08-04
     */
    @RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
    @ApiOperation("批量删除")
    public ResponseData batchDelete(@RequestBody PackageRequest request) {
        this.erpPackageService.batchDelete(request.getPackageId());
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author qr
     * @Date 2021-08-04
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody ErpPackageParam erpPackageParam) {
        ErpPackage detail = this.erpPackageService.getById(erpPackageParam.getPackageId());
        ErpPackageResult result = new ErpPackageResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author qr
     * @Date 2021-08-04
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ErpPackageResult> list(@RequestBody(required = false) ErpPackageParam erpPackageParam) {
        if (ToolUtil.isEmpty(erpPackageParam)) {
            erpPackageParam = new ErpPackageParam();
        }
//        return this.erpPackageService.findPageBySpec(erpPackageParam);
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.erpPackageService.findPageBySpec(erpPackageParam, null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope());
            return this.erpPackageService.findPageBySpec(erpPackageParam, dataScope);
        }
    }


    /**
     * 选择列表
     *
     * @author 1
     * @Date 2021-07-14
     */
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData listSelect() {
        QueryWrapper<ErpPackage> packageQueryWrapper = new QueryWrapper<>();
        packageQueryWrapper.in("display", 1);
        List<Map<String, Object>> list = this.erpPackageService.listMaps(packageQueryWrapper);
        ErpPackageSelectWrapper factory = new ErpPackageSelectWrapper(list);
        List<Map<String, Object>> result = factory.wrap();
        return ResponseData.success(result);
    }


}


