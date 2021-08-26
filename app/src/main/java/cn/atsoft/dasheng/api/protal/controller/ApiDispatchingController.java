package cn.atsoft.dasheng.api.protal.controller;

import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.portal.dispatching.model.params.DispatchingParam;
import cn.atsoft.dasheng.portal.dispatching.service.DispatchingService;
import io.swagger.annotations.ApiOperation;
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
    @RequestMapping(value = "/saveState", method = RequestMethod.POST)
    public ResponseData saveState(@RequestBody DispatchingParam dispatchingParam) {
        this.dispatchingService.update(dispatchingParam);
        return ResponseData.success();
    }
}
