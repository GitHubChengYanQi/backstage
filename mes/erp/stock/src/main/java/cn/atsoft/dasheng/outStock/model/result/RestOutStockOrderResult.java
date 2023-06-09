package cn.atsoft.dasheng.outStock.model.result;

//import cn.atsoft.dasheng.app.model.result.StorehouseResult;
//import cn.atsoft.dasheng.erp.model.result.AnnouncementsResult;
//import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
//import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
//import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 领料单
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-25
 */
@Data
@ApiModel
public class RestOutStockOrderResult implements Serializable {
    @ApiModelProperty("是否有物料可以操作")
    private Boolean canOperate;

    @ApiModelProperty("相关的库位")
    private List<Long> positionIds;

    @ApiModelProperty("相关的库位")
    @JSONField(serialize = false)
    private List<Long> skuIds;

    @ApiModelProperty("是否可领料")
    private Boolean canPick;
    private String type;
    private static final long serialVersionUID = 1L;

//    @ApiModelProperty("任务")
//    private ProductionTaskResult productionTaskResult;

//    @ApiModelProperty("任务集合")
//    private List<ProductionTaskResult> productionTaskResults;

//    @ApiModelProperty("状态名称")
//    private UserResult createUserResult;

//    @ApiModelProperty("负责人")
//    private UserResult userResult;

    @ApiModelProperty("子表集合")
    @JSONField(serialize = false)
    private List<RestOutStockOrderDetailResult> detailResults;

    @ApiModelProperty("购物车集合")
    private List<RestOutStockCartResult> cartResults;

//    @ApiModelProperty("仓库返回集合")
//    private List<StorehouseResult> storehouseResults;

//    @ApiModelProperty("库位返回集合")
//    private List<StorehousePositionsResult> storehousePositionsResults;

    @ApiModelProperty("状态名称")
    private String statusName;

//    @ApiModelProperty("注意事项")
//    private List<AnnouncementsResult> announcementsResults;

    @ApiModelProperty("附件urls")
    private List<String> enclosureUrl;

//    @ApiModelProperty("物料返回集合")
//    private List<SkuSimpleResult> skuResults;

    @ApiModelProperty("出库单名称")
    private String pickListsName;

    @ApiModelProperty("总数")
    private Integer numberCount  ;

    @ApiModelProperty("已经完成总数")
    private Integer receivedCount ;

    @ApiModelProperty("购物车中未领取数量")
    private Integer cartNumCount ;

    @ApiModelProperty("sku总数")
    private Integer skuCount  ;

    @ApiModelProperty("仓库总数")
    private Integer positionCount ;
    
    @ApiModelProperty("任务id")
    private Long taskId;

    /**
     * 附件
     */
    @ApiModelProperty("附件")
    private String enclosure;
    /**
     * 附件
     */
    @ApiModelProperty("关联人（多）")
    private String userIds;
    /**
     * remarks
     */
    @ApiModelProperty("注意事项")
    private String remarks;

    /**
     * note
     */
    @ApiModelProperty("备注")
    private String note;

    /**
     * 领料单
     */
    @JSONField(serializeUsing= ToStringSerializer.class)
    @ApiModelProperty("领料单")
    private Long pickListsId;

    /**
     * 领取物料码
     */
    @ApiModelProperty("领取物料码")
    private String coding;

    @ApiModelProperty("")
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long userId;

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
     * 创建者
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 修改者
     */
    @JSONField(serialize = false)
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
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 状态
     */
    @JSONField(serialize = false)
    @ApiModelProperty("状态")
    private Integer display;

    /**
     * 部门id
     */
    @JSONField(serialize = false)
    @ApiModelProperty("部门id")
    private Long deptId;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Long status;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
