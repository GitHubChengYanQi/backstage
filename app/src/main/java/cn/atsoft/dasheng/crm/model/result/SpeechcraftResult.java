package cn.atsoft.dasheng.crm.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 话术基础资料
 * </p>
 *
 * @author 
 * @since 2021-09-11
 */
@Data
@ApiModel
public class SpeechcraftResult implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("")
    private Long speechcraftId;

    private Long speechcraftType;

    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String speechcraftTitle;

    /**
     * 详情
     */
    @ApiModelProperty("详情")
    private String speechcraftDetails;

    /**
     * 关键词
     */
    @ApiModelProperty("关键词")
    private String speechcraftKey;

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
