package cn.atsoft.dasheng.form.controller;


import cn.atsoft.dasheng.binding.wxUser.entity.WxuserInfo;
import cn.atsoft.dasheng.binding.wxUser.service.WxuserInfoService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.pojo.UserList;
import cn.atsoft.dasheng.form.service.StepsService;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.uc.entity.UcOpenUserInfo;
import cn.atsoft.dasheng.uc.service.UcOpenUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/formUser")
public class UserController extends BaseController {
    @Autowired
    private StepsService stepsService;
    @Autowired
    private WxuserInfoService wxuserInfoService;
    @Autowired
    private UcOpenUserInfoService openUserInfoService;

    @RequestMapping(value = "/userList", method = RequestMethod.POST)
    public ResponseData userList(@RequestBody(required = false) User user) {
        if (ToolUtil.isEmpty(user)) {
            user = new User();
        }
        List<UserList> userLists = stepsService.userLists(user.getName());
        return ResponseData.success(userLists);
    }
    private void format(List<UserList> users){
        List<Long> userIds = new ArrayList<>();
        for (UserList user : users) {
            WxuserInfo infoList = wxuserInfoService.query().eq("user_id", user.getUserId()).eq("source", "wxCp").last("limit 1").one();
            if (ToolUtil.isNotEmpty(infoList)) {
                UcOpenUserInfo userInfo = openUserInfoService.query().eq("member_id", infoList.getMemberId()).eq("source", "wxCp").one();
                if (ToolUtil.isNotEmpty(userInfo)) {
                  user.setAvatar(userInfo.getAvatar());
                }
            }
        }

    }
}
