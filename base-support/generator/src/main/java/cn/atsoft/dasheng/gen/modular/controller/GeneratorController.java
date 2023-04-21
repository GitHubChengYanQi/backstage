package cn.atsoft.dasheng.gen.modular.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.db.model.result.FieldConfigResult;
import cn.atsoft.dasheng.gen.core.enums.GenSessionKeyFlags;
import cn.atsoft.dasheng.gen.core.generator.at.AtApiExecutor;
import cn.atsoft.dasheng.gen.core.generator.base.model.ContextParam;
import cn.atsoft.dasheng.gen.core.generator.guns.GunsExecutor;
import cn.atsoft.dasheng.gen.core.generator.restful.RestfulApiExecutor;
import cn.atsoft.dasheng.gen.core.util.ConcatUtil;
import cn.atsoft.dasheng.gen.core.util.FieldsConfigHolder;
import cn.atsoft.dasheng.gen.modular.model.params.ExecuteParam;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import cn.atsoft.dasheng.base.db.entity.DatabaseInfo;
import cn.atsoft.dasheng.base.db.service.DatabaseInfoService;
import cn.atsoft.dasheng.base.pojo.page.LayuiPageInfo;
import cn.atsoft.dasheng.gen.core.enums.GenDownloadEnum;
import cn.atsoft.dasheng.gen.core.generator.restful.mybatisplus.param.MpParam;
import cn.atsoft.dasheng.gen.modular.model.FieldConfig;
import cn.atsoft.dasheng.gen.modular.model.GenSessionFieldConfigs;
import cn.atsoft.dasheng.gen.modular.model.params.SaveFieldConfigParam;
import cn.atsoft.dasheng.gen.modular.service.GenerateService;
import cn.atsoft.dasheng.core.util.HttpContext;
import cn.atsoft.dasheng.core.util.SpringContextHolder;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.model.response.SuccessResponseData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.*;
import java.util.*;


/**
 * 代码生成控制器
 *
 * @author fengshuonan
 * @date 2019-01-30-2:39 PM
 */
@Controller
@Slf4j
public class GeneratorController {

    @Autowired
    private GenerateService tableService;

    /**
     * 代码生成主页
     */
    @RequestMapping("/gen")
    public String index(Model model) {

        //获取数据源容器service
        DatabaseInfoService databaseInfoService = null;
        try {
            databaseInfoService = SpringContextHolder.getBean(DatabaseInfoService.class);
        } catch (Exception e) {
            throw new ServiceException(500, "请先开启数据源容器模块！");
        }

        List<DatabaseInfo> all = databaseInfoService.list(new QueryWrapper<>());
        model.addAttribute("dataSources", all);

        //清空session的缓存
        HttpSession session = HttpContext.getRequest().getSession();
        session.removeAttribute(GenSessionKeyFlags.TABLE_FIELD_STYLES.name());

        return "/modular/gen/gen.html";
    }

    /**
     * 跳转到字段列表页面
     */
    @RequestMapping("/tableFields")
    public String tableFields(@RequestParam("dbId") Long dbId,
                              @RequestParam("tableName") String tableName,
                              Model model) {

        model.addAttribute("tableName", tableName);
        model.addAttribute("dbId", dbId);

        return "/modular/gen/tableFields.html";
    }

    /**
     * 获取表的字段列表
     *
     * @author fengshuonan
     * @Date 2019/1/30 2:49 PM
     */
    @RequestMapping("/getTableFieldConfigs")
    @ResponseBody
    public LayuiPageInfo getTableFieldConfigs(@RequestParam("dbId") Long dbId,
                                              @RequestParam("tableName") String tableName) {

        //获取这个表的所有字段以及其他信息
        List<FieldConfig> tableFields = tableService.getTableFieldsConfig(dbId, tableName);

        LayuiPageInfo layuiPageInfo = new LayuiPageInfo();
        layuiPageInfo.setData(tableFields);
        layuiPageInfo.setCount(tableFields.size());

        return layuiPageInfo;
    }

    @RequestMapping("/getTableField")
    @ResponseBody
    public PageInfo getTableField(@RequestParam("dbId") Long dbId,
                                  @RequestParam("tableName") String tableName) {

        //获取这个表的所有字段以及其他信息
        List<FieldConfigResult> tableFields = tableService.getTableFields(dbId, tableName);

        PageInfo PageInfo = new PageInfo();
        PageInfo.setData(tableFields);
        PageInfo.setCount(tableFields.size());

        return PageInfo;
    }
    /**
     * 设置条件字段
     */
    @RequestMapping("/saveFieldsConfig")
    @ResponseBody
    public ResponseData saveFieldsConfig(@RequestBody @Valid SaveFieldConfigParam saveFieldConfigParam) {
        tableService.setTableFieldsConfig(saveFieldConfigParam);
        return new SuccessResponseData();
    }

