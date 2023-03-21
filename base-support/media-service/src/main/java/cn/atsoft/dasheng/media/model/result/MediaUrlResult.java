package cn.atsoft.dasheng.media.model.result;

import lombok.Data;

@Data
public class MediaUrlResult {
    private Long  mediaId;
    private String filedName;
    private String url;
    private String type;
    private String thumbUrl;
}
