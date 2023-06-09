package cn.atsoft.dasheng.app.model.result;

import cn.atsoft.dasheng.app.model.params.ErpPartsDetailParam;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.model.result.BackSku;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import cn.atsoft.dasheng.erp.model.result.SpuResult;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.core.conditions.interfaces.Compare;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 清单
 * </p>
 *
 * @author song
 * @since 2021-10-21
 */
@Data
@ApiModel
public class PartsResult implements Serializable {

    private static final long serialVersionUID = 1L;
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long skuId;
    private Long productionTaskId;
    private List<BackSku> backSkus;
    private SkuResult skuResult;
    private UserResult userResult;
    private SpuResult spuResult;
    private Integer status;
    private Item item;
    private Long parentId;
    private Integer done;
    //零件
    private List<PartsResult> partsResults;

    private List<ErpPartsDetailResult> parts;


    private String name;

    private String version;

    private Double bomNum;  //配套数量

    private String children;

    private String type;


    private String childrens;

    private Sku sku;


    @ApiModelProperty("关联sku")
    private String skus;
    /**
     * 清单id
     */
    @ApiModelProperty("清单id")
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long partsId;

    private String partsAttributes;

    /**
     * 物料名称
     */
    @ApiModelProperty("物料名称")
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long spuId;

    /**
     * 规格描述
     */
    @ApiModelProperty("规格描述")
    private String attribute;

    /**
     * 组成物品id
     */
    @ApiModelProperty("组成物品id")
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long pid;

    /**
     * 零件数量
     */
    @ApiModelProperty("零件数量")
    private Integer number;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long createUser;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serializeUsing= ToStringSerializer.class)
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
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long deptId;


    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String note;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
