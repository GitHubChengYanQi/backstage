package cn.atsoft.dasheng.Excel.service.impl;

import cn.atsoft.dasheng.Excel.service.IExcelImportService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IExcelImportServiceImpl<T> implements IExcelImportService<T> {

    List<T> data = new ArrayList<>();

    Map<String,String> columList = new HashMap<String,String>();

    @Override
    public void importExcel(ExcelReader excelReader,Class<T> clazz) {

        Map<String, String> columList = this.columList;
        //设置标题行的别名Map



        for (Map<String, Object> stringObjectMap : excelReader.readAll()) {
            try{
                T t = clazz.newInstance();
                Field[] fields = t.getClass().getDeclaredFields();
                for (Field field : fields) {
                    for (String columNameKey : columList.keySet()) {
                        if (field.getName().equals(columList.get(columNameKey))) {
                            field.setAccessible(true);
                            field.set(t,convert(stringObjectMap.get(columNameKey),field.getType()));
                        }
                    }
                }
                data.add(t);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
    /**
     * Field类型转换
     */
    private static <T> T convert(Object obj, Class<T> type) {
        if (obj != null && StrUtil.isNotBlank(obj.toString())) {
            if (type.equals(String.class)) {
                return (T) obj.toString();
            } else if (type.equals(BigDecimal.class)) {
                return (T) new BigDecimal(obj.toString());
            }
            //其他类型转换......
        }
        return null;
    }

    @Override
    public List<T> getData() {
        return data;
    }

    @Override
    public void columList(Map columList) {
        this.columList = columList;
    }
}
