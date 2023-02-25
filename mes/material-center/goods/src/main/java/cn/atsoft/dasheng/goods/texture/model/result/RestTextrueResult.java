package cn.atsoft.dasheng.goods.texture.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 材质
 * </p>
 *
 * @author 1
 * @since 2021-07-14
 */
@Data
@ApiModel
public class RestTextrueResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 材质id
     */
    @ApiModelProperty("材质id")
    private Long materialId;

    /**
     * 材质名字
     */
    @ApiModelProperty("材质名字")
    private String name;

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

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
    private Long deptId;
}
