package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.model.params.CustomerParam;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
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

@RestController
@RequestMapping("/selfEnterprise")
@Api(tags = "自身企业")
public class SelfEenterpriseController {
    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
//    @Permission
    public PageInfo<CustomerResult> list(@RequestBody(required = false) CustomerParam customerParam) {
        if (ToolUtil.isEmpty(customerParam)) {
            customerParam = new CustomerParam();
        }

        customerParam.setStatus(99);
//        if (LoginContextHolder.getContext().isAdmin()) {
        return this.customerService.findPageBySpec(null,customerParam);
//        }else{
//            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope());
//            PageInfo<CustomerResult> customer= customerService.findPageBySpec(dataScope,customerParam);
//            return this.customerService.findPageBySpec(dataScope,customerParam);
//        }
    }

    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
//    @Permission
    public ResponseData<CustomerResult> detail(@RequestBody(required = false) CustomerParam customerParam) {
        if (ToolUtil.isNotEmpty(customerParam) && ToolUtil.isNotEmpty(customerParam.getCustomerId())) {
            Long customerId = customerParam.getCustomerId();
            CustomerResult detail = customerService.detail(customerId);
            return ResponseData.success(detail);
        }else {
            CustomerResult customerResult = new CustomerResult();
            Customer customer = customerService.query().eq("status", 99).eq("display", 1).one();
            if (ToolUtil.isNotEmpty(customer)){
                customerResult = customerService.detail(customer.getCustomerId());
            }else {
                Customer entity = new Customer();
                entity.setStatus(99);
                entity.setSupply(99);
                this.customerService.save(entity);
                ToolUtil.copyProperties(entity,customerResult);
            }
            return ResponseData.success(customerResult);
        }

    }



}
