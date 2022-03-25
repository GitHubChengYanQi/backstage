package cn.atsoft.dasheng.production.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobBookingDetailCount {
    private Long skuId;
    private String source;
    private Long sourceId;
    private Integer number;
}
