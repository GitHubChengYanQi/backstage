package cn.atsoft.dasheng.gen.core.generator.at.controller;

import cn.atsoft.dasheng.core.util.SpringContextHolder;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.db.entity.DBFieldConfig;
import cn.atsoft.dasheng.db.model.params.FieldConfigParam;
import cn.atsoft.dasheng.db.service.FieldConfigService;
import cn.atsoft.dasheng.gen.core.generator.base.AbstractCustomGenerator;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import org.beetl.core.Template;

import java.io.File;
import java.util.List;
import java.util.Map;

public class AtControllerGenerator extends AbstractCustomGenerator {

    public AtControllerGenerator(Map<String, Object> tableContext) {
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

        template.binding("keyField", "");
        template.binding("titleField", "");
        template.binding("parentField", "");

        for (DBFieldConfig dbFieldConfig : result) {
            // 设置主键字段
            for (TableField tableField : fields) {
                if (tableField.isKeyFlag() && tableField.getName().equals(dbFieldConfig.getFieldName())) {
                    template.binding("keyField", dbFieldConfig.getFieldName());
                }
            }

            // 绑定title字段，生成select接口
            if (ToolUtil.isNotEmpty(dbFieldConfig.getType()) && dbFieldConfig.getType().equals("title")) {
                for (TableField tableField : fields) {
                    if (tableField.getName().equals(dbFieldConfig.getFieldName())) {
                        template.binding("titleField", dbFieldConfig.getFieldName());
                    }
                }
            }
            // 绑定print字段，生成Tree接口
            if (ToolUtil.isNotEmpty(dbFieldConfig.getType()) && dbFieldConfig.getType().equals("parentKey")) {
                for (TableField tableField : fields) {
                    if (tableField.getName().equals(dbFieldConfig.getFieldName())) {
                        template.binding("parentField", dbFieldConfig.getFieldName());
                    }
                }
            }
        }

        template.binding("wrapperPackage", contextParam.getProPackage() + ".wrapper." + tableContext.get("entity") + "SelectWrapper");
        template.binding("controllerPackage", contextParam.getProPackage() + ".controller");
    }

    @Override
    public String getTemplateResourcePath() {
        return "/atTemplates/controller.java.btl";
    }

    @Override
    public String getGenerateFileTempPath() {
        String proPackage = this.contextParam.getProPackage();
        String proPath = proPackage.replaceAll("\\.", "/");
        File file = new File(contextParam.getOutputPath() + "/" + proPath + "/controller/" + tableContext.get("entity") + "Controller.java");
        return file.getAbsolutePath();
    }

    @Override
    public String getGenerateFileDirectPath() {
        String proPackage = this.contextParam.getProPackage();
        String proPath = proPackage.replaceAll("\\.", "/");
        File file = new File(contextParam.getOutputPath() + "/java/" + proPath + "/controller/" + tableContext.get("entity") + "Controller.java");
        return file.getAbsolutePath();
    }
}
