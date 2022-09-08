package cn.atsoft.dasheng.dynamic.controller;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.dynamic.model.params.DynamicParam;
import cn.atsoft.dasheng.dynamic.model.result.DynamicResult;
import cn.atsoft.dasheng.dynamic.service.DynamicService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dynamic/{v}")
public class DynamicSController extends BaseController {
    @Autowired
    private DynamicService dynamicService;
    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-08-10
     */
    @ApiVersion("1.0.1")
    @RequestMapping(value = "/lsitBySelf", method = RequestMethod.POST)
    @ApiOperation("查看自己动态列表")
    public PageInfo<DynamicResult> lsitBySelf(@RequestBody(required = false) DynamicParam dynamicParam) {
        if(ToolUtil.isEmpty(dynamicParam)){
            dynamicParam = new DynamicParam();
        }
        dynamicParam.setUserId(LoginContextHolder.getContext().getUserId());
        return this.dynamicService.findPageBySpec(dynamicParam);
    }
    @ApiVersion("1.0.1")
    @RequestMapping(value = "/lsit", method = RequestMethod.POST)
    @ApiOperation("条件查看动态列表")
    public PageInfo<DynamicResult> lsit(@RequestBody(required = false) DynamicParam dynamicParam) {
        if(ToolUtil.isEmpty(dynamicParam)){
            dynamicParam = new DynamicParam();
        }
        return this.dynamicService.findPageBySpec(dynamicParam);
    }
}
