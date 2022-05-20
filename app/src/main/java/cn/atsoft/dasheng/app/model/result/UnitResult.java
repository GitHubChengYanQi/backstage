package cn.atsoft.dasheng.app.model.result;

import cn.atsoft.dasheng.appBase.aop.FieldPermission;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * <p>
 * 单位表
 * </p>
 *
 * @author cheng
 * @since 2021-08-11
 */
@Data
@ApiModel
public class UnitResult implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 单位id
     */
    @ApiModelProperty("单位id")
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long unitId;

    /**
     * 单位名称
     */
    @ApiModelProperty("单位名称")
    @FieldPermission(value = true)
    private String unitName;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long updateUser;

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
     * 部门编号
     */
    @ApiModelProperty("部门编号")
    private Long deptId;
    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;

}
