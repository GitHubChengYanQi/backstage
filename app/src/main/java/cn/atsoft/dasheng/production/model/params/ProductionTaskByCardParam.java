package cn.atsoft.dasheng.production.model.params;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ProductionTaskByCardParam {
    @NotNull
    private List<DetailParam> details;

    private List<Long> taskIds;

    @NotNull
    private Long cardId;

    @Data
    public static class DetailParam{
        @NotNull
        private Long skuId;
        @NotNull
        private Long parentId;
        @NotNull
        private Integer number;
        @NotNull
        private Long bomId;
    }
}
