package cn.atsoft.dasheng.app.model.result;

import cn.atsoft.dasheng.app.entity.Items;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 商机明细表
 * </p>
 *
 * @author qr
 * @since 2021-08-04
 */
@Data
@ApiModel
public class CrmBusinessDetailedResult implements Serializable {

    private static final long serialVersionUID = 1L;

   private List<Items> items;

    /**
     * id
     */
    @ApiModelProperty("id")
    private Long id;

    /**
     * 商机id
     */
    @ApiModelProperty("商机id")
    private Long businessId;

    /**
     * 物品id
     */
    @ApiModelProperty("物品id")
    private Long itemId;

    /**
     * 物品数量
     */
    @ApiModelProperty("物品数量")
    private Long quantity;

  /**
   * 销售单价
   */
  @ApiModelProperty("销售单价")
  private Double salePrice;

  /**
   * 总计
   */
  @ApiModelProperty("总计")
  private Double totalPrice;


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
