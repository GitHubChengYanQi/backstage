package cn.atsoft.dasheng.erp.model.params;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

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
public class MaintenanceParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;
    List<MaintenanceDetailParam> detailParams;
    //材质id
    private String  materialIds;


    /**
     * 品牌id
     */
    private String brandIds;

    /**
     * 库位id
     */
    private String storehousePositionsIds;
    /**
     * 库位id
     */
    private String partsIds;

    /**
     * 库位id
     */
    private String spuClassificationIds;
    private String coding;
    /**
     * 养护
     */
    @ApiModelProperty("养护")
    private Long maintenanceId;

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

    @Override
    public String checkParam() {
        return null;
    }

}
