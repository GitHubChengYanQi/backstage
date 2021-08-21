package cn.atsoft.dasheng.portal.companyaddress.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 报修
 * </p>
 *
 * @author siqiang
 * @since 2021-08-20
 */
@Data
@ApiModel
public class CompanyAddressResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 单位
     */
    @ApiModelProperty("单位")
    private Long companyId;

    /**
     * 报修id
     */
    @ApiModelProperty("报修id")
    private Long repairId;

    /**
     * 报修地址
     */
    @ApiModelProperty("报修地址")
    private Long customerId;

    /**
     * 省
     */
    @ApiModelProperty("省")
    private String province;

    /**
     * 市
     */
    @ApiModelProperty("市")
    private String city;

    /**
     * 区
     */
    @ApiModelProperty("区")
    private String area;

    /**
     * 详细地址
     */
    @ApiModelProperty("详细地址")
    private String address;

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

    private Long imgUrl;

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

    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    private String people;

    /**
     * 职务
     */
    @ApiModelProperty("职务")
    private String position;

    /**
     * 电话
     */
    @ApiModelProperty("电话")
    private Integer telephone;

    /**
     * 区分：报修单位1，使用单位2
     */
    @ApiModelProperty("区分：报修单位1，使用单位2")
    private Long identify;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
