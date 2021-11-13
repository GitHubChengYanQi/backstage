package cn.atsoft.dasheng.crm.controller;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;

import cn.atsoft.dasheng.Tool.VoUtilsTool;
import cn.atsoft.dasheng.app.entity.Adress;
import cn.atsoft.dasheng.app.entity.Contacts;
import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.service.AdressService;
import cn.atsoft.dasheng.app.service.ContactsService;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.base.consts.ConstantsContext;
import cn.atsoft.dasheng.base.pojo.page.LayuiPageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.entity.excel.AdressExcelItem;
import cn.atsoft.dasheng.crm.entity.excel.ContactsExcelItem;
import cn.atsoft.dasheng.crm.entity.excel.CustomerExcelItem;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.orCode.entity.OrCode;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.orCode.service.OrCodeService;
import cn.atsoft.dasheng.sys.core.exception.enums.BizExceptionEnum;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * excel导入导出示例
 */
@Controller
@RequestMapping("/api")
@Slf4j
public class CrmExcelController {

    @Autowired
    private UserService userService;
    @Autowired
    private AdressService adressService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ContactsService contactsService;

    @Autowired
    private OrCodeBindService orCodeBindService;
    @Autowired
    private OrCodeService orCodeService;

    /**
     * 上传excel填报
     */
    @RequestMapping("/importCustomer")
    @ResponseBody
    public ResponseData uploadExcel(@RequestParam("file") MultipartFile file) {
        String name = file.getOriginalFilename();
        String fileSavePath = ConstantsContext.getFileUploadPath();
        try {
            File excelFile = new File(fileSavePath + name);
            file.transferTo(excelFile);
            try {
                ImportParams params = new ImportParams();
                params.setTitleRows(1);
                params.setHeadRows(1);
                List<CustomerExcelItem> result = ExcelImportUtil.importExcel(excelFile, CustomerExcelItem.class, params);
                List<Customer> costomerList = new ArrayList<>();
                for (CustomerExcelItem customerExcelItem : result) {
                    Customer customer = new Customer();
                    ToolUtil.copyProperties(costomerList, customer);
                    costomerList.add(customer);
                }
                customerService.saveBatch(costomerList);
                return ResponseData.success();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            log.error("上传那文件出错！", e);
            throw new ServiceException(BizExceptionEnum.UPLOAD_ERROR);
        }
        return null;
    }

    @RequestMapping("/importAdress")
    @ResponseBody
    public ResponseData adressExcel(@RequestParam("file") MultipartFile file) {
        String name = file.getOriginalFilename();
        String fileSavePath = ConstantsContext.getFileUploadPath();
        try {
            File excelFile = new File(fileSavePath + name);
            file.transferTo(excelFile);
            try {
                ImportParams params = new ImportParams();
                params.setTitleRows(1);
                params.setHeadRows(1);
                List<AdressExcelItem> result = ExcelImportUtil.importExcel(excelFile, AdressExcelItem.class, params);

                List<Adress> adresses = new ArrayList<>();
                for (AdressExcelItem adressExcelItem : result) {
                    boolean isNull = VoUtilsTool.checkObjFieldIsNull(adressExcelItem);
                    if (isNull) {
                        throw new ServiceException(500, "导入失败");
                    }

                    Adress adress = new Adress();
                    Customer customer = customerService.lambdaQuery().eq(Customer::getCustomerName, adressExcelItem.getCustomerName()).one();
                    if (ToolUtil.isNotEmpty(customer)) {
                        adressExcelItem.setCustomerId(customer.getCustomerId());
                    } else {
                        throw new ServiceException(500, "没有此客户");
                    }
                    ToolUtil.copyProperties(adressExcelItem, adress);
                    adresses.add(adress);
                }
                adressService.saveBatch(adresses);
                return ResponseData.success("导入成功");
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            log.error("上传那文件出错！", e);
            throw new ServiceException(BizExceptionEnum.UPLOAD_ERROR);
        }
        return null;
    }

    @RequestMapping("/importContacts")
    @ResponseBody
    public ResponseData contactsExcel(@RequestParam("file") MultipartFile file) {
        String name = file.getOriginalFilename();
        String fileSavePath = ConstantsContext.getFileUploadPath();
        try {
            File excelFile = new File(fileSavePath + name);
            file.transferTo(excelFile);
            try {
                ImportParams params = new ImportParams();
                params.setTitleRows(1);
                params.setHeadRows(1);
                List<ContactsExcelItem> result = ExcelImportUtil.importExcel(excelFile, ContactsExcelItem.class, params);

                List<Contacts> contactsList = new ArrayList<>();
                for (ContactsExcelItem contactsExcelItem : result) {
                    Contacts contacts = new Contacts();
                    ToolUtil.copyProperties(contactsExcelItem, contacts);
                    contactsList.add(contacts);
                }
                contactsService.saveBatch(contactsList);
                return ResponseData.success();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            log.error("上传那文件出错！", e);
            throw new ServiceException(BizExceptionEnum.UPLOAD_ERROR);
        }
        return null;
    }


    @RequestMapping(value = "/qrCodetoExcel", method = RequestMethod.GET)
    @ApiOperation("导出")
    public void qrCodetoExcel(HttpServletResponse response, Long type, String url) throws IOException {
        String title = "二维码导出表单";
        String[] header = {"序号", "Id", "地址","请扫描"};


        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("二维码导出");
        sheet.setDefaultColumnWidth(40);
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, 3);
        sheet.addMergedRegion(region);
//        sheet.setColumnWidth(0, 10);
        HSSFRow titleRow = sheet.createRow(0);
        HSSFCell ti = titleRow.createCell(0);
        ti.setCellValue(title);
        HSSFCellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        ti.setCellStyle(titleStyle);



        HSSFRow headrow = sheet.createRow(1);

        for (int i = 0; i < header.length; i++) {
            //创建一个单元格
            HSSFCell cell = headrow.createCell(i);

            //创建一个内容对象
            HSSFRichTextString text = new HSSFRichTextString(header[i]);


            HSSFCellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            //将内容对象的文字内容写入到单元格中
            cell.setCellValue(text);
            cell.setCellStyle(headerStyle);
        }


        List<Map<String, String>> list = new ArrayList<>();
        //所有二维码
        if (type == 0) {
            List<OrCode> orCodes = orCodeService.query().list();
            for (OrCode orCode : orCodes) {
                Map<String, String> codeMap = new HashMap<>();
                Long orCodeId = orCode.getOrCodeId();
                String replace = url.replace("codeId", orCode.getOrCodeId().toString());
                codeMap.put("id", orCodeId.toString());
                codeMap.put("url", replace);
                list.add(codeMap);
            }
        }
        //未绑定的二维码
        if ((type == 1)) {
            List<OrCodeBind> codeBinds = orCodeBindService.query().list();
            List<Long> ids = new ArrayList<>();
            for (OrCodeBind codeBind : codeBinds) {
                ids.add(codeBind.getOrCodeId());
            }
            List<OrCode> codes = orCodeService.query().notIn("qr_code_id", ids).list();
            for (OrCode code : codes) {
                Map<String, String> codeMap = new HashMap<>();
                Long orCodeId = code.getOrCodeId();
                String replace = url.replace("codeId", orCodeId.toString());
                codeMap.put("id", orCodeId.toString());
                codeMap.put("url", replace);
                list.add(codeMap);
            }
        }
        //绑定的二维码
        if (type == 2) {
            List<OrCodeBind> binds = orCodeBindService.list();
            for (OrCodeBind bind : binds) {
                Map<String, String> codeMap = new HashMap<>();
                Long orCodeId = bind.getOrCodeId();
                String replace = url.replace("codeId", orCodeId.toString());
                codeMap.put("id", orCodeId.toString());
                codeMap.put("url", replace);
                list.add(codeMap);
            }
        }

        int i = 2;
        for (Map<String, String> longStringMap : list) {

            HSSFRow row1 = sheet.createRow(i);
            HSSFCell id = row1.createCell(1);
            HSSFCell url1 = row1.createCell(2);
            HSSFRichTextString idValue = new HSSFRichTextString(longStringMap.get("id"));
            HSSFRichTextString urlValue = new HSSFRichTextString(longStringMap.get("url"));
            id.setCellValue(idValue);
            url1.setCellValue(urlValue);
            i++;

        }


//        int i =1;
//        for (Long orCode : codeIds) {
//            if (ToolUtil.isEmpty(orCode)) {
//                HSSFRow row1 = sheet.createRow(i);
//                HSSFCell id = row1.createCell(1);
//                HSSFCell url = row1.createCell(2);
//                HSSFRichTextString text = new HSSFRichTextString(orCode.getOrCodeId().toString());
//                id.setCellValue(text);
//                url.setCellValue("www.baidu.com");
//                i++;
//            }
//        }

        //准备将Excel的输出流通过response输出到页面下载
        //八进制输出流
        response.setContentType("application/octet-stream");

        //这后面可以设置导出Excel的名称
        response.setHeader("Content-disposition", "attachment;filename=qrCode.xls");

        //刷新缓冲
        response.flushBuffer();

        //workbook将Excel写入到response的输出流中，供页面下载
        workbook.write(response.getOutputStream());
//        System.out.println(workbook.write(response.getOutputStream()));
    }
}

