package cn.atsoft.dasheng.appBase.model.result;

import lombok.Data;

@Data
public class MediaUrlResult {
    private Long  mediaId;
    private String filedName;
    private String url;
    private String type;
    private String thumbUrl;
}
