package cn.atsoft.dasheng.form.controller;

import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.form.model.ModelProcessDao;
import cn.atsoft.dasheng.form.model.enums.ModelEnum;
import cn.atsoft.dasheng.form.pojo.ProcessType;
import cn.atsoft.dasheng.form.service.ActivitiProcessService;
import cn.atsoft.dasheng.form.service.ModelService;
import cn.atsoft.dasheng.model.response.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/processType/{version}")
@Api(tags = "流程类型")
@ApiVersion("2.0")
public class RestProcessTypeController {

    @Autowired
    private ModelService modelService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiOperation("列表")
    public ResponseData addItem() {
        List<ModelProcessDao> list = modelService.list();
        return ResponseData.success(list);
    }

    @RequestMapping(value = "/getByModel", method = RequestMethod.GET)
    public ResponseData getModuleEnum(@RequestParam ModelEnum model) {

        return ResponseData.success(modelService.getByModel(model));
    }


    @RequestMapping(value = "/getFormByModel", method = RequestMethod.GET)
    public ResponseData getFormByModel(@RequestParam ModelEnum model) {

        return ResponseData.success(modelService.getFormByModel(model));
    }

}
