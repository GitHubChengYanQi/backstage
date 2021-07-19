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
 * 清单
 * </p>
 *
 * @author 1
 * @since 2021-07-14
 */
@Data
@ApiModel
public class PartsParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private  String brandName;
    private  String name;
    /**
     * 清单id
     */
    @ApiModelProperty("清单id")
    private Long partsId;

    /**
     * 物品id
     */
    @ApiModelProperty("物品id")
    private Long itemId;

    /**
     * 品牌id
     */
    @ApiModelProperty("品牌id")
    private Long brandId;

    /**
     * 零件数量
     */
    @ApiModelProperty("零件数量")
    private Integer number;



    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;

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

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
