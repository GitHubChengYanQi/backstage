package cn.atsoft.dasheng.view.model.result;

import lombok.Data;

@Data
public class StockNumberResult {
    private Long type;
    private Long number;

    public StockNumberResult(Long type, Long number) {
        this.type = type;
        this.number = number;
    }
}
