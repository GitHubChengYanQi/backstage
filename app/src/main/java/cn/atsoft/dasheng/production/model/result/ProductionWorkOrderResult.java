package cn.atsoft.dasheng.production.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 工单
 * </p>
 *
 * @author 
 * @since 2022-02-28
 */
@Data
@ApiModel
public class ProductionWorkOrderResult implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("")
    private Long workOrderId;

    /**
     * 工序id
     */
    @ApiModelProperty("工序id")
    private Long shipSetpId;

    @ApiModelProperty("")
    private Long skuId;

    /**
     * 生产数量
     */
    @ApiModelProperty("生产数量")
    private Integer count;

    /**
     * 来源
     */
    @ApiModelProperty("来源")
    private String source;

    /**
     * 来源id
     */
    @ApiModelProperty("来源id")
    private Long sourceId;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;

    /**
     * 步骤id
     */
    @ApiModelProperty("步骤id")
    private Long stepsId;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    /**
     * 主题
     */
    @ApiModelProperty("主题")
    private String theme;

    /**
     * 来源Json字符串
     */
    @ApiModelProperty("来源Json字符串")
    private String origin;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
