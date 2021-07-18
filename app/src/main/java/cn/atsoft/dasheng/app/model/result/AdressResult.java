package cn.atsoft.dasheng.app.model.result;

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
 * @since 2021-07-16
 */
@Data
@ApiModel
public class AdressResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 客户名称
     */
    @ApiModelProperty("客户名称")
    private String name;

    /**
     * 地址id
     */
    @ApiModelProperty("地址id")
    private Long adressId;

    /**
     * 地址1id
     */
    @ApiModelProperty("地址1id")
    private Long adress1Id;

    /**
     * 地址1
     */
    @ApiModelProperty("地址1")
    private String adress1;

    /**
     * 地址2id
     */
    @ApiModelProperty("地址2id")
    private Long adress2Id;

    /**
     * 地址2
     */
    @ApiModelProperty("地址2")
    private String adress2;

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
