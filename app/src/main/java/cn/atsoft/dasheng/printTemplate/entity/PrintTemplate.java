package cn.atsoft.dasheng.printTemplate.entity;

import cn.atsoft.dasheng.form.pojo.PrintTemplateEnum;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 编辑模板
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-28
 */
@TableName("daoxin_print_template")
public class PrintTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编辑模板id
     */
      @TableId(value = "print_template_id", type = IdType.ID_WORKER)
    private Long printTemplateId;

    /**
     * 类型
     */
    @TableField("type")
    @JSONField(serialzeFeatures= SerializerFeature.WriteEnumUsingToString)
    private PrintTemplateEnum type;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 模板
     */
    @TableField("templete")
    private String templete;

    /**
     * 创建时间
     */
      @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 创建者
     */
      @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改时间
     */
      @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

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
    @TableField(value = "deptId" , fill =FieldFill.INSERT)
    private Long deptId;


    public Long getPrintTemplateId() {
        return printTemplateId;
    }

    public void setPrintTemplateId(Long printTemplateId) {
        this.printTemplateId = printTemplateId;
    }

    public PrintTemplateEnum getType() {
        return type;
    }

    public void setType(PrintTemplateEnum type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplete() {
        return templete;
    }

    public void setTemplete(String templete) {
        this.templete = templete;
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

    @Override
    public String toString() {
        return "PrintTemplate{" +
        "printTemplateId=" + printTemplateId +
        ", type=" + type +
        ", name=" + name +
        ", templete=" + templete +
        ", createTime=" + createTime +
        ", createUser=" + createUser +
        ", updateTime=" + updateTime +
        ", updateUser=" + updateUser +
        ", display=" + display +
        ", deptId=" + deptId +
        "}";
    }
}
