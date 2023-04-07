package cn.atsoft.dasheng.portal.goodsDetails.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 首页商品详情
 * </p>
 *
 * @author siqiang
 * @since 2021-08-19
 */
@TableName("daoxin_portal_goods_details")
public class GoodsDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品明细id
     */
      @TableId(value = "good_details_id", type = IdType.ID_WORKER)
    private Long goodDetailsId;

    /**
     * 商品id
     */
    @TableField("good_id")
    private Long goodId;

    /**
     * 商品轮播图id
     */
    @TableField("detail_banner_id")
    private Long detailBannerId;

    /**
     * 商品标题
     */
    @TableField("title")
    private String title;

    /**
     * 商品售价
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * 商品原价
     */
    @TableField("last_price")
    private BigDecimal lastPrice;

    /**
     * 服务
     */
    @TableField("server")
    private String server;

    /**
     * 规格id
     */
    @TableField("specification_id")
    private Long specificationId;

    /**
     * 商品详情
     */
    @TableField("details")
    private String details;

    /**
     * 排序
     */
    @TableField("sort")
    private Long sort;

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
     * 部门id
     */
    @TableField(value = "deptId",fill =FieldFill.INSERT)
    private Long deptId;

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getGoodDetailsId() {
        return goodDetailsId;
    }
    @TableField(value = "tenant_id" , fill = FieldFill.INSERT)
    private Long tenantId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
    public void setGoodDetailsId(Long goodDetailsId) {
        this.goodDetailsId = goodDetailsId;
    }

    public Long getGoodId() {
        return goodId;
    }

    public void setGoodId(Long goodId) {
        this.goodId = goodId;
    }

    public Long getDetailBannerId() {
        return detailBannerId;
    }

    public void setDetailBannerId(Long detailBannerId) {
        this.detailBannerId = detailBannerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(BigDecimal lastPrice) {
        this.lastPrice = lastPrice;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public Long getSpecificationId() {
        return specificationId;
    }

    public void setSpecificationId(Long specificationId) {
        this.specificationId = specificationId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
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
        return "GoodsDetails{" +
        "goodDetailsId=" + goodDetailsId +
        ", goodId=" + goodId +
        ", detailBannerId=" + detailBannerId +
        ", title=" + title +
        ", price=" + price +
        ", lastPrice=" + lastPrice +
        ", server=" + server +
        ", specificationId=" + specificationId +
        ", details=" + details +
        ", sort=" + sort +
        ", createTime=" + createTime +
        ", createUser=" + createUser +
        ", updateTime=" + updateTime +
        ", updateUser=" + updateUser +
        ", display=" + display +
        "}";
    }
}
