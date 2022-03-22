package cn.atsoft.dasheng.app.pojo;

import lombok.Data;

import java.util.List;

@Data
public class AllBomParam {

    private List<skuNumberParam> params;

    @Data
    public class skuNumberParam {
        private Long skuId;
        private Long number;
    }
}
