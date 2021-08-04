package cn.atsoft.dasheng.app.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 行业表
 * </p>
 *
 * @author 
 * @since 2021-08-02
 */
@Data
@ApiModel
public class CrmIndustryResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 行业id
     */
    @ApiModelProperty("行业id")
    private Long industryId;

    /**
     * 行业名称
     */
    @ApiModelProperty("行业名称")
    private String industryName;

    @ApiModelProperty("上级行业名称")
    private String parentName;
    /**
     * 上级
     */
    @ApiModelProperty("上级")
    private Long parentId;

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
