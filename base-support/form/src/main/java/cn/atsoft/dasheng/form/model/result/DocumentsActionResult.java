package cn.atsoft.dasheng.form.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 单据动作
 * </p>
 *
 * @author song
 * @since 2022-04-28
 */
@Data
@ApiModel
public class DocumentsActionResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 单据动作
     */
    @ApiModelProperty("单据动作")
    private Long documentsActionId;


    private String actionName;

    /**
     * 单据状态id
     */
    @ApiModelProperty("单据状态id")
    private Long documentsStatusId;

    /**
     * 动作
     */
    @ApiModelProperty("动作")
    private String action;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

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
}
