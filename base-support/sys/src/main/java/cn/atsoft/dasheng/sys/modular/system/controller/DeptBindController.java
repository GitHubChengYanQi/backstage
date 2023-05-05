package cn.atsoft.dasheng.sys.modular.system.controller;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.modular.system.entity.DeptBind;
import cn.atsoft.dasheng.sys.modular.system.model.params.DeptBindParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.DeptBindResult;
import cn.atsoft.dasheng.sys.modular.system.service.DeptBindService;
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
 * 各租户内部门绑定表控制器
 *
 * @author Captain_Jazz
 * @Date 2023-05-04 18:31:50
 */
@RestController
@RequestMapping("/deptBind")
@Api(tags = "各租户内部门绑定表")
public class DeptBindController extends BaseController {

    @Autowired
    private DeptBindService deptBindService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2023-05-04
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody DeptBindParam deptBindParam) {
        this.deptBindService.add(deptBindParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2023-05-04
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody DeptBindParam deptBindParam) {
        DeptBind detail = this.deptBindService.getById(deptBindParam.getDeptBindId());
        if (!LoginContextHolder.getContext().getTenantId().equals(detail.getTenantId())){
            throw new ServiceException(500,"您无权操作该数据");
        }
        this.deptBindService.update(deptBindParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2023-05-04
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody DeptBindParam deptBindParam)  {
        this.deptBindService.delete(deptBindParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2023-05-04
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<DeptBindResult> detail(@RequestBody DeptBindParam deptBindParam) {
        DeptBind detail = this.deptBindService.getById(deptBindParam.getDeptBindId());
        if (!LoginContextHolder.getContext().getTenantId().equals(detail.getTenantId())){
            throw new ServiceException(500,"您无权查看该数据");
        }
        DeptBindResult result = new DeptBindResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2023-05-04
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<DeptBindResult> list(@RequestBody(required = false) DeptBindParam deptBindParam) {
        if(ToolUtil.isEmpty(deptBindParam)){
            deptBindParam = new DeptBindParam();
        }
        if (LoginContextHolder.getContext().isAdmin()) {
             return this.deptBindService.findPageBySpec(deptBindParam,null);
        } else {
             DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope(),LoginContextHolder.getContext().getTenantId());
             return  this.deptBindService.findPageBySpec(deptBindParam,dataScope);
        }
//        return this.deptBindService.findPageBySpec(deptBindParam);
    }




}


