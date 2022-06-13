package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
public class AnomalyResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private String checkNumber;

    private User user;

    private Long otherNumber;

    private Long errorNumber;

    private SkuResult skuResult;

    private SkuSimpleResult skuSimpleResult;

    private Brand brand;

    private Customer customer;

    private Long instockNumber;

    private List<AnomalyDetailResult> details;

    private List<String> filedUrls;

    @ApiModelProperty("")
    private Long anomalyId;

    @ApiModelProperty("")
    private String type;

    @ApiModelProperty("")
    private Long formId;

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
     * 供应商id
     */

    private Long customerId;
    /**
     * 品牌
     */
    private Long brandId;

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
    private Long skuId;

    /**
     * 需要数量
     */
    @ApiModelProperty("需要数量")
    private Long needNumber;

    /**
     * 实际数量
     */
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
}
