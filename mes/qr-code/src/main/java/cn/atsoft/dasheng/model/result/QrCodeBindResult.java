package cn.atsoft.dasheng.model.result;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 二维码绑定
 * </p>
 *
 * @author song
 * @since 2021-10-29
 */
@Data
@ApiModel
public class QrCodeBindResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long num;
    /**
     * 类型
     */
    private String type;
    /**
     * 绑定id
     */
    @ApiModelProperty("绑定id")
    private Long orCodeBindId;

    /**
     * 二维码id
     */
    @ApiModelProperty("二维码id")
    private Long orCodeId;

    /**
     * 来源
     */
    @ApiModelProperty("来源")
    private String source;

    /**
     * 表单id
     */
    @ApiModelProperty("表单id")
    private Long formId;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    private Long deptId;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
