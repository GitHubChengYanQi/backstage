package cn.atsoft.dasheng.app.model.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 出库表
 * </p>
 *
 * @author song
 * @since 2021-07-17
 */
@Data
@ApiModel
public class
DeliveryResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 出库id
     */
    @ApiModelProperty("出库id")
    private Long deliveryId;

    /**
     * 库存id
     */
    @ApiModelProperty("库存id")
    private Long stockId;

    /**
     * 出库时间
     */
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("出库时间")
    private Date deliveryTime;

    /**
     * 出库数量
     */
    @ApiModelProperty("出库数量")
    private Long number;

    /**
     * 出库价格
     */
    @ApiModelProperty("出库价格")
    private Integer price;

    /**
     * 出库品牌
     */
    private  String name;
    private String  brandName;


    @ApiModelProperty("出库品牌")
    private String brand;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Long createUser;

    @ApiModelProperty(hidden = true)
    private Date updateTime;

    @ApiModelProperty(hidden = true)
    private Long updateUser;

    @ApiModelProperty("")
    private Integer display;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
