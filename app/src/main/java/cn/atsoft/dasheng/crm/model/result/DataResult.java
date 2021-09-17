package cn.atsoft.dasheng.crm.model.result;

import cn.atsoft.dasheng.app.model.result.ItemsResult;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 资料
 * </p>
 *
 * @author song
 * @since 2021-09-11
 */
@Data
@ApiModel
public class DataResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<ItemsResult> itemId;
    /**
     * 资料id
     */
    @ApiModelProperty("资料id")
    private Long dataId;
    private  String titile;
    private Long dataClassificationId;

    private String name;
    /**
     * 内容
     */
    @ApiModelProperty("内容")
    private String content;

    /**
     * 附件
     */
    @ApiModelProperty("附件")
    private String attachment;

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
    /**
     * 部门id
     */
    @ApiModelProperty("部门Id")
    private Long deptId;
}
