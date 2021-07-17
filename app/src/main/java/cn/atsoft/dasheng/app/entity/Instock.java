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
 * 入库表
 * </p>
 *
 * @author song
 * @since 2021-07-17
 */
@TableName("daoxin_instock")
public class Instock implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 物品编号
     */
      @TableId(value = "instock_id", type = IdType.ID_WORKER)
    private Long instockId;
    @TableField("palce_id")
    private Long palceId;

    public Long getPalceId() {
        return palceId;
    }

    public void setPalceId(Long palceId) {
        this.palceId = palceId;
    }

    /**
     * 物品名称
     */
    @TableField("item_id")
    private Long itemId;

    /**
     * 登记时间
     */
    @TableField("register_time")
    private Date registerTime;

    /**
     * 入库数量
     */
    @TableField("number")
    private Long number;

    /**
     * 价格
     */
    @TableField("price")
    private Integer price;

    /**
     * 品牌
     */
    @TableField("brand_id")
    private long brandId;

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


    public Long getInstockId() {
        return instockId;
    }

    public void setInstockId(Long instockId) {
        this.instockId = instockId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public long getBrandId() {
        return brandId;
    }

    public void setBrandId(long brandId) {
        this.brandId = brandId;
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

    @Override
    public String toString() {
        return "Instock{" +
        "instockId=" + instockId +
        ", itemId=" + itemId +
        ", registerTime=" + registerTime +
        ", number=" + number +
        ", price=" + price +
        ", brandId=" + brandId +
        ", createTime=" + createTime +
        ", createUser=" + createUser +
        ", updateTime=" + updateTime +
        ", updateUser=" + updateUser +
        ", display=" + display +
        "}";
    }
}
