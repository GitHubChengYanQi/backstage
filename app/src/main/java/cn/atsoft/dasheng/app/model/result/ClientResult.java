package cn.atsoft.dasheng.app.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 客户管理表
 * </p>
 *
 * @author 
 * @since 2021-07-16
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
     * 客户名称
     */
    @ApiModelProperty("客户名称")
    private String name;

    /**
     * 客户地址id
     */
    @ApiModelProperty("客户地址id")
    private String adressId;

    /**
     * 联系电话
     */
    @ApiModelProperty("联系电话")
    private Long phone1;

    /**
     * 固定电话
     */
    @ApiModelProperty("固定电话")
    private Long phone2;

    /**
     * 订单号
     */
    @ApiModelProperty("订单号")
    private Long orderId;

    /**
     * 下单时间
     */
    @ApiModelProperty("下单时间")
    private Date orderTime;

    /**
     * 成立时间
     */
    @ApiModelProperty("成立时间")
    private Date setup;

    /**
     * 法定代表人
     */
    @ApiModelProperty("法定代表人")
    private String legal;

    /**
     * 统一社会信用代码
     */
    @ApiModelProperty("统一社会信用代码")
    private Long utscc;

    /**
     * 公司类型
     */
    @ApiModelProperty("公司类型")
    private String companyType;

    /**
     * 营业期限
     */
    @ApiModelProperty("营业期限")
    private Long businessTerm;

    /**
     * 注册地址
     */
    @ApiModelProperty("注册地址")
    private String signIn;

    /**
     * 简介
     */
    @ApiModelProperty("简介")
    private String introduction;

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
