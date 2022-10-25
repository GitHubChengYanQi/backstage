package cn.atsoft.dasheng.crm.controller;

import cn.atsoft.dasheng.app.entity.Contract;
import cn.atsoft.dasheng.app.service.ContractService;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.entity.Order;
import cn.atsoft.dasheng.crm.entity.OrderDetail;
import cn.atsoft.dasheng.crm.entity.Payment;
import cn.atsoft.dasheng.crm.entity.PaymentDetail;
import cn.atsoft.dasheng.crm.model.params.OrderParam;
import cn.atsoft.dasheng.crm.model.result.OrderResult;
import cn.atsoft.dasheng.crm.service.OrderDetailService;
import cn.atsoft.dasheng.crm.service.OrderService;
import cn.atsoft.dasheng.crm.service.PaymentDetailService;
import cn.atsoft.dasheng.crm.service.PaymentService;
import cn.atsoft.dasheng.erp.entity.CodingRules;
import cn.atsoft.dasheng.erp.service.CodingRulesService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


/**
 * 订单表控制器
 *
 * @author song
 * @Date 2022-02-23 09:48:33
 */
@RestController
@RequestMapping("/order")
@Api(tags = "订单表")
public class OrderVersionController extends BaseController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CodingRulesService codingRulesService;
    @Autowired
    private OrderDetailService detailService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentDetailService paymentDetailService;

    @Autowired
    private ContractService contractService;


    /**
     * 新增接口
     *
     * @author song
     * @Date 2022-02-23
     */
    @ApiVersion("1.1")
    @RequestMapping(value = "/{v}/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody OrderParam orderParam) {
        if (ToolUtil.isEmpty(orderParam.getCoding())) {
            CodingRules codingRules = codingRulesService.query().eq("module", "11").eq("state", 1).one();
            if (ToolUtil.isNotEmpty(codingRules)) {
                String coding = codingRulesService.backCoding(codingRules.getCodingRulesId());
                orderParam.setCoding(coding);
            } else {
                throw new ServiceException(500, "请配置采购单据自动生成编码规则");
            }
        }
        /**
         * 对接前段自定义添加
         */

        if (ToolUtil.isNotEmpty(orderParam.getOrderId())) {  //先删除 后添加
            orderService.removeById(orderParam.getOrderId());  //订单
            detailService.remove(new QueryWrapper<OrderDetail>() {{  //订单详情
                eq("order_id", orderParam.getOrderId());
            }});
            contractService.remove(new QueryWrapper<Contract>() {{  //合同
                eq("source_id", orderParam.getOrderId());
            }});

            List<Payment> payments = paymentService.query().eq("order_id", orderParam.getOrderId()).list();  //付款主表
            if (ToolUtil.isNotEmpty(payments)) {
                List<Long> payIds = new ArrayList<>();
                for (Payment payment : payments) {
                    payIds.add(payment.getPaymentId());
                }
                paymentService.removeByIds(payIds);
                paymentDetailService.remove(new QueryWrapper<PaymentDetail>() {{                                    //付款详情表
                    in("payment_id", payIds);
                }});
            }


        }
        Order order = this.orderService.add(orderParam);
        return ResponseData.success(order);
    }


}


