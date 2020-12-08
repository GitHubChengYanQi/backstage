package cn.atsoft.dasheng.gen.core.generator.at.page;

import cn.atsoft.dasheng.gen.core.generator.base.AbstractCustomGenerator;
import org.beetl.core.Template;

import java.io.File;
import java.util.Map;

public class AtPageListGenerator extends AbstractCustomGenerator {

    public AtPageListGenerator(Map<String, Object> tableContext) {
        super(tableContext);
    }

    @Override
    public void bindingOthers(Template template) {
        super.bindingConditionParams(template);
    }

    @Override
    public String getTemplateResourcePath() {
        return "/atTemplates/List.js.btl";
    }

    @Override
    public String getGenerateFileTempPath() {
        String lowerEntity = (String) this.tableContext.get("lowerEntity");
        File file = new File(contextParam.getOutputPath() + "/page/" + lowerEntity + "/" + lowerEntity + "List/" + lowerEntity + "List.js");
        return file.getAbsolutePath();
    }

    @Override
    public String getGenerateFileDirectPath() {
        String lowerEntity = (String) this.tableContext.get("lowerEntity");
        File file = new File(contextParam.getOutputPath() + "/page/" + lowerEntity + "/" + lowerEntity + "List/" + lowerEntity + "List.js");
        return file.getAbsolutePath();
    }
}
