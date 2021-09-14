package cn.atsoft.dasheng.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

/**
 * <p>
 * 销售流程
 * </p>
 *
 * @author
 * @since 2021-08-04
 */
@TableName("daoxin_crm_business_sales_process")
public class CrmBusinessSalesProcess implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableField("win_rate")
     private  Long winRate ;
    /**
     * 赢率id
     */
    @TableId(value = "sales_process_id", type = IdType.ID_WORKER)
    private Long salesProcessId;
    @TableField("note")
    private String note;

@TableField("sales_process_plan_id")
    private Long salesProcessPlanId;
    /**
     * 流程名称
     */
    @TableField("name")
    private String name;

    /**
     * 百分比
     */
    @TableField("percentage")
    private Integer percentage;

    /**
     * 流程id
     */
    @TableField("sales_id")
    private Long salesId;

    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;

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

    public Long getWinRate() {
        return winRate;
    }

    public void setWinRate(Long winRate) {
        this.winRate = winRate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getSalesProcessId() {
        return salesProcessId;
    }

    public void setSalesProcessId(Long salesProcessId) {
        this.salesProcessId = salesProcessId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPercentage() {
        return percentage;
    }

    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }

    public Long getSalesId() {
        return salesId;
    }

    public void setSalesId(Long salesId) {
        this.salesId = salesId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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

    public Long getSalesProcessPlanId() {
        return salesProcessPlanId;
    }

    public void setSalesProcessPlanId(Long salesProcessPlanId) {
        this.salesProcessPlanId = salesProcessPlanId;
    }

    @Override
    public String toString() {
        return "CrmBusinessSalesProcess{" +
                "salesProcessId=" + salesProcessId +
                ", name=" + name +
                ", percentage=" + percentage +
                ", salesId=" + salesId +
                ", sort=" + sort +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", display=" + display +
                "}";
    }
}
