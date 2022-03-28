package cn.atsoft.dasheng.Excel;


import cn.afterturn.easypoi.word.WordExportUtil;
import cn.atsoft.dasheng.app.entity.Contract;
import cn.atsoft.dasheng.app.service.ContractService;
import cn.atsoft.dasheng.model.response.ResponseData;

import io.lettuce.core.dynamic.annotation.Param;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import java.io.*;


@Controller
@RequestMapping("/Excel")
@Slf4j
public class ContractExcel {

    @Autowired
    private ContractService contractService;


    @RequestMapping(value = "/exportContract", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData exportContract(@Param("id") Long id) throws Exception {
        Contract contract = contractService.getById(id);


        String noteName = "d:/";
        String reportDirName = "合同.doc";
        htmlToWord(reportDirName, noteName, contract.getContent());
        return ResponseData.success();
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

}
