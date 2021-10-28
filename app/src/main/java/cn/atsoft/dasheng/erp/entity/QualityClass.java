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
 * 质检方案详情分类
 * </p>
 *
 * @author song
 * @since 2021-10-28
 */
@TableName("daoxin_erp_quality_class")
public class QualityClass implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 质检方案分类id
     */
      @TableId(value = "quality_plan_class_id", type = IdType.ID_WORKER)
    private Long qualityPlanClassId;

    /**
     * 质检方案id
     */
    @TableField("quality_plan_id")
    private Long qualityPlanId;

    /**
     * 质检项分类id
     */
    @TableField("quality_check_classification_id")
    private Long qualityCheckClassificationId;

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
     * 分类名称
     */
    @TableField("name")
    private String name;


    public Long getQualityPlanClassId() {
        return qualityPlanClassId;
    }

    public void setQualityPlanClassId(Long qualityPlanClassId) {
        this.qualityPlanClassId = qualityPlanClassId;
    }

    public Long getQualityPlanId() {
        return qualityPlanId;
    }

    public void setQualityPlanId(Long qualityPlanId) {
        this.qualityPlanId = qualityPlanId;
    }

    public Long getQualityCheckClassificationId() {
        return qualityCheckClassificationId;
    }

    public void setQualityCheckClassificationId(Long qualityCheckClassificationId) {
        this.qualityCheckClassificationId = qualityCheckClassificationId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "QualityClass{" +
        "qualityPlanClassId=" + qualityPlanClassId +
        ", qualityPlanId=" + qualityPlanId +
        ", qualityCheckClassificationId=" + qualityCheckClassificationId +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", display=" + display +
        ", deptId=" + deptId +
        ", name=" + name +
        "}";
    }
}
