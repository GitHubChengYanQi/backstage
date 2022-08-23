package cn.atsoft.dasheng.api.protal.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.binding.wxUser.model.params.WxuserInfoParam;
import cn.atsoft.dasheng.binding.wxUser.model.result.WxuserInfoResult;
import cn.atsoft.dasheng.binding.wxUser.service.WxuserInfoService;
import cn.atsoft.dasheng.uc.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiWxUserController {

    @Autowired
    private WxuserInfoService wxuserInfoService;
    @RequestMapping(value = "/getWxUser", method = RequestMethod.POST)
    public ResponseData getWxUser() {
        WxuserInfoParam wxuserInfoParam = new WxuserInfoParam();
        wxuserInfoParam.setMemberId(UserUtils.getUserId());
        PageInfo list = this.wxuserInfoService.findPageBySpec(wxuserInfoParam);
        if(ToolUtil.isNotEmpty(list)){
            return  ResponseData.success(list);
        }else{
            return  ResponseData.success();
        }

    }
}
