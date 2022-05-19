package cn.atsoft.dasheng.form.model.params;

import cn.atsoft.dasheng.form.service.DocumentsOperationService;
import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 单据权限
 * </p>
 *
 * @author
 * @since 2022-05-18
 */
@Data
@ApiModel
public class DocumentsPermissionsParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private List<DocumentsOperationParam> operationParams;
    /**
     * 通用单据权限
     */
    @ApiModelProperty("通用单据权限")
    private Long permissionsId;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 表单类型
     */
    @ApiModelProperty("表单类型")
    private String formType;

    /**
     * 字段名
     */
    @ApiModelProperty("字段名")
    private String filedName;

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
