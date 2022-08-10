package cn.atsoft.dasheng.production.model.result;

import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.model.result.SkuRequest;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import cn.atsoft.dasheng.production.model.request.StoreHouseNameAndSkuNumber;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 领料单详情表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-25
 */
@Data
@ApiModel
public class ProductionPickListsDetailResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private SkuSimpleResult skuResult;
    private List<BrandResult> brandResults;
    private List<StoreHouseNameAndSkuNumber> positionAndStockDetail;
    private Integer status;
    private String pickListsName;
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long storehousePositionsId;
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long storehouseId;
    private UserResult userResult;
    private String pickListsCoding;
    private Integer stockNumber = 0;
    private Boolean isMeet;
    private List<ProductionPickListsCartResult> cartResults;
    private Map<String,String> brandResult;
    private ProductionPickListsResult pickListsResult;
    private Boolean canPick;
    /**
     * 子表id
     */
    @ApiModelProperty("子表id")
    @JSONField(serializeUsing= ToStringSerializer.class)

    private Long pickListsDetailId;

    @JSONField(serializeUsing= ToStringSerializer.class)
    private Integer receivedNumber;

    /**
     * 主表id
     */
    @ApiModelProperty("主表id")
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long pickListsId;

    /**
     * 物料id
     */
    @ApiModelProperty("物料id")
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long skuId;

    @ApiModelProperty("")
    private Integer number;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false,serializeUsing= ToStringSerializer.class)

    private Long createUser;

    /**
     * 修改者
     */

    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false,serializeUsing= ToStringSerializer.class)
    private Long updateUser;


    /**
     * 品牌
     */
    @ApiModelProperty("品牌")
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long brandId;



    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 修改时间
     */
    @JSONField(serialize = false,serializeUsing= ToStringSerializer.class)
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 状态
     */
    @JSONField(serialize = false,serializeUsing= ToStringSerializer.class)
    @ApiModelProperty("状态")
    private Integer display;

    /**
     * 部门id
     */
    @JSONField(serialize = false,serializeUsing= ToStringSerializer.class)
    @ApiModelProperty("部门id")
    private Long deptId;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
