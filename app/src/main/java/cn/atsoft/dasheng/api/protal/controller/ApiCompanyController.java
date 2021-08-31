package cn.atsoft.dasheng.api.protal.controller;

import cn.atsoft.dasheng.app.model.params.CustomerParam;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.portal.wxUser.entity.WxuserInfo;
import cn.atsoft.dasheng.portal.wxUser.model.params.WxuserInfoParam;
import cn.atsoft.dasheng.portal.wxUser.model.result.WxuserInfoResult;
import cn.atsoft.dasheng.portal.wxUser.service.WxuserInfoService;
import cn.atsoft.dasheng.uc.utils.UserUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        Long userId = null;
        QueryWrapper<WxuserInfo> wxuserInfoQueryWrapper = new QueryWrapper<>();
        wxuserInfoQueryWrapper.in("member_id", UserUtils.getUserId());
        List<WxuserInfo> userList = wxuserInfoService.list(wxuserInfoQueryWrapper);
        for(WxuserInfo data : userList){
            userId = data.getUserId();
            break;
        }
        wxuserInfoParam.setUserId(userId);

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