package cn.atsoft.dasheng.crm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

/**
 * <p>
 * 发票
 * </p>
 *
 * @author song
 * @since 2022-04-22
 */
@TableName("daoxin_invoice_bill")
public class InvoiceBill implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 发票id
     */
    @TableId(value = "invoice_bill_id", type = IdType.ID_WORKER)
    private Long invoiceBillId;
    /**
     * 发票id
     */
    @TableField(value = "invoice_bill_no")
    private String invoiceBillNo;

    /**
     * 金额
     */
    @TableField("money")
    private Long money;

    /**
     * 附件
     */
    @TableField("enclosure_id")
    private String enclosureId;

    /**
     * 发票名称
     */
    @TableField("name")
    private String name;

    /**
     * 发票日期
     */
    @TableField("Invoice_date")
    private Date invoiceDate;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private Long updateUser;

    @TableField("display")
    private Integer display;

    /**
     * 部门编号
     */
    @TableField(value= "deptId" , fill = FieldFill.INSERT)
    private Long deptId;
    @TableField(value = "tenant_id" , fill = FieldFill.INSERT)
    private Long tenantId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * 关联订单
     */
    @TableField("order_id")
    private Long orderId;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getInvoiceBillId() {
        return invoiceBillId;
    }

    public void setInvoiceBillId(Long invoiceBillId) {
        this.invoiceBillId = invoiceBillId;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public String getEnclosureId() {
        return enclosureId;
    }

    public void setEnclosureId(String enclosureId) {
        this.enclosureId = enclosureId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
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

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getInvoiceBillNo() {
        return invoiceBillNo;
    }

    public void setInvoiceBillNo(String invoiceBillNo) {
        this.invoiceBillNo = invoiceBillNo;
    }

    @Override
    public String toString() {
        return "InvoiceBill{" +
                "invoiceBillId=" + invoiceBillId +
                ", invoiceBillNo='" + invoiceBillNo + '\'' +
                ", money=" + money +
                ", enclosureId='" + enclosureId + '\'' +
                ", name='" + name + '\'' +
                ", invoiceDate=" + invoiceDate +
                ", createTime=" + createTime +
                ", createUser=" + createUser +
                ", updateTime=" + updateTime +
                ", updateUser=" + updateUser +
                ", display=" + display +
                ", deptId=" + deptId +
                ", orderId=" + orderId +
                '}';
    }
}
