package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.purchase.pojo.ThemeAndOrigin;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 养护记录
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-08-11
 */
@Data
@ApiModel
public class MaintenanceLogResult implements Serializable {

    private static final long serialVersionUID = 1L;
    List<MaintenanceLogDetailResult> detailResults;
    private String coding;

    private Long skuId;

    private Long inkindId;

    private String noticeIds;

    private SkuResult skuResult;

    private InkindResult inkindResult;

    List<AnnouncementsResult> announcementsResults;

    @ApiModelProperty("")
    private Long maintenanceLogId;


    private String origin;

    private String theme;
    private String remark;

    private String source;

    private Long sourceId;

    private ThemeAndOrigin themeAndOrigin;

    private UserResult createUserResult;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
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
    private Long updateUser;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    private Long deptId;

    @ApiModelProperty("")
    private Long maintenanceDetailId;

    @ApiModelProperty("")
    private Long maintenanceId;

    /**
     * 附件
     */
    @ApiModelProperty("附件")
    private String enclosure;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
