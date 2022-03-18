package cn.atsoft.dasheng.erp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

/**
 * <p>
 * sku详情表
 * </p>
 *
 * @author
 * @since 2021-10-18
 */
@TableName("daoxin_erp_sku_values")
public class SkuValues implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "sku_detail_id", type = IdType.ID_WORKER)
    private Long skuDetailId;

    @TableField("sku_id")
    private Long skuId;

    /**
     * 属性Id
     */
    @TableField("attribute_id")
    private Long attributeId;

    /**
     * 属性值id
     */
    @TableField("attribute_values_id")
    private Long attributeValuesId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 创建者
     */
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

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


    public Long getSkuDetailId() {
        return skuDetailId;
    }

    public void setSkuDetailId(Long skuDetailId) {
        this.skuDetailId = skuDetailId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(Long attributeId) {
        this.attributeId = attributeId;
    }

    public Long getAttributeValuesId() {
        return attributeValuesId;
    }

    public void setAttributeValuesId(Long attributeValuesId) {
        this.attributeValuesId = attributeValuesId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    public Integer getDisplay() {
        return display;
    }

    public void setDisplay(Integer display) {
        this.display = display;
    }

    @Override
    public String toString() {
        return "SkuValues{" +
                "skuDetailId=" + skuDetailId +
                ", skuId=" + skuId +
                ", attributeId=" + attributeId +
                ", attributeValuesId=" + attributeValuesId +
                ", createTime=" + createTime +
                ", createUser=" + createUser +
                ", updateTime=" + updateTime +
                ", updateUser=" + updateUser +
                ", display=" + display +
                "}";
    }
}