    /**
     * 执行代码生成
     */
    @RequestMapping(value = "/execute", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity execute(ExecuteParam executeParam) {

        //获取字符串拼接数组
        String[] tableArray = ConcatUtil.getArray(executeParam.getTables());

        //获取跳转类型的数组
        String[] jumpTypeArray = ConcatUtil.getArray(executeParam.getJumpTypeStr());

        Set<String> jumpTypeSet = CollUtil.newHashSet(jumpTypeArray);
        Map<String, Boolean> jumpTypeMap = new HashMap<>(8);
        for (String tableName : tableArray) {
            jumpTypeMap.put(tableName, jumpTypeSet.contains(tableName));
        }

        //获取数据源信息
        DatabaseInfoService databaseInfoService = null;
        try {
            databaseInfoService = SpringContextHolder.getBean(DatabaseInfoService.class);
        } catch (Exception e) {
            throw new ServiceException(500, "请先开启数据源容器模块！");
        }
        DatabaseInfo databaseInfo = databaseInfoService.getById(executeParam.getDataSourceId());

        ContextParam contextParam = new ContextParam();
        contextParam.setAuthor(executeParam.getAuthor());
        contextParam.setProPackage(executeParam.getProPackage());
        contextParam.setJdbcDriver(databaseInfo.getJdbcDriver());
        contextParam.setJdbcUserName(databaseInfo.getUserName());
        contextParam.setJdbcPassword(databaseInfo.getPassword());
        contextParam.setJdbcUrl(databaseInfo.getJdbcUrl());
        contextParam.setJumpTypeMap(jumpTypeMap);
        if ("Y".equalsIgnoreCase(executeParam.getSwagger())) {
            contextParam.setSwagger(true);
        } else {
            contextParam.setSwagger(false);
        }
        if ("Y".equalsIgnoreCase(executeParam.getRemote())) {
            contextParam.setRemote(true);
        } else {
            contextParam.setRemote(false);
        }

        //处理modularName，如果modularName传值不为空，则待上左斜杠路径
        if (ToolUtil.isNotEmpty(executeParam.getModularName())) {
            String modularName = "/" + executeParam.getModularName();
            contextParam.setModularName(modularName);
        }

        String outputPath = "";
        long fileName = IdWorker.getId();

        if (Objects.equals(GenDownloadEnum.DEFAULT_PATH.name(), executeParam.getGenLocation())) {

            //获取临时目录
            outputPath = System.getProperty("java.io.tmpdir") + File.separator + "gunsGeneration" + File.separator + fileName;
            contextParam.setGenDownloadEnum(GenDownloadEnum.DEFAULT_PATH);

        } else if (Objects.equals(GenDownloadEnum.PROJECT_PATH.name(), executeParam.getGenLocation())) {

            //获取项目的绝对路径 TODO 可作为参数变量
            outputPath = new File("").getAbsolutePath() + File.separator + "app" + File.separator + "src" + File.separator + "main";
            contextParam.setGenDownloadEnum(GenDownloadEnum.PROJECT_PATH);

        }

        contextParam.setOutputPath(outputPath);

        MpParam mpContextParam = new MpParam();
        mpContextParam.setGeneratorInterface(true);
        mpContextParam.setIncludeTables(tableArray);

        if (StrUtil.isNotEmpty(executeParam.getRemovePrefix())) {
            mpContextParam.setRemoveTablePrefix(new String[]{(executeParam.getRemovePrefix())});
        }

        try {
            //将session中的字段配置数据传递到threadlocal中
            HttpSession session = HttpContext.getRequest().getSession();
            GenSessionFieldConfigs sessionFieldConfigs = (GenSessionFieldConfigs) session.getAttribute(GenSessionKeyFlags.TABLE_FIELD_STYLES.name());
            FieldsConfigHolder.put(sessionFieldConfigs);

            //如果是Guns单体版本生成
            if (executeParam.getVersion().equalsIgnoreCase("single")) {
                GunsExecutor.executor(contextParam, mpContextParam);
            } else if(executeParam.getVersion().equalsIgnoreCase("at")){
                AtApiExecutor.executor(contextParam, mpContextParam);
            }else {
                //如果是微服务版本代码生成
                RestfulApiExecutor.executor(contextParam, mpContextParam);
            }

        } finally {
            FieldsConfigHolder.remove();
        }

        //如果是打包下载的
        if (Objects.equals(GenDownloadEnum.DEFAULT_PATH.name(), executeParam.getGenLocation())) {
            File zip = ZipUtil.zip(outputPath);
            return renderFile(fileName + ".zip", zip.getAbsolutePath());
        } else {
            return null;
        }
    }


    /**
     * 返回前台文件流
     */
    private ResponseEntity<InputStreamResource> renderFile(String fileName, InputStream inputStream) {
        InputStreamResource resource = new InputStreamResource(inputStream);
        String dfileName = null;
        try {
            dfileName = new String(fileName.getBytes("gb2312"), "iso8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", dfileName);
        return new ResponseEntity<>(resource, headers, HttpStatus.CREATED);
    }

    /**
     * 返回前台文件流
     */
    private ResponseEntity<InputStreamResource> renderFile(String fileName, String filePath) {
        try {
            final FileInputStream inputStream = new FileInputStream(filePath);
            return renderFile(fileName, inputStream);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("文件读取错误");
        }
    }

    /**
     * 返回前台文件流
     */
    private ResponseEntity<InputStreamResource> renderFile(String fileName, byte[] fileBytes) {
        return renderFile(fileName, new ByteArrayInputStream(fileBytes));
    }
    /**
     * 执行代码生成
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData add(ExecuteParam executeParam) {
        MpParam mpContextParam = new MpParam();
        mpContextParam.setGeneratorInterface(true);
//        mpContextParam.setIncludeTables(tableArray);
//        AtApiExecutor.batchAddColumn();
        return  ResponseData.success();
    }


}
