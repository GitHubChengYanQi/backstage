package cn.atsoft.dasheng.db.controller;

import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.db.entity.DBFieldConfig;
import cn.atsoft.dasheng.db.model.params.FieldConfigParam;
import cn.atsoft.dasheng.db.model.params.FieldConfigPostParam;
import cn.atsoft.dasheng.db.service.FieldConfigService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * 字段配置控制器
 *
 * @author Sing
 * @Date 2020-12-12 10:33:42
 */
@RestController
@RequestMapping("/fieldConfig")
@Api(tags = "字段配置")
public class RestFieldConfigController extends BaseController {

    @Autowired
    private FieldConfigService fieldConfigService;

    /**
     * 新增接口
     *
     * @author Sing
     * @Date 2020-12-12
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("字段信息保存")
    public ResponseData addItem(@RequestBody FieldConfigPostParam fieldConfigPostParam) {

        if (ToolUtil.isEmpty(fieldConfigPostParam.getTableName()) || ToolUtil.isEmpty(fieldConfigPostParam.getFieldLists())) {
            throw new ServiceException(500, "参数错误");
        }

        String tableName = fieldConfigPostParam.getTableName();

        Collection<DBFieldConfig> fieldConfigs = new ArrayList<>();
        List<FieldConfigParam> fieldLists = fieldConfigPostParam.getFieldLists();



        for (FieldConfigParam fieldConfigParam : fieldLists) {
            DBFieldConfig fieldConfig = new DBFieldConfig();
            if (ToolUtil.isEmpty(fieldConfigParam.getFieldName())) {
                throw new ServiceException(500, "参数错误");
            }
            ToolUtil.copyProperties(fieldConfigParam, fieldConfig);
            fieldConfig.setTableName(tableName);
//            fieldConfigParam.setTableName(tableName);
            fieldConfig.setFieldId(null);
            fieldConfigs.add(fieldConfig);
        }
        QueryWrapper<DBFieldConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("table_name",tableName);
        this.fieldConfigService.remove(queryWrapper);
        for (DBFieldConfig configItem : fieldConfigs) {
            this.fieldConfigService.save(configItem);
        }
//        this.fieldConfigService.saveBatch(fieldConfigs);
        return ResponseData.success();
    }
}


