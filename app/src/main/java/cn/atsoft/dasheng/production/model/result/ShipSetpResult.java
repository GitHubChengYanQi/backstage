package cn.atsoft.dasheng.production.model.result;

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
 * 工序表
 * </p>
 *
 * @author song
 * @since 2022-02-10
 */
@Data
@ApiModel
public class ShipSetpResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private UserResult userResult;

    private ShipSetpClassResult shipSetpClassResult;

    private List<ShipSetpBindResult> binds;
    private SopResult sopResult;
    private Long sopId;
    private Integer productionType;


    /**
     * 工序
     */
    @ApiModelProperty("工序")
    private Long shipSetpId;

    /**
     * 工序名称
     */
    @ApiModelProperty("工序名称")
    private String shipSetpName;

    /**
     * 工序分类
     */
    @ApiModelProperty("工序分类")
    private Long shipSetpClassId;

    /**
     * 工序名称
     */
    @ApiModelProperty("编码")
    private String coding;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 人员id
     */
    @ApiModelProperty("人员id")
    private Long userId;

    /**
     * 工位id
     */
    @ApiModelProperty("工位id")
    private Long productionStationId;

    /**
     * 附件
     */
    @ApiModelProperty("附件")
    private String accessories;

    /**
     * 创建者
     */
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
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
