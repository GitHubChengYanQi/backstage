package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.app.model.params.OutstockOrderParam;
import cn.atsoft.dasheng.app.model.params.OutstockParam;
import cn.atsoft.dasheng.app.service.OutstockService;
import cn.atsoft.dasheng.erp.entity.OutstockListing;
import cn.atsoft.dasheng.erp.model.params.ApplyDetailsParam;
import cn.atsoft.dasheng.erp.model.params.OutstockListingParam;
import cn.atsoft.dasheng.erp.service.OutBoundService;
import cn.atsoft.dasheng.model.response.ResponseData;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/outBound")
public class OutBoundController {
    @Autowired
    private OutBoundService outBoundService;
    @Autowired
    private OutstockService outstockService;


    @RequestMapping(value = "/send", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody OutstockOrderParam outstockOrderParam) {

        String judgeOutBound = this.outBoundService.judgeOutBound(outstockOrderParam.getOutstockOrderId(), outstockOrderParam.getStorehouseId());
        OutstockParam outstockParam = new OutstockParam();
        outstockParam.setOutstockOrderId(outstockOrderParam.getOutstockOrderId());
//        outstockParam.setu
        outstockService.update();
        return ResponseData.success(judgeOutBound);
    }
}
