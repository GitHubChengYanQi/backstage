package cn.atsoft.dasheng.erp.model.params;

import io.swagger.models.auth.In;
import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author song
 * @since 2022-05-27
 */
@Data
@ApiModel
public class AnomalyDetailParam implements Serializable, BaseValidatingParam {

    private Long anomalyOrderId;

    private String type;

    private List<Long> orderIds;

    private static final long serialVersionUID = 1L;

    private Long pidInKind;

    private Long instockListId;

    @ApiModelProperty("")
    private Long detailId;

    private Long number;


    @ApiModelProperty("")

    private Long anomalyId;

    private List<Long> noticeIds;

    /**
     * 实物id
     */
    @ApiModelProperty("实物id")
    private Long inkindId;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Long stauts;

    /**
     * 异常原因
     */
    @ApiModelProperty("异常原因")
    private String remark;

    /**
     * 原因图片
     */
    @ApiModelProperty("原因图片")
    private String reasonImg;

    /**
     * 处理人
     */
    @ApiModelProperty("处理人")
    private Long userId;

    /**
     * 处理意见
     */
    @ApiModelProperty("处理意见")
    private String opinion;

    /**
     * 意见图片
     */
    @ApiModelProperty("意见图片")
    private String opinionImg;

    /**
     * 异常表述
     */
    @ApiModelProperty("异常表述")
    private String description;

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

    @Override
    public String checkParam() {
        return null;
    }

}
