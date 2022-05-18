package cn.atsoft.dasheng.form.model.result;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 单据状态
 * </p>
 *
 * @author song
 * @since 2022-04-28
 */
@Data
@ApiModel
public class DocumentsStatusResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<DocumentsActionResult> actionResults;
    /**
     * 通用单据状态id
     */
    @ApiModelProperty("通用单据状态id")
    private Long documentsStatusId;

    private String name;

    /**
     * 动作
     */
    @ApiModelProperty("动作")
    private String action;

    /**
     * 表单状态
     */
    @ApiModelProperty("表单状态")
    private Integer formStatus;

    /**
     * 表单类型
     */

    @ApiModelProperty("表单类型")
    private String formType;

    /**
     * 创建者
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 修改者
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    /**
     * 创建时间
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false)
    private Date updateTime;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    @JSONField(serialize = false)
    private Integer display;
    @ApiModelProperty("父ID顺序数组")
    @JSONField(serialize = false)
    private List<String> pidValue;
}
