package cn.atsoft.dasheng.controller;

import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.core.util.ToolUtil;
//import cn.atsoft.dasheng.erp.model.params.RestTraceabilityParam;
//import cn.atsoft.dasheng.erp.model.result.RestTraceabilityResult;
//import cn.atsoft.dasheng.erp.service.RestTraceabilityService;
import cn.atsoft.dasheng.model.params.RestTraceabilityParam;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.model.result.RestTraceabilityResult;
import cn.atsoft.dasheng.service.RestTraceabilityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 实物表控制器
 *
 * @author song
 * @Date 2021-11-01 14:31:45
 */
@RestController
@RequestMapping("/inkind/{version}")
@Api(tags = "实物表")
@ApiVersion("2.0")
public class RestTraceabilityController extends BaseController {

    @Autowired
    private RestTraceabilityService RestTraceabilityService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-11-01
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody RestTraceabilityParam RestTraceabilityParam) {
        this.RestTraceabilityService.add(RestTraceabilityParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-11-01
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @BussinessLog(value = "修改实物", key = "name", dict = RestTraceabilityParam.class)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody RestTraceabilityParam RestTraceabilityParam) {

        this.RestTraceabilityService.update(RestTraceabilityParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-11-01
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @BussinessLog(value = "删除实物", key = "name", dict = RestTraceabilityParam.class)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody RestTraceabilityParam RestTraceabilityParam) {
        this.RestTraceabilityService.delete(RestTraceabilityParam);
        return ResponseData.success();
    }

//    /**
//     * 查看详情接口
//     *
//     * @author song
//     * @Date 2021-11-01
//     */
//    @RequestMapping(value = "/detail", method = RequestMethod.POST)
//    @ApiOperation("详情")
//    public ResponseData detail(@RequestBody RestTraceabilityParam RestTraceabilityParam) {
//        RestTraceabilityResult result = this.RestTraceabilityService.RestTraceabilityDetail(RestTraceabilityParam);
//        return ResponseData.success(result);
//    }

//
//    /**
//     * 查看详情接口
//     *
//     * @author song
//     * @Date 2021-11-01
//     */
//    @RequestMapping(value = "/details", method = RequestMethod.POST)
//    @ApiOperation("详情")
//    public ResponseData details(@RequestBody RestTraceabilityParam RestTraceabilityParam) {
//        List<RestTraceabilityResult> RestTraceabilityResults = this.RestTraceabilityService.details(RestTraceabilityParam);
//        return ResponseData.success(RestTraceabilityResults);
//    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-11-01
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<RestTraceabilityResult> list(@RequestBody(required = false) RestTraceabilityParam RestTraceabilityParam) {
        if (ToolUtil.isEmpty(RestTraceabilityParam)) {
            RestTraceabilityParam = new RestTraceabilityParam();
        }
        return this.RestTraceabilityService.findPageBySpec(RestTraceabilityParam);
    }



//    @RequestMapping(value = "/stockRestTraceability", method = RequestMethod.POST)
//    @ApiOperation("库存实物列表")
//    public PageInfo<RestTraceabilityResult> stockRestTraceability(@RequestBody(required = false) RestTraceabilityParam RestTraceabilityParam) {
//        if (ToolUtil.isEmpty(RestTraceabilityParam)) {
//            RestTraceabilityParam = new RestTraceabilityParam();
//        }
//        return this.RestTraceabilityService.stockRestTraceability(RestTraceabilityParam);
//    }

}


