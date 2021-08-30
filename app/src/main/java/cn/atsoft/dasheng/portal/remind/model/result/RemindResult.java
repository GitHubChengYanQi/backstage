package cn.atsoft.dasheng.portal.remind.model.result;

import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import lombok.Data;
import java.util.Date;
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
public class RemindResult implements Serializable {

    private static final long serialVersionUID = 1L;


    private  String subscriptionType;

    /**
     * 提醒id
     */
    @ApiModelProperty("提醒id")
    private Long remindId;

    private UserResult userResults;
    private List<User> users;

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
}
