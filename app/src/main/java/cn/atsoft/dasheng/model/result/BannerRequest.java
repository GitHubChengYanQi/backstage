package cn.atsoft.dasheng.model.result;

import lombok.Data;

import java.util.List;

@Data
public class BannerRequest {
    private List<Long> bannerId;
}
