package cn.atsoft.dasheng.form.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 工序步骤详情表
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
@TableName("activiti_setp_set_detail")
public class ActivitiSetpSetDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 详情Id
     */
      @TableId(value = "detail_id", type = IdType.AUTO)
    private Long detailId;

    /**
     * 步骤设置Id
     */
    @TableField("setps_id")
    private Long setpsId;

    /**
     * in（投入），out（产出）
     */
    @TableField("type")
    private String type;

    /**
     * 产品物料Id
     */
    @TableField("spu_id")
    private Long spuId;

    /**
     * skuId
     */
    @TableField("sku_id")
    private Long skuId;

    /**
     * 需要数量
     */
    @TableField("num")
    private Integer num;

    /**
     * 0为主要材料，有值代表替换物，值对应的是detail_id
     */
    @TableField("parent_id")
    private Long parentId;



    /**
     * 质检方案Id
     */
    @TableField("quality_id")
    private Long qualityId;



    /**
     * 自检方案Id
     */
    @TableField("my_quality_id")
    private Long myQualityId;

    /**
     * 删除状态
     */
    @TableField("display")
    private Integer display;

      @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

      @TableField(value = "update_user", fill = FieldFill.UPDATE)
    private Long updateUser;

      @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

      @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;


    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Long getSetpsId() {
        return setpsId;
    }

    public void setSetpsId(Long setpsId) {
        this.setpsId = setpsId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getQualityId() {
        return qualityId;
    }

    public void setQualityId(Long qualityId) {
        this.qualityId = qualityId;
    }

    public Long getMyQualityId() {
        return myQualityId;
    }

    public void setMyQualityId(Long myQualityId) {
        this.myQualityId = myQualityId;
    }



    public Integer getDisplay() {
        return display;
    }

    public void setDisplay(Integer display) {
        this.display = display;
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

    @Override
    public String toString() {
        return "ActivitiSetpSetDetail{" +
        "detailId=" + detailId +
        ", setpsId=" + setpsId +
        ", type=" + type +
        ", spuId=" + spuId +
        ", skuId=" + skuId +
        ", num=" + num +
        ", parentId=" + parentId +
        ", qualityId=" + qualityId +
        ", display=" + display +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
