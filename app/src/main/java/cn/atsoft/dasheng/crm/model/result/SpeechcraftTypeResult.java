package cn.atsoft.dasheng.crm.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 话术分类
 * </p>
 *
 * @author 
 * @since 2021-09-13
 */
@Data
@ApiModel
public class SpeechcraftTypeResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 分类id
     */
    @ApiModelProperty("分类id")
    private Long speechcraftTypeId;

    /**
     * 分类排序
     */
    @ApiModelProperty("分类排序")
    private Long speechcraftTypeSort;

    /**
     * 分类名称
     */
    @ApiModelProperty("分类名称")
    private String speechcraftTypeName;

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
