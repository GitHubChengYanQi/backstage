package cn.atsoft.dasheng.Excel.pojo;

import cn.hutool.poi.excel.sax.handler.RowHandler;
import lombok.Data;

import java.util.List;

@Data
public class Bom implements RowHandler {
    private List<Object> objects;


    @Override
    public void handle(int sheetIndex, long rowIndex, List<Object> rowList) {
        objects.add(rowList.get(sheetIndex));
    }
}
