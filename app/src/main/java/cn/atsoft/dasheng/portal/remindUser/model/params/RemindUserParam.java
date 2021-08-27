package cn.atsoft.dasheng.portal.remindUser.model.params;

import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author c
 * @since 2021-08-27
 */
@Data
@ApiModel
public class RemindUserParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 消息提醒用户id
     */
    @ApiModelProperty("消息提醒用户id")
    private Long remindUserId;

    /**
     * 消息提醒id
     */
    @ApiModelProperty("消息提醒id")
    private Long remindId;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

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
