package cn.atsoft.dasheng.gen.core.generator.at.page;

import cn.atsoft.dasheng.gen.core.generator.base.AbstractCustomGenerator;


import java.io.File;
import java.util.Map;

public class AtPageEditGenerator extends AbstractCustomGenerator {

    public AtPageEditGenerator(Map<String, Object> tableContext) {
        super(tableContext);
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
