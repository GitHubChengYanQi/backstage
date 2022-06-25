package cn.atsoft.dasheng.message.service;

import cn.atsoft.dasheng.app.entity.Adress;
import cn.atsoft.dasheng.app.model.params.AdressParam;
import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.erp.config.MobileConfig;
import cn.atsoft.dasheng.message.entity.MarkDownTemplate;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.production.model.result.ProductionPickListsResult;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.sendTemplate.WxCpTemplate;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.api.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/sendTest")
@Api(tags = "客户地址表")
public class TestController {
    @Autowired
    private WxCpSendTemplate wxCpSendTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private MobileConfig mobileConfig;


    @RequestMapping(value = "/send", method = RequestMethod.POST)
    @ApiOperation("test")
    public ResponseData addItem(@RequestBody MarkDownTemplate markDownTemplate) {
        LoginUser user = LoginContextHolder.getContext().getUser();
        markDownTemplate.setUserIds(new ArrayList<Long>(){{
            add(user.getId());
        }});
        markDownTemplate.setCreateUserName(user.getName());
        wxCpSendTemplate.sendMarkDownTemplate(markDownTemplate);

        return ResponseData.success();
    }

}
