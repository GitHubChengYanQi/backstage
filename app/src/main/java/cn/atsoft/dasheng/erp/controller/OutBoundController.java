package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.app.model.params.OutstockOrderParam;
import cn.atsoft.dasheng.app.model.params.OutstockParam;
import cn.atsoft.dasheng.app.service.OutstockOrderService;
import cn.atsoft.dasheng.app.service.OutstockService;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.erp.entity.OutstockListing;
import cn.atsoft.dasheng.erp.model.params.ApplyDetailsParam;
import cn.atsoft.dasheng.erp.model.params.ItemAttributeParam;
import cn.atsoft.dasheng.erp.model.params.OutstockApplyParam;
import cn.atsoft.dasheng.erp.model.params.OutstockListingParam;
import cn.atsoft.dasheng.erp.service.OutBoundService;
import cn.atsoft.dasheng.model.response.ResponseData;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.TreeSet;

@RestController
@RequestMapping("/outBound")
public class OutBoundController {
    @Autowired
    private OutBoundService outBoundService;
    @Autowired
    private OutstockOrderService outstockOrderService;


    @RequestMapping(value = "/send", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody OutstockOrderParam outstockOrderParam) {

        String judgeOutBound = this.outBoundService.judgeOutBound(outstockOrderParam.getOutstockOrderId(), outstockOrderParam.getStorehouseId());
        outstockOrderService.update(outstockOrderParam);
        return ResponseData.success(judgeOutBound);
    }

    @RequestMapping(value = "/AKeyDelivery", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData AKeyDelivery(@RequestBody OutstockApplyParam outstockApplyParam) {

        String aKeyDelivery = outBoundService.aKeyDelivery(outstockApplyParam);
        return ResponseData.success(aKeyDelivery);
    }
}
