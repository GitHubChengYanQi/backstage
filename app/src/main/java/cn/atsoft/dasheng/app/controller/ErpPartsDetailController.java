package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.ErpPartsDetail;
import cn.atsoft.dasheng.app.model.params.ErpPartsDetailParam;
import cn.atsoft.dasheng.app.model.result.ErpPartsDetailResult;
import cn.atsoft.dasheng.app.service.ErpPartsDetailService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 清单详情控制器
 *
 * @author cheng
 * @Date 2021-10-26 10:59:07
 */
@RestController
@RequestMapping("/erpPartsDetail")
@Api(tags = "清单详情")
public class ErpPartsDetailController extends BaseController {

    @Autowired
    private ErpPartsDetailService erpPartsDetailService;

    /**
     * 新增接口
     *
     * @author cheng
     * @Date 2021-10-26
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ErpPartsDetailParam erpPartsDetailParam) {
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
    public ResponseData update(@RequestBody ErpPartsDetailParam erpPartsDetailParam) {

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
    public ResponseData delete(@RequestBody ErpPartsDetailParam erpPartsDetailParam)  {
//        this.erpPartsDetailService.delete(erpPartsDetailParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author cheng
     * @Date 2021-10-26
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody ErpPartsDetailParam erpPartsDetailParam) {
        ErpPartsDetail detail = this.erpPartsDetailService.getById(erpPartsDetailParam.getPartsDetailId());
        ErpPartsDetailResult result = new ErpPartsDetailResult();
        ToolUtil.copyProperties(detail, result);

        return ResponseData.success(result);
    }



    @RequestMapping(value = "/bomList", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData bomList(@RequestBody ErpPartsDetailParam erpPartsDetailParam) {
        List<ErpPartsDetailResult> detailResults = this.erpPartsDetailService.bomList(erpPartsDetailParam);
        return ResponseData.success(detailResults);
    }


    /**
     * 查询列表
     *
     * @author cheng
     * @Date 2021-10-26
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ErpPartsDetailResult> list(@RequestBody(required = false) ErpPartsDetailParam erpPartsDetailParam) {
        if(ToolUtil.isEmpty(erpPartsDetailParam)){
            erpPartsDetailParam = new ErpPartsDetailParam();
        }
        return this.erpPartsDetailService.findPageBySpec(erpPartsDetailParam);
    }




}


