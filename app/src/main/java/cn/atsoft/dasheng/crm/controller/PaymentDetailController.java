package cn.atsoft.dasheng.crm.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.PaymentDetail;
import cn.atsoft.dasheng.crm.model.params.PaymentDetailParam;
import cn.atsoft.dasheng.crm.model.result.PaymentDetailResult;
import cn.atsoft.dasheng.crm.service.PaymentDetailService;
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
 * 付款详情控制器
 *
 * @author song
 * @Date 2022-02-23 09:48:33
 */
@RestController
@RequestMapping("/paymentDetail")
@Api(tags = "付款详情")
public class PaymentDetailController extends BaseController {

    @Autowired
    private PaymentDetailService paymentDetailService;

//    /**
//     * 新增接口
//     *
//     * @author song
//     * @Date 2022-02-23
//     */
//    @RequestMapping(value = "/add", method = RequestMethod.POST)
//    @ApiOperation("新增")
//    public ResponseData addItem(@RequestBody PaymentDetailParam paymentDetailParam) {
//        this.paymentDetailService.add(paymentDetailParam);
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
//    public ResponseData update(@RequestBody PaymentDetailParam paymentDetailParam) {
//
//        this.paymentDetailService.update(paymentDetailParam);
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
//    public ResponseData delete(@RequestBody PaymentDetailParam paymentDetailParam)  {
//        this.paymentDetailService.delete(paymentDetailParam);
//        return ResponseData.success();
//    }
//

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2022-02-23
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody PaymentDetailParam paymentDetailParam) {
        PaymentDetail detail = this.paymentDetailService.getById(paymentDetailParam.getDetailId());
        PaymentDetailResult result = new PaymentDetailResult();
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
    public PageInfo<PaymentDetailResult> list(@RequestBody(required = false) PaymentDetailParam paymentDetailParam) {
        if(ToolUtil.isEmpty(paymentDetailParam)){
            paymentDetailParam = new PaymentDetailParam();
        }
        return this.paymentDetailService.findPageBySpec(paymentDetailParam);
    }




}


