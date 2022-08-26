package cn.atsoft.dasheng.Excel.pojo;

import lombok.Data;

import java.util.List;

@Data
public class TempReplaceRule {

    public List<ReplaceRule> replaceRules;

    @Data
    public class ReplaceRule {
        private String type;
        private Integer trIndex;
        private Integer tableIndex;
        private String tableType;
    }


}
