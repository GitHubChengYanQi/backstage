package cn.atsoft.dasheng.fieldAuthority.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 字段操作权限表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-05-17
 */
@Data
@ApiModel
public class FieldAuthorityParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("")
    private Long authorityId;

    /**
     * 出入参实体类名称
     */
    @ApiModelProperty("出入参实体类名称")
    private String modelNames;

    /**
     * 自定义字段权限
     */
    @ApiModelProperty("自定义字段权限")
    private String detail;

    /**
     * 删除状态
     */
    @ApiModelProperty("删除状态")
    private Integer display;

    @ApiModelProperty(hidden = true)
    private Long createUser;

    @ApiModelProperty(hidden = true)
    private Long updateUser;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Date updateTime;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
