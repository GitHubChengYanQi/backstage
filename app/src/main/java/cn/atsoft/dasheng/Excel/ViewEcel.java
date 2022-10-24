package cn.atsoft.dasheng.Excel;

import cn.atsoft.dasheng.Excel.service.InstockViewExcel;
import cn.atsoft.dasheng.Excel.service.OutStockViewExcel;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.erp.model.params.DataStatisticsViewParam;
import cn.atsoft.dasheng.message.enmu.MicroServiceType;
import cn.atsoft.dasheng.message.enmu.OperationType;
import cn.atsoft.dasheng.message.entity.MicroServiceEntity;
import cn.atsoft.dasheng.message.producer.MessageProducer;
import cn.atsoft.dasheng.model.response.ResponseData;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/viewExcel")
public class ViewEcel {

    @Autowired
    private InstockViewExcel instockViewExcel;
    @Autowired
    private OutStockViewExcel outStockViewExcel;
    @Autowired
    private MessageProducer messageProducer;

    @RequestMapping(value = "/export", method = RequestMethod.POST)
    @ApiOperation("导出")
    public ResponseData export(@RequestBody DataStatisticsViewParam param) {
        param.setSendUser(LoginContextHolder.getContext().getUserId());
        MicroServiceEntity microServiceEntity = new MicroServiceEntity();
        microServiceEntity.setOperationType(OperationType.PRINT);
        microServiceEntity.setType(MicroServiceType.INSTOCK_VIEW_EXCEL);
        microServiceEntity.setObject(param);
        microServiceEntity.setMaxTimes(2);
        microServiceEntity.setTimes(0);
        messageProducer.microService(microServiceEntity);
        return ResponseData.success();

    }
    @RequestMapping(value = "/outStockExport", method = RequestMethod.POST)
    @ApiOperation("导出")
    public ResponseData outStockExport(@RequestBody DataStatisticsViewParam param){
        param.setSendUser(LoginContextHolder.getContext().getUserId());
        MicroServiceEntity microServiceEntity = new MicroServiceEntity();
        microServiceEntity.setOperationType(OperationType.PRINT);
        microServiceEntity.setType(MicroServiceType.OUTSTOCK_VIEW_EXCEL);
        microServiceEntity.setObject(param);
        microServiceEntity.setMaxTimes(2);
        microServiceEntity.setTimes(0);
        messageProducer.microService(microServiceEntity);
        return ResponseData.success();
    }



}
