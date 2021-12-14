package cn.atsoft.dasheng.erp.model.params;

import cn.atsoft.dasheng.erp.entity.QualityTaskDetail;
import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 质检任务拒检
 * </p>
 *
 * @author song
 * @since 2021-12-14
 */
@Data
@ApiModel
public class QualityTaskRefuseParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private List<QualityTaskDetailParam> detailParams;


    @ApiModelProperty("")
    private Long refuseId;

    private String note;

    /**
     * 质检任务id
     */
    @ApiModelProperty("质检任务id")
    private Long qualityTaskId;

    /**
     * 质检任务详情id
     */
    @ApiModelProperty("质检任务详情id")
    private Long qualityTaskDetailId;

    /**
     * 物料id
     */
    @ApiModelProperty("物料id")
    private Long skuId;

    /**
     * 品牌
     */
    @ApiModelProperty("品牌")
    private Long brandId;

    /**
     * 拒绝数量
     */
    @ApiModelProperty("拒绝数量")
    private Long number;

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

    @Override
    public String checkParam() {
        return null;
    }

}
