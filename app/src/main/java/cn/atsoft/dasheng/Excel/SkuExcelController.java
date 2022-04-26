package cn.atsoft.dasheng.Excel;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.atsoft.dasheng.Excel.pojo.SkuExcelItem;
import cn.atsoft.dasheng.Excel.pojo.SkuExcelResult;
import cn.atsoft.dasheng.Excel.pojo.Specifications;
import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.entity.Unit;
import cn.atsoft.dasheng.app.service.UnitService;
import cn.atsoft.dasheng.base.consts.ConstantsContext;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.entity.excel.CustomerExcelItem;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.message.topic.TopicMessage;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.serial.model.params.SerialNumberParam;
import cn.atsoft.dasheng.serial.service.SerialNumberService;
import cn.atsoft.dasheng.sys.core.exception.enums.BizExceptionEnum;
import cn.atsoft.dasheng.sys.modular.system.entity.FileInfo;
import cn.atsoft.dasheng.sys.modular.system.service.FileInfoService;
import cn.atsoft.dasheng.task.entity.AsynTask;
import cn.atsoft.dasheng.task.entity.AsynTaskDetail;
import cn.atsoft.dasheng.task.service.AsynTaskDetailService;
import cn.atsoft.dasheng.task.service.AsynTaskService;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.print.DocFlavor;
import javax.sound.midi.Soundbank;
import javax.tools.Tool;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/Excel")
@Slf4j
public class SkuExcelController {

    @Autowired
    private SpuService spuService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private ItemAttributeService attributeService;
    @Autowired
    private AttributeValuesService valuesService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private SpuClassificationService classificationService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CodingRulesService codingRulesService;
    @Autowired
    private SerialNumberService serialNumberService;
    @Autowired
    private FileInfoService fileInfoService;
    @Autowired
    private AsynTaskService taskService;
    @Autowired
    private AsynTaskDetailService taskDetailService;
    @Autowired
    private SkuExcelAsync skuExcelAsync;

    protected static final Logger logger = LoggerFactory.getLogger(SkuExcelController.class);

    /**
     * 上传excel填报
     */
    @RequestMapping(value = "/importSku", method = RequestMethod.GET)
    @ResponseBody

    public ResponseData uploadExcel(@RequestParam("fileId") Long fileId) {

        FileInfo fileInfo = fileInfoService.getById(fileId);
        File excelFile = new File(fileInfo.getFilePath());


        ExcelReader excelReader = ExcelUtil.getReader(excelFile, 0);
        List<Object> readRow = excelReader.readRow(0);
        List<List<Object>> read = excelReader.read(1);
        List<SkuExcelItem> skuExcelItemList = new ArrayList<>();

        for (int i = 0; i < read.size(); i++) {
            List<Object> hang = read.get(i);
            SkuExcelItem skuExcelItem = new SkuExcelItem();
            skuExcelItem.setLine(i + 1);

            for (int j = 0; j < readRow.size() + hang.size(); j++) {
                try {
                    Object header = readRow.get(j);
                    String data = hang.get(j).toString();
                    switch (header.toString()) {
                        case "物料编码":
                            if (ToolUtil.isEmpty(data)) {
                                data = null;
                                skuExcelItem.setStandard(null);
                            } else {
                                skuExcelItem.setStandard(data);
                            }
                            break;
                        case "分类":
                            skuExcelItem.setSpuClass(data);
                            break;
                        case "产品":
                            skuExcelItem.setClassItem(data);
                            break;
                        case "型号":
                            skuExcelItem.setSkuName(data);
                            break;
                        case "单位":
                            skuExcelItem.setUnit(data);
                            break;
                        case "是否批量":
                            skuExcelItem.setIsNotBatch(data);
                            break;
                        case "规则名称":
                            skuExcelItem.setItemRule(data);
                            break;
                        case "规格":
                            skuExcelItem.setSpecifications(data);
                            break;
                        case "物料描述":
                            skuExcelItem.setDescribe(data);
                        default:
//                            List<Specifications> describe = new ArrayList<>();
//                            if (ToolUtil.isNotEmpty(header) && ToolUtil.isNotEmpty(data)) {
//                                Specifications specifications = new Specifications();
//                                specifications.setAttribute(header.toString());
//                                specifications.setValue(data);
//                                specificationsList.add(specifications);
//                                describe.add(specifications);
//                            }
                    }
                } catch (Exception e) {
                    logger.error("读取异常:" + e);
                }
            }
            skuExcelItemList.add(skuExcelItem);
        }
//---------------------------------------------以上是读取excel数据----别动！！！---------------------------------------------
        /**
         * 调用异步添加
         */
        skuExcelAsync.add(skuExcelItemList);
        //-------------------------------------------------------------------------------------------------------------

        return ResponseData.success("ok");
    }


}
