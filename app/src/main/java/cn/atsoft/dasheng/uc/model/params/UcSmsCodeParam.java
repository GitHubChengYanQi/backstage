package cn.atsoft.dasheng.uc.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 * <p>
 *  - 
 * </p>
 *
 * @author Sing
 * @since 2021-03-16
 */
@Data
@ApiModel
public class UcSmsCodeParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("")
    private Long smsId;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String mobile;

    /**
     * 验证码
     */
    @ApiModelProperty("验证码")
    private String code;

    /**
     * 发送时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 是否有效
     */
    @ApiModelProperty("是否有效")
    private Integer used;

    /**
     * 设备信息
     */
    @ApiModelProperty("设备信息")
    private String device;

    /**
     * Ip地址
     */
    @ApiModelProperty("Ip地址")
    private String ip;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
