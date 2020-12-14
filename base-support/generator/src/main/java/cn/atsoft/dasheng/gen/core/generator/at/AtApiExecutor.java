package cn.atsoft.dasheng.gen.core.generator.at;

import cn.atsoft.dasheng.db.model.params.FieldConfigParam;
import cn.atsoft.dasheng.db.model.result.FieldConfigResult;
import cn.atsoft.dasheng.db.service.FieldConfigService;
import cn.atsoft.dasheng.gen.core.generator.at.mybatisplus.AtMpGenerator;
import cn.atsoft.dasheng.gen.core.generator.at.page.AtPageApiGenerator;
import cn.atsoft.dasheng.gen.core.generator.at.page.AtPageEditGenerator;
import cn.atsoft.dasheng.gen.core.generator.at.page.AtPageListGenerator;
import cn.atsoft.dasheng.gen.core.generator.at.page.AtPageRouterGenerator;
import cn.atsoft.dasheng.gen.core.generator.base.model.ContextParam;
import cn.atsoft.dasheng.gen.core.generator.at.controller.AtControllerGenerator;
import cn.atsoft.dasheng.gen.core.generator.restful.mybatisplus.param.MpParam;
import cn.atsoft.dasheng.gen.modular.service.GenerateService;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class AtApiExecutor {

    public static void executor(ContextParam contextParam, MpParam mpContext) {

        AtMpGenerator atMpGenerator = new AtMpGenerator(mpContext);
        atMpGenerator.initContext(contextParam);
        atMpGenerator.doGeneration();

        List<TableInfo> tableInfos = atMpGenerator.getTableInfos();
        Map<String, Map<String, Object>> everyTableContexts = atMpGenerator.getEveryTableContexts();

        // 遍历所有表
        for (TableInfo tableInfo : tableInfos) {

            String tableName = tableInfo.getName();
            Map<String, Object> map = everyTableContexts.get(tableName);

            // 生成控制器
            AtControllerGenerator atControllerGenerator = new AtControllerGenerator(map);
            atControllerGenerator.initContext(contextParam);
            atControllerGenerator.doGeneration();

            // 生成接口路径文件
            AtPageApiGenerator  atPageApiGenerator = new AtPageApiGenerator(map);
            atPageApiGenerator.initContext(contextParam);
            atPageApiGenerator.doGeneration();

            // 生成编辑JS
            AtPageEditGenerator atPageEditGenerator = new AtPageEditGenerator(map);
            atPageEditGenerator.initContext(contextParam);
            atPageEditGenerator.doGeneration();

            // 生成列表JS
            AtPageListGenerator atPageListGenerator = new AtPageListGenerator(map);
            atPageListGenerator.initContext(contextParam);
            atPageListGenerator.doGeneration();

            // 生成路由js
            AtPageRouterGenerator atPageRouterGenerator = new AtPageRouterGenerator(map);
            atPageRouterGenerator.initContext(contextParam);
            atPageRouterGenerator.doGeneration();
        }
    }
}
