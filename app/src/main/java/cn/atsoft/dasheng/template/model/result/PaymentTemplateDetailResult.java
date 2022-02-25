package cn.atsoft.dasheng.template.model.result;

import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 付款模板详情
 * </p>
 *
 * @author song
 * @since 2022-02-24
 */
@Data
@ApiModel
public class PaymentTemplateDetailResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 付款详情主见
     */
    @ApiModelProperty("付款详情主见")
    private Long detailId;

    /**
     * 模板id
     */
    @ApiModelProperty("模板id")
    private Long templateId;

    /**
     * 金额
     */
    @ApiModelProperty("金额")
    private Integer money;

    /**
     * 百分比
     */
    @ApiModelProperty("百分比")
    private Integer percentum;

    /**
     * 付款类型
     */
    @ApiModelProperty("付款类型")
    private Integer payType;

    /**
     * 付款日期
     */
    @ApiModelProperty("付款日期")
    private Date payTime;

    /**
     * 日期方式
     */
    @ApiModelProperty("日期方式")
    private Integer dateWay;

    /**
     * 日期数
     */
    @ApiModelProperty("日期数")
    private Long dateNumber;

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

    /**
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
