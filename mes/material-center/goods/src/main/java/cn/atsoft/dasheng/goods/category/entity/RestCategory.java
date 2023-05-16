package cn.atsoft.dasheng.goods.category.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * SPU分类
 * </p>
 *
 * @author song
 * @since 2021-10-25
 */
@TableName("goods_spu_class")
public class RestCategory implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * 表单风格
     */
    @TableField("form_style_id")
    private Long formStyleId;

    @TableField("children")
    private String children;

    @TableField("childrens")
    private String childrens;
    /**
     * 编码分类
     */
    @TableField("coding_class")
    private String codingClass;
    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;
    /**
     * pid
     */
    @TableField("pid")
    private Long pid;


    /**
     * 是否是产品分类
     */
    @TableField("type")
    private Long type;

    /**
     * spu分类id
     */
    @TableId(value = "spu_classification_id", type = IdType.ID_WORKER)
    private Long spuClassificationId;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

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
    /**
     * 租户编号
     */
    @TableField(value = "tenant_id" , fill = FieldFill.INSERT)
    private Long tenantId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }


    public Long getFormStyleId() {
        return formStyleId;
    }

    public void setFormStyleId(Long formStyleId) {
        this.formStyleId = formStyleId;
    }

    public Long getSpuClassificationId() {
        return spuClassificationId;
    }

    public void setSpuClassificationId(Long spuClassificationId) {
        this.spuClassificationId = spuClassificationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getCodingClass() {
        return codingClass;
    }

    public void setCodingClass(String codingClass) {
        this.codingClass = codingClass;
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

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "SpuClassification{" +
                "spuClassificationId=" + spuClassificationId +
                ", name=" + name +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", createUser=" + createUser +
                ", updateUser=" + updateUser +
                ", display=" + display +
                ", deptId=" + deptId +
                "}";
    }
}
