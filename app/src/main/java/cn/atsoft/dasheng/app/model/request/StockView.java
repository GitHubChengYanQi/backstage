package cn.atsoft.dasheng.app.model.request;

import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessTaskResult;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import lombok.Data;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class StockView {
    private Long pickListsId;
    private Long userId;
    private UserResult userResult;
    private Long skuId;
    private Long brandId;
    private BrandResult brandResult;
    private CustomerResult customerResult;
    private Long customerId;
    private Long spuClassId;
    private SkuSimpleResult skuResult;
    private List<SkuAndNumber> skuAndNumbers;
    private List<UserAndNumber> userAndNumbers;
    private Date createDate;
    private Date createTime;
    private Long status;
    private Map<String,Integer> numberByMonth;
    private Map<String,Integer> errorNumberByMonth;
    private String type;
    private Integer pickSkuCount;
    private Integer instockNumber;
    private Integer noInstockNumber;
    private Integer number;
    private Integer pickNumCount;
    private Integer outSkuCount;
    private Integer outNumCount;
    private Integer inNumCount;
    private Integer inSkuCount;
    private Integer errorSkuCount;
    private Integer errorNumCount;
    private Integer orderCount;
    private Long createUser;
    private String spuClassName;
    private String customerName;
    private String storehouseName;
    private String monthOfYear;
    private List<ActivitiProcessTaskResult> taskResults;

    @Data
    public static class SkuAndNumber{
        private Long skuId;
        private Long classId;
        private Long userId;
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
