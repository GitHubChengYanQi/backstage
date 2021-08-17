package cn.atsoft.dasheng.gen.modular.model.params;

import cn.atsoft.dasheng.gen.modular.model.FieldConfig;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.portal.model.validator.BaseValidatingParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 保存字段配置的请求参数
 *
 * @author fengshuonan
 * @date 2020-01-19-5:19 下午
 */
@Data
public class SaveFieldConfigParam implements BaseValidatingParam {

    /**
     * 表名称
     */
    @NotBlank
    private String tableName;

    /**
     * 表字段配置
     */
    @NotEmpty
    private List<FieldConfig> fieldConfigList;

    @Override
    public String checkParam() {
        if (ToolUtil.isEmpty(tableName)) {
            return "参数表名为空！";
        } else if (ToolUtil.isEmpty(fieldConfigList)) {
            return "参数表对应的字段属性为空！";
        } else {
            return null;
        }
    }
}
