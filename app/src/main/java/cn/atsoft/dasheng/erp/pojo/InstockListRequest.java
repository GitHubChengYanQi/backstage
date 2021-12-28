package cn.atsoft.dasheng.erp.pojo;

import cn.atsoft.dasheng.erp.entity.Inkind;
import lombok.Data;

@Data
public class InstockListRequest {
    private Long codeId;
    private Inkind inkind;
}
