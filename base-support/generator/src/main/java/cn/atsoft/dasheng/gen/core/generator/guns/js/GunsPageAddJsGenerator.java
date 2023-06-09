package cn.atsoft.dasheng.gen.core.generator.guns.js;

import cn.atsoft.dasheng.gen.core.generator.base.AbstractCustomGenerator;
import org.beetl.core.Template;

import java.io.File;
import java.util.Map;

/**
 * Guns添加页面js生成器
 *
 * @author fengshuonan
 * @date 2018-12-13-2:20 PM
 */
public class GunsPageAddJsGenerator extends AbstractCustomGenerator {

    public GunsPageAddJsGenerator(Map<String, Object> tableContext) {
        super(tableContext);
    }

    @Override
    public void bindingOthers(Template template) {
        super.bindingInputsParams(template);
    }

    @Override
    public String getTemplateResourcePath() {
        return "/gunsTemplates/page_add.js.btl";
    }

    @Override
    public String getGenerateFileTempPath() {
        String lowerEntity = (String) this.tableContext.get("lowerEntity");
        File file = new File(contextParam.getOutputPath() + "/js/" + lowerEntity + "/" + lowerEntity + "_add.js");
        return file.getAbsolutePath();
    }

    @Override
    public String getGenerateFileDirectPath() {
        String lowerEntity = (String) this.tableContext.get("lowerEntity");
        File file = new File(contextParam.getOutputPath() + "/webapp/assets/" + lowerEntity + "/" + lowerEntity + "_add.js");
        return file.getAbsolutePath();
    }
}
