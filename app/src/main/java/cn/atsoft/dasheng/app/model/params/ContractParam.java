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
 * 合同表
 * </p>
 *
 * @author
 * @since 2021-07-21
 */
@Data
@ApiModel
public class ContractParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 合同id
     */
    @ApiModelProperty("合同id")
    private Long contractId;

    private Integer audit;


    private Long templateId;

    private Long partyA;

    private Long partyAAdressId;
    private Long partyBAdressId;
    private Long partyAContactsId;
    private Long partyBContactsId;

    private Long partyB;

    /**
     * 合同名称
     */
    @ApiModelProperty("合同名称")
    private String name;

    /**
     * 负责人id
     */
    @ApiModelProperty("负责人id")
    private String userId;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String note;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date time;

    /**
     * 内容
     */
    @ApiModelProperty("内容")
    private String content;

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
