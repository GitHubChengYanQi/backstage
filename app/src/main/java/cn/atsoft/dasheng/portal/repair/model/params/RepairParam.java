package cn.atsoft.dasheng.portal.repair.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 * <p>
 * 报修
 * </p>
 *
 * @author siqiang
 * @since 2021-08-20
 */
@Data
@ApiModel
public class RepairParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 报修id
     */
    @ApiModelProperty("报修id")
    private Long repairId;

    /**
     * 报修单位
     */
    @ApiModelProperty("报修单位")
    private Long companyId;

    /**
     * 保修部位图片
     */
    @ApiModelProperty("保修部位图片")
    private String itemImgUrl;

    /**
     * 设备id
     */
    @ApiModelProperty("设备id")
    private Long itemId;

    /**
     * 服务类型
     */
    @ApiModelProperty("服务类型")
    private String serviceType;

    /**
     * 期望到达日期
     */
    @ApiModelProperty("期望到达日期")
    private Date expectTime;

    /**
     * 描述
     */
    @ApiModelProperty("描述")
    private String comment;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;

    /**
     * 工程进度
     */
    @ApiModelProperty("工程进度")
    private Long progress;

    /**
     * 维修费用
     */
    @ApiModelProperty("维修费用")
    private Long money;

    /**
     * 质保类型
     */
    @ApiModelProperty("质保类型")
    private String qualityType;

    /**
     * 合同类型
     */
    @ApiModelProperty("合同类型")
    private String contractType;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
