package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.Excel.WordUtils;
import cn.atsoft.dasheng.Excel.pojo.ContractLabel;
import cn.atsoft.dasheng.Excel.pojo.LabelResult;
import cn.atsoft.dasheng.app.entity.Contract;
import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.entity.Template;
import cn.atsoft.dasheng.app.model.params.TemplateParam;
import cn.atsoft.dasheng.app.model.request.ContractDetailSetRequest;
import cn.atsoft.dasheng.app.model.result.ContractDetailResult;
import cn.atsoft.dasheng.app.model.result.ContractRequest;
import cn.atsoft.dasheng.app.model.result.TemplateResult;
import cn.atsoft.dasheng.app.service.TemplateService;
import cn.atsoft.dasheng.app.wrapper.ContractDetailSelectWrapper;
import cn.atsoft.dasheng.app.wrapper.ContractMachineSelectWrapper;
import cn.atsoft.dasheng.app.wrapper.CustomerSelectWrapper;
import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.model.params.ContractParam;
import cn.atsoft.dasheng.app.model.result.ContractResult;
import cn.atsoft.dasheng.app.service.ContractService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.entity.ContractTemplete;
import cn.atsoft.dasheng.crm.entity.ContractTempleteDetail;
import cn.atsoft.dasheng.crm.model.result.ContractTempleteDetailResult;
import cn.atsoft.dasheng.crm.model.result.ContractTempleteResult;
import cn.atsoft.dasheng.crm.pojo.ContractEnum;
import cn.atsoft.dasheng.crm.service.ContractTempleteDetailService;
import cn.atsoft.dasheng.crm.service.ContractTempleteService;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.sys.modular.system.entity.FileInfo;
import cn.atsoft.dasheng.sys.modular.system.service.FileInfoService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.word.DocUtil;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.File;
import java.util.*;


/**
 * 合同表控制器
 *
 * @author
 * @Date 2021-07-21 13:36:21
 */
@RestController
@RequestMapping("/contract")
@Api(tags = "合同表")
public class ContractController extends BaseController {

    @Autowired
    private ContractService contractService;
    private Long customerId;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private FileInfoService fileInfoService;
    @Autowired
    private ContractTempleteService contractTempleteService;
    @Autowired
    private ContractTempleteDetailService templeteDetailService;

