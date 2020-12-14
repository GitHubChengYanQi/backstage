package cn.atsoft.dasheng.gen.core.generator.at.controller;

import cn.atsoft.dasheng.db.model.params.FieldConfigParam;
import cn.atsoft.dasheng.db.model.result.FieldConfigResult;
import cn.atsoft.dasheng.db.service.FieldConfigService;
import cn.atsoft.dasheng.gen.core.generator.base.AbstractCustomGenerator;
import cn.atsoft.dasheng.gen.modular.service.GenerateService;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import org.beetl.core.Template;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.List;
import java.util.Map;

public class AtControllerGenerator extends AbstractCustomGenerator {

    @Autowired
    private FieldConfigService fieldConfigService;

    public AtControllerGenerator(Map<String, Object> tableContext) {
        super(tableContext);
    }

    @Override
    public void bindingOthers(Template template) {

        TableInfo table = (TableInfo) tableContext.get("table");
        List<TableField> fields = table.getFields();
        String tableName = table.getName();
        FieldConfigParam fieldConfigParam = new FieldConfigParam();
        fieldConfigParam.setTable(tableName);
        List<FieldConfigResult> result = fieldConfigService.findListBySpec(fieldConfigParam);

        // TODO 绑定title字段，生成select接口
        // TODO 绑定print字段，生成Tree接口
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
