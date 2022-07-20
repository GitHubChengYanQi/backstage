package cn.atsoft.dasheng.message.entity;

import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.sendTemplate.pojo.MarkDownTemplateTypeEnum;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private Long taskId;
    private Long createUser;
    private Long sourceId;
}
