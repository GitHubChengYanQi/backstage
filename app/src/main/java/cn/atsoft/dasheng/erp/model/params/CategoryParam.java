package cn.atsoft.dasheng.erp.model.params;

import cn.atsoft.dasheng.app.model.params.Attribute;
import cn.atsoft.dasheng.base.dict.AbstractDictMap;
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
 * 物品分类表
 * </p>
 *
 * @author jazz
 * @since 2021-10-18
 */
@Data
@ApiModel
public class CategoryParam extends AbstractDictMap implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private List<ItemAttributeParam> itemAttributeParams;

    /**
     * 排序
     */
    private Long sort;

    /**
     * 物品类目Id
     */
    @ApiModelProperty("物品类目Id")
    private Long categoryId;

    /**
     * 上级分类id
     */
    @ApiModelProperty("上级分类id")
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


    private String children;

    private String childrens;

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
