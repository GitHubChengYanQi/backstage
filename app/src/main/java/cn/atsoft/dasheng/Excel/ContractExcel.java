package cn.atsoft.dasheng.Excel;


import cn.afterturn.easypoi.exception.word.enmus.WordExportEnum;
import cn.afterturn.easypoi.word.WordExportUtil;
import cn.atsoft.dasheng.app.entity.Contract;
import cn.atsoft.dasheng.app.entity.Template;
import cn.atsoft.dasheng.app.service.ContractService;
import cn.atsoft.dasheng.app.service.TemplateService;
import cn.atsoft.dasheng.base.consts.ConstantsContext;
import cn.atsoft.dasheng.base.oshi.model.SysFileInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.sys.modular.system.entity.FileInfo;
import cn.atsoft.dasheng.sys.modular.system.service.FileInfoService;
import cn.hutool.core.lang.Assert;
import cn.hutool.poi.word.DocUtil;
import cn.hutool.poi.word.Word07Writer;
import io.lettuce.core.dynamic.annotation.Param;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/Excel")
@Slf4j
public class ContractExcel {

    @Autowired
    private ContractService contractService;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private FileInfoService fileInfoService;


    @RequestMapping(value = "/exportContract", method = RequestMethod.GET)
    public void test(HttpServletRequest request, HttpServletResponse response, Long id) {
        try {
            Contract contract = contractService.getById(id);
            //word内容
            byte b[] = contract.getContent().getBytes(StandardCharsets.UTF_8);  //这里是必须要设置编码的，不然导出中文就会乱码。
            ByteArrayInputStream bais = new ByteArrayInputStream(b);//将字节数组包装到流中
            /*
             * 关键地方
             * 生成word格式 */
            POIFSFileSystem poifs = new POIFSFileSystem();
            DirectoryEntry directory = poifs.getRoot();
            DocumentEntry documentEntry = directory.createDocument("文档名称", bais);
            //输出文件
            request.setCharacterEncoding("utf-8");
            response.setContentType("application/contractWord");//导出word格式
            response.setHeader("Content-disposition", "attachment;filename=contract.doc");

            OutputStream ostream = response.getOutputStream();
            poifs.writeFilesystem(ostream);
            bais.close();
            ostream.close();
        } catch (Exception e) {
            //异常处理
        }

    }

    @RequestMapping(value = "/importWord", method = RequestMethod.GET)
    public void importWord(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {


        String name = file.getOriginalFilename();
        String fileSavePath = ConstantsContext.getFileUploadPath();
        File excelFile = new File(fileSavePath + name);

        try {
            file.transferTo(excelFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        WordUtils wordUtils = new WordUtils();
        XWPFDocument document = DocUtil.create(new File(fileSavePath + name));

        Map<String, Object> params = new HashMap<>();
        params.put("合同编号", "让子弹飞");
        params.put("header1", "序号");
        params.put("header2", "演员");
        params.put("header3", "角色");
        params.put("header4", "备注");
        params.put("header5", "介绍");


        wordUtils.replaceInPara(document, params);

        // 替换表格中的参数
        wordUtils.replaceInTable(document, new int[]{0}, params);

        String fileName = "test.docx";
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        OutputStream os = response.getOutputStream();
        document.write(os);

    }

    @RequestMapping(value = "/getWordLables", method = RequestMethod.GET)
    public ResponseData getWordLable(@RequestParam("id") Long id) {
        List<String> list = new ArrayList<>();

        Template template = templateService.getById(id);

        if (ToolUtil.isNotEmpty(template) && ToolUtil.isNotEmpty(template.getFileId())) {
            FileInfo fileInfo = fileInfoService.getById(template.getFileId());
            WordUtils wordUtils = new WordUtils();
            XWPFDocument document = DocUtil.create(new File(fileInfo.getFilePath()));
            list = wordUtils.getLabel(document);
        }


        return ResponseData.success(list);
    }
}
