package cn.atsoft.dasheng.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 销售
 * </p>
 *
 * @author 
 * @since 2021-07-31
 */
@TableName("daoxin_crm_business_sales")
public class CrmBusinessSales implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 销售id
     */
      @TableId(value = "sales_id", type = IdType.AUTO)
    private Long salesId;

    /**
     * 名称
     */
    @TableField("name")
    private String name;


    public Long getSalesId() {
        return salesId;
    }

    public void setSalesId(Long salesId) {
        this.salesId = salesId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CrmBusinessSales{" +
        "salesId=" + salesId +
        ", name=" + name +
        "}";
    }
}
