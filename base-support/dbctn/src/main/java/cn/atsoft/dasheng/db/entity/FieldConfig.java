package cn.atsoft.dasheng.db.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 字段配置
 * </p>
 *
 * @author Sing
 * @since 2020-12-11
 */
@TableName("database_info_field_config")
public class FieldConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 字段名
     */
      @TableId(value = "field_name", type = IdType.ID_WORKER)
    private String fieldName;

    /**
     * 字段类型
     */
    @TableField("type")
    private String type;

    /**
     * 数据配置
     */
    @TableField("config")
    private String config;


    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    @Override
    public String toString() {
        return "FieldConfig{" +
        "fieldName=" + fieldName +
        ", type=" + type +
        ", config=" + config +
        "}";
    }
}
