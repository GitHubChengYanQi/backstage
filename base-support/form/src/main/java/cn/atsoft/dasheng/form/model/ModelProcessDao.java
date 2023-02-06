package cn.atsoft.dasheng.form.model;

import lombok.Data;

import java.util.List;

@Data
public class ModelProcessDao {
    private String model;
    private String modelName;
    List<ProcessStepType> stepTypes;

    @Data
    public static class ProcessStepType{
        private String type;
        private String typeName;

    }
}
