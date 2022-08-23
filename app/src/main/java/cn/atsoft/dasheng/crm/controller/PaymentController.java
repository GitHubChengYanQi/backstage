package cn.atsoft.dasheng.crm.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Payment;
import cn.atsoft.dasheng.crm.model.params.PaymentParam;
import cn.atsoft.dasheng.crm.model.result.PaymentResult;
import cn.atsoft.dasheng.crm.service.PaymentService;
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
 * 付款信息表控制器
 *
 * @author song
 * @Date 2022-02-23 09:48:33
 */
@RestController
@RequestMapping("/payment")
@Api(tags = "付款信息表")
public class PaymentController extends BaseController {

    @Autowired
    private PaymentService paymentService;

//    /**
//     * 新增接口
//     *
//     * @author song
//     * @Date 2022-02-23
//     */
//    @RequestMapping(value = "/add", method = RequestMethod.POST)
//    @ApiOperation("新增")
//    public ResponseData addItem(@RequestBody PaymentParam paymentParam) {
//        this.paymentService.add(paymentParam);
//        return ResponseData.success();
//    }
//
//    /**
//     * 编辑接口
//     *
//     * @author song
//     * @Date 2022-02-23
//     */
//    @RequestMapping(value = "/edit", method = RequestMethod.POST)
//    @ApiOperation("编辑")
//    public ResponseData update(@RequestBody PaymentParam paymentParam) {
//
//        this.paymentService.update(paymentParam);
//        return ResponseData.success();
//    }
//
//    /**
//     * 删除接口
//     *
//     * @author song
//     * @Date 2022-02-23
//     */
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    @ApiOperation("删除")
//    public ResponseData delete(@RequestBody PaymentParam paymentParam)  {
//        this.paymentService.delete(paymentParam);
//        return ResponseData.success();
//    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2022-02-23
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody PaymentParam paymentParam) {
        Payment detail = this.paymentService.getById(paymentParam.getPaymentId());
        PaymentResult result = new PaymentResult();
        ToolUtil.copyProperties(detail, result);


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-02-23
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<PaymentResult> list(@RequestBody(required = false) PaymentParam paymentParam) {
        if(ToolUtil.isEmpty(paymentParam)){
            paymentParam = new PaymentParam();
        }
        return this.paymentService.findPageBySpec(paymentParam);
    }




}


