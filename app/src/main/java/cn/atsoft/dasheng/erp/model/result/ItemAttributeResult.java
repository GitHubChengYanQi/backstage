package cn.atsoft.dasheng.erp.model.result;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 产品属性表
 * </p>
 *
 * @author song
 * @since 2021-10-18
 */
@Data
@ApiModel
public class ItemAttributeResult implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<AttributeValuesResult> attributeValuesResults;


    private SpuResult spuResult;
    /**
     * 属性id
     */
    @ApiModelProperty("属性id")
    private Long attributeId;
    private Long categoryId;

    /**
     * 属性名
     */
    @ApiModelProperty("属性名")
    private String attribute;

    /**
     * 产品id
     */
    @ApiModelProperty("产品id")
    private Long itemId;

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
     * 排序
     */
    @ApiModelProperty("排序")
    private Long sort;


    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
