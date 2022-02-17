package cn.atsoft.dasheng.daoxin.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * daoxin部门表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-02-17
 */
@Data
@ApiModel
public class DaoxinDeptParam implements Serializable, BaseValidatingParam {

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

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
