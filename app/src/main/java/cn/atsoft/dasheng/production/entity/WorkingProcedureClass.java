package cn.atsoft.dasheng.production.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 工序分类表
 * </p>
 *
 * @author 
 * @since 2021-10-29
 */
@TableName("working_porcedure_class")
public class WorkingProcedureClass implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 工序分类id
     */
      @TableId(value = "wp_class_id", type = IdType.ID_WORKER)
    private Long wpClassId;

    /**
     * 工序分类名称
     */
    @TableField("wp_class_name")
    private String wpClassName;


    public Long getWpClassId() {
        return wpClassId;
    }

    public void setWpClassId(Long wpClassId) {
        this.wpClassId = wpClassId;
    }

    public String getWpClassName() {
        return wpClassName;
    }

    public void setWpClassName(String wpClassName) {
        this.wpClassName = wpClassName;
    }

    @Override
    public String toString() {
        return "WorkingPorcedureClass{" +
        "wpClassId=" + wpClassId +
        ", wpClassName=" + wpClassName +
        "}";
    }
}
