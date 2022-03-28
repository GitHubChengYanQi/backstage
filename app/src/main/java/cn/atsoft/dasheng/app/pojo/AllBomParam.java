package cn.atsoft.dasheng.app.pojo;

import lombok.Data;

import java.util.List;

@Data
public class AllBomParam {

    public List<SkuNumberParam> skuIds;

    @Data
    public static class SkuNumberParam {
        private Long skuId;
        private Long num;
        private boolean fixed;
    }
}
