package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.Excel.WordUtils;
import cn.atsoft.dasheng.app.entity.Contract;
import cn.atsoft.dasheng.app.model.result.BatchDeleteRequest;
import cn.atsoft.dasheng.app.pojo.Lable;
import cn.atsoft.dasheng.app.wrapper.TemplateSelectWrapper;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Template;
import cn.atsoft.dasheng.app.model.params.TemplateParam;
import cn.atsoft.dasheng.app.model.result.TemplateResult;
import cn.atsoft.dasheng.app.service.TemplateService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.sys.modular.system.entity.FileInfo;
import cn.atsoft.dasheng.sys.modular.system.service.FileInfoService;
import cn.hutool.poi.word.DocUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 合同模板控制器
 *
 * @author
 * @Date 2021-07-21 08:22:02
 */
@RestController
@RequestMapping("/template")
@Api(tags = "合同模板")
public class TemplateController extends BaseController {

    @Autowired
    private TemplateService templateService;
    @Autowired
    private FileInfoService fileInfoService;


    /**
     * 新增接口
     *
     * @author
     * @Date 2021-07-21
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody TemplateParam templateParam) {
        Long add = this.templateService.add(templateParam);
        return ResponseData.success(add);
    }

    /**
     * 编辑接口
     *
     * @author
     * @Date 2021-07-21
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody TemplateParam templateParam) {

        this.templateService.update(templateParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author
     * @Date 2021-07-21
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody TemplateParam templateParam) {
        this.templateService.delete(templateParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author
     * @Date 2021-07-21
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<TemplateResult> detail(@RequestBody TemplateParam templateParam) {
        Template detail = this.templateService.getById(templateParam.getTemplateId());
        TemplateResult result = new TemplateResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author
     * @Date 2021-07-21
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<TemplateResult> list(@RequestBody(required = false) TemplateParam templateParam) {
        if (ToolUtil.isEmpty(templateParam)) {
            templateParam = new TemplateParam();
        }
//        return this.templateService.findPageBySpec(templateParam);
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.templateService.findPageBySpec(templateParam, null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope());
            return this.templateService.findPageBySpec(templateParam, dataScope);
        }
    }


    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    public ResponseData<List<Map<String, Object>>> listSelect() {
        QueryWrapper<Template> templateQueryWrapper = new QueryWrapper<>();
        templateQueryWrapper.in("display", 1);
        List<Map<String, Object>> list = this.templateService.listMaps(templateQueryWrapper);
        TemplateSelectWrapper templateSelectWrapper = new TemplateSelectWrapper(list);
        List<Map<String, Object>> result = templateSelectWrapper.wrap();
        return ResponseData.success(result);
    }

    /**
     * 批量删除
     *
     * @author
     * @Date 2021-07-21
     */
    @RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData batchDelete(@RequestBody BatchDeleteRequest batchDeleteRequest) {
        this.templateService.batchDelete(batchDeleteRequest.getIds());
        return ResponseData.success();
    }


    @RequestMapping(value = "/getLabel", method = RequestMethod.GET)
    public ResponseData getLabel(@RequestParam Long id) {
        Lable label = this.templateService.getLabel(id);
        return ResponseData.success(label);
    }




    @RequestMapping(value = "/getWordLables", method = RequestMethod.GET)
    public ResponseData getWordLable(@RequestParam("id") Long id) {
        List<String> list = new ArrayList<>();

        Template template = templateService.getById(id);

        if (ToolUtil.isNotEmpty(template) && ToolUtil.isNotEmpty(template.getFileId())) {
            FileInfo fileInfo = fileInfoService.getById(template.getFileId());
            WordUtils wordUtils = new WordUtils();

            XWPFDocument document = DocUtil.create(new File(fileInfo.getFilePath()));

            list.addAll(wordUtils.getLabel(document));


            // 替换表格中的参数
            list.addAll(wordUtils.getLabelInTable(document, new int[]{0}));

        }

        List<String> list1 = new ArrayList<>();
        list1.add("采购合同编号");
        list1.add("合同签订地点");
        list1.add("合同签订时间");
        list1.add("需方公司名称");
        list1.add("供方公司名称");
        list1.add("提取(交付)地点");
        list1.add("接货人员");
        list1.add("接货人电话");
        list1.add("需方公司地址");
        list1.add("需方公司电话");
        list1.add("需方公司传真");
        list1.add("需方法人代表");
        list1.add("需方法人电话");
        list1.add("需方委托代表");
        list1.add("需方代表电话");
        list1.add("需方开户银行");
        list1.add("需方银行账号");
        list1.add("需方开户行号");
        list1.add("需方邮政编码");
        list1.add("需方公司电邮");
        list1.add("需方税号");
        list1.add("交货地址");
        list1.add("供货人及电话");
        list1.add("供方公司地址");
        list1.add("供方公司电话");
        list1.add("供方公司传真");
        list1.add("供方法人代表");
        list1.add("供方法人电话");
        list1.add("供方委托代表");
        list1.add("供方代表电话");
        list1.add("供方开户银行");
        list1.add("供方银行账号");
        list1.add("供方开户行号");
        list1.add("供方邮政编码");
        list1.add("供方公司电邮");
        list1.add("供方税号");
        list1.add("日期方式");
        list1.add("付款比例");
        list1.add("付款日期");
        list1.add("物料编码");
        list1.add("产品名称");
        list1.add("型号规格");
        list1.add("品牌厂家");
        list1.add("单位");
        list1.add("总价");
        list1.add("数量");
        list1.add("单价");
        list1.add("交货日期");
        list1.add("合计金额大写");
        list1.add("合计金额小写");
        list1.add("产品备注");
        list1.add("发票类型");

        for (int i = 0; i < list.size(); ) {
            String s1 = list.get(i);
            if (ToolUtil.isNotEmpty(s1)) {
                for (String s : list1) {
                    if (s1.equals(s)) {
                        list.remove(i);
                    } else {
                        i++;
                    }
                }
            }
        }






        return ResponseData.success(list);
    }
}


