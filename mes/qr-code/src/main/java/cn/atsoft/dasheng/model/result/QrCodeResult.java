package cn.atsoft.dasheng.model.result;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 二维码
 * </p>
 *
 * @author song
 * @since 2021-10-29
 */
@Data
@ApiModel
public class QrCodeResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private Object object;
    /**
     * 绑定状态
     */
    private Integer state;
    /**
     * 码id
     */
    @ApiModelProperty("码id")
    private Long orCodeId;

    /**
     * 类型
     */
    @ApiModelProperty("类型")
    private String type;

    /**
     * 信息
     */
    @ApiModelProperty("信息")
    private String data;

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
