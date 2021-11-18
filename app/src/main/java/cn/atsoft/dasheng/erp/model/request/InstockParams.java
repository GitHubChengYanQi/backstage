package cn.atsoft.dasheng.erp.model.request;

import lombok.Data;

import java.util.List;

@Data
public class InstockParams {
    private List<Long> inkinds; //实物id
    private Long coding;
    private Long storeHouseId;
    private Long userId;
    private String url;
    private Long number;

}
