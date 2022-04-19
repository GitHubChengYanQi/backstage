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
public class ContractTempleteResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<ContractTempleteDetailResult> detailResults;

    @ApiModelProperty("")
    private Long contractTemplateId;

    /**
     * 类型
     */
    @ApiModelProperty("类型")
    private String type;

    /**
     * 是否显示
     */
    @ApiModelProperty("是否显示")
    private Integer isHidden;

    /**
     * 标题名称
     */
    @ApiModelProperty("标题名称")
    private String name;

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
