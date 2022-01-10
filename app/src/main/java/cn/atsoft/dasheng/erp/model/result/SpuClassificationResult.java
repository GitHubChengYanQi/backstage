package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.erp.entity.Spu;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
public class SpuClassificationResult implements Serializable {

    private static final long serialVersionUID = 1L;
    @JSONField(serialize = false)
    private String children;

    @JSONField(serialize = false)
    private String childrens;
    /**
     * 编码分类
     */
    private String codingClass;

    private String pidName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * pid
     */
    private Long pid;

    private List<Spu> spuList;
    /**
     * spu分类id
     */
    @ApiModelProperty("spu分类id")
    private Long spuClassificationId;

    /**
     * 是否是产品分类
     */
    @ApiModelProperty("是否是产品分类")
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
}
