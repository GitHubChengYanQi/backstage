package cn.atsoft.dasheng.production.model.result;

import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
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
/**
 * <p>
 * 生产任务
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-22
 */
@Data
@ApiModel
public class ProductionTaskResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private UserResult createUserResult;
    private SkuSimpleResult skuResult;
    private UserResult userResult;
    private List<UserResult> userResults;

    private Long parentPartsId;
    private Long partsId;
    private ProductionWorkOrderResult workOrderResult;

    private ShipSetpResult shipSetpResult;
    @JSONField(serialize = false)
    private List<ProductionTaskDetailResult> taskDetailResults;


    /**
     * 生产任务id
     */
    @ApiModelProperty("生产任务id")
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long productionTaskId;

    /**
     * 编码
     */
    @ApiModelProperty("编码")
    private String coding;


    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private Date endTime;

    /**
     * 生产任务名称
     */
    @ApiModelProperty("生产任务名称")
    private String productionTaskName;

    @ApiModelProperty("")
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long skuId;

    /**
     * 数量
     */
    @ApiModelProperty("数量")
    private Integer number;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 负责人
     */
    @ApiModelProperty("负责人")
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long userId;

    /**
     * 工单id
     */
    @ApiModelProperty("工单id")
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long workOrderId;

    /**
     * 工序id
     */
    @ApiModelProperty("工序id")
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long shipSetpId;

    /**
     * 单台生产周期(天)
     */
    @ApiModelProperty("单台生产周期(天)")
    private Integer singleProductionCycle;

    /**
     * 生产时间
     */
    @ApiModelProperty("生产时间")
    private Date productionTime;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long createUser;

    /**
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long deptId;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;

    /**
     * 修改时间
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 修改者
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Long updateUser;

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

    private Integer status;

    /**
     * 成员
     */
    @ApiModelProperty("成员")
    private String userIds;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
