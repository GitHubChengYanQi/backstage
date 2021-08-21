package cn.atsoft.dasheng.api.protal.controller;

import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.portal.companyaddress.model.params.CompanyAddressParam;
import cn.atsoft.dasheng.portal.companyaddress.service.CompanyAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiCompanyController {

    @Autowired
    private CompanyAddressService companyAddressService;

    @RequestMapping(value = "/saveCompanny", method = RequestMethod.POST)
    public ResponseData saveCompanny(@RequestBody CompanyAddressParam companyAddressParam) {
        this.companyAddressService.add(companyAddressParam);
        return ResponseData.success();
    }
}