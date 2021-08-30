package cn.atsoft.dasheng.api.protal.controller;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.userInfo.controller.WxTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiSendMessageController {


    @Autowired
    private WxTemplate wxTemplate;

    @RequestMapping(value = "/sendType", method = RequestMethod.POST)
    public void sendType(@RequestParam Long type) {
        
        wxTemplate.template(type);
    }
}
