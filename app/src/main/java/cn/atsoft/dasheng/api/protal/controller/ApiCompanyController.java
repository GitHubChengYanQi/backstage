package cn.atsoft.dasheng.api.protal.controller;

import cn.atsoft.dasheng.app.model.params.CustomerParam;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.portal.wxUser.model.params.WxuserInfoParam;
import cn.atsoft.dasheng.portal.wxUser.model.result.WxuserInfoResult;
import cn.atsoft.dasheng.portal.wxUser.service.WxuserInfoService;
import cn.atsoft.dasheng.uc.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiCompanyController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private WxuserInfoService wxuserInfoService;

    @RequestMapping(value = "/getCompannyList", method = RequestMethod.POST)
    public PageInfo<CustomerResult> list(@RequestBody(required = false) CustomerParam customerParam) {
        WxuserInfoParam wxuserInfoParam = new WxuserInfoParam();
        wxuserInfoParam.setUserId(UserUtils.getUserId());

        PageInfo<WxuserInfoResult> res = wxuserInfoService.findPageBySpec(wxuserInfoParam);
        if(ToolUtil.isNotEmpty(res)){
            if (ToolUtil.isEmpty(customerParam)) {
                customerParam = new CustomerParam();
            }
            return customerService.findPageBySpec(customerParam);
        }else{
            return null;
        }
    }
}