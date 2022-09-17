package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.Excel.pojo.TempReplaceRule;
import cn.atsoft.dasheng.Word.OrderReplace;
import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.entity.Template;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.app.service.TemplateService;
import cn.atsoft.dasheng.appBase.service.MediaService;
import cn.atsoft.dasheng.appBase.service.WxCpService;
import cn.atsoft.dasheng.base.consts.ConstantsContext;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.CodingRules;
import cn.atsoft.dasheng.erp.entity.InstockLogDetail;
import cn.atsoft.dasheng.erp.entity.InstockOrder;
import cn.atsoft.dasheng.erp.entity.InstockReceipt;
import cn.atsoft.dasheng.erp.mapper.InstockReceiptMapper;
import cn.atsoft.dasheng.erp.model.params.InstockOrderParam;
import cn.atsoft.dasheng.erp.model.params.InstockReceiptParam;
import cn.atsoft.dasheng.erp.model.result.InstockLogDetailResult;
import cn.atsoft.dasheng.erp.model.result.InstockReceiptResult;
import cn.atsoft.dasheng.erp.pojo.ReplaceSku;
import cn.atsoft.dasheng.erp.service.CodingRulesService;
import cn.atsoft.dasheng.erp.service.InstockLogDetailService;
import cn.atsoft.dasheng.erp.service.InstockOrderService;
import cn.atsoft.dasheng.erp.service.InstockReceiptService;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.form.service.StepsService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.modular.system.entity.FileInfo;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.FileInfoService;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.xwpf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * <p>
 * 入库记录单 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-08-11
 */
@Service
public class InstockReceiptServiceImpl extends ServiceImpl<InstockReceiptMapper, InstockReceipt> implements InstockReceiptService {

    @Autowired
    private CodingRulesService codingRulesService;
    @Autowired
    private InstockLogDetailService instockLogDetailService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService userService;
    @Autowired
    private StepsService stepsService;
    @Autowired
    private InstockOrderService instockOrderService;
    @Autowired
    private ActivitiProcessTaskService taskService;
    @Autowired
    private FileInfoService fileInfoService;
    @Autowired
    private OrderReplace orderReplace;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private WxCpService wxCpService;
    @Autowired
    private MediaService mediaService;
    @Autowired
    private OrderUpload orderUpload;

    protected static final Logger logger = LoggerFactory.getLogger(InstockReceiptServiceImpl.class);

    @Override

    public void add(InstockReceiptParam param) {
        InstockReceipt entity = getEntity(param);
        this.save(entity);
    }

    /**
     * 入库单
     *
     * @param param
     * @param instockLogDetails
     */
    @Override
    public void addReceipt(InstockOrderParam param, List<InstockLogDetail> instockLogDetails) {

        InstockReceipt entity = new InstockReceipt();
        CodingRules codingRules = codingRulesService.query().eq("module", "1").eq("state", 1).one();
        if (ToolUtil.isNotEmpty(codingRules)) {
            String coding = codingRulesService.backCoding(codingRules.getCodingRulesId());
            entity.setCoding(coding);
        }
        entity.setInstockOrderId(param.getInstockOrderId());
        this.save(entity);

        for (InstockLogDetail instockLogDetail : instockLogDetails) {
            instockLogDetail.setReceiptId(entity.getReceiptId());
        }

    }


