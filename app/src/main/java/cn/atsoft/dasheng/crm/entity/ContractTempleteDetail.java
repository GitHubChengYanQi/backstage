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
 * 自定义合同变量
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-04-18
 */
@TableName("daoxin_crm_contract_templete_detail")
public class ContractTempleteDetail implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "contract_templete_detail_id", type = IdType.ID_WORKER)
    private Long contractTempleteDetailId;

    @TableField("contract_templete_id")
    private Long contractTempleteId;

    /**
     * 值
     */
    @TableField("value")
    private String value;

    /**
     * 是否为默认值
     */
    @TableField("is_default")
    private Integer isDefault;

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


    public Long getContractTempleteDetailId() {
        return contractTempleteDetailId;
    }

    public void setContractTempleteDetailId(Long contractTempleteDetailId) {
        this.contractTempleteDetailId = contractTempleteDetailId;
    }

    public Long getContractTempleteId() {
        return contractTempleteId;
    }

    public void setContractTempleteId(Long contractTempleteId) {
        this.contractTempleteId = contractTempleteId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
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
        return "ContractTempleteDetail{" +
        "contractTempleteDetailId=" + contractTempleteDetailId +
        ", contractTempleteId=" + contractTempleteId +
        ", value=" + value +
        ", isDefault=" + isDefault +
        ", display=" + display +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
