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
 * 资料
 * </p>
 *
 * @author song
 * @since 2021-09-11
 */
@Data
@ApiModel
public class DataParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private List<Long> itemId;
    /**
     * 资料id
     */
    @ApiModelProperty("资料id")
    private Long dataId;


    private  String name;
    /**
     * 内容
     */
    @ApiModelProperty("内容")
    private String content;

    /**
     * 附件
     */
    @ApiModelProperty("附件")
    private String attachment;

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
