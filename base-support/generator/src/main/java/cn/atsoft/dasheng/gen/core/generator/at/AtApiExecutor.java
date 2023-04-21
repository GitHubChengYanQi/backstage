package cn.atsoft.dasheng.gen.core.generator.at;

import cn.atsoft.dasheng.gen.core.generator.at.mybatisplus.AtMpGenerator;
import cn.atsoft.dasheng.gen.core.generator.at.page.*;
import cn.atsoft.dasheng.gen.core.generator.at.wrapper.AtWrapperGenerator;
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

        // 遍历所有表
        for (TableInfo tableInfo : tableInfos) {

            String tableName = tableInfo.getName();
            Map<String, Object> map = everyTableContexts.get(tableName);

            // 生成控制器
            AtControllerGenerator atControllerGenerator = new AtControllerGenerator(map);
            atControllerGenerator.initContext(contextParam);
            atControllerGenerator.doGeneration();

            // 生成 select数据的wrapper
            AtWrapperGenerator atWrapperGenerator = new AtWrapperGenerator(map);
            atWrapperGenerator.initContext(contextParam);
            atWrapperGenerator.doGeneration();

            // 生成字段配置文件
            AtPageFieldGenerator atPageFieldGenerator = new AtPageFieldGenerator(map);
            atPageFieldGenerator.initContext(contextParam);
            atPageFieldGenerator.doGeneration();

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
    public static void batchAddColumn(ContextParam contextParam, MpParam mpContext) {

        AtMpGenerator atMpGenerator = new AtMpGenerator(mpContext);
        atMpGenerator.initContext(contextParam);
        atMpGenerator.doGeneration();

        List<TableInfo> tableInfos = atMpGenerator.getTableInfos();
        Map<String, Map<String, Object>> everyTableContexts = atMpGenerator.getEveryTableContexts();
        StringBuffer sqlALl = new StringBuffer();
        // 遍历所有表
        for (TableInfo tableInfo : tableInfos) {

            String tableName = tableInfo.getName();
            Map<String, Object> map = everyTableContexts.get(tableName);

            String sql = "ALTER TABLE `"+tableName+"` \n" +
                    "ADD COLUMN `tenant_id` bigint(20) NOT NULL AFTER `deptId`;";
            sqlALl.append(sql);
        }
        System.out.println(sqlALl);
    }
}
