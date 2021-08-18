package cn.atsoft.dasheng.portal.navigationdifference.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 导航分类
 * </p>
 *
 * @author 
 * @since 2021-08-18
 */
@Data
@ApiModel
public class NavigationDifferenceResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 导航分类
     */
    @ApiModelProperty("导航分类")
    private Long classificationId;

    /**
     * 分类id
     */
    @ApiModelProperty("分类id")
    private String difference;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
