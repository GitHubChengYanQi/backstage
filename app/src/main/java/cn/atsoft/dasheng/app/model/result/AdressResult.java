package cn.atsoft.dasheng.app.model.result;

import cn.atsoft.dasheng.crm.region.RegionResult;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 客户地址表
 * </p>
 *
 * @author 
 * @since 2021-07-23
 */
@Data
@ApiModel
public class AdressResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private RegionResult regionResult;


    /**
     * 地址id
     */
    @ApiModelProperty("地址id")
    private Long adressId;

    /**
     * 地址
     */
    @ApiModelProperty("地址")
    private String location;

    private String region;

    /**
     * 经度
     */
    @ApiModelProperty("经度")
    private Double longitude;

    /**
     * 纬度
     */
    @ApiModelProperty("纬度")
    private Double latitude;

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
     * 部门id
     */
    @ApiModelProperty("部门id")
    private Long deptId;

    /**
     * 客户id
     */
    @ApiModelProperty("客户id")
    private Long customerId;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
