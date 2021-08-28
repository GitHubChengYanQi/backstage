package cn.atsoft.dasheng.uc.controller;

import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.sys.core.exception.InvalidKaptchaException;
import cn.atsoft.dasheng.uc.entity.UcSmsCode;
import cn.atsoft.dasheng.uc.model.params.SmsPhoneParam;
import cn.atsoft.dasheng.uc.service.UcSmsCodeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.code.kaptcha.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/sms")
@Api(tags = "短信验证码")
public class SmsController extends BaseController {

    @Autowired
    private UcSmsCodeService ucSmsCodeService;


    @RequestMapping("/sendCode")
    @ApiOperation(value = "发送登录验证码",httpMethod = "POST")
    public ResponseData sendCode(@RequestBody @Valid SmsPhoneParam phoneParam) throws MissingServletRequestParameterException {


        if (ToolUtil.isEmpty(phoneParam.getPhone())) {
            throw new MissingServletRequestParameterException("手机必填", "SmsPhoneParam");
        }

        String kaptcha = phoneParam.getKaptcha().trim();
        String code = (String) super.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        super.getSession().removeAttribute(Constants.KAPTCHA_SESSION_KEY);
        if (ToolUtil.isEmpty(kaptcha) || !kaptcha.equalsIgnoreCase(code)) {
            throw new InvalidKaptchaException();
        }

        String dateTime = ToolUtil.getCreateTimeBefore(60);
        QueryWrapper<UcSmsCode> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("create_time", dateTime);
        queryWrapper.eq("mobile",phoneParam.getPhone());

        UcSmsCode ucSmsCode = ucSmsCodeService.getOne(queryWrapper);
        if (ToolUtil.isNotEmpty(ucSmsCode)) {
            throw new ServiceException(500, "发送验证码频率过快");
        }
        String Code = ucSmsCodeService.getCode(phoneParam.getPhone());

        ucSmsCodeService.sendCode(phoneParam.getPhone(), Code);

        return ResponseData.success();
    }
}
