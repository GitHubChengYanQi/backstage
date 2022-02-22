package cn.atsoft.dasheng.Tool;

import cn.atsoft.dasheng.form.pojo.MoneyTypeEnum;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/Enum")
public class MoneyEController {

    @RequestMapping(value = "/money", method = RequestMethod.GET)
    public ResponseData money() {
        return ResponseData.success(MoneyTypeEnum.enumList());
    }
}
