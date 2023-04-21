package cn.atsoft.dasheng.orderPaymentApply.controller;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.orderPaymentApply.model.params.CrmOrderPaymentApplyParam;
import cn.atsoft.dasheng.orderPaymentApply.service.CrmOrderPaymentApplyService;
import cn.atsoft.dasheng.purchase.model.params.PurchaseListParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("paymentApply")
@Api(tags = "预购管理")
public class OrderPaymentApplyController {

    @Autowired
    private CrmOrderPaymentApplyService crmOrderPaymentApplyService;


    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2023-03-04
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("新增")
    public PageInfo addItem(@RequestBody CrmOrderPaymentApplyParam param) {
        if (LoginContextHolder.getContext().isAdmin()) {
        return this.crmOrderPaymentApplyService.findPageBySpec(null,param);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope(),LoginContextHolder.getContext().getTenantId());
            return this.crmOrderPaymentApplyService.findPageBySpec(dataScope, param);
        }
    }
    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2023-03-04
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData detail(@RequestBody CrmOrderPaymentApplyParam param) {

        return ResponseData.success(this.crmOrderPaymentApplyService.detail(param));
    }
}
