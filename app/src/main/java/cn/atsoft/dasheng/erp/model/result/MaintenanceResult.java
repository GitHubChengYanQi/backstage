package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.app.entity.Material;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.MaterialResult;
import cn.atsoft.dasheng.app.model.result.PartsResult;
import cn.atsoft.dasheng.erp.entity.MaintenanceDetail;
import cn.atsoft.dasheng.erp.model.params.MaintenanceAndInventorySelectParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 养护申请主表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-06-28
 */
@Data
@ApiModel
public class MaintenanceResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private String coding;
    private String statusName;
    private Integer skuCount;
    private Integer numberCount;
    private Integer positionCount;
    private Integer doneNumberCount;
    private List<String> enclosureUrl;
    private UserResult userResult;
    private UserResult createUserResult;
    private List<MaintenanceDetailResult> maintenanceDetailResults;
    private List<StorehousePositionsResult> detailResultsByPositions;
    private List<AnnouncementsResult> announcementsResults;

    private List<MaintenanceAndInventorySelectParam> selectParamResults;
    private String selectParams;


    @ApiModelProperty("养护")
    private Long maintenanceId;

    private String notice;

    /**
     * name
     */
    @ApiModelProperty("name")
    private String maintenanceName;

    /**
     * 负责人
     */
    @ApiModelProperty("负责人")
    private Long userId;

    /**
     * 类型
     */
    @ApiModelProperty("类型")
    private String type;

    /**
     * 养护临近期
     */
    @ApiModelProperty("养护临近期")
    private Integer nearMaintenance;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private Date endTime;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false)
    private Long createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false)
    private Long updateUser;

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
    private Integer status;

    /**
     * 附件
     */
    @ApiModelProperty("附件")
    private String enclosure;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String note;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
