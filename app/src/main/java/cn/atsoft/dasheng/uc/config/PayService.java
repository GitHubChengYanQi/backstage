package cn.atsoft.dasheng.uc.config;

import cn.atsoft.dasheng.uc.entity.UcPayOrder;
import cn.atsoft.dasheng.uc.model.params.UcPayOrderParam;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.symmetric.AES;
import com.alibaba.fastjson.JSON;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import lombok.Data;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Configuration
@EnableConfigurationProperties(PayProperties.class)
@Data
public class PayService {

    private PayProperties config;

    private Boolean aliFactory = false;

    public PayService(PayProperties config) {
        this.config = config;
    }

    // 解密数据，并返回支付订单实体对象
    public UcPayOrderParam getUcPayOrderParam(String content, String postData) {
        String privateKey = this.getConfig().getUcConfig().getPrivateKeyStr();
        String publicKey = this.getConfig().getUcConfig().getPublicKeyStr();
        RSA rsa = new RSA(privateKey, publicKey);
        try {
            String key = rsa.decryptStr(postData, KeyType.PrivateKey);

            AES aes = new AES(key.getBytes(StandardCharsets.UTF_8));
            String decryptStr = aes.decryptStr(content);
            return JSON.parseObject(decryptStr, UcPayOrderParam.class);
        } catch (Exception e) {
            throw new ServiceException(500, "加密信息错误");
        }
    }

    public HashMap<String, Object> buildPushData(UcPayOrder ucPayOrder) {

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("outTradeNo", ucPayOrder.getOutTradeNo());
        paramMap.put("status", ucPayOrder.getStatus().equals(1) ? "success" : "fail");
        paramMap.put("totalAmount", ucPayOrder.getTotalAmount());
        paramMap.put("pay_time", ucPayOrder.getUpdateTime());
        String key = ToolUtil.getRandomString(16);
        AES aes = new AES(key.getBytes(StandardCharsets.UTF_8));

        String content = JSON.toJSONString(paramMap);
        String encryptStr = aes.encryptBase64(content);

        String privateKey = this.getConfig().getUcConfig().getPrivateKeyStr();
        String publicKey = this.getConfig().getUcConfig().getPublicKeyStr();
        RSA rsa = new RSA(privateKey, publicKey);

        String postData = rsa.encryptBase64(key, KeyType.PublicKey);

        HashMap<String, Object> result = new HashMap<>();
        result.put("payData", encryptStr);
        result.put("postData", postData);
        return result;
    }

    public WxPayService getWeiXinPay() {

        WxPayConfig payConfig = new WxPayConfig();

        PayProperties.WeiXinPay weiXinPay = this.config.getWeiXinPay();

        payConfig.setAppId(weiXinPay.getAppId());
        payConfig.setMchId(weiXinPay.getMchId());
        payConfig.setMchKey(weiXinPay.getMchKey());

        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }

    public WxPayService getWeiXinMiNiPay() {

        WxPayConfig payConfig = new WxPayConfig();

        PayProperties.WeiXinMiNiPay weiXinMiNiPay = this.config.getWeiXinMiNiPay();

        payConfig.setAppId(weiXinMiNiPay.getAppId());
        payConfig.setMchId(weiXinMiNiPay.getMchId());
        payConfig.setMchKey(weiXinMiNiPay.getMchKey());

        payConfig.setUseSandboxEnv(false);

        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }

    public void setAliFactory() {
//        if (!aliFactory) {
        Config config = new Config();

        config.protocol = "https";
        config.gatewayHost = "openapi.alipay.com";
        config.signType = "RSA2";
        config.appId = this.config.getAlipay().getAppId();
        config.alipayPublicKey = this.config.getAlipay().getAlipayPublicKey();
        config.merchantPrivateKey = this.config.getAlipay().getAppPrivateKey();

        Factory.setOptions(config);
        this.aliFactory = true;
//        }
    }
}
