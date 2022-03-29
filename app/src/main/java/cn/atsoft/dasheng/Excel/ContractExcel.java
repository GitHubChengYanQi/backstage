package cn.atsoft.dasheng.Excel;


import cn.afterturn.easypoi.exception.word.enmus.WordExportEnum;
import cn.afterturn.easypoi.word.WordExportUtil;
import cn.atsoft.dasheng.app.entity.Contract;
import cn.atsoft.dasheng.app.service.ContractService;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.lang.Assert;
import cn.hutool.poi.word.Word07Writer;
import io.lettuce.core.dynamic.annotation.Param;
import lombok.extern.slf4j.Slf4j;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


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

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public void export(HttpServletRequest request, HttpServletResponse response, Long id) {
        Contract contract = contractService.getById(id);

        Map<String, Object> params = new HashMap<>();
        params.put("title", "这是标题");
        params.put("name", "李四");
        //这里是我说的一行代码
        exportWord(contract.getContent(), "F:/test", "aaa.docx", params, request, response);
    }


    /**
     * 导出word
     * <p>第一步生成替换后的word文件，只支持docx</p>
     * <p>第二步下载生成的文件</p>
     * <p>第三步删除生成的临时文件</p>
     * 模版变量中变量格式：{{foo}}
     *
     * @param templatePath word模板地址
     * @param temDir       生成临时文件存放地址
     * @param fileName     文件名
     * @param params       替换的参数
     * @param request      HttpServletRequest
     * @param response     HttpServletResponse
     */

    public static void exportWord(String templatePath, String temDir, String fileName, Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) {
        Assert.notNull(templatePath, "模板路径不能为空");
        Assert.notNull(temDir, "临时文件路径不能为空");
        Assert.notNull(fileName, "导出文件名不能为空");
        Assert.isTrue(fileName.endsWith(".docx"), "word导出请使用docx格式");
        if (!temDir.endsWith("/")) {
            temDir = temDir + File.separator;
        }
        File dir = new File(temDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            String userAgent = request.getHeader("user-agent").toLowerCase();
            if (userAgent.contains("msie") || userAgent.contains("like gecko")) {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } else {
                fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            }
            XWPFDocument doc = WordExportUtil.exportWord07(templatePath, params);
            String tmpPath = temDir + fileName;
            FileOutputStream fos = new FileOutputStream(tmpPath);
            doc.write(fos);
            // 设置强制下载不打开
            response.setContentType("application/force-download");
            // 设置文件名
            response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
            OutputStream out = response.getOutputStream();
            doc.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            delAllFile(temDir);//这一步看具体需求，要不要删
        }

    }
}
