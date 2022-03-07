package cn.atsoft.dasheng.Excel;

import cn.atsoft.dasheng.base.consts.ConstantsContext;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.lang.Console;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/Excel")
@Slf4j
public class BomController {
    @RequestMapping("/importBom")
    @ResponseBody
    public ResponseData uploadExcel(@RequestParam("file") MultipartFile file) {

        String name = file.getOriginalFilename();
        String fileSavePath = ConstantsContext.getFileUploadPath();
        File excelFile = new File(fileSavePath + name);
        try {
            file.transferTo(excelFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //默认获取第一页
        ExcelReader reader = ExcelUtil.getReader(excelFile);
        List<List<Object>> readAll = reader.read(Integer.MAX_VALUE);
        for (List<Object> list : readAll) {
            
        }

        

        return ResponseData.success();
    }


}
