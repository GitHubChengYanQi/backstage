package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.CustomerDynamic;
import cn.atsoft.dasheng.app.model.params.CustomerDynamicParam;
import cn.atsoft.dasheng.app.model.result.CustomerDynamicResult;
import cn.atsoft.dasheng.app.service.CustomerDynamicService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 客户动态表控制器
 *
 * @author cheng
 * @Date 2021-08-10 08:33:37
 */
@RestController
@RequestMapping("/customerDynamic")
@Api(tags = "客户动态表")
public class CustomerDynamicController extends BaseController {

    @Autowired
    private CustomerDynamicService customerDynamicService;

    /**
     * 新增接口
     *
     * @author cheng
     * @Date 2021-08-10
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody CustomerDynamicParam customerDynamicParam) {
        this.customerDynamicService.add(customerDynamicParam);
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
    public ResponseData update(@RequestBody CustomerDynamicParam customerDynamicParam) {

        this.customerDynamicService.update(customerDynamicParam);
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
    public ResponseData delete(@RequestBody CustomerDynamicParam customerDynamicParam)  {
        this.customerDynamicService.delete(customerDynamicParam);
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
    public ResponseData detail(@RequestBody CustomerDynamicParam customerDynamicParam) {
        CustomerDynamic detail = this.customerDynamicService.getById(customerDynamicParam.getDynamicId());
        CustomerDynamicResult result = new CustomerDynamicResult();
if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

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
    public PageInfo list(@RequestBody(required = false) CustomerDynamicParam customerDynamicParam) {
        if(ToolUtil.isEmpty(customerDynamicParam)){
            customerDynamicParam = new CustomerDynamicParam();
        }
//        return this.customerDynamicService.findPageBySpec(customerDynamicParam);
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.customerDynamicService.findPageBySpec(customerDynamicParam, null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope(),LoginContextHolder.getContext().getTenantId());
            return this.customerDynamicService.findPageBySpec(customerDynamicParam, dataScope);
        }
    }




}


