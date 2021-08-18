package cn.atsoft.dasheng.portal.classdifferencedetail.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 分类明细内容
 * </p>
 *
 * @author siqiang
 * @since 2021-08-18
 */
@Data
@ApiModel
public class ClassDifferenceDetailsResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 产品id
     */
    @ApiModelProperty("产品id")
    private Long detailId;

    /**
     * 分类明细id
     */
    @ApiModelProperty("分类明细id")
    private Long classDifferenceId;

    /**
     * 产品名
     */
    @ApiModelProperty("产品名")
    private String title;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Long sort;

    /**
     * 图片路径
     */
    @ApiModelProperty("图片路径")
    private String imgUrl;

    /**
     * 链接
     */
    @ApiModelProperty("链接")
    private String link;

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
