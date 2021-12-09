package cn.atsoft.dasheng.form.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DeptPosition {

    private List<Positions> positions;
    private String title;
    private String key;


    /**
     * 职位
     */
    @Data
    public class Positions {
        private String label;
        private String value;
    }
}
