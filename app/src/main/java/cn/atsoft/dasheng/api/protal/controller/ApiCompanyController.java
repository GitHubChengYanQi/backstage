package cn.atsoft.dasheng.api.protal.controller;

import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.app.wrapper.CustomerSelectWrapper;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.portal.companyaddress.model.params.CompanyAddressParam;
import cn.atsoft.dasheng.portal.companyaddress.service.CompanyAddressService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiCompanyController {

    @Autowired
    private CompanyAddressService companyAddressService;
    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = "/getCompannyList", method = RequestMethod.GET)
    @ApiOperation("Select数据接口")
    public ResponseData<List<Map<String, Object>>> listSelect() {
        QueryWrapper<Customer> queryWrapper = new QueryWrapper();
        queryWrapper.in("display",1);
        List<Map<String, Object>> list = this.customerService.listMaps(queryWrapper);
        CustomerSelectWrapper customerSelectWrapper = new CustomerSelectWrapper(list);
        List<Map<String, Object>> result = customerSelectWrapper.wrap();
        return ResponseData.success(result);
    }

}