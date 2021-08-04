package cn.atsoft.dasheng.app.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 * <p>
 * 销售流程
 * </p>
 *
 * @author 
 * @since 2021-08-04
 */
@Data
@ApiModel
public class CrmBusinessSalesProcessParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 赢率id
     */
    @ApiModelProperty("赢率id")
    private Long salesProcessId;

    /**
     * 流程名称
     */
    @ApiModelProperty("流程名称")
    private String name;

    /**
     * 百分比
     */
    @ApiModelProperty("百分比")
    private Integer percentage;

    /**
     * 流程id
     */
    @ApiModelProperty("流程id")
    private Long salesId;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

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
