package cn.atsoft.dasheng.dynamic.model.result;

import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import com.alibaba.fastjson.annotation.JSONField;
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
 * @author Captain_Jazz
 * @since 2022-08-10
 */
@Data
@ApiModel
public class DynamicResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private UserResult userResult;

    /**
     * 商机动态id
     */
    @ApiModelProperty("商机动态id")
    private Long dynamicId;

    private String sourceName;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    @JSONField(serialize = false)
    private Long userId;

    /**
     * 实物id
     */
    @ApiModelProperty("实物id")
    @JSONField(serialize = false)
    private Long inkindId;

    /**
     * 内容
     */
    @ApiModelProperty("内容")
    private String content;

    /**
     * 更改之后数据
     */
    @ApiModelProperty("更改之后数据")
    @JSONField(serialize = false)
    private String after;

    @ApiModelProperty("")
    private String type;

    /**
     * 更改之前数据
     */
    @ApiModelProperty("更改之前数据")
    @JSONField(serialize = false)
    private String before;

    /**
     * 来源
     */
    @ApiModelProperty("来源")
    private String source;

    /**
     * 来源id
     */
    @ApiModelProperty("来源id")
    @JSONField(serialize = false)
    private Long sourceId;

    /**
     * 物料id
     */
    @ApiModelProperty("物料id")
    @JSONField(serialize = false)
    private Long skuId;

    @ApiModelProperty("")
    @JSONField(serialize = false)
    private Long spuId;

    /**
     * 任务id
     */
    @ApiModelProperty("任务id")
    @JSONField(serialize = false)
    private Long taskId;

    /**
     * 仓库id
     */
    @ApiModelProperty("仓库id")
    @JSONField(serialize = false)
    private Long storehouseId;

    /**
     * 库位id
     */
    @ApiModelProperty("库位id")
    @JSONField(serialize = false)
    private Long storehousePositionsId;

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
    @ApiModelProperty("父ID顺序数组")
    @JSONField(serialize = false)
    private List<String> pidValue;
}
