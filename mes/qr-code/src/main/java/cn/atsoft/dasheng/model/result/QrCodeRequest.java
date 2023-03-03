package cn.atsoft.dasheng.model.result;

import lombok.Data;

import java.util.List;

@Data
public class QrCodeRequest {
    private List<Long> ids;
    private String type;
}
