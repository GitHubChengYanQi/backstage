package cn.atsoft.dasheng.form.model;

import lombok.Data;

import java.util.List;

@Data
public class ModelServiceDao {
    //业务模块
    private String model;
    //业务模块中文名称
    private String modelName;
    //业务模块子分类
    private List<ModelType> details;
    @Data
    public static class ModelType{
        //名称
        private String name;
        //类型
        private String typ;
    }
}
