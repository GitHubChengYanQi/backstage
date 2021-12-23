package cn.atsoft.dasheng.purchase.model.result;

import cn.atsoft.dasheng.sys.modular.system.entity.User;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 询价任务
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-23
 */
@Data
@ApiModel
public class InquiryTaskResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private User user;

    /**
     * 询价任务id
     */
    @ApiModelProperty("询价任务id")
    private Long inquiryTaskId;

    /**
     * 询价任务名称
     */
    @ApiModelProperty("询价任务名称")
    private String inquiryTaskName;

    /**
     * 负责人id
     */
    @ApiModelProperty("负责人id")
    private Long userId;

    /**
     * 截至日期
     */
    @ApiModelProperty("截至日期")
    private Date deadline;

    /**
     * 供应商等级
     */
    @ApiModelProperty("供应商等级")
    private String supplierLevel;

    /**
     * 是否供应商物料
     */
    @ApiModelProperty("是否供应商物料")
    private Integer isSupplier;

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
     * 创建用户
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 修改用户
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Long display;

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    private Long deptId;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
