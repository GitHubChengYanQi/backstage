package cn.atsoft.dasheng.Word;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.*;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class OrderWordUtils {
    /**
     * 对word进行参数替换并生成新的文件
     *
     * @param inputPath word模板路径和名称
     * @param outPath   word模板路径和名称
     * @param params    待填充的数据 params.put("key",value) 文档中对应为 ${key}
     */
    public static void readwriteWord(String inputPath, String outPath, Map<String, Object> params) throws IOException, InvalidFormatException {

        InputStream is = new FileInputStream(inputPath);
        XWPFDocument document = new XWPFDocument(OPCPackage.open(is));
        replaceParams(document, params);
        OutputStream os = new FileOutputStream(outPath);
        document.write(os);
        close(os);
        close(is);
    }

    /**
     * 替换word中的参数与动态新增表格行数
     *
     * @param in         模板输入流
     * @param os         修改后的输出流
     * @param params     待填充的数据 params.put("key",value) 文档中对应为 ${key}
     * @param tableIndex 表格下标，从0开始，如Word中3个表格，仅对1、3个表格进行修改，参数为 int[]{0,2}
     * @param tables     新增表格的数据，数据格式为 [表[行[单元格]]]
     * @throws InvalidFormatException
     * @throws IOException
     */
    public static void readwriteWord(InputStream in, OutputStream os, Map<String, Object> params, int[] tableIndex, List<List<String[]>> tables) throws IOException, InvalidFormatException {
        XWPFDocument document = new XWPFDocument(OPCPackage.open(in));
        replaceParams(document, params);
        if (tableIndex.length != tables.size()) throw new RemoteException("表格下标数量与表格参数数量不一致！");
        for (int i = 0; i < tableIndex.length; i++) {
            changeTable(document, params, i, tables.get(i));
        }
        document.write(os);
    }

    /**
     * 实现对word读取和修改操作
     *
     * @param in     模板输入流
     * @param out    输出流
     * @param params 待填充的数据，从数据库读取
     * @throws IOException
     * @throws InvalidFormatException
     */
    public static void readwriteWord(InputStream in, OutputStream out, Map<String, Object> params) {
        try {
            XWPFDocument document;
            document = new XWPFDocument(OPCPackage.open(in));
            replaceParams(document, params);
            document.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 替换段落里面的变量
     *
     * @param doc    要替换的文档
     * @param params 参数
     */
    private static void replaceParams(XWPFDocument doc, Map<String, Object> params) {
        Iterator<XWPFParagraph> iterator = doc.getParagraphsIterator();
        XWPFParagraph paragraph;
        while (iterator.hasNext()) {
            paragraph = iterator.next();
            replaceParam(paragraph, params);
        }
    }

    /**
     * @param doc        docx解析对象
     * @param params     需要替换的信息集合
     * @param tableIndex 第几个表格
     * @param tableList  需要插入的表格信息集合
     */
    public static void changeTable(XWPFDocument doc, Map<String, Object> params, int tableIndex, List<String[]> tableList) {
        //获取表格对象集合
        List<XWPFTable> tables = doc.getTables();
        //获取第一个表格   根据实际模板情况 决定去第几个word中的表格
        XWPFTable table = tables.get(tableIndex);
        //替换表格中的参数
        replaceTableParams(doc, params);
        //在表格中插入数据
        insertTable(table, tableList);
    }

    /**
     * 替换表格里面的变量
     *
     * @param doc    要替换的文档
     * @param params 参数
     */
    private static void replaceTableParams(XWPFDocument doc, Map<String, Object> params) {
        Iterator<XWPFTable> iterator = doc.getTablesIterator();
        XWPFTable table;
        List<XWPFTableRow> rows;
        List<XWPFTableCell> cells;
        List<XWPFParagraph> paras;
        while (iterator.hasNext()) {
            table = iterator.next();
            //判断表格是需要替换还是需要插入，判断逻辑有$为替换，表格无$为插入
            if (matcher(table.getText()).find()) {
                rows = table.getRows();
                for (XWPFTableRow row : rows) {
                    cells = row.getTableCells();
                    for (XWPFTableCell cell : cells) {
                        paras = cell.getParagraphs();
                        for (XWPFParagraph para : paras) {
                            replaceParam(para, params);
                        }
                    }
                }
            }

        }
    }

    /**
     * 为表格插入行数，此处不处理表头，所以从第二行开始
     *
     * @param table     需要插入数据的表格
     * @param tableList 插入数据集合
     */
    private static void insertTable(XWPFTable table, List<String[]> tableList) {
        //创建与数据一致的行数
        for (int i = 0; i < tableList.size(); i++) {
            table.createRow();
        }
        int length = table.getRows().size();
        for (int i = 1; i < length; i++) {
            XWPFTableRow newRow = table.getRow(i);
            List<XWPFTableCell> cells = newRow.getTableCells();
            for (int j = 0; j < cells.size(); j++) {
                XWPFTableCell cell = cells.get(j);
                cell.setText(tableList.get(i - 1)[j]);
            }
        }
    }

    /**
     * 替换段落里面的变量
     *
     * @param paragraph 要替换的段落
     * @param params    参数
     */
    private static void replaceParam(XWPFParagraph paragraph, Map<String, Object> params) {
        List<XWPFRun> runs;
        Matcher matcher;
        String runText = "";

        if (matcher(paragraph.getParagraphText()).find()) {
            runs = paragraph.getRuns();
            int j = runs.size();
            for (int i = 0; i < j; i++) {
                runText += runs.get(0).toString();
                //保留最后一个段落，在这段落中替换值，保留段落样式
                if (!((j - 1) == i)) {
                    paragraph.removeRun(0);
                }
            }
            matcher = matcher(runText);
            if (matcher.find()) {
                while ((matcher = matcher(runText)).find()) {
                    runText = matcher.replaceFirst(String.valueOf(params.get(matcher.group(1))));
                }
                runs.get(0).setText(runText, 0);
            }
        }

    }

    /**
     * 正则匹配字符串
     *
     * @param str
     * @return
     */
    private static Matcher matcher(String str) {
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher;
    }

    /**
     * 关闭输入流
     *
     * @param is
     */
    private static void close(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭输出流
     *
     * @param os
     */
    private static void close(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


