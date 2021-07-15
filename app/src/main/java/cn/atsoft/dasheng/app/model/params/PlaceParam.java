package cn.atsoft.dasheng.app.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
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
public class PlaceParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 仓库id
     */
    @ApiModelProperty("仓库id")
    private Long palceId;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 地点
     */
    @ApiModelProperty("地点")
    private String position;

    /**
     * 位置
     */
    @ApiModelProperty("位置")
    private String palce;

    /**
     * 面积
     */
    @ApiModelProperty("面积")
    private String measure;

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

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
