package cn.atsoft.dasheng.entity;

import cn.atsoft.dasheng.enmu.MarkDownTemplateTypeEnum;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MarkDownTemplate {
    private String title;
    private String items;
    private String description;
    private MarkDownTemplateTypeEnum function;
    private Date createTime;
    private Long userId;
    private String url;
    private Integer type;
    private List<Long> userIds;
    private String source;
    private String coding;
    //任务id
    private Long taskId;
    private Long createUser;
    private Long sourceId;
}
