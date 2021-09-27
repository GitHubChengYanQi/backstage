package cn.atsoft.dasheng.crm.controller;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;

import cn.atsoft.dasheng.base.consts.ConstantsContext;
import cn.atsoft.dasheng.base.pojo.page.LayuiPageInfo;
import cn.atsoft.dasheng.crm.entity.excel.CustomerExcelItem;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.sys.core.exception.enums.BizExceptionEnum;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import java.util.List;


/**
 * excel导入导出示例
 */
@Controller
@RequestMapping("/crm/excel")
@Slf4j
public class ExcelController {

    @Autowired
    private UserService userService;

    /**
     * 上传excel填报
     */
    @RequestMapping("/importCustomer")
    @ResponseBody
    public ResponseData uploadExcel(@RequestPart("file") MultipartFile file) {
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


//                return returns;
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
