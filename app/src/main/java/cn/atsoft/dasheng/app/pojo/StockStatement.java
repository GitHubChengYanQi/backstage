package cn.atsoft.dasheng.app.pojo;

import lombok.Data;

/**
 * 库存报表
 */
@Data
public class StockStatement {
    private String name;
    private String month;
    private Long value;
}
