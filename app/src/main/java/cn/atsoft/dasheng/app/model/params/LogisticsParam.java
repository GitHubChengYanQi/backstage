package cn.atsoft.dasheng.app.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 * <p>
 * 物流表
 * </p>
 *
 * @author 
 * @since 2021-07-15
 */
@Data
@ApiModel
public class LogisticsParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 物流id
     */
    @ApiModelProperty("物流id")
    private Long logisticsId;

    /**
     * 发货编号
     */
    @ApiModelProperty("发货编号")
    private Long orderId;

    /**
     * 到货地址
     */
    @ApiModelProperty("到货地址")
    private Long clientId;

    /**
     * 当前位置
     */
    @ApiModelProperty("当前位置")
    private String position;

    /**
     * 到货地址
     */
    @ApiModelProperty("到货地址")
    private String adress;

     private  Date outtime;
    /**
     * 物流电话
     */
    @ApiModelProperty("物流电话")
    private Long phone;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Long createUser;

    @ApiModelProperty(hidden = true)
    private Date updateTime;

    @ApiModelProperty(hidden = true)
    private Long updateUser;

    @ApiModelProperty("")
    private Integer display;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
