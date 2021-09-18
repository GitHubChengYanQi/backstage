package cn.atsoft.dasheng.app.model.result;

import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 跟进内容
 * </p>
 *
 * @author cheng
 * @since 2021-09-17
 */
@Data
@ApiModel
public class BusinessTrackResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private UserResult userResult;

    /**
     * 跟进内容id
     */
    @ApiModelProperty("跟进内容id")
    private Long trackId;

    /**
     * 跟踪内容
     */
    @ApiModelProperty("跟踪内容")
    private String message;

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
     * 消息提醒内容
     */
    @ApiModelProperty("消息提醒内容")
    private String tixing;

    /**
     * 跟踪类型
     */
    @ApiModelProperty("跟踪类型")
    private String type;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;

    /**
     * 提醒时间
     */
    @ApiModelProperty("提醒时间")
    private Date time;

    /**
     * 提醒内容
     */
    @ApiModelProperty("提醒内容")
    private String note;

    /**
     * 图片
     */
    @ApiModelProperty("图片")
    private String image;

    /**
     * 经度
     */
    @ApiModelProperty("经度")
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @ApiModelProperty("纬度")
    private BigDecimal latitude;

    /**
     * 负责人
     */
    @ApiModelProperty("负责人")
    private Long userId;

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    private Long deptId;

    /**
     * 分类
     */
    @ApiModelProperty("分类")
    private Integer classify;

    /**
     * 分类id
     */
    @ApiModelProperty("分类id")
    private Long classifyId;

    /**
     * 跟进总表id
     */
    @ApiModelProperty("跟进总表id")
    private Long trackMessageId;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
