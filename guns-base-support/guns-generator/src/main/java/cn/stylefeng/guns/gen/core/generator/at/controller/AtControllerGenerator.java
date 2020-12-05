package cn.stylefeng.guns.gen.core.generator.at.controller;

import cn.stylefeng.guns.gen.core.generator.base.AbstractCustomGenerator;
import org.beetl.core.Template;

import java.util.Map;

public class AtControllerGenerator extends AbstractCustomGenerator {

    public AtControllerGenerator(Map<String, Object> tableContext) {
        super(tableContext);
    }

    @Override
    public void bindingOthers(Template template) {
        template.binding("controllerPackage", contextParam.getProPackage() + ".controller");
    }

    @Override
    public String getTemplateResourcePath() {
        return "/atTemplates/controller.java.btl";
    }

    @Override
    public String getGenerateFileTempPath() {
        return null;
    }

    @Override
    public String getGenerateFileDirectPath() {
        return null;
    }
}
