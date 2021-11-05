package cn.atsoft.dasheng.app.model.result;

import cn.atsoft.dasheng.app.model.params.Spu;
import cn.atsoft.dasheng.app.model.params.SpuAttribute;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.model.result.BackSku;
import cn.atsoft.dasheng.erp.model.result.SpuResult;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 清单详情
 * </p>
 *
 * @author cheng
 * @since 2021-10-26
 */
@Data
@ApiModel
public class ErpPartsDetailResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<BackSku> backSkus;
    private SpuResult spuResult;

    private Boolean isNull;

    private Sku sku;
    /**
     * 清单详情id
     */
    @ApiModelProperty("清单详情id")
    private Long partsDetailId;

    private String partsAttributes;

    /**
     * 规格描述
     */
    @ApiModelProperty("规格描述")
    private String attribute;

    private Long skuId;

    /**
     * 组成物品id
     */
    @ApiModelProperty("组成物品id")
    private Long spuId;

    @ApiModelProperty("清单id")
    private Long partsId;

    /**
     * 零件数量
     */
    @ApiModelProperty("零件数量")
    private Integer number;

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
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;

    /**
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String note;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
