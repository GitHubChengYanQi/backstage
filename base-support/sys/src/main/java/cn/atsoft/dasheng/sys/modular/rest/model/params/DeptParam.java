package cn.atsoft.dasheng.sys.modular.rest.model.params;

import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DeptParam implements Serializable, BaseValidatingParam {

    private Long deptId;

    private String condition;


    List<Sort> sortList;

    @Data
    public static class Sort{
        private Long deptId;
        private Integer sort;
    }
}
