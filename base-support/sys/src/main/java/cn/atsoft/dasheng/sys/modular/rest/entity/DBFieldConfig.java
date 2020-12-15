package cn.atsoft.dasheng.sys.modular.rest.entity;

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
 * @since 2020-12-12
 */
@TableName("sys_field_config")
public class DBFieldConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "field_id", type = IdType.ID_WORKER)
    private Long fieldId;

    /**
     * 字段名
     */
    @TableField("field_name")
    private String fieldName;

    /**
     * 表名
     */
    @TableField("table")
    private String table;

    /**
     * 字段类型，根据type影响controller生成的结果
     * dataScope    数据范围
     * input    输入框
     * title    生成selectList接口
     * printKey 生成selectTree 接口
     * textArea 文本区域
     * select   下拉选择
     * tree 树形选择
     * imageUpload  图片上传
     * edit 编辑器
     */
    @TableField("type")
    private String type;

    /**
     * 数据配置
     */
    @TableField("config")
    private String config;

    /**
     * 是否列表显示
     */
    @TableField("showList")
    private String showList;

    /**
     * 是否搜索
     */
    @TableField("isSearch")
    private String isSearch;


    public Long getFieldId() {
        return fieldId;
    }

    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
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

    public String getShowList() {
        return showList;
    }

    public void setShowList(String showList) {
        this.showList = showList;
    }

    public String getIsSearch() {
        return isSearch;
    }

    public void setIsSearch(String isSearch) {
        this.isSearch = isSearch;
    }

    @Override
    public String toString() {
        return "FieldConfig{" +
        "fieldId=" + fieldId +
        ", fieldName=" + fieldName +
        ", table=" + table +
        ", type=" + type +
        ", config=" + config +
        ", showList=" + showList +
        ", isSearch=" + isSearch +
        "}";
    }
}
