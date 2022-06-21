package cn.atsoft.dasheng.message.entity;

import cn.atsoft.dasheng.core.util.ToolUtil;
import lombok.Data;

import java.util.List;

@Data
public class MarkDownTemplate {
    private String title;
    private String items;
    private String remark;
    private String description;
    private String createUserName;
    private String url;
    private Integer type;
    private List<Long> userIds;


    public String getContent(){
        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("您有新的").append(title).append("任务").append("\n\n");
        stringBuffer.append(">**").append("任务详情").append("**").append("\n\n");
        if (ToolUtil.isNotEmpty(items)){
            stringBuffer.append(">**事　项**：").append("<font color=\"info\">").append(items).append("</font>").append("\n\n");
        }
        if (ToolUtil.isNotEmpty(createUserName)){
            stringBuffer.append(">**发起人**：").append(createUserName).append("\n\n");
            stringBuffer.append(">").append("\n\n");
        }
        if (ToolUtil.isNotEmpty(description)){
            stringBuffer.append(">**详　情**：").append("<font color=\"warning\">").append(description).append("</font>").append("\n\n");
        }
        if (ToolUtil.isNotEmpty(remark)){
            stringBuffer.append(">**备　注**：").append("<font color=\"comment\">").append(remark).append("</font>").append("\n\n");
        }
        if (ToolUtil.isNotEmpty(url)){
            stringBuffer.append(">").append("\n\n");
            stringBuffer.append(">[点击查看详情](").append(url).append(")");
        }
        return stringBuffer.toString();
    }
}
