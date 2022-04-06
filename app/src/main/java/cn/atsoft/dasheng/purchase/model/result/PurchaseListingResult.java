package cn.atsoft.dasheng.purchase.model.result;

import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.sql.Time;
import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 采购清单
 * </p>
 *
 * @author song
 * @since 2021-12-15
 */
@Data
@ApiModel
public class PurchaseListingResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private SkuResult skuResult;


    private Long brandId;


    private BrandResult brandResult;


    private Time deliveryTime;


    private User user;
    /**
     * 采购清单id
     */

    @ApiModelProperty("采购清单id")
    private Long purchaseListingId;


    @JSONField(serialize = false)
    private Integer status;
    /**
     * 采购申请id
     */
    @JSONField(serialize = false)
    @ApiModelProperty("采购申请id")
    private Long purchaseAskId;


    @ApiModelProperty("")
    private Long skuId;

    /**
     * 申请数量
     */
    @ApiModelProperty("申请数量")
    private Long applyNumber;

    /**
     * 可用数量
     */
    @ApiModelProperty("可用数量")
    private Long availableNumber;

    /**
     * 交付日期
     */
    @ApiModelProperty("交付日期")
    private Date deliveryDate;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    @JSONField(serialize = false)
    private String note;

    /**
     * 创建时间
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 修改时间
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 创建用户
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 修改用户
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    /**
     * 状态
     */
    @JSONField(serialize = false)
    @ApiModelProperty("状态")
    private Long display;

    /**
     * 部门id
     */
    @JSONField(serialize = false)
    @ApiModelProperty("部门id")
    private Long deptId;
    @JSONField(serialize = false)
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
