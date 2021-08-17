package cn.atsoft.dasheng.uc.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author Sing
 * @since 2021-03-16
 */
@Data
@ApiModel
public class UcMemberResult implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("")
    private Long memberId;

    @ApiModelProperty("")
    private String phone;

    @ApiModelProperty("")
    private Integer status;

    @ApiModelProperty("")
    private Integer display;

    @ApiModelProperty(hidden = true)
    private Long createUser;

    @ApiModelProperty(hidden = true)
    private Long updateUser;

    @ApiModelProperty(hidden = true)
    private Date updateTime;

    @ApiModelProperty(hidden = true)
    private Date createTime;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
