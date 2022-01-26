package cn.atsoft.dasheng.Excel.pojo;

import cn.atsoft.dasheng.erp.entity.Inkind;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class InstockExcel {
    private String standard;  //成品码

    private Long number;      //数量

    private String house;      //仓库

    private String position;       //库位

    private String brand;     //品牌

    private String supplier;        //供应商


    @JSONField
    private Long skuId;
    @JSONField
    private Long brandId;
    @JSONField
    private Long supplierId;
    @JSONField
    private Long positionId;
    @JSONField
    private Long houseId;
    @JSONField
    private Inkind inkind;
    @JSONField
    private Long qrCodeId;

}
