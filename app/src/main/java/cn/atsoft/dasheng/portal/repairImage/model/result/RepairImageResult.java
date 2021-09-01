package cn.atsoft.dasheng.portal.repairImage.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 报修图片
 * </p>
 *
 * @author 1
 * @since 2021-09-01
 */
@Data
@ApiModel
public class RepairImageResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 报修图片
     */
    @ApiModelProperty("报修图片")
    private Long repairImageId;

    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String title;

    /**
     * 路径
     */
    @ApiModelProperty("路径")
    private String imgUrl;

    /**
     * 报修id
     */
    @ApiModelProperty("报修id")
    private Long repairId;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Long state;

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
