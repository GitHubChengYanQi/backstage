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
 * 编码规则
 * </p>
 *
 * @author song
 * @since 2021-10-22
 */
@TableName("daoxin_erp_coding_rules")
public class CodingRules implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 排序
     */
    @TableField("sort")
    private Long sort;

    /**
     * 编码规则id
     */
    @TableId(value = "coding_rules_id", type = IdType.ID_WORKER)
    private Long codingRulesId;

    /**
     * 编码规则分类id
     */
    @TableField("coding_rules_classification_id")
    private Long codingRulesClassificationId;

    /**
     * 编码规则名称
     */
    @TableField("name")
    private String name;

    /**
     * 编码规则
     */
    @TableField("coding_rules")
    private String codingRules;

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


    public Long getCodingRulesId() {
        return codingRulesId;
    }

    public void setCodingRulesId(Long codingRulesId) {
        this.codingRulesId = codingRulesId;
    }

    public Long getCodingRulesClassificationId() {
        return codingRulesClassificationId;
    }

    public void setCodingRulesClassificationId(Long codingRulesClassificationId) {
        this.codingRulesClassificationId = codingRulesClassificationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCodingRules() {
        return codingRules;
    }

    public void setCodingRules(String codingRules) {
        this.codingRules = codingRules;
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

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "CodingRules{" +
                "codingRulesId=" + codingRulesId +
                ", codingRulesClassificationId=" + codingRulesClassificationId +
                ", name=" + name +
                ", codingRules=" + codingRules +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", display=" + display +
                ", deptId=" + deptId +
                "}";
    }
}