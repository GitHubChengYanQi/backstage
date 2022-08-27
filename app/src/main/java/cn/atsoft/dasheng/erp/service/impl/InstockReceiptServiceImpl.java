package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.Excel.pojo.TempReplaceRule;
import cn.atsoft.dasheng.Word.OrderReplace;
import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.entity.Template;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.app.service.TemplateService;
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
import cn.atsoft.dasheng.erp.service.CodingRulesService;
import cn.atsoft.dasheng.erp.service.InstockLogDetailService;
import cn.atsoft.dasheng.erp.service.InstockOrderService;
import cn.atsoft.dasheng.erp.service.InstockReceiptService;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.form.service.StepsService;
import cn.atsoft.dasheng.sys.modular.system.entity.FileInfo;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.FileInfoService;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public XWPFDocument createWord(Long receiptId, Long templateId) {
        InstockReceiptResult detail = detail(receiptId);
        /**
         * 取出模板  和替换规则
         */
        Template template = templateService.getById(templateId);
        FileInfo fileInfo = fileInfoService.getById(template.getFileId());
        List<TempReplaceRule.ReplaceRule> replaceRules = JSON.parseArray(template.getReplaceRule(), TempReplaceRule.ReplaceRule.class);

        try {
            InputStream inputStream = new FileInputStream(fileInfo.getFilePath());
            XWPFDocument document = new XWPFDocument(inputStream);

            for (int i = 0; i < document.getTables().size(); i++) {


                TempReplaceRule.ReplaceRule tableRule = OrderReplace.getTableRule(i, replaceRules);   //表格规则

                if (ToolUtil.isNotEmpty(tableRule) && tableRule.getTableType().equals("sku")) {        //循环插入规则则

                    XWPFTable xwpfTable = document.getTableArray(i);     //需要替换表格的位置
                    Map<String, List<InstockLogDetailResult>> customerMap = detail.getCustomerMap();
                    List<XWPFTable> xwpfTables = new ArrayList<>();


                    for (String customer : customerMap.keySet()) {
                        XWPFTable newTable = orderReplace.replaceInTable(document, xwpfTable);//表格循环插入
                        List<InstockLogDetailResult> results = detail.getCustomerMap().get(customer);
                        replace(document, newTable, customer, results, tableRule, replaceRules);
                        xwpfTables.add(newTable);
                    }


                    int pos = document.getPosOfTable(xwpfTable);  //删除模板中需替换的表格
                    document.removeBodyElement(pos);

                    int tablePos = pos;
                    for (XWPFTable table : xwpfTables) {          //插入替换完的表格
                        document.insertTable(tablePos, table);
                        tablePos++;
                    }
                }
            }


            return document;

        } catch (Exception e) {
            //异常处理
            e.printStackTrace();
        }

        return null;
    }


    private void replace(XWPFDocument document, XWPFTable xwpfTable, String customer, List<InstockLogDetailResult> results, TempReplaceRule.ReplaceRule tableRule, List<TempReplaceRule.ReplaceRule> replaceRules) {

        for (int i = 0; i < xwpfTable.getRows().size(); i++) {   //表格里循环 行

            String rowRule = OrderReplace.getRowRule(i, tableRule, replaceRules);   //行替换规则
            XWPFTableRow row = xwpfTable.getRow(i);         //获取当期行
            if (rowRule.equals("none")) {

            }

            boolean copy = copy(xwpfTable, row, customer, results);
            if (copy) {
                document.createParagraph();   //表格之间 插入回车
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


    public boolean copy(XWPFTable table, XWPFTableRow sourceRow, String customer, List<InstockLogDetailResult> results) {
        List<XWPFTableCell> cellList = sourceRow.getTableCells();
        if (null == cellList) {
            return true;
        }
        for (XWPFTableCell sourceCell : cellList) {
            Matcher matcher = matcher(sourceCell.getText());
            while (matcher.find()) {
                String group = matcher.group(0);
                switch (group) {
                    case "${供应商}":
                        sourceCell.removeParagraph(0);
                        sourceCell.setText(customer);
                        break;
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
    private boolean insertNewRow(XWPFTable table, XWPFTableRow xwpfTableRow, List<InstockLogDetailResult> results) {
        int size;

        int size1 = results.size();//  需要插入行的长度


        for (int i1 = 0; i1 < size1; i1++) {
            size = table.getRows().size();   //当前表格最后一行的位置
            List<XWPFTableCell> cellList = xwpfTableRow.getTableCells();
            if (null == cellList) {
                return true;
            }
            InstockLogDetailResult instockLogDetailResult = results.get(i1);
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
                replace(xwpfTableCell, instockLogDetailResult, i1);  //替换
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
     * @param instockLogDetailResult
     * @param i
     */
    private void replace(XWPFTableCell xwpfTableCell, InstockLogDetailResult instockLogDetailResult, int i) {
        Matcher matcher = matcher(xwpfTableCell.getText());
        while (matcher.find()) {      //每一列替换
            String group = matcher.group(0);
            switch (group) {
                case "${物料名称}":
                    xwpfTableCell.removeParagraph(0);
                    xwpfTableCell.setText(instockLogDetailResult.getSkuResult().getStandard());
                    break;
                case "${产品名称}":
                    xwpfTableCell.removeParagraph(0);
                    xwpfTableCell.setText(instockLogDetailResult.getSkuResult().getSpuResult().getName());
                    break;
                case "${型号规格}":
                    xwpfTableCell.removeParagraph(0);
                    xwpfTableCell.setText(instockLogDetailResult.getSkuResult().getSpecifications());
                    break;
                case "${品牌厂家}":
                    xwpfTableCell.removeParagraph(0);
                    if (ToolUtil.isEmpty(instockLogDetailResult.getBrandResult()) || ToolUtil.isEmpty(instockLogDetailResult.getBrandResult().getBrandName())) {
                        xwpfTableCell.setText("");
                    } else {
                        xwpfTableCell.setText(instockLogDetailResult.getBrandResult().getBrandName());
                    }
                    break;
                case "${数量}":
                    xwpfTableCell.removeParagraph(0);
                    xwpfTableCell.setText(instockLogDetailResult.getNumber() + "");
                    break;
                case "${序号}":
                    xwpfTableCell.removeParagraph(0);
                    xwpfTableCell.setText(i + 1 + "");
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
