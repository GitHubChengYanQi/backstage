package cn.atsoft.dasheng.production.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 工序表
 * </p>
 *
 * @author 
 * @since 2021-10-29
 */
@TableName("working_procedure")
public class WorkingProcedure implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 工序
     */
      @TableId(value = "working_procedure_id", type = IdType.ID_WORKER)
    private Long workingProcedureId;

    /**
     * 人员id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 工位id
     */
    @TableField("production_station_id")
    private Long productionStationId;

    /**
     * 附件
     */
    @TableField("accessories")
    private String accessories;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 工序分类
     */
    @TableField("working_procedure_class")
    private Long workingProcedureClass;



    /**
     * 工序名称	
     */
    @TableField("working_procedure_name")
    private String workingProcedureName;


    public Long getWorkingProcedureId() {
        return workingProcedureId;
    }

    public void setWorkingProcedureId(Long workingProcedureId) {
        this.workingProcedureId = workingProcedureId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductionStationId() {
        return productionStationId;
    }

    public void setProductionStationId(Long productionStationId) {
        this.productionStationId = productionStationId;
    }

    public String getAccessories() {
        return accessories;
    }

    public void setAccessories(String accessories) {
        this.accessories = accessories;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getWorkingProcedureName() {
        return workingProcedureName;
    }

    public void setWorkingProcedureName(String workingProcedureName) {
        this.workingProcedureName = workingProcedureName;
    }
    public Long getWorkingProcedureClass() {
        return workingProcedureClass;
    }

    public void setWorkingProcedureClass(Long workingProcedureClass) {
        this.workingProcedureClass = workingProcedureClass;
    }
    @Override
    public String toString() {
        return "WorkingProcedure{" +
        " workingProcedureId=" + workingProcedureId +
        ", userId=" + userId +
        ", productionStationId=" + productionStationId +
        ", accessories=" + accessories +
        ", remark=" + remark +
        ", workingProcedureName=" + workingProcedureName +
        "}";
    }
}
