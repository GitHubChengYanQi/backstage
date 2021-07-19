package cn.atsoft.dasheng.app.model.result;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 商机表
 * </p>
 *
 * @author cheng
 * @since 2021-07-19
 */
@Data
@ApiModel
public class BusinessResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private String iname;
    private Long stockId;
    private  String sname;
    private  Long sourceId;

    /**
     * 商机id
     */
    @ApiModelProperty("商机id")
    private Long businessId;

    /**
     * 客户id
     */
    @ApiModelProperty("客户id")
    private Long clitenId;




    /**
     * 立项日期
     */
    @ApiModelProperty("立项日期")
    private String time;

    /**
     * 商机状态
     */
    @ApiModelProperty("商机状态")
    private Integer state;

    /**
     * 商机阶段
     */
    @ApiModelProperty("商机阶段")
    private String stage;

    /**
     * 负责人
     */
    @ApiModelProperty("负责人")
    private Long person;

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
     * 创建用户
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 修改用户
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;
    private String name;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
