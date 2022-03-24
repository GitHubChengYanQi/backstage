package cn.atsoft.dasheng.production.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 报工详情表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-23
 */
@Data
@ApiModel
public class ProductionJobBookingDetailParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;
    private Integer number;

    /**
     * 报工详情表
     */
    @ApiModelProperty("报工详情表")
    private Long jobBookingDetailId;

    /**
     * 生成的实物id
     */
    @ApiModelProperty("生成的实物id")
    private Long inkindId;

    /**
     * 物料id
     */
    @ApiModelProperty("物料id")
    private Long skuId;

    /**
     * 步骤id
     */
    @ApiModelProperty("步骤id")
    private Long stepsId;

    /**
     * 主表id
     */
    @ApiModelProperty("主表id")
    private Long jobBookingId;

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

    @Override
    public String checkParam() {
        return null;
    }

}
