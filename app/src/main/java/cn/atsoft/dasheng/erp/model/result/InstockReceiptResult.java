package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.sys.modular.system.entity.User;
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
 * 入库记录单
 * </p>
 *
 * @author song
 * @since 2022-08-11
 */
@Data
@ApiModel
public class InstockReceiptResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, List<InstockLogDetailResult>> customerMap;

    private User user;

    private String source;
    /**
     * 物品编号
     */
    @ApiModelProperty("物品编号")
    private Long receiptId;

    /**
     * 编码
     */
    @ApiModelProperty("编码")
    private String coding;

    /**
     * 入库单
     */
    @ApiModelProperty("入库单")
    private Long instockOrderId;

    /**
     * 审批人
     */
    @ApiModelProperty("审批人")
    private String auditPerson;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Long createUser;

    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    @JSONField(serialize = false)
    @ApiModelProperty("")
    private Integer display;

    /**
     * 部门编号
     */
    @JSONField(serialize = false)
    @ApiModelProperty("部门编号")
    private Long deptId;

    @JSONField(serialize = false)
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
