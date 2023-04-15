package cn.atsoft.dasheng.miniapp;

import cn.atsoft.dasheng.audit.model.params.MiniAppGenCodeParam;
import cn.atsoft.dasheng.audit.service.RestWxCpService;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.core.codec.Base64;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/miniApp")
@Api(tags = "sku表")
public class MiniAppController {
    @Autowired
    private RestWxCpService wxCpService;
    @Autowired
    private WxMaService wxMaService;
    /**
     * 直接物料 新增接口
     *
     * @author
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/getWxaCodeUnLimit", method = RequestMethod.POST)
    @ApiOperation("获取二不限制的小程序码")
    public ResponseData getWxaCodeUnLimit(@RequestBody MiniAppGenCodeParam param) throws WxErrorException {
        byte[] wxaCodeUnlimitBytes = wxMaService.getQrcodeService().createWxaCodeUnlimitBytes(param.getScene(),param.getPage(),param.isCheckPath(), param.getEnvVersion(), param.getWidth(), param.isAutoColor(), param.getLineColor(), param.isHyaline());
        return ResponseData.success(Base64.encode(wxaCodeUnlimitBytes));
    }
}
