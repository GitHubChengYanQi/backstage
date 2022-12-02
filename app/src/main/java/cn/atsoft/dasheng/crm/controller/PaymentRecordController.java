package cn.atsoft.dasheng.crm.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.PaymentRecord;
import cn.atsoft.dasheng.crm.model.params.PaymentRecordParam;
import cn.atsoft.dasheng.crm.model.result.PaymentRecordResult;
import cn.atsoft.dasheng.crm.service.PaymentRecordService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import javafx.beans.binding.ObjectExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 付款记录控制器
 *
 * @author song
 * @Date 2022-03-01 13:56:31
 */
@RestController
@RequestMapping("/paymentRecord")
@Api(tags = "付款记录")
public class PaymentRecordController extends BaseController {

    @Autowired
    private PaymentRecordService paymentRecordService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2022-03-01
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody @Valid PaymentRecordParam paymentRecordParam) {
        Object add = this.paymentRecordService.add(paymentRecordParam);
        return ResponseData.success(add);
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2022-03-01
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody PaymentRecordParam paymentRecordParam) {

        this.paymentRecordService.update(paymentRecordParam);
        return ResponseData.success();
    }
//
//    /**
//     * 删除接口
//     *
//     * @author song
//     * @Date 2022-03-01
//     */
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    @ApiOperation("删除")
//    public ResponseData delete(@RequestBody PaymentRecordParam paymentRecordParam)  {
//        this.paymentRecordService.delete(paymentRecordParam);
//        return ResponseData.success();
//    }
    /**
     * 状态作废接口
     */
    @RequestMapping(value = "/obsolete",method = RequestMethod.POST)
    @ApiOperation("作废")
    public ResponseData obsolete(@RequestBody PaymentRecordParam paymentRecordParam) {
        this.paymentRecordService.obsolete(paymentRecordParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2022-03-01
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody PaymentRecordParam paymentRecordParam) {
        PaymentRecord detail = this.paymentRecordService.getById(paymentRecordParam.getRecordId());
        PaymentRecordResult result = new PaymentRecordResult();
        ToolUtil.copyProperties(detail, result);
        paymentRecordService.format(new ArrayList<PaymentRecordResult>(){{
            add(result);
        }});

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-03-01
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<PaymentRecordResult> list(@RequestBody(required = false) PaymentRecordParam paymentRecordParam) {
        if (ToolUtil.isEmpty(paymentRecordParam)) {
            paymentRecordParam = new PaymentRecordParam();
        }
        return this.paymentRecordService.findPageBySpec(paymentRecordParam);
    }


}


