package cn.atsoft.dasheng.api.protal.controller;

import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.portal.remind.entity.Remind;
import cn.atsoft.dasheng.portal.remind.model.result.RemindResult;
import cn.atsoft.dasheng.portal.remind.service.RemindService;
import cn.atsoft.dasheng.portal.remindUser.entity.RemindUser;
import cn.atsoft.dasheng.portal.remindUser.model.params.RemindUserParam;
import cn.atsoft.dasheng.portal.remindUser.service.RemindUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
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
    public List<Long> getUserPermission(@RequestBody(required = false) RemindUserParam remindUserParam) {
        if(ToolUtil.isEmpty(remindUserParam)){
            remindUserParam = new RemindUserParam();
        }
        QueryWrapper<RemindUser> remindUserQueryWrapper = new QueryWrapper<>();
        remindUserQueryWrapper.in("user_id", remindUserParam.getUserId());
        List<RemindUser> remindUserList = this.remindUserService.list(remindUserQueryWrapper);
        List<Remind> remindList = remindService.list();
        List<Long> types = new ArrayList<>();
        for(RemindUser data : remindUserList){
            for (Remind user : remindList){
                if(data.getRemindUserId() == user.getRemindId()){
                    types.add(user.getType());
                }
            }
        }
        return types;
    }

}
