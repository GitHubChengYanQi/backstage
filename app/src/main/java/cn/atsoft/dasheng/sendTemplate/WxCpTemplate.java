package cn.atsoft.dasheng.sendTemplate;

import lombok.Data;

import java.util.List;

@Data
public class WxCpTemplate {
    private String url;
    private String description;
    private String title;
    private List<Long> userIds;
}
