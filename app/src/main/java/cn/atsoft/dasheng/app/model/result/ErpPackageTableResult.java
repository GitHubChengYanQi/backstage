package cn.atsoft.dasheng.app.model.result;

import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.entity.Items;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.aspectj.weaver.IUnwovenClassFile;

import java.util.List;
/**
 * <p>
 * 套餐分表
 * </p>
 *
 * @author qr
 * @since 2021-08-04
 */
@Data
@ApiModel
public class ErpPackageTableResult implements Serializable {

    private static final long serialVersionUID = 1L;
      private ItemsResult itemsResult;
      private BrandResult brandResult;
    /**
     * id
     */
    @ApiModelProperty("id")
    private Long id;

    /**
     * 套餐id
     */
    @ApiModelProperty("套餐id")
    private Long packageId;

  /**
   * 产品id
   */
  @ApiModelProperty("产品id")
  private Long itemId;

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

  /**
   * 销售单价
   */
  @ApiModelProperty("销售单价")
  private int salePrice;

  /**
   * 总计
   */
  @ApiModelProperty("总计")
  private int totalPrice;

  /**
   * 品牌
   */
  @ApiModelProperty("品牌")
  private Long brandId;

  /**
   * 数量
   */
  @ApiModelProperty("数量")
  private Long quantity;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
