package cn.atsoft.dasheng.production.model.result;

import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import lombok.Data;

import java.util.List;

@Data
public class PickListsStorehouseResult {
    private StorehouseResult storehouseResult;
    private Integer skuCount;
    private Integer numberCount;
    List<ClassCount> classCounts;


    @Data
    public static class ClassCount{
        private String className;
        private Integer classCount;
        private Integer numberCount;
    }
}
