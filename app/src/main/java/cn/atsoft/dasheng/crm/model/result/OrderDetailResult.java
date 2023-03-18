package cn.atsoft.dasheng.crm.model.result;

import cn.atsoft.dasheng.app.entity.Contract;
import cn.atsoft.dasheng.app.entity.Unit;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.entity.Order;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.taxRate.entity.TaxRate;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 订单明细表
 * </p>
 *
 * @author song
 * @since 2022-02-23
 */
@Data
@ApiModel
public class OrderDetailResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private String sign; //币种

    private OrderResult orderResult;
    private SkuResult skuResult;
    private BrandResult brandResult;
    private CustomerResult customerResult;
    private Integer inStockNumber;
    private Unit unit;
    private TaxRate taxRate;
    private String remark;

    private Contract contract;

    /**
     * 合同id
     */
    private Long contractId;

    /**
     * 详情id
     */
    @ApiModelProperty("详情id")
    private Long detailId;

    /**
     * 订单id
     */
    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("")
    private Long skuId;

    @ApiModelProperty("")
    private Long brandId;

    @ApiModelProperty("")
    private Long customerId;

    /**
     * 预购数量
     */
    @ApiModelProperty("预购数量")
    private Long preordeNumber;

    /**
     * 采购数量
     */
    @ApiModelProperty("采购数量")
    private Integer purchaseNumber;

    /**
     * 单位id
     */
    @ApiModelProperty("单位id")
    @JSONField(serialize = false)
    private Long unitId;

    /**
     * 单价
     */
    @ApiModelProperty("单价")
    private Double onePrice;

    /**
     * 总价
     */
    @ApiModelProperty("总价")
    private Double totalPrice;

    /**
     * 票据类型
     */
    @ApiModelProperty("票据类型")
    private Integer paperType;

    /**
     * 锐率
     */
    @ApiModelProperty("锐率")
    private Long rate;

    /**
     * 交货日期
     */
    @ApiModelProperty("交货日期")
    private Integer deliveryDate;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false)
    private Long createUser;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false)
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
    @JSONField(serialize = false)
    private Date updateTime;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    @JSONField(serialize = false)
    private Integer display;

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    @JSONField(serialize = false)
    private Long deptId;

    /**
     * 到货数量
     */
    @ApiModelProperty("到货数量")
    private Integer arrivalNumber;

    @ApiModelProperty("父ID顺序数组")
    @JSONField(serialize = false)
    private List<String> pidValue;
    public Double getOnePrice() {
        if (ToolUtil.isEmpty(onePrice)){
            return 0.0;
        }
        return BigDecimal.valueOf(onePrice).divide(BigDecimal.valueOf(100),2).doubleValue();
    }


    public Double getTotalPrice() {
        if (ToolUtil.isEmpty(totalPrice)){
            return 0.0;
        }
        return BigDecimal.valueOf(totalPrice).divide(BigDecimal.valueOf(100),2).doubleValue();
    }
}
