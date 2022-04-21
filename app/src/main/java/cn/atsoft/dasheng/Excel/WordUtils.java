package cn.atsoft.dasheng.Excel;

import cn.atsoft.dasheng.Excel.pojo.TempReplaceRule;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.model.result.OrderDetailResult;
import cn.atsoft.dasheng.crm.model.result.PaymentDetailResult;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTDLbls;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import javax.print.Doc;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.function.DoubleConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author fax
 * @date 2021-08-23 11:08
 */
public class WordUtils {


    /**
     * 设置表格位置
     *
     * @param xwpfTable
     * @param location  整个表格居中center,left居左，right居右，both两端对齐
     */


    public static void setTableLocation(XWPFTable xwpfTable, String location) {
        CTTbl cttbl = xwpfTable.getCTTbl();
        CTTblPr tblpr = cttbl.getTblPr() == null ? cttbl.addNewTblPr() : cttbl.getTblPr();
        CTJc cTJc = tblpr.addNewJc();
        cTJc.setVal(STJc.Enum.forString(location));
    }

    /**
     * 设置单元格水平位置和垂直位置
     *
     * @param xwpfTable
     * @param verticalLoction    单元格中内容垂直上TOP，下BOTTOM，居中CENTER，BOTH两端对齐
     * @param horizontalLocation 单元格中内容水平居中center,left居左，right居右，both两端对齐
     */
    public static void setCellLocation(XWPFTable xwpfTable, String verticalLoction, String horizontalLocation) {
        List<XWPFTableRow> rows = xwpfTable.getRows();
        for (XWPFTableRow row : rows) {
            List<XWPFTableCell> cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                CTTc cttc = cell.getCTTc();
                CTP ctp = cttc.getPList().get(0);
                CTPPr ctppr = ctp.getPPr();
                if (ctppr == null) {
                    ctppr = ctp.addNewPPr();
                }
                CTJc ctjc = ctppr.getJc();
                if (ctjc == null) {
                    ctjc = ctppr.addNewJc();
                }
                ctjc.setVal(STJc.Enum.forString(horizontalLocation)); //水平居中
                cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.valueOf(verticalLoction));//垂直居中
            }
        }
    }

    /**
     * 替换文档中的内容
     *
     * @param doc    Word的文档
     * @param params 待填充的数据
     *               params.put("key",value) 文档中对应为 ${key}
     */
    public void replaceInPara(XWPFDocument doc, Map<String, Object> params) {
        Iterator<XWPFParagraph> iterator = doc.getParagraphsIterator();
        while (iterator.hasNext()) {
            replaceInPara(iterator.next(), params);
        }
    }


    /**
     * 获取 word 中需要替换的标签
     *
     * @param doc
     */
    public List<String> getLabel(XWPFDocument doc) {
        List<String> list = new ArrayList<>();

        Iterator<XWPFParagraph> iterator = doc.getParagraphsIterator();
        while (iterator.hasNext()) {
            list.addAll(labelInPara(iterator.next()));
        }
        return list;
    }


    private List<String> labelInPara(XWPFParagraph para) {
        List<XWPFRun> runs;

        List<String> list = new ArrayList<>();

        if (matcher(para.getParagraphText()).find()) {
            runs = para.getRuns();
            int j = runs.size();
            StringBuilder runText;
            for (int i = 0; i < j; i++) {

                if (ToolUtil.isNotEmpty(runs.get(i).getText(0))) {
                    runText = new StringBuilder((runs.get(i).getText(0)));
                } else {
                    runText = new StringBuilder("");
                }
                String text = runText.toString();

                Matcher matcher;
                while ((matcher = matcher(text)).find()) {
                    list.add(matcher.group(1));
                    text = matcher.replaceFirst("");
                }


            }

        }
        return list;
    }

    /**
     * 遍历所有表格，替换表格里面的变量
     *
     * @param doc    要替换的文档
     * @param params 参数
     */
    public void replaceInAllTable(XWPFDocument doc, Map<String, Object> params) {
        Iterator<XWPFTable> iterator = doc.getTablesIterator();
        while (iterator.hasNext()) {
            replaceInTable(iterator.next(), params);
        }
    }

    public List<String> getLabelInTable(XWPFDocument doc, int[] tabIndex) {
        List<String> list = new ArrayList<>();
        List<XWPFTable> tables = doc.getTables();
        if (tables.size() == 0) {
            return list;
        }
        for (int index : tabIndex) {
            list.addAll(labelInTable(tables.get(index)));
        }
        return list;
    }


    public List<String> labelInTable(XWPFTable table) {
        List<XWPFTableRow> rows;
        List<XWPFTableCell> cells;
        List<XWPFParagraph> paras;
        List<String> list = new ArrayList<>();
        //判断表格中是否有 ${} 有就表示需要替换值
        if (matcher(table.getText()).find()) {
            rows = table.getRows();
            for (XWPFTableRow row : rows) {
                cells = row.getTableCells();
                for (XWPFTableCell cell : cells) {
                    paras = cell.getParagraphs();
                    for (XWPFParagraph para : paras) {
                        list.addAll(labelInPara(para));
                    }
                }
            }
        }
        return list;
    }


    /**
     * 替换指定表格中的变量
     *
     * @param doc
     * @param tabIndex 表格下标
     * @param params   替换参数
     */
    public void replaceInTable(XWPFDocument doc, Map<String, Object> params, List<TempReplaceRule.ReplaceRule> replaceRules) {


        List<XWPFTable> tables = doc.getTables();

        for (int i = 0; i < tables.size(); i++) {
            XWPFTable xwpfTable = tables.get(i);   //表

            List<XWPFTableRow> rows = xwpfTable.getRows();

            for (int j = 0; j < rows.size(); j++) {    //行
                XWPFTableRow tableRow = rows.get(j);


//                xwpfTable.addRow()

//                for (XWPFTableCell tableCell : tableRow.getTableCells()) {
//
//                }

                String rule = getRule(i, j, replaceRules);
                if (ToolUtil.isNotEmpty(rule)) {
                    switch (rule) {
                        case "sku":
                            List<OrderDetailResult> results = (List<OrderDetailResult>) params.get(rule);
                            int i1 = 1;
                            for (OrderDetailResult result : results) {

                                XWPFTableRow xwpfTableRow = xwpfTable.insertNewTableRow(j + i1);  //新行

                                Map<String, Object> orderFormat = ContractExcel.orderFormat(result);
                                List<XWPFTableCell> cells = tableRow.getTableCells();
                                for (XWPFTableCell cell : cells) {          //段落

                                    XWPFTableCell xwpfTableCell = xwpfTableRow.addNewTableCell();
                                    //列属性
                                    xwpfTableCell.getCTTc().setTcPr(cell.getCTTc().getTcPr());
                                    //段落属性
                                    if (cell.getParagraphs() != null && cell.getParagraphs().size() > 0) {
                                        xwpfTableCell.getParagraphs().get(0).getCTP().setPPr(cell.getParagraphs().get(0).getCTP().getPPr());
                                        if (cell.getParagraphs().get(0).getRuns() != null && cell.getParagraphs().get(0).getRuns().size() > 0) {
                                            XWPFRun cellR = xwpfTableCell.getParagraphs().get(0).createRun();
                                            cellR.setText(cell.getText());
                                            cellR.setBold(cell.getParagraphs().get(0).getRuns().get(0).isBold());
                                        } else {
                                            xwpfTableCell.setText(cell.getText());
                                        }
                                    } else {
                                        xwpfTableCell.setText(cell.getText());
                                    }
                                    List<XWPFParagraph> paras = xwpfTableCell.getParagraphs();
                                    for (XWPFParagraph para : paras) {
                                        replaceInPara(para, orderFormat);
                                    }
                                }

                                i1++;
                            }
                            xwpfTable.removeRow(j);
                            break;
                        case "pay":
                            List<PaymentDetailResult> payList = (List<PaymentDetailResult>) params.get(rule);
                            int i2 = 1;
                            for (PaymentDetailResult paymentDetailResult : payList) {
                                XWPFTableRow xwpfTableRow = xwpfTable.insertNewTableRow(j + i2);  //新行

                                Map<String, Object> orderFormat = ContractExcel.payFormat(paymentDetailResult);

                                List<XWPFTableCell> cells = tableRow.getTableCells();
                                for (XWPFTableCell cell : cells) {          //段落

                                    XWPFTableCell xwpfTableCell = xwpfTableRow.addNewTableCell();
                                    //列属性
                                    xwpfTableCell.getCTTc().setTcPr(cell.getCTTc().getTcPr());
                                    //段落属性
                                    if (cell.getParagraphs() != null && cell.getParagraphs().size() > 0) {
                                        xwpfTableCell.getParagraphs().get(0).getCTP().setPPr(cell.getParagraphs().get(0).getCTP().getPPr());
                                        if (cell.getParagraphs().get(0).getRuns() != null && cell.getParagraphs().get(0).getRuns().size() > 0) {
                                            XWPFRun cellR = xwpfTableCell.getParagraphs().get(0).createRun();
                                            cellR.setText(cell.getText());
                                            cellR.setBold(cell.getParagraphs().get(0).getRuns().get(0).isBold());
                                        } else {
                                            xwpfTableCell.setText(cell.getText());
                                        }
                                    } else {
                                        xwpfTableCell.setText(cell.getText());
                                    }

                                    List<XWPFParagraph> paras = xwpfTableCell.getParagraphs();
                                    for (XWPFParagraph para : paras) {
                                        replaceInPara(para, orderFormat);
                                    }
                                }

                                i2++;
                            }
                            break;
                    }
                }

            }
            replaceInTable(tables.get(i), params);

        }

//        for (int index : tabIndex) {
//            replaceInTable(tables.get(index), params);
//        }
    }

    public String getRule(int table, int tr, List<TempReplaceRule.ReplaceRule> replaceRules) {
        for (TempReplaceRule.ReplaceRule replaceRule : replaceRules) {
            if (replaceRule.getTableIndex().equals(table) && replaceRule.getTrIndex().equals(tr)) {
                return replaceRule.getType();
            }
        }
        return null;
    }

    /**
     * 获取word 中所有表格
     *
     * @param doc
     * @return
     */
    public List<XWPFTable> getTables(XWPFDocument doc) {
        List<XWPFTable> tables = doc.getTables();
        return tables;
    }


    /**
     * 替换表格中的变量
     *
     * @param table
     * @param params
     */
    public void replaceInTable(XWPFTable table, Map<String, Object> params) {
        List<XWPFTableRow> rows;
        List<XWPFTableCell> cells;
        List<XWPFParagraph> paras;
        //判断表格中是否有 ${} 有就表示需要替换值
        if (matcher(table.getText()).find()) {
            rows = table.getRows();
            for (XWPFTableRow row : rows) {
                cells = row.getTableCells();
                for (XWPFTableCell cell : cells) {
                    paras = cell.getParagraphs();
                    for (XWPFParagraph para : paras) {
                        replaceInPara(para, params);
                    }
                }
            }
        }
    }


    /**
     * 在表格中新增行数并填充数据
     *
     * @param table    需要插入数据的表格
     * @param rowDatas 插入数据集合（注：填充的数据要与单元格的数量保持一致）
     */
    public void insertTableRow(XWPFTable table, List<String[]> rowDatas) {
        for (String[] cellDatas : rowDatas) {
            XWPFTableRow row = table.createRow();
            List<XWPFTableCell> cells = row.getTableCells();
            for (int j = 0; j < cells.size(); j++) {
                cells.get(j).setText(cellDatas[j]);
            }
        }
    }

    private Boolean getSkuInPara(XWPFParagraph para, Map<String, Object> params) {
        List<XWPFRun> runs;
        StringBuilder runText;
        if (matcher(para.getParagraphText()).find()) {
            runs = para.getRuns();
            int j = runs.size();
            for (int i = 0; i < j; i++) {
                if (ToolUtil.isNotEmpty(runs.get(i).getText(0))) {
                    runText = new StringBuilder((runs.get(i).getText(0)));
                } else {
                    runText = new StringBuilder("");
                }
                String text = runText.toString();
                Matcher matcher;

                while ((matcher = matcher(text)).find()) {
                    for (String s : params.keySet()) {
                        if (s.equals(matcher.group(1))) {
                            return true;
                        }
                    }
                    text = matcher.replaceFirst("");
                }

            }
        }
        return false;
    }

    /**
     * 替换段落里面的变量
     *
     * @param para   要替换的段落
     * @param params 参数
     */
    private void replaceInPara(XWPFParagraph para, Map<String, Object> params) {
        for (String key : params.keySet()) {
            params.putIfAbsent(key, "");
        }

        List<XWPFRun> runs;
        StringBuilder runText = new StringBuilder();

        if (matcher(para.getParagraphText()).find()) {
            runs = para.getRuns();
            int j = runs.size();
            for (int i = 0; i < j; i++) {

                if (ToolUtil.isNotEmpty(runs.get(0).getText(0))) {
//                    runText = new StringBuilder((runs.get(i).getText(0)));
                    runText.append(runs.get(0).getText(0));
                }
//                else {
//                    runText = new StringBuilder("");
//                }


                //保留最后一个段落，在这段落中替换值，保留原有段落样式
                if (!((j - 1) == i)) {
                    para.removeRun(0);
                }
            }
            String text = runText.toString();
            Matcher matcher;
            while ((matcher = matcher(text)).find()) {
                String group = matcher.group(1);
                if (group.equals("${sku}") || group.equals("${pay}")) {
                    text = matcher.replaceFirst("");
                } else {
                    text = matcher.replaceFirst(String.valueOf(params.get(group)));
                }
            }
            runs.get(0).setText(text, 0);

        }
    }

    /**
     * 创建标题
     *
     * @param paragraph
     * @param data
     */
    public void createTitle(XWPFParagraph paragraph, String data) {
        XWPFRun createRun = paragraph.insertNewRun(0);
        createRun.setText(data);
        createRun.setFontFamily("宋体");
        createRun.setFontSize(14);
        createRun.setBold(true);
        paragraph.setSpacingAfter(10);
        paragraph.setSpacingBefore(10);
        //对齐方式
        paragraph.setAlignment(ParagraphAlignment.LEFT);
    }

    /**
     * 创建段落
     *
     * @param paragraph
     * @param data
     */
    public void createParagraph(XWPFParagraph paragraph, String data) {
        XWPFRun createRun = paragraph.createRun();
        createRun.setText(data);
        createRun.setFontFamily("宋体");
        createRun.setFontSize(12);

        paragraph.setFirstLineIndent(20);
        paragraph.setAlignment(ParagraphAlignment.BOTH);
        paragraph.setIndentationFirstLine(600);
        paragraph.setSpacingAfter(10);
        paragraph.setSpacingBefore(10);
    }

    /**
     * 创建表格
     *
     * @param document
     * @param tableList
     */
    public void createTable(XWPFDocument document, List<String[]> tableList) {
        XWPFTable table = document.createTable(tableList.size(), tableList.get(0).length);
//        XWPFTable table = new XWPFTable(document.getBody().addNewTbl(), this, rows, cols);
        //设置表格的宽度
        CTTblWidth comTableWidth = table.getCTTbl().addNewTblPr().addNewTblW();
        comTableWidth.setType(STTblWidth.PCT);
        comTableWidth.setW(BigInteger.valueOf(5000));

        // 填充数据
        int length = tableList.size();
        for (int i = 0; i < length; i++) {
            XWPFTableRow row = table.getRow(i);
            List<XWPFTableCell> cells = row.getTableCells();
            for (int j = 0; j < cells.size(); j++) {
                cells.get(j).setText(tableList.get(i)[j]);
            }
        }

    }

    public void insertTable(int pos, XWPFDocument document) {
        XWPFTable xwpfTable = document.createTable();

        List<String[]> table2 = new ArrayList<>();
        table2.add(new String[]{"1", "姜文", "张牧之", "xxx"});
        table2.add(new String[]{"2", "周润发", "黄四郎", "xxx"});
        table2.add(new String[]{"3", "葛优", "马邦德", "xxx"});

        createTable(document, table2);
        document.insertTable(pos, xwpfTable);
    }


    /**
     * 饼图
     *
     * @param document
     * @param title      图的标题
     * @param valueTitle 图种类名称
     * @param values     图种类的值
     */
    public void createPieChart(XWPFDocument document, String title, String[] valueTitle, Double[] values) throws IOException, InvalidFormatException {

        XWPFChart chart = document.createChart(15 * Units.EMU_PER_CENTIMETER, 10 * Units.EMU_PER_CENTIMETER);
        // 标题
        chart.setTitleText(title);
        // 标题是否覆盖图表
        chart.setTitleOverlay(false);

        // 图例位置
        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.TOP_RIGHT);

        XDDFCategoryDataSource categorys = XDDFDataSourcesFactory.fromArray(valueTitle);

        XDDFNumericalDataSource<Double> numerical = XDDFDataSourcesFactory.fromArray(values);

        XDDFChartData data = chart.createData(ChartTypes.PIE, null, null);
        // 设置为可变颜色
        data.setVaryColors(true);
        // 图表加载数据
        data.addSeries(categorys, numerical);
        // 绘制
        chart.plot(data);

        CTDLbls dLbls = chart.getCTChart().getPlotArea().getPieChartArray(0).getSerArray(0).addNewDLbls();
        dLbls.addNewShowVal().setVal(false);//不显示值
        dLbls.addNewShowLegendKey().setVal(false);
        dLbls.addNewShowCatName().setVal(true);//类别名称
        dLbls.addNewShowSerName().setVal(false);//不显示系列名称
        dLbls.addNewShowPercent().setVal(true);//显示百分比
        dLbls.addNewShowLeaderLines().setVal(true); //显示引导线

    }

    /**
     * 柱状图
     *
     * @param document
     * @param title      标题
     * @param xAxisTitle X轴标题
     * @param yAxisTitle Y轴标题
     * @param categorys  种类
     * @param values     值
     * @throws IOException
     * @throws InvalidFormatException
     */
    public void createBarChart(XWPFDocument document, String title, String xAxisTitle, String yAxisTitle, String[] categorys, Map<String, Number[]> values) throws IOException, InvalidFormatException {

        XWPFChart chart = document.createChart(15 * Units.EMU_PER_CENTIMETER, 10 * Units.EMU_PER_CENTIMETER);
        // 标题
        chart.setTitleText(title);
        // 标题覆盖
        chart.setTitleOverlay(false);
        // 图例位置
        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.TOP);
        legend.setOverlay(true);

        //X轴属性
        XDDFCategoryAxis xAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        xAxis.setTitle(xAxisTitle);

        // Y轴属性
        XDDFValueAxis yAxis = chart.createValueAxis(AxisPosition.LEFT);
        yAxis.setTitle(yAxisTitle);
        yAxis.setCrosses(AxisCrosses.AUTO_ZERO);
        yAxis.setCrossBetween(AxisCrossBetween.BETWEEN);

        XDDFChartData data = chart.createData(ChartTypes.BAR, xAxis, yAxis);
        data.setVaryColors(true);
        ((XDDFBarChartData) data).setBarDirection(BarDirection.COL); // 设置方向为竖状

        XDDFDataSource<String> categoriesData = XDDFDataSourcesFactory.fromArray(categorys);
        values.forEach((k, v) -> {
            XDDFChartData.Series series = data.addSeries(categoriesData, XDDFDataSourcesFactory.fromArray(v));
            series.setTitle(k, null);
        });
        chart.plot(data);


    }


    /**
     * 柱状图
     *
     * @param document
     * @param title      标题
     * @param xAxisTitle X轴标题
     * @param yAxisTitle Y轴标题
     * @param categorys  种类
     * @param values     值
     * @throws IOException
     * @throws InvalidFormatException
     */
    public void createLineChart(XWPFDocument document, String title, String xAxisTitle, String yAxisTitle, String[] categorys, Map<String, Number[]> values) throws IOException, InvalidFormatException {
        XWPFChart chart = document.createChart(15 * Units.EMU_PER_CENTIMETER, 10 * Units.EMU_PER_CENTIMETER);

        // 标题
        chart.setTitleText(title);
        // 标题覆盖
        chart.setTitleOverlay(false);
        // 图例位置
        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.TOP);
        legend.setOverlay(true);

        //X轴属性
        XDDFCategoryAxis xAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        xAxis.setTitle(xAxisTitle);

        //Y轴属性
        XDDFValueAxis yAxis = chart.createValueAxis(AxisPosition.LEFT);
        yAxis.setTitle(yAxisTitle);

        // 折线图，
        XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, xAxis, yAxis);

        XDDFCategoryDataSource countries = XDDFDataSourcesFactory.fromArray(categorys);
        // 加载数据
        values.forEach((k, v) -> {
            XDDFLineChartData.Series series = (XDDFLineChartData.Series) data.addSeries(countries, XDDFDataSourcesFactory.fromArray(v));
            series.setTitle(k, null); // 折线图例标题
            series.setMarkerStyle(MarkerStyle.CIRCLE); // 设置标记样式
        });
        // 绘制
        chart.plot(data);
    }

    /**
     * 柱状图
     *
     * @param document
     * @param title      标题
     * @param xAxisTitle X轴标题
     * @param yAxisTitle Y轴标题
     * @param categorys  种类
     * @param values     值
     * @throws IOException
     * @throws InvalidFormatException
     */
    public void createBarLineChart(XWPFDocument document, String title, String xAxisTitle, String yAxisTitle, String[] categorys, Map<String, Number[]> values) throws IOException, InvalidFormatException {
        XWPFChart chart = document.createChart(15 * Units.EMU_PER_CENTIMETER, 10 * Units.EMU_PER_CENTIMETER);

        // 标题
        chart.setTitleText(title);
        // 标题覆盖
        chart.setTitleOverlay(false);
        // 图例位置
        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.TOP);
        legend.setOverlay(true);

        //X轴属性
        XDDFCategoryAxis xAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        xAxis.setTitle(xAxisTitle);

        //Y轴属性
        XDDFValueAxis yAxis = chart.createValueAxis(AxisPosition.LEFT);
        yAxis.setTitle(yAxisTitle);
        yAxis.setCrosses(AxisCrosses.AUTO_ZERO);
        yAxis.setCrossBetween(AxisCrossBetween.BETWEEN);

        // 折线图，
        XDDFLineChartData lineData = (XDDFLineChartData) chart.createData(ChartTypes.LINE, xAxis, yAxis);
        // 柱状图
        XDDFChartData barData = chart.createData(ChartTypes.BAR, xAxis, yAxis);
        barData.setVaryColors(true);
        ((XDDFBarChartData) barData).setBarDirection(BarDirection.COL); // 设置方向为竖状

        XDDFCategoryDataSource countries = XDDFDataSourcesFactory.fromArray(categorys);
        // 加载数据
        values.forEach((k, v) -> {
            XDDFChartData.Series barSeries = barData.addSeries(countries, XDDFDataSourcesFactory.fromArray(v));
            barSeries.setTitle(k, null);
            XDDFLineChartData.Series lineSeries = (XDDFLineChartData.Series) lineData.addSeries(countries, XDDFDataSourcesFactory.fromArray(v));
            lineSeries.setTitle(k, null); // 折线图例标题
            lineSeries.setMarkerStyle(MarkerStyle.CIRCLE); // 设置标记样式
        });
        // 绘制
        chart.plot(lineData);
        chart.plot(barData);
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
}

