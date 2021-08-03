package cn.atsoft.dasheng.app.model.params;

import cn.atsoft.dasheng.app.entity.CrmBusinessSalesProcess;
import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 * <p>
 * 销售
 * </p>
 *
 * @author 
 * @since 2021-08-02
 */
@Data
@ApiModel
public class CrmBusinessSalesParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

     private  List<CrmBusinessSalesProcess> getprocess;
    /**
     * 销售流程id
     */
    @ApiModelProperty("销售流程id")
    private Long salesId;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

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
