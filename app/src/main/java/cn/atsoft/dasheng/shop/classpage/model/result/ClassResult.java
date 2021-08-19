package cn.atsoft.dasheng.shop.classpage.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 一级分类导航
 * </p>
 *
 * @author 
 * @since 2021-08-19
 */
@Data
@ApiModel
public class ClassResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 分类id
     */
    @ApiModelProperty("分类id")
    private Long classId;

    /**
     * 分类名称
     */
    @ApiModelProperty("分类名称")
    private String className;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Long sort;

    /**
     * 轮播图分类id
     */
    @ApiModelProperty("轮播图分类id")
    private Long classificationId;

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