    /**
     * 新增接口
     *
     * @author
     * @Date 2021-07-21
     */
    @Permission
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ContractParam contractParam) {
        if (contractParam.getTemplateId() != null) {
            TemplateParam templateParam = new TemplateParam();
            templateParam.setTemplateId(contractParam.getTemplateId());
            PageInfo<TemplateResult> pageBySpec = templateService.findPageBySpec(templateParam, null);
            contractParam.setContent(pageBySpec.getData().get(0).getContent());
        }
        ContractResult contractResult = this.contractService.addResult(contractParam);

        return ResponseData.success(contractResult);
    }

    /**
     * 编辑接口
     *
     * @author
     * @Date 2021-07-21
     */
    @Permission
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ContractParam contractParam) {
//        if (contractParam.getTemplateId()!=null){
//            TemplateParam templateParam = new TemplateParam();
//            templateParam.setTemplateId(contractParam.getTemplateId());
//            PageInfo<TemplateResult> pageBySpec = templateService.findPageBySpec(templateParam);
//            contractParam.setContent(pageBySpec.getData().get(0).getContent());
//        }
        this.contractService.update(contractParam);
        return ResponseData.success();
    }


    /**
     * 返回表格
     */
    @RequestMapping(value = "/getWordTables", method = RequestMethod.GET)
    public ResponseData getWordTables(@RequestParam("fileId") Long fileId) {
        FileInfo fileInfo = fileInfoService.getById(fileId);
        XWPFDocument document = DocUtil.create(new File(fileInfo.getFilePath()));
        List<XWPFTable> tables = document.getTables();


        List<List<List<String>>> tableList = new ArrayList<>();

        for (XWPFTable table : tables) {   //表格
            List<List<String>> lines = new ArrayList<>();
            List<XWPFTableRow> rows = table.getRows();
            for (XWPFTableRow row : rows) {   //行
                List<String> cells = new ArrayList<>();
                for (XWPFTableCell tableCell : row.getTableCells()) {   //列
                    cells.add(tableCell.getText());
                }
                lines.add(cells);
            }
            tableList.add(lines);
        }

        return ResponseData.success(tableList);
    }

    /**
     * 删除接口
     *
     * @author
     * @Date 2021-07-21
     */
    @Permission
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ContractParam contractParam) {
        this.contractService.delete(contractParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author
     * @Date 2021-07-21
     */
    @Permission
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<ContractResult> detail(@RequestBody ContractParam contractParam) {
        Long customerId = contractParam.getContractId();
        ContractResult detail = contractService.detail(customerId);
        return ResponseData.success(detail);
    }

    /**
     * 查询列表
     *
     * @author
     * @Date 2021-07-21
     */
    @Permission
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ContractResult> list(@RequestBody(required = false) ContractParam contractParam) {
        if (ToolUtil.isEmpty(contractParam)) {
            contractParam = new ContractParam();
        }
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.contractService.findPageBySpec(contractParam, null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope());
            return this.contractService.findPageBySpec(contractParam, dataScope);
        }
    }

    @Permission
    @RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
    @ApiOperation("批量删除")
    public ResponseData batchDelete(@RequestBody ContractRequest contractIdRequest) {
        contractService.batchDelete(contractIdRequest.getContractId());
        return ResponseData.success();
    }

    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    @Permission
    public ResponseData<List<Map<String, Object>>> listSelect(@RequestBody(required = false) ContractParam contractParam) {
        QueryWrapper<Contract> queryWrapper = new QueryWrapper();
        queryWrapper.in("display", 1);
        if (ToolUtil.isNotEmpty(contractParam) && ToolUtil.isNotEmpty(contractParam.getContractId())) {
            queryWrapper.in("contract_id", contractParam.getContractId());
        }
        List<Map<String, Object>> list = this.contractService.listMaps(queryWrapper);
        ContractMachineSelectWrapper customerSelectWrapper = new ContractMachineSelectWrapper(list);
        List<Map<String, Object>> result = customerSelectWrapper.wrap();
        return ResponseData.success(result);
    }

    @RequestMapping(value = "/pendingProductionPlan", method = RequestMethod.POST)
    @ApiOperation("待生产计划")
    public ResponseData pendingProductionPlan() {
        Set<ContractDetailSetRequest> contractDetailSetRequests = this.contractService.pendingProductionPlan();
        return ResponseData.success(contractDetailSetRequests);
    }


    @RequestMapping(value = "/getLabel", method = RequestMethod.GET)
    public ResponseData getLable(@RequestParam("templateId") Long templateId) {
        List<String> list = new ArrayList<>();
        Template template = templateService.getById(templateId);

        if (ToolUtil.isNotEmpty(template) && ToolUtil.isNotEmpty(template.getFileId())) {
            FileInfo fileInfo = fileInfoService.getById(template.getFileId());
            WordUtils wordUtils = new WordUtils();
            XWPFDocument document = DocUtil.create(new File(fileInfo.getFilePath()));
            list.addAll(wordUtils.getLabel(document));
            // 替换表格中的参数
            list.addAll(wordUtils.getLabelInTable(document, new int[]{0}));
        }


        List<String> newStrings = new ArrayList<>();
        for (String s : list) {
            boolean judge = judge(s);
            if (judge) {
                newStrings.add(s);
            }
        }


        List<ContractTemplete> templetes = list.size() == 0 ? new ArrayList<>() : contractTempleteService.query().in("name", newStrings).list();
        List<ContractTempleteResult> contractTempleteResults = BeanUtil.copyToList(templetes, ContractTempleteResult.class, new CopyOptions());
        List<Long> ids = new ArrayList<>();
        for (ContractTemplete templete : templetes) {
            ids.add(templete.getContractTemplateId());
        }
        List<ContractTempleteDetail> templeteDetails = ids.size() == 0 ? new ArrayList<>() : templeteDetailService.query().in("contract_templete_id", ids).list();
        List<ContractTempleteDetailResult> templeteDetailResults = BeanUtil.copyToList(templeteDetails, ContractTempleteDetailResult.class, new CopyOptions());


        List<LabelResult> results = new ArrayList<>();
        for (ContractTempleteResult contractTempleteResult : contractTempleteResults) {

            LabelResult labelResult = new LabelResult();
            labelResult.setName(contractTempleteResult.getName());
            labelResult.setTitle(contractTempleteResult.getName());
            labelResult.setType(contractTempleteResult.getType());

            List<LabelResult.LabelDetail> detail = new ArrayList<>();

            for (ContractTempleteDetailResult templeteDetailResult : templeteDetailResults) {
                if (contractTempleteResult.getContractTemplateId().equals(templeteDetailResult.getContractTempleteId())) {
                    LabelResult.LabelDetail labelDetail = new LabelResult.LabelDetail();
                    labelDetail.setName(templeteDetailResult.getValue());
                    labelDetail.setIsDefault(templeteDetailResult.getIsDefault());
                    detail.add(labelDetail);
                }
            }

            labelResult.setDetail(detail);
            results.add(labelResult);
        }

        return ResponseData.success(results);
    }


    private boolean judge(String s) {
        for (String label : ContractLabel.labels) {
            if (s.equals(label)) {
                return false;
            }
        }
        return true;
    }
}


