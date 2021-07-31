package cn.atsoft.dasheng.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 销售流程
 * </p>
 *
 * @author 
 * @since 2021-07-31
 */
@TableName("daoxin_crm_business_sales_process")
public class CrmBusinessSalesProcess implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 销售流程
     */
      @TableId(value = "sales_process_id", type = IdType.ID_WORKER)
    private Long salesProcessId;

    /**
     * 流程名称
     */
    @TableField("name1")
    private String name1;

    @TableField("name2")
    private String name2;

    @TableField("name3")
    private String name3;

    @TableField("name4")
    private String name4;

    @TableField("name5")
    private String name5;

    /**
     * 状态
     */
    @TableField("state")
    private Integer state;


    public Long getSalesProcessId() {
        return salesProcessId;
    }

    public void setSalesProcessId(Long salesProcessId) {
        this.salesProcessId = salesProcessId;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getName3() {
        return name3;
    }

    public void setName3(String name3) {
        this.name3 = name3;
    }

    public String getName4() {
        return name4;
    }

    public void setName4(String name4) {
        this.name4 = name4;
    }

    public String getName5() {
        return name5;
    }

    public void setName5(String name5) {
        this.name5 = name5;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "CrmBusinessSalesProcess{" +
        "salesProcessId=" + salesProcessId +
        ", name1=" + name1 +
        ", name2=" + name2 +
        ", name3=" + name3 +
        ", name4=" + name4 +
        ", name5=" + name5 +
        ", state=" + state +
        "}";
    }
}
