package cn.atsoft.dasheng.erp.model.params;

import cn.atsoft.dasheng.app.model.params.Attribute;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 产品属性数据表
 * </p>
 *
 * @author song
 * @since 2021-10-18
 */
@Data
@ApiModel
public class AttributeValuesParam implements Serializable, BaseValidatingParam,Comparable<AttributeValuesParam> {

    private static final long serialVersionUID = 1L;
    /**
     * 属性Id
     */
    @Override
    public int compareTo(AttributeValuesParam o) {
        return (int)(o.getAttributeId() - this.attributeId);
    }
    private Long attributeId;

    /**
     * 属性值id
     */
    @ApiModelProperty("属性值id")
    private Long attributeValuesId;

    /**
     * 属性值
     */
    @ApiModelProperty("属性值")
    private String attributeValues;

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
