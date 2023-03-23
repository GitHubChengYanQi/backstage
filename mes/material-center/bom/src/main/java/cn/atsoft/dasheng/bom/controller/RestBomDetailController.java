package cn.atsoft.dasheng.bom.controller;


import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.bom.entity.RestBomDetail;
import cn.atsoft.dasheng.bom.model.params.RestBomDetailParam;
import cn.atsoft.dasheng.bom.model.result.RestBomDetailResult;
import cn.atsoft.dasheng.bom.service.RestBomDetailService;
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
@RequestMapping("/bomDetail/{version}")
@ApiVersion("2.0")
@Api(tags = "清单详情管理")
public class RestBomDetailController extends BaseController {

    @Autowired
    private RestBomDetailService erpPartsDetailService;

    /**
     * 新增接口
     *
     * @author cheng
     * @Date 2021-10-26
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody RestBomDetailParam erpPartsDetailParam) {
        this.erpPartsDetailService.add(erpPartsDetailParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author cheng
     * @Date 2021-10-26
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody RestBomDetailParam erpPartsDetailParam) {

//        this.erpPartsDetailService.update(erpPartsDetailParam);
        return ResponseData.success();
    }




    /**
     * 删除接口
     *
     * @author cheng
     * @Date 2021-10-26
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody RestBomDetailParam erpPartsDetailParam)  {
//        this.erpPartsDetailService.delete(erpPartsDetailParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author cheng
     * @Date 2021-10-26
     */
//    @RequestMapping(value = "/detail", method = RequestMethod.POST)
//    @ApiOperation("详情")
//    public ResponseData detail(@RequestBody RestBomDetailParam erpPartsDetailParam) {
//        RestBomDetail detail = this.erpPartsDetailService.getById(erpPartsDetailParam.getPartsDetailId());
//        RestBomDetailResult result = new RestBomDetailResult();
//        if (ToolUtil.isNotEmpty(detail)) {
//            ToolUtil.copyProperties(detail, result);
//        }
//
//        return ResponseData.success(result);
//    }



//    @RequestMapping(value = "/bomList", method = RequestMethod.POST)
//    @ApiOperation("详情")
//    public ResponseData bomList(@RequestBody RestBomDetailParam erpPartsDetailParam) {
//        List<RestBomDetailResult> detailResults = this.erpPartsDetailService.bomList(erpPartsDetailParam);
//        return ResponseData.success(detailResults);
//    }


    /**
     * 查询列表
     *
     * @author cheng
     * @Date 2021-10-26
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo list(@RequestBody(required = false) RestBomDetailParam erpPartsDetailParam) {
        if(ToolUtil.isEmpty(erpPartsDetailParam)){
            erpPartsDetailParam = new RestBomDetailParam();
        }
        return this.erpPartsDetailService.findPageBySpec(erpPartsDetailParam);
    }




}


