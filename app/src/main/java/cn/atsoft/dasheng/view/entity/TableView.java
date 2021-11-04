package cn.atsoft.dasheng.view.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2021-11-04
 */
@TableName("view_table_view")
public class TableView implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 表视图主键
     */
      @TableId(value = "table_view_id", type = IdType.ID_WORKER)
    private Long tableViewId;

    @TableField("table_key")
    private String tableKey;

    @TableField("user_id")
    private Long userId;

    @TableField("field")
    private String field;


    public Long getTableViewId() {
        return tableViewId;
    }

    public void setTableViewId(Long tableViewId) {
        this.tableViewId = tableViewId;
    }

    public String getTableKey() {
        return tableKey;
    }

    public void setTableKey(String tableKey) {
        this.tableKey = tableKey;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return "TableView{" +
        "tableViewId=" + tableViewId +
        ", tableKey=" + tableKey +
        ", userId=" + userId +
        ", field=" + field +
        "}";
    }
}
