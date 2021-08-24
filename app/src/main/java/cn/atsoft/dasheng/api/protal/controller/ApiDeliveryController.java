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
    public List<DeliveryDetailsResult> getDeliveryList(@RequestBody(required = false) DeliveryParam deliveryParam) {

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
        List<DeliveryDetailsResult> rtnList = new ArrayList<>();

        List<DeliveryDetails> deliveryList = this.deliveryDetailsService.list();
        for(DeliveryDetails data : deliveryList){
            if(deliveryIds.contains(data.getDeliveryId())){
                DeliveryDetailsResult res = new DeliveryDetailsResult();
                res.setDeliveryDetailsId(data.getDeliveryDetailsId());
                res.setItemId(data.getItemId());
                res.setStockItemId(data.getStockItemId());
                res.setStage(data.getStage());
                res.setQualityType(data.getQualityType());
                res.setDeliveryId(data.getDeliveryId());
                res.setBrandId(data.getBrandId());
                res.setCreateTime(data.getCreateTime());
                rtnList.add(res);
            }
        }
        deliveryDetailsService.format(rtnList);
        return  rtnList;
    }

}
