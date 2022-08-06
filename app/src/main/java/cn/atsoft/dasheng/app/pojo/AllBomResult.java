package cn.atsoft.dasheng.app.pojo;

import cn.atsoft.dasheng.erp.model.result.SkuResult;
import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Data
public class AllBomResult {

    private List<BomOrder> result;

    private List<AnalysisResult> owe;

    private List<View> view;

    @Data
    public static class View {

        private Long skuId;

        private Long num;

        private Boolean fixed;

        private String name;
    }
}
