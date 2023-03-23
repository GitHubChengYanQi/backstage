package cn.atsoft.dasheng.template.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.template.entity.PaymentTemplateDetail;
import cn.atsoft.dasheng.template.model.params.PaymentTemplateDetailParam;
import cn.atsoft.dasheng.template.model.result.PaymentTemplateDetailResult;
import cn.atsoft.dasheng.template.service.PaymentTemplateDetailService;
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
 * 付款模板详情控制器
 *
 * @author song
 * @Date 2022-02-24 10:36:06
 */
@RestController
@RequestMapping("/paymentTemplateDetail")
@Api(tags = "付款模板详情")
public class PaymentTemplateDetailController extends BaseController {

    @Autowired
    private PaymentTemplateDetailService paymentTemplateDetailService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2022-02-24
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody PaymentTemplateDetailParam paymentTemplateDetailParam) {
        this.paymentTemplateDetailService.add(paymentTemplateDetailParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2022-02-24
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody PaymentTemplateDetailParam paymentTemplateDetailParam) {

        this.paymentTemplateDetailService.update(paymentTemplateDetailParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2022-02-24
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody PaymentTemplateDetailParam paymentTemplateDetailParam)  {
        this.paymentTemplateDetailService.delete(paymentTemplateDetailParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2022-02-24
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody PaymentTemplateDetailParam paymentTemplateDetailParam) {
        PaymentTemplateDetail detail = this.paymentTemplateDetailService.getById(paymentTemplateDetailParam.getDetailId());
        PaymentTemplateDetailResult result = new PaymentTemplateDetailResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-02-24
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<PaymentTemplateDetailResult> list(@RequestBody(required = false) PaymentTemplateDetailParam paymentTemplateDetailParam) {
        if(ToolUtil.isEmpty(paymentTemplateDetailParam)){
            paymentTemplateDetailParam = new PaymentTemplateDetailParam();
        }
        return this.paymentTemplateDetailService.findPageBySpec(paymentTemplateDetailParam);
    }




}


