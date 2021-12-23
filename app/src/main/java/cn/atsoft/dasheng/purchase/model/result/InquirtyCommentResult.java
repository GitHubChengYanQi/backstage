package cn.atsoft.dasheng.purchase.model.result;

import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-23
 */
@Data
@ApiModel
public class InquirtyCommentResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private User user;
    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private Long commentId;

    /**
     * 来源id
     */
    @ApiModelProperty("来源id")
    private Long formId;

    /**
     * 来源类型
     */
    @ApiModelProperty("来源类型")
    private String formType;

    /**
     * 图片id逗号分隔
     */
    @ApiModelProperty("图片id逗号分隔")
    private String imageIds;

    /**
     * 对话详情
     */
    @ApiModelProperty("对话详情")
    private String details;

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
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;

    /**
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
