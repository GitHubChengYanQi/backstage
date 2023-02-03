package cn.atsoft.dasheng.bom.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
@TableName("daoxin_erp_bom")
public class RestBom {
    @TableId(value = "bom_id" , type = IdType.ID_WORKER)
    private Long bomId;

    /**
     * 物料Id
     */
    @TableField("sku_id")
    private Long skuId;

    /**
     * 版本
     */
    @TableField("version")
    private String version;

    @TableField("note")
    private String note;

    @TableField("children")
    private String children;

    @TableField("childrens")
    private String childrens;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 创建者
     */
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改者
     */
    @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private Long updateUser;

    /**
     * 状态
     */
    @TableField("display")
    private Integer display;
}
