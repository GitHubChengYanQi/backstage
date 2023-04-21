package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.DeliveryDetails;
import cn.atsoft.dasheng.app.model.params.DeliveryDetailsParam;
import cn.atsoft.dasheng.app.model.result.DeliveryDetailsResult;
import cn.atsoft.dasheng.app.service.DeliveryDetailsService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.datascope.DataScope;
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
 * 控制器
 *
 * @author  
 * @Date 2021-08-20 13:14:51
 */
@RestController
@RequestMapping("/deliveryDetails")
@Api(tags = "")
public class DeliveryDetailsController extends BaseController {

    @Autowired
    private DeliveryDetailsService deliveryDetailsService;

    /**
     * 新增接口
     *
     * @author  
     * @Date 2021-08-20
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody DeliveryDetailsParam deliveryDetailsParam) {
        DeliveryDetails add = this.deliveryDetailsService.add(deliveryDetailsParam);
        return ResponseData.success(add);
    }

    /**
     * 编辑接口
     *
     * @author  
     * @Date 2021-08-20
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody DeliveryDetailsParam deliveryDetailsParam) {

        this.deliveryDetailsService.update(deliveryDetailsParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author  
     * @Date 2021-08-20
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody DeliveryDetailsParam deliveryDetailsParam)  {
        this.deliveryDetailsService.delete(deliveryDetailsParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author  
     * @Date 2021-08-20
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody DeliveryDetailsParam deliveryDetailsParam) {
        DeliveryDetails detail = this.deliveryDetailsService.getById(deliveryDetailsParam.getDeliveryDetailsId());
        DeliveryDetailsResult result = new DeliveryDetailsResult();
if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author  
     * @Date 2021-08-20
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo list(@RequestBody(required = false) DeliveryDetailsParam deliveryDetailsParam) {
        if(ToolUtil.isEmpty(deliveryDetailsParam)){
            deliveryDetailsParam = new DeliveryDetailsParam();
        }
//        return this.deliveryDetailsService.findPageBySpec(deliveryDetailsParam);
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.deliveryDetailsService.findPageBySpec(deliveryDetailsParam, null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope(),LoginContextHolder.getContext().getTenantId());
            return this.deliveryDetailsService.findPageBySpec(deliveryDetailsParam, dataScope);
        }
    }
    /**
     * 查询所有列表
     *
     * @author
     * @Date 2021-08-20
     */
    @RequestMapping(value = "/listAll", method = RequestMethod.POST)
    @ApiOperation("列表")
    public List<DeliveryDetailsResult> listAll(@RequestBody(required = false) DeliveryDetailsParam deliveryDetailsParam) {
        if(ToolUtil.isEmpty(deliveryDetailsParam)){
            deliveryDetailsParam = new DeliveryDetailsParam();
        }
        return this.deliveryDetailsService.findListBySpec(deliveryDetailsParam);
    }




}


