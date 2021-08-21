package cn.atsoft.dasheng.app.model.result;

import cn.atsoft.dasheng.app.service.AdressService;
import cn.atsoft.dasheng.app.service.ContactsService;
import cn.atsoft.dasheng.app.service.PhoneService;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author
 * @since 2021-08-20
 */
@Data
@ApiModel
public class DeliveryDetailsResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private DeliveryResult deliveryResult;
    private Integer stage;
    private  ItemsResult itemsResult;

    /**
     * 发货详情id
     */
    @ApiModelProperty("发货详情id")
    private Long deliveryDetailsId;

    /**
     * 产品id
     */
    @ApiModelProperty("产品id")
    private Long itemId;


    /**
     * 出库明细id
     */
    @ApiModelProperty("出库明细id")
    private Long stockItemId;

    /**
     * 出库单
     */
    @ApiModelProperty("出库单")
    private Long deliveryId;

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
