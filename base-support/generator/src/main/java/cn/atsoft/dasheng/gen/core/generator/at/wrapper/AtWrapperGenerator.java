package cn.atsoft.dasheng.gen.core.generator.at.wrapper;

import cn.atsoft.dasheng.gen.core.generator.base.AbstractCustomGenerator;
import org.beetl.core.Template;

import java.io.File;
import java.util.Map;

public class AtWrapperGenerator extends AbstractCustomGenerator {

    public AtWrapperGenerator(Map<String, Object> tableContext) {
        super(tableContext);
    }

    @Override
    public void bindingOthers(Template template) {
        template.binding("wrapperPackage", contextParam.getProPackage() + ".wrapper");
    }

    @Override
    public String getTemplateResourcePath() {
        return "/atTemplates/wrapper.java.btl";
    }

    @Override
    public String getGenerateFileTempPath() {
        String proPackage = this.contextParam.getProPackage();
        String proPath = proPackage.replaceAll("\\.", "/");
        File file = new File(contextParam.getOutputPath() + "/" + proPath + "/wrapper/" + tableContext.get("entity") + "SelectWrapper.java");
        return file.getAbsolutePath();
    }

    @Override
    public String getGenerateFileDirectPath() {
        String proPackage = this.contextParam.getProPackage();
        String proPath = proPackage.replaceAll("\\.", "/");
        File file = new File(contextParam.getOutputPath() + "/java/" + proPath + "/wrapper/" + tableContext.get("entity") + "SelectWrapper.java");
        return file.getAbsolutePath();
    }
}
