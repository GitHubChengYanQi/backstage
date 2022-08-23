package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.entity.ErpPartsDetail;
import cn.atsoft.dasheng.app.model.params.ErpPartsDetailParam;
import cn.atsoft.dasheng.app.model.result.ErpPartsDetailResult;
import cn.atsoft.dasheng.app.service.ErpPartsDetailService;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
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

import java.util.List;


/**
 * 清单详情控制器
 *
 * @author cheng
 * @Date 2021-10-26 10:59:07
 */
@RestController
@RequestMapping("/erpPartsDetail/v1")
@Api(tags = "清单详情")

public class ErpPartsDetailVersionController extends BaseController {

    @Autowired
    private ErpPartsDetailService erpPartsDetailService;


    @RequestMapping(value = "/bomList", method = RequestMethod.POST)
    @ApiVersion("1.1")
    @ApiOperation("详情")
    public ResponseData bomList(@RequestBody ErpPartsDetailParam erpPartsDetailParam) {
        List<ErpPartsDetailResult> detailResults = this.erpPartsDetailService.bomListVersion(erpPartsDetailParam);
        return ResponseData.success(detailResults);
    }


}


