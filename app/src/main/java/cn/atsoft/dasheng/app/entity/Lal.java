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
 * 经纬度表
 * </p>
 *
 * @author 
 * @since 2021-07-16
 */
@TableName("daoxin_lal")
public class Lal implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 客户名称
     */
    @TableField("name")
    private String name;

    /**
     * 经纬度id
     */
      @TableId(value = "lal_id", type = IdType.ID_WORKER)
    private Long lalId;

    /**
     * 经度
     */
    @TableField("ew_itude")
    private Integer ewItude;

    /**
     * 纬度
     */
    @TableField("sn_itude")
    private Integer snItude;

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


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getLalId() {
        return lalId;
    }

    public void setLalId(Long lalId) {
        this.lalId = lalId;
    }

    public Integer getEwItude() {
        return ewItude;
    }

    public void setEwItude(Integer ewItude) {
        this.ewItude = ewItude;
    }

    public Integer getSnItude() {
        return snItude;
    }

    public void setSnItude(Integer snItude) {
        this.snItude = snItude;
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

    @Override
    public String toString() {
        return "Lal{" +
        "name=" + name +
        ", lalId=" + lalId +
        ", ewItude=" + ewItude +
        ", snItude=" + snItude +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", display=" + display +
        "}";
    }
}
