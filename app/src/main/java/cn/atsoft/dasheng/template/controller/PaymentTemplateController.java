package cn.atsoft.dasheng.template.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.template.entity.PaymentTemplate;
import cn.atsoft.dasheng.template.model.params.PaymentTemplateParam;
import cn.atsoft.dasheng.template.model.result.PaymentTemplateResult;
import cn.atsoft.dasheng.template.service.PaymentTemplateService;
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
 * 付款模板控制器
 *
 * @author song
 * @Date 2022-02-24 10:36:06
 */
@RestController
@RequestMapping("/paymentTemplate")
@Api(tags = "付款模板")
public class PaymentTemplateController extends BaseController {

    @Autowired
    private PaymentTemplateService paymentTemplateService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2022-02-24
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody PaymentTemplateParam paymentTemplateParam) {
        this.paymentTemplateService.add(paymentTemplateParam);
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
    public ResponseData update(@RequestBody PaymentTemplateParam paymentTemplateParam) {

        this.paymentTemplateService.update(paymentTemplateParam);
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
    public ResponseData delete(@RequestBody PaymentTemplateParam paymentTemplateParam)  {
        this.paymentTemplateService.delete(paymentTemplateParam);
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
    public ResponseData<PaymentTemplateResult> detail(@RequestBody PaymentTemplateParam paymentTemplateParam) {
        PaymentTemplate detail = this.paymentTemplateService.getById(paymentTemplateParam.getTemplateId());
        PaymentTemplateResult result = new PaymentTemplateResult();
        ToolUtil.copyProperties(detail, result);

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
    public PageInfo<PaymentTemplateResult> list(@RequestBody(required = false) PaymentTemplateParam paymentTemplateParam) {
        if(ToolUtil.isEmpty(paymentTemplateParam)){
            paymentTemplateParam = new PaymentTemplateParam();
        }
        return this.paymentTemplateService.findPageBySpec(paymentTemplateParam);
    }




}


