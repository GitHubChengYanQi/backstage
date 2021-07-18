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
 * 客户地址表
 * </p>
 *
 * @author 
 * @since 2021-07-16
 */
@TableName("daoxin_adress")
public class Adress implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 客户名称
     */
    @TableField("name")
    private String name;

    /**
     * 地址id
     */
      @TableId(value = "adress_id", type = IdType.ID_WORKER)
    private Long adressId;

    /**
     * 地址1id
     */
    @TableField("adress1_id")
    private Long adress1Id;

    /**
     * 地址1
     */
    @TableField("adress1")
    private String adress1;

    /**
     * 地址2id
     */
    @TableField("adress2_id")
    private Long adress2Id;

    /**
     * 地址2
     */
    @TableField("adress2")
    private String adress2;

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

    public Long getAdressId() {
        return adressId;
    }

    public void setAdressId(Long adressId) {
        this.adressId = adressId;
    }

    public Long getAdress1Id() {
        return adress1Id;
    }

    public void setAdress1Id(Long adress1Id) {
        this.adress1Id = adress1Id;
    }

    public String getAdress1() {
        return adress1;
    }

    public void setAdress1(String adress1) {
        this.adress1 = adress1;
    }

    public Long getAdress2Id() {
        return adress2Id;
    }

    public void setAdress2Id(Long adress2Id) {
        this.adress2Id = adress2Id;
    }

    public String getAdress2() {
        return adress2;
    }

    public void setAdress2(String adress2) {
        this.adress2 = adress2;
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
        return "Adress{" +
        "name=" + name +
        ", adressId=" + adressId +
        ", adress1Id=" + adress1Id +
        ", adress1=" + adress1 +
        ", adress2Id=" + adress2Id +
        ", adress2=" + adress2 +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", display=" + display +
        "}";
    }
}
