package cn.atsoft.dasheng.shop.classDifference.model.result;

import cn.atsoft.dasheng.shop.classDifferenceDetail.model.result.ClassDifferenceDetailsResult;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 分类明细
 * </p>
 *
 * @author siqiang
 * @since 2021-08-18
 */
@Data
@ApiModel
public class ClassDifferenceResult implements Serializable {

    private static final long serialVersionUID = 1L;

  private  List<ClassDifferenceDetailsResult> list;
    /**
     * 分类明细id
     */
    @ApiModelProperty("分类明细id")
    private Long classDifferenceId;

    /**
     * 分类id
     */
    @ApiModelProperty("分类id")
    private Long classId;

    /**
     * 分类名
     */
    @ApiModelProperty("分类名")
    private String title;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Long sort;

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
   * 部门id
   */
  @ApiModelProperty("部门Id")
  private Long deptId;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
