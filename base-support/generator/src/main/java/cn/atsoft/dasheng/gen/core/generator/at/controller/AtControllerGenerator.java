package cn.atsoft.dasheng.gen.core.generator.at.controller;

import cn.atsoft.dasheng.gen.core.generator.base.AbstractCustomGenerator;
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
