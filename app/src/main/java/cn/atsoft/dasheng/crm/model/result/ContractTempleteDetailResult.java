package cn.atsoft.dasheng.crm.model.result;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 自定义合同变量
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-04-18
 */
@Data
@ApiModel
public class ContractTempleteDetailResult implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("")
    private Long contractTempleteDetailId;

    @ApiModelProperty("")
    private Long contractTempleteId;

    /**
     * 值
     */
    @ApiModelProperty("值")
    private String value;

    /**
     * 是否为默认值
     */
    @ApiModelProperty("是否为默认值")
    private Integer isDefault;

    /**
     * 删除状态
     */
    @JSONField(serialize = false)
    @ApiModelProperty("删除状态")
    private Integer display;
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Long createUser;
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Long updateUser;
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Date createTime;
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Date updateTime;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
