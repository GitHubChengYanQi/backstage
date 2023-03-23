package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.model.result.OutstockRequest;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Delivery;
import cn.atsoft.dasheng.app.model.params.DeliveryParam;
import cn.atsoft.dasheng.app.model.result.DeliveryResult;
import cn.atsoft.dasheng.app.service.DeliveryService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 发货表控制器
 *
 * @author  
 * @Date 2021-08-20 13:14:51
 */
@RestController
@RequestMapping("/delivery")
@Api(tags = "发货表")
public class DeliveryController extends BaseController {

    @Autowired
    private DeliveryService deliveryService;

    /**
     * 新增接口
     *
     * @author  
     * @Date 2021-08-20
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    @Permission
    public ResponseData addItem(@RequestBody DeliveryParam deliveryParam) {
        this.deliveryService.add(deliveryParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author  
     * @Date 2021-08-20
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    @Permission
    public ResponseData update(@RequestBody DeliveryParam deliveryParam) {

        this.deliveryService.update(deliveryParam);
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
    @Permission
    public ResponseData delete(@RequestBody DeliveryParam deliveryParam)  {
        this.deliveryService.delete(deliveryParam);
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
    @Permission
    public ResponseData detail(@RequestBody DeliveryParam deliveryParam) {
        Delivery detail = this.deliveryService.getById(deliveryParam.getDeliveryId());
        DeliveryResult result = new DeliveryResult();
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
    @Permission
    public PageInfo list(@RequestBody(required = false) DeliveryParam deliveryParam) {
        if(ToolUtil.isEmpty(deliveryParam)){
            deliveryParam = new DeliveryParam();
        }
//        return this.deliveryService.findPageBySpec(deliveryParam);
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.deliveryService.findPageBySpec(deliveryParam, null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope());
            return this.deliveryService.findPageBySpec(deliveryParam, dataScope);
        }
    }

    @RequestMapping(value = "/listAll", method = RequestMethod.POST)
    @ApiOperation("所有列表")
    @Permission
    public List<DeliveryResult> listAll(@RequestBody(required = false) DeliveryParam deliveryParam) {
        List<DeliveryResult> listBySpec = deliveryService.findListBySpec(deliveryParam);
        deliveryService.format(listBySpec);
        return listBySpec;
    }

    @RequestMapping(value = "/bulkShipment", method = RequestMethod.POST)
    @ApiOperation("列表")
    @Permission
    public ResponseData bulkShipment(@RequestBody OutstockRequest outstockRequest){
        deliveryService.bulkShipment(outstockRequest);
        return ResponseData.success();
    }

}


