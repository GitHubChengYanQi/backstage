package cn.atsoft.dasheng.crm.model.result;

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
 * @since 2022-04-23
 */
@Data
@ApiModel
public class DocumentsStatusResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 通用单据状态id
     */
    @ApiModelProperty("通用单据状态id")
    private Long documentsStatusId;

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