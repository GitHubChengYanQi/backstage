package cn.atsoft.dasheng.app.entity;

import cn.atsoft.dasheng.erp.model.result.SkuResult;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

/**
 * <p>
 * 清单详情
 * </p>
 *
 * @author cheng
 * @since 2021-10-26
 */
@TableName("daoxin_erp_parts_detail")
public class ErpPartsDetail implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 清单详情id
     */
    @TableId(value = "parts_detail_id", type = IdType.ID_WORKER)
    private Long partsDetailId;

    /**
     * 规格描述
     */
    @TableField("attribute")
    private String attribute;

    /**
     * 组成物品id
     */
    @TableField("spu_id")
    private Long spuId;

    /**
     * 组成物品id
     */
    @TableField("parts_id")
    private Long partsId;

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    /**
     * 组成物品id
     */
    @TableField("sku_id")
    private Long skuId;

    /**
     * 零件数量
     */
    @TableField("number")
    private Integer number;

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

    /**
     * 部门编号
     */
    @TableField("deptId")
    private Long deptId;

    /**
     * 备注
     */
    @TableField("note")
    private String note;


    public Long getPartsDetailId() {
        return partsDetailId;
    }

    public void setPartsDetailId(Long partsDetailId) {
        this.partsDetailId = partsDetailId;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public Long getPartsId() {
        return partsId;
    }

    public void setPartsId(Long partsId) {
        this.partsId = partsId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
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

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "ErpPartsDetail{" +
                "partsDetailId=" + partsDetailId +
                ", attribute=" + attribute +
                ", number=" + number +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", display=" + display +
                ", deptId=" + deptId +
                ", note=" + note +
                "}";
    }
}
