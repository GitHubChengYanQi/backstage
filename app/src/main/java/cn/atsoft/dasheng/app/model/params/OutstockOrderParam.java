package cn.atsoft.dasheng.app.model.params;

import cn.atsoft.dasheng.erp.entity.ApplyDetails;
import cn.atsoft.dasheng.erp.model.params.OutstockListingParam;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 出库单
 * </p>
 *
 * @author cheng
 * @since 2021-08-16
 */
@Data
@ApiModel
public class OutstockOrderParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private List<OutstockListingParam> listingParams;

    /**
     * 主题
     */
    @ApiModelProperty("主题")
    private String theme;

    /**
     * 来源
     */
    @ApiModelProperty("来源")
    private String origin;

    /**
     * 来源
     */
    private String source;

    /**
     * 来源id
     */
    private Long sourceId;


    private String url;
    /**
     * 出库详细id
     */
    @ApiModelProperty("出库详细id")
    private Long outstockOrderId;

    private List<ApplyDetails> applyDetails;

    private Long outstockApplyId;

    private Long storehouseId;

    /**
     * 出库状态
     */
    @ApiModelProperty("出库状态")
    private Integer state;
    /**
     * 经手人
     */
    @ApiModelProperty("经手人")
    private Long userId;
    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String note;
    private String coding;
    /**
     * /**
     * 计划出库时间
     */
    @ApiModelProperty("计划出库时间")
    private Date time;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 修改者
     */
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
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
    private Long deptId;

    @Override
    public String checkParam() {
        return null;
    }

}
