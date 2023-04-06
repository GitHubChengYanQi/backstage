package cn.atsoft.dasheng.production.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;

/**
 * <p>
 * 工艺路线列表
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-02-15
 */
@TableName("activiti_process_route")
public class ProcessRoute implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 工艺路线id
     */
    @TableId(value = "process_route_id", type = IdType.ID_WORKER)

    private Long processRouteId;


    @TableField("pid")
    private Long pid;
    /**
     * sku
     */
    @TableField("sku_id")
    private Long skuId;

    /**
     * 工艺路线编号
     */
    @TableField("process_route_coding")
    private String processRouteCoding;

    @TableField("children")
    private String children;

    @TableField("childrens")
    private String childrens;
    /**
     * 工艺路线名称
     */
    @TableField("process_rote_name")
    private String processRoteName;

    /**
     * 关联工艺物料清单
     */
    @TableField("parts_id")
    private Long partsId;

    /**
     * 版本号
     */
    @TableField("version")
    private Long version;

    /**
     * 状态
     */
    @TableField("status")
    private Integer status;

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
    @TableField(value = "deptId" , fill = FieldFill.INSERT)
    private Long deptId;

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getChildren() {
        return children;
    }

    public void setChildren(String children) {
        this.children = children;
    }

    public String getChildrens() {
        return childrens;
    }

    public void setChildrens(String childrens) {
        this.childrens = childrens;
    }

    public Long getProcessRouteId() {
        return processRouteId;
    }

    public void setProcessRouteId(Long processRouteId) {
        this.processRouteId = processRouteId;
    }

    public String getProcessRouteCoding() {
        return processRouteCoding;
    }

    public void setProcessRouteCoding(String processRouteCoding) {
        this.processRouteCoding = processRouteCoding;
    }

    public String getProcessRoteName() {
        return processRoteName;
    }

    public void setProcessRoteName(String processRoteName) {
        this.processRoteName = processRoteName;
    }

    public Long getPartsId() {
        return partsId;
    }

    public void setPartsId(Long partsId) {
        this.partsId = partsId;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    @Override
    public String toString() {
        return "ProcessRoute{" +
                "processRouteId=" + processRouteId +
                ", processRouteCoding=" + processRouteCoding +
                ", processRoteName=" + processRoteName +
                ", partsId=" + partsId +
                ", version=" + version +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", display=" + display +
                ", deptId=" + deptId +
                "}";
    }
}
