package cn.atsoft.dasheng.app.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 客户表
 * </p>
 *
 * @author 
 * @since 2021-07-15
 */
@Data
@ApiModel
public class ClientResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 客户id
     */
    @ApiModelProperty("客户id")
    private Long clientId;

    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    private String name;

    /**
     * 居住地址
     */
    @ApiModelProperty("居住地址")
    private String adress;

    /**
     * 联系电话
     */
    @ApiModelProperty("联系电话")
    private Long phone;

    /**
     * 下单时间
     */
    @ApiModelProperty("下单时间")
    private Date orderTime;

    /**
     * 价格
     */
    @ApiModelProperty("价格")
    private Integer price;


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
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    private  Long orderId;
    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
