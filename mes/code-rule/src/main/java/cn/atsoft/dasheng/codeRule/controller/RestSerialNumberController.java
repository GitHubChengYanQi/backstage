package cn.atsoft.dasheng.codeRule.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.codeRule.entity.RestSerialNumber;
import cn.atsoft.dasheng.codeRule.model.params.RestSerialNumberParam;
import cn.atsoft.dasheng.codeRule.model.result.RestSerialNumberResult;
import cn.atsoft.dasheng.codeRule.service.RestSerialNumberService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * 流水号控制器
 *
 * @author 
 * @Date 2021-11-04 18:59:26
 */
@RestController
@RequestMapping("/serialNumber/{version}")
@ApiVersion("2.0")
@Api(tags = "流水号 管理")
public class RestSerialNumberController extends BaseController {

    @Autowired
    private RestSerialNumberService serialNumberService;

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2021-11-04
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody RestSerialNumberParam serialNumberParam) {
        RestSerialNumber detail = this.serialNumberService.getById(serialNumberParam.getSerialId());
        RestSerialNumberResult result = new RestSerialNumberResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

        return ResponseData.success(result);

    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-11-04
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<RestSerialNumberResult> list(@RequestBody(required = false) RestSerialNumberParam serialNumberParam) {
        if(ToolUtil.isEmpty(serialNumberParam)){
            serialNumberParam = new RestSerialNumberParam();
        }
        return this.serialNumberService.findPageBySpec(serialNumberParam);
    }




}


