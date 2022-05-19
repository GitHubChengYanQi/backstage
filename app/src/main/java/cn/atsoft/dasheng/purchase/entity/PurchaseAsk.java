package cn.atsoft.dasheng.purchase.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 采购申请
 * </p>
 *
 * @author song
 * @since 2021-12-15
 */
@TableName("daoxin_purchase_ask")
public class PurchaseAsk implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 采购申请id
     */
      @TableId(value = "purchase_ask_id", type = IdType.ID_WORKER)
    private Long purchaseAskId;

    /**
     * 编号
     */
    @TableField("coding")
    private String coding;

    /**
     * 采购类型
     */
    @TableField("type")
    private String type;

    /**
     * 备注
     */
    @TableField("note")
    private String note;

    /**
     * 申请状态
     */
    @TableField("status")
    private Long status;

    /**
     * 总金额
     */
    @TableField("money")
    private Integer money;

    /**
     * 总数量
     */
    @TableField("number")
    private Long number;

    /**
     * 种类数量
     */
    @TableField("type_number")
    private Long typeNumber;

    /**
     * 来源
     */
    @TableField("source")
    private String source;

    /**
     * 来源id
     */
    @TableField("source_id")
    private Long sourceId;

    /**
     * 主题
     */
    @TableField("theme")
    private String theme;

    /**
     * 来源
     */
    @TableField("origin")
    private String origin;

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

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
     * 创建用户
     */
      @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改用户
     */
      @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private Long updateUser;

    /**
     * 状态
     */
    @TableField("display")
    private Long display;

    /**
     * 部门id
     */
    @TableField("deptId")
    private Long deptId;


    public Long getPurchaseAskId() {
        return purchaseAskId;
    }

    public void setPurchaseAskId(Long purchaseAskId) {
        this.purchaseAskId = purchaseAskId;
    }

    public String getCoding() {
        return coding;
    }

    public void setCoding(String coding) {
        this.coding = coding;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Long getTypeNumber() {
        return typeNumber;
    }

    public void setTypeNumber(Long typeNumber) {
        this.typeNumber = typeNumber;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
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

    public Long getDisplay() {
        return display;
    }

    public void setDisplay(Long display) {
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
        return "PurchaseAsk{" +
        "purchaseAskId=" + purchaseAskId +
        ", coding=" + coding +
        ", type=" + type +
        ", note=" + note +
        ", status=" + status +
        ", money=" + money +
        ", number=" + number +
        ", typeNumber=" + typeNumber +
        ", source=" + source +
        ", sourceId=" + sourceId +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", display=" + display +
        ", deptId=" + deptId +
        "}";
    }
}
