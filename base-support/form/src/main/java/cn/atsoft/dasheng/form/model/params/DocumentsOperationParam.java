package cn.atsoft.dasheng.form.model.params;

import cn.atsoft.dasheng.form.pojo.CanDo;
import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 单据权限操作
 * </p>
 *
 * @author
 * @since 2022-05-18
 */
@Data
@ApiModel
public class DocumentsOperationParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private List<CanDo> canDos;

    /**
     * 详情
     */
    @ApiModelProperty("详情")
    private Long detailsId;

    /**
     * 通用单据权限
     */
    @ApiModelProperty("通用单据权限")
    private Long permissionsId;

    /**
     * 角色
     */
    @ApiModelProperty("角色")
    private Long roleId;

    /**
     * 可操作
     */
    @ApiModelProperty("可操作")
    private String operational;

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

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
