package cn.atsoft.dasheng.general.model.result;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel
public class ClassListResult implements Serializable {

    @JSONField(serialize = false)
    private String childrens;

    private Long formStyleId;

    /**
     * 编码分类
     */
    private String codingClass;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * pid
     */
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long pid;

    @ApiModelProperty("spu分类id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long spuClassificationId;

    /**
     * 是否是产品分类
     */
    @ApiModelProperty("是否是产品分类")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long type;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

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
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long createUser;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long updateUser;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer display;

    private Long deptId;

}
