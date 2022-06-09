package cn.atsoft.dasheng.erp.model.params;

import cn.atsoft.dasheng.erp.pojo.AnomalyType;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author song
 * @since 2022-05-27
 */
@Data
@ApiModel
public class AnomalyParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private Long orderId;

    private List<AnomalyParam> params;

    private Long instockListId;

    private List<AnomalyDetailParam> detailParams;

    @ApiModelProperty("")
    private Long anomalyId;

    @ApiModelProperty("")
    private String type;

    @ApiModelProperty("")
    private Long formId;


    @NotNull
    private AnomalyType anomalyType;


    /**
     * 供应商id
     */

    private Long customerId;
    /**
     * 品牌
     */
    private Long brandId;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 附件
     */
    @ApiModelProperty("附件")
    private String enclosure;

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
     * 物料
     */
    @ApiModelProperty("物料")
    @NotNull
    private Long skuId;

    /**
     * 需要数量
     */
    @NotNull
    @ApiModelProperty("需要数量")
    private Long needNumber;

    /**
     * 实际数量
     */
    @NotNull
    @ApiModelProperty("实际数量")
    private Long realNumber;

    /**
     * 原因说明
     */
    @ApiModelProperty("原因说明")
    private String reason;

    /**
     * 可以入库
     */
    @ApiModelProperty("可以入库 ")
    private Integer status;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Long createUser;

    @ApiModelProperty(hidden = true)
    private Date updateTime;

    @ApiModelProperty(hidden = true)
    private Long updateUser;

    @ApiModelProperty("")
    private Integer display;

    /**
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
