package cn.atsoft.dasheng.Excel.pojo;

import lombok.Data;

import java.util.List;


@Data
public class LabelResult {
    public String name;
    public String title;
    public String type;
    public List<LabelDetail> detail;
    public String value;
    private Integer isHidden;

    @Data
    public static class LabelDetail {
        private Integer isDefault;
        public String name;
    }
}
