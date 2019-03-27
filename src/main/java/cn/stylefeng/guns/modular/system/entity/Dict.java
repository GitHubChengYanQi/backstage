package cn.stylefeng.guns.modular.system.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 基础字典
 * </p>
 *
 * @author stylefeng
 * @since 2019-03-13
 */
@TableName("sys_dict")
@Data
public class Dict implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 字典id
     */
    @TableId("DICT_ID")
    private Long dictId;

    /**
     * 所属字典类型的id
     */
    @TableField("DICT_TYPE_ID")
    private Long dictTypeId;

    /**
     * 字典编码
     */
    @TableField("CODE")
    private String code;

    /**
     * 字典名称
     */
    @TableField("NAME")
    private String name;

    /**
     * 上级代码id
     */
    @TableField("PARENT_ID")
    private Long parentId;

    /**
     * 所有上级代码id
     */
    @TableField("PARENT_IDS")
    private String parentIds;

    /**
     * 状态（字典）
     */
    @TableField("STATUS")
    private String status;

    /**
     * 字典的描述
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 排序
     */
    @TableField("SORT")
    private Integer sort;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME", fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField(value = "CREATE_USER", fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改人
     */
    @TableField(value = "UPDATE_USER", fill = FieldFill.UPDATE)
    private Long updateUser;

}
