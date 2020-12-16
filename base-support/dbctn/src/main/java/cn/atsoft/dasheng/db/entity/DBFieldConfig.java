package cn.atsoft.dasheng.db.entity;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;

import java.io.Serializable;

/**
 * <p>
 * 字段配置
 * </p>
 *
 * @author Sing
 * @since 2020-12-12
 */
@TableName(value = "sys_field_config",autoResultMap = true)
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
    @TableField("table_name")
    private String tableName;

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
     * @TableName(autoResultMap = true)
     */
    @TableField(value = "config",typeHandler = FastjsonTypeHandler.class)
    private JSONArray config;

    /**
     * 是否列表显示
     * @TableName(autoResultMap = true)
     */
    @TableField(value = "showList",typeHandler = FastjsonTypeHandler.class)
    private JSONArray showList;

    /**
     * 是否搜索
     * @TableName(autoResultMap = true)
     */
    @TableField(value = "isSearch",typeHandler = FastjsonTypeHandler.class)
    private JSONArray isSearch;


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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String table) {
        this.tableName = table;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JSONArray getConfig() {
        return config;
    }

    public void setConfig(JSONArray config) {
        this.config = config;
    }

    public JSONArray getShowList() {
        return showList;
    }

    public void setShowList(JSONArray showList) {
        this.showList = showList;
    }

    public JSONArray getIsSearch() {
        return isSearch;
    }

    public void setIsSearch(JSONArray isSearch) {
        this.isSearch = isSearch;
    }

    @Override
    public String toString() {
        return "FieldConfig{" +
        "fieldId=" + fieldId +
        ", fieldName=" + fieldName +
        ", table=" + tableName +
        ", type=" + type +
        ", config=" + config +
        ", showList=" + showList +
        ", isSearch=" + isSearch +
        "}";
    }
}
