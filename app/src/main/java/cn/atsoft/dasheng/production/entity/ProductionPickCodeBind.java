package cn.atsoft.dasheng.production.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-29
 */
@TableName("daoxin_production_pick_code_bind")
public class ProductionPickCodeBind implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "pick_code_bind_id", type = IdType.ID_WORKER)
    private Long pickCodeBindId;

    @TableField("pick_code_id")
    private Long pickCodeId;

    @TableField("sku_id")
    private Long skuId;

    @TableField("number")
    private Integer number;

    @TableField("production_task_id")
    private Long productionTaskId;

    @TableField("pick_lists_id")
    private Long pickListsId;

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
     * 状态
     */
    @TableField("display")
    private Integer display;

    /**
     * 部门id
     */
    @TableField("deptId")
    private Long deptId;


    public Long getPickCodeBindId() {
        return pickCodeBindId;
    }

    public void setPickCodeBindId(Long pickCodeBindId) {
        this.pickCodeBindId = pickCodeBindId;
    }

    public Long getPickCodeId() {
        return pickCodeId;
    }

    public void setPickCodeId(Long pickCodeId) {
        this.pickCodeId = pickCodeId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Long getProductionTaskId() {
        return productionTaskId;
    }

    public void setProductionTaskId(Long productionTaskId) {
        this.productionTaskId = productionTaskId;
    }

    public Long getPickListsId() {
        return pickListsId;
    }

    public void setPickListsId(Long pickListsId) {
        this.pickListsId = pickListsId;
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

    @Override
    public String toString() {
        return "ProductionPickCodeBind{" +
        "pickCodeBindId=" + pickCodeBindId +
        ", pickCodeId=" + pickCodeId +
        ", skuId=" + skuId +
        ", number=" + number +
        ", productionTaskId=" + productionTaskId +
        ", pickListsId=" + pickListsId +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", display=" + display +
        ", deptId=" + deptId +
        "}";
    }
}