    @Override
    public XWPFDocument createWord(String module, Map<String, Object> map, Map<String, List<ReplaceSku>> skuMap) {


        /**
         * 取出模板  和替换规则
         */
        Template template = templateService.query().eq("module", module).eq("display", 1).one();
        if (ToolUtil.isEmpty(template)) {
            throw new ServiceException(500, "没有当前单据推送模板");
        }
        FileInfo fileInfo = fileInfoService.getById(template.getFileId());
        TempReplaceRule replaceRules = JSON.parseObject(template.getReplaceRule(), TempReplaceRule.class);

        try {
            InputStream inputStream = new FileInputStream(fileInfo.getFilePath());
            XWPFDocument document = new XWPFDocument(inputStream);

            replaceInPara(document, map);   //段落替换

            replaceTable(document, replaceRules, skuMap, map);  //表格替换

            String uploadPath = ConstantsContext.getFileUploadPath();  //读取系统文件路径位置
            uploadPath = uploadPath.replace("\\", "");
            String filePath = uploadPath + map.get("path");
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            document.write(bao);
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            fileOutputStream.write(bao.toByteArray());
            File file = new File(filePath);
            orderUpload.upload(file);
            return document;
        } catch (Exception e) {
            logger.info("单据推送报错日志:" + e);
            //异常处理
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public Map<String, Object> detailBackMap(Long id) {
        Map<String, Object> map = new HashMap<>();
        InstockReceipt instockReceipt = this.getById(id);

        User user = userService.getById(instockReceipt.getCreateUser());
        InstockOrder instockOrder = instockOrderService.getById(instockReceipt.getInstockOrderId());
        User inStockUser = userService.getById(instockOrder.getCreateUser());
        ActivitiProcessTask task = taskService.getById(instockOrder.getTaskId());

        map.put("关联任务", task.getTaskName() + "/" + instockOrder.getCoding());
        map.put("申请人", inStockUser.getName());
        map.put("申请时间", new DateTime(instockOrder.getCreateTime()));
        map.put("执行人", user.getName());
        map.put("执行时间", new DateTime(instockReceipt.getCreateTime()));
        map.put("path", instockReceipt.getCoding() + ".docx");
        map.put("单号", instockReceipt.getCoding());

        return map;
    }


    @Override
    public Map<String, List<ReplaceSku>> detailBackSkuMap(Long id) {
        Map<String, List<ReplaceSku>> skuMap = new HashMap<>();


        List<InstockLogDetail> instockLogDetails = instockLogDetailService.query().eq("receipt_id", id).eq("display", 1).list();
        List<InstockLogDetailResult> detailResults = BeanUtil.copyToList(instockLogDetails, InstockLogDetailResult.class);
        instockLogDetailService.format(detailResults);

        Map<Long, List<InstockLogDetailResult>> map = new HashMap<>();


        for (InstockLogDetailResult detailResult : detailResults) {
            List<InstockLogDetailResult> results = map.get(detailResult.getCustomerId());
            if (ToolUtil.isEmpty(results)) {
                results = new ArrayList<>();
            }
            results.add(detailResult);
            map.put(detailResult.getCustomerId(), results);
        }

        for (Long customerId : map.keySet()) {
            List<InstockLogDetailResult> results = map.get(customerId);
            Customer customer = customerService.getById(customerId);

            List<ReplaceSku> replaceSkus = new ArrayList<>();
            for (InstockLogDetailResult result : results) {
                ReplaceSku replaceSku = new ReplaceSku();
                replaceSku.setSkuName(result.getSkuResult().getSkuName());
                replaceSku.setStandard(result.getSkuResult().getStandard());
                replaceSku.setSpuName(result.getSkuResult().getSpuResult().getName());
                if (ToolUtil.isNotEmpty(result.getBrandResult())) {
                    replaceSku.setBrandName(result.getBrandResult().getBrandName());
                } else {
                    replaceSku.setBrandName("");
                }
                replaceSku.setNum(result.getNumber() + "");
                replaceSku.setUnit(result.getSkuResult().getSpuResult().getUnitResult().getUnitName());
                replaceSkus.add(replaceSku);
            }
            skuMap.put(customer.getCustomerName(), replaceSkus);
        }


        return skuMap;
    }


    public void replaceTable(XWPFDocument document, TempReplaceRule replaceRules, Map<String, List<ReplaceSku>> skuMap, Map<String, Object> map) {
        for (int i = 0; i < document.getTables().size(); i++) {

            TempReplaceRule.ReplaceRule tableRule = OrderReplace.getTableRule(i, replaceRules);   //表格规则
            if (ToolUtil.isNotEmpty(tableRule) && ToolUtil.isNotEmpty(tableRule.getTableType())) {       //循环插入规则则
                XWPFTable xwpfTable = document.getTableArray(i);     //需要替换表格的位置

                switch (tableRule.getTableType()) {
                    case "sku":
                        List<XWPFTable> xwpfTables = new ArrayList<>();
                        for (String customer : skuMap.keySet()) {
                            XWPFTable newTable = orderReplace.replaceInTable(document, xwpfTable);//表格循环插入
                            List<ReplaceSku> results = skuMap.get(customer);
                            replace(document, newTable, customer, results, tableRule, replaceRules.getReplaceRules());
                            xwpfTables.add(newTable);
                        }
                        int pos = document.getPosOfTable(xwpfTable);  //删除模板中需替换的表格
                        document.removeBodyElement(pos);
                        int tablePos = pos - 1;
                        for (XWPFTable table : xwpfTables) {          //插入替换完的表格
                            document.insertTable(tablePos, table);
                            tablePos++;
                        }
                        break;
                    case "none":
                        replace(xwpfTable, map);
                        break;

                }

            }
        }
    }

    public void replaceInPara(XWPFDocument doc, Map<String, Object> params) {
        Iterator<XWPFParagraph> iterator = doc.getParagraphsIterator();
        while (iterator.hasNext()) {
            replaceInPara(iterator.next(), params);
        }
    }

    /**
     * 段落替换
     *
     * @param para
     * @param params
     */
    private void replaceInPara(XWPFParagraph para, Map<String, Object> params) {
        for (String key : params.keySet()) {
            params.putIfAbsent(key, "");
        }

        List<XWPFRun> runs;
        StringBuilder runText = new StringBuilder();

        if (matcher(para.getParagraphText()).find()) {
            runs = para.getRuns();
            int j = runs.size();
            for (int i = 0; i < j; i++) {

                if (ToolUtil.isNotEmpty(runs.get(0).getText(0))) {
                    runText.append(runs.get(0).getText(0));
                }
                //保留最后一个段落，在这段落中替换值，保留原有段落样式
                if (!((j - 1) == i)) {
                    para.removeRun(0);
                }
            }
            String text = runText.toString();
            Matcher matcher;
            while ((matcher = matcher(text)).find()) {
                String group = matcher.group(1);
                if (group.equals("${sku}") || group.equals("${pay}")) {
                    text = matcher.replaceFirst("");
                } else {
                    text = matcher.replaceFirst(String.valueOf(params.get(group)));
                }
            }
            runs.get(0).setText(text, 0);
        }
    }


    private void replace(XWPFTable xwpfTable, Map<String, Object> map) {

        for (int i = 0; i < xwpfTable.getRows().size(); i++) {

            XWPFTableRow row = xwpfTable.getRow(i);
            noneCopy(row, map);
        }
    }


    private void replace(XWPFDocument document, XWPFTable xwpfTable, String customer, List<ReplaceSku> results, TempReplaceRule.ReplaceRule tableRule, List<TempReplaceRule.ReplaceRule> replaceRules) {

        for (int i = 0; i < xwpfTable.getRows().size(); i++) {   //表格里循环 行

            String rowRule = OrderReplace.getRowRule(i, tableRule, replaceRules);   //行替换规则
            XWPFTableRow row = xwpfTable.getRow(i);         //获取当期行
            switch (rowRule) {
                case "none":
                    easyCopy(row, customer);
                    break;
                case "sku":
                    boolean copy = loopCopy(xwpfTable, row, results);
                    if (copy) {
                        document.createParagraph();   //表格之间 插入回车
                        return;
                    }
                    break;
            }
        }
    }

    private void easyCopy(XWPFTableRow sourceRow, String content) {
        List<XWPFTableCell> cellList = sourceRow.getTableCells();
        if (ToolUtil.isEmpty(cellList)) {
            return;
        }
        for (XWPFTableCell xwpfTableCell : cellList) {
            Matcher matcher = matcher(xwpfTableCell.getText());
            while (matcher.find()) {
                String group = matcher.group(0);
                switch (group) {
                    case "${供应商}":
                        xwpfTableCell.removeParagraph(0);
                        xwpfTableCell.setText(content);
                        break;
                }
            }
        }
    }

    /**
     * 表格普通替换
     *
     * @param sourceRow
     * @param map
     */
    private void noneCopy(XWPFTableRow sourceRow, Map<String, Object> map) {
        List<XWPFTableCell> cellList = sourceRow.getTableCells();
        if (ToolUtil.isEmpty(cellList)) {
            return;
        }
        for (XWPFTableCell xwpfTableCell : cellList) {
            Matcher matcher = matcher(xwpfTableCell.getText());
            while (matcher.find()) {
                String group = matcher.group(1);
                Object o = map.get(group);
                if (ToolUtil.isNotEmpty(o)) {
                    xwpfTableCell.removeParagraph(0);
                    xwpfTableCell.setText(String.valueOf(map.get(group)));
                }
            }
        }
    }


    public boolean loopCopy(XWPFTable table, XWPFTableRow sourceRow, List<ReplaceSku> results) {
        List<XWPFTableCell> cellList = sourceRow.getTableCells();
        if (null == cellList) {
            return true;
        }
        for (XWPFTableCell sourceCell : cellList) {
            Matcher matcher = matcher(sourceCell.getText());
            while (matcher.find()) {
                String group = matcher.group(0);
                switch (group) {
                    case "${物料名称}":   //TODO  取标的物替换规则
                    case "${产品名称}":
                    case "${型号规格}":
                    case "${品牌厂家}":
                    case "${数量}":
                    case "${序号}":
                        return insertNewRow(table, sourceRow, results);
                }
            }
        }
        return false;
    }

    /**
     * 拷贝当前行 新建
     *
     * @param table
     * @param xwpfTableRow
     * @param results
     */
    private boolean insertNewRow(XWPFTable table, XWPFTableRow xwpfTableRow, List<ReplaceSku> results) {
        int size;

        int size1 = results.size();//  需要插入行的长度


        for (int i1 = 0; i1 < size1; i1++) {
            size = table.getRows().size();   //当前表格最后一行的位置
            List<XWPFTableCell> cellList = xwpfTableRow.getTableCells();
            if (null == cellList) {
                return true;
            }
            ReplaceSku replaceSku = results.get(i1);
            XWPFTableRow newRow = table.insertNewTableRow(size);
            newRow.getCtRow().setTrPr(xwpfTableRow.getCtRow().getTrPr());

            for (XWPFTableCell xwpfTableCell : cellList) {    //新插入列

                //复制列及其属性和内容
                XWPFTableCell targetCell = newRow.addNewTableCell();
                //列属性
                targetCell.getCTTc().setTcPr(xwpfTableCell.getCTTc().getTcPr());
                //段落属性
                if (xwpfTableCell.getParagraphs() != null && xwpfTableCell.getParagraphs().size() > 0) {
                    targetCell.getParagraphs().get(0).getCTP().setPPr(xwpfTableCell.getParagraphs().get(0).getCTP().getPPr());
                    if (xwpfTableCell.getParagraphs().get(0).getRuns() != null && xwpfTableCell.getParagraphs().get(0).getRuns().size() > 0) {
                        XWPFRun cellR = targetCell.getParagraphs().get(0).createRun();
                        cellR.setText(xwpfTableCell.getText());
                        cellR.setBold(xwpfTableCell.getParagraphs().get(0).getRuns().get(0).isBold());
                    } else {
                        targetCell.setText(xwpfTableCell.getText());
                    }
                } else {
                    targetCell.setText(xwpfTableCell.getText());
                }
                replace(xwpfTableCell, replaceSku, i1);  //替换
            }

            xwpfTableRow = newRow;
        }
        table.removeRow(table.getRows().size() - 1);
        return true;
    }

    /**
     * 替换
     *
     * @param xwpfTableCell
     * @param
     * @param i
     */
    private void replace(XWPFTableCell xwpfTableCell, ReplaceSku replaceSku, int i) {
        Matcher matcher = matcher(xwpfTableCell.getText());
        while (matcher.find()) {      //每一列替换
            String group = matcher.group(0);
            switch (group) {
                case "${物料名称}":
                    xwpfTableCell.removeParagraph(0);
                    xwpfTableCell.setText(replaceSku.getStandard());
                    break;
                case "${产品名称}":
                    xwpfTableCell.removeParagraph(0);
                    xwpfTableCell.setText(replaceSku.getSpuName());
                    break;
                case "${型号规格}":
                    xwpfTableCell.removeParagraph(0);
                    xwpfTableCell.setText(replaceSku.getSkuName());
                    break;
                case "${品牌厂家}":
                    xwpfTableCell.removeParagraph(0);
                    xwpfTableCell.setText(replaceSku.getBrandName());
                    break;
                case "${数量}":
                    xwpfTableCell.removeParagraph(0);
                    xwpfTableCell.setText(replaceSku.getNum() + "");
                    break;
                case "${序号}":
                    xwpfTableCell.removeParagraph(0);
                    xwpfTableCell.setText(i + 1 + "");
                    break;
                case "${单位}":
                    xwpfTableCell.removeParagraph(0);
                    xwpfTableCell.setText(replaceSku.getUnit());
                    break;
            }
        }
    }


    /**
     * 正则匹配字符串
     *
     * @param str
     * @return
     */
    public static Matcher matcher(String str) {
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher;
    }


    @Override
    public InstockReceiptResult detail(Long receiptId) {
        InstockReceipt instockReceipt = this.getById(receiptId);
        InstockReceiptResult result = new InstockReceiptResult();
        ToolUtil.copyProperties(instockReceipt, result);

        List<InstockLogDetail> instockLogDetails = instockLogDetailService.query().eq("receipt_id", result.getReceiptId()).eq("display", 1).list();
        List<InstockLogDetailResult> detailResults = BeanUtil.copyToList(instockLogDetails, InstockLogDetailResult.class);
        instockLogDetailService.format(detailResults);
        Map<Long, List<InstockLogDetailResult>> map = new HashMap<>();
        Map<String, List<InstockLogDetailResult>> customerMap = new HashMap<>();

        for (InstockLogDetailResult detailResult : detailResults) {
            List<InstockLogDetailResult> results = map.get(detailResult.getCustomerId());
            if (ToolUtil.isEmpty(results)) {
                results = new ArrayList<>();
            }
            results.add(detailResult);
            map.put(detailResult.getCustomerId(), results);
        }

        for (Long customerId : map.keySet()) {
            List<InstockLogDetailResult> results = map.get(customerId);
            Customer customer = customerService.getById(customerId);
            customerMap.put(customer.getCustomerName(), results);
        }

        if (ToolUtil.isNotEmpty(result.getCreateUser())) {
            User user = userService.getById(result.getCreateUser());
            result.setUser(user);
        }

        if (ToolUtil.isNotEmpty(result.getInstockOrderId())) {
            String source;
            InstockOrder instockOrder = instockOrderService.getById(result.getInstockOrderId());
            ActivitiProcessTask task = taskService.getByFormId(instockOrder.getInstockOrderId());
            source = task.getTaskName() + "/" + instockOrder.getCoding();
            result.setSource(source);
        }


        result.setCustomerMap(customerMap);
        return result;
    }


    @Override
    public void delete(InstockReceiptParam param) {
        this.removeById(getKey(param));
    }


    @Override
    public void update(InstockReceiptParam param) {
        InstockReceipt oldEntity = getOldEntity(param);
        InstockReceipt newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public InstockReceiptResult findBySpec(InstockReceiptParam param) {
        return null;
    }

    @Override
    public List<InstockReceiptResult> findListBySpec(InstockReceiptParam param) {
        return null;
    }

    @Override
    public PageInfo<InstockReceiptResult> findPageBySpec(InstockReceiptParam param) {
        Page<InstockReceiptResult> pageContext = getPageContext();
        IPage<InstockReceiptResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(InstockReceiptParam param) {
        return param.getReceiptId();
    }

    private Page<InstockReceiptResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private InstockReceipt getOldEntity(InstockReceiptParam param) {
        return this.getById(getKey(param));
    }

    private InstockReceipt getEntity(InstockReceiptParam param) {
        InstockReceipt entity = new InstockReceipt();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
