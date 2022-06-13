package cn.atsoft.dasheng.erp.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 异常生成的实物 绑定
 * </p>
 *
 * @author song
 * @since 2022-05-28
 */
@Data
@ApiModel
public class AnomalyBindResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 异常物品
     */
    @ApiModelProperty("异常物品")
    private Long bindId;

    /**
     * 实物
     */
    @ApiModelProperty("实物")
    private Long inkindId;

    @ApiModelProperty("")
    private Long detailId;

    @ApiModelProperty("")
    private Long anomalyId;

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

    @ApiModelProperty("")
    private Long deptId;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
