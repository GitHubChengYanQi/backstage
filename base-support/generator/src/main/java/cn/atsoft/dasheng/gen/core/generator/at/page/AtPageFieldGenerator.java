package cn.atsoft.dasheng.gen.core.generator.at.page;

import cn.atsoft.dasheng.core.util.SpringContextHolder;
import cn.atsoft.dasheng.core.util.ToolUtil;
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

public class AtPageFieldGenerator extends AbstractCustomGenerator {

    public AtPageFieldGenerator(Map<String, Object> tableContext) {
        super(tableContext);
    }

    @Override
    public String getTemplateResourcePath() {
        return "/atTemplates/field.js.btl";
    }

    @Override
    public String getGenerateFileTempPath() {
        String lowerEntity = (String) this.tableContext.get("lowerEntity");
        File file = new File(contextParam.getOutputPath() + "/page/" + lowerEntity + "/" + lowerEntity + "Field/index.jsx");
        return file.getAbsolutePath();
    }

    @Override
    public String getGenerateFileDirectPath() {
        String lowerEntity = (String) this.tableContext.get("lowerEntity");
        File file = new File(contextParam.getOutputPath() + "/page/" + lowerEntity + "/" + lowerEntity + "Field/index.jsx");
        return file.getAbsolutePath();
    }
}
