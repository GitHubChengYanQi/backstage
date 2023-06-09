package cn.atsoft.dasheng.portal.navigation.model.result;

import cn.atsoft.dasheng.portal.navigationDifference.model.result.NavigationDifferenceResult;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 导航表
 * </p>
 *
 * @author 
 * @since 2021-08-18
 */
@Data
@ApiModel
public class NavigationResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private NavigationDifferenceResult differenceResult;
    private Long difference;
    private Long sort;

    private String link;
    /**
     * 导航id
     */
    @ApiModelProperty("导航id")
    private Long navigationId;

    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String title;

    /**
     * 图标
     */
    @ApiModelProperty("图标")
    private String icon;

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
    /**
     * 部门id
     */
    @ApiModelProperty("部门Id")
    private Long deptId;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
