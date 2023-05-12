package cn.atsoft.dasheng.goods.category.model.params;

import cn.atsoft.dasheng.base.dict.AbstractDictMap;
import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * SPU分类
 * </p>
 *
 * @author song
 * @since 2021-10-25
 */
@Data
@ApiModel
public class RestCategoryParam extends AbstractDictMap implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;

    private String children;

    private String typeSetting;

    private Integer isNotproduct;

    private List<Sort> sortParam;

    private String childrens;
    /**
     * 编码分类
     */
    private String codingClass;
    /**
     * pid
     */
    private Long pid;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * spu分类id
     */
    @ApiModelProperty("spu分类id")
    private Long spuClassificationId;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 是否是产品分类
     */
    @ApiModelProperty("是否是产品分类")
    private Long type;

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
    private Long createUser;

    /**
     * 修改者
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

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
    @Data
    public static class Sort{
        public Integer sort;
        public Long spuClassificationId;
    }
}
