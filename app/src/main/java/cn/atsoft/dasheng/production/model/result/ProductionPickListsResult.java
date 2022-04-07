package cn.atsoft.dasheng.production.model.result;

import cn.atsoft.dasheng.LongJsonSerializer.LongJsonDeserializer;
import cn.atsoft.dasheng.LongJsonSerializer.LongJsonSerializer;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Map;

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
public class ProductionPickListsResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private ProductionTaskResult productionTaskResult;
    private List<ProductionTaskResult> productionTaskResults;
    private UserResult createUserResult;
    private UserResult userResult;
    private List<ProductionPickListsDetailResult> detailResults;
    private List<ProductionPickListsCartResult> cartResults;
    private List<StorehouseResult> storehouseResults;
    private List<StorehousePositionsResult> storehousePositionsResults;

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
    @JSONField(serialize = false)
    @ApiModelProperty("来源")
    private String source;

    /**
     * 来源id
     */
    @JSONField(serialize = false)
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
    private Integer status;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
