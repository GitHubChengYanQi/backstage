package cn.atsoft.dasheng.pojo;

import lombok.Data;

@Data
public class InkindQrcode {
    public InkindQrcode(Long inkindId, Long qrCodeId) {
        this.inkindId = inkindId;
        this.qrCodeId = qrCodeId;
    }

    private Long inkindId;
    private Long qrCodeId;
}
