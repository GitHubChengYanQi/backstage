package cn.atsoft.dasheng.app.model.params;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
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
public class DeliveryParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;



    private  String name;
    private  String brandName;

    public String getDeliveryTime() {
        if (deliveryTime!=null&&!deliveryTime.equals("")){
            StringBuffer stringBuffer = new StringBuffer(deliveryTime);
            String date = stringBuffer.substring(0,10);
            return date;
        }else {
            return deliveryTime;
        }


    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

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
    @JsonFormat(pattern = "yyyy-MM-dd  HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("出库时间")
    private String deliveryTime;

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

    @Override
    public String checkParam() {
        return null;
    }

}
