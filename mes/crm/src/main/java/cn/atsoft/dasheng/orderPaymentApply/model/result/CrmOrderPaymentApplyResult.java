package cn.atsoft.dasheng.orderPaymentApply.model.result;

import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import me.chanjar.weixin.cp.bean.oa.WxCpOaApplyEventRequest;

import java.util.List;
/**
 * <p>
 * 
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-03-18
 */
@Data
@ApiModel
public class CrmOrderPaymentApplyResult implements Serializable {

    private static final long serialVersionUID = 1L;
    @JSONField(serialize = false)
    private String msg;

    private WxCpOaApplyEventRequest applyEventRequest;

    @ApiModelProperty("")
    private Long creatorUser;

    @ApiModelProperty("")
    private Long orderId;

    @ApiModelProperty("")
    private Long tenantId;

    @ApiModelProperty("")
    private Double newMoney;

    /**
     * 删除状态
     */
    @ApiModelProperty("删除状态")
    private Integer display;

    @ApiModelProperty(hidden = true)
    private Long createUser;

    @ApiModelProperty(hidden = true)
    private Long updateUser;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Date updateTime;

    private Date doneTime;

    @ApiModelProperty("")
    private String spNo;

    private String filed;

    @ApiModelProperty("")
    private Integer status;

    private UserResult userResult;

    public Double getNewMoney() {
        if (ToolUtil.isEmpty(newMoney) || newMoney == 0) {
            return 0.0;
        }
        return BigDecimal.valueOf(newMoney).divide(BigDecimal.valueOf(100), 2, RoundingMode.DOWN).doubleValue();
    }

}
