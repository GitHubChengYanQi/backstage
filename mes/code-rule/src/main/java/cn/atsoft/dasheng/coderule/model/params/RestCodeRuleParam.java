package cn.atsoft.dasheng.coderule.model.params;

import cn.atsoft.dasheng.base.dict.AbstractDictMap;
import cn.atsoft.dasheng.coderule.model.RestCodingSerial;
import cn.atsoft.dasheng.coderule.model.RestCodings;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 编码规则
 * </p>
 *
 * @author song
 * @since 2021-10-22
 */
@Data
@ApiModel
public class RestCodeRuleParam extends AbstractDictMap implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    /**
     * 流水号
     */
    private String serial;

    private List<RestCodingSerial> codingSerials;

    /**
     * 状态
     */
    private Integer state;
    /**
     * 排序
     */
    private Long sort;

    private Integer module;

    private List<RestCodings> codings;

    /**
     * 编码规则id
     */
    @ApiModelProperty("编码规则id")
    private Long codingRulesId;

    /**
     * 编码规则分类id
     */
    @ApiModelProperty("编码规则分类id")
    private Long codingRulesClassificationId;

    /**
     * 编码规则名称
     */
    @ApiModelProperty("编码规则名称")
    private String name;

    @ApiModelProperty("描述")
    private String note;

    /**
     * 编码规则
     */
    @ApiModelProperty("编码规则")
    private String codingRules;

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

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    private Long deptId;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    @Override
    public String checkParam() {
        return null;
    }

    @Override
    public void init() {

    }

    @Override
    protected void initBeWrapped() {

    }
}
