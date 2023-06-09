package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.app.model.result.StorehouseResult;
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
 * 调拨主表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-07-13
 */
@Data
@ApiModel
public class AllocationResult implements Serializable {

    private List<SkuSimpleResult> skuResults;
    private static final long serialVersionUID = 1L;
    private List<AllocationDetailResult> detailResults;
    private Integer haveBrand;
    private List<AllocationCartResult> allocationCartResults;
    private StorehouseResult storehouseResult;
    private Integer detailNumber;
    private Integer doneNumber;
    private String statusName;
    private UserResult userResult;
    @ApiModelProperty("sku总数")
    private Integer skuCount  ;
    @ApiModelProperty("仓库总数")
    private Integer positionCount ;


    /**
     * 调拨id
     */
    @ApiModelProperty("调拨id")
    private Long allocationId;

    /**
     * 编码
     */
    @ApiModelProperty("编码")
    private String coding;

    @ApiModelProperty("")
    private String allocationName;

    /**
     * 仓库id
     */
    @ApiModelProperty("仓库id")
    private Long storehouseId;

    /**
     * 负责人
     */
    @ApiModelProperty("负责人")
    private Long userId;

    @ApiModelProperty("")
    private String reason;

    @ApiModelProperty("")
    private String remark;

    @ApiModelProperty("")
    private String note;

    private Integer allocationType;

    /**
     * 附件
     */
    @ApiModelProperty("附件")
    private String enclosure;

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
    @JSONField(serialize = false)
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
     * 状态
     */
    @ApiModelProperty("状态")
    private Long status;

    /**
     * 主题
     */
    @ApiModelProperty("主题")
    @JSONField(serialize = false)
    private String theme;

    /**
     * 来源Json字符串
     */
    @ApiModelProperty("来源Json字符串")
    @JSONField(serialize = false)
    private String origin;

    /**
     * 库间调拨  仓库调拨
     */
    @ApiModelProperty("库间调拨  仓库调拨")
    private String type;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
