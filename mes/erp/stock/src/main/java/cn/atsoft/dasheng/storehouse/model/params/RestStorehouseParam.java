package cn.atsoft.dasheng.storehouse.model.params;

import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 地点表
 * </p>
 *
 * @author 
 * @since 2021-07-15
 */
@Data
@ApiModel
public class RestStorehouseParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;
    /**
     * 纬度
     */
    @ApiModelProperty("纬度")
    private BigDecimal latitude;

    /**
     * 经度
     */
    @ApiModelProperty("经度")
    private BigDecimal longitude;

    /**
     * 仓库id
     */
    @ApiModelProperty("仓库id")
    private Long storehouseId;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;


    /**
     * 位置
     */
    @ApiModelProperty("位置")
    private String palce;
    private String coding;

    /**
     * 面积
     */
    @ApiModelProperty("面积")
    private Long measure;

    /**
     * 容量
     */
    @ApiModelProperty("容量")
    private Long capacity;

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
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;

    private String children;

    private String childrens;


    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
    private Long deptId;
    @Override
    public String checkParam() {
        return null;
    }

}
