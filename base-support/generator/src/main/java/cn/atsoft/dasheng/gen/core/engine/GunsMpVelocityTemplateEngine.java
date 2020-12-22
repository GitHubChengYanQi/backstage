/**
 * Copyright (c) 2011-2020, hubin (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cn.atsoft.dasheng.gen.core.engine;

import cn.atsoft.dasheng.core.util.SpringContextHolder;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.db.entity.DBFieldConfig;
import cn.atsoft.dasheng.db.model.params.FieldConfigParam;
import cn.atsoft.dasheng.db.service.FieldConfigService;
import cn.atsoft.dasheng.gen.core.util.TemplateUtil;
import cn.atsoft.dasheng.gen.modular.model.FieldConfig;
import cn.atsoft.dasheng.gen.modular.model.GenSessionFieldConfigs;
import cn.atsoft.dasheng.gen.core.util.FieldsConfigHolder;
import cn.atsoft.dasheng.gen.core.util.TableInfoUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 重写Mybatis-Plus的Velocity模板引擎实现
 * </p>
 */
public class GunsMpVelocityTemplateEngine extends VelocityTemplateEngine {

    private static final String DOT_VM = ".vm";
    private VelocityEngine velocityEngine;

    @Override
    public VelocityTemplateEngine init(ConfigBuilder configBuilder) {
        super.init(configBuilder);
        if (null == velocityEngine) {
            Properties p = new Properties();
            p.setProperty(ConstVal.VM_LOAD_PATH_KEY, ConstVal.VM_LOAD_PATH_VALUE);
            p.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, StringPool.EMPTY);
            p.setProperty(Velocity.ENCODING_DEFAULT, ConstVal.UTF8);
            p.setProperty(Velocity.INPUT_ENCODING, ConstVal.UTF8);
            p.setProperty("file.resource.loader.unicode", StringPool.TRUE);
            velocityEngine = new VelocityEngine(p);
        }
        return this;
    }


    @Override
    public void writer(Map<String, Object> objectMap, String templatePath, String outputFile) throws Exception {
        if (StringUtils.isBlank(templatePath)) {
            return;
        }
        Template template = velocityEngine.getTemplate(templatePath, ConstVal.UTF8);
        try (FileOutputStream fos = new FileOutputStream(outputFile);
             OutputStreamWriter ow = new OutputStreamWriter(fos, ConstVal.UTF8);
             BufferedWriter writer = new BufferedWriter(ow)) {
            template.merge(new VelocityContext(objectMap), writer);
        }
        logger.debug("模板:" + templatePath + ";  文件:" + outputFile);
    }


    @Override
    public String templateFilePath(String filePath) {
        if (null == filePath || filePath.contains(DOT_VM)) {
            return filePath;
        }
        return filePath + DOT_VM;
    }

    /**
     * 重写父类的方法，增加一个变量，为了在mapping.xml的Base_Column_List增加base前缀
     */
    @Override
    public Map<String, Object> getObjectMap(TableInfo tableInfo) {
        Map<String, Object> objectMap = super.getObjectMap(tableInfo);
        objectMap.put("tableRebuild", TableInfoUtil.getFieldNames(tableInfo));

        //获取当前线程变量获取的字段配置信息
        GenSessionFieldConfigs genSessionFieldConfigs = FieldsConfigHolder.get();
        List<FieldConfig> fieldConfigs = genSessionFieldConfigs.getFieldConfigs(tableInfo.getName());

        //带条件配置的字段
        List<FieldConfig> conditionFields = fieldConfigs.stream().filter(FieldConfig::getQueryConditionFlag)
                .collect(Collectors.toList());

        objectMap.put("mapperConditions", conditionFields);

        List<TableField> fields = tableInfo.getFields();
        String tableName = tableInfo.getName();
        FieldConfigParam fieldConfigParam = new FieldConfigParam();
        fieldConfigParam.setTableName(tableName);

        FieldConfigService fieldConfigService = SpringContextHolder.getBean(FieldConfigService.class);
        List<DBFieldConfig> result = fieldConfigService.findListBySpec(fieldConfigParam);

        List<Map<String,Object>> sysFieldConfigs = new ArrayList<>();

        objectMap.put("keyField", "");
        objectMap.put("titleField", "");
        objectMap.put("parentType", "");
        objectMap.put("parentField", "");
        objectMap.put("bigParentField", "");

        for (DBFieldConfig dbFieldConfig : result) {
            for (TableField tableField : fields) {

                if (tableField.isKeyFlag() && tableField.getName().equals(dbFieldConfig.getFieldName())) {
                    objectMap.put("keyField", dbFieldConfig.getFieldName());
                }

                if (ToolUtil.isNotEmpty(dbFieldConfig.getType()) && dbFieldConfig.getType().equals("parentKey") && tableField.getName().equals(dbFieldConfig.getFieldName())) {
                    objectMap.put("parentType", tableField.getColumnType());
                    objectMap.put("parentField", dbFieldConfig.getFieldName());
                    objectMap.put("bigParentField", StrUtil.upperFirst(dbFieldConfig.getFieldName()));
                }
                // 绑定title字段，生成select接口
                if (ToolUtil.isNotEmpty(dbFieldConfig.getType()) && dbFieldConfig.getType().equals("title") && tableField.getName().equals(dbFieldConfig.getFieldName())) {
                    objectMap.put("titleField", dbFieldConfig.getFieldName());
                }

                if (tableField.getName().equals(dbFieldConfig.getFieldName())) {
                    Map<String,Object> field = new HashMap<>();
                    field.put("propertyName", TemplateUtil.upperFirst(tableField.getPropertyName()));
                    field.put("comment", tableField.getComment());
                    field.put("camelFieldName", tableField.getPropertyName());
                    field.put("columnName", tableField.getColumnName());
                    field.put("columnType", tableField.getColumnType());
                    field.put("keyFlag", tableField.isKeyFlag());
                    field.put("type",dbFieldConfig.getType());
                    field.put("config",dbFieldConfig.getConfig());
                    field.put("showList",ToolUtil.isNotEmpty(dbFieldConfig.getShowList())?dbFieldConfig.getShowList(): new ArrayList<>());
                    field.put("isSearch",ToolUtil.isNotEmpty(dbFieldConfig.getIsSearch())?dbFieldConfig.getIsSearch(): new ArrayList<>());
                    field.put("inEdit",ToolUtil.isNotEmpty(dbFieldConfig.getInEdit())?dbFieldConfig.getInEdit(): new ArrayList<>());//dbFieldConfig.getInEdit());
                    sysFieldConfigs.add(field);
                    break;
                }
            }
        }
        objectMap.put("sysFieldConfigs", sysFieldConfigs);

        return objectMap;
    }
}
