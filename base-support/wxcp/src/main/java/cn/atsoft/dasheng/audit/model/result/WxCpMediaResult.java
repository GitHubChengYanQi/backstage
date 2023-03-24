package cn.atsoft.dasheng.audit.model.result;

import lombok.Data;

import java.io.File;
@Data
public class WxCpMediaResult {
    private String mediaId;

    private File file;
}
