package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.erp.model.params.CategoryRequest;
import cn.atsoft.dasheng.erp.model.params.ItemAttributeParam;
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
 * 物品分类表
 * </p>
 *
 * @author jazz
 * @since 2021-10-18
 */
@Data
@ApiModel
public class CategoryResult implements Serializable {

    /**
     * 排序
     */
    private Long sort;

    private static final long serialVersionUID = 1L;
    private CategoryResult pidCategoryResult;
    private List<CategoryRequest> categoryRequests;
    private List<ItemAttributeParam> itemAttributeParams;


    /**
     * 物品类目Id
     */
    @ApiModelProperty("物品类目Id")
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long categoryId;

    /**
     * 上级分类id
     */
    @ApiModelProperty("上级分类id")
    @JSONField(serializeUsing= ToStringSerializer.class)
    private Long pid;

    /**
     * 物品类目名称
     */
    @ApiModelProperty("物品类目名称")
    private String categoryName;

    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false)
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

    @JSONField(serialize = false,serializeUsing= ToStringSerializer.class)
    private Long updateUser;

    /**
     * 修改时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;

    /**
     * 状态
     */
    @JSONField(serialize = false)
    @ApiModelProperty("状态")
    private Integer display;
    @JSONField(serialize = false)
    private String children;
    @JSONField(serialize = false)
    private String childrens;

    @ApiModelProperty("父ID顺序数组")
    private List<String> pidValue;
}
