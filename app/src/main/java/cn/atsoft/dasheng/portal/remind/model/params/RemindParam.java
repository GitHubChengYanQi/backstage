package cn.atsoft.dasheng.portal.remind.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 提醒表
 * </p>
 *
 * @author cheng
 * @since 2021-08-26
 */
@Data
@ApiModel
public class RemindParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private String templateType;


    /**
     * 提醒id
     */
    @ApiModelProperty("提醒id")
    private Long remindId;

    private List<Long> users;

    /**
     * 提醒类型
     */
    @ApiModelProperty("提醒类型")
    private Long type;

    /**
     * 提醒人
     */
    @ApiModelProperty("提醒人")
    private Long userId;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

    private WxTemplateData template;
    /**
     * 部门id
     */
    @ApiModelProperty("部门Id")
    private Long deptId;

    @Override
    public String checkParam() {
        return null;
    }

}
