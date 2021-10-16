package cn.atsoft.dasheng.api.protal.controller;

import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.portal.dispatChing.entity.Dispatching;
import cn.atsoft.dasheng.portal.dispatChing.model.params.DispatchingParam;
import cn.atsoft.dasheng.portal.dispatChing.service.DispatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class ApiDispatchingController {
    @Autowired
    private DispatchingService dispatchingService;


    @RequestMapping(value = "/saveDispatching", method = RequestMethod.POST)
    public ResponseData saveDispatching(@RequestBody DispatchingParam dispatchingParam) {
        Dispatching dispatching = dispatchingService.lambdaQuery().eq(Dispatching::getRepairId, dispatchingParam.getRepairId()).one();
        if (ToolUtil.isNotEmpty(dispatching)) {
            throw new ServiceException(500, "已派工");
        }
        this.dispatchingService.add(dispatchingParam);
        return ResponseData.success();
    }
}
