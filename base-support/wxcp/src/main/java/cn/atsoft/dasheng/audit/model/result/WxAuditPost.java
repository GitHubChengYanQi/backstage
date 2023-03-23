package cn.atsoft.dasheng.audit.model.result;

import lombok.Data;

@Data
public class WxAuditPost {
    private Integer errcode;
    private String errmsg;
    private String sp_no;
}
