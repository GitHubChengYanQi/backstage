package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.BusinessDynamic;
import cn.atsoft.dasheng.app.model.params.BusinessDynamicParam;
import cn.atsoft.dasheng.app.model.result.BusinessDynamicResult;
import cn.atsoft.dasheng.app.service.BusinessDynamicService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 商机动态表控制器
 *
 * @author cheng
 * @Date 2021-08-10 16:06:26
 */
@RestController
@RequestMapping("/businessDynamic")
@Api(tags = "商机动态表")
public class BusinessDynamicController extends BaseController {

    @Autowired
    private BusinessDynamicService businessDynamicService;

    /**
     * 新增接口
     *
     * @author cheng
     * @Date 2021-08-10
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody BusinessDynamicParam businessDynamicParam) {
        this.businessDynamicService.add(businessDynamicParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author cheng
     * @Date 2021-08-10
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody BusinessDynamicParam businessDynamicParam) {

        this.businessDynamicService.update(businessDynamicParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author cheng
     * @Date 2021-08-10
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody BusinessDynamicParam businessDynamicParam) {
        this.businessDynamicService.delete(businessDynamicParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author cheng
     * @Date 2021-08-10
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody BusinessDynamicParam businessDynamicParam) {
        BusinessDynamic detail = this.businessDynamicService.getById(businessDynamicParam.getDynamicId());
        BusinessDynamicResult result = new BusinessDynamicResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author cheng
     * @Date 2021-08-10
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo list(@RequestBody(required = false) BusinessDynamicParam businessDynamicParam) {
        if (ToolUtil.isEmpty(businessDynamicParam)) {
            businessDynamicParam = new BusinessDynamicParam();
        }
//        return this.businessDynamicService.findPageBySpec(businessDynamicParam);
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.businessDynamicService.findPageBySpec(businessDynamicParam, null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope());
            return this.businessDynamicService.findPageBySpec(businessDynamicParam, dataScope);
        }

    }


}


