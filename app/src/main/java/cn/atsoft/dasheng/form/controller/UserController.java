package cn.atsoft.dasheng.form.controller;


import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.form.pojo.UserList;
import cn.atsoft.dasheng.form.service.StepsService;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/formUser")
public class UserController extends BaseController {
    @Autowired
    private StepsService stepsService;


    @RequestMapping(value = "/userList", method = RequestMethod.POST)
    public ResponseData userList(@RequestBody(required = false) User user) {
        List<UserList> userLists = stepsService.userLists(user.getName());
        return ResponseData.success(userLists);
    }
}
