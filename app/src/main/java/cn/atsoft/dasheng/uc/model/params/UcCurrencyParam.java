package cn.atsoft.dasheng.uc.model.params;

import cn.atsoft.dasheng.portal.model.validator.BaseValidatingParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author Sing
 * @since 2021-03-22
 */
@Data
@ApiModel
public class UcCurrencyParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("")
    private Long currencyId;

    @ApiModelProperty("")
    private String name;

    @ApiModelProperty("")
    private Integer status;

    @ApiModelProperty("")
    private Integer rate;

    @ApiModelProperty("")
    private Integer display;

    @ApiModelProperty(hidden = true)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Date updateTime;

    @ApiModelProperty(hidden = true)
    private Long createUser;

    @ApiModelProperty(hidden = true)
    private Long updateUser;

    @ApiModelProperty("")
    private Integer isDefault;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

}
