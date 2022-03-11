package cn.atsoft.dasheng.app.pojo;

import lombok.Data;

import java.util.List;

@Data

public class PayReplace {

    public List<PayCycle> cycles;

    @Data
    public static class PayCycle {
        public String oldText;
        public String newText;
    }
}
