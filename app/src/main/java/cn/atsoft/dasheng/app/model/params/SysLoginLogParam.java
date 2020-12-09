package cn.atsoft.dasheng.app.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 登录记录
 * </p>
 *
 * @author sing
 * @since 2020-12-09
 */
@Data
@ApiModel
public class SysLoginLogParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private Long loginLogId;

    /**
     * 日志名称
     */
    @ApiModelProperty("日志名称")
    private String logName;

    /**
     * 管理员id
     */
    @ApiModelProperty("管理员id")
    private Long userId;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 是否执行成功
     */
    @ApiModelProperty("是否执行成功")
    private String succeed;

    /**
     * 具体消息
     */
    @ApiModelProperty("具体消息")
    private String message;

    /**
     * 登录ip
     */
    @ApiModelProperty("登录ip")
    private String ipAddress;

    @Override
    public String checkParam() {
        return null;
    }

}
