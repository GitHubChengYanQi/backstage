package cn.atsoft.dasheng.portal.remind.model.params;

import lombok.Data;

import java.util.List;

@Data
public class WxTemplateData {
    private String templateId;
    private List<DataList> dataList;

    @Data
    public static class DataList {
        private String key;
        private String value;

    }
}
