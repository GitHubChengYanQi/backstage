package cn.atsoft.dasheng.uc.model.params;

import lombok.Data;

import java.util.List;

@Data
public class AppleParam {

    private List<key> keys;

    @Data
    public static class key {

        private String n;

        private String e;

        private String kid;
    }
}
