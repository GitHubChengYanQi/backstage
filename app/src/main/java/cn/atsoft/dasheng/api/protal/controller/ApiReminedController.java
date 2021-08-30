package cn.atsoft.dasheng.api.protal.controller;

import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.portal.remind.entity.Remind;
import cn.atsoft.dasheng.portal.remind.service.RemindService;
import cn.atsoft.dasheng.portal.remindUser.entity.RemindUser;
import cn.atsoft.dasheng.portal.remindUser.model.params.RemindUserParam;
import cn.atsoft.dasheng.portal.remindUser.service.RemindUserService;
import cn.atsoft.dasheng.uc.utils.UserUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiReminedController {

    @Autowired
    private RemindService remindService;
    @Autowired
    private RemindUserService remindUserService;

    @RequestMapping(value = "/getUserPermission", method = RequestMethod.POST)
    public List<Long> getPermission() {
        QueryWrapper<RemindUser> remindUserQueryWrapper = new QueryWrapper<>();
        remindUserQueryWrapper.in("user_id", UserUtils.getUserId());
        List<RemindUser> remindUserList = this.remindUserService.list(remindUserQueryWrapper);
        List<Remind> remindList = remindService.list();
        List<Long> types = new ArrayList<>();
        for(RemindUser data : remindUserList){
            for (Remind user : remindList){
                if(data.getRemindId().equals(user.getRemindId())){
                    types.add(user.getType());
                }
            }
        }
        return types;
    }

}
