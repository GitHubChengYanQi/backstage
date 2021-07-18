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
 * 经纬度表
 * </p>
 *
 * @author 
 * @since 2021-07-16
 */
@Data
@ApiModel
public class LalParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 客户名称
     */
    @ApiModelProperty("客户名称")
    private String name;

    /**
     * 经纬度id
     */
    @ApiModelProperty("经纬度id")
    private Long lalId;

    /**
     * 经度
     */
    @ApiModelProperty("经度")
    private Integer ewItude;

    /**
     * 纬度
     */
    @ApiModelProperty("纬度")
    private Integer snItude;

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

    @Override
    public String checkParam() {
        return null;
    }

}
