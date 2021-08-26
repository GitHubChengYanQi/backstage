package cn.atsoft.dasheng.api.protal.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.portal.wxUser.model.params.WxuserInfoParam;
import cn.atsoft.dasheng.portal.wxUser.model.result.WxuserInfoResult;
import cn.atsoft.dasheng.portal.wxUser.service.WxuserInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiWxUserController {

    @Autowired
    private WxuserInfoService wxuserInfoService;
    @RequestMapping(value = "/getWxUser", method = RequestMethod.POST)
    public PageInfo<WxuserInfoResult> getWxUser(@RequestBody(required = false) WxuserInfoParam wxuserInfoParam) {
        if(ToolUtil.isEmpty(wxuserInfoParam)){
            wxuserInfoParam = new WxuserInfoParam();
        }
        return this.wxuserInfoService.findPageBySpec(wxuserInfoParam);
    }
}
