package cn.atsoft.dasheng.api.protal.controller;

import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.entity.Delivery;
import cn.atsoft.dasheng.app.entity.DeliveryDetails;
import cn.atsoft.dasheng.app.model.params.DeliveryDetailsParam;
import cn.atsoft.dasheng.app.model.params.DeliveryParam;
import cn.atsoft.dasheng.app.model.result.DeliveryDetailsResult;
import cn.atsoft.dasheng.app.service.DeliveryDetailsService;
import cn.atsoft.dasheng.app.service.DeliveryService;
import cn.atsoft.dasheng.app.service.impl.DeliveryDetailsServiceImpl;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiDeliveryController {


    @Autowired
    private DeliveryDetailsService deliveryDetailsService;
    @Autowired
    private DeliveryService deliveryService;

    @RequestMapping(value = "/getDeliveryList", method = RequestMethod.POST)
    @ApiOperation("列表")
    public List<DeliveryDetails> getDeliveryList(@RequestBody(required = false) DeliveryParam deliveryParam) {

        Long customerId = deliveryParam.getCustomerId();
        QueryWrapper<Delivery> deliveryQueryWrapper = new QueryWrapper<>();
        deliveryQueryWrapper.in("customer_id",customerId);
        List<Delivery> list = this.deliveryService.list(deliveryQueryWrapper);
        List deliveryIds = new ArrayList<>();
        if(ToolUtil.isNotEmpty(list)){
            for (Delivery data: list){
                deliveryIds.add(data.getDeliveryId());
            }
        }
        QueryWrapper<DeliveryDetails> deliveryDetailQueryWrapper = new QueryWrapper<>();
        deliveryDetailQueryWrapper.in("delivery_id",deliveryIds);
        List<DeliveryDetails> deliveryList = this.deliveryDetailsService.list(deliveryDetailQueryWrapper);
        return  deliveryList;
    }

}
