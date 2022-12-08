package cn.atsoft.dasheng.app.model.request;

import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OutStockView {
    private Long pickListsId;
    private Long userId;
    private UserResult userResult;
    private Long skuId;
//    private Integer detailCount;
//    private Integer doneNumber;
    private SkuSimpleResult skuResult;
    private List<SkuAndNumber> skuAndNumbers;
    private List<UserAndNumber> userAndNumbers;
    private Date createDate;
    private Integer pickSkuCount;
    private Integer pickNumCount;
    private Integer outSkuCount;
    private Integer outNumCount;
    private Integer orderCount;

    @Data
    public static class SkuAndNumber{
        private Long skuId;
        private SkuSimpleResult skuResult;
        private Integer number;
    }
    @Data
    public static class UserAndNumber{
        private Long userId;
        private UserResult userResult;
        private Integer number;
    }
}
