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
 * 自定义合同变量
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-04-18
 */
@Data
@ApiModel
public class ContractTempleteParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;
    List<ContractTempleteDetailParam> detailParams;


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
    @ApiModelProperty("删除状态")
    private Integer display;

    @ApiModelProperty(hidden = true)
    private Long createUser;

    @ApiModelProperty(hidden = true)
    private Long updateUser;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Date updateTime;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
