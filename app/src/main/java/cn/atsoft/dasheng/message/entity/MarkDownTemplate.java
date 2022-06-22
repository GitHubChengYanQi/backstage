package cn.atsoft.dasheng.message.entity;

import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Data
public class MarkDownTemplate {
    private String title;
    private String items;
    private String remark;
    private String description;
    private String createUserName;
    private Long createUser;
    private String url;
    private Integer type;
    private List<Long> userIds;
    private String source;
    private Long sourceId;

    @Autowired
    UserService userService;

    public String getContent() {
        if (ToolUtil.isNotEmpty(createUser)) {
            User user = userService.getById(createUser);
            createUserName = user.getName();
        }
        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("您有新的").append(title).append("任务").append("\n \n");
        stringBuffer.append(">**").append("任务详情").append("**").append("\n \n");
        if (ToolUtil.isNotEmpty(items)) {
            stringBuffer.append(">**事　项**：").append("<font color=\"info\">").append(items).append("</font>").append("\n \n ");
        }
        if (ToolUtil.isNotEmpty(createUserName)) {
            stringBuffer.append(">**发起人**：").append(createUserName).append("\n \n");
            stringBuffer.append(">").append("\n > \n");
        }
        if (ToolUtil.isNotEmpty(description)) {
            stringBuffer.append(">**详　情**：").append("<font color=\"warning\">").append(description).append("</font>").append("\n\n");
        }
        if (ToolUtil.isNotEmpty(remark)) {
            stringBuffer.append(">**备　注**：").append("<font color=\"comment\">").append(remark).append("</font>").append("\n\n");
        }
        if (ToolUtil.isNotEmpty(url)) {
            stringBuffer.append(">").append("\n\n");
            stringBuffer.append(">[点击查看详情](").append(url).append(")");
        }
        return stringBuffer.toString();
    }

    void selectTitle() {
        ToolUtil.isNotEmpty(type);
        {
            switch (type) {
                case 1:
                    setTitle("待办");
                    break;
                case 2:
                    break;
                case 3:
                    break;
            }
        }
    }
}
