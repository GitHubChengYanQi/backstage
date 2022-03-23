package cn.atsoft.dasheng.app.pojo;

import lombok.Data;

import java.util.List;

@Data
public class AllBomParam {

    private List<skuNumberParam> skuIds;

    @Data
    public class skuNumberParam {
        private Long skuId;
        private Long num;
        private boolean fixed;
    }
}
