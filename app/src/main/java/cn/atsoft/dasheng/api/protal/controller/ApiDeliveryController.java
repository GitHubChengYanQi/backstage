package cn.atsoft.dasheng.api.protal.controller;

import cn.atsoft.dasheng.app.model.params.DeliveryDetailsParam;
import cn.atsoft.dasheng.app.model.result.DeliveryDetailsResult;
import cn.atsoft.dasheng.app.service.DeliveryDetailsService;
import cn.atsoft.dasheng.app.service.impl.DeliveryDetailsServiceImpl;
import cn.atsoft.dasheng.core.util.ToolUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiDeliveryController {


    @Autowired
    private DeliveryDetailsService deliveryDetailsService;

    @RequestMapping(value = "/getDeliveryList", method = RequestMethod.POST)
    @ApiOperation("列表")
    public List<DeliveryDetailsResult> listAll(@RequestBody(required = false) DeliveryDetailsParam deliveryDetailsParam) {
        if(ToolUtil.isEmpty(deliveryDetailsParam)){
            deliveryDetailsParam = new DeliveryDetailsParam();
        }
        return this.deliveryDetailsService.findListBySpec(deliveryDetailsParam);
    }

}
