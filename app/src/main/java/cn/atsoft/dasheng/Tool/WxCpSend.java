package cn.atsoft.dasheng.Tool;

import java.util.List;

public interface WxCpSend {
    //根据任务id 发送企业微信卡片
    void sendById(Long id);

    void sendByTask(Long taskId);

    //根据用户id 推送
    void sendByUser(Long userId);
    //根据多个用户id 推送
    void sendByUserIds(List<Long> userIds);
}
