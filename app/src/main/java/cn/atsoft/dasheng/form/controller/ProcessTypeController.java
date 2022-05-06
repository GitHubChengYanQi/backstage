package cn.atsoft.dasheng.form.controller;

import cn.atsoft.dasheng.form.pojo.ProcessParam;
import cn.atsoft.dasheng.form.pojo.ProcessType;
import cn.atsoft.dasheng.model.response.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/processType")
@Api(tags = "流程类型")
public class ProcessTypeController {
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiOperation("列表")
    public ResponseData addItem() {
        List<Map<String, Object>> maps = ProcessType.enumList();
        return ResponseData.success(maps);
    }
    @RequestMapping(value = "/getModuleEnum", method = RequestMethod.GET)
    public ResponseData getModuleEnum() {
        List<Map<String, Object>> maps = ProcessType.enumList();
        return ResponseData.success(maps);
    }
}
