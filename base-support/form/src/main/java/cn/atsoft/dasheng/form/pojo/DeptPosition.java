package cn.atsoft.dasheng.form.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public  class DeptPosition{

    private List<Position> positions;
    private String title;
    private String key;



    /**
     * 职位
     */
    @Data
    public static class Position {
        private String label;
        private String value;
    }
}
