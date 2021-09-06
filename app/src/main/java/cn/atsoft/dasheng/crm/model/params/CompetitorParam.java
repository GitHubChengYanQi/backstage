package cn.atsoft.dasheng.crm.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 竞争对手管理
 * </p>
 *
 * @author 
 * @since 2021-09-06
 */
@Data
@ApiModel
public class CompetitorParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 竞争对手id
     */
    @ApiModelProperty("竞争对手id")
    private Long competitorId;

    /**
     * 竞争对手企业名称
     */
    @ApiModelProperty("竞争对手企业名称")
    private String name;

    /**
     * 竞争对手企业性质
     */
    @ApiModelProperty("竞争对手企业性质")
    private Integer nature;
    private  Long businessId;

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