package cn.atsoft.dasheng.crm.controller;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;

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
import cn.atsoft.dasheng.sys.core.exception.enums.BizExceptionEnum;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

import java.util.ArrayList;
import java.util.List;


/**
 * excel导入导出示例
 */
@Controller
@RequestMapping("/crm/excel")
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
                    Adress adress = new Adress();
                    Customer customer = customerService.lambdaQuery().eq(Customer::getCustomerName, adressExcelItem.getCustomerName()).one();
                    if (ToolUtil.isNotEmpty(customer)) {
                        adress.setCustomerId(customer.getCustomerId());
                    }
                    ToolUtil.copyProperties(adressExcelItem, adress);
                    adresses.add(adress);
                }
                adressService.saveBatch(adresses);
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
                    Adress adress = new Adress();
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
}

