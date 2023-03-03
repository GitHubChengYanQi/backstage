package cn.atsoft.dasheng.erp.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
@TableName("daoxin_erp_bom_detail")
public class BomDetail {
    @TableId(value = "bom_detail_id", type = IdType.ID_WORKER)
    private Long bomDetailId;

    @TableField("sku_id")
    private Long skuId;

    @TableField("version_bom_id")
    private Long versionBomId;

    @TableField("bom_id")
    private Long bomId;

    @TableField("number")
    private Integer number;

    @TableField("note")
    private String note;

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
