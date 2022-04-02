package cn.atsoft.dasheng.app.pojo;

import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class AllBomParam {
    @NonNull
    public List<SkuNumberParam> skuIds;

    @Data
    public static class SkuNumberParam {
        @NonNull
        private Long skuId;
        @NonNull
        private Long num;
        @NonNull
        private Boolean fixed;
    }
}
