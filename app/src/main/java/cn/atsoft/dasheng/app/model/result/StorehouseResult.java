package cn.atsoft.dasheng.app.model.result;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
public class StorehouseResult implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 纬度
     */
    private BigDecimal latitude;


    /**
     * 经度
     */

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

    private String palce;

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
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 创建者
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 修改时间
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 修改者
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    /**
     * 状态
     */
    @JSONField(serialize = false)
    @ApiModelProperty("状态")
    private Integer display;
    @JSONField(serialize = false)
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
    @JSONField(serialize = false)
    private Long deptId;
}
