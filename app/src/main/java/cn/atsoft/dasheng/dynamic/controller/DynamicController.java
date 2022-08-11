package cn.atsoft.dasheng.dynamic.controller;

import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.auth.context.LoginContext;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.dynamic.model.result.DynamicResult;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.dynamic.entity.Dynamic;
import cn.atsoft.dasheng.dynamic.model.params.DynamicParam;
import cn.atsoft.dasheng.dynamic.service.DynamicService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 控制器
 *
 * @author Captain_Jazz
 * @Date 2022-08-10 14:38:56
 */
@RestController
@RequestMapping("/dynamic")
@Api(tags = "")
public class DynamicController extends BaseController {

    @Autowired
    private DynamicService dynamicService;

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2022-08-10
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    @Permission
    public ResponseData<DynamicResult> detail(@RequestBody DynamicParam dynamicParam) {
        Dynamic detail = this.dynamicService.getById(dynamicParam.getDynamicId());
        DynamicResult result = new DynamicResult();
        ToolUtil.copyProperties(detail, result);

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-08-10
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    @Permission
    public PageInfo<DynamicResult> list(@RequestBody(required = false) DynamicParam dynamicParam) {
        if(ToolUtil.isEmpty(dynamicParam)){
            dynamicParam = new DynamicParam();
        }
        return this.dynamicService.findPageBySpec(dynamicParam);
    }

 /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-08-10
     */
    @RequestMapping(value = "/lsitBySelf", method = RequestMethod.POST)
    @ApiOperation("列表")
    @Permission
    public PageInfo<DynamicResult> lsitBySelf(@RequestBody(required = false) DynamicParam dynamicParam) {
        if(ToolUtil.isEmpty(dynamicParam)){
            dynamicParam = new DynamicParam();
        }
        dynamicParam.setUserId(LoginContextHolder.getContext().getUserId());
        return this.dynamicService.findPageBySpec(dynamicParam);
    }

 /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-08-10
     */
    @RequestMapping(value = "/ListByUser", method = RequestMethod.POST)
    @ApiOperation("列表")
    @Permission
    public PageInfo<DynamicResult> ListByUser(@RequestBody(required = false) DynamicParam dynamicParam) {
        if(ToolUtil.isEmpty(dynamicParam)){
            dynamicParam = new DynamicParam();
        }
        if (ToolUtil.isEmpty(dynamicParam.getUserId())){
            throw new ServiceException(500,"请选择您要查看的人");
        }
        return this.dynamicService.findPageBySpec(dynamicParam);
    }




}


