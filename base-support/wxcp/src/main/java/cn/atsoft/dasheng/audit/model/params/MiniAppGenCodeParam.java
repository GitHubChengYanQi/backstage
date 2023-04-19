package cn.atsoft.dasheng.audit.model.params;

import cn.binarywang.wx.miniapp.bean.WxMaCodeLineColor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MiniAppGenCodeParam {
    @NotNull
    private String scene;

    private String page;

    private boolean checkPath;

    private String envVersion;

    private int width;

    private boolean autoColor;

    private WxMaCodeLineColor lineColor;

    private boolean isHyaline;
}
