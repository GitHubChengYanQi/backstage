package cn.atsoft.dasheng.purchase.model.result;

import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 询价任务详情
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-23
 */
@Data
@ApiModel
public class InquiryTaskDetailResult implements Serializable {

    private static final long serialVersionUID = 1L;


    private SkuResult skuResult;

    private BrandResult brandResult;
    /**
     * 详情id
     */
    @ApiModelProperty("详情id")
    private Long inquiryDetailId;

    /**
     * 品牌id
     */
    @ApiModelProperty("品牌id")
    private Long brandId;

    /**
     * 主表id
     */
    @ApiModelProperty("主表id")
    private Long inquiryTaskId;

    /**
     * 物料id
     */
    @ApiModelProperty("物料id")
    private Long skuId;

    /**
     * 数量
     */
    @ApiModelProperty("数量")
    private Integer total;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

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

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Long display;

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    private Long deptId;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
