package cn.atsoft.dasheng.Excel.pojo;

import cn.atsoft.dasheng.erp.entity.Inkind;
import lombok.Data;

@Data
public class ExcelInkind {
    private Long qrCodeId;
    private Inkind inkind;

    public ExcelInkind(Long qrCodeId, Inkind inkind) {
        this.qrCodeId = qrCodeId;
        this.inkind = inkind;
    }
}
