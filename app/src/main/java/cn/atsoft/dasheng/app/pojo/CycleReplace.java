package cn.atsoft.dasheng.app.pojo;

import lombok.Data;

import java.util.List;

@Data
public class CycleReplace {

    public List<Cycle> cycles;

    @Data
    public static class Cycle {
        public String oldText;
        public String newText;
    }
}
