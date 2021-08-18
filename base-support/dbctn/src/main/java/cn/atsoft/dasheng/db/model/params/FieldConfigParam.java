package cn.atsoft.dasheng.db.model.params;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import cn.atsoft.dasheng.portal.banner.model.validator.BaseValidatingParam;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 字段配置
 * </p>
 *
 * @author Sing
 * @since 2020-12-12
 */
@Data
@ApiModel
public class FieldConfigParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private Long fieldId;

    /**
     * 字段名
     */
    @ApiModelProperty("字段名")
    private String fieldName;

    /**
     * 表名
     */
    @ApiModelProperty("表名")
    private String tableName;

    /**
     * 字段类型
     */
    @ApiModelProperty("字段类型")
    private String type;

    /**
     * 数据配置
     */
    @ApiModelProperty("数据配置")
    private JSONObject config;

    /**
     * 是否列表显示
     */
    @ApiModelProperty("是否列表显示")
    private String[] showList;

    /**
     * 是否搜索
     */
    @ApiModelProperty("是否搜索")
    private String[] isSearch;

    @ApiModelProperty("编辑页显示")
    private String[] inEdit;

    @Override
    public String checkParam() {
        return null;
    }

}
