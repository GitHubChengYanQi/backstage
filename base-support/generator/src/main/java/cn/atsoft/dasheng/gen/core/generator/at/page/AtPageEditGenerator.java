package cn.atsoft.dasheng.gen.core.generator.at.page;

import cn.atsoft.dasheng.core.util.SpringContextHolder;
import cn.atsoft.dasheng.db.entity.DBFieldConfig;
import cn.atsoft.dasheng.db.model.params.FieldConfigParam;
import cn.atsoft.dasheng.db.service.FieldConfigService;
import cn.atsoft.dasheng.gen.core.generator.base.AbstractCustomGenerator;
import cn.atsoft.dasheng.gen.core.util.TemplateUtil;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import org.beetl.core.Template;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AtPageEditGenerator extends AbstractCustomGenerator {

    public AtPageEditGenerator(Map<String, Object> tableContext) {
        super(tableContext);
    }

    @Override
    public void bindingOthers(Template template) {

        TableInfo table = (TableInfo) tableContext.get("table");
        List<TableField> fields = table.getFields();
        String tableName = table.getName();
        FieldConfigParam fieldConfigParam = new FieldConfigParam();
        fieldConfigParam.setTableName(tableName);

        FieldConfigService fieldConfigService = SpringContextHolder.getBean(FieldConfigService.class);
        List<DBFieldConfig> result = fieldConfigService.findListBySpec(fieldConfigParam);

        List<Map<String,Object>> fieldConfigs = new ArrayList<>();

        for (DBFieldConfig dbFieldConfig : result) {
            for (TableField tableField : fields) {
                if (tableField.getName().equals(dbFieldConfig.getFieldName())) {
                    Map<String,Object> field = new HashMap<>();
                    field.put("propertyName", TemplateUtil.upperFirst(tableField.getPropertyName()));
                    field.put("comment", tableField.getComment());
                    field.put("camelFieldName", tableField.getPropertyName());
                    field.put("keyFlag", tableField.isKeyFlag());
                    field.put("type",dbFieldConfig.getType());
                    field.put("config",dbFieldConfig.getConfig());
                    field.put("showList",dbFieldConfig.getShowList());
                    field.put("isSearch",dbFieldConfig.getIsSearch());
                    field.put("inEdit",dbFieldConfig.getInEdit());
                    fieldConfigs.add(field);
                    break;
                }
            }
        }
        template.binding("sysFieldConfigs", fieldConfigs);
    }

    @Override
    public String getTemplateResourcePath() {
        return "/atTemplates/edit.js.btl";
    }

    @Override
    public String getGenerateFileTempPath() {
        String lowerEntity = (String) this.tableContext.get("lowerEntity");
        File file = new File(contextParam.getOutputPath() + "/page/" + lowerEntity + "/" + lowerEntity + "Edit/index.jsx");
        return file.getAbsolutePath();
    }

    @Override
    public String getGenerateFileDirectPath() {
        String lowerEntity = (String) this.tableContext.get("lowerEntity");
        File file = new File(contextParam.getOutputPath() + "/page/" + lowerEntity + "/" + lowerEntity + "Edit/index.jsx");
        return file.getAbsolutePath();
    }
}
