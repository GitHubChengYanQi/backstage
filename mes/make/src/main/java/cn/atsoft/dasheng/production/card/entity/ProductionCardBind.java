package cn.atsoft.dasheng.production.card.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 生产卡片绑定
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-03-10
 */
@TableName("daoxin_production_card_bind")
public class ProductionCardBind implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 工单id
     */
      @TableId(value = "production_card_bind_id", type = IdType.ID_WORKER)
    private Long productionCardBindId;

    /**
     * 生产卡片id
     */
    @TableField("production_card_id")
    private Long productionCardId;

    /**
     * 来源类型
     */
    @TableField("source")
    private String source;

    /**
     * 来源id
     */
    @TableField("sourse_id")
    private Long sourseId;

    /**
     * 部门编号
     */
    @TableField(value = "deptId" , fill = FieldFill.INSERT)
    private Long deptId;

    /**
     * 状态
     */
    @TableField("display")
    private Integer display;

    /**
     * 创建者
     */
      @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
      @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改者
     */
      @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     */
      @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;


    public Long getProductionCardBindId() {
        return productionCardBindId;
    }

    public void setProductionCardBindId(Long productionCardBindId) {
        this.productionCardBindId = productionCardBindId;
    }

    public Long getProductionCardId() {
        return productionCardId;
    }

    public void setProductionCardId(Long productionCardId) {
        this.productionCardId = productionCardId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getSourseId() {
        return sourseId;
    }

    public void setSourseId(Long sourseId) {
        this.sourseId = sourseId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Integer getDisplay() {
        return display;
    }

    public void setDisplay(Integer display) {
        this.display = display;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "ProductionCardBind{" +
        "productionCardBindId=" + productionCardBindId +
        ", productionCardId=" + productionCardId +
        ", source=" + source +
        ", sourseId=" + sourseId +
        ", deptId=" + deptId +
        ", display=" + display +
        ", createUser=" + createUser +
        ", createTime=" + createTime +
        ", updateUser=" + updateUser +
        ", updateTime=" + updateTime +
        "}";
    }
}
