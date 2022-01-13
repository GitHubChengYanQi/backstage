package cn.atsoft.dasheng.Tool.pojo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UpdateSort {
    @NotNull
    private List<SortParam> sortParams;
    @NotNull
    private String type;

    @Data
    public class SortParam {
        private Long id;
        private Long sort;
    }
}
