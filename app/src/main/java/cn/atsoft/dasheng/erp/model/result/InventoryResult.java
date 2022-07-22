package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.erp.entity.Announcements;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 盘点任务主表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-27
 */
@Data
@ApiModel
public class InventoryResult implements Serializable {

    private Long inkindId;

    private static final long serialVersionUID = 1L;

    private StorehousePositionsResult positionsResult;

    private Integer total;

    private Integer handle;

    private List<Announcements> announcements; //盘点原由

    private List<User> participantList;   //参与人

    private User principal; //负责人

    private List<String> mediaUrls; //附件

    private String statusName;

    private Long status;

    private Object taskList;

    private String coding;

    private int skuSize;

    private int positionSize;

    private List<InventoryDetailResult> detailResults;

    private String enclosure;

    private String participants;

    private String notice;

    private Long positionId;

    private User user;

    private Date endTime;


    private Date beginTime;

    private Long userId;


    private String mode;


    private String method;
    /**
     * 盘点任务id
     */
    @ApiModelProperty("盘点任务id")
    private Long inventoryTaskId;

    /**
     * 盘点任务名称
     */
    @ApiModelProperty("盘点任务名称")
    private String inventoryTaskName;

    /**
     * 盘点任务备注
     */
    @ApiModelProperty("盘点任务备注")
    private String remark;

    /**
     * 删除状态
     */
    @ApiModelProperty("删除状态")
    @JSONField(deserialize = false)
    private Integer display;

    @ApiModelProperty(hidden = true)
    @JSONField(deserialize = false)
    private Long createUser;

    @ApiModelProperty(hidden = true)
    @JSONField(deserialize = false)
    private Long updateUser;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    @JSONField(deserialize = false)
    private Date updateTime;

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    private Long deptId;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
