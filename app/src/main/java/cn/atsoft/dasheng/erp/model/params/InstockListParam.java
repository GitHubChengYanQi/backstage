package cn.atsoft.dasheng.erp.model.params;

import cn.atsoft.dasheng.base.dict.AbstractDictMap;
import cn.atsoft.dasheng.erp.pojo.InstockListRequest;
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
 * 入库清单
 * </p>
 *
 * @author song
 * @since 2021-10-06
 */
@Data
@ApiModel
public class InstockListParam extends AbstractDictMap implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private String type;

    private Long codeId;

    private Long num;

    private Long instockNumber;

    private List<Long> inkindIds;

    private Boolean batch = false;

    private List<InstockListRequest> requests;

    private Long inkind;

    @NotNull
    private Long cartId;

    private String anomalyHandle;
    /**
     * 供应商
     */
    private Long customerId;

    /**
     * 实际数量
     */
    private Long realNumber;

    /**
     * 库位id
     */
    private Long storehousePositionsId;
    /**
     * skuId
     */
    @NotNull
    private Long skuId;

    private Long inkindId;

    /**
     * 入库清单
     */
    @NotNull
    @ApiModelProperty("入库清单")
    private Long instockListId;

    private Integer costPrice;

    private Integer sellingPrice;

    private Long storeHouseId;

    /**
     * 品牌id
     */
    @ApiModelProperty("品牌id")
    private Long brandId;

    /**
     * 产品id
     */

    private Long status;

    /**
     * 数量
     */
    @NotNull
    @ApiModelProperty("数量")
    private Long number;


    /**
     * 到货时间
     */
    @ApiModelProperty("到货时间")
    private Date receivedDate;

    /**
     * 有效日期
     */
    @ApiModelProperty("有效日期")
    private Date effectiveDate;

    /**
     * 生产日期
     */
    @ApiModelProperty("生产日期")
    private Date manufactureDate;

    /**
     * 批号
     */
    @ApiModelProperty("批号")
    private String lotNumber;

    /**
     * 流水号
     */
    @ApiModelProperty("流水号")
    private String serialNumber;

    /**
     * 入库单id
     */
    @ApiModelProperty("入库单id")
    private Long instockOrderId;

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

    private Long orderDetailId;

    /**
     * 到货数量
     */
    private  Integer arrivalNumber;

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

    @Override
    public void init() {

    }

    @Override
    protected void initBeWrapped() {

    }
}
