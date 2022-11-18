package cn.atsoft.dasheng.production.model.params;

import cn.atsoft.dasheng.crm.model.params.OrderDetailParam;
import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 生产计划主表
 * </p>
 *
 * @author 
 * @since 2022-02-25
 */
@Data
@ApiModel
public class ProductionPlanParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;
    List<ProductionPlanDetailParam> productionPlanDetailParams;
    List<OrderDetailParam> orderDetailParams;



    /**
     * 生产计划id
     */
    @ApiModelProperty("生产计划id")
    private Long productionPlanId;

    /**
     * 生产计划id
     */
    @ApiModelProperty("编码")
    private String coding;

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

    /**
     * 开始时间
     */
    @ApiModelProperty("执行时间")
    private Date executionTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private Date endTime;

    /**
     * 负责人
     */
    @ApiModelProperty("负责人")
    private Long userId;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

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
     * 卡片编码
     */
    @ApiModelProperty("卡片编码")
    private String cardCoding;

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

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
