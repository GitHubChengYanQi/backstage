package cn.atsoft.dasheng.app.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 部门表
 * </p>
 *
 * @author 
 * @since 2020-12-18
 */
@Data
@ApiModel
public class SysDeptResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private Long deptId;

    /**
     * 父部门id
     */
    @ApiModelProperty("父部门id")
    private Long pid;

    /**
     * 父级ids
     */
    @ApiModelProperty("父级ids")
    private String pids;

    /**
     * 简称
     */
    @ApiModelProperty("简称")
    private String simpleName;

    /**
     * 全称
     */
    @ApiModelProperty("全称")
    private String fullName;

    /**
     * 描述
     */
    @ApiModelProperty("描述")
    private String description;

    /**
     * 版本（乐观锁保留字段）
     */
    @ApiModelProperty(hidden = true)
    private Integer version;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

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
     * 创建人
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 修改人
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

}
