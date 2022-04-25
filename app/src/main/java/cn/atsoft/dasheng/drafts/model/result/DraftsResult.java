package cn.atsoft.dasheng.drafts.model.result;

import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 草稿箱
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-04-21
 */
@Data
@ApiModel
public class DraftsResult implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("")
    private String type;

    private UserResult createUserResult;


    /**
     * 草稿箱id
     */
    @ApiModelProperty("草稿箱id")
    private Long draftsId;

    @ApiModelProperty("")
    private String info;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false)
    private Long createUser;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false)
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
    @JSONField(serialize = false)
    private Date updateTime;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
