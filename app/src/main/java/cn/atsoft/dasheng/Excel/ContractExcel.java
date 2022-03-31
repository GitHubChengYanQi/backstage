package cn.atsoft.dasheng.Excel;


import cn.afterturn.easypoi.word.WordExportUtil;
import cn.atsoft.dasheng.app.entity.Contract;
import cn.atsoft.dasheng.app.service.ContractService;
import cn.atsoft.dasheng.model.response.ResponseData;

import io.lettuce.core.dynamic.annotation.Param;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;


@Controller
@RequestMapping("/Excel")
@Slf4j
public class ContractExcel {

    @Autowired
    private ContractService contractService;


    @ResponseBody
    public ResponseData exportContract(HttpServletResponse response, @Param("id") Long id) throws Exception {
        Contract contract = contractService.getById(id);

        String noteName = "d:/";
        String reportDirName = "合同.doc";
        htmlToWord(reportDirName, noteName, contract.getContent());
        return ResponseData.success("保存路径：" + noteName + reportDirName);
    }


    /**
     * HTML转word
     *
     * @param noteName         导出文件名称
     * @param researchNoteInfo 文件的html
     * @return void
     * @paramre portDirName 文件路径
     * @author Solitary
     * @date 2019/1/11 9:21
     */
    public static void htmlToWord(String noteName, String reportDirName, String researchNoteInfo) throws Exception {
        //拼一个标准的HTML格式文档
        Document document = Jsoup.parse(researchNoteInfo);
        InputStream is = new ByteArrayInputStream(document.html().getBytes("GBK"));
        OutputStream os = new FileOutputStream(reportDirName + noteName);
        inputStreamToWord(is, os);
    }

    /**
     * 把is写入到对应的word输出流os中
     *
     * @param is
     * @param os
     * @throws IOException
     */
    private static void inputStreamToWord(InputStream is, OutputStream os) throws IOException {
        POIFSFileSystem fs = new POIFSFileSystem();
        DirectoryNode root = fs.getRoot();
        root.createDocument("WordDocument", is);
        fs.writeFilesystem(os);
        os.close();
        is.close();
    }

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

}
