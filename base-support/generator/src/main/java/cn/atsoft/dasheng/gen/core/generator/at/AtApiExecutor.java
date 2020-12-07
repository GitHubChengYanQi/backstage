package cn.atsoft.dasheng.gen.core.generator.at;

import cn.atsoft.dasheng.gen.core.generator.at.mybatisplus.AtMpGenerator;
import cn.atsoft.dasheng.gen.core.generator.base.model.ContextParam;
import cn.atsoft.dasheng.gen.core.generator.at.controller.AtControllerGenerator;
import cn.atsoft.dasheng.gen.core.generator.restful.mybatisplus.param.MpParam;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;

import java.util.List;
import java.util.Map;

public class AtApiExecutor {
    public static void executor(ContextParam contextParam, MpParam mpContext) {

        AtMpGenerator atMpGenerator = new AtMpGenerator(mpContext);
        atMpGenerator.initContext(contextParam);
        atMpGenerator.doGeneration();

        List<TableInfo> tableInfos = atMpGenerator.getTableInfos();
        Map<String, Map<String, Object>> everyTableContexts = atMpGenerator.getEveryTableContexts();
        //遍历所有表
        for (TableInfo tableInfo : tableInfos) {
            Map<String, Object> map = everyTableContexts.get(tableInfo.getName());

            // 生成控制器
            AtControllerGenerator atControllerGenerator = new AtControllerGenerator(map);
            atControllerGenerator.initContext(contextParam);
            atControllerGenerator.doGeneration();
        }
    }
}
